package com.example.demo.sss;

/**
 * Created by zhaohy on 2018/11/7.
 */
public class User implements  java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	private int userId;

	private String userName;

}
