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

import com.ggbg.note.bean.Account;
import com.ggbg.note.bean.Role;
import com.ggbg.note.bean.Token;
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

	@Override
	public String signUp(Account account) {
		BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder(10);
		account.setRole(Role.USER);
		account.setPassword(bcryptPasswordEncoder.encode(account.getPassword()));

		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		account.setCreateDate(simpleDateFormat.format(date));
		accountRepo.save(account); // 만약 db가 꺠져서 저장이 안되던가 하는 상황에서는 에러처리를 어떻게 해야하는지 jpa search
		return "success";
	}

	@Override
	public String emailCheck(String email) {
		Optional<Account> optional = accountRepo.findAccountByEmail(email);
		if (optional.isPresent()) {
			return "fail";
		} else {
			return "success"; // 해당부분에 대해서는 예외처리로
		}
	}

	@Override
	public String emailAuthSend(String email) {

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
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "fail";
	}

	@Override
	public String emailAuthCheck(String email, String authNum) { // 여기서 fail 뜨면 시간 만료된거니까 다시 인증코드 보내라하세염
		/*
		 * redis 에서 가져와서 같은 값인지 검증하는 부분
		 */
		System.out.println(email);
		System.out.println(authNum);
		ValueOperations<String, Object> vop = redisTemplate.opsForValue();
		String key = email + "-auth";

		Token token = (Token) vop.get(key);
		if (token != null) {
			String value = jwtTokenUtil.getEmailAuthNumFromToken(token.getToken());
			if (authNum.equals(value)) {
				return "success";
			}
		}
		
		return "fail";
	}

}