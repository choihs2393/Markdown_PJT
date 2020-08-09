package com.ggbg.note.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/accountBand")
@RestController
public class AccountBandController {
/*
 * band에 회원 invite 하는 기능에서 필요한 거는

band_pk 하나 던져주면 그 안에 있는 회원들의 pk, 닉네임, 이메일, 권한을 return

email이랑 band_pk 하나씩 던져주면 accont_band테이블에 그 회원이랑 그룹 넣어주고, 그 회원의 pk, 닉네임, 이메일, 권한 return

email 넣었을 때 account_TB 내에 있는 회원인지 확인

band에서 회원 내보내는 기능에서 필요한거는

account_pk 랑 band_pk 던져주면, account_band테이블에서 row 삭제 후, band테이블에서 number-1 해주세용. 아무것도 return 안해줘도 됨.
 */
	
}
