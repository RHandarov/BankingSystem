package BankingSystem;

import BankingSystem.Console.UI.ConsoleEngine;

import java.io.IOException;

public class Startup {
    private static Session session;

    public static Session getSession() {
        return Startup.session;
    }

    public static void main(String[] args) throws IOException {
        Startup.session = new Session();

        ConsoleEngine.run();

        Startup.session.close();
    }
}
