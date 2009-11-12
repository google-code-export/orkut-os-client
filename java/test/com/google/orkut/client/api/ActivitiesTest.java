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

import com.google.orkut.client.api.ActivityEntry.ActivityType;
import com.google.orkut.client.api.FriendsTest.JohnPoe;
import com.google.orkut.client.api.ProfileTest.JaneDoe;
import com.google.orkut.client.sample.Transport;

import junit.framework.TestCase;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

/**
 * Integration tests for Activities API.
 *
 * @author Shishir Birmiwal
 */
public class ActivitiesTest extends TestCase {
  private ActivityTxFactory factory;
  private Transport transport;
  private PhotoCommentActivity expectedPhotoCommentActivity;

  protected void setUp() throws Exception {
    super.setUp();

    expectedPhotoCommentActivity = getExpectedPhotoCommentActivity();
    factory = new ActivityTxFactory();
    transport = new Transport(AlbumsTest.OAUTH_PROPS_FILE);
    transport.init();
  }

  public void testGetActivities() throws Exception {
    boolean photoCommentActivityFound = false;

    GetActivitiesTx activities = factory.getActivities();

    transport.add(activities).run();

    while (true) {
      for (int i = 0; i < activities.getActivityCount(); i++) {
        ActivityEntry activity = activities.getActivity(i);
        if (ActivityType.PHOTO_COMMENT.equals(activity.type())) {
          PhotoCommentActivity photoCommentActivity = (PhotoCommentActivity) activity;
          assertPhotoCommentActivityEquals(expectedPhotoCommentActivity, photoCommentActivity);
          photoCommentActivityFound = true;
        }

        // TODO(birmiwal): Add tests for other types of activities
      }

      if (!activities.hasNext()) {
        break;
      } else {
        activities = factory.getNext(activities);
        transport.add(activities).run();
      }
    }

    assertTrue(photoCommentActivityFound);
  }


  void assertPhotoCommentActivityEquals(PhotoCommentActivity expected,
      PhotoCommentActivity actual) {
    assertEquals(expected.getId(), actual.getId());
    assertEquals(expected.getRelevantUserIdSize(), actual.getRelevantUserIdSize());
    for (int i = 0; i < expected.getRelevantUserIdSize(); i++) {
      assertEquals(expected.getRelevantUserId(i), actual.getRelevantUserId(i));
    }
    assertEquals(expected.getOwnerId(), actual.getOwnerId());
    assertEquals(expected.getAlbumId(), actual.getAlbumId());
    assertMediaItemEquals(expected.getMediaItem(), actual.getMediaItem());
    assertEquals(expected.getPostedTime(), actual.getPostedTime());
    assertEquals(expected.getCommentsCount(), actual.getCommentsCount());
    for (int i = 0; i < expected.getCommentsCount(); i++) {
      assertCommentEquals(expected.getComment(i), actual.getComment(i));
    }
  }

  private void assertCommentEquals(Comment expected, Comment actual) {
    assertEquals(expected.getAuthorId(), actual.getAuthorId());
    assertEquals(expected.getText(), actual.getText());
    assertEquals(expected.getCreatedTime(), actual.getCreatedTime());
  }

  private void assertMediaItemEquals(MediaItem expected, MediaItem actual) {
    assertTrue(actual.getUrl().contains(expected.getUrl()));
    assertEquals(expected.getType(), actual.getType());
    assertEquals(expected.getPageUrl(), actual.getPageUrl());
    assertEquals(expected.getId(), actual.getId());
  }

  PhotoCommentActivity getExpectedPhotoCommentActivity() throws JSONException {
    JSONObject json = new JSONObject();
    JSONArray relevantUserIds = new JSONArray();
    relevantUserIds.put(JaneDoe.ID);
    Util.putJsonValue(json, Fields.RELEVANT_USER_IDS, relevantUserIds);
    Util.putJsonValue(json, Fields.POSTED_TIME, 1258004895);
    Util.putJsonValue(json, Params.USER_ID, JaneDoe.ID);
    Util.putJsonValue(json, Fields.ID, "1655875281");
    Util.putJsonValue(json, Fields.TEMPLATE_PARAMS, new JSONObject("{'activityType':'PHOTO_COMMENT', 'albumId':'5400844972090467400'}"));
    Util.putJsonValue(json, Fields.MEDIA_ITEMS, new JSONArray("[{'url':'orkut.com/images/milieu/1257482211/1257484424232/543695944/ep/Znuzhin.jpg',"
    		+ "'type':'image','id':'1257484424232'}]"));
    Util.putJsonValue(json, Fields.COMMENTS, new JSONArray("[{'authorId':'" + JohnPoe.ID + "', 'text':'i love goa!', 'created':'1258004895'}]"));
    PhotoCommentActivity commentActivity = new PhotoCommentActivity(json );
    return commentActivity ;
  }
}
