package application.locationotes.DataObjects.Personal;

/**
 * This class represents the input details the user typing at the log in
 * @author Tzion Beniaminov
 */
import java.io.Serializable;

public class LoginData implements Serializable {
    private String email;
    private String password;

    /*c'tor*/
    public LoginData(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /*getters*/
    public String getEmail() {return email; }
    public String getPassword() {return password;}
}

