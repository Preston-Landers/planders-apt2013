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
 * on 2013-11-24 at 20:06:43 UTC 
 * Modify at your own risk.
 */

package com.appspot.cee_me.register;

/**
 * Service definition for Register (v1).
 *
 * <p>
 * Register devices on Cee.me (cee-me.appspot.com)
 * </p>
 *
 * <p>
 * For more information about this service, see the
 * <a href="" target="_blank">API Documentation</a>
 * </p>
 *
 * <p>
 * This service uses {@link RegisterRequestInitializer} to initialize global parameters via its
 * {@link Builder}.
 * </p>
 *
 * @since 1.3
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public class Register extends com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient {

  // Note: Leave this static initializer at the top of the file.
  static {
    com.google.api.client.util.Preconditions.checkState(
        com.google.api.client.googleapis.GoogleUtils.MAJOR_VERSION == 1 &&
        com.google.api.client.googleapis.GoogleUtils.MINOR_VERSION >= 15,
        "You are currently running with version %s of google-api-client. " +
        "You need at least version 1.15 of google-api-client to run version " +
        "1.17.0-rc of the register library.", com.google.api.client.googleapis.GoogleUtils.VERSION);
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
  public static final String DEFAULT_SERVICE_PATH = "register/v1/";

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
  public Register(com.google.api.client.http.HttpTransport transport, com.google.api.client.json.JsonFactory jsonFactory,
      com.google.api.client.http.HttpRequestInitializer httpRequestInitializer) {
    this(new Builder(transport, jsonFactory, httpRequestInitializer));
  }

  /**
   * @param builder builder
   */
  Register(Builder builder) {
    super(builder);
  }

  @Override
  protected void initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest<?> httpClientRequest) throws java.io.IOException {
    super.initialize(httpClientRequest);
  }

  /**
   * Create a request for the method "deleteRegistration".
   *
   * This request holds the parameters needed by the register server.  After setting any optional
   * parameters, call the {@link DeleteRegistration#execute()} method to invoke the remote operation.
   *
   * @param deviceKey
   * @return the request
   */
  public DeleteRegistration deleteRegistration(java.lang.String deviceKey) throws java.io.IOException {
    DeleteRegistration result = new DeleteRegistration(deviceKey);
    initialize(result);
    return result;
  }

  public class DeleteRegistration extends RegisterRequest<Void> {

    private static final String REST_PATH = "registration/{deviceKey}";

    /**
     * Create a request for the method "deleteRegistration".
     *
     * This request holds the parameters needed by the the register server.  After setting any
     * optional parameters, call the {@link DeleteRegistration#execute()} method to invoke the remote
     * operation. <p> {@link DeleteRegistration#initialize(com.google.api.client.googleapis.services.A
     * bstractGoogleClientRequest)} must be called to initialize this instance immediately after
     * invoking the constructor. </p>
     *
     * @param deviceKey
     * @since 1.13
     */
    protected DeleteRegistration(java.lang.String deviceKey) {
      super(Register.this, "POST", REST_PATH, null, Void.class);
      this.deviceKey = com.google.api.client.util.Preconditions.checkNotNull(deviceKey, "Required parameter deviceKey must be specified.");
    }

    @Override
    public DeleteRegistration setAlt(java.lang.String alt) {
      return (DeleteRegistration) super.setAlt(alt);
    }

    @Override
    public DeleteRegistration setFields(java.lang.String fields) {
      return (DeleteRegistration) super.setFields(fields);
    }

    @Override
    public DeleteRegistration setKey(java.lang.String key) {
      return (DeleteRegistration) super.setKey(key);
    }

    @Override
    public DeleteRegistration setOauthToken(java.lang.String oauthToken) {
      return (DeleteRegistration) super.setOauthToken(oauthToken);
    }

    @Override
    public DeleteRegistration setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (DeleteRegistration) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public DeleteRegistration setQuotaUser(java.lang.String quotaUser) {
      return (DeleteRegistration) super.setQuotaUser(quotaUser);
    }

    @Override
    public DeleteRegistration setUserIp(java.lang.String userIp) {
      return (DeleteRegistration) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.String deviceKey;

    /**

     */
    public java.lang.String getDeviceKey() {
      return deviceKey;
    }

    public DeleteRegistration setDeviceKey(java.lang.String deviceKey) {
      this.deviceKey = deviceKey;
      return this;
    }

    @Override
    public DeleteRegistration set(String parameterName, Object value) {
      return (DeleteRegistration) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "getDevice".
   *
   * This request holds the parameters needed by the register server.  After setting any optional
   * parameters, call the {@link GetDevice#execute()} method to invoke the remote operation.
   *
   * @param deviceKey
   * @return the request
   */
  public GetDevice getDevice(java.lang.String deviceKey) throws java.io.IOException {
    GetDevice result = new GetDevice(deviceKey);
    initialize(result);
    return result;
  }

  public class GetDevice extends RegisterRequest<com.appspot.cee_me.register.model.Device> {

    private static final String REST_PATH = "device/{deviceKey}";

    /**
     * Create a request for the method "getDevice".
     *
     * This request holds the parameters needed by the the register server.  After setting any
     * optional parameters, call the {@link GetDevice#execute()} method to invoke the remote
     * operation. <p> {@link
     * GetDevice#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)}
     * must be called to initialize this instance immediately after invoking the constructor. </p>
     *
     * @param deviceKey
     * @since 1.13
     */
    protected GetDevice(java.lang.String deviceKey) {
      super(Register.this, "GET", REST_PATH, null, com.appspot.cee_me.register.model.Device.class);
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
    public GetDevice setAlt(java.lang.String alt) {
      return (GetDevice) super.setAlt(alt);
    }

    @Override
    public GetDevice setFields(java.lang.String fields) {
      return (GetDevice) super.setFields(fields);
    }

    @Override
    public GetDevice setKey(java.lang.String key) {
      return (GetDevice) super.setKey(key);
    }

    @Override
    public GetDevice setOauthToken(java.lang.String oauthToken) {
      return (GetDevice) super.setOauthToken(oauthToken);
    }

    @Override
    public GetDevice setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (GetDevice) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public GetDevice setQuotaUser(java.lang.String quotaUser) {
      return (GetDevice) super.setQuotaUser(quotaUser);
    }

    @Override
    public GetDevice setUserIp(java.lang.String userIp) {
      return (GetDevice) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.String deviceKey;

    /**

     */
    public java.lang.String getDeviceKey() {
      return deviceKey;
    }

    public GetDevice setDeviceKey(java.lang.String deviceKey) {
      this.deviceKey = deviceKey;
      return this;
    }

    @Override
    public GetDevice set(String parameterName, Object value) {
      return (GetDevice) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "getDeviceDirectory".
   *
   * This request holds the parameters needed by the register server.  After setting any optional
   * parameters, call the {@link GetDeviceDirectory#execute()} method to invoke the remote operation.
   *
   * @param limit
   * @param offset
   * @return the request
   */
  public GetDeviceDirectory getDeviceDirectory(java.lang.Integer limit, java.lang.Integer offset) throws java.io.IOException {
    GetDeviceDirectory result = new GetDeviceDirectory(limit, offset);
    initialize(result);
    return result;
  }

  public class GetDeviceDirectory extends RegisterRequest<com.appspot.cee_me.register.model.DeviceCollection> {

    private static final String REST_PATH = "devicecollection/{limit}/{offset}";

    /**
     * Create a request for the method "getDeviceDirectory".
     *
     * This request holds the parameters needed by the the register server.  After setting any
     * optional parameters, call the {@link GetDeviceDirectory#execute()} method to invoke the remote
     * operation. <p> {@link GetDeviceDirectory#initialize(com.google.api.client.googleapis.services.A
     * bstractGoogleClientRequest)} must be called to initialize this instance immediately after
     * invoking the constructor. </p>
     *
     * @param limit
     * @param offset
     * @since 1.13
     */
    protected GetDeviceDirectory(java.lang.Integer limit, java.lang.Integer offset) {
      super(Register.this, "GET", REST_PATH, null, com.appspot.cee_me.register.model.DeviceCollection.class);
      this.limit = com.google.api.client.util.Preconditions.checkNotNull(limit, "Required parameter limit must be specified.");
      this.offset = com.google.api.client.util.Preconditions.checkNotNull(offset, "Required parameter offset must be specified.");
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
    public GetDeviceDirectory setAlt(java.lang.String alt) {
      return (GetDeviceDirectory) super.setAlt(alt);
    }

    @Override
    public GetDeviceDirectory setFields(java.lang.String fields) {
      return (GetDeviceDirectory) super.setFields(fields);
    }

    @Override
    public GetDeviceDirectory setKey(java.lang.String key) {
      return (GetDeviceDirectory) super.setKey(key);
    }

    @Override
    public GetDeviceDirectory setOauthToken(java.lang.String oauthToken) {
      return (GetDeviceDirectory) super.setOauthToken(oauthToken);
    }

    @Override
    public GetDeviceDirectory setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (GetDeviceDirectory) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public GetDeviceDirectory setQuotaUser(java.lang.String quotaUser) {
      return (GetDeviceDirectory) super.setQuotaUser(quotaUser);
    }

    @Override
    public GetDeviceDirectory setUserIp(java.lang.String userIp) {
      return (GetDeviceDirectory) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.Integer limit;

    /**

     */
    public java.lang.Integer getLimit() {
      return limit;
    }

    public GetDeviceDirectory setLimit(java.lang.Integer limit) {
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

    public GetDeviceDirectory setOffset(java.lang.Integer offset) {
      this.offset = offset;
      return this;
    }

    @com.google.api.client.util.Key
    private java.lang.Double lat;

    /**

     */
    public java.lang.Double getLat() {
      return lat;
    }

    public GetDeviceDirectory setLat(java.lang.Double lat) {
      this.lat = lat;
      return this;
    }

    @com.google.api.client.util.Key
    private java.lang.String query;

    /**

     */
    public java.lang.String getQuery() {
      return query;
    }

    public GetDeviceDirectory setQuery(java.lang.String query) {
      this.query = query;
      return this;
    }

    @com.google.api.client.util.Key("long")
    private java.lang.Double registerLong;

    /**

     */
    public java.lang.Double getLong() {
      return registerLong;
    }

    public GetDeviceDirectory setLong(java.lang.Double registerLong) {
      this.registerLong = registerLong;
      return this;
    }

    @Override
    public GetDeviceDirectory set(String parameterName, Object value) {
      return (GetDeviceDirectory) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "listMyDevices".
   *
   * This request holds the parameters needed by the register server.  After setting any optional
   * parameters, call the {@link ListMyDevices#execute()} method to invoke the remote operation.
   *
   * @return the request
   */
  public ListMyDevices listMyDevices() throws java.io.IOException {
    ListMyDevices result = new ListMyDevices();
    initialize(result);
    return result;
  }

  public class ListMyDevices extends RegisterRequest<com.appspot.cee_me.register.model.DeviceCollection> {

    private static final String REST_PATH = "device";

    /**
     * Create a request for the method "listMyDevices".
     *
     * This request holds the parameters needed by the the register server.  After setting any
     * optional parameters, call the {@link ListMyDevices#execute()} method to invoke the remote
     * operation. <p> {@link ListMyDevices#initialize(com.google.api.client.googleapis.services.Abstra
     * ctGoogleClientRequest)} must be called to initialize this instance immediately after invoking
     * the constructor. </p>
     *
     * @since 1.13
     */
    protected ListMyDevices() {
      super(Register.this, "GET", REST_PATH, null, com.appspot.cee_me.register.model.DeviceCollection.class);
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
    public ListMyDevices setAlt(java.lang.String alt) {
      return (ListMyDevices) super.setAlt(alt);
    }

    @Override
    public ListMyDevices setFields(java.lang.String fields) {
      return (ListMyDevices) super.setFields(fields);
    }

    @Override
    public ListMyDevices setKey(java.lang.String key) {
      return (ListMyDevices) super.setKey(key);
    }

    @Override
    public ListMyDevices setOauthToken(java.lang.String oauthToken) {
      return (ListMyDevices) super.setOauthToken(oauthToken);
    }

    @Override
    public ListMyDevices setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (ListMyDevices) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public ListMyDevices setQuotaUser(java.lang.String quotaUser) {
      return (ListMyDevices) super.setQuotaUser(quotaUser);
    }

    @Override
    public ListMyDevices setUserIp(java.lang.String userIp) {
      return (ListMyDevices) super.setUserIp(userIp);
    }

    @Override
    public ListMyDevices set(String parameterName, Object value) {
      return (ListMyDevices) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "registerDevice".
   *
   * This request holds the parameters needed by the register server.  After setting any optional
   * parameters, call the {@link RegisterDevice#execute()} method to invoke the remote operation.
   *
   * @param name
   * @param hardwareDescription
   * @param gcmRegistrationId
   * @return the request
   */
  public RegisterDevice registerDevice(java.lang.String name, java.lang.String hardwareDescription, java.lang.String gcmRegistrationId) throws java.io.IOException {
    RegisterDevice result = new RegisterDevice(name, hardwareDescription, gcmRegistrationId);
    initialize(result);
    return result;
  }

  public class RegisterDevice extends RegisterRequest<com.appspot.cee_me.register.model.Device> {

    private static final String REST_PATH = "registerDevice/{name}/{hardwareDescription}/{gcmRegistrationId}";

    /**
     * Create a request for the method "registerDevice".
     *
     * This request holds the parameters needed by the the register server.  After setting any
     * optional parameters, call the {@link RegisterDevice#execute()} method to invoke the remote
     * operation. <p> {@link RegisterDevice#initialize(com.google.api.client.googleapis.services.Abstr
     * actGoogleClientRequest)} must be called to initialize this instance immediately after invoking
     * the constructor. </p>
     *
     * @param name
     * @param hardwareDescription
     * @param gcmRegistrationId
     * @since 1.13
     */
    protected RegisterDevice(java.lang.String name, java.lang.String hardwareDescription, java.lang.String gcmRegistrationId) {
      super(Register.this, "POST", REST_PATH, null, com.appspot.cee_me.register.model.Device.class);
      this.name = com.google.api.client.util.Preconditions.checkNotNull(name, "Required parameter name must be specified.");
      this.hardwareDescription = com.google.api.client.util.Preconditions.checkNotNull(hardwareDescription, "Required parameter hardwareDescription must be specified.");
      this.gcmRegistrationId = com.google.api.client.util.Preconditions.checkNotNull(gcmRegistrationId, "Required parameter gcmRegistrationId must be specified.");
    }

    @Override
    public RegisterDevice setAlt(java.lang.String alt) {
      return (RegisterDevice) super.setAlt(alt);
    }

    @Override
    public RegisterDevice setFields(java.lang.String fields) {
      return (RegisterDevice) super.setFields(fields);
    }

    @Override
    public RegisterDevice setKey(java.lang.String key) {
      return (RegisterDevice) super.setKey(key);
    }

    @Override
    public RegisterDevice setOauthToken(java.lang.String oauthToken) {
      return (RegisterDevice) super.setOauthToken(oauthToken);
    }

    @Override
    public RegisterDevice setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (RegisterDevice) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public RegisterDevice setQuotaUser(java.lang.String quotaUser) {
      return (RegisterDevice) super.setQuotaUser(quotaUser);
    }

    @Override
    public RegisterDevice setUserIp(java.lang.String userIp) {
      return (RegisterDevice) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.String name;

    /**

     */
    public java.lang.String getName() {
      return name;
    }

    public RegisterDevice setName(java.lang.String name) {
      this.name = name;
      return this;
    }

    @com.google.api.client.util.Key
    private java.lang.String hardwareDescription;

    /**

     */
    public java.lang.String getHardwareDescription() {
      return hardwareDescription;
    }

    public RegisterDevice setHardwareDescription(java.lang.String hardwareDescription) {
      this.hardwareDescription = hardwareDescription;
      return this;
    }

    @com.google.api.client.util.Key
    private java.lang.String gcmRegistrationId;

    /**

     */
    public java.lang.String getGcmRegistrationId() {
      return gcmRegistrationId;
    }

    public RegisterDevice setGcmRegistrationId(java.lang.String gcmRegistrationId) {
      this.gcmRegistrationId = gcmRegistrationId;
      return this;
    }

    @com.google.api.client.util.Key
    private java.lang.String comment;

    /**

     */
    public java.lang.String getComment() {
      return comment;
    }

    public RegisterDevice setComment(java.lang.String comment) {
      this.comment = comment;
      return this;
    }

    @Override
    public RegisterDevice set(String parameterName, Object value) {
      return (RegisterDevice) super.set(parameterName, value);
    }
  }

  /**
   * Create a request for the method "updateDevice".
   *
   * This request holds the parameters needed by the register server.  After setting any optional
   * parameters, call the {@link UpdateDevice#execute()} method to invoke the remote operation.
   *
   * @param deviceKey
   * @return the request
   */
  public UpdateDevice updateDevice(java.lang.String deviceKey) throws java.io.IOException {
    UpdateDevice result = new UpdateDevice(deviceKey);
    initialize(result);
    return result;
  }

  public class UpdateDevice extends RegisterRequest<com.appspot.cee_me.register.model.Device> {

    private static final String REST_PATH = "device/{deviceKey}";

    /**
     * Create a request for the method "updateDevice".
     *
     * This request holds the parameters needed by the the register server.  After setting any
     * optional parameters, call the {@link UpdateDevice#execute()} method to invoke the remote
     * operation. <p> {@link
     * UpdateDevice#initialize(com.google.api.client.googleapis.services.AbstractGoogleClientRequest)}
     * must be called to initialize this instance immediately after invoking the constructor. </p>
     *
     * @param deviceKey
     * @since 1.13
     */
    protected UpdateDevice(java.lang.String deviceKey) {
      super(Register.this, "POST", REST_PATH, null, com.appspot.cee_me.register.model.Device.class);
      this.deviceKey = com.google.api.client.util.Preconditions.checkNotNull(deviceKey, "Required parameter deviceKey must be specified.");
    }

    @Override
    public UpdateDevice setAlt(java.lang.String alt) {
      return (UpdateDevice) super.setAlt(alt);
    }

    @Override
    public UpdateDevice setFields(java.lang.String fields) {
      return (UpdateDevice) super.setFields(fields);
    }

    @Override
    public UpdateDevice setKey(java.lang.String key) {
      return (UpdateDevice) super.setKey(key);
    }

    @Override
    public UpdateDevice setOauthToken(java.lang.String oauthToken) {
      return (UpdateDevice) super.setOauthToken(oauthToken);
    }

    @Override
    public UpdateDevice setPrettyPrint(java.lang.Boolean prettyPrint) {
      return (UpdateDevice) super.setPrettyPrint(prettyPrint);
    }

    @Override
    public UpdateDevice setQuotaUser(java.lang.String quotaUser) {
      return (UpdateDevice) super.setQuotaUser(quotaUser);
    }

    @Override
    public UpdateDevice setUserIp(java.lang.String userIp) {
      return (UpdateDevice) super.setUserIp(userIp);
    }

    @com.google.api.client.util.Key
    private java.lang.String deviceKey;

    /**

     */
    public java.lang.String getDeviceKey() {
      return deviceKey;
    }

    public UpdateDevice setDeviceKey(java.lang.String deviceKey) {
      this.deviceKey = deviceKey;
      return this;
    }

    @com.google.api.client.util.Key
    private java.lang.String comment;

    /**

     */
    public java.lang.String getComment() {
      return comment;
    }

    public UpdateDevice setComment(java.lang.String comment) {
      this.comment = comment;
      return this;
    }

    @com.google.api.client.util.Key
    private java.lang.String hardwareDescription;

    /**

     */
    public java.lang.String getHardwareDescription() {
      return hardwareDescription;
    }

    public UpdateDevice setHardwareDescription(java.lang.String hardwareDescription) {
      this.hardwareDescription = hardwareDescription;
      return this;
    }

    @com.google.api.client.util.Key
    private java.lang.String gcmRegistrationId;

    /**

     */
    public java.lang.String getGcmRegistrationId() {
      return gcmRegistrationId;
    }

    public UpdateDevice setGcmRegistrationId(java.lang.String gcmRegistrationId) {
      this.gcmRegistrationId = gcmRegistrationId;
      return this;
    }

    @com.google.api.client.util.Key
    private java.lang.String name;

    /**

     */
    public java.lang.String getName() {
      return name;
    }

    public UpdateDevice setName(java.lang.String name) {
      this.name = name;
      return this;
    }

    @Override
    public UpdateDevice set(String parameterName, Object value) {
      return (UpdateDevice) super.set(parameterName, value);
    }
  }

  /**
   * Builder for {@link Register}.
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

    /** Builds a new instance of {@link Register}. */
    @Override
    public Register build() {
      return new Register(this);
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
     * Set the {@link RegisterRequestInitializer}.
     *
     * @since 1.12
     */
    public Builder setRegisterRequestInitializer(
        RegisterRequestInitializer registerRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(registerRequestInitializer);
    }

    @Override
    public Builder setGoogleClientRequestInitializer(
        com.google.api.client.googleapis.services.GoogleClientRequestInitializer googleClientRequestInitializer) {
      return (Builder) super.setGoogleClientRequestInitializer(googleClientRequestInitializer);
    }
  }
}
