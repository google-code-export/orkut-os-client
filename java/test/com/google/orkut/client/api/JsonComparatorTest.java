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

import junit.framework.TestCase;

import org.json.me.JSONArray;
import org.json.me.JSONObject;

/**
 * Tests for {@link JsonComparator}
 * 
 * @author Sachin Shenoy
 */
public class JsonComparatorTest extends TestCase {

  private JsonComparator strict;
  private JsonComparator simple;

  protected void setUp() throws Exception {
    super.setUp();
    strict = new JsonComparator(JsonComparator.STRICT);
    simple = new JsonComparator(JsonComparator.SIMPLE);
  }
  
  /** Begin JSONObject **/
  
  public void testJsonObject_stringValueMatches() throws Exception {
    JSONObject a = new JSONObject("{'a':'b'}");
    JSONObject b = new JSONObject("{'a' : 'b'}");
    assertTrue(strict.isEquals(a, b));
    assertTrue(simple.isEquals(a, b));
  }

  public void testJsonObject_jsonObjectValueMatches() throws Exception {
    JSONObject a = new JSONObject("{'a': {'xkcd' : 'super!'}}");
    JSONObject b = new JSONObject("{'a':{'xkcd':'super!'}}");
    assertTrue(strict.isEquals(a, b));
    assertTrue(simple.isEquals(a, b));
  }

  public void testJsonObject_jsonArrayValueMatches() throws Exception {
    JSONObject a = new JSONObject("{'a':['1', '2', 'hi']}");
    JSONObject b = new JSONObject("{'a':['1', '2', 'hi']}");
    assertTrue(strict.isEquals(a, b));
    assertTrue(simple.isEquals(a, b));
  }

  public void testJsonObject_longValueMatches() throws Exception {
    JSONObject a = new JSONObject("{'a': 123456}");
    JSONObject b = new JSONObject("{'a' : 123456}");
    assertTrue(strict.isEquals(a, b));
    assertTrue(simple.isEquals(a, b));
  }

  public void testJsonObject_booleanValueMatches() throws Exception {
    JSONObject a = new JSONObject("{'a': true}");
    JSONObject b = new JSONObject("{'a' : true}");
    assertTrue(strict.isEquals(a, b));
    assertTrue(simple.isEquals(a, b));
  }

  public void testJsonObject_extraStringElement() throws Exception {
    JSONObject a = new JSONObject("{'a':'b'}");
    JSONObject b = new JSONObject("{'c' : 'd', 'a':'b'}");
    assertFalse(strict.isEquals(a, b));
    assertTrue(simple.isEquals(a, b));
  }

  public void testJsonObject_stringValueDoesNotMatch() throws Exception {
    JSONObject a = new JSONObject("{'a':'b'}");
    JSONObject b = new JSONObject("{'a':'c'}");
    assertFalse(strict.isEquals(a, b));
    assertFalse(simple.isEquals(a, b));
  }

  public void testJsonObject_jsonObjectValueDoesNotMatches() throws Exception {
    JSONObject a = new JSONObject("{'a': {'xkcd' : 'super!'}}");
    JSONObject b = new JSONObject("{'a':{'abcd':'super!'}}");
    assertFalse(strict.isEquals(a, b));
    assertFalse(simple.isEquals(a, b));
  }

  public void testJsonObject_jsonArrayValueDoesNotMatches() throws Exception {
    JSONObject a = new JSONObject("{'a':['1', '3', 'hi']}");
    JSONObject b = new JSONObject("{'a':['1', '2', 'hi']}");
    assertFalse(strict.isEquals(a, b));
    assertFalse(simple.isEquals(a, b));
  }

  public void testJsonObject_longValueDoesNotMatch() throws Exception {
    JSONObject a = new JSONObject("{'a':1234}");
    JSONObject b = new JSONObject("{'a':12345}");
    assertFalse(strict.isEquals(a, b));
    assertFalse(simple.isEquals(a, b));
  }

  public void testJsonObject_booleanValueDoesNotMatch() throws Exception {
    JSONObject a = new JSONObject("{'a':true}");
    JSONObject b = new JSONObject("{'a':false}");
    assertFalse(strict.isEquals(a, b));
    assertFalse(simple.isEquals(a, b));
  }
  /** End JSONObject **/
  
  /** Begin JSONArray **/
  public void testJsonArray_valueMatches() throws Exception {
    JSONArray a = new JSONArray("[['two'], 'a', 123, true, {'name':'good'}, ['hello']]");
    JSONArray b = new JSONArray("[true, 'a', ['hello'], {'name':'good'}, 123, ['two']]");
    assertTrue(strict.isEquals(a, b));
    assertTrue(simple.isEquals(a, b));
  }
  
  public void testJsonArray_extraElement() throws Exception {
    JSONArray a = new JSONArray("['a', 123, 'extra', true]");
    JSONArray b = new JSONArray("[true, 'a', 123]");
    assertFalse(strict.isEquals(a, b));
    assertFalse(simple.isEquals(a, b));
  }
  /** End JSONArray **/
}
