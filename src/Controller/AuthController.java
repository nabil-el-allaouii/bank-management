package Controller;

import Modal.Client;
import Modal.Person;
import Services.AuthService;

public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    public void Login(String email , String password){
        try {
            Person person = authService.login(email, password);
            if(person instanceof Client){
                System.out.println("Welcome Client"+person.getName());
            }else{
                System.out.println("Welcome Manager"+person.getName());
            }
        }catch (RuntimeException e){
            System.out.println("Login Error "+e.getMessage());
        }
    }
}
