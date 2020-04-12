import java.sql.*;
import java.util.ArrayList;

// Everytime your program adds or modifies a record in a table, it should print an message to the screen to that effect. If an error is encountered, you should print a message to the screen to the specifics of the error and continue processing

// You will need to implement code to check that the book being checked out exists in a library, and that a book being checked in has a corresponding checkout record

// Once you have finished loading the data for the day's activities, run the following queries
// Print the contents of the Borrowed_by table
// For each member that has a book checked out (Last name, first name, member id), print a list of the book titles currently checked out



public class Lab4 {

    private static final String NOT_AVAILABLE_VALUE = "N/A/";

    public static void main(String args[]){

        // connect to the SQL Server
        Connection connection = connectToSQL();

        if (connection != null) {

            // 	read in and parse the activity file in XML format
            try {
                Lab4_xml showXML = new Lab4_xml();
                showXML.readXML ("/s/bach/a/class/cs430dl/Current/more_assignments/LabData/Libdata.xml");
                ArrayList<BorrowedBy> borrowedByList = showXML.getBorrowedByArrayList();

                for (BorrowedBy bb : borrowedByList) {
                    // 	If the transaction is a checkin, simply update the corresponding record appropriately
                    if (!bb.getCheckin_date().equals(NOT_AVAILABLE_VALUE)) {
                        checkInBook(connection, bb);
                    } else {
                        //  If the transaction is a checkout, a new record is created
                        checkOutBook(connection, bb);
                    }
                }

            }catch( Exception e ) {
                e.printStackTrace();
            }

        } else {
            System.out.println(("USAGE: No connection found. Exiting program."));
            System.exit(1);
        }
    }

    private static Connection connectToSQL() {
        Connection con = null;

        try {
            Statement stmt;
            ResultSet rs;

            // Register the JDBC driver for MySQL.
            Class.forName("com.mysql.jdbc.Driver");

            // Define URL of database server for
            // database named 'user' on the faure.
            String url =
                    "jdbc:mysql://faure/dkielman";

            // Get a connection to the database for a
            // user named 'user' with the password
            // 123456789.
            con = DriverManager.getConnection(
                    url, "dkielman", "832167848");


        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }

    // executes check in SQL statements that inserts data into the appropriate relation tables
    private static void checkInBook(Connection connection, BorrowedBy borrowedBy) {
        try {
            Statement stmt;
            ResultSet rs;

            // Get a Statement object
            stmt = connection.createStatement();

            // see if the book is checked out already
            rs = stmt.executeQuery("SELECT * FROM BorrowedBy WHERE isbn = '" + borrowedBy.getIsbn() + "' AND member_id = '" + borrowedBy.getMember_id() + "' AND checkin_date = '9999-01-01';");

            if (!rs.next()) {
                System.out.println("The book with the ISBN " + borrowedBy.getIsbn() + " is not checked out.");
            } else {
                // Get a Statement object
                stmt = connection.createStatement();
                stmt.executeUpdate("UPDATE BorrowedBy SET checkin_date = '" + borrowedBy.getCheckin_date() + "' WHERE isbn = '" + borrowedBy.getIsbn() + "' AND member_id = '" + borrowedBy.getMember_id() + "' AND checkin_date IS NULL;");

                System.out.println("The book with the ISBN " + borrowedBy.getIsbn() + " is now checked in.");
            }

        } catch( Exception e ) {
            e.printStackTrace();
        }
    }

    // executes check in SQL statements that inserts data into the appropriate relation tables
    private static void checkOutBook(Connection connection, BorrowedBy borrowedBy) {
        try {
            Statement stmt;
            ResultSet rs;

            // Get a Statement object
            stmt = connection.createStatement();

            // see if there are copies available to check out first
            rs = stmt.executeQuery("SELECT total FROM StoredOn WHERE isbn = '" + borrowedBy.getIsbn() + "';");

            if (!rs.next()) {
                System.out.println("The book with the ISBN " + borrowedBy.getIsbn() + " is not present in the system with any copies.");
            } else {

                int totalCopies = 0;

                while (rs.next()) {
                    totalCopies += rs.getInt("total");
                }

                if (totalCopies > 0) {
                    // Get a Statement object
                    stmt = connection.createStatement();
                    stmt.executeUpdate("INSERT INTO BorrowedBy VALUES ('" + borrowedBy.getIsbn() + "', '" + borrowedBy.getMember_id() + "', '" + borrowedBy.getCheckout_date() + "', '9999-01-01';");

                    System.out.println("The book with the ISBN " + borrowedBy.getIsbn() + " is now checked out");
                } else {
                    System.out.println("The book with the ISBN " + borrowedBy.getIsbn() + " does not have any available copies to check out at the moment.");
                }
            }

        } catch( Exception e ) {
            e.printStackTrace();
        }
    }

}