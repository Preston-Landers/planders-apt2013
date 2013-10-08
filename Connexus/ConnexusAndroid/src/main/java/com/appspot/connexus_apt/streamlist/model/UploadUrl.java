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
 * (build: 2013-09-16 16:01:30 UTC)
 * on 2013-10-08 at 01:40:14 UTC 
 * Modify at your own risk.
 */

package com.appspot.connexus_apt.streamlist.model;

/**
 * Model definition for UploadUrl.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the streamlist. For a detailed explanation see:
 * <a href="http://code.google.com/p/google-http-java-client/wiki/JSON">http://code.google.com/p/google-http-java-client/wiki/JSON</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class UploadUrl extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @com.google.api.client.json.JsonString
  private java.lang.Long streamId;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @com.google.api.client.json.JsonString
  private java.lang.Long streamOwnerId;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String uploadUrl;

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Long getStreamId() {
    return streamId;
  }

  /**
   * @param streamId streamId or {@code null} for none
   */
  public UploadUrl setStreamId(java.lang.Long streamId) {
    this.streamId = streamId;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Long getStreamOwnerId() {
    return streamOwnerId;
  }

  /**
   * @param streamOwnerId streamOwnerId or {@code null} for none
   */
  public UploadUrl setStreamOwnerId(java.lang.Long streamOwnerId) {
    this.streamOwnerId = streamOwnerId;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getUploadUrl() {
    return uploadUrl;
  }

  /**
   * @param uploadUrl uploadUrl or {@code null} for none
   */
  public UploadUrl setUploadUrl(java.lang.String uploadUrl) {
    this.uploadUrl = uploadUrl;
    return this;
  }

  @Override
  public UploadUrl set(String fieldName, Object value) {
    return (UploadUrl) super.set(fieldName, value);
  }

  @Override
  public UploadUrl clone() {
    return (UploadUrl) super.clone();
  }

}
