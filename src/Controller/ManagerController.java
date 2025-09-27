package Controller;


import Modal.Client;
import Services.ManagerService;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class ManagerController {
    private final ManagerService managerService;
    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }
    public void updateClientNameById(String clientId , String name){
        try{
            managerService.updateClientNameById(clientId,name);
            System.out.println("Client Name Updated Successfully To "+ name);
        } catch (NoSuchElementException e) {
            System.out.println("No Such Client" + e.getMessage());
        }
    }
    public void updateClientEmailById(String clientId , String email){
        try{
            managerService.updateClientEmailById(clientId,email);
            System.out.println("Client Email Updated Successfully To "+ email);
        }catch (NoSuchElementException e){
            System.out.println("No Such Client" + e.getMessage());
        }
    }

    public void updateClientPasswordById(String clientId , String password){
        try{
            managerService.updateClientPasswordById(clientId,password);
            System.out.println("Client Password Updated Successfully To "+ password);
        }catch(NoSuchElementException e){
            System.out.println("No Such Client" + e.getMessage());
        }
    }

    public List<Client> getAllClients(){
        return managerService.getAllClients();
    }
}
