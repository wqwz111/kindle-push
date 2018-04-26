package personal.leo.kindlepush.data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;

@Entity
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Email
    @Size(max = 50)
    @Column(name = "sender_email")
    private String senderEmail;

    @NotNull
    @Size(max = 100)
    @Column(name = "sender_email_password")
    private String senderEmailPassword;

    @NotNull
    @Email
    @Size(max = 50)
    @Column(name = "kindle_email")
    private String kindleEmail;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    public UserProfile() {}

    public UserProfile(String senderEmail, String senderEmailPassword, String kindleEmail) {
        this.senderEmail = senderEmail;
        this.senderEmailPassword = senderEmailPassword;
        this.kindleEmail = kindleEmail;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getSenderEmailPassword() {
        return senderEmailPassword;
    }

    public void setSenderEmailPassword(String senderEmailPassword) {
        this.senderEmailPassword = senderEmailPassword;
    }

    public String getKindleEmail() {
        return kindleEmail;
    }

    public void setKindleEmail(String kindleEmail) {
        this.kindleEmail = kindleEmail;
    }

    @Override
    public String toString() {
        return String.format("User Profile[id=%s, user id=%s, sender email=%s, kindle email=%s]",
                id, user.getId(), senderEmail, kindleEmail);
    }
}
