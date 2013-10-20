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

	// Get a valid StreamHandle if available from the HTTP Request
	// Otherwise you get null
	public static StreamHandle getStreamHandleFromRequest(
			HttpServletRequest req, Ref<Site> site) throws RuntimeException {

		String streamUserAndId = req.getParameter("v");
		if (streamUserAndId == null) {
			throw new RuntimeException("No stream ID (v) in request.");
		}
		String[] splitStreamId = streamUserAndId.split("[:]", 2);
		if (splitStreamId.length < 2) {
			throw new RuntimeException("Invalid stream ID (v) in request.");
		}
		String streamUserId = splitStreamId[0];
		String streamId = splitStreamId[1];		
		if (streamId == null) {
			throw new RuntimeException("No stream ID (v) in request.");
		}
		if (streamUserId == null) {
			throw new RuntimeException("No stream ID (v) in request.");
		}

		CUser viewingStreamUser = CUser.getById(Long.parseLong(streamUserId),
				site.getKey());
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
