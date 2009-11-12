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

import com.google.orkut.client.api.Album.AclEntry;


/**
 * Constants used in orkut client library api.
 *
 * @author Shishir Birmiwal
 */
public class Constants {
  /** Possible values for the field returned by {@link AclEntry#getAccessorType()}. */
  public static class AlbumAccessorType {
    // Defines permission for an email address.
    public static final String EXTERNAL = "external";
    // Defines permission for a phone number.
    public static final String PHONE_NUMBER = "phoneNumber";
    // Defines permission for an orkut user.
    public static final String USER = "user";
  }

  /**
   * Possible values for the field returned by {@link AclEntry#getAccessType()}.
   * Note: When sharing with another user, set access-type to {@link AlbumAccessType#READ}.
   *       Other values may not have the desired effect (and are not fully supported at present).
   */
  public static class AlbumAccessType {
    public static final String CREATE = "create";
    public static final String DELETE = "delete";
    public static final String READ = "read";
    public static final String UPDATE = "update";
  }

  /**
   * Defines possible values for a person's gender value
   * as returned from {@link OrkutPerson#getGender()}.
   */
  public static class Gender {
    public static final String FEMALE = "female";
    public static final String MALE = "male";
  }

  /**
   * Defines possible values for a media-item in an activity,
   * as returned from {@link MediaItem#getType()}.
   */
  public static class MediaItemType {
    public static final String IMAGE = "image";
    public static final String VIDEO = "video";
  }

  /**
   * Defines the possible value to be sent in the call to
   * {@link UpdateProfileTx#setRelationshipStatus(String)}.
   */
  public static class RelationshipStatus {
    public static String COMMITTED = "committed";
    public static String MARRIED = "married";
    public static String OPEN_MARRIAGE = "open marriage";
    public static String OPEN_RELATIONSHIP = "open relationship";
    public static String SINGLE = "single";
  }

  /** A user-id which represents the logged in person. */
  public static final String USERID_ME = "@me";
}
