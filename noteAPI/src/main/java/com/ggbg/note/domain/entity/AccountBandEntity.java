package com.ggbg.note.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.domain.Persistable;

import com.ggbg.note.domain.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "account_band")
public class AccountBandEntity implements Persistable<String>{
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, name = "ab_no")
	private int no;
	
	@Column(nullable = false, name = "band_no")
	private int bandNo;

	@Column(nullable = false, name = "account_no")
	private int accountNo;

	@Column(nullable = false, name = "account_status")
	private int status;


	@Override
	public String getId() {
		return this.no + "";
	}

	@Override
	public boolean isNew() {
		return false;
	}

}
