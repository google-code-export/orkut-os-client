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

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Builds a batch request from {@link Transaction}s.
 *
 * <pre>
 * {@code
 * BatchTransaction batch = new BatchTransaction();
 * batch.add(transaction1)
 *      .add(transaction2)
 *      .add(transaction3)
 *      .getRequest();
 *
 * batch.setResponse(response);
 * }</pre>
 *
 * @author Sachin Shenoy
 * @author Shishir Birmiwal
 */
public class BatchTransaction {
  /**
   * Map of request-id to transaction. Used to de-mux responses to appropriate
   * transaction
   */
  private final HashMap transactions = new HashMap();

  /**
   * Array of all JSON request in this batch.
   */
  private final JSONArray batch = new JSONArray();

  /**
   * Adds the given request to the batch.
   *
   * @param transaction transaction to be added to the batch.
   * @return this to facilitate chaining
   */
  public BatchTransaction add(Transaction transaction) {
    if (transaction == null) {
      throw new NullPointerException("transaction cannot be null ");
    }
    transactions.put(transaction.getId(), transaction);
    batch.put(transaction.getRequestAsJson());
    return this;
  }

  /** Returns the value of content type
   * @throws IOException
   */
  public String getContentType() throws IOException {
    return "application/json";
  }

  /**
   * Builds and returns the batch json request string. Send this out to the
   * server with appropriate authentication, and pass the response received to
   * {@link #setResponse(String)}
   * @throws IOException
   */
  public byte[] getRequestBody() throws IOException {
    return batch.toString().getBytes("UTF-8");
  }

  /**
   * Set the response received from server for the batch request.
   *
   * @param batchResponseString
   */
  public void setResponse(String batchResponseString) {
    JSONArray batchResponse;
    try {
      batchResponse = new JSONArray(batchResponseString);
      for (int i = 0; i < batchResponse.length(); ++i) {
        JSONObject response = batchResponse.getJSONObject(i);
        String id = response.optString(Fields.ID);
        Transaction transaction = (Transaction) transactions.get(id);
        if (transaction != null) {
          transaction.setResponse(response);
        }
      }
    } catch (JSONException e) {
      throw new RuntimeException("Unexpected exception while setting response", e);
    }
  }
}
