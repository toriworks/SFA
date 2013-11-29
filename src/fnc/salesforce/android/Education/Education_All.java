package fnc.salesforce.android.Education;


import java.io.File;

import com.artifex.mupdfdemo.MuPDFActivity;

import cx.hell.android.pdfview.OpenFileActivity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.net.Uri;
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
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.ImageView.ScaleType;
import fnc.salesforce.android.R;
import fnc.salesforce.android.Constance.CONTENTS_PAGE_ALL_LIST_CONSTANCE;
import fnc.salesforce.android.Constance.Constance;
import fnc.salesforce.android.LIB.BackPress;
import fnc.salesforce.android.LIB.CipherUtils;
import fnc.salesforce.android.LIB.DownloadFile;
import fnc.salesforce.android.LIB.KumaLog;
import fnc.salesforce.android.LIB.libJSON;
import fnc.salesforce.android.LIB.libJSON_GET;

public class Education_All implements OnTouchListener, OnClickListener{

	LayoutInflater inflater;
	
	private Context mContext;
		
	ProgressDialog pd;
	
	public Education_All(Context context){
		mContext = context;
		
		pd = new ProgressDialog( context );
		
		inflater = ((Activity) mContext).getLayoutInflater();
	}
	
	private LinearLayout navigationRow;
	
	private String strBrandCD = "", strCategoryID = "", strContentsType = "", strCategoryName = "";
	
	public View setEducationView(String brand, String category, String strType, String mategoryName){
		
		CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.clear();
		
		CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PAGE_ALL_LIST_THUMBPATH.clear();
		
		CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.clear();
		
		CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST_PAGE.clear();
		
		strBrandCD = brand;
		strCategoryID = category;
		strContentsType = strType;
		strCategoryName = mategoryName;

		View view = null;
		
		ContentsAreaLayout = null;
		
		BKPA = null;
		
		num = 1;
		
		view = inflater.inflate(R.layout.ad_gallery_main, null);
		
		flipper			= ( ViewFlipper ) view.findViewById( R.id.flipper );
		navigationRow	= ( LinearLayout ) view.findViewById( R.id.navigationRow );
		txtNavi	 		= ( TextView ) view.findViewById( R.id.txtNavi );
		
		flipper.setOnTouchListener( this );

		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				new setDeviceInfor().execute();
			}
		}, 200);
		
		mPager = (ViewPager) view.findViewById(R.id.pager);
		
		mPager.setOnPageChangeListener( new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				if( position == 0 ){
					
					CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.clear();
					CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST_PAGE.clear();
					CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.clear();
					
					CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PAGE_ALL_LIST_THUMBPATH.clear();
					
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
		
		return view;
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
	
	private static class ContentsImgBack_Param {
		static int width = 195;
		static int height = 176;
		static int Count = 4;
		static int Margine_top = 214;
		static int Margine_Left = 195;
	};
	
	private static class ContentsImg_Param {
		static int width = 191;
		static int height = 135;
		static int Count = 4;
		static int Margine_top = 214;
		static int Margine_Left = 195;
	};
	
	private static class ContentsImg_Thumb_Param {
		static int width = 191;
		static int height =  19;
		static int Count = 4;
		static int Margine_top = 116;
		static int Margine_Left = 195;
	};
	
	private static class Contents_Name_Param {
		static int width = 182;
		static int height =  40;
		static int Count = 4;
		static int Margine_top = 136;
		static int Margine_Left = 195;
	};
	
	private static class Contents_Category_Param {
		static int width = 171;
		static int height =  19;
		static int Count = 4;
		static int Margine_top = 115;
		static int Margine_Left = 195;
	};
	
	private void setTest( int position){
		
		
		try {
			statrPosition = AllCategoryContents * ( position - 1 );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if( ContentsBack == null ){
			ContentsBack		= new ImageView[ AllCount ];
			ContentsImg_Img		= new ImageView[ AllCount ];
			ContentsImg_Select	= new ImageView[ AllCount ];
			ContentsImg_subThumb= new ImageView[ AllCount ];
			ContentsImg_DownState	= new ImageView[ AllCount ];
			
			ContentsName		= new TextView[ AllCount ];
			CategoryName		= new TextView[ AllCount ];
		} else {
			if( position == 1 ){
				ContentsBack = null;
				ContentsImg_Img = null;
				ContentsImg_Select = null;
				ContentsImg_subThumb = null;
				ContentsName = null;
				CategoryName = null;
				ContentsImg_DownState	= null;
				
				ContentsBack		= new ImageView[ AllCount ];
				ContentsImg_Img		= new ImageView[ AllCount ];
				ContentsImg_Select	= new ImageView[ AllCount ];
				ContentsImg_subThumb= new ImageView[ AllCount ];
				ContentsImg_DownState	= new ImageView[ AllCount ];
				
				ContentsName		= new TextView[ AllCount ];
				CategoryName		= new TextView[ AllCount ];
				
				for( int i = 0; i < ContentsAreaLayout.length; i++ ){
					if( ContentsAreaLayout[i] != null)
						ContentsAreaLayout[i].removeAllViews();
				}
			}
		}

		int ContentsCount = 0;
		
		try {
			if( !strContentsType.equals("ALL") ){
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
			
			ContentsImg_subThumb[statrPosition] = new ImageView( mContext );
			ContentsBack[statrPosition] 		= new ImageView( mContext );
			ContentsImg_Img[statrPosition] 		= new ImageView( mContext );
			ContentsImg_Select[statrPosition] 	= new ImageView( mContext );
			ContentsImg_DownState[statrPosition] = new ImageView( mContext );
			
			ContentsName[statrPosition] = new TextView( mContext );
			CategoryName[statrPosition] = new TextView( mContext );
//			
//			RelativeLayout.LayoutParams imgContentslayout = new RelativeLayout.LayoutParams(
//					195, 176);
//			
//			if( ( i % AllCategoryContents ) / 4 == 0 ){
//				if( i > ( AllCategoryContents - 1 ) ){
//					imgContentslayout.setMargins( ( 195 * ( i % 4 ) ), 0, 0, 0);	
//				} else {
//					imgContentslayout.setMargins( ( 195 * i ), 0, 0, 0);
//				}
//			} else if ( ( i % AllCategoryContents ) / 4 == 1 ){
//				imgContentslayout.setMargins( ( 195 * ( i % 4 ) ), 214, 0, 0);
//			} else if ( ( i % AllCategoryContents ) / 4 == 2 ){
//				imgContentslayout.setMargins( ( 195 * ( i % 4 ) ), 428, 0, 0);
//			} else {
//				imgContentslayout.setMargins( ( 195 * ( i % 4 ) ), 642, 0, 0);
//			}
//			
			RelativeLayout.LayoutParams imgContentslayout = new RelativeLayout.LayoutParams(
					ContentsImgBack_Param.width, ContentsImgBack_Param.height);

			int height_MargineCount =  i / ContentsImgBack_Param.Count;
			
			int Width_MargineCount =  i % ContentsImgBack_Param.Count;
			
			int Margine_Left  = 0;
			
			Margine_Left = ( ContentsImgBack_Param.Margine_Left * Width_MargineCount ) + 2;
			
			
			imgContentslayout.setMargins( Margine_Left , ContentsImgBack_Param.Margine_top * height_MargineCount , 0, 0);
			
			ContentsBack[statrPosition].setLayoutParams( imgContentslayout );
			ContentsBack[statrPosition].setBackgroundResource( R.drawable.at_sub_thumbbg );
			
//			RelativeLayout.LayoutParams imgContentslayout_Type = new RelativeLayout.LayoutParams(
//					191, 135);
//
//			if( ( i % AllCategoryContents ) / 4 == 0 ){
//				if( i > ( AllCategoryContents - 1 ) ){
//					imgContentslayout_Type.setMargins( ( 195 * ( i % 4 ) ) + 2, 0, 0, 0);;	
//				} else {
//					imgContentslayout_Type.setMargins( ( 195 * i ) + 2, 0, 0, 0);
//				}
//				
//			} else if ( ( i % AllCategoryContents ) / 4 == 1 ){
//				imgContentslayout_Type.setMargins( ( 195 * ( i % 4 ) ) + 2, 214, 0, 0);
//			} else if ( ( i % AllCategoryContents ) / 4 == 2 ){
//				imgContentslayout_Type.setMargins( ( 195 * ( i % 4 ) ) + 2 , 428, 0, 0);
//			} else {
//				imgContentslayout_Type.setMargins( ( 195 * ( i % 4 ) ) + 2, 642, 0, 0);
//			}
			
			RelativeLayout.LayoutParams imgContentslayout_Img = new RelativeLayout.LayoutParams(
					ContentsImg_Param.width, ContentsImg_Param.height);

			height_MargineCount =  i / ContentsImg_Param.Count;
			
			Width_MargineCount =  i % ContentsImg_Param.Count;
			
			Margine_Left  = 0;
			
			Margine_Left = ( ContentsImg_Param.Margine_Left * Width_MargineCount ) + 2;
			
			
			imgContentslayout_Img.setMargins( Margine_Left , ( ContentsImg_Param.Margine_top * height_MargineCount ), 0, 0);
			
			ContentsImg_Img[statrPosition].setLayoutParams( imgContentslayout_Img );
//			ContentsImg_Img[i].setBackgroundResource( R.drawable.at_main_icon01 );

			
			try {
				ContentsImg_Img[statrPosition].setImageBitmap( BitmapFactory.decodeFile( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PAGE_ALL_LIST_THUMBPATH.get( statrPosition ).toString())  );
			} catch (Exception e) {
				e.printStackTrace();
			}
			ContentsImg_Img[statrPosition].setScaleType( ScaleType.FIT_XY );
			ContentsImg_Img[statrPosition].setOnClickListener( this );	
			
			ContentsImg_Select[statrPosition].setLayoutParams( imgContentslayout_Img );
//			ContentsImg_Select[i].setBackgroundResource( R.drawable.at_sub_thumbselect );

			
//			RelativeLayout.LayoutParams imgContentslayout_SubThumb = new RelativeLayout.LayoutParams(
//					191, 19);
//
//			if( ( i % AllCategoryContents ) / 4 == 0 ){
//				if( i > ( AllCategoryContents - 1 ) ){
//					imgContentslayout_SubThumb.setMargins( ( 195 * ( i % 4 ) ) + 2, 116, 0, 0);;	
//				} else {
//					imgContentslayout_SubThumb.setMargins( ( 195 * i ) + 2, 116, 0, 0);
//				}
//				
//			} else if ( ( i % AllCategoryContents ) / 4 == 1 ){
//				imgContentslayout_SubThumb.setMargins( ( 195 * ( i % 4 ) ) + 2, 214 + 116, 0, 0);
//			} else if ( ( i % AllCategoryContents ) / 4 == 2 ){
//				imgContentslayout_SubThumb.setMargins( ( 195 * ( i % 4 ) ) + 2 , 428 + 116, 0, 0);
//			} else {
//				imgContentslayout_SubThumb.setMargins( ( 195 * ( i % 4 ) ) + 2, 642 + 116, 0, 0);
//			}
//			
			RelativeLayout.LayoutParams imgContentslayout_SubThumb = new RelativeLayout.LayoutParams(
					ContentsImg_Thumb_Param.width, ContentsImg_Thumb_Param.height);

			height_MargineCount =  i / ContentsImg_Thumb_Param.Count;
			
			Width_MargineCount =  i % ContentsImg_Thumb_Param.Count;
			
			Margine_Left  = 0;
			
			Margine_Left = ( ContentsImg_Thumb_Param.Margine_Left * Width_MargineCount ) + 2;
			
			
			imgContentslayout_SubThumb.setMargins( Margine_Left , ( ContentsImgBack_Param.Margine_top * height_MargineCount ) + ContentsImg_Thumb_Param.Margine_top, 0, 0);
			

			ContentsImg_subThumb[statrPosition].setLayoutParams( imgContentslayout_SubThumb );
			
			ContentsImg_subThumb[statrPosition].setScaleType( ScaleType.FIT_XY );
			ContentsImg_subThumb[statrPosition].setBackgroundResource( R.drawable.at_sub_thumbtagbg );
			
//			RelativeLayout.LayoutParams imgContentslayout_Name = new RelativeLayout.LayoutParams(
//					189, 40);
//
//			if( ( i % AllCategoryContents ) / 4 == 0 ){
//				if( i > ( AllCategoryContents - 1 ) ){
//					imgContentslayout_Name.setMargins( ( 195 * ( i % 4 ) ) + 4, 136, 0, 0);
//				} else {
//					imgContentslayout_Name.setMargins( ( 195 * i ) + 4, 136, 0, 0);
//				}
//				
//			} else if ( ( i % AllCategoryContents ) / 4 == 1 ){
//				imgContentslayout_Name.setMargins( ( 195 * ( i % 4 ) ) + 4, 350, 0, 0);
//			} else if ( ( i % AllCategoryContents ) / 4 == 2 ){
//				imgContentslayout_Name.setMargins( ( 195 * ( i % 4 ) ) + 4, 564, 0, 0);
//			} else {
//				imgContentslayout_Name.setMargins( ( 195 * ( i % 4 ) ) + 4, 778, 0, 0);
//			}
			
			RelativeLayout.LayoutParams imgContentslayout_Name = new RelativeLayout.LayoutParams(
					Contents_Name_Param.width, Contents_Name_Param.height);

			height_MargineCount =  i / Contents_Name_Param.Count;
			
			Width_MargineCount =  i % Contents_Name_Param.Count;
			
			Margine_Left  = 0;
			
			Margine_Left = ( Contents_Name_Param.Margine_Left * Width_MargineCount ) + 10;
			
			
			imgContentslayout_Name.setMargins( Margine_Left , ( ContentsImgBack_Param.Margine_top * height_MargineCount ) + Contents_Name_Param.Margine_top, 0, 0);
			
			
			try {
				if( strContentsType.equals("ALL") ){
					ContentsName[statrPosition].setText( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( statrPosition ).cntntsNm );
				} else {
					ContentsName[statrPosition].setText( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.get( statrPosition ).cntntsNm );
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			ContentsName[statrPosition].setLayoutParams(imgContentslayout_Name);
			ContentsName[statrPosition].setGravity( Gravity.CENTER_VERTICAL );
			ContentsName[statrPosition].setTextSize( 13 );
//			ContentsName[i].setSingleLine();

//			RelativeLayout.LayoutParams imgContentslayout_Category = new RelativeLayout.LayoutParams(
//					171, 19);
//
//			if( ( i % AllCategoryContents ) / 4 == 0 ){
//				if( i > ( AllCategoryContents - 1 ) ){
//					imgContentslayout_Category.setMargins( ( 195 * ( i % 4 ) ) + 22, 117, 0, 0);;	
//				} else {
//					imgContentslayout_Category.setMargins( ( 195 * i ) + 22, 117, 0, 0);
//				}
//				
//			} else if ( ( i % AllCategoryContents ) / 4 == 1 ){
//				imgContentslayout_Category.setMargins( ( 195 * ( i % 4 ) ) + 22, 214 + 117, 0, 0);
//			} else if ( ( i % AllCategoryContents ) / 4 == 2 ){
//				imgContentslayout_Category.setMargins( ( 195 * ( i % 4 ) ) + 22 , 428 + 117, 0, 0);
//			} else {
//				imgContentslayout_Category.setMargins( ( 195 * ( i % 4 ) ) + 22, 642 + 117, 0, 0);
//			}
			
			RelativeLayout.LayoutParams imgContentslayout_Category = new RelativeLayout.LayoutParams(
					Contents_Category_Param.width, Contents_Category_Param.height);

			height_MargineCount =  i / Contents_Category_Param.Count;
			
			Width_MargineCount =  i % Contents_Category_Param.Count;
			
			Margine_Left  = 0;
			
			Margine_Left = ( Contents_Category_Param.Margine_Left * Width_MargineCount ) + 25;
			
			
			imgContentslayout_Category.setMargins( Margine_Left , ( ContentsImgBack_Param.Margine_top * height_MargineCount ) + Contents_Category_Param.Margine_top, 0, 0);
			
			
			
			try {
				if( strContentsType.equals("ALL") ){
					CategoryName[statrPosition].setText( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( statrPosition ).ctgryNm );
				} else {
					CategoryName[statrPosition].setText( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.get( statrPosition ).ctgryNm );
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			String ContentsType = "";
			
			try {
				if( strContentsType.equals("ALL") ){
					ContentsType = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( statrPosition ).cntntsTyCd ;
				} else {
					ContentsType = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.get( statrPosition ).cntntsTyCd ;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if( ContentsType.equals("009002")){
				RelativeLayout.LayoutParams imgContentslayout_Down = new RelativeLayout.LayoutParams(
						imgContentslayout_Down_Param.width, imgContentslayout_Down_Param.height);

				height_MargineCount =  i / imgContentslayout_Down_Param.Count;
				
				Width_MargineCount =  i % imgContentslayout_Down_Param.Count;
				
				Margine_Left  = 0;
				 
				
				if( Width_MargineCount == 0 ){
					Margine_Left = 0;
				} else {
					Margine_Left = ( imgContentslayout_Down_Param.Margine_Left * Width_MargineCount );
				}
				int Margine_Top = 0;
				if( height_MargineCount == 0 ){
					Margine_Top = imgContentslayout_Down_Param.Margine_top;
				} else {
					Margine_Top = ( ( 214 * height_MargineCount ) + imgContentslayout_Down_Param.Margine_top);
				}
				
				imgContentslayout_Down.setMargins( Margine_Left + 170 , Margine_Top , 0, 0);
				
				ContentsImg_DownState[statrPosition].setLayoutParams(imgContentslayout_Down);
				ContentsImg_DownState[statrPosition].setScaleType( ScaleType.FIT_XY );
				
				String mFileName = "";
				
				try {
					if( strContentsType.equals("ALL") ){
						mFileName = "/" + CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( statrPosition ).brandCd;
						mFileName = mFileName + "/" + CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( statrPosition ).cntntsId;
						mFileName = mFileName + "/" +CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( statrPosition ).cntntsFileLc;
					} else {
						mFileName = "/" + CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.get( statrPosition ).brandCd;
						mFileName = mFileName + "/" + CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.get( statrPosition ).cntntsId;
						mFileName = mFileName + "/" +CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.get( statrPosition ).cntntsFileLc;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				String filePath = Constance.FILEPATH + "/" + mFileName;
				
				File imageFile = new File(filePath);
				
				if ( !imageFile.exists() ) {
					ContentsImg_DownState[statrPosition].setBackgroundResource( R.drawable.at_pdf_down_before );
				} else {
					ContentsImg_DownState[statrPosition].setBackgroundResource( R.drawable.at_pdf_down_after );
				}
			}
//			cntntsTyCd": "009002",
			
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
				if( ContentsType.equals("009002")){
					ContentsAreaLayout[position].addView( ContentsImg_DownState[statrPosition] );
				}
			}
			
			statrPosition++;
		}
		
//		BKPA.notifyDataSetChanged();
	}
	
	private static class imgContentslayout_Down_Param {
		static int width = 20;
		static int height = 19;
		static int Count = 4;
		static int Margine_top = 115;
		static int Margine_Left = 195;
	};
	
	private ImageView[] imgNavi, ContentsImg_DownState;
	private RelativeLayout[] NaviLayout_SUB;
	private TextView txtNavi, txtNavi_Next;
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
						aaa[3] = "16";
						aaa[4] = "1";
						long timestamp = System.currentTimeMillis();
						aaa[5] = mCipher.encrypt( Constance.SEED , Constance.MAC + "@" + timestamp ); 
						aaa[6] = String.valueOf( timestamp ).toString().trim(); 
						if( strContentsType.equals("ALL") ){
							GET_LibJSON.getCONTENTS_PAGE_ALL_LIST( aaa );
						} else {
							GET_LibJSON.getCONTENTS_PAGE_LIST( aaa );
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
						if( strContentsType.equals("ALL") ){
							new setThumbNailDown().execute();
						} else {
							new setThumbNailDown_SUB().execute();
						}
					} catch (Exception e) {
						pd.dismiss();
						pd.cancel();
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
			
			verMax = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.size();
			
			CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PAGE_ALL_LIST_THUMBPATH.clear();
			
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
//						long timestamp = System.currentTimeMillis();
//						aaa[2] = mCipher.encrypt( Constance.SEED , Constance.MAC + "@" + timestamp ); 
//						aaa[3] = String.valueOf( timestamp ).toString().trim(); 
//						String Down_URL = GET_LibJSON.getTHUMBNAIL_FILE( aaa );
//						KumaLog.LogE("test"," verValue : "  + verValue );
						
						String Down_URL = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( verValue ).dwldThumbPath;
						String strimgNAme = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( verValue ).thumbFileLc;
						
//						KumaLog.LogE("test"," Down_URL : "  + Down_URL );
//						if( verValue == 0 ){
//							Intent intent = new Intent(Intent.ACTION_VIEW);
//							intent.setData(Uri.parse(Down_URL));
//							mContext.startActivity( intent );
//						}
						
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
			
			CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PAGE_ALL_LIST_THUMBPATH.clear();
			
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
//						long timestamp = System.currentTimeMillis();
//						aaa[2] = mCipher.encrypt( Constance.SEED , Constance.MAC + "@" + timestamp ); 
//						aaa[3] = String.valueOf( timestamp ).toString().trim(); 
//						String Down_URL = GET_LibJSON.getTHUMBNAIL_FILE( aaa );
//						KumaLog.LogE("test"," verValue : "  + verValue );
						
						String Down_URL = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.get( verValue ).dwldThumbPath;
						String strimgNAme = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.get( verValue ).thumbFileLc;
						
//						if( verValue == 0 ){
//							Intent intent = new Intent(Intent.ACTION_VIEW);
//							intent.setData(Uri.parse(Down_URL));
//							mContext.startActivity( intent );
//						}
						
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
	private ImageView[] ContentsBack, ContentsImg_Img, ContentsImg_Select, ContentsImg_subThumb;
	private TextView[] ContentsName, CategoryName;
	private int MaxContentsCount = 12;
	private RelativeLayout[] ContentsAreaLayout;
	private int AllCategoryContents = 16;
	
	int AllCount = 0;
	
	private void setContentxArea(){
		
		if( strContentsType.equals("ALL") ){
			MaxContentsCount 	= CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.size();
		} else {
			MaxContentsCount 	= CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.size();
		}
		
		
		if( strContentsType.equals("ALL") ){
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
			
			if ((start.x - event.getX()) > 50) {
				// 왼쪽 방향 에니메이션 지정
				flipper.setInAnimation(AnimationUtils.loadAnimation(mContext,
						R.anim.push_left_in));
				flipper.setOutAnimation(AnimationUtils.loadAnimation(mContext,
						R.anim.push_left_out));
				
				if (num != MaxPage ) {
					num++;
//					FllingView_Next();
					ResetContents();
				}
			} else if( (start.x - event.getX()) < -50 ){
				flipper.setInAnimation(AnimationUtils.loadAnimation(mContext,
						R.anim.push_right_in));
				flipper.setOutAnimation(AnimationUtils.loadAnimation(mContext,
						R.anim.push_right_out));					
				if (num != 1) {
					num--;
//					FllingView_Previous();
					ResetContents();
				}
			} else {
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
				for( int i = 0; i < MaxContentsCount; i++){ 
					if( v == ContentsImg_Img[i] ){
						
						String ContentsTypeCode = "";
						
						pdf_Position = i;
						
						if( strContentsType.equals("ALL") ){
							ContentsTypeCode =  CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( i ).cntntsTyCd;
							
							Constance.arrTrans_UP_Information[0] = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( i ).cntntsTyCd;
							Constance.arrTrans_UP_Information[1] = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( i ).linkUrl;
							Constance.arrTrans_UP_Information[2] = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( i ).cntntsCn;
							
						} else {
							ContentsTypeCode =  CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.get( i ).cntntsTyCd;
							
							Constance.arrTrans_UP_Information[0] = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.get( i ).cntntsTyCd;
							Constance.arrTrans_UP_Information[1] = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.get( i ).linkUrl;
							Constance.arrTrans_UP_Information[2] = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.get( i ).cntntsCn;
						}
						
						KumaLog.LogD("test_1"," ContentsTypeCode : " + ContentsTypeCode );
						if( ContentsTypeCode.equals("009001")){							
							BackPress.getDetailPage( 0 );
						} else if( ContentsTypeCode.equals("009002")){
							new setPDF_DOWNLOADB().execute();
						} else if( ContentsTypeCode.equals("009003")){
							
						}
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private int pdf_Position = 0;
	private String pdf_Path = "", mdwldYn = "";
	
	class setPDF_DOWNLOADB extends AsyncTask<Integer, Integer, Integer> {
		@Override
		protected void onPreExecute() {
			
			verMax = 1;
			verValue = 0;
			
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
						String[] aaa = new String[4];
						String Down_URL = "", strimgNAme = ""; 
						
						String[] parameter = new String[5];
						
						parameter[0] = strBrandCD;
						parameter[1] = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( pdf_Position ).cntntsId;
						parameter[2] = Constance.SHOPCD;
						
						long timestamp = System.currentTimeMillis();
						
						parameter[3] = mCipher.encrypt( Constance.SEED , Constance.MAC + "@" + timestamp ); 
						parameter[4] = String.valueOf( timestamp ).toString().trim(); 
						
						GET_LibJSON.getPRODUCT_LIST( parameter );
						
						
						if( strContentsType.equals("ALL") ){
							aaa[0] = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( pdf_Position ).brandCd;
							
							aaa[1] = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( pdf_Position ).cntntsId;
							
							Down_URL= CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( pdf_Position ).dwldCntntsPath;
							strimgNAme = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( pdf_Position ).cntntsFileLc;
							mdwldYn	= CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( pdf_Position ).dwldYn;
						} else {
							aaa[0] = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.get( pdf_Position ).brandCd;
							
							aaa[1] = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.get( pdf_Position ).cntntsId;
							
							Down_URL = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.get( pdf_Position ).dwldCntntsPath;
							strimgNAme = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.get( pdf_Position ).cntntsFileLc;
							mdwldYn	= CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.get( pdf_Position ).dwldYn;
						}

						pdf_Path =  DLF.Download_HttpFile(Down_URL, Constance.FILEPATH + "/" + aaa[0] + "/" + aaa[1] +"/", strimgNAme);

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
					pd.dismiss();
					pd.cancel();
					
//					File f = new File( pdf_Path );
//					
//					Intent intent = new Intent();
//					intent.setDataAndType(Uri.fromFile(f), "application/pdf");
//					// intent.setClass(this, GridViewActivity.class);
//					intent.putExtra("ctgryId", strCategoryID );
//					intent.putExtra("brandCd", Constance.BEANDCD );
//					intent.putExtra("ctgryNm", strCategoryName);
//					intent.putExtra("Activity", "Education");
//					intent.setClass(mContext, OpenFileActivity.class);
//					intent.setAction("android.intent.action.VIEW");
//					mContext.startActivity(intent);
//					mContext.fileList();
					
					
					File f = new File( pdf_Path );
					
//					Intent intent = new Intent();
//					intent.setDataAndType(Uri.fromFile(f), "application/pdf");
//					// intent.setClass(this, GridViewActivity.class);
//					intent.putExtra("ctgryId", strCategoryID );
//					intent.putExtra("brandCd", Constance.BEANDCD );
//					intent.putExtra("ctgryNm", strCategoryName);
//					intent.setClass(mContext, OpenFileActivity.class);
//					intent.setAction("android.intent.action.VIEW");					
//					mContext.startActivity(intent);
//					mContext.fileList();
					
					Uri uri = Uri.parse(f.getAbsolutePath());
					Intent intent = new Intent(mContext,MuPDFActivity.class);
					intent.setAction(Intent.ACTION_VIEW);
					intent.setData(uri);
					intent.putExtra("ctgryId", strCategoryID );
					intent.putExtra("brandCd", Constance.BEANDCD );
					intent.putExtra("ctgryNm", strCategoryName);
					intent.putExtra("Activity", "Education");
					intent.putExtra("dwldYn", mdwldYn);

					mContext.startActivity(intent);
					mContext.fileList();
				}
			});
		}
		@Override
		protected void onCancelled() {
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
