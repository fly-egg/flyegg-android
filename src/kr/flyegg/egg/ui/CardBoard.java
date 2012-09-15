package kr.flyegg.egg.ui;


import java.util.List;

import kr.flyegg.egg.R;
import kr.flyegg.egg.dao.Category;
import kr.flyegg.egg.dao.CategoryAccesser;
import kr.flyegg.egg.ui.tabview.AllOfAllActivity;
import kr.flyegg.egg.ui.tabview.PlusActivity;
import kr.flyegg.egg.ui.tabview.TabViewActivity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.nfc.TagLostException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CardBoard extends TabActivity implements TabHost.TabContentFactory {

	TabHost tabHost = null;
	
	private int count;
	//sticker images array
	private Bitmap[] thumbnails;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.card_board);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		tabHost = getTabHost();
		TabHost.TabSpec spec = null;
	
		
		//무조건 들어가야하는 전체 탭
		tabHost.addTab(tabHost.newTabSpec(getString(R.string.all_tab))
                .setIndicator(getString(R.string.all_tab))
                .setContent(new Intent(CardBoard.this, AllOfAllActivity.class)));
		
		//여기서부터 동적으로 동작하기
		setCategory(tabHost);
		
		tabHost.addTab(tabHost.newTabSpec(getString(R.string.plus))
                .setIndicator(getString(R.string.plus))
                .setContent(CardBoard.this));
//                .setContent(new Intent(this, PlusActivity.class)));
		
		tabHost.getTabWidget().getChildAt(tabHost.getTabWidget().getChildCount()-1).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				if(dialog == null) {
					//10 개 이상일 때 개수 세서 보여주기 toast 띄우던가
					insertCategoryAlert();
				}
			}
		});
		
		
		tabHost.setCurrentTab( 0 );
		
		
		for(int tab=0; tab < tabHost.getTabWidget().getChildCount(); tab++) {
			//size 정하기
			tabHost.getTabWidget().getChildAt(tab).getLayoutParams().width = 150;

			//0은 전체라서 지워지면 안된다.
			if(tab != 0) {
				//길게 누를 때 나오는 팝업 설정하기
				tabHost.getTabWidget().getChildAt(tab).setOnLongClickListener(new OnLongClickListener() {
					public boolean onLongClick(View v) {
						
						return false;
					}
				});
			}
		}
	}

	
	
    @Override
	protected void onStart() {
		super.onStart();
	}


	private void setCategory(TabHost tabHost2) {
    	
    	//db에서 list를 가져온다.
    	CategoryAccesser accesser = new CategoryAccesser(getApplicationContext());
    	List<Category> list = accesser.getCategory();
    	
    	for(Category c : list) {
    		
    		Intent tab_intent = new Intent(CardBoard.this, TabViewActivity.class);
    		tab_intent.putExtra(TabViewActivity.PROROSAL, c.getCategory());
    		
    		
    		tabHost.addTab(tabHost.newTabSpec(c.getCategory())
                    .setIndicator(c.getCategory())
                    .setContent(tab_intent));
//                    .setContent(new Intent(this, PlusActivity.class)));
    	}
	}

	
	private AlertDialog dialog = null;
	
	@SuppressLint("NewApi")
	private void insertCategoryAlert() {
		try {
			
		 LayoutInflater factory = LayoutInflater.from(this);
         final View textEntryView = factory.inflate(R.layout.alert_dialog_text_entry, null);
         dialog = new AlertDialog.Builder(CardBoard.this)
             .setTitle(R.string.alert_dialog_text_entry)
             .setView(textEntryView)
             .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            	 
                 public void onClick(DialogInterface dialog, int whichButton) {
                	 EditText text = (EditText) textEntryView.findViewById(R.id.category_name_edit);
                	 String a = text.getText().toString() ;
                	 save(a);
                 }
             })
             .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                 public void onClick(DialogInterface dialog, int whichButton) {
                	 //취소
                	 dialog.cancel();
                	 restart();
                 }
             })
             .create();
         
         dialog.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	@SuppressLint("NewApi")
	private void save(String category) {
		//save category
		CategoryAccesser accesser = new CategoryAccesser(getApplicationContext());
		accesser.insert(new Category(category));
		
		restart();
//		recreate();
	}



	private void restart() {
		closeDialog();
		
		Intent intent = getIntent();
		finish();
		startActivity(intent);
	}
	
	

	private void closeDialog() {
		if(dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
	}


	
	public View createTabContent(String tag) {
//    	GridView imagegrid = (GridView) findViewById(R.id.gridview);
//		imageAdapter = new ImageAdapter(this);
//		imagegrid.setAdapter(imageAdapter);
//    	
//		return imagegrid;
    	
        final TextView tv = new TextView(this);
        tv.setText("Content for tab with tag " + tag);
        return tv;
    }
    
    
    

//		public ImageAdapter() {
//			mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		}
//
//		public int getCount() {
//			return count;
//		}
//
//		public Object getItem(int position) {
//			return position;
//		}
//
//		public long getItemId(int position) {
//			return position;
//		}
//
//		public View getView(int position, View convertView, ViewGroup parent) {
//			ViewStruct holder;
//			
//			if (convertView == null) {
//				holder = new ViewStruct();
//				convertView = mInflater.inflate(R.layout.galleryitem, null);
//				holder.imageview = (ImageView) convertView.findViewById(R.id.sticker);
//				holder.textview = (TextView) convertView.findViewById(R.id.descript);
//
//				convertView.setTag(holder);
//			} else {
//				holder = (ViewStruct) convertView.getTag();
//			}
//			holder.textview.setId(position);
//			holder.imageview.setId(position);
//			
//			holder.textview.setOnClickListener(new OnClickListener() {
//
//				public void onClick(View v) {
//					
//				}
//			});
//			
//			holder.imageview.setOnClickListener(new OnClickListener() {
//
//				public void onClick(View v) {
//				}
//			});
//			
//			holder.imageview.setImageBitmap(thumbnails[position]);
//			holder.textview.setText("test");
//			holder.id = position;
//			return convertView;
//		}
//	}
//	
//	class ViewStruct {
//		ImageView imageview;
//		TextView textview;
//		String name;
//		int id;
//	}    
	
}
