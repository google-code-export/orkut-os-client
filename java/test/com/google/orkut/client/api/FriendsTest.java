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
 * Integration tests for Friends related APIs.
 *
 * @author Shishir Birmiwal
 */
public class FriendsTest extends TestCase {

  private FriendTxFactory factory;
  private Transport transport;

  static class JohnPoe {
    static final String ID = "05486674863471704172";
    private static final String GIVEN_NAME = "John";
    private static final String FAMILY_NAME = "Poe";
    private static final String THUMBNAIL_URL = "orkut.com/images/small/1257975884/545289376/ep.jpg";
    private static final String PROFILE_URL = "Profile.aspx?uid=14146392242427648085";
    private static final String GENDER = Constants.GENDER_MALE;
    private static final String STATUS = "on orkut";
    private static final String EMAIL_ADDRESS = "oocl19@gmail.com";
    private static final String PHONE_NUMBER = "123456";
  }

  protected void setUp() throws Exception {
    super.setUp();

    factory = new FriendTxFactory();
    transport = new Transport(AlbumsTest.OAUTH_PROPS_FILE);
    transport.init();
  }

  public void testSelfGetFriends() throws Exception {
    GetFriendTx selfFriendsTx = factory.getSelfFriends();
    selfFriendsTx.alsoGetName().alsoGetStatus()
        .alsoGetGender().alsoGetProfileUrl().alsoGetThumbnailUrl()
        .alsoGetEmails().alsoGetPhoneNumbers();
    transport.add(selfFriendsTx).run();

    int friendsCount = selfFriendsTx.getFriendsCount();
    assertEquals(2, friendsCount);

    for (int i = 0; i < friendsCount; i++) {
      OrkutPerson friend = selfFriendsTx.getFriend(i);
      assertFriendValidForJaneDoe(friend);
    }
  }

  public void testSelfGetFriendsWithPagination() throws Exception {
    GetFriendTx selfFriendsTx = factory.getSelfFriends();
    selfFriendsTx.setCount(1);
    selfFriendsTx.alsoGetName().alsoGetStatus()
        .alsoGetGender().alsoGetProfileUrl().alsoGetThumbnailUrl()
        .alsoGetEmails().alsoGetPhoneNumbers();
    transport.add(selfFriendsTx).run();

    assertEquals(1, selfFriendsTx.getFriendsCount());

    while (true) {
      OrkutPerson friend = selfFriendsTx.getFriend(0);
      assertFriendValidForJaneDoe(friend);

      if (!selfFriendsTx.canGetMoreFriends()) {
        break;
      }

      selfFriendsTx = factory.getNextFriends(selfFriendsTx);
      transport.add(selfFriendsTx).run();
    }
  }

  public void testGetFriendsOf() throws Exception {
    GetFriendTx getFriendTx = factory.getFriendsOf(JohnPoe.ID);
    getFriendTx.alsoGetEmails().alsoGetGender().alsoGetName()
        .alsoGetPhoneNumbers().alsoGetProfileUrl()
        .alsoGetStatus().alsoGetThumbnailUrl();
    transport.add(getFriendTx).run();

    assertEquals(1, getFriendTx.getFriendsCount());

    ProfileTest.assertPersonIsJaneDoe(getFriendTx.getFriend(0), false);
  }

  // Jane Doe's friends are John Doe and John Poe!
  private void assertFriendValidForJaneDoe(OrkutPerson friend) {
    if (friend.getId().equals(JohnPoe.ID)) {
      assertPersonIsJohnPoe(friend);
    } else {
      ProfileTest.assertPersonIsJohnDoe(friend);
    }
  }

  static void assertPersonIsJohnPoe(OrkutPerson profile) {
    assertEquals(JohnPoe.GIVEN_NAME, profile.getGivenName());
    assertEquals(JohnPoe.FAMILY_NAME, profile.getFamilyName());
    assertEquals(JohnPoe.ID, profile.getId());
    assertTrue(profile.getThumbnailUrl().contains(JohnPoe.THUMBNAIL_URL));
    assertEquals(JohnPoe.STATUS, profile.getStatus());
    assertEquals(JohnPoe.GENDER, profile.getGender());
    assertEquals(1, profile.getEmailCount());
    assertEquals(JohnPoe.EMAIL_ADDRESS, profile.getEmail(0));
    assertEquals(1, profile.getPhoneNumberCount());
    assertEquals(JohnPoe.PHONE_NUMBER, profile.getPhoneNumber(0));
    assertTrue(profile.getProfileUrl().contains(JohnPoe.PROFILE_URL));
    assertTrue(profile.getThumbnailUrl().contains(JohnPoe.THUMBNAIL_URL));
  }
}
