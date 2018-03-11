package com.demo.model;

public class WebServiceFeedback {

	//BOX_METADATA_COLLABORATE
	public static final String BOX_COLLABORATE_CREATE = "/collaborate/create"; //
	public static final String BOX_COLLABORATE_MEMBER_ADD = "/collaborate/addmember"; //
	public static final String BOX_COLLABORATE_ACCEPTED_UPDATE = "/collaborate/update_member_accepted"; //
	public static final String BOX_COLLABORATE_DELETE = "/collaborate/delete";
	public static final String BOX_COLLABORATE_MEMBER_DELETE = "/collaborate/deletemember";
	public static final String BOX_COLLABORATE_INFO = "/collaborate/info";
	public static final String BOX_COLLABORATE_LIST = "/collaborate/list";
		
	//BOX_METADATA_USER
	public static final String BOX_USER_INFO = "/user/info"; //
	
	//BOX_METADATA_FILE
	public static final String BOX_FILE_CREATE = "/file/create"; //
	
	//BOX_METADATA_FOLDER
	public static final String BOX_FOLDER_INFO = "/folder/info"; 
	public static final String BOX_FOLDER_CREATE = "/folder/create"; 
	
	//BOX_GATEWAY_FILE_UPLOAD
	public static final String BOX_FILE_UPLOAD = "/file";
	
	//SSO_USER
	public static final String SSO_USER_INFO_SINGLE = "/user/user_info"; //
	public static final String SSO_USER_INFO_MULTIPLE = "/user/info"; //
	
	//SSO_DEVICE
	public static final String SSO_DEVICES_INFO = "/devices/info"; //
	
	//callback_name
	public static final String BOX_COLLABORATE_CALLBACK = "BoxCollaborateControlService";
	
	private String action;
	private String status;
	private Object detail;
	
	public WebServiceFeedback() {
		
	}
	
	public WebServiceFeedback(String status) {
		this.status = status;
	}
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Object getDetail() {
		return detail;
	}
	public void setDetail(Object detail) {
		this.detail = detail;
	}
	
	
}
