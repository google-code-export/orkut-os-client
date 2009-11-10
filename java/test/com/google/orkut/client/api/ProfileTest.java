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

  private static final String GIVEN_NAME = "Jane";
  private static final String FAMILY_NAME = "Doe";
  private Transport transport;
  private ProfileTxFactory factory;

  protected void setUp() throws Exception {
    super.setUp();

    factory = new ProfileTxFactory();
    transport = new Transport(AlbumsTest.OAUTH_PROPS_FILE);
    transport.init();
  }

  public void testGetSelfProfile() throws Exception {
    GetProfileTx profileTx = factory.getSelfProfile();
    profileTx.alsoGetEmails().alsoGetGender().alsoGetStatus();

    transport.add(profileTx).run();
    OrkutPerson profile = profileTx.getProfile();

    assertEquals(GIVEN_NAME, profile.getGivenName());
    assertEquals(FAMILY_NAME, profile.getFamilyName());
    assertEquals(PhotosTest.ID_SELF, profile.getId());
//    assertEquals("", profile.getThumbnailUrl());
  }
}
