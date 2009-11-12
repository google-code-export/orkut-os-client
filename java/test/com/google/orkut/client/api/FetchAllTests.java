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

import com.google.orkut.client.sample.Transport;

import junit.framework.TestCase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * These tests collate data from various test accounts and then check
 * data for consistency.
 *
 * Note: To run these tests, save required authorization tokens for
 * the test accounts in "java/test/com/google/orkut/client/api/account*.properties"
 *
 * @author Shishir Birmiwal
 */
public class FetchAllTests extends TestCase {
  private static final int BIG_FETCH_COUNT = 50;
  private static final int MAX_FETCH_COUNT = 200;

  private static final String TRANSPORT_FILES_PATH = "test/com/google/orkut/client/api/";
  private static final String[] TRANSPORT_FILES = {
    "account0.properties",
    "account1.properties",
    "account2.properties",
    "account3.properties",
    "account4.properties",
    "account5.properties",
    "account6.properties",
    "account7.properties",
    "account8.properties",
    "account9.properties",
  };

  static List<Transport> transports;

  static List<ActivityEntry> activities;

  static <T> List<T> createList() {
    return new ArrayList<T>();
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();

    // create transports and fetch data only once as
    // they are expensive operations
    if (transports == null) {
      transports = createTransports();
    }

    for (Transport transport : transports) {
      fetchAllActivities(transport);
    }
  }

  private void fetchAllActivities(Transport transport) throws IOException {
    if (activities == null) {
      activities = createList();
    }

    ActivityTxFactory factory = new ActivityTxFactory();
    GetActivitiesTx getActivities = factory.getActivities();
    getActivities.setCount(BIG_FETCH_COUNT);
    transport.add(getActivities).run();

    int count = 0;
    while (getActivities.getActivityCount() > 0) {
      for (int i = 0; i < getActivities.getActivityCount(); i++) {
        activities.add(getActivities.getActivity(i));
      }
      count += getActivities.getActivityCount();
      if (count > MAX_FETCH_COUNT) {
        break;
      }
      getActivities = factory.getNext(getActivities);
      transport.add(getActivities).run();
    }
  }

  /**
   * Create authenticated transport channels to send requests.
   */
  private List<Transport> createTransports() throws Exception {
    List<Transport> transports = createList();

    for (int i = 0; i < TRANSPORT_FILES.length; i++) {
      Transport transport = new Transport(TRANSPORT_FILES_PATH + TRANSPORT_FILES[i]);
      transport.init();
      transports.add(transport);
    }

    return transports;
  }
}
