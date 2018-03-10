package com.demo.model;

import java.io.Serializable;

import com.demo.anno.AnnoField;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TestModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userId;

    @JsonProperty("user_name")
    private String userName;
    
    @AnnoField
    @JsonProperty("nick_name")
    private String nickName;


    public TestModel() {
        super();
    }

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
}
