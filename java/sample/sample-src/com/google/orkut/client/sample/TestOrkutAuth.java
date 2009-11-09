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

import com.google.orkut.client.api.CaptchaTxFactory;
import com.google.orkut.client.api.Transaction;

/**
 * A Sample App to test if authentication is fine.
 *
 * @author Sachin Shenoy
 */
public class TestOrkutAuth {

  private final Transport transport;

  private TestOrkutAuth(String propfile) throws Exception {
    transport = new Transport(propfile);
  }

  private void run() throws Exception {
    transport.init();
    CaptchaTxFactory captchaTxFactory = new CaptchaTxFactory();
    Transaction answerCaptcha = captchaTxFactory.answerCaptcha("token", "answer");
    transport.add(answerCaptcha).run();
    if (!answerCaptcha.hasError()) {
      System.out.println("*** Authentication PASSED ***");
    } else {
      System.out.println("Operation FAILED");
    }
  }
  
  public static void main(String[] args) throws Exception {
    if (args.length < 1) {
      System.out.println("please pass the file oauth.properties as the first argument");
      System.exit(-1);
    }
    try {
      new TestOrkutAuth(args[0]).run();
    } catch (Exception ope) {
      System.out.println("Authentication FAILED");
      throw ope;
    }
  }
}
