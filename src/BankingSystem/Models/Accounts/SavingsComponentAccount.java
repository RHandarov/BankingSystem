package BankingSystem.Models.Accounts;

import BankingSystem.Exceptions.AccountExceptions.InvalidAccountOwnerException;
import BankingSystem.Exceptions.AccountExceptions.InvalidTransferOperationException;
import BankingSystem.Models.User;

public class SavingsComponentAccount extends Account {
    protected CurrentAccount attachedCurrentAccount;

    public SavingsComponentAccount(int id, double amount, User owner, CurrentAccount attachedCurrentAccount) {
        super(id, amount, owner, AccountType.SAVINGS_COMPONENT);
        this.setAttachedCurrentAccount(attachedCurrentAccount);

    }

    private void setAttachedCurrentAccount(CurrentAccount currentAccount) {
        if (currentAccount == null) {
            throw new NullPointerException("You should provide a valid current account!");
        }

        if (!this.owner.equals(currentAccount.owner)) {
            throw new InvalidAccountOwnerException("The current account does not belong to you!");
        }

        if (currentAccount.attachedSavingsComponentAccount != null) {
            throw new IllegalStateException("The current account already has its own component!");
        }

        this.attachedCurrentAccount = currentAccount;
    }

    public CurrentAccount getAttachedCurrentAccount() {
        return this.attachedCurrentAccount;
    }

    @Override
    public void deposit(double addedAmount) {
        if (addedAmount <= 0.0) {
            throw new IllegalArgumentException("You should add positive amount of money!");
        }

        if (addedAmount > this.attachedCurrentAccount.amount) {
            throw new InvalidTransferOperationException("You cannot deposit more money than what you have!");
        }

        this.attachedCurrentAccount.withdraw(addedAmount);
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

        this.attachedCurrentAccount.deposit(withdrawnAmount);
        this.amount -= withdrawnAmount;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(super.toString());

        builder.append(';');
        builder.append(this.attachedCurrentAccount.id);

        return builder.toString();
    }
}
