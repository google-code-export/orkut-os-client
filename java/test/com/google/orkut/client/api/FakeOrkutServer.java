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

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Fake server to make testing of Client library easy.
 * 
 * @author Sachin Shenoy
 */
public class FakeOrkutServer {
  public static final String FRIEND_INVITE_NOTIFICATIONS = "friend-invite-notifications";
  public static final String BIRTHDAY_NOTIFICATIONS = "birthday-notificaions";
  private JSONObject entry;
  
  public FakeOrkutServer(String file) {
   try {
     File dir1 = new File (".");
     System.out.println("" + dir1.getCanonicalPath());
     String readFile = readFile(file);
     entry = new JSONObject(readFile);
   } catch (JSONException e) {
     e.printStackTrace();
   } catch (IOException e) {
     
   }
  }
  
  String readFile(String file) {
    try {
      FileInputStream fstream = new FileInputStream(file);
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String strLine;
      //Read File Line By Line
      StringBuilder builder = new StringBuilder();
      while ((strLine = br.readLine()) != null)   {
        // Print the content on the console
        builder.append(strLine);
      }
      in.close();
      return builder.toString();
    } catch (IOException e){
      System.err.println("Error: " + e.getMessage());
      return null;
    }
  }
  

  JSONObject getRequest(String requestType) throws JSONException {
    return entry.getJSONObject(requestType); 
  }
}
