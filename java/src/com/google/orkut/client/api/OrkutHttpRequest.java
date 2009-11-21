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
   * 
   * @return
   */
  public Collection getParameters() {
    return params;
  }

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
