// Copyright 2009 Google Inc. All Rights Reserved.

package com.google.orkut.client.api;

import com.google.orkut.client.sample.Transport;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Integration tests for Albums API.
 * This test should be run with the (logged in) user oocl17 at gmail.
 *
 * @author Shishir Birmiwal
 */
public class AlbumsTest extends TestCase {
  private static final int MAX_COUNT = 100;
  private static final String OAUTH_PROPS_FILE = "sample/oauth.properties";

  private AlbumsTxFactory factory;
  private Map<String, Album> fixedAlbumsMap;

  private Transport transport;

  public void testGetPhotos() throws Exception {
    GetAlbumsTx albumsTx = factory.getAlbums(Constants.USERID_ME);
    albumsTx.setCount(MAX_COUNT);
    transport.add(albumsTx).run();
    assertEquals(fixedAlbumsMap.size(), albumsTx.getAlbumCount());

    for (int i = 0; i < albumsTx.getAlbumCount(); i++) {
      Album album = albumsTx.getAlbum(i);
      assertAlbumEquals(album, fixedAlbumsMap.get(album.getId()));
    }
  }

  public void testGetPhotosPagination() throws Exception {
    GetAlbumsTx albumsTx = factory.getAlbums(Constants.USERID_ME);
    albumsTx.setCount(fixedAlbumsMap.size());
    transport.add(albumsTx).run();
    assertEquals(fixedAlbumsMap.size(), albumsTx.getAlbumCount());

    List<Album> albums = new ArrayList<Album>();
    for (int i = 0; i < albumsTx.getAlbumCount(); i++) {
      albums.add(albumsTx.getAlbum(i));
    }

    albumsTx = factory.getAlbums(Constants.USERID_ME);
    albumsTx.setCount(1);
    transport.add(albumsTx).run();
    assertAlbumEquals(albumsTx.getAlbum(), albums.get(0));

    albumsTx = factory.getNextAlbums(albumsTx);
    transport.add(albumsTx).run();
    assertAlbumEquals(albumsTx.getAlbum(), albums.get(1));

    albumsTx = factory.getNextAlbums(albumsTx);
    albumsTx.setCount(2);
    transport.add(albumsTx).run();
    assertAlbumEquals(albumsTx.getAlbum(0), albums.get(2));
    assertAlbumEquals(albumsTx.getAlbum(1), albums.get(3));
  }

  protected void setUp() throws Exception {
    super.setUp();
    transport = new Transport(OAUTH_PROPS_FILE);
    transport.init();

    fixedAlbumsMap = getFixedAlbums();
    factory = new AlbumsTxFactory();
  }

  private void assertAlbumEquals(Album fromServer, Album expected) {
    assertEquals(fromServer.getId(), expected.getId());
    assertTrue(fromServer.getThumbnailUrl().contains(expected.getThumbnailUrl()));
    assertEquals(fromServer.getTitle(), expected.getTitle());
    assertEquals(fromServer.getDescription(), expected.getDescription());
    assertEquals(fromServer.getNumPhotos(), expected.getNumPhotos());
    assertEquals(fromServer.getOwnerId(), expected.getOwnerId());
  }

  private Map<String, Album> getFixedAlbums() {
    Map<String, Album> map = new HashMap<String, Album>();

    // Empty Album
    Album album = new Album("02776157447964356030", "5400848279215285320");
    album.setDescription("This is an empty album");
    album.setTitle("Empty Album");
    album.setNumPhotos(0);
    album.setThumbnailUrl("orkut.com/img/i_noalbumcover160.jpg");
    map.put(album.getId(), album);

    // Random Photos Album
    album = new Album("02776157447964356030", "5400845139594191944");
    album.setTitle("Random Photos");
    album.setDescription("Random Photos From Everywhere");
    album.setNumPhotos(8);
    album.setThumbnailUrl("orkut.com/images/milieu/1257482250/1257484502052/543695944/ep/Z63ddtb.jpg");
    map.put(album.getId(), album);

    // Goa album
    album = new Album("02776157447964356030", "5400844972090467400");
    album.setTitle("Goa");
    album.setDescription("Photos from Goa");
    album.setNumPhotos(3);
    album.setThumbnailUrl("orkut.com/images/milieu/1257482211/1257484405451/543695944/ep/Z1umc0h9.jpg");
    map.put(album.getId(), album);

    // Sri Lanka
    album = new Album("02776157447964356030", "5400844753047135304");
    album.setTitle("Sri Lanka");
    album.setDescription("Photos from Sri Lanka");
    album.setNumPhotos(9);
    album.setThumbnailUrl("orkut.com/images/milieu/1257482160/1257484285208/543695944/ep/Z1v9lgja.jpg");
    map.put(album.getId(), album);

    return map;
  }

  // for debugging
  private void printAlbum(Album album) {
    System.out.println(album.getId());
    System.out.println(album.getTitle());
    System.out.println(album.getDescription());
    System.out.println(album.getOwnerId());
    System.out.println(album.getThumbnailUrl());
    System.out.println(album.getNumPhotos());
  }
}
