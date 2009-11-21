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

import org.json.me.JSONException;
import org.json.me.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Integration tests for messages.
 *
 * @author Shishir Birmiwal
 */
public class MessagesTest extends JaneDoeTestCase {

  private Transport transport;
  private ScrapTxFactory factory;

  List<ScrapEntry> expectedScraps;

  protected void setUp() throws Exception {
    super.setUp();

    expectedScraps = getExpectedScraps();
    factory = new ScrapTxFactory();
    transport = new Transport(AlbumsTest.OAUTH_PROPS_FILE);
    transport.init();
  }

  public void testGetScraps() throws Exception {
    if (doesNotMeetJaneDoeDependency(transport)) {
      // skipping test :(
      return;
    }

    GetScrapsTx getScrapsTx = factory.getSelfScraps();

    transport.add(getScrapsTx).run();

    assertEquals(expectedScraps.size(), getScrapsTx.getScrapCount());
    for (int i = 0; i < expectedScraps.size(); i++) {
      ScrapEntry scrap = getScrapsTx.getScrap(i);
      assertScrapEquals(expectedScraps.get(i), scrap);
    }
  }

  public void testGetScrapsPagination() throws Exception {
    if (doesNotMeetJaneDoeDependency(transport)) {
      // skipping test :(
      return;
    }

    GetScrapsTx getScrapsTx = factory.getSelfScraps();
    getScrapsTx.setCount(1);
    transport.add(getScrapsTx).run();
    int offset = 0;

    GetScrapsTx lastGetScrapTx = getScrapsTx;
    while (getScrapsTx.getScrapCount() != 0) {
      assertEquals(1, getScrapsTx.getScrapCount());
      assertScrapEquals(expectedScraps.get(offset), getScrapsTx.getScrap(0));
      offset++;
      lastGetScrapTx = getScrapsTx;
      getScrapsTx = factory.getNext(getScrapsTx);
      transport.add(getScrapsTx).run();
    }

    assertEquals(expectedScraps.size(), offset);
    offset--;

    while (offset > 0) {
      lastGetScrapTx = factory.getPrev(lastGetScrapTx);
      transport.add(lastGetScrapTx).run();
      assertEquals(1, lastGetScrapTx.getScrapCount());
      offset--;
      assertScrapEquals(expectedScraps.get(offset), lastGetScrapTx.getScrap(0));
    }
  }

  private void assertScrapEquals(ScrapEntry expected, ScrapEntry actual) {
    assertEquals(expected.getBody(), actual.getBody());
    assertEquals(expected.getFromUserId(), actual.getFromUserId());
    assertEquals(expected.getId(), actual.getId());
    assertEquals(expected.getTime(), actual.getTime());
    assertFromUserProfileEquals(expected.getFromUserProfile(), actual.getFromUserProfile());
  }

  private void assertFromUserProfileEquals(OrkutPerson expected, OrkutPerson actual) {
    assertEquals(expected.getId(), actual.getId());
    assertEquals(expected.getDisplayName(), actual.getDisplayName());
    assertEquals(expected.getFamilyName(), actual.getFamilyName());
    assertEquals(expected.getGivenName(), actual.getGivenName());
    assertTrue(actual.getThumbnailUrl().contains(expected.getThumbnailUrl()));
    assertTrue(actual.getProfileUrl().contains(expected.getProfileUrl()));
  }

  private List<ScrapEntry> getExpectedScraps() throws JSONException {
    List<ScrapEntry> scraps = new ArrayList<ScrapEntry>();

    JSONObject scrapJson = new JSONObject("{'body':'not yet. will land soon!','type':'PUBLIC_MESSAGE',"
        + "'fromUserId':'12658990920245756486','id':'543695944:1257981767:544604496:0',"
        + "'time':1258006967,'fromUserProfile':{'profileUrl':'Profile.aspx?uid=13381700609108014072',"
        + "'id':'12658990920245756486','thumbnailUrl':'orkut.com/images/small/1257896766/544604496/ep.jpg',"
        + "'name':{'familyName':'Doe','givenName':'John'}}}");
    scraps.add(new ScrapEntry(scrapJson));

    scrapJson = new JSONObject("{'body':'I&#39;m good! How are you?','type':'PUBLIC_MESSAGE',"
        + "'fromUserId':'05486674863471704172','id':'543695944:1257981704:545289376:0',"
        + "'time':1258006904,'fromUserProfile':{'profileUrl':'Profile.aspx?uid=14146392242427648085',"
        + "'id':'05486674863471704172','thumbnailUrl':'orkut.com/images/small/1257975884/545289376/ep.jpg',"
        + "'name':{'familyName':'Poe','givenName':'John'}}}");
    scraps.add(new ScrapEntry(scrapJson));
    return scraps;
  }
}
