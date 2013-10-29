package connexus.model;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.*;

import java.util.*;

import static connexus.OfyService.ofy;

/**
 * Maintains the text index for search auto-completion.
 */
@Entity
@Cache
public class AutocompleteIndex {
    public static final long acId = 1; // ID for this singleton-like object

    @Id
    Long id;

    @Parent
    Key<Site> site;

    Date lastUpdated;

    List<String> autocompleteList;

    @SuppressWarnings("unused")
    private AutocompleteIndex() {
    }

    @SuppressWarnings("deprecation")
    public AutocompleteIndex(Long id, Key<Site> site) {
        this.id = id;
        if (id == null) {
            this.id = acId;
        }
        if (site == null) {
            site = Site.load(null).getKey();
        }
        this.site = site;
        this.lastUpdated = new Date();
        this.autocompleteList = new ArrayList<String>();
    }

    public List<String> doSearch(String term, final int maxResults) {
        SortedSet<String> resultSet = new TreeSet<String>();
        int results = 0;
        term = term.toLowerCase();
        for (String indexTerm : getAutocompleteList()) {
            if (indexTerm.toLowerCase().contains(term)) {
                resultSet.add(indexTerm);
                results++;
            }
            if (results >= maxResults) {
                break;
            }
        }
        return new ArrayList<String>(resultSet);
    }

    /**
     * This is what the cron job runs to generate the index for search auto-complete
     */
    public static void generateAutocompleteIndex() {
        AutocompleteIndex autocompleteIndex = load(null, null);
        SortedSet<String> acStrings = new TreeSet<String>();

        for (Stream stream : Stream.getAllStreams(null)) {
            acStrings.add(stream.getName());
            acStrings.add(stream.getOwnerName());
            List<String> tagList = stream.getTags();
            if (tagList != null) {
                for (String tag : tagList) {
                    acStrings.add(tag);
                }
            }
            List<Media> mediaList = stream.getMedia(0, 0);
            if (mediaList != null) {
                for (Media media : mediaList) {
                    acStrings.add(media.getComments());
                }
            }

        }

        autocompleteIndex.setAutocompleteList(new ArrayList<String>(acStrings));
        autocompleteIndex.save();
    }

    public static AutocompleteIndex load(Long id, Key<Site> site) {
        if (id == null) {
            id = acId;
        }
        if (site == null) {
            site = Site.load(null).getKey();
        }
        AutocompleteIndex autocompleteIndex = ofy().load().type(AutocompleteIndex.class).parent(site).id(id).get();
        if (autocompleteIndex == null) {
            autocompleteIndex = new AutocompleteIndex(null, null);
            autocompleteIndex.save();
        }

        return autocompleteIndex;
    }

    public void save() {
        updateNow();
        ofy().save().entities(this); // .now();
    }

    public void updateNow() {
        setLastUpdated(new Date());
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Key<Site> getSite() {
        return site;
    }

    public void setSite(Key<Site> site) {
        this.site = site;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public List<String> getAutocompleteList() {
        return autocompleteList;
    }

    public void setAutocompleteList(List<String> autocompleteList) {
        this.autocompleteList = autocompleteList;
    }
}
