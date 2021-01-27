package org.ksj.server.file.agent.util;

import java.util.Arrays;

import org.ksj.server.file.agent.exception.AgentException;
import org.ksj.server.file.agent.exception.AgentExceptionCode;
import org.ksj.server.file.agent.vo.TelegramVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TelegramUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(TelegramUtil.class);
	
	private static final int TELEGRAM_DEFAULT_SIZE = 14;
	private static final int TELEGRAM_SIZE = 8;
	private static final int TELEGRAM_INST_SIZE = 4;
	private static final int TELEGRAM_DATA_SIZE = 8;
	private static final String END = "@@";
	
	/**
	 * 전문byte를 TelegramVo로 변경
	 * @param input
	 * @return
	 */
	public static TelegramVo parsing(byte[] input) {
		// 전문 체크
		check(input);
		
		TelegramVo inputVo = parse(input);
		
		return inputVo;
	}
	
	/**
	 * TelegramVo를 byte[]로 변경
	 * @param outVo
	 * @return byte배열의 전문
	 */
	public static byte[] makeTelegram(TelegramVo outVo) {
		// 전체 사이즈 계산
		int telegramSize = 0;
		telegramSize += TELEGRAM_SIZE;
		telegramSize += TELEGRAM_INST_SIZE;
		for(int i = 0 ; i <outVo.getDataSize() ; ++i) {
			telegramSize += TELEGRAM_DATA_SIZE;
			telegramSize += outVo.getData(i).length;
		}
		telegramSize += END.length();
		
		byte[] retByteArray = new byte[telegramSize];
		
		// 전문 조립
		int offset = 0;
		
		// 전체사이즈 계산
		copyByteArray(retByteArray, makeTelegram(telegramSize, TELEGRAM_SIZE), 0);
		offset += TELEGRAM_SIZE;
		// 명령어부 조립
		copyByteArray(retByteArray, makeTelegram(outVo.getInst(), TELEGRAM_INST_SIZE), offset);
		offset += TELEGRAM_INST_SIZE;
		// 개별부 조립
		for(int i = 0 ; i <outVo.getDataSize() ; ++i) {
			copyByteArray(retByteArray, makeTelegram(outVo.getData(i).length, TELEGRAM_DATA_SIZE), offset);
			offset += TELEGRAM_DATA_SIZE;
			copyByteArray(retByteArray, outVo.getData(i), offset);
			offset += outVo.getData(i).length;
		}
		// 종료부 조립
		copyByteArray(retByteArray, END, offset);
		
		
		return retByteArray;
		
		
		
	}
	
	private static byte[] copyByteArray(byte[] array, String val, int offset) {
		System.arraycopy(val.getBytes(), 0, array, offset, val.getBytes().length);
		
		return array;
	}
	
	private static byte[] copyByteArray(byte[] array, byte[] source, int offset) {
		System.arraycopy(source, 0, array, offset, source.length);
		
		return array;
	}
	
	/**
	 * 전문에 대한 초기 Validation 수행
	 * @param byteArray 전문Input
	 * @exception AgentException 발생
	 */
	private static void check(byte[] byteArray) {
		// Zero 체크
		if (byteArray == null || byteArray.length == 0) {
			throw new AgentException(AgentExceptionCode.MSG_IS_EMPTY);
		}
		
		// 기본사이즈와 비교
		if(byteArray.length < TELEGRAM_DEFAULT_SIZE) {
			LOGGER.error("byteArray length is {}. But default size is {}", byteArray.length, TELEGRAM_DEFAULT_SIZE);
			throw new AgentException(AgentExceptionCode.MSG_SIZE_IS_SMALL_DEFAULT_SIZE);
		}
		
		// 종료부 비교
		String end = ConvertUtil.byteArrayToString(Arrays.copyOfRange(byteArray, byteArray.length-2, byteArray.length));
		if(!END.equals(end)) {
			LOGGER.error("endPoint is not equals. expected: {}, real: {}", END, end);
			throw new AgentException(AgentExceptionCode.MSG_END_NOT_MATCHED);
		}
		
	}
	
	private static TelegramVo parse(byte[] input) {
		int offset = 0;
		TelegramVo inputVo = new TelegramVo();
		// 전체자릿수 parsing
		long size = ConvertUtil.byteArrayToLong(Arrays.copyOfRange(input, offset, offset + TELEGRAM_SIZE));
		LOGGER.debug("전체크기: {}", size);
		inputVo.setSize(size);
		offset += TELEGRAM_SIZE;
		
		// 전체크기 검증
		if(size != input.length) {
			LOGGER.error("Telegram size: {}, but real size: {}", size, input.length);
			throw new AgentException(AgentExceptionCode.MSG_SIZE_NOT_MATCHED);
		}
		
		// 명령어 parsing
		int inst = ConvertUtil.byteArrayToInt(Arrays.copyOfRange(input, offset, offset + TELEGRAM_INST_SIZE));
		inputVo.setInst(inst);
		offset += TELEGRAM_INST_SIZE;
		
		// TODO INST 명령어에 포함되어 있는지 확인
		
		// 데이터 파싱
		while(inputVo.getSize() != (offset + END.length()) ) {
			int dataSize = ConvertUtil.byteArrayToInt(Arrays.copyOfRange(input, offset, offset + TELEGRAM_DATA_SIZE));
			offset += TELEGRAM_DATA_SIZE;
			byte[] data = Arrays.copyOfRange(input, offset, offset + dataSize);
			offset += dataSize;
			inputVo.addData(data);
		}
		
		return inputVo;
	}
	
	/**
	 * int 형태의 값을 전문으로 변환시 "0" Append 처리하여 String으로 변환하는 로직
	 * 
	 * @param value int값
	 * @param size 실제 전문 사이즈
	 * @return 전문형태로 변경된 size
	 * @throws value의 size가 실제 전문 사이즈를 넘어서면 오류가 발생함
	 */
	private static String makeTelegram(int value, int size) {
		String val = String.valueOf(value);
		
		if(val.length() > size) {
			//TODO 에러 정제 필요
			throw new RuntimeException("Size Over");
		}
		
		int appendCnt = size - val.length();
		StringBuffer sb = new StringBuffer();
		for(int i = 0 ; i < appendCnt ; ++i) {
			sb.append("0");
		}
		sb.append(val);
		
		return sb.toString();
	}
}
