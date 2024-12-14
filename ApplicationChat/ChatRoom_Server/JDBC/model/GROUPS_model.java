package model;

import java.sql.Timestamp;

public class GROUPS_model {
	private String groupID;
	private String groupName;
	private int quantityMember;
	private String createBy;
	private Timestamp createTime;

	public GROUPS_model(String groupName, int quantityMember, String createBy, Timestamp createTime) {
		super();
		this.groupName = groupName;
		this.quantityMember = quantityMember;
		this.createBy = createBy;
		this.createTime = createTime;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public int getQuantityMember() {
		return quantityMember;
	}

	public void setQuantityMember(int quantityMember) {
		this.quantityMember = quantityMember;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

}
