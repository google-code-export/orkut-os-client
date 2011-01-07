/* Sample app to test that the binary release of the library is working.
 *
 * Place your consumer key and secret in the appropriate fields,
 * then simply link this against orkut-os-client-<releasename>.jar
 * and run.
 */

import com.google.orkut.client.api.OrkutPerson;
import com.google.orkut.client.api.OrkutAdapter;
import com.google.orkut.client.api.BatchTransaction;
import com.google.orkut.client.api.OrkutAdapterDebugListener;
import com.google.orkut.client.api.GetProfileTx;
import com.google.orkut.client.api.GetFriendTx;
import com.google.orkut.client.api.UpdateProfileTx;
import com.google.orkut.client.api.GetActivitiesTx;
import com.google.orkut.client.api.BirthdayNotificationTx;
import com.google.orkut.client.api.BirthdayNotificationEntry;
import com.google.orkut.client.api.ActivityEntry;
import com.google.orkut.client.api.PostActivityTx;
import com.google.orkut.client.api.GetScrapsTx;
import com.google.orkut.client.api.WriteScrapTx;
import com.google.orkut.client.api.ScrapEntry;
import com.google.orkut.client.api.OrkutError;
import com.google.orkut.client.api.Album;
import com.google.orkut.client.api.GetAlbumsTx;
import com.google.orkut.client.api.CreateAlbumTx;
import com.google.orkut.client.api.UpdateAlbumTx;
import com.google.orkut.client.api.DeleteAlbumTx;
import com.google.orkut.client.api.GetPhotosTx;
import com.google.orkut.client.api.Photo;
import com.google.orkut.client.api.UploadPhotoTx;

import java.util.Properties;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;

public class Sample {
   public static final String CALLBACK_URL = 
                 "http://orkut-os-client-test.appspot.com/debugcallback";
   public static void say(String s) { System.err.println(s); }

   public static void main_inner() throws Exception {
      say("Reading key file");
      Properties props = new Properties();

      try {
         FileInputStream fin = new FileInputStream("key.cfg");
         props.load(fin);
         fin.close();
      }
      catch (java.io.FileNotFoundException ex) {
         say("The key.cfg configuration file was not found.");
         say("Please create it using key.cfg.template as an example.");
         say("You must place your consumer key/secret in this file.");
         say("For more information, please see the README file.");
         return;
      }

      String consumerKey = props.getProperty("consumerKey");
      String consumerSecret = props.getProperty("consumerSecret");

      if (consumerKey.equals("your.consumer.key.here") ||
          consumerSecret.equals("YourConsumerSecretHere")) {

         say("");
         say("You haven't set your consumerKey and consumerSecret in");
         say("the key file (key.cfg). Please do that.");
         say("For more info on this, see README.");
         return;
      }

      say("Consumer key: " + consumerKey);
      say("Consumer secret: " + consumerSecret);

      say("Setting up adapter.");
      OrkutAdapter orkad = new OrkutAdapter(consumerKey,
                consumerSecret, CALLBACK_URL, false,
                new OrkutAdapterDebugListener() {
                   public void printOrkutAdapterMessage(String s) {
                      System.err.println(s);
                   }
      });

      say("Would you like to [a]uthenticate or [l]oad an access pass?");
      if (readline().toLowerCase().startsWith("a")) {
         // authenticate
         say("Requesting authorization URL from adapter.");
         String authURL = orkad.requestAuthURL();
   
         say("");
         say("Authorization URL: " + authURL);
         say("");
         say("Now launch a browser with this URL, log in, let it redirect to");
         say("the callback URL, look for the oauth_verifier parameter and ");
         say("copy it here to proceed with authentication.");
         say("");
   
         say("Input verifier code: ");
         String verifier = readline();
   
         say("Authenticating.");
         orkad.authenticate(verifier);
   
         say("Got access pass: " + orkad.getAccessPass());
   
         say("Would you like to save this access pass for future use [y/n]?");
         if (readline().toLowerCase().startsWith("y"))
            saveAccessPass(orkad);  // save for future use
      }
      else {
         // load access pass
         if (!loadAccessPass(orkad)) return;
      }

      // At this point, setup is complete. We may now access the user's
      // data by making transactions.
      boolean quit = false;
      while (!quit) {
         say("");
         say("COMMANDS:");
         say("i         Who am I");
         say("lsf       List my friends");
         say("lsac      List my activities");
         say("ups       Update status");
         say("gp        Get extended profile info");
         say("gbn       Get birthday notifications");
         say("pac       Post new activity");
         say("lssc      List scraps");
         say("psc       Post scrap");
         say("lsal      List albums");
         say("cral      Create album");
         say("upal      Update album");
         say("rmal      Delete album");
         say("lsph      Get photos in album");
         say("upph      Upload photo to an album");
         say("q         Quit");
         say("");
         say("Enter your choice:");
         String choice = readline().toLowerCase();
         say("");
         if      (choice.equals(""))     continue;
         else if (choice.equals("i"))    whoAmI(orkad);
         else if (choice.equals("lsf"))  listFriends(orkad);
         else if (choice.equals("lsac")) listActivities(orkad);
         else if (choice.equals("ups"))  updateStatus(orkad);
         else if (choice.equals("gp"))   getFullProfile(orkad);
         else if (choice.equals("gbn"))  getBirthdayNotifications(orkad);
         else if (choice.equals("pac"))  postNewActivity(orkad);
         else if (choice.equals("lssc")) getScraps(orkad);
         else if (choice.equals("psc"))  writeScrap(orkad);
         else if (choice.equals("lsal")) listAlbums(orkad);
         else if (choice.equals("cral")) createAlbum(orkad);
         else if (choice.equals("rmal")) deleteAlbum(orkad);
         else if (choice.equals("lsph")) listPhotos(orkad);
         else if (choice.equals("upph")) uploadPhoto(orkad);
         else if (choice.equals("q"))    quit = true;
         else                            say("Invalid choice");
      }
   }

   public static void saveAccessPass(OrkutAdapter orkad) throws Exception {
      say("Saving access pass to file: .access_pass...");
      Properties p = new Properties();
      p.setProperty("accesspass", orkad.getAccessPass());
      FileOutputStream fout = new FileOutputStream(".access_pass");
      p.store(fout, "access pass");
      fout.close();
   }

   public static boolean loadAccessPass(OrkutAdapter orkad) 
                                                throws Exception {
      say("Load access pass from file (.access_pass)...");
      Properties p = new Properties();
      try {
         FileInputStream fin = new FileInputStream(".access_pass");
         p.load(fin);
         fin.close();
         orkad.setAccessPass(p.getProperty("accesspass"));
         return true;
      }
      catch (Exception ex) {
         if (ex instanceof FileNotFoundException)
            say("Access pass file not found.");
         else ex.printStackTrace();
         return false;
      }
   }

   public static void whoAmI(OrkutAdapter orkad) throws Exception {
      BatchTransaction btx = orkad.newBatch();
      GetProfileTx profile = orkad.profileTxFactory.getSelfProfile();
      btx.add(profile);

      say("Getting self profile...");
      orkad.submitBatch(btx);
      say("...done.");

      if (profile.hasError()) {
         say("*** Error in transaction.");
         return;
      }

      OrkutPerson person = profile.getProfile();
      say("You are: " + person.getGivenName() + " " +
                person.getFamilyName());
      pause();
   }

   public static void listFriends(OrkutAdapter orkad) throws Exception {
      BatchTransaction btx = orkad.newBatch();
      GetFriendTx friends = orkad.friendTxFactory.getSelfFriends();
      friends.setCount(100); // get a maximum of 100 friends
      btx.add(friends);

      say("Getting friends...");
      orkad.submitBatch(btx);
      say("...done.");

      if (friends.hasError()) {
         say("*** Error in transaction.");
         return;
      }

      say("You have " + String.valueOf(friends.getFriendsCount()) + 
                        " friend(s):");
      int i;
      for (i = 0; i < friends.getFriendsCount(); i++) {
         OrkutPerson f = friends.getFriend(i);
         say("- " + f.getDisplayName() + " (id: " + f.getId() + ")");
      }
      pause();
   }
            
   public static void updateStatus(OrkutAdapter orkad) throws Exception {
      UpdateProfileTx tx = orkad.profileTxFactory.updateSelfProfile();
      say("What's the new status message?");
      String newStatus = readline();
      tx.setStatus(newStatus);

      BatchTransaction btx = orkad.newBatch();
      btx.add(tx);
      
      say("Updating...");
      orkad.submitBatch(btx);
      say("Done.");

      if (tx.hasError()) {
         say("*** Error updating profile!");
      }
   }

   public static void listActivities(OrkutAdapter orkad) throws Exception {
      BatchTransaction btx = orkad.newBatch();
      GetActivitiesTx activities = orkad.activityTxFactory.getSelfActivities();
      btx.add(activities);

      say("Getting activities...");
      orkad.submitBatch(btx);
      say("...done");

      int page = 0;
      while (true) {
         say("Page: " + String.valueOf(++page));
         for (int i = 0; i < activities.getActivityCount(); i++) {
            ActivityEntry entry = activities.getActivity(i);
            say(entry.toString());
         }

         if (!activities.hasNext()) break;
         say("Get next page [y/n]? ");
         if (!readline().toLowerCase().startsWith("y")) break;

         activities = orkad.activityTxFactory.getNext(activities);
         btx = orkad.newBatch();
         btx.add(activities);
         orkad.submitBatch(btx);
      }
      pause();
   }

   public static void getFullProfile(OrkutAdapter orkad) throws Exception {
      BatchTransaction btx = orkad.newBatch();
      GetProfileTx profile;

      say("Whose profile? Press ENTER to read your own profile, or");
      say("enter the profile ID you would like to query:");
      String id = readline();

      if (id.equals("")) profile = orkad.profileTxFactory.getSelfProfile();
      else profile = orkad.profileTxFactory.getProfileOf(id);

      profile.alsoGetName();
      profile.alsoGetThumbnailUrl();
      profile.alsoGetProfileUrl();
      profile.alsoGetStatus();
      profile.alsoGetEmails();
      profile.alsoGetGender();
      profile.alsoGetPhoneNumbers();
      profile.alsoGetBirthday();
      profile.alsoGetAddress();
      btx.add(profile);

      say("Getting info...");
      orkad.submitBatch(btx);
      say("...done.");

      if (profile.hasError()) {
         say("Error in profile transaction.");
         return;
      }

      OrkutPerson person = profile.getProfile();
      if (person == null) {
         say("No profile data returned.");
         return;
      }

      say("ID                    : " + nullsafe(person.getId()));
      say("Given name            : " + nullsafe(person.getGivenName()));
      say("Family name           : " + nullsafe(person.getFamilyName()));
      say("Display name          : " + nullsafe(person.getDisplayName()));
      say("Thumbnail URL         : " + nullsafe(person.getThumbnailUrl()));
      say("Status                : " + nullsafe(person.getStatus()));
      say("Gender                : " + nullsafe(person.getGender()));
      say("Profile URL           : " + nullsafe(person.getProfileUrl()));
      say("Birthday (long)       : " + String.valueOf(person.getBirthday()));

      com.google.orkut.client.api.Address address = person.getAddress();
      if (address != null) {
         say("Address               : ");
         say("       Country code   : " + nullsafe(address.getCountryCode()));
         say("       Region code    : " + nullsafe(address.getRegion()));
         say("       Locality       : " + nullsafe(address.getLocality()));
         say("       Postal Code    : " + nullsafe(address.getPostalCode()));
      }
      else
         say("Address                  : [not returned]");

      int count = person.getEmailCount(); 
      int i;
      say("E-Mails: (" + String.valueOf(count) + ")");
      for (i = 0; i < count; i++) say("  - " + nullsafe(person.getEmail(i)));
      
      count = person.getPhoneNumberCount(); 
      say("Phones: (" + String.valueOf(count) + ")");
      for (i = 0; i < count; i++)
         say("  - " + nullsafe(person.getPhoneNumber(i)));

      pause();
   }

   public static void getBirthdayNotifications(OrkutAdapter orkad) 
                                                        throws Exception {
      BirthdayNotificationTx tx = 
                        orkad.friendTxFactory.getBirthdayNotification();
      BatchTransaction btx = orkad.newBatch();
      btx.add(tx);
      orkad.submitBatch(btx);

      if (tx.hasError()) {
          // handle error
          say("Error fetching birthday notifications.");
          return;
      }
                        
      for (int i = 0; i < tx.getBirthdayNotificationCount(); ++i) {
         BirthdayNotificationEntry entry = tx.getBirthdayNotification(i);
         say("User: " + entry.getUserProfile().getDisplayName() +
              " (" + entry.getUserId() + ") Birthdate: " + 
              entry.getBirthDay() + "/" + entry.getBirthMonth());
      }
   }

   public static void postNewActivity(OrkutAdapter orkad)
                                                throws Exception {
      say("Enter title:");
      String title = readline();
      say("");
      say("Enter body:");
      String body = readline();

      PostActivityTx tx = orkad.activityTxFactory.postActivity(title,body);
      BatchTransaction btx = orkad.newBatch();
      btx.add(tx);
      say("Sending...");
      orkad.submitBatch(btx);
      say("Done.");

      if (tx.hasError()) {
         say("*** Error posting activity.");
         return;
      }
   }

   public static void getScraps(OrkutAdapter orkad) throws Exception {
      BatchTransaction btx = orkad.newBatch();
      GetScrapsTx tx = orkad.scrapTxFactory.getSelfScraps();
      tx.setMessageFormat(GetScrapsTx.MessageFormat.FULL_HTML);
      tx.setCount(20);
      btx.add(tx);
      say("Getting first 20 scraps...");
      orkad.submitBatch(btx);
      say("Done.");

      if (tx.hasError()) {
         say("*** Error fetching scraps.");
         return;
      }

      int i;
      say(String.valueOf(tx.getScrapCount()) + " scrap(s) returned");
      for (i = 0; i < tx.getScrapCount(); i++) {
         ScrapEntry se = tx.getScrap(i);
         String id = nullsafe(se.getId());
         String fromName = se.hasFromUserProfile() ? 
                (nullsafe(se.getFromUserProfile().getDisplayName())) : "n/a";
         String body = nullsafe(se.getBody());

         say("Scrap #" + String.valueOf(i) + " (id " + id + "):");
         say("From: " + fromName);
         say("Body: " + body);
         say("");
      }

      pause();
   }

   public static void writeScrap(OrkutAdapter orkad) throws Exception {
      WriteScrapTx tx;
      String body;

      say("Who are you writing the scrap to? Enter his/her ID:");
      String personId = readline();

      say("Are you [r]elying to a scrap or writing a [n]ew one?");
      if (readline().toLowerCase().startsWith("r")) {
         say("What is the id of the scrap you are replying to?");
         String scrapId = readline();
         say("Enter the reply text.");
         body = readline();
         tx = orkad.scrapTxFactory.replyToScrap(personId,scrapId,body);
      }
      else {
         say("Enter the scrap text.");
         body = readline();
         tx = orkad.scrapTxFactory.writeScrap(personId,body);
      }

      BatchTransaction btx = orkad.newBatch();
      btx.add(tx);
      say("Sending scrap...");
      orkad.submitBatch(btx);
      say("Done.");

      if (tx.hasError()) {
         OrkutError error = tx.getError();
         if (error == null || !error.isCaptchaError()) {
            say("*** Unknown error sending scrap.");
            return;
         }
         say("Captcha solving is required.");
         say("Downloading captcha.");
         orkad.saveCaptchaToFile(error, "captcha.jpg");
         say("What is the text on the image captcha.jpg?");
         String answer = readline();

         say("Submitting transaction with captcha...");
         orkad.submitBatchWithCaptcha(btx, error, answer);
         return;
      }
   }

   public static void createAlbum(OrkutAdapter orkad) throws Exception {
      say("Album title?");
      String title = readline();
      say("Album description?");
      String desc = readline();

      say("Creating...");
      CreateAlbumTx tx = orkad.albumsTxFactory.createAlbum(title,desc);
      BatchTransaction btx = orkad.newBatch();
      btx.add(tx);
      orkad.submitBatch(btx);

      if (tx.hasError()) {
         OrkutError err = tx.getError();
         say("*** Transaction Error: " + (err == null ? 
                                          "unknown" : err.toString()));
         return;
      }
      say("Done.");
   }

   public static void listAlbums(OrkutAdapter orkad) throws Exception {
      GetAlbumsTx tx = orkad.albumsTxFactory.getSelfAlbums();
      tx.setCount(5);  // get first 5 albums

      BatchTransaction btx = orkad.newBatch();
      btx.add(tx);
      say("Fetching first few albums...");
      orkad.submitBatch(btx);

      if (tx.hasError()) {
         OrkutError err = tx.getError();
         say("*** Transaction error: " + (err == null ? 
                                        "unknown" : err.toString()));
         return;
      }

      int i;
      for (i = 0; i < tx.getAlbumCount(); i++) {
         Album album = tx.getAlbum(i);
         say("Album Title  : " + nullsafe(album.getTitle()));
         say("Description  : " + nullsafe(album.getDescription()));
         say("Thumbnail URL: " + nullsafe(album.getThumbnailUrl()));
         say("Album ID     : " + nullsafe(album.getId()));
         say("Owner ID     : " + nullsafe(album.getOwnerId()));
         say("");
      }
   }

   public static void updateAlbum(OrkutAdapter orkad) throws Exception {
      say("What's the album ID?");
      String albumId = readline();

      say("What's the new title?");
      String newTitle = readline();

      say("What's the new description?");
      String newDesc = readline();

      GetAlbumsTx tx = orkad.albumsTxFactory.getSelfAlbum(albumId);
      BatchTransaction btx = orkad.newBatch();
      btx.add(tx);
      say("Getting album...");
      orkad.submitBatch(btx);

      if (tx.hasError()) {
         say("*** Failed to get album.");
         return;
      }

      if (tx.getAlbumCount() != 1) {
         say("*** Unexpected album count (expected 1): " + 
                                String.valueOf(tx.getAlbumCount()));
         return;
      }

      Album album = tx.getAlbum(0);

      say("Updating the album...");
      album.setTitle(newTitle);
      album.setDescription(newDesc);

      btx = orkad.newBatch();
      UpdateAlbumTx utx = orkad.albumsTxFactory.updateAlbum(album);
      btx.add(utx);
      orkad.submitBatch(btx);

      if (utx.hasError())
         say("*** Error updating album: " +
            (utx.getError() == null ? "(unknown)":utx.getError().toString()));
      else
         say("Success.");
   }

   public static void deleteAlbum(OrkutAdapter orkad) throws Exception {
      say("What's the album ID?");
      String albumId = readline();

      DeleteAlbumTx tx = orkad.albumsTxFactory.deleteAlbum(albumId);
      BatchTransaction btx = orkad.newBatch();
      btx.add(tx);
      say("Deleting...");
      orkad.submitBatch(btx);

      if (tx.hasError()) say("*** Error deleting album.");
      else say("Deleted.");
   }

   public static void listPhotos(OrkutAdapter orkad) throws Exception {
      say("What's the album ID?");
      String albumId = readline();

      GetPhotosTx tx = orkad.photosTxFactory.getSelfPhotos(albumId);
      BatchTransaction btx = orkad.newBatch();
      tx.setCount(20); // get up to 20 photos
      btx.add(tx);
      say("Getting photos...");
      orkad.submitBatch(btx);

      if (tx.hasError()) {
         say("*** Error getting photos.");
         return;
      }

      int i;
      say(String.valueOf(tx.getPhotoCount()) + " photos returned.");
      for (i = 0; i < tx.getPhotoCount(); i++) {
         Photo p = tx.getPhoto(i);
         say("Photo ID        : " + p.getId());
         say("Photo Title     : " + p.getTitle());
         say("Photo URL       : " + p.getUrl());
         say("Thumbnail URL   : " + p.getThumbnailUrl());
         say("");
      }
   }

   public static void uploadPhoto(OrkutAdapter orkad) throws Exception {
      say("What's the album ID?");
      String albumId = readline();
      say("What's the path to the JPG file?");
      String filePath = readline();
      say("What's the title of the photo?");
      String title = readline();

      UploadPhotoTx tx =
                 orkad.photosTxFactory.uploadPhoto(albumId,filePath,title);
      BatchTransaction btx = orkad.newBatch();
      btx.add(tx);
      say("Submitting photo...");
      orkad.submitBatch(btx);

      if (tx.hasError()) {
         say("*** Error uploading photo.");
         if (tx.getError() != null) say("*** " + tx.getError().toString());
      }
      else say("Success.");
   }

   private static String nullsafe(String s) {
      return s == null ? "(null)" : s;
   }

   public static void pause() throws Exception {
      say("Press ENTER to continue.");
      readline();
   }

   public static String readline() throws Exception {
      java.io.BufferedReader br = new java.io.BufferedReader(
                        new java.io.InputStreamReader(
                                System.in));
      return br.readLine();
   }

   public static void main(String[] args) {
      try {
         say("Starting.");
         main_inner();
      }
      catch (Exception e) {
         say("");
         say("*** Houston, we have a problem: ");
         e.printStackTrace();
      }
   }
}

