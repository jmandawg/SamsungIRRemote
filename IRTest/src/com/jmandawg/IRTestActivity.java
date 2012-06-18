package com.jmandawg;

import java.io.File;
import java.util.Iterator;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import com.jmandawg.ircomm.IIRComm;
import com.jmandawg.ircomm.SamsungIRComm;
import com.jmandawg.popups.TestProntoPopup;
import com.jmandawg.utils.Utils;

public class IRTestActivity extends Activity {
	
	private static final int REMOTES_MENU_GROUP = 777;
	
	JSONObject currentRemote = null;
	String defaultRemote;
    private static boolean toggle = false;
    private static IIRComm irComm;
    private static Context mContext;
    
    
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mContext = this;
        irComm = new SamsungIRComm();
        
    	defaultRemote = getResources().getString(R.string.mce);
    	if(currentRemote == null)
        	loadCurrentRemote(defaultRemote);
    }
    
   
	private void loadAllRemotes(SubMenu remotesMenu)
    {
		try {
			int j;
			String[] files = getAssets().list("codes");
			//JSONObject remotes = Utils.getJSONObjectFromFile(getResources().getString(R.string.remotesJSONFilePath));
			//Iterator<String> keys = remotes.keys();
			//int i=0;
			//while(keys.hasNext())
			for(j=0;j<files.length;j++)
	    	{
				//String key = keys.next();
				String key = files[j].replace("_", " ");
				MenuItem mi = remotesMenu.add(REMOTES_MENU_GROUP, j, j, key);
				if(key.equals(defaultRemote))
				{
					mi.setChecked(true);
				}
				//i++;
	    	}
			
			//Now check the SD Card folder
			String path = Environment.getExternalStorageDirectory().getAbsolutePath();
			File remotesDir = new File(path + "/remotes");
			if(remotesDir.exists())
			{
				String[] children = remotesDir.list();
				if (children != null) {
				    for (int i=0; i<children.length; i++) {
				        // Get filename of file or directory
				    	j++;
				        String filename = children[i].replace("_"," ");
				        MenuItem mi = remotesMenu.add(REMOTES_MENU_GROUP, j, j, filename);
						if(filename.equals(defaultRemote))
						{
							mi.setChecked(true);
						}
				    }
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    private void loadCurrentRemote(String name) {
    	//JSONObject remotes = 
    	 try {
    		
			//currentRemote =  Utils.getJSONObjectFromFile("codes/" + name.replace(" ", "_")).getJSONObject(name);
			currentRemote =  Utils.getJSONObjectFromFile(name).getJSONObject(name);
			//remotes.getJSONObject(name);
			setTitle(name);
    	 } 
    	 catch (Exception e) {
 			e.printStackTrace();
 		}
    	
    }
    /** Called when the user selects the Send button */
    public void onClick(View view) {
    	//Button b = (Button)view.getId();
    	try {
    		
			String irCode;
    		boolean enableToggle = currentRemote.getBoolean("toggle");
			
    		if(enableToggle)
				irCode = currentRemote.getString(view.getTag().toString() + (toggle?"T":""));
			else
				irCode = currentRemote.getString(view.getTag().toString());
    		
    		sendIRCode(irCode);
    		
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public void optionsClick(View view) {
    	this.openOptionsMenu();
	}
    
    public static void sendIRCode(String irCode)
    {
    	if(irCode != null)
    	{
    		if(irCode.startsWith("0000 "))
    			irCode = Utils.convertProntoHexStringToIntString(irCode);
    		irComm.sendIRCode(irCode);	
    		toggle = !toggle;
    	}
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	
    	//MenuItem miChooseRemote = menu.add(R.string.chooseRemote);
       SubMenu smRemotes = menu.addSubMenu(R.string.chooseRemote);
       loadAllRemotes(smRemotes);
       smRemotes.setGroupCheckable(REMOTES_MENU_GROUP, true, true);
   
        MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.options_menu, menu);
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
 		if(item.getGroupId() == REMOTES_MENU_GROUP)
 		{
 			item.setChecked(true);
 			loadCurrentRemote(item.getTitle().toString());
 			return true;
 		}
        switch (item.getItemId()) {
	        case R.id.testPronto:
	        	TestProntoPopup alert = new TestProntoPopup(this);
	        	alert.show();
	        	return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    

    public static Context getContext() {
		return mContext;
	}
    
}