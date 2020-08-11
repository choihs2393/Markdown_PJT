package com.ggbg.note.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ggbg.note.domain.Role;
import com.ggbg.note.domain.Token;
import com.ggbg.note.domain.dto.AccountDTO;
import com.ggbg.note.domain.entity.AccountEntity;
import com.ggbg.note.exception.InternalServerException;
import com.ggbg.note.repository.AccountRepo;
import com.ggbg.note.util.JwtTokenUtil;

@Service
public class NonMemberServiceImpl implements INonMemberService {

	@Autowired
	private AccountRepo accountRepo;

	@Autowired
	RedisTemplate<String, Object> redisTemplate;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	JwtTokenUtil jtu;

	@Autowired
	private JavaMailSender mailSender;

	@Transactional
	@Override
	public boolean signUp(AccountDTO accountDTO) {
		BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder(10);
		accountDTO.setRole(Role.USER);
		accountDTO.setPassword(bcryptPasswordEncoder.encode(accountDTO.getPassword()));

		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		accountDTO.setCreateDate(simpleDateFormat.format(date));
		accountRepo.save(AccountEntity.builder()
									.email(accountDTO.getEmail())
									.name(accountDTO.getName())
									.password(accountDTO.getPassword())
									.role(accountDTO.getRole())
									.createDate(accountDTO.getCreateDate())
									.build()
						);
		
		return true;
	}

	@Override
	public boolean emailCheck(String email) {
		Optional<AccountEntity> optional = accountRepo.findAccountByEmail(email);
		if (optional.isPresent()) {
			return false;
		} else {
			return true; // 해당부분에 대해서는 예외처리로
		}
	}

	@Override
	public boolean emailAuthSend(String email) {

		Random random = new Random();
		int randNum = random.nextInt(3829375) + 293817;

		String setfrom = "Test";
		String tomail = email;
		String title = "Note 회원가입 인증 이메일 입니다.";
		String content = System.getProperty("line.separator") + System.getProperty("line.separator") + " 인증 코드입니다 : "
				+ randNum;

		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			mimeMessageHelper.setFrom(setfrom);
			mimeMessageHelper.setTo(tomail);
			mimeMessageHelper.setSubject(title);
			mimeMessageHelper.setText(content);
			mailSender.send(mimeMessage);

			/*
			 * redis save part
			 */

			String key = email + "-auth";
			String value = jwtTokenUtil.generateEmailAuthToken(randNum + "");

			Token token = new Token();
			token.setEmail(email);
			token.setToken(value);
			ValueOperations<String, Object> vop = redisTemplate.opsForValue();
			vop.set(key, token);
			redisTemplate.expire(key, 60 * 5, TimeUnit.SECONDS); // 5분
			return true;
		} catch (Exception e) {
			throw new InternalServerException("emailAuthSend");
		}
	}

	@Override
	public boolean emailAuthCheck(String email, String authNum) { // 여기서 fail 뜨면 시간 만료된거니까 다시 인증코드 보내라하세염
		/*
		 * redis 에서 가져와서 같은 값인지 검증하는 부분
		 */
		ValueOperations<String, Object> vop = redisTemplate.opsForValue();
		String key = email + "-auth";

		Token token = (Token) vop.get(key);
		if (token != null) {
			String value = jwtTokenUtil.getEmailAuthNumFromToken(token.getToken());
			if (authNum.equals(value)) {
				return true;
			}
		}
		
		return false;
	}

}
