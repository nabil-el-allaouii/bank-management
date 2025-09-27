package Controller;

import Modal.Client;
import Modal.Person;
import Services.AuthService;

public class AuthController {
    private final AuthService authService;
    private Person loggedInUser;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    public void register(String name, String email , String password , String role){
        if(role.equalsIgnoreCase("CLIENT")){
            try{
                authService.RegisterClient(name, email , password);
                System.out.println(name + " register successfully");
            }catch(Exception e){
                System.out.println(" register failed"+e.getMessage());
            }
        }else if(role.equalsIgnoreCase("MANAGER")){
            try{
                authService.RegisterManager(name, email , password);
            }catch (Exception e){
                System.out.println(" register failed"+e.getMessage());
            }
        }else {
            System.out.println("Invalid role , Registration failed");
        }
    }

    public void login(String email , String password){
        try {
            Person person = authService.login(email, password);
            loggedInUser = person;
            if(person instanceof Client){
                System.out.println("Welcome Client"+person.getName());
            }else{
                System.out.println("Welcome Manager"+person.getName());
            }
        }catch (RuntimeException e){
            System.out.println("Login Error "+e.getMessage());
        }
    }
    public void logout(){
        loggedInUser = null;
    }

    public Person getLoggedInUser(){
        return loggedInUser;
    }

}
