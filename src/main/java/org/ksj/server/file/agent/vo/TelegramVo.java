package org.ksj.server.file.agent.vo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.ksj.server.file.agent.util.ConvertUtil;

public class TelegramVo {
	private long size;
	private int inst;
	private List<Byte[]> data = new ArrayList<Byte[]>();
	
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public int getInst() {
		return inst;
	}
	public void setInst(int inst) {
		this.inst = inst;
	}
	public boolean addData(byte[] byteArray) {
		return data.add(ArrayUtils.toObject(byteArray));
	}
	public byte[] getData(int idx) {
		return  ArrayUtils.toPrimitive(data.get(idx));
	}
	public int getDataSize() {
		return data.size();
	}
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("size: ");
		sb.append(size);
		sb.append("\n");
		sb.append("inst: ");
		sb.append(inst);
		sb.append("\n");
		sb.append("data count: ");
		sb.append(data.size());
		sb.append("\n");
		return sb.toString();
	}
	
	
}
