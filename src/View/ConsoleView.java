package View;

import Controller.AuthController;
import Controller.ManagerController;
import Modal.Account;
import Modal.Client;
import Modal.Enums.AccountType;

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
                handleRegisterOrLogin();
            } else if (authController.getLoggedInUser() instanceof Client) {
                handleClientMenu();
            } else {
                handleManagerMenu();
            }
        }
    }

    public void handleRegisterOrLogin() {
        showMenu();
        String choice = scanner.nextLine();
        HandleChoice(choice);
    }

    public void handleClientMenu() {
        showClientMenu();
        String choice = scanner.nextLine();
    }

    public void handleManagerMenu() {
        showManagerMenu();
        String choice = scanner.nextLine();
        HandleManagerChoice(choice);
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
                authController.register(clientName, clientEmail, clientPassword, "client");
                break;
            case "2":
                updateClientExtraMenu();
                String choice2 = scanner.nextLine();
                HandleUpdateClientChoice(choice2);
                break;
            case "3":
                System.out.println("Enter The Client ID: ");
                String clientID = scanner.nextLine();
                managerController.deleteClientById(clientID);
                break;
            case "4":
                System.out.println("1) Checking Account");
                System.out.println("1) Saving Account");
                System.out.println("1) Deposit Account");
                System.out.println("Enter The Account Type: ");
                String typeChoice = scanner.nextLine();
                AccountType type = HandleAccountTypes(typeChoice);
                System.out.println("Enter The Account Amount: ");
                double amount = scanner.nextDouble();
                scanner.nextLine();
                System.out.println("Enter The Client ID: ");
                String clientId = scanner.nextLine();
                managerController.createAccountForClient(type, amount, clientId);
                break;
            case "5":
                HandleUpdatingAccount();
                String choice5 = scanner.nextLine();
                HandleUpdateAccount(choice5);
                break;
            case "6":
                 System.out.println("Enter The Account ID: ");
                 String accountID = scanner.nextLine();
                 managerController.deleteAccountById(accountID);
                break;
            case "9":
                System.out.println("Enter The Client ID: ");
                String getClientID = scanner.nextLine();
                for(Account accou : managerController.getAccountByClientId(getClientID)) {
                    System.out.println("Account ID: " + accou.getAccountId());
                    System.out.println("Account Type: " + accou.getAccountType());
                    System.out.println("Account Balance: " + accou.getBalance());
                    System.out.println("Client ID: " + accou.getClientId());
                }
                break;
            case "15":
                System.out.println("List Of All the Clients: ");
                for (Client c : managerController.getAllClients()) {
                    System.out.println("Client ID: " + c.getClientId());
                    System.out.println("Client Name: " + c.getName());
                    System.out.println("Client Email: " + c.getEmail());
                    System.out.println("Client Password: " + c.getPassword());
                }
                break;
        }
    }

    public void updateClientExtraMenu() {
        System.out.println("1) Update Client Name");
        System.out.println("2) Update Client Email: ");
        System.out.println("3) Update Client Password: ");
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
            case "3":
                System.out.println("Enter The New Client Password: ");
                String clientPassword = scanner.nextLine();
                managerController.updateClientPasswordById(clientId, clientPassword);
                break;
        }
    }

    public AccountType HandleAccountTypes(String choice) {
        switch (choice) {
            case "1":
                return AccountType.COURANT;
            case "2":
                return AccountType.EPARGNE;
            case "3":
                return AccountType.DEPOTATERME;
            default:
                throw new IllegalArgumentException("Invalid choice");
        }
    }

    public void HandleUpdatingAccount() {
        System.out.println("1) Update Balance");
        System.out.println("1) Update Type");
    }

    public void HandleUpdateAccount(String choice) {
        String accountId = scanner.nextLine();
        switch (choice) {
            case "1":
                System.out.println("Enter New Account Balance: ");
                double newBalance = scanner.nextDouble();
                managerController.updateAccountBalanceById(accountId, newBalance);
                break;
            case "2":
                System.out.println("Enter New Account Type: ");
                String typeChoice = scanner.nextLine();
                AccountType type = HandleAccountTypes(typeChoice);
                managerController.updateAccountTypeById(accountId, type);
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
