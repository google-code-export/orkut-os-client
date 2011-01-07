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

import com.google.orkut.client.api.GetProfileTx;
import com.google.orkut.client.api.ProfileTxFactory;
import com.google.orkut.client.api.UpdateProfileTx;

import java.io.IOException;

public class ProfileTxSample {

  private final Transport transport;
  private ProfileTxFactory factory;

  public ProfileTxSample(Transport transport) {
    this.transport = transport;
    factory = new ProfileTxFactory();
  }

  public void run() throws IOException {
    getSelfProfile();
    updateSelfProfile();
  }

  private void getSelfProfile() throws IOException {
    GetProfileTx selfProfileTx = factory.getSelfProfile();
    transport.add(selfProfileTx).run();

    if (selfProfileTx.hasError()) {
      // something went wrong!
      return;
    }
    System.out.println("Hi, I am " + selfProfileTx.getProfile().getGivenName());
  }

  private void updateSelfProfile() throws IOException {
    UpdateProfileTx updateProfileTx = factory.updateSelfProfile();
    updateProfileTx.setStatus("Random Status");
    transport.add(updateProfileTx).run();

    if (updateProfileTx.hasError()) {
      // something went wrong!
      return;
    }
  }
}
