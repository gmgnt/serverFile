package org.ksj.server.file.agent.exception;

public class AgentException extends RuntimeException {

	private static final long serialVersionUID = 3654942167378540966L;
	
	private int code;
	
	public AgentException(AgentExceptionCode agnetExceptionCode) {
		super(agnetExceptionCode.msg);
		this.code = agnetExceptionCode.code;
	}
	
	public AgentException(AgentExceptionCode agnetExceptionCode, Throwable t) {
		super(agnetExceptionCode.msg, t);
		this.code = agnetExceptionCode.code;
	}
	
	public int getCode() {
		return this.code;
	}
 
}
