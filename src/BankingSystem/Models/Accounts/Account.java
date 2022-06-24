package BankingSystem.Models.Accounts;

import BankingSystem.Models.User;

public abstract class Account {
    protected int id;
    protected double amount;
    protected User owner;
    protected AccountType accountType;

    protected Account(int id, double amount, User owner, AccountType accountType) {
        this.setId(id);
        this.setAmount(amount);
        this.setOwner(owner);
        this.setAccountType(accountType);
    }

    private void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID should be a positive integer!");
        }

        this.id = id;
    }

    private void setAmount(double amount) {
        if (amount < 0.0) {
            throw new IllegalArgumentException("Account cannot have negative amount of money!");
        }

        this.amount = amount;
    }

    private void setOwner(User owner) {
        if (owner == null) {
            throw new NullPointerException("The account should have owner!");
        }

        this.owner = owner;
    }

    private void setAccountType(AccountType accountType) {
        if (accountType == null) {
            throw new NullPointerException("The account should have some type!");
        }

        this.accountType = accountType;
    }

    public int getId() {
        return this.id;
    }

    public double getAmount() {
        return this.amount;
    }

    public User getOwner() {
        return new User(this.owner);
    }

    public AccountType getAccountType() {
        return this.accountType;
    }

    public abstract void deposit(double addedAmount);

    public abstract void withdraw(double withdrawnAmount);

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(this.id);
        builder.append(';');
        builder.append(this.amount);
        builder.append(';');
        builder.append(this.owner.getUsername());
        builder.append(';');
        builder.append(this.accountType);

        return builder.toString();
    }
}
