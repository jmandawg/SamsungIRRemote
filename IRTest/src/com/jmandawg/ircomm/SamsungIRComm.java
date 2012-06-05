package com.jmandawg.ircomm;

import java.lang.reflect.Method;

import com.jmandawg.IRTestActivity;

public class SamsungIRComm implements IIRComm {

	private Object irService;
	private Class irClass;
	private Method sendIR;
	private Method readIR;
	
	public SamsungIRComm() {
		try{
			irService = IRTestActivity.getContext().getSystemService("irda");
	    	irService.getClass();
	    	irClass = irService.getClass();
		
			readIR = irClass.getMethod("read_irsend");
			sendIR = irClass.getMethod("write_irsend", new Class[]{String.class});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendIRCode(String irCode) {
		try {
			sendIR.invoke(irService, irCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String readIRCode() {
		try {
			return readIR.invoke(irService).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
