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

import org.json.me.JSONObject;

/**
 * An {@link ActivityEntry} which represents a friend being added.
 *
 * @author Sachin Shenoy
 */
public class FriendAddActivity extends ActivityEntry {

  private final String personAId;
  private final String personBId;
  private final OrkutPerson personAProfile;
  private final OrkutPerson personBProfile;

  FriendAddActivity(JSONObject json) {
    super(json);
    if (getRelevantUserIdSize() < 2) {
      throw new RuntimeException("FriendAdd activity needs at least two relevantUserIds");
    }
    personAId = getRelevantUserId(0);
    personBId = getRelevantUserId(1);

    personAProfile = getRelevantProfile(personAId);
    personBProfile = getRelevantProfile(personBId);
  }

  public String type() {
    return ActivityEntry.ActivityType.FRIEND_ADD;
  }

  public String getPersonAId() {
    return personAId;
  }

  public String getPersonBId() {
    return personBId;
  }

  public boolean hasPersonAProfile() {
    return personAProfile != null;
  }

  public boolean hasPersonBProfile() {
    return personBProfile != null;
  }

  public OrkutPerson getPersonAProfile() {
    return personAProfile;
  }

  public OrkutPerson getPersonBProfile() {
    return personBProfile;
  }
}
