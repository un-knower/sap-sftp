package com.hcicloud.sap.vo;

public class AnnoyModel {

	private String voiceId;
	private String staffId;
	private String platForm;
	private String annoyType;
	private String startTime;
	private String endTime;
	private String callPhone;
	/*
	 *追加骚扰详细和服务态度字段
	 *20170626
	 */
	private String seatAttitude;
	public String getCallPhone() {
		return callPhone;
	}
	public void setCallPhone(String callPhone) {
		this.callPhone = callPhone;
	}
	public String getVoiceId() {
		return voiceId;
	}
	public void setVoiceId(String voiceId) {
		this.voiceId = voiceId;
	}
	public String getStaffId() {
		return staffId;
	}
	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}
	public String getPlatForm() {
		return platForm;
	}
	public void setPlatForm(String platForm) {
		this.platForm = platForm;
	}
	public String getAnnoyType() {
		return annoyType;
	}
	public void setAnnoyType(String annoyType) {
		this.annoyType = annoyType;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getSeatAttitude() {
		return seatAttitude;
	}
	public void setSeatAttitude(String seatAttitude) {
		this.seatAttitude = seatAttitude;
	}
	
}
