import java.sql.*;
import java.util.ArrayList;

public class Lab4 {

    private static final String NOT_AVAILABLE_VALUE = "N/A";

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
                    //System.out.println(("Check in date value: " + bb.getCheckin_date()));
                    if (!bb.getCheckin_date().equals(NOT_AVAILABLE_VALUE)) {
                        System.out.println(("Checking in book."));
                        checkInBook(connection, bb);
                    } else {
                        //  If the transaction is a checkout, a new record is created
                        System.out.println(("Checking out book."));
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
            // Register the JDBC driver for MySQL.
            Class.forName("com.mysql.jdbc.Driver");

            // Define URL of database server for
            // database named 'user' on the faure.
            String url =
                    "jdbc:mysql://faure/dkielman?serverTimezone=UTC";

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

            int totalCopies = 0;

            while (rs.next()) {
                totalCopies += rs.getInt("total");
            }

            //System.out.println("The book with the ISBN " + borrowedBy.getIsbn() + " has " + totalCopies + " available.");

            if (totalCopies > 0) {
                // Get a Statement object
                stmt = connection.createStatement();
                stmt.executeUpdate("INSERT INTO BorrowedBy VALUES ('" + borrowedBy.getIsbn() + "', '" + borrowedBy.getMember_id() + "', '" + borrowedBy.getCheckout_date() + "', '9999-01-01');");

                System.out.println("The book with the ISBN " + borrowedBy.getIsbn() + " is now checked out");
            } else {
                System.out.println("The book with the ISBN " + borrowedBy.getIsbn() + " does not have any available copies to check out at the moment.");
            }

        } catch( Exception e ) {
            e.printStackTrace();
        }
    }

}