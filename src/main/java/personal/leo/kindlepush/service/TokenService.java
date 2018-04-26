package personal.leo.kindlepush.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import personal.leo.kindlepush.data.User;
import personal.leo.kindlepush.data.UserRepository;
import personal.leo.kindlepush.error.AuthException;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Optional;

@Service
public class TokenService {

    private final String SECRET_KEY;
    private final long EXPIRE;

    private final String CLAIM_USER_ID = "userid";
    private final String JWT_ISSUER = "kindle-push";

    private final MyUserDetails myUserDetails;
    private final UserRepository userRepository;

    @Autowired
    public TokenService(@Value("${jwt-secret-key}") String secretKey,
                        @Value("${jwt-expire}") long expire,
                        MyUserDetails myUserDetails,
                        UserRepository userRepository) {
        this.SECRET_KEY = secretKey;
        this.EXPIRE = expire;
        this.myUserDetails = myUserDetails;
        this.userRepository = userRepository;
    }

    public String generateToken(Long userId) throws AuthException {
        String token = null;
        Date now = new Date();
        Date expireAt = new Date(now.getTime() + EXPIRE);
        try {
            Algorithm algorithmHS = Algorithm.HMAC256(SECRET_KEY);
            token = JWT.create()
                    .withIssuer(JWT_ISSUER)
                    .withClaim(CLAIM_USER_ID, userId)
                    .withIssuedAt(now)
                    .withExpiresAt(expireAt)
                    .sign(algorithmHS);
        } catch (Exception e) {
            throw new AuthException("Generate token failed.", e);
        }

        return token;
    }

    public Authentication getAuthentication(String token) throws AuthException {
        UserDetails userDetails = myUserDetails.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public long getUserId(String token) throws AuthException {
        long userId;
        try {
            userId = JWT.decode(token).getClaim(CLAIM_USER_ID).asLong();
        } catch (JWTDecodeException e) {
            throw new AuthException("Invalid token.", e);
        }
        return userId;
    }

    private String getUsername(String token) throws AuthException {
        String username = null;
        Optional<User> optUser = userRepository.findById(getUserId(token));
        if (optUser.isPresent()) {
            username = optUser.get().getUsername();
        }
        return username;
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

    public boolean validateToken(String token) throws AuthException {
        try {
            Algorithm algorithmHS = Algorithm.HMAC256(SECRET_KEY);
            JWTVerifier verifier = JWT.require(algorithmHS)
                    .withIssuer(JWT_ISSUER)
                    .build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        } catch (UnsupportedEncodingException e) {
            throw new AuthException("Validate token failed.", e);
        }
    }
}
