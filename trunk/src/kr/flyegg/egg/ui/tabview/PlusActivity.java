package kr.flyegg.egg.ui.tabview;

import kr.flyegg.egg.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class PlusActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		callAlert();
	}

	
	AlertDialog dialog = null;
	@SuppressLint("NewApi")
	private void callAlert() {
		
		 LayoutInflater factory = LayoutInflater.from(this);
         final View textEntryView = factory.inflate(R.layout.alert_dialog_text_entry, null);
         dialog = new AlertDialog.Builder(PlusActivity.this)
             //.setIconAttribute(android.R.attr.alertDialogIcon)
             .setTitle(R.string.alert_dialog_text_entry)
             .setView(textEntryView)
             .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                 public void onClick(DialogInterface dialog, int whichButton) {
 
                     /* User clicked OK so do some stuff */
                 }
             })
             .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                 public void onClick(DialogInterface dialog, int whichButton) {

                     /* User clicked cancel so do some stuff */
                 }
             })
             .create();
         
         dialog.show();
		
	}







	@Override
	protected void onDestroy() {
		if(dialog != null)
			dialog = null;
		
		super.onDestroy();
	}

	
	
}
