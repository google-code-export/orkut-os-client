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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Integration tests for Photos API.
 *
 * @author Shishir Birmiwal
 */
public class PhotosTest extends JaneDoeTestCase {
  static final String ALBUM_ID = "5400844753047135304";
  private Transport transport;
  private PhotosTxFactory factory;
  private Map<String, Photo> expectedPhotos;

  protected void setUp() throws Exception {
    super.setUp();
    transport = new Transport(AlbumsTest.OAUTH_PROPS_FILE);
    transport.init();

    expectedPhotos = getKnownPhotos();
    factory = new PhotosTxFactory();
  }

  public void testGetPhotos() throws Exception {
    if (doesNotMeetJaneDoeDependency(transport)) {
      // skipping test :(
      return;
    }
    GetPhotosTx getPhotosTx = factory.getPhotos(Constants.USERID_ME, ALBUM_ID);
    transport.add(getPhotosTx).run();

    assertEquals(expectedPhotos.size(), getPhotosTx.getPhotoCount());

    for (int i = 0; i < getPhotosTx.getPhotoCount(); i++) {
      Photo photo = getPhotosTx.getPhoto(i);
      assertPhotoEquals(expectedPhotos.get(photo.getId()), photo);
      printPhoto(getPhotosTx.getPhoto(i));
    }
  }

  public void testGetPhotosPagination() throws Exception {
    if (doesNotMeetJaneDoeDependency(transport)) {
      // skipping test :(
      return;
    }
    GetPhotosTx getPhotosTx = factory.getPhotos(Constants.USERID_ME, ALBUM_ID);
    transport.add(getPhotosTx).run();
    assertEquals(expectedPhotos.size(), getPhotosTx.getPhotoCount());

    List<String> photoIds = new ArrayList<String>();
    for (int i = 0; i < getPhotosTx.getPhotoCount(); i++) {
      photoIds.add(getPhotosTx.getPhoto(i).getId());
    }

    for (int pageSize = 1; pageSize <= 10; pageSize+=3) {
      int photoIndex = 0;

      getPhotosTx = factory.getPhotos(Constants.USERID_ME, ALBUM_ID);
      getPhotosTx.setCount(pageSize);
      transport.add(getPhotosTx).run();
      while (true) {
        int expected = pageSize;
        if (photoIndex + expected > expectedPhotos.size()) {
          expected = expectedPhotos.size() - photoIndex;
        }
        assertEquals(expected, getPhotosTx.getPhotoCount());

        for (int i = 0; i < getPhotosTx.getPhotoCount(); i++) {
          assertPhotoEquals(expectedPhotos.get(photoIds.get(photoIndex)), getPhotosTx.getPhoto(i));
          photoIndex++;
        }

        if (!getPhotosTx.canGetMorePhotos()) {
          break;
        }

        getPhotosTx = factory.getNextPhotos(getPhotosTx);
        transport.add(getPhotosTx).run();
      }
    }
  }

  private void assertPhotoEquals(Photo expected, Photo actual) {
    assertEquals(expected.getId(), actual.getId());
    assertEquals(expected.getAlbumId(), actual.getAlbumId());
    assertEquals(expected.getOwnerId(), actual.getOwnerId());
    assertEquals(expected.getTitle(), actual.getTitle());
    assertTrue(actual.getThumbnailUrl().contains(expected.getThumbnailUrl()));
    assertTrue(actual.getUrl().contains(expected.getUrl()));
  }

  private void printPhoto(Photo photo) {
    System.out.println("photoId: " + photo.getId() + "; albumId: " + photo.getAlbumId());
    System.out.println("thumbnailUrl: " + photo.getThumbnailUrl());
    System.out.println("url: " + photo.getUrl());
    System.out.println("title: " + photo.getTitle());
  }

  private Map<String, Photo> getKnownPhotos() {
    Map<String, Photo> map = new HashMap<String, Photo>();

    Photo photo = new Photo(Constants.USERID_ME, ALBUM_ID, "1257484193076");
    photo.setThumbnailUrl("orkut.com/images/milieu/1257482160/1257484193076/543695944/ep/Z1pg99jz.jpg");
    photo.setUrl("orkut.com/orkut/photos/OgAAALqcqfkodr7jZKffG5-n9BAD_q_es6YPhoZr7TBtf1oFITlmBOKGa5UVezI7JtY1jC6jW93tLPcSMNjWVqdinIcAm1T1UM56KeHSAxlAEXs5vErM2fVYPJbH.jpg");
    photo.setTitle("cinnamon");
    map.put(photo.getId(), photo);

    photo = new Photo(Constants.USERID_ME, ALBUM_ID, "1257484193077");
    photo.setThumbnailUrl("orkut.com/images/milieu/1257482160/1257484193077/543695944/ep/Z1tdykuh.jpg");
    photo.setUrl("orkut.com/orkut/photos/OgAAAEqsgLkMZgn8Y7Vlny_sBNLyo1N-eyWqr09Epk9IGoejH88KHNGm3doTafiQGZlol_EtG1ZRiiRJHWVcVifn1CwAm1T1UF3l2vubcK-BrHymmRgg_FnekcVg.jpg");
    photo.setTitle("water lily");
    map.put(photo.getId(), photo);

    photo = new Photo(Constants.USERID_ME, ALBUM_ID, "1257484239963");
    photo.setThumbnailUrl("orkut.com/images/milieu/1257482160/1257484239963/543695944/ep/Z1ip3ohq.jpg");
    photo.setUrl("orkut.com/orkut/photos/OgAAAJpi-VV_h0VqneCKjfpw-k7SWw9dCfDslOd6BpcsAarjSELh83uhuiysBt1ICKruXu1xFwMi8s4o3aPP_PtYcsUAm1T1UNi1PCkCa2pO8K82CIVSiEJCR6gN.jpg");
    photo.setTitle("a turtle");
    map.put(photo.getId(), photo);

    photo = new Photo(Constants.USERID_ME, ALBUM_ID, "1257484260418");
    photo.setThumbnailUrl("orkut.com/images/milieu/1257482160/1257484260418/543695944/ep/Zcqfz17.jpg");
    photo.setUrl("orkut.com/orkut/photos/OgAAACRAPapqnHUp34WCXNGdypnN97wVys-qtLAqLB2spKfQXqADkI4viuEQvdm_T6MY2YKeooqmi7XvVTb7AfjbdwkAm1T1UERRo-0sWLxR4oEWULQwfjUqk481.jpg");
    photo.setTitle("explorers of the lost ark!?");
    map.put(photo.getId(), photo);

    photo = new Photo(Constants.USERID_ME, ALBUM_ID, "1257484260419");
    photo.setThumbnailUrl("orkut.com/images/milieu/1257482160/1257484260419/543695944/ep/Z171ifkw.jpg");
    photo.setUrl("orkut.com/orkut/photos/OgAAAJBNkTyc_RZNh69vsPkquY2KIRvL0D0PjkQrz9C1aTtEtj_90P8n1SUyZ9X5yR-Q6ffHgk56EVfbrIdGIW7G43EAm1T1UKigjuyOmCkWhPLt5e9ubm2GGJMf.jpg");
    photo.setTitle("");
    map.put(photo.getId(), photo);

    photo = new Photo(Constants.USERID_ME, ALBUM_ID, "1257484285207");
    photo.setThumbnailUrl("orkut.com/images/milieu/1257482160/1257484285207/543695944/ep/Zqr3yrq.jpg");
    photo.setUrl("orkut.com/orkut/photos/OgAAAMh-w4bburdTfGrfutktjOa4wGTYCL1nyJHoA9BBky058lYOnf6H2Wp3VEaYrhyFoXRX5ndE9FS0zCEH4yN5KDYAm1T1UOfLWqWbaY8xjBRCM7eTgxPfZXGC.jpg");
    photo.setTitle("");
    map.put(photo.getId(), photo);

    photo = new Photo(Constants.USERID_ME, ALBUM_ID, "1257484285208");
    photo.setThumbnailUrl("orkut.com/images/milieu/1257482160/1257484285208/543695944/ep/Z1v9lgja.jpg");
    photo.setUrl("orkut.com/orkut/photos/OgAAABHj0tp2c5iXFeME-gzcWC9mt3qIm9HegV8NO2E_FOOWhIsCaaZr9MXKVmhvZfLSvNdLbXKI8KdH84DNFyJPzFYAm1T1UF0HLi0rktuOoPen-HkWdWWfmA2u.jpg");
    photo.setTitle("'paradise'");
    map.put(photo.getId(), photo);

    photo = new Photo(Constants.USERID_ME, ALBUM_ID, "1257484303529");
    photo.setThumbnailUrl("orkut.com/images/milieu/1257482160/1257484303529/543695944/ep/Zmk46bg.jpg");
    photo.setUrl("orkut.com/orkut/photos/OgAAAKiARNOxMiLN1dd_ZIAlHesYVcAg1e0TRHCVGzSmaC3OIKc9mR2d3Z5fWXP06GGiwGksfDD44QkY87Q6xAlRTBMAm1T1UBmaPXMa8nfzUU0ZLXieqrcBfgw6.jpg");
    photo.setTitle("kingfisher!");
    map.put(photo.getId(), photo);

    photo = new Photo(Constants.USERID_ME, ALBUM_ID, "1257484331190");
    photo.setThumbnailUrl("orkut.com/images/milieu/1257482160/1257484331190/543695944/ep/Zsk3701.jpg");
    photo.setUrl("orkut.com/orkut/photos/OgAAAAnNCNUDr0cZ77tHRwX8Eus4WS84B8beYABqPjEcBkhaXqf3SrPudU4HmD8jiJTWFIbdldu-1qwkonjwC9FsJqIAm1T1UKisdfQZT56MUIrj7dh11Jkk8pnI.jpg");
    photo.setTitle("");
    map.put(photo.getId(), photo);

    return map;
  }
}
