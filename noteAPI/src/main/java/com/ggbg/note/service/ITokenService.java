package com.ggbg.note.service;

public interface ITokenService {
	// newToken
	
	public boolean newAccessTokenByAccessToken(String accessToken);
	// return - accessToken 발급 중 탈퇴한 회원인 경우 false / 외 true
	public boolean newAccessTokenByRefreshToken(String refreshToken);
	public boolean newRefreshTokenByRefreshToken(String refreshToken);
}
