/* EXPERIMENTAL (really) */
/* Copyright (c) 2010 Google Inc.
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

import org.json.me.JSONException;
import org.json.me.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Integration tests for videos.
 *
 * @author Prashant Tiwari
 */
public class VideosTest extends AccountDependantTestCase {

  private VideoTxFactory factory;

  List<Video> expectedVideos;

  @Override
  protected void setUp() throws Exception {
    super.setUp();

    expectedVideos = getExpectedVideos();
    factory = new VideoTxFactory();
  }

  public void testGetVideos() throws Exception {
    GetVideosTx getVideosTx = factory.getVideos();

    transport.add(getVideosTx).run();

    assertEquals(expectedVideos.size(), getVideosTx.getVideoCount());
    for (int i = 0; i < expectedVideos.size(); i++) {
      Video video = getVideosTx.getVideo(i);
      assertVideoEquals(expectedVideos.get(i), video);
    }
  }

  public void testGetNextPrevVideos() throws Exception {
    GetVideosTx getVideosTx = factory.getVideos().setCount(1);
    transport.add(getVideosTx).run();

    assertEquals(1, getVideosTx.getVideoCount());
    assertVideoEquals(getVideo1(), getVideosTx.getVideo(0));

    getVideosTx = factory.getNext(getVideosTx);
    transport.add(getVideosTx).run();
    
    assertVideoEquals(getVideo2(), getVideosTx.getVideo(0));
    
    getVideosTx = factory.getPrev(getVideosTx);
    transport.add(getVideosTx).run();
    
    assertVideoEquals(getVideo1(), getVideosTx.getVideo(0));
  }
  
  public void testHasNextVideos() throws Exception {
    GetVideosTx getVideosTx = factory.getVideos().setCount(1);
    transport.add(getVideosTx).run();
    assertTrue(getVideosTx.hasNext());
    
    getVideosTx = factory.getNext(getVideosTx);
    transport.add(getVideosTx).run();
    assertTrue(!getVideosTx.hasNext());
  }

  private void assertVideoEquals(Video expected, Video actual) {
    assertEquals(expected.getDuration(), actual.getDuration());
    assertEquals(expected.getId(), actual.getId());
    assertEquals(expected.getURL(), actual.getURL());
    assertEquals(expected.getThumbnailUrl(), actual.getThumbnailUrl());
  }

  private List<Video> getExpectedVideos() throws JSONException {
    List<Video> videos = new ArrayList<Video>();
    
    videos.add(getVideo1());
    videos.add(getVideo2());
    
    return videos;
  }
  
  private Video getVideo1() throws JSONException {
    
    JSONObject videoJson =
      new JSONObject(
          "{'id':'1272362767:ad_0875146080','duration':56,'title':'Charlie " +
          "bit my finger - again !','container':'youtube','description':" +
          "'http://b61523eyi58zblasj7fjcwdrb1.hop.clickbank.net/?tid=HDCCB1\\n" +
          "Even had I thought of trying to get my boys to do this I probably " +
          "couldn\\'t have. Neither were coerced into any of this and neither " +
          "were hurt (for very long anyway).  This was just one of those " +
          "moments when I had the video camera out because the boys were " +
          "being fun and they provided something really very funny.\\n\\n" +
          "Harry and Charlie Blogging\\nhttp://harryandcharlie.blogspot.com/" +
          "\\n\\nTwitter\\nhttp://twitter.com/harryandcharlie'," +
          "'thumbnailUrl':'http://i.ytimg.com/vi/" +
          "_OBlgSz8sSM/default.jpg','type':'video','url':'http://www." +
          "youtube.com/watch?v=_OBlgSz8sSM'}");
  
    return new Video(videoJson);
  }
  
  private Video getVideo2() throws JSONException {
    
    JSONObject videoJson =
        new JSONObject(
            "{'id':'1272303269:ad_0875205578','duration':183,'title':" +
            "'Vande Mataram - Revival - A.R.Rahman','container':'youtube'," +
            "'description':'A.R.Rahman\\'s version of Bankim Chandra " +
            "Chattopadhyay\\'s original Vande Mataram composition - the track " +
            "\\'Revival\\' from his 1997 album Vande Mataram. Produced by " +
            "Bharatbala Productions','thumbnailUrl':" +
            "'http://i.ytimg.com/vi/MRPpSgRqtRc/default.jpg','type':'video'," +
            "'url':'http://www.youtube.com/watch?v=MRPpSgRqtRc'}");
    
    return new Video(videoJson);
  }
}
