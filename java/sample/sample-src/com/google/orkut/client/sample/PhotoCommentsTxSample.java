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

import com.google.orkut.client.api.CommentsEntry;
import com.google.orkut.client.api.CommentsTxFactory;
import com.google.orkut.client.api.GetPhotoCommentsTx;
import com.google.orkut.client.api.GetProfileTx;
import com.google.orkut.client.api.ProfileTxFactory;
import com.google.orkut.client.api.WritePhotoCommentsTx;

import java.io.IOException;

/**
 * Sample code for Photo comments.
 *
 * @author sachins@google.com (Sachin Shenoy)
 */
public class PhotoCommentsTxSample {

  private static final String TEST_PHOTO_COMMENT = "Test Photo Comment";
  private final Transport transport;
  private final CommentsTxFactory factory;
  private String userId;
  private String albumId;
  private String photoId;

  public PhotoCommentsTxSample(Transport transport) {
    this.transport = transport;
    factory = new CommentsTxFactory();
  }

  private void setUp() throws IOException {
    // For sample code for comments, we need a user, user's album and photo
    // on which will fetch comments, write and delete comments.
    userId = getSelfUserId();
    albumId = getAlbumId();
    photoId = getPhotoId(albumId);
  }

  public void run() throws IOException {
    setUp();
    fetchComments();
    String commentId = writeComments();
    if (commentId != null) {
      deleteComment(commentId);
    }
  }

  /** Sample to fetch photo comments */
  private void fetchComments() throws IOException {
    GetPhotoCommentsTx fetchPhotoComments = factory.getPhotoComments(userId, albumId, photoId);
    transport.add(fetchPhotoComments).run();

    if (fetchPhotoComments.hasError()) {
      // handle error
      return;
    }

    for (int i = 0; i < fetchPhotoComments.getCommentsCount(); ++i) {
      System.out.println("" + fetchPhotoComments.getComment(i));
    }
  }

  /** Sample on how to write a comment. */
  private String writeComments() throws IOException {
    WritePhotoCommentsTx writePhotoComments = factory.writePhotoComments(
        userId, albumId, photoId, TEST_PHOTO_COMMENT);
    transport.add(writePhotoComments).run();

    if (writePhotoComments.hasError()) {
      // handle error
      return null;
    }

    CommentsEntry commentsEntry = writePhotoComments.getCommentsEntry();

    // we return the commentId so that we can delete it next.
    return commentsEntry.getId();
  }

  /** Sample for deleting comments */
  private void deleteComment(String commentId) throws IOException {
    com.google.orkut.client.api.Transaction deletePhotoComment =
        factory.deletePhotoComments(userId, albumId, photoId, commentId);
    transport.add(deletePhotoComment).run();

    if (deletePhotoComment.hasError()) {
      // handle error.
      return;
    }
  }

  private String getSelfUserId() throws IOException {
    // This is here only to find the user-id of the logged in user (@self).
    ProfileTxFactory profileTxFactory = new ProfileTxFactory();
    GetProfileTx getProfileTx = profileTxFactory.getSelfProfile();
    transport.add(getProfileTx).run();
    return getProfileTx.getProfile().getId();
  }

  private String getAlbumId() throws IOException {
    return "5397133857837401222";
    /*
    AlbumsTxFactory albumTxFactory = new AlbumsTxFactory();
    GetAlbumsTx getAlbumsTx = albumTxFactory.get(userId, 1);
    transport.add(getAlbumsTx).run();

    return getAlbumsTx.getAlbum().getId(); */
  }

  private String getPhotoId(String albumId) throws IOException {
    return "1256618947809";
  }
}
