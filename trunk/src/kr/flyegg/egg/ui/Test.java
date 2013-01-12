package kr.flyegg.egg.ui;

import java.io.File;

import junit.framework.TestCase;
import android.os.Environment;
import android.util.Log;

public class Test extends TestCase {
	private static final String TAG = "MyTest";
	
	public void testTest() {
//		String filePath = Environment.getDataDirectory().getAbsolutePath() + "/files/cards";
//		Log.d(TAG, "***" + filePath);
//		File file = new File(filePath);
//		
		File data = Environment.getDataDirectory();
		
		String filepath = data.getAbsolutePath();
		Log.d(TAG, "filepath is " + filepath);
		
		/*
		File data = new File(Environment.getDataDirectory().getAbsolutePath() + "/files/cards");
		boolean b_temp = data.mkdirs();
		Log.d(TAG, "result is " + b_temp);
		*/

		/*
		String tmp_filename = "temp_file";
		
		FileOutputStream fos = null;
		try {
			fos = openFileOutput(tmp_filename, Context.MODE_PRIVATE);
			fos.write(tmp_filename.getBytes());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		*/


		assertEquals(1, 1);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

}
