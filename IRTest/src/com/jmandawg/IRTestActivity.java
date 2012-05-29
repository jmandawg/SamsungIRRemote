package com.jmandawg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Method;

import org.apache.http.entity.InputStreamEntity;
import org.json.JSONObject;

import android.R.bool;
import android.R.integer;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class IRTestActivity extends Activity {
	
	Object irService;
	Class irClass;
	Method sendIR;
	Method readIR;
	JSONObject codes;
    boolean toggle = false;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        try {
        	
		InputStream is = getAssets().open("codes/Sharp.json");
		BufferedReader r = new BufferedReader(new InputStreamReader(is));
		StringBuilder total = new StringBuilder();
		String line;
		while ((line = r.readLine()) != null) {
		    total.append(line);
		}
		r.close();
		is.close();
		codes = new JSONObject(total.toString());
		
        irService = getSystemService("irda");
    	irService.getClass();
    	irClass = irService.getClass();
    	
    		readIR = irClass.getMethod("read_irsend");
			sendIR = irClass.getMethod("write_irsend", new Class[]{String.class});
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    /** Called when the user selects the Send button */
    public void onClick(View view) {
    	//Button b = (Button)view.getId();
    	try {
			String irCode = codes.getJSONObject("MCE").getString(view.getTag().toString() + (toggle?"T":""));
			sendIR.invoke(irService, irCode);
			toggle = !toggle;
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public void onClick3(View view) {
    	
    	//Build the IR String
		String hexCode = "0000 0071 0000 0020 0061 0021 0010 0011 0010 0011 0010 0021 0010 0021 0030 0021 0010 0011 0010 0011 0010 0011 0010 0011 0010 0011 0010 0011 0010 0011 0010 0011 0010 0011 0010 0011 0020 0011 0010 0011 0010 0011 0010 0021 0010 0011 0020 0011 0010 0021 0020 0021 0010 0011 0010 0011 0010 0011 0010 0011 0010 0011 0020 0011 0010 0021 0020 09EB";
		String[] codes = hexCode.split(" ");
		StringBuilder param = new StringBuilder("38400,");
		for(int i=0;i<codes.length;i++)
			param.append(convertHexStringToIntString(codes[i]) + ",");
    	
    	String mycode = param.substring(0, param.length() - 1);
    	
    	try {
    		//public java.lang.String android.app.IrdaManager.read_irsend() throws android.os.RemoteException
			
			String result = readIR.invoke(irService).toString();
			//for(int i=0;i<2;i++)
			//sendIR.invoke(irService, "37300,11,67,10,28,10,28,10,27,11,27,11,27,11,67,11,67,11,27,11,68,10,27,11,27,11,27,11,67,11,27,11,1731,11,68,10,28,10,27,11,27,11,27,11,67,11,27,11,27,11,67,11,27,11,67,11,67,11,68,10,27,11,68,10,1653," + toggle);
			if(toggle)
				sendIR.invoke(irService, "36000,96,32,16,16,16,16,16,32,16,32,48,32,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,32,16,16,16,16,16,16,32,16,16,16,16,16,16,16,16,32,32,16,16,16,16,16,16,16,16,16,16,16,16,32,16,16,16,16,2492");
			else
				sendIR.invoke(irService, "36000,96,32,16,16,16,16,16,32,16,32,48,32,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,16,32,16,16,16,16,16,16,16,16,32,16,16,16,16,16,16,32,32,16,16,16,16,16,16,16,16,16,16,16,16,32,16,16,16,16,2492");
			toggle = !toggle;
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    
    private String convertHexStringToIntString(String s)
    {
    	Integer i = Integer.parseInt(s, 16);
    	return i.toString(); 
    }
    
}