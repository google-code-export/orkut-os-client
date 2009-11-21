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

import com.google.orkut.client.api.ProfileTest.JaneDoe;
import com.google.orkut.client.sample.Transport;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import junit.framework.TestCase;

/**
 * Tests that need to be run as Jane-Doe user.
 * Tests that depend on the logged in user to be Jane-Doe should
 * extend {@link JaneDoeTestCase} and call {@link #doesNotMeetJaneDoeDependency(Transport)}
 * before executing the test:
 * <pre><code>
 * public void testSomeTest() throws Exception {
 *   if (doesNotMeetJaneDoeDependency(transport)) {
 *     // skip this test
 *     return;
 *   }
 * }
 * </code></pre>
 *
 * @author Shishir Birmiwal
 */
public class JaneDoeTestCase extends TestCase {
  private static final Logger logger = Logger.getLogger(JaneDoeTestCase.class.getCanonicalName());
  private static Map<Transport, Boolean> isUserJaneDoeMap = new HashMap<Transport, Boolean>();

  protected boolean isUserJaneDoe(Transport transport) throws IOException {
    if (!isUserJaneDoeMap.containsKey(transport)) {
      ProfileTxFactory txFactory = new ProfileTxFactory();
      GetProfileTx selfProfile = txFactory.getSelfProfile();
      transport.add(selfProfile).run();
      isUserJaneDoeMap.put(transport, selfProfile.getProfile().getId().equals(JaneDoe.ID));
    }
    return isUserJaneDoeMap.get(transport);
  }

  protected boolean doesNotMeetJaneDoeDependency(Transport transport) throws IOException {
    if (!isUserJaneDoe(transport)) {
      logger.severe("SKIPPING TEST - Logged in account is NOT JANE-DOE, but RUN_JANE_DOE_TESTS is TRUE");
      return true;
    }
    return false;
  }
}
