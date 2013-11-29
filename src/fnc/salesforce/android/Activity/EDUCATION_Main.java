package fnc.salesforce.android.Activity;

import java.util.regex.Pattern;

import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import fnc.salesforce.android.LoginPage;
import fnc.salesforce.android.Main_Page;
import fnc.salesforce.android.R;
import fnc.salesforce.android.AD_10003.DeveloperKey;
import fnc.salesforce.android.Catal_10002.Catalog_All;
import fnc.salesforce.android.Constance.CONTENTS_PAGE_ALL_LIST_CONSTANCE;
import fnc.salesforce.android.Constance.Constance;
import fnc.salesforce.android.Education.Education_All;
import fnc.salesforce.android.Education.Education_Media;
import fnc.salesforce.android.LIB.BackPress;
import fnc.salesforce.android.LIB.BrandActivity;
import fnc.salesforce.android.LIB.CipherUtils;
import fnc.salesforce.android.LIB.DownloadFile;
import fnc.salesforce.android.LIB.KumaLog;
import fnc.salesforce.android.LIB.MenuActivity;
import fnc.salesforce.android.LIB.ProductDialog;
import fnc.salesforce.android.LIB.libJSON;
import fnc.salesforce.android.LIB.libJSON_GET;
import fnc.salesforce.android.LIB.libUDID;

public class EDUCATION_Main extends Activity implements OnClickListener{

	@Override
	public void onBackPressed() {
		Intent intent = new Intent( mContext, Main_Page.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			System.gc();
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
		if( Constance.TimeOverState ){
			Intent intent = new Intent( mContext, LoginPage.class );
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		}
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

	private LinearLayout layoutCategoryView, layout_Tabbar;
	
	private RelativeLayout layoutContetsView;
	
	private Context mContext;
	
	private ProgressDialog pd;
	
	private String AllCateGory = "";
	
	private String strContentsID = "", strBrandCD = "";;
	
	
	private libUDID lUDID = new libUDID();
	
	private Education_All EA;
	
	private String[] arrCategory_ID = new String[2];
	
	private ProductDialog IDD;
	
	private ImageView  btn_Brand_LogOut, btnMenu_Activity;
	
	private ImageButton btn_Brand_Activity;
	
	private MenuActivity mMenu;
	
	private BrandActivity mBrand;
	
	private AlertDialog.Builder dialogBuilder;
	
	private TextView txtCategoryName, txt_AdGallery_Logo;
	
	String strCategoryName = "", strBackState = "";
	
	private ImageView btnGo_Main, btnBack_Page;
	
	private CipherUtils mCipher = new CipherUtils();
	
	private int verValue = 0, verMax = 1;

	private libJSON LibJSON = new libJSON();
	
	private libJSON_GET GET_LibJSON = new libJSON_GET();
	
	private DownloadFile DLF = new DownloadFile();
	
	public String FOLDER_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test/";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView( R.layout.education_main );
		
		mContext = this;
		
		try {
			lUDID.Check_UDID( this );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		dialogBuilder = new AlertDialog.Builder(this);
		
		mMenu = new MenuActivity( this, R.style.Transparent);
		
		mBrand	= new BrandActivity( this, R.style.Transparent);

		EA = new Education_All( this );
		
		mEducation_Media = new Education_Media( this );
		
		pd = new ProgressDialog( this );
		
		arrCategory_ID[0] = "";
		arrCategory_ID[1] = "";
		
		layoutContetsView 	= (RelativeLayout) findViewById( R.id.flipper );
		layout_Tabbar 		= (LinearLayout) findViewById( R.id.layout_Tabbar );
		
		layoutCategoryView 	= (LinearLayout) findViewById( R.id.layoutCategoryView );
		
		subCategory			= ( HorizontalScrollView ) findViewById( R.id.subCategory );
		
		btn_Brand_Activity	= ( ImageButton ) findViewById( R.id.btn_Brand_Activity );
		
		btn_Brand_LogOut    = (ImageView) findViewById( R.id.btn_Brand_LogOut );
		txt_AdGallery_Logo  = (TextView) findViewById( R.id.txt_AdGallery_Logo );
		btnMenu_Activity    = (ImageView) findViewById( R.id.btnMenu_Activity );
		
		btnGo_Main    		= (ImageView) findViewById( R.id.btnGo_Main );
		btnBack_Page    	= (ImageView) findViewById( R.id.btnBack_Page );
		
		txtCategoryName    = (TextView) findViewById( R.id.txtCategoryName );
		
		
		Intent intent = getIntent();
		
        if( intent.getStringExtra("ctgryId") != null ){
        	arrCategory_ID[0] = intent.getStringExtra("ctgryId");
        	arrCategory_ID[1] = intent.getStringExtra("brandCd");
        	strCategoryName = intent.getStringExtra("ctgryNm");
        	strBackState = intent.getStringExtra("backState");
        }
        
		strCategoryName = "교육관리";
        txtCategoryName.setText( strCategoryName );
        
        try {
        	txt_AdGallery_Logo.setText( Constance.SHOPNAME );
		} catch (Exception e) {
			e.printStackTrace();
		}
        
    	btnMenu_Activity.setOnClickListener( this );
    	
    	btn_Brand_Activity.setOnClickListener( this );
    	btn_Brand_LogOut.setOnClickListener( this );
    	
    	btnGo_Main.setOnClickListener( this );
		btnBack_Page.setOnClickListener( this );
		
        strContentsID  	= arrCategory_ID[0];
        strBrandCD  	= arrCategory_ID[1];
        
        
        
        BackPress.setDetailPage(this, "getPageCategory_Trans", 0);
        
        mDialogPassword = new DlgTest( this );
		
        if( strBackState.equals("pdf") ){
        	handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					new setEducationInfor().execute();
				}
			}, 100);
        } else {
        	mDialogPassword.show();
        }
	}
	
	private DlgTest mDialogPassword;
	
	public class DlgTest extends Dialog implements OnClickListener
    {
		private Button btnClose, btnCofirm, btnCancle;
		private EditText edit_Password;
		public DlgTest( Context context ) 
		{
			super(context);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			
			setContentView(R.layout.dialog_default);
			
			edit_Password = (EditText) findViewById( R.id.edit_Password );
			
			btnClose = (Button) findViewById( R.id.btnClose );
			btnCofirm = (Button) findViewById( R.id.btnCofirm );
			btnCancle = (Button) findViewById( R.id.btnCancle );
			
			btnClose.setOnClickListener( this );
			btnCofirm.setOnClickListener( this );
			btnCancle.setOnClickListener( this );
		}

		@Override
		public void onClick(View v) {
			mDialogPassword.cancel();
			mDialogPassword.dismiss();
			if( v == btnCancle ||  v == btnClose ){
				Intent intent = new Intent( mContext, Main_Page.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
			} else if( v == btnCofirm ){
				if( edit_Password.getText().toString().trim().equals( Constance.LOGIN_PASSWORD ) ){
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							new setEducationInfor().execute();
						}
					}, 100);
				} else {
					edit_Password.setText("");
					dialogBuilder.setTitle("SalesForce");
					dialogBuilder.setMessage( "비밀번호가 틀립니다. 다시 입력하시겠습니까?");
			        dialogBuilder.setPositiveButton( "확인" , retryPassWord );
			        dialogBuilder.setNegativeButton("취소", GoHome);
			        dialogBuilder.show();
				}
			}
		}
    }
	
	private DialogInterface.OnClickListener retryPassWord = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			mDialogPassword.show();
		}
	};
	
	private DialogInterface.OnClickListener GoHome = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			Intent intent = new Intent( mContext, Main_Page.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		}
	};
	
	private Education_Media mEducation_Media;
	public void getPageCategory_Trans(int is){

		  String upCategory = "null";

		  for( int j = 0; j < CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_CATEGORY.size(); j++){
			  if( Constance.arrTrans_UP_Information[3].equals( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_CATEGORY.get( j ).ctgryNm ) ) {
				  upCategory = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_CATEGORY.get( j ).ctgryId;
				  setChoiceCategory( j + 1 );
			  }
		  }
		if( Constance.arrTrans_UP_Information[0].equals("009001") ){
			
			layoutContetsView.removeAllViews();						
			
			layoutContetsView.addView( mEducation_Media.setEducation_MediaView( Constance.arrTrans_UP_Information[1], Constance.arrTrans_UP_Information[2], "" ) );
			
		} else if( Constance.arrTrans_UP_Information[1].equals("010001") ){
			
		}
	}
	private HorizontalScrollView subCategory;
	
	private void setCategory( boolean state){
		RelativeLayout.LayoutParams imglinelayout = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		if( state ){
			subCategory.setVisibility( View.VISIBLE );
			imglinelayout.setMargins( 0, 230, 0, 0);
		} else {
			subCategory.setVisibility( View.INVISIBLE );
			imglinelayout.setMargins( 0, 176, 0, 0);
		}
		
		layoutContetsView.setLayoutParams(imglinelayout);
	}

	private int MaxMainTabCount = 0;
	
	private ImageView[] mainTabImg;
	
	private RelativeLayout[] MainCategoryAreaLayout;
	
	TextView[] txtMainTabView;
	
	private void setMainCategory(){
		MaxMainTabCount = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_CATEGORY.size() + 1;
		
		mainTabImg					= new ImageView[ MaxMainTabCount ];
		MainCategoryAreaLayout		= new RelativeLayout[ MaxMainTabCount ];
		txtMainTabView				= new TextView[ MaxMainTabCount ];
				
//		TabLayout = new RelativeLayout( mContext ); 
		
		for( int i = 0; i < MaxMainTabCount; i++ ){
			mainTabImg[i] = new ImageButton( mContext );
			txtMainTabView[i] = new TextView( mContext );
			MainCategoryAreaLayout[i] = new RelativeLayout ( mContext );
			
			
			MainCategoryAreaLayout[i].setGravity( Gravity.CENTER_VERTICAL );
			
			RelativeLayout.LayoutParams imglinelayout = new RelativeLayout.LayoutParams(
					9, 9);
			imglinelayout.setMargins( 1, 12, 0, 0);
			String ctgryNm = "";
			
			int mCategoryWidth = 0;
			if( i == 0 ){
				txtMainTabView[i].setText( "전체" );
				mCategoryWidth = 35;
			} else {
				
				ctgryNm = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_CATEGORY.get( i - 1).ctgryNm;
				
				txtMainTabView[i].setText( ctgryNm );
				
				mCategoryWidth = ( 9 * validateInputString ( ctgryNm.toString().trim() ) );
			}
			
			RelativeLayout.LayoutParams Contentslayout = new RelativeLayout.LayoutParams(
					mCategoryWidth + 10, 32);
			MainCategoryAreaLayout[i].setLayoutParams(Contentslayout);
			
			mainTabImg[i].setLayoutParams(imglinelayout);
			mainTabImg[i].setBackgroundResource( R.drawable.at_sub_tabbullet_n );
			
			RelativeLayout.LayoutParams txtlayout = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, 32);
			txtlayout.setMargins( 15, 0, 0, 0);
			
			txtMainTabView[i].setLayoutParams(txtlayout);
			txtMainTabView[i].setGravity( Gravity.CENTER );
			
			MainCategoryAreaLayout[i].addView( mainTabImg[i] );
			MainCategoryAreaLayout[i].addView( txtMainTabView[i] );
			MainCategoryAreaLayout[i].setGravity( Gravity.CENTER );
			
			txtMainTabView[i].setOnClickListener( this );
			
			layoutCategoryView.addView( MainCategoryAreaLayout[i] );
		}
		
		txtMainTabView[0].setTextColor( Color.parseColor( "#f45947" ) );
		mainTabImg[0].setBackgroundResource( R.drawable.at_sub_tabbullet_o );
		
		setCategory( false );
		layoutContetsView.removeAllViews();
		layoutContetsView.addView( EA.setEducationView( Constance.BEANDCD, strContentsID, "ALL", strCategoryName) );
	}
	
	private int validateInputString( String workType) {
		int TextLeng = 0;
		try {
			for( int i = 0; i < workType.length(); i++ ){
				if(Pattern.matches("^[ㄱ-ㅎㅏ-ㅣ가-�R]+$", String.valueOf( workType.charAt( i ) ) ) ){  //한글일경우의 패턴 체크
					TextLeng = TextLeng + 2;
				} else {
					TextLeng = TextLeng + 1;
				}
			}
		} catch (Exception e) {
			TextLeng = 50;
			e.printStackTrace();
		}
		return TextLeng;
	 }
	
	private int MaxTabbCount = 0;
	
	RelativeLayout[] tabImgBtn;
	
	View[] viewTerm;
	
	private void setScrollViewTAbBar(){
		layout_Tabbar.removeAllViews();
		
		MaxTabbCount = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_CATEGORY_SUB.size() + 1;
		
		if( tabImgBtn != null )
			tabImgBtn = null;
		
		if( viewTerm != null )
			viewTerm = null;
		
		tabImgBtn	= new RelativeLayout[ MaxTabbCount ];
		
		viewTerm	= new View[ MaxTabbCount ];
		
		TextView[] tabtxtView	= new TextView[ MaxTabbCount ];

		for( int i = 0; i < MaxTabbCount; i++ ){
			tabImgBtn[i] 	= new RelativeLayout( mContext );

			viewTerm[i] 	= new View( mContext );
			
			tabtxtView[i] 	= new TextView( mContext );
			
			String ctgryNm = "";
			
			int mCategoryWidth = 0;
			
			if( i == 0 ){
				tabtxtView[i].setText( "전체" );
				mCategoryWidth = 60;
			} else {
				ctgryNm = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_CATEGORY_SUB.get( i - 1).ctgryNm;
				
				tabtxtView[i].setText( ctgryNm );
				
				mCategoryWidth = ( 12 * validateInputString ( ctgryNm.toString().trim() ) );
			}
			
			
			RelativeLayout.LayoutParams Contentslayout = new RelativeLayout.LayoutParams(
					mCategoryWidth + 10, 42);
			
			tabImgBtn[i].setLayoutParams(Contentslayout);			
			tabImgBtn[i].setOnClickListener( this );
			
			RelativeLayout.LayoutParams imglinelayout = new RelativeLayout.LayoutParams(
					mCategoryWidth, 40);
			
			tabtxtView[i].setLayoutParams(imglinelayout);
			tabtxtView[i].setGravity( Gravity.CENTER );
			tabtxtView[i].setTextColor( Color.WHITE );
			
			tabImgBtn[i].setBackgroundResource( R.drawable.at_sub_tabbg_n );
			tabImgBtn[i].addView( tabtxtView[i] );
			tabImgBtn[i].setGravity( Gravity.CENTER );
			
			RelativeLayout.LayoutParams termLayout = new RelativeLayout.LayoutParams(
					2, 40);
			viewTerm[i].setLayoutParams(termLayout);
			viewTerm[i].setBackgroundColor( Color.parseColor( "#00000000"));
			layout_Tabbar.addView( tabImgBtn[i] );
			layout_Tabbar.addView( viewTerm[i] );
			
		}

		layoutContetsView.removeAllViews();
		layoutContetsView.addView( EA.setEducationView( strBrandCD, strContentsID, "ALL", strCategoryName) );
		
		tabImgBtn[0].setBackgroundResource( R.drawable.at_sub_tabbg_o );
	}
	
	class setEducationInfor extends AsyncTask<Integer, Integer, Integer> {
		@Override
		protected void onPreExecute() {
			try {
				pd.setMessage("정보를 요청중 입니다. 잠시만 기다려 주십시요.");
				pd.setCancelable( false );
				pd.show();
			} catch (Exception e) {
				e.printStackTrace();
			}
			verValue = 0;
			verMax = 1;
		}
		
		@Override
		protected Integer doInBackground(Integer... params) {
			while (isCancelled() == false) {
				if (verValue < verMax) { 
					try {
						String[] aaa = new String[7];
						
						aaa[0] = Constance.BEANDCD;
						
						long timestamp = System.currentTimeMillis();
						
						aaa[1] = mCipher.encrypt( Constance.SEED , Constance.MAC + "@" + timestamp ); 
						
						aaa[2] = String.valueOf( timestamp ).toString().trim(); 
						
						GET_LibJSON.getEDUCATION_CONTENTS( aaa );
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					break;
				}
				try {
					Thread.sleep(10);
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
					new setCategoryInfor().execute();
				}
			});
		}
		
		@Override
		protected void onCancelled() {
		}
	}
	
	
	
	class setCategoryInfor extends AsyncTask<Integer, Integer, Integer> {
		@Override
		protected void onPreExecute() {
			try {
				if( !pd.isShowing() ){
					pd.setMessage("정보를 요청중 입니다. 잠시만 기다려 주십시요.");
					pd.setCancelable( false );
					pd.show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			verValue = 0;
			verMax = 1;
		}
		
		@Override
		protected Integer doInBackground(Integer... params) {
			while (isCancelled() == false) {
				if (verValue < verMax) { 
					try {
						String[] aaa = new String[7];
						
						aaa[0] = Constance.BEANDCD;
						arrCategory_ID[0] = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_EDUCATION_CONTENTS.get( 0 ).ctgryId;
						aaa[1] = arrCategory_ID[0];
						strContentsID = arrCategory_ID[0];
						
						KumaLog.LogD("test_1"," strContentsID " + strContentsID );
						long timestamp = System.currentTimeMillis();
						
						aaa[2] = mCipher.encrypt( Constance.SEED , Constance.MAC + "@" + timestamp ); 
						
						aaa[3] = String.valueOf( timestamp ).toString().trim(); 
						
						GET_LibJSON.getCATEGORY_LIST(  aaa );
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					break;
				}
				try {
					Thread.sleep(10);
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
					setMainCategory();
					pd.dismiss();
					pd.cancel();
				}
			});
		}
		
		@Override
		protected void onCancelled() {
		}
	}
	
	class setCategoryInfor_SUB extends AsyncTask<Integer, Integer, Integer> {
		@Override
		protected void onPreExecute() {
			try {
				if( !pd.isShowing()){
					pd.setMessage("정보를 요청중 입니다. 잠시만 기다려 주십시요.");
					pd.setCancelable( false );
					pd.show();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			verValue = 0;
			verMax = 1;
		}
		
		@Override
		protected Integer doInBackground(Integer... params) {
			while (isCancelled() == false) {
				if (verValue < verMax) { 
					try {
						String[] aaa = new String[7];
						
						aaa[0] = strBrandCD;
						
						aaa[1] = strContentsID;
						
						long timestamp = System.currentTimeMillis();
						
						aaa[2] = mCipher.encrypt( Constance.SEED , Constance.MAC + "@" + timestamp ); 
						
						aaa[3] = String.valueOf( timestamp ).toString().trim(); 
						
						GET_LibJSON.getCATEGORY_LIST_SUB(  aaa );
						
					} catch (Exception e) {
						e.printStackTrace();
					} 
				} else {
					break;
				}
				try {
					Thread.sleep(10);
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
					setScrollViewTAbBar();
					pd.dismiss();
					pd.cancel();
				}
			});
		} 
		
		@Override
		protected void onCancelled() {
		}
	}
	
	private Handler handler = new Handler();

	private boolean ContentsType = false;
	
	private void setChoiceCategory( int position ){
		
		for( int i = 0; i < txtMainTabView.length; i++ ){
			if( position == i ){
				txtMainTabView[i].setTextColor( Color.parseColor( "#f45947" ) );
				mainTabImg[i].setBackgroundResource( R.drawable.at_sub_tabbullet_o );
			} else {
				txtMainTabView[i].setTextColor( Color.parseColor( "#ffffff" ) );
				mainTabImg[i].setBackgroundResource( R.drawable.at_sub_tabbullet_n );
			}
		}
	}
	
	@Override
	public void onClick(View v) {		
		if( txtMainTabView != null ){
			for( int i = 0; i < MaxMainTabCount; i++){
				if( v == txtMainTabView[i] ){
					setChoiceCategory( i );
					if( i == 0 ){
						
						txtMainTabView[0].setTextColor( Color.parseColor( "#f45947" ) );
						mainTabImg[0].setBackgroundResource( R.drawable.at_sub_tabbullet_o );
						
						strBrandCD = Constance.BEANDCD;
						strContentsID = arrCategory_ID[0];
						setCategory( false );
						ContentsType = false;
						
						layoutContetsView.removeAllViews();
						layoutContetsView.addView( EA.setEducationView( strBrandCD, strContentsID, "ALL", strCategoryName) );
						
					} else {
						strBrandCD = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_CATEGORY.get( i - 1 ).brandCd;
						strContentsID = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_CATEGORY.get( i - 1 ).ctgryId;
						setCategory( true );
						ContentsType = true;
						new setCategoryInfor_SUB().execute();
					}
				}
			}
		}
		
		try {
			if( tabImgBtn != null ){
				for( int i = 0; i < MaxTabbCount; i++){
					if( v == tabImgBtn[i] ){
						setSubTabBarChicoe( i );
						layoutContetsView.removeAllViews();
						if( i == 0 ){
							layoutContetsView.addView( EA.setEducationView( strBrandCD, strContentsID, "ALL", strCategoryName) );
						} else {
							layoutContetsView.addView( EA.setEducationView( strBrandCD, CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_CATEGORY_SUB.get( i - 1).ctgryId, "SUB", strCategoryName) );
						}
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
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
			try {
				Intent intent = new Intent( mContext, Main_Page.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
			} catch (Exception e) {
				e.printStackTrace();
			}
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

	private void setSubTabBarChicoe( int position ){
		for( int i = 0; i < tabImgBtn.length; i++ ){
			if( i == position ){
				tabImgBtn[i].setBackgroundResource( R.drawable.at_sub_tabbg_o );
			} else {
				tabImgBtn[i].setBackgroundResource( R.drawable.at_sub_tabbg_n);
			}
		}
	}
}