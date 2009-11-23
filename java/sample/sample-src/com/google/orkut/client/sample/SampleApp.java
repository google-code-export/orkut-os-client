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

import com.google.orkut.client.api.FriendTxFactory;
import com.google.orkut.client.api.GetFriendTx;
import com.google.orkut.client.api.GetProfileTx;
import com.google.orkut.client.api.OrkutPerson;
import com.google.orkut.client.api.ProfileTxFactory;

/**
 * A Sample application to get a person and his/her friend's details.
 *
 * @author Sachin Shenoy
 */
public class SampleApp {
  private final Transport transport;

  private SampleApp(String propfile) throws Exception {
    transport = new Transport(propfile);
    transport.init();
  }

  public static void main(String[] args) throws Exception {
    if (args.length == 0) {
      System.out.println("please specfiy oauth.properties file as argument");
      return;
    }
    new SampleApp(args[0]).run();
  }

  private void run() throws Exception {
    ProfileTxFactory profileTxFactory = new ProfileTxFactory();
    GetProfileTx profile = profileTxFactory.getSelfProfile();

    transport.add(profile);

    FriendTxFactory friendTxFactory = new FriendTxFactory();
    GetFriendTx friends = friendTxFactory.getSelfFriends();

    transport.add(friends);

    transport.run();

    OrkutPerson person = profile.getProfile();
    System.out.println("Hello, " + person.getGivenName() + " " + person.getFamilyName() + "!");

    for (int i = 0; i < friends.getFriendsCount(); i++) {
      OrkutPerson friend = friends.getFriend(i);
      System.out.println("Friend: " + friend.getGivenName());
    }
  }
}
