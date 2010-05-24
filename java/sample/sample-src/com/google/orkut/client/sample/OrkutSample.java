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


/**
 * A Sample App to show usage of orkut client library.
 *
 * @author Sachin Shenoy
 */
public class OrkutSample {

  private final Transport transport;

  private OrkutSample(String propfile) throws Exception {
    transport = new Transport(propfile);
    transport.init();
  }

  private void run() throws Exception {
    new PhotosTxSample(transport).run();
    new FriendTxSample(transport).run();
    new AlbumsTxSample(transport).run();
    new ScrapTxSample(transport).run();
    new ProfileTxSample(transport).run();
    new ActivityTxSample(transport).run();
    new PhotoCommentsTxSample(transport).run();
    new VideoTxSample(transport).run();
  }

  public static void main(String[] args) throws Exception {
    if (args.length < 1) {
      System.out.println("please pass the file oauth.properties as the first argument");
      System.exit(-1);
    }
    new OrkutSample(args[0]).run();
  }
}
