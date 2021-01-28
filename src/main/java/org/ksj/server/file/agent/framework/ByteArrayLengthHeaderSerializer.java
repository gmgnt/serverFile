package org.ksj.server.file.agent.framework;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.ksj.server.file.agent.util.ConvertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ByteArrayLengthHeaderSerializer
		extends org.springframework.integration.ip.tcp.serializer.ByteArrayLengthHeaderSerializer {
	private static final Logger LOGGER = LoggerFactory.getLogger(ByteArrayLengthHeaderSerializer.class);

	private int headerSize;

	@Override
	protected int readHeader(InputStream inputStream) throws IOException {
		byte[] length = new byte[headerSize];

		int status = read(inputStream, length, true);

//		if (status < 0) {
//			LOGGER.error("ByteArrayLengthHeaderSerializer read status: {}", status);
//			// TODO 에러 정제
//			throw new RuntimeException("status Error");
//		}

		int messageLength = ConvertUtil.byteArrayToInt(length);
		LOGGER.debug("read messageLength: {}", messageLength);

		return messageLength;
	}

	@Override
	protected void writeHeader(OutputStream outputStream, int length) throws IOException {
		int len = length;

		String lengthPart = String.format("%0" + this.headerSize + "d", len);

		LOGGER.debug("writeHeader Input Length: {}", len);
		LOGGER.debug("writeHeader write Length value: {}", lengthPart);

		outputStream.write(lengthPart.getBytes());
	}

	public void setHeaderSize(int headerSize) {
		this.headerSize = headerSize;
	}

}
