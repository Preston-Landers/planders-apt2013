package connexus.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static connexus.OfyService.ofy;

import com.google.appengine.api.blobstore.*;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.users.User;

import com.google.gson.Gson;
import com.googlecode.objectify.Ref;
import connexus.*;
import connexus.model.*;

public class View extends ConnexusServletBase {

    /**
     *
     */
    private static final long serialVersionUID = -4946172986473915671L;
    public static final String uri = "/view";
    public static final String dispatcher = "/WEB-INF/jsp/view.jsp";
    private final BlobstoreService blobstoreService = BlobstoreServiceFactory
            .getBlobstoreService();
    private final BlobInfoFactory blobInfoFactory = new BlobInfoFactory();

//    private Stream viewingStream;
//    private CUser viewingStreamUser;
//    private StreamHandle viewingStreamHandle;

    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        ViewContext viewContext = InitializeViewContext(req, resp); // Base site context initialization
        Stream viewingStream = viewContext.getViewingStream();

        // How many images to show
        int offset;
        int limit;
        try {
            offset = Integer.parseInt(req.getParameter("offset"));
        } catch (NumberFormatException e) {
            offset = 0;
        }
        try {
            limit = Integer.parseInt(req.getParameter("limit"));
        } catch (NumberFormatException e) {
            limit = 3;
        }

        req.setAttribute("offset", offset);
        req.setAttribute("limit", limit);

        boolean canDoUpload = false;

        if (viewingStream != null) {
            // We are viewing a stream, so set some variables for the JSP
            List<Media> mediaList = viewingStream.getMedia(offset, limit);
            req.setAttribute("mediaList", mediaList);
            int numberOfMedia = Config.safeLongToInt(viewingStream.getNumberOfMedia());

            int newerOffset = Math.max((offset - limit), 0);
            int newerLimit = limit;
            int olderOffset = Math.min((offset + limit),
                    (numberOfMedia - limit));
            int olderLimit = limit;

            boolean showNewerButton = false;
            boolean showOlderButton = false;
            if (offset > 0) {
                showNewerButton = true;
            }
            if (offset + limit < numberOfMedia) {
                showOlderButton = true;
            }

            req.setAttribute("numberOfMedia", numberOfMedia);

            req.setAttribute("olderOffset", olderOffset);
            req.setAttribute("olderLimit", olderLimit);
            req.setAttribute("newerOffset", newerOffset);
            req.setAttribute("newerLimit", newerLimit);

            req.setAttribute("showNewerButton", showNewerButton);
            req.setAttribute("showOlderButton", showOlderButton);
            // Always points to the "head" of the stream
            req.setAttribute("streamViewUrl", viewingStream.getViewURI());

            // Media and streams have separate view counts.
            for (Media media : mediaList) {
                media.getAndIncrementViews();
            }
            viewingStream.getAndIncrementViews();

            // For now, only allow uploading if this is YOUR stream.
            canDoUpload = (viewContext.getViewingStreamUser() == viewContext.getConnexusContext().getCuser());
        } else {
            // No stream selected... let them browse all streams.
            List<Stream> allStreams = Stream.getAllStreams(
                    viewContext.getConnexusContext().getSite().getKey());
            req.setAttribute("allStreamsList", allStreams);
        }

        req.setAttribute("canDoUpload", canDoUpload);

        // Forward to JSP page to display them in a HTML table.
        req.getRequestDispatcher(dispatcher).forward(req, resp);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        ViewContext viewContext = InitializeViewContext(req, resp); // Base site context initialization
        Stream viewingStream = viewContext.getViewingStream();
        if (viewingStream == null) {
            String errorMsg = "Error: Did not provide a valid stream id (v) parameter.";
            System.err.println(errorMsg);
            alertError(req, errorMsg);
            resp.sendRedirect(uri);
            return;
        }
        if (req.getParameter("upload") != null) {
            uploadMedia(viewContext, req, resp);
            return;
        } else if (req.getParameter("delete") != null) {
            deleteMedia(viewContext, req, resp);
        } else if (req.getParameter("setCover") != null) {
            setCover(viewContext, req, resp);
        } else {
            alertInfo(req, "TODO: Not implemented yet.");
        }

        resp.sendRedirect(viewingStream.getViewURI());
    }

    protected ViewContext InitializeViewContext(HttpServletRequest req,
                                                HttpServletResponse resp) throws IOException, ServletException {

        ConnexusContext connexusContext = super.InitializeContext(req, resp);
        CUser cuser = connexusContext.getCuser();
        Ref<Site> site = connexusContext.getSite();


        StreamHandle viewingStreamHandle = null;
        Stream viewingStream = null;
        CUser viewingStreamUser = null;
        if (req.getParameter("v") != null) {
            try {
                viewingStreamHandle = StreamHandle.getStreamHandleFromRequest(
                        req, site);
                viewingStream = viewingStreamHandle.getStream();
                viewingStreamUser = viewingStreamHandle.getCuser();
                req.setAttribute("viewingStream", viewingStream);
                req.setAttribute("viewingStreamUser", viewingStreamUser);
                boolean isMyStream = false;
                if ((cuser != null) &&
                        (viewingStream.getOwner().equals(cuser.getKey()))) {
                    isMyStream = true;
                }
                req.setAttribute("isMyStream", isMyStream);
            } catch (RuntimeException e) {
                e.printStackTrace(System.err);
                alertError(req, e.toString());
            }
        }
        // Determine if the current user is subscribed to this stream.
        Subscription mySubForStream = null;
        if (cuser != null && viewingStreamHandle != null) {
            mySubForStream = Subscription.getUserSubscriptionFromStreamHandle(
                    cuser, viewingStreamHandle);
        }
        req.setAttribute("mySubForStream", mySubForStream);
        return new ViewContext(connexusContext, viewingStream, viewingStreamUser, viewingStreamHandle);
    }

    private void setCover(ViewContext viewContext, HttpServletRequest req, HttpServletResponse resp) {
        Stream viewingStream = viewContext.getViewingStream();
        Media media = Media.getById(Long.parseLong(req.getParameter("m")),
                viewingStream);
        if (media == null) {
            alertError(req, "Cannot find media to pin.");
            return;
        }
        String mediaURL = media.getThumbURL();
        viewingStream.setCoverURL(mediaURL);
        viewingStream.fixNumMedia();
        viewingStream.save();
        alertInfo(req, "Set new cover image.");
    }

    private void deleteMedia(ViewContext viewContext, HttpServletRequest req, HttpServletResponse resp) {
        Stream viewingStream = viewContext.getViewingStream();
        Media media = Media.getById(Long.parseLong(req.getParameter("delete")),
                viewingStream);
        if (media == null) {
            alertError(req, "Cannot find media to delete.");
            return;
        }
        media.deleteMedia();
        alertWarning(req, "Image was deleted.");
    }

    @SuppressWarnings("deprecation")
    private void uploadMedia(ViewContext viewContext, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        Stream viewingStream = viewContext.getViewingStream();
        User guser = viewContext.getConnexusContext().getGuser();
        CUser cuser = viewContext.getConnexusContext().getCuser();
        Ref<Site> site = viewContext.getConnexusContext().getSite();

        // Can bypass the normal requirement to be logged in in order to upload
        // by providing the 'uu' parameter with your cuser ID.
        String uploadUserId = req.getParameter("uu");

        if (viewingStream == null) {
            System.err.println("UPLOAD ERROR: viewingStream == null");
            throw new ServletException("Internal error in upload.");
        }
        if (guser == null && uploadUserId == null) {
            System.err.println("UPLOAD ERROR: not logged in");
            throw new ServletException("Internal error in upload: not logged in.");
        }
        if (guser == null) {
            Long uploadUID = Long.parseLong(uploadUserId);
            cuser = CUser.getById(uploadUID, site.getKey());
            if (cuser == null) {
                System.err.println("UPLOAD ERROR: not logged in");
                throw new ServletException("Internal error in upload: not logged in.");
            }
        }

        Map<String, List<BlobKey>> uploadMap = blobstoreService.getUploads(req);

        List<BlobKey> blobKeyList = uploadMap.get("media");
        if (blobKeyList == null) {
            System.err.println("UPLOAD ERROR: can't extract blobkey list from blobstore");
            alertError(req, "I'm sorry, there was a problem with your upload.");
            return;
        }
        int i = -1;
        List<FileMeta> fileList = new ArrayList<FileMeta>();
        int blobKeyListSize = blobKeyList.size();

        String[] latitudeParams = req.getParameterValues("latitude");
        if (latitudeParams == null) {
            latitudeParams = new String[blobKeyListSize];
        }
        String[] longitudeParams = req.getParameterValues("longitude");
        if (longitudeParams == null) {
            longitudeParams = new String[blobKeyListSize];
        }
        String[] mediaCTParams = req.getParameterValues("mediaCT");
        if (mediaCTParams == null) {
            mediaCTParams = new String[blobKeyListSize];
        }
//        String[] commentParams = req.getParameterValues("comments");
//        if (commentParams == null) {
//            commentParams = new String[blobKeyListSize+1];
//        }
        for (BlobKey bkey : blobKeyList) {
            i++;
            String bkeyStr = bkey.getKeyString();

            double latitude = 0;
            double longitude = 0;
            try {
                String latitudeStr = latitudeParams[i];
                latitude = Double.parseDouble(latitudeStr);
            } catch (Exception e) {
                latitude = 0;
            }
            try {
                String longitudeStr = longitudeParams[i];
                longitude = Double.parseDouble(longitudeStr);
            } catch (Exception e) {
                longitude = 0;
            }

            BlobInfo bInfo = blobInfoFactory.loadBlobInfo(bkey);

            Media media = new Media(null, viewingStream.getKey(), bkey, bkeyStr,
                    cuser.getKey());

            String mediaCT = mediaCTParams[i];
            if (mediaCT != null) {
                media.setMimeType(mediaCT);
            } else {
                media.setMimeType(bInfo.getContentType());
            }

            String fileName = bInfo.getFilename();
            String comments = req.getParameter("comments" + fileName);
            if (comments == null || comments.length() == 0) {
                comments = fileName;
            }

            media.setFileName(fileName);
            media.setSize(bInfo.getSize());
            media.setComments(comments);
            media.setLongitude(longitude);
            media.setLatitude(latitude);

            ofy().save().entities(media).now();
            viewingStream.incNumberOfMedia();

            fileList.add(
                    new FileMeta(fileName, bInfo.getSize(),
                            media.getMediaServingURL(), media.getThumbURL(), media.getComments()));

            System.err.println("MEDIA was into space! " + media);

        }


        // If the stream does not already have a cover, make the first image the cover.
        if (viewingStream.getCoverURL() == null || viewingStream.getCoverURL().length() == 0) {
            ImagesService imagesService = ImagesServiceFactory.getImagesService();
            try {
                BlobKey bkey = blobKeyList.get(0);
                viewingStream.setCoverURL(imagesService.getServingUrl(bkey));
            } catch (IllegalArgumentException e) {
                System.err.println("UPLOAD ERROR: can't get media serving url");
                e.printStackTrace(System.err);
            }
        }
        viewingStream.save();

        // Return a JSON string describing the upload we just handled
        FileUploadEntity fileUploadEntity = new FileUploadEntity(fileList);
        Gson gson = new Gson();
        resp.setContentType("text/json");
        PrintWriter outpw = resp.getWriter();
        String outJson = gson.toJson(fileUploadEntity);
        outpw.println(outJson);

    }

}
