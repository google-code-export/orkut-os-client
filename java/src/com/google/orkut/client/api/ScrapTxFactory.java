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

import com.google.orkut.client.api.InternalConstants.Values;

/**
 * Factory for creating {@link Transaction}s related to Scraps.
 *
 * @author Sachin Shenoy
 */
public class ScrapTxFactory {

  public GetScrapsTx getScrap() {
    return new GetScrapsTx();
  }

  public GetScrapsTx getNext(GetScrapsTx prev) {
    return prev.getNext();
  }

  public GetScrapsTx getPrev(GetScrapsTx last) {
    return last.getPrev();
  }

  public Transaction deleteScrap(ScrapEntry scrapEntry) {
    Transaction transaction = new Transaction(RequestIds.SCRAPS_DELETE, MethodNames.MESSAGES_DELETE);
    transaction.request.setUserId(Constants.USERID_ME)
                       .setGroupId(Group.SELF)
                       .addParameter(Params.MESSAGE_TYPE, Values.PUBLIC_MESSAGE)
                       .addParameter(Params.MSG_ID, scrapEntry.getId());
    return transaction;
  }

  public WriteScrapTx writeScrap(String personId, String body) {
    return new WriteScrapTx(personId, body);
  }

  public WriteScrapTx replyToScrap(ScrapEntry scrapEntry, String body) {
    return new WriteScrapTx(scrapEntry.getFromUserId(), body, scrapEntry.getId());
  }
}
