package uz.pdp.cinema_room_rest_service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import uz.pdp.cinema_room_rest_service.security.JWTFilter;
import uz.pdp.cinema_room_rest_service.security.JwtAuthenticationEntryPoint;
import uz.pdp.cinema_room_rest_service.service.AuthService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    AuthService authService;


    @Autowired
    JwtAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public PasswordEncoder getEncoder(){
        return new BCryptPasswordEncoder();
    }



    @Bean
    public AuthenticationProvider getProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(authService);
        provider.setPasswordEncoder(getEncoder());
        return provider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(getProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/login", "/register", "/").permitAll()
                .mvcMatchers("/dashboard").hasAnyRole("ADMIN", "SUPER_ADMIN")
                .mvcMatchers("/show_bucket","/refund").authenticated()
                        .mvcMatchers(HttpMethod.GET,"/")
                                .permitAll()
                                        .mvcMatchers(HttpMethod.POST,"/**")
                                                .authenticated()
                        .mvcMatchers("/cashBox").hasRole("SUPER_ADMIN");

//                .authorizeRequests()
//                .mvcMatchers("/dashboard").hasRole("ADMIN")
//                .mvcMatchers("/register", "/register", "/home","/login", "/").permitAll()
//                .mvcMatchers("/**").authenticated()
//                .and().formLogin().loginPage("/login").permitAll().defaultSuccessUrl("/courses");
        http.addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
//                .mvcMatchers("/login","/register")
//                .permitAll()
//                .antMatchers(HttpMethod.GET,"/**/movie/**")
//                .permitAll()
//                .mvcMatchers("/**")
//                .authenticated();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManagerBean();
    }


    @Bean
    public JWTFilter jwtFilter() {
        return new JWTFilter();
    }

}
