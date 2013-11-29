package fnc.salesforce.android.AD_10003;

import fnc.salesforce.android.R;
import fnc.salesforce.android.Constance.CONTENTS_PAGE_ALL_LIST_CONSTANCE;
import fnc.salesforce.android.Constance.Constance;
import fnc.salesforce.android.LIB.CipherUtils;
import fnc.salesforce.android.LIB.DownloadFile;
import fnc.salesforce.android.LIB.KumaLog;
import fnc.salesforce.android.LIB.libJSON;
import fnc.salesforce.android.LIB.libJSON_GET;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.ImageView.ScaleType;

public class MediaCutView implements OnTouchListener, OnClickListener{
	
	LayoutInflater inflater;
	
	private Context mContext;
	
	private String strCategory = "";
	
	public MediaCutView(Context context){
		mContext = context;
		
		pd = new ProgressDialog( context );
		
		inflater = ((Activity) mContext).getLayoutInflater();
	}
	
	private ImageView imgPruDuct;
	
	
	ProgressDialog pd;
	
	private String AllCateGory = "", strBrandCD = "", strContents_ID = "", strTabCategory_ID = "";
	
	private TextView txtContentsName, txtContentsDetail;
	
	private ImageView btnContentsCn, btnContentsProductList;

	private ADGalleryProductListAdabter aa;
	
	private ListView beautyList;
	
	private LinearLayout navigationRow;
	
	private boolean ContentsInsertState = false;
	
	// Fasle : ALL
	private boolean CategoryState = false;
	
	private TextView txtProductCount;
	
	private int ctpositon = 0, ContentsClickPosition = 0;
	
	View view_Media = null;
	
	public View setMediaCutView(String BrandCD, String TabCategory_ID, String CategoryID, String strContentsID){

		CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.clear();
		
		CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PAGE_ALL_LIST_THUMBPATH.clear();
		
		CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.clear();
		
		CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST_PAGE.clear();
		
		if( CategoryID.length() > 3 ){
			AllCateGory	  = TabCategory_ID;
			strCategory   = TabCategory_ID;
		} else { 
			strCategory = "";
		}
		
		CategoryState= false;
		
		ContentsAreaLayout = null;
		
		strContents_ID = strContentsID;
		
		strTabCategory_ID = TabCategory_ID;
		
		strBrandCD = BrandCD;
		 
		ContentsInsertState = false;
		
		ctpositon = 0;
		
		ContentsClickPosition = 0;
		
		num = 1;
		
		
		
		view_Media = inflater.inflate(R.layout.media_cut_view, null);
		
		TabLayout				= ( LinearLayout ) view_Media.findViewById(R.id.layout_Tabbar);
		navigationRow			= ( LinearLayout ) view_Media.findViewById(R.id.navigationRow);
		
		imgPruDuct				= (ImageView) view_Media.findViewById(R.id.imgPruDuct);
		btnContentsCn			= (ImageView) view_Media.findViewById(R.id.btnContentsCn);
		btnContentsProductList	= (ImageView) view_Media.findViewById(R.id.btnContentsProductList);
		
		beautyList 				= (ListView) view_Media.findViewById(R.id.beautyList);
		
		txtContentsName 		= ( TextView ) view_Media.findViewById(R.id.txtContentsName);
		txtContentsDetail		= ( TextView ) view_Media.findViewById(R.id.txtContentsDetail);
		txtProductCount			= ( TextView ) view_Media.findViewById(R.id.txtProductCount);
		
		
		flipper		= ( ViewFlipper ) view_Media.findViewById( R.id.flipper );
		
		flipper.setOnTouchListener( this );
		btnContentsProductList.setOnClickListener( this );
		btnContentsCn.setOnClickListener( this );
		
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				new setCategoryInfor().execute();
			}
		}, 200);
	
		mPager = (ViewPager) view_Media.findViewById(R.id.pager);
		
		BKPA	= null;
		
		mPager.setOnPageChangeListener( new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				KumaLog.LogE("test_1"," onPageSelected ");
				if( position == 0 ){
					
					CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.clear();
					CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST_PAGE.clear();
					CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.clear();
					
					CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PAGE_ALL_LIST_THUMBPATH.clear();
					
					PageState = false;
					
					num = 1;
					
					new setContentsInfor().execute();
				} else {
					if( PageState ){
						if( position > num){
							
							num = position;
							
							new setContentsInfor().execute();
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
		
		return view_Media;
	}

	public View getView(){
		return view_Media;
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
				
				ContentsBack		= new ImageView[ AllCount ];
				ContentsImg_Img		= new ImageView[ AllCount ];
				ContentsImg_Select	= new ImageView[ AllCount ];
				ContentsImg_subThumb= new ImageView[ AllCount ];
				
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
			if( CategoryState ){
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
				if( CategoryState ){
					ContentsName[statrPosition].setText( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.get( statrPosition ).cntntsNm );
				} else {
					ContentsName[statrPosition].setText( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( statrPosition ).cntntsNm );
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			ContentsName[statrPosition].setLayoutParams(imgContentslayout_Name);
			ContentsName[statrPosition].setGravity( Gravity.CENTER_VERTICAL );
			ContentsName[statrPosition].setTextSize( 15 );
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
				if( CategoryState ){
					CategoryName[statrPosition].setText( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.get( statrPosition ).ctgryNm );
				} else {
					CategoryName[statrPosition].setText( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( statrPosition ).ctgryNm );
				}
				
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
		
//		BKPA.notifyDataSetChanged();
	}

	private void setPruDuct(){
		
		try {
//			if( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_INFORMATION.get( 0 ).cntntsNm.length() > 30 ){
//				RelativeLayout.LayoutParams ContentsNameParam = new RelativeLayout.LayoutParams(
//						640, 24);
//				txtContentsName.setLayoutParams(ContentsNameParam);
//			} else {
//				RelativeLayout.LayoutParams ContentsNameParam = new RelativeLayout.LayoutParams(
//						340, 24);
//				txtContentsName.setLayoutParams(ContentsNameParam);
//			}
			
			txtContentsName.setText( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_INFORMATION.get( 0 ).cntntsNm );
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			txtContentsDetail.setText( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_INFORMATION.get( 0 ).cntntsCn );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if( aa == null ){
			aa = new ADGalleryProductListAdabter( mContext );
			beautyList.setAdapter( aa );
		} else {
			aa.notifyDataSetChanged();
		}
		
		
		txtContentsDetail.setVisibility( View.GONE );
		beautyList.setVisibility( View.GONE );
		
		try {
			txtProductCount.setText( "(" + CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_LIST.size() + ")");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			imgPruDuct.setImageBitmap( BitmapFactory.decodeFile(  CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_INFORMATION_THUMB.get( 0 ).toString() ) );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private ImageView[] imgNavi;
	private RelativeLayout[] NaviLayout_SUB;
	
	private void setNavi( int position){
		navigationRow.removeAllViews();
		
		if( imgNavi != null )
			imgNavi = null;
		
		if( NaviLayout_SUB != null )
			NaviLayout_SUB = null;
		
		imgNavi 		= new ImageView[ MaxPage ];
		NaviLayout_SUB 	= new RelativeLayout[ MaxPage ];
		
		for( int i = 0; i < MaxPage; i++ ){
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
		
		imgNavi[position - 1].setBackgroundResource( R.drawable.at_btn_slidecircle_o );
	}
	
	private int MaxTabbCount = 0;
	
	private LinearLayout TabLayout;
	
	RelativeLayout[] tabImgBtn;
	
	View[] viewTerm;
	
	private void setScrollViewTAbBar(){
		
		MaxTabbCount = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_CATEGORY_SUB.size() + 1;
		
		if( tabImgBtn != null )
			tabImgBtn = null;
		
		if( viewTerm != null )
			viewTerm = null;
		
		tabImgBtn	= new RelativeLayout[ MaxTabbCount ];
		
		viewTerm	= new View[ MaxTabbCount ];
		
		TextView[] tabtxtView	= new TextView[ MaxTabbCount ];

		for( int i = 0; i < MaxTabbCount; i++ ){
			tabImgBtn[i] 	= new RelativeLayout( mContext );

			viewTerm[i] 	= new View( mContext );
			
			tabtxtView[i] 	= new TextView( mContext );
			
			String ctgryNm = "";
			
			int mCategoryWidth = 0;
			
			if( i == 0 ){
				tabtxtView[i].setText( "전체" );
				mCategoryWidth = 95;
			} else {
				ctgryNm = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_CATEGORY_SUB.get( i - 1).ctgryNm;
				
				tabtxtView[i].setText( ctgryNm );
				
				mCategoryWidth = ( 11 * ctgryNm.toString().trim().length() ) + 10;
			}
			
			
			RelativeLayout.LayoutParams Contentslayout = new RelativeLayout.LayoutParams(
					mCategoryWidth + 10, 42);
			
			tabImgBtn[i].setLayoutParams(Contentslayout);			
			tabImgBtn[i].setOnClickListener( this );
			
			RelativeLayout.LayoutParams imglinelayout = new RelativeLayout.LayoutParams(
					mCategoryWidth, 40);
			
			tabtxtView[i].setLayoutParams(imglinelayout);
			tabtxtView[i].setGravity( Gravity.CENTER );
			tabtxtView[i].setTextColor( Color.WHITE );
			
			tabImgBtn[i].setBackgroundResource( R.drawable.at_sub_tabbg_n );
			tabImgBtn[i].addView( tabtxtView[i] );
			tabImgBtn[i].setGravity( Gravity.CENTER );
			
			RelativeLayout.LayoutParams termLayout = new RelativeLayout.LayoutParams(
					2, 40);
			viewTerm[i].setLayoutParams(termLayout);
			viewTerm[i].setBackgroundColor( Color.parseColor( "#00000000"));
			TabLayout.addView( tabImgBtn[i] );
			TabLayout.addView( viewTerm[i] );
			
		}
		tabImgBtn[ 0 ].setBackgroundResource( R.drawable.at_sub_tabbg_o );
	}
	
	private ViewFlipper flipper;
	private ImageView[] ContentsBack, ContentsImg_Img, ContentsImg_Select, ContentsImg_subThumb;
	private TextView[] ContentsName, CategoryName;
	private int MaxContentsCount = 12;
	private RelativeLayout[] ContentsAreaLayout;
	
	private int AllCategoryContents = 8;
	
	int AllCount = 0;
	
	private void setContentxArea(){
		
		if( CategoryState ){
			MaxContentsCount 	= CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.size();
		} else {
			MaxContentsCount 	= CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.size();
		}
				
		try {
			if( CategoryState ){
				AllCount = Integer.parseInt( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST_PAGE.get(0).countItem );
			} else {
				AllCount = Integer.parseInt( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_ALL_LIST_PAGEING.get(0).countItem );
			}
		} catch (Exception e) {
			e.printStackTrace();
			AllCount = 0;
		}
		
		
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
		} else {
			if( num == 1 ){
				BKPA.notifyDataSetChanged();
			}
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
	
	private CipherUtils mCipher = new CipherUtils();
	
	private int verValue = 0, verMax = 1;

	private libJSON LibJSON = new libJSON();
	private libJSON_GET GET_LibJSON = new libJSON_GET();
	private DownloadFile DLF = new DownloadFile();
	public String FOLDER_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test/";
	
	class setCategoryInfor extends AsyncTask<Integer, Integer, Integer> {
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
						String[] aaa = new String[7];
						
						aaa[0] = strBrandCD;
						aaa[1] = strTabCategory_ID;
						
						long timestamp = System.currentTimeMillis();
						aaa[2] = mCipher.encrypt( Constance.SEED , Constance.MAC + "@" + timestamp ); 
						aaa[3] = String.valueOf( timestamp ).toString().trim(); 
						GET_LibJSON.getCATEGORY_LIST_SUB(  aaa );
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
					setScrollViewTAbBar();
					new setContentsInfor().execute();
				}
			});
		}
		@Override
		protected void onCancelled() {
		}
	}
	
	
	
	class setContentsInfor extends AsyncTask<Integer, Integer, Integer> {
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
						String[] aaa = new String[7];
						
						aaa[0] = strBrandCD;
						
						if( CategoryState ){
							if( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_CATEGORY_SUB.size() > 0 ){
								aaa[1] = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_CATEGORY_SUB.get( ctpositon ).ctgryId;	
							}
						} else {
							aaa[1] = strCategory;
						}
						aaa[2] = String.valueOf( num );
						aaa[3] = "8";
						aaa[4] = "1";
						long timestamp = System.currentTimeMillis();
						aaa[5] = mCipher.encrypt( Constance.SEED , Constance.MAC + "@" + timestamp ); 
						aaa[6] = String.valueOf( timestamp ).toString().trim(); 
						
						if( CategoryState ){
							GET_LibJSON.getCONTENTS_PAGE_LIST( aaa );
						} else {
							GET_LibJSON.getCONTENTS_PAGE_ALL_LIST( aaa );
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
					new setContentsThumbNailDown().execute();
				}
			});
		}
		@Override
		protected void onCancelled() {
		}
	}
	
	class setContentsThumbNailDown extends AsyncTask<Integer, Integer, Integer> {
		@Override
		protected void onPreExecute() {
			
			if( CategoryState ){
				verMax = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.size();
			} else {
				verMax = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.size();
			}
			
			CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PAGE_ALL_LIST_THUMBPATH.clear();
			
			verValue = 0;
		}
		
		@Override
		protected Integer doInBackground(Integer... params) {
			while (isCancelled() == false) {
				if (verValue < verMax) {
					try {
						String[] aaa = new String[4];
						
						String Down_URL = "", strimgNAme = "";
						
						if( CategoryState ){
							aaa[0] = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.get( verValue ).brandCd;
							aaa[1] = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.get( verValue ).cntntsId;

							
							Down_URL 	= CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.get( verValue ).dwldThumbPath;
							strimgNAme 	= CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.get( verValue ).thumbFileLc;
						} else {
							aaa[0] = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( verValue ).brandCd;
							aaa[1] = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( verValue ).cntntsId;

							
							Down_URL 	= CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( verValue ).dwldThumbPath;
							strimgNAme 	= CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( verValue ).thumbFileLc;
						}
						
						
						
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
					
					setTest(num);
					
					if( num == 1 ) {
						mPager.setCurrentItem( num );
						PageState = true;
					}
					
					if( !ContentsInsertState ){
						new setProDuctInformation().execute();
					} else {
						pd.dismiss();
						pd.cancel();
					}
				}
			});
		}
		@Override
		protected void onCancelled() {
		}
	}
	
	class setProDuctInformation extends AsyncTask<Integer, Integer, Integer> {
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
			
			CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_INFORMATION_THUMB.clear();
			verMax = 1;
			
			verValue = 0;
		}
		@Override
		protected Integer doInBackground(Integer... params) {
			while (isCancelled() == false) {
				if (verValue < verMax) {
					try {
						boolean PruductState = false;
						String[] ContetnsParameter = new String[7];
						
						ContetnsParameter[0] = strBrandCD;
						if( strContents_ID.equals( "null") ){
							if( CategoryState ){
								ContetnsParameter[1] = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.get( ContentsClickPosition ).cntntsId;
							} else {
								ContetnsParameter[1] = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( ContentsClickPosition ).cntntsId;
							}
						} else {
							ContetnsParameter[1] = strContents_ID;
						}
						
						
						long timestamp_1 = System.currentTimeMillis();
						
						ContetnsParameter[2] = mCipher.encrypt( Constance.SEED , Constance.MAC + "@" + timestamp_1 ); 
						ContetnsParameter[3] = String.valueOf( timestamp_1 ).toString().trim();
						
						PruductState = GET_LibJSON.getCONTENTS_INFORMATION(  ContetnsParameter );
						
						
						if( PruductState ){
							String[] parameter = new String[5];
							
							parameter[0] = strBrandCD;
							parameter[1] = ContetnsParameter[1];
							parameter[2] = Constance.SHOPCD;
							
							long timestamp = System.currentTimeMillis();
							
							parameter[3] = ContetnsParameter[2]; 
							parameter[4] = ContetnsParameter[3]; 
							
							GET_LibJSON.getPRODUCT_LIST( parameter );
							
							String[] aaa = new String[4];
							
							aaa[0] = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_INFORMATION.get( verValue ).brandCd;
							aaa[1] = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_INFORMATION.get( verValue ).cntntsId;

							
							String Down_URL = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_INFORMATION.get( verValue ).dwldCntntsPath;
							String strimgNAme = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_INFORMATION.get( verValue ).cntntsFileLc;
							
							CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_INFORMATION_THUMB.add( DLF.Download_HttpFile(Down_URL, Constance.FILEPATH + "/" + aaa[0] + "/" + aaa[1] +"/", strimgNAme) );
							
						}
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
					setPruDuct();
					ContentsInsertState = true;
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
					ResetContents();
				}
			} else if( (start.x - event.getX()) < -50 ){
				flipper.setInAnimation(AnimationUtils.loadAnimation(mContext,
						R.anim.push_right_in));
				flipper.setOutAnimation(AnimationUtils.loadAnimation(mContext,
						R.anim.push_right_out));					
				if (num != 1) {
					num--;
					ResetContents();
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
	
	private void ResetContents(){
		new setContentsInfor().execute();
	}
	
//	public void FllingView_Next(){
////		flipper.removeAllViews();
//		flipper.showNext();
////		flipper.addView( ContentsAreaLayout[num - 1] );
//	}
//	
//	public void FllingView_Previous(){
////		flipper.removeAllViews();
////		flipper.addView( ContentsAreaLayout[num - 1] );
//		flipper.showPrevious();
//	}

	@Override
	public void onClick(View v) {
		try {
			if( ContentsImg_Img != null ){
				for( int i = 0; i < MaxContentsCount; i++){
					if( v == ContentsImg_Img[i] ){
						ContentsClickPosition = i;
						
						strContents_ID = "null";
						new setProDuctInformation().execute();
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			if( tabImgBtn != null ){
				for( int i = 0; i < MaxTabbCount; i++){
					if( v == tabImgBtn[i] ){
						CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.clear();
						
						CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PAGE_ALL_LIST_THUMBPATH.clear();
						
						CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.clear();
						
						CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST_PAGE.clear();
						
						setSubTabBarChicoe( i ) ;
						
						num = 1;
						
						if( i == 0 ){
							CategoryState = false;
							strCategory = AllCateGory;
							ctpositon = i;
						} else {
							CategoryState = true;
							ctpositon = i - 1;
						}
						new setContentsInfor().execute();
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if( v == btnContentsProductList ){
			setProduct();
		} else if ( v == btnContentsCn ){
			setContentsDetail();
		}
	}
	
	
	private void setSubTabBarChicoe( int position ){
		for( int i = 0; i < tabImgBtn.length; i++ ){
			if( i == position ){
				tabImgBtn[i].setBackgroundResource( R.drawable.at_sub_tabbg_o );
			} else {
				tabImgBtn[i].setBackgroundResource( R.drawable.at_sub_tabbg_n);
			}
		}
	}
	
	private void setContentsDetail(){
		
		if( beautyList.getVisibility() == View.VISIBLE) {
			Animation aaa = AnimationUtils.loadAnimation(mContext, R.anim.push_right_out);
			
			aaa.setAnimationListener( new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
					btnContentsProductList.setBackgroundResource( R.drawable.at_sub_btn_productlist_n );
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					RelativeLayout.LayoutParams imglinelayout = new RelativeLayout.LayoutParams(
							282, 430);
					imglinelayout.setMargins( 780, 32, 0, 0);
					beautyList.setLayoutParams(imglinelayout);
					beautyList.setVisibility( View.GONE );	
				}
			});
			beautyList.startAnimation( aaa );
		}
		
		if( txtContentsDetail.getVisibility() == View.GONE ){
			Animation aaa = AnimationUtils.loadAnimation(mContext, R.anim.push_left_in);
			
			RelativeLayout.LayoutParams imglinelayout = new RelativeLayout.LayoutParams(
					282, 430);
			imglinelayout.setMargins( 498, 32, 0, 0);
			txtContentsDetail.setLayoutParams(imglinelayout);
			aaa.setAnimationListener( new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub
					txtContentsDetail.setVisibility( View.VISIBLE );
					btnContentsCn.setBackgroundResource( R.drawable.at_sub_btn_descript_o);
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					
				}
			});
			txtContentsDetail.startAnimation( aaa );
		} else {
			Animation aaa = AnimationUtils.loadAnimation(mContext, R.anim.push_right_out);
			
			aaa.setAnimationListener( new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
					btnContentsCn.setBackgroundResource( R.drawable.at_sub_btn_descript_n);
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					RelativeLayout.LayoutParams imglinelayout = new RelativeLayout.LayoutParams(
							282, 430);
					imglinelayout.setMargins( 780, 32, 0, 0);
					txtContentsDetail.setLayoutParams(imglinelayout);
					txtContentsDetail.setVisibility( View.GONE );
				}
			});
			txtContentsDetail.startAnimation( aaa );
		}
	}
	
	
	private void setProduct(){
		
		if( txtContentsDetail.getVisibility() == View.VISIBLE) {
			Animation aaa = AnimationUtils.loadAnimation(mContext, R.anim.push_right_out);
			
			aaa.setAnimationListener( new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
					btnContentsCn.setBackgroundResource( R.drawable.at_sub_btn_descript_n);
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					RelativeLayout.LayoutParams imglinelayout = new RelativeLayout.LayoutParams(
							282, 430);
					imglinelayout.setMargins( 780, 32, 0, 0);
					txtContentsDetail.setLayoutParams(imglinelayout);
					txtContentsDetail.setVisibility( View.GONE );
				}
			});
			txtContentsDetail.startAnimation( aaa );
		}
		
		if( beautyList.getVisibility() == View.GONE ){
			Animation aaa = AnimationUtils.loadAnimation(mContext, R.anim.push_left_in);
			
			RelativeLayout.LayoutParams imglinelayout = new RelativeLayout.LayoutParams(
					282, 430);
			imglinelayout.setMargins( 498, 32, 0, 0);
			beautyList.setLayoutParams(imglinelayout);
			aaa.setAnimationListener( new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub
					beautyList.setVisibility( View.VISIBLE );
					btnContentsProductList.setBackgroundResource( R.drawable.at_sub_btn_productlist_o );
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					
				}
			});
			beautyList.startAnimation( aaa );
		} else {
			Animation aaa = AnimationUtils.loadAnimation(mContext, R.anim.push_right_out);
			
			aaa.setAnimationListener( new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
					btnContentsProductList.setBackgroundResource( R.drawable.at_sub_btn_productlist_n );
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					RelativeLayout.LayoutParams imglinelayout = new RelativeLayout.LayoutParams(
							282, 430);
					imglinelayout.setMargins( 780, 32, 0, 0);
					beautyList.setLayoutParams(imglinelayout);
					beautyList.setVisibility( View.GONE );
					
				}
			});
			beautyList.startAnimation( aaa );
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
