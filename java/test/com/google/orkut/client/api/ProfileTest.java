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

/**
 * Integration tests for Profile fetch.
 *
 * @author Shishir Birmiwal
 */
public class ProfileTest extends TestCase {

  static class JaneDoe {
    static final String ID = "02776157447964356030";
    private static final String GIVEN_NAME = "Jane";
    private static final String FAMILY_NAME = "Doe";
    private static final String THUMBNAIL_URL = "orkut.com/images/small/1257819534/543695944/ln.jpg";
    private static final String PROFILE_URL = "Profile.aspx?uid=784965567615271151";
    private static final String GENDER = Constants.GENDER_FEMALE;
    private static final String STATUS = "having fun!";
    private static final String EMAIL_ADDRESS = "oocl17@gmail.com";
    private static final String PHONE_NUMBER = "9900110000";
  }

  static class JohnDoe {
    static final String ID = "12658990920245756486";
    private static final String GIVEN_NAME = "John";
    private static final String FAMILY_NAME = "Doe";
    private static final String THUMBNAIL_URL = "orkut.com/images/small/1257896766/544604496/ep.jpg";
    private static final String PROFILE_URL = "Profile.aspx?uid=13381700609108014072";
    private static final String GENDER = Constants.GENDER_MALE;
    private static final String STATUS = "leaving on a jet plane";
    private static final String EMAIL_ADDRESS = "oocl18@gmail.com";
    private static final String PHONE_NUMBER_0 = "456789123";
    private static final String PHONE_NUMBER_1 = "123890664";
  }

  private Transport transport;
  private ProfileTxFactory factory;

  protected void setUp() throws Exception {
    super.setUp();

    factory = new ProfileTxFactory();
    transport = new Transport(AlbumsTest.OAUTH_PROPS_FILE);
    transport.init();
  }

  public void testGetSelfProfile() throws Exception {
    GetProfileTx getProfileTx = factory.getSelfProfile();
    getProfileTx.alsoGetEmails().alsoGetGender().alsoGetStatus()
        .alsoGetName().alsoGetPhoneNumbers()
        .alsoGetProfileUrl().alsoGetThumbnailUrl();

    transport.add(getProfileTx).run();
    OrkutPerson profile = getProfileTx.getProfile();

    assertEquals(JaneDoe.GIVEN_NAME, profile.getGivenName());
    assertEquals(JaneDoe.FAMILY_NAME, profile.getFamilyName());
    assertEquals(JaneDoe.ID, profile.getId());
    assertTrue(profile.getThumbnailUrl().contains(JaneDoe.THUMBNAIL_URL));
    assertEquals(JaneDoe.STATUS, profile.getStatus());
    assertEquals(JaneDoe.GENDER, profile.getGender());
    assertEquals(1, profile.getEmailCount());
    assertEquals(JaneDoe.EMAIL_ADDRESS, profile.getEmail(0));
    assertEquals(1, profile.getPhoneNumberCount());
    assertEquals(JaneDoe.PHONE_NUMBER, profile.getPhoneNumber(0));
    assertTrue(profile.getProfileUrl().contains(JaneDoe.PROFILE_URL));
    assertTrue(profile.getThumbnailUrl().contains(JaneDoe.THUMBNAIL_URL));
  }

  public void testGetFriend() throws Exception {
    GetProfileTx getProfileTx = factory.getProfileOf(JohnDoe.ID);
    getProfileTx.alsoGetEmails().alsoGetGender().alsoGetStatus()
        .alsoGetName().alsoGetPhoneNumbers()
        .alsoGetProfileUrl().alsoGetThumbnailUrl();

    transport.add(getProfileTx).run();
    OrkutPerson profile = getProfileTx.getProfile();

    assertEquals(JohnDoe.GIVEN_NAME, profile.getGivenName());
    assertEquals(JohnDoe.FAMILY_NAME, profile.getFamilyName());
    assertEquals(JohnDoe.ID, profile.getId());
    assertTrue(profile.getThumbnailUrl().contains(JohnDoe.THUMBNAIL_URL));
    assertEquals(JohnDoe.STATUS, profile.getStatus());
    assertEquals(JohnDoe.GENDER, profile.getGender());
    assertEquals(1, profile.getEmailCount());
    assertEquals(JohnDoe.EMAIL_ADDRESS, profile.getEmail(0));
    assertEquals(2, profile.getPhoneNumberCount());
    assertEquals(JohnDoe.PHONE_NUMBER_0, profile.getPhoneNumber(0));
    assertEquals(JohnDoe.PHONE_NUMBER_1, profile.getPhoneNumber(1));
    assertTrue(profile.getProfileUrl().contains(JohnDoe.PROFILE_URL));
    assertTrue(profile.getThumbnailUrl().contains(JohnDoe.THUMBNAIL_URL));

  }
}
