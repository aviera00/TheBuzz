package edu.lehigh.cse216.too223.backend;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
// get code from admin app and compare and replace functions from datastore accordingly
public class Database {
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
     * A prepared statement for getting one row from the database
     */
    private PreparedStatement mSelectOne;

    /**
     * A prepared statement for deleting a row from the database
     */
    private PreparedStatement mDeleteOne;

    /**
     * A prepared statement for inserting into the database
     */
    private PreparedStatement mInsertOne;


    /**
     * A prepared statement for updating a single row in the database
     */
    private PreparedStatement mUpdateOne;

    /**
     * A prepared statement for updating like score single row in the database
     */
    private PreparedStatement mLikeScore;

     /**
     * A prepared statement for updating likes
     */
    private PreparedStatement mLikes;

     /**
     * A prepared statement for updating dislikes
     */
    private PreparedStatement mDisLikes;

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
    private PreparedStatement mCreateUserTable;

    /**
     * A prepared statement for creating the table in our database
     */
    private PreparedStatement mCreateAffinityTable;

    /**
     * A prepared statement for dropping the table in our database
     */
    private PreparedStatement mDropUserTable;

    /**
     * A prepared statement for dropping the table in our database
     */
    private PreparedStatement mDropIdeaTable;

    /**
     * A prepared statement for dropping the table in our database
     */
    private PreparedStatement mDropAffinityTable;

    /**
     * A prepared statement for creating the comment table in our database
     */
    private PreparedStatement mCreateCommentTable;

    /**
     * A prepared statement for dropping the comment table in our database
     */
    private PreparedStatement mDropCommentTable;

    /**
     * A prepared statement for setting down vote count
     */
    private PreparedStatement mDownVote;
    /**
     * A prepared statement for setting up vote count
     */
    private PreparedStatement mUpVote;
    /**
     * A prepared statement for showing down vote count
     */
    private PreparedStatement mDownVoteCount;
    /**
     * A prepared statement for showing down vote count
     */
    private PreparedStatement mUpVoteCount;

    /**
     * A prepared statement for adding a comment
     */
    private PreparedStatement changeComment;

    /**
     * A prepared statement for seeing a comment
     */
    private PreparedStatement seeComment;

    private PreparedStatement countComments;


    /**
     * RowData is like a struct in C: we use it to hold data, and we allow 
     * direct access to its fields.  In the context of this Database, RowData 
     * represents the data we'd see in a row.
     * 
     * We make RowData a static class of Database because we don't really want
     * to encourage users to think of RowData as being anything other than an
     * abstract representation of a row of the database.  RowData and the 
     * Database are tightly coupled: if one changes, the other should too.
     */
    public static class RowData {
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
        int mLikeScore;
        
        int mLikes;

        int mDislikes;

        int upVoteCount;

        int downVoteCount;


        //TODO: Add voting stuff
        /**
         * Construct a RowData object by providing values for its fields
         */
        public RowData(int id, String idea_title, String message) {
            mId = id;
            midea_title = idea_title;
            mMessage = message;
            mLikeScore = mLikes = mDislikes = upVoteCount = downVoteCount = 0;
        } // add voting stuff to constructor

        /**
         * Construct a RowData object by providing values for its fields
         */
        public RowData(int id, String idea_title, String message, int likescore) {
            mId = id;
            midea_title = idea_title;
            mMessage = message;
            mLikeScore = likescore;
            upVoteCount = downVoteCount = 0;
        }

        /**
         * Construct a RowData object by providing values for its fields, including vote stuff
         * @param id id
         * @param idea_title title
         * @param message message
         * @param likescore likescore
         * @param up upvotecount
         * @param down downvotecount
         */
        public RowData(int id, String idea_title, String message, int likescore, int up, int down) {
            mId = id;
            midea_title = idea_title;
            mMessage = message;
            mLikeScore = likescore;
            upVoteCount = up;
            downVoteCount = down;
        }
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
            String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() +"?sslmode=require";
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
                    + "NOT NULL, password VARCHAR(500) NOT NULL, firstName VARCHAR(255) NOT NULL,lastName VARCHAR(255) NOT NULL, email VARCHAR(255) NOT NULL, vote_status bit)"); //vote_status: 0(down),1(up),null(neutral)
            db.mDropUserTable = db.mConnection.prepareStatement("DROP TABLE Users");

            db.mCreateIdeaTable = db.mConnection.prepareStatement(
                    "CREATE TABLE idea (ideaID SERIAL PRIMARY KEY, idea_username VARCHAR(50) "
                    + "NOT NULL, idea_title VARCHAR(255) NOT NULL, idea_body VARCHAR(500) NOT NULL, timestamp VARCHAR(50) NOT NULL, likecount INTEGER DEFAULT 0)");
            db.mDropIdeaTable = db.mConnection.prepareStatement("DROP TABLE idea");
            
            db.mCreateAffinityTable = db.mConnection.prepareStatement(
                "CREATE TABLE Affinities (username VARCHAR(255), ideaID BIGINT "
                + "NOT NULL, like_status bit, up_vote_count BIGINT, down_vote_count BIGINT, PRIMARY KEY(username,ideaID))");
            db.mDropAffinityTable = db.mConnection.prepareStatement("DROP TABLE Affinities");

            db.mCreateCommentTable = db.mConnection.prepareStatement(
                "CREATE TABLE Comments (username VARCHAR(255), ideaID BIGINT "
                + "NOT NULL, comment VARCHAR(500), commentID BIGINT PRIMARY KEY)");
            db.mDropCommentTable = db.mConnection.prepareStatement("DROP TABLE Comments");

            // Standard CRUD operations
            db.mDeleteOne = db.mConnection.prepareStatement("DELETE FROM idea WHERE ideaID = ?");
            db.mInsertOne = db.mConnection.prepareStatement("INSERT INTO idea VALUES (default, ?, ?)");
            db.mSelectAll = db.mConnection.prepareStatement("SELECT ideaID, idea_title FROM idea");
            db.mSelectOne = db.mConnection.prepareStatement("SELECT * from idea WHERE ideaID=?");
            db.mUpdateOne = db.mConnection.prepareStatement("UPDATE idea SET message = ? WHERE ideaID = ?");
            db.mLikeScore = db.mConnection.prepareStatement("SELECT likecount FROM idea WHERE ideaID = ?");
            db.mLikes = db.mConnection.prepareStatement("UPDATE Affinities SET likestatus = ? WHERE ideaID = ?");
            db.mDisLikes = db.mConnection.prepareStatement("UPDATE idea SET dislikes = ? WHERE ideaID = ?");
            //voting stuff
            db.mUpVote=db.mConnection.prepareStatement("UPDATE Affinites SET up_vote_count = ? WHERE ideaID = ?");
            db.mDownVote=db.mConnection.prepareStatement("UPDATE Affinities SET down_vote_count = ? WHERE ideaID = ?");
            db.mUpVoteCount=db.mConnection.prepareStatement("SELECT up_vote_count FROM Affinities WHERE ideaID = ?");
            db.mDownVoteCount=db.mConnection.prepareStatement("SELECT down_vote_count FROM Affinities WHERE ideaID = ?");
            //commenting stuff
            db.changeComment=db.mConnection.prepareStatement("UPDATE Comments SET comment = ? WHERE commentID = ?");
            db.seeComment=db.mConnection.prepareStatement("SELECT comment, commentID FROM Comments WHERE ideaID = ?");
            db.countComments=db.mConnection.prepareStatement("SELECT count(comment) FROM Comments WHERE ideaID = ?");
        } catch (SQLException e) {
            System.err.println("Error creating prepared statement");
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
     * Insert a row into the database
     * 
     * @param idea_title The idea_title for this new row
     * @param message The message body for this new row
     * 
     * @return The number of rows that were inserted
     */
    int insertRow(String idea_title, String message) {
        int count = 0;
        try {
            mInsertOne.setString(1, idea_title);
            mInsertOne.setString(2, message);
            count += mInsertOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * Query the database for a list of all idea_titles and their IDs
     * 
     * @return All rows, as an ArrayList
     */
    ArrayList<RowData> selectAll() {
        ArrayList<RowData> res = new ArrayList<RowData>();
        try {
            ResultSet rs = mSelectAll.executeQuery();
            while (rs.next()) {
                res.add(new RowData(rs.getInt("ideaID"), rs.getString("idea_title"), null)); // ADD VOTES if needed
            }
            rs.close();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get all data for a specific row, by ID
     * 
     * @param id The id of the row being requested
     * 
     * @return The data for the requested row, or null if the ID was invalid
     */
    RowData selectOne(int id) {
        RowData res = null;
        try {
            mSelectOne.setInt(1, id);
            ResultSet rs = mSelectOne.executeQuery();
            if (rs.next()) {
                res = new RowData(rs.getInt("ideaID"), rs.getString("idea_title"), rs.getString("message"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Delete a row by ID
     * 
     * @param id The id of the row to delete
     * 
     * @return The number of rows that were deleted.  -1 indicates an error.
     */
    int deleteRow(int id) {
        int res = -1;
        try {
            mDeleteOne.setInt(1, id);
            res = mDeleteOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Update the message for a row in the database
     * 
     * @param id The id of the row to update
     * @param message The new message contents
     * 
     * @return The number of rows that were updated.  -1 indicates an error.
     */
    int updateOne(int id, String message) {
        int res = -1;
        try {
            mUpdateOne.setString(1, message);
            mUpdateOne.setInt(2, id);
            res = mUpdateOne.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Create idea table.  If it already exists, this will print an error
     */
    void createIdeaTable() {
        try {
            mCreateIdeaTable.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove idea from the database.  If it does not exist, this will print
     * an error.
     * @param ideaID the idea id that will be deleted
     */
    void removeIdea(int ideaID){
        try{
            mDeleteOne.setInt(1, ideaID);
            mDeleteOne.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }


    /**
     * Set the up vote count for an idea. Prints an error if not exist
     * @param count new up vote count
     * @param ideaID idea id
     */
    void setUpVote(int count, int ideaID){
        try {
            mUpVote.setInt(1, count);
            mUpVote.setInt(2, ideaID);
            mUpVote.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Upvote an idea
     * @param ideaID The id of the idea to upvote
     */
    void upVoteOne(int ideaID){
        try {
            mUpVote.setInt(1, getVoteCount(1, ideaID)+1);
            mUpVote.setInt(2, ideaID);
            mUpVote.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

     /**
     * Downvote an idea
     * @param ideaID The id of the idea to Downvote
     */
    void downVoteOne(int ideaID){
        try {
            mDownVote.setInt(1, getVoteCount(0, ideaID)+1);
            mDownVote.setInt(2, ideaID);
            mDownVote.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    /**
     * Set the down vote count for an idea. Prints an error if not exist
     * @param count new down vote count
     * @param ideaID idea id
     */
    void setDownVote(int count, int ideaID){
        try {
            mDownVote.setInt(1, count);
            mDownVote.setInt(2, ideaID);
            mUpVote.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Get the vote counts of an idea, error if invalid option or idea not exist and returns -1.
     * @param option either 0 or 1; 0:get down vote count, 1:get up vote count, 2:get likecount(up-down)
     * @param ideaID idea ID
     * @return the count of vote
     */
    int getVoteCount(int option, int ideaID){
        try{
            if (option==0){
                mDownVoteCount.setInt(1, ideaID);
                return mDownVoteCount.executeQuery().getInt(1);
            }
            else if (option==1){
                mUpVoteCount.setInt(1, ideaID);
                return mUpVoteCount.executeQuery().getInt(1);
            }
            else if (option==2){
                mUpVoteCount.setInt(1, ideaID);
                mDownVoteCount.setInt(1, ideaID);
                return mUpVoteCount.executeQuery().getInt(1)-mDownVoteCount.executeQuery().getInt(1);
            }
            else{
                throw new SQLException("option is either 0 or 1 or 2!");
            }
        } catch(SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            return -1;
        }
    }

     /**
      * Adding comment to an idea
      * @param ideaID The idea Id to comment on
      * @param usrn The user name of commentor
      * @param c comment body
      * @return -1 on error
      */
    int commentOne(int ideaID, String usrn, String c){
        try {
            countComments.setInt(1, ideaID);
            int commentNumber=countComments.executeQuery().getInt(1);
                PreparedStatement create=mConnection.prepareStatement("INSERT INTO Comments VALUES (?,?,?,?)");
                create.setString(1, usrn);
                create.setInt(2, ideaID);
                create.setString(3, c);
                create.setInt(4, commentNumber+1);
                return create.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;

    }

      /**
      * changing comment to an idea
      * @param ideaID The idea Id to comment on
      * @param usrn The user name of commentor
      * @param c comment body
      * @param cID comment id
      */
      int editComment(int ideaID, int cID, String usrn, String c){
        try {
            PreparedStatement check;
            check=mConnection.prepareStatement("SELECT username FROM Comments WHERE ideaID = ? AND commentID = ?");
            check.setInt(1, ideaID);
            check.setInt(2, cID);
            if (!check.executeQuery().getString(1).equals(usrn)){
                return 1;
            }
            changeComment.setString(1,c);
            changeComment.setInt(2, cID);
            mDownVote.executeUpdate();
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * view all comments for an idea
     * @param ideaID the id for the comment to view
     * @return
     */
    Hashtable<Integer,String> viewComment(int ideaID){
        try {
            seeComment.setInt(1, ideaID);
            ResultSet r = seeComment.executeQuery();
            Hashtable<Integer,String> result = new Hashtable<Integer,String>();
            while(r.next()){
                result.put((Integer)(r.getInt(2)),r.getString(1));
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     * The general method that drops tables. Error if table cannot be found.
     * @param tableName the name of the table to drop. Options: Users, Affinity, Comment, Idea
     */
    void dropTable(String tableName){
        try{
            switch(tableName){
                case "Users":
                mDropUserTable.execute();
                break;

                case "Affinity":
                mDropAffinityTable.execute();
                break;

                case "Comment":
                mDropCommentTable.execute();
                break;

                case "Idea":
                mDropIdeaTable.execute();
                break;

                default:
                throw new SQLException("can't recognize specified table!");
            }

        } catch(SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }
}
