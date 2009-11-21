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

import net.oauth.OAuth;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

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

  private String contentType;

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
  
  public OrkutHttpRequest build() throws IOException {
    OrkutHttpRequest request;
    if (hasUpload()) {
      MultipartBuilder builder = new MultipartBuilder();
      addBody(builder);
      request = new OrkutHttpRequest(builder.build(), builder.getContentType());
      request.addParam("request", batch.toString());
    } else {
      request = new OrkutHttpRequest(batch.toString().getBytes("UTF-8"), "application/json");
    }
    request.addHeader(InternalConstants.ORKUT_CLIENT_LIB_HEADER, InternalConstants.VERSION_STRING);
    return request;
  }

  private boolean hasUpload() {
    Iterator it = transactions.values().iterator();
    while (it.hasNext()) {
       Transaction transaction = (Transaction) it.next();
      if (transaction instanceof UploadPhotoTx) {
        return true;
      }
    }
    return false;
  }

  private void addBody(MultipartBuilder builder) throws IOException {
    Iterator it = transactions.values().iterator();
    while (it.hasNext()) {
       Transaction transaction = (Transaction) it.next();
      if (transaction instanceof UploadPhotoTx) {
        UploadPhotoTx photoTx = (UploadPhotoTx) transaction;
        photoTx.addBody(builder);
      }
    }
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
