package connexus.model;

import static connexus.OfyService.ofy;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.internet.InternetAddress;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.Parent;

import connexus.Config;
import connexus.EmailHelper;
import connexus.model.Stream;

@Entity
@Cache
/**
 * Represents the top streams by views within a certain time window.
 */
public class Leaderboard implements Comparable<Leaderboard> {
	@Id
	Long id;
	@Parent
	Key<Site> site;
	Date lastUpdated;
	@Load List<Ref<Stream>> leaderBoard;
	@Index
	Long reportFrequencySec;
	@Index
	Date reportLastRun;
	
	Date lastClean;

	// Report frequency options
	public static final long FREQ_NONE = 0;
	public static final long FREQ_5MIN = 60 * 5;
	public static final long FREQ_HOUR = 60 * 60;
	public static final long FREQ_DAY = 60 * 60 * 24;

	public static final long lbId = 1;

	// Number of items to track in the LB
	public static final long lbSize = 3;

	// Frequency of LB updates
	public static final long lbUpdateSec = 60 * 5;

	// Size of time window for LB in seconds
	public static final long lbTimeWindowSec = 3600;

	// Amount of time to keep StreamView objects before deleting them
	public static final long lbKeepViewsTimeSec = 3600 * 48;
	
	// How often to clean up StreamView objects
	public static final long lbCleanTimeSec = 3600 * 24;

	@SuppressWarnings("unused")
	private Leaderboard() {
	}

	@SuppressWarnings("deprecation")
	public Leaderboard(Long id, Key<Site> site) {
		this.id = id;
		if (id == null) {
			this.id = lbId;
		}
		this.site = site;
		this.lastUpdated = new Date();
		this.leaderBoard = new ArrayList<Ref<Stream>>();
		this.reportFrequencySec = FREQ_NONE;
		this.reportLastRun = new Date(90,1,1);
		this.lastClean = new Date(90,1,1);
	}

	public Key<Leaderboard> getKey() {
		return Key.create(Leaderboard.class, id);
	}

	public String toString() {
		// TODO Add actual LB stats here
		return "Leaderboard " + id + " last updated: " + lastUpdated;
	}

	public Long getId() {
		return id;
	}

	// public void setId(Long id) {
	// this.id = id;
	// }

	public void updateNow() {
		setLastUpdated(new Date());
	}

	public void setLastUpdated(Date date) {
		lastUpdated = date;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public static Leaderboard load(Long id, Key<Site> site) {
		if (id == null) {
			id = lbId;
		}
		if (site == null) {
			site = Site.load(null).getKey();
		}
		return ofy().load().type(Leaderboard.class).parent(site).id(id).get();
	}

	public List<Ref<Stream>> getLeaderBoard() {
		return leaderBoard;
	}

	public List<Stream> getLeaderBoardList() {
		// This is surely not the best way to do this...
		List<Stream> rv = new ArrayList<Stream>();
		List<Ref<Stream>> lbList = getLeaderBoard();
		if (lbList != null && lbList.size() > 0) {
			for (Ref<Stream> streamRef : getLeaderBoard()) {
				rv.add(streamRef.get());
			}
		}
		return rv;		
	}

	public void setLeaderBoard(List<Ref<Stream>> leaderBoard) {
		this.leaderBoard = leaderBoard;
	}

	public Long getReportFrequencySec() {
		return reportFrequencySec;
	}

	public void setReportFrequencySec(Long reportFrequencySec) {
		this.reportFrequencySec = reportFrequencySec;
	}

	public Date getReportLastRun() {
		return reportLastRun;
	}

	public void setReportLastRun(Date reportLastRun) {
		this.reportLastRun = reportLastRun;
	}

	public Key<Site> getSite() {
		return site;
	}

	public void save() {
		updateNow();
		ofy().save().entities(this).now();
	}

	/**
	 * This is what the cron job runs to generate leaderboard stats
	 */
	public static void generateLeaderBoard() {
		Site site = Site.load(null);
		Leaderboard LB = load(null, site.getKey());
		if (LB == null) {
			System.err.println("Error: leaderboard is missing!");
			return;
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		
		cal.add(Calendar.SECOND, Config.safeLongToInt(-Leaderboard.lbTimeWindowSec));
		Date timeWindow = cal.getTime();

		Map<Key<Stream>, Long> streamToTrendViews = new HashMap<Key<Stream>, Long>();
		for (StreamView view: ofy().load().type(StreamView.class).filter("date >=", timeWindow)) {
			Key<Stream> streamKey = view.stream;
			Long count = streamToTrendViews.get(streamKey);
			if (count == null) {
				count = new Long(0);
			}
			count++;
			streamToTrendViews.put(streamKey, count);
		}
		
		List<Stream> allStreams = new ArrayList<Stream>();
		
		for (Key<Stream> streamKey : streamToTrendViews.keySet()) {			
			Stream stream = ofy().load().key(streamKey).get();
			stream.setTrendingViews(streamToTrendViews.get(streamKey));
			stream.save();
			allStreams.add(stream);
		}

		Collections.sort(allStreams, new Stream.TrendingComparator());

		List<Ref<Stream>> rv = new ArrayList<Ref<Stream>>();
		int i = 0;
		for (Stream stream : allStreams) {
			i++;
//			System.err.println("SETTING LEADERBOARD " + i + " "
//					+ stream.getName() + " -- " + stream.getTrendingViews());
			rv.add(stream.getRef());
			if (i >= lbSize) {
				break;
			}
		}

		LB.setLeaderBoard(rv);
		LB.save();
	}

	@SuppressWarnings("deprecation")
	public void maybeRunStreamViewCleanup() {
		Date lastClean = getLastClean();
		if (lastClean == null) {
			lastClean = new Date(90, 1, 1);
		}
		Long cleanFreqSec = lbCleanTimeSec;
		if (cleanFreqSec == null || cleanFreqSec.equals(0)) {
			System.err.println("Cleanup disabled. Last run: " + lastClean);
			return;
		}
		Date now = new Date();
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(lastClean);
		
		cal.add(Calendar.SECOND, Config.safeLongToInt(cleanFreqSec));
		Date nextCleanTime = cal.getTime();
		
		if ( now.compareTo(nextCleanTime) < 0 ) {
			// System.err.println("Not time for cleaning yet. Next report time is: " + nextCleanTime);
			return;
		}
		// 
		fixAllStreamMediaCounts();
		StreamView.cleanupStreamViews();
		setLastClean(new Date());
		save();
	}
	
	public Date getLastClean() {
		return lastClean;
	}

	public void setLastClean(Date lastClean) {
		this.lastClean = lastClean;
	}
	
	public void fixAllStreamMediaCounts() {
		for (Stream stream : Stream.getAllStreams(site)) {
			stream.fixNumMedia();
			stream.save();
		}
		
	}

	@SuppressWarnings("deprecation")
	public void maybeRunTrendingReport() {
		Date lastRun = getReportLastRun();
		if (lastRun == null) {
			lastRun = new Date(90, 1, 1);
		}
		Long reportFreqSec = getReportFrequencySec();
		if (reportFreqSec == null || reportFreqSec.equals(FREQ_NONE)) {
			// System.err.println("Email Report is disabled. Last run: " + lastRun);
			return;
		}
		Date now = new Date();
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(lastRun);
		
		cal.add(Calendar.SECOND, Config.safeLongToInt(reportFreqSec));
		Date nextReportTime = cal.getTime();
		
		if ( now.compareTo(nextReportTime) < 0 ) {
			// System.err.println("Not time for report yet. Next report time is: " + nextReportTime);
			return;
		}
		// 
//		System.err.println(now
//				+ " It's time to run the report. Report Interval: "
//				+ reportFreqSec + " sec. Last run " + lastRun);
		runTrendingReport();
	}
	
	public void runTrendingReport() {
		List<Stream> lbStreams = getLeaderBoardList();
		StringBuilder sb = new StringBuilder();
		sb.append("Trending Streams report for connexus-apt.appspot.com.\n\n\n");
		int i = 0;
		for (Stream stream : lbStreams ) {
			i++;
			sb.append("#" + i + " : " + stream.getName() + " has " + stream.getTrendingViews() + " views in last hour\n\n");	
		}
		sb.append("\n\n (end of report)\n\n");

		// Indicate that the report ran ... even if the email doesn't actually fire off...
		setReportLastRun(new Date());
		save();
		
		
		String subject = Config.emailSubject;
		List<InternetAddress> toAddr = EmailHelper.getReportToAddresses();
		EmailHelper.sendEmail(toAddr, null, subject, sb.toString());
	}
	
	@Override
	public int compareTo(Leaderboard other) {
		if (lastUpdated.after(other.getLastUpdated())) {
			return 1;
		} else if (lastUpdated.before(other.getLastUpdated())) {
			return -1;
		}
		return 0;
	}
}