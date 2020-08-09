package com.ggbg.note.domain;

public enum Role {
	USER("ROLE_USER", "user"),
	ADMIN("ROLE_ADMIN","admin");

	private final String key;
	private final String title;
	
	private Role(String key, String title) {
		this.key = key;
		this.title = title;
	}
	public String getKey() {
		return key;
	}
	public String getTitle() {
		return title;
	}
}
