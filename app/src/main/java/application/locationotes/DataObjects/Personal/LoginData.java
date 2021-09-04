package application.locationotes.DataObjects.Personal;

/**
 * This class represents that input details the user typing at the log in
 * @author Tzion Beniaminov
 */
import androidx.annotation.Nullable;
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

//    /*will help to check the correctness of the user*/
//    @Override
//    public boolean equals(@Nullable Object obj){
//        if(obj instanceof LoginData){
//            LoginData loginData = (LoginData) obj;
//            if(this.email.equals(loginData.email) && this.password.equals(loginData.password)){return true;}
//            return false;
//        }
//        return false;
//    }
}

