package BankingSystem.Models.Accounts;

import BankingSystem.Exceptions.AccountExceptions.InvalidAccountOwnerException;
import BankingSystem.Exceptions.AccountExceptions.InvalidTransferOperationException;
import BankingSystem.Models.User;

public class CurrentAccount extends Account {
    protected SavingsComponentAccount attachedSavingsComponentAccount;

    public CurrentAccount(int id, double amount, User owner) {
        super(id, amount, owner, AccountType.CURRENT);
        this.attachedSavingsComponentAccount = null;
    }

    public void setAttachedSavingsComponentAccount(SavingsComponentAccount savingsComponentAccount) {
        if (savingsComponentAccount == null) {
            throw new NullPointerException("You should provide a valid savings account!");
        }

        if (!this.owner.equals(savingsComponentAccount.owner)) {
            throw new InvalidAccountOwnerException("This savings account does not belong to you!");
        }

        if (savingsComponentAccount.attachedCurrentAccount != this) {
            throw new IllegalStateException("The savings account should be attached to this account!");
        }

        this.attachedSavingsComponentAccount = savingsComponentAccount;
    }

    public SavingsComponentAccount getAttachedSavingsComponentAccount() {
        return this.attachedSavingsComponentAccount;
    }

    @Override
    public void deposit(double addedAmount) {
        if (addedAmount <= 0.0) {
            throw new IllegalArgumentException("You should add positive amount of money!");
        }

        this.amount += addedAmount;
    }

    @Override
    public void withdraw(double withdrawnAmount) {
        if (withdrawnAmount <= 0.0) {
            throw new IllegalArgumentException("You cannot withdraw non-positive amount of money!");
        }

        if (withdrawnAmount > this.amount) {
            throw new InvalidTransferOperationException("You cannot withdraw more than you have!");
        }

        this.amount -= withdrawnAmount;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(super.toString());

        builder.append(';');
        builder.append(this.attachedSavingsComponentAccount.id);

        return builder.toString();
    }
}
