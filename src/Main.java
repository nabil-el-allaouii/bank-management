import Controller.AuthController;
import Controller.ClientController;
import Controller.ManagerController;
import Repositories.InMemoryAccountService;
import Repositories.InMemoryClientService;
import Repositories.InMemoryManagerService;
import Repositories.InMemoryTransactionService;
import Services.AuthService;
import Services.ClientService;
import Services.ManagerService;
import Services.TransactionService;
import View.ConsoleView;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        InMemoryManagerService managerRepo = new InMemoryManagerService();
        InMemoryClientService clientRepo = new InMemoryClientService();
        InMemoryAccountService accountRepo = new InMemoryAccountService();
        InMemoryTransactionService transactionRepo = new InMemoryTransactionService();
        AuthService authService = new AuthService(clientRepo, managerRepo);
        AuthController authController = new AuthController(authService);
        TransactionService transactionService = new TransactionService(accountRepo, transactionRepo);
        ManagerService managerService = new ManagerService(clientRepo, accountRepo);
        ManagerController managerController = new ManagerController(managerService, transactionService);
        ClientService clientService = new ClientService(clientRepo, accountRepo, transactionService);
        ClientController clientController = new ClientController(clientService);
        ConsoleView consoleView = new ConsoleView(authController, managerController, clientController);

        consoleView.start();
    }
}