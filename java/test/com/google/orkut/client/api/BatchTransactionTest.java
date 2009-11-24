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

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;

import com.google.orkut.client.config.SandboxConfigFactory;
import com.google.orkut.client.transport.HttpRequest;
import com.google.orkut.client.transport.OrkutHttpRequestFactory;
import com.google.orkut.client.transport.HttpRequest.Header;

import org.easymock.Capture;
import org.easymock.classextension.EasyMock;
import org.easymock.classextension.IMocksControl;
import org.json.me.JSONObject;

import java.util.Collection;
import java.util.Iterator;

import junit.framework.TestCase;

/**
 * @author Hari S
 */
public class BatchTransactionTest extends TestCase {

  private static final String ID = "id";
  private static final String VALUE = "value";
  private static final String ID_A = "idA";
  private static final String VALUE_A = "A";
  private static final String ID_B = "idB";
  private static final String VALUE_B = "B";

  private BatchTransaction batchRequest;
  private IMocksControl mockControl;
  private Transaction transactionA;
  private Transaction transactionB;

  private JSONObject jsonA;
  private JSONObject jsonB;
  private Capture<JSONObject> jsonResponseA;
  private Capture<JSONObject> jsonResponseB;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    batchRequest = new BatchTransaction(new OrkutHttpRequestFactory(), new SandboxConfigFactory());
    mockControl = EasyMock.createControl();
    transactionA = mockControl.createMock(Transaction.class);
    transactionB = mockControl.createMock(Transaction.class);
    jsonA = new JSONObject();
    jsonB = new JSONObject();
    jsonResponseA = new Capture<JSONObject>();
    jsonResponseB = new Capture<JSONObject>();
  }

  public void testBatchTransaction_addNullTransaction() throws Exception {
    try {
      batchRequest.add(null);
      fail();
    } catch (NullPointerException npe) {
      // expected
    }
  }

  public void testBatchTransaction_getRequestWithoutAddingAnyTransaction() throws Exception {
    HttpRequest request = batchRequest.build();
    assertEquals("[]", new String(request.getRequestBody()));
    Collection headers = request.getHeaders();
    assertEquals(2, headers.size());
    Iterator iterator = headers.iterator();
    boolean contentTypeFound = false;
    boolean versionFound = false;
    while (iterator.hasNext()) {
      Header header = (Header) iterator.next();
      String name = header.getName();
      if (name.equals("Content-Type")) {
        assertEquals("application/json", header.getValue());
        contentTypeFound = true;
      } else {
        assertEquals(InternalConstants.ORKUT_CLIENT_LIB_HEADER, header.getName());
        assertEquals(InternalConstants.VERSION_STRING, header.getValue());
        versionFound = true;
      }
    }
    assertTrue(contentTypeFound);
    assertTrue(versionFound);
  }

  public void testBatchTransaction_withOneTransaction() throws Exception {
    jsonA.put("req", "A");
    expect(transactionA.getId()).andStubReturn(ID_A);
    expect(transactionA.getRequestAsJson()).andReturn(jsonA);
    transactionA.setResponse(capture(jsonResponseA));

    mockControl.replay();

    batchRequest.add(transactionA);

    HttpRequest request = batchRequest.build();
    assertEquals("[{\"req\":\"A\"}]", new String(request.getRequestBody()));

    batchRequest.setResponse("[{'id':'idA', 'value':'A'}]");

    JSONObject jsonRspA = jsonResponseA.getValue();
    assertEquals(ID_A, jsonRspA.optString(ID));
    assertEquals(VALUE_A, jsonRspA.optString(VALUE));
    mockControl.verify();
  }

  public void testBatchTransaction_withTwoTransaction() throws Exception {
    jsonA.put("req", "A");
    expect(transactionA.getId()).andStubReturn(ID_A);
    expect(transactionA.getRequestAsJson()).andReturn(jsonA);
    transactionA.setResponse(capture(jsonResponseA));

    jsonB.put("req", "B");
    expect(transactionB.getId()).andStubReturn(ID_B);
    expect(transactionB.getRequestAsJson()).andReturn(jsonB);
    transactionB.setResponse(capture(jsonResponseB));

    mockControl.replay();

    batchRequest.add(transactionA).add(transactionB);

    HttpRequest request = batchRequest.build();
    assertEquals("[{\"req\":\"A\"},{\"req\":\"B\"}]",
        new String(request.getRequestBody()));

    // Note: The order of responses are reversed here from that of the request.
    batchRequest.setResponse("[{'id':'idB', 'value':'B'},{'id':'idA', 'value':'A'}]");

    JSONObject jsonRspA = jsonResponseA.getValue();
    assertEquals(ID_A, jsonRspA.optString(ID));
    assertEquals(VALUE_A, jsonRspA.optString(VALUE));

    JSONObject jsonRspB = jsonResponseB.getValue();
    assertEquals(ID_B, jsonRspB.optString(ID));
    assertEquals(VALUE_B, jsonRspB.optString(VALUE));

    mockControl.verify();
  }

}
