import java.util.ArrayList;

public class Lab5Controller {

    private Lab5View lab5View;
    private Lab5Model lab5Model;

    public Lab5Controller() {
        lab5Model = new Lab5Model();
        lab5View = new Lab5View();
        runLab4();
        searchForMemberID();
    }

    private void runLab4() {
        int results = lab5View.runLab4View();

        lab5Model.setupLab4(results);
    }

    private void searchForMemberID() {

        int memberID = lab5View.searchMemberIDView();

        if (memberID > -1) {
            // verify that the member has a valid entry
            int memberStatus = lab5Model.searchForMemberByID(memberID);

            if (memberStatus >= 0) {
                if (memberStatus == Protocol.MEMBER_EXISTS) {
                    searchForBookSelection();
                } else if (memberStatus == Protocol.MEMBER_DOES_NOT_EXISTS) {
                    addNewMember();
                }
            }
        } else if (memberID == -1) {
            addNewMember();
        }
    }

    private void addNewMember() {

        Member member = lab5View.addNewMemberView();

        if (member != null) {
            if (member.getMemberID() != -1) {
                int newMemberID = lab5Model.generateNewMemberID();
                if (newMemberID > -1) {
                    member.setMemberID(newMemberID);
                    lab5Model.addNewMember(member);
                    lab5View.displayNewMemberInformationView(member);
                    searchForBookSelection();
                } else {
                    String message = "There was an error in generating the new Member. Please try again.";
                    Lab5View.errorMessageView(message);
                    addNewMember();
                }
            } else {
                searchForMemberID();
            }
        } else {
            searchForMemberID();
        }

    }

    private void searchForBookSelection() {
        String selection = lab5View.searchForBookView();

        if (selection != "") {
            if (selection.equals("ISBN")) {
                //isbn search view
                String isbn = lab5View.searchForBookByISBNView();
                if (isbn != "") {
                    String message = lab5Model.searchForBookByISBN(isbn);
                    lab5View.displayBookInformation(message);
                    searchForMemberID();
                } else {
                    String message = "Please enter a valid ISBN and try again.";
                    Lab5View.errorMessageView(message);
                    searchForBookSelection();
                }

            } else if (selection.equals("Name")) {
                // name search view
                String name = lab5View.searchForBookByNameView();
                if (name != "") {
                    ArrayList<String> bookTitleISBNs = lab5Model.searchForBookByTitle(name);
                    if (!bookTitleISBNs.isEmpty()) {
                        //System.out.println(bookTitleISBNs);
                        String title = Lab5View.displayAndGetBookFromSelections(bookTitleISBNs);
                        String selectedISBN = Lab5Model.getBookISBNByTitle(title);
                        if (selectedISBN != "") {
                            String message = lab5Model.searchForBookByISBN(selectedISBN);
                            lab5View.displayBookInformation(message);
                            searchForMemberID();
                        }
                    } else {
                        String message = "No Books were found that match your title. Please search again.";
                        Lab5View.errorMessageView(message);
                        searchForBookSelection();
                    }

                }

            } else if (selection.equals("Author")) {
                // author search view
                Author author = lab5View.searchForBookByAuthorView();
                if (author != null) {
                    ArrayList<String> authorTitleISBNs = lab5Model.getBookISBNsByAuthor(author);

                    if (!authorTitleISBNs.isEmpty()) {
                        String title = Lab5View.displayAndGetBookFromSelections(authorTitleISBNs);
                        String selectedISBN = Lab5Model.getBookISBNByTitle(title);
                        if (selectedISBN != "") {
                            String message = lab5Model.searchForBookByISBN(selectedISBN);
                            lab5View.displayBookInformation(message);
                            searchForMemberID();
                        }
                    } else {
                        String message = "No Authors were found that match your search. Please search again.";
                        Lab5View.errorMessageView(message);
                        searchForBookSelection();
                    }
                }

            } else if (selection.equals(Protocol.CANCEL_SELECTION)) {
                searchForMemberID();
            }
        }

        searchForMemberID();

    }

}
