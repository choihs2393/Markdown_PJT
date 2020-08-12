package com.ggbg.note.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.ggbg.note.domain.dto.AccountBandDTO;
import com.ggbg.note.domain.dto.BandDTO;
import com.ggbg.note.domain.entity.AccountBandEntity;
import com.ggbg.note.domain.entity.BandEntity;
import com.ggbg.note.exception.InternalServerException;
import com.ggbg.note.repository.AccountBandRepo;
import com.ggbg.note.repository.BandRepo;
import com.ggbg.note.util.JwtTokenUtil;

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
	public BandDTO addBand(String bandName, int accountNo, String bandMasterName) {
		// band add
		BandDTO bandDTO = new BandDTO();
		bandDTO.setName(bandName);
		bandDTO.setMaster(accountNo);
		bandDTO.setBandMasterName(bandMasterName);
		
		int bandNo = -1;
		bandNo = bandRepo.save(BandEntity.builder()
							.no(bandDTO.getNo())
							.name(bandDTO.getName())
							.master(bandDTO.getMaster())
							.masterName(bandDTO.getBandMasterName())
							.build()
				).getNo();
		
		if(bandNo == -1)
			throw new InternalServerException("addBand");
		
		bandDTO.setNo(bandNo);
		
		AccountBandDTO accountBandDTO = new AccountBandDTO();
		accountBandDTO.setBandNo(bandNo);
		accountBandDTO.setAccountNo(accountNo);
		accountBandDTO.setStatus(0);
		
		int accountBandNo = -1;
		
		accountBandNo = accountBandRepo.save(AccountBandEntity.builder()
										.bandNo(accountBandDTO.getBandNo())
										.accountNo(accountBandDTO.getAccountNo())
										.status(accountBandDTO.getStatus())
										.build()
				).getNo();
		
		if(accountBandNo == -1)
			throw new InternalServerException("addBand");
		
		return bandDTO;
	}
	
	@Override
	public int deleteBand(int bandNo, int accountNo) {
		int ret = -1;
		
		Optional<BandEntity> optional = bandRepo.findBandByNo(bandNo);
		if(optional.isPresent()) {
			BandEntity band = optional.get();
			if(band.getMaster() == accountNo)
				ret = bandRepo.deleteBandByNo(bandNo);
		}else {
			return ret;
		}
		return ret;
	}
	
	
	@Override
	public int renameBand(String newBandName, int bandNo, int accountNo) {
		int ret = -1;
		
		Optional<BandEntity> optional = bandRepo.findBandByNo(bandNo);
		if(optional.isPresent()) {
			BandEntity band = optional.get();
			if(band.getMaster() == accountNo)
				ret = bandRepo.updateBandByNo(newBandName, bandNo);
		}else {
			return ret;
		}
		return ret;
	}

}
