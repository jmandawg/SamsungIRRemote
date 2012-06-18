package com.jmandawg.popups;

import com.jmandawg.IRTestActivity;
import com.jmandawg.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

public class TestProntoPopup extends AlertDialog.Builder implements DialogInterface.OnClickListener {

	EditText input;

	public TestProntoPopup(Context context) {
		super(context);
		input = new EditText(context);
		input.setText("0000 006D 0000 0020 000A 0046 000A 001E 000A 001E 000A 001E 000A 001E 000A 001E 000A 0046 000A 0046 000A 001E 000A 0046 000A 001E 000A 001E 000A 001E 000A 0046 000A 001E 000A 0679 000A 0046 000A 001E 000A 001E 000A 001E 000A 001E 000A 0046 000A 001E 000A 001E 000A 0046 000A 001E 000A 0046 000A 0046 000A 0046 000A 001E 000A 0046 000A 0679");
		setTitle("Enter Pronto Code");
		setView(input);
		setPositiveButton("Send Test", this);
		setNegativeButton("Cancel", this);

		// TODO Auto-generated constructor stub
	}

	public void onClick(DialogInterface dialog, int whichButton) {
		if(whichButton == -1)
		{
			String value = input.getText().toString();
			IRTestActivity.sendIRCode(value);
			AlertDialog.Builder a = new AlertDialog.Builder(this.getContext());
			a.setTitle("Code Sent");
			a.setPositiveButton("ok", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface d, int id) {
		                d.dismiss();
		           }
		       });
			a.show();
		}
		else
		{
			dialog.cancel();
		}
	}
}
