package personal.leo.kindlepush.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import personal.leo.kindlepush.data.*;
import personal.leo.kindlepush.error.BadRequestException;
import personal.leo.kindlepush.error.AuthException;
import personal.leo.kindlepush.model.ErrorResult;
import personal.leo.kindlepush.model.UserResult;
import personal.leo.kindlepush.service.TokenService;

import java.util.Optional;

@RestController
@RequestMapping(path = "/user")
public class UserController {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserProfileRepository userProfileRepository,
                          UserRepository userRepository,
                          TokenService tokenService,
                          PasswordEncoder passwordEncoder) {
        this.userProfileRepository = userProfileRepository;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(path = "/register")
    public UserResult register(@RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam("email") String email) throws AuthException {

        Optional<User> optUser = userRepository.findByUsername(username);
        if (optUser.isPresent()) {
            throw new AuthException("Username already exists.");
        }
        User user = new User(username, passwordEncoder.encode(password), email);
        user = userRepository.save(user);

        return new UserResult(tokenService.generateToken(user.getId()));
    }

    @PostMapping(path = "/login")
    public UserResult login(@RequestParam("username") String username,
                        @RequestParam("password") String password) throws AuthException {
        Optional<User> optUser = userRepository.findByUsername(username);
        if (optUser.isPresent()) {
            User user = optUser.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return new UserResult(tokenService.generateToken(user.getId()));
            } else  {
                throw new AuthException("Password does not match.");
            }
        } else {
            throw new AuthException("Username does not exist.");
        }
    }

    @PostMapping(path = "/social-login")
    public String socialLogin(@RequestParam("username") String username,
                              @RequestParam("password") String password){
        return "success";
    }

    @PostMapping(path = "/bind/{userId}")
    public String bindUser(@PathVariable("userId") Long userId,
                           @RequestParam("sender_email") String email,
                           @RequestParam("sender_email_password") String password,
                           @RequestParam("kindle_email") String kindleEmail) {

        Optional<UserProfile> optUserProfile = userProfileRepository.findByUserId(userId);
        if (optUserProfile.isPresent()) {
            UserProfile userProfile = optUserProfile.get();
            userProfile.setKindleEmail(kindleEmail);
            userProfile.setSenderEmail(email);
            userProfile.setSenderEmailPassword(password);
            userProfileRepository.save(userProfile);
        } else {
            UserProfile userProfile = new UserProfile(email, password, kindleEmail);
            Optional<User> optUser = userRepository.findById(userId);
            if (optUser.isPresent()) {
                userProfile.setUser(optUser.get());
                userProfileRepository.save(userProfile);
            } else {
                throw new BadRequestException(HttpStatus.BAD_REQUEST, "Invalid request parameters.");
            }
        }

        return "Bound";
    }

}
