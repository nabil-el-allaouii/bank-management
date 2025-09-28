package View;

import Controller.AuthController;
import Controller.ClientController;
import Controller.ManagerController;
import Modal.Account;
import Modal.Client;
import Modal.Enums.AccountType;
import Modal.Enums.TransactionType;
import Modal.Person;
import Modal.Transaction;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ConsoleView {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final AuthController authController;
    private final ManagerController managerController;
    private final ClientController clientController;
    private final Scanner scanner;
    private boolean running = true;

    public ConsoleView(AuthController authController, ManagerController managerController,
            ClientController clientController) {
        this.authController = authController;
        this.managerController = managerController;
        this.clientController = clientController;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (running) {
            Person loggedIn = authController.getLoggedInUser();
            if (loggedIn == null) {
                handleRegisterOrLogin();
            } else if (loggedIn instanceof Client) {
                handleClientMenu((Client) loggedIn);
            } else {
                handleManagerMenu();
            }
        }
    }

    private void handleRegisterOrLogin() {
        boolean awaitingAuthAction = true;
        while (awaitingAuthAction && authController.getLoggedInUser() == null && running) {
            showAuthMenu();
            String choice = scanner.nextLine().trim();
            awaitingAuthAction = processAuthChoice(choice);
        }
    }

    private void handleClientMenu(Client client) {
        boolean inClientMenu = true;
        while (inClientMenu && authController.getLoggedInUser() instanceof Client) {
            showClientMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> displayClientProfile(client);
                case "2" -> displayTransactions(clientController.sortTransactionsByDate(client.getClientId(), false));
                case "3" -> handleClientFilterMenu(client);
                case "4" -> displayClientTotals(client);
                case "5" -> {
                    authController.logout();
                    inClientMenu = false;
                    System.out.println("Déconnexion réussie.");
                }
                case "0" -> {
                    running = false;
                    inClientMenu = false;
                }
                default -> System.out.println("Choix invalide.");
            }
        }
    }

    private void handleManagerMenu() {
        boolean inManagerMenu = true;
        while (inManagerMenu && authController.getLoggedInUser() != null
                && !(authController.getLoggedInUser() instanceof Client)) {
            showManagerMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> handleCreateClient();
                case "2" -> handleUpdateClient();
                case "3" -> handleDeleteClient();
                case "4" -> handleCreateAccount();
                case "5" -> handleUpdateAccount();
                case "6" -> handleDeleteAccount();
                case "7" -> handleDepositOrWithdrawal();
                case "8" -> handleTransfer();
                case "9" -> handleViewClientAccounts();
                case "10" -> handleViewTransactions();
                case "11" -> handleUpdateTransaction();
                case "12" -> handleDeleteTransaction();
                case "13" -> handleManagerFilterMenu();
                case "14" -> handleManagerReports();
                case "15" -> handleSuspiciousTransactions();
                case "16" -> listAllClients();
                case "17" -> {
                    authController.logout();
                    inManagerMenu = false;
                    System.out.println("Déconnexion réussie.");
                }
                case "0" -> {
                    running = false;
                    inManagerMenu = false;
                }
                default -> System.out.println("Choix invalide.");
            }
        }
    }

    private void showAuthMenu() {
        System.out.println("===== BANK MANAGEMENT =====");
        System.out.println("1. Inscription");
        System.out.println("2. Connexion");
        System.out.println("0. Quitter");
        System.out.print("Votre choix : ");
    }

    private boolean processAuthChoice(String choice) {
        switch (choice) {
            case "1" -> handleRegistration();
            case "2" -> handleLogin();
            case "0" -> {
                running = false;
                return false;
            }
            default -> System.out.println("Choix invalide.");
        }
        return true;
    }

    private void showClientMenu() {
        System.out.println("===== ESPACE CLIENT =====");
        System.out.println("1. Voir mes informations et comptes");
        System.out.println("2. Historique des transactions");
        System.out.println("3. Filtrer / trier mes transactions");
        System.out.println("4. Totaux (solde, dépôts, retraits)");
        System.out.println("5. Déconnexion");
        System.out.println("0. Quitter l'application");
        System.out.print("Votre choix : ");
    }

    private void showManagerMenu() {
        System.out.println("===== ESPACE GESTIONNAIRE =====");
        System.out.println("1. Créer un client");
        System.out.println("2. Mettre à jour un client");
        System.out.println("3. Supprimer un client");
        System.out.println("4. Créer un compte");
        System.out.println("5. Mettre à jour un compte");
        System.out.println("6. Supprimer un compte");
        System.out.println("7. Ajouter dépôt / retrait");
        System.out.println("8. Ajouter virement");
        System.out.println("9. Voir comptes d'un client");
        System.out.println("10. Voir transactions");
        System.out.println("11. Modifier une transaction");
        System.out.println("12. Supprimer une transaction");
        System.out.println("13. Filtrer / trier transactions");
        System.out.println("14. Rapports par client");
        System.out.println("15. Transactions suspectes");
        System.out.println("16. Lister tous les clients");
        System.out.println("17. Déconnexion");
        System.out.println("0. Quitter l'application");
        System.out.print("Votre choix : ");
    }

    private void handleRegistration() {
        System.out.print("Rôle (client/manager) : ");
        String role = scanner.nextLine().trim();
        System.out.print("Nom : ");
        String name = scanner.nextLine().trim();
        System.out.print("Email : ");
        String email = scanner.nextLine().trim();
        System.out.print("Mot de passe : ");
        String password = scanner.nextLine().trim();
        authController.register(name, email, password, role);
    }

    private void handleLogin() {
        System.out.print("Email : ");
        String email = scanner.nextLine().trim();
        System.out.print("Mot de passe : ");
        String password = scanner.nextLine().trim();
        authController.login(email, password);
    }

    private void displayClientProfile(Client client) {
        System.out.println("===== PROFIL CLIENT =====");
        System.out.println("ID : " + client.getClientId());
        System.out.println("Nom : " + client.getName());
        System.out.println("Email : " + client.getEmail());
        System.out.println("--- Comptes ---");
        displayAccounts(clientController.getClientAccounts(client.getClientId()));
    }

    private void displayClientTotals(Client client) {
        double totalBalance = clientController.getTotalBalance(client.getClientId());
        Map<TransactionType, Double> totalsByType = clientController.getTotalsByType(client.getClientId());
        System.out.println("Solde total : " + totalBalance);
        System.out.println("Montant total des dépôts : " + totalsByType.getOrDefault(TransactionType.DEPOT, 0.0));
        System.out.println("Montant total des retraits : " + totalsByType.getOrDefault(TransactionType.RETRAIT, 0.0));
        System.out.println("Montant total des virements : " + totalsByType.getOrDefault(TransactionType.VIREMENT, 0.0));
    }

    private void handleClientFilterMenu(Client client) {
        TransactionType type = promptTransactionTypeOptional();
        Double minAmount = promptDoubleOrNull("Montant minimum (laisser vide pour ignorer) : ");
        Double maxAmount = promptDoubleOrNull("Montant maximum (laisser vide pour ignorer) : ");
        LocalDate date = promptDateOrNull("Date (yyyy-MM-dd, laisser vide pour ignorer) : ");

        List<Transaction> result = clientController.filterTransactions(
                client.getClientId(),
                type,
                minAmount,
                maxAmount,
                date);

        applySortingChoice(result);
        displayTransactions(result);
    }

    private void handleCreateClient() {
        System.out.print("Nom du client : ");
        String name = scanner.nextLine().trim();
        System.out.print("Email du client : ");
        String email = scanner.nextLine().trim();
        System.out.print("Mot de passe : ");
        String password = scanner.nextLine().trim();
        authController.register(name, email, password, "client");
    }

    private void handleUpdateClient() {
        System.out.println("1. Nom\n2. Email\n3. Mot de passe");
        System.out.print("Champ à mettre à jour : ");
        String fieldChoice = scanner.nextLine().trim();
        System.out.print("ID du client : ");
        String clientId = scanner.nextLine().trim();

        switch (fieldChoice) {
            case "1" -> {
                System.out.print("Nouveau nom : ");
                String name = scanner.nextLine().trim();
                managerController.updateClientNameById(clientId, name);
            }
            case "2" -> {
                System.out.print("Nouvel email : ");
                String email = scanner.nextLine().trim();
                managerController.updateClientEmailById(clientId, email);
            }
            case "3" -> {
                System.out.print("Nouveau mot de passe : ");
                String password = scanner.nextLine().trim();
                managerController.updateClientPasswordById(clientId, password);
            }
            default -> System.out.println("Choix invalide");
        }
    }

    private void handleDeleteClient() {
        System.out.print("ID du client à supprimer : ");
        String clientId = scanner.nextLine().trim();
        managerController.deleteClientById(clientId);
    }

    private void handleCreateAccount() {
        AccountType accountType = promptAccountType();
        double initialBalance = promptPositiveDouble("Solde initial : ");
        System.out.print("ID du client : ");
        String clientId = scanner.nextLine().trim();
        managerController.createAccountForClient(accountType, initialBalance, clientId);
    }

    private void handleUpdateAccount() {
        System.out.println("1. Mettre à jour le solde\n2. Mettre à jour le type");
        String choice = scanner.nextLine().trim();
        System.out.print("ID du compte : ");
        String accountId = scanner.nextLine().trim();
        switch (choice) {
            case "1" -> {
                double newBalance = promptPositiveDouble("Nouveau solde : ");
                managerController.updateAccountBalanceById(accountId, newBalance);
            }
            case "2" -> {
                AccountType newType = promptAccountType();
                managerController.updateAccountTypeById(accountId, newType);
            }
            default -> System.out.println("Choix invalide");
        }
    }

    private void handleDeleteAccount() {
        System.out.print("ID du compte à supprimer : ");
        String accountId = scanner.nextLine().trim();
        managerController.deleteAccountById(accountId);
    }

    private void handleDepositOrWithdrawal() {
        System.out.println("1. Dépôt\n2. Retrait");
        String choice = scanner.nextLine().trim();
        System.out.print("ID du compte : ");
        String accountId = scanner.nextLine().trim();
        double amount = promptPositiveDouble("Montant : ");
        System.out.print("Motif : ");
        String motif = scanner.nextLine().trim();

        if ("1".equals(choice)) {
            managerController.addDeposit(accountId, amount, motif);
        } else if ("2".equals(choice)) {
            managerController.addWithdrawal(accountId, amount, motif);
        } else {
            System.out.println("Choix invalide");
        }
    }

    private void handleTransfer() {
        System.out.print("ID du compte source : ");
        String sourceId = scanner.nextLine().trim();
        System.out.print("ID du compte destination : ");
        String destinationId = scanner.nextLine().trim();
        double amount = promptPositiveDouble("Montant : ");
        System.out.print("Motif : ");
        String motif = scanner.nextLine().trim();
        managerController.addTransfer(sourceId, destinationId, amount, motif);
    }

    private void handleViewClientAccounts() {
        System.out.print("ID du client : ");
        String clientId = scanner.nextLine().trim();
        displayAccounts(managerController.getAccountByClientId(clientId));
    }

    private void handleViewTransactions() {
        System.out.println("1. Par client\n2. Par compte");
        String choice = scanner.nextLine().trim();
        if ("1".equals(choice)) {
            System.out.print("ID du client : ");
            String clientId = scanner.nextLine().trim();
            displayTransactions(managerController.getTransactionsForClient(clientId));
        } else if ("2".equals(choice)) {
            System.out.print("ID du compte : ");
            String accountId = scanner.nextLine().trim();
            displayTransactions(managerController.getTransactionsForAccount(accountId));
        } else {
            System.out.println("Choix invalide");
        }
    }

    private void handleUpdateTransaction() {
        System.out.print("ID de la transaction : ");
        String transactionId = scanner.nextLine().trim();
        System.out.println("1. Modifier le montant\n2. Modifier le motif");
        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1" -> {
                double newAmount = promptPositiveDouble("Nouveau montant : ");
                managerController.updateTransactionAmount(transactionId, newAmount);
            }
            case "2" -> {
                System.out.print("Nouveau motif : ");
                String motif = scanner.nextLine().trim();
                managerController.updateTransactionMotif(transactionId, motif);
            }
            default -> System.out.println("Choix invalide");
        }
    }

    private void handleDeleteTransaction() {
        System.out.print("ID de la transaction à supprimer : ");
        String transactionId = scanner.nextLine().trim();
        managerController.deleteTransaction(transactionId);
    }

    private void handleManagerFilterMenu() {
        System.out.print("ID du client : ");
        String clientId = scanner.nextLine().trim();
        TransactionType type = promptTransactionTypeOptional();
        Double minAmount = promptDoubleOrNull("Montant minimum (laisser vide pour ignorer) : ");
        Double maxAmount = promptDoubleOrNull("Montant maximum (laisser vide pour ignorer) : ");
        LocalDate date = promptDateOrNull("Date (yyyy-MM-dd, laisser vide pour ignorer) : ");

        List<Transaction> filtered = managerController.filterTransactions(clientId, type, minAmount, maxAmount, date);
        applySortingChoice(filtered);
        displayTransactions(filtered);
    }

    private void handleManagerReports() {
        System.out.print("ID du client : ");
        String clientId = scanner.nextLine().trim();
        double totalBalance = managerController.getTotalBalanceForClient(clientId);
        Map<TransactionType, Double> totalsByType = managerController.getTotalsByTypeForClient(clientId);
        System.out.println("Solde total du client : " + totalBalance);
        System.out.println("Total des dépôts : " + totalsByType.getOrDefault(TransactionType.DEPOT, 0.0));
        System.out.println("Total des retraits : " + totalsByType.getOrDefault(TransactionType.RETRAIT, 0.0));
        System.out.println("Total des virements : " + totalsByType.getOrDefault(TransactionType.VIREMENT, 0.0));
    }

    private void handleSuspiciousTransactions() {
        System.out.print("ID du client : ");
        String clientId = scanner.nextLine().trim();
        List<Transaction> suspicious = managerController.detectSuspiciousTransactions(clientId);
        if (suspicious.isEmpty()) {
            System.out.println("Aucune transaction suspecte détectée.");
        } else {
            System.out.println("Transactions suspectes :");
            displayTransactions(suspicious);
        }
    }

    private void listAllClients() {
        List<Client> clients = managerController.getAllClients();
        if (clients.isEmpty()) {
            System.out.println("Aucun client enregistré.");
            return;
        }
        System.out.println("===== CLIENTS =====");
        clients.forEach(client -> {
            System.out.println("ID : " + client.getClientId());
            System.out.println("Nom : " + client.getName());
            System.out.println("Email : " + client.getEmail());
            System.out.println("---------------------------");
        });
    }

    private void displayAccounts(List<Account> accounts) {
        if (accounts.isEmpty()) {
            System.out.println("Aucun compte disponible.");
            return;
        }
        accounts.forEach(account -> {
            System.out.println("ID compte : " + account.getAccountId());
            System.out.println("Type : " + account.getAccountType());
            System.out.println("Solde : " + account.getBalance());
            System.out.println("Client ID : " + account.getClientId());
            System.out.println("---------------------------");
        });
    }

    private void displayTransactions(List<Transaction> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            System.out.println("Aucune transaction trouvée.");
            return;
        }
        transactions.forEach(transaction -> {
            System.out.println("ID transaction : " + transaction.getTransactionId());
            System.out.println("Compte source : " + transaction.getAccountId());
            System.out.println("Type : " + transaction.getTransactionType());
            System.out.println("Montant : " + transaction.getAmount());
            System.out.println("Motif : " + transaction.getMotif());
            if (transaction.getAccountDestinationId() != null) {
                System.out.println("Compte destination : " + transaction.getAccountDestinationId());
            }
            System.out.println("Date : " + transaction.getTransactionDate().format(DATE_FORMAT));
            System.out.println("---------------------------");
        });
    }

    private AccountType promptAccountType() {
        System.out.println("Type de compte : 1.COURANT  2.EPARGNE  3.DEPOT A TERME");
        String choice = scanner.nextLine().trim();
        return switch (choice) {
            case "1" -> AccountType.COURANT;
            case "2" -> AccountType.EPARGNE;
            case "3" -> AccountType.DEPOTATERME;
            default -> throw new IllegalArgumentException("Type de compte invalide");
        };
    }

    private TransactionType promptTransactionTypeOptional() {
        System.out.println("Type de transaction (1.Dépôt 2.Retrait 3.Virement 0.Ne pas filtrer) : ");
        String choice = scanner.nextLine().trim();
        return switch (choice) {
            case "1" -> TransactionType.DEPOT;
            case "2" -> TransactionType.RETRAIT;
            case "3" -> TransactionType.VIREMENT;
            default -> null;
        };
    }

    private double promptPositiveDouble(String message) {
        Double value = null;
        while (value == null) {
            System.out.print(message);
            String input = scanner.nextLine().trim();
            try {
                double parsed = Double.parseDouble(input);
                if (parsed <= 0) {
                    System.out.println("Le montant doit être positif.");
                } else {
                    value = parsed;
                }
            } catch (NumberFormatException e) {
                System.out.println("Valeur numérique invalide.");
            }
        }
        return value;
    }

    private Double promptDoubleOrNull(String message) {
        System.out.print(message);
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            return null;
        }
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            System.out.println("Valeur numérique invalide. Filtre ignoré.");
            return null;
        }
    }

    private LocalDate promptDateOrNull(String message) {
        System.out.print(message);
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(input, DATE_FORMAT);
        } catch (DateTimeParseException e) {
            System.out.println("Format de date invalide. Filtre ignoré.");
            return null;
        }
    }

    private void applySortingChoice(List<Transaction> transactions) {
        if (transactions == null || transactions.size() <= 1) {
            return;
        }
        System.out.println("Tri : 1.Montant asc 2.Montant desc 3.Date asc 4.Date desc 0.Aucun");
        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1" -> transactions.sort(Comparator.comparingDouble(Transaction::getAmount));
            case "2" -> transactions.sort(Comparator.comparingDouble(Transaction::getAmount).reversed());
            case "3" -> transactions.sort(Comparator.comparing(Transaction::getTransactionDate));
            case "4" -> transactions.sort(Comparator.comparing(Transaction::getTransactionDate).reversed());
            default -> {
            }
        }
    }
}
