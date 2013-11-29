package fnc.salesforce.android.Activity;

import java.util.regex.Pattern;

import fnc.salesforce.android.LoginPage;
import fnc.salesforce.android.Main_Page;
import fnc.salesforce.android.R;
import fnc.salesforce.android.Activity.Promotion_10004.setCategoryInfor;
import fnc.salesforce.android.Activity.Promotion_10004.setCategoryInfor_SUB;
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
import fnc.salesforce.android.Promotion_10004.Promotion_10004_Sub;
import fnc.salesforce.android.Promotion_10004.Promotion_10004_Sub_Detail;
import fnc.salesforce.android.Repair.Repair_Before_After;
import fnc.salesforce.android.Repair.Repair_Cost;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RepairInformaiton_Main extends Activity implements OnClickListener{

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
	
	private LinearLayout layoutCategoryView;
	
	private RelativeLayout layoutContetsView;
	
	private Context mContext;
	
	private ProgressDialog pd;
		
	private libUDID lUDID = new libUDID();
	
	private String[] arrCategory_ID = new String[2];
	
	private ProductDialog IDD;
	
	private ImageView btn_Brand_LogOut, btnMenu_Activity;
	
	private ImageButton btn_Brand_Activity;
	
	private MenuActivity mMenu;
	
	private BrandActivity mBrand;
	
	private AlertDialog.Builder dialogBuilder;
	
	private TextView txtCategoryName, txt_AdGallery_Logo;
	
	private Repair_Cost mRepair_All;

	private Repair_Before_After mRepair_Before_After;
	
	private ImageView btnGo_Main, btnBack_Page;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView( R.layout.repair_main );
		
		mContext = this;
		try {
			lUDID.Check_UDID( this );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		dialogBuilder = new AlertDialog.Builder(this);
		
		mMenu = new MenuActivity( this, R.style.Transparent);
		
		mBrand	= new BrandActivity( this, R.style.Transparent);

		mRepair_All 			= new Repair_Cost( this );
		mRepair_Before_After	= new Repair_Before_After( this );
		pd = new ProgressDialog( this );
		
		MainTabPosition = 0;
		
		arrCategory_ID[0] = "";
		arrCategory_ID[1] = "";
		
		layoutContetsView 	= (RelativeLayout) findViewById( R.id.flipper );
		
		layoutCategoryView 	= (LinearLayout) findViewById( R.id.layoutCategoryView );
		btn_Brand_Activity	= ( ImageButton ) findViewById( R.id.btn_Brand_Activity );
		
		btn_Brand_LogOut    = (ImageView) findViewById( R.id.btn_Brand_LogOut );
		txt_AdGallery_Logo  = (TextView) findViewById( R.id.txt_AdGallery_Logo );
		btnMenu_Activity    = (ImageView) findViewById( R.id.btnMenu_Activity );
		
		txtCategoryName    = (TextView) findViewById( R.id.txtCategoryName );
		
		btnGo_Main    		= (ImageView) findViewById( R.id.btnGo_Main );
		btnBack_Page    	= (ImageView) findViewById( R.id.btnBack_Page );
		
    	btnGo_Main.setOnClickListener( this );
		btnBack_Page.setOnClickListener( this );
		        
        txtCategoryName.setText( "수선정보" );
        
        try {
        	txt_AdGallery_Logo.setText( Constance.SHOPNAME );
		} catch (Exception e) {
			e.printStackTrace();
		}
        
    	btnMenu_Activity.setOnClickListener( this );
    	
    	btn_Brand_Activity.setOnClickListener( this );
    	btn_Brand_LogOut.setOnClickListener( this );
    	
        handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				setMainCategory();
			}
		}, 100);
	}

	private int MaxMainTabCount = 0;
	
	private ImageView[] mainTabImg;
	
	private RelativeLayout[] MainCategoryAreaLayout;
	
	TextView[] txtMainTabView;
	
	private void setMainCategory(){
		
		MaxMainTabCount = 2;

		layoutCategoryView.removeAllViews();
		
		if( mainTabImg != null )
			mainTabImg = null;
		
		if( MainCategoryAreaLayout != null )
			MainCategoryAreaLayout = null;
		
		if( txtMainTabView != null )
			txtMainTabView = null;
		
		
		mainTabImg					= new ImageView[ MaxMainTabCount ];
		MainCategoryAreaLayout		= new RelativeLayout[ MaxMainTabCount ];
		txtMainTabView				= new TextView[ MaxMainTabCount ];
				
//		TabLayout = new RelativeLayout( mContext ); 
		
		for( int i = 0; i < MaxMainTabCount; i++ ){
			mainTabImg[i] = new ImageButton( mContext );
			txtMainTabView[i] = new TextView( mContext );
			MainCategoryAreaLayout[i] = new RelativeLayout ( mContext );

			RelativeLayout.LayoutParams imglinelayout = new RelativeLayout.LayoutParams(
					9, 9);
			imglinelayout.setMargins( 1, 12, 0, 0);
			
			String ctgryNm = "";
			
			int mCategoryWidth = 0;
			
			if( i == 0 ){
				ctgryNm = "수선 단가표" ;
				txtMainTabView[i].setText( ctgryNm );
			} else {
				ctgryNm = "수선 전후이미지" ;
				txtMainTabView[i].setText( ctgryNm );
			}
			
			mCategoryWidth = ( 9 * validateInputString ( ctgryNm.toString().trim() ) );
			
			RelativeLayout.LayoutParams Contentslayout = new RelativeLayout.LayoutParams(
					mCategoryWidth + 10, 32);
			
			MainCategoryAreaLayout[i].setLayoutParams(Contentslayout);
			MainCategoryAreaLayout[i].setGravity( Gravity.CENTER_VERTICAL );
			
			
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
		
		RelativeLayout.LayoutParams imglinelayout = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		imglinelayout.setMargins( 0, 176, 0, 0);
		
		layoutContetsView.setLayoutParams(imglinelayout);
		
		layoutContetsView.removeAllViews();
		
		layoutContetsView.addView( mRepair_All.setRepair_AllView() );
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
	
	private CipherUtils mCipher = new CipherUtils();
	
	private int verValue = 0, verMax = 1;

	private libJSON LibJSON = new libJSON();
	
	private libJSON_GET GET_LibJSON = new libJSON_GET();
	
	private DownloadFile DLF = new DownloadFile();
	
	
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
						if( layoutContetsView.getChildAt( 0 ) != mRepair_All.getView()){
							layoutContetsView.removeAllViews();
							
							layoutContetsView.addView( mRepair_All.setRepair_AllView() );
						}
					} else {
						
						if( layoutContetsView.getChildAt( 0 ) != mRepair_Before_After.getView()){
							layoutContetsView.removeAllViews();
							
							layoutContetsView.addView( mRepair_Before_After.setRepair_Before_After() );
						}
					}
				}
			}
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
			Intent intent = new Intent( mContext, Main_Page.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		}
	}
	
	private int MainTabPosition = 0;
	
	
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
