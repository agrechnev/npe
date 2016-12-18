package agrechnev.model;

import agrechnev.helpers.Util;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Oleksiy Grechnyev on 11/21/2016.
 * User entity for the NPE project
 */
@Entity
public class UserEntity implements EntityWithId {


    @Id
    @GeneratedValue
    private Long id;  // Unique user id

    //------------- Proper fields -----------------

    // User's info
    @Column(nullable = false)
    private String login;

    @Column(nullable = false)
    private String passw;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String email;

    /**
     * The point system, any new user has few (say 0) points form the start
     * And you need to reach, say, 1000 to become an EXPERT
     */
    private int points;

    @Column(nullable = false)
    private UserRole role;

    //-------------- Links -----------------------
    @ManyToMany
    private Set<CategoryEntity> myCategories = new HashSet<>();

    @OneToMany
    private Set<PostEntity> myPosts = new HashSet<>();

    //--------------------------------------------
    //---------------------------------------------------------
    // Constructors


    public UserEntity() {
    }

    public UserEntity(String login, String passw, String fullName, String email, int points,
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

    public Set<CategoryEntity> getMyCategories() {
        return myCategories;
    }

    public void setMyCategories(Set<CategoryEntity> myCategories) {
        this.myCategories = myCategories;
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
        sb.append(", myCategories=[");
        sb.append(Util.separatedList(myCategories.stream().map(CategoryEntity::getCategoryName)
                .collect(Collectors.toList()), ", "));
        sb.append("]}");
        return sb.toString();
    }

    //--------------- equals + hashCode -------------

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEntity)) return false;

        UserEntity that = (UserEntity) o;

        return getId() != null ? getId().equals(that.getId()) : that.getId() == null;

    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
