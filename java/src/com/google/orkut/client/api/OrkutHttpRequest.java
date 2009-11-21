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

package com.google.orkut.client.api;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents an HTTP request to be sent on the wire. This class is created
 * using {@link BatchTransaction#build()}. Clients are supposed to form an
 * HTTP request using the values returned by this class, add authentication to
 * it and send to orkut servers.
 * 
 * Look at {@link com.google.orkut.client.sample.Transport} for example.
 * 
 * @author Sachin Shenoy
 */
public class OrkutHttpRequest {

  private final byte[] body;
  private final String contentType;
  private ArrayList params;
  private ArrayList headers;

  public class Parameter {
    String key;
    String value;
    
    Parameter(String key, String value) {
      this.key = key;
      this.value = value;
    }
    
    public String getKey() {
      return key;
    }
    
    public String getValue() {
      return value;
    }
  }
  
  public class Header {
    String name;
    String value;
    
    Header(String name, String value) {
      this.name = name;
      this.value = value;
    }
    
    public String getName() {
      return name;
    }
    
    public String getValue() {
      return value;
    }
  }
  
  OrkutHttpRequest(byte[] body, String contentType) {
    this.body = body;
    this.contentType = contentType;
    this.params = new ArrayList();
    this.headers = new ArrayList();
  }
  
  /**
   * Returns the content type header value
   */
  public String getContentType() {
    return contentType;
  }

  /**
   * Returns the body of the request
   */
  public byte[] getRequestBody() {
    return body;
  }

  /**
   * Returns Collection of {@link Parameter} to be sent with the HTTP request.
   */
  public Collection getParameters() {
    return params;
  }

  /**
   * Returns Colleciton of {@link Header} to be sent with the HTTP request.
   */
  public Collection getHeaders() {
    return headers;
  }
  
  OrkutHttpRequest addParam(String key, String value) {
    params.add(new Parameter(key, value));
    return this;
  }
  
  OrkutHttpRequest addHeader(String name, String value) {
    headers.add(new Header(name, value));
    return this;
  }
}
