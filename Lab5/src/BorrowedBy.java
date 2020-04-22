public class BorrowedBy {

    private String isbn;
    private int member_id;
    private String checkout_date;
    private String checkin_date;

    public BorrowedBy(String isbn, int member_id, String checkout_date, String checkin_date) {
        this.isbn = isbn;
        this.member_id = member_id;
        this.checkout_date = checkout_date;
        this.checkin_date = checkin_date;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public void setCheckout_date(String checkout_date) {
        this.checkout_date = checkout_date;
    }

    public void setCheckin_date(String checkin_date) {
        this.checkin_date = checkin_date;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getMember_id() {
        return member_id;
    }

    public String getCheckout_date() {
        return checkout_date;
    }

    public String getCheckin_date() {
        return checkin_date;
    }

    @Override
    public String toString() {
        return "BorrowedBy{" +
                "isbn='" + isbn + '\'' +
                ", member_id=" + member_id +
                ", checkout_date='" + checkout_date + '\'' +
                ", checkin_date='" + checkin_date + '\'' +
                '}';
    }
}
