package model;

public class GROUPMEMBERS_model {
	private String groupMemberID;
	private String groupID;
	private String userID;

	public GROUPMEMBERS_model(String groupMemberID, String groupID, String userID) {
		super();
		this.groupMemberID = groupMemberID;
		this.groupID = groupID;
		this.userID = userID;
	}

	public String getGroupMemberID() {
		return groupMemberID;
	}

	public void setGroupMemberID(String groupMemberID) {
		this.groupMemberID = groupMemberID;
	}

	public String getGroupID() {
		return groupID;
	}

	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

}
