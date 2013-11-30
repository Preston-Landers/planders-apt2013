/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * This code was generated by https://code.google.com/p/google-apis-client-generator/
 * (build: 2013-11-22 19:59:01 UTC)
 * on 2013-11-30 at 01:29:59 UTC 
 * Modify at your own risk.
 */

package com.appspot.cee_me.sync;

/**
 * Service definition for Sync (v1).
 *
 * <p>
 * Synchronize messages from Cee.me (cee-me.appspot.com)
 * </p>
 *
 * <p>
 * For more information about this service, see the
 * <a href="" target="_blank">API Documentation</a>
 * </p>
 *
 * <p>
 * This service uses {@link SyncRequestInitializer} to initialize global parameters via its
 * {@link Builder}.
 * </p>
 *
 * @since 1.3
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public class Sync extends com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient {

  // Note: Leave this static initializer at the top of the file.
  static {
    com.google.api.client.util.Preconditions.checkState(
        com.google.api.client.googleapis.GoogleUtils.MAJOR_VERSION == 1 &&
        com.google.api.client.googleapis.GoogleUtils.MINOR_VERSION >= 15,
        "You are currently running with version %s of google-api-client. " +
        "You need at least version 1.15 of google-api-client to run version " +
        "1.17.0-rc of the sync library.", com.google.api.client.googleapis.GoogleUtils.VERSION);
  }

  /**
   * The default encoded root URL of the service. This is determined when the library is generated
   * and normally should not be changed.
   *
   * @since 1.7
   */
  public static final String DEFAULT_ROOT_URL = "https://cee-me.appspot.com/_ah/api/";

  /**
   * The default encoded service path of the service. This is determined when the library is
   * generated and normally should not be changed.
   *
   * @since 1.7
   */
  public static final String DEFAULT_SERVICE_PATH = "sync/v1/";

  /**
   * The default encoded base URL of the service. This is determined when the library is generated
   * and normally should not be changed.
   */
  public static final String DEFAULT_BASE_URL = DEFAULT_ROOT_URL + DEFAULT_SERVICE_PATH;

  /**
   * Constructor.
   *
   * <p>
   * Use {@link Builder} if you need to specify any of the optional parameters.
   * </p>
   *
   * @param transport HTTP transport, which should normally be:
   *        <ul>
   *        <li>Google App Engine:
   *        {@code com.google.api.client.extensions.appengine.http.UrlFetchTransport}</li>
   *        <li>Android: {@code newCompatibleTransport} from
   *        {@code com.google.api.client.extensions.android.http.AndroidHttp}</li>
   *        <li>Java: {@link com.google.api.client.googleapis.javanet.GoogleNetHttpTransport#newTrustedTransport()}
   *        </li>
   *        </ul>
   * @param jsonFactory JSON factory, which may be:
   *        <ul>
   *        <li>Jackson: {@code com.google.api.client.json.jackson2.JacksonFactory}</li>
   *        <li>Google GSON: {@code com.google.api.client.json.gson.GsonFactory}</li>
   *        <li>Android Honeycomb or higher:
   *        {@code com.google.api.client.extensions.android.json.AndroidJsonFactory}</li>
   *        </ul>
   * @param httpRequestInitializer HTTP request initializer or {@code null} for none
   * @since 1.7
   */
  public Sync(com.google.api.client.http.HttpTransport transport, com.google.api.client.json.JsonFactory jsonFactory,
      com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
    this(new Builder(transport, jsonFactory, httpRequestInitializer));
  }

  /**
   * @param builder builder
   */
  Sync(Builder builder) {
    super(builder);
  }

  @Override
  protected void initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest<?> httpClientRequest) throws java.io.IOException {
    super.initialize(httpClientRequest);
  }

  /**
   * Create a request for the method "createMedia".
   *
   * This request holds the parameters needed by the sync server.  After setting any optional
   * parameters, call the {@link CreateMedia#execute()} method to invoke the remote operation.
   *
   * @param fileName
   * @param gcsFilename
   * @param mimeType
   * @param size
   * @return the request
   */
  public CreateMedia createMedia(java.lang.String fileName, java.lang.String gcsFilename, java.lang.String mimeType, java.lang.Long size) throws java.io.IOException {
    CreateMedia result = new CreateMedia(fileName, gcsFilename, mimeType, size);
    initialize(result);
    return result;
  }

  public class CreateMedia extends SyncRequest<com.appspot.cee_me.sync.model.Media> {

    private static final String REST_PATH = "createMedia";

    /**
     * Create a request for the method "createMedia".
     *
     * This request holds the parameters needed by the the sync server.  After setting any optional
     * parameters, call the {@link CreateMedia#execute()} method to invoke the remote operation. <p>
     * {@link
     * CreateMedia#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)}
     * must be called to initialize this instance immediately after invoking the constructor. </p>
     *
     * @param fileName
     * @param gcsFilename
     * @param mimeType
     * @param size
     * @since 1.13
     */
    protected CreateMedia(java.lang.String fileName, java.lang.String gcsFilename, java.lang.String mimeType, java.lang.Long size) {
      super(Sync.this, "POST", REST_PATH, null, com.appspot.cee_me.sync.model.Media.class);
      this.fileName = com.google.api.client.util.Preconditions.checkNotNull(fileName, "Required parameter fileName must be specified.");
      this.gcsFilename = com.google.api.client.util.Preconditions.checkNotNull(gcsFilename, "Required parameter gcsFilename must be specified.");
      this.mimeType = com.google.api.client.util.Preconditions.checkNotNull(mimeType, "Required parameter mimeType must be specified.");
      this.size = com.google.api.client.util.Preconditions.checkNotNull(size, "Required parameter size must be specified.");
    }

    @Override
    public CreateMedia setAlt(java.lang.String alt) {
      return (CreateMedia) super.setAlt(alt);
    }

    @Override
    public CreateMedia setFields(java.lang.String fields) {
      return (CreateMedia) super.setFields(fields);
    }

    @Override
    public CreateMedia setKey(java.lang.String key) {
      return (CreateMedia) super.setKey(key);
    }

    @Override
    public CreateMedia setOauthToken(java.lang.String oauthToken) {
      return (CreateMedia) super.setOauthToken(oauthToken);
    }

    @Override
    public CreateMedia setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (CreateMedia) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public CreateMedia setQuotaUser(java.lang.String quotaUser) {
      return (CreateMedia) super.setQuotaUser(quotaUser);
    }

    @Override
    public CreateMedia setUserIp(java.lang.String userIp) {
      return (CreateMedia) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.String fileName;

    /**

     */
    public java.lang.String getFileName() {
      return fileName;
    }

    public CreateMedia setFileName(java.lang.String fileName) {
      this.fileName = fileName;
      return this;
    }

    @com.google.api.client.util.Key
    private java.lang.String gcsFilename;

    /**

     */
    public java.lang.String getGcsFilename() {
      return gcsFilename;
    }

    public CreateMedia setGcsFilename(java.lang.String gcsFilename) {
      this.gcsFilename = gcsFilename;
      return this;
    }

    @com.google.api.client.util.Key
    private java.lang.String mimeType;

    /**

     */
    public java.lang.String getMimeType() {
      return mimeType;
    }

    public CreateMedia setMimeType(java.lang.String mimeType) {
      this.mimeType = mimeType;
      return this;
    }

    @com.google.api.client.util.Key
    private java.lang.Long size;

    /**

     */
    public java.lang.Long getSize() {
      return size;
    }

    public CreateMedia setSize(java.lang.Long size) {
      this.size = size;
      return this;
    }

    @com.google.api.client.util.Key
    private java.lang.Double longitude;

    /**

     */
    public java.lang.Double getLongitude() {
      return longitude;
    }

    public CreateMedia setLongitude(java.lang.Double longitude) {
      this.longitude = longitude;
      return this;
    }

    @com.google.api.client.util.Key
    private java.lang.String comments;

    /**

     */
    public java.lang.String getComments() {
      return comments;
    }

    public CreateMedia setComments(java.lang.String comments) {
      this.comments = comments;
      return this;
    }

    @com.google.api.client.util.Key
    private java.lang.Double latitude;

    /**

     */
    public java.lang.Double getLatitude() {
      return latitude;
    }

    public CreateMedia setLatitude(java.lang.Double latitude) {
      this.latitude = latitude;
      return this;
    }

    @com.google.api.client.util.Key
    private java.lang.String sha256;

    /**

     */
    public java.lang.String getSha256() {
      return sha256;
    }

    public CreateMedia setSha256(java.lang.String sha256) {
      this.sha256 = sha256;
      return this;
    }

    @Override
    public CreateMedia set(String parameterName, Object value) {
      return (CreateMedia) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "deleteMessage".
   *
   * This request holds the parameters needed by the sync server.  After setting any optional
   * parameters, call the {@link DeleteMessage#execute()} method to invoke the remote operation.
   *
   * @param messageKey
   * @return the request
   */
  public DeleteMessage deleteMessage(java.lang.String messageKey) throws java.io.IOException {
    DeleteMessage result = new DeleteMessage(messageKey);
    initialize(result);
    return result;
  }

  public class DeleteMessage extends SyncRequest<com.appspot.cee_me.sync.model.Message> {

    private static final String REST_PATH = "message/{messageKey}";

    /**
     * Create a request for the method "deleteMessage".
     *
     * This request holds the parameters needed by the the sync server.  After setting any optional
     * parameters, call the {@link DeleteMessage#execute()} method to invoke the remote operation. <p>
     * {@link DeleteMessage#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientR
     * equest)} must be called to initialize this instance immediately after invoking the constructor.
     * </p>
     *
     * @param messageKey
     * @since 1.13
     */
    protected DeleteMessage(java.lang.String messageKey) {
      super(Sync.this, "POST", REST_PATH, null, com.appspot.cee_me.sync.model.Message.class);
      this.messageKey = com.google.api.client.util.Preconditions.checkNotNull(messageKey, "Required parameter messageKey must be specified.");
    }

    @Override
    public DeleteMessage setAlt(java.lang.String alt) {
      return (DeleteMessage) super.setAlt(alt);
    }

    @Override
    public DeleteMessage setFields(java.lang.String fields) {
      return (DeleteMessage) super.setFields(fields);
    }

    @Override
    public DeleteMessage setKey(java.lang.String key) {
      return (DeleteMessage) super.setKey(key);
    }

    @Override
    public DeleteMessage setOauthToken(java.lang.String oauthToken) {
      return (DeleteMessage) super.setOauthToken(oauthToken);
    }

    @Override
    public DeleteMessage setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (DeleteMessage) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public DeleteMessage setQuotaUser(java.lang.String quotaUser) {
      return (DeleteMessage) super.setQuotaUser(quotaUser);
    }

    @Override
    public DeleteMessage setUserIp(java.lang.String userIp) {
      return (DeleteMessage) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.String messageKey;

    /**

     */
    public java.lang.String getMessageKey() {
      return messageKey;
    }

    public DeleteMessage setMessageKey(java.lang.String messageKey) {
      this.messageKey = messageKey;
      return this;
    }

    @Override
    public DeleteMessage set(String parameterName, Object value) {
      return (DeleteMessage) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "echoMessage".
   *
   * This request holds the parameters needed by the sync server.  After setting any optional
   * parameters, call the {@link EchoMessage#execute()} method to invoke the remote operation.
   *
   * @param text
   * @return the request
   */
  public EchoMessage echoMessage(java.lang.String text) throws java.io.IOException {
    EchoMessage result = new EchoMessage(text);
    initialize(result);
    return result;
  }

  public class EchoMessage extends SyncRequest<com.appspot.cee_me.sync.model.EchoResult> {

    private static final String REST_PATH = "echoMessage/{text}";

    /**
     * Create a request for the method "echoMessage".
     *
     * This request holds the parameters needed by the the sync server.  After setting any optional
     * parameters, call the {@link EchoMessage#execute()} method to invoke the remote operation. <p>
     * {@link
     * EchoMessage#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)}
     * must be called to initialize this instance immediately after invoking the constructor. </p>
     *
     * @param text
     * @since 1.13
     */
    protected EchoMessage(java.lang.String text) {
      super(Sync.this, "GET", REST_PATH, null, com.appspot.cee_me.sync.model.EchoResult.class);
      this.text = com.google.api.client.util.Preconditions.checkNotNull(text, "Required parameter text must be specified.");
    }

    @Override
    public com.google.api.client.http.HttpResponse executeUsingHead() throws java.io.IOException {
      return super.executeUsingHead();
    }

    @Override
    public com.google.api.client.http.HttpRequest buildHttpRequestUsingHead() throws java.io.IOException {
      return super.buildHttpRequestUsingHead();
    }

    @Override
    public EchoMessage setAlt(java.lang.String alt) {
      return (EchoMessage) super.setAlt(alt);
    }

    @Override
    public EchoMessage setFields(java.lang.String fields) {
      return (EchoMessage) super.setFields(fields);
    }

    @Override
    public EchoMessage setKey(java.lang.String key) {
      return (EchoMessage) super.setKey(key);
    }

    @Override
    public EchoMessage setOauthToken(java.lang.String oauthToken) {
      return (EchoMessage) super.setOauthToken(oauthToken);
    }

    @Override
    public EchoMessage setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (EchoMessage) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public EchoMessage setQuotaUser(java.lang.String quotaUser) {
      return (EchoMessage) super.setQuotaUser(quotaUser);
    }

    @Override
    public EchoMessage setUserIp(java.lang.String userIp) {
      return (EchoMessage) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.String text;

    /**

     */
    public java.lang.String getText() {
      return text;
    }

    public EchoMessage setText(java.lang.String text) {
      this.text = text;
      return this;
    }

    @Override
    public EchoMessage set(String parameterName, Object value) {
      return (EchoMessage) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "getMessage".
   *
   * This request holds the parameters needed by the sync server.  After setting any optional
   * parameters, call the {@link GetMessage#execute()} method to invoke the remote operation.
   *
   * @param messageKey
   * @return the request
   */
  public GetMessage getMessage(java.lang.String messageKey) throws java.io.IOException {
    GetMessage result = new GetMessage(messageKey);
    initialize(result);
    return result;
  }

  public class GetMessage extends SyncRequest<com.appspot.cee_me.sync.model.Message> {

    private static final String REST_PATH = "message/{messageKey}";

    /**
     * Create a request for the method "getMessage".
     *
     * This request holds the parameters needed by the the sync server.  After setting any optional
     * parameters, call the {@link GetMessage#execute()} method to invoke the remote operation. <p>
     * {@link
     * GetMessage#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)}
     * must be called to initialize this instance immediately after invoking the constructor. </p>
     *
     * @param messageKey
     * @since 1.13
     */
    protected GetMessage(java.lang.String messageKey) {
      super(Sync.this, "GET", REST_PATH, null, com.appspot.cee_me.sync.model.Message.class);
      this.messageKey = com.google.api.client.util.Preconditions.checkNotNull(messageKey, "Required parameter messageKey must be specified.");
    }

    @Override
    public com.google.api.client.http.HttpResponse executeUsingHead() throws java.io.IOException {
      return super.executeUsingHead();
    }

    @Override
    public com.google.api.client.http.HttpRequest buildHttpRequestUsingHead() throws java.io.IOException {
      return super.buildHttpRequestUsingHead();
    }

    @Override
    public GetMessage setAlt(java.lang.String alt) {
      return (GetMessage) super.setAlt(alt);
    }

    @Override
    public GetMessage setFields(java.lang.String fields) {
      return (GetMessage) super.setFields(fields);
    }

    @Override
    public GetMessage setKey(java.lang.String key) {
      return (GetMessage) super.setKey(key);
    }

    @Override
    public GetMessage setOauthToken(java.lang.String oauthToken) {
      return (GetMessage) super.setOauthToken(oauthToken);
    }

    @Override
    public GetMessage setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (GetMessage) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public GetMessage setQuotaUser(java.lang.String quotaUser) {
      return (GetMessage) super.setQuotaUser(quotaUser);
    }

    @Override
    public GetMessage setUserIp(java.lang.String userIp) {
      return (GetMessage) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.String messageKey;

    /**

     */
    public java.lang.String getMessageKey() {
      return messageKey;
    }

    public GetMessage setMessageKey(java.lang.String messageKey) {
      this.messageKey = messageKey;
      return this;
    }

    @Override
    public GetMessage set(String parameterName, Object value) {
      return (GetMessage) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "getMessages".
   *
   * This request holds the parameters needed by the sync server.  After setting any optional
   * parameters, call the {@link GetMessages#execute()} method to invoke the remote operation.
   *
   * @param deviceKey
   * @return the request
   */
  public GetMessages getMessages(java.lang.String deviceKey) throws java.io.IOException {
    GetMessages result = new GetMessages(deviceKey);
    initialize(result);
    return result;
  }

  public class GetMessages extends SyncRequest<com.appspot.cee_me.sync.model.MessageQuery> {

    private static final String REST_PATH = "messagequery/{deviceKey}";

    /**
     * Create a request for the method "getMessages".
     *
     * This request holds the parameters needed by the the sync server.  After setting any optional
     * parameters, call the {@link GetMessages#execute()} method to invoke the remote operation. <p>
     * {@link
     * GetMessages#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)}
     * must be called to initialize this instance immediately after invoking the constructor. </p>
     *
     * @param deviceKey
     * @since 1.13
     */
    protected GetMessages(java.lang.String deviceKey) {
      super(Sync.this, "GET", REST_PATH, null, com.appspot.cee_me.sync.model.MessageQuery.class);
      this.deviceKey = com.google.api.client.util.Preconditions.checkNotNull(deviceKey, "Required parameter deviceKey must be specified.");
    }

    @Override
    public com.google.api.client.http.HttpResponse executeUsingHead() throws java.io.IOException {
      return super.executeUsingHead();
    }

    @Override
    public com.google.api.client.http.HttpRequest buildHttpRequestUsingHead() throws java.io.IOException {
      return super.buildHttpRequestUsingHead();
    }

    @Override
    public GetMessages setAlt(java.lang.String alt) {
      return (GetMessages) super.setAlt(alt);
    }

    @Override
    public GetMessages setFields(java.lang.String fields) {
      return (GetMessages) super.setFields(fields);
    }

    @Override
    public GetMessages setKey(java.lang.String key) {
      return (GetMessages) super.setKey(key);
    }

    @Override
    public GetMessages setOauthToken(java.lang.String oauthToken) {
      return (GetMessages) super.setOauthToken(oauthToken);
    }

    @Override
    public GetMessages setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (GetMessages) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public GetMessages setQuotaUser(java.lang.String quotaUser) {
      return (GetMessages) super.setQuotaUser(quotaUser);
    }

    @Override
    public GetMessages setUserIp(java.lang.String userIp) {
      return (GetMessages) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.String deviceKey;

    /**

     */
    public java.lang.String getDeviceKey() {
      return deviceKey;
    }

    public GetMessages setDeviceKey(java.lang.String deviceKey) {
      this.deviceKey = deviceKey;
      return this;
    }

    @com.google.api.client.util.Key
    private com.google.api.client.util.DateTime since;

    /**

     */
    public com.google.api.client.util.DateTime getSince() {
      return since;
    }

    public GetMessages setSince(com.google.api.client.util.DateTime since) {
      this.since = since;
      return this;
    }

    @com.google.api.client.util.Key
    private java.lang.Integer limit;

    /**

     */
    public java.lang.Integer getLimit() {
      return limit;
    }

    public GetMessages setLimit(java.lang.Integer limit) {
      this.limit = limit;
      return this;
    }

    @com.google.api.client.util.Key
    private java.lang.Integer offset;

    /**

     */
    public java.lang.Integer getOffset() {
      return offset;
    }

    public GetMessages setOffset(java.lang.Integer offset) {
      this.offset = offset;
      return this;
    }

    @Override
    public GetMessages set(String parameterName, Object value) {
      return (GetMessages) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "sendMessage".
   *
   * This request holds the parameters needed by the sync server.  After setting any optional
   * parameters, call the {@link SendMessage#execute()} method to invoke the remote operation.
   *
   * @param fromDevice
   * @param toDevice
   * @return the request
   */
  public SendMessage sendMessage(java.lang.String fromDevice, java.lang.String toDevice) throws java.io.IOException {
    SendMessage result = new SendMessage(fromDevice, toDevice);
    initialize(result);
    return result;
  }

  public class SendMessage extends SyncRequest<com.appspot.cee_me.sync.model.Message> {

    private static final String REST_PATH = "sendMessage/{fromDevice}/{toDevice}";

    /**
     * Create a request for the method "sendMessage".
     *
     * This request holds the parameters needed by the the sync server.  After setting any optional
     * parameters, call the {@link SendMessage#execute()} method to invoke the remote operation. <p>
     * {@link
     * SendMessage#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)}
     * must be called to initialize this instance immediately after invoking the constructor. </p>
     *
     * @param fromDevice
     * @param toDevice
     * @since 1.13
     */
    protected SendMessage(java.lang.String fromDevice, java.lang.String toDevice) {
      super(Sync.this, "POST", REST_PATH, null, com.appspot.cee_me.sync.model.Message.class);
      this.fromDevice = com.google.api.client.util.Preconditions.checkNotNull(fromDevice, "Required parameter fromDevice must be specified.");
      this.toDevice = com.google.api.client.util.Preconditions.checkNotNull(toDevice, "Required parameter toDevice must be specified.");
    }

    @Override
    public SendMessage setAlt(java.lang.String alt) {
      return (SendMessage) super.setAlt(alt);
    }

    @Override
    public SendMessage setFields(java.lang.String fields) {
      return (SendMessage) super.setFields(fields);
    }

    @Override
    public SendMessage setKey(java.lang.String key) {
      return (SendMessage) super.setKey(key);
    }

    @Override
    public SendMessage setOauthToken(java.lang.String oauthToken) {
      return (SendMessage) super.setOauthToken(oauthToken);
    }

    @Override
    public SendMessage setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (SendMessage) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public SendMessage setQuotaUser(java.lang.String quotaUser) {
      return (SendMessage) super.setQuotaUser(quotaUser);
    }

    @Override
    public SendMessage setUserIp(java.lang.String userIp) {
      return (SendMessage) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.String fromDevice;

    /**

     */
    public java.lang.String getFromDevice() {
      return fromDevice;
    }

    public SendMessage setFromDevice(java.lang.String fromDevice) {
      this.fromDevice = fromDevice;
      return this;
    }

    @com.google.api.client.util.Key
    private java.lang.String toDevice;

    /**

     */
    public java.lang.String getToDevice() {
      return toDevice;
    }

    public SendMessage setToDevice(java.lang.String toDevice) {
      this.toDevice = toDevice;
      return this;
    }

    @com.google.api.client.util.Key
    private java.lang.String urlData;

    /**

     */
    public java.lang.String getUrlData() {
      return urlData;
    }

    public SendMessage setUrlData(java.lang.String urlData) {
      this.urlData = urlData;
      return this;
    }

    @com.google.api.client.util.Key
    private java.lang.String mediaKey;

    /**

     */
    public java.lang.String getMediaKey() {
      return mediaKey;
    }

    public SendMessage setMediaKey(java.lang.String mediaKey) {
      this.mediaKey = mediaKey;
      return this;
    }

    @com.google.api.client.util.Key
    private java.lang.String text;

    /**

     */
    public java.lang.String getText() {
      return text;
    }

    public SendMessage setText(java.lang.String text) {
      this.text = text;
      return this;
    }

    @Override
    public SendMessage set(String parameterName, Object value) {
      return (SendMessage) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "setMessageAccepted".
   *
   * This request holds the parameters needed by the sync server.  After setting any optional
   * parameters, call the {@link SetMessageAccepted#execute()} method to invoke the remote operation.
   *
   * @param messageKey
   * @return the request
   */
  public SetMessageAccepted setMessageAccepted(java.lang.String messageKey) throws java.io.IOException {
    SetMessageAccepted result = new SetMessageAccepted(messageKey);
    initialize(result);
    return result;
  }

  public class SetMessageAccepted extends SyncRequest<com.appspot.cee_me.sync.model.Message> {

    private static final String REST_PATH = "setMessageAccepted/{messageKey}";

    /**
     * Create a request for the method "setMessageAccepted".
     *
     * This request holds the parameters needed by the the sync server.  After setting any optional
     * parameters, call the {@link SetMessageAccepted#execute()} method to invoke the remote
     * operation. <p> {@link SetMessageAccepted#initialize(com.google.api.client.googleapis.services.A
     * bstractGoogleClientRequest)} must be called to initialize this instance immediately after
     * invoking the constructor. </p>
     *
     * @param messageKey
     * @since 1.13
     */
    protected SetMessageAccepted(java.lang.String messageKey) {
      super(Sync.this, "POST", REST_PATH, null, com.appspot.cee_me.sync.model.Message.class);
      this.messageKey = com.google.api.client.util.Preconditions.checkNotNull(messageKey, "Required parameter messageKey must be specified.");
    }

    @Override
    public SetMessageAccepted setAlt(java.lang.String alt) {
      return (SetMessageAccepted) super.setAlt(alt);
    }

    @Override
    public SetMessageAccepted setFields(java.lang.String fields) {
      return (SetMessageAccepted) super.setFields(fields);
    }

    @Override
    public SetMessageAccepted setKey(java.lang.String key) {
      return (SetMessageAccepted) super.setKey(key);
    }

    @Override
    public SetMessageAccepted setOauthToken(java.lang.String oauthToken) {
      return (SetMessageAccepted) super.setOauthToken(oauthToken);
    }

    @Override
    public SetMessageAccepted setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (SetMessageAccepted) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public SetMessageAccepted setQuotaUser(java.lang.String quotaUser) {
      return (SetMessageAccepted) super.setQuotaUser(quotaUser);
    }

    @Override
    public SetMessageAccepted setUserIp(java.lang.String userIp) {
      return (SetMessageAccepted) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.String messageKey;

    /**

     */
    public java.lang.String getMessageKey() {
      return messageKey;
    }

    public SetMessageAccepted setMessageKey(java.lang.String messageKey) {
      this.messageKey = messageKey;
      return this;
    }

    @Override
    public SetMessageAccepted set(String parameterName, Object value) {
      return (SetMessageAccepted) super.set(parameterName, value);
    }
  }

  /**
   * Builder for {@link Sync}.
   *
   * <p>
   * Implementation is not thread-safe.
   * </p>
   *
   * @since 1.3.0
   */
  public static final class Builder extends com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient.Builder {

    /**
     * Returns an instance of a new builder.
     *
     * @param transport HTTP transport, which should normally be:
     *        <ul>
     *        <li>Google App Engine:
     *        {@code com.google.api.client.extensions.appengine.http.UrlFetchTransport}</li>
     *        <li>Android: {@code newCompatibleTransport} from
     *        {@code com.google.api.client.extensions.android.http.AndroidHttp}</li>
     *        <li>Java: {@link com.google.api.client.googleapis.javanet.GoogleNetHttpTransport#newTrustedTransport()}
     *        </li>
     *        </ul>
     * @param jsonFactory JSON factory, which may be:
     *        <ul>
     *        <li>Jackson: {@code com.google.api.client.json.jackson2.JacksonFactory}</li>
     *        <li>Google GSON: {@code com.google.api.client.json.gson.GsonFactory}</li>
     *        <li>Android Honeycomb or higher:
     *        {@code com.google.api.client.extensions.android.json.AndroidJsonFactory}</li>
     *        </ul>
     * @param httpRequestInitializer HTTP request initializer or {@code null} for none
     * @since 1.7
     */
    public Builder(com.google.api.client.http.HttpTransport transport, com.google.api.client.json.JsonFactory jsonFactory,
        com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
      super(
          transport,
          jsonFactory,
          DEFAULT_ROOT_URL,
          DEFAULT_SERVICE_PATH,
          httpRequestInitializer,
          false);
    }

    /** Builds a new instance of {@link Sync}. */
    @Override
    public Sync build() {
      return new Sync(this);
    }

    @Override
    public Builder setRootUrl(String rootUrl) {
      return (Builder) super.setRootUrl(rootUrl);
    }

    @Override
    public Builder setServicePath(String servicePath) {
      return (Builder) super.setServicePath(servicePath);
    }

    @Override
    public Builder setHttpRequestInitializer(com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
      return (Builder) super.setHttpRequestInitializer(httpRequestInitializer);
    }

    @Override
    public Builder setApplicationName(String applicationName) {
      return (Builder) super.setApplicationName(applicationName);
    }

    @Override
    public Builder setSuppressPatternChecks(boolean suppressPatternChecks) {
      return (Builder) super.setSuppressPatternChecks(suppressPatternChecks);
    }

    @Override
    public Builder setSuppressRequiredParameterChecks(boolean suppressRequiredParameterChecks) {
      return (Builder) super.setSuppressRequiredParameterChecks(suppressRequiredParameterChecks);
    }

    @Override
    public Builder setSuppressAllChecks(boolean suppressAllChecks) {
      return (Builder) super.setSuppressAllChecks(suppressAllChecks);
    }

    /**
     * Set the {@link SyncRequestInitializer}.
     *
     * @since 1.12
     */
    public Builder setSyncRequestInitializer(
        SyncRequestInitializer syncRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(syncRequestInitializer);
    }

    @Override
    public Builder setGoogleClientRequestInitializer(
        com.google.api.client.googleapis.services.GoogleClientRequestInitializer googleClientRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(googleClientRequestInitializer);
    }
  }
}
