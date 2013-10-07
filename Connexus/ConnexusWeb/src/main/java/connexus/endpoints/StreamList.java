package connexus.endpoints;

import com.googlecode.objectify.Ref;
import connexus.Config;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.appengine.api.users.User;
import connexus.model.CUser;
import connexus.model.Site;
import connexus.servlet.ConnexusServletBase;

import javax.annotation.Nullable;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

import static connexus.OfyService.ofy;

/**
 * Defines v1 of a streamlist API, which provides lists of Connexus streams.
 */
@Api(name = "streamlist", version = "v1",
        scopes = {Config.EMAIL_SCOPE},
        clientIds = {
                com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID,
                Config.ANDROID_CLIENT_ID,
                Config.WEB_CLIENT_ID
        },
        audiences = {Config.ANDROID_AUDIENCE})
public class StreamList {

    @ApiMethod(name = "getStreams", httpMethod = "get")
    public List<Stream> getStreams(User user,
                                   @Named("queryLimit") Integer limit,
                                   @Named("queryOffset") Integer offset,
                                   @Named("latitude") @Nullable Double latitude,
                                   @Named("longitude") @Nullable Double longitude,
                                   @Named("query") @Nullable String query,
                                   @Named("mySubs") @Nullable Boolean mySubs) {
        List<Stream> streams = new ArrayList<Stream>();

        Ref<Site> site = ofy().load().type(Site.class).id(Config.siteId);
        if (site == null) {
            // Should probably be logging something somewhere...
            return streams;
        }
        CUser cUser;
        if (user != null) {
            cUser = ConnexusServletBase.getOrCreateUserRecord(user, site);
        }

        if (limit == null) { limit = 0; }
        if (limit.equals(0)) {
            limit = 16;
        }
        if (limit > 32) { limit = 32; } // XXX TODO: move to config?
        if (offset == null) { offset = 0; }
        List<connexus.model.Stream> modelStreams = connexus.model.Stream.getAllStreams(site.getKey(), limit, offset);

        int streamCount = 0;
        for (connexus.model.Stream stream : modelStreams) {
            Stream endpointStream = convertStreamToAPI(stream);
            streams.add(endpointStream);

            endpointStream.setQueryIndex(streamCount++);
            endpointStream.setQueryOffset(offset);
            endpointStream.setQueryLimit(limit);

            // Do an initial image search on each stream to save the subsequent requests
            int mediaOffset = 0;
            int mediaLimit = 16;

            List<Media> mediaList = getMediaHelper(stream, mediaLimit, mediaOffset);
            endpointStream.setMediaOffset(mediaOffset);
            endpointStream.setMediaLimit(mediaLimit);
            endpointStream.setMediaList(mediaList);

        }

        // TODO: Handle query, latlong, mysubs!

        return streams;
    }

    @ApiMethod(name = "getMedia", httpMethod = "get")
    public List<Media> getMedia(User user,
                                 @Named("streamId") Long streamId,
                                 @Named("ownerId") Long ownerId,
                                 @Named("queryLimit") Integer limit,
                                 @Named("queryOffset") Integer offset) {
        if (limit > 32) { limit = 32; } // XXX TODO: move to config?
        Site site = Site.load(null);
        if (site == null) {
            // Should probably be logging something somewhere...
            throw new IllegalArgumentException("Unable to load site");
        }
        CUser owner = CUser.getById(ownerId, site.getKey());
        if (owner == null) {
            throw new IllegalArgumentException("Unable to load user");

        }
        connexus.model.Stream stream = connexus.model.Stream.getById(streamId, owner);
        if (stream == null) {
            throw new IllegalArgumentException("Unable to load stream");
        }
        return getMediaHelper(stream, limit, offset);
    }

    private List<Media> getMediaHelper(connexus.model.Stream stream, int limit, int offset) {
        List<connexus.model.Media> modelMediaList = stream.getMedia(offset, limit);
        if (modelMediaList == null) {
            throw new IllegalArgumentException("Can't load media list");
        }
        List<Media> mediaList = new ArrayList<Media>();
        int mediaCount = 0;
        for (connexus.model.Media modelMedia : modelMediaList) {
            Media endpointMedia = convertMediaToAPI(modelMedia);
            endpointMedia.setQueryIndex(mediaCount++);
            endpointMedia.setQueryLimit(limit);
            endpointMedia.setQueryOffset(offset);
            mediaList.add(endpointMedia);
        }
        return mediaList;
    }

    private static Stream convertStreamToAPI(connexus.model.Stream modelStream) {
        Stream stream = new Stream();
        stream.setId(modelStream.getId());
        stream.setOwnerId(modelStream.getOwner().getId());
        stream.setName(modelStream.getName());
        stream.setOwnerEmail(modelStream.getOwnerName());
        stream.setCoverURL(modelStream.getCoverURL());
        stream.setTags(modelStream.getTags());
        stream.setCreationDate(modelStream.getCreationDate());
        stream.setNumberOfMedia(modelStream.getNumberOfMedia());
        stream.setViews(modelStream.getViews());
        stream.setTrendingViews(modelStream.getTrendingViews());
        stream.setLastNewMedia(modelStream.getLastNewMediaDate());
        return stream;
    }

    private static Media convertMediaToAPI(connexus.model.Media modelMedia) {
        Media media = new Media();
        media.setId(modelMedia.getId());
        media.setStreamId(modelMedia.getStream().getId());
        media.setOwnerId(modelMedia.getUploader().getId());
        media.setUrl(modelMedia.getMediaServingURL());
        media.setThumbUrl(modelMedia.getThumbURL());
        media.setFileName(modelMedia.getFileName());
        media.setMimeType(modelMedia.getMimeType());
        media.setSize(modelMedia.getSize());
        media.setComments(modelMedia.getComments());
        media.setCreationDate(modelMedia.getCreationDate());
        media.setUploader(modelMedia.getUploaderNow().getRealName());
        media.setViews(modelMedia.getViews());
        return media;
    }
}