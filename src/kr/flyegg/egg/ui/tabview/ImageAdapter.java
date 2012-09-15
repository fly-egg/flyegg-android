package kr.flyegg.egg.ui.tabview;

import kr.flyegg.egg.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {

//	private LayoutInflater mInflater;

    private static final int ITEM_WIDTH = 136;
    private static final int ITEM_HEIGHT = 88;

    private final int mGalleryItemBackground;
    private final Context mContext;

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
        TypedArray a = c.obtainStyledAttributes(R.styleable.gallery_sticker);
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
