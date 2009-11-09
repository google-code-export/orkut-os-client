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

import com.google.orkut.client.api.ActivityEntry;
import com.google.orkut.client.api.ActivityTxFactory;
import com.google.orkut.client.api.GetActivitiesTx;

import java.io.IOException;

/**
 *
 * @author sachins@google.com (Sachin Shenoy)
 */
public class ActivityTxSample {

  private final Transport transport;
  private final ActivityTxFactory factory;

  public ActivityTxSample(Transport transport) {
    this.transport = transport;
    factory = new ActivityTxFactory();
  }

  public void run() throws IOException {
    fetchActivity();
  }

  private void fetchActivity() throws IOException {
    GetActivitiesTx fetchActivityTx = factory.getActivities();
    fetchActivityTx.fields().addPageUrl();
    transport.add(fetchActivityTx).run();

    for (int i = 0; i < fetchActivityTx.activityCount(); ++i) {
      ActivityEntry entry = fetchActivityTx.getActivity(i);
      System.out.println(entry);
    }
  }
}
