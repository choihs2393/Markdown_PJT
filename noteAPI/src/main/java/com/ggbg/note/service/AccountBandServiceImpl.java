package com.ggbg.note.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ggbg.note.domain.dto.BandMemberDTO;
import com.ggbg.note.domain.dto.PrimitiveAccountDTO;
import com.ggbg.note.domain.entity.AccountBandEntity;
import com.ggbg.note.domain.entity.AccountEntity;
import com.ggbg.note.domain.entity.BandEntity;
import com.ggbg.note.exception.InternalServerException;
import com.ggbg.note.exception.UnAuthorizationException;
import com.ggbg.note.repository.AccountBandRepo;
import com.ggbg.note.repository.AccountRepo;
import com.ggbg.note.repository.BandMemberRepo;
import com.ggbg.note.repository.BandRepo;
import com.ggbg.note.util.MapperUtil;

@Service
public class AccountBandServiceImpl implements IAccountBandService{
	@Autowired
	private MapperUtil mapperUtil;
	
	@Autowired
	private BandMemberRepo bandMemberRepo;
	
	@Autowired
	private BandRepo bandRepo;
	
	@Autowired
	AccountBandRepo accountBandRepo;
	
	@Autowired
	private AccountRepo accountRepo;

	@Override
	public List<BandMemberDTO> getBandMember(int bandNo) {
		List<BandMemberDTO> list = bandMemberRepo.getBandMemberDTOList(bandNo);
		return list;
	}
	
	@Override
	public BandMemberDTO inviteBandMember(String email, int masterNo, int bandNo) {
		
		Integer retMaster = -1;
		retMaster = bandRepo.findMasterNoByBandNo(bandNo);
		if(retMaster == null)
			throw new InternalServerException("InviteBandMember exception");
		if(retMaster != masterNo)
			throw new UnAuthorizationException(masterNo + " :, this account is not a master");
		
		Optional<AccountEntity> optional = accountRepo.findAccountByEmail(email);
		
		BandMemberDTO bandMemberDTO = new BandMemberDTO();
		
		
		if(optional.isPresent()) {
			AccountEntity accountEntity = optional.get();
			bandMemberDTO.setNo(accountEntity.getNo());
			bandMemberDTO.setName(accountEntity.getName());
			bandMemberDTO.setEmail(accountEntity.getEmail());
			bandMemberDTO.setStatus(2);
			
			if(accountBandRepo.findAccountBandByAccountNoAndBandNo(bandMemberDTO.getNo(), bandNo) != 0) {
				return null;
			}
		}
		
		accountBandRepo.save(AccountBandEntity.builder()
				.bandNo(bandNo)
				.accountNo(bandMemberDTO.getNo())
				.status(2)
				.build());
		
		return bandMemberDTO;
	}
	
	@Override
	public List<PrimitiveAccountDTO> findAccountList(String email) {
		List<PrimitiveAccountDTO> list = new ArrayList<PrimitiveAccountDTO>();
		list = bandMemberRepo.getAPrimitiveAccountDTOList(email);
		return list;
	}
	
	@Override
	public boolean deleteMember(int masterNo, int accountNo, int bandNo) {
		int ret = -1;
		
		Integer retMaster = -1;
		retMaster = bandRepo.findMasterNoByBandNo(bandNo);
		System.out.println(masterNo);
		if(retMaster == null)
			throw new InternalServerException("InviteBandMember exception");
		if(retMaster != accountNo)
			throw new UnAuthorizationException(masterNo + " :this account is not a master");
		
		ret = accountBandRepo.deleteAccountBandByNo(accountNo, bandNo);
		if(ret == 1)
			return true;
		return false;
	}
	
	@Override
	public boolean acceptInvite(int accountNo, int bandNo) {
		int ret = -1;
		
		ret = accountBandRepo.updateAccountBandByNo(accountNo, bandNo);
		if(ret == 1) {
			return true;
		}
		
		return false; // false 가 나오는경우는 업데이트가 안된경우 -> 그럼 얘는 탈퇴를 했다는것 -> 목록에서 지워버리면됨
	}
	
	@Override
	public boolean declineInvite(int accountNo, int bandNo) {
		int ret = -1;
		
		ret = accountBandRepo.deleteAccountBandByNo(accountNo, bandNo);
		
		if(ret == 1)
			return true;
		return false;
	}
	
}
