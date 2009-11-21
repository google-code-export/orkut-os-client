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

/**
 * A factory to create activity related {@link Transaction} on orkut.
 *
 * @author Sachin Shenoy
 */
public class ActivityTxFactory {

  /**
   * Get a {@link GetActivitiesTx} to get the logged in users' activities.
   */
  public GetActivitiesTx getSelfActivities() {
    return new GetActivitiesTx();
  }

  /**
   * Get a {@link GetActivitiesTx} to get a person's activities.
   */
  public GetActivitiesTx getActivitiesOf(String personId) {
    return new GetActivitiesTx(personId);
  }

  public GetActivitiesTx getNext(GetActivitiesTx prev) {
    return prev.getNext();
  }

  public GetActivitiesTx getPrev(GetActivitiesTx last) {
    return last.getPrev();
  }

  /**
   * Posts an activity (visible to the logged in user and his/her friends).
   */
  public PostActivityTx postActivity(String title, String body) {
    return new PostActivityTx(title, body);
  }
}