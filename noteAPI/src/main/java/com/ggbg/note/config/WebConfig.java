package com.ggbg.note.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig{
	/*
	 * 	interceptor 추가해서 처리해야하는 부분
	 * 	1. 사용하고 있는 email 과 같은지???
	 * ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
	 *  2. 만약에 admin 권한이 있는거에 접근할 때 interceptor 로 진짜 admin 권한인지 usertable 찾아서 검증
	 *  ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
	 *  만약 a라는 사용자가 자신의 accesstoken 을 변조시켜서 다른 사람의 게시글에 접근하려고 하면?
	 * 
	 */
    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods(HttpMethod.GET.name(),
		                            	HttpMethod.HEAD.name(),
		                            	HttpMethod.POST.name(),
		                            	HttpMethod.PUT.name(),
		                            	HttpMethod.DELETE.name())
                        .allowCredentials(false)
                        .maxAge(3600);
            }
        };
    }
}
