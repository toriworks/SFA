package fnc.salesforce.android.Repair;

import java.io.File;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import fnc.salesforce.android.LoginPage;
import fnc.salesforce.android.Main_Page;
import fnc.salesforce.android.R;
import fnc.salesforce.android.Constance.CONTENTS_PAGE_ALL_LIST_CONSTANCE;
import fnc.salesforce.android.Constance.Constance;
import fnc.salesforce.android.LIB.BrandActivity;
import fnc.salesforce.android.LIB.CipherUtils;
import fnc.salesforce.android.LIB.DownloadFile;
import fnc.salesforce.android.LIB.KumaLog;
import fnc.salesforce.android.LIB.MenuActivity;
import fnc.salesforce.android.LIB.ProductDialog;
import fnc.salesforce.android.LIB.libJSON;
import fnc.salesforce.android.LIB.libJSON_GET;
import fnc.salesforce.android.LIB.libUDID;

public class Repair_Before_After_Detail extends Activity implements OnClickListener{

	@Override
	public void onBackPressed() {
		finish();
	}
	
	private Context mContext;
	
	private ProgressDialog pd;
		
	private libUDID lUDID = new libUDID();
		
	private ProductDialog IDD;
	
	private ImageView btn_Brand_LogOut, btnMenu_Activity;
	
	private ImageButton btn_Brand_Activity;
	
	private MenuActivity mMenu;
	
	private BrandActivity mBrand;
	
	private AlertDialog.Builder dialogBuilder;
	
	private TextView txtCategoryName, txt_AdGallery_Logo;
	
	private ImageView btnGo_Main, btnBack_Page;
	
	private ImageView imgRepaireAfter, imgRepaireBefore, imgRepaireFull;
	
	private TextView txtRepaireEffect, txtRepaireMethod, txtRepaireCode;
	
	private int DetailPosition = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView( R.layout.repaire_before_after_detail );
		
		mContext = this;
		try {
			lUDID.Check_UDID( this );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		dialogBuilder = new AlertDialog.Builder(this);
		
		mMenu = new MenuActivity( this, R.style.Transparent);
		
		mBrand	= new BrandActivity( this, R.style.Transparent);
		
		pd = new ProgressDialog( this );

		btn_Brand_Activity	= ( ImageButton ) findViewById( R.id.btn_Brand_Activity );
		
		btn_Brand_LogOut    = (ImageView) findViewById( R.id.btn_Brand_LogOut );
		txt_AdGallery_Logo  = (TextView) findViewById( R.id.txt_AdGallery_Logo );
		btnMenu_Activity    = (ImageView) findViewById( R.id.btnMenu_Activity );
		
		txtCategoryName    = (TextView) findViewById( R.id.txtCategoryName );
		
		btnGo_Main    		= (ImageView) findViewById( R.id.btnGo_Main );
		btnBack_Page    	= (ImageView) findViewById( R.id.btnBack_Page );
				
		imgRepaireAfter    	= (ImageView) findViewById( R.id.imgRepaireAfter );
		imgRepaireBefore    = (ImageView) findViewById( R.id.imgRepaireBefore );
		imgRepaireFull    	= (ImageView) findViewById( R.id.imgRepaireFull );
				
		txtRepaireEffect    = (TextView) findViewById( R.id.txtRepaireEffect );
		txtRepaireMethod    = (TextView) findViewById( R.id.txtRepaireMethod );
		txtRepaireCode    	= (TextView) findViewById( R.id.txtRepaireCode );

    	btnGo_Main.setOnClickListener( this );
		btnBack_Page.setOnClickListener( this );
		        
        txtCategoryName.setText( "수선정보" );
        
        try {
        	txt_AdGallery_Logo.setText( Constance.SHOPNAME );
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        Intent intent = getIntent();
		
        try {
        	DetailPosition = intent.getIntExtra("position", 0);
		} catch (Exception e) {
			finish();
		}
        
        try {
        	txtRepaireCode.setText( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_REPAIR_IMAGE_RESULT_LIST.get( DetailPosition ).asNm );
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        try {
        	txtRepaireMethod.setText( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_REPAIR_IMAGE_RESULT_LIST.get( DetailPosition ).asMethod );
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        try {
        	txtRepaireEffect.setText( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_REPAIR_IMAGE_RESULT_LIST.get( DetailPosition ).asEffect );
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        try {
        	DownPath[0] = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_REPAIR_IMAGE_RESULT_LIST.get( DetailPosition ).refairAfterTotImage;
        	DownPath[1] = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_REPAIR_IMAGE_RESULT_LIST.get( DetailPosition ).refairBeforePartImage;
        	DownPath[2] = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_REPAIR_IMAGE_RESULT_LIST.get( DetailPosition ).refairAfterPartImage;
		} catch (Exception e) {
			e.printStackTrace();
		}
    	btnMenu_Activity.setOnClickListener( this );
    	
    	btn_Brand_Activity.setOnClickListener( this );
    	btn_Brand_LogOut.setOnClickListener( this );
    	
        handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				new setThumbNailDown().execute();
			}
		}, 100);
	}
	
	String[] DownPath = new String[3];
	String[] LIST_THUMBPATH = new String[3];
	
	class setThumbNailDown extends AsyncTask<Integer, Integer, Integer> {
		@Override
		protected void onPreExecute() {
			
			verMax		= 3;
						
			verValue	= 0;
			
			if( !pd.isShowing() ) {
				pd.setMessage("정보를 요청중 입니다. 잠시만 기다려 주십시요.");
				pd.setCancelable( false );
				pd.show();
			}
		}
		
		@Override
		protected Integer doInBackground(Integer... params) {
			while (isCancelled() == false) {
				if (verValue < verMax) {
					try {
						if( verValue == 0 ){
							DeleteDir( mFolder_Filepath);
						}

						String Down_URL = DownPath[ verValue ];
						String strimgNAme = String.valueOf( verValue ) + ".jpg";
						
						LIST_THUMBPATH[ verValue ] =  DLF.Download_HttpFile(Down_URL, mFolder_Filepath + "", strimgNAme);
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						Thread.sleep(50);
					} catch (Exception e) {

					}
				} else {
					break;
				}
				
				try {
					Thread.sleep(30);
				} catch (Exception e) {

				}
				verValue++;
			}
			return verValue;
		}
		@Override
		protected void onProgressUpdate(Integer... values) {
		}

		@Override
		protected void onPostExecute(Integer result) {
			handler.post(new Runnable() {
				public void run() {
					setImage();
					pd.dismiss();
					pd.cancel();
				}
			});
		}
		@Override
		protected void onCancelled() {
		}
	}
	
	private void setImage(){
		for( int i = 0; i < LIST_THUMBPATH.length; i++ ){
			if( i == 0 ){
				try {
					imgRepaireFull.setImageBitmap( ReturnBitmap_Rotate90 ( BitmapFactory.decodeFile( LIST_THUMBPATH[i] ), 173, 173 )  );
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if( i == 1 ){
				try {
					imgRepaireBefore.setImageBitmap( ReturnBitmap_Rotate90 ( BitmapFactory.decodeFile( LIST_THUMBPATH[i] ), 173, 173 )  );
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if( i == 2 ){
				try {
					imgRepaireAfter.setImageBitmap( ReturnBitmap_Rotate90 ( BitmapFactory.decodeFile( LIST_THUMBPATH[i] ), 173, 173 )  );
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	private Bitmap ReturnBitmap_Rotate90(Bitmap bitmap, int width, int height) {
		Bitmap bitmapOrg = bitmap;
	
		int Orgwidth = bitmapOrg.getWidth();
		int Orgheight = bitmapOrg.getHeight();
		
		// calculate the scale - in this case = 0.4f
		float scaleWidth = ((float) width) / Orgwidth;
		float scaleHeight = ((float) height) / Orgheight;
	
		// createa matrix for the manipulation
		Matrix matrix = new Matrix();
		// resize the bit map
		matrix.postScale(scaleWidth, scaleHeight);
	
		// recreate the new Bitmap
		Bitmap resizeBm = Bitmap.createBitmap(bitmapOrg, 0, 0, Orgwidth,
				Orgheight, matrix, true);
		return resizeBm;
	}
	
	private int MaxTabbCount = 0;
	
	RelativeLayout[] tabImgBtn;
	
	View[] viewTerm;
	
	private CipherUtils mCipher = new CipherUtils();
	
	private int verValue = 0, verMax = 1;

	private libJSON LibJSON = new libJSON();
	
	private libJSON_GET GET_LibJSON = new libJSON_GET();
	
	private DownloadFile DLF = new DownloadFile();
	
	
	private Handler handler = new Handler();

	private boolean ContentsType = false;

	String mFolder_Filepath = Constance.FILEPATH + "/수선정보/수선After/";
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	private void DeleteDir(String path) 
	{ 
	    File file = new File(path);
	    File[] childFileList = file.listFiles();
	    
	    try {
	    	for(File childFile : childFileList)
		    {
		        if(childFile.isDirectory()) {
		            DeleteDir(childFile.getAbsolutePath());     //하위 디렉토리 루프 
		        } else {
		            childFile.delete();    //하위 파일삭제
		        }
		    }
//		    file.delete();    //root 삭제 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	public void onClick(View v) {

		if( v == btnMenu_Activity ){
			mMenu.show();
		} else if( v == btn_Brand_Activity ) {
			mBrand.show();
		} else if( v == btn_Brand_LogOut ) {
			dialogBuilder.setTitle("SalesForce");
			dialogBuilder.setMessage( "로그아웃 하시겠습니까?");
	        dialogBuilder.setPositiveButton( "확인" , LogOut_Yes );
	        dialogBuilder.setNegativeButton("취소", null);
	        dialogBuilder.show();
		} else if( v == btnGo_Main ) {
			Intent intent = new Intent( mContext, Main_Page.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		} else if( v == btnBack_Page ) {
			finish();
		}
	}
	
	private DialogInterface.OnClickListener LogOut_Yes = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			Constance.SHOPCD = "";
			Constance.USER_ID = "";
			Intent intent = new Intent( mContext, LoginPage.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		}
	};

}
