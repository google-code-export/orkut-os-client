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

import net.oauth.OAuth;

import org.json.me.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A {@link Transaction} to upload a photo to an orkut album.
 *
 * @author Sachin Shenoy
 */
public class UploadPhotoTx extends Transaction {

  private final JSONObject mediaItem;
  private final String paramName;
  private final byte[] image;
  private final String type;
  private MultipartBuilder builder;

  public static class ImageType {
    public static final String PNG = "png";
    public static final String JPG = "jpg";
    public static final String GIF = "gif";
  }

  UploadPhotoTx(String albumId, byte[] image, String type, String title) {
    super(MethodNames.MEDIAITEMS_CREATE);
    this.image = image;
    this.type = type;

    paramName = "image" + request.getId();
    mediaItem = new JSONObject();
    Util.putJsonValue(mediaItem, "title", title);
    Util.putJsonValue(mediaItem, "url", "@field:" + paramName);
    request.setUserId(Constants.USERID_ME)
           .setGroupId(Group.SELF)
           .setAlbumId(albumId)
           .addParameter("mediaItem", mediaItem);
    builder = new MultipartBuilder();
  }

  public byte[] getBody() throws IOException {
    builder.addFile(paramName, "uploaded", "image/" + type, image);
    return builder.build();
  }

  public String getContentType() {
    return builder.getContentType();
  }
  
  public Collection<? extends Map.Entry> getParameters() {
    ArrayList<Entry<String, String>> params = new ArrayList<Map.Entry<String, String>>();
    params.add(new OAuth.Parameter("request", request.toString()));
    return params;
  }
}
