package org.ksj.server.file.agent.exception;

public enum AgentExceptionCode {
	MSG_IS_EMPTY(1000, "Message is empty."),
	MSG_SIZE_IS_SMALL_DEFAULT_SIZE(1001, "Message size is smaller than default size"),
	MSG_SIZE_NOT_MATCHED(1002, "Message size is not matched"),
	MSG_END_NOT_MATCHED(1003, "Message end is not matched");
	
	
	int code;
	String msg;
	
	private AgentExceptionCode(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	
	public int getCode() {
		return code;
	}
}
