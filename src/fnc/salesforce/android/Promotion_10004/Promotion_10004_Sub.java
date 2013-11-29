package fnc.salesforce.android.Promotion_10004;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.ImageView.ScaleType;
import fnc.salesforce.android.R;
import fnc.salesforce.android.Activity.Promotion_10004;
import fnc.salesforce.android.Constance.CONTENTS_PAGE_ALL_LIST_CONSTANCE;
import fnc.salesforce.android.Constance.Constance;
import fnc.salesforce.android.LIB.CipherUtils;
import fnc.salesforce.android.LIB.DownloadFile;
import fnc.salesforce.android.LIB.KumaLog;
import fnc.salesforce.android.LIB.libJSON;
import fnc.salesforce.android.LIB.libJSON_GET;

public class Promotion_10004_Sub  implements OnTouchListener, OnClickListener{

	LayoutInflater inflater;
	
	private Activity mContext;
		
	ProgressDialog pd;
	
	public Promotion_10004_Sub(Activity context){		
		mContext = context;
		
		pd = new ProgressDialog( context );
		
		inflater = ((Activity) mContext).getLayoutInflater();
	}
	
	private LinearLayout navigationRow;
	
	private String strBrandCD = "", strCategoryID = "", strContentsType = "";
	
	private View mReturnView = null;
	
	public View serReturnView(){
		return mReturnView;
	}
	
	public View setPromotion_10004_Sub(String brand, String category, String strType){
		CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.clear();
		
		CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_LOOKBOOK_THUMB_PATH.clear();
		
		CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.clear();
		
		CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST_PAGE.clear();
		
		strBrandCD = brand;
		strCategoryID = category;
		strContentsType = strType;

		ContentsAreaLayout = null;
		
		BKPA = null;
		
		num = 1;
		
		mReturnView = inflater.inflate(R.layout.ad_gallery_main, null);
		
		flipper			= ( ViewFlipper ) mReturnView.findViewById( R.id.flipper );
		navigationRow	= ( LinearLayout ) mReturnView.findViewById( R.id.navigationRow );
		txtNavi	 		= ( TextView ) mReturnView.findViewById( R.id.txtNavi );
		
		flipper.setOnTouchListener( this );

		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				new setDeviceInfor().execute();
			}
		}, 200);
		
		mPager = (ViewPager) mReturnView.findViewById(R.id.pager);
		
		mPager.setOnPageChangeListener( new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				if( position == 0 ){
					
					CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.clear();
					CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST_PAGE.clear();
					CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.clear();
					
					CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_LOOKBOOK_THUMB_PATH.clear();
					
					PageState = false;
					
					num = 1;
					
					new setDeviceInfor().execute();
				} else {
					try {
						if( PageState ){
							if( position > num){
								
								num = position;
								
								new setDeviceInfor().execute();
							} else {
								setNavi(position);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		
		return mReturnView;
	}
	
	private boolean PageState = false;

	private int statrPosition = 0;
	
	private BkPagerAdapter BKPA;
	
	private ViewPager mPager;
	
	//Pager �ƴ��� ����
	private class BkPagerAdapter extends PagerAdapter{
		public BkPagerAdapter( Context con) { 
			super(); 
		}

		@Override 
		public int getCount() {
			return MaxPage + 1;
		}

		//�������� ����� �䰴ü ��/���
		@Override 
		public Object instantiateItem(View pager, int position)
		{			
			if( ContentsAreaLayout[position] == null ){

				ContentsAreaLayout[position] = new RelativeLayout( mContext );
				
				RelativeLayout.LayoutParams Contentslayout = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
				ContentsAreaLayout[position].setLayoutParams(Contentslayout);
				
				
				((ViewPager)pager).addView(ContentsAreaLayout[position], 0);
			}
			return ContentsAreaLayout[position];
		}
		//�� ��ü ����.
		@Override 
		public void destroyItem(View pager, int position, Object view) {
//			((ViewPager)pager).removeView((View)view);
		}

		@Override 
		public boolean isViewFromObject(View view, Object obj) { return view == obj; }

		@Override 
		public void finishUpdate(View arg0) {
			try {
				System.gc();
			} catch (Exception e) {
				e.printStackTrace(); 
			}
		}
		@Override 
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}
		@Override 
		public Parcelable saveState() {
			return null; 
		}
		@Override 
		public void startUpdate(View arg0) {
			
		}
	}
	
	// 메인 좌측 메뉴 Param
	private static class ContentsImg_Param {
		static int width = 361;
		static int height = 145;
		static int Count = 2;
		static int Margine_top = 245;
		static int Margine_Left = 393;
	};
	
//	메인 좌측 메뉴 Param
	private static class ContentsImg_back_Param {
		static int width = 365;
		static int height = 205;
		static int Count = 2;
		static int Margine_top = 245;
		static int Margine_Left = 395;
	};
	
	private static class ContentsImg_Link_Param {
		static int width = 30;
		static int height = 30;
		static int Count = 2;
		static int Margine_top = 115;
		static int Margine_Left = 415;
	};
	
	private static class ContentsName_Param {
		static int width = 345;
		static int height = 30;
		static int Count = 2;
		static int Margine_top = 145;
		static int Margine_Left = 415;
	};
	
	private static class PromotionDate_Param {
		static int width = 345;
		static int height = 25;
		static int Count = 2;
		static int Margine_top = 170;
		static int Margine_Left = 415;
	};
	
	private int AllCategoryContents = 8;
	
	private TextView[] ContentsName, PromotionDate;
	
	private void setTest( int position){
		
		try {
			statrPosition = AllCategoryContents * ( position - 1 );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if( ContentsImg_Img == null ){
			ContentsImg_Img		= new ImageView[ AllCount ];
			ContentsImg_back	= new ImageView[ AllCount ];
			ContentsImg_Link	= new ImageView[ AllCount ];
			
			ContentsName		= new TextView[ AllCount ];
			PromotionDate		= new TextView[ AllCount ];
		} else {
			if( position == 1 ){
				ContentsImg_Img = null;
				ContentsImg_back = null;
				ContentsImg_Link	= null;
				ContentsName = null;
				PromotionDate = null;
				
				ContentsImg_Img		= new ImageView[ AllCount ];
				ContentsImg_back	= new ImageView[ AllCount ];
				ContentsImg_Link	= new ImageView[ AllCount ];
				
				ContentsName		= new TextView[ AllCount ];
				PromotionDate		= new TextView[ AllCount ];
				
				for( int i = 0; i < ContentsAreaLayout.length; i++ ){
					if( ContentsAreaLayout[i] != null)
						ContentsAreaLayout[i].removeAllViews();
				}
			}
		}

		int ContentsCount = 0;
		String mLinkState = "";
		try {
			if( !strContentsType.contains("ALL") ){
				if ( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.size() == 0 ){
					ContentsCount = 0;
				} else if( ( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.size() % AllCategoryContents ) == 0 ){
					ContentsCount = AllCategoryContents;
				} else {
					ContentsCount = ( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.size() % AllCategoryContents );
				}
			} else {
				if ( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.size() == 0 ){
					ContentsCount = 0;
				} else if( ( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.size() % AllCategoryContents ) == 0 ){
					ContentsCount = AllCategoryContents;
				} else {
					ContentsCount = ( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.size() % AllCategoryContents );
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		for( int i = 0; i < ContentsCount; i++ ){

			ContentsImg_Img[statrPosition] 		= new ImageView( mContext );
			ContentsImg_back[statrPosition] 	= new ImageView( mContext );
			ContentsImg_Link[statrPosition] 	= new ImageView( mContext );
			
			ContentsName[statrPosition]			= new TextView( mContext );
			PromotionDate[statrPosition]			= new TextView( mContext );
			
			RelativeLayout.LayoutParams imgContentslayout_Type = new RelativeLayout.LayoutParams(
					ContentsImg_Param.width, ContentsImg_Param.height);

			int height_MargineCount =  i / ContentsImg_Param.Count;
			
			int Width_MargineCount =  i % ContentsImg_Param.Count;
			
			int Margine_Left  = 0;
			
			if( Width_MargineCount == 0 ){
				Margine_Left = 12;
			} else {
				Margine_Left = ( ContentsImg_Param.Margine_Left * Width_MargineCount ) + 12;
			}
			
			
			imgContentslayout_Type.setMargins( Margine_Left , ContentsImg_Param.Margine_top * height_MargineCount , 0, 0);

			ContentsImg_Img[statrPosition].setLayoutParams( imgContentslayout_Type );
			
			try {
				ContentsImg_Img[statrPosition].setImageBitmap( BitmapFactory.decodeFile( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_LOOKBOOK_THUMB_PATH.get( statrPosition ).toString())  );
			} catch (Exception e) {
				e.printStackTrace();
			}
			ContentsImg_Img[statrPosition].setScaleType( ScaleType.FIT_XY );
			ContentsImg_Img[statrPosition].setOnClickListener( this );	
			
			RelativeLayout.LayoutParams imgContentslayout_Back = new RelativeLayout.LayoutParams(
					ContentsImg_back_Param.width, ContentsImg_back_Param.height);

			height_MargineCount =  i / ContentsImg_back_Param.Count;
			Width_MargineCount =  i % ContentsImg_back_Param.Count;
			
			Margine_Left  = 0;
			
			if( Width_MargineCount == 0 ){
				Margine_Left = 10;
			} else {
				Margine_Left = ( ContentsImg_back_Param.Margine_Left * Width_MargineCount ) + 10;
			}
			
			
			imgContentslayout_Back.setMargins( Margine_Left , ContentsImg_back_Param.Margine_top * height_MargineCount , 0, 0);

			ContentsImg_back[statrPosition].setLayoutParams( imgContentslayout_Back );
			
			try {
				ContentsImg_back[statrPosition].setBackgroundResource( R.drawable.at_pr_thumbbg);
			} catch (Exception e) {
				e.printStackTrace();
			}
			

			RelativeLayout.LayoutParams imgContentslayout_Link= new RelativeLayout.LayoutParams(
					ContentsImg_Link_Param.width, ContentsImg_Link_Param.height);

			height_MargineCount =  i / ContentsImg_Link_Param.Count;
			
			Width_MargineCount =  i % ContentsImg_Link_Param.Count;
			
			Margine_Left  = 0;
			
			int Margine_Top  = 0; 
			
			if( Width_MargineCount == 0 ){
				Margine_Left = 20;
			} else {
				Margine_Left = ( ContentsImg_Link_Param.Margine_Left * Width_MargineCount );
			}

			if( height_MargineCount == 0 ){
				Margine_Top = ContentsImg_Link_Param.Margine_top;
			} else {
				Margine_Top = ( ( ContentsImg_Param.Margine_top * height_MargineCount ) + ContentsImg_Link_Param.Margine_top );
			}
			
			imgContentslayout_Link.setMargins( ( Margine_Left + 323), Margine_Top , 0, 0);
			

			ContentsImg_Link[statrPosition].setLayoutParams( imgContentslayout_Link );
			
			try {
				ContentsImg_Link[statrPosition].setBackgroundResource( R.drawable.at_promo_link);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if( !strContentsType.contains("ALL") ){
				mLinkState  = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.get( statrPosition ).linkUrl;
			} else {
				mLinkState  = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( statrPosition ).linkUrl;
			}

			if( !mLinkState.contains("http")){
				ContentsImg_Link[statrPosition].setVisibility( View.GONE );
			}
			
			RelativeLayout.LayoutParams imgContentsContentsName= new RelativeLayout.LayoutParams(
					ContentsName_Param.width, ContentsName_Param.height);

			height_MargineCount =  i / ContentsName_Param.Count;
			
			Width_MargineCount =  i % ContentsName_Param.Count;
			
			Margine_Left  = 0;
			
			if( Width_MargineCount == 0 ){
				Margine_Left = 20;
			} else {
				Margine_Left = ( ContentsName_Param.Margine_Left * Width_MargineCount );
			}

			if( height_MargineCount == 0 ){
				Margine_Top = ContentsName_Param.Margine_top;
			} else {
				Margine_Top = ( ( ContentsImg_Param.Margine_top * height_MargineCount ) + ContentsName_Param.Margine_top );
			}
			
			imgContentsContentsName.setMargins( Margine_Left , Margine_Top , 0, 0);

			ContentsName[statrPosition].setLayoutParams( imgContentsContentsName );
			
			try {
				ContentsName[statrPosition].setBackgroundColor( Color.parseColor("#00000000"));
				ContentsName[statrPosition].setTextColor( Color.WHITE );
				ContentsName[statrPosition].setTextSize( 14 );
				ContentsName[statrPosition].setGravity( Gravity.CENTER_VERTICAL );
				
				if( !strContentsType.contains("ALL") ){
					ContentsName[statrPosition].setText( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.get( statrPosition ).cntntsNm );
				} else {
					ContentsName[statrPosition].setText( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( statrPosition ).cntntsNm );
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			RelativeLayout.LayoutParams imgContentsPromotionDate = new RelativeLayout.LayoutParams(
					PromotionDate_Param.width, PromotionDate_Param.height);

			height_MargineCount =  i / PromotionDate_Param.Count;
			
			Width_MargineCount =  i % PromotionDate_Param.Count;
			
			Margine_Left  = 0;
			 
			
			if( Width_MargineCount == 0 ){
				Margine_Left = 20;
			} else {
				Margine_Left = ( PromotionDate_Param.Margine_Left * Width_MargineCount );
			}
			
			if( height_MargineCount == 0 ){
				Margine_Top = PromotionDate_Param.Margine_top;
			} else {
				Margine_Top = ( ( ContentsImg_Param.Margine_top * height_MargineCount ) + PromotionDate_Param.Margine_top);
			}
			
			imgContentsPromotionDate.setMargins( Margine_Left , Margine_Top , 0, 0);

			PromotionDate[statrPosition].setLayoutParams( imgContentsPromotionDate );
			
			try {
				PromotionDate[statrPosition].setBackgroundColor( Color.parseColor("#00000000"));
				PromotionDate[statrPosition].setTextColor( Color.parseColor("#66ffffff") );
				PromotionDate[statrPosition].setTextSize( 13 );
				PromotionDate[statrPosition].setGravity( Gravity.CENTER_VERTICAL );
				
				if( !strContentsType.contains("ALL") ){
					String date = setDate( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.get( statrPosition ).bgnDt ) + " ~ " +
							setDate(CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.get( statrPosition ).endDt );
					PromotionDate[statrPosition].setText( date );
				} else {
					String date = setDate( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( statrPosition ).bgnDt ) + " ~ " +
							setDate(CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( statrPosition ).endDt );
					PromotionDate[statrPosition].setText( date );
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if( ContentsAreaLayout[position] != null ){
				ContentsAreaLayout[position].addView( ContentsImg_back[statrPosition] );
				ContentsAreaLayout[position].addView( ContentsImg_Img[statrPosition] );
				
				
				ContentsAreaLayout[position].addView( ContentsName[statrPosition] );
				ContentsAreaLayout[position].addView( PromotionDate[statrPosition] );
				
				ContentsAreaLayout[position].addView( ContentsImg_Link[statrPosition] );
			}
			
			statrPosition++;
		}
	}
	
	private String setDate( String strDate ){
		String mDate = "";
		for( int i = 0; i < strDate.length(); i++){
			mDate += strDate.charAt( i );
			if( i == 3 ){
				mDate = mDate +"-";
			} else if( i == 5 ){
				mDate = mDate +"-";
			}
		}
		
		return mDate;
	}
	
	private String[] DetailParam;
	
	private ImageView[] imgNavi;
	private TextView txtNavi, txtNavi_Next;
	private RelativeLayout[] NaviLayout_SUB;
	private RelativeLayout txtNaviLayout;
	private int MaxNaviPage = 10;
	
	private void setNavi( int position){
		MaxNaviPage = 10;
		
		int NaviPosition = 0;
		
		if( MaxPage / MaxNaviPage == ( position - 1 ) / MaxNaviPage){
			MaxNaviPage = MaxPage % MaxNaviPage;
		} else {
			MaxNaviPage = 10;
		}

		navigationRow.removeAllViews();
		
		if( imgNavi != null ){
			imgNavi = null;
			txtNavi_Next = null;
		}
		if( NaviLayout_SUB != null )
			NaviLayout_SUB = null;
		
		imgNavi 		= new ImageView[ MaxNaviPage ];
		txtNavi_Next	= new TextView( mContext );
		txtNaviLayout	= new RelativeLayout( mContext );
		NaviLayout_SUB 	= new RelativeLayout[ MaxNaviPage ];
		
		for( int i = 0; i < MaxNaviPage; i++ ){
			imgNavi[i] 			= new ImageView( mContext );
			NaviLayout_SUB[i] 	= new RelativeLayout( mContext );
			
			RelativeLayout.LayoutParams NaviLayout = new RelativeLayout.LayoutParams(
					11, 12);
			NaviLayout.setMargins( 0, 0, 0, 0);;
			
			imgNavi[i].setLayoutParams( NaviLayout );
			
			imgNavi[i].setBackgroundResource( R.drawable.at_btn_slidecircle_n );
			
			RelativeLayout.LayoutParams Navi_SUB_Layout = new RelativeLayout.LayoutParams(
					20, 12);
			Navi_SUB_Layout.setMargins( 0, 0, 0, 0);;
			NaviLayout_SUB[i].setLayoutParams( Navi_SUB_Layout );
			NaviLayout_SUB[i].setGravity( Gravity.CENTER );
			NaviLayout_SUB[i].addView( imgNavi[i] );
			navigationRow.addView( NaviLayout_SUB[i] );
		}
		
		if( strContentsType.equals("ALL") ){
			RelativeLayout.LayoutParams Navi_Layout = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT, 15);
			Navi_Layout.setMargins( 0, 1000, 0, 0);
			navigationRow.setLayoutParams( Navi_Layout );
			
			RelativeLayout.LayoutParams Navi_Layout_1 = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT, 15);
			Navi_Layout_1.setMargins( 0, 1015, 0, 0);
			txtNavi.setLayoutParams( Navi_Layout_1 );
			
		} else {
			RelativeLayout.LayoutParams Navi_Layout = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT, 15);
			Navi_Layout.setMargins( 0, 950, 0, 0);
			navigationRow.setLayoutParams( Navi_Layout );
			
			RelativeLayout.LayoutParams Navi_Layout_1 = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT, 15);
			Navi_Layout_1.setMargins( 0, 965, 0, 0);
			txtNavi.setLayoutParams( Navi_Layout_1 );
		}
		
		if( MaxNaviPage == 10){
			RelativeLayout.LayoutParams Navi_Next_Layout = new RelativeLayout.LayoutParams(
					20, 20);
			Navi_Next_Layout.setMargins( 0, 0, 0, 0);
			txtNaviLayout.setLayoutParams( Navi_Next_Layout );
			
			Navi_Next_Layout.setMargins( 0, -5, 0, 0);
			txtNavi_Next.setLayoutParams( Navi_Next_Layout );
			txtNavi_Next.setText(">");
			txtNavi_Next.setTextColor( Color.parseColor( "#ffffff" ) );
			txtNavi_Next.setGravity( Gravity.CENTER );
			txtNavi_Next.setTextSize( 19 );

			txtNaviLayout.addView( txtNavi_Next );
			navigationRow.addView( txtNaviLayout );
		}
		
		try {
			txtNavi.setText( "" + position );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		NaviPosition = position % 10;
		
		if( NaviPosition == 0 ){
			NaviPosition = 10;
		}
		
		imgNavi[NaviPosition - 1].setBackgroundResource( R.drawable.at_btn_slidecircle_o );
	}
	
	private CipherUtils mCipher = new CipherUtils();
	
	private int verValue = 0, verMax = 1;

	private libJSON LibJSON = new libJSON();
	private libJSON_GET GET_LibJSON = new libJSON_GET();
	private DownloadFile DLF = new DownloadFile();
	public String FOLDER_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test/";
	
	class setDeviceInfor extends AsyncTask<Integer, Integer, Integer> {
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
						
						aaa[0] = strBrandCD;
						aaa[1] = strCategoryID;
						aaa[2] = String.valueOf( num );
						aaa[3] = String.valueOf( AllCategoryContents );;
						aaa[4] = "1";
						
						long timestamp = System.currentTimeMillis();
						
						aaa[5] = mCipher.encrypt( Constance.SEED , Constance.MAC + "@" + timestamp ); 
						aaa[6] = String.valueOf( timestamp ).toString().trim(); 
						
						if( strContentsType.contains("ALL") ){
							GET_LibJSON.getCONTENTS_PAGE_ALL_LIST( aaa );
						} else {
							GET_LibJSON.getCONTENTS_PAGE_LIST( aaa );
						}
						
//						GET_LibJSON.getMAIN_LOGO( "6J" );
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
					if( strContentsType.contains("ALL") ){
						new setThumbNailDown().execute();
					} else {
						new setThumbNailDown_SUB().execute();
					}
					
				}
			});
		}
		@Override
		protected void onCancelled() {
		}
	}
	
	class setThumbNailDown extends AsyncTask<Integer, Integer, Integer> {
		@Override
		protected void onPreExecute() {
			
			verMax = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.size();
			
			verValue = 0;
		}
		
		@Override
		protected Integer doInBackground(Integer... params) {
			while (isCancelled() == false) {
				if (verValue < verMax) {
					try {
						String[] aaa = new String[4];
						
						aaa[0] = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( verValue ).brandCd;
						aaa[1] = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( verValue ).cntntsId;						
						String Down_URL = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( verValue ).dwldThumbPath;
						String strimgNAme = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( verValue ).thumbFileLc;

						CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_LOOKBOOK_THUMB_PATH.add( DLF.Download_HttpFile(Down_URL, Constance.FILEPATH + "/" + aaa[0] + "/" + aaa[1] +"/", strimgNAme) );
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
					try {
						setContentxArea();
						
						setTest(num);
						
						if( num == 1 ) {
							mPager.setCurrentItem( num );
							PageState = true;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					
					pd.dismiss();
					pd.cancel();
				}
			});
		}
		@Override
		protected void onCancelled() {
		}
	}
	
	class setThumbNailDown_SUB extends AsyncTask<Integer, Integer, Integer> {
		@Override
		protected void onPreExecute() {
			
			verMax = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.size();
			
			verValue = 0;
		}
		
		@Override
		protected Integer doInBackground(Integer... params) {
			while (isCancelled() == false) {
				if (verValue < verMax) {
					try {
						String[] aaa = new String[4];
						
						aaa[0] = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.get( verValue ).brandCd;
						aaa[1] = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.get( verValue ).cntntsId;
						
						String Down_URL = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.get( verValue ).dwldThumbPath;
						String strimgNAme = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.get( verValue ).thumbFileLc;
						
						CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_LOOKBOOK_THUMB_PATH.add( DLF.Download_HttpFile(Down_URL, Constance.FILEPATH + "/" + aaa[0] + "/" + aaa[1] +"/", strimgNAme) );
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
					
					try {
						setContentxArea();
						setTest(num);
						
						if( num == 1 ) {
							mPager.setCurrentItem( num );
							PageState = true;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					
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
	
	private ViewFlipper flipper;
	private ImageView[] ContentsImg_Img, ContentsImg_back, ContentsImg_Link;
	private int MaxContentsCount = 12;
	private RelativeLayout[] ContentsAreaLayout;
	
	
	int AllCount = 0;
	
	private void setContentxArea(){
		
		if( strContentsType.contains("ALL") ){
			MaxContentsCount 	= CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.size();
		} else {
			MaxContentsCount 	= CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.size();
		}
		
		
		if( strContentsType.contains("ALL") ){
			if( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_ALL_LIST_PAGEING.size() > 0 ){
				AllCount = Integer.parseInt( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_ALL_LIST_PAGEING.get(0).countItem );
			} else {
				AllCount = 0;
			}
		} else {
			if( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST_PAGE.size() > 0 ){
				AllCount = Integer.parseInt( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST_PAGE.get(0).countItem );
			} else {
				AllCount = 0;
			}
			
		}
		
		if( AllCount / AllCategoryContents == 0 ){
			MaxPage = 1;
		} else {
			MaxPage = ( AllCount / AllCategoryContents ) + 1;
		}

		try {
			setNavi( num );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if( ContentsAreaLayout == null ){
			ContentsAreaLayout	= new RelativeLayout[ MaxPage + 1];
		}
		
		if( BKPA == null ){
			BKPA = new BkPagerAdapter( mContext );
			mPager.setAdapter( BKPA );
		} else {
			if( num == 1 )
				BKPA.notifyDataSetChanged();
		}
	}
	
	PointF start = new PointF();
	PointF mid = new PointF();
		
	private int num = 1, MaxPage = 1;
	
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
			break;
		case MotionEvent.ACTION_POINTER_DOWN: // second finger down
			break;

		case MotionEvent.ACTION_MOVE:
			break;
		}

		return true; // indicate event was handled
	} 

	@Override
	public void onClick(View v) {
		try {
			if( ContentsImg_Img != null ){
				for( int i = 0; i < MaxContentsCount; i++){
					if( v == ContentsImg_Img[i] ){
						
						String ContentsID = "", strLinkURL = "", strDownURL = "", strFileName = "";
						
						if( strContentsType.contains("ALL") ){
							ContentsID =  CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( i ).cntntsId;
							strDownURL =  CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( i ).dwldCntntsPath;
							strLinkURL =  CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( i ).linkUrl;
							strFileName =  CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( i ).cntntsFileLc;
						} else {
							ContentsID =  CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.get( i ).cntntsId;
							strDownURL =  CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.get( i ).dwldCntntsPath;
							strLinkURL =  CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.get( i ).linkUrl;
							strFileName =  CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.get( i ).cntntsFileLc;
						}
						
						DetailParam = new String[4];
												
						DetailParam[0] = strDownURL;
						DetailParam[1] = strLinkURL;
						DetailParam[2] = ContentsID;
						DetailParam[3] = strFileName;
						
						((Promotion_10004) mContext).setDetailPage(DetailParam);
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	public boolean onDown(MotionEvent e) {
		return false;
	}

	public boolean onFling(MotionEvent startEvent, MotionEvent endEvent,
			float velocityX, float velocityY) {
		return false;
	}

	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}
}
