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
import com.google.orkut.client.sample.Transport;

import junit.framework.TestCase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * These tests collate data from various test accounts and then check
 * data for consistency.
 *
 * Note: To run these tests, save required authorization tokens for
 * the test accounts in "java/test/com/google/orkut/client/api/account*.properties"
 *
 * @author Shishir Birmiwal
 */
public class FetchAllTests extends TestCase {
  private static final int FETCH_COUNT = 200;

  private static final String TRANSPORT_FILES_PATH = "test/com/google/orkut/client/api/";
  private static final String[] TRANSPORT_FILES = {
    "account0.properties",
    "account1.properties",
    "account2.properties",
    "account3.properties",
    "account4.properties",
    "account5.properties",
    "account6.properties",
    "account7.properties",
    "account8.properties",
    "account9.properties",
  };

  static List<Transport> transports;

  static List<ActivityEntry> activities;

  static <T> List<T> createList() {
    return new ArrayList<T>();
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();

    // create transports and fetch data only once as
    // they are expensive operations
    if (transports == null) {
      transports = createTransports();
      activities = createList();
      for (Transport transport : transports) {
        activities.addAll(fetchAllActivities(transport));
      }
    }
  }

  public void testActivities() throws Exception {
    Asserts asserts = new Asserts();
    for (ActivityEntry activity : activities) {
      if (ActivityType.FRIEND_ADD.equals(activity.type())) {
        FriendAddActivity friendAddActivity = (FriendAddActivity) activity;
        asserts.inspectFriendAddActivity(friendAddActivity);
      }

      if (ActivityType.MAKAMAKA.equals(activity.type())) {
        MakamakaActivity makamakaActivity = (MakamakaActivity) activity;
        asserts.inspectMakamakaActivity(makamakaActivity);
      }

      if (ActivityType.PHOTO.equals(activity.type())) {
        PhotoShareActivity photoShareActivity = (PhotoShareActivity) activity;
        asserts.inspectPhotoShareActivity(photoShareActivity);
      }

      if (ActivityType.PHOTO_COMMENT.equals(activity.type())) {
        PhotoCommentActivity photoCommentActivity = (PhotoCommentActivity) activity;
        asserts.inspectPhotoCommentActivity(photoCommentActivity);
      }

      if (ActivityType.PROFILE_UPDATE.equals(activity.type())) {
        ProfileUpdateActivity profileUpdateActivity = (ProfileUpdateActivity) activity;
        asserts.inspectProfileUpdateActivity(profileUpdateActivity);
      }

      if (ActivityType.SCRAP.equals(activity.type())) {
        ScrapActivity scrapActivity = (ScrapActivity) activity;
        asserts.inspectScrapActivity(scrapActivity);
      }

      if (ActivityType.SOCIAL_EVENTS_CREATION.equals(activity.type())) {
        SocialEventActivity socialEventActivity = (SocialEventActivity) activity;
        asserts.inspectSocialEventActivity(socialEventActivity);
      }

      if (ActivityType.STATUS_MSG.equals(activity.type())) {
        StatusMessageActivity statusMessageActivity = (StatusMessageActivity) activity;
        asserts.inspectStatusMessageActivity(statusMessageActivity);
      }

      if (ActivityType.TESTIMONIAL.equals(activity.type())) {
        TestimonialActivity testimonialActivity = (TestimonialActivity) activity;
        asserts.inspectTestimonialActivity(testimonialActivity);
      }

      if (ActivityType.VIDEO.equals(activity.type())) {
        VideoShareActivity videoShareActivity = (VideoShareActivity) activity;
        asserts.inspectVideoShareActivity(videoShareActivity);
      }
    }

    assertTrue("Friend Add Activities OK", asserts.areFriendAddActivitiesSane());
    assertTrue("Makamaka Activities OK", asserts.areMakamakaActivitiesSane());
    assertTrue("Photo Comment Activities OK", asserts.arePhotoCommentActivitiesSane());
    assertTrue("Photo Share Activities OK", asserts.arePhotoShareActivitiesSane());
    assertTrue("Profile Update Activities OK", asserts.areProfileUpdateActivitiesSane());
    // scrap activities aren't sent at the moment?
    // assertTrue("ScrapActivity OK", asserts.areScrapActivitiesSane());
    assertTrue("Social Event Activities OK", asserts.areSocialEventActivitiesSane());
    assertTrue("Status Message Activities OK", asserts.areStatusMessageActivitiesSane());
    assertTrue("Testimonial Activities OK", asserts.areTestimonialActivitiesSane());
    assertTrue("Video Share Activities OK", asserts.areVideoShareActivitiesSane());
  }

  private List<ActivityEntry> fetchAllActivities(Transport transport) throws IOException {
    List<ActivityEntry> activities = createList();

    ActivityTxFactory factory = new ActivityTxFactory();
    GetActivitiesTx getActivities = factory.getActivities();
    getActivities.setCount(FETCH_COUNT);
    getActivities.alsoGetRelevantProfiles().alsoGetPageUrls()
        .alsoGetYoutubeUrls();
    transport.add(getActivities).run();

    for (int i = 0; i < getActivities.getActivityCount(); i++) {
      activities.add(getActivities.getActivity(i));
    }

    return activities;
  }

  /**
   * Create authenticated transport channels to send requests.
   */
  private List<Transport> createTransports() throws Exception {
    List<Transport> transports = createList();

    for (int i = 0; i < TRANSPORT_FILES.length; i++) {
      Transport transport = new Transport(TRANSPORT_FILES_PATH + TRANSPORT_FILES[i]);
      transport.init();
      transports.add(transport);
    }

    return transports;
  }
}
