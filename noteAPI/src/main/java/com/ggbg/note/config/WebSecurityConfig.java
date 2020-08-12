package com.ggbg.note.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

   @Override
   protected void configure(HttpSecurity http) throws Exception {
//      super.configure(http);
      
      http.csrf().disable() // 기본값이 on인 csrf 취약점 보안을 해제한다. on으로 설정해도 되나, 설정할 경우 웹페이지에서 추가 처리가 필요함.
      .headers()
      .frameOptions().sameOrigin() // SockJS는 기본적으로 HTML iframe 요소를 통한 전송을 허용하지 않도록 설정되는데, 해당 내용을 해제한다.
//      .and().formLogin() // 권한없이 페이지 접근하면 로그인 페이지로 이동시킨다.
      .and().authorizeRequests()
            .antMatchers("/ws/**").hasRole("USER") // ws로 시작하는 리소스에 대한 접근 권한 설정
            .anyRequest().permitAll(); // 나머지 리소스에 대한 접근 설정
   }
   
   @Override
   protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//      super.configure(auth);
   }
}