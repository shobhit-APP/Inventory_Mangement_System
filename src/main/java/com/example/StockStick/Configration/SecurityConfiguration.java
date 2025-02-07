package com.example.StockStick.Configration;

import com.example.StockStick.CustomAcessFilter.UserPlanAccessFilter;
import com.example.StockStick.Service.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Autowired
 private UserDetailsService userDetailsService;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
          return http
                  .csrf(AbstractHttpConfigurer::disable)
                  .authorizeHttpRequests(request->request.requestMatchers("/Stock-Stick","/app/StockStick.com","/app/StockStick","static/**","/image/**","/app/AboutUs" ,"/app/ContactUs","/app/Pricing","/app/Register","/app/terms_Policy","/app/UserPlan","/app/SelectedPlan","/app/login").permitAll()
                          .anyRequest().authenticated()
                  )
                  .formLogin(form->form
                          .loginPage("/app/login")
                          .loginProcessingUrl("/app/login")
                          .usernameParameter("emailAddress") // Ensure this matches your form field
                          .passwordParameter("password")
                          .defaultSuccessUrl("/app/Inventory",true)
                          .failureUrl("/app/login?error=true")
                          .permitAll()
                  )
                  .logout(logout->logout.logoutUrl("/app/logout")
                          .logoutSuccessUrl("/app/logout-success")
                          .invalidateHttpSession(true)
                          .clearAuthentication(true)
                          .deleteCookies("JSESSIONID")
                          .permitAll()
                  )
                  .exceptionHandling(exception->exception.accessDeniedPage("/app/Unauthorized")
                  )
                  .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                  .build();

    }
    @Bean
    public AuthenticationProvider authenticationProvider()
    {
        DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }
    @Bean
    public PasswordEncoder passwordEncoder()
    {
      return new BCryptPasswordEncoder(12);
    }
    @Bean
    public FilterRegistrationBean<UserPlanAccessFilter> userPlanAccessFilterFilterRegistrationBean()
    {
        FilterRegistrationBean<UserPlanAccessFilter> registrationBean=new FilterRegistrationBean<>();
        registrationBean.setFilter(new UserPlanAccessFilter());
        registrationBean.addUrlPatterns("/app/UserPlan");
        return  registrationBean;
    }
}
