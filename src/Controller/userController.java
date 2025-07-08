package Controller;

import java.util.ArrayList;

public class userController {

    private String email;
    private String password;

    public userController() {

    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
    
    public void setUser(String email, String password){
        this.email = email;
        this.password = password;
    }

}
