package com.ggbg.note.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ggbg.note.interceptor.VerifyAccountInterceptor;
import com.ggbg.note.interceptor.VerifyBandMemberInterceptor;
import com.ggbg.note.interceptor.VerifyTokenInterceptor;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
	
	@Autowired
	private VerifyAccountInterceptor verifyAccountInterceptor;
	
//	@Autowired
//	private VerifyTokenInterceptor verifyTokenInterceptor;
	
//	@Autowired
//	private VerifyBandMemberInterceptor verifyBandMemberInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(verifyAccountInterceptor)
			.addPathPatterns("/account/v1/*")
			.addPathPatterns("/band/v1/*")
			.addPathPatterns("/accountBand/v1/*")
			.order(0);
		
//		registry.addInterceptor(verifyBandMemberInterceptor)
//			.addPathPatterns("/note/v2/*")
//			.order(1);
	}
}
