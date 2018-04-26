package personal.leo.kindlepush.data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 8, max = 16)
    @Column(unique = true, updatable = false, nullable = false)
    private String username;

    @NotNull
    @Size(max = 100)
    private String password;

    @NotNull
    @Email
    @Size(max = 50)
    private String email;

    @ElementCollection(fetch = FetchType.EAGER)
    List<Role> roles;

    public User() {}

    public User(String username, String password, String email, List<Role> roles) {
        this.username = username;
        this.password = password;
        this.email = email;
        if (roles == null) {
            this.roles = new ArrayList<>();
            this.roles.add(Role.ROLE_CLIENT);
        } else {
            this.roles = roles;
        }
    }

    public User(String username, String password, String email) {
        this(username, password, email, null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return String.format("User[id=%s, username=%s, email=%s]",
                id, username, email);
    }
}
