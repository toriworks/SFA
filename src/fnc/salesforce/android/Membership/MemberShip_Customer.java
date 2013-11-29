package fnc.salesforce.android.Membership;

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
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import fnc.salesforce.android.LoginPage;
import fnc.salesforce.android.R;
import fnc.salesforce.android.Constance.CONTENTS_PAGE_ALL_LIST_CONSTANCE;
import fnc.salesforce.android.Constance.Constance;
import fnc.salesforce.android.LIB.CipherUtils;
import fnc.salesforce.android.LIB.HidekeyBoard;
import fnc.salesforce.android.LIB.libJSON_GET;
import fnc.salesforce.android.PullToList.BottomPullToRefreshView;

public class MemberShip_Customer implements OnTouchListener, OnClickListener{
	
	private BottomPullToRefreshView pullView2 = null;
	private ListView listView = null;
	private MemberShipCustomerAdabter adapter =null;

	private Context mContext;
	
	private ProgressDialog pd;
		
	private Button btnSearch;
	
	private EditText edit_Name, edit_PhoneNumber, edit_PassportNumber;
			
	LayoutInflater inflater;
	
	private AlertDialog.Builder dialogBuilder;
	
	public MemberShip_Customer(Context context){
				
		mContext = context;
		
		pd = new ProgressDialog( context );
		
		inflater = ((Activity) mContext).getLayoutInflater();
	}
	
	View mMemberShip_Customerview = null;
    /** Called when the activity is first created. */
	public View setMemberShip_Customer(){
		CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_LIST.clear();
		
		mMemberShip_Customerview = null;
        
		dialogBuilder = new AlertDialog.Builder( mContext );
		
        pd = new ProgressDialog( mContext );
        
        mDlgCustomerSeach = new DlgCustomerSeach( mContext );
        
        mMemberShip_Customerview = inflater.inflate(R.layout.membership_customer_search, null);
        pullView2			= (BottomPullToRefreshView) mMemberShip_Customerview.findViewById(R.id.pull_to_refresh2);
        
        btnSearch			= ( Button ) mMemberShip_Customerview.findViewById( R.id.btnSearch );
        listView 			= (ListView) mMemberShip_Customerview.findViewById(R.id.listView);
        edit_Name			= ( EditText ) mMemberShip_Customerview.findViewById( R.id.edit_Name );
        edit_PhoneNumber	= ( EditText ) mMemberShip_Customerview.findViewById( R.id.edit_PhoneNumber );
        edit_PassportNumber	= ( EditText ) mMemberShip_Customerview.findViewById( R.id.edit_PassportNumber );
        
        adapter = new MemberShipCustomerAdabter( mContext );
        		
        btnSearch.setOnClickListener( this );
        
        pullView2.setListener(new BottomPullToRefreshView.Listener() {
			@Override
			public void onChangeMode(fnc.salesforce.android.PullToList.BottomPullToRefreshView.MODE mode) {
				switch(mode) {
					case NORMAL:
						listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_DISABLED);
						break;
					case PULL:
						
					case READY_TO_REFRESH:
						if (pullView2.isBottom()) {
							listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
						}
						break;
					case REFRESH:						
						int MaxPage = Integer.parseInt( Constance.PageCount );
						if( num < MaxPage ){
							num  += 1;
							try {
								handler.postDelayed(new Runnable() {
									@Override
									public void run() {
										new setRepair_Search_Infor().execute();
									}
								}, 200);
								
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {
							try {
								pullView2.completeRefresh();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						break;
				}
			}
	
		});

       listView.setAdapter(adapter);
        
       listView.setOnTouchListener(this);
       listView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				View firstView = view.getChildAt(0);
				if (firstView == null) {
					return;
				}
				
				View lastView = view.getChildAt(view.getChildCount()-1);
				if (lastView == null) {
					return;
				}
				if (totalItemCount == firstVisibleItem + visibleItemCount && lastView.getBottom() <= view.getHeight()) {
					pullView2.setBottom(true);
				}
				else {
					pullView2.setBottom(false);
				}
			}
		});

       listView.setOnItemClickListener( new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				mDlgCustomerSeach.show();

				CustomerNumber = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_LIST.get( arg2 ).csNo;
			}
		});
        
        return mMemberShip_Customerview;
    }
	
	private static DlgCustomerSeach mDlgCustomerSeach;
	
	public class DlgCustomerSeach extends Dialog implements OnClickListener
    {
		private Button btnCofirm, btnClose;
		private EditText edit_Password;
		
		public DlgCustomerSeach( Context context ) 
		{
			super(context);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			
			setContentView(R.layout.customer_search_dialog);
			
			btnCofirm = (Button) findViewById( R.id.btnCofirm );
			btnClose = (Button) findViewById( R.id.btnClose );
			
			edit_Password = (EditText) findViewById( R.id.edit_Password );
			
			btnCofirm.setOnClickListener( this );
			btnClose.setOnClickListener( this );
		}

		@Override
		public void show() {
			// TODO Auto-generated method stub
			super.show();
			edit_Password.setText( "" );
			
		}
		@Override
		public void onClick(View v) {
			if( v == btnClose ){
				 dismiss();
				 cancel();
			} else if( v == btnCofirm ){
				CustomerPassWord = edit_Password.getText().toString().trim();
				new setCustomer_detail_Pass().execute();
			} 
		}
    }
	
	private static String CustomerNumber = "";

	private String CustomerPassWord = "";
		
	class setCustomer_detail_Pass extends AsyncTask<Integer, Integer, Integer> {
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
						
						aaa[2] = CustomerNumber; 
						aaa[3] = CustomerPassWord;
						aaa[4] = Constance.USER_ID; 

								
						NetworkResult = GET_LibJSON.getMEMBER_CUSTOMER_SEARCH_PASSWORD_CHECK( aaa );

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
						
						mDlgCustomerSeach.dismiss();
						mDlgCustomerSeach.cancel();
						
						if( NetworkResult.equals("N") ){
							dialogBuilder.setTitle("SalesForce");
							dialogBuilder.setMessage( "네트워크 오류입니다. 다시 시도 하시겠습니까?");
					        dialogBuilder.setPositiveButton( "확인" , Retry_Yes );
					        dialogBuilder.setNegativeButton("취소", null);
					        dialogBuilder.show();
						} else if( NetworkResult.equals("F") ){
							dialogBuilder.setTitle("SalesForce");
							dialogBuilder.setMessage( "비밀번호가 일치하지 않습니다. 다시 시도하시겠습니까?");
					        dialogBuilder.setPositiveButton( "확인" , L_Yes );
					        dialogBuilder.setNegativeButton("취소", null);
					        dialogBuilder.show();
						} else if( NetworkResult.equals("T") ){
							Intent intent = new Intent( mContext, MemberShip_Customer_Detail.class);
							
							intent.putExtra("csNo", CustomerNumber );
							mContext.startActivity(intent);
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
			mDlgCustomerSeach.show();
		}
	};

	private DialogInterface.OnClickListener Retry_Yes = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			new setCustomer_detail_Pass().execute();
		}
	};
	
	
	public View getView(){
		return mMemberShip_Customerview;
	}
	
	private CipherUtils mCipher = new CipherUtils();
	
	private int verValue = 0, verMax = 1;

	private libJSON_GET GET_LibJSON = new libJSON_GET();

	public String FOLDER_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test/";
	
	private int num = 1, mMaxResult = 23;
	private String mSearchName = "", mSearchCode = "";

	
	class setRepair_Search_Infor extends AsyncTask<Integer, Integer, Integer> {
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
						
						String[] aaa = new String[8];

						long timestamp = System.currentTimeMillis();
						aaa[0] = mCipher.encrypt( Constance.SEED , Constance.MAC + "@" + timestamp ); 
						aaa[1] = String.valueOf( timestamp ).toString().trim();
						
						aaa[2] = String.valueOf( num ); 
						aaa[3] = String.valueOf( mMaxResult );
						aaa[4] = "1"; 
						
						aaa[5] = csName;
						aaa[6] = mobileTelNo;
						aaa[7] = birthday;
								
						GET_LibJSON.getMEMBER_CUSTOMER_SEARCH( aaa );

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
						adapter.notifyDataSetChanged();
						
						try {
							pullView2.completeRefresh();
						} catch (Exception e) {
							e.printStackTrace();
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
	/**
	 * @param v
	 * @param event
	 * @return
	 * @see android.view.View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		pullView2.touchDelegate(v, event);
		return false;
	}

	private String csName = "", mobileTelNo = "", birthday = "";
	
	@Override
	public void onClick(View v) {

		if( v == btnSearch  ){
			
			HidekeyBoard.KeyboardHide(mContext, btnSearch.getWindowToken() );
			
			if( edit_Name.getText().toString().trim().length() >= 2 ){
				if( edit_PhoneNumber.getText().toString().trim().length() >= 10 || edit_PhoneNumber.getText().toString().trim().length() == 4 
						|| edit_PhoneNumber.getText().toString().trim().length() == 0){
					if( edit_PassportNumber.getText().toString().trim().length() == 8 || edit_PassportNumber.getText().toString().trim().length() == 0){
						CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_LIST.clear();
						
						csName = edit_Name.getText().toString().trim();
						mobileTelNo = edit_PhoneNumber.getText().toString().trim();
						birthday = edit_PassportNumber.getText().toString().trim();
						
						new setRepair_Search_Infor().execute();
					} else {
						dialogBuilder.setTitle("SalesForce");
						dialogBuilder.setMessage( "주민번호 생일을 올바르게 입력해주십시오.");
				        dialogBuilder.setPositiveButton( "확인" , null );
				        dialogBuilder.show();
					}
				} else {
					dialogBuilder.setTitle("SalesForce");
					dialogBuilder.setMessage( "휴대폰 번호를 올바르게 입력해주십시오.");
			        dialogBuilder.setPositiveButton( "확인" , null );
			        dialogBuilder.show();
				}
			} else {
				if( edit_PhoneNumber.getText().toString().trim().length() >= 10 ){
					CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_LIST.clear();
					
					csName = edit_Name.getText().toString().trim();
					mobileTelNo = edit_PhoneNumber.getText().toString().trim();
					birthday = edit_PassportNumber.getText().toString().trim();
					
					new setRepair_Search_Infor().execute();
				} else {
					if( edit_PhoneNumber.getText().toString().trim().length() == 4 ){
						if( edit_Name.getText().toString().trim().length() >= 2 ){
							if( edit_PassportNumber.getText().toString().trim().length() == 8 || edit_PassportNumber.getText().toString().trim().length() == 0){
								CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_LIST.clear();
								
								csName = edit_Name.getText().toString().trim();
								mobileTelNo = edit_PhoneNumber.getText().toString().trim();
								birthday = edit_PassportNumber.getText().toString().trim();
								
								new setRepair_Search_Infor().execute();
							} else {
								dialogBuilder.setTitle("SalesForce");
								dialogBuilder.setMessage( "주민번호 생일을 올바르게 입력해주십시오.");
						        dialogBuilder.setPositiveButton( "확인" , null );
						        dialogBuilder.show();
							}
						} else {
							dialogBuilder.setTitle("SalesForce");
							dialogBuilder.setMessage( "이름을 올바르게 입력해주십시오.");
					        dialogBuilder.setPositiveButton( "확인" , null );
					        dialogBuilder.show();
						}
					} else {
						if( edit_PhoneNumber.getText().toString().trim().length() == 0 ){
							if( edit_PassportNumber.getText().toString().trim().length() == 8 ){
								if( edit_Name.getText().toString().trim().length() >= 2 ){
									
									CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_LIST.clear();
									
									csName = edit_Name.getText().toString().trim();
									mobileTelNo = edit_PhoneNumber.getText().toString().trim();
									birthday = edit_PassportNumber.getText().toString().trim();
									
									new setRepair_Search_Infor().execute();
								} else {
									dialogBuilder.setTitle("SalesForce");
									dialogBuilder.setMessage( "이름을 올바르게 입력해주십시오.");
							        dialogBuilder.setPositiveButton( "확인" , null );
							        dialogBuilder.show();
								}
							} else {
								dialogBuilder.setTitle("SalesForce");
								dialogBuilder.setMessage( "검색조건을 올바르게 입력해주세요.");
						        dialogBuilder.setPositiveButton( "확인" , null );
						        dialogBuilder.show();
							}
						} else {
							if( edit_Name.getText().toString().trim().length() >= 2 ){
								CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_LIST.clear();
								
								csName = edit_Name.getText().toString().trim();
								mobileTelNo = edit_PhoneNumber.getText().toString().trim();
								birthday = edit_PassportNumber.getText().toString().trim();
								
								new setRepair_Search_Infor().execute();
							} else {
								dialogBuilder.setTitle("SalesForce");
								dialogBuilder.setMessage( "이름을 올바르게 입력해주십시오.");
						        dialogBuilder.setPositiveButton( "확인" , null );
						        dialogBuilder.show();
							}
						}
					}
				}
			}
			edit_Name			= ( EditText ) mMemberShip_Customerview.findViewById( R.id.edit_Name );
		    edit_PhoneNumber	= ( EditText ) mMemberShip_Customerview.findViewById( R.id.edit_PhoneNumber );
		    edit_PassportNumber	= ( EditText ) mMemberShip_Customerview.findViewById( R.id.edit_PassportNumber );
		}
	}
	
	private DialogInterface.OnClickListener aa = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface arg0, int arg1) {

		}
	};
}
