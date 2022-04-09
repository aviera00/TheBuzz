package edu.lehigh.cse216.yis223.admin;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.ArrayList;
 
// get code from admin app and compare and replace functions from datastore accordingly
public class Database {
	private String [] inapr = {"fuck", "bitch"};
    /**
     * The connection to the database.  When there is no connection, it should
     * be null.  Otherwise, there is a valid open connection
     */
    private Connection mConnection;

    /**
     * A prepared statement for getting all data in the database
     */
    private PreparedStatement mSelectAll;

    /**
     * A prepared statement for getting one idea from the database
     */
    private PreparedStatement mSelectIdea;

     /**
     * A prepared statement for getting one idea from the database
     */
    private PreparedStatement mSelectUsername;



    /**
     * A prepared statement for inserting into the database
     */
    private PreparedStatement mInsertOne;



    /**
     * A prepared statement for updating like score single row in the database
     */
    private PreparedStatement mLikeScore;



 

    /**
     * A prepared statement for inserting user into the database
     */
    private PreparedStatement mAddUser;

    /**
     * A prepared statement for creating the table in our database
     */
    private PreparedStatement mCreateIdeaTable;
    
    /**
     * A prepared statement for creating the table in our database
     */
    private PreparedStatement mCreateIdeaDocumentTable;
    
    /**
     * A prepared statement for creating the table in our database
     */
    private PreparedStatement mCreateCommentDocumentTable;
    
    /**
     * A prepared statement for adding an idea
     */
    private PreparedStatement mAddIdeaDocument;
    
    /**
     * A prepared statement for adding an idea
     */
    private PreparedStatement mAddCommentDocument;

    /**
     * A prepared statement for adding an idea
     */
    private PreparedStatement mAddIdea;

    /**
     * A prepared statement for creating the table in our database
     */
    private PreparedStatement mCreateUserTable;

    /**
     * A prepared statement for creating the table in our database
     */
    private PreparedStatement mCreateAffinityTable;

    /**
     * A prepared statement for dropping the user in our database
     */
    private PreparedStatement mDropUser;

    /**
     * A prepared statement for dropping the idea in our database
     */
    private PreparedStatement mDropIdea;

    /**
     * A prepared statement for dropping the table in our database
     */
    private PreparedStatement mDropAffinity;
    
    private PreparedStatement mDropIdeaDocument;
    
    private PreparedStatement mDropIdeaDocumentTable;
    
    private PreparedStatement mDropCommentDocument;
    
    private PreparedStatement mDropCommentDocumentTable;

	private PreparedStatement mCreateCommentTable;

	private PreparedStatement mAddComment;

	private PreparedStatement mDropComment;

	private PreparedStatement mAffinity;

	private PreparedStatement mSelectUser;
	
	private PreparedStatement mSelectIdeaDocument;
	
	private PreparedStatement mSelectCommentDocument;

    private PreparedStatement mDropIdeaTable;

    private PreparedStatement mDropCommentTable;

    private PreparedStatement mDropAffinityTable;

    private PreparedStatement mDropUserTable;

    private PreparedStatement mAddLike;

    private PreparedStatement mAddDislike;

    private PreparedStatement mDropAllAffinity;

    private PreparedStatement mSelectComment;

    private PreparedStatement mCheckLike;
    
    private PreparedStatement mCheckFlagg;
    
    private PreparedStatement mCheckDate;
    
    private PreparedStatement mCheckInapr;
    
    
    
    
    private PreparedStatement mCheckFlaggC;
    
    private PreparedStatement mCheckDateC;
    
    private PreparedStatement mCheckInaprC;

    /**
     * ideaData is like a struct in C: we use it to hold data, and we allow 
     * direct access to its fields.  In the context of this Database, ideaData 
     * represents the data we'd see in a row.
     * 
     * We make ideaData a static class of Database because we don't really want
     * to encourage users to think of ideaData as being anything other than an
     * abstract representation of a row of the database.  ideaData and the 
     * Database are tightly coupled: if one changes, the other should too.
     */
    public static class ideaData {
        /**
         * The ID of this row of the database
         */
        int mId;
        /**
         * The idea_title stored in this row
         */
        String midea_title;
        /**
         * The message stored in this row
         */
        String mMessage;
        /**
         * The likescore
         */      
        int mAffinity;




        /**
         * Construct a ideaData object by providing values for its fields
         */
        public ideaData(int id, String idea_title, String message, int affinity) {
            mId = id;
            midea_title = idea_title;
            mMessage = message;
            mAffinity = affinity;
        }

    }
    
    public static class ideaDocumentData {
        /**
         * The ID of this row of the database
         */
        int mideaId;
        /**
         * The idea_title stored in this row
         */
        int mideaDocumentId;
        /**
         * The message stored in this row
         */
        String mDocumentReference;
        /**
         * The flagged variable (true if reported)
         */      
        int mFlagged;


        Date mlastAccessed = new Date();

        /**
         * Construct a ideaData object by providing values for its fields
         */
        public ideaDocumentData(int ideaId, int ideaDocumentId, String documentReference, int flagged, Date lastAccessed) {
            mideaId = ideaId;
            mideaDocumentId = ideaDocumentId;
            mDocumentReference = documentReference;
            mFlagged = flagged;
            mlastAccessed = lastAccessed;
        }

    }
    
    
    
    
    
    
    public static class commentDocumentData {
        /**
         * The ID of this row of the database
         */
        int mcommentId;
        /**
         * The idea_title stored in this row
         */
        int mcommentDocumentId;
        /**
         * The message stored in this row
         */
        String mDocumentReference;
        /**
         * The flagged variable (true if reported)
         */      
        int mFlagged;


        Date mlastAccessed = new Date();

        /**
         * Construct a ideaData object by providing values for its fields
         */
        public commentDocumentData(int commentId, int commentDocumentId, String documentReference, int flagged, Date lastAccessed) {
            mcommentId = commentId;
            mcommentDocumentId = commentDocumentId;
            mDocumentReference = documentReference;
            mFlagged = flagged;
            mlastAccessed = lastAccessed;
        }

    }
    
    
    
    
    

    public static class userData {
        /**
         * The unique identifier associated with a User.  It's final, because
         * we never want to change it.
         */
        public String musername;
        /**
         * The user's date of birth
         */
        public String mdob;
        /**
         * The user's password
         */
        public String mpassword;
    
        /**
         * User's first name
         */
        public String mfirstName;
    
        /**
         * User's last name
         */
        public String mlastName;
    
        /**
         * User's email address
         */
        public String memail;
    
        /**
         * Create a new user
         * @param username The user name of the user
         * 
         */
        public userData(String username, String dob, String password, String firstName, String lastName, String email){
            musername = username;
            mdob = dob;
            mpassword = password;
            mfirstName = firstName;
            mlastName = lastName;
            memail = email;
        }
    }

    public class Affinity {
        /**
         * The user name
         */
        public String user;
        
        /**
         * ID of the idea
         */
        public final int ideaID;
    
        /**
         * Like status
         */
        public boolean likeStatus;
    
        Affinity(String user,int id,boolean like){
            ideaID = id;
            this.user = user;
            likeStatus = like;
    
        }
    
    }

    public static class commentData {
      
        int mcommentID;

        String musername;

        int mideaID;
 
        String mcomment;



      
        public commentData(int commentID, String username, int ideaID, String comment) {
            mcommentID = commentID;
            musername = username;
            mideaID = ideaID;
            mcomment = comment;
        } // add voting stuff to constructor

    }

    /**
     * The Database constructor is private: we only create Database objects 
     * through the getDatabase() method.
     */
    private Database() {
    }

    /**
     * Get a fully-configured connection to the database
     * 
     * @param ip   The IP address of the database server
     * @param port The port on the database server to which connection requests
     *             should be sent
     * @param user The user ID to use when connecting
     * @param pass The password to use when connecting
     * 
     * @return A Database object, or null if we cannot connect properly
     */
    static Database getDatabase(String db_url) {
        // Create an un-configured Database object
        Database db = new Database();

        // Give the Database object a connection, fail if we cannot get one
        try {
            Class.forName("org.postgresql.Driver");
            URI dbUri = new URI(db_url);
            String username = dbUri.getUserInfo().split(":")[0];
            String password = dbUri.getUserInfo().split(":")[1];
            String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
            Connection conn = DriverManager.getConnection(dbUrl, username, password);
            if (conn == null) {
                System.err.println("Error: DriverManager.getConnection() returned a null object");
                return null;
            }
            db.mConnection = conn;
            
        } catch (SQLException e) {
            System.err.println("Error: DriverManager.getConnection() threw a SQLException");
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Unable to find postgresql driver");
            return null;
        } catch (URISyntaxException s) {
            System.out.println("URI Syntax Error");
            return null;
        }

        // Attempt to create all of our prepared statements.  If any of these 
        // fail, the whole getDatabase() call should fail
        try {
            // NB: we can easily get ourselves in trouble here by typing the
            //     SQL incorrectly.  We really should have things like "idea"
            //     as constants, and then build the strings for the statements
            //     from those constants.

            // Note: no "IF NOT EXISTS" or "IF EXISTS" checks on table 
            // creation/deletion, so multiple executions will cause an exception
            db.mCreateUserTable = db.mConnection.prepareStatement(
                    "CREATE TABLE Users (username VARCHAR(50) PRIMARY KEY, DOB VARCHAR(50) "
                    + "NOT NULL, password VARCHAR(500) NOT NULL, firstName VARCHAR(255) NOT NULL, lastName VARCHAR(255) NOT NULL, email VARCHAR(255) NOT NULL)"); //ADD FOR VOTING STUFF AS WELL INTEGER DEFAULT 0

            db.mCreateIdeaTable = db.mConnection.prepareStatement(
                    "CREATE TABLE Idea (ideaID SERIAL PRIMARY KEY, username VARCHAR(50) "
                    + "NOT NULL, idea_title VARCHAR(255) NOT NULL, idea_body VARCHAR(500) NOT NULL, likecount INT DEFAULT 0, FOREIGN KEY (username) REFERENCES Users)");
            
            db.mCreateIdeaDocumentTable = db.mConnection.prepareStatement(
                    "CREATE TABLE IdeaDocument (ideaDocumentID SERIAL PRIMARY KEY, ideaID INT, FOREIGN KEY (ideaId) REFERENCES Idea , documentReference VARCHAR(255) NOT NULL, flagged INT DEFAULT 0, lastAccessed DATE)");
            
            db.mCreateCommentDocumentTable = db.mConnection.prepareStatement(
                    "CREATE TABLE commentDocument (commentDocumentID SERIAL PRIMARY KEY, commentID INT, FOREIGN KEY (commentId) REFERENCES Comments , documentReference VARCHAR(255) NOT NULL, flagged INT DEFAULT 0, lastAccessed DATE)");
            
            db.mCreateAffinityTable = db.mConnection.prepareStatement(
                "CREATE TABLE Affinities (username VARCHAR(255) NOT NULL, ideaID INT "
                + "NOT NULL, like_status bool, PRIMARY KEY(username,ideaID), FOREIGN KEY (username) REFERENCES Users, FOREIGN KEY (ideaID) REFERENCES Idea)");
  

            db.mCreateCommentTable = db.mConnection.prepareStatement("CREATE TABLE Comments (commentID SERIAL PRIMARY KEY, username VARCHAR(50) NOT NULL, ideaid INT NOT NULL, comment VARCHAR(500) NOT NULL, FOREIGN KEY (username) REFERENCES Users, FOREIGN KEY (ideaid) REFERENCES Idea)");

            // Standard CRUD operations
            db.mDropIdea = db.mConnection.prepareStatement("DELETE FROM idea WHERE ideaID = ?");
            db.mDropIdeaTable = db.mConnection.prepareStatement("DROP table idea");
            db.mDropIdeaDocument = db.mConnection.prepareStatement("DELETE FROM ideaDocument WHERE ideaDocumentId = ?");
            db.mDropIdeaDocumentTable = db.mConnection.prepareStatement("DROP table ideaDocument");
            
            db.mDropCommentDocument = db.mConnection.prepareStatement("DELETE FROM commentDocument WHERE commentDocumentId = ?");
            db.mDropCommentDocumentTable = db.mConnection.prepareStatement("DROP table commentDocument");
            
            db.mDropCommentTable = db.mConnection.prepareStatement("DROP table comments");
            db.mDropAffinityTable = db.mConnection.prepareStatement("DROP table affinities");
            db.mDropUserTable = db.mConnection.prepareStatement("DROP table users");
            db.mAddIdea = db.mConnection.prepareStatement("INSERT INTO idea VALUES (DEFAULT, ?, ?, ?)");
            db.mAddIdeaDocument = db.mConnection.prepareStatement("INSERT INTO ideaDocument VALUES (?, ?, ?, ?, ?)");//////////////////////////////////////////////////////
            db.mAddCommentDocument = db.mConnection.prepareStatement("INSERT INTO commentDocument VALUES (?, ?, ?, ?, ?)");//////////////////////////////////////////////////////
            
            db.mAddUser = db.mConnection.prepareStatement("INSERT INTO users VALUES (?, ?, ?, ?, ?, ?)");
            db.mDropUser = db.mConnection.prepareStatement("DELETE FROM users WHERE username = ?");
            db.mAddComment = db.mConnection.prepareStatement("INSERT INTO comments VALUES (DEFAULT, ?, ?, ?)");
            db.mDropComment = db.mConnection.prepareStatement("DELETE FROM comments WHERE commentID = ?");
            db.mSelectAll = db.mConnection.prepareStatement("SELECT ideaID, idea_title FROM idea");
            db.mSelectIdea = db.mConnection.prepareStatement("SELECT * from idea WHERE username = ?");
            db.mSelectIdeaDocument = db.mConnection.prepareStatement("SELECT * from ideaDocument WHERE ideaDocumentId = ?");
            
            db.mSelectCommentDocument = db.mConnection.prepareStatement("SELECT * from commentDocument WHERE commentDocumentId = ?");
            db.mSelectUsername = db.mConnection.prepareStatement("SELECT username from idea WHERE userName= ?");
            db.mSelectUser = db.mConnection.prepareStatement("SELECT * from users WHERE username = ?");
            db.mLikeScore = db.mConnection.prepareStatement("SELECT likecount FROM idea WHERE ideaID = ?");
            db.mAffinity = db.mConnection.prepareStatement("INSERT INTO affinities VALUES (?, ?, ?)");
            db.mAddLike = db.mConnection.prepareStatement("UPDATE idea SET likecount = likecount + 1 WHERE ideaID = ?");
            db.mAddDislike = db.mConnection.prepareStatement("UPDATE idea SET likecount = likecount - 1 WHERE ideaID = ?");
            db.mDropAffinity = db.mConnection.prepareStatement("DELETE FROM affinities where username = ? and ideaID = ?");
            db.mDropAllAffinity = db.mConnection.prepareStatement("DELETE FROM affinities WHERE ideaID = ?");
            db.mSelectComment = db.mConnection.prepareStatement("SELECT * FROM comments where idea_ID = ?");
            db.mCheckLike = db.mConnection.prepareStatement("SELECT like_status from affinities where ideaID = ? and username = ?");
            db.mCheckFlagg = db.mConnection.prepareStatement("SELECT ideaDocumentId, flagged from ideaDocument");
            db.mCheckDate = db.mConnection.prepareStatement("SELECT * from ideaDocument order by lastAccessed limit 1");
            db.mCheckInapr= db.mConnection.prepareStatement("SELECT ideaID, message from idea");
            
            db.mCheckFlaggC = db.mConnection.prepareStatement("SELECT commentDocumentId, flagged from commentDocument");
            db.mCheckDateC = db.mConnection.prepareStatement("SELECT * from commentDocument order by lastAccessed limit 1");
            db.mCheckInaprC = db.mConnection.prepareStatement("SELECT commentID, comment from comments");

            
            
            //ADD VOTING OPTIONS
        } catch (SQLException e) {
            e.printStackTrace();
            db.disconnect();
            return null;
        }
        return db;
    }

    /**
     * Close the current connection to the database, if one exists.
     * 
     * NB: The connection will always be null after this call, even if an 
     *     error occurred during the closing operation.
     * 
     * @return True if the connection was cleanly closed, false otherwise
     */
    boolean disconnect() {
        if (mConnection == null) {
            System.err.println("Unable to close connection: Connection was null");
            return false;
        }
        try {
            mConnection.close();
        } catch (SQLException e) {
            System.err.println("Error: Connection.close() threw a SQLException");
            e.printStackTrace();
            mConnection = null;
            return false;
        }
        mConnection = null;
        return true;
    }


    /**
     * Query the database for a list of all idea_titles and their IDs
     * 
     * @return All rows, as an ArrayList
     */
    ArrayList<ideaData> selectIdea(String username) {
        ArrayList<ideaData> res = new ArrayList<ideaData>();
        try {
            mSelectIdea.setString(1, username);
            ResultSet rs = mSelectIdea.executeQuery();
            while (rs.next()) {
                res.add(new ideaData(rs.getInt("ideaID"), rs.getString("idea_title"), rs.getString("idea_body"), rs.getInt("likecount"))); // ADD VOTES if needed
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    ArrayList<ideaDocumentData> selectIdeaDocument(int ideaDocumentId) {
        ArrayList<ideaDocumentData> res = new ArrayList<ideaDocumentData>();
        try {
            mSelectIdeaDocument.setInt(1, ideaDocumentId);
            ResultSet rs = mSelectIdeaDocument.executeQuery();
            while (rs.next()) {
                res.add(new ideaDocumentData(rs.getInt("ideaID"), rs.getInt("ideaDocumentID"), rs.getString("Document_Reference"), rs.getInt("flagged"), rs.getDate("lastAccessed"))); // ADD VOTES if needed
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
    
    
    
    
    ArrayList<commentDocumentData> selectCommentDocument(int commentDocumentId) {
        ArrayList<commentDocumentData> res = new ArrayList<commentDocumentData>();
        try {
            mSelectCommentDocument.setInt(1, commentDocumentId);
            ResultSet rs = mSelectCommentDocument.executeQuery();
            while (rs.next()) {
                res.add(new commentDocumentData(rs.getInt("commentDocumentID"), rs.getInt("commentID"), rs.getString("Document_Reference"), rs.getInt("flagged"), rs.getDate("lastAccessed"))); // ADD VOTES if needed
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    


    userData selectUser(String username) {
        userData res = null;
        try {
            mSelectUser.setString(1, username);
            ResultSet rs = mSelectUser.executeQuery();
            if (rs.next()) {
                res = new userData(rs.getString("username"), rs.getString("dob"), rs.getString("password"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    ArrayList<commentData> selectComments(int ideaID){
        ArrayList<commentData> res = new ArrayList<commentData>();
        try {
            mSelectComment.setInt(1, ideaID);
            ResultSet rs = mSelectComment.executeQuery();
            while (rs.next()) {
                res.add(new commentData(rs.getInt("commentID"), rs.getString("username"), rs.getInt("idea_ID") , rs.getString("comment")));
            } 
            rs.close();
            return res;
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }     
    }


    /**
     * Create idea.  If it already exists, this will print an error
     */
    void createIdeaTable() {
        try {
            mCreateIdeaTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void addIdea(String username, String title, String message) {
        try {
            mAddIdea.setString(1, username);
            mAddIdea.setString(2, title);
            mAddIdea.setString(3, message);
            mAddIdea.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void dropIdea(int id) {
        try {
            mDropComment.setInt(1, id);
            mDropAllAffinity.setInt(1, id);
            mDropIdea.setInt(1, id);
            mDropComment.executeUpdate();
            mDropAllAffinity.executeUpdate();
            mDropIdea.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    ////////////////////////////////////////////////////////////////////////
    
    /**
     * Create ideaDocument.  If it already exists, this will print an error
     */
    void createIdeaDocumentTable() {
        try {
            mCreateIdeaDocumentTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void addIdeaDocument(int ideaId, int ideaDocumentId, String documentReference, int flagged) {
    	  Date date = new Date();  
        try {
            mAddIdeaDocument.setInt(1, ideaId);
            mAddIdeaDocument.setInt(2, ideaDocumentId);
            mAddIdeaDocument.setString(3, documentReference);
            mAddIdeaDocument.setInt(4, flagged);
            mAddIdeaDocument.setDate(5, new java.sql.Date(date.getTime()));
            mAddIdeaDocument.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void dropIdeaDocument(int ideaDocumentId) {
        try {
            mDropIdeaDocument.setInt(1, ideaDocumentId);
            mDropIdeaDocument.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    ////////////////////////////////////////////////
    

    
    
    
    
    
////////////////////////////////////////////////////////////////////////

	/**
	 * Create ideaDocument. If it already exists, this will print an error
	 */
	void createCommentDocumentTable() {
		try {
			mCreateCommentDocumentTable.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	void addCommentDocument(int commentId, int commentDocumentId, String documentReference, int flagged) {
		Date date = new Date();
		try {
			mAddCommentDocument.setInt(1, commentId);
			mAddCommentDocument.setInt(2, commentDocumentId);
			mAddCommentDocument.setString(3, documentReference);
			mAddCommentDocument.setInt(4, flagged);
			mAddCommentDocument.setDate(5, new java.sql.Date(date.getTime()));
			mAddCommentDocument.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	void dropCommentDocument(int commentDocumentId) {
		try {
			mDropCommentDocument.setInt(1, commentDocumentId);
			mDropCommentDocument.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

////////////////////////////////////////////////

    void createUserTable() {
        try {
            mCreateUserTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void dropIdeaTable() {
        try{
        mDropAffinityTable.execute();
        mDropCommentTable.execute();
        mDropIdeaTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
    
    void dropIdeaDocumentTable() {
        try{
        mDropIdeaDocumentTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
    
    void dropCommentDocumentTable() {
        try{
        mDropCommentDocumentTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }

    /**
     * Remove an idea from the database.  If it does not exist, this will print
     * an error.
     */
    void dropUser(String userName) {
        try {        
        	mDropUser.setString(1, userName);
            mDropUser.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void addUser(String username, String dob, String password, String firstName, String lastName, String email){
        try {
        	mAddUser.setString(1, username);
        	mAddUser.setString(2, dob);
        	mAddUser.setString(3, password);
        	mAddUser.setString(4, firstName);
        	mAddUser.setString(5, lastName);
        	mAddUser.setString(6, email);
            mAddUser.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void dropComment(int id) {
        try {
        	mDropComment.setInt(1, id);
            mDropComment.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void addComment(String username, int id, String comment){
        try {
        	mAddComment.setString(1, username);
        	mAddComment.setInt(2, id);
        	mAddComment.setString(3, comment);
            mAddComment.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    void createCommentTable() {
        try {
            mCreateCommentTable.execute();
        } catch (SQLException e) {
           e.printStackTrace();
        }
    }
    
    int likeScore(int id) {
        try {
            mLikeScore.setInt(1, id);
            ResultSet rs = mLikeScore.executeQuery();
            if (rs.next()) {
            int likeCount = rs.getInt("likecount");
            return likeCount;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    void likes(String username, int id) {
        try {
        	mAffinity.setString(1, username);
        	mAffinity.setInt(2, id);
        	mAffinity.setBoolean(3, true);
        	mAffinity.executeUpdate();
            mAddLike.setInt(1, id);
            mAddLike.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void dislikes(String username, int id) {
        try {
        	mAffinity.setString(1, username);
        	mAffinity.setInt(2, id);
        	mAffinity.setBoolean(3, false);
        	mAffinity.executeUpdate();
            mAddDislike.setInt(1, id);
            mAddDislike.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void createAffinityTable() {
        try {
            mCreateAffinityTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Table created");
    }

    void dropAffinity(String username, int id) {
        try {
            mCheckLike.setInt(1, id);
            mCheckLike.setString(2, username);
            ResultSet rs = mCheckLike.executeQuery();
            if (rs != null){                
                if (rs.next()){
                      mDropAffinity.setString(1, username);
        	          mDropAffinity.setInt(2, id);    	
                      mDropAffinity.executeUpdate();
                     boolean checkLike = rs.getBoolean("like_status");
                     if (checkLike = true){
                        mAddDislike.setInt(1, id);
                        mAddDislike.executeUpdate();
                    }   
                    else{
                        mAddLike.setInt(1, id);
                        mAddLike.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
     void DropCommentTable(){
         try{
        mDropCommentTable.execute();
         }catch(SQLException e){
            e.printStackTrace();
         }
         
     }


     void dropInnapropriateContent() {
    	 try {
             ResultSet rs = mCheckFlagg.executeQuery();
             if (rs != null){                
                 if (rs.next()){
                	 
                	 while(rs.next()) {
                      if (rs.getInt(2) == 1){////////////check flagg
                    	 mDropIdeaDocument.setInt(1, rs.getInt("ideaDocumentId"));    	
                     	 mDropIdeaDocument.executeUpdate();
                     }   
                     
                	 }//end of while loop
                 }
             }
         } catch (SQLException e) {
             e.printStackTrace();
         }
     }//end of method
     
	 void dropLeastViewedContent() {
		 try {
	            ResultSet rs = mCheckDate.executeQuery();
	            if (rs != null){                
	                if (rs.next()){
	        	          mDropIdeaDocument.setInt(1, rs.getInt("ideaDocumentId"));    ///////////////////	chegg and compare all dates of document
	        	          mDropIdeaDocument.executeUpdate();
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    	 
	     }
	 
	 
	 
	 
	 
	 void dropInapr() {
    	 try {
             ResultSet rs = mCheckInapr.executeQuery();
             if (rs != null){                
                 if (rs.next()){
                	 int i = 0;
                	 while(rs.next()) {
                      if (rs.getString("message").contains(inapr[i])){////////////check flagg
                    	 mDropIdea.setInt(1, rs.getInt("ideaID"));    	
                     	 mDropIdea.executeUpdate();
                     	 i++;
                     }   
                     
                	 }//end of while loop
                 }
             }
         } catch (SQLException e) {
             e.printStackTrace();
         }
     }//end of method

	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 void dropInnapropriateContentC() {
    	 try {
             ResultSet rs = mCheckFlaggC.executeQuery();
             if (rs != null){                
                 if (rs.next()){
                	 
                	 while(rs.next()) {
                      if (rs.getInt(2) == 1){////////////check flagg
                    	 mDropCommentDocument.setInt(1, rs.getInt("commentDocumentId"));    	
                     	 mDropCommentDocument.executeUpdate();
                     }   
                     
                	 }//end of while loop
                 }
             }
         } catch (SQLException e) {
             e.printStackTrace();
         }
     }//end of method
     
	 void dropLeastViewedContentC() {
		 try {
	            ResultSet rs = mCheckDateC.executeQuery();
	            if (rs != null){                
	                if (rs.next()){
	        	          mDropCommentDocument.setInt(1, rs.getInt("commentDocumentId"));    ///////////////////	chegg and compare all dates of document
	        	          mDropCommentDocument.executeUpdate();
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    	 
	     }
	 
	 
	 
	 
	 
	 void dropInaprC() {
    	 try {
             ResultSet rs = mCheckInaprC.executeQuery();
             if (rs != null){                
                 if (rs.next()){
                	 int i = 0;
                	 while(rs.next()) {
                      if (rs.getString("message").contains(inapr[i])){////////////check flagg
                    	 mDropComment.setInt(1, rs.getInt("commentID"));    	
                     	 mDropComment.executeUpdate();
                     	 i++;
                     }   
                     
                	 }//end of while loop
                 }
             }
         } catch (SQLException e) {
             e.printStackTrace();
         }
     }//end of method
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
     
     
}
