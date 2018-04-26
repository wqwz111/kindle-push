package personal.leo.kindlepush;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import personal.leo.kindlepush.service.TokenService;

@Configuration
public class JWTFilterConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {


    private TokenService tokenService;

    @Autowired
    public JWTFilterConfig(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public void configure(HttpSecurity builder) throws Exception {

        builder.addFilterBefore(new JWTFilter(tokenService), UsernamePasswordAuthenticationFilter.class);
    }
}
