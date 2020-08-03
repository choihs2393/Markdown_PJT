package com.ggbg.note.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account")
public class Account implements Persistable<String>{
	@Id
	@Column(nullable = false, name = "account_email")
	private String email;

	@Column(nullable = false, name = "account_name")
	private String name;

	@Column(nullable = false, name = "account_password")
	private String password;

	@Column(nullable = false, name = "account_role")
	private Role role;

	@Column(nullable = false, name = "account_create_date")
	private String createDate;
	
	public String getRoleKey() {
		return this.role.getKey();
	}

	@Override
	public String getId() {
		return this.email;
	}

	@Override
	public boolean isNew() {
		return false;
	}

}
