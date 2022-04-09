package edu.lehigh.cse216.too223.backend;

public class MyUser {
    /**
     * The unique identifier associated with a User.  It's final, because
     * we never want to change it.
     */
    private String userName;
    /**
     * The user's date of birth
     */
    private String dob;
    /**
     * The user's password
     */
    private String password;

    /**
     * User's first name
     */
    public String firstName;

    /**
     * User's last name
     */
    public String lastName;

    /**
     * User's email address
     */
    private String email;

    
    /**
     * vote implemented as state machine(...n->up->down->n...)
     */
    private enum vote{
        upvoted {
            @Override
            public vote nextState() {
                return downvoted;
            }
            public int state(){
                return 1;
            }
        },
        downvoted {
            @Override
            public vote nextState() {
                return neutral;
            }
            public int state(){
                return -1;
            }
        },
        neutral {
            @Override
            public vote nextState() {
                return upvoted;
            }
            public int state(){
                return 0;
            }
        };
        /**
         * Switch the state machine to next state
         * @return the new state (...n->up->down->n...)
         */
        public abstract vote nextState();
        public abstract int state();
        /**
         * set the state machine to upvoted state
         * @return upvoted state
         */
        public vote upvote(){
            return upvoted;
        }
        /**
         * set the state machine to downvoted state
         * @return downvoted state
         */
        public vote downvote(){
            return downvoted;
        }
        /**
         * set the state machine to neutral state
         * @return netural state
         */
        public vote toNeutral(){
            return neutral;
        }
    };


    /**
    * Vote status of this user
    */
    private vote voteStatus;

    /**
     * Creates a new user object. This is designed to hold information retrieved from the database. The voting state is by default neutral.
     * @param username username
     * @param dob date of birth
     * @param pw password
     * @param fn first name
     * @param ln last name
     * @param email email
     */
    public MyUser(String username, String dob, String pw, String fn, String ln, String email){
        userName = username;
        this.dob=dob;
        password=pw;
        firstName=fn;
        lastName=ln;
        this.email=email;
        voteStatus=vote.neutral;
    } 

    //getters
    public String getUserName() {
        return userName;
    }

    public String getDob(){
        return dob;
    }

    public String getpw(){
        return password;
    }

    public String getFn(){
        return firstName;
    }

    public String getLn(){
        return lastName;
    }

    public String getEmail(){
        return email;
    }

    public int getVoteStatus(){
        return voteStatus.state();
    }

    //setters
    public void setDob(String n){
        dob=n;
    }

    public void setpw(String n){
        password=n;
    }

    public void setFn(String n){
        firstName=n;
    }

    public void setLn(String n){
        lastName=n;
    }

    public void setEmail(String n){
        email=n;
    }

    /**
     * upon clicking the vote button, the user's voting state is set to next state. (...n->up->down->n...)
     */
    public int clickVote(){
        voteStatus.nextState();
        return voteStatus.state();
    }

}
