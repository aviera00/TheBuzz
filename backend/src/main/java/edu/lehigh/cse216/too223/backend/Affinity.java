package edu.lehigh.cse216.too223.backend;

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
