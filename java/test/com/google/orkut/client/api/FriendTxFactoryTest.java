package com.google.orkut.client.api;

import junit.framework.TestCase;

import org.json.me.JSONObject;

/**
 * 
 * @author Sachin Shenoy
 */
public class FriendTxFactoryTest extends TestCase {

  private static final FakeOrkutServer server = new FakeOrkutServer("sample/repo/orkut-db.json");
  private FriendTxFactory factory;
  private JsonComparator comparator;
  
  protected void setUp() throws Exception {
    super.setUp();
    factory = new FriendTxFactory();
    comparator = new JsonComparator(JsonComparator.SIMPLE);
  }

  public void testFriendInviteNotification() throws Exception {
    PendingFriendRequestTx requestTx = factory.getPendingFriendRequest();
    JSONObject json = requestTx.getRequestAsJson();
    json.remove("id");
    
    JSONObject entry = server.getRequest(FakeOrkutServer.FRIEND_INVITE_NOTIFICATIONS);
    assertTrue(comparator.isEquals(json, entry.getJSONObject("request")));
    requestTx.setResponse(entry.getJSONObject("reply"));
    assertEquals(2, requestTx.getPendingFriendRequestCount());
    
    FriendRequestEntry e = requestTx.getPendingFriendRequest(0);
    assertEquals("1234", e.getUserId());
    assertEquals("body 1234", e.getMessage());
    assertEquals("/Main#Profile.aspx?uid=11234", e.getUserProfile().getProfileUrl());
    assertEquals("1234", e.getUserProfile().getId());
    assertEquals("Dummy", e.getUserProfile().getFamilyName());
    assertEquals("Account", e.getUserProfile().getGivenName());
    assertEquals("http://img3.orkut.com/images/small/1234/1234/photo.jpg",
        e.getUserProfile().getThumbnailUrl());
    
    e = requestTx.getPendingFriendRequest(1);
    assertEquals("5678", e.getUserId());
    assertEquals("body 5678", e.getMessage());
    assertEquals("/Main#Profile.aspx?uid=55678", e.getUserProfile().getProfileUrl());
    assertEquals("5678", e.getUserProfile().getId());
    assertEquals("test", e.getUserProfile().getFamilyName());
    assertEquals("newui10", e.getUserProfile().getGivenName());
    assertEquals("http://img2.orkut.com/images/small/5678/5678/photo.jpg",
        e.getUserProfile().getThumbnailUrl());
  }
  
  public void testBirthdayNotificaiton() throws Exception {
    BirthdayNotificationTx requestTx = factory.getBirthdayNotification();
    JSONObject json = requestTx.getRequestAsJson();
    json.remove("id");
    
    JSONObject entry = server.getRequest(FakeOrkutServer.BIRTHDAY_NOTIFICATIONS);
    assertTrue(comparator.isEquals(json, entry.getJSONObject("request")));
    requestTx.setResponse(entry.getJSONObject("reply"));
    assertEquals(2, requestTx.getBirthdayNotificationCount());
    
    BirthdayNotificationEntry e = requestTx.getBirthdayNotification(0);
    assertEquals("1234", e.getUserId());
    assertEquals(22, e.getBirthDay());
    assertEquals(11, e.getBirthMonth());
    assertEquals("/Main#Profile.aspx?uid=11234", e.getUserProfile().getProfileUrl());
    assertEquals("1234", e.getUserProfile().getId());
    assertEquals("Dummy", e.getUserProfile().getFamilyName());
    assertEquals("Account", e.getUserProfile().getGivenName());
    assertEquals("http://img3.orkut.com/images/small/1234/1234/photo.jpg",
        e.getUserProfile().getThumbnailUrl());
    
    e = requestTx.getBirthdayNotification(1);
    assertEquals("5678", e.getUserId());
    assertEquals(23, e.getBirthDay());
    assertEquals(11, e.getBirthMonth());
    assertEquals("/Main#Profile.aspx?uid=55678", e.getUserProfile().getProfileUrl());
    assertEquals("5678", e.getUserProfile().getId());
    assertEquals("test", e.getUserProfile().getFamilyName());
    assertEquals("newui10", e.getUserProfile().getGivenName());
    assertEquals("http://img2.orkut.com/images/small/5678/5678/photo.jpg",
        e.getUserProfile().getThumbnailUrl());
    
  }
}
