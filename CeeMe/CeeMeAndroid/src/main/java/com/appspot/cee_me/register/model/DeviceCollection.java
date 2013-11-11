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
 * on 2013-11-11 at 01:09:21 UTC 
 * Modify at your own risk.
 */

package com.appspot.cee_me.register.model;

/**
 * Model definition for DeviceCollection.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the register. For a detailed explanation see:
 * <a href="http://code.google.com/p/google-http-java-client/wiki/JSON">http://code.google.com/p/google-http-java-client/wiki/JSON</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class DeviceCollection extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.util.List<Device> items;

  static {
    // hack to force ProGuard to consider Device used, since otherwise it would be stripped out
    // see http://code.google.com/p/google-api-java-client/issues/detail?id=528
    com.google.api.client.util.Data.nullOf(Device.class);
  }

  /**
   * @return value or {@code null} for none
   */
  public java.util.List<Device> getItems() {
    return items;
  }

  /**
   * @param items items or {@code null} for none
   */
  public DeviceCollection setItems(java.util.List<Device> items) {
    this.items = items;
    return this;
  }

  @Override
  public DeviceCollection set(String fieldName, Object value) {
    return (DeviceCollection) super.set(fieldName, value);
  }

  @Override
  public DeviceCollection clone() {
    return (DeviceCollection) super.clone();
  }

}