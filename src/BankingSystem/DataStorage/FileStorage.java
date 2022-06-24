package BankingSystem.DataStorage;

import BankingSystem.Exceptions.UserExceptions.UserAlreadyExistException;
import BankingSystem.Models.Accounts.Account;
import BankingSystem.Models.Accounts.AccountType;
import BankingSystem.Models.Accounts.CurrentAccount;
import BankingSystem.Models.Accounts.SavingsComponentAccount;
import BankingSystem.Models.User;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public final class FileStorage implements IDataStorage {
    private static final String USERS_FILE_PATH = "data/users.txt";
    private static final String CURRENT_ACCOUNTS_FILE_PATH = "data/accounts/currentAccounts.txt";
    private static final String SAVING_COMPONENT_ACCOUNTS_FILE_PATH = "data/accounts/savingComponentAccounts.txt";

    private static final String TOKEN_SEPARATOR = ";";

    private static int MAX_ACCOUNT_ID = 0;

    private static FileStorage instance = null;

    private Map<String, User> users;

    private FileStorage() throws IOException {
        this.loadUsers();
        this.loadAccounts();
    }

    private void loadUsers() throws IOException {
        this.users = new HashMap<>();

        File usersFile = new File(FileStorage.USERS_FILE_PATH);
        Scanner scanner = new Scanner(usersFile);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            String[] tokens = line.split(FileStorage.TOKEN_SEPARATOR);
            int currentId = Integer.parseInt(tokens[0]);
            String currentUsername = tokens[1];
            String currentPassword = tokens[2];

            this.users.put(currentUsername, new User(currentId, currentUsername, currentPassword));
        }

        scanner.close();
    }

    private void loadAccounts() throws IOException {
        File currentAccountFile = new File(FileStorage.CURRENT_ACCOUNTS_FILE_PATH);
        Scanner scanner = new Scanner(currentAccountFile);

        while (scanner.hasNextLine()) {
            String[] tokens = scanner.nextLine().split(FileStorage.TOKEN_SEPARATOR);

            int accountID = Integer.parseInt(tokens[0]);
            double accountAmount = Double.parseDouble(tokens[1]);
            User owner = this.users.get(tokens[2]);
            String accountType = tokens[3];
            int connectedAccountID = Integer.parseInt(tokens[4]);

            FileStorage.MAX_ACCOUNT_ID = Math.max(FileStorage.MAX_ACCOUNT_ID, accountID);

            CurrentAccount currentAccount = new CurrentAccount(accountID, accountAmount, owner);
            owner.addAccount(currentAccount);
        }

        scanner.close();

        File savingsFile = new File(FileStorage.SAVING_COMPONENT_ACCOUNTS_FILE_PATH);
        scanner = new Scanner(savingsFile);

        while (scanner.hasNextLine()) {
            String[] tokens = scanner.nextLine().split(FileStorage.TOKEN_SEPARATOR);

            int accountID = Integer.parseInt(tokens[0]);
            double accountAmount = Double.parseDouble(tokens[1]);
            User owner = this.users.get(tokens[2]);
            String accountType = tokens[3];
            int connectedAccountID = Integer.parseInt(tokens[4]);

            FileStorage.MAX_ACCOUNT_ID = Math.max(FileStorage.MAX_ACCOUNT_ID, accountID);

            CurrentAccount connectedAccount = (CurrentAccount) owner.getAccount(connectedAccountID);
            SavingsComponentAccount currentAccount = new SavingsComponentAccount(accountID, accountAmount, owner, connectedAccount);
            connectedAccount.setAttachedSavingsComponentAccount(currentAccount);
            owner.addAccount(currentAccount);
        }

        scanner.close();
    }

    private void saveUsersInFile() throws IOException {
        File usersFile = new File(FileStorage.USERS_FILE_PATH);
        FileWriter writer = new FileWriter(usersFile);

        for (Map.Entry<String, User> entry : this.users.entrySet()) {
            writer.write(entry.getValue() + "\n");
        }

        writer.close();
    }

    private void saveAccountsInFile() throws IOException {
        File currentAccountsFile = new File(FileStorage.CURRENT_ACCOUNTS_FILE_PATH);
        File savingsAccountsFile = new File(FileStorage.SAVING_COMPONENT_ACCOUNTS_FILE_PATH);

        FileWriter currentAccountsWriter = new FileWriter(currentAccountsFile);
        FileWriter savingAccountsWriter = new FileWriter(savingsAccountsFile);

        for (Map.Entry<String, User> userEntry : this.users.entrySet()) {
            User user = userEntry.getValue();

            for (Map.Entry<Integer, Account> accountEntry : user.getAccounts().entrySet()) {
                Account account = accountEntry.getValue();

                if (account.getAccountType() == AccountType.CURRENT) {
                    currentAccountsWriter.write(account + "\n");
                } else {
                    savingAccountsWriter.write(account + "\n");
                }
            }
        }

        currentAccountsWriter.close();
        savingAccountsWriter.close();
    }

    public static FileStorage getInstance() throws IOException {
        if (FileStorage.instance == null) {
            FileStorage.instance = new FileStorage();
        }

        return FileStorage.instance;
    }

    @Override
    public User getUser(String username) {
        return this.users.get(username);
    }

    @Override
    public void saveUser(String username, String password) {
        if (this.getUser(username) != null) {
            throw new UserAlreadyExistException("User with username " + username + " has already registered! Please try with another username!");
        }

        User newUser = new User(this.users.size() + 1, username, User.hashPassword(password));
        this.users.put(username, newUser);
    }

    @Override
    public void saveAccount(User owner) {
        FileStorage.MAX_ACCOUNT_ID++;
        CurrentAccount currentAccount = new CurrentAccount(FileStorage.MAX_ACCOUNT_ID, 0.0, owner);
        owner.addAccount(currentAccount);

        FileStorage.MAX_ACCOUNT_ID++;
        SavingsComponentAccount savingsAccount = new SavingsComponentAccount(FileStorage.MAX_ACCOUNT_ID, 0.0, owner, currentAccount);
        owner.addAccount(savingsAccount);

        currentAccount.setAttachedSavingsComponentAccount(savingsAccount);
    }

    @Override
    public void deleteAccount(User owner, int accountId) {
        if (owner == null) {
            throw new NullPointerException("You should provide a valid user!");
        }

        owner.removeAccount(accountId);
    }

    @Override
    public void update() throws IOException {
        this.saveUsersInFile();
        this.saveAccountsInFile();
    }
}
