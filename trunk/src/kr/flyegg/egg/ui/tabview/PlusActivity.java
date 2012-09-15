package kr.flyegg.egg.ui.tabview;

import kr.flyegg.egg.R;
import kr.flyegg.egg.dao.Category;
import kr.flyegg.egg.dao.CategoryAccesser;
import kr.flyegg.egg.ui.CardBoard;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class PlusActivity extends Activity {

	//이거 안씀 나중에 정리할게~
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		callAlert();
	}

	
	private void save(String category) {
		//save category
		CategoryAccesser accesser = new CategoryAccesser(getApplicationContext());
		accesser.insert(new Category(category));
		
		closeDialog();
		returnActivity();
		
	}

	private void returnActivity() {
		Intent i = new Intent(getApplicationContext(), CardBoard.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);
	}

	private void closeDialog() {
		if(dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
	}
	
	private AlertDialog dialog = null;
	
	@SuppressLint("NewApi")
	private void callAlert() {
		try {
		 LayoutInflater factory = LayoutInflater.from(this);
         final View textEntryView = factory.inflate(R.layout.alert_dialog_text_entry, null);
         dialog = new AlertDialog.Builder(PlusActivity.this)
             //.setIconAttribute(android.R.attr.alertDialogIcon)
             .setTitle(R.string.alert_dialog_text_entry)
             .setView(textEntryView)
             .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                 public void onClick(DialogInterface dialog, int whichButton) {
                	 //db에 카테고리 저장하기
                	 save(getString(R.id.category_name_edit));
                 }
             })
             .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                 public void onClick(DialogInterface dialog, int whichButton) {
                	 //취소
                	 dialog.cancel();
                	 returnActivity();
                 }
             })
             .create();
         
         dialog.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	protected void onDestroy() {
		closeDialog();
		
		super.onDestroy();
	}
	
}
