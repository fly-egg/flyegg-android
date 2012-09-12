package kr.flyegg.egg.ui;

import kr.flyegg.egg.R;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.nfc.TagLostException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
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
	private ImageAdapter imageAdapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.card_board);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		tabHost = getTabHost();
		TabHost.TabSpec spec = null;
	
		
		 for (int i=1; i <= 30; i++) {
            String name = "Tab " + i;
            tabHost.addTab(tabHost.newTabSpec(name)
                    .setIndicator(name)
                    .setContent(this));
        }
		
//		spec = tabHost.newTabSpec("test");
//		tabHost.addTab( spec );
//		spec = tabHost.newTabSpec("test1");
//		tabHost.addTab(spec);
//		spec = tabHost.newTabSpec("test2");
//		tabHost.addTab(spec);
//		
		tabHost.setCurrentTab( 0 );
		
		///////////////////////////////////////////////////////////////////////////////////
		Gallery g = (Gallery) findViewById(R.id.gallery);
        // Set the adapter to our custom adapter (below)
        g.setAdapter(new ImageAdapter(this));
        
        // Set a item click listener, and just Toast the clicked position
        g.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(CardBoard.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });
		
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
    
    
    
	public class ImageAdapter extends BaseAdapter {
//		private LayoutInflater mInflater;

        private static final int ITEM_WIDTH = 136;
        private static final int ITEM_HEIGHT = 88;

        private final int mGalleryItemBackground;
        private final Context mContext;

        //특정파일 불러 올 수 있게 수정하기.. 
        private final Integer[] mImageIds = {
                R.drawable.gallery_photo_1,
                R.drawable.gallery_photo_2,
                R.drawable.gallery_photo_3,
                R.drawable.gallery_photo_4,
                R.drawable.gallery_photo_5,
                R.drawable.gallery_photo_6,
                R.drawable.gallery_photo_7,
                R.drawable.gallery_photo_8
        };

        private final float mDensity;

        public ImageAdapter(Context c) {
            mContext = c;
            // See res/values/attrs.xml for the <declare-styleable> that defines
            // Gallery1.
            TypedArray a = obtainStyledAttributes(R.styleable.gallery_sticker);
            mGalleryItemBackground = a.getResourceId(R.styleable.gallery_sticker_android_galleryItemBackground, 0);
            a.recycle();

            mDensity = c.getResources().getDisplayMetrics().density;
        }

        public int getCount() {
            return mImageIds.length;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                convertView = new ImageView(mContext);

                imageView = (ImageView) convertView;
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setLayoutParams(new Gallery.LayoutParams(
                        (int) (ITEM_WIDTH * mDensity + 0.5f),
                        (int) (ITEM_HEIGHT * mDensity + 0.5f)));
            
                // The preferred Gallery item background
                imageView.setBackgroundResource(mGalleryItemBackground);
            } else {
                imageView = (ImageView) convertView;
            }

            imageView.setImageResource(mImageIds[position]);

            return imageView;
        }
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
