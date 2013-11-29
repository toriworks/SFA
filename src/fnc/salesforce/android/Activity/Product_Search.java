package fnc.salesforce.android.Activity;


import fnc.salesforce.android.LoginPage;
import fnc.salesforce.android.Main_Page;
import fnc.salesforce.android.R;
import fnc.salesforce.android.Constance.Constance;
import fnc.salesforce.android.LIB.BrandActivity;
import fnc.salesforce.android.LIB.MenuActivity;
import fnc.salesforce.android.LIB.libUDID;
import fnc.salesforce.android.Product_Search.Product_Search_Main;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Product_Search extends Activity implements OnClickListener{

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
	
	private RelativeLayout layoutContetsView;
	
	private Context mContext;
	
	private libUDID lUDID = new libUDID();
	
	private Product_Search_Main PSM;
	
	private String[] arrCategory_ID = new String[2];
	
	private ImageView  btn_Brand_LogOut, btnMenu_Activity;
	
	private ImageButton btn_Brand_Activity;
	
	private MenuActivity mMenu;
	
	private BrandActivity mBrand;
	
	private AlertDialog.Builder dialogBuilder;
	
	private TextView txtCategoryName, txt_AdGallery_Logo;
	
	String strCategoryName = "", strBackState = "";
	
	private ImageView btnGo_Main, btnBack_Page;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView( R.layout.product_search_main );
		
		mContext = this;
		
		try {
			lUDID.Check_UDID( this );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		dialogBuilder = new AlertDialog.Builder(this);
		
		mMenu = new MenuActivity( this, R.style.Transparent);
		
		mBrand	= new BrandActivity( this, R.style.Transparent);

		PSM = new Product_Search_Main( this );
				
		arrCategory_ID[0] = "";
		arrCategory_ID[1] = "";
		
		layoutContetsView 	= (RelativeLayout) findViewById( R.id.flipper );
		
		btn_Brand_Activity	= ( ImageButton ) findViewById( R.id.btn_Brand_Activity );
		
		btn_Brand_LogOut    = (ImageView) findViewById( R.id.btn_Brand_LogOut );
		txt_AdGallery_Logo  = (TextView) findViewById( R.id.txt_AdGallery_Logo );
		btnMenu_Activity    = (ImageView) findViewById( R.id.btnMenu_Activity );
		
		btnGo_Main    		= (ImageView) findViewById( R.id.btnGo_Main );
		btnBack_Page    	= (ImageView) findViewById( R.id.btnBack_Page );
		
		txtCategoryName    = (TextView) findViewById( R.id.txtCategoryName );
		
        
		strCategoryName = "제품조회";
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
		        		
        handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				setMainCategory();
			}
		}, 100);
	}
	
	private void setMainCategory(){
		layoutContetsView.removeAllViews();
		layoutContetsView.addView( PSM.setEducationView() );
	}
	
	
	private Handler handler = new Handler();
	
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

}
