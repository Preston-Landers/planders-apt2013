package connexus.model;

import static connexus.OfyService.ofy;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

@Entity
/**
 * Represents the top streams by views within a certain time window.
 */
public class Leaderboard implements Comparable<Leaderboard> {
	@Id Long id;
	@Parent Key<Site> site;
	Date lastUpdated;
	List<Stream> leaderBoard;
	@Index Long reportFrequencySec;
	
	// Report frequency options
	public static final long FREQ_NONE = 0;
	public static final long FREQ_5MIN = 60 * 5;
	public static final long FREQ_HOUR = 60 * 60;
	public static final long FREQ_DAY  = 60 * 60 * 24;
	
	
	public static final long lbId = 1;

	// Number of items to track in the LB
	public static final long lbSize = 3;

	// Frequency of LB updates
	public static final long lbUpdateSec = 60 * 5;

	// Size of time window for LB in seconds
	public static final long lbTimeWindowSec = 3600;
	
	// Amount of time to keep StreamView objects before deleting them
	public static final long lbKeepViewsTimeSec = 3600 * 48;
	
	@SuppressWarnings("unused")
	private Leaderboard() {
	}

	public Leaderboard(Long id, Key<Site> site) {
		this.id = id;
		if (id == null) {
			this.id = lbId;
		}
		this.site = site;
		this.lastUpdated = new Date();
		this.leaderBoard = new ArrayList<Stream>();
		this.reportFrequencySec = FREQ_NONE;
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

//	public void setId(Long id) {
//		this.id = id;
//	}


	public Date getLastUpdated() {
		return lastUpdated;
	}

	public static Leaderboard load(Long id, Key<Site> site) {
		if (id == null) {
			id = lbId;
		}
		return ofy().load().type(Leaderboard.class).parent(site).id(id).get();
	}

	public List<Stream> getLeaderBoard() {
		return leaderBoard;
	}

	public void setLeaderBoard(List<Stream> leaderBoard) {
		this.leaderBoard = leaderBoard;
	}

	/**
	 * This is what the cron job will run to generate leaderboard stats
	 */
	public static void generateLeaderBoard() {
		Site site = Site.load(null);
		Leaderboard LB = load(null, site.getKey());
		
		List<Stream> allStreams = Stream.getAllStreams(site.getKey());
		for (Stream stream : allStreams ) {
			
		}
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