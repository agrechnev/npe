package agrechnev.dto;

import agrechnev.model.UserRole;

/**
 * Created by Oleksiy Grechnyev on 11/21/2016.
 * User DTO for the NPE project
 * Note: password is NOT set when reading user data from the repo
 */
public class UserDto {

    private Long id;  // Unique user id

    //------------- Proper fields -----------------

    // User's info
    private String login;
    private String passw;
    private String fullName;
    private String email;

    /**
     * The point system, any new user has few (say 0) points form the start
     * And you need to reach, say, 1000 to become an EXPERT
     */
    private int points;

    private UserRole role;

    //--------------------------------------------
    //---------------------------------------------------------
    // Constructors


    public UserDto() {
    }

    public UserDto(String login, String passw, String fullName, String email, int points,
                   UserRole role) {
        this.login = login;
        this.passw = passw;
        this.fullName = fullName;
        this.email = email;
        this.points = points;
        this.role = role;
    }

    //---------------------------------------------------------
    // Getetrs+Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassw() {
        return passw;
    }

    public void setPassw(String passw) {
        this.passw = passw;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    //----------------------------------------------------------
    // toString()


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserEntity{");
        sb.append("id=").append(id);
        sb.append(", login='").append(login).append('\'');
        sb.append(", passw='").append(passw).append('\'');
        sb.append(", fullName='").append(fullName).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", points=").append(points);
        sb.append(", role=").append(role);
        sb.append("}");
        return sb.toString();
    }

    //--------------- equals + hashCode -------------

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDto)) return false;

        UserDto that = (UserDto) o;

        return getId() != null ? getId().equals(that.getId()) : that.getId() == null;

    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
