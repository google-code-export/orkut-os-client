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

import junit.framework.TestCase;

/**
 * Integration tests for Photos API.
 * 
 * @author Shishir Birmiwal
 */
public class PhotosTest extends TestCase {
  static final String ALBUM_ID = "5400844753047135304";
  
  private Transport transport;
  private PhotosTxFactory factory;

  protected void setUp() throws Exception {
    super.setUp();
    transport = new Transport(AlbumsTest.OAUTH_PROPS_FILE);
    transport.init();
    
    factory = new PhotosTxFactory();
  }
  
  public void testGetPhotos() throws Exception {
    GetPhotosTx getPhotosTx = factory.getPhotos(Constants.USERID_ME, ALBUM_ID);
    transport.add(getPhotosTx).run();
    
    for (int i = 0; i < getPhotosTx.getPhotoCount(); i++) {
      printPhoto(getPhotosTx.getPhoto(i));
    }
  }

  private void printPhoto(Photo photo) {
    System.out.println("photoId: " + photo.getId() + "; albumId: " + photo.getAlbumId());
    System.out.println("thumbnailUrl: " + photo.getThumbnailUrl());
    System.out.println("url: " + photo.getUrl());
    System.out.println("title: " + photo.getTitle());
  }
}
