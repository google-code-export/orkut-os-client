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

import com.google.orkut.client.api.OrkutPerson;
import com.google.orkut.client.api.RequiredFieldNotFoundException;

import org.json.me.JSONException;
import org.json.me.JSONObject;
import org.junit.Test;

import junit.framework.TestCase;

/**
 * @author harisasidharan@google.com (Hari S)
 */
public class OrkutPersonTest extends TestCase {

  public static final String MOCK_ORKUT_USERID = "03020289226933090195";
  public static final String MOCK_GIVEN_NAME = "Hari";
  public static final String MOCK_FAMILY_NAME = "S";
  public static final String MOCK_THUMBNAIL_URL =
    "http://img4.orkut.com/images/small/1241863374/178739716/ep.jpg";

  private static int counter = 1;

  protected void setUp() throws Exception {
    super.setUp();
  }

  protected void tearDown() throws Exception {
    super.tearDown();
  }

  public static JSONObject getTestProfileInJSON() throws JSONException {
    return getTestProfileInJSON(true, true, true);
  }

  private static JSONObject getTestProfileInJSON(boolean hasUserId, boolean hasGivenName,
      boolean hasFamilyName) throws JSONException {
    JSONObject profileInJSON = new JSONObject();
    if (hasUserId) {
      profileInJSON.put(Fields.ID, MOCK_ORKUT_USERID + " - " + counter++);
    }

    JSONObject name = new JSONObject();
    if (hasGivenName) {
      name.put(Fields.NAME_GIVEN_NAME, MOCK_GIVEN_NAME);
    }
    if (hasFamilyName) {
      name.put(Fields.NAME_FAMILY_NAME, MOCK_FAMILY_NAME);
    }
    if (hasGivenName || hasFamilyName) {
      profileInJSON.put(Fields.NAME, name);
    }
    return profileInJSON;
  }

  private boolean assertContains(String hayStack, String needle) {
    return hayStack.contains(needle);
  }

  @Test
  public void testParsingValidPersonObject() throws Exception {
    JSONObject profileInJSON = getTestProfileInJSON();
    OrkutPerson profileObject = new OrkutPerson(profileInJSON);
    assertEquals(profileInJSON, profileObject.getJSONbject());
    assertContains(profileObject.getId(), MOCK_ORKUT_USERID);
    assertEquals(MOCK_GIVEN_NAME, profileObject.getGivenName());
    assertEquals(MOCK_FAMILY_NAME, profileObject.getFamilyName());
  }

  @Test
  public void testParsingPersonObject_noUserId() throws Exception {
    try {
      OrkutPerson profileObject = new OrkutPerson(getTestProfileInJSON(false, true, true));
      profileObject.getId();

      // An exception should have been raised by the previous call
      assertFalse(true);
    } catch (RequiredFieldNotFoundException rfnfe) {
      assertEquals("id", rfnfe.getField());
    }
  }

  @Test
  public void testParsingPersonObject_noGivenName() throws Exception {
    OrkutPerson profileObject = new OrkutPerson(getTestProfileInJSON(true, false, true));
    assertContains(profileObject.getId(), MOCK_ORKUT_USERID);
    assertEquals(null, profileObject.getGivenName());
    assertEquals(MOCK_FAMILY_NAME, profileObject.getFamilyName());
  }

  @Test
  public void testParsingPersonObject_noFamilyName() throws Exception {
    JSONObject profileInJSON = getTestProfileInJSON(true, true, false);
    OrkutPerson profileObject = new OrkutPerson(profileInJSON);
    assertEquals(profileInJSON, profileObject.getJSONbject());
    assertContains(profileObject.getId(), MOCK_ORKUT_USERID);
    assertEquals(MOCK_GIVEN_NAME, profileObject.getGivenName());
    assertEquals(null, profileObject.getFamilyName());
  }

  @Test
  public void testParsingPersonObject_noName() throws Exception {
    OrkutPerson profileObject = new OrkutPerson(getTestProfileInJSON(true, false, false));
    assertContains(profileObject.getId(), MOCK_ORKUT_USERID);
    assertEquals(null, profileObject.getGivenName());
    assertEquals(null, profileObject.getFamilyName());
  }

  @Test
  public void testParsingPersonObject_withThumbnailUrl() throws Exception {
    JSONObject profileInJSON = getTestProfileInJSON(true, true, true);
    profileInJSON.put(Fields.THUMBNAIL_URL, MOCK_THUMBNAIL_URL);
    OrkutPerson profileObject = new OrkutPerson(profileInJSON);
    assertContains(profileObject.getId(), MOCK_ORKUT_USERID);
    assertEquals(MOCK_GIVEN_NAME, profileObject.getGivenName());
    assertEquals(null, profileObject.getDisplayName());
    assertEquals(MOCK_FAMILY_NAME, profileObject.getFamilyName());
    assertEquals(MOCK_THUMBNAIL_URL, profileObject.getThumbnailUrl());
  }
}
