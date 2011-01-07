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

import org.junit.Test;

import junit.framework.TestCase;

/**
 * Tests for {@link Util}.
 *
 * @author Hari S?
 */
public class UtilTest extends TestCase {

  @Test
  public void testIsEmpty_null() {
    assertTrue(Util.isEmpty(null));
  }

  @Test
  public void testIsEmpty_emptyString() {
    assertTrue(Util.isEmpty(""));
  }

  @Test
  public void testIsEmpty_whiteSpace() {
    assertFalse(Util.isEmpty("  "));
  }

  @Test
  public void testIsEmpty_nonEmpty() {
    assertFalse(Util.isEmpty("a"));
  }

  @Test
  public void testIsEmptyOrWhiteSpace_null() {
    assertTrue(Util.isEmptyOrWhiteSpace(null));
  }

  @Test
  public void testIsEmptyOrWhiteSpace_emptyString() {
    assertTrue(Util.isEmptyOrWhiteSpace(""));
  }

  @Test
  public void testIsEmptyOrWhiteSpace_whiteSpace() {
    assertTrue(Util.isEmptyOrWhiteSpace(" \n\t  "));
  }

  @Test
  public void testIsEmptyOrWhiteSpace_nonEmpty() {
    assertFalse(Util.isEmptyOrWhiteSpace("a"));
  }
}
