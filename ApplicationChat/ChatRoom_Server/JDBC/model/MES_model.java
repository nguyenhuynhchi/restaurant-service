package model;

import java.sql.Timestamp;

public class MES_model {
	private String messageID;
	private String senderID;
	private String receiverID;
	private String receiverGroupID;
	private String contentMessage;
	private Timestamp timeReceive;

	public MES_model(String senderID, String receiverID, String receiverGroupID,
			String contentMessage, Timestamp timeReceive) {
		super();
//		this.messageID = messageID;
		this.senderID = senderID;
		this.receiverID = receiverID;
		this.receiverGroupID = receiverGroupID;
		this.contentMessage = contentMessage;
		this.timeReceive = timeReceive;
	}

	public String getMessageID() {
		return messageID;
	}

	public void setMessageID(String messageID) {
		this.messageID = messageID;
	}

	public String getSenderID() {
		return senderID;
	}

	public void setSenderID(String senderID) {
		this.senderID = senderID;
	}

	public String getReceiverID() {
		return receiverID;
	}

	public void setReceiverID(String receiverID) {
		this.receiverID = receiverID;
	}

	public String getReceiverGroupID() {
		return receiverGroupID;
	}

	public void setReceiverGroupID(String receiverGroupID) {
		this.receiverGroupID = receiverGroupID;
	}

	public String getContentMessage() {
		return contentMessage;
	}

	public void setContentMessage(String contentMessage) {
		this.contentMessage = contentMessage;
	}

	public Timestamp getTimeReceive() {
		return timeReceive;
	}

	public void setTimeReceive(Timestamp timeReceive) {
		this.timeReceive = timeReceive;
	}

}
