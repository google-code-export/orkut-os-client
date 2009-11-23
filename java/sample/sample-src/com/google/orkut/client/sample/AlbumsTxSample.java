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
import com.google.orkut.client.api.CreateAlbumTx;
import com.google.orkut.client.api.DeleteAlbumTx;
import com.google.orkut.client.api.GetAlbumsTx;
import com.google.orkut.client.api.OrkutError;
import com.google.orkut.client.api.Transaction;
import com.google.orkut.client.api.UpdateAlbumTx;

import java.io.IOException;

/**
 * A Sample App to demonstrate the Albums API for orkut.
 *
 * @author Shishir Birmiwal
 */
public class AlbumsTxSample {

  private final Transport transport;
  private final AlbumsTxFactory factory;

  public AlbumsTxSample(Transport transport) {
    this.transport = transport;
    factory = new AlbumsTxFactory();
  }

  public void run() throws IOException {
    Album album = createAlbum();
    album.setTitle("bleh bleh bleh");
    album.setDescription("yoohoo!");
    updateAlbum(album);
    shareAlbum(album);
    fetchAlbum(album.getId());
    deleteAlbum(album.getId());
    fetchAlbums();
  }

  private void shareAlbum(Album album) throws IOException {
    Transaction shareAlbumWithFriends = factory.shareAlbumWithFriends(album);
    transport.add(shareAlbumWithFriends).run();

    if (shareAlbumWithFriends.hasError()) {
      System.err.println("Error sharing album: " + shareAlbumWithFriends.getError().toString());
    }
  }

  private void deleteAlbum(String albumId) throws IOException {
    DeleteAlbumTx deleteAlbumTx = factory.deleteAlbum(albumId);
    transport.add(deleteAlbumTx).run();

    // TODO(birmiwal): Delete album deletes album and throws 500 internal error
//    if (deleteAlbumTx.hasError()) {
//      OrkutError err = deleteAlbumTx.getError();
//      throw new RuntimeException("unable to delete album:" + albumId + ";"
//          + err.code() + err.errorType() + err.message());
//    }
  }

  private void fetchAlbum(String albumId) throws IOException {
    GetAlbumsTx getAlbumsTx = factory.getAlbum(Constants.USERID_ME, albumId);
    transport.add(getAlbumsTx).run();

    if (getAlbumsTx.hasError()) {
      OrkutError err = getAlbumsTx.getError();
      throw new RuntimeException("unable to get album:" + albumId + ";" + err.toString());
    }

    printAlbum(getAlbumsTx.getAlbum());
  }

  private void updateAlbum(Album album) throws IOException {
    UpdateAlbumTx updateAlbumTx = factory.updateAlbum(album);
    transport.add(updateAlbumTx).run();

    if (updateAlbumTx.hasError()) {
      OrkutError err = updateAlbumTx.getError();
      throw new RuntimeException("unable to update album." + err.toString());
    }
  }

  private Album createAlbum() throws IOException {
    CreateAlbumTx createAlbumTx = factory.createAlbum("a new album", "blah blah blah");
    transport.add(createAlbumTx).run();

    if (createAlbumTx.hasError()) {
      OrkutError err = createAlbumTx.getError();
      throw new RuntimeException("unable to create album." + err.toString());
    }
    Album album = createAlbumTx.getAlbum();
    printAlbum(album);
    return album;
  }

  private void fetchAlbums() throws IOException {
    GetAlbumsTx getAlbumsTx = factory.getAlbums(Constants.USERID_ME);
    transport.add(getAlbumsTx).run();

    while (getAlbumsTx.getAlbumCount() != 0) {
      for (int i = 0; i < getAlbumsTx.getAlbumCount(); i++) {
        Album album = getAlbumsTx.getAlbum(i);
        printAlbum(album);
      }
      getAlbumsTx = factory.getNextAlbums(getAlbumsTx);
      transport.add(getAlbumsTx).run();
    }
  }

  private static void printAlbum(Album album) {
    System.out.println("--------------------------");
    System.out.println(album.getTitle());
    System.out.println(album.getDescription());
    System.out.println(album.getThumbnailUrl());
    System.out.println("album id " + album.getId());
    System.out.println(album.getOwnerId());
  }
}
