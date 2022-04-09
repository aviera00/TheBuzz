package edu.lehigh.cse216.yis223.admin;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;


import edu.lehigh.cse216.yis223.admin.Database.commentData;
import edu.lehigh.cse216.yis223.admin.Database.ideaData;
import edu.lehigh.cse216.yis223.admin.Database.userData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * App is our basic admin app. For now, it is a demonstration of the six key
 * operations on a database: connect, insert, update, query, delete, disconnect
 */
public class App {

	/**
	 * Print the menu for our program
	 */
	static void menu() {
		System.out.println("Main Menu");
		System.out.println("  [A] Create user table");
		System.out.println("  [B] Add user");
		System.out.println("  [C] Drop user");
		System.out.println("  [D] Create idea table");
		System.out.println("  [E] Add idea");
		System.out.println("  [F] Drop idea");
		System.out.println("  [G] Create affinity table");
		System.out.println("  [H] Add like");
		System.out.println("  [I] Add dislike");
		System.out.println("  [J] Drop affinity");
		System.out.println("  [K] Create comment table");
		System.out.println("  [L] Add comment");
		System.out.println("  [M] Drop comment");
		System.out.println("  [N] Find user");
		System.out.println("  [O] Find ideas from a user");
		System.out.println("  [P] Find comments on an idea");
		System.out.println("  [Q] Find an idea's like count ");
		System.out.println("  [Z] Quit Program");
		System.out.println("  [?] Help (this message)");
	}

	/**
	 * Ask the user to enter a menu option; repeat until we get a valid option
	 * 
	 * @param in      A BufferedReader, for reading from the keyboard
	 * @param message message to display
	 * @return The character corresponding to the chosen menu option
	 */
	static char prompt(BufferedReader in, String message) {
		// The valid actions:
		String actions = "Enter action: ? for help";
		if (message != null) {
			actions = message;
		}

		// We repeat until a valid single-character option is selected
		while (true) {
			System.out.print("[" + actions + "] :> ");
			String action;
			try {
				action = in.readLine();
				actions = action;
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
			if (action.length() != 1)
				continue;
			if (actions.contains(action)) {
				return action.charAt(0);
			}
			System.out.println("Invalid Command");
		}
	}

	/**
	 * Ask the user to enter a String message
	 * 
	 * @param in      A BufferedReader, for reading from the keyboard
	 * @param message A message to display when asking for input
	 * 
	 * @return The string that the user provided. May be "".
	 */
	static String getString(BufferedReader in, String message) {
		String s;
		try {
			System.out.print(message + " :> ");
			s = in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
		return s;
	}

	/**
	 * Ask the user to enter an integer
	 * 
	 * @param in      A BufferedReader, for reading from the keyboard
	 * @param message A message to display when asking for input
	 * 
	 * @return The integer that the user provided. On error, it will be -1
	 */
	static int getInt(BufferedReader in, String message) {
		int i = -1;
		try {
			System.out.print(message + " :> ");
			i = Integer.parseInt(in.readLine());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return i;
	}

	/**
	 * The main routine runs a loop that gets a request from the user and processes
	 * it
	 * 
	 * @param argv Command-line options. Ignored by this program.
	 */
	public static void main(String[] argv) {
		Scanner keyboard = new Scanner(System.in);
		int operation = 0;
		int operation2 = 0;
		int operation3 = 0;
		int operation4 = 0;
		int operation5 = 0;
		// get the Postgres configuration from the environment
		Map<String, String> env = System.getenv();
		String db_url = env.get("DATABASE_URL") + "?sslmode=require";

		Database db = Database.getDatabase(db_url);
		if (db == null)
			return;
		// postgres://rnekgftpvgjpkg:6ff4064ce148c71d5b57b60242f91bd09a3d41eb6222f575da2d0aa4796bb7de@ec2-23-23-128-222.compute-1.amazonaws.com:5432/d8q2nctgjjbrv2

		// Get a fully-configured connection to the database, or exit
		// immediately

		// Start our basic command-line interpreter:
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		do
		{
			operation = getOperation(keyboard);
			switch (operation) {
			case 1: // manage users
	/////////////////////////////////////////////////////////SUBMENUE
				do {
					operation2 = getOperationUsers(keyboard);
					switch (operation2) {
					case 1: //Create user table
						db.createUserTable();
						break;
					case 2: // Add user
						String userName = getString(in, "Enter username");
						String dob = getString(in, "Enter dob"); // add check to make sure a valid date
						String password = getString(in, "Enter password");
						String firstName = getString(in, "Enter first name");
						String lastName = getString(in, "Enter last name");
						String email = getString(in, "Enter email"); // add check to make sure a valid email
						db.addUser(userName, dob, password, firstName, lastName, email);
						break;
					case 3: // Drop user
						String userName1 = getString(in, "Enter username");
						db.dropUser(userName1);
						break;
					case 4: // Find user
						String username8 = getString(in, "Enter username");
						userData res = db.selectUser(username8);
						if (res != null) {
							System.out.println("username:  " + res.musername);
							System.out.println("dob:  " + res.mdob);
							System.out.println("password:  " + res.mpassword);
							System.out.println("first name:  " + res.mfirstName);
							System.out.println("last name:  " + res.mlastName);
							System.out.println("email:  " + res.memail);
						}
						break;
					case 5: // find ideas from users
						String username9 = getString(in, "Enter username");
						ArrayList<Database.ideaData> res1 = db.selectIdea(username9);
						if (res1 == null)
							continue;
						System.out.println("  -------------------------");
						for (Database.ideaData rd : res1) {
							System.out.println("  [" + rd.mId + "] " + rd.midea_title + ": " + rd.mMessage);
						}
						break;
					case 6: //
						System.out.println("Back to main menue");
						for(int i = 0; i < 80*300; i++) // Default Height of cmd is 300 and Default width is 80
						    System.out.print("-"); // Prints a backspace
						break;
					}
				} while (operation2 != 6);
	////////////////////////////////////////////////////////////SUBMENUE END

				break;
			case 2: //
	/////////////////////////////////////////////////////////SUBMENUE
				do {
					//operation3 = getOperation2(keyboard);
					switch (operation3) {
					case 1: // view balance
						
						break;
					case 2: // deposit
						
						break;
					case 3: // withdraw
						
						break;
					case 4: // view monthly interest
						
						break;
					case 5: // View profit/loss
						
						break;
					case 6: // return to main menue
						
						break;
					}
				} while (operation3 != 6);
	////////////////////////////////////////////////////////////SUBMENUE END

				break;/// End of manage users
			case 3: // manage ideas
	/////////////////////////////////////////////////////////SUBMENUE
				do {
					operation4 = getOperationIdeas(keyboard);
					switch (operation4) {
					case 1: //
						db.createIdeaTable();
						break;
					case 2: //
						String username = getString(in, "Enter username"); // Add check to make sure a valid username
						String title = getString(in, "Enter title");
						String message = getString(in, "Enter message");
						db.addIdea(username, title, message);
						break;
					case 3: //
						int id = getInt(in, "Enter id");
						db.dropIdea(id);
						break;
					case 4: //
						db.createAffinityTable();
						break;
					case 5: //
						String username2 = getString(in, "Enter username");
						int id1 = getInt(in, "Enter id");
						db.likes(username2, id1);
						break;
					case 6: //
						String username3 = getString(in, "Enter username");
						int id2 = getInt(in, "Enter id");
						db.dislikes(username3, id2);
						break;
					case 7: //
						String username4 = getString(in, "Enter username"); // add check for valid username and id
						int id5 = getInt(in, "Enter id");
						db.dropAffinity(username4, id5);
						break;
					case 8: //
						String username9 = getString(in, "Enter username");
						ArrayList<Database.ideaData> res1 = db.selectIdea(username9);
						if (res1 == null)
							continue;
						System.out.println("  -------------------------");
						for (Database.ideaData rd : res1) {
							System.out.println("  [" + rd.mId + "] " + rd.midea_title + ": " + rd.mMessage);
						}
						break;
					case 9: //
						int id11 = getInt(in, "Enter idea id");
						ArrayList<Database.commentData> res3 = db.selectComments(id11);
						if (res3 != null) {
							for (Database.commentData rd : res3) {
								System.out.println("commentID: " + rd.mcommentID);
								System.out.println("username: " + rd.musername);
								System.out.println("ideaID: " + rd.mideaID);
								System.out.println("comment: " + rd.mcomment);
								System.out.println("");
							}
						}
						break;
					case 10: //
						int ideaId = getInt(in, "Enter idea id");

						int likescore = db.likeScore(ideaId);
						System.out.println(likescore);
						break;
					case 11: //
						db.createIdeaDocumentTable();
						break;
					case 12: //
						int ideaId10 = getInt(in, "Enter idea document ID"); // Add check to make sure a valid username
						int ideaDocumentId = getInt(in, "idea Id");
						String documentReference = getString(in, "Enter reference Link");
						int flagged = getInt(in, "flagged, 0 if yes, 1 if not");// 0 if not flagged one if flagged
						db.addIdeaDocument(ideaId10, ideaDocumentId, documentReference, flagged);
						break;
					case 13: // Drop idea document
						int ideaDocumentIdd = getInt(in, "Enter id");
						db.dropIdeaDocument(ideaDocumentIdd);
						break;
					case 14: // drop idea document table
						db.dropIdeaDocumentTable();
						break;
					case 15: // drop least used
						db.dropLeastViewedContent();
						break;
					case 16: // drop inappropriate content
						db.dropInnapropriateContent();
						break;
					case 17: //
						System.out.println("Back to main menue");
						for(int i = 0; i < 80*300; i++) // Default Height of cmd is 300 and Default width is 80
						    System.out.print("-"); // Prints a backspace
						break;
					}
				} while (operation4 != 17);
	////////////////////////////////////////////////////////////SUBMENUE END

				break;/// End of manage ideas
			case 4: // manage comments
	/////////////////////////////////////////////////////////SUBMENUE
				do {
					operation5 = getOperationComments(keyboard);
					switch (operation5) {
					case 1: //
						db.createCommentTable();
						break;
					case 2: //
						String username6 = getString(in, "Enter username"); // Add check to make sure a valid username
						int id7 = getInt(in, "Enter idea ID");
						String comment = getString(in, "Enter comment");
						db.addComment(username6, id7, comment);
						break;
					case 3: //
						int id8 = getInt(in, "Enter comment ID");
						db.dropComment(id8);
						break;
					case 4: //
						int id11 = getInt(in, "Enter idea id");
						ArrayList<Database.commentData> res3 = db.selectComments(id11);
						if (res3 != null) {
							for (Database.commentData rd : res3) {
								System.out.println("commentID: " + rd.mcommentID);
								System.out.println("username: " + rd.musername);
								System.out.println("ideaID: " + rd.mideaID);
								System.out.println("comment: " + rd.mcomment);
								System.out.println("");
							}
						}
						break;
					case 5: //
						db.createCommentDocumentTable();
						break;
					case 6: //
						int CommentId10 = getInt(in, "Enter Comment document ID"); // Add check to make sure a valid username
						int CommentDocumentId = getInt(in, "Comment Id");
						String documentReference = getString(in, "Enter reference Link");
						int flagged = getInt(in, "flagged, 0 if yes, 1 if not");// 0 if not flagged one if flagged
						db.addCommentDocument(CommentId10, CommentDocumentId, documentReference, flagged);
						break;
					case 7: // Drop idea document
						int CommentDocumentIdd = getInt(in, "Enter id");
						db.dropCommentDocument(CommentDocumentIdd);
						break;
					case 8: // drop idea document table
						db.dropCommentDocumentTable();
						break;
					case 9: // drop least used
						db.dropLeastViewedContentC();
						break;
					case 10: // drop inappropriate content
						db.dropInnapropriateContentC();
						break;
					case 11: //
						System.out.println("Back to main menue");
						for(int i = 0; i < 80*300; i++) // Default Height of cmd is 300 and Default width is 80
						    System.out.print("-"); // Prints a backspace
						break;
					}
				} while (operation5 != 11);
	////////////////////////////////////////////////////////////SUBMENUE END

				break; /// End of manage comments
			case 5: // Quit program

				break;
			}
		}while(operation!=5); //end of main menue
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		/*
		do {
			operation = getOperation(keyboard);
			switch (operation) {
			case 1: //
				db.createUserTable();
				break;
			case 2: //
				String userName = getString(in, "Enter username");
				String dob = getString(in, "Enter dob"); // add check to make sure a valid date
				String password = getString(in, "Enter password");
				String firstName = getString(in, "Enter first name");
				String lastName = getString(in, "Enter last name");
				String email = getString(in, "Enter email"); // add check to make sure a valid email
				db.addUser(userName, dob, password, firstName, lastName, email);
				break;
			case 3: //
				String userName1 = getString(in, "Enter username");
				db.dropUser(userName1);
				break;
			case 4: //
				db.createIdeaTable();
				break;
			case 5: //
				String username = getString(in, "Enter username"); // Add check to make sure a valid username
				String title = getString(in, "Enter title");
				String message = getString(in, "Enter message");
				db.addIdea(username, title, message);
				break;
			case 6: //
				int id = getInt(in, "Enter id");
				db.dropIdea(id);
				break;
			case 7: //
				db.createAffinityTable();
				break;
			case 8: //
				String username2 = getString(in, "Enter username");
				int id1 = getInt(in, "Enter id");
				db.likes(username2, id1);
				break;
			case 9: //
				String username3 = getString(in, "Enter username");
				int id2 = getInt(in, "Enter id");
				db.dislikes(username3, id2);
				break;
			case 10: //
				String username4 = getString(in, "Enter username"); // add check for valid username and id
				int id5 = getInt(in, "Enter id");
				db.dropAffinity(username4, id5);
				break;
			case 11: //
				db.createCommentTable();
				break;
			case 12: //
				String username6 = getString(in, "Enter username"); // Add check to make sure a valid username
				int id7 = getInt(in, "Enter idea ID");
				String comment = getString(in, "Enter comment");
				db.addComment(username6, id7, comment);
				break;
			case 13: //
				int id8 = getInt(in, "Enter comment ID");
				db.dropComment(id8);
				break;
			case 14: //
				String username8 = getString(in, "Enter username");
				userData res = db.selectUser(username8);
				if (res != null) {
					System.out.println("username:  " + res.musername);
					System.out.println("dob:  " + res.mdob);
					System.out.println("password:  " + res.mpassword);
					System.out.println("first name:  " + res.mfirstName);
					System.out.println("last name:  " + res.mlastName);
					System.out.println("email:  " + res.memail);
				}
				break;
			case 15: //
				String username9 = getString(in, "Enter username");
				ArrayList<Database.ideaData> res1 = db.selectIdea(username9);
				if (res1 == null)
					continue;
				System.out.println("  -------------------------");
				for (Database.ideaData rd : res1) {
					System.out.println("  [" + rd.mId + "] " + rd.midea_title + ": " + rd.mMessage);
				}
				break;
			case 16: //
				int id11 = getInt(in, "Enter idea id");
				ArrayList<Database.commentData> res3 = db.selectComments(id11);
				if (res3 != null) {
					for (Database.commentData rd : res3) {
						System.out.println("commentID: " + rd.mcommentID);
						System.out.println("username: " + rd.musername);
						System.out.println("ideaID: " + rd.mideaID);
						System.out.println("comment: " + rd.mcomment);
						System.out.println("");
					}
				}
				break;
			case 17: //
				int ideaId = getInt(in, "Enter idea id");

				int likescore = db.likeScore(ideaId);
				System.out.println(likescore);
				break;
			case 18: //
				db.createIdeaDocumentTable();
				break;
			case 19: //
				int ideaId10 = getInt(in, "Enter idea document ID"); // Add check to make sure a valid username
				int ideaDocumentId = getInt(in, "idea Id");
				String documentReference = getString(in, "Enter reference Link");
				int flagged = getInt(in, "flagged, 0 if yes, 1 if not");// 0 if not flagged one if flagged
				db.addIdeaDocument(ideaId10, ideaDocumentId, documentReference, flagged);
				break;
			case 20: // Drop idea document
				int ideaDocumentIdd = getInt(in, "Enter id");
				db.dropIdeaDocument(ideaDocumentIdd);
				break;
			case 21: // drop idea document table
				db.dropIdeaDocumentTable();
				break;
			case 22: // drop least used
				db.dropLeastViewedContent();
				break;
			case 23: // drop inappropriate content
				db.dropInnapropriateContent();
				break;
			case 24: //
				System.out.println("Thank you for using my program.");
				break;

			}
		} while (operation != 24);
		
		
		*/
		//////////////////////////////////////////////////////////// Menu END
		/*
		 * while (true) { // Get the user's request, and do it // // NB: for better
		 * testability, each action should be a separate // function call char action =
		 * prompt(in, null); if (action == '?') { menu(); } else if (action == 'Z') {
		 * break; } else if (action == 'A') { db.createUserTable();
		 * 
		 * } else if (action == 'B') { String userName = getString(in,
		 * "Enter username"); String dob = getString(in, "Enter dob"); // add check to
		 * make sure a valid date String password = getString(in, "Enter password");
		 * String firstName = getString(in, "Enter first name"); String lastName =
		 * getString(in, "Enter last name"); String email = getString(in,
		 * "Enter email"); // add check to make sure a valid email db.addUser(userName,
		 * dob, password, firstName, lastName, email); } else if (action == 'C') {
		 * String userName = getString(in, "Enter username"); db.dropUser(userName); }
		 * else if (action == 'D') { db.createIdeaTable(); } else if (action == 'E') {
		 * String username = getString(in, "Enter username"); // Add check to make sure
		 * a valid username String title = getString(in, "Enter title"); String message
		 * = getString(in, "Enter message"); db.addIdea(username, title, message); }
		 * else if (action == 'F') { int id = getInt(in, "Enter id"); db.dropIdea(id); }
		 * else if (action == 'G') { db.createAffinityTable(); } else if (action == 'H')
		 * { String username = getString(in, "Enter username"); int id = getInt(in,
		 * "Enter id"); db.likes(username, id); } else if (action == 'I') { String
		 * username = getString(in, "Enter username"); int id = getInt(in, "Enter id");
		 * db.dislikes(username, id); } else if (action == 'J') { String username =
		 * getString(in, "Enter username"); // add check for valid username and id int
		 * id = getInt(in, "Enter id"); db.dropAffinity(username, id); } else if (action
		 * == 'K') { db.createCommentTable(); } else if (action == 'L') { String
		 * username = getString(in, "Enter username"); // Add check to make sure a valid
		 * username int id = getInt(in, "Enter idea ID"); String comment = getString(in,
		 * "Enter comment"); db.addComment(username, id, comment); } else if (action ==
		 * 'M') { int id = getInt(in, "Enter comment ID"); db.dropComment(id); } else if
		 * (action == 'N') { String username = getString(in, "Enter username"); userData
		 * res = db.selectUser(username); if (res != null) {
		 * System.out.println("username:  " + res.musername);
		 * System.out.println("dob:  " + res.mdob); System.out.println("password:  " +
		 * res.mpassword); System.out.println("first name:  " + res.mfirstName);
		 * System.out.println("last name:  " + res.mlastName);
		 * System.out.println("email:  " + res.memail); } } else if (action == 'O') {
		 * String username = getString(in, "Enter username");
		 * ArrayList<Database.ideaData> res = db.selectIdea(username); if (res == null)
		 * continue; System.out.println("  -------------------------"); for
		 * (Database.ideaData rd : res) { System.out.println("  [" + rd.mId + "] " +
		 * rd.midea_title + ": " + rd.mMessage); } } else if (action == 'P') { int id =
		 * getInt(in, "Enter idea id"); ArrayList<Database.commentData> res =
		 * db.selectComments(id); if (res != null) { for (Database.commentData rd : res)
		 * { System.out.println("commentID: " + rd.mcommentID);
		 * System.out.println("username: " + rd.musername);
		 * System.out.println("ideaID: " + rd.mideaID); System.out.println("comment: " +
		 * rd.mcomment); System.out.println(""); } } } else if (action == 'Q') { int
		 * ideaId = getInt(in, "Enter idea id");
		 * 
		 * int likescore = db.likeScore(ideaId); System.out.println(likescore);
		 * 
		 * }
		 * 
		 * }
		 */
		db.disconnect();
	}// end of main

///////////////////////////////////////////////////////////////////////////////////////////////

	public static int getOperation(Scanner input) {
		int op = 0;
		do {
			System.out.println("Main Menu");
			System.out.println("  [1] Manage Users");
			System.out.println("  [2] Nothing yet");
			System.out.println("  [3] Manage Ideas");
			System.out.println("  [4] Manage comments");
			System.out.println("  [5] Quit");

			if (input.hasNext()) {

				op = input.nextInt();
				if (op >= 1 && op <= 5)
					break;
				else
					System.out.println("Invalid operation. Must be an integer from 1 to 18.");
			} else {
				input.nextLine();
				System.out.println("Invalid operation. Must be an integer.");
			}
		} while (true);
		return op;
	}

//////////////////////////////////////////////////////////////////////////////////////////////
	public static int getOperationUsers(Scanner input) {
		int op = 0;
		do {
			System.out.println("Users Menu");
			System.out.println("  [1] Create user table");
			System.out.println("  [2] Add user");
			System.out.println("  [3] Drop user");
			System.out.println("  [4] Find user");
			System.out.println("  [5] Find ideas from a user");
			System.out.println("  [6] Quit Program");

			if (input.hasNext()) {

				op = input.nextInt();
				if (op >= 1 && op <= 6)
					break;
				else
					System.out.println("Invalid operation. Must be an integer from 1 to 18.");
			} else {
				input.nextLine();
				System.out.println("Invalid operation. Must be an integer.");
			}
		} while (true);
		return op;
	}

//////////////////////////////////////////////////////////////////////////////////////////////
	public static int getOperationComments(Scanner input) {
		int op = 0;
		do {
			System.out.println("Comments Menu");
			System.out.println("  [1] Create comment table");
			System.out.println("  [2] Add comment");
			System.out.println("  [3] Drop comment");
			System.out.println("  [4] Find comments on an idea");
			System.out.println("  [5] Create comment document table");
			System.out.println("  [6] Add comment document");
			System.out.println("  [7] Drop comment document");
			System.out.println("  [8] Drop comment document table");
			System.out.println("  [9] Drop document, quota met!");
			System.out.println("  [10] Drop innapropriate content");
			System.out.println("  [11] Quit Program");

			if (input.hasNext()) {

				op = input.nextInt();
				if (op >= 1 && op <= 11)
					break;
				else
					System.out.println("Invalid operation. Must be an integer from 1 to 18.");
			} else {
				input.nextLine();
				System.out.println("Invalid operation. Must be an integer.");
			}
		} while (true);
		return op;
	}

//////////////////////////////////////////////////////////////////////////////////////////////
	public static int getOperationIdeas(Scanner input) {
		int op = 0;
		do {
			System.out.println("Ideas Menu");
			System.out.println("  [1] Create idea table");
			System.out.println("  [2] Add idea");
			System.out.println("  [3] Drop idea");
			System.out.println("  [4] Create affinity table");
			System.out.println("  [5] Add like");
			System.out.println("  [6] Add dislike");
			System.out.println("  [7] Drop affinity");
			System.out.println("  [8] Find ideas from a user");
			System.out.println("  [9] Find comments on an idea");
			System.out.println("  [10] Find an idea's like count ");
			System.out.println("  [11] Create idea document table");
			System.out.println("  [12] Add idea document");
			System.out.println("  [13] Drop idea document");
			System.out.println("  [14] Drop idea document table");
			System.out.println("  [15] Drop document, quota met!");
			System.out.println("  [16] Drop innapropriate content");
			System.out.println("  [17] Quit Program");

			if (input.hasNext()) {

				op = input.nextInt();
				if (op >= 1 && op <= 17)
					break;
				else
					System.out.println("Invalid operation. Must be an integer from 1 to 18.");
			} else {
				input.nextLine();
				System.out.println("Invalid operation. Must be an integer.");
			}
		} while (true);
		return op;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	
	
	
	


	
	
	
	
	
	
}// end of class