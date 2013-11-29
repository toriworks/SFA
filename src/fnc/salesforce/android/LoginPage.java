package fnc.salesforce.android;

import fnc.salesforce.android.Constance.Constance;
import fnc.salesforce.android.LIB.CipherUtils;
import fnc.salesforce.android.LIB.Dialogtest;
import fnc.salesforce.android.LIB.DownloadFile;
import fnc.salesforce.android.LIB.KumaLog;
import fnc.salesforce.android.LIB.MySwitch;
import fnc.salesforce.android.LIB.SlideButton;
import fnc.salesforce.android.LIB.UpdateAPK;
import fnc.salesforce.android.LIB.MySwitch.OnChangeAttemptListener;
import fnc.salesforce.android.LIB.SharedPreferenceUtil;
import fnc.salesforce.android.LIB.libJSON;
import fnc.salesforce.android.LIB.libJSON_GET;
import fnc.salesforce.android.LIB.libUDID;
import fnc.salesforce.android.LIB.setLoginAlram;
import fnc.salesforce.android.Main_Page.setMainInformation;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class LoginPage extends Activity implements OnClickListener, OnChangeAttemptListener, OnCheckedChangeListener{

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if( viewFA_Notice.getVisibility() == View.VISIBLE ){
			setLogin();
		} else {
			Constance.LoginStateType = false;
			mAlram.releaseAlarm(mContext);
			android.os.Process.killProcess(android.os.Process.myPid());
		}
	}

	private UpdateAPK mUpdateApk;
	private ImageView imgCommandCheck, imgShopCheck;
	
	private EditText editLoginID, editLoginPass, editFA_Name, editFA_Phone;
	
	private Button btnLogin, btnFA_Input;
	
	private ProgressDialog pd;
	
	private libUDID lUDID = new libUDID();
	
	private AlertDialog.Builder dialogBuilder;
	
	private Context mContext;
	
	private View viewFA_Notice;
	
	private TableRow rowLoginType, rowUserChange;
	
	private TextView txtShopArea, txtHeadArea;
			
	private CheckBox check_ID_Save;
	
	private SlideButton sb;

	private LinearLayout slideContentLayout, slideContentLayout1111;
	
	private Button btnCancle, btnLogOut, btnUserChange;
	
	private boolean state = true;
	
	String mUserChange = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView( R.layout.login_page );
		
		mContext = this;
		
		Constance.TimeOverState = false;
		
		try {
			lUDID.Check_UDID( this );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		mUpdateApk = new UpdateAPK( this );
		
		pd = new ProgressDialog( this );
		
		DLGDD = new Dialogtest( this );
		
		dialogBuilder = new AlertDialog.Builder(this);
		
		imgCommandCheck 	= (ImageView ) findViewById( R.id.imgCommandCheck );
		imgShopCheck 		= (ImageView ) findViewById( R.id.imgShopCheck );

		editLoginID 	= (EditText ) findViewById( R.id.editLoginID );
		editLoginPass 	= (EditText ) findViewById( R.id.editLoginPass );

		editFA_Name 	= (EditText ) findViewById( R.id.editFA_Name );
		editFA_Phone 	= (EditText ) findViewById( R.id.editFA_Phone );
		
		btnLogin 		= (Button ) findViewById( R.id.btnLogin );
		btnFA_Input		= (Button ) findViewById( R.id.btnFA_Input );
		
		viewFA_Notice 	= (View) findViewById(R.id.viewFA_Notice);
		
		rowLoginType 	= (TableRow) findViewById(R.id.rowLoginType);
		
		txtShopArea 	= (TextView) findViewById(R.id.txtShopArea);
		txtHeadArea 	= (TextView) findViewById(R.id.txtHeadArea);
		
		check_ID_Save	= (CheckBox) findViewById(R.id.check_ID_Save);
		
		btnUserChange	= (Button ) findViewById( R.id.btnUserChange );
		btnLogOut		= (Button ) findViewById( R.id.btnLogOut );
		btnCancle		= (Button ) findViewById( R.id.btnCancle );
		
		rowUserChange	= (TableRow ) findViewById( R.id.rowUserChange );
		
		
		txtShopArea.setOnClickListener( this );
		txtHeadArea.setOnClickListener( this );
		
		btnFA_Input.setOnClickListener( this );
		btnLogin.setOnClickListener( this );
		
		btnUserChange.setOnClickListener( this );
		btnLogOut.setOnClickListener( this );
		btnCancle.setOnClickListener( this );
		
		sb = (SlideButton)findViewById(R.id.slideButton);
		
		slideContentLayout	= (LinearLayout)findViewById(R.id.slideContentLayout);
		slideContentLayout1111	= (LinearLayout)findViewById(R.id.slideContentLayout1111);
		
		Intent intent = getIntent();
		
        if( intent.getStringExtra("userChange") != null ){
        	mUserChange = intent.getStringExtra("userChange");
        } else {
        	mUserChange = "N";
        }
        
        if( mUserChange.equals("Y") ){
        	rowUserChange.setVisibility( View.VISIBLE );
        	btnFA_Input.setVisibility( View.GONE );
        	btnLogin.setVisibility( View.GONE );
        } else {
        	rowUserChange.setVisibility( View.GONE );
        	btnFA_Input.setVisibility( View.GONE );
        	btnLogin.setVisibility( View.VISIBLE );
        }
		
	    sb.setOnCheckChangedListner(new SlideButton.OnCheckChangedListner() {
	        @Override
	        public void onCheckChanged(View v, boolean isChecked) {
	        	setLoginType( isChecked );
	        		        	
	        	if( isChecked ){
	        		Constance.HEAD_LOGIN = false;
	        		slideContentLayout1111.setVisibility( View.VISIBLE);
	        	} else {
	        		Constance.HEAD_LOGIN = true;
	        		slideContentLayout1111.setVisibility( View.GONE);
	        	}
	        }
	    });
	    
	    slideContentLayout.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				state = true;
				sb.setChecked( state );
			}
		});
	    
	    slideContentLayout1111.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				state = false;
				sb.setChecked( state );
			}
		});
        
		try {
			LoginTypeState = SharedPreferenceUtil.getBooleanSharedPreference( this, "LoginTypeState");
		} catch (Exception e) {
			LoginTypeState = true;
			e.printStackTrace();
		}

		sb.setChecked( LoginTypeState);
		
		
		check_ID_Save.setOnCheckedChangeListener( new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if( isChecked ){
					SharedPreferenceUtil.putSharedPreference( mContext, "LOGIN_STATE", false);
				} else {
					SharedPreferenceUtil.putSharedPreference( mContext, "LOGIN_STATE", true);
				}
			}
		});
				
		try {
			if( !SharedPreferenceUtil.getBooleanSharedPreference( this , "LOGIN_STATE") ){
				check_ID_Save.setChecked( true );
				
				if( SharedPreferenceUtil.getSharedPreference( this, "LOGIN_ID" ) != null ) 
					editLoginID.setText( SharedPreferenceUtil.getSharedPreference( this, "LOGIN_ID" ) );
				
			} else {
				check_ID_Save.setChecked( false );
			}
		} catch (Exception e) {
			check_ID_Save.setChecked( true );
			SharedPreferenceUtil.putSharedPreference(this, "LOGIN_STATE", false);
			e.printStackTrace();
		}
		
				
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				new getVersionInformation().execute();
			}
		}, 200);
		
		
		editLoginID.addTextChangedListener(textWatcherInput);
		
		try {
			mAlram.releaseAlarm( mContext );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
    
	String sss = "";
	
	private int a = 1, mStart = 0;
	
	TextWatcher textWatcherInput = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			mStart = count;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			sss = s.toString().toUpperCase();
			
			if( LoginTypeState && a == 1){
				handler.post(new Runnable() {
					public void run() {
						a = 2;
						editLoginID.setText( sss  );
					}
				});
			} else {
				if( LoginTypeState ){
					editLoginID.setSelection( mStart );
				}
				a = 1;
			}
		}
		
	};
	
	
	
	private String getVersion()
	{
		try {
			PackageInfo pi= this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
			return pi.versionName;
		} catch (NameNotFoundException e) {
			return null;
		}
	}
	
	
	
	class getVersionInformation extends AsyncTask<Integer, Integer, Integer> {
		String VersionState = "Y";
		@Override
		protected void onPreExecute() {
			try {
				pd.setMessage("버전 확인중 입니다. 잠시만 기다려 주십시요.");
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
						if( !getVersion().equals( GET_LibJSON.getVersion("").toString().trim() )){
							VersionState = "F";
						} else {
							VersionState = "Y";
						}
						
					} catch (Exception e) {
						VersionState = "N";
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
					pd.dismiss();
					pd.cancel();
					if( VersionState.equals("F") ){
						dialogBuilder.setTitle("SalesForce");
						dialogBuilder.setMessage( "새로운 버전이 있습니다. 다운 받으시겠습니까?");
				        dialogBuilder.setPositiveButton( "확인" , Retry_Yes );
				        dialogBuilder.setNegativeButton("취소", null);
				        dialogBuilder.show();
						
					}
				}
			});
		}
		@Override
		protected void onCancelled() {
		}
	}
	
	private DialogInterface.OnClickListener Retry_Yes = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			mUpdateApk.getFile();
		}
	};

	@Override
	public void onChangeAttempted(boolean isChecked) {
//		Log.d(TAG,"onChangeAttemped(checked = "+isChecked+")");
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		
	}
	
	private boolean LoginTypeState = false;
	
	private void setLoginType( boolean state ){
		LoginTypeState = state;
		SharedPreferenceUtil.putSharedPreference( mContext , "LoginTypeState", state);
		
		if( !state ){
			imgCommandCheck.setBackgroundResource( R.drawable.at_logincheck_o );
			imgShopCheck.setBackgroundResource( R.drawable.at_logincheck_n );
		} else {
			imgCommandCheck.setBackgroundResource( R.drawable.at_logincheck_n );
			imgShopCheck.setBackgroundResource( R.drawable.at_logincheck_o );
		}
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

	private Dialogtest DLGDD;
	
	@Override
	public void onClick(View v) {
		if( v == btnLogin || v == btnUserChange ){
			if( editLoginID != null && editLoginID.getText().length() > 0 
					&& editLoginPass != null && editLoginPass.getText().length() > 0){
				
				Constance.LOGIN_PASSWORD = editLoginPass.getText().toString().trim();
				
				new setLogin().execute();
			} else {
				dialogBuilder.setTitle("알림");
				dialogBuilder.setMessage( "ID 및 Password 를 확인해주세요." );
		        dialogBuilder.setPositiveButton( "확인" , null);
		        dialogBuilder.show();
			}
		} else if( v == btnFA_Input ){
			if( editFA_Name != null && editFA_Name.getText().length() > 0 
					&& editFA_Phone != null && editFA_Phone.getText().length() > 8){
				new setFA_AUTH().execute();
			} else {
				dialogBuilder.setTitle("알림");
				dialogBuilder.setMessage( "이름 및 햄드폰 번호 를 확인해주세요." );
		        dialogBuilder.setPositiveButton( "확인" , null);
		        dialogBuilder.show();
			}
		} else if( v == txtShopArea ){
			LoginTypeState = true;
			sb.setChecked( true );
		} else if( v == txtHeadArea ){
			sb.setChecked( false );
			LoginTypeState = false;
		} else if( v == btnCancle ){
			Intent intent = new Intent( mContext, Main_Page.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("brandCd", Constance.BEANDCD );
			startActivity(intent);
			finish();
		} else if( v == btnLogOut ){
			Constance.LoginStateType = false;
			mAlram.releaseAlarm(mContext);
			finish();
		}
	}
	
	
	private libJSON LibJSON = new libJSON();
	private libJSON_GET GET_LibJSON = new libJSON_GET();
	
	private Handler handler = new Handler();
	
	private CipherUtils mCipher = new CipherUtils();
	
	private int verValue = 0, verMax = 1;
	
	private String[] LoginState = new String[2];
	
	class setLogin extends AsyncTask<Integer, Integer, Integer> {
		@Override
		protected void onPreExecute() {
			try {
				pd.setMessage("로그인 중 입니다. 잠시만 기다려 주십시요.");
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
						if( !LoginTypeState ){
							String[] arrParametr = new String[7];
							
							arrParametr[0] =  editLoginID.getText().toString().trim();
							arrParametr[1] =  editLoginPass.getText().toString().trim();
							long timestamp = System.currentTimeMillis();
							
							arrParametr[2] = mCipher.encrypt( Constance.SEED , Constance.MAC + "@" + timestamp ); 
							arrParametr[3] = String.valueOf( timestamp ).toString().trim(); 
							
//							GET_LibJSON.getREMOVE_DEVICE(arrParametr);
							
							LoginState = GET_LibJSON.getHEAD_OF_LOGIN(  arrParametr );

							if( !LoginState[0].equals("200") ){
								if( LoginState[0].equals("401.101") ){
									String[] arrDeviceINPut = new String[3];
									arrDeviceINPut[0] = arrParametr[2];
									arrDeviceINPut[1] = arrParametr[3];
									arrDeviceINPut[2] = arrParametr[0];
									
									LoginState = LibJSON.getDEVICE_INPUT( arrDeviceINPut );
									
									if( LoginState[0].equals("200") ){
										LoginState = GET_LibJSON.getHEAD_OF_LOGIN(  arrParametr );
									}
								}
							}
						} else {
							
							String[] arrParametr = new String[7];
							
							arrParametr[0] = editLoginID.getText().toString().trim();
							arrParametr[1] =  editLoginPass.getText().toString().trim();
							long timestamp = System.currentTimeMillis();
							
							arrParametr[2] = mCipher.encrypt( Constance.SEED , Constance.MAC + "@" + timestamp ); 
							arrParametr[3] = String.valueOf( timestamp ).toString().trim(); 
							
//							GET_LibJSON.getREMOVE_DEVICE(arrParametr);
							
							LoginState = GET_LibJSON.getSHOP_LOGIN(  arrParametr );

							if( !LoginState[0].equals("200") ){
								if( LoginState[0].equals("401.101") ){
									String[] arrDeviceINPut = new String[3];
									arrDeviceINPut[0] = arrParametr[2];
									arrDeviceINPut[1] = arrParametr[3];
									arrDeviceINPut[2] = arrParametr[0];
									LoginState = LibJSON.getDEVICE_INPUT_SHOP( arrDeviceINPut );
								}
							}
							
						}
						
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
					
					pd.dismiss();
					pd.cancel();
					if( !LoginState[0].equals("200") ){
						dialogBuilder.setTitle("알림");
						dialogBuilder.setMessage( LoginState[1] );
						if( LoginState[0].equals("401.100") ){
							dialogBuilder.setPositiveButton( "확인" , FA_Error);
						} else {
							dialogBuilder.setPositiveButton( "확인" , null);
						}
				        dialogBuilder.show();
					} else {
						Constance.USER_ID = editLoginID.getText().toString().trim();
//						dialogBuilder.setTitle("알림");
//						dialogBuilder.setMessage( "로그인 성공");
//				        dialogBuilder.setPositiveButton( "확인" , yesButtonClickListener );
//				        dialogBuilder.show();
						
						if( !SharedPreferenceUtil.getBooleanSharedPreference( mContext , "LOGIN_STATE") ){
							SharedPreferenceUtil.putSharedPreference(mContext, "LOGIN_ID", editLoginID.getText().toString().trim() );
						} else {
							SharedPreferenceUtil.putSharedPreference(mContext, "LOGIN_ID", "" );
						}
						
						mAlram.setAlarm( mContext, setTimer );
						
						Intent intent = new Intent( mContext, BrandChoice.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						finish();
					}
				}
			});
		}
		@Override
		protected void onCancelled() {
		}
	}
	
	private int setTimer = 12;
	
	private setLoginAlram mAlram = new setLoginAlram(); 
	
	class setFA_AUTH extends AsyncTask<Integer, Integer, Integer> {
		@Override
		protected void onPreExecute() {
			try {
				pd.setMessage("FA정보 인증 중 입니다. 잠시만 기다려 주십시요.");
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
						String[] arrParametr = new String[6];
						
						long timestamp = System.currentTimeMillis();
						
						arrParametr[0] = mCipher.encrypt( Constance.SEED , Constance.MAC + "@" + timestamp ); 
						arrParametr[1] = String.valueOf( timestamp ).toString().trim(); 
						
						arrParametr[2] = editFA_Name.getText().toString().trim();
						
						String[] aaa = PhoneNumber( editFA_Phone.getText().toString().trim() );
						
						arrParametr[3] = aaa[0];
						arrParametr[4] = aaa[1];
						arrParametr[5] = aaa[2];
						
						LoginState = LibJSON.getFA_DEVICE_INPUT(  arrParametr );

						if( LoginState[0].equals("200") ){
							String[] arrLoginParametr = new String[7];
							
							arrLoginParametr[0] = editLoginID.getText().toString().trim();
							arrLoginParametr[1] =  editLoginPass.getText().toString().trim();
							timestamp = System.currentTimeMillis();
							
							arrLoginParametr[2] = mCipher.encrypt( Constance.SEED , Constance.MAC + "@" + timestamp ); 
							arrLoginParametr[3] = String.valueOf( timestamp ).toString().trim(); 
							
							LoginState = GET_LibJSON.getSHOP_LOGIN(  arrLoginParametr );
						}
						
						
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
					
					pd.dismiss();
					pd.cancel();
					if( !LoginState[0].equals("200") ){
						dialogBuilder.setTitle("알림");
						dialogBuilder.setMessage( LoginState[1] );
						if( LoginState[0].equals("401.100") ){
							dialogBuilder.setPositiveButton( "확인" , FA_Error);
						} else {
							dialogBuilder.setPositiveButton( "확인" , null);
						}
				        dialogBuilder.show();
					} else {
						if( !SharedPreferenceUtil.getBooleanSharedPreference( mContext , "LOGIN_STATE") ){
							SharedPreferenceUtil.putSharedPreference(mContext, "LOGIN_ID", editLoginID.getText().toString().trim() );
						} else {
							SharedPreferenceUtil.putSharedPreference(mContext, "LOGIN_ID", "" );
						}
						
						dialogBuilder.setTitle("알림");
						dialogBuilder.setMessage( "로그인 성공");
				        dialogBuilder.setPositiveButton( "확인" , yesButtonClickListener );
				        dialogBuilder.show();
					}
				}
			});
		}
		@Override
		protected void onCancelled() {
		}
	}
	
	String[] arrPhone = new String[3];
	
	private String[] PhoneNumber(String strNum){
		String strPhone = "";
		
		if( strNum.length() == 10 ){
			//true 서울
			boolean AreaState = false;
			
			for( int i = 0; i < 2; i++){			
				strPhone += strNum.charAt(i);
				if( i == 1){
					if( !strPhone.equals("02") ){
						AreaState = false;
					} else{
						AreaState = true;
					} 
				}
			}
			strPhone = "";
			if( !AreaState ){
				for( int i = 0; i < 10; i++){			
					strPhone += strNum.charAt(i);
					if( i == 2){
						arrPhone[0] = strPhone;
						strPhone = "";
					} else if( i == 5){
						arrPhone[1] = strPhone;
						strPhone = "";
					} else if( i == 9){
						arrPhone[2] = strPhone;
						strPhone = "";
					}
				}
			} else {
				for( int i = 0; i < 10; i++){			
					strPhone += strNum.charAt(i);
					if( i == 1){
						arrPhone[0] = strPhone;
						strPhone = "";
					} else if( i == 5){
						arrPhone[1] = strPhone;
						strPhone = "";
					} else if( i == 9){
						arrPhone[2] = strPhone;
						strPhone = "";
					}
				}
			}
			
			
		} else if( strNum.length() == 9 ){
			for( int i = 0; i < 9; i++){			
				strPhone += strNum.charAt(i);
				if( i == 1){
					arrPhone[0] = strPhone;
					strPhone = "";
				} else if( i == 4){
					arrPhone[1] = strPhone;
					strPhone = "";
				} else if( i == 8){
					arrPhone[2] = strPhone;
					strPhone = "";
				}
			}
		} else {
			for( int i = 0; i < 11; i++){			
				strPhone += strNum.charAt(i);
				if( i == 2){
					arrPhone[0] = strPhone;
					strPhone = "";
				} else if( i == 6){
					arrPhone[1] = strPhone;
					strPhone = "";
				} else if( i == 10){
					arrPhone[2] = strPhone;
					strPhone = "";
				}
			}
		}
		return arrPhone;
	}
	
	private void setFaAuth(){
		rowUserChange.setVisibility( View.GONE );
		editLoginID.setVisibility( View.GONE );
		editLoginPass.setVisibility( View.GONE );
		btnLogin.setVisibility( View.GONE );
		rowLoginType.setVisibility( View.GONE );
		
		viewFA_Notice.setVisibility( View.VISIBLE );
		btnFA_Input.setVisibility( View.VISIBLE );
		editFA_Name.setVisibility( View.VISIBLE );
		editFA_Phone.setVisibility( View.VISIBLE );
	}
	
	private void setLogin(){
		
		if( mUserChange.equals("Y") ){
        	rowUserChange.setVisibility( View.VISIBLE );
        	btnLogin.setVisibility( View.GONE );        	
        } else {
        	rowUserChange.setVisibility( View.GONE );
        	btnLogin.setVisibility( View.VISIBLE );
        }
		editLoginID.setVisibility( View.VISIBLE );
		editLoginPass.setVisibility( View.VISIBLE );
		
		editLoginID.setText("");
		editLoginPass.setText("");
		
		rowLoginType.setVisibility( View.VISIBLE );
		
		viewFA_Notice.setVisibility( View.GONE );
		btnFA_Input.setVisibility( View.GONE );
		editFA_Name.setVisibility( View.GONE );
		editFA_Phone.setVisibility( View.GONE );
	}
	
	private DialogInterface.OnClickListener FA_Error = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface arg0, int arg1) {			
			setFaAuth();
		}
	};
	
	private DialogInterface.OnClickListener yesButtonClickListener = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			
			mAlram.setAlarm( mContext, setTimer );
			
			Intent intent = new Intent( mContext, BrandChoice.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		}
	};
	
}
