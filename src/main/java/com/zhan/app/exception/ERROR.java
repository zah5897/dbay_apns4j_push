package com.zhan.app.exception;

public enum ERROR {
	ERR_FAILED(-1, "操作失败"),ERR_NO_ERR(0, "操作成功"), ERR_NO_LOGIN(1, "当前账号没登录"), ERR_PARAM(2, "参数异常:"), ERR_SYS(3, "系统错误"), ERR_USER_EXIST(4,
			"该用户已存在"), ERR_FILE_UPLOAD(5, "文件上传失败"), ERR_USER_NOT_EXIST(6, "用户不存在"),ERR_PASSWORD(7, "密码错误"),ERR_FREUENT(8, "操作频繁"),ERR_NO_AGREE(9, "非法请求");
	private int value;
	private String errorMsg;

	private ERROR(int code, String errorMsg) {
		this.value = code;
		this.errorMsg = errorMsg;
	}

	public int getValue() {
		return value;
	}

	public ERROR setNewText(String text) {
		errorMsg += text;
		return this;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
