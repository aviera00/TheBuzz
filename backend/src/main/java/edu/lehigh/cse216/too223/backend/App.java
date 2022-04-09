package edu.lehigh.cse216.too223.backend;
import com.fasterxml.jackson.core.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import java.util.Hashtable;

// Import the Spark package, so that we can make use of the "get" function to 
// create an HTTP GET route
import spark.Spark;

import java.util.Arrays;
import java.util.Collections;

import javax.xml.crypto.Data;
//import Google's JSON Library
import com.google.gson.*;
import com.heroku.api.User;

import org.apache.http.io.HttpTransportMetrics;
//memcachier? imports
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.auth.AuthInfo;
import net.rubyeye.xmemcached.command.BinaryCommandFactory;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.utils.AddrUtil;

import java.lang.InterruptedException;
import java.net.InetSocketAddress;
import java.security.GeneralSecurityException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

//gdrive imports
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

/**
 * For now, our app creates an HTTP server that can only get and add data.
 */
public class App {
    public static void main(String[] args) throws IOException, GeneralSecurityException {
        //memcachier connection
        List<InetSocketAddress> servers =
        AddrUtil.getAddresses(System.getenv("MEMCACHIER_SERVER").replace(",", " "));
      AuthInfo authInfo =
        AuthInfo.plain(System.getenv("MEMCACHIER_USERNAME"),
                       System.getenv("MEMCACHIER_PASSWORD"));
  
      MemcachedClientBuilder builder = new XMemcachedClientBuilder(servers);
      // Configure SASL auth for each server
      for(InetSocketAddress server : servers) {
        builder.addAuthInfo(server, authInfo);
      }
      // Use binary protocol
    builder.setCommandFactory(new BinaryCommandFactory());
    // Connection timeout in milliseconds (default: )
    builder.setConnectTimeout(1000);
    // Reconnect to servers (default: true)
    builder.setEnableHealSession(true);
    // Delay until reconnect attempt in milliseconds (default: 2000)
    builder.setHealSessionInterval(2000);
      try {
        MemcachedClient mc = builder.build();
        try {
          mc.set("foo", 0, "bar");
          String val = mc.get("foo");
          System.out.println(val);
        } catch (TimeoutException te) {
          System.err.println("Timeout during set or get: " +
                             te.getMessage());
        } catch (InterruptedException ie) {
          System.err.println("Interrupt during set or get: " +
                             ie.getMessage());
        } catch (MemcachedException me) {
          System.err.println("Memcached error during get or set: " +
                             me.getMessage());
        }
      } catch (IOException ioe) {
        System.err.println("Couldn't create a connection to MemCachier: " +
                           ioe.getMessage());
      }
    
        //Hashtable that stores verified users
        Hashtable<String,String> users = new Hashtable();
        //dummy user for voting state machine
        MyUser dummy = new MyUser("","","","","","");
        //Google Drive stuff
        NetHttpTransport HTTP_TRANSPORT;
        DriveQuickstart start=new DriveQuickstart();
        Drive service;
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        //String APPLICATION_NAME = "Google Drive API Java Quickstart";
        service = new Drive.Builder(HTTP_TRANSPORT, start.JSON_FACTORY, start.getCredentials(HTTP_TRANSPORT))
                .setApplicationName(start.APPLICATION_NAME)
                .build();
        // Get the port on which to listen for requests
        Spark.port(getIntFromEnv("PORT", 4567));
        
        // gson provides us with a way to turn JSON into objects, and objects
        // into JSON.
        //
        // NB: it must be final, so that it can be accessed from our lambdas
        //
        // NB: Gson is thread-safe.  See 
        // https://stackoverflow.com/questions/10380835/is-it-ok-to-use-gson-instance-as-a-static-field-in-a-model-bean-reuse
        final Gson gson = new Gson();

        Map<String, String> env = System.getenv();
        String db_url = env.get("DATABASE_URL")+"?sslmode=require";

        
        
        Database db = Database.getDatabase(db_url);
        if (db  == null){
            return;
        }

        // Set up the location for serving static files.  If the STATIC_LOCATION
        // environment variable is set, we will serve from it.  Otherwise, serve
        // from "/web"

        //TODO: uncomment this thing when it is being used with web
        
        String static_location_override = System.getenv("STATIC_LOCATION");
        if (static_location_override == null) {
            Spark.staticFileLocation("/web");
        } else {
            Spark.staticFiles.externalLocation(static_location_override);
        } 

        String cors_enabled = System.getenv("CORS_ENABLED");
        if (cors_enabled.equals("True")) {
            final String acceptCrossOriginRequestsFrom = "*";
            final String acceptedCrossOriginRoutes = "GET,PUT,POST,DELETE,OPTIONS";
            final String supportedRequestHeaders = "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin";
            enableCORS(acceptCrossOriginRequestsFrom, acceptedCrossOriginRoutes, supportedRequestHeaders);
        }


        // Set up the location for serving static files
        

        // Set up a route for serving the main page
        Spark.get("/", (req, res) -> {
            res.redirect("/index.html");
            return "";
        });

        // GET route that returns all message titles and Ids.  All we do is get 
        // the data, embed it in a StructuredResponse, turn it into JSON, and 
        // return it.  If there's no data, we return "[]", so there's no need 
        // for error handling.
        Spark.get("/ideas", (request, response) -> {
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            return gson.toJson(new StructuredResponse("ok", null, db.selectAll())); //change to db.SELCTALL
        });

        // GET route that returns everything for a single row in the DataStore.
        // The ":id" suffix in the first parameter to get() becomes 
        // request.params("id"), so that we can get the requested row ID.  If 
        // ":id" isn't a number, Spark will reply with a status 500 Internal
        // Server Error.  Otherwise, we have an integer, and the only possible 
        // error is that it doesn't correspond to a row with data.
        Spark.get("/ideas/:id", (request, response) -> {
            int idx = Integer.parseInt(request.params("id"));
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            Database.RowData data = db.selectOne(idx); //change to db :jdbc DB.SELECTONE 
            if (data == null) {
                return gson.toJson(new StructuredResponse("error", idx + " not found", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, data));
            }
        });

        // POST route for adding a new element to the DataStore.  This will read
        // JSON from the body of the request, turn it into a SimpleRequest 
        // object, extract the title and message, insert them, and return the 
        // ID of the newly created row.
        Spark.post("/ideas", (request, response) -> {
            // NB: if gson.Json fails, Spark will reply with status 500 Internal 
            // Server Error
            SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);
            //add file to simplerequest, its a byte stream
            //if its null just do nothing, otherwise gotta convert and add to drive and also to cache
            if(req.fileUrl!=null){
                //file exists so we have to do stuff with it
                String link=fileUpload(service, req.fileName, req.fileUrl);//call upload function
                //not sure if DB currently supports this, will also need to overload insertRow function to support 3rd argument
                //db.insertRow(req.mTitle, req.mMessage, link);
        }
            // ensure status 200 OK, with a MIME type of JSON
            // NB: even on error, we return 200, but with a JSON object that
            //     describes the error.
            response.status(200);
            response.type("application/json");
            // NB: createEntry checks for null title and message
            int newId = db.insertRow(req.mTitle, req.mMessage); //DBINSERTROW
            if (newId == -1) {
                return gson.toJson(new StructuredResponse("error", "error performing insertion", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", "" + newId, null));
            }
        });


        // PUT route for updating a row in the DataStore.  This is almost 
        // exactly the same as POST
        Spark.put("/ideas/:id/", (request, response) -> {
            // If we can't get an ID or can't parse the JSON, Spark will send
            // a status 500
            int idx = Integer.parseInt(request.params("id"));
            SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            int result = db.updateOne(idx, req.mMessage); //DB.UPDATEONE
            if (result == -1) {
                return gson.toJson(new StructuredResponse("error", "unable to update row " + idx, null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, result));
            }
        });


        // PUT route for verification
        Spark.put("/ideas/:key/:username/:token/", (request, response) -> {
            // If we can't get an ID or can't parse the JSON, Spark will send
            // a status 500
            String token = request.params("token");
            String key = request.params("key");
            String usern = request.params("username");
            HttpTransport transport=new NetHttpTransport();
            String CLIENT_ID="981023697185-p70qekjifu3k3dbi6m61ga3j86kbguer.apps.googleusercontent.com";
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, JacksonFactory.getDefaultInstance())
        .setAudience(Arrays.asList(CLIENT_ID))
        .build();
            SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            int result=0;
            GoogleIdToken idToken = verifier.verify(token);
                if (idToken != null) {
                    users.put(key,usern);
                }else{
                    result = -1;
                }
            if (result == -1) {
                return gson.toJson(new StructuredResponse("error", "unable to verify user " + usern, null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, key));
            }
        });

        // PUT route for letting a user to vote, 
        //returns 0 on neutral vote, 1 on up vote and 2 on down vote.
        Spark.put("/ideas/:id/:key", (request, response) -> {
            // If we can't get an ID or can't parse the JSON, Spark will send
            // a status 500
            int idx = Integer.parseInt(request.params("id"));
            int votestate=0;
            String key = request.params("key");
            try{users.get(key);}catch(NullPointerException e){
                return gson.toJson(new StructuredResponse("error", "Not any logged in user", null));
            }
            votestate=dummy.clickVote();
            SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            int result = -1;
            switch(votestate){
                case 0:
                result=0;
                break;
                case 1:
                result=1;
                db.upVoteOne(idx);
                case -1:
                result=2;
                db.downVoteOne(idx);
            }
            if (result == -1) {
                return gson.toJson(new StructuredResponse("error", "unable to vote for idea" + idx, null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, result));
            }
        });

        //Route for allowing a user with "key" to comment on an idea with "id", comment is request message
        Spark.post("/ideas/:id/:key", (request, response) -> {
            // NB: if gson.Json fails, Spark will reply with status 500 Internal 
            // Server Error
            int idx = Integer.parseInt(request.params("id"));
            String key = request.params("key");
            try{users.get(key);}catch(NullPointerException e){
                return gson.toJson(new StructuredResponse("error", "Not any logged in user", null));
            }
            SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);
            if(req.fileUrl!=null){
                //file exists so we have to do stuff with it
                String link=fileUpload(service, req.fileName, req.fileUrl);//call upload function
                //not sure if DB currently supports this, will also need to overload insertRow function to support 3rd argument
                //db.insertRow(req.mTitle, req.mMessage, link);
        }
            // ensure status 200 OK, with a MIME type of JSON
            // NB: even on error, we return 200, but with a JSON object that
            //     describes the error.
            response.status(200);
            response.type("application/json");
            // NB: createEntry checks for null title and message
            int result = db.commentOne(idx, users.get(key), req.mMessage); //adding comment
            if (result == -1) {
                return gson.toJson(new StructuredResponse("error", "error performing insertion", null));
            } else {
                return gson.toJson(new StructuredResponse("ok", "" + result, null));
            }
        });

        //Get all comments on the idea with "id", returns as a hash table (commentID, comment body)
        //Any one can see comments, so no session key required.
        Spark.get("/ideas/:id", (request, response) -> {
            // NB: if gson.Json fails, Spark will reply with status 500 Internal 
            // Server Error
            int idx = Integer.parseInt(request.params("id"));
            SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);
            // ensure status 200 OK, with a MIME type of JSON
            // NB: even on error, we return 200, but with a JSON object that
            //     describes the error.
            response.status(200);
            response.type("application/json");
            // NB: createEntry checks for null title and message
            Hashtable result = db.viewComment(idx);
            return gson.toJson(new StructuredResponse("ok", "" + result, null));
        });

       
        //Route for allowing a user with "key" to attempt to 
        //edit comment with "cid" on an idea with "id", new comment is request message
        Spark.post("/ideas/:id/:cid/:key", (request, response) -> {
            // NB: if gson.Json fails, Spark will reply with status 500 Internal 
            // Server Error
            int idx = Integer.parseInt(request.params("id"));
            int idy = Integer.parseInt(request.params("cid"));
            String key = request.params("key");
            try{users.get(key);}catch(NullPointerException e){
                return gson.toJson(new StructuredResponse("error", "Not any logged in user", null));
            }
            SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);
            // ensure status 200 OK, with a MIME type of JSON
            // NB: even on error, we return 200, but with a JSON object that
            //     describes the error.
            response.status(200);
            response.type("application/json");
            // NB: createEntry checks for null title and message
            int result = db.editComment(idx, idy, users.get(key), req.mMessage); //editing comment
            if (result == -1) {
                return gson.toJson(new StructuredResponse("error", "error performing change", null));
            } else if (result==1){
                return gson.toJson(new StructuredResponse("error", "error editing: not comment owner" + result, null));
            }
            else{
                return gson.toJson(new StructuredResponse("ok", null, result));
            }
        });


        // GET route for getting the likescore(upvote-downvote) for an idea. 
        Spark.get("/ideas/:id/likescore", (request, response) -> {
            // If we can't get an ID or can't parse the JSON, Spark will send
            // a status 500
            int idx = Integer.parseInt(request.params("id"));
            SimpleRequest req = gson.fromJson(request.body(), SimpleRequest.class);
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            int result = db.getVoteCount(2, idx); //DB.UPDATEONE
            if (result == -1) {
                return gson.toJson(new StructuredResponse("error", "unable to get votes " + idx, null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, result));
            }
        });


        // DELETE route for removing a row from the DataStore
        Spark.delete("/ideas/:id", (request, response) -> {
            // If we can't get an ID, Spark will send a status 500
            int idx = Integer.parseInt(request.params("id"));
            // ensure status 200 OK, with a MIME type of JSON
            response.status(200);
            response.type("application/json");
            // NB: we won't concern ourselves too much with the quality of the 
            //     message sent on a successful delete

            // boolean result = db.deleteRow(idx); //db.deleteRow(idx)
            if (db.deleteRow(idx)==-1) {
                return gson.toJson(new StructuredResponse("error", "unable to delete row " + idx, null));
            } else {
                return gson.toJson(new StructuredResponse("ok", null, null));
            }
        });

        
    }

    /**
     * Get an integer environment varible if it exists, and otherwise return the
     * default value.
     * 
     * @envar      The name of the environment variable to get.
     * @defaultVal The integer value to use as the default if envar isn't found
     * 
     * @returns The best answer we could come up with for a value for envar
     */
    static int getIntFromEnv(String envar, int defaultVal) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get(envar) != null) {
            return Integer.parseInt(processBuilder.environment().get(envar));
        }
        return defaultVal;
    }

    /**
     * Set up CORS headers for the OPTIONS verb, and for every response that the
     * server sends.  This only needs to be called once.
     * 
     * @param origin The server that is allowed to send requests to this server
     * @param methods The allowed HTTP verbs from the above origin
     * @param headers The headers that can be sent with a request from the above
     *                origin
     */
    private static void enableCORS(String origin, String methods, String headers) {
        // Create an OPTIONS route that reports the allowed CORS headers and methods
        Spark.options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }
            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }
            return "OK";
        });

        // 'before' is a decorator, which will run before any 
        // get/post/put/delete.  In our case, it will put three extra CORS
        // headers into the response
        Spark.before((request, response) -> {
            response.header("Access-Control-Allow-Origin", origin);
            response.header("Access-Control-Request-Method", methods);
            response.header("Access-Control-Allow-Headers", headers);
        });
    }
    public static String fileUpload(Drive service,String fileName, String fileUrl) throws IOException{
        //get the bytes
        byte[] byteArray = fileUrl.getBytes();
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            fos.write(byteArray);
         }
         //now that we have a file, shove it to GDrive and also into cache
         File fileMetadata = new File();
        fileMetadata.setName(fileName);
        java.io.File filePath = new java.io.File(fileName);
        String fileType="";
        if(fileName.contains(".jpg")||fileName.contains(".jfif")){
            fileType="image/jpeg";
        }
        else if(fileName.contains(".png")){
            fileType="image/png";
        }
        else if(fileName.contains(".jfif")){
            fileType="image/jfif";
        }
        else{
            fileType="text/"+fileName.split(".")[1];
        }
        FileContent mediaContent = new FileContent(fileType, filePath);
        File file = service.files().create(fileMetadata, mediaContent)
        .setFields("id")
        .execute();
         //now we have to get the link
         //service.files().get().setFields("webViewLink");
         String pageToken = null;
         String link="";
        do {
        FileList result = service.files().list()
            .setQ("id='"+file.getId()+"'")
            .setSpaces("drive")
            .setFields("nextPageToken, webViewLink")
            .setPageToken(pageToken)
            .execute();
        for (File file1 : result.getFiles()) {
            link=file1.getWebViewLink();
            break;
        }
        pageToken = result.getNextPageToken();
        } while (pageToken != null);
        //now delete the local file
        filePath.delete();
        //since we're done, return the file link
        return link;
    }
}