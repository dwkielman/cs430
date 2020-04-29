import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Lab5View {

    private static final String[] SEARCH_OPTIONS = { "ISBN", "Name", "Author" };

    public static int searchMemberIDView() {

        int memberID = -1;

        JTextField memberIDTextField = new JTextField(5);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0,2));
        panel.add(new JLabel("Enter your Member ID:  "));
        panel.add(memberIDTextField);
        int response = JOptionPane.showConfirmDialog(null, panel, "Lab 5 - Library Database", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (response == JOptionPane.CANCEL_OPTION) {
            // go to the add member page
        } else if (response == JOptionPane.OK_OPTION) {
            String userentry = memberIDTextField.getText();
            try {
                memberID = Integer.parseInt(userentry);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (response == JOptionPane.CLOSED_OPTION) {
            System.exit(0);
        }

        return memberID;
    }

    public static Member addNewMemberView() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0,2));

        JTextField firstNameTextField = new JTextField(14);
        panel.add(new JLabel("Enter your First Name:  "));
        panel.add(firstNameTextField);

        JTextField lastNameTextField = new JTextField(16);
        panel.add(new JLabel("Enter your Last Name:  "));
        panel.add(lastNameTextField);

        JTextField dobTextField = new JTextField(30);
        panel.add(new JLabel("Enter your Date of Birth (yyyy-MM-dd format):  "));
        panel.add(dobTextField);

        JTextField genderTextField = new JTextField(1);
        panel.add(new JLabel("Enter your Gender (M for Male, F for Female):  "));
        panel.add(genderTextField);

        int response = JOptionPane.showConfirmDialog(null, panel, "Lab 5 - Library Database: Adding New Member", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        Member member = new Member();

        if (response == JOptionPane.CANCEL_OPTION) {
            // go back to the main page
        } else if (response == JOptionPane.OK_OPTION) {
            String firstName = firstNameTextField.getText();
            String lastName = lastNameTextField.getText();
            String dob = dobTextField.getText();
            String gender = genderTextField.getText();

            if (firstName.isEmpty() || lastName.isEmpty() || dob.isEmpty() || gender.isEmpty()) {
                // error message here
                JFrame parent = new JFrame();
                JOptionPane.showMessageDialog(parent, "Can not have empty values. Please Enter Member Information again.");
                addNewMemberView();
            } else {
                member = new Member(lastName, firstName, dob, gender);
            }
        } else if (response == JOptionPane.CLOSED_OPTION) {
            System.exit(0);
        }

        return member;
    }

    public static String searchForBookView() {

        String selection = "";

        JList<String> selectionList = new JList<>(SEARCH_OPTIONS);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0,2));
        panel.add(new JScrollPane(selectionList));
        selectionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        int response = JOptionPane.showConfirmDialog(null, panel, "Lab 5 - Library Database: Search Selection", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (response == JOptionPane.CANCEL_OPTION) {
            // go back to the main page
            cancelSelected();
        } else if (response == JOptionPane.OK_OPTION) {
            selection = selectionList.getSelectedValue();
        } else if (response == JOptionPane.CLOSED_OPTION) {
            System.exit(0);
        }

        return selection;
    }

    public static String searchForBookByISBNView() {

        String isbn = "";

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0,2));

        JTextField isbnSearchTextField = new JTextField(14);
        panel.add(new JLabel("Enter the book ISBN:  "));
        panel.add(isbnSearchTextField);

        int response = JOptionPane.showConfirmDialog(null, panel, "Lab 5 - Library Database: Search by ISBN", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (response == JOptionPane.CANCEL_OPTION) {
            // go back to the main page
            cancelSelected();
        } else if (response == JOptionPane.OK_OPTION) {
            isbn = isbnSearchTextField.getText();
        } else if (response == JOptionPane.CLOSED_OPTION) {
            System.exit(0);
        }

        return isbn;
    }

    public static String searchForBookByNameView() {

        String name = "";

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0,2));

        JTextField bookSearchTextField = new JTextField(14);
        panel.add(new JLabel("Enter the book Name:  "));
        panel.add(bookSearchTextField);

        int response = JOptionPane.showConfirmDialog(null, panel, "Lab 5 - Library Database: Search by Name", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (response == JOptionPane.CANCEL_OPTION) {
            // go back to the main page
            cancelSelected();
        } else if (response == JOptionPane.OK_OPTION) {
            name = bookSearchTextField.getText();

            if (name.isEmpty()) {
                // error message here
                JFrame parent = new JFrame();
                JOptionPane.showMessageDialog(parent, "Can not have empty values. Please Enter Book Information again.");
                searchForBookByNameView();
            }
        } else if (response == JOptionPane.CLOSED_OPTION) {
            System.exit(0);
        }

        return name;
    }

    public static Author searchForBookByAuthorView() {

        Author author = new Author();

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0,2));

        JTextField authorSearchFirstNameTextField = new JTextField(14);
        panel.add(new JLabel("Author First Name:  "));
        panel.add(authorSearchFirstNameTextField);

        JTextField authorSearchLastNameTextField = new JTextField(14);
        panel.add(new JLabel("Author Last Name:  "));
        panel.add(authorSearchLastNameTextField);

        int response = JOptionPane.showConfirmDialog(null, panel, "Lab 5 - Library Database: Search by Author", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (response == JOptionPane.CANCEL_OPTION) {
            // go back to the main page
            cancelSelected();
        } else if (response == JOptionPane.OK_OPTION) {
            String firstName = authorSearchFirstNameTextField.getText();
            String lastName = authorSearchLastNameTextField.getText();

            if (firstName.isEmpty() || lastName.isEmpty()) {
                // error message here
                JFrame parent = new JFrame();
                JOptionPane.showMessageDialog(parent, "Can not have empty values. Please Enter Author Information again.");
                searchForBookByAuthorView();
            } else {
                author = new Author(firstName, lastName);
            }
        } else if (response == JOptionPane.CLOSED_OPTION) {
            System.exit(0);
        }

        return author;
    }

    public static String displayAndGetBookFromSelections(ArrayList<String> bookTitles) {

        String selection = "";

        String[] array = bookTitles.toArray(new String[bookTitles.size()]);

        JList<String> selectionList = new JList<>(array);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0,2));
        panel.add(new JScrollPane(selectionList));
        selectionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        int response = JOptionPane.showConfirmDialog(null, panel, "Lab 5 - Library Database: Book Results", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (response == JOptionPane.CANCEL_OPTION) {
            // go back to the main page
            cancelSelected();
        } else if (response == JOptionPane.OK_OPTION) {
            selection = selectionList.getSelectedValue();
        } else if (response == JOptionPane.CLOSED_OPTION) {
            System.exit(0);
        }

        return selection;

    }


    public static int displayBookInformation(String message) {
        JPanel panel = new JPanel();
        panel.add(new JLabel(message));

        int response = JOptionPane.showConfirmDialog(null, panel, "Lab 5 - Library Database: Book Information Results", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        return response;
    }

    private static String cancelSelected() {
        return Protocol.CANCEL_SELECTION;
    }

}