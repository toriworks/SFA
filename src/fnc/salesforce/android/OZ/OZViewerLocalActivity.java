package fnc.salesforce.android.OZ;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.http.util.ByteArrayBuffer;
import org.xmlpull.v1.XmlPullParser;

import fnc.salesforce.android.LoginPage;
import fnc.salesforce.android.Main_Page;
import fnc.salesforce.android.R;
import fnc.salesforce.android.Activity.MemberShip_Main;
import fnc.salesforce.android.Activity.MemberShip_Main.DlgeKolonDialog;
import fnc.salesforce.android.Constance.CONTENTS_PAGE_ALL_LIST_CONSTANCE;
import fnc.salesforce.android.Constance.Constance;
import fnc.salesforce.android.Constance.OZVIEWER_CONSTANCE;
import fnc.salesforce.android.LIB.CipherUtils;
import fnc.salesforce.android.LIB.DownloadFile;
import fnc.salesforce.android.LIB.KumaLog;
import fnc.salesforce.android.LIB.ZABARARequest;
import fnc.salesforce.android.LIB.libJSON;
import fnc.salesforce.android.LIB.libJSON_GET;
import fnc.salesforce.android.LIB.setXML_StringBurffer;
import fnc.salesforce.android.Membership.MemberShip_zipAdabter;

import oz.api.OZRTextBoxCmd;
import oz.api.OZReportAPI;
import oz.api.OZReportCommandListener;
import oz.api.OZReportViewer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.SyncStateContract.Constants;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class OZViewerLocalActivity extends Activity {	
    /** Called when the activity is first created. */
    static 
    {
    	try {
        	System.loadLibrary("ozrv");			
		} catch (Throwable e) {
			Log.e("lib", e.getLocalizedMessage(), e);
		}
    }
    
    @Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
    	Intent intent = new Intent( mContext, MemberShip_Main.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
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

	OZReportViewer viewer = null; // 
    
    private Button btn_Upload;
    
    private FrameLayout layoutE_Form;
    
    private AlertDialog.Builder dialogBuilder;
    
    private String mFileName = "eKOLON.ozd";
    
    private Context mContext;
    
    private ImageView btnBack_Page;
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
        setContentView( R.layout.eform_main );
        
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mContext = this;
        
        btn_Upload = (Button ) findViewById( R.id.btn_Upload );
        layoutE_Form = (FrameLayout ) findViewById( R.id.layoutE_Form );
        btnBack_Page = (ImageView ) findViewById( R.id.btnBack_Page );
		pd = new ProgressDialog( this );
		
		mDlgEForm_ZipCode = new DlgEForm_ZipCode( this );
		
		dialogBuilder = new AlertDialog.Builder(this);
		
		
		Intent intent = getIntent();

	    if( intent.getStringArrayExtra("hp") != null ){
	    	arrMobileNo = intent.getStringArrayExtra("hp");
        }
	    
	    btnBack_Page.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent( mContext, MemberShip_Main.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
			}
		} );
	    
		setOzdViewer();
//		handler.postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				new setViewer().execute();
//			}
//		}, 10);
    }
	
	private String[] arrMobileNo = new String[3];

	private void setOzdViewer(){
//		String params = "connection.openfile=/sdcard/ozviewer/ozd/smart.ozd";
		String params = "";
		
		AssetManager am = this.getResources().getAssets();
		
		String mFolder = Constance.FILEPATH + "/eForm/";
		File folder = new File( mFolder );
		 
		DeleteDir(mFolder);
		 
		if (!folder.exists()) {
			folder.mkdirs();
		}
		
        String FilePath = mFolder + mFileName;
        File file = new File ( FilePath );
       
		try {
			if( !file.exists() ) {
				   
				InputStream is = am.open("ozd/" + mFileName);
				
				long filesize = is.available();
	            
	            BufferedInputStream bis = new BufferedInputStream(is);
				
				ByteArrayBuffer baf = new ByteArrayBuffer( (int)filesize );
				
				int curent = 0;
				
				while(( curent = bis.read()) != -1 ){
					baf.append((byte) curent );
				}
				
				FileOutputStream fos = new FileOutputStream( file );
				
				fos.write( baf.toByteArray() );
				fos.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
		
		String ozdFileName = FilePath;

		
		if (ozdFileName != null)
		{
			params = "connection.openfile="+ozdFileName;
		} 
		
		params +="\n" + "information.debug=true";
		params +="\n" + "eform.signpad_type=zoom";
		params +="\n" + "eform.signpad_zoom=1500";
		params +="\n" + "viewer.usetoolbar=true";
		  
		params +="\n" + "export.format=jpg";
		params +="\n" + "export.mode=silent";
		params +="\n" + "export.confirmsave=false";
		  
		params += "\n" + "font.fontnames=font1";
		params += "\n" + "font.font1.url=res://NanumGothic.ttf";
		params += "\n" + "font.font1.name=나눔고딕"; 

		OZReportCommandListener listener = new OZReportCommandListener() {
			@Override
			public void OZUserActionCommand(String type, String attrs) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void OZProgressCommand(String step, String state, String reportname) {
				// TODO Auto-generated method stub
				Log.d("OZViewer", String.format("[Event] OZProgressCommand: %s, %s, %s", step, state, reportname));				
			}
			
			@Override
			public void OZPrintCommand(String msg, String code, String reportname,
					String printername, String printcopy, String printpages,
					String printrange, String username, String printerdrivername,
					String printpagesrange) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void OZPostCommand(String cmd, String msg) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void OZMailCommand(String code) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void OZLinkCommand(String docIndex, String compName, String tag,
					String value, String mouseButton) {
				// TODO Auto-generated method stub
				
				if (compName.equals("KICA"))
				{
					Log.d("OZViewer","KICA----------------------------");
				}else
				{
					Log.d("OZViewer","docIndex="+docIndex+"  compName="+compName+" tag="+tag+" valule="+value);
				}
			}
			
			@Override
			public void OZExportCommand(String code, String path, String filename,
					String pagecount, String filenames) {
				// TODO Auto-generated method stub
				
				
				
			}
			
			@Override
			public void OZExitCommand() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void OZErrorCommand(String code, String errmsg, String detailmsg,
					String reportname) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void OZCommand(String code, String args) {
				// TODO Auto-generated method stub
				Log.d("OZViewer", String.format("[Event] OZCommand: %s, %s", code, args));
			}
			
			@Override
			public void OZBankBookPrintCommand(String data) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public String OZUserEvent(String mField, String arg1, String arg2) {
				// TODO Auto-generated method stub
				if( mField.equals("btn_zip") ){
					mDlgEForm_ZipCode.show();
				}
				
				return "Good!!";
			}

			@Override
			public boolean OZWillChangeIndex_Paging(int arg0, int arg1) {
				// TODO Auto-generated method stub
				return true;
			}

			@Override
			public void OZReportChangeCommand(String arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void OZPageBindCommand(String arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void OZPageChangeCommand(String arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void OZTextBoxCommand(OZRTextBoxCmd arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}

		}; 
		
//		System.setProperty("R.layout.oz_sign_pad", String.valueOf(R.layout.oz_sign_pad));
//		System.setProperty("R.id.SignPadRelativeLayout", String.valueOf(oz.viewer.app.R.id.SignPadRelativeLayout));
//		System.setProperty("R.id.OZSignPadInitButton", String.valueOf(oz.viewer.app.R.id.OZSignPadInitButton));
//		System.setProperty("R.id.OZSignPadConfirmButton", String.valueOf(oz.viewer.app.R.id.OZSignPadConfirmButton));

		params += "\n" + "comment.all=true";
		params += "\n" + "comment.autohide=false";

		btn_Upload.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if( viewer.GetInformation("INPUT_CHECK_VALIDITY").toString().trim().equals("valid")){
					
					
                    String res = viewer.GetInformation("INPUT_XML_ALL");
                    
                    KumaLog.LogD("test_1"," res : " + res );
                    searchXml( res );
                    
                    viewer.Document().SetGlobal("exportN", "f");
                    
                    viewer.GetInformation("INPUT_TRIGGER_CLICK");
                    
                    viewer.ScriptEx("save", "export.format=jpg;export.mode=silent;export.confirmsave=false;export.path="+ mFilePAth + ";export.filename=OriginalImage", ";");
                    
                    Constance.UPLOAD_FILE_NAME_ORIGINAL = "OriginalImage.jpg";
                    
                    String filepath = mFilePAth + "/" + Constance.UPLOAD_FILE_NAME_ORIGINAL;                                        
                    
                    viewer.Document().SetGlobal("exportN", "s");
                    
                    viewer.GetInformation("INPUT_TRIGGER_CLICK");
                    
                    viewer.ScriptEx("save", "export.format=jpg;export.mode=silent;export.confirmsave=false;export.path="+ mFilePAth + ";export.filename=MaskImage", ";");
					
                    Constance.UPLOAD_FILE_NAME_MASK = "MaskImage.jpg";                    
					
					String filepath_Mask = mFilePAth + "/" + Constance.UPLOAD_FILE_NAME_MASK;
					
					File file = new File( filepath );
					
					File file_Mask = new File( filepath_Mask );
					
					if ( file.exists() && file_Mask.exists() ){
						new setMemberImageUpload().execute();
					} else {
						dialogBuilder.setTitle("SalesForce");
						dialogBuilder.setMessage( "파일 저장에 실패하였습니다.");
				        dialogBuilder.setPositiveButton( "확인" , null );
				        dialogBuilder.show();
					}
				}
			}
		});

		
		viewer = OZReportAPI.createViewer(layoutE_Form, listener, params);
		
		viewer.Document().SetGlobal("hp1", arrMobileNo[0] );
		viewer.Document().SetGlobal("hp2", arrMobileNo[1] );
		viewer.Document().SetGlobal("hp3", arrMobileNo[2] );

		String mJuminBirthDay = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_AUTH_LIST.get(0).juminBirthday.substring(0,4);
		mJuminBirthDay = mJuminBirthDay + "-" + CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_AUTH_LIST.get(0).juminBirthday.substring(4,6);
		mJuminBirthDay = mJuminBirthDay + "-" + CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_AUTH_LIST.get(0).juminBirthday.substring(6,8);
		
		viewer.Document().SetGlobal("birth_dt", mJuminBirthDay );
		
		viewer.Document().SetGlobal("rbrth_dt", mJuminBirthDay );
		
		viewer.Document().SetGlobal("now_date", setAuthDate() );
		
		viewer.Document().SetGlobal("name", CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_AUTH_LIST.get(0).csNm );
		
		if( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_AUTH_LIST.get( 0 ).certificationTp.equals("1") ){
			viewer.Document().SetGlobal("auth_code", "0");
						
			if( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_AUTH_LIST.get(0).csGender.equals("1") ){
				viewer.Document().SetGlobal("gender", "0");
			} else {
				viewer.Document().SetGlobal("gender", "1");
			}			
		} else {
			
			viewer.Document().SetGlobal("auth_code", "1");
		}
		
		viewer.GetInformation("INPUT_TRIGGER_CLICK");

	}
	
	private String setAuthDate(){
		GregorianCalendar gc = new GregorianCalendar();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd"); // 기본 데이타베이스 저장 타입
        Date d = gc.getTime(); // Date -> util 패키지
        String str = sf.format(d);
        
        return str;
	}
	
	
	String mFilePAth = Constance.FILEPATH + "/ozviewer";
	
	class setViewer extends AsyncTask<Integer, Integer, Integer> {
		@Override
		protected void onPreExecute() {
			try {

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

						setOzdViewer();
						
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
					
				}
			});
		}
		@Override
		protected void onCancelled() {
		}
	}
	
	private void DeleteDir(String path) { 
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
	
	
	
//	chargeConstance.areaCd 	= getParameterString ( nameArray,j,"areaCd");
//	chargeConstance.areaNm 	= getParameterString ( nameArray, j,"areaNm");
	
	
	
	public void searchXml(String mXML) {
		String sTag;
		
		OZVIEWER_CONSTANCE chargeConstance = new OZVIEWER_CONSTANCE();
		OZVIEWER_CONSTANCE.ARR_OZVIEWER_XML_LIST.clear();
		try {
			XmlPullParser xpp =  ZABARARequest.Return_XML( mXML );
			int eventType = xpp.getEventType();

			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_DOCUMENT) {
					Log.e("test_1", "START_DOCUMENT");
				} else if (eventType == XmlPullParser.END_DOCUMENT) {
					Log.e("test_1", "END_DOCUMENT");
				} else if (eventType == XmlPullParser.START_TAG) {
					sTag = xpp.getName();
					
					Log.e("test_1", "sTag : " + sTag );
					if (sTag.equals("hp1")) {
						chargeConstance.hp1 = xpp.nextText();
					} else if (sTag.equals("hp2")) {
						chargeConstance.hp2 = xpp.nextText();
					} else if (sTag.equals("hp3")) {
						chargeConstance.hp3 = xpp.nextText();
					} else if (sTag.equals("crd1")) {
						chargeConstance.crd1 = xpp.nextText();
					} else if (sTag.equals("crd2")) {
						chargeConstance.crd2 = xpp.nextText();
					} else if (sTag.equals("crd3")) {
						chargeConstance.crd3 = xpp.nextText();
					} else if (sTag.equals("zip")) {
						chargeConstance.zip = xpp.nextText();
					} else if (sTag.equals("address")) {
						chargeConstance.address = xpp.nextText();
					} else if (sTag.equals("pwd")) {
						chargeConstance.pwd = xpp.nextText();
					} else if (sTag.equals("mgr_dt")) {
						chargeConstance.mgr_dt = xpp.nextText();
					} else if (sTag.equals("email1")) {
						chargeConstance.email1 = xpp.nextText();
					} else if (sTag.equals("email2")) {
						chargeConstance.email2 = xpp.nextText();
					} else if (sTag.equals("rbrth_dt")) {
						chargeConstance.rbrth_dt = xpp.nextText();
					} else if (sTag.equals("birth_dt")) {
						chargeConstance.birth_dt = xpp.nextText();
					} else if (sTag.equals("tel1")) {
						chargeConstance.tel1 = xpp.nextText();
					} else if (sTag.equals("tel2")) {
						chargeConstance.tel2 = xpp.nextText();
					} else if (sTag.equals("tel3")) {
						chargeConstance.tel3 = xpp.nextText();
					} else if (sTag.equals("now_date")) {
						chargeConstance.now_date = xpp.nextText();
					} else if (sTag.equals("autogrp")) {
						chargeConstance.autogrp = xpp.nextText();
					} else if (sTag.equals("name")) {
						chargeConstance.name = xpp.nextText();
					} else if (sTag.equals("gender")) {
						chargeConstance.gender = xpp.nextText();
					} else if (sTag.equals("hp_cp")) {
						chargeConstance.hp_cp = xpp.nextText();
					} else if (sTag.equals("msg_yn")) {
						chargeConstance.msg_yn = xpp.nextText();
					} else if (sTag.equals("zip_type")) {
						chargeConstance.zip_type = xpp.nextText();
					} else if (sTag.equals("mail_yn")) {
						chargeConstance.mail_yn = xpp.nextText();
					} else if (sTag.equals("mgr_yn")) {
						chargeConstance.mgr_yn = xpp.nextText();
					} else if (sTag.equals("email_yn")) {
						chargeConstance.email_yn = xpp.nextText();
					} else if (sTag.equals("solar_yn")) {
						chargeConstance.solar_yn = xpp.nextText();
					} else if (sTag.equals("agr1_yn")) {
						chargeConstance.agr1_yn = xpp.nextText();
					} else if (sTag.equals("agr2_yn")) {
						chargeConstance.agr2_yn = xpp.nextText();
					} else if (sTag.equals("crd0")) {
						chargeConstance.crd0 = xpp.nextText();
					} else if (sTag.equals("now_date")) {
						chargeConstance.now_date = xpp.nextText();
					}
					
				} else if (eventType == XmlPullParser.END_TAG) {
					sTag = xpp.getName();
					if (sTag.equals("ozform")) {
						Log.e("test_1", " ozform_END " );
						OZVIEWER_CONSTANCE.ARR_OZVIEWER_XML_LIST.add(chargeConstance);
					}
				} else if (eventType == XmlPullParser.TEXT) {
					// System.out.println("Text "+xpp.getText());
				}
				
				eventType = xpp.next();
			}
		} catch (Exception e) {
			Log.e("Main", "searchXml Error : " + e);
		}
	}
	
	private DlgEForm_ZipCode mDlgEForm_ZipCode;
	
	private String mZipCodeSearchStr = "";
	
	
	
	public class DlgEForm_ZipCode extends Dialog implements OnClickListener
    {
		private Button btnConfirm, btnClose, btnCancle, btn_ZipSearch;
		private EditText edit_Area_Name, edit_Addr_Detail;
		private TextView edit_zip_Code, edit_Addr;
		private ListView listView;
		
		private int zipPosition = 0;
		
		private MemberShip_zipAdabter mZipAdabter = null;
		
		public DlgEForm_ZipCode( Context context ) 
		{
			super(context);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			
			setContentView(R.layout.membership_zip_code_dialog );
			
			btnConfirm 		= (Button) findViewById( R.id.btnConfirm );
			btnClose 		= (Button) findViewById( R.id.btnClose );
			btnCancle 		= (Button) findViewById( R.id.btnCancle );
			btn_ZipSearch	= (Button) findViewById( R.id.btn_ZipSearch );
			
			edit_zip_Code 	= (TextView) findViewById( R.id.edit_zip_Code );
			edit_Addr		= (TextView) findViewById( R.id.edit_Addr );
			
			edit_Area_Name 		= (EditText) findViewById( R.id.edit_Area_Name );
			edit_Addr_Detail	= (EditText) findViewById( R.id.edit_Addr_Detail );
			
			listView			= (ListView) findViewById( R.id.listView );
			
			mZipAdabter = new MemberShip_zipAdabter( context );
			
			listView.setAdapter( mZipAdabter );
			
			listView.setOnItemClickListener( new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					zipPosition = arg2;
					edit_zip_Code.setText( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_ZIPCODE_LIST.get(zipPosition).postNo );
					edit_Addr.setText( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_ZIPCODE_LIST.get(zipPosition).postArea );
				}
			});
			
			btnConfirm.setOnClickListener( this );
			btnClose.setOnClickListener( this );
			btnCancle.setOnClickListener( this );
			btn_ZipSearch.setOnClickListener( this );
		}

		public void RefreshList(){
			mZipAdabter.notifyDataSetChanged();
		}
		
		@Override
		public void show() {
			// TODO Auto-generated method stub
			super.show();
			mZipCodeSearchStr = "";
			zipPosition = 0;
			edit_zip_Code.setText( "" );
			edit_Addr.setText( "" );
			edit_Area_Name.setText( "" );
			edit_Addr_Detail.setText( "" );
			CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_ZIPCODE_LIST.clear();
			RefreshList();
		}
		
		@Override
		public void onClick(View v) {
			
			if( v == btnConfirm ){
				dismiss();
				cancel();
				 try {
					 viewer.Document().SetGlobal("zip", CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_ZIPCODE_LIST.get(zipPosition).postNo );
					 String FullAddress = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_ZIPCODE_LIST.get(zipPosition).postArea
							 + " " + edit_Addr_Detail.getText().toString().trim();
					 viewer.Document().SetGlobal("address", FullAddress );
					 viewer.GetInformation("INPUT_TRIGGER_CLICK_AT=0");
				 } catch (Exception e) {
					 e.printStackTrace();
				 }
			} else if( v == btnCancle || v == btnClose ){
				dismiss();
				cancel();
			} else if( v == btn_ZipSearch ){
				if( edit_Area_Name.getText().toString().trim().length() > 0 ){					
					mZipCodeSearchStr = edit_Area_Name.getText().toString().trim();
					new setZipCodeInformation().execute();
				} else {
					dialogBuilder.setTitle("SalesForce");
					dialogBuilder.setMessage( "읍,면,동을 정확히 입력해주세요.");
			        dialogBuilder.setPositiveButton( "확인" , null );
			        dialogBuilder.show();
				}
			}
		}
    }
	
	private CipherUtils mCipher = new CipherUtils();
	
	private int verValue = 0, verMax = 1;

	ProgressDialog pd;
	
	private libJSON LibJSON = new libJSON();
	private libJSON_GET GET_LibJSON = new libJSON_GET();
	private DownloadFile DLF = new DownloadFile();
	public String FOLDER_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test/";
	
	class setZipCodeInformation extends AsyncTask<Integer, Integer, Integer> {
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
						String[] aaa = new String[3];	

						long timestamp = System.currentTimeMillis();
						aaa[0] = mCipher.encrypt( Constance.SEED , Constance.MAC + "@" + timestamp ); 
						aaa[1] = String.valueOf( timestamp ).toString().trim(); 
						aaa[2] = mZipCodeSearchStr;
						
						GET_LibJSON.getMEMBER_ZIP_CODE( aaa );
						
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

						mDlgEForm_ZipCode.RefreshList();
						
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
	
	private String[] mFileUpLoadState = new String[2];
	
	class setMemberImageUpload extends AsyncTask<Integer, Integer, Integer> {
		@Override
		protected void onPreExecute() {
			try {
				if( !pd.isShowing() ) {
					pd.setMessage("회원가입 신청중입니다. 잠시만 기다려 주십시요.");
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
						String[] aaa = new String[3];	

						long timestamp = System.currentTimeMillis();
						aaa[0] = mCipher.encrypt( Constance.SEED , Constance.MAC + "@" + timestamp ); 
						aaa[1] = String.valueOf( timestamp ).toString().trim(); 
						aaa[2] = mFilePAth + "/" + Constance.UPLOAD_FILE_NAME_ORIGINAL;
						
						mFileUpLoadState = LibJSON.getEFORM_UPLOAD_ORIGINAL( aaa );
						if( mFileUpLoadState[0].equals("200") ){
							aaa[0] = mCipher.encrypt( Constance.SEED , Constance.MAC + "@" + timestamp ); 
							aaa[1] = String.valueOf( timestamp ).toString().trim(); 
							aaa[2] = mFilePAth + "/" + Constance.UPLOAD_FILE_NAME_MASK;
							
							mFileUpLoadState = LibJSON.getEFORM_UPLOAD_MASK( aaa );
							if( mFileUpLoadState[0].equals("200") ){
								aaa[0] = mCipher.encrypt( Constance.SEED , Constance.MAC + "@" + timestamp ); 
								aaa[1] = String.valueOf( timestamp ).toString().trim(); 
								
								mFileUpLoadState = LibJSON.getCUTOMER_INSERT( aaa );
							} else {
								dialogBuilder.setTitle("SalesForce");
								dialogBuilder.setMessage( "업로드에 실패하였습니다. 다시 시도하십시오.");
						        dialogBuilder.setPositiveButton( "확인" , null );
						        dialogBuilder.show();
							}
						} else {
							dialogBuilder.setTitle("SalesForce");
							dialogBuilder.setMessage( "업로드에 실패하였습니다. 다시 시도하십시오.");
					        dialogBuilder.setPositiveButton( "확인" , null );
					        dialogBuilder.show();
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
					try {
						pd.dismiss();
						pd.cancel();
						DeleteDir( mFilePAth + "/" );
						
						if( mFileUpLoadState[0].equals("Y") ){
							dialogBuilder.setTitle("SalesForce");
							dialogBuilder.setMessage( mFileUpLoadState[1] );
					        dialogBuilder.setPositiveButton( "확인" , null );
					        dialogBuilder.show();
					        btn_Upload.setEnabled( false );
						} else {
							dialogBuilder.setTitle("SalesForce");
							dialogBuilder.setMessage( mFileUpLoadState[1] );
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
	
	private Handler handler = new Handler();
	
	@Override
	public void onDestroy() 
	{
		Log.d("lib", "OZViewerActivity onDestroy");
		super.onDestroy();
		if(viewer != null) {
			viewer.dispose();
			viewer = null;
		}
	}
}