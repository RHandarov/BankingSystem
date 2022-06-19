package UI.Console;

public final class ConsoleEngine {
    private ConsoleEngine() throws InstantiationException {
        throw new InstantiationException("Cannot instantiate ConsoleEngline class!");
    }

    public static void run() {
        System.out.println("hello world!");
    }
}
