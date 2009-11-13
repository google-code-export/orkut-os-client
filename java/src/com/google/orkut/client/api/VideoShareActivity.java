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


import org.json.me.JSONObject;

import java.util.Vector;

/**
 * An {@link ActivityEntry} which represents a video being shared on orkut.
 *
 * @author Sachin Shenoy
 */
public class VideoShareActivity extends ActivityEntry {

  private Vector mediaItems;

  public VideoShareActivity(JSONObject json) {
    super(json);
    parse(json);
  }

  private void parse(JSONObject json) {
    mediaItems = Util.forEachItemInList(json, Fields.MEDIA_ITEMS, new Converter() {
          Object convert(JSONObject json) {
            if (json == null) {
              throw new ConversionErrorException("media-item cannot be null");
            }
            return new MediaItem(json);
          }
        });
  }

  public String type() {
    return ActivityEntry.ActivityType.VIDEO;
  }

  public int getMediaItemCount() {
    return mediaItems.size();
  }

  public MediaItem getMediaItem(int index) {
    return (MediaItem) mediaItems.get(index);
  }
}