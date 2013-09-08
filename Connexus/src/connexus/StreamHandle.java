package connexus;

import javax.servlet.http.HttpServletRequest;

import com.googlecode.objectify.Ref;

import connexus.model.CUser;
import connexus.model.Site;
import connexus.model.Stream;

/**
 * Streams must be looked up with an associated user. This ties them together
 * and provides a method to extract both from the HTTP Request.
 * 
 * @author Preston
 * 
 */
public class StreamHandle {

	private final Stream stream;
	private final CUser cuser;

	public StreamHandle(Stream _stream, CUser _cuser) {
		stream = _stream;
		cuser = _cuser;
	}

	public StreamHandle() {
		stream = null;
		cuser = null;
	}

	public Stream getStream() {
		return stream;
	}

	public CUser getCuser() {
		return cuser;
	}

	// TODO: fix exception class?
	public static StreamHandle getStreamHandleFromRequest(
			HttpServletRequest req, Ref<Site> site) throws RuntimeException {

		String streamId = req.getParameter("v");
		if (streamId == null) {
			throw new RuntimeException("No stream ID (v) in request.");
		}
		String streamUserId = req.getParameter("vu");
		if (streamUserId == null) {
			throw new RuntimeException("No stream User ID (vu) in request.");
		}

		CUser viewingStreamUser = CUser.getById(Long.parseLong(streamUserId),
				site.get());
		if (viewingStreamUser == null) {
			throw new RuntimeException(
					"The stream you requested does not exist (cannot locate user).");
		}

		Stream viewingStream = Stream.getById(Long.parseLong(streamId),
				viewingStreamUser);
		if (viewingStream == null) {
			throw new RuntimeException(
					"The stream you requested does not exist.");
		}
		return new StreamHandle(viewingStream, viewingStreamUser);
	}
}
