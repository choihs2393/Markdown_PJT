package com.ggbg.note.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ggbg.note.security.CustomAccessDeniedHandler;
import com.ggbg.note.security.CustomAuthenticationEntryPoint;
import com.ggbg.note.security.CustomJwtRequestFilter;
import com.ggbg.note.security.CustomLogoutHandler;
import com.ggbg.note.security.CustomLogoutSuccessHandler;
import com.ggbg.note.security.CustomUsernamePasswordAuthenticationFilter;
import com.ggbg.note.service.CustomAccountDetailService;
import com.ggbg.note.service.CustomOAuth2UserService;
import com.ggbg.note.util.JwtTokenUtil;
//
//
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private CustomOAuth2UserService OAuth2UserService;
	
	@Autowired
    private CustomJwtRequestFilter jwtRequestFilter;
	
	@Autowired
	private CustomAuthenticationEntryPoint authenticationEntryPoint;
	
	@Autowired
	private CustomAccessDeniedHandler accessDeniedHandler;
	
	@Autowired
	private CustomAccountDetailService detailService;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	RedisTemplate<String, Object> redisTemplate;

	@Autowired
	private CustomLogoutHandler customLogoutHandler;
	
	@Autowired
	private CustomLogoutSuccessHandler customLogoutSuccessHandler;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.httpBasic().disable() // for rest api
            .cors();
		
        http
			.csrf().disable() // for rest api
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
        http
            .authorizeRequests()
//            .antMatchers("/admin/**").hasAnyRole("ADMIN")
            .antMatchers("/account/**").hasAnyRole("USER")
            .antMatchers("/nonmember/**").permitAll()
            .antMatchers("/token/**").permitAll()
            .anyRequest().authenticated();
        
        http
        	.logout()
//	        	.addLogoutHandler(customLogoutHandler)
	        	.logoutUrl("/account/logout")
	        	.logoutSuccessHandler(customLogoutSuccessHandler);
//        	
        http
			.addFilterAt(getAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        	
		
		http
        	.oauth2Login()
            	.userInfoEndpoint()
                	.userService(OAuth2UserService);
		
    	http
		    .exceptionHandling() 
		        .authenticationEntryPoint(authenticationEntryPoint)
		        .accessDeniedHandler(accessDeniedHandler);	
	}
	
	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(detailService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    
	protected CustomUsernamePasswordAuthenticationFilter getAuthenticationFilter() {
		CustomUsernamePasswordAuthenticationFilter filter = new CustomUsernamePasswordAuthenticationFilter(jwtTokenUtil, redisTemplate);
		
		try {
			filter.setAuthenticationManager(this.authenticationManagerBean());
			filter.setFilterProcessesUrl("/nonmember/login");
			filter.setPostOnly(true);
			filter.setUsernameParameter("username");
			filter.setPasswordParameter("password");
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return filter;
	}
	
}
