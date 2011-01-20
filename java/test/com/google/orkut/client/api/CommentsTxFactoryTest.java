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

import junit.framework.TestCase;

import org.json.me.JSONObject;

/**
 * Tests for {@link CommentsTxFactory}.
 * 
 * @author Sachin Shenoy
 */
public class CommentsTxFactoryTest extends TestCase {

  private static final String COMMENT_ID = "5678:123456789";
  private static final int COMMENT_CREATION_TIME = 123456789;
  private static final String AUTHOR_ID = "5678";
  private static final String PHOTO_COMMENT = "What is the puzzle?";
  private static final String REQUEST = "request";
  private static final String REPLY = "reply";
  private static final String PHOTO_ID = "2";
  private static final String ALBUM_ID = "1";
  private static final String USER_ID = "1234";
  private static final FakeOrkutServer server = new FakeOrkutServer("sample/repo/orkut-db.json");
  private CommentsTxFactory factory;
  private JsonComparator comparator;
  
  protected void setUp() throws Exception {
    super.setUp();
    factory = new CommentsTxFactory();
    comparator = new JsonComparator(JsonComparator.STRICT);
  }

  public void testGetPhotoComments() throws Exception {
    GetPhotoCommentsTx commentsTx = factory.getPhotoComments(USER_ID, ALBUM_ID, PHOTO_ID);
    JSONObject json = commentsTx.getRequestAsJson();
    json.remove("id");
    
    JSONObject entry = server.getRequest(FakeOrkutServer.GET_PHOTO_COMMENTS);
    assertTrue(comparator.isEquals(json, entry.getJSONObject(REQUEST)));
    commentsTx.setResponse(entry.getJSONObject(REPLY));
    
    assertFalse(commentsTx.hasError());
    assertEquals(1, commentsTx.getCommentsCount());
    CommentEntry comment = commentsTx.getComment(0);
    assertEquals(AUTHOR_ID, comment.getAuthorId());
    assertEquals(123456789, comment.getCreatedTime());
    assertEquals(COMMENT_ID, comment.getId());
    assertEquals(PHOTO_COMMENT, comment.getText());
  }
  
  public void testWritePhotoComment() throws Exception {
    WritePhotoCommentsTx commentsTx =
        factory.writePhotoComments(USER_ID, ALBUM_ID, PHOTO_ID, PHOTO_COMMENT);
    JSONObject json = commentsTx.getRequestAsJson();
    json.remove("id");
    
    JSONObject entry = server.getRequest(FakeOrkutServer.WRITE_PHOTO_COMMENTS);
    assertTrue(comparator.isEquals(json, entry.getJSONObject(REQUEST)));
    commentsTx.setResponse(entry.getJSONObject(REPLY));
    
    assertFalse(commentsTx.hasError());
    CommentEntry comment = commentsTx.getCommentEntry();
    assertEquals(AUTHOR_ID, comment.getAuthorId());
    assertEquals(COMMENT_CREATION_TIME, comment.getCreatedTime());
    assertEquals(COMMENT_ID, comment.getId());
    assertEquals(PHOTO_COMMENT, comment.getText());
  }
  
  public void testDeletePhotoComment() throws Exception {
    Transaction commentsTx = factory.deletePhotoComments(USER_ID, ALBUM_ID, PHOTO_ID, COMMENT_ID);
    JSONObject json = commentsTx.getRequestAsJson();
    json.remove("id");
    
    JSONObject entry = server.getRequest(FakeOrkutServer.DELETE_PHOTO_COMMENTS);
    assertTrue(comparator.isEquals(json, entry.getJSONObject(REQUEST)));
    commentsTx.setResponse(entry.getJSONObject(REPLY));
    
    assertFalse(commentsTx.hasError());
  }
}
