package kr.flyegg.egg.ui;

import java.io.File;
import java.io.IOException;
import java.util.List;

import kr.flyegg.egg.R;
import kr.flyegg.egg.dao.Card;
import kr.flyegg.egg.dao.CardAccesser;
import kr.flyegg.egg.mirror.AudioRecorder;
import kr.flyegg.egg.ui.event.DisplayNextView;
import kr.flyegg.egg.ui.event.Flip3dAnimation;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Size;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

/**
 * 말하는 거울
 * @author junho85
 */
public class MirrorMain extends Activity {
	private static final String TAG = "MirrorMain";
	
	// ------------------------------------------
	// 카메라 관련

    private Preview mPreview;
    Camera mCamera;
    int numberOfCameras;
    int cameraCurrentlyLocked;

    // The first rear facing camera
    int defaultCameraId;

	// ------------------------------------------
    // 녹음기 관련
//	private String mVoicePath = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
	// /sdcard/mnt/sdcard.3gp 에 저장되었음
    
    private String mVoicePath = "/voice.3gp";
	private AudioRecorder mAudioRecorder = new AudioRecorder(mVoicePath);

	boolean recordToggle = false;	// 녹음 버튼 토글

	// ------------------------------------------
	// Flip 관련
	private boolean isFirstImage = true;
	
	private ViewFlipper m_viewFlipper;
	private int m_nPreTouchPosX = 0;

    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// ------------------------------------------
		// 화면 기본 세팅
		// ------------------------------------------
		// 화면 회전 방지 - LANDSCAPE 로 고정
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // PORTRAIT

		// Hide the window title.
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// ------------------------------------------
		// 기존 녹음 된 파일이 있을 경우 삭제
		// ------------------------------------------
		String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + mVoicePath;
		File file = new File(filePath);
		
		if (file.exists() == true) {
			file.delete();
		}
		
		// ------------------------------------------
		// 카메라 세팅
		// ------------------------------------------

		// Create a RelativeLayout container that will hold a SurfaceView,
		// and set it as the content of our activity.
		mPreview = new Preview(this);

		// Find the total number of cameras available
		numberOfCameras = Camera.getNumberOfCameras();

		// Find the ID of the default camera
		CameraInfo cameraInfo = new CameraInfo();
		for (int i = 0; i < numberOfCameras; i++) {
			Camera.getCameraInfo(i, cameraInfo);
//			if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
			if (cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT) {
				defaultCameraId = i;
			}
		}
		
		
		// ------------------------------------------
		// Display
		// ------------------------------------------
		setContentView(mPreview);	// 카메라를 먼저 띄움

		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.activity_mirror_main, null);
		addContentView(layout, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		
		
		// ------------------------------------------
		// Flip 관련
		OnTouchListener MyTouchListener = new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					m_nPreTouchPosX = (int) event.getX();
				}

				if (event.getAction() == MotionEvent.ACTION_UP) {
					int nTouchPosX = (int) event.getX();

					
					if (Math.abs(nTouchPosX - m_nPreTouchPosX) > 10) {
						// ------------------------------------------
						// 슬라이드
						
						// ------------------------------------------
						// 회전 관련 초기화
						if (isFirstImage == false) {
							// 이미 돌아간 경우 역회전 시킴
							RelativeLayout out = (RelativeLayout)m_viewFlipper.getCurrentView();
							ImageView imageView = (ImageView)out.getChildAt(0);
							TextView textView = (TextView)out.getChildAt(1);

							applyRotation(imageView, textView, 0, -90);
							isFirstImage = !isFirstImage;
						}
						
						if (nTouchPosX < m_nPreTouchPosX) {
							MoveNextView();
						} else if (nTouchPosX > m_nPreTouchPosX) {
							MovePreviousView();
						}
					} else {
						// 터치
						RelativeLayout out = (RelativeLayout)m_viewFlipper.getCurrentView();
						ImageView imageView = (ImageView)out.getChildAt(0);
						TextView textView = (TextView)out.getChildAt(1);

						if (isFirstImage) {
							applyRotation(imageView, textView, 0, 90);
							isFirstImage = !isFirstImage;
						} else {
							applyRotation(imageView, textView, 0, -90);
							isFirstImage = !isFirstImage;
						}
					}

					m_nPreTouchPosX = nTouchPosX;
				}

				return true;
//				return false;
			}
		};
		
		m_viewFlipper = (ViewFlipper)findViewById(R.id.viewFlipper);
		m_viewFlipper.setOnTouchListener(MyTouchListener);
		m_viewFlipper.removeAllViews();	// 초기화
		
		// 카드 리스트 불러오기
		// TODO: 지정된 카테고리의 카드만 불러오도록 해야 됨
		CardAccesser cardAccesser = new CardAccesser(getApplicationContext());
        List<Card> list = cardAccesser.getCardList();
        
//        int i=0;
        for (Card card : list) {
        	// TODO: 메모리 부족 현상을 대비해서 카드 수 제한이 필요 할 수 있음
//        	i++;
//        	if (i>5)
//        		break;
        	// 이미지
        	ImageView imageView = new ImageView(this);

        	imageView.setTag(card);
        	
//			Bitmap bitmap = BitmapFactory.decodeFile(card.getImgPath());
//			Bitmap bitmap = BitmapFactory.decodeFile(getFilesDir().getAbsolutePath() + "/cards/" + card.get_id());
        	imageView.setBackgroundResource(R.drawable.card_front);
        	
			Bitmap bitmap = BitmapFactory.decodeFile(getFilesDir().getAbsolutePath() + "/cards/" + card.get_id() + ".png");
        	imageView.setImageBitmap(bitmap);
        	
        	imageView.setPadding(30,30,30,30);
        	
			RelativeLayout out = new RelativeLayout(getApplicationContext());
			out.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			
			// 단어
			TextView textView = new TextView(getApplicationContext());
			textView.setText(card.getWord());
			textView.setVisibility(View.GONE);
			textView.setPadding(30,30,30,30);
			
//			textView.setWidth(340);
//			textView.setHeight(256);
			textView.setWidth(440);
			textView.setHeight(356);
			textView.setTextSize(100.0f);
			textView.setTextColor(Color.BLACK);
			textView.setBackgroundResource(R.drawable.card_background);
			
			textView.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			textView.setGravity(Gravity.CENTER);

			out.addView(imageView);
			out.addView(textView);

			m_viewFlipper.addView(out);
        } // for
	}
	
	@Override
	protected void onResume() {
		super.onResume();

		mCamera = Camera.open(defaultCameraId);
		cameraCurrentlyLocked = defaultCameraId;
		mPreview.setCamera(mCamera);
	}

	@Override
	protected void onPause() {
		super.onPause();

		// Because the Camera object is a shared resource, it's very
		// important to release it when the activity is paused.
		if (mCamera != null) {
			mPreview.setCamera(null);
			mCamera.release();
			mCamera = null;
		}
	}

	
	// 녹음 30초 제한
	Handler mRecordHandler = new Handler() {
		public void handleMessage(Message msg) {
			Toast.makeText(getApplicationContext(), "30초가 지나서 녹음을 정지 합니다.", Toast.LENGTH_SHORT).show();
			stopRecord();
		}
	};
	
	/**
	 * 버튼 클릭 이벤트
	 * @param v
	 */
	public void onClick(View v) {
		if (v.getId() == R.id.btnVoiceRecord) {
			// ------------------------------------------
			// 녹음 버튼 클릭
			// ------------------------------------------
			
			if (recordToggle == false) {
				// 30초 후 녹음 정지
				mRecordHandler.sendEmptyMessageDelayed(0, 30000);
				
//				Toast.makeText(getApplicationContext(), "Record Start:" + mVoicePath, Toast.LENGTH_SHORT).show();
				Log.d(TAG, "Record Start:" + mVoicePath);

				try {
					mAudioRecorder = new AudioRecorder(mVoicePath);
					mAudioRecorder.start();

					Button btn = (Button) findViewById(R.id.btnVoiceRecord);
					btn.setBackgroundResource(R.drawable.btn_record_stop);
					recordToggle = true;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				// 녹음 정지
				stopRecord();
			}

		} else if (v.getId() == R.id.btnVoicePlay) {
			// ------------------------------------------
			// 재생 버튼 클릭
			
			// 녹음중 재생 버튼 클릭 막음
			if (recordToggle == true) {
				return;
			}
			
			// 재생 파일 존재 여부 검사
			String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + mVoicePath;
			File file = new File(filePath);
			if (file.exists() == false) {
				return;
			}
			
			// 재생
			MediaPlayer mediaPlayer;
            mediaPlayer = new MediaPlayer();
            try {
				mediaPlayer.setDataSource(Environment.getExternalStorageDirectory().getAbsolutePath() + mVoicePath);
                mediaPlayer.prepare();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e(TAG, "error: " + e.getMessage(), e);
			}
            mediaPlayer.start();
            
            Log.d(TAG, "Play:" + mVoicePath);
//			Toast.makeText(getApplicationContext(), "Play:" + mVoicePath, Toast.LENGTH_SHORT).show();
		} else if (v.getId() == R.id.ivLeft) {
			if (isFirstImage == false) {
				// 이미 돌아간 경우 역회전 시킴
				RelativeLayout out = (RelativeLayout)m_viewFlipper.getCurrentView();
				ImageView imageView = (ImageView)out.getChildAt(0);
				TextView textView = (TextView)out.getChildAt(1);

				applyRotation(imageView, textView, 0, -90);
				isFirstImage = !isFirstImage;
			}
			MovePreviousView();
		} else if (v.getId() == R.id.ivRight){
			if (isFirstImage == false) {
				// 이미 돌아간 경우 역회전 시킴
				RelativeLayout out = (RelativeLayout)m_viewFlipper.getCurrentView();
				ImageView imageView = (ImageView)out.getChildAt(0);
				TextView textView = (TextView)out.getChildAt(1);

				applyRotation(imageView, textView, 0, -90);
				isFirstImage = !isFirstImage;
			}
			MoveNextView();
		}
	}

	private void stopRecord() {
		Log.d(TAG, "Record Stop");
		try {
			mAudioRecorder.stop();
			mAudioRecorder = null;

			Button btn = (Button) findViewById(R.id.btnVoiceRecord);
			btn.setBackgroundResource(R.drawable.btn_record_start);
			recordToggle = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

    private void MoveNextView()
    {
    	m_viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,	R.anim.appear_from_right));
		m_viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.disappear_to_left));
		m_viewFlipper.showNext();
    }
    
    private void MovePreviousView()
    {
    	m_viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,	R.anim.appear_from_left));
		m_viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.disappear_to_right));
    	m_viewFlipper.showPrevious();
    }
    

    /**
     * 회전
     * @param view1
     * @param view2
     * @param start
     * @param end
     */
	private void applyRotation(View view1, View view2, float start, float end) {
		// Find the center of image
		final float centerX = view1.getWidth() / 2.0f;
		final float centerY = view1.getHeight() / 2.0f;

		// Create a new 3D rotation with the supplied parameter
		// The animation listener is used to trigger the next animation
		final Flip3dAnimation rotation = new Flip3dAnimation(start, end, centerX, centerY);
		rotation.setDuration(500);
		rotation.setFillAfter(true);
		rotation.setInterpolator(new AccelerateInterpolator());
		rotation.setAnimationListener(new DisplayNextView(isFirstImage, view1, view2));

		if (isFirstImage) {
			view1.startAnimation(rotation);
		} else {
			view2.startAnimation(rotation);
		}

	}

}


// ----------------------------------------------------------------------

/**
 * A simple wrapper around a Camera and a SurfaceView that renders a centered
 * preview of the Camera to the surface. We need to center the SurfaceView
 * because not all devices have cameras that support preview sizes at the same
 * aspect ratio as the device's display.
 */
class Preview extends ViewGroup implements SurfaceHolder.Callback {
	private final String TAG = "Preview";

	SurfaceView mSurfaceView;
	SurfaceHolder mHolder;
	Size mPreviewSize;
	List<Size> mSupportedPreviewSizes;
	Camera mCamera;
	
	Preview(Context context) {
		super(context);

		mSurfaceView = new SurfaceView(context);
		addView(mSurfaceView);

		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed.
		mHolder = mSurfaceView.getHolder();
		mHolder.addCallback(this);
		
		// deprecated setting, but required on Android versions prior to 3.0
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	public void setCamera(Camera camera) {
		mCamera = camera;
		if (mCamera != null) {
			mSupportedPreviewSizes = mCamera.getParameters().getSupportedPreviewSizes();
			requestLayout();
		}
	}

	public void switchCamera(Camera camera) {
		setCamera(camera);
		try {
			camera.setPreviewDisplay(mHolder);
		} catch (IOException exception) {
			Log.e(TAG, "IOException caused by setPreviewDisplay()", exception);
		}
		Camera.Parameters parameters = camera.getParameters();
		parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
		requestLayout();

		camera.setParameters(parameters);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// We purposely disregard child measurements because act as a
		// wrapper to a SurfaceView that centers the camera preview instead
		// of stretching it.
		final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
		final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
		setMeasuredDimension(width, height);

		if (mSupportedPreviewSizes != null) {
			mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, width, height);
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (changed && getChildCount() > 0) {
			final View child = getChildAt(0);

			final int width = r - l;
			final int height = b - t;

			int previewWidth = width;
			int previewHeight = height;
			/*
			if (mPreviewSize != null) {
				previewWidth = mPreviewSize.width;
				previewHeight = mPreviewSize.height;
			}
			*/

			// Center the child SurfaceView within the parent.
			if (width * previewHeight > height * previewWidth) {
				final int scaledChildWidth = previewWidth * height / previewHeight;
				child.layout((width - scaledChildWidth) / 2, 0, (width + scaledChildWidth) / 2, height);
			} else {
				final int scaledChildHeight = previewHeight * width / previewWidth;
				child.layout(0, (height - scaledChildHeight) / 2, width, (height + scaledChildHeight) / 2);
			}
		}
	}

	public void surfaceCreated(SurfaceHolder holder) {
		// The Surface has been created, acquire the camera and tell it where
		// to draw.
		try {
			if (mCamera != null) {
				mCamera.setPreviewDisplay(holder);
			}
		} catch (IOException exception) {
			Log.e(TAG, "IOException caused by setPreviewDisplay()", exception);
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// Surface will be destroyed when we return, so stop the preview.
		if (mCamera != null) {
			mCamera.stopPreview();
		}
	}

	private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
		final double ASPECT_TOLERANCE = 0.1;
		double targetRatio = (double) w / h;
		if (sizes == null)
			return null;

		Size optimalSize = null;
		double minDiff = Double.MAX_VALUE;

		int targetHeight = h;

		// Try to find an size match aspect ratio and size
		for (Size size : sizes) {
			double ratio = (double) size.width / size.height;
			if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
				continue;
			if (Math.abs(size.height - targetHeight) < minDiff) {
				optimalSize = size;
				minDiff = Math.abs(size.height - targetHeight);
			}
		}

		// Cannot find the one match the aspect ratio, ignore the requirement
		if (optimalSize == null) {
			minDiff = Double.MAX_VALUE;
			for (Size size : sizes) {
				if (Math.abs(size.height - targetHeight) < minDiff) {
					optimalSize = size;
					minDiff = Math.abs(size.height - targetHeight);
				}
			}
		}
		return optimalSize;
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		// Now that the size is known, set up the camera parameters and begin
		// the preview.
		Camera.Parameters parameters = mCamera.getParameters();
		parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
		requestLayout();

		mCamera.setParameters(parameters);
		mCamera.startPreview();
	}

}