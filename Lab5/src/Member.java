public class Member {

    private String lastName;
    private String firstName;
    private String dob;
    private String gender;
    private int memberID;

    public Member() {}

    public Member(String lastName, String firstName, String dob, String gender) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.dob = dob;
        this.gender = gender;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getDob() {
        return dob;
    }

    public String getGender() {
        return gender;
    }

    public void setMemberID(int memberID) {
        this.memberID = memberID;
    }

    public int getMemberID() {
        return this.memberID;
    }
}
