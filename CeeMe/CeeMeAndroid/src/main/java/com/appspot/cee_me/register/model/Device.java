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
 * on 2013-11-30 at 01:29:51 UTC 
 * Modify at your own risk.
 */

package com.appspot.cee_me.register.model;

/**
 * Model definition for Device.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the register. For a detailed explanation see:
 * <a href="http://code.google.com/p/google-http-java-client/wiki/JSON">http://code.google.com/p/google-http-java-client/wiki/JSON</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class Device extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String comment;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private DateTime creationDate;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String deviceKey;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String gcmRegistrationId;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String hardwareDescription;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private DateTime lastIncomingMessageDate;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private DateTime lastOutgoingMessageDate;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String name;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String ownerAccountName;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String ownerKey;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String publicId;

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getComment() {
    return comment;
  }

  /**
   * @param comment comment or {@code null} for none
   */
  public Device setComment(java.lang.String comment) {
    this.comment = comment;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public DateTime getCreationDate() {
    return creationDate;
  }

  /**
   * @param creationDate creationDate or {@code null} for none
   */
  public Device setCreationDate(DateTime creationDate) {
    this.creationDate = creationDate;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getDeviceKey() {
    return deviceKey;
  }

  /**
   * @param deviceKey deviceKey or {@code null} for none
   */
  public Device setDeviceKey(java.lang.String deviceKey) {
    this.deviceKey = deviceKey;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getGcmRegistrationId() {
    return gcmRegistrationId;
  }

  /**
   * @param gcmRegistrationId gcmRegistrationId or {@code null} for none
   */
  public Device setGcmRegistrationId(java.lang.String gcmRegistrationId) {
    this.gcmRegistrationId = gcmRegistrationId;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getHardwareDescription() {
    return hardwareDescription;
  }

  /**
   * @param hardwareDescription hardwareDescription or {@code null} for none
   */
  public Device setHardwareDescription(java.lang.String hardwareDescription) {
    this.hardwareDescription = hardwareDescription;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public DateTime getLastIncomingMessageDate() {
    return lastIncomingMessageDate;
  }

  /**
   * @param lastIncomingMessageDate lastIncomingMessageDate or {@code null} for none
   */
  public Device setLastIncomingMessageDate(DateTime lastIncomingMessageDate) {
    this.lastIncomingMessageDate = lastIncomingMessageDate;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public DateTime getLastOutgoingMessageDate() {
    return lastOutgoingMessageDate;
  }

  /**
   * @param lastOutgoingMessageDate lastOutgoingMessageDate or {@code null} for none
   */
  public Device setLastOutgoingMessageDate(DateTime lastOutgoingMessageDate) {
    this.lastOutgoingMessageDate = lastOutgoingMessageDate;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getName() {
    return name;
  }

  /**
   * @param name name or {@code null} for none
   */
  public Device setName(java.lang.String name) {
    this.name = name;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getOwnerAccountName() {
    return ownerAccountName;
  }

  /**
   * @param ownerAccountName ownerAccountName or {@code null} for none
   */
  public Device setOwnerAccountName(java.lang.String ownerAccountName) {
    this.ownerAccountName = ownerAccountName;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getOwnerKey() {
    return ownerKey;
  }

  /**
   * @param ownerKey ownerKey or {@code null} for none
   */
  public Device setOwnerKey(java.lang.String ownerKey) {
    this.ownerKey = ownerKey;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getPublicId() {
    return publicId;
  }

  /**
   * @param publicId publicId or {@code null} for none
   */
  public Device setPublicId(java.lang.String publicId) {
    this.publicId = publicId;
    return this;
  }

  @Override
  public Device set(String fieldName, Object value) {
    return (Device) super.set(fieldName, value);
  }

  @Override
  public Device clone() {
    return (Device) super.clone();
  }

}
