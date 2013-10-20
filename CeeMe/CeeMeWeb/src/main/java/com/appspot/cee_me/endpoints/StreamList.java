package connexus.endpoints;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.Expiration;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.googlecode.objectify.Ref;
import connexus.Config;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.appengine.api.users.User;
import connexus.model.CUser;
import connexus.model.LocMedia;
import connexus.model.Site;
import connexus.model.Subscription;
import connexus.servlet.ConnexusServletBase;

import javax.annotation.Nullable;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import static connexus.OfyService.ofy;

/**
 * Defines v1 of a streamlist API, which provides lists of Connexus streams.
 */
@Api(name = "streamlist", version = "v1",
        description = "Retrieve Connex.us photo streams.",
        scopes = {Config.EMAIL_SCOPE},
        clientIds = {
                com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID,
                Config.ANDROID_CLIENT_ID,
                Config.WEB_CLIENT_ID
        },
        audiences = {Config.ANDROID_AUDIENCE})
public class StreamList {
    protected final static MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();

    static {
        syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
    }

    /**
     * Retrieve a list of photo streams according to criteria you specify. They are returned in
     * the most recently modified order unless otherwise specified. Note that the search
     * term query and "my subscription" features are mutually exclusive. Each Stream is
     * returned with a mediaList object giving the first 16 media objects of the stream to save
     * you the separate call to getMedia.
     *
     * @param user   Google Account of the requesting user
     * @param limit  Number of results to return. Max of 32.
     * @param offset Index into total result set (used for pagination)
     * @param query  (Optional) - search term to query
     * @param mySubs (Optional) - if true, limit results to my subscriptions.
     * @return a list of Stream objects matching the criteria
     */
    @ApiMethod(name = "getStreams", httpMethod = "get")
    public List<Stream> getStreams(User user,
                                   @Named("queryLimit") Integer limit,
                                   @Named("queryOffset") Integer offset,
                                   @Named("query") @Nullable String query,
                                   @Named("mySubs") @Nullable Boolean mySubs) {
        System.err.println((user == null ? "" : user) + " "
                + (user == null ? "": user.getUserId())
                + " -> getStreams limit " + limit + " offset: " + offset + " query: "
                + (query == null ? "": query )+ " mySubs: "
                + (mySubs == null ? "": mySubs ));
        if (mySubs == null) {
            mySubs = new Boolean(false);
        }

        List<Stream> streams = new ArrayList<Stream>();

        Ref<Site> site = ofy().load().type(Site.class).id(Config.siteId);
        if (site == null) {
            System.err.println("-> getStreams can't load site");
            throw new IllegalArgumentException("Can't load site object.");
        }
        // CUser.normalizeAllAccountNames(site.getKey()); // XXX TODO
        CUser cUser = null;
        if (user != null) {
            cUser = ConnexusServletBase.getOrCreateUserRecord(user, site.getKey());
        }

        if (limit == null) {
            limit = 0;
        }
        if (limit.equals(0)) {
            limit = 16;
        }
        if (limit > 32) {
            limit = 32;
        } // XXX TODO: move to config?
        if (offset == null) {
            offset = 0;
        }

        List<connexus.model.Stream> modelStreams;
        if (mySubs) {
            // System.err.println("-> getStreams using mysubs");
            // TODO: move some of this into subscription class
            modelStreams = new ArrayList<connexus.model.Stream>();
            int i = 0;
            if (cUser != null) {
                for (Subscription sub : Subscription.getSubscriptionsForUser(cUser.getKey())) {
                    // System.err.println("-> getStreams using mysubs cuser: " + cUser.getId());
                    connexus.model.Stream stream = ofy().load().key(sub.getStream()).get();
                    // System.err.println("-> getStreams sub " + Integer.toString(i));
                    if (stream != null) {
                        if (i >= offset && i <= limit) {
                            modelStreams.add(stream);
                        }
                    }
                    i++;
                }
                // System.err.println("-> getStreams using mysubs " + modelStreams.size() + " cUser " + cUser.getId() + " gUser: " + user);

            } else {
                System.err.println("-> getStreams error: asked for subs but not logged in!");
            }

        } else if (query != null) {
            modelStreams = connexus.servlet.Search.performStreamSearch(query, site.getKey(), limit, offset);
            // System.err.println("-> getStreams using search " + modelStreams.size() + " results for query " + query);
        } else {
            modelStreams = connexus.model.Stream.getAllStreams(site.getKey(), limit, offset);
            // System.err.println("-> getStreams general " + modelStreams.size());
        }


        int streamCount = 0;
        for (connexus.model.Stream stream : modelStreams) {
            if (stream == null) {
                continue;
            }
            Stream endpointStream = convertStreamToAPI(stream);
            streams.add(endpointStream);

            endpointStream.setQueryIndex(streamCount++);
            endpointStream.setQueryOffset(offset);
            endpointStream.setQueryLimit(limit);

            // Do an initial image search on each stream to save the subsequent requests
            // actually, don't do this because we're not actually using it.
            int mediaOffset = 0;
            // int mediaLimit = 16;
            int mediaLimit = 0;

            // List<Media> mediaList = getMediaHelper(stream, mediaLimit, mediaOffset);
            List<Media> mediaList = null;
            endpointStream.setMediaOffset(mediaOffset);
            endpointStream.setMediaLimit(mediaLimit);
            endpointStream.setMediaList(mediaList);

        }

        // System.err.println("-> getStreams return " + streams.size() );
        return streams;
    }

    private static class MediaCacheKey implements Serializable {
        Long streamId;
        Long streamOwnerId;
        Integer limit;
        Integer offset;

        MediaCacheKey(Long streamId, Long streamOwnerId, Integer limit, Integer offset) {
            this.streamId = streamId;
            this.streamOwnerId = streamOwnerId;
            this.limit = limit;
            this.offset = offset;
        }
    }

    /**
     * Retrieve a list of Media (images) from a single photo Stream. You must provide both
     * the streamId AND the ownerId.
     *
     * @param user          Google User
     * @param streamId      stream Id
     * @param streamOwnerId owner Id of the stream (not necessarily of all the images within)
     * @param limit         max of 32
     * @param offset        offset within results for pagination
     * @return a list of media objects matching the search criteria.
     */
    @ApiMethod(name = "getMedia", httpMethod = "get")
    public StreamResult getMedia(User user,
                                 @Named("streamId") Long streamId,
                                 @Named("streamOwnerId") Long streamOwnerId,
                                 @Named("queryLimit") Integer limit,
                                 @Named("queryOffset") Integer offset) {

        // System.err.println("getMedia-> streamId: " + streamId + " ownerId: " + streamOwnerId);
        StreamResult returnVal;
        MediaCacheKey cacheKey = null;
        // Disabling cache on stream view due to incrementing view count..
        // and who cares really?
        boolean useCache = false;
        if (useCache && Config.API_CACHE_TIME_SEC >= 0) {
            cacheKey = new MediaCacheKey(streamId, streamOwnerId, limit, offset);
            returnVal = (StreamResult) syncCache.get(cacheKey);
            if (returnVal != null) {
                return returnVal;
            }
        }

        if (limit > 32) {
            limit = 32;
        } // XXX TODO: move to config?
        Site site = Site.load(null);
        if (site == null) {
            // Should probably be logging something somewhere...
            throw new IllegalArgumentException("Unable to load site");
        }
        CUser cUser;
        if (user != null) {
            cUser = ConnexusServletBase.getOrCreateUserRecord(user, site.getKey());
        } else {
            cUser = null;
        }

        CUser owner = CUser.getById(streamOwnerId, site.getKey());
        if (owner == null) {
            throw new IllegalArgumentException("Unable to load user");

        }
        connexus.model.Stream stream = connexus.model.Stream.getById(streamId, owner);
        if (stream == null) {
            throw new IllegalArgumentException("Unable to load stream ID: " + streamId + " ownerID: " + streamOwnerId);
        }
        returnVal = new StreamResult();
        List<Media> mediaList = getMediaHelper(stream, limit, offset);
        returnVal.setMediaList(mediaList);
        if (mediaList != null) {
            returnVal.setResultSize(mediaList.size());
        } else {
            returnVal.setResultSize(0);
        }
        returnVal.setStreamId(streamId);
        returnVal.setStreamOwnerId(streamOwnerId);
        if (cUser != null) {
            returnVal.setMyId(cUser.getId());
        } else {
            returnVal.setMyId(null);
        }
        boolean canUpload = false;
        if (cUser != null) {
            // Currently, you can only upload if you own the stream
            if (cUser.getId().equals(owner.getId())) {
                canUpload = true;
            }
        }

        // for now, letting anyone upload to any stream thru mobile for funsies
        canUpload = true;

        returnVal.setCanUpload(canUpload);
        if (canUpload) {
            final BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
            returnVal.setUploadUrl(blobstoreService.createUploadUrl("/view"));
        } else {
            returnVal.setUploadUrl(null);
        }

        returnVal.setQueryLimit(limit);
        returnVal.setQueryOffset(offset);
        returnVal.setStreamName(stream.getName());
        returnVal.setStreamOwnerName(stream.getOwnerName());
        returnVal.setStream(convertStreamToAPI(stream));
        if (useCache && cacheKey != null && Config.API_CACHE_TIME_SEC >= 0) {
            syncCache.put(cacheKey, returnVal, Expiration.byDeltaSeconds(Config.API_CACHE_TIME_SEC));
        }
        return returnVal;
    }

    protected List<Media> getMediaHelper(connexus.model.Stream stream, int limit, int offset) {
        List<connexus.model.Media> modelMediaList = stream.getMedia(offset, limit);
        if (modelMediaList == null) {
            throw new IllegalArgumentException("Can't load media list");
        }
        List<Media> mediaList = new ArrayList<Media>();
        int mediaCount = 0;
        for (connexus.model.Media modelMedia : modelMediaList) {
            if (modelMedia == null) {
                continue;
            }
            Media endpointMedia = convertMediaToAPI(modelMedia);
            endpointMedia.setQueryIndex(mediaCount++);
            endpointMedia.setQueryLimit(limit);
            endpointMedia.setQueryOffset(offset);
            mediaList.add(endpointMedia);
        }
        return mediaList;
    }

    /**
     * Search for images whose location tag is nearby the given coordinates.
     *
     * @param user      Google Account of the requesting user
     * @param limit     Number of results to return. Max of 32.
     * @param offset    Index into total result set (used for pagination)
     * @param latitude  search latitude
     * @param longitude search longitude
     * @return a list of Stream objects matching the criteria
     */
    @ApiMethod(name = "getNearby", httpMethod = "get")
    public NearbyResult getNearby(User user,
                                  @Named("queryLimit") Integer limit,
                                  @Named("queryOffset") Integer offset,
                                  @Named("latitude") Double latitude,
                                  @Named("longitude") Double longitude) {
        NearbyResult nearbyResult = new NearbyResult();
        nearbyResult.setQueryLimit(limit);
        nearbyResult.setQueryOffset(offset);
        nearbyResult.setSearchLatitude(latitude);
        nearbyResult.setSearchLongitude(longitude);
        List<Media> mediaList = new ArrayList<Media>();
        for (LocMedia locMedia:
                connexus.model.Media.searchByLocation(limit, offset, latitude, longitude)) {
            if (locMedia!= null) {
                connexus.model.Media media = locMedia.getMedia();
                Media epMedia = convertMediaToAPI(media);
                epMedia.setMetersToSearchPoint(locMedia.getDistanceToOrigin());
                mediaList.add(epMedia);
            }
        }
        nearbyResult.setMediaList(mediaList);
        nearbyResult.setResultSize(mediaList.size());
        return nearbyResult;
    }

    protected static Stream convertStreamToAPI(connexus.model.Stream modelStream) {
        Stream stream = new Stream();
        stream.setId(modelStream.getId());
        stream.setOwnerId(modelStream.getOwner().getId());
        stream.setName(modelStream.getName());
        stream.setOwnerEmail(modelStream.getOwnerName());
        stream.setCoverURL(modelStream.getCoverURL());
        stream.setTags(modelStream.getTags());
        stream.setCreationDate(modelStream.getCreationDate());
        stream.setNumberOfMedia(modelStream.getNumberOfMedia());
        Long views = modelStream.getAndIncrementViews();
        if (views == null) {
            views = new Long(0);
        }
        stream.setViews(views);
        stream.setTrendingViews(modelStream.getTrendingViews());
        stream.setLastNewMedia(modelStream.getLastNewMediaDate());
        return stream;
    }

    protected static Media convertMediaToAPI(connexus.model.Media modelMedia) {
        Media media = new Media();
        media.setId(modelMedia.getId());
        media.setStreamId(modelMedia.getStream().getId());
        media.setStreamOwnerId(modelMedia.getStreamOwnerId());
        media.setUrl(modelMedia.getMediaServingURL());
        media.setThumbUrl(modelMedia.getThumbURL());
        media.setFileName(modelMedia.getFileName());
        media.setMimeType(modelMedia.getMimeType());
        media.setSize(modelMedia.getSize());
        media.setComments(modelMedia.getComments());
        media.setCreationDate(modelMedia.getCreationDate());
        media.setUploader(modelMedia.getUploaderNow().getRealName());
        media.setViews(modelMedia.getAndIncrementViews());

        Double lLat = modelMedia.getLatitude();
        Double lLong = modelMedia.getLongitude();
        double p_lat = 0.0;
        double p_long = 0.0;
        if (lLat != null) {
            p_lat = lLat.doubleValue();
        }
        if (lLong != null) {
            p_long = lLong.doubleValue();
        }

        media.setLatitude(p_lat);
        media.setLongitude(p_long);
        return media;
    }
}