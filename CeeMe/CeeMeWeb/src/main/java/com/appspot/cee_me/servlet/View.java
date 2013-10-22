package connexus.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static connexus.OfyService.ofy;

import com.google.appengine.api.blobstore.*;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;

import connexus.Config;
import connexus.StreamHandle;
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

	private Stream viewingStream;
	private CUser viewingStreamUser;
	private StreamHandle viewingStreamHandle;

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		InitializeContext(req, resp); // Base site context initialization

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

			// Media and streams have separate view counts.
			for (Media media: mediaList) {
				media.getAndIncrementViews();
			}
			viewingStream.getAndIncrementViews();
			
		} else {
			// No stream selected... let them browse all streams.
			List<Stream> allStreams = Stream.getAllStreams(site.getKey());
			req.setAttribute("allStreamsList", allStreams);
		}

		// Get the Blobstore upload URL.
		String uploadURL = blobstoreService.createUploadUrl("/view");
		req.setAttribute("uploadURL", uploadURL);

		// Forward to JSP page to display them in a HTML table.
		req.getRequestDispatcher(dispatcher).forward(req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		InitializeContext(req, resp); // Base site context initialization

		if (req.getParameter("upload") != null) {
			uploadMedia(req, resp);
		} else if (req.getParameter("delete") != null) {
			deleteMedia(req, resp);
		} else if (req.getParameter("setCover") != null) {
			setCover(req, resp);
		} else {
			alertInfo(req, "TODO: Not implemented yet.");
		}

		resp.sendRedirect(viewingStream.getViewURI());
	}

	@Override
	protected void InitializeContext(HttpServletRequest req,
			HttpServletResponse resp) throws IOException, ServletException {
		super.InitializeContext(req, resp);


		viewingStreamHandle = null;
		viewingStream = null;
		viewingStreamUser = null;
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

	}

	private void setCover(HttpServletRequest req, HttpServletResponse resp) {
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

	private void deleteMedia(HttpServletRequest req, HttpServletResponse resp) {
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
	private void uploadMedia(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {

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
            System.err.println("UPLOAD ERROR: can't extract blobkey from blobstore");
			alertError(req, "I'm sorry, there was a problem with your upload.");
			return;
		}
		BlobKey bkey = blobKeyList.get(0);
		String bkeyStr = bkey.getKeyString();
		String comments = req.getParameter("comments");

        double latitude = 0;
        double longitude = 0;
        try {
            latitude = Double.parseDouble(req.getParameter("latitude"));
        } catch (Exception e) {
            latitude = 0;
        }
        try {
            longitude = Double.parseDouble(req.getParameter("longitude"));
        } catch (Exception e) {
            longitude = 0;
        }

		BlobInfo bInfo = blobInfoFactory.loadBlobInfo(bkey);

		Media media = new Media(null, viewingStream.getKey(), bkey, bkeyStr,
				cuser.getKey());

        String mediaCT = req.getParameter("mediaCT");
        if (mediaCT != null) {
            media.setMimeType(mediaCT);
        } else {
            media.setMimeType(bInfo.getContentType());
        }

		media.setFileName(bInfo.getFilename());
		media.setSize(bInfo.getSize());
		media.setComments(comments);
        media.setLongitude(longitude);
        media.setLatitude(latitude);

		ofy().save().entities(media).now();
		viewingStream.incNumberOfMedia();

		// If the stream does not already have a cover, make this the cover.
		if (viewingStream.getCoverURL() == null || viewingStream.getCoverURL().length() == 0) {
			ImagesService imagesService = ImagesServiceFactory.getImagesService();
			try {
    			viewingStream.setCoverURL(imagesService.getServingUrl(bkey));
			} catch (IllegalArgumentException e) {
                System.err.println("UPLOAD ERROR: can't get media serving url");
                e.printStackTrace(System.err);
			}
		}
		viewingStream.save();
		
		// System.err.println("MEDIA was into space! " + media);

	}

}