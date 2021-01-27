package org.ksj.server.file.agent.util;

import org.junit.Test;
import org.ksj.server.file.agent.exception.AgentException;
import org.ksj.server.file.agent.exception.AgentExceptionCode;

import org.junit.Assert;

public class TelegramUtilTest {
	// 전체 사이즈 에러 테스트
	@Test
	public void parsing1() {
		String telegram = "000000191000@@";
		try {
			TelegramUtil.parsing(telegram.getBytes());
		} catch(AgentException e) {
			Assert.assertEquals(AgentExceptionCode.MSG_SIZE_NOT_MATCHED.getCode(), e.getCode());
		}
	}

	// END Point 처리
	@Test
	public void parsing2() {
		String telegram = "000000141000@X";

		try {
			TelegramUtil.parsing(telegram.getBytes());
		} catch(AgentException e) {
			Assert.assertEquals(AgentExceptionCode.MSG_END_NOT_MATCHED.getCode(), e.getCode());
		}
	}
}
