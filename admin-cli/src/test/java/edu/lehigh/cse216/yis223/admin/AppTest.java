package edu.lehigh.cse216.yis223.admin;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.ArrayList;

import org.eclipse.jgit.transport.CredentialItem.Username;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
        
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * @throws SQLException
     * 
     *
     */
    public void testDB() throws SQLException
    {   Map<String, String> env = System.getenv();
        
        String db_url = env.get("TEST_URL")+"?sslmode=require";
        Database db = Database.getDatabase("db_url"); 
        assertFalse("Cannot connect to database specified",db==null); 
        try{    
            db.addUser(null, null, null, null, null, null);
        } catch(Exception e){
            System.out.println("This should not work");
        }

        try{    
            db.addIdea(null, null, null);
        } catch(Exception e){
            System.out.println("This should not work");
        }
        
        try{    
            db.addIdeaDocument(50, 7, null, 0);
        } catch(Exception e){
            System.out.println("This should not work");
        }
        
        try{    
            db.addCommentDocument(14, 1, null, 0);
        } catch(Exception e){
            System.out.println("This should not work");
        }
        
        try{    
            db.dropCommentDocument(0);
        } catch(Exception e){
            System.out.println("This should not work");
        }
        
        try{    
            db.dropCommentDocument(0);
        } catch(Exception e){
            System.out.println("This should not work");
        }
        

        try{    
            db.addComment(null, 1, null);;
        } catch(Exception e){
            System.out.println("This should not work");
        }

        try{    
            db.likes(null, 1);
        } catch(Exception e){
            System.out.println("This should not work");
        }
        
        try{    
            db.dislikes(null, 1);
        } catch(Exception e){
            System.out.println("This should not work");
        }
        
        
        
    }
}
