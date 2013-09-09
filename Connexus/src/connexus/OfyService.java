package connexus;

//import static connexus.OfyService.ofy;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

import connexus.model.*;

public class OfyService {
    static {
    	// Perform entity registrations here
    	factory().register(Site.class);
    	factory().register(Leaderboard.class);
    	factory().register(StreamView.class);
        factory().register(CUser.class);
        factory().register(Media.class);
        factory().register(Stream.class);
        factory().register(Subscription.class);
        
        // Ensure that our logical site entity exists.
        // Ancestor entity for searching all root-less objects.

        Site site = Site.load(Config.siteId);
        if (site == null) {
        	site = new Site(Config.siteId, "Primary site");
        	ofy().save().entity(site).now();
        }
        
        // Create the leaderboard if needed
        Leaderboard lb = Leaderboard.load(null, site.getKey());
        if (lb == null) {
        	lb = new Leaderboard(Leaderboard.lbId, site.getKey());
        	ofy().save().entity(lb).now();
        }
    }

    public static Objectify ofy() {
        return ObjectifyService.ofy();
    }

    public static ObjectifyFactory factory() {
        return ObjectifyService.factory();
    }
}