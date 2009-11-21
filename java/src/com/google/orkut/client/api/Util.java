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

import java.util.Vector;

/**
 * Contains utility functions required by the orkut client library
 *
 * @author Hari S
 */
public class Util {
  private Util() {
  }


  public static String getHttpVersionHeaderName() {
    return InternalConstants.ORKUT_CLIENT_LIB_HEADER;
  }

  public static String getHttpVersionHeaderValue() {
    return InternalConstants.VERSION_STRING;
  }

  public static boolean isEmpty(String str) {
    return null == str || "".equals(str);
  }

  public static boolean isEmptyOrWhiteSpace(String str) {
    return null == str || 0 == str.trim().length();
  }

  static String getRuntimeErrorMessage(String methodName) {
    return methodName + " : Unexpected exception ";
  }

  static void putJsonValue(JSONObject json, String key, Object value) {
    try {
      json.put(key, value);
    } catch (JSONException e) {
      throw new RuntimeException("Null key while writing into json", e);
    }
  }


  static Vector forEachItemInList(JSONObject data, String key, Converter processor) {
    Vector items = new Vector();
    try {
      JSONArray itemList = data.getJSONArray(key);
      if (itemList == null) {
        return items;
      }
      int numItems = itemList.length();
      for (int i = 0; i < numItems; i++) {
        JSONObject json = itemList.getJSONObject(i);
        try {
          items.add(processor.convert(json));
        } catch (CreationException e) {
          // ignore and skip conversion of this item
        } catch (RuntimeException e) {
          // we skip any runtime exception too.
        }
      }
    } catch (JSONException jse) {
      throw new RuntimeException("Unexpected json exception.", jse);
    }
    return items;
  }

  static class JSONUtil {

    static String getRequiredStringField(String fieldName, JSONObject dataObject) {
      try {
        return dataObject.getString(fieldName);
      } catch (JSONException jse) {
        throw new RequiredFieldNotFoundException(fieldName, dataObject);
      }
    }

    static int getRequiredIntField(String fieldName, JSONObject dataObject) {
      try {
        return dataObject.getInt(fieldName);
      } catch (JSONException jse) {
        throw new RequiredFieldNotFoundException(fieldName, dataObject);
      }
    }

    static JSONObject getRequiredJSONObjectField(String fieldName, JSONObject dataObject) {
      try {
        return dataObject.getJSONObject(fieldName);
      } catch (JSONException jse) {
        throw new RequiredFieldNotFoundException(fieldName, dataObject);
      }
    }
  }
}
