package connexus.servlet;

import com.google.appengine.api.blobstore.*;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.common.collect.Lists;
import connexus.Entity;
import connexus.FileMeta;
import connexus.FileUrl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

/**
 * RESTful interface for uploading files
 * https://github.com/blueimp/jQuery-File-Upload/wiki/Google-App-Engine-Java
 */
@Path("/file")
public class FileResource {
    private final BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    private final BlobInfoFactory blobInfoFactory = new BlobInfoFactory();

    /* step 1. get a unique url */

    @GET
    @Path("/url")
    public Response getCallbackUrl() {
        /* this is /_ah/upload and it redirects to its given path */
        String url = blobstoreService.createUploadUrl("/view");
        return Response.ok(new FileUrl(url)).build();
    }

    /* step 2. post a file */
/*
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response post(@Context HttpServletRequest req,
                         @Context HttpServletResponse res) throws IOException,
            URISyntaxException {
        Map<String, BlobKey> blobs = blobstoreService.getUploadedBlobs(req);
        BlobKey blobKey = blobs.get("files[]");

        BlobInfo info = blobInfoFactory.loadBlobInfo(blobKey);
        String name = info.getFilename();
        long size = info.getSize();
        String url = "/rest/file/" + blobKey.getKeyString();

        ImagesService imagesService = ImagesServiceFactory.getImagesService();
        ServingUrlOptions.Builder.withBlobKey(blobKey).crop(true).imageSize(80);
        int sizePreview = 80;
        String urlPreview = imagesService
                .getServingUrl(ServingUrlOptions.Builder.withBlobKey(blobKey)
                        .crop(true).imageSize(sizePreview));

        FileMeta meta = new FileMeta(name, size, url, urlPreview);

        List<FileMeta> metas = Lists.newArrayList(meta);
        Entity entity = new Entity(metas);
        return Response.ok(entity, MediaType.APPLICATION_JSON).build();
    }
*/
    /* step 3. redirected to the meta info */
/*
    @GET
    @Path("/{key}/meta")
    public Response redirect(@PathParam("key") String key) throws IOException {
        BlobKey blobKey = new BlobKey(key);
        BlobInfo info = blobInfoFactory.loadBlobInfo(blobKey);

        String name = info.getFilename();
        long size = info.getSize();
        String url = "/rest/file/" + key;

        ImagesService imagesService = ImagesServiceFactory.getImagesService();
        ServingUrlOptions.Builder.withBlobKey(blobKey).crop(true).imageSize(80);
        int sizePreview = 80;
        String urlPreview = imagesService
                .getServingUrl(ServingUrlOptions.Builder.withBlobKey(blobKey)
                        .crop(true).imageSize(sizePreview));

        FileMeta meta = new FileMeta(name, size, url, urlPreview);

        List<FileMeta> metas = Lists.newArrayList(meta);
        Entity entity = new Entity(metas);
        return Response.ok(entity, MediaType.APPLICATION_JSON).build();
    }
*/
    /* step 4. download the file */

    @GET
    @Path("/{key}")
    public Response serve(@PathParam("key") String key, @Context HttpServletResponse response) throws IOException {
        BlobKey blobKey = new BlobKey(key);
        final BlobInfo blobInfo = blobInfoFactory.loadBlobInfo(blobKey);
        response.setHeader("Content-Disposition", "attachment; filename=" + blobInfo.getFilename());
        BlobstoreServiceFactory.getBlobstoreService().serve(blobKey, response);
        return Response.ok().build();
    }

    /* step 5. delete the file */
/*
    @DELETE
    @Path("/{key}")
    public Response delete(@PathParam("key") String key) {
        Status status;
        try {
            blobstoreService.delete(new BlobKey(key));
            status = Status.OK;
        } catch (BlobstoreFailureException bfe) {
            status = Status.NOT_FOUND;
        }
        return Response.status(status).build();

    }
*/
}
