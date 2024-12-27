package model;

import java.sql.Timestamp;

public class USERS_model {
	private String userID;
	private String userName;
	private String fullName;
	private String password;
	private Timestamp createTime;

	public USERS_model(String userID, String userName, String fullName, String password, Timestamp createTime) {
		super();
		this.userID = userID;
		this.userName = userName;
		this.fullName = fullName;
		this.password = password;
		this.createTime = createTime;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	
}
