package BankingSystem.Console.UI;

import BankingSystem.Exceptions.UserExceptions.UserAlreadyExistException;
import BankingSystem.Models.Accounts.Account;
import BankingSystem.Models.Accounts.AccountType;
import BankingSystem.Models.Accounts.SavingsComponentAccount;
import BankingSystem.Models.User;
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

    private static void showHelpMessage() {
        System.out.println("List of possible commands:");
        System.out.println("0) Show list of possible commands");
        System.out.println("1) Logout");
        System.out.println("2) Check all your accounts");
        System.out.println("3) Create new account");
        System.out.println("4) Deposit to [account id] [amount of money]");
        System.out.println("5) Withdraw from [account id] [amount of money]");
    }

    private static void showLoggedUserPage() {
        System.out.println("Hello, " + Startup.getSession().getLoggedUser().getUsername() + "!");
        ConsoleEngine.showHelpMessage();

        while (true) {
            System.out.print("Please enter command number (number in front of each command in the list below): ");
            int commandId = ConsoleEngine.scanner.nextInt();

            if (commandId == 0) {
                ConsoleEngine.showHelpMessage();
            } else if (commandId == 1) {
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
            } else if (commandId == 3) {
                try {
                    User currentUser = Startup.getSession().getLoggedUser();
                    Startup.getSession().getDataStorage().saveAccount(currentUser);

                    System.out.println("The account is successfully created!");
                } catch (IOException ioException) {
                    System.err.println("ERROR: Failed to create the account: " + ioException.getMessage());
                }
            } else if (commandId == 4) {
                int accountID = ConsoleEngine.scanner.nextInt();
                double amount = ConsoleEngine.scanner.nextDouble();

                Account account = Startup.getSession().getLoggedUser().getAccount(accountID);

                if (account == null) {
                    System.err.println("ERROR: This account does not exist or does not belong to you!");
                    continue;
                }

                try {
                    account.deposit(amount);
                } catch (Exception exception) {
                    System.err.println("ERROR: " + exception.getMessage());
                }
            } else if (commandId == 5) {
                int accountID = ConsoleEngine.scanner.nextInt();
                double amount = ConsoleEngine.scanner.nextDouble();

                Account account = Startup.getSession().getLoggedUser().getAccount(accountID);

                if (account == null) {
                    System.err.println("ERROR: This account does not exist or does not belong to you!");
                    continue;
                }

                try {
                    account.withdraw(amount);
                } catch (Exception exception) {
                    System.err.println("ERROR: " + exception.getMessage());
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
