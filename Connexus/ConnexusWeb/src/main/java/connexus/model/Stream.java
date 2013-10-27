package connexus.model;

import static connexus.OfyService.ofy;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.google.common.base.CharMatcher;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;
import com.googlecode.objectify.condition.IfNotNull;

import connexus.Config;

@Entity
@Cache
public class Stream implements Comparable<Stream> {
	@Id Long id;
	@Parent Key<CUser> owner;
	@Index({IfNotNull.class}) String name;

	String coverURL;
	@Index({IfNotNull.class}) List<String> tags;
	@Index Date creationDate;
	@Index Long numberOfMedia;
    @Index Date lastUpdated;

	@Index Long views;   // all time views
	long trendingViews;  // views only within the trending window

    Long displayLimit;
    Long displayOffset;

	@SuppressWarnings("unused")
	private Stream() {
	}

	public Stream(Long id, Key<CUser> owner, String name) {
		this.id = id;
		this.owner = owner;
		this.name = name;
		this.creationDate = new Date();
        this.lastUpdated = creationDate;
        this.displayLimit = null;
        this.displayOffset = null;
	}

	public Key<Stream> getKey() {
		return Key.create(owner, Stream.class, id);
	}

	public Ref<Stream> getRef() {
		return Ref.create(getKey(), this);
	}

	public static Stream getById(Long objectId, CUser cuser) {
		return ofy().load().type(Stream.class).parent(cuser).id(objectId).get();
	}

	public String getAbsoluteViewURI() {
		return Config.productURL + getViewURI();
	}

	public String getViewURI() {
		// Use URIBuilder?
		List<String[]> params = new ArrayList<String[]>();

        // When gathering Streams for display (e.g. search results)
        // You can setDisplayLimit() and setDisplayOffset()
        // to control where view link goes so it jumps to the appropriate image.
        Long displayLimit = getDisplayLimit();
        if (displayLimit != null) {
            params.add(new String[] {"limit", displayLimit.toString()});
        }
        Long displayOffset = getDisplayOffset();
        if (displayOffset != null) {
            params.add(new String[] {"offset", displayOffset.toString()});
        }
		params.add(new String[] {"v", getObjectURI()});
		return Config.getURIWithParams("/view", params);
	}

	public String getObjectURI() {
		return owner.getId() + ":" + id;
	}

	// Doesn't really belong here...
	public static String getSearchURI(String term) {
		try {
			return "/search?q=" + java.net.URLEncoder.encode(term, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// Good luck!
			return "/search?q=" + term;
		}
	}

	public String toString() {
		return "Stream " + id + " : " + name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Key<CUser> getOwner() {
		return owner;
	}

	public void setOwner(Key<CUser> owner) {
		this.owner = owner;
	}

	public String getCoverURL() {
		return coverURL;
	}

	public void setCoverURL(String coverURL) {
		this.coverURL = coverURL;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		CharMatcher wspace = CharMatcher.is(' ');

		// Ensure trimmed tags
		List<String> newTags = new ArrayList<String>();
		for (String tag : tags) {
			String newTag = wspace.trimFrom(tag);
			if (newTag.length() > 0) {
				newTags.add(newTag);
			}
		}
		this.tags = newTags;
	}


	public String getOwnerName() {
		return ofy().load().key(owner).get().getRealName();
	}

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void touchUpdated() {
        setLastUpdated(new Date());
    }

    public Date getLastNewMediaDate() {
        return getLastUpdated();
//		List<Media> ml = getMedia(0, 1);
//		if (ml == null || ml.isEmpty()) {
//			// kind of bogus but I hate nulls
//			return getCreationDate();
//		}
//		Media lastMedia = ml.get(0);
//		return lastMedia.getCreationDate();
	}


	public String getLastNewMedia() {
		Date lnmDate = getLastNewMediaDate();
		if (lnmDate == null) {
			return "";
		}
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		return df.format(lnmDate);
	}

	public void incNumberOfMedia() {
		Long num = getNumberOfMedia();
		num++;
		setNumberOfMedia(num);
        touchUpdated();
	}

	public void decNumberOfMedia() {
		Long num = getNumberOfMedia();
		num--;
		setNumberOfMedia(num);
        touchUpdated();
	}

	public void setNumberOfMedia(Long num) {
		numberOfMedia = num;
	}

	public Long getNumberOfMedia() {
		if (numberOfMedia != null) {
			return numberOfMedia;
		}
		fixNumMedia();
		return numberOfMedia;
	}

	public void fixNumMedia() {
		int rvi = ofy().load().type(Media.class).ancestor(this).count();
		Long rv = new Long(rvi);
        setNumberOfMedia(rv);

        // Fix the last modified time if necessary
        Date lastUpdate = getLastUpdated();
        if (lastUpdate == null) {
            Media lastMedia = ofy().load().type(Media.class).ancestor(this).order("-creationDate").limit(1).first().get();
            if (lastMedia == null) {
                lastUpdate = new Date();
            } else {
                lastUpdate = lastMedia.getCreationDate();
                if (lastUpdate == null) {
                    lastUpdate = new Date();
                }
            }
            setLastUpdated(lastUpdate);
        }

	}

	public boolean deleteStream() {
		// TODO: logging and TRANSACTION...
		System.err.println("Deleting stream and all its media: " + this);

		// TODO Delete all Media in this stream
		for (Media media : getMedia(0, 0)) {
			media.deleteMedia();
		}

		// Nuke it
		ofy().delete().entities(this).now();
		return true;
	}

	/**
	 * Returns ALL streams available in the system, sorted by most recently updated first.
	 * TODO: memcache?
	 */
	public static List<Stream> getAllStreams(Key<Site> site) {
        return getAllStreams(site, 0, 0);
	}

    public static List<Stream> getAllStreams(Key<Site> site, int limit, int offset) {
        if (site == null) {
            site = Site.load(null).getKey();
        }
        // order by getLastNewMedia()
        List<Stream> rv = ofy().load().type(Stream.class).ancestor(site).order("-lastUpdated")
                .limit(limit).offset(offset).list();
//        Collections.sort(rv);
//        Collections.reverse(rv);
        return rv;
    }

    public static List<Stream> getAllStreamsNoOrder(Key<Site> site) {
        if (site == null) {
            site = Site.load(null).getKey();
        }
        List<Stream> rv = ofy().load().type(Stream.class).ancestor(site).list();
        return rv;
    }


    /*
	 * Get a list of media for this stream sorted by creation date
	 */
	public List<Media> getMedia(int offset, int limit) {
		return ofy().load().type(Media.class).ancestor(this).order("-creationDate")
				.offset(offset).limit(limit).list();
	}


	public Long getViews() {
		return views;
	}

	public void setViews(Long views) {
		this.views = views;
	}

	public Long getAndIncrementViews() {
		Long views = getViews();
		if (views == null) {
			views = new Long(0);
		}
		views++;
		setViews(views);
		save(false); // async

		StreamView streamView = new StreamView(null, getKey());
		ofy().save().entities(streamView);  // async
		return views;
	}

	public void save() {
		// synchronous by default
        save(true);
	}

    public void save(boolean now) {
        if (now) {
            ofy().save().entities(this).now();
        } else {
            ofy().save().entities(this);
        }
    }

	public long getTrendingViews() {
		return trendingViews;
	}

	public void setTrendingViews(long trendingViews) {
		this.trendingViews = trendingViews;
	}

	/**
	 * Discover and set the views of this stream within the last hour (does a save)
	 *
	 * Fix to use the constants
	 */
	public void generateTrendingViews() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());

		// dis cast ok?
		cal.add(Calendar.SECOND, Config.safeLongToInt(-Leaderboard.lbTimeWindowSec));
		Date timeWindow = cal.getTime();
		long views = ofy().load().type(StreamView.class).filter("stream ==", getKey()).filter("date >=", timeWindow).count();
		setTrendingViews(views);
		save();
	}

	@Override
	public int compareTo(Stream other) {
		Date myDate = getLastNewMediaDate();
		Date otherDate = other.getLastNewMediaDate();
		if (myDate.after(otherDate)) {
			return 1;
		} else if (myDate.before(otherDate)) {
			return -1;
		}
		return 0;
	}

	public static class TrendingComparator implements Comparator<Stream> {
		public int compare(Stream p1, Stream p2) {
			// REVERSE order (highest first)
			return (int) (p2.getTrendingViews() - p1.getTrendingViews());
		}
	}

    public Long getDisplayLimit() {
        return displayLimit;
    }

    public void setDisplayLimit(Long displayLimit) {
        this.displayLimit = displayLimit;
    }

    public Long getDisplayOffset() {
        return displayOffset;
    }

    public void setDisplayOffset(Long displayOffset) {
        this.displayOffset = displayOffset;
    }
}