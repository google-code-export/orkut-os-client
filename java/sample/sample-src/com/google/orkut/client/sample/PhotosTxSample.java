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

import com.google.orkut.client.api.Album;
import com.google.orkut.client.api.AlbumsTxFactory;
import com.google.orkut.client.api.Constants;
import com.google.orkut.client.api.GetAlbumsTx;
import com.google.orkut.client.api.GetPhotosTx;
import com.google.orkut.client.api.Photo;
import com.google.orkut.client.api.PhotosTxFactory;
import com.google.orkut.client.api.UploadPhotoTx;
import com.google.orkut.client.api.Util;
import com.google.orkut.client.api.UploadPhotoTx.ImageType;

import org.json.me.JSONObject;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * A Sample App to demonstrate the Photos API for orkut.
 *
 * @author Shishir Birmiwal
 */
public class PhotosTxSample {

  private final Transport transport;
  private final AlbumsTxFactory albumsFactory;
  private final PhotosTxFactory photosFactory;

  public PhotosTxSample(Transport transport) {
    this.transport = transport;
    albumsFactory = new AlbumsTxFactory();
    photosFactory = new PhotosTxFactory();
  }

  public void run() throws Exception {
    Album album = fetchFirstAlbum();
    fetchAllPhotos(album);
    uploadPhoto(album);
  }

  private void fetchAllPhotos(Album album) throws IOException {
    // in this example, we paginate by 2 - but any reasonable number - like 20
    // can be used
    GetPhotosTx getPhotos = photosFactory.getPhotos(album);
    // or also photosFactory.get(album.getOwnerId(), album.getAlbumId(), numPhotosToFetch);

    transport.add(getPhotos).run();
    while (getPhotos.getPhotoCount() != 0) {
      for (int i = 0; i < getPhotos.getPhotoCount(); i++) {
        Photo photo = getPhotos.getPhoto(i);
        printPhoto(photo);
      }
      getPhotos = photosFactory.getNextPhotos(getPhotos);
      transport.add(getPhotos).run();
    }
  }
  
  private void uploadPhoto(Album album) throws Exception {
    byte[] image = loadImage("sample/repo/images/orkut-logo.png");
    System.out.println("Length := " + image.length);
    UploadPhotoTx uploadPhoto = photosFactory.uploadPhoto(
        album, image, ImageType.PNG, "My Image");
    
    String response = transport.sendRequest(
        uploadPhoto.getContentType(),
        uploadPhoto.getBody(),
        uploadPhoto.getParameters(),
        Util.getHttpVersionHeaderName(),
        Util.getHttpVersionHeaderValue());
    uploadPhoto.setResponse(new JSONObject(response));
  }

  private byte[] loadImage(String filename) throws Exception {
    File file = new File(filename); 
    //File length
    int size = (int)file.length(); 
    if (size > Integer.MAX_VALUE){
      System.out.println("File is to larger");
    }
    byte[] bytes = new byte[size]; 
    DataInputStream dis = new DataInputStream(new FileInputStream(file)); 
    int read = 0;
    int numRead = 0;
    while (read < bytes.length && (numRead=dis.read(bytes, read,
                                              bytes.length-read)) >= 0) {
      read = read + numRead;
    }
    System.out.println("File size: " + read);
    // Ensure all the bytes have been read in
    if (read < bytes.length) {
      return null;
    }
    return bytes;
  }

  private Album fetchFirstAlbum() throws IOException {
    GetAlbumsTx getAlbumTx = albumsFactory.getAlbums(Constants.USERID_ME);
    getAlbumTx.setCount(1);
    transport.add(getAlbumTx).run();
    return getAlbumTx.getAlbum();
  }

  private void printPhoto(Photo photo) {
    System.out.println("----------------------");
    System.out.println(photo.getTitle());
    System.out.println(photo.getId());
    System.out.println(photo.getOwnerId());
    System.out.println(photo.getThumbnailUrl());
    System.out.println(photo.getUrl());
  }
}
