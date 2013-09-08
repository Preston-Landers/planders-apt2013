package connexus.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
// import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
// import javax.servlet.http.HttpSession;








import static connexus.OfyService.ofy;










import com.google.appengine.api.blobstore.*;

import connexus.StreamHandle;
import connexus.model.*;

public class View extends ConnexusServletBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4946172986473915671L;
	public static final String uri = "/view";
	public static final String dispatcher = "/WEB-INF/jsp/view.jsp";
	private final BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
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
		}
		catch (NumberFormatException e) {
			limit = 3;
		}
		
		req.setAttribute("offset", offset);
		req.setAttribute("limit", limit);

		// List<String> 
		if (viewingStream != null) {
			req.setAttribute("mediaList", viewingStream.getMedia(offset, limit));
			int numberOfMedia = viewingStream.getNumberOfMedia();
			
			int newerOffset = Math.max((offset - limit), 0);
			int newerLimit = limit;
			int olderOffset = Math.min((offset + limit), (numberOfMedia-limit));
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
			
		} else {
			// No stream selected... let them browse all streams.
			List<Stream> allStreams = Stream.getAllStreams(site);
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
		} else {
			alertInfo(req, "TODO: Not implemented yet.");	
		}
				
		resp.sendRedirect(viewingStream.getViewURI());
	}

	@Override
	protected void InitializeContext(HttpServletRequest req,
			HttpServletResponse resp) throws IOException, ServletException {
		super.InitializeContext(req, resp);

		// initialize my subscriptions (cuser could be null, you get an empty list)
		List<Subscription> mySubs = Subscription.getSubscriptionsForUser(cuser);
		if (mySubs.size() < 1) {
			System.err.println("User has no subs. " + cuser);
		}
		
		try {
			viewingStreamHandle = StreamHandle.getStreamHandleFromRequest(req, site);
			viewingStream = viewingStreamHandle.getStream();
			viewingStreamUser = viewingStreamHandle.getCuser();
			req.setAttribute("viewingStream", viewingStream);
			req.setAttribute("viewingStreamUser", viewingStreamUser);
		} catch (RuntimeException e) {
			viewingStreamHandle = null;
			alertError(req, e.toString());
		}
		
//		
//		
//		viewingStream = null;
//		if (req.getParameter("v") != null) {
//			
//			CUser viewingStreamUser = null;
//			if (req.getParameter("vu") != null) {
//				viewingStreamUser = CUser.getById(Long.parseLong(req.getParameter("vu")), site.get());
//			}
//			req.setAttribute("viewingStreamUser", viewingStreamUser);
//			if (viewingStreamUser == null) {
//				alertError(req, "The stream you requested does not exist (cannot locate user).");
//				// throw new ServletException("Invalid stream requested");
//			} else {
//				viewingStream = Stream.getById(
//						Long.parseLong(req.getParameter("v")),
//						viewingStreamUser);
//				if (viewingStream == null) {
//					alertError(req, "The stream you requested does not exist.");
//					// throw new ServletException("Invalid stream requested");
//				}
//			}
//		}
//		req.setAttribute("viewingStream", viewingStream);
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
	
	private void uploadMedia(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		// TODO: verify user is logged in?
		
		Map<String,List<BlobKey>> uploadMap = blobstoreService.getUploads(req);
		
        List<BlobKey> blobKeyList = uploadMap.get("media");
        if (blobKeyList == null) {
        	alertError(req, "I'm sorry, there was a problem with your upload.");
        	return;
        }
        BlobKey bkey = blobKeyList.get(0);
        String bkeyStr = bkey.getKeyString();
        String comments = req.getParameter("comments");
		
        BlobInfo bInfo = blobInfoFactory.loadBlobInfo(bkey);
		
        Media media = new Media(null, viewingStream.getKey(), bkey, bkeyStr, cuser.getKey());
        media.setMimeType(bInfo.getContentType());
        media.setFileName(bInfo.getFilename());
        media.setSize(bInfo.getSize());
        media.setComments(comments);
        
        ofy().save().entities(media).now();
        
        System.err.println("MEDIA was into space! " + media);
        
	}

	
}
