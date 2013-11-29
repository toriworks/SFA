package fnc.salesforce.android;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import fnc.salesforce.android.Activity.AD_Gallery_10003;
import fnc.salesforce.android.Activity.Catalog__10002;
import fnc.salesforce.android.Activity.EDUCATION_Main;
import fnc.salesforce.android.Activity.Look_10001;
import fnc.salesforce.android.Activity.MemberShip_Main;
import fnc.salesforce.android.Activity.Product_Search;
import fnc.salesforce.android.Activity.Promotion_10004;
import fnc.salesforce.android.Activity.RepairInformaiton_Main;
import fnc.salesforce.android.Activity.ShopInformation;
import fnc.salesforce.android.Activity.ShopStopSystem;
import fnc.salesforce.android.Constance.CONTENTS_PAGE_ALL_LIST_CONSTANCE;
import fnc.salesforce.android.Constance.Constance;
import fnc.salesforce.android.Constance.MenuIconConstance;
import fnc.salesforce.android.LIB.CipherUtils;
import fnc.salesforce.android.LIB.DownloadFile;
import fnc.salesforce.android.LIB.KumaLog;
import fnc.salesforce.android.LIB.libJSON;
import fnc.salesforce.android.LIB.libJSON_GET;
import fnc.salesforce.android.LIB.libUDID;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.renderscript.Font.Style;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.InputType;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

public class Main_Page extends Activity implements OnClickListener, OnTouchListener{
	
	private RelativeLayout layout_Menu_Area;

    // Volley RequestQueue 선언
    private RequestQueue mRequestQueue;

	@Override
	public void onBackPressed() {
		if( !ScreenSaverState ){
			ScreenSaverState = true;
			setScreeSaver();
		} else {
			android.os.Process.killProcess(android.os.Process.myPid());
//			try {
//				dialogBuilder.setTitle("SalesForce");
//				dialogBuilder.setMessage( "로그아웃 하시겠습니까?");
//		        dialogBuilder.setPositiveButton( "확인" , LogOut_Yes );
//		        dialogBuilder.setNegativeButton("취소", null);
//		        dialogBuilder.show();
//			} catch (Exception e) {
//				e.printStackTrace();
//				android.os.Process.killProcess(android.os.Process.myPid());
//			}
		}
	}

		
	private ProgressDialog pd;
	
	private libUDID lUDID = new libUDID();
	
	private RelativeLayout imgContentsArea, imgContentsArea_ScreenSaver;
//	private ViewFlipper imgContentsFlipper;
	
	private RelativeLayout layout_ScreenSaver;
	
	private static final int MSG_TOP_BANNER = 1;
	
	private static final int MSG_CLOCK = 2;
	
	private int ScreenSaverTimer = 3000;
	
	private int ClockTimer = 1000;
	
	private boolean ScreenSaverState = true;
	
	int Scroll_X = 0;
		
//	private TextView txtAM, txtPM, txtToday, txtDate, txtTime;
	
	private ImageButton btnBrandList, btnUserChange;
	
	private ImageView btnLogOut, imgBrandThumb;
	private ListView BrandList;
	private RelativeLayout layoutBrandList;
	
	private AlertDialog.Builder dialogBuilder;
	
	private PowerManager mPM;
	
	private WakeLock mWakelock;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView( R.layout.main_page );
		
		mPM = (PowerManager) getSystemService( Context.POWER_SERVICE );
		

		mWakelock = mPM.newWakeLock( PowerManager.FULL_WAKE_LOCK,"Kuma Wake Lock");
		mContext = this;
		
		try {
			lUDID.Check_UDID( this );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Intent intent = getIntent();
        if( intent.getStringExtra("brandCd") != null ){
        	Constance.BEANDCD = intent.getStringExtra("brandCd");
        }
        
        dialogBuilder = new AlertDialog.Builder(this);
        
//		imgContentsFlipper = new ViewFlipper( this );
		
		layout_Menu_Area			= (RelativeLayout) findViewById(R.id.layout_Menu_Area);
		layout_ScreenSaver			= (RelativeLayout) findViewById(R.id.layout_ScreenSaver);
		
		LayoutThumb 				= (LinearLayout) findViewById(R.id.LayoutThumb);
		imgContentsArea				= (RelativeLayout) findViewById(R.id.imgContentsArea);
		imgContentsArea_ScreenSaver	= (RelativeLayout) findViewById(R.id.imgContentsArea_ScreenSaver);
		
		horaThumb					= (HorizontalScrollView) findViewById(R.id.horaThumb);
		
//		txtAM					= (TextView) findViewById(R.id.txtAM);
//		txtPM					= (TextView) findViewById(R.id.txtPM);
//		txtToday				= (TextView) findViewById(R.id.txtToday);
//		txtDate					= (TextView) findViewById(R.id.txtDate);
//		txtTime					= (TextView) findViewById(R.id.txtTime);
		
		btnBrandList			= ( ImageButton ) findViewById( R.id.btnBrandList );
		btnUserChange			= ( ImageButton ) findViewById( R.id.btnUserChange );
		
		imgBrandThumb			= ( ImageView ) findViewById( R.id.imgBrandThumb );
		btnLogOut				= ( ImageView ) findViewById( R.id.btnLogOut );
		
		BrandList				= ( ListView ) findViewById( R.id.BrandList );
		layoutBrandList			= ( RelativeLayout ) findViewById( R.id.layoutBrandList );

		try {
			imgBrandThumb.setBackgroundResource( Constance.getBrandImg_Large( Constance.BEANDCD ) );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		layoutBrandList.setVisibility( View.GONE );
		
		BrandList.setAdapter( new BrandItemListAdabter( this ) );
		
		btnUserChange.setOnClickListener( this );
		btnBrandList.setOnClickListener( this );
		
		layout_ScreenSaver.setOnClickListener( this ); 
		
		pd = new ProgressDialog( this );
		
		ScreenSaverTouchLayout = new RelativeLayout( this );
		
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				new setMenuInfor().execute();
			}
		}, 200);
		
//		mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_CLOCK), ClockTimer);
		
		mPager = new ViewPager( this );		
		
		mPager.setOnPageChangeListener(new OnPageChangeListener() {	//�������� ����Ǹ�, gallery�� listview�� onItemSelectedListener�� ���
			@Override public void onPageSelected(int position) {
				num = position;
				Scroll_X =  ( scView_Scrool_Y * num );
				
				horaThumb.smoothScrollTo( Scroll_X, 0);
				
				ThumScrollSelect();
			}
			
			@Override public void onPageScrolled(int position, float positionOffest, int positionOffsetPixels) {

			}
			@Override public void onPageScrollStateChanged(int state) {
				
			}
		});
		
        // Volley RequestQueue 초기화
        mRequestQueue = Volley.newRequestQueue(Main_Page.this);

    }
	
	private BkPagerAdapter BKPA;
	private ViewPager mPager;			
	
	//Pager �ƴ��� ����
	private class BkPagerAdapter extends PagerAdapter{
		private Context mContext;
		public BkPagerAdapter( Context con) { 
			super(); 
			mContext = con; 
		}

		@Override public int getCount() { 
			return MaxContents;
		}

		//�������� ����� �䰴ü ��/���
		@Override public Object instantiateItem(View pager, int position) 
		{
			
			ImageView tv = new ImageView(mContext);					//�ؽ�Ʈ��
			
			tv.setImageBitmap(ReturnBitmap_Rotate90( 
					BitmapFactory.decodeFile( strMainFilePath[0][position].toString()) , 800, 997 ));
			tv.setScaleType( ScaleType.CENTER_CROP);
			((ViewPager)pager).addView(tv, 0);		//�� ������ �߰�

			return tv;
		}
		//�� ��ü ����.
		@Override public void destroyItem(View pager, int position, Object view) {
			((ViewPager)pager).removeView((View)view);
		}

		// instantiateItem�޼ҵ忡�� ���� ��ü�� �̿��� ������
		@Override public boolean isViewFromObject(View view, Object obj) { return view == obj; }

		@Override public void finishUpdate(View arg0) {
			try {
				System.gc();
			} catch (Exception e) {
				e.printStackTrace(); 
			}
		}
		@Override public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}
		@Override public Parcelable saveState() {
			return null; 
		}
		@Override public void startUpdate(View arg0) {
			
		}
	}
	private Handler handler = new Handler();
	
	private Context mContext;
	
	private ImageButton[] tabImgBtn;
	private ImageView[] MenuIcon;
	
	private int MaxTabbCount = 0;
	
	// 메인 좌측 메뉴 Param
	private static class TabBackImageParam {
		static int width = 146;
		static int height = 100;
		static int margin_Top = 103;
	};
	
	private static class TabTextViewParam {
		static int width = 146;
		static int height = 35;
		static int margin_Top = 60;
		static int TextSize = 18;
	};
	
	private static class TabMenuIconParam {
		static int width = 44;
		static int height = 44;
		static int margin_Top = 10;
		static int margin_Left = 51;
	};
	
	private void setScrollViewTAbBar(){
		
		MaxTabbCount = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MAIN_MENU.size() + Constance.MenuGojung.length;
		

		if( tabImgBtn != null )
			tabImgBtn = null;
		
		if( MenuIcon != null )
			MenuIcon = null;
		
		tabImgBtn	= new ImageButton[ MaxTabbCount ];
		
		MenuIcon	= new ImageView[ MaxTabbCount ];
		
		TextView[] tabtxtView	= new TextView[ MaxTabbCount ];
		
		for( int i = 0; i < MaxTabbCount; i++ ){
			tabImgBtn[i] 	= new ImageButton( mContext );
			MenuIcon[i] 	= new ImageView( mContext );
			tabtxtView[i] 	= new TextView( mContext );
			
			RelativeLayout.LayoutParams imglinelayout = new RelativeLayout.LayoutParams(
					TabBackImageParam.width, TabBackImageParam.height);
			imglinelayout.setMargins( 0, ( TabBackImageParam.margin_Top ) * i, 0, 0);

			tabImgBtn[i].setLayoutParams(imglinelayout);
			
			int bgPosition = ( i % Constance.MainMenuBG.length );
			
			tabImgBtn[i].setBackgroundResource( Constance.MainMenuBG[ bgPosition ] );
			
			RelativeLayout.LayoutParams txtMenulayout = new RelativeLayout.LayoutParams(
					TabTextViewParam.width, TabTextViewParam.height);
			txtMenulayout.setMargins( 0, ( ( TabBackImageParam.margin_Top ) * i ) + TabTextViewParam.margin_Top, 0, 0);
			
			tabtxtView[i].setLayoutParams(txtMenulayout);
			tabtxtView[i].setGravity( Gravity.CENTER );
            // 왼쪽 메뉴에 대한 shadow 값 조정(2013.11)
			tabtxtView[i].setShadowLayer((float) 0.5, 1, 1, Color.parseColor("#000000"));
			tabtxtView[i].setTypeface( Typeface.DEFAULT_BOLD, Typeface.BOLD );
			tabtxtView[i].setTextColor( Color.parseColor( "#ffffff" ) );

            // 왼쪽 메뉴 그리기
            int sizeOfMainMenu = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MAIN_MENU.size();
            if( i < 3) {
                // 메뉴명
                tabtxtView[i].setText(Constance.MenuGojung[i]);
                // 아이콘
                MenuIcon[i].setBackgroundResource( setMenuIcon( Constance.MenuGojung_IMG[ i ]) );
            } else if(i < sizeOfMainMenu + 3) {
                // 메뉴명
                tabtxtView[i].setText(CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MAIN_MENU.get(i-3).ctgryNm);
                // 아이콘
                MenuIcon[i].setBackgroundResource( setMenuIcon( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MAIN_MENU.get( i - 3 ).iconTyCd ) );
            } else  {
                // 메뉴명
                tabtxtView[i].setText(Constance.MenuGojung[i - sizeOfMainMenu]);
                // 아이콘
                MenuIcon[i].setBackgroundResource( setMenuIcon( Constance.MenuGojung_IMG[ i - sizeOfMainMenu ]) );
            }

			if( i == ( MaxTabbCount - 1 ) ){
				if( Constance.HEAD_LOGIN  ){
					tabImgBtn[i].setVisibility( View.VISIBLE );
					MenuIcon[i].setVisibility( View.VISIBLE );
					tabtxtView[i].setVisibility( View.VISIBLE );
				} else {
					tabImgBtn[i].setVisibility( View.GONE );
					MenuIcon[i].setVisibility( View.GONE );
					tabtxtView[i].setVisibility( View.GONE );
				}
			}
			
			tabtxtView[i].setTextSize( TabTextViewParam.TextSize );
			
			tabImgBtn[i].setOnClickListener( this );
			
			RelativeLayout.LayoutParams imgIconlayout = new RelativeLayout.LayoutParams(
					TabMenuIconParam.width, TabMenuIconParam.height);
			imgIconlayout.setMargins( TabMenuIconParam.margin_Left, ( ( TabBackImageParam.margin_Top ) * i ) + TabMenuIconParam.margin_Top, 0, 0);
			
			MenuIcon[i].setLayoutParams(imgIconlayout);
			
			
			layout_Menu_Area.addView( tabImgBtn[i] );
			layout_Menu_Area.addView( MenuIcon[i] );
			layout_Menu_Area.addView( tabtxtView[i] );
		}
	}
	
	
	private int setMenuIcon( String mImgName ){
		
		for ( int i = 0; i < MenuIconConstance.MenuName.length; i++ ){
			if( mImgName.toLowerCase().toString().trim().equals( MenuIconConstance.MenuName[i]) ){
				return MenuIconConstance.MenuID[i];
			}
		}
		return 0;
		
	}

	LinearLayout LayoutThumb;
	
	private HorizontalScrollView horaThumb;
	
	ImageView[] imgTumb, imgTumb_Selected;
//	mainContents
	RelativeLayout[] ThumbLayout;
	
	private int num = 0, MaxContents = 15;
	
	private void setThumblayout(){
		MaxContents = strMainFilePath[0].length;
		
		imgTumb				= new ImageView[ MaxContents ];
//		mainContents		= new ImageView[ MaxContents ];
		imgTumb_Selected	= new ImageView[ MaxContents ];
		ThumbLayout			= new RelativeLayout[ MaxContents ];
		
		
		for( int i = 0; i < MaxContents; i++ ){
			imgTumb[i] 			= new ImageView( mContext );
//			mainContents[i] 	= new ImageView( mContext );
			imgTumb_Selected[i] = new ImageView( mContext );
			
			ThumbLayout[i] 		= new RelativeLayout( mContext );
			
			LinearLayout.LayoutParams imglinelayout = new LinearLayout.LayoutParams(
					155, 143);
			imglinelayout.setMargins( 0, 0, 5, 0);
			imgTumb[i].setLayoutParams(imglinelayout);
			
//			BitmapFactory.Options options = new BitmapFactory.Options();
//			
//			options.inSampleSize = 0;
			
			Bitmap src = BitmapFactory.decodeFile(strMainFilePath[1][i] );
//			Bitmap resized = Bitmap.createScaledBitmap(src, 85, 70, true);
			
			
			imgTumb[i].setImageBitmap( src );
			imgTumb[i].setScaleType( ScaleType.CENTER_CROP );
			imgTumb[i].setOnClickListener( this );
			
			imgTumb_Selected[i].setLayoutParams(imglinelayout);
			imgTumb_Selected[i].setBackgroundResource( R.drawable.at_main_thumbselect );
			
			imgTumb_Selected[i].setVisibility( View.INVISIBLE );
			
			
			LinearLayout.LayoutParams imgBacklayout = new LinearLayout.LayoutParams(
					155, 145);
			imgBacklayout.setMargins( 0, 0, 5, 0);
			ThumbLayout[i].setLayoutParams(imgBacklayout);
			ThumbLayout[i].setBackgroundResource( R.drawable.at_main_thumbbg );
			ThumbLayout[i].setGravity( Gravity.CENTER );
			
			ThumbLayout[i].addView( imgTumb[i] );
			ThumbLayout[i].addView( imgTumb_Selected[i] );
			
			LayoutThumb.addView( ThumbLayout[i] );
		}
		
		if( BKPA == null ){
			BKPA = new BkPagerAdapter( mContext );
			mPager.setAdapter( BKPA );//PagerAdapter�� ����
		} else {
			BKPA.notifyDataSetChanged();
		}
		
		imgContentsArea.addView( mPager );
		
		LayoutThumb.setGravity( Gravity.CENTER_VERTICAL );
		
		imgTumb_Selected[0].setVisibility( View.VISIBLE );
	}
	
	
	public Bitmap ReturnBitmap_Rotate90(Bitmap bitmap, int width, int height) {
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
	
	PointF start = new PointF();
	PointF mid = new PointF();
			
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub

		int act = event.getAction();

		switch (act & MotionEvent.ACTION_MASK) {

		case MotionEvent.ACTION_DOWN: // first finger down only
			start.set(event.getX(), event.getY());// 드래그시 클릭시 일정 크기 만큼 ↘방향으로 내려감
			break;

		case MotionEvent.ACTION_UP: // first finger lifted
		case MotionEvent.ACTION_POINTER_UP: // second finger lifted
			if ((start.x - event.getX()) > 50) {

			} else if( (start.x - event.getX()) < -50 ){
			} else {
				if( !ScreenSaverState ){
					ScreenSaverState = true;
					setScreeSaver();
				}
			}
			break;
		case MotionEvent.ACTION_POINTER_DOWN: // second finger down
			break;

		case MotionEvent.ACTION_MOVE:
			break;
		}

		return true; // indicate event was handled
	}
	
	int scView_Scrool_Y = 159;

	private void ThumScrollSelect (){
		try {
			for( int i = 0; i < imgTumb_Selected.length; i++ ){
				if (imgTumb_Selected[i].getVisibility() == View.VISIBLE ){
					imgTumb_Selected[i].setVisibility( View.INVISIBLE );
				}
			}
			imgTumb_Selected[num].setVisibility( View.VISIBLE );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	private CipherUtils mCipher = new CipherUtils();
	
	private int verValue = 0, verMax = 1;

	private libJSON LibJSON = new libJSON();
	private libJSON_GET GET_LibJSON = new libJSON_GET();
	private DownloadFile DLF = new DownloadFile();

    /**
     * 왼쪽 메뉴 정보를 얻는 클래스
     */
	class setMenuInfor extends AsyncTask<Integer, Integer, Integer> {
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
						String[] arrParametr = new String[7];
						
						arrParametr[0] = Constance.BEANDCD;
						
						long timestamp = System.currentTimeMillis();
						
						arrParametr[1] = mCipher.encrypt( Constance.SEED , Constance.MAC + "@" + timestamp ); 
						arrParametr[2] = String.valueOf( timestamp ).toString().trim(); 
						
						GET_LibJSON.getMAIN_MENU(  arrParametr );

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
                    // 메뉴 그리기
					setScrollViewTAbBar();
					//
                    // 메인 컨텐츠 얻기
                    new setMainInformation().execute();
				}
			});
		}
		@Override
		protected void onCancelled() {
		}
	}
	
	
	class setMainInformation extends AsyncTask<Integer, Integer, Integer> {
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
						String[] arrParametr = new String[7];
						
						arrParametr[0] = Constance.BEANDCD;

						long timestamp = System.currentTimeMillis();
						
						arrParametr[1] = mCipher.encrypt( Constance.SEED , Constance.MAC + "@" + timestamp ); 
						arrParametr[2] = String.valueOf( timestamp ).toString().trim(); 
						
						GET_LibJSON.getMAIN_INFORMATIO(  arrParametr );
						
						String strPath = "/Main/" + CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MAIN_INFORMATION.get(0).brandCd + "/" + CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MAIN_INFORMATION.get(0).cntntsId + "/Thumb/";
						
						CONTENTS_PAGE_ALL_LIST_CONSTANCE.MAIN_BRAND_THUMB_PATH = 
								DLF.Download_HttpFile( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MAIN_INFORMATION.get(0).dwldThumbPath , Constance.FILEPATH + strPath,
								CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MAIN_INFORMATION.get(0).thumbFileLc);
						
						strPath = "/Main/" + CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MAIN_INFORMATION.get(0).brandCd + "/" + CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MAIN_INFORMATION.get(0).cntntsId + "/";
						
						String zipPath = DLF.Download_HttpFile( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MAIN_INFORMATION.get(0).dwldCntntsPath , Constance.FILEPATH + strPath,
								CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MAIN_INFORMATION.get(0).cntntsFileLc);
						
						strMainFilePath = DLF.Zipent(Constance.FILEPATH + strPath, CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MAIN_INFORMATION.get(0).cntntsFileLc );
						
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
					setThumblayout();
					pd.cancel();
					pd.dismiss();
				}
			});
		}
		@Override
		protected void onCancelled() {
		}
	}
	
	private String[][] strMainFilePath = null;
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			mHandler.removeMessages(MSG_TOP_BANNER);
			mHandler.removeMessages(MSG_CLOCK);
			
			System.gc();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		try {
			if( mWakelock.isHeld() ){
				mWakelock.release();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	@Override
	public void onClick(View v) {
		try {
			if( tabImgBtn != null ){
				for( int i = 0; i < MaxTabbCount; i++){
					if( v == tabImgBtn[i] ){
						if( i < 3){
                            switch(i) {
                                case 0:
                                    Intent intent = new Intent( mContext, MemberShip_Main.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                    break;
                                case 1:
                                    Intent intent2 = new Intent( mContext, ShopStopSystem.class);
                                    intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent2);
                                    finish();
                                    break;
                                case 2:
                                    Intent intent3 = new Intent( mContext, Product_Search.class);
                                    intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent3);
                                    finish();
                                    break;
                            }
						} else if ( 1 < i && i < CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MAIN_MENU.size() + 3 ) {
							int position = i - 3;
                            String scrinTyCd = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MAIN_MENU.get( position ).scrinTyCd;
                            String ctgryTyCd = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MAIN_MENU.get( position ).ctgryTyCd;

							if( scrinTyCd.equals("010001") ){
								if( ctgryTyCd.equals("007003") ){
									//교육관리
									Intent intent = new Intent( mContext, EDUCATION_Main.class);
									intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									intent.putExtra("backState", "main" );
									startActivity(intent);
									finish();
								} else if( ctgryTyCd.equals("007002") ){
									// LookBook ( 콘텐츠 => 상세 )
									Intent intent = new Intent( mContext, Look_10001.class);
									intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
									intent.putExtra("ctgryId", CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MAIN_MENU.get( position ).ctgryId );
									intent.putExtra("brandCd", CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MAIN_MENU.get( position ).brandCd);
									intent.putExtra("ctgryNm", CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MAIN_MENU.get( position ).ctgryNm);
									intent.putExtra("Detail_State", "Main");
									startActivity(intent);
									finish();
								}
							} else if( scrinTyCd.equals("010002") ){
								// 카탈로그 ( 콘텐츠 => 상세 )
								Intent intent = new Intent( mContext, Catalog__10002.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								intent.putExtra("ctgryId", CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MAIN_MENU.get( position ).ctgryId );
								intent.putExtra("brandCd", CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MAIN_MENU.get( position ).brandCd);
								intent.putExtra("ctgryNm", CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MAIN_MENU.get( position ).ctgryNm);
								startActivity(intent);
								finish();
							} else if( scrinTyCd.equals("010003") ){
								// AD Gallery ( 콘텐츠 + 상세 + 제품  )
								Intent intent = new Intent( mContext, AD_Gallery_10003.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								intent.putExtra("ctgryId", CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MAIN_MENU.get( position ).ctgryId );
								intent.putExtra("brandCd", CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MAIN_MENU.get( position ).brandCd);
								intent.putExtra("ctgryNm", CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MAIN_MENU.get( position ).ctgryNm);

								startActivity(intent);
								finish();
							} else if( scrinTyCd.equals("010004") ){
								Intent intent = new Intent( mContext, Promotion_10004.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								intent.putExtra("ctgryId", CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MAIN_MENU.get( position ).ctgryId );
								intent.putExtra("brandCd", CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MAIN_MENU.get( position ).brandCd);
								intent.putExtra("ctgryNm", CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MAIN_MENU.get( position ).ctgryNm);

								startActivity(intent);
								finish();
							}
						} else {
                            int sizeOfMainMenu = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_MAIN_MENU.size();
							if( ( i - sizeOfMainMenu)  == 3){
								Intent intent = new Intent( mContext, RepairInformaiton_Main.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(intent);
								finish();
							} else if( ( i - sizeOfMainMenu )  == 4){
								Intent intent = new Intent( mContext, ShopInformation.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(intent);
								finish();
							}
                            // TODO : 매장 CHECKLIST 이후에 붙일 것
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if( imgTumb != null ){
				for( int i = 0; i < MaxContents; i++){
					if( v == imgTumb[i] ){
						if( num != i ){
							setThumSelected( i );
//							imgContentsFlipper.setDisplayedChild( i );
							mPager.setCurrentItem( i );
							
						}
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if( v == layout_ScreenSaver ){
			ScreenSaverState = false;
			setScreeSaver();
		} else if( v == btnBrandList ){
			if( layoutBrandList.getVisibility() == View.GONE ){
				layoutBrandList.setVisibility( View.VISIBLE );
			} else {
				layoutBrandList.setVisibility( View.GONE );
			}
		} else if( v == btnUserChange ){
//			dialogBuilder.setTitle("SalesForce");
//			dialogBuilder.setMessage( "로그아웃 하시겠습니까?");
//	        dialogBuilder.setPositiveButton( "확인" , LogOut_Yes );
//	        dialogBuilder.setNegativeButton("취소", null);
//	        dialogBuilder.show();
			
			Intent intent = new Intent( mContext, LoginPage.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("userChange", "Y");
			startActivity(intent);
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
			intent.putExtra("userChange", "N");
			startActivity(intent);
			finish();
		}
	};
	
	private void setScreeSaver(){ 
		
		try {
			mHandler.removeMessages(MSG_TOP_BANNER);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if( ScreenSaverState ){
			imgContentsArea_ScreenSaver.removeAllViews();
			imgContentsArea.removeAllViews();
			imgContentsArea.addView( mPager );
			imgContentsArea_ScreenSaver.setVisibility( View.GONE );
			imgContentsArea.setVisibility( View.VISIBLE );
			
			try {
				RelativeLayout.LayoutParams imglinelayout = new RelativeLayout.LayoutParams(
						648, 993);
				imglinelayout.setMargins( 152, 88, 0, 0);
				imgContentsArea.setLayoutParams(imglinelayout);
			} catch (Exception e) {
				e.printStackTrace();
			}
			setThumSelected(num);
			
			try {
				if( mWakelock.isHeld() ){
					mWakelock.release();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} else {
			try {
				mWakelock.acquire();
			} catch (Exception e) {
				e.printStackTrace();
			}
			RelativeLayout.LayoutParams imglinelayout = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
			ScreenSaverTouchLayout.setLayoutParams(imglinelayout);
			
			imgContentsArea.removeAllViews();
			imgContentsArea.setVisibility( View.GONE );
			
			ScreenSaverTouchLayout.setOnTouchListener( this );
			
			imgContentsArea_ScreenSaver.removeAllViews();
			imgContentsArea_ScreenSaver.addView( mPager );
			imgContentsArea_ScreenSaver.addView( ScreenSaverTouchLayout );
			imgContentsArea_ScreenSaver.setVisibility( View.VISIBLE );
			imgContentsArea_ScreenSaver.setBackgroundColor( Color.parseColor("#ffffff"));
			imgContentsArea_ScreenSaver.setGravity( Gravity.CENTER );

			mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_TOP_BANNER), ScreenSaverTimer);
		}
	}
	
	private RelativeLayout ScreenSaverTouchLayout;
	
	private final Handler mHandler = new Handler() {	
    	@Override
    	public void handleMessage(Message msg) {
    		switch(msg.what) {
    		// 서버에서 받아온 XML을 토대로 광고 타입에 맞는 광고 함수 호출
    		case MSG_TOP_BANNER :
    			
    			if( num == MaxContents ){
    				num = -1;
    			}
    			num++;
    			
    			mPager.setCurrentItem(num, true);
    			
    			mHandler.sendMessageDelayed( mHandler.obtainMessage( MSG_TOP_BANNER ), ScreenSaverTimer);
    			
    			break;
    		case MSG_CLOCK :
//    			long time = System.currentTimeMillis();
//    			Date today = new Date(time);       
//    			
//    			String todayStr = SimpleDateFormat.getDateInstance(SimpleDateFormat.FULL,
//    			        Locale.ENGLISH).format(today);
//    			
//    			String todayStr_1 = SimpleDateFormat.getTimeInstance(SimpleDateFormat.MEDIUM,
//    			        Locale.ENGLISH).format(today);
//    			
//    			String[] arrTime = null, arrDate = null;
//
//    			if( todayStr_1.contains("AM") ){
//    				arrTime = todayStr_1.split("AM");
//    				txtAM.setTextColor( Color.parseColor("#ffffff") );
//    				txtPM.setTextColor( Color.parseColor("#66ffffff") );
//    			} else {
//    				arrTime = todayStr_1.split("PM");
//    				txtAM.setTextColor( Color.parseColor("#66ffffff") );
//    				txtPM.setTextColor( Color.parseColor("#ffffff") );
//    			}
//    			
//    			try {
//    				arrDate = todayStr.split(",");
//    				txtToday.setText( arrDate[0] );
//    				txtDate.setText( arrDate[1] + ", " + arrDate[2] );
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//    			
//    			txtTime.setText( arrTime[0].toString().trim() );
//    			
//    			mHandler.sendMessageDelayed( mHandler.obtainMessage( MSG_CLOCK ), ClockTimer);
    			
    			break;
    		}
    	}
    };
	
	private void setThumSelected( int positon ){
		
		Scroll_X =  ( scView_Scrool_Y * positon );
		
		horaThumb.smoothScrollTo( Scroll_X, 0);
		
		num = positon;
		
		ThumScrollSelect();
	}
	
	

}
