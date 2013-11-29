package fnc.salesforce.android.Activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import fnc.salesforce.android.LoginPage;
import fnc.salesforce.android.Main_Page;
import fnc.salesforce.android.R;
import fnc.salesforce.android.Constance.CONTENTS_PAGE_ALL_LIST_CONSTANCE;
import fnc.salesforce.android.Constance.Constance;
import fnc.salesforce.android.Constance.OZVIEWER_CONSTANCE;
import fnc.salesforce.android.LIB.BrandActivity;
import fnc.salesforce.android.LIB.CipherUtils;
import fnc.salesforce.android.LIB.HidekeyBoard;
import fnc.salesforce.android.LIB.KumaLog;
import fnc.salesforce.android.LIB.MenuActivity;
import fnc.salesforce.android.LIB.libJSON_GET;
import fnc.salesforce.android.LIB.libUDID;
import fnc.salesforce.android.Membership.MemberShip_Customer;
import fnc.salesforce.android.Membership.MemberShip_Customer_Detail;
import fnc.salesforce.android.OZ.OZViewerLocalActivity;
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
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MemberShip_Main extends Activity implements OnClickListener{

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
			mHandler.removeMessages(MSG_CLOCK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
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
	
	private String[] arrCategory_ID = new String[2];
	
	private ImageView btn_Brand_LogOut, btnMenu_Activity;
	
	private ImageButton btn_Brand_Activity;
	
	private MenuActivity mMenu;
	
	private BrandActivity mBrand;
	
	private AlertDialog.Builder dialogBuilder;
	
	private TextView txtCategoryName, txt_AdGallery_Logo;
	
	private ImageView btnGo_Main, btnBack_Page, btnE_Kolon_Card_Insert, btn_Mobile_Insert;
	
	private ImageView btn_E_Kolon_Card_Intorduce, btn_Brand_Grade_Intorduce, btn_Customer_Search;
	
	private MemberShip_Customer mMemberShip_Customer;
	
	private ProgressDialog pd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView( R.layout.membership_main );
		
		mContext = this;
		try {
			lUDID.Check_UDID( this );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		pd = new ProgressDialog( mContext );
		
		dialogBuilder = new AlertDialog.Builder(this);
		
		mMenu = new MenuActivity( this, R.style.Transparent);
		
		mBrand	= new BrandActivity( this, R.style.Transparent);
				
		mDlgMobileMembershipDialog = new DlgMobileMembershipDialog( this );
		
		mMemberShip_Customer = new MemberShip_Customer( this );
		
		mDlgeKolonDialog = new DlgeKolonDialog( this );
		
		mDlgCutomerAuthDialog = new DlgCutomerAuthDialog( this );
		
		mDlgPhoneAuth = new DlgPhoneAuth( this );
		
		arrCategory_ID[0] = "";
		arrCategory_ID[1] = "";
		
		layoutContetsView 	= (RelativeLayout) findViewById( R.id.flipper );

		btn_Brand_Activity	= ( ImageButton ) findViewById( R.id.btn_Brand_Activity );
		
		btn_Brand_LogOut    = (ImageView) findViewById( R.id.btn_Brand_LogOut );
		txt_AdGallery_Logo  = (TextView) findViewById( R.id.txt_AdGallery_Logo );
		btnMenu_Activity    = (ImageView) findViewById( R.id.btnMenu_Activity );
		
		txtCategoryName    = (TextView) findViewById( R.id.txtCategoryName );
		
		btnGo_Main    		= (ImageView) findViewById( R.id.btnGo_Main );
		btnBack_Page    	= (ImageView) findViewById( R.id.btnBack_Page );
		
		btnE_Kolon_Card_Insert    		= (ImageView) findViewById( R.id.btnE_Kolon_Card_Insert );
		btn_Mobile_Insert    			= (ImageView) findViewById( R.id.btn_Mobile_Insert );
		btn_E_Kolon_Card_Intorduce   	= (ImageView) findViewById( R.id.btn_E_Kolon_Card_Intorduce );
		btn_Brand_Grade_Intorduce    	= (ImageView) findViewById( R.id.btn_Brand_Grade_Intorduce );
		btn_Customer_Search    			= (ImageView) findViewById( R.id.btn_Customer_Search );
		
    	btnGo_Main.setOnClickListener( this );
		btnBack_Page.setOnClickListener( this );
		
		
        txtCategoryName.setText( "Membership" );
        
        try {
        	txt_AdGallery_Logo.setText( Constance.SHOPNAME );
		} catch (Exception e) {
			e.printStackTrace();
		}
        
    	btnMenu_Activity.setOnClickListener( this );
    	
    	btn_Brand_Activity.setOnClickListener( this );
    	btn_Brand_LogOut.setOnClickListener( this );
    	btn_Mobile_Insert.setOnClickListener( this );
    	btnE_Kolon_Card_Insert.setOnClickListener( this );
    	
    	btn_E_Kolon_Card_Intorduce.setOnClickListener( this );
    	btn_Brand_Grade_Intorduce.setOnClickListener( this );
    	btn_Customer_Search.setOnClickListener( this );
    	
    	imgE_Kolon_Card = new ImageView( this );
    	imgBrand_Grade = new ImageView( this );
    	
    	imgE_Kolon_Card.setScaleType( ScaleType.FIT_CENTER);
    	imgBrand_Grade.setScaleType( ScaleType.FIT_CENTER);
    	
        handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				
			}
		}, 100);
                
        setMenuChange( 1 );
        setBrandGrade();
	}
	
	private void setMenuChange( int position ){
		if( position == 0 ){
			btn_E_Kolon_Card_Intorduce.setBackgroundResource( R.drawable.at_sf_mem_cat_btn_01_o );
			btn_Brand_Grade_Intorduce.setBackgroundResource( R.drawable.at_sf_mem_cat_btn_02_n );
			btn_Customer_Search.setBackgroundResource( R.drawable.at_sf_mem_cat_btn_03_n );
		} else if( position == 1 ){
			btn_E_Kolon_Card_Intorduce.setBackgroundResource( R.drawable.at_sf_mem_cat_btn_01_n );
			btn_Brand_Grade_Intorduce.setBackgroundResource( R.drawable.at_sf_mem_cat_btn_02_o );
			btn_Customer_Search.setBackgroundResource( R.drawable.at_sf_mem_cat_btn_03_n );
		} else if( position == 2 ){
			btn_E_Kolon_Card_Intorduce.setBackgroundResource( R.drawable.at_sf_mem_cat_btn_01_n );
			btn_Brand_Grade_Intorduce.setBackgroundResource( R.drawable.at_sf_mem_cat_btn_02_n );
			btn_Customer_Search.setBackgroundResource( R.drawable.at_sf_mem_cat_btn_03_o );
		}
	}
	
	private ImageView imgE_Kolon_Card, imgBrand_Grade;
	
	private void setEKolonCard(){
		if( layoutContetsView.getChildAt( 0 ) != imgE_Kolon_Card ){
			setMenuChange( 0 );
			
			RelativeLayout.LayoutParams imgLayout = new RelativeLayout.LayoutParams(
					730, 943);
			imgLayout.setMargins( 25, 10, 0, 0);
			
			imgE_Kolon_Card.setLayoutParams( imgLayout );
			
			imgE_Kolon_Card.setBackgroundResource( R.drawable.e_kolon_card);
				
			layoutContetsView.removeAllViews();
			
			layoutContetsView.addView( imgE_Kolon_Card );
		}
		
	}
	
	private void setBrandGrade(){
		if( layoutContetsView.getChildAt( 0 ) != imgBrand_Grade ){
			setMenuChange( 1 );
			RelativeLayout.LayoutParams imgLayout = new RelativeLayout.LayoutParams(
					730, 821);
			imgLayout.setMargins( 25, 10, 0, 0);
			
			imgBrand_Grade.setLayoutParams( imgLayout );
			
			imgBrand_Grade.setBackgroundResource( R.drawable.brand_grade);
			
			layoutContetsView.removeAllViews();
			
			layoutContetsView.addView( imgBrand_Grade );			
		}
	}
	
	private void setCustomerSearch(){
		
		if( layoutContetsView.getChildAt( 0 ) != mMemberShip_Customer.getView() ){
			setMenuChange( 2 );
			
			layoutContetsView.removeAllViews();
			
			layoutContetsView.addView( mMemberShip_Customer.setMemberShip_Customer() );
		}
	}

	private DlgMobileMembershipDialog mDlgMobileMembershipDialog;
	
	private String phoneType = "01";
	
	public class DlgMobileMembershipDialog extends Dialog implements OnClickListener
    {
		private Button btnPhoneSKT, btnClose, btnPhoneKT;
		private Button btnLetter, btnCancle, btnPhoneLG;
		private EditText edit_Name, edit_PhoneNumber;
		
		public DlgMobileMembershipDialog( Context context ) 
		{
			super(context);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			
			setContentView(R.layout.membershi_mobile);
			
			btnPhoneSKT 		= (Button) findViewById( R.id.btnPhoneSKT );
			btnClose 			= (Button) findViewById( R.id.btnClose );
			btnPhoneLG 			= (Button) findViewById( R.id.btnPhoneLG );
			btnPhoneKT 			= (Button) findViewById( R.id.btnPhoneKT );
			btnLetter 			= (Button) findViewById( R.id.btnLetter );
			btnCancle 			= (Button) findViewById( R.id.btnCancle );
			
			edit_Name 					= (EditText) findViewById( R.id.edit_Name );
			edit_PhoneNumber 			= (EditText) findViewById( R.id.edit_PhoneNumber );
			
			btnPhoneSKT.setOnClickListener( this );
			btnPhoneLG.setOnClickListener( this );
			btnPhoneKT.setOnClickListener( this );
			btnLetter.setOnClickListener( this );
			btnCancle.setOnClickListener( this );
			btnClose.setOnClickListener( this );
		}

		@Override
		public void show() {
			// TODO Auto-generated method stub
			super.show();
			edit_Name.setText("");
			edit_PhoneNumber.setText("");
			
		}
		@Override
		public void onClick(View v) {
			if( v == btnPhoneSKT ){
				setBtnBackGround( 1 );
				phoneType = "01";
			} else if( v == btnPhoneKT ){
				setBtnBackGround( 2 );
				phoneType = "02";
			} else if( v == btnPhoneLG ){
				setBtnBackGround( 3 );
				phoneType = "03";
			} else if( v == btnLetter ){
				HidekeyBoard.KeyboardHide(mContext, btnLetter.getWindowToken() );
				if( edit_Name.getText().toString().trim().length() > 0 ){
					if( edit_PhoneNumber.getText().toString().trim().length() >= 10 ){
						getPhoneNumber( edit_PhoneNumber.getText().toString().trim() );
						
						mMobiledName = edit_Name.getText().toString().trim();
						mMobile_Number = argsCustomer[0] + "-" + argsCustomer[1] + "-" + argsCustomer[2];
						new setCustomer_MobileTrans().execute();
					} else {
						dialogBuilder.setTitle("SalesForce");
						dialogBuilder.setMessage( "휴대폰번호를 올바르게 입력해주십시오.( 10~11자 )");
				        dialogBuilder.setPositiveButton( "확인" , null );
				        dialogBuilder.show();
					}
				} else {
					dialogBuilder.setTitle("SalesForce");
					dialogBuilder.setMessage( "성명을 입력해주세요.");
			        dialogBuilder.setPositiveButton( "확인" , null );
			        dialogBuilder.show();
				}
				
			} else if( v == btnCancle ||  v == btnClose){
				dismiss();
				cancel();
			}
		}
		private void setBtnBackGround( int position ){
			if( position == 1 ){
				btnPhoneSKT.setBackgroundResource( R.drawable.at_popup_btn_bg01);
				btnPhoneKT.setBackgroundResource( R.drawable.at_popup_btn_bg02);
				btnPhoneLG.setBackgroundResource( R.drawable.at_popup_btn_bg02);
			} else if( position == 2 ){
				btnPhoneSKT.setBackgroundResource( R.drawable.at_popup_btn_bg02);
				btnPhoneKT.setBackgroundResource( R.drawable.at_popup_btn_bg01);
				btnPhoneLG.setBackgroundResource( R.drawable.at_popup_btn_bg02);
			} else if( position == 3 ){
				btnPhoneSKT.setBackgroundResource( R.drawable.at_popup_btn_bg02);
				btnPhoneKT.setBackgroundResource( R.drawable.at_popup_btn_bg02);
				btnPhoneLG.setBackgroundResource( R.drawable.at_popup_btn_bg01);
			}
		}
    }
	
	private String mMobiledName = "", mMobile_Number = "";
	
	private CipherUtils mCipher = new CipherUtils();
	
	private int verValue = 0, verMax = 1;

	private libJSON_GET GET_LibJSON = new libJSON_GET();
	
	class setCustomer_MobileTrans extends AsyncTask<Integer, Integer, Integer> {
		String NetworkResult = "";
		@Override
		protected void onPreExecute() {
			try {
				if( !pd.isShowing() ) {
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
						
						String[] aaa = new String[5];

						long timestamp = System.currentTimeMillis();
						aaa[0] = mCipher.encrypt( Constance.SEED , Constance.MAC + "@" + timestamp ); 
						aaa[1] = String.valueOf( timestamp ).toString().trim();
						
						aaa[2] = mMobiledName; 
						aaa[3] = mMobile_Number; 
						aaa[4] = phoneType;

						NetworkResult = GET_LibJSON.MEMBER_MOBILE_ENTRY( aaa );

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
					try {
						pd.dismiss();
						pd.cancel();
						
						mDlgMobileMembershipDialog.dismiss();
						mDlgMobileMembershipDialog.cancel();
						
						if( NetworkResult.equals("F") ){
							dialogBuilder.setTitle("SalesForce");
							dialogBuilder.setMessage( "네트워크 오류입니다. 다시 시도 하시겠습니까?");
					        dialogBuilder.setPositiveButton( "확인" , Retry_Yes );
					        dialogBuilder.setNegativeButton("취소", null);
					        dialogBuilder.show();
						} else if( NetworkResult.equals("N") ){
							dialogBuilder.setTitle("SalesForce");
							dialogBuilder.setMessage( "인증에 실패하였습니다. 다시 시도하시겠습니까?");
					        dialogBuilder.setPositiveButton( "확인" , L_Yes );
					        dialogBuilder.setNegativeButton("취소", null);
					        dialogBuilder.show();
						} else if( NetworkResult.equals("T") ){
							dialogBuilder.setTitle("SalesForce");
							dialogBuilder.setMessage( "문자가 전송 되었습니다.");
					        dialogBuilder.setPositiveButton( "확인" , null );
					        dialogBuilder.show();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
		@Override
		protected void onCancelled() {
			
		}
	}
	
	private DialogInterface.OnClickListener L_Yes = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			mDlgMobileMembershipDialog.show();
		}
	};

	private DialogInterface.OnClickListener Retry_Yes = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			new setCustomer_MobileTrans().execute();
		}
	};
	
	private String[] argsCustomer = new String[3];
	
	private void getPhoneNumber( String strnumber ){
		argsCustomer[0] = "";
		argsCustomer[1] = "";
		argsCustomer[2] = "";
		
		if( strnumber.length() == 10 ){
			for( int i = 0; i < strnumber.length(); i++){
				if( i < 3 ){
					argsCustomer[0] += strnumber.charAt(i);
				} else if ( i >= 3 && i < 6 ){
					argsCustomer[1] += strnumber.charAt(i);
				} else {
					argsCustomer[2] += strnumber.charAt(i);
				}
			}
			
		} else if( strnumber.length() == 11 ){
			for( int i = 0; i < strnumber.length(); i++){
				if( i < 3 ){
					argsCustomer[0] += strnumber.charAt(i);
				} else if ( i >= 3 && i < 7 ){
					argsCustomer[1] += strnumber.charAt(i);
				} else {
					argsCustomer[2] += strnumber.charAt(i);
				}
			}
		}
	}
	
	private DlgCutomerAuthDialog mDlgCutomerAuthDialog;
	
	private String AuthGender = "1", mJuminBirth = "";
	
	public class DlgCutomerAuthDialog extends Dialog implements OnClickListener
    {
		private Button btnConfirm, btnClose, btnCancle, btnAuth_Women, btnAuth_Man;
		private EditText edit_Auth_phoneNumber, edit_Auth_Jumin_1, edit_Auth_Jumin_2, edit_Auth_Jumin_3;
		private EditText edit_Auth_Name;
		public DlgCutomerAuthDialog( Context context ) 
		{
			super(context);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			
			setContentView(R.layout.membership_man_authdialog);
			
			btnConfirm 		= (Button) findViewById( R.id.btnConfirm );
			btnClose 		= (Button) findViewById( R.id.btnClose );
			btnCancle 		= (Button) findViewById( R.id.btnCancle );
			btnAuth_Man 	= (Button) findViewById( R.id.btnAuth_Man );
			btnAuth_Women 	= (Button) findViewById( R.id.btnAuth_Women );
			
			edit_Auth_phoneNumber 	= (EditText) findViewById( R.id.edit_Auth_phoneNumber );
			edit_Auth_Jumin_1 		= (EditText) findViewById( R.id.edit_Auth_Jumin_1 );
			edit_Auth_Jumin_2 		= (EditText) findViewById( R.id.edit_Auth_Jumin_2 );
			edit_Auth_Jumin_3 		= (EditText) findViewById( R.id.edit_Auth_Jumin_3 );
			edit_Auth_Name 			= (EditText) findViewById( R.id.edit_Auth_Name );
			
			btnConfirm.setOnClickListener( this );
			btnClose.setOnClickListener( this );
			btnCancle.setOnClickListener( this );
			btnAuth_Man.setOnClickListener( this );
			btnAuth_Women.setOnClickListener( this );
			
		}
		
		@Override
		public void show() {
			// TODO Auto-generated method stub
			super.show();
			
			mcertificationTP = "1";
			
			setBtnBackGround( 1 );
			AuthGender = "1";
			
			edit_Auth_phoneNumber.setText("");
			edit_Auth_Jumin_1.setText("");
			edit_Auth_Jumin_2.setText("");
			edit_Auth_Jumin_3.setText("");
			edit_Auth_Name.setText("");
		}
		@Override
		public void onClick(View v) {
			if( v == btnAuth_Man ){
				setBtnBackGround( 1 );
				AuthGender = "1";
			} else if( v == btnAuth_Women ){
				setBtnBackGround( 2 );
				AuthGender = "2";
			} else if( v == btnConfirm ){
				HidekeyBoard.KeyboardHide(mContext, btnConfirm.getWindowToken() );
				if( edit_Auth_Name.getText().toString().trim().length() > 0 ){
					if( edit_Auth_phoneNumber.getText().toString().trim().length() >= 10 ){
						if( edit_Auth_Jumin_1.getText().toString().trim().length() == 4 ){
							if( edit_Auth_Jumin_2.getText().toString().trim().length() == 2 ){
								if( edit_Auth_Jumin_3.getText().toString().trim().length() == 2 ){
									mJuminBirth = edit_Auth_Jumin_1.getText().toString().trim();
									mJuminBirth = mJuminBirth + edit_Auth_Jumin_2.getText().toString().trim();
									mJuminBirth = mJuminBirth + edit_Auth_Jumin_3.getText().toString().trim();
									
									getPhoneNumber( edit_Auth_phoneNumber.getText().toString().trim() );
									
									mMobiledName = edit_Auth_Name.getText().toString().trim();
									mMobile_Number = argsCustomer[0] + "-" + argsCustomer[1] + "-" + argsCustomer[2];
									mcertificationTP = "1";
									mcertificationNo = "";
									new setCustomer_AUTH().execute();
								} else {
									dialogBuilder.setTitle("SalesForce");
									dialogBuilder.setMessage( "일 입력을 확인해주세요. ex) 09");
							        dialogBuilder.setPositiveButton( "확인" , null );
							        dialogBuilder.show();
								}
							} else {
								dialogBuilder.setTitle("SalesForce");
								dialogBuilder.setMessage( "월 입력을 확인해주세요. ex) 09");
						        dialogBuilder.setPositiveButton( "확인" , null );
						        dialogBuilder.show();
							}
						} else {
							dialogBuilder.setTitle("SalesForce");
							dialogBuilder.setMessage( "년도 입력을 확인해주세요. ex) 1983");
					        dialogBuilder.setPositiveButton( "확인" , null );
					        dialogBuilder.show();
						}
					} else {
						dialogBuilder.setTitle("SalesForce");
						dialogBuilder.setMessage( "휴대폰번호를 올바르게 입력해주십시오.( 10~11자 )");
				        dialogBuilder.setPositiveButton( "확인" , null );
				        dialogBuilder.show();
					}
				} else {
					dialogBuilder.setTitle("SalesForce");
					dialogBuilder.setMessage( "성명을 입력해주세요.");
			        dialogBuilder.setPositiveButton( "확인" , null );
			        dialogBuilder.show();
				}
			} else if( v == btnClose || v == btnCancle ){
				dismiss();
				cancel();
			}
		}
		
		private void setBtnBackGround( int position ){
			if( position == 1 ){
				btnAuth_Man.setBackgroundResource( R.drawable.at_popup_btn_bg01);
				btnAuth_Women.setBackgroundResource( R.drawable.at_popup_btn_bg02);
			} else if( position == 2 ){
				btnAuth_Man.setBackgroundResource( R.drawable.at_popup_btn_bg02);
				btnAuth_Women.setBackgroundResource( R.drawable.at_popup_btn_bg01);
			}
		}
    }
	private boolean AuthResult = false;
	private String mcertificationNo = "", mcertificationTP = "";
	
	class setCustomer_AUTH extends AsyncTask<Integer, Integer, Integer> {
		
		@Override
		protected void onPreExecute() {
			try {
				if( !pd.isShowing() ) {
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

						long timestamp = System.currentTimeMillis();
						aaa[0] = mCipher.encrypt( Constance.SEED , Constance.MAC + "@" + timestamp ); 
						aaa[1] = String.valueOf( timestamp ).toString().trim();
						
						aaa[2] = mMobiledName; 
						aaa[3] = mJuminBirth;
						aaa[4] = mMobile_Number;
						aaa[5] = mcertificationTP; // 인증 타입  1: 본인 인증  2: 핸드폰 소유인증						
						aaa[6] = AuthGender; 

						AuthResult = GET_LibJSON.getMEMBER_CERTI_AUTH( aaa );

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
					try {
						pd.dismiss();
						pd.cancel();
						
						mDlgCutomerAuthDialog.dismiss();
						mDlgCutomerAuthDialog.cancel();
						
						if( !AuthResult ){
							dialogBuilder.setTitle("SalesForce");
							dialogBuilder.setMessage( "네트워크 오류입니다. 다시 시도 하시겠습니까?");
					        dialogBuilder.setPositiveButton( "확인" , AuthRetry_Network );
					        dialogBuilder.setNegativeButton("취소", null);
					        dialogBuilder.show();
						} else {
							if( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_AUTH_LIST.get( 0 ).resultCertification.equals("N") ){
								dialogBuilder.setTitle("SalesForce");
								dialogBuilder.setMessage( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_AUTH_LIST.get( 0 ).errorMsg );
						        dialogBuilder.setPositiveButton( "확인" , AuthRetry );
						        dialogBuilder.setNegativeButton("취소", null);
						        dialogBuilder.show();
							} else if( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_AUTH_LIST.get( 0 ).resultCertification.equals("Y") ){
								if( mcertificationTP.equals("1") ){
									
									setAuthDate();
									
									mDlgCutomerAuthDialog.dismiss();
									mDlgCutomerAuthDialog.cancel();
									
									Go_eFrom();
									
//									dialogBuilder.setTitle("SalesForce");
//									dialogBuilder.setMessage( "인증이 완료 되었습니다.");
//							        dialogBuilder.setPositiveButton( "확인" , Eform_GO );
//							        dialogBuilder.setNegativeButton("취소", null);
//							        dialogBuilder.show();
								} else if( mcertificationTP.equals("2") ){
									mHandler.removeMessages(MSG_CLOCK_LIMETE);
									
									mHandler.sendMessageDelayed( mHandler.obtainMessage( MSG_CLOCK_LIMETE ), ClockTimer_RETRYE_AUTHNUMBER);
									
									mHandler.removeMessages(MSG_CLOCK);
									
									mHandler.sendMessageDelayed( mHandler.obtainMessage( MSG_CLOCK ), ClockTimer);
									
									dialogBuilder.setTitle("SalesForce");
									dialogBuilder.setMessage( "인증번호가 발송 되었습니다.");
							        dialogBuilder.setPositiveButton( "확인" , null );
							        dialogBuilder.show();
								}
								
							} else {
								dialogBuilder.setTitle("SalesForce");
								dialogBuilder.setMessage( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_AUTH_LIST.get( 0 ).errorMsg );
						        dialogBuilder.setPositiveButton( "확인" , null );
						        dialogBuilder.show();
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
		@Override
		protected void onCancelled() {
			
		}
	}
	
	private void setAuthDate(){
		GregorianCalendar gc = new GregorianCalendar();
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd"); // 기본 데이타베이스 저장 타입
        Date d = gc.getTime(); // Date -> util 패키지
        String str = sf.format(d);
        
        OZVIEWER_CONSTANCE.mCertificationDate = str;
	}
	
	private DialogInterface.OnClickListener AuthRetry_Network = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			mDlgCutomerAuthDialog.show();
		}
	};

	private DialogInterface.OnClickListener AuthRetry = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			new setCustomer_AUTH().execute();
		}
	};
	
	private DialogInterface.OnClickListener Eform_GO = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface arg0, int arg1) {

			mDlgCutomerAuthDialog.dismiss();
			mDlgCutomerAuthDialog.cancel();
			
			Go_eFrom();

		}
	};
	
	private void Go_eFrom(){
		Intent intent = new Intent( mContext, OZViewerLocalActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("hp", argsCustomer );
		startActivity(intent);
		finish();
		
//		chargeConstance.resultCertification	= getParameterString ( nameArray,"resultCertification");
//		chargeConstance.csNm 				= mCipher.decrypt( Constance.SEED , getParameterString ( nameArray, "csNm") );
//		chargeConstance.certificationTp		= getParameterString ( nameArray, "certificationTp");
//		chargeConstance.csGender 			= getParameterString ( nameArray, "csGender");
//		chargeConstance.juminBirthday		= mCipher.decrypt( Constance.SEED , getParameterString ( nameArray, "juminBirthday") );
//		chargeConstance.mobileTelNo 		= mCipher.decrypt( Constance.SEED , getParameterString ( nameArray, "mobileTelNo") );
//		chargeConstance.certificationNo		= getParameterString ( nameArray, "certificationNo");
//		chargeConstance.errorCd				= getParameterString ( nameArray, "errorCd");
//		chargeConstance.errorMsg 			= getParameterString ( nameArray, "errorMsg");
//		chargeConstance.shopCd				= getParameterString ( nameArray, "shopCd");
//		chargeConstance.telecomCd			= getParameterString ( nameArray, "telecomCd");
//		chargeConstance.ci 					= getParameterString ( nameArray, "ci");
//		chargeConstance.di					= getParameterString ( nameArray, "di");
//        
//		CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_AUTH_LIST.add(chargeConstance);
	}
	
	
	private DlgeKolonDialog mDlgeKolonDialog;
	
	public class DlgeKolonDialog extends Dialog implements OnClickListener
    {
		private Button btnCustomerConfirm, btnClose, btnCustomerPhoneConfirm;
		
		public DlgeKolonDialog( Context context ) 
		{
			super(context);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			
			setContentView(R.layout.membership_ekolon_card);
			
			btnCustomerConfirm 			= (Button) findViewById( R.id.btnCustomerConfirm );
			btnClose 					= (Button) findViewById( R.id.btnClose );
			btnCustomerPhoneConfirm 	= (Button) findViewById( R.id.btnCustomerPhoneConfirm );

			
			btnCustomerConfirm.setOnClickListener( this );
			btnClose.setOnClickListener( this );
			btnCustomerPhoneConfirm.setOnClickListener( this );
		}

		@Override
		public void show() {
			// TODO Auto-generated method stub
			super.show();
			mcertificationTP = "2";
		}
		@Override
		public void onClick(View v) {
			if( v == btnCustomerPhoneConfirm ){
				mDlgPhoneAuth.show();
			} else if( v == btnCustomerConfirm ){
				mDlgCutomerAuthDialog.show();
			}
			
			dismiss();
			cancel();
		}
    }
	
	private int ClockTimer = 1000;
	
	private int ClockTimer_RETRYE_AUTHNUMBER = 5000;
	
	private final int MSG_CLOCK = 0;
	
	private final int MSG_CLOCK_LIMETE = 1;
	
	private int MaxTime = 300;
	
	private boolean TimeState = true;
	
	private final Handler mHandler = new Handler() {	
    	@Override
    	public void handleMessage(Message msg) {
    		switch(msg.what) {
    		case MSG_CLOCK :
    			int mMinute = 0;
    			int mSecond = 0;
    			
    			mMinute = MaxTime / 60;
    			
    			mSecond = MaxTime % 60;
    			
    			String mTime = "";
    			
    			if( mSecond < 10 ){
    				mTime = "0" + mMinute + " : 0" + mSecond;
    			} else {
    				mTime = "0" + mMinute + " : " + mSecond;
    			}
    			
    			if( mMinute == 0 && mSecond == 0 ){
    				TimeState = false;
    				mHandler.removeMessages(MSG_CLOCK);
    				MaxTime = 300;
    			} else {
    				mHandler.sendMessageDelayed( mHandler.obtainMessage( MSG_CLOCK ), ClockTimer);
    			}
    			
    			mDlgPhoneAuth.SetAuthtime( mTime );
    			MaxTime = MaxTime - 1;
    			break;
    		case MSG_CLOCK_LIMETE :
    			mDlgPhoneAuth.SetAuthBtnState( true );
    			break;
    		}
    	}
    };
    
	private DlgPhoneAuth mDlgPhoneAuth;
	
	public class DlgPhoneAuth extends Dialog implements OnClickListener
    {
		private Button btnAuth_Phone_NumberTrans, btnClose, btnConfirm, btnCancle;
		private EditText edit_Phone_Auth_Name, edit_Phone_Auth_phoneNumber, edit_Auth_Number;
		private EditText edit_Auth_Jumin_1, edit_Auth_Jumin_2, edit_Auth_Jumin_3;
		private TextView txtAuthTime;
		
		public DlgPhoneAuth( Context context ) 
		{
			super(context);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			
			setContentView(R.layout.membership_phone_authdialog);
			
			btnAuth_Phone_NumberTrans 	= (Button) findViewById( R.id.btnAuth_Phone_NumberTrans );
			btnClose 					= (Button) findViewById( R.id.btnClose );
			btnConfirm 					= (Button) findViewById( R.id.btnConfirm );
			btnCancle 					= (Button) findViewById( R.id.btnCancle );

			edit_Phone_Auth_Name 		= (EditText) findViewById( R.id.edit_Phone_Auth_Name );
			edit_Phone_Auth_phoneNumber = (EditText) findViewById( R.id.edit_Phone_Auth_phoneNumber );
			edit_Auth_Number 			= (EditText) findViewById( R.id.edit_Auth_Number );
			
			edit_Auth_Jumin_1 			= (EditText) findViewById( R.id.edit_Auth_Jumin_1 );
			edit_Auth_Jumin_2 			= (EditText) findViewById( R.id.edit_Auth_Jumin_2 );
			edit_Auth_Jumin_3 			= (EditText) findViewById( R.id.edit_Auth_Jumin_3 );
			
			
			txtAuthTime					= (TextView) findViewById( R.id.txtAuthTime );
			
			btnCancle.setOnClickListener( this );
			btnClose.setOnClickListener( this );
			btnConfirm.setOnClickListener( this );
			btnAuth_Phone_NumberTrans.setOnClickListener( this );
		}

		public void SetAuthBtnState( boolean state ){
			btnAuth_Phone_NumberTrans.setEnabled( state );
		}
		
		public void SetAuthtime( String mTime ){
			txtAuthTime.setText(mTime);
		}
		
		@Override
		public void show() {
			// TODO Auto-generated method stub
			super.show();
			mHandler.removeMessages(MSG_CLOCK_LIMETE);
			
			mHandler.removeMessages(MSG_CLOCK);
			
			edit_Phone_Auth_Name.setText("");
			edit_Phone_Auth_phoneNumber.setText("");
			edit_Auth_Number.setText("");
			edit_Auth_Jumin_1.setText("");
			edit_Auth_Jumin_2.setText("");
			edit_Auth_Jumin_3.setText("");
			txtAuthTime.setText("");
		}
		
		@Override
		public void cancel() {
			// TODO Auto-generated method stub
			super.cancel();
			mHandler.removeMessages(MSG_CLOCK_LIMETE);
			
			mHandler.removeMessages(MSG_CLOCK);
		}

		@Override
		public void dismiss() {
			// TODO Auto-generated method stub
			super.dismiss();
			
			mHandler.removeMessages(MSG_CLOCK_LIMETE);
			
			mHandler.removeMessages(MSG_CLOCK);
		}

		@Override
		public void onClick(View v) {
			if( v == btnClose || v == btnCancle ){
				dismiss();
				cancel();
			} else if( v == btnAuth_Phone_NumberTrans ){
				
				if( edit_Phone_Auth_Name.getText().toString().trim().length() > 0 ){
					if( edit_Phone_Auth_phoneNumber.getText().toString().trim().length() >= 10 ){
						
						if( edit_Auth_Jumin_1.getText().toString().trim().length() == 4 ){
							if( edit_Auth_Jumin_2.getText().toString().trim().length() == 2 ){
								if( edit_Auth_Jumin_3.getText().toString().trim().length() == 2 ){
									
									mJuminBirth = edit_Auth_Jumin_1.getText().toString().trim();
									mJuminBirth = mJuminBirth + edit_Auth_Jumin_2.getText().toString().trim();
									mJuminBirth = mJuminBirth + edit_Auth_Jumin_3.getText().toString().trim();
									
									getPhoneNumber( edit_Phone_Auth_phoneNumber.getText().toString().trim() );
									
									mMobiledName = edit_Phone_Auth_Name.getText().toString().trim();
									mMobile_Number = argsCustomer[0] + "-" + argsCustomer[1] + "-" + argsCustomer[2];
									new setCustomer_AUTH().execute();
									
									MaxTime = 300;
									
									SetAuthBtnState( false );
								} else {
									dialogBuilder.setTitle("SalesForce");
									dialogBuilder.setMessage( "일 입력을 확인해주세요. ex) 09");
							        dialogBuilder.setPositiveButton( "확인" , null );
							        dialogBuilder.show();
								}
							} else {
								dialogBuilder.setTitle("SalesForce");
								dialogBuilder.setMessage( "월 입력을 확인해주세요. ex) 09");
						        dialogBuilder.setPositiveButton( "확인" , null );
						        dialogBuilder.show();
							}
						} else {
							dialogBuilder.setTitle("SalesForce");
							dialogBuilder.setMessage( "년도 입력을 확인해주세요. ex) 1983");
					        dialogBuilder.setPositiveButton( "확인" , null );
					        dialogBuilder.show();
						}
						
					} else {
						dialogBuilder.setTitle("SalesForce");
						dialogBuilder.setMessage( "휴대폰번호를 올바르게 입력해주십시오.( 10~11자 )");
				        dialogBuilder.setPositiveButton( "확인" , null );
				        dialogBuilder.show();
					}
				} else {
					dialogBuilder.setTitle("SalesForce");
					dialogBuilder.setMessage( "성명을 입력해주세요.");
			        dialogBuilder.setPositiveButton( "확인" , null );
			        dialogBuilder.show();
				}
			} else if( v == btnConfirm ){
				HidekeyBoard.KeyboardHide(mContext, btnConfirm.getWindowToken() );
				if( TimeState ){
					if( edit_Auth_Number.getText().toString().trim().length() > 3 ){
						String Auth_Number = edit_Auth_Number.getText().toString().trim();
						if( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_AUTH_LIST.get( 0 ).certificationNo.equals( Auth_Number ) ){
							
							mHandler.removeMessages(MSG_CLOCK);
							
							setAuthDate();
							
							dismiss();
							cancel();
							
							mDlgCutomerAuthDialog.dismiss();
							mDlgCutomerAuthDialog.cancel();
							
							Go_eFrom();
							
//							dialogBuilder.setTitle("SalesForce");
//							dialogBuilder.setMessage( "인증이 완료 되었습니다.");
//					        dialogBuilder.setPositiveButton( "확인" , Eform_GO );
//					        dialogBuilder.setNegativeButton("취소", null);
//					        dialogBuilder.show();
						} else {
							dialogBuilder.setTitle("SalesForce");
							dialogBuilder.setMessage( "인증번호가 다릅니다. 다시 확인해주세요.");
					        dialogBuilder.setPositiveButton( "확인" , null );
					        dialogBuilder.show();
						}
					} else {
						dialogBuilder.setTitle("SalesForce");
						dialogBuilder.setMessage( "인증번호를 다시 입력해주세요.");
				        dialogBuilder.setPositiveButton( "확인" , null );
				        dialogBuilder.show();
					}
				} else {
					edit_Auth_Number.setText( "" );
					
					dialogBuilder.setTitle("SalesForce");
					dialogBuilder.setMessage( "인증번호를 재 발송해주십시오." );
			        dialogBuilder.setPositiveButton( "확인" , null );
			        dialogBuilder.show();
				}
			}
		}
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
			Intent intent = new Intent( mContext, Main_Page.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		} else if( v == btn_Mobile_Insert ) {
			mDlgMobileMembershipDialog.show();
		} else if( v == btnE_Kolon_Card_Insert ){
			mDlgeKolonDialog.show();
		} else if( v == btn_E_Kolon_Card_Intorduce ){
			setEKolonCard();
		} else if( v == btn_Brand_Grade_Intorduce ){
			setBrandGrade();
		} else if( v == btn_Customer_Search ){
			setCustomerSearch();
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
