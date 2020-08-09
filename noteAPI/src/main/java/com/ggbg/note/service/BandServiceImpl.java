package com.ggbg.note.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.ggbg.note.bean.AccountBand;
import com.ggbg.note.bean.Band;
import com.ggbg.note.exception.ExpiredTokenException;
import com.ggbg.note.exception.InternalServerException;
import com.ggbg.note.exception.UnAuthorizationException;
import com.ggbg.note.repository.AccountBandRepo;
import com.ggbg.note.repository.AccountRepo;
import com.ggbg.note.repository.BandRepo;
import com.ggbg.note.util.JwtTokenUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;

@Service
public class BandServiceImpl implements IBandService {
	
	@Autowired
	private AccountBandRepo accountBandRepo;
	
	@Autowired
	private BandRepo bandRepo;
	
	@Autowired
	RedisTemplate<String, Object> redisTemplate;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	JwtTokenUtil jtu;

	
	
	@Override
	public void addBand(String bandName, int accountNo) {
		// band add
		Band band = new Band();
		band.setName(bandName);
		band.setNumber(1);
		
		int bandNo = -1;
		bandNo = bandRepo.save(band).getNo();
		
		if(bandNo == -1)
			throw new InternalServerException("addBand");
		
		AccountBand accountBand = new AccountBand();
		accountBand.setBandNo(bandNo);
		accountBand.setAccountNo(accountNo);
		accountBand.setStatus(0);
		
		int accountBandNo = -1;
		
		accountBandNo = accountBandRepo.save(accountBand).getNo();
		
		if(accountBandNo == -1)
			throw new InternalServerException("addBand");
	}

}
