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

    /*generated automatically by Google Firebase*/
    private String GOOGLE_ID;

    /*c'tor*/
    public UserDetails(String email, String firstName, String lastName, String password, String GOOGLE_ID) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.GOOGLE_ID = null;
    }

    /*getters*/
    public String getEmail() {return email;}
    public String getFirstName() {return firstName;}
    public String getLastName() { return lastName;}
    public String getPassword() {return password;}
    public String getGOOGLE_ID() {return GOOGLE_ID;}
}

