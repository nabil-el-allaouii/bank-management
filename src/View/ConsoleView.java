package View;

import Controller.AuthController;
import Controller.ManagerController;
import Modal.Client;

import java.util.Scanner;

public class ConsoleView {
    private final AuthController authController;
    private final ManagerController managerController;
    private final Scanner scanner;
    private boolean running = true;

    public ConsoleView(AuthController authController, ManagerController managerController) {
        this.authController = authController;
        this.managerController = managerController;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (running) {
            if (authController.getLoggedInUser() == null) {
                showMenu();
            }
            String choice = scanner.nextLine();
            HandleChoice(choice);
            if (authController.getLoggedInUser() != null) {
                if (authController.getLoggedInUser() instanceof Client) {
                    showClientMenu();
                } else {
                    showManagerMenu();
                    String choice2 = scanner.nextLine();
                    HandleManagerChoice(choice2);
                }
            }
        }
    }

    public void handleRegisterOrLogin(){
        
    }

    public void showMenu() {
        System.out.println("Bank Management System");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Logout");
        System.out.println("4. Exit");
        System.out.println("Enter your choice:");
    }

    public void showClientMenu() {
        System.out.println("Client Dashboard");
        System.out.println("1. Show Profile Details");
        System.out.println("2. Transaction History");
        System.out.println("3. Filter Transactions(Type,Amount,date)");
        System.out.println("4. Calculate The Solde sum and the deposit deposited or withdrawal");
    }

    public void showManagerMenu() {
        System.out.println("===== MANAGER MENU =====");
        System.out.println("1. Create Client");
        System.out.println("2. Update Client");
        System.out.println("3. Delete Client");
        System.out.println("4. Create Account for Client");
        System.out.println("5. Update Account");
        System.out.println("6. Delete Account");
        System.out.println("7. Add Deposit / Withdrawal");
        System.out.println("8. Add Transfer between Accounts");
        System.out.println("9. View Client Accounts");
        System.out.println("10. View Transactions (by Client or Account)");
        System.out.println("11. Filter / Sort Transactions");
        System.out.println("12. Reports (Total Balance, Totals by Type)");
        System.out.println("13. Detect Suspicious Transactions");
        System.out.println("15. List All Clients");
        System.out.println("14. Logout");
        System.out.println("0. Exit");
        System.out.println("========================");
        System.out.print("Enter your choice: ");
    }

    public void HandleManagerChoice(String choice) {
        switch (choice) {
            case "1":
                System.out.println("Enter The Client Name: ");
                String clientName = scanner.nextLine();
                System.out.println("Enter The Client Email: ");
                String clientEmail = scanner.nextLine();
                System.out.println("Enter The Client Password: ");
                String clientPassword = scanner.nextLine();
                authController.register(clientName, clientPassword, clientEmail, "client");
                break;
            case "2":
                updateClientExtraMenu();
                String choice2 = scanner.nextLine();
                HandleManagerChoice(choice2);
                break;
            case "15":
                System.out.println("List Of All the Clients: ");
                for(Client c : managerController.getAllClients()){
                    System.out.println("Client Name: " + c.getName());
                    System.out.println("Client Email: " + c.getEmail());
                    System.out.println("Client Password: " + c.getPassword());
                }
                break;
        }
    }

    public void updateClientExtraMenu() {
        System.out.println("Update Client Name");
        System.out.println("Update Client Email: ");
        System.out.println("Update Client Password: ");
    }

    public void HandleUpdateClientChoice(String choice) {
        System.out.println("Enter The Client ID: ");
        String clientId = scanner.nextLine();
        switch (choice) {
            case "1":
                System.out.println("Enter The New Client Name: ");
                String clientName = scanner.nextLine();
                managerController.updateClientNameById(clientId, clientName);
                break;
            case "2":
                System.out.println("Enter The New Client Email: ");
                String clientEmail = scanner.nextLine();
                managerController.updateClientEmailById(clientId, clientEmail);
                break;
        }
    }


    public void HandleChoice(String choice) {
        switch (choice) {
            case "1":
                System.out.println("Enter The Role (client , manager):");
                String role = scanner.nextLine();
                System.out.println("Enter The Name:");
                String name = scanner.nextLine();
                System.out.println("Enter The Email:");
                String email = scanner.nextLine();
                System.out.println("Enter The Password:");
                String password = scanner.nextLine();
                authController.register(name, email, password, role);
                break;

            case "2":
                System.out.println("Enter The Email:");
                String logginEmail = scanner.nextLine();
                System.out.println("Enter The Password:");
                String logginPassword = scanner.nextLine();
                authController.login(logginEmail, logginPassword);
                break;
            case "3":
                authController.logout();
                System.out.println("You have been logged out");
                break;
            case "4":
                running = false;
                break;
        }
    }

}
