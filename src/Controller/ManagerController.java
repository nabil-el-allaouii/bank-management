package Controller;


import Modal.Account;
import Modal.Client;
import Modal.Enums.AccountType;
import Services.ManagerService;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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

    public void deleteClientById(String clientId){
        try{
            managerService.removeClientById(clientId);
            System.out.println("Client Deleted Successfully");
        }catch(NoSuchElementException e){
            System.out.println("No Such Client" + e.getMessage());
        }
    }

    public void createAccountForClient(AccountType accountType , double balance , String clientId){
        try{
            managerService.createAccountForClient(accountType,balance,clientId);
            System.out.println("Account Created Successfully To "+ clientId);
        }catch(IllegalArgumentException e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void updateAccountBalanceById(String accountId , double amount){
        try{
            managerService.updateAccountBalanceById(accountId, amount);
            System.out.println("Account Balance Updated Successfully To ");
        }catch(NoSuchElementException e){
            System.out.println("No Such Account" + e.getMessage());
        }
    }

    public void updateAccountTypeById(String accountId , AccountType type){
        try{
            managerService.updateAccountTypeById(accountId, type);
            System.out.println("Account Type Updated Successfully To ");
        }catch(NoSuchElementException e){
            System.out.println("No Such Account" + e.getMessage());
        }
    }
    public void deleteAccountById(String accountId){
        try{
            managerService.deleteAccountById(accountId);
            System.out.println("Account Deleted Successfully ");
        }catch(NoSuchElementException e){
            System.out.println("No Such Account" + e.getMessage());
        }
    }
    public List<Account> getAccountByClientId(String clientId){
        try{
            return managerService.getAccountById(clientId);
        }catch(NoSuchElementException e){
            throw new NoSuchElementException("No Such Client" + e.getMessage());
        }
    }
}
