package agrechnev.web;

/**
 * Created by Oleksiy Grechnyev on 12/30/2016.
 * A trivial class used to change password
 */
public class PasswordChanger {
    public String oldPassw;
    public String newPassw;

    public PasswordChanger() {
    }

    public PasswordChanger(String oldPassw, String newPassw) {
        this.oldPassw = oldPassw;
        this.newPassw = newPassw;
    }
}
