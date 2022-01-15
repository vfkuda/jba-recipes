package recipes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import recipes.user.UserService;

@EnableWebSecurity
public class WebSecurityConfigurerAdapterImpl extends WebSecurityConfigurerAdapter {

    @Autowired
    UserService userService;
    @Autowired
    PasswordEncoder passEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).
                passwordEncoder(passEncoder);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().
                mvcMatchers("/").
                mvcMatchers("/h2").
                mvcMatchers("/h2/**").
                mvcMatchers("/actuator/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers("/").permitAll()
                .mvcMatchers("/api/register").permitAll()
                .mvcMatchers("/login").permitAll()
                .anyRequest().authenticated()
                .and().httpBasic();
        http.csrf().disable();
//        http.csrf().ignoringAntMatchers("/h2/**");
//        http.headers().frameOptions().disable();
        http.headers().frameOptions().sameOrigin();
    }
}



