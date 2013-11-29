package fnc.salesforce.android.AD_10003;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.ImageView.ScaleType;
import fnc.salesforce.android.R;
import fnc.salesforce.android.Constance.CONTENTS_PAGE_ALL_LIST_CONSTANCE;
import fnc.salesforce.android.Constance.Constance;
import fnc.salesforce.android.Constance.URLConstance;
import fnc.salesforce.android.LIB.BackPress;
import fnc.salesforce.android.LIB.CipherUtils;
import fnc.salesforce.android.LIB.DownloadFile;
import fnc.salesforce.android.LIB.KumaLog;
import fnc.salesforce.android.LIB.libJSON;
import fnc.salesforce.android.LIB.libJSON_GET;


public class AD_GalleryMain implements OnTouchListener, OnClickListener{

	LayoutInflater inflater;
	
	private Context mContext;
		
	ProgressDialog pd;
	
	private LinearLayout navigationRow;
	
	private boolean PageState = false;
	
	public AD_GalleryMain(Context context){
		mContext = context;
		
		pd = new ProgressDialog( context );
		
		inflater = ((Activity) mContext).getLayoutInflater();
	}
		
	String strBrnadCD = "", strContentsID = "", strContentsType ;
	View view1 = null;
	public View setAD_GalleryView(String BrandCD, String ContetsID, String strType){
		
		CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.clear();
		
		CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PAGE_ALL_LIST_THUMBPATH.clear();
		
		PageState = false;
		
		view1 = null;
		
		strBrnadCD = BrandCD;
		strContentsID = ContetsID;
		strContentsType = strType;
		
		view1 = inflater.inflate(R.layout.ad_gallery_main, null);
		
		flipper		= ( ViewFlipper ) view1.findViewById( R.id.flipper );
		
		navigationRow = ( LinearLayout ) view1.findViewById( R.id.navigationRow );
		
		txtNavi	 		= ( TextView ) view1.findViewById( R.id.txtNavi );
		
		flipper.setOnTouchListener( this );
		
		ContentsAreaLayout = null;
		
		BKPA = null;
		
		num = 1;
		
		ViewPagerPosition = 1;
		
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				new setDeviceInfor().execute();
			}
		}, 200);
		mPager = (ViewPager) view1.findViewById(R.id.pager);
		
		mPager.setOnPageChangeListener( new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				if( position == 0 ){
					CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.clear();
					
					CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PAGE_ALL_LIST_THUMBPATH.clear();
					
					PageState = false;
					
					num = 1;
					
					ViewPagerPosition = 1;
					
					new setDeviceInfor().execute();
				} else {
					if( PageState ){
						if( position > ViewPagerPosition){
							num = position;
							
							ViewPagerPosition = position;
							
							new setDeviceInfor().execute();
						} else {
							setNavi(position);
						}
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

		return view1;
	}

	public View getView(){
		return view1;
	}

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
	private int statrPosition = 0;
	
	private ImageView[] ContentsBack, ContentsImg_Img, ContentsImg_Select, ContentsImg_subThumb;
	
	private TextView[] ContentsName, CategoryName;
	
	private void removeMemory(ImageView[] mImageView ){
		
		if( mImageView != null ){
			try {
				for( int i = 0; i < mImageView.length; i++){
					if( mImageView[i] != null ){
						mImageView[i].setImageBitmap( null );
						mImageView[i] = null;
					}					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			try {
				mImageView = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		try {
			System.gc();
		} catch (Exception e) {
			e.printStackTrace();	
		}
	}
	
	private void removeMemory(TextView[] mTextView ){
		
		if( mTextView != null ){
			for( int i = 0; i < mTextView.length; i++){
				mTextView[i] = null;
			}
			
			try {
				mTextView = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		try {
			System.gc();
		} catch (Exception e) {
			e.printStackTrace();	
		}
	}
	
	private void removeMemory(RelativeLayout[] mRelativeLayout ){
		
		if( mRelativeLayout != null ){
			for( int i = 0; i < mRelativeLayout.length; i++){
				if( mRelativeLayout[i] != null ){
					mRelativeLayout[i].removeAllViews();
				}
			}
			
		}
		try {
			System.gc();
		} catch (Exception e) {
			e.printStackTrace();	
		}
	}
	
	public void DestroyView(){
		try {
			if( BKPA != null ){
				for( int i = 0; i < ( MaxPage + 1 ); i++ ){
					BKPA.destroyItem(mPager, i, ContentsAreaLayout);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		try {
			removeMemory( ContentsBack );
			removeMemory( ContentsImg_Img );
			removeMemory( ContentsImg_Select );
			removeMemory( ContentsImg_subThumb );

			removeMemory( ContentsName );
			removeMemory( CategoryName );
			
			removeMemory( ContentsAreaLayout );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setTest( int position){

		statrPosition = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.size() - 16;
		
		if( statrPosition < 0 ){
			statrPosition = 0;
		}
		int ContentsCount = 0;
		
		if ( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.size() == 0 ){
			ContentsCount = 0;
		} else if( ( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.size() % 16 ) == 0 ){
			ContentsCount = 16;
		} else {
			ContentsCount = ( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.size() % 16 );
		}
		
		if( ContentsBack == null ){
			ContentsBack		= new ImageView[ AllCount ];
			ContentsImg_Img		= new ImageView[ AllCount ];
			ContentsImg_Select	= new ImageView[ AllCount ];
			ContentsImg_subThumb= new ImageView[ AllCount ];
			
			ContentsName		= new TextView[ AllCount ];
			CategoryName		= new TextView[ AllCount ];
		} else {
			if( position == 1 ){
				
				removeMemory( ContentsBack );
				removeMemory( ContentsImg_Img );
				removeMemory( ContentsImg_Select );
				removeMemory( ContentsImg_subThumb );

				removeMemory( ContentsName );
				removeMemory( CategoryName );
				
				removeMemory( ContentsAreaLayout );
				
				ContentsBack		= new ImageView[ AllCount ];
				ContentsImg_Img		= new ImageView[ AllCount ];
				ContentsImg_Select	= new ImageView[ AllCount ];
				ContentsImg_subThumb= new ImageView[ AllCount ];
				
				ContentsName		= new TextView[ AllCount ];
				CategoryName		= new TextView[ AllCount ];
				
			}
		}

		
//		ContentsAreaLayout[position].removeAllViews();
		
		for( int i = 0; i < ContentsCount; i++ ){
			
			ContentsImg_subThumb[statrPosition] = new ImageView( mContext );
			ContentsBack[statrPosition] 		= new ImageView( mContext );
			ContentsImg_Img[statrPosition] 		= new ImageView( mContext );
			ContentsImg_Select[statrPosition] 	= new ImageView( mContext );
			
			ContentsName[statrPosition] = new TextView( mContext );
			CategoryName[statrPosition] = new TextView( mContext );
			
			RelativeLayout.LayoutParams imgContentslayout = new RelativeLayout.LayoutParams(
					195, 176);
			
			if( ( i % AllCategoryContents ) / 4 == 0 ){
				if( i > ( AllCategoryContents - 1 ) ){
					imgContentslayout.setMargins( ( 195 * ( i % 4 ) ), 0, 0, 0);	
				} else {
					imgContentslayout.setMargins( ( 195 * i ), 0, 0, 0);
				}
			} else if ( ( i % AllCategoryContents ) / 4 == 1 ){
				imgContentslayout.setMargins( ( 195 * ( i % 4 ) ), 214, 0, 0);
			} else if ( ( i % AllCategoryContents ) / 4 == 2 ){
				imgContentslayout.setMargins( ( 195 * ( i % 4 ) ), 428, 0, 0);
			} else {
				imgContentslayout.setMargins( ( 195 * ( i % 4 ) ), 642, 0, 0);
			}
			
			ContentsBack[statrPosition].setLayoutParams( imgContentslayout );
			ContentsBack[statrPosition].setBackgroundResource( R.drawable.at_sub_thumbbg );
						
			
			RelativeLayout.LayoutParams imgContentslayout_Type = new RelativeLayout.LayoutParams(
					191, 135);

			if( ( i % AllCategoryContents ) / 4 == 0 ){
				if( i > ( AllCategoryContents - 1 ) ){
					imgContentslayout_Type.setMargins( ( 195 * ( i % 4 ) ) + 2, 0, 0, 0);;	
				} else {
					imgContentslayout_Type.setMargins( ( 195 * i ) + 2, 0, 0, 0);
				}
				
			} else if ( ( i % AllCategoryContents ) / 4 == 1 ){
				imgContentslayout_Type.setMargins( ( 195 * ( i % 4 ) ) + 2, 214, 0, 0);
			} else if ( ( i % AllCategoryContents ) / 4 == 2 ){
				imgContentslayout_Type.setMargins( ( 195 * ( i % 4 ) ) + 2 , 428, 0, 0);
			} else {
				imgContentslayout_Type.setMargins( ( 195 * ( i % 4 ) ) + 2, 642, 0, 0);
			}
			
			ContentsImg_Img[statrPosition].setLayoutParams( imgContentslayout_Type );
//			ContentsImg_Img[i].setBackgroundResource( R.drawable.at_main_icon01 );

			
			try {
				ContentsImg_Img[statrPosition].setImageBitmap( BitmapFactory.decodeFile( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PAGE_ALL_LIST_THUMBPATH.get( statrPosition ).toString())  );
			} catch (Exception e) {
				e.printStackTrace();
			}
			ContentsImg_Img[statrPosition].setScaleType( ScaleType.FIT_XY );
			ContentsImg_Img[statrPosition].setOnClickListener( this );	
			
			ContentsImg_Select[statrPosition].setLayoutParams( imgContentslayout_Type );
//			ContentsImg_Select[i].setBackgroundResource( R.drawable.at_sub_thumbselect );
			
			
			RelativeLayout.LayoutParams imgContentslayout_SubThumb = new RelativeLayout.LayoutParams(
					191, 19);

			if( ( i % AllCategoryContents ) / 4 == 0 ){
				if( i > ( AllCategoryContents - 1 ) ){
					imgContentslayout_SubThumb.setMargins( ( 195 * ( i % 4 ) ) + 2, 116, 0, 0);;	
				} else {
					imgContentslayout_SubThumb.setMargins( ( 195 * i ) + 2, 116, 0, 0);
				}
				
			} else if ( ( i % AllCategoryContents ) / 4 == 1 ){
				imgContentslayout_SubThumb.setMargins( ( 195 * ( i % 4 ) ) + 2, 214 + 116, 0, 0);
			} else if ( ( i % AllCategoryContents ) / 4 == 2 ){
				imgContentslayout_SubThumb.setMargins( ( 195 * ( i % 4 ) ) + 2 , 428 + 116, 0, 0);
			} else {
				imgContentslayout_SubThumb.setMargins( ( 195 * ( i % 4 ) ) + 2, 642 + 116, 0, 0);
			}
			

			ContentsImg_subThumb[statrPosition].setLayoutParams( imgContentslayout_SubThumb );
			
			ContentsImg_subThumb[statrPosition].setScaleType( ScaleType.FIT_XY );
			ContentsImg_subThumb[statrPosition].setBackgroundResource( R.drawable.at_sub_thumbtagbg );
			
			
			
			
			RelativeLayout.LayoutParams imgContentslayout_Name = new RelativeLayout.LayoutParams(
					189, 40);

			if( ( i % AllCategoryContents ) / 4 == 0 ){
				if( i > ( AllCategoryContents - 1 ) ){
					imgContentslayout_Name.setMargins( ( 195 * ( i % 4 ) ) + 4, 136, 0, 0);
				} else {
					imgContentslayout_Name.setMargins( ( 195 * i ) + 4, 136, 0, 0);
				}
				
			} else if ( ( i % AllCategoryContents ) / 4 == 1 ){
				imgContentslayout_Name.setMargins( ( 195 * ( i % 4 ) ) + 4, 350, 0, 0);
			} else if ( ( i % AllCategoryContents ) / 4 == 2 ){
				imgContentslayout_Name.setMargins( ( 195 * ( i % 4 ) ) + 4, 564, 0, 0);
			} else {
				imgContentslayout_Name.setMargins( ( 195 * ( i % 4 ) ) + 4, 778, 0, 0);
			}
			
			try {
				ContentsName[statrPosition].setText( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( statrPosition ).cntntsNm );
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			ContentsName[statrPosition].setLayoutParams(imgContentslayout_Name);
			ContentsName[statrPosition].setGravity( Gravity.CENTER_VERTICAL );
			ContentsName[statrPosition].setTextSize( 13 );
//			ContentsName[i].setSingleLine();

			RelativeLayout.LayoutParams imgContentslayout_Category = new RelativeLayout.LayoutParams(
					171, 19);

			if( ( i % AllCategoryContents ) / 4 == 0 ){
				if( i > ( AllCategoryContents - 1 ) ){
					imgContentslayout_Category.setMargins( ( 195 * ( i % 4 ) ) + 22, 117, 0, 0);;	
				} else {
					imgContentslayout_Category.setMargins( ( 195 * i ) + 22, 117, 0, 0);
				}
				
			} else if ( ( i % AllCategoryContents ) / 4 == 1 ){
				imgContentslayout_Category.setMargins( ( 195 * ( i % 4 ) ) + 22, 214 + 117, 0, 0);
			} else if ( ( i % AllCategoryContents ) / 4 == 2 ){
				imgContentslayout_Category.setMargins( ( 195 * ( i % 4 ) ) + 22 , 428 + 117, 0, 0);
			} else {
				imgContentslayout_Category.setMargins( ( 195 * ( i % 4 ) ) + 22, 642 + 117, 0, 0);
			}
			
			try {
				CategoryName[statrPosition].setText( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( statrPosition ).ctgryNm );
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			CategoryName[statrPosition].setLayoutParams(imgContentslayout_Category);
			CategoryName[statrPosition].setGravity( Gravity.CENTER_VERTICAL );
			CategoryName[statrPosition].setTextColor( Color.WHITE );
			CategoryName[statrPosition].setTextSize( 12 );
			
			if( ContentsAreaLayout[position] != null ){
				ContentsAreaLayout[position].addView( ContentsBack[statrPosition] );
				ContentsAreaLayout[position].addView( ContentsImg_Img[statrPosition] );
				ContentsAreaLayout[position].addView( ContentsImg_Select[statrPosition] );
				ContentsAreaLayout[position].addView( ContentsName[statrPosition] );
				ContentsAreaLayout[position].addView( ContentsImg_subThumb[statrPosition] );
				ContentsAreaLayout[position].addView( CategoryName[statrPosition] );
			}
			
			statrPosition++;
		}
		
		try {
			BKPA.notifyDataSetChanged();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private int ViewPagerPosition = 1;
    
	private CipherUtils mCipher = new CipherUtils();
	
	private int verValue = 0, verMax = 1;

	private libJSON_GET GET_LibJSON = new libJSON_GET();
	private DownloadFile DLF = new DownloadFile();

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
			verMax = 1; 
			verValue = 0;
		}
		
		@Override
		protected Integer doInBackground(Integer... params) {
			while (isCancelled() == false) {
				if (verValue < verMax) {
					try {
						String[] aaa = new String[7];
						
						aaa[0] = strBrnadCD;
						aaa[1] = strContentsID;
						aaa[2] = String.valueOf( num );
						aaa[3] = String.valueOf( AllCategoryContents );
						aaa[4] = "1";
						long timestamp = System.currentTimeMillis();
						aaa[5] = mCipher.encrypt( Constance.SEED , Constance.MAC + "@" + timestamp ); 
						aaa[6] = String.valueOf( timestamp ).toString().trim(); 
						
						GET_LibJSON.getCONTENTS_PAGE_ALL_LIST( aaa );
						
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
					new setThumbNailDown().execute();
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
			
			verMax		= CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.size();
						
			verValue	= CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.size() - 16;
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
						
						CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PAGE_ALL_LIST_THUMBPATH.add( DLF.Download_HttpFile(Down_URL, Constance.FILEPATH + "/" + aaa[0] + "/" + aaa[1] +"/", strimgNAme) );
						
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
					setContentxArea();
					
					setTest(ViewPagerPosition);
					
					if( ViewPagerPosition == 1 ) {
						mPager.setCurrentItem( ViewPagerPosition );
						PageState = true;
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
	
	private Handler handler = new Handler();
	
	private ViewFlipper flipper;
//	private ImageView[] ContentsBack, ContentsImg_Img, ContentsImg_Select, ContentsImg_subThumb;
//	private TextView[] ContentsName, CategoryName;
	private int MaxContentsCount = 12;
	
	private RelativeLayout[] ContentsAreaLayout;
	
	private int AllCategoryContents = 16;
	
	int AllCount = 0;
	
	private void setContentxArea(){
		flipper.removeAllViews();
		
		MaxContentsCount 	= CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.size();
				
		AllCount = Integer.parseInt( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_ALL_LIST_PAGEING.get(0).countItem );
		
		if( AllCount / AllCategoryContents == 0 ){
			MaxPage = 1;
		} else {
			MaxPage = ( AllCount / AllCategoryContents ) + 1;
		}
		
		try {
			setNavi(num);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if( ContentsAreaLayout == null ){
			ContentsAreaLayout	= new RelativeLayout[ MaxPage + 1];
		}

		if( BKPA == null ){
			BKPA = new BkPagerAdapter( mContext );
			mPager.setAdapter( BKPA );
		}
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
			if ((start.x - event.getX()) > 50) {
				
			} else if( (start.x - event.getX()) < -50 ){
				
			}
			break;
		case MotionEvent.ACTION_POINTER_DOWN: // second finger down
			break;

		case MotionEvent.ACTION_MOVE:
			break;
		}

		return true; // indicate event was handled
	}
	
	private void ResetContents(){
		new setDeviceInfor().execute();
	}
	
	@Override
	public void onClick(View v) {
		try {
			if( ContentsImg_Img != null ){
				for( int i = 0; i < AllCount; i++){
					if( v == ContentsImg_Img[i] ){
						Constance.Ad_GalleryPage_Trans = i;
						
						
						Constance.arrTrans_UP_Information[0] = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( i ).ctgryId;
						Constance.arrTrans_UP_Information[1] = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( i ).cntntsTyCd;
						Constance.arrTrans_UP_Information[2] = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( i ).cntntsId;
						Constance.arrTrans_UP_Information[3] = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( i ).ctgryNm;
										
						Constance.arrTransInformation[0] = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( i ).brandCd;
						Constance.arrTransInformation[1] = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( i ).cntntsId;

						BackPress.getDetailPage( 3 );
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
