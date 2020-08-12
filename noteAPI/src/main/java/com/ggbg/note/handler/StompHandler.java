package com.ggbg.note.handler;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import com.ggbg.note.util.JwtTokenUtil;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private final JwtTokenUtil jwtTokenProviderUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        
        if (StompCommand.CONNECT == accessor.getCommand()) {
           String token = accessor.getFirstNativeHeader("Authorization");
           token = token.substring(7);
           
           System.out.println("StompHandler -> token " + token);
           try {
              jwtTokenProviderUtil.isTokenExpired(token);
           }
           catch (Exception e) {
              System.out.println("StompHandler Error!");
           }

        }
        return message;
    }
}