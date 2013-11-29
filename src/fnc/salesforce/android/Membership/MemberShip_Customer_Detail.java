package fnc.salesforce.android.Membership;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import fnc.salesforce.android.LoginPage;
import fnc.salesforce.android.Main_Page;
import fnc.salesforce.android.R;
import fnc.salesforce.android.Constance.CONTENTS_PAGE_ALL_LIST_CONSTANCE;
import fnc.salesforce.android.Constance.Constance;
import fnc.salesforce.android.LIB.BrandActivity;
import fnc.salesforce.android.LIB.CipherUtils;
import fnc.salesforce.android.LIB.Decimal_Coma;
import fnc.salesforce.android.LIB.DownloadFile;
import fnc.salesforce.android.LIB.KumaLog;
import fnc.salesforce.android.LIB.MenuActivity;
import fnc.salesforce.android.LIB.ProductDialog;
import fnc.salesforce.android.LIB.libJSON;
import fnc.salesforce.android.LIB.libJSON_GET;
import fnc.salesforce.android.LIB.libUDID;
import fnc.salesforce.android.PullToList.BottomPullToRefreshView;
import fnc.salesforce.android.PullToList.ShopListAdabter;

public class MemberShip_Customer_Detail extends Activity  implements OnTouchListener, OnClickListener{

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
 
	private ImageView btn_CustomerProfile, btn_Customer_Buy, btn_Customer_Campaign, btn_Customer_Close;
	
	private RelativeLayout Customer_ProfileLayout, layoutListViewTop;
	
	private LinearLayout layout_List;
	
	private BottomPullToRefreshView pullView2 = null;
	private ListView listView = null;
	private member_Buy_Adabter Buyadapter = null;
	private member_NoUse_Adabter NoUseadapter = null;
	
	private boolean RefreshState = true;
	
	private int num = 1;
	
	private String mCsNo = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView( R.layout.member_customer_detail );
		
		CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_BUY_STORY_LIST.clear();
		
		CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_NOUSE_CAMPAIGN_LIST.clear();
		
		mContext = this;
		try {
			lUDID.Check_UDID( this );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Intent intent = getIntent();
			
        try {
        	mCsNo = intent.getStringExtra( "csNo" );
		} catch (Exception e) {
			finish();
		}
	        
		num = 1;
		
		NoUseadapter = new member_NoUse_Adabter( this );
		
		Buyadapter = new member_Buy_Adabter( this );
		
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
		
		btn_CustomerProfile    	= (ImageView) findViewById( R.id.btn_CustomerProfile );
		btn_Customer_Buy    	= (ImageView) findViewById( R.id.btn_Customer_Buy );
		btn_Customer_Campaign  	= (ImageView) findViewById( R.id.btn_Customer_Campaign );
		btn_Customer_Close    	= (ImageView) findViewById( R.id.btn_Customer_Close );

		layout_List    			= (LinearLayout) findViewById( R.id.layout_List );
		
		Customer_ProfileLayout  = (RelativeLayout) findViewById( R.id.Customer_ProfileLayout );
		layoutListViewTop    	= (RelativeLayout) findViewById( R.id.layoutListViewTop );
		
		pullView2			= (BottomPullToRefreshView)findViewById(R.id.pull_to_refresh2);
		
        listView 			= (ListView)findViewById(R.id.listView);

    	btnGo_Main.setOnClickListener( this );
		btnBack_Page.setOnClickListener( this );
		        
        txtCategoryName.setText( "Membership" );
        
        try {
        	txt_AdGallery_Logo.setText( Constance.SHOPNAME );
		} catch (Exception e) {
			e.printStackTrace();
		}

        layout_List.setVisibility( View.GONE );
        
        btn_CustomerProfile.setOnClickListener( this );
        btn_Customer_Buy.setOnClickListener( this );
        btn_Customer_Campaign.setOnClickListener( this );
        btn_Customer_Close.setOnClickListener( this );
    	btnMenu_Activity.setOnClickListener( this );
    	
    	btn_Brand_Activity.setOnClickListener( this );
    	btn_Brand_LogOut.setOnClickListener( this );
    	
    	
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
						RefreshState = false;
						
						int MaxPage = Integer.parseInt( Constance.PageCount );
						if( num < MaxPage ){
							num  += 1;
							try {
								handler.postDelayed(new Runnable() {
									@Override
									public void run() {
										if( layoutListViewTop.getChildAt( 0 ) == BuyListTopView) {
											new setCustomerBuyList().execute();
										} else {
											new setCustomerCampaignList().execute();
										}
									}
								}, 300);
								
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
    	
        handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				new setCustomerProfile().execute();
			}
		}, 100);
        
       
	}
	private View ProfileView, BuyListTopView, CampaignListTopView;
	
	
	private TextView txtCardNum, txtPhoneNum, txtName, txtPassprtBirth;
	private TextView txtRealBirth, txtTellNum, txtMarryDate, txtaddr;
	private TextView txtUseFullPoint_Depart, txtCommPanyGrade, txtUseFullPoint_Shop, txtBrandGrade;
	private TextView txtJoinDate, txtShopGrade, txtJoinShop;
	
	private ImageView imgName;
	
	private Decimal_Coma DC = new Decimal_Coma();
	
	private void BtnSetProfile_Layout(){
		
		LayoutInflater inflater  = getLayoutInflater();
        
        ProfileView = inflater.inflate(R.layout.membership_customer_information, null);
        
        txtCardNum 				= ( TextView ) ProfileView.findViewById( R.id.txtCardNum );
        txtPhoneNum 			= ( TextView ) ProfileView.findViewById( R.id.txtPhoneNum );
        txtTellNum 				= ( TextView ) ProfileView.findViewById( R.id.txtTellNum );
        txtName 				= ( TextView ) ProfileView.findViewById( R.id.txtName );
        txtPassprtBirth 		= ( TextView ) ProfileView.findViewById( R.id.txtPassprtBirth );
        txtRealBirth 			= ( TextView ) ProfileView.findViewById( R.id.txtRealBirth );
        txtMarryDate 			= ( TextView ) ProfileView.findViewById( R.id.txtMarryDate );
        txtaddr 				= ( TextView ) ProfileView.findViewById( R.id.txtaddr );
        txtUseFullPoint_Depart 	= ( TextView ) ProfileView.findViewById( R.id.txtUseFullPoint_Depart );
        txtCommPanyGrade 		= ( TextView ) ProfileView.findViewById( R.id.txtCommPanyGrade );
        txtUseFullPoint_Shop 	= ( TextView ) ProfileView.findViewById( R.id.txtUseFullPoint_Shop );
        txtBrandGrade 			= ( TextView ) ProfileView.findViewById( R.id.txtBrandGrade );
        txtJoinDate 			= ( TextView ) ProfileView.findViewById( R.id.txtJoinDate );
        txtShopGrade 			= ( TextView ) ProfileView.findViewById( R.id.txtShopGrade );
        txtJoinShop 			= ( TextView ) ProfileView.findViewById( R.id.txtJoinShop );
        	
        imgName					= ( ImageView ) ProfileView.findViewById( R.id.imgName );
        
        try {
			String mCardNum = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_PROFILE_LIST.get( 0 ).cardNo;
			if( mCardNum.equals("null") ){
				txtCardNum.setText( "" );
			} else {
				txtCardNum.setText( mCardNum );
			}
		} catch (Exception e) {
			txtCardNum.setText( "" );
		}
        try {
			String telNo = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_PROFILE_LIST.get( 0 ).telNo;
			if( telNo.equals("null") ){
				txtTellNum.setText( "" );
			} else {
				txtTellNum.setText( telNo );
			}
		} catch (Exception e) {
			txtTellNum.setText( "" );
		}
        
        
        try {
			String mPhoneNum = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_PROFILE_LIST.get( 0 ).mobileTelNo;
			if( mPhoneNum.equals("null") ){
				txtPhoneNum.setText( "" );
			} else {
				txtPhoneNum.setText( mPhoneNum );
			}
		} catch (Exception e) {
			txtPhoneNum.setText( "" );
		}
        
        try {
			String mtxtName = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_PROFILE_LIST.get( 0 ).csName;
			if( mtxtName.equals("null") ){
				txtName.setText( "" );
			} else {
				txtName.setText( mtxtName );
			}
		} catch (Exception e) {
			txtName.setText( "" );
		}
        
        try {
			String mgender = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_PROFILE_LIST.get( 0 ).gender;
			if( mgender.equals("남") ){
				imgName.setBackgroundResource( R.drawable.at_sf_mem_icn_male );
			} else {
				imgName.setBackgroundResource( R.drawable.at_sf_mem_icn_female );
			}
		} catch (Exception e) {
			imgName.setBackgroundResource( R.drawable.at_sf_mem_icn_male );
		}
        
        
        try {
			String mjuminBirthday = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_PROFILE_LIST.get( 0 ).juminBirthday;
			if( mjuminBirthday.equals("null") ){
				txtPassprtBirth.setText( "" );
			} else {
				txtPassprtBirth.setText( mjuminBirthday );
			}
		} catch (Exception e) {
			txtPassprtBirth.setText( "" );
		}
        
        try {
			String mbirthday = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_PROFILE_LIST.get( 0 ).birthday;
			if( mbirthday.equals("null") ){
				txtRealBirth.setText( "" );
			} else {
				txtRealBirth.setText( mbirthday );
			}
		} catch (Exception e) {
			txtRealBirth.setText( "" );
		}
        
        try {
			String weddingAnniversary = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_PROFILE_LIST.get( 0 ).weddingAnniversary;
			if( weddingAnniversary.equals("null") ){
				txtMarryDate.setText( "" );
			} else {
				txtMarryDate.setText( weddingAnniversary );
			}
		} catch (Exception e) {
			txtMarryDate.setText( "" );
		}
        
        try {
        	String mZipCd = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_PROFILE_LIST.get( 0 ).zipCd;
			String maddr = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_PROFILE_LIST.get( 0 ).addr;
			if( maddr.equals("null") ){
				txtaddr.setText( "" );
			} else {
				txtaddr.setText( "( " + mZipCd + " )  " + maddr );
			}
		} catch (Exception e) {
			txtaddr.setText( "" );
		}
        
        try {
			String drtsUsefulPoint = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_PROFILE_LIST.get( 0 ).drtsUsefulPoint;
			if( drtsUsefulPoint.equals("null") ){
				txtUseFullPoint_Depart.setText( "" );
			} else {
				txtUseFullPoint_Depart.setText( DC.Numeric3comma( Integer.parseInt( drtsUsefulPoint ) ) );
			}
		} catch (Exception e) {
			txtUseFullPoint_Depart.setText( "" );
		}
        
        
        try {
			String companyGrade = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_PROFILE_LIST.get( 0 ).companyGrade;
			if( companyGrade.equals("null") ){
				txtCommPanyGrade.setText( "" );
			} else {
				txtCommPanyGrade.setText( companyGrade );
			}
		} catch (Exception e) {
			txtCommPanyGrade.setText( "" );
		}
        
        try {
			String agencyUsefulPoint = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_PROFILE_LIST.get( 0 ).agencyUsefulPoint;
			if( agencyUsefulPoint.equals("null") ){
				txtUseFullPoint_Shop.setText( "" );
			} else {
				txtUseFullPoint_Shop.setText( DC.Numeric3comma( Integer.parseInt( agencyUsefulPoint ) ) );
			}
		} catch (Exception e) {
			txtUseFullPoint_Shop.setText( "" );
		}

        try {
			String brandGrade = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_PROFILE_LIST.get( 0 ).brandGrade;
			if( brandGrade.equals("null") ){
				txtBrandGrade.setText( "" );
			} else {
				txtBrandGrade.setText( brandGrade );
			}
		} catch (Exception e) {
			txtBrandGrade.setText( "" );
		}
        
        try {
			String entrydate = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_PROFILE_LIST.get( 0 ).entrydate;
			if( entrydate.equals("null") ){
				txtJoinDate.setText( "" );
			} else {
				txtJoinDate.setText( entrydate );
			}
		} catch (Exception e) {
			txtJoinDate.setText( "" );
		}

        try {
			String shopGrade = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_PROFILE_LIST.get( 0 ).shopGrade;
			if( shopGrade.equals("null") ){
				txtShopGrade.setText( "" );
			} else {
				txtShopGrade.setText( shopGrade );
			}
		} catch (Exception e) {
			txtShopGrade.setText( "" );
		}
        
        try {
			String srcNm = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_PROFILE_LIST.get( 0 ).srcNm;
			if( srcNm.equals("null") ){
				txtJoinShop.setText( "" );
			} else {
				txtJoinShop.setText( srcNm );
			}
		} catch (Exception e) {
			txtJoinShop.setText( "" );
		}
       
        Customer_ProfileLayout.addView( ProfileView );
	}
	
	private void BtnSetProfile(){
		if( Customer_ProfileLayout.getVisibility() == View.GONE ){
			Customer_ProfileLayout.setVisibility( View.VISIBLE );
			layout_List.setVisibility( View.GONE );
		}
	}
	
	private void setBuyList_Top(){
		if( layout_List.getVisibility() == View.GONE ){
			layout_List.setVisibility( View.VISIBLE );
			Customer_ProfileLayout.setVisibility( View.GONE );
		}

		if( layoutListViewTop.getChildAt( 0 ) != BuyListTopView || layoutListViewTop.getChildCount() == 0) {
			layoutListViewTop.removeAllViews();
			
			LayoutInflater inflater  = getLayoutInflater();
	        
			BuyListTopView = inflater.inflate(R.layout.membership_customer_buy, null);
	        
			LinearLayout.LayoutParams imglinelayout = new LinearLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT, 52);

			layoutListViewTop.setLayoutParams(imglinelayout);
			
	        layoutListViewTop.addView( BuyListTopView );
	        listView.setAdapter( Buyadapter );
	        Buyadapter.notifyDataSetChanged();
		} else {
			Buyadapter.notifyDataSetChanged();
		}
	}
	
	private void setNoUseCamPaignList_Top(){
		
		if( layout_List.getVisibility() == View.GONE ){
			layout_List.setVisibility( View.VISIBLE );
			Customer_ProfileLayout.setVisibility( View.GONE );
		}

		if( layoutListViewTop.getChildAt( 0 ) != CampaignListTopView || layoutListViewTop.getChildCount() == 0) {
			layoutListViewTop.removeAllViews();
			
			LayoutInflater inflater  = getLayoutInflater();
	        
	        CampaignListTopView = inflater.inflate(R.layout.membership_customer_nouse_listtop, null);
	        
	        LinearLayout.LayoutParams imglinelayout = new LinearLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT, 38);

			layoutListViewTop.setLayoutParams(imglinelayout);
			
	        layoutListViewTop.addView( CampaignListTopView );
	        listView.setAdapter( NoUseadapter );
	        NoUseadapter.notifyDataSetChanged();
		} else {
			NoUseadapter.notifyDataSetChanged();
		}
		
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		pullView2.touchDelegate(v, event);
		return false;
	}
	
	class setCustomerProfile extends AsyncTask<Integer, Integer, Integer> {
		@Override
		protected void onPreExecute() {
			
			verMax		= 1;
						
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
						String[] aaa = new String[3];

						long timestamp = System.currentTimeMillis();
						aaa[0] = mCipher.encrypt( Constance.SEED , Constance.MAC + "@" + timestamp ); 
						aaa[1] = String.valueOf( timestamp ).toString().trim();
						
						aaa[2] = mCsNo;
								
						GET_LibJSON.getMEMBER_CUSTOMER_SEARCH_DETAIL( aaa );
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
					 
					BtnSetProfile_Layout();
					 
					pd.dismiss();
					pd.cancel();
				}
			});
		}
		@Override
		protected void onCancelled() {
		}
	}


	class setCustomerBuyList extends AsyncTask<Integer, Integer, Integer> {
		@Override
		protected void onPreExecute() {
			
			verMax		= 1;
						
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
						String[] aaa = new String[8];

						long timestamp = System.currentTimeMillis();
						aaa[0] = mCipher.encrypt( Constance.SEED , Constance.MAC + "@" + timestamp ); 
						aaa[1] = String.valueOf( timestamp ).toString().trim();
						aaa[2] =  String.valueOf( num );
						aaa[3] = "19";
						aaa[4] = "1";
						aaa[5] = mCsNo;
								
						GET_LibJSON.getMEMBER_CUSTOMER_SEARCH_BUY_STORY( aaa );
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
					pullView2.completeRefresh();
					setBuyList_Top();

					pd.dismiss();
					pd.cancel();
				}
			});
		}
		@Override
		protected void onCancelled() {
		}
	}
	
	class setCustomerCampaignList extends AsyncTask<Integer, Integer, Integer> {
		@Override
		protected void onPreExecute() {
			
			verMax		= 1;
						
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
						String[] aaa = new String[8];

						long timestamp = System.currentTimeMillis();
						aaa[0] = mCipher.encrypt( Constance.SEED , Constance.MAC + "@" + timestamp ); 
						aaa[1] = String.valueOf( timestamp ).toString().trim();
						
						aaa[2] =  String.valueOf( num );
						aaa[3] = "23";
						aaa[4] = "1";
						aaa[5] = mCsNo;
								
						GET_LibJSON.getMEMBER_CUSTOMER_SEARCH_NONUSE_CAMPAIGN_STORY( aaa );
						
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
					pullView2.completeRefresh();
					setNoUseCamPaignList_Top();

					pd.dismiss();
					pd.cancel();
				}
			});
		}
		@Override
		protected void onCancelled() {
		}
	}
	
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
		} else if( v == btn_CustomerProfile ) {
			ChahangeBack( 0 );
			BtnSetProfile();
		} else if( v == btn_Customer_Buy ) {
			ChahangeBack( 1 );
			if (pullView2.isBottom()) {
				listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
			}
			
			num = 1;
			
			layoutListViewTop.removeAllViews();
			
			CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_BUY_STORY_LIST.clear();
			
			new setCustomerBuyList().execute();
		} else if( v == btn_Customer_Campaign ) {
			ChahangeBack( 2 );
			if (pullView2.isBottom()) {
				listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
			}
			
			num = 1;
			
			layoutListViewTop.removeAllViews();
			
			CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MEMBER_SEARCH_NOUSE_CAMPAIGN_LIST.clear();
			
			new setCustomerCampaignList().execute();
			
		} else if( v == btn_Customer_Close ) {
			finish();
		}
	}
	
	private void ChahangeBack( int position ) {
		if( position == 0 ){
			btn_CustomerProfile.setBackgroundResource( R.drawable.at_sf_mem_btn_profile_o );
			btn_Customer_Buy.setBackgroundResource( R.drawable.at_sf_mem_btn_buy_n );
			btn_Customer_Campaign.setBackgroundResource( R.drawable.at_sf_mem_btn_campaign_n );
		} else if( position == 1 ){
			btn_CustomerProfile.setBackgroundResource( R.drawable.at_sf_mem_btn_profile_n );
			btn_Customer_Buy.setBackgroundResource( R.drawable.at_sf_mem_btn_buy_o );
			btn_Customer_Campaign.setBackgroundResource( R.drawable.at_sf_mem_btn_campaign_n );
		} else if( position == 2 ){
			btn_CustomerProfile.setBackgroundResource( R.drawable.at_sf_mem_btn_profile_n );
			btn_Customer_Buy.setBackgroundResource( R.drawable.at_sf_mem_btn_buy_n );
			btn_Customer_Campaign.setBackgroundResource( R.drawable.at_sf_mem_btn_campaign_o );
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
