package com.appspot.cee_me.android;

import com.google.api.client.http.javanet.NetHttpTransport;

/**
 * A singleton within the application to hold the NetHttpTransport
 * used for API and Endpoint calls
 */
public class HttpTransport {
    private HttpTransport() { }

    private NetHttpTransport httpTransport;

    /**
     * SingletonHolder is loaded on the first execution of Singleton.getInstance()
     * or the first access to SingletonHolder.INSTANCE, not before.
     */
    private static class SingletonHolder {
        public static final HttpTransport INSTANCE = new HttpTransport();

    }

    public static HttpTransport getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public NetHttpTransport getHttpTransport() {
        return httpTransport;
    }

    public void setHttpTransport(NetHttpTransport httpTransport) {
        this.httpTransport = httpTransport;
    }

    public void initialize() {
        getInstance().setHttpTransport(new NetHttpTransport());
    }

}
