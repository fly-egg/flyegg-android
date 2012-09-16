package kr.flyegg.egg.ui.tabview;


import kr.flyegg.egg.R;
import kr.flyegg.egg.ui.AddCardMain;
import kr.flyegg.egg.ui.CardBoard;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class AllOfAllActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tabs);
		
		GridView g = (GridView) findViewById(R.id.myGrid);
        g.setAdapter(new AppsAdapter());
		
        Button plus = (Button) findViewById(R.id.tags_plus);
        plus.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				insertCategoryAlert();
			}
		});
        
        Button select = (Button) findViewById(R.id.select);
        select.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				closeDialog();
				finish();
			}
		});
        
	}

	
	private void closeDialog() {
		if(dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
	}
	
	private AlertDialog dialog = null;
	
	@SuppressLint("NewApi")
	private void insertCategoryAlert() {
		try {
			
		 LayoutInflater factory = LayoutInflater.from(this);
         final View textEntryView = factory.inflate(R.layout.alert_dialog_text_entry, null);
         dialog = new AlertDialog.Builder(AllOfAllActivity.this)
             .setTitle(R.string.alert_dialog_text_entry)
             .setView(textEntryView)
             .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            	 
                 public void onClick(DialogInterface dialog, int whichButton) {
                	 dialog.cancel();
                 }
             })
             .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                 public void onClick(DialogInterface dialog, int whichButton) {
                	 //취소
                	 dialog.cancel();
                 }
             })
             .create();
         
         dialog.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}

    private final Integer[] mImageIds = {
            R.drawable.banana,
            R.drawable.grape,
            R.drawable.kiwi,
            R.drawable.pineapp,
            R.drawable.watermelon,
            R.drawable.card_frame
    };
	
    private final String[] mStrIds = {
            "바나나",
            "포도",
            "키위",
            "파인애플",
            "수박",
            "카드추가"
    };
    
    public void startaddCard() {
    	Intent i = new Intent(getApplicationContext(), AddCardMain.class);
    	startActivity(i);
    }
    
	public class AppsAdapter extends BaseAdapter {
        public AppsAdapter() {
        }

        public View getView(int position, View convertView, ViewGroup parent) {
        	CheckableLayout l;
        	
            ImageView i;
            TextView tv;
            
            if (convertView == null) {
            	
                tv = new TextView(getApplicationContext());
                tv.setTextSize(50);
                tv.setPadding(0, 400, 0, 0);
                
                i = new ImageView(AllOfAllActivity.this);
                i.setScaleType(ImageView.ScaleType.FIT_CENTER);
                i.setLayoutParams(new ViewGroup.LayoutParams(400, 400));
                l = new CheckableLayout(AllOfAllActivity.this);
                l.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT,
                        GridView.LayoutParams.WRAP_CONTENT));
                l.addView(i);
                l.addView(tv);

                
            } else {
                l = (CheckableLayout) convertView;
            	i = (ImageView) l.getChildAt(0);
            	tv = (TextView) l.getChildAt(1);
            }

            i.setImageResource(mImageIds[position]);
            tv.setText(mStrIds[position]);
            if(position == 5) {
            	tv.setOnClickListener(new OnClickListener() {
					
					public void onClick(View v) {
						startaddCard();
					}
				});
            }
            
            return l;
        }


        public final int getCount() {
            return mImageIds.length;
        }

        public final Object getItem(int position) {
            return position;
        }

        public final long getItemId(int position) {
            return position;
        }
    }

    public class CheckableLayout extends FrameLayout implements Checkable {
        private boolean mChecked;

        public CheckableLayout(Context context) {
            super(context);
        }

        public void setChecked(boolean checked) {
            mChecked = checked;
            setBackgroundDrawable(checked ?
                    getResources().getDrawable(R.drawable.blue)
                    : null);
        }

        public boolean isChecked() {
            return mChecked;
        }

        public void toggle() {
            setChecked(!mChecked);
        }

    }
	
	

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
