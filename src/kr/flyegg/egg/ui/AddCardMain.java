package kr.flyegg.egg.ui;

import java.util.ArrayList;
import java.util.List;

import kr.flyegg.egg.R;
import kr.flyegg.egg.dao.Card;
import kr.flyegg.egg.dao.CardAccesser;
import kr.flyegg.egg.dao.Category;
import kr.flyegg.egg.dao.CategoryAccesser;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * 카드추가 메인 Activity
 */
public class AddCardMain extends Activity {

	private static final String TAG = "AddGameMain";

	private static final int FROM_GALLERY = 1;
	private static final int FROM_CAMERA = 2;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_addcard_main);
		
//		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title_1);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		

		// ------------------------
		// 카드 데이터 테스트
		CardAccesser cardAccesser = new CardAccesser(getApplicationContext());
        List<Card> list = cardAccesser.getCardList();
        for (Card c : list) {
        	Log.d(TAG, "word=" + c.getWord());
        }
        
		// ------------------------
		// 카테고리 리스트 스피너에 넣기
		Spinner spinner = (Spinner) findViewById(R.id.spinnerCategory);
		
		ArrayList<String> categoryList = new ArrayList<String>();
		
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//				Toast.makeText(getApplicationContext(), "Spinner1: position=" + position + " id=" + id, Toast.LENGTH_SHORT).show();
			}

			public void onNothingSelected(AdapterView<?> parent) {
//				Toast.makeText(getApplicationContext(), "Spinner1: unselected", Toast.LENGTH_SHORT).show();
			}
		});

		List<Category> categories = getCategory();
		for (Category category : categories) {
			categoryList.add(category.getCategory());
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoryList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		spinner.setAdapter(adapter);

		spinner.setSelection(0);
		spinner.setPrompt(categoryList.get(0));

	    // 레벨 클릭 처리
		OnClickListener levelClickListener = new OnClickListener() {

			public void onClick(View v) {
				
//				if (v.getId() == R.id.btnAddCategory) {
				if (false) {
					// 카테고리 추가 버튼
					if (dialog == null) {
						insertCategoryAlert();
					}
				} else if (v.getId() == R.id.btnAddCardFromGallery) {
					// 갤러리 띄워서 사진 선택 받기
					Intent intent = new Intent( Intent.ACTION_PICK ) ;
					intent.setType( android.provider.MediaStore.Images.Media.CONTENT_TYPE ) ;
					startActivityForResult( intent, FROM_GALLERY ) ;
				} else if (v.getId() == R.id.btnAddCardFromCamera) {
					// 카메라 띄우기
					Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		            startActivityForResult(intent, FROM_CAMERA);
				} else if (v.getId() == R.id.btnOK) {
					// 이미지
					if (mImagePath == null) {
						Toast.makeText(getApplicationContext(), "선택된 이미지가 없습니다.", Toast.LENGTH_SHORT).show();
						return;
					}
					
					// 카드명
					EditText txtCardName = (EditText) findViewById(R.id.txtCardName);
					String cardName = txtCardName.getText().toString().trim();
					if (cardName.equals("")) {
						Toast.makeText(getApplicationContext(), "카드명을 입력해 주세요.", Toast.LENGTH_SHORT).show();
						return;
					}
					
					Card card = new Card();
					
					Spinner spinner = (Spinner) findViewById(R.id.spinnerCategory);
					String category = spinner.getPrompt().toString();
					card.setCategory(category);
					card.setImgPath(mImagePath);
					
					// 테그
					String[] tags = {};
					card.setTags(tags);
					
					// 썸네일
					Bitmap bitmap = BitmapFactory.decodeFile(mImagePath);
					card.setThumbnail(bitmap);

					// 단어
					card.setWord(cardName);
					
                    // 카드추가
                    CardAccesser cardAccesser = new CardAccesser(getApplicationContext());
                    cardAccesser.insert(card);
                    
                    Toast.makeText(getApplicationContext(), "카드를 추가 하였습니다.", Toast.LENGTH_SHORT).show();
                    
                    // 카드 추가 후 Activity 닫기
                    finish();
				} else if (v.getId() == R.id.btnCancel) {
					finish();
				} else {
					Toast.makeText(getApplicationContext(), "Wrong", Toast.LENGTH_SHORT).show();
					return;
				}
			}
		};
		
		// 레벨 클릭 이벤트 연결
//		findViewById(R.id.btnAddCategory).setOnClickListener(levelClickListener);
		findViewById(R.id.btnAddCardFromGallery).setOnClickListener(levelClickListener);
		findViewById(R.id.btnAddCardFromCamera).setOnClickListener(levelClickListener);
		findViewById(R.id.btnOK).setOnClickListener(levelClickListener);
		findViewById(R.id.btnCancel).setOnClickListener(levelClickListener);
	}

	private AlertDialog dialog = null;

	private String mImagePath;

	private void insertCategoryAlert() {
		try {
			LayoutInflater factory = LayoutInflater.from(this);
			final View textEntryView = factory.inflate(R.layout.alert_dialog_text_entry, null);
			dialog = new AlertDialog.Builder(AddCardMain.this).setTitle(R.string.alert_dialog_text_entry).setView(textEntryView).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int whichButton) {
					EditText text = (EditText) textEntryView.findViewById(R.id.category_name_edit);
					String category = text.getText().toString();
					save(category);
				}
			}).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					// 취소
					dialog.cancel();
				}
			}).create();

			dialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void save(String category) {
		// save category
		CategoryAccesser accesser = new CategoryAccesser(getApplicationContext());
		accesser.insert(new Category(category));

		closeDialog();

		Intent intent = getIntent();
		finish();
		startActivity(intent);
		// recreate();
	}

	private void closeDialog() {
		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
	}

	/**
	 * 카테고리 리스트를 가져온다
	 * @return 카테고리 리스트
	 */
	private List<Category> getCategory() {
    	// db에서 list를 가져온다.
    	CategoryAccesser accesser = new CategoryAccesser(getApplicationContext());
    	List<Category> list = accesser.getCategory();
    	return list;
	}

    private static final String[] IMAGE_PROJECTION = {
    	MediaStore.Images.ImageColumns.DATA,
    	MediaStore.Images.Thumbnails.DATA };
    
	/**
	 * ActivityResult
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Image from Gallery
		if (requestCode == FROM_GALLERY) {
			if (resultCode == Activity.RESULT_OK) {
				
                Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
                if (cursor.moveToNext()) {
                    mImagePath = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));

                    Bitmap bitmap = BitmapFactory.decodeFile(mImagePath);
                    
                    ImageView ivAddImage = (ImageView)findViewById(R.id.ivAddImage);
                    ivAddImage.setImageBitmap(bitmap);
                    
                    Toast.makeText(getApplicationContext(), "imageUrl=" + mImagePath + ";", Toast.LENGTH_SHORT).show();
                }
			}
		} else if (requestCode == FROM_CAMERA) {
			if (resultCode == Activity.RESULT_OK) {
				final Uri uriImages = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//				final Uri uriImagesthum = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI;
				final Cursor cursorImages = getContentResolver().query(uriImages, IMAGE_PROJECTION, null, null, null);

				if (cursorImages != null && cursorImages.moveToLast()) {
					mImagePath = cursorImages.getString(0);
					cursorImages.close();

					Bitmap bitmap = BitmapFactory.decodeFile(mImagePath);

					ImageView ivAddImage = (ImageView) findViewById(R.id.ivAddImage);
					ivAddImage.setImageBitmap(bitmap);

					Toast.makeText(getApplicationContext(), "imageUrl=" + mImagePath + ";", Toast.LENGTH_SHORT).show();
				}

			}
		}
	} // end nonActivityResult
	
	public String getRealPathFromURI(Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(contentUri, proj, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}
}
