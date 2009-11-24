/* EXPERIMENTAL (really) */
/* Copyright (c) 2009 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.orkut.client.transport;

import java.util.ArrayList;
import java.util.Collection;

/**
 * An model implementation of {@link HttpRequest}.
 *
 * @author Sachin Shenoy
 */
public class OrkutHttpRequest implements HttpRequest {

  private final byte[] body;
  private final String contentType;
  private final ArrayList params;
  private final ArrayList headers;

  public OrkutHttpRequest(byte[] body, String contentType) {
    this.body = body;
    this.contentType = contentType;
    this.params = new ArrayList();
    this.headers = new ArrayList();
  }

  /* (non-Javadoc)
   * @see com.google.orkut.client.transport.HttpRequest#getContentType()
   */
  public String getContentType() {
    return contentType;
  }

  /* (non-Javadoc)
   * @see com.google.orkut.client.transport.HttpRequest#getRequestBody()
   */
  public byte[] getRequestBody() {
    return body;
  }

  /* (non-Javadoc)
   * @see com.google.orkut.client.transport.HttpRequest#getParameters()
   */
  public Collection getParameters() {
    return params;
  }

  /* (non-Javadoc)
   * @see com.google.orkut.client.transport.HttpRequest#getHeaders()
   */
  public Collection getHeaders() {
    return headers;
  }

  public HttpRequest addParam(String key, String value) {
    params.add(new Parameter(key, value));
    return this;
  }

  public HttpRequest addHeader(String name, String value) {
    headers.add(new Header(name, value));
    return this;
  }
}
