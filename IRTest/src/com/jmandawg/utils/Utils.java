package com.jmandawg.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Environment;

import com.jmandawg.IRTestActivity;

public class Utils {

	public static JSONObject getJSONObjectFromFile(String path) {
		try {
			return new JSONObject(getJSONStringFromFile(path));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getJSONStringFromFile(String name) {
		try {
			File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/remotes/" + name.replace(" ", "_"));
			InputStream is;
    		if(f.exists())
    		{
    			
    			is = new FileInputStream(f);
    		}
    		else
    		{
    			is = IRTestActivity.getContext().getAssets().open("codes/" + name.replace(" ", "_"));
    		}
			BufferedReader r = new BufferedReader(new InputStreamReader(is));
			StringBuilder jsonString = new StringBuilder();
			String line;
			while ((line = r.readLine()) != null) {
				jsonString.append(line);
			}
			r.close();
			is.close();
			return jsonString.toString();
		} catch (Exception e) {
			showMsgBox("Error reading file", "Error reading file, check JSON format\n" + name);
			e.printStackTrace();
			return null;
		}
	}

	public static String convertProntoHexStringToIntString(String s) {
		String[] codes = s.split(" ");
		StringBuilder sb = new StringBuilder();
		sb.append(getFrequency(codes[1]) + ",");
		for (int i = 4; i < codes.length; i++) {
			sb.append(Integer.parseInt(codes[i], 16) + ",");
		}
		return sb.toString();
	}

	public static String getFrequency(String s) {
		int val = Integer.parseInt(s, 16);
		Integer i = (int) (1000000 / (val * .241246));
		return i.toString();
	}
	
	public static void showMsgBox(String title, String Msg)
	{
		AlertDialog alertDialog = new AlertDialog.Builder(IRTestActivity.getContext()).create();
		alertDialog.setTitle("Getting Remotes");
		alertDialog.setMessage("Are you sure?");
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
		   public void onClick(DialogInterface dialog, int which) {
		      dialog.dismiss();
		   }
		});
		//alertDialog.setIcon(R.drawable.icon);
		alertDialog.show();
	}
}
