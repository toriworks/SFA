package fnc.salesforce.android.Repair;

import java.io.File;

import com.artifex.mupdfdemo.MuPDFActivity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.ImageView.ScaleType;
import fnc.salesforce.android.R;
import fnc.salesforce.android.Activity.EDUCATION_Main;
import fnc.salesforce.android.Constance.CONTENTS_PAGE_ALL_LIST_CONSTANCE;
import fnc.salesforce.android.Constance.Constance;
import fnc.salesforce.android.Constance.URLConstance;
import fnc.salesforce.android.LIB.CipherUtils;
import fnc.salesforce.android.LIB.Decimal_Coma;
import fnc.salesforce.android.LIB.DownloadFile;
import fnc.salesforce.android.LIB.ImageDownloader;
import fnc.salesforce.android.LIB.ImageDownloader_Second;
import fnc.salesforce.android.LIB.KumaLog;
import fnc.salesforce.android.LIB.ProductDialog;
import fnc.salesforce.android.LIB.libJSON;
import fnc.salesforce.android.LIB.libJSON_GET;

public class Repair_Before_After implements OnClickListener{

	LayoutInflater inflater;
	
	private Context mContext;
		
	ProgressDialog pd;
	
	public Repair_Before_After(Context context){
		mContext = context;
		
		pd = new ProgressDialog( context );
		
		inflater = ((Activity) mContext).getLayoutInflater();
	}
	private RelativeLayout layoutRepairGoods, layoutRepairItem;

	private LinearLayout navigationRow;
	
	View mRepair_Before_Afterview = null;
	
	public View setRepair_Before_After(){
		
		CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_REPAIR_IMAGE_RESULT_LIST.clear();
				
		CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PAGE_ALL_LIST_THUMBPATH.clear();
		mRepair_Before_Afterview = null;
		
		ContentsAreaLayout = null;
		
		BKPA = null;
		
		num = 1;
		
		mRepair_Before_Afterview = inflater.inflate(R.layout.repaire_before_after, null);
		
		navigationRow		= ( LinearLayout ) mRepair_Before_Afterview.findViewById( R.id.navigationRow );
		layoutRepairGoods	= ( RelativeLayout ) mRepair_Before_Afterview.findViewById( R.id.layoutRepairGoods );
		layoutRepairItem	= ( RelativeLayout ) mRepair_Before_Afterview.findViewById( R.id.layoutRepairItem );
		txtNavi	 			= ( TextView ) mRepair_Before_Afterview.findViewById( R.id.txtNavi );
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				new setRepairGoods().execute();
			}
		}, 200);
		
		mPager = (ViewPager) mRepair_Before_Afterview.findViewById(R.id.pager);
		
		mPager.setOnPageChangeListener( new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				if( position == 0 ){
					
					CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_REPAIR_IMAGE_RESULT_LIST.clear();
					
					CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PAGE_ALL_LIST_THUMBPATH.clear();
					
					PageState = false;
					
					statrPosition = 0;
					
					num = 1;
					
					new setRepair_Before_After_Search().execute();
				} else {
					try {
						if( PageState ){
							if( position > num){
								
								num = position;
								
								new setRepair_Before_After_Search().execute();
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
		return mRepair_Before_Afterview;
	}
	
	public View getView(){
		return mRepair_Before_Afterview;
	}
	
	// 메인 좌측 메뉴 Param
	private static class mRepair_Goods_Param {
		static int width = 140;
		static int height = 30;
		static int Count = 2;
		static int Margine_top = 30;
		static int Margine_Left = 155;
	};
	
	
	private TextView[] mRepair_Goods_txt;
	
	private void setRepair_Goods(){
		layoutRepairGoods.removeAllViews();
		
		int productCodeSize = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_REPAIR_SPECIES_LIST.size() + 1;
		
		if( mRepair_Goods_txt == null ){
			mRepair_Goods_txt		= new TextView[ productCodeSize ];
		} else {
			mRepair_Goods_txt = null;
			
			mRepair_Goods_txt		= new TextView[ productCodeSize ];
		}
		
		for( int i = 0; i < productCodeSize; i++ ){
			
			mRepair_Goods_txt[i] 		= new TextView( mContext );

			RelativeLayout.LayoutParams imgContentslayout_Back = new RelativeLayout.LayoutParams(
					mRepair_Goods_Param.width, mRepair_Goods_Param.height);

			int height_MargineCount =  i / mRepair_Goods_Param.Count;
			
			int Width_MargineCount =  i % mRepair_Goods_Param.Count;
			
			int Margine_Left  = 0;
			
			Margine_Left = ( mRepair_Goods_Param.Margine_Left * Width_MargineCount ) + 7;
			
			
			imgContentslayout_Back.setMargins( Margine_Left , ( mRepair_Goods_Param.Margine_top * height_MargineCount ) + 5 , 0, 0);
			
			try {
				if( i == 0 ){
					mRepair_Goods_txt[i].setText( "전체" );
				} else {
					mRepair_Goods_txt[i].setText( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_REPAIR_SPECIES_LIST.get( i - 1 ).rpairsSpeciesNm );
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			mRepair_Goods_txt[i].setOnClickListener( this );
			
			mRepair_Goods_txt[i].setLayoutParams(imgContentslayout_Back);
			mRepair_Goods_txt[i].setGravity( Gravity.CENTER_VERTICAL );
			mRepair_Goods_txt[i].setTextSize( 15 );
			mRepair_Goods_txt[i].setTextColor(Color.WHITE );
			mRepair_Goods_txt[i].setSingleLine();

			layoutRepairGoods.addView( mRepair_Goods_txt[i] );
		}
	}
	
	private void setRepair_Goods_Choice( int position ){
		
		for( int i = 0; i < mRepair_Goods_txt.length; i++ ){
			if( i == position ){
				mRepair_Goods_txt[ i ].setTextColor(Color.parseColor("#e3cf2a") );
			} else {
				mRepair_Goods_txt[i].setTextColor(Color.parseColor("#ffffff") );
			}
		}
	}
	
	
	
	// 메인 좌측 메뉴 Param
	private static class Repair_Item_Type {
		static int width = 140;
		static int height = 30;
		static int Count = 3;
		static int Margine_top = 30;
		static int Margine_Left = 155;
	};
	
	
	private TextView[] mRepair_Item_txt;

	private void setRepair_Item(){
		layoutRepairItem.removeAllViews();
		
		int productCodeSize = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_REPAIR_IMAGE_ITEM_LIST.size() + 1;
		
		if( mRepair_Item_txt == null ){
			mRepair_Item_txt		= new TextView[ productCodeSize ];
		} else {
			mRepair_Item_txt = null;
			
			mRepair_Item_txt		= new TextView[ productCodeSize ];
		}
		
		for( int i = 0; i < productCodeSize; i++ ){
			
			mRepair_Item_txt[i] 		= new TextView( mContext );

			RelativeLayout.LayoutParams imgContentslayout_Back = new RelativeLayout.LayoutParams(
					Repair_Item_Type.width, Repair_Item_Type.height);

			int height_MargineCount =  i / Repair_Item_Type.Count;
			
			int Width_MargineCount =  i % Repair_Item_Type.Count;
			
			int Margine_Left  = 0;
			
			Margine_Left = ( Repair_Item_Type.Margine_Left * Width_MargineCount ) + 7;
			
			
			imgContentslayout_Back.setMargins( Margine_Left , ( Repair_Item_Type.Margine_top * height_MargineCount ) + 5 , 0, 0);
			
			try {
				if( i == 0 ){
					mRepair_Item_txt[i].setText( "전체" );
				} else {
					mRepair_Item_txt[i].setText( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_REPAIR_IMAGE_ITEM_LIST.get( i - 1 ).rpairsItemNm );
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			mRepair_Item_txt[i].setOnClickListener( this );
			
			mRepair_Item_txt[i].setLayoutParams(imgContentslayout_Back);
			mRepair_Item_txt[i].setGravity( Gravity.CENTER_VERTICAL );
			mRepair_Item_txt[i].setTextSize( 15 );
			mRepair_Item_txt[i].setTextColor(Color.WHITE );
			mRepair_Item_txt[i].setSingleLine();

			layoutRepairItem.addView( mRepair_Item_txt[i] );
		}
	}
	
	private void setRepair_Item_Choice( int position ){

		for( int i = 0; i < mRepair_Item_txt.length; i++ ){
			if( i == position ){
				mRepair_Item_txt[ i ].setTextColor(Color.parseColor("#89bd50") );
			} else {
				mRepair_Item_txt[i].setTextColor(Color.parseColor("#ffffff") );
			}
		}
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

	private ImageDownloader mImageDown = new ImageDownloader();
	
	// 메인 좌측 메뉴 Param
	private static class ContentsImgBack_Param {
		static int width = 188;
		static int height = 233;
		static int Count = 4;
		static int Margine_top = 243;
		static int Margine_Left = 193;
	};
	// 메인 좌측 메뉴 Param
	private static class ContentsImg_Param {
		static int width = 173;
		static int height = 173;
		static int Count = 4;
		static int Margine_top = 240;
		static int Margine_Left = 193;
	};
		
	// 메인 좌측 메뉴 Param
	private static class ContentstxtGoods_Param {
		static int width = 173;
		static int height = 20;
		static int Count = 4;
		static int Margine_top = 186;
		static int Margine_Left = 193;
	};
	
	// 메인 좌측 메뉴 Param
	private static class Contentstxt_ASCODE_Param {
		static int width = 173;
		static int height = 20;
		static int Count = 4;
		static int Margine_top = 206;
		static int Margine_Left = 193;
	};

	private Decimal_Coma DC = new Decimal_Coma();
		
	private void setTest( int position){
		try {
			statrPosition = AllCategoryContents * ( position - 1 );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if( ContentsBack == null ){
			ContentsBack		= new ImageView[ AllCount ];
			ContentsImg_Img		= new ImageView[ AllCount ];
			ContentsImg_subThumb= new ImageView[ AllCount ];
			
			ContentsName		= new TextView[ AllCount ];
			ProductCode			= new TextView[ AllCount ];

		} else {
			if( position == 1 ){
				ContentsBack = null;
				ContentsImg_Img = null;
				ContentsImg_subThumb = null;
				ContentsName = null;
				ProductCode = null;
				
				ContentsBack		= new ImageView[ AllCount ];
				ContentsImg_Img		= new ImageView[ AllCount ];
				ContentsImg_subThumb= new ImageView[ AllCount ];
				
				ContentsName		= new TextView[ AllCount ];
				ProductCode			= new TextView[ AllCount ];

				for( int i = 0; i < ContentsAreaLayout.length; i++ ){
					if( ContentsAreaLayout[i] != null)
						ContentsAreaLayout[i].removeAllViews();
				}
			}
		}

		int ContentsCount = 0;
		
		try {
			if ( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_REPAIR_IMAGE_RESULT_LIST.size() == 0 ){
				ContentsCount = 0;
			} else if( ( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_REPAIR_IMAGE_RESULT_LIST.size() % AllCategoryContents ) == 0 ){
				ContentsCount = AllCategoryContents;
			} else {
				ContentsCount = ( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_REPAIR_IMAGE_RESULT_LIST.size() % AllCategoryContents );
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		for( int i = 0; i < ContentsCount; i++ ){
			
			ContentsImg_subThumb[statrPosition] = new ImageView( mContext );
			ContentsBack[statrPosition] 		= new ImageView( mContext );
			ContentsImg_Img[statrPosition] 		= new ImageView( mContext );
			
			ContentsName[statrPosition]	 	= new TextView( mContext );
			ProductCode[statrPosition] 		= new TextView( mContext );
			
			RelativeLayout.LayoutParams imgContentslayout_Back = new RelativeLayout.LayoutParams(
					ContentsImgBack_Param.width, ContentsImgBack_Param.height);

			int height_MargineCount =  i / ContentsImgBack_Param.Count;
			
			int Width_MargineCount =  i % ContentsImgBack_Param.Count;
			
			int Margine_Left  = 0;
			
			Margine_Left = ( ContentsImgBack_Param.Margine_Left * Width_MargineCount ) + 7;
			
			
			imgContentslayout_Back.setMargins( Margine_Left , ContentsImgBack_Param.Margine_top * height_MargineCount , 0, 0);
			
			ContentsBack[statrPosition].setLayoutParams( imgContentslayout_Back );
			ContentsBack[statrPosition].setBackgroundResource( R.drawable.at_sf_as_thumbbg );

			
			RelativeLayout.LayoutParams imgContentslayout_Img = new RelativeLayout.LayoutParams(
					ContentsImg_Param.width, ContentsImg_Param.height);

			height_MargineCount =  i / ContentsImg_Param.Count;
			
			Width_MargineCount =  i % ContentsImg_Param.Count;
			
			Margine_Left  = 0;
			
			Margine_Left = ( ContentsImg_Param.Margine_Left * Width_MargineCount ) + 14;
			
			
			imgContentslayout_Img.setMargins( Margine_Left , ( ContentsImg_Param.Margine_top * height_MargineCount ) + 10, 0, 0);

			ContentsImg_Img[statrPosition].setLayoutParams( imgContentslayout_Img );


//			try {
//				String First_URL = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_REPAIR_IMAGE_RESULT_LIST.get( statrPosition ).thumbUrl.toString();
//				mImageDown.download( First_URL, ContentsImg_Img[statrPosition], null);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
			
			try {
				ContentsImg_Img[statrPosition].setImageBitmap( ReturnBitmap_Rotate90 ( BitmapFactory.decodeFile( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PAGE_ALL_LIST_THUMBPATH.get( statrPosition ).toString()), 173, 173 )  );
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			ContentsImg_Img[statrPosition].setScaleType( ScaleType.FIT_XY );
			ContentsImg_Img[statrPosition].setOnClickListener( this );	

			RelativeLayout.LayoutParams imgContentslayout_Name = new RelativeLayout.LayoutParams(
					ContentstxtGoods_Param.width, ContentstxtGoods_Param.height);

			height_MargineCount =  i / ContentstxtGoods_Param.Count;
			
			Width_MargineCount =  i % ContentstxtGoods_Param.Count;
			
			Margine_Left  = 0;
			
			Margine_Left = ( ContentstxtGoods_Param.Margine_Left * Width_MargineCount ) + 14;
	
			
			imgContentslayout_Name.setMargins( Margine_Left , ( ContentsImgBack_Param.Margine_top * height_MargineCount ) + ContentstxtGoods_Param.Margine_top, 0, 0);
			
			try {
				ContentsName[statrPosition].setText( "품종 : " + CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_REPAIR_IMAGE_RESULT_LIST.get( statrPosition ).pumNm );
			} catch (Exception e) {
				ContentsName[statrPosition].setText( "품종 : " );
				e.printStackTrace();
			}
			
			ContentsName[statrPosition].setLayoutParams(imgContentslayout_Name);
			ContentsName[statrPosition].setGravity( Gravity.CENTER_VERTICAL );
			ContentsName[statrPosition].setTextSize( 13 );
			ContentsName[statrPosition].setTextColor(Color.WHITE );
			ContentsName[i].setSingleLine();

			RelativeLayout.LayoutParams imgContentslayout_Code = new RelativeLayout.LayoutParams(
					Contentstxt_ASCODE_Param.width, Contentstxt_ASCODE_Param.height);

			height_MargineCount =  i / Contentstxt_ASCODE_Param.Count;
			
			Width_MargineCount =  i % Contentstxt_ASCODE_Param.Count;
			
			Margine_Left  = 0;
			
			Margine_Left = ( Contentstxt_ASCODE_Param.Margine_Left * Width_MargineCount ) + 14;
			
			
			imgContentslayout_Code.setMargins( Margine_Left , ( ContentsImgBack_Param.Margine_top * height_MargineCount ) + Contentstxt_ASCODE_Param.Margine_top, 0, 0);
			
//			try {
//				ProductCode[statrPosition].setText( "AS코드 : " + CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_REPAIR_IMAGE_RESULT_LIST.get( statrPosition ).asNm );
//			} catch (Exception e) {
//				ProductCode[statrPosition].setText( "AS코드 : " );
//				e.printStackTrace();
//			}
			try {
				String campaignNm = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_REPAIR_IMAGE_RESULT_LIST.get( statrPosition ).asNm;
				if( campaignNm.equals("null") ){
					ProductCode[statrPosition].setText( "AS코드 : " + " " );
				} else {
					ProductCode[statrPosition].setText( "AS코드 : " + campaignNm );
				}
			} catch (Exception e) {
				ProductCode[statrPosition].setText( "AS코드 : " + " " );
			}
			
			ProductCode[statrPosition].setLayoutParams(imgContentslayout_Code);
			ProductCode[statrPosition].setGravity( Gravity.CENTER_VERTICAL );
			ProductCode[statrPosition].setTextSize( 13 );
			ProductCode[statrPosition].setTextColor(Color.WHITE );
			ProductCode[i].setSingleLine();


			if( ContentsAreaLayout[position] != null ){				
				ContentsAreaLayout[position].addView( ContentsBack[statrPosition] );
				ContentsAreaLayout[position].addView( ContentsImg_Img[statrPosition] );
				ContentsAreaLayout[position].addView( ContentsName[statrPosition] );
				ContentsAreaLayout[position].addView( ProductCode[statrPosition] );
			}
			
			statrPosition++;
		}
		
//		BKPA.notifyDataSetChanged();
	}
	
	private Bitmap ReturnBitmap_Rotate90(Bitmap bitmap, int width, int height) {
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
	
	private ImageView[] imgNavi;
	private TextView txtNavi, txtNavi_Next;
	private RelativeLayout[] NaviLayout_SUB;
	private RelativeLayout txtNaviLayout;
	private int MaxNaviPage = 10;
		
	private void setNavi( int position ){
		MaxNaviPage = 10;
				
		int NaviPosition = 0;
//		MaxPage
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
			NaviLayout.setMargins( 0, 0, 0, 0);
			
			imgNavi[i].setLayoutParams( NaviLayout );
			
			imgNavi[i].setBackgroundResource( R.drawable.at_btn_slidecircle_n );
			
			
			RelativeLayout.LayoutParams Navi_SUB_Layout = new RelativeLayout.LayoutParams(
					20, 12);
			Navi_SUB_Layout.setMargins( 0, 0, 0, 0);
			
			NaviLayout_SUB[i].setLayoutParams( Navi_SUB_Layout );
			NaviLayout_SUB[i].setGravity( Gravity.CENTER );
			NaviLayout_SUB[i].addView( imgNavi[i] );
			navigationRow.addView( NaviLayout_SUB[i] );
		}

		RelativeLayout.LayoutParams Navi_Layout = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, 20);
		Navi_Layout.setMargins( 0, 1000, 0, 0);
		
		navigationRow.setLayoutParams( Navi_Layout );

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

	private String mRpairsItemCd = "", mRpairsSpeciesCd = "";;
	private String strmClassName = "전체";
	
	private libJSON LibJSON = new libJSON();
	private libJSON_GET GET_LibJSON = new libJSON_GET();
	private DownloadFile DLF = new DownloadFile();
	public String FOLDER_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test/";
	
	class setRepair_Before_After_Search extends AsyncTask<Integer, Integer, Integer> {
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
						
						aaa[0] = String.valueOf( num );
						aaa[1] = "12";
						aaa[2] = "1";
						long timestamp = System.currentTimeMillis();
						aaa[3] = mCipher.encrypt( Constance.SEED , Constance.MAC + "@" + timestamp ); 
						aaa[4] = String.valueOf( timestamp ).toString().trim(); 
						aaa[5] = Constance.BEANDCD;
						aaa[6] = mRpairsSpeciesCd;
						aaa[7] = mRpairsItemCd;
						
						
						GET_LibJSON.getREPAIR_IMAGE_RESULT_LIST( aaa );
						
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

						new setThumbNailDown().execute();
						
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
	
	class setThumbNailDown extends AsyncTask<Integer, Integer, Integer> {
		@Override
		protected void onPreExecute() {
			
			verMax		= CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_REPAIR_IMAGE_RESULT_LIST.size();
						
			verValue	= statrPosition;
		}
		
		@Override
		protected Integer doInBackground(Integer... params) {
			while (isCancelled() == false) {
				if (verValue < verMax) {
					try {

						String Down_URL = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_REPAIR_IMAGE_RESULT_LIST.get( verValue ).thumbUrl;
						String strimgNAme = String.valueOf( verValue ) + ".jpg";
						
						CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PAGE_ALL_LIST_THUMBPATH.add( DLF.Download_HttpFile(Down_URL, Constance.FILEPATH + "/수선정보/수선이미지/", strimgNAme) );
						
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
					
					setTest(num);
					
					if( num == 1 ) {
						mPager.setCurrentItem( num );
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
	
	
	
	class setRepairGoods extends AsyncTask<Integer, Integer, Integer> {
		@Override
		protected void onPreExecute() {
			
			if( !pd.isShowing() ) {
				pd.setMessage("");
				pd.setCancelable( false );
				pd.show();
			}
			
			verMax = 1;
			verValue = 0;
		}
		
		@Override
		protected Integer doInBackground(Integer... params) {
			while (isCancelled() == false) {
				if (verValue < verMax) {
					
					try {
						DeleteDir( Constance.FILEPATH + "/수선정보/수선이미지/" );
					} catch (Exception e) {
						// TODO: handle exception
					}
					try {
						String[] aaa = new String[3];

						long timestamp = System.currentTimeMillis();
						aaa[0] = mCipher.encrypt( Constance.SEED , Constance.MAC + "@" + timestamp ); 
						aaa[1] = String.valueOf( timestamp ).toString().trim(); 
						
						GET_LibJSON.getREPAIR_IMAGE_SPECIES_LIST( aaa );
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
						new setRepaire_Item_Information().execute();
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
	
	class setRepaire_Item_Information extends AsyncTask<Integer, Integer, Integer> {
		@Override
		protected void onPreExecute() {
			
			if( !pd.isShowing() ) {
				pd.setMessage("");
				pd.setCancelable( false );
				pd.show();
			}
			
			verMax = 1;
			verValue = 0;
		}
		
		@Override
		protected Integer doInBackground(Integer... params) {
			while (isCancelled() == false) {
				if (verValue < verMax) {
					try {
						String[] aaa = new String[4];

						long timestamp = System.currentTimeMillis();
						aaa[0] = mCipher.encrypt( Constance.SEED , Constance.MAC + "@" + timestamp ); 
						aaa[1] = String.valueOf( timestamp ).toString().trim();
						
						GET_LibJSON.getREPAIR_IMAGE_ITEN_LIST( aaa );
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
					if( pd.isShowing() ){
						
						setRepair_Goods();
						setRepair_Item();
						
						pd.dismiss();
						pd.cancel();
						
						setRepair_Goods_Choice( 0 );
						setRepair_Item_Choice( 0 );
						
						mRpairsItemCd = "";
						mRpairsSpeciesCd = "";
						new setRepair_Before_After_Search().execute();
					}

				}
			});
		}
		@Override
		protected void onCancelled() {
		}
	}
	
	private Handler handler = new Handler();

	private ImageView[] ContentsBack, ContentsImg_Img, ContentsImg_subThumb;
	private TextView[] ContentsName, ProductCode;
	private int MaxContentsCount = 0;
	private RelativeLayout[] ContentsAreaLayout;
	private int AllCategoryContents = 12;
	
	int AllCount = 0;
	
	private void setContentxArea(){
		
		MaxContentsCount 	= CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_REPAIR_IMAGE_RESULT_LIST.size();
		
		MaxPage = Integer.parseInt( Constance.PageCount );
		
		AllCount = Integer.parseInt( Constance.Page_ALL_Count );
				
		if( MaxPage == 0 ){
			MaxPage = 1;
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
	public void onClick(View v) {
		try {
			if( ContentsImg_Img != null ){
				for( int i = 0; i < MaxContentsCount; i++){ 
					if( v == ContentsImg_Img[i] ){
						Intent intent = new Intent( mContext, Repair_Before_After_Detail.class);
						intent.putExtra("position", i );
						mContext.startActivity(intent);
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			if( mRepair_Goods_txt != null ){
				for( int i = 0; i < mRepair_Goods_txt.length; i++){ 
					if( v == mRepair_Goods_txt[i] ){
						
						setRepair_Goods_Choice( i );
						
						PageState = false;
						
						statrPosition = 0;
						
						num = 1;
						
						CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_REPAIR_IMAGE_RESULT_LIST.clear();
						
						CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PAGE_ALL_LIST_THUMBPATH.clear();
						
						if( i == 0 ){
							mRpairsSpeciesCd = "";
						} else {
							mRpairsSpeciesCd = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_REPAIR_SPECIES_LIST.get( i - 1 ).rpairsSpeciesCd;
						}
						
						new setRepair_Before_After_Search().execute();
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			if( mRepair_Item_txt != null ){
				for( int i = 0; i < mRepair_Item_txt.length; i++){ 
					if( v == mRepair_Item_txt[i] ){
						setRepair_Item_Choice( i );
						
						PageState = false;
						
						statrPosition = 0;
						
						num = 1;
						
						CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_REPAIR_IMAGE_RESULT_LIST.clear();
						
						CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PAGE_ALL_LIST_THUMBPATH.clear();
						
						if( i == 0 ){
							mRpairsItemCd = "";
						} else {
							mRpairsItemCd = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_REPAIR_IMAGE_ITEM_LIST.get( i - 1 ).rpairsItemCd;
						}
						new setRepair_Before_After_Search().execute();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean SearchType = true;
	
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
