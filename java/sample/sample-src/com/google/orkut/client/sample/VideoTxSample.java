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

import com.google.orkut.client.api.GetVideosTx;
import com.google.orkut.client.api.VideoTxFactory;

import java.io.IOException;

/**
 * A Sample App to demonstrate the Videos API for getting the videos shared on an orkut profile
 *
 * @author Prashant Tiwari
 */
public class VideoTxSample {
  private final Transport transport;
  private final VideoTxFactory factory;
  
  public VideoTxSample(Transport transport) {
    this.transport = transport;
    factory = new VideoTxFactory();
  }

  public void run() throws IOException {
    GetVideosTx videos = fetchVideos();
    if (videos != null)
      fetchMoreVideos(videos);
  }
  
  /**
   * @return the videos fetched in the current transaction
   * @throws IOException
   */
  private GetVideosTx fetchVideos() throws IOException {
    GetVideosTx getVideosTx = factory.getVideos().setCount(1);
    transport.add(getVideosTx).run();
    if (getVideosTx.hasError()) {
      System.out.println("error fetching videos");
      return null;
    }
    printVideos(getVideosTx);
    return getVideosTx;
  }
  
  /**
   * shows the use of the getNext method to get more videos
   * 
   * @param getVideosTx the previously used GetVideosTx transaction
   * @return the next list of videos
   * @throws IOException
   */
  private GetVideosTx fetchMoreVideos(GetVideosTx getVideosTx) throws IOException {
    getVideosTx = factory.getNext(getVideosTx);
    transport.add(getVideosTx).run();
    if (getVideosTx.hasError()) {
      System.out.println("error fetching videos");
      return null;
    }
    printVideos(getVideosTx);
    return getVideosTx;
  }
  
  private void printVideos(GetVideosTx getVideosTx) {
    for (int i = 0; i < getVideosTx.getVideoCount(); i++) {
      System.out.println(getVideosTx.getVideo(i) + "\n");
    }
  }
}
