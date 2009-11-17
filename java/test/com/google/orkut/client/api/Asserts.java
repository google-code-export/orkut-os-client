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

import java.util.logging.Logger;

/**
 * Asserts for testing entities.
 * The following are checked for Activities ({@link ActivityEntry})
 *
 *
 * @author Shishir Birmiwal
 */
public class Asserts {
  private static final Logger LOG = Logger.getLogger(Asserts.class.getCanonicalName());
  private boolean foundBadFriendAddActivity;
  private boolean foundBadMakamakaActivity;
  private boolean foundBadPhotoCommentActivity;
  private boolean foundBadPhotoShareActivity;
  private boolean foundBadProfileUpdateActivity;
  private boolean foundBadScrapActivity;
  private boolean foundBadSocialEventActivity;
  private boolean foundBadStatusMessageActivity;
  private boolean foundBadTestimonialActivity;
  private boolean foundBadVideoShareActivity;

  private boolean friendAddActivitySeen;
  private boolean makamakaActivitySeen;
  private boolean photoCommentActivitySeen;
  private boolean photoShareActivitySeen;
  private boolean profileUpdateActivitySeen;
  private boolean scrapActivitySeen;
  private boolean socialEventActivitySeen;
  private boolean statusMessageActivitySeen;
  private boolean testimonialActivitySeen;
  private boolean videoShareActivitySeen;

  public boolean areFriendAddActivitiesSane() {
    if (!friendAddActivitySeen) {
      LOG.warning("FriendAddActivity not seen");
    }
    return friendAddActivitySeen && !foundBadFriendAddActivity;
  }

  public boolean areMakamakaActivitiesSane() {
    if (!makamakaActivitySeen) {
      LOG.warning("Makamaka Activity not seen");
    }
    return makamakaActivitySeen && !foundBadMakamakaActivity;
  }

  public boolean arePhotoCommentActivitiesSane() {
    if (!photoCommentActivitySeen) {
      LOG.warning("FriendAddActivity not seen");
    }
    return photoCommentActivitySeen && !foundBadPhotoCommentActivity;
  }

  public boolean arePhotoShareActivitiesSane() {
    if (!photoShareActivitySeen) {
      LOG.warning("PhotoShareActivities not seen");
    }
    return photoShareActivitySeen && !foundBadPhotoShareActivity;
  }

  public boolean areProfileUpdateActivitiesSane() {
    if (!profileUpdateActivitySeen) {
      LOG.warning("ProfileUpdateActivity not seen");
    }
    return profileUpdateActivitySeen && !foundBadProfileUpdateActivity;
  }

  public boolean areScrapActivitiesSane() {
    if (!scrapActivitySeen) {
      LOG.warning("ScrapActivity not seen");
    }
    return scrapActivitySeen && !foundBadScrapActivity;
  }

  public boolean areSocialEventActivitiesSane() {
    if (!socialEventActivitySeen) {
      LOG.warning("SocialEventActivity not seen");
    }
    return socialEventActivitySeen && !foundBadSocialEventActivity;
  }

  public boolean areStatusMessageActivitiesSane() {
    if (!statusMessageActivitySeen) {
      LOG.warning("StatusMessageActivity not seen");
    }
    return statusMessageActivitySeen && !foundBadStatusMessageActivity;
  }

  public boolean areTestimonialActivitiesSane() {
    if (!testimonialActivitySeen) {
      LOG.warning("TestimonialActivity not seen");
    }
    return testimonialActivitySeen && !foundBadTestimonialActivity;
  }

  public boolean areVideoShareActivitiesSane() {
    if (!videoShareActivitySeen) {
      LOG.warning("TestimonialActivity not seen");
    }
    return videoShareActivitySeen && !foundBadVideoShareActivity;
  }

  public void inspectFriendAddActivity(FriendAddActivity activity) {
    friendAddActivitySeen = true;
    foundBadFriendAddActivity |= isInvalidActivity(activity);
    foundBadFriendAddActivity |= isNull(activity.getPersonAId())
        || isNull(activity.getPersonBId())
        || isInvalidProfile(activity.getPersonAProfile())
        || isInvalidProfile(activity.getPersonBProfile());
  }

  public void inspectMakamakaActivity(MakamakaActivity activity) {
    makamakaActivitySeen = true;
    foundBadMakamakaActivity |= isInvalidActivity(activity);
    foundBadMakamakaActivity |= isNull(activity.getTitle())
        || isNull(activity.getBody());
  }

  public void inspectPhotoCommentActivity(PhotoCommentActivity activity) {
    photoCommentActivitySeen = true;
    foundBadPhotoCommentActivity |= isInvalidActivity(activity);
    foundBadPhotoCommentActivity |= isNull(activity.getAlbumId())
// TODO(birmiwal): uncomment this in a few weeks when all activities have album titles
//        || isNull(activity.getAlbumTitle())
        || isInvalidMediaItem(activity.getMediaItem())
        || isInvalidProfile(activity.getPhotoOwnerProfile())
        || isZero(activity.getCommentsCount());
    for (int i = 0; i < activity.getCommentsCount(); i++) {
      Comment comment = activity.getComment(i);
      foundBadPhotoCommentActivity |= isInvalidComment(comment);
          // TODO(birmiwal): fix this on backend
          // || isInvalidProfile(activity.getRelevantProfile(comment.getAuthorId()));
    }
  }

  public void inspectPhotoShareActivity(PhotoShareActivity activity) {
    photoShareActivitySeen = true;
    foundBadPhotoShareActivity |= isInvalidActivity(activity);
    foundBadPhotoShareActivity |= isZero(activity.getMediaItemCount());
    for (int i = 0; i < activity.getMediaItemCount(); i++) {
      foundBadPhotoShareActivity |= isInvalidMediaItem(activity.getMediaItem(i));
    }
  }

  public void inspectProfileUpdateActivity(ProfileUpdateActivity activity) {
    profileUpdateActivitySeen = true;
    foundBadProfileUpdateActivity |= isInvalidActivity(activity)
        || isZero(activity.getProfileFieldCount());
    for (int i = 0; i < activity.getProfileFieldCount(); i++) {
      foundBadProfileUpdateActivity |= isNull(activity.getProfileField(i));
    }
  }

  public void inspectScrapActivity(ScrapActivity activity) {
    scrapActivitySeen = true;
    foundBadScrapActivity |= isInvalidActivity(activity)
        || isNull(activity.getBody())
        || isNull(activity.getReceiverId())
        || isInvalidProfile(activity.getReceiverProfile())
        || isNull(activity.getSenderId())
        || isInvalidProfile(activity.getSenderProfile());
  }

  public void inspectSocialEventActivity(SocialEventActivity socialEventActivity) {
    socialEventActivitySeen = true;
    foundBadSocialEventActivity |= isInvalidActivity(socialEventActivity);
  }

  public void inspectStatusMessageActivity(StatusMessageActivity activity) {
    statusMessageActivitySeen = true;
    foundBadStatusMessageActivity |= isInvalidActivity(activity)
        || isNull(activity.getBody());
  }

  public void inspectTestimonialActivity(TestimonialActivity activity) {
    testimonialActivitySeen = true;
    foundBadTestimonialActivity |= isInvalidActivity(activity)
        || isNull(activity.getWriterId())
        || isNull(activity.getReceiverId())
        || isInvalidProfile(activity.getWriterProfile())
        || isInvalidProfile(activity.getReceiverProfile());
  }

  public void inspectVideoShareActivity(VideoShareActivity activity) {
    videoShareActivitySeen = true;
    foundBadVideoShareActivity |= isInvalidActivity(activity)
        || isZero(activity.getMediaItemCount());
    for (int i = 0; i < activity.getMediaItemCount(); i++) {
      foundBadVideoShareActivity |= isInvalidMediaItem(activity.getMediaItem(i));
    }
  }

  boolean isNull(Object o) {
    return o == null;
  }

  private boolean isInvalidActivity(ActivityEntry activity) {
    boolean isBadActivity = false;
    OrkutPerson profile = activity.getOwnerProfile();
    isBadActivity |= isNull(activity.getId())
        || isNull(activity.getOwnerId())
        || isNull(activity.getPostedTime())
        || isNull(profile);
    if (profile != null) {
      isBadActivity |= isInvalidProfile(profile);
    }
    isBadActivity |= isZero(activity.getRelevantUserIdCount());
    for (int i = 0; i < activity.getRelevantUserIdCount(); i++) {
      String userId = activity.getRelevantUserId(i);
      isBadActivity |= isNull(userId);
      OrkutPerson person = activity.getRelevantProfile(userId);
      isBadActivity |= isNull(person) || isInvalidProfile(person);
    }
    return isBadActivity;
  }

  private boolean isInvalidComment(Comment comment) {
    return isNull(comment)
        || isNull(comment.getAuthorId())
        || isNull(comment.getCreatedTime())
        || isNull(comment.getText());
  }

  private boolean isInvalidMediaItem(MediaItem item) {
    return isNull(item)
        || isNull(item.getId())
        || isNull(item.getType())
        || isNull(item.getUrl());
    // TODO(birmiwal): uncomment this when supported from backend
    //    || isInvalidPageUrl(item.getPageUrl())
    //    || (!item.getType().equals(Constants.MEDIA_ITEM_TYPE_VIDEO)
    //        || isInvalidYoutubeUrl(item.getYoutubeUrl()));
  }

  private boolean isInvalidPageUrl(String pageUrl) {
    return isNull(pageUrl)
        || !(pageUrl.contains("/Main#FavoriteVideoView") || pageUrl.contains("/Main#AlbumZoom"));
  }

  private boolean isInvalidProfile(OrkutPerson profile) {
    return isNull(profile)
        || isNull(profile.getFamilyName())
        || isNull(profile.getGivenName())
        || isNull(profile.getId())
        || isInvalidProfileUrl(profile.getProfileUrl())
        || isInvalidThumbnailUrl(profile.getThumbnailUrl());
  }

  private boolean isInvalidProfileUrl(String profileUrl) {
    return isNull(profileUrl)
        || !profileUrl.contains("Profile.aspx?")
        || !profileUrl.contains("uid=");
  }

  private boolean isInvalidThumbnailUrl(String thumbnailUrl) {
    return isNull(thumbnailUrl)
        || !thumbnailUrl.startsWith("http://")
        || !thumbnailUrl.contains("orkut.com")
        || !(thumbnailUrl.contains(".jpg") || thumbnailUrl.contains(".gif"));
  }

  private boolean isInvalidYoutubeUrl(String youtubeUrl) {
    return isNull(youtubeUrl)
        || !youtubeUrl.startsWith("http://")
        || !youtubeUrl.contains("youtube.com")
        || !youtubeUrl.contains("watch?")
        || !youtubeUrl.contains("?v=");
  }

  private boolean isZero(int count) {
    return count == 0;
  }
}
