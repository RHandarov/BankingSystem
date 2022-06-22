package BankingSystem.Console.UI;

import BankingSystem.Exceptions.UserExceptions.UserAlreadyExistException;
import BankingSystem.Models.Accounts.Account;
import BankingSystem.Models.Accounts.AccountType;
import BankingSystem.Models.Accounts.SavingsComponentAccount;
import BankingSystem.Startup;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

public final class ConsoleEngine {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String[] AUTH_OPERATIONS = new String[] {
            "login",
            "register",
            "exit"
    };

    private ConsoleEngine() throws InstantiationException {
        throw new InstantiationException("Cannot instantiate ConsoleEngine class!");
    }

    private static void showHelloMessage() {
        System.out.println("Welcome to our bank! Thank that you are our client!");
    }

    private static void showAuthMessage() {
        System.out.print("Please type the operation you want (login, register or exit): ");
    }

    private static boolean isAuthOperationSupported(String authOperation) {
        for (String allowedAuthOperation : ConsoleEngine.AUTH_OPERATIONS) {
            if (authOperation.equals(allowedAuthOperation)) {
                return true;
            }
        }

        return false;
    }

    private static String getAuthOperationType() {
        ConsoleEngine.showAuthMessage();

        String authOperation = scanner.next().toLowerCase();

        if (ConsoleEngine.isAuthOperationSupported(authOperation)) {
            return authOperation;
        }

        throw new IllegalArgumentException("The operation " + authOperation + " is invalid! It should be login or register!");
    }

    private static void showLoggedUserPage() {
        System.out.println("Hello, " + Startup.getSession().getLoggedUser().getUsername() + "!");
        System.out.println("List of possible commands:");
        System.out.println("1) Logout");
        System.out.println("2) Check all your accounts");

        while (true) {
            System.out.print("Please enter command number (number in front of each command in the list below): ");
            int commandId = ConsoleEngine.scanner.nextInt();

            if (commandId == 1) {
                System.out.println("Goodbye, " + Startup.getSession().getLoggedUser().getUsername() + "! See you soon!");
                Startup.getSession().logOutUser();
                break;
            } else if (commandId == 2) {
                Map<Integer, Account> accounts = Startup.getSession().getLoggedUser().getAccounts();

                for (Map.Entry<Integer, Account> entry : accounts.entrySet()) {
                    Account account = entry.getValue();

                    if (account.getAccountType() == AccountType.CURRENT) {
                        System.out.println("Current account #" + account.getId() + " has " + account.getAmount() + " lv.");
                    } else {
                        System.out.println("Savings component account #" + account.getId() + " connected to account #" +
                                ((SavingsComponentAccount) account).getAttachedCurrentAccount().getId() +
                                " has " + account.getAmount() + " lv.");
                    }
                }
            } else {
                System.out.println("Invalid command! Please try again!");
            }
        }
    }

    public static void run() {
        ConsoleEngine.showHelloMessage();

        while (true) {
            String authOperationType;
            try {
                authOperationType = ConsoleEngine.getAuthOperationType();
            } catch (IllegalArgumentException illegalArgumentException) {
                System.out.print(illegalArgumentException.getMessage());
                System.out.println(" Please try again!");
                continue;
            }

            if (authOperationType.equals("login")) {
                while (true) {
                    System.out.print("Username: ");
                    String username = ConsoleEngine.scanner.next();

                    System.out.print("Password: ");
                    String password = ConsoleEngine.scanner.next();

                    try {
                        Startup.getSession().logInUser(username, password);
                        ConsoleEngine.showLoggedUserPage();
                        break;
                    } catch (RuntimeException runtimeException) {
                        System.out.println(runtimeException.getMessage());
                    }
                }
            } else if (authOperationType.equals("register")) {
                while (true) {
                    System.out.print("Username: ");
                    String username = ConsoleEngine.scanner.next();

                    System.out.print("Password: ");
                    String password = ConsoleEngine.scanner.next();

                    try {
                        Startup.getSession().getDataStorage().saveUser(username, password);
                    } catch (IOException ioException) {
                        System.out.println("ERROR: " + ioException.getMessage());
                        continue;
                    } catch (UserAlreadyExistException userAlreadyExistException) {
                        System.out.println(userAlreadyExistException.getMessage());
                        continue;
                    }

                    System.out.println("User " + username + " is successfully registered!");
                    break;
                }
            } else {
                System.out.println("Goodbye!");
                break;
            }
        }
    }
}
