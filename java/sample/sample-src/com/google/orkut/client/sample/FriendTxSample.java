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

import com.google.orkut.client.api.BirthdayNotificationEntry;
import com.google.orkut.client.api.BirthdayNotificationTx;
import com.google.orkut.client.api.FriendRequestEntry;
import com.google.orkut.client.api.FriendTxFactory;
import com.google.orkut.client.api.PendingFriendRequestTx;

/**
 * Sample code for friend transactions.
 * 
 * @author Sachin Shenoy
 */
public class FriendTxSample {

  private Transport transport;
  private FriendTxFactory factory;

  public FriendTxSample(Transport transport) {
    this.transport = transport;
    factory = new FriendTxFactory();
  }

  public void run() throws Exception {
    fetchBirthayNotifications();
    fetchFriendInviteNotifications();
  }

  private void fetchFriendInviteNotifications() throws Exception {
    PendingFriendRequestTx pendingFriendRequestTx = factory.getPendingFriendRequest();
    transport.add(pendingFriendRequestTx).run();
    
    if (pendingFriendRequestTx.hasError()) {
      // handle error
      return;
    }
    
    for (int i = 0; i < pendingFriendRequestTx.getPendingFriendRequestCount(); ++i) {
      FriendRequestEntry entry = pendingFriendRequestTx.getPendingFriendRequest(i);
      System.out.print("User: " + entry.getUserProfile().getDisplayName());
      System.out.print(" (" + entry.getUserId() + ") ");
      System.out.println("Message: " + entry.getMessage());
    }
  }

  private void fetchBirthayNotifications() throws Exception {
    BirthdayNotificationTx birthdayNotificationTx = factory.getBirthdayNotification();
    transport.add(birthdayNotificationTx).run();
    
    if (birthdayNotificationTx.hasError()) {
      // handle error
      return;
    }
    
    for (int i = 0; i < birthdayNotificationTx.getBirthdayNotificationCount(); ++i) {
      BirthdayNotificationEntry entry = birthdayNotificationTx.getBirthdayNotification(i);
      System.out.print("User: " + entry.getUserProfile().getDisplayName());
      System.out.print(" (" + entry.getUserId() + ") ");
      System.out.println("Birthdate: " + entry.getBirthDay() + "/" + entry.getBirthMonth());
    }
  }
}
