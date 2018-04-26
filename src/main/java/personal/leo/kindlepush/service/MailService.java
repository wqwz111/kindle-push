package personal.leo.kindlepush.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import personal.leo.kindlepush.data.UserProfile;
import personal.leo.kindlepush.data.UserProfileRepository;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Properties;

@Service
public class MailService {

    public enum MailStatus {
        SUCCESS (0),
        FAIL (-1),
        PENDING (1);

        private int value;

        MailStatus(int i) {
            this.value = i;
        }

        public int getValue() {
            return value;
        }
    }

    private UserProfileRepository userProfileRepository;

    private JavaMailSenderImpl mailSender;

    private UserProfile userProfile;

    @Autowired
    public MailService(UserProfileRepository repository) {
        this.userProfileRepository = repository;
    }

    public MailService prepare(String userId) throws MessagingException {
        Optional<UserProfile> optUserProfile = userProfileRepository.findByUserId(Long.valueOf(userId));
        if (!optUserProfile.isPresent()) {
            throw new MessagingException("Sending mail failed. No user found with ID: " + userId);
        }
        userProfile = optUserProfile.get();

        String[] emailParts = userProfile.getSenderEmail().split("@");
        String username = emailParts[0];
        String host = "smtp.".concat(emailParts[1]);

        mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(587);
        mailSender.setUsername(username);
        mailSender.setPassword(userProfile.getSenderEmailPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.debug", "true");

        return this;
    }

    @Async
    public void sendMail(Path path) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(userProfile.getSenderEmail());
        helper.setTo(userProfile.getKindleEmail());
        helper.setSubject("convet");
        helper.setText("");
        helper.addAttachment(path.getFileName().toString(), path.toFile());

        mailSender.send(message);
    }

    public int cancelPendingMail(int id) {
        return 0;
    }

    public int cancelAllPendingMails(long senderId) {
        return 0;
    }

    public MailStatus getStatusById(int id) {
        return MailStatus.FAIL;
    }

    public MailStatus getStatusBySender(long senderId) {
        return MailStatus.FAIL;
    }

}
