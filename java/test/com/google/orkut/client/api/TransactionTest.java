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

import junit.framework.TestCase;

import org.json.me.JSONObject;

/**
 * @author Sachin Shenoy
 */
public class TransactionTest extends TestCase {

  private static final String METHOD = "transaction.method";

  private Transaction transaction;
  private JSONObject data;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    transaction = new Transaction(METHOD) {
      @Override
      public void setResponseData(JSONObject d) {
        data = d;
      }
    };
  }

  public void testSetResponse_responseWithResult() throws Exception {
    transaction.setResponse(new JSONObject("{'result':{'age':10}}"));
    assertNotNull(data);
    assertEquals(10, data.optInt("age"));
    assertFalse(transaction.hasError());
  }

  public void testSetResponse_responseWithResultInData() throws Exception {
    transaction.setResponse(new JSONObject("{'data':{'age':10}}"));
    assertNotNull(data);
    assertEquals(10, data.optInt("age"));
    assertFalse(transaction.hasError());
  }

  public void testSetResponse_responseWithError() throws Exception {
    String errorResponse = "{'error':{'message':'detailed message'," +
    "'data':{'errorType':'tosNotAccepted'},'code':401}}";
    transaction.setResponse(new JSONObject(errorResponse));
    assertNull(data);
    assertTrue(transaction.hasError());
    OrkutError error = transaction.getError();
    assertEquals("detailed message", error.getMessage());
    assertTrue(error.isTosNotAcceptedByUser());
    assertTrue(error.isClientError());
  }

  public void testSetResponse_responseBothDataAndResultAreMissing() throws Exception {
    try {
      transaction.setResponse(new JSONObject("{}"));
      fail();
    } catch (RuntimeException re) {
      // we expect this.
    }
    assertNull(data);
  }
}