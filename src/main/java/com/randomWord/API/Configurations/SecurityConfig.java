package com.randomWord.API.Configurations;

import java.io.IOException;

import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.randomWord.API.Services.CustomUserDetailsService;



@EnableWebSecurity
@Configuration
@Order(1) 
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfiguration {
	@Value("${USERNAME}")
	String username;
	@Value("${PASSWORD}")
	String password;

 

	@Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
        	.cors().configurationSource(corsConfigurationSource())
	        .and()    
	        .authorizeRequests(requests -> requests
	        		.antMatchers("/**").permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/home")).permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/list")).permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/search")).permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/beginsWith/**")).permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/endsWith/**")).permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/length/**")).permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/css/**")).permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/images/**")).permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/add/**")).authenticated()
                    .requestMatchers(new AntPathRequestMatcher("/updateData/**")).authenticated()
                    .requestMatchers(new AntPathRequestMatcher("/update/**")).authenticated()
                    .requestMatchers(new AntPathRequestMatcher("/add/**")).authenticated()
                    .requestMatchers(new AntPathRequestMatcher("/delete/**")).authenticated()
                    .requestMatchers(new AntPathRequestMatcher("/deleteId/**")).authenticated()
                    .requestMatchers(new AntPathRequestMatcher("/deleteMulti/**")).authenticated()
                    .requestMatchers(new AntPathRequestMatcher("/change/**")).authenticated()
                    .anyRequest().authenticated())
                .formLogin()
                    .successHandler(new SavedRequestAwareAuthenticationSuccessHandler())
      	          .permitAll()
      	          .and()
      	      .logout()
      	      	  .logoutSuccessUrl("/login?logout")
      	          .permitAll()
      	          .and()
      	      .exceptionHandling()
      	      	.accessDeniedPage("/login");


            return http.build();
          }

    @Bean
	public PasswordEncoder passwordEncoder() {
		
	    return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
	    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	    authProvider.setUserDetailsService(userDetailsService());
	    authProvider.setPasswordEncoder(passwordEncoder());
	    return authProvider;
	}
	

    
    @Bean
    public CustomUserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }



    @Bean
    private static CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:8080"); 
        configuration.addAllowedMethod("GET");
        configuration.addAllowedMethod("POST");

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    

}   
    