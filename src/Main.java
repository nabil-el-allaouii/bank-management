import Controller.AuthController;
import Controller.ManagerController;
import Modal.Client;
import Modal.Person;
import Repositories.InMemoryClientService;
import Repositories.InMemoryManagerService;
import Services.AuthService;
import Services.ManagerService;
import View.ConsoleView;

import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        InMemoryManagerService managerRepo = new InMemoryManagerService();
        InMemoryClientService clientRepo = new InMemoryClientService();
        AuthService authService = new AuthService(clientRepo, managerRepo);
        AuthController authController = new AuthController(authService);
        ManagerService managerService = new ManagerService(clientRepo);
        ManagerController managerController = new ManagerController(managerService);
        ConsoleView consoleView = new ConsoleView(authController, managerController);

        consoleView.start();
    }
}