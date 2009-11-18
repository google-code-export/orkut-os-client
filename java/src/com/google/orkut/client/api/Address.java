// Copyright 2009 Google Inc. All Rights Reserved.

package com.google.orkut.client.api;

import org.json.me.JSONObject;

/**
 * The address of a user.
 *
 * @author Shishir Birmiwal
 */
public class Address {
  private final JSONObject json;

  public Address() {
    this(new JSONObject());
  }

  Address(JSONObject json) {
    this.json = json;
  }

  public String getCountryCode() {
    return json.optString(Fields.COUNTRY);
  }

  public String getRegion() {
    return json.optString(Fields.REGION);
  }

  public String getLocality() {
    return json.optString(Fields.LOCALITY);
  }

  public String getPostalCode() {
    return json.optString(Fields.POSTAL_CODE);
  }

  public Address setCountryCode(String code) {
    Util.putJsonValue(json, Fields.COUNTRY, code);
    return this;
  }

  public Address setRegion(String region) {
    Util.putJsonValue(json, Fields.REGION, region);
    return this;
  }

  public Address setLocality(String locality) {
    Util.putJsonValue(json, Fields.LOCALITY, locality);
    return this;
  }

  public Address setPostalCode(String postalCode) {
    Util.putJsonValue(json, Fields.POSTAL_CODE, postalCode);
    return this;
  }

  JSONObject getJson() {
    return json;
  }
}
