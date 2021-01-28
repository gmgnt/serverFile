package org.ksj.server.file.agent.server.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.ksj.server.file.agent.cnst.Cnst;
import org.ksj.server.file.agent.util.ConvertUtil;
import org.ksj.server.file.agent.util.HashUtil;
import org.ksj.server.file.agent.vo.TelegramVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerFileReceive {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServerFileReceive.class);
	
	public TelegramVo fileReceive(String key, TelegramVo input) {
		// 1. 암호화 파일 Hash 추출
		byte[] encFile = input.getData(1);
		
		byte[] encFileHash = HashUtil.sha256(encFile);
		
		// 2. 복호화 수행
		// TODO 복호화 수행
		byte[] decFile = encFile;
		
		// 3. 파일 생성
		String filePath = ConvertUtil.byteArrayToString(input.getData(0));
		
		File file = new File(filePath);
		try(FileOutputStream stream = new FileOutputStream(file)) {
			stream.write(decFile);
		} catch(IOException e) {
			// TODO 에러 정제 필요
			throw new RuntimeException(e);
		}
		
		TelegramVo outVo = new TelegramVo();
		outVo.setInst(Cnst.INST_RES_FILE_SEND);
		outVo.addData(encFileHash);
		
		return outVo;
	}
}
