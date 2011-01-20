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

package com.google.orkut.client.sample;

import com.google.orkut.client.api.GetProfileTx;
import com.google.orkut.client.api.GetScrapsTx;
import com.google.orkut.client.api.ProfileTxFactory;
import com.google.orkut.client.api.ScrapEntry;
import com.google.orkut.client.api.ScrapTxFactory;
import com.google.orkut.client.api.Transaction;
import com.google.orkut.client.api.WriteScrapTx;

import java.io.IOException;

/**
 * Sample code for Scrap Transaction. This has example code for fetching users
 * scraps, paginating through scrapbook, deleting scrap, and writing/replying to
 * scrap.
 *
 * @author sachins@google.com (Sachin Shenoy)
 */
public class ScrapTxSample {
  private static final String TEST_SCRAP_BODY = "test scrap body";
  private final Transport transport;
  private final ScrapTxFactory factory;
  private String selfId;

  public ScrapTxSample(Transport transport) {
    this.transport = transport;
    factory = new ScrapTxFactory();
  }

  private void setUp() throws IOException {
    // This is here only to find the user-id of the logged in user (@self).
    ProfileTxFactory profileTxFactory = new ProfileTxFactory();
    GetProfileTx getProfileTx = profileTxFactory.getSelfProfile();
    transport.add(getProfileTx).run();
    selfId = getProfileTx.getProfile().getId();
  }

  public void run() throws IOException {
    setUp();
    fetchScrap();
    writeScrap();
    replyScrap();
    deleteScrap();
  }

  private void fetchScrap() throws IOException {
    System.out.println("Run fetchScrapTx:");

    // Create a transaction to fetch my scraps.
    GetScrapsTx fetchScrapTx = factory.getSelfScraps();

    // execute the transaction.
    transport.add(fetchScrapTx).run();

    // check if the fetch resulted in error.
    if (fetchScrapTx.hasError()) {
      // handle error
      return;
    }

    // Print the fetched scraps.
    printScrapTx(fetchScrapTx);

    // Fetch the next set of scraps, paginate to "next".
    fetchScrapTx = factory.getNext(fetchScrapTx);
    fetchAndPrintScrapTx(fetchScrapTx);

    // Fetch the previous set of scraps, paginate to "prev".
    fetchScrapTx = factory.getPrev(fetchScrapTx);
    fetchAndPrintScrapTx(fetchScrapTx);
  }

  private void writeScrap() throws IOException {
    System.out.println("Run writeScrapTx:");

    // Write a scrap to myself!
    WriteScrapTx writeScrapTx = factory.writeScrap(selfId, TEST_SCRAP_BODY);
    transport.add(writeScrapTx).run();

    if (writeScrapTx.hasError()) {
      // handle error
      return;
    }
  }

  private void replyScrap() throws IOException {
    // Fetch the first scrap of user.
    ScrapEntry scrapEntry = fetchFirstScrapEntry();

    WriteScrapTx replyToScrapTx = factory.replyToScrap(scrapEntry, TEST_SCRAP_BODY);
    transport.add(replyToScrapTx).run();

    if (replyToScrapTx.hasError()) {
      // handle error
      return;
    }
  }

  private void deleteScrap() throws IOException {
    System.out.println("Run deleteScrapTx:");

    // Fetch the first scrap of user.
    ScrapEntry scrapEntry = fetchFirstScrapEntry();

    // Check if it is a test scrap.
    if (!scrapEntry.getBody().equals(TEST_SCRAP_BODY)) {
      return;
    }

    // Delete scrap iff it is a test scrap.
    Transaction deleteScrap = factory.deleteScrap(scrapEntry);
    transport.add(deleteScrap).run();
  }

  private ScrapEntry fetchFirstScrapEntry() throws IOException {
    GetScrapsTx fetchScrapTx = factory.getSelfScraps();
    transport.add(fetchScrapTx).run();

    if (fetchScrapTx.hasError()) {
      // handle error
      return null;
    }

    if (fetchScrapTx.getScrapCount() == 0) {
      // No scraps out there to delete??
      return null;
    }
    return fetchScrapTx.getScrap(0);
  }

  private void fetchAndPrintScrapTx(GetScrapsTx fetchScrapTx) throws IOException {
    transport.add(fetchScrapTx).run();
    if (fetchScrapTx.hasError()) {
      // handle error
      return;
    }

    printScrapTx(fetchScrapTx);
  }

  private void printScrapTx(GetScrapsTx fetchScrapTx) {
    // Print body of all fetched scrap.
    for (int i = 0; i< fetchScrapTx.getScrapCount(); ++i) {
      System.out.println("Scrap " + i + ")" + fetchScrapTx.getScrap(i));
    }
  }
}
