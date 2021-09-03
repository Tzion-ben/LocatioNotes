package application.locationotes.DataObjects.Personal;

/**
 * This class represents user details at the SignUp
 * @author Tzion Beniaminov
 */

public class UserDetails {
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private int numberOfNotes;

    private int SET_ID;

    /*generated automatically by Google Firebase*/
    private String GOOGLE_ID;

    /*c'tor*/
    public UserDetails(String email, String firstName, String lastName, String password) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.GOOGLE_ID = null;
        numberOfNotes = 0;

        SET_ID = 0;
    }

    /*getters*/
    public String getEmail() {return email;}
    public String getFirstName() {return firstName;}
    public String getLastName() { return lastName;}
    public String getPassword() {return password;}
    public String getGOOGLE_ID() {return GOOGLE_ID;}
    public int getNumberOfNotes() {return numberOfNotes;}

    public void setGOOGLE_ID(String GOOGLE_ID) {
        /**can set it ONLY ONCE, at creation*/
        if(this.SET_ID == 0) {
            this.GOOGLE_ID = GOOGLE_ID;
            this.SET_ID = 1;
        }
    }
}

