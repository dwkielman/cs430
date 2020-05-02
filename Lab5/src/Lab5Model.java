import java.sql.*;
import java.util.ArrayList;

public class Lab5Model {

    private static final String NOT_AVAILABLE_VALUE = "N/A";
    private static Connection connection;

    public static void setupLab4(int setupFlag) {

        // connect to the SQL Server
        connection = connectToSQL();

        if (connection != null) {

            if (setupFlag == 0) {
                // 	read in and parse the activity file in XML format
                try {
                    Lab4_xml showXML = new Lab4_xml();
                    showXML.readXML("/s/bach/a/class/cs430dl/Current/more_assignments/LabData/Libdata.xml");
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

                } catch (Exception e) {
                    e.printStackTrace();
                }
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

    public static int searchForMemberByID(int memberID) {

        int searchMessageStatus = -1;

        try {
            Statement stmt;
            ResultSet rs;

            stmt = connection.createStatement();
            rs = stmt.executeQuery("SELECT * FROM Member WHERE member_id = " + memberID);

            if (!rs.next()) {
                searchMessageStatus = Protocol.MEMBER_DOES_NOT_EXISTS;
            } else {
                searchMessageStatus = Protocol.MEMBER_EXISTS;
            }
        } catch( Exception e ) {
            e.printStackTrace();
        }

        return searchMessageStatus;
    }

    public static int generateNewMemberID() {

        int generatedMemberID = -1;

        try {
            Statement stmt;
            ResultSet rs;

            // Get a Statement object
            stmt = connection.createStatement();

            // see if the book is checked out already
            rs = stmt.executeQuery("SELECT MAX(member_id) FROM Member;");

            rs.next();

            generatedMemberID = rs.getInt("MAX(member_id)") + 1;

            return generatedMemberID;

        } catch( Exception e ) {
            e.printStackTrace();
        }

        return generatedMemberID;
    }

    public static void addNewMember(Member member) {

        try {
            Statement stmt;
            ResultSet rs;

            // Get a Statement object
            stmt = connection.createStatement();

            // add the member to the database
            stmt.executeUpdate("INSERT INTO Member VALUES ('" + member.getMemberID() + "', '" + member.getLastName() + "', '" + member.getFirstName() + "', '" + member.getDob() + "', '" + member.getGender() + "');");
        } catch( Exception e ) {
            e.printStackTrace();
        }
    }

    public static String searchForBookByISBN(String isbn) {

        String message = "";

        try {
            Statement stmt;
            ResultSet rs;

            // Get a Statement object
            stmt = connection.createStatement();

            // see if the title is even in the library
            rs = stmt.executeQuery("SELECT title FROM Book WHERE isbn = '" + isbn + "';");

            if (!rs.next()) {
                // book isn't available in any library
                message += "<html>Sorry, this book does not appear to exist in the Library. Please try again.</html>";
            } else {
                // see if there are copies available to check out first
                int totalCopies = 0;

                // get the total number of books available in all of the libraries
                rs = stmt.executeQuery("SELECT total FROM StoredOn WHERE isbn = '" + isbn + "';");

                // book is in library, but no copies are available
                if (!rs.next()) {
                    message += "<html>Sorry, there are currently no copies available of your book at any of our libraries.</html>";
                } else {

                    rs.beforeFirst();
                    while (rs.next()) {
                        totalCopies += rs.getInt("total");
                    }

                    rs = stmt.executeQuery("SELECT COUNT(isbn) FROM BorrowedBy WHERE isbn = '" + isbn + "' AND checkin_date = '9999-01-01';");

                    rs.next();
                    int loanedTotal = rs.getInt("COUNT(isbn)");

                    rs = stmt.executeQuery("SELECT title FROM Book WHERE isbn = '" + isbn + "';");
                    rs.next();
                    String bookTitle = rs.getString("title");

                    message += "<html>Book Title: " + bookTitle + "<br><br>";

                    if (totalCopies > loanedTotal) {
                        // If the libraries have the book and there are copies available, the program should print a message telling the member what library and shelf the book is on (there may be more than one).
                        rs = stmt.executeQuery("SELECT lib_name, s_number FROM StoredOn WHERE isbn = '" + isbn + "';");
                        while (rs.next()) {
                            message += "Library Name: " + rs.getString("lib_name") + ", located on Shelf Number: " + rs.getInt("s_number") + "<br>";
                        }
                        message += "Total Available Copies: " + (totalCopies - loanedTotal) + "</html>";
                    } else {
                        message += "Sorry, all books are currently loaned out.</html>";
                    }
                }
            }

        } catch( Exception e ) {
            e.printStackTrace();
        }

        return message;
    }

    public ArrayList<String> searchForBookByTitle(String title) {
        ArrayList<String> bookTitleISBNs = new ArrayList<String>();

        try {
            Statement stmt;
            ResultSet rs;

            // Get a Statement object
            stmt = connection.createStatement();

            // add the member to the database
            rs = stmt.executeQuery("SELECT title FROM Book WHERE title LIKE '%" + title + "%';");

            while (rs.next()) {
                bookTitleISBNs.add(rs.getString("title"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bookTitleISBNs;

    }

    public static String getBookISBNByTitle(String title) {
        String isbn = "";

        try {
            Statement stmt;
            ResultSet rs;

            // Get a Statement object
            stmt = connection.createStatement();

            // add the member to the database
            rs = stmt.executeQuery("SELECT isbn FROM Book WHERE title = '" + title + "';");

            if (!rs.next()) {

            } else {
                isbn = rs.getString("isbn");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isbn;
    }

    public static ArrayList<String> getBookISBNsByAuthor(Author author) {

        ArrayList<String> authorBookISBNs = new ArrayList<String>();

        try {
            Statement stmt;
            ResultSet rs;

            // Get a Statement object
            stmt = connection.createStatement();

            // add the member to the database
            rs = stmt.executeQuery("SELECT author_ID FROM Author WHERE last_name = '" + author.getLastName() + "' AND first_name = '" + author.getFirstName() + "';");

            if (!rs.next()) {

            } else {
                int authorID = rs.getInt("author_ID");
                author.setAuthorID(authorID);
            }

            //rs = stmt.executeQuery("SELECT isbn FROM WrittenBy WHERE author_ID = " + author.getAuthorID() + ";");
            rs = stmt.executeQuery("SELECT title, isbn FROM Book NATURAL JOIN WrittenBy WHERE author_ID = " + author.getAuthorID() + ";");

            while (rs.next()) {
                authorBookISBNs.add(rs.getString("title"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return authorBookISBNs;

    }


}