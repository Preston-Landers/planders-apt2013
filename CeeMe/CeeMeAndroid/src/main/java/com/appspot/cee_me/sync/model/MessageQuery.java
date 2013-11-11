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
 * (build: 2013-10-30 15:57:41 UTC)
 * on 2013-11-11 at 01:09:28 UTC 
 * Modify at your own risk.
 */

package com.appspot.cee_me.sync.model;

/**
 * Model definition for MessageQuery.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the sync. For a detailed explanation see:
 * <a href="http://code.google.com/p/google-http-java-client/wiki/JSON">http://code.google.com/p/google-http-java-client/wiki/JSON</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class MessageQuery extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Integer limit;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.util.List<Message> messageList;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Integer offset;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private DateTime sinceDateTime;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private Device toDevice;

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Integer getLimit() {
    return limit;
  }

  /**
   * @param limit limit or {@code null} for none
   */
  public MessageQuery setLimit(java.lang.Integer limit) {
    this.limit = limit;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.util.List<Message> getMessageList() {
    return messageList;
  }

  /**
   * @param messageList messageList or {@code null} for none
   */
  public MessageQuery setMessageList(java.util.List<Message> messageList) {
    this.messageList = messageList;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Integer getOffset() {
    return offset;
  }

  /**
   * @param offset offset or {@code null} for none
   */
  public MessageQuery setOffset(java.lang.Integer offset) {
    this.offset = offset;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public DateTime getSinceDateTime() {
    return sinceDateTime;
  }

  /**
   * @param sinceDateTime sinceDateTime or {@code null} for none
   */
  public MessageQuery setSinceDateTime(DateTime sinceDateTime) {
    this.sinceDateTime = sinceDateTime;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public Device getToDevice() {
    return toDevice;
  }

  /**
   * @param toDevice toDevice or {@code null} for none
   */
  public MessageQuery setToDevice(Device toDevice) {
    this.toDevice = toDevice;
    return this;
  }

  @Override
  public MessageQuery set(String fieldName, Object value) {
    return (MessageQuery) super.set(fieldName, value);
  }

  @Override
  public MessageQuery clone() {
    return (MessageQuery) super.clone();
  }

}
