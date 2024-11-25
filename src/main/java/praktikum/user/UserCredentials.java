package praktikum.user;

public class UserCredentials {
    private final String email;
    private final String password;
    private final String name;

    public UserCredentials(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public static UserCredentials fromUser(praktikum.user.User user) {
        return new UserCredentials(user.getEmail(), user.getPassword(), user.getName());
    }

    public static UserCredentials fromUserWithoutEmail(praktikum.user.User user) {
        return new UserCredentials(null, user.getPassword(), user.getName());
    }

    public static UserCredentials fromUserWithoutPassword(praktikum.user.User user) {
        return new UserCredentials(user.getEmail(), null, user.getName());
    }

    public static UserCredentials fromUserWithoutName(praktikum.user.User user) {
        return new UserCredentials(user.getEmail(), user.getPassword(), null);
    }

    public static UserCredentials fromUserWithWrongEmail(praktikum.user.User user) {
        return new UserCredentials(user.getEmail() + "a1", user.getPassword(), user.getName());
    }

    public static UserCredentials fromUserWithWrongPassword(praktikum.user.User user) {
        return new UserCredentials(user.getEmail(), user.getPassword() + "a1", user.getName());
    }
    public static UserCredentials fromUserWithWrongName(praktikum.user.User user) {
        return new UserCredentials(user.getEmail(), user.getPassword(), user.getName() + "a1");
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }
}
