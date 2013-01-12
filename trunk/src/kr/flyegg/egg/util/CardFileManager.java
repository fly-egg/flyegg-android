package kr.flyegg.egg.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;


public class CardFileManager {
	private static final String TAG = "CardFileManager";

	public CardFileManager() {
	}
	
	/**
	 * 썸네일 이미지를 Internal Storage 에 저장
	 * @param saveImgFilename
	 * @param srcImgPath
	 */
	public static void uploadScaledImage(Context context, String srcImgPath, String saveImgFilename) {

		// 썸네일 만들어서 저장 하기
		final int THUMBNAIL_HEIGHT = 400;
		final int THUMBNAIL_WIDTH = 300;
		
		Bitmap imgOrg = BitmapFactory.decodeFile(srcImgPath);
		Float width  = new Float(imgOrg.getWidth());
		Float height = new Float(imgOrg.getHeight());
		Float ratio = height/width;

		// resize
		Bitmap imgThumb = Bitmap.createScaledBitmap(imgOrg, THUMBNAIL_WIDTH, (int)(THUMBNAIL_WIDTH*ratio), true);
//		card.setThumbnail(imgThumb);	// 의미없음

		File file_tmp = new File(context.getFilesDir().getAbsolutePath() + "/cards/");
		
		file_tmp.mkdirs();	// cards 폴더 만들기
		
		FileOutputStream myOutput = null;
		FileOutputStream fos = null;
		try {
			Log.d(TAG, "file_tmp's absolutePath is " + file_tmp.getAbsolutePath());
			myOutput = new FileOutputStream(new File(file_tmp.getAbsolutePath(), saveImgFilename + ".png"));
			imgThumb.compress(Bitmap.CompressFormat.PNG, 90, myOutput);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
				if (myOutput != null) {
					myOutput.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
