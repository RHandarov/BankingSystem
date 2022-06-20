package UI.Console;

import java.util.Scanner;

public final class ConsoleEngine {
    private static Scanner scanner = new Scanner(System.in);
    private static final String[] AUTH_OPERATIONS = new String[] {
            "login",
            "register",
            "exit"
    };

    private ConsoleEngine() throws InstantiationException {
        throw new InstantiationException("Cannot instantiate ConsoleEngline class!");
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

            } else if (authOperationType.equals("register")) {
                //TODO: implement it
            } else {
                System.out.println("Goodbye!");
                break;
            }
        }
    }
}
