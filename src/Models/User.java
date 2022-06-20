package Models;

public class User {
    private int id;
    private String username;
    private String password;

    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(this.id);
        builder.append(';');
        builder.append(this.username);
        builder.append(';');
        builder.append(this.password);

        return builder.toString();
    }
}
