package connexus;

import com.google.appengine.api.users.User;
import com.googlecode.objectify.Ref;
import connexus.model.CUser;
import connexus.model.Site;
import connexus.model.Stream;

/**
 * An object to represent the current servlet request context
 * within the View & Upload screens.
 */
public class ViewContext {
    private Stream viewingStream;
    private CUser viewingStreamUser;
    private StreamHandle viewingStreamHandle;
    private ConnexusContext connexusContext;

    public ViewContext(ConnexusContext connexusContext, Stream viewingStream, CUser viewingStreamUser, StreamHandle viewingStreamHandle) {
        this.connexusContext = connexusContext;
        this.viewingStream = viewingStream;
        this.viewingStreamUser = viewingStreamUser;
        this.viewingStreamHandle = viewingStreamHandle;
    }

    public Stream getViewingStream() {
        return viewingStream;
    }

    public void setViewingStream(Stream viewingStream) {
        this.viewingStream = viewingStream;
    }

    public CUser getViewingStreamUser() {
        return viewingStreamUser;
    }

    public void setViewingStreamUser(CUser viewingStreamUser) {
        this.viewingStreamUser = viewingStreamUser;
    }

    public StreamHandle getViewingStreamHandle() {
        return viewingStreamHandle;
    }

    public void setViewingStreamHandle(StreamHandle viewingStreamHandle) {
        this.viewingStreamHandle = viewingStreamHandle;
    }

    public ConnexusContext getConnexusContext() {
        return connexusContext;
    }

    public void setConnexusContext(ConnexusContext connexusContext) {
        this.connexusContext = connexusContext;
    }


}
