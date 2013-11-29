package fnc.salesforce.android.Product_Search;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
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
import fnc.salesforce.android.Constance.CONTENTS_PAGE_ALL_LIST_CONSTANCE;
import fnc.salesforce.android.Constance.Constance;
import fnc.salesforce.android.LIB.CipherUtils;
import fnc.salesforce.android.LIB.Decimal_Coma;
import fnc.salesforce.android.LIB.DownloadFile;
import fnc.salesforce.android.LIB.HidekeyBoard;
import fnc.salesforce.android.LIB.ImageDownloader;
import fnc.salesforce.android.LIB.ImageDownloader_Second;
import fnc.salesforce.android.LIB.KumaLog;
import fnc.salesforce.android.LIB.ProductDialog;
import fnc.salesforce.android.LIB.libJSON;
import fnc.salesforce.android.LIB.libJSON_GET;

public class Product_Search_Main implements OnClickListener{

	LayoutInflater inflater;
	
	private Context mContext;
		
	ProgressDialog pd;
	
	public Product_Search_Main(Context context){
		mContext = context;
		
		pd = new ProgressDialog( context );
		
		mProductDetailDialog = new ProductDialog( mContext, R.style.Transparent);
		
		
				
		inflater = ((Activity) mContext).getLayoutInflater();
	}
	
	private LinearLayout navigationRow;
	
	private TextView txtSales, txtNewProduct, txtSearchKeyWord;
	
	private ImageView btnSearchKeyWordCancle, layout_Text;
	
	private Button btnproductDetail, btnproductSearch, btnproductName, btnproductTypeCode;
	
	private EditText editSearchKeyWord;
	
	private TableRow rowKeyWord, rowSort;

	private AlertDialog.Builder dialogBuilder;
	
	public View setEducationView(){
		
		CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.clear();
		
		CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PAGE_ALL_LIST_THUMBPATH.clear();
		
		
		CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST_PAGE.clear();
		
		View view = null;
		
		ContentsAreaLayout = null;
		
		BKPA = null;
		
		dialogBuilder = new AlertDialog.Builder( mContext );
		
		num = 1;
		
		view = inflater.inflate(R.layout.product_search_all, null);
		
		navigationRow	= ( LinearLayout ) view.findViewById( R.id.navigationRow );
		
		txtNavi					= ( TextView ) view.findViewById( R.id.txtNavi );
		txtSales				= ( TextView ) view.findViewById( R.id.txtSales );
		txtNewProduct			= ( TextView ) view.findViewById( R.id.txtNewProduct );
		txtSearchKeyWord		= ( TextView ) view.findViewById( R.id.txtSearchKeyWord );
		
		btnSearchKeyWordCancle	= ( ImageView ) view.findViewById( R.id.btnSearchKeyWordCancle );
		layout_Text				= ( ImageView ) view.findViewById( R.id.layout_Text );
		
		editSearchKeyWord		= ( EditText ) view.findViewById( R.id.editSearchKeyWord );
		
		rowKeyWord				= ( TableRow ) view.findViewById( R.id.rowKeyWord );
		rowSort					= ( TableRow ) view.findViewById( R.id.rowSort );
		
		btnproductDetail		= ( Button ) view.findViewById( R.id.btnproductDetail );
		btnproductSearch		= ( Button ) view.findViewById( R.id.btnproductSearch );
		btnproductName			= ( Button ) view.findViewById( R.id.btnproductName );
		btnproductTypeCode		= ( Button ) view.findViewById( R.id.btnproductTypeCode );

		mDialogPassword = new DlgTest( mContext );
		
		rowKeyWord.setVisibility( View.GONE );
		rowSort.setVisibility( View.GONE );
		layout_Text.setVisibility( View.GONE );
		
		btnproductDetail.setOnClickListener( this );
		btnproductSearch.setOnClickListener( this );
		btnproductName.setOnClickListener( this );
		btnproductTypeCode.setOnClickListener( this );
		btnSearchKeyWordCancle.setOnClickListener( this );
		txtNewProduct.setOnClickListener( this );
		txtSales.setOnClickListener( this );
		
		txtNewProduct.setTextColor( Color.parseColor( "#89bd50" ) );
		txtSales.setTextColor( Color.WHITE );
		
		mPager = (ViewPager) view.findViewById(R.id.pager);
		
		mPager.setOnPageChangeListener( new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				if( position == 0 && PageState){
					
					CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST_PAGE.clear();
					CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.clear();
					
					CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PAGE_ALL_LIST_THUMBPATH.clear();
					
					PageState = false;
					
					num = 1;
					
					new setProductSearch().execute();
				} else {
					try {
						if( PageState ){
							if( position > num ){
								
								num = position;
								
								new setProductSearch().execute();
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

	private ImageDownloader_Second mImageDown = new ImageDownloader_Second();
	
	// 메인 좌측 메뉴 Param
	private static class ContentsImgBack_Param {
		static int width = 188;
		static int height = 246;
		static int Count = 4;
		static int Margine_top = 256;
		static int Margine_Left = 193;
	};
	// 메인 좌측 메뉴 Param
	private static class ContentsImg_Param {
		static int width = 173;
		static int height = 173;
		static int Count = 4;
		static int Margine_top = 256;
		static int Margine_Left = 193;
	};
		
	// 메인 좌측 메뉴 Param
	private static class ContentstxtName_Param {
		static int width = 173;
		static int height = 20;
		static int Count = 4;
		static int Margine_top = 184;
		static int Margine_Left = 193;
	};
	
	// 메인 좌측 메뉴 Param
	private static class ContentstxtCode_Param {
		static int width = 173;
		static int height = 20;
		static int Count = 4;
		static int Margine_top = 204;
		static int Margine_Left = 193;
	};
	
	// 메인 좌측 메뉴 Param
	private static class ContentstxtPrice_Param {
		static int width = 173;
		static int height = 20;
		static int Count = 4;
		static int Margine_top = 224;
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
			ContentsImg_Select	= new RelativeLayout[ AllCount ];
			ContentsImg_subThumb= new ImageView[ AllCount ];
			
			ContentsName		= new TextView[ AllCount ];
			ProductCode			= new TextView[ AllCount ];
			ProductPrice		= new TextView[ AllCount ];
		} else {
			if( position == 1 ){
				ContentsBack = null;
				ContentsImg_Img = null;
				ContentsImg_Select = null;
				ContentsImg_subThumb = null;
				ContentsName = null;
				ProductCode = null;
				ProductPrice = null;
				
				ContentsBack		= new ImageView[ AllCount ];
				ContentsImg_Img		= new ImageView[ AllCount ];
				ContentsImg_Select	= new RelativeLayout[ AllCount ];
				ContentsImg_subThumb= new ImageView[ AllCount ];
				
				ContentsName		= new TextView[ AllCount ];
				ProductCode			= new TextView[ AllCount ];
				ProductPrice		= new TextView[ AllCount ];
				for( int i = 0; i < ContentsAreaLayout.length; i++ ){
					if( ContentsAreaLayout[i] != null)
						ContentsAreaLayout[i].removeAllViews();
				}
			}
		}

		int ContentsCount = 0;
		
		try {
			if ( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.size() == 0 ){
				ContentsCount = 0;
			} else if( ( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.size() % AllCategoryContents ) == 0 ){
				ContentsCount = AllCategoryContents;
			} else {
				ContentsCount = ( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.size() % AllCategoryContents );
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		for( int i = 0; i < ContentsCount; i++ ){
			
			ContentsImg_subThumb[statrPosition] = new ImageView( mContext );
			ContentsBack[statrPosition] 		= new ImageView( mContext );
			ContentsImg_Img[statrPosition] 		= new ImageView( mContext );
			ContentsImg_Select[statrPosition] 	= new RelativeLayout( mContext );
			
			ContentsName[statrPosition]	 	= new TextView( mContext );
			ProductCode[statrPosition] 		= new TextView( mContext );
			ProductPrice[statrPosition] 	= new TextView( mContext );
			RelativeLayout.LayoutParams imgContentslayout_Back = new RelativeLayout.LayoutParams(
					ContentsImgBack_Param.width, ContentsImgBack_Param.height);

			int height_MargineCount =  i / ContentsImgBack_Param.Count;
			
			int Width_MargineCount =  i % ContentsImgBack_Param.Count;
			
			int Margine_Left  = 0;
			
			Margine_Left = ( ContentsImgBack_Param.Margine_Left * Width_MargineCount ) + 7;
			
			
			imgContentslayout_Back.setMargins( Margine_Left , ContentsImgBack_Param.Margine_top * height_MargineCount , 0, 0);
			
			ContentsBack[statrPosition].setLayoutParams( imgContentslayout_Back );
			ContentsBack[statrPosition].setBackgroundResource( R.drawable.at_as_thumbbg );

			
			RelativeLayout.LayoutParams imgContentslayout_Img = new RelativeLayout.LayoutParams(
					ContentsImg_Param.width, ContentsImg_Param.height);

			height_MargineCount =  i / ContentsImg_Param.Count;
			
			Width_MargineCount =  i % ContentsImg_Param.Count;
			
			Margine_Left  = 0;
			
			Margine_Left = ( ContentsImg_Param.Margine_Left * Width_MargineCount ) + 14;
			
			
			imgContentslayout_Img.setMargins( Margine_Left , ( ContentsImg_Param.Margine_top * height_MargineCount ) + 6, 0, 0);

			ContentsImg_Img[statrPosition].setLayoutParams( imgContentslayout_Img );

			ContentsImg_Img[statrPosition].setScaleType( ScaleType.CENTER );
			try {
				String First_URL = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( statrPosition ).thumbUrl.toString();
				String Second_URL = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( statrPosition ).extImgUrl.toString();
				mImageDown.download(mContext, First_URL, Second_URL, ContentsImg_Img[statrPosition], null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			ContentsImg_Img[statrPosition].setOnClickListener( this );	
			
			ContentsImg_Select[statrPosition].setLayoutParams( imgContentslayout_Img );


			RelativeLayout.LayoutParams imgContentslayout_Name = new RelativeLayout.LayoutParams(
					ContentstxtName_Param.width, ContentstxtName_Param.height);

			height_MargineCount =  i / ContentstxtName_Param.Count;
			
			Width_MargineCount =  i % ContentstxtName_Param.Count;
			
			Margine_Left  = 0;
			
			Margine_Left = ( ContentstxtName_Param.Margine_Left * Width_MargineCount ) + 14;
			
			
			imgContentslayout_Name.setMargins( Margine_Left , ( ContentsImgBack_Param.Margine_top * height_MargineCount ) + ContentstxtName_Param.Margine_top, 0, 0);
			
			try {
				ContentsName[statrPosition].setText( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( statrPosition ).prductNm );
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			ContentsName[statrPosition].setLayoutParams(imgContentslayout_Name);
			ContentsName[statrPosition].setGravity( Gravity.CENTER );
			ContentsName[statrPosition].setTextSize( 11 );
			ContentsName[statrPosition].setTextColor(Color.WHITE );
			ContentsName[statrPosition].setSingleLine();

			
			RelativeLayout.LayoutParams imgContentslayout_Code = new RelativeLayout.LayoutParams(
					ContentstxtCode_Param.width, ContentstxtCode_Param.height);

			height_MargineCount =  i / ContentstxtCode_Param.Count;
			
			Width_MargineCount =  i % ContentstxtCode_Param.Count;
			
			Margine_Left  = 0;
			
			Margine_Left = ( ContentstxtCode_Param.Margine_Left * Width_MargineCount ) + 14;
			
			
			imgContentslayout_Code.setMargins( Margine_Left , ( ContentsImgBack_Param.Margine_top * height_MargineCount ) + ContentstxtCode_Param.Margine_top, 0, 0);
			
			try {
				ProductCode[statrPosition].setText( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( statrPosition ).prductCd );
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			ProductCode[statrPosition].setLayoutParams(imgContentslayout_Code);
			ProductCode[statrPosition].setGravity( Gravity.CENTER );
			ProductCode[statrPosition].setTextSize( 11 );
			ProductCode[statrPosition].setTextColor(Color.WHITE );
			ProductCode[statrPosition].setSingleLine();
			

			RelativeLayout.LayoutParams imgContentslayout_Price = new RelativeLayout.LayoutParams(
					ContentstxtPrice_Param.width, ContentstxtPrice_Param.height);

			height_MargineCount =  i / ContentstxtPrice_Param.Count;
			
			Width_MargineCount =  i % ContentstxtPrice_Param.Count;
			
			Margine_Left  = 0;
			
			Margine_Left = ( ContentstxtPrice_Param.Margine_Left * Width_MargineCount ) + 14;
			
			
			imgContentslayout_Price.setMargins( Margine_Left , ( ContentsImgBack_Param.Margine_top * height_MargineCount ) + ContentstxtPrice_Param.Margine_top, 0, 0);
			
			try {
				String mPrice = "<font color=#ffffff>" + DC.Numeric3comma( Integer.parseInt( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( statrPosition ).copr ) ) + "  원    / </font>" ;
				String mJego = "<font color=#89bd50>" + CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( statrPosition ).jegoTotqy + "</font>";
				ProductPrice[statrPosition].setText( Html.fromHtml( mPrice + mJego)  );
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			ProductPrice[statrPosition].setLayoutParams(imgContentslayout_Price);
			ProductPrice[statrPosition].setGravity( Gravity.CENTER );
			ProductPrice[statrPosition].setTextSize( 11 );
			ProductPrice[statrPosition].setSingleLine();
			
									
			RelativeLayout.LayoutParams imgContentslayout_SubThumb = new RelativeLayout.LayoutParams(
					133, 44);
			imgContentslayout_SubThumb.setMargins( 0 , 0, 0, 0);
			ContentsImg_subThumb[statrPosition].setLayoutParams( imgContentslayout_SubThumb );
			
			ContentsImg_subThumb[statrPosition].setScaleType( ScaleType.FIT_XY );
						
			try {
				if( !CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( statrPosition ).soldoutTy.equals("N") ) {
					ContentsImg_subThumb[statrPosition].setBackgroundResource( R.drawable.at_sf_soldout );
					imgContentslayout_SubThumb.setMargins( 20 , 115, 0, 0);
					ContentsImg_subThumb[statrPosition].setLayoutParams( imgContentslayout_SubThumb );
//					ContentsImg_Select[statrPosition].setGravity( Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL );
				} else {
					if( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( statrPosition ).saledRate.equals("70")) {
						ContentsImg_subThumb[statrPosition].setBackgroundResource( R.drawable.at_sf_70per );
						ContentsImg_Select[statrPosition].setGravity( Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL );
					} else if( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( statrPosition ).saledRate.equals("90")) {
						ContentsImg_subThumb[statrPosition].setBackgroundResource( R.drawable.at_sf_90per );
						ContentsImg_Select[statrPosition].setGravity( Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL );
					} else {
						ContentsImg_subThumb[statrPosition].setBackgroundResource( R.drawable.at_sf_90per );
						ContentsImg_Select[statrPosition].setGravity( Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL );
						ContentsImg_Select[statrPosition].setVisibility( View.GONE );
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
//			soldoutTy
			
//			ContentsImg_subThumb[statrPosition].setBackgroundColor( Color.CYAN );
			
			ContentsImg_Select[statrPosition].addView( ContentsImg_subThumb[statrPosition] );

			if( ContentsAreaLayout[position] != null ){				
				ContentsAreaLayout[position].addView( ContentsBack[statrPosition] );
				ContentsAreaLayout[position].addView( ContentsImg_Img[statrPosition] );
				ContentsAreaLayout[position].addView( ContentsImg_Select[statrPosition] );
				ContentsAreaLayout[position].addView( ContentsName[statrPosition] );
				ContentsAreaLayout[position].addView( ProductCode[statrPosition] );
				ContentsAreaLayout[position].addView( ProductPrice[statrPosition] );
			}
			
			statrPosition++;
		}
		
		if( layout_Text.getVisibility() == View.GONE ){
			rowSort.setVisibility( View.VISIBLE );
			layout_Text.setVisibility( View.VISIBLE );
		}
		
//		BKPA.notifyDataSetChanged();
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
		
		try {
			imgNavi[NaviPosition - 1].setBackgroundResource( R.drawable.at_btn_slidecircle_o );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private DlgTest mDialogPassword;
	
	// 메인 좌측 메뉴 Param
	private static class ProdcutDetail_Code {
		static int width = 114;
		static int height = 30;
		static int Count = 3;
		static int Margine_top = 30;
		static int Margine_Left = 126;
	};
	
	// 메인 좌측 메뉴 Param
	private static class ProdcutDetail_Size{
		static int width = 90;
		static int height = 30;
		static int Count = 2;
		static int Margine_top = 30;
		static int Margine_Left = 95;
	};
			
	public class DlgTest extends Dialog implements OnClickListener
    {
		private Button btnDetailConfirm, btnDetailCancle;
		private RelativeLayout layoutSearchSize, layoutSearchCode;
		private String TypeCD = "", TypeName = "전체", SizeCD = "", SizeNAME = "";
		public DlgTest( Context context ) 
		{
			super(context);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			
			setContentView(R.layout.productdialog);
			
			layoutSearchSize = (RelativeLayout) findViewById( R.id.layoutSearchSize );
			layoutSearchCode = (RelativeLayout) findViewById( R.id.layoutSearchCode );
			
			btnDetailConfirm = (Button) findViewById( R.id.btnDetailConfirm );
			btnDetailCancle	= (Button) findViewById( R.id.btnDetailCancle );
			btnDetailConfirm.setOnClickListener( this );
			btnDetailCancle.setOnClickListener( this );
		}
		
		private TextView[] mProductCode_txt;

		private void setProductCode(){
			layoutSearchCode.removeAllViews();
			
			int productCodeSize = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRODUCT_DETAIL_SEARCH.size() + 1;
			
			if( mProductCode_txt == null ){
				mProductCode_txt		= new TextView[ productCodeSize ];
			} else {
				mProductCode_txt = null;
				
				mProductCode_txt		= new TextView[ productCodeSize ];
			}
			
			for( int i = 0; i < productCodeSize; i++ ){
				
				mProductCode_txt[i] 		= new TextView( mContext );

				RelativeLayout.LayoutParams imgContentslayout_Back = new RelativeLayout.LayoutParams(
						ProdcutDetail_Code.width, ProdcutDetail_Code.height);

				int height_MargineCount =  i / ProdcutDetail_Code.Count;
				
				int Width_MargineCount =  i % ProdcutDetail_Code.Count;
				
				int Margine_Left  = 0;
				
				Margine_Left = ( ProdcutDetail_Code.Margine_Left * Width_MargineCount ) + 5;
				
				
				imgContentslayout_Back.setMargins( Margine_Left , ( ProdcutDetail_Code.Margine_top * height_MargineCount ) + 5 , 0, 0);
				
				try {
					if( i == 0 ){
						mProductCode_txt[i].setText( "전체" );
					} else {
						mProductCode_txt[i].setText( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRODUCT_DETAIL_SEARCH.get( i  - 1 ).classNm );
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				mProductCode_txt[i].setOnClickListener( this );
				
				mProductCode_txt[i].setLayoutParams(imgContentslayout_Back);
				mProductCode_txt[i].setGravity( Gravity.CENTER_VERTICAL );
				mProductCode_txt[i].setTextSize( 13 );
				mProductCode_txt[i].setTextColor(Color.WHITE );
				mProductCode_txt[i].setSingleLine();

				layoutSearchCode.addView( mProductCode_txt[i] );
			}
			
			mProductCode_txt[ 0 ].setTextColor(Color.parseColor("#44b6ce") );
			
			ProductPosition = 0;
						
		}
		
		private TextView[] mProductSize_txt;

		private void setProductSize(){
			layoutSearchSize.removeAllViews();
			int productCodeSize = 0;
			if( ProductPosition == 0 ){
				productCodeSize = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRODUCT_DETAIL_SEARCH_SIZE.size();
			} else {
				productCodeSize = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRODUCT_DETAIL_SEARCH_SIZE.size() + 1;
			}
			
			
			if( mProductSize_txt == null ){
				mProductSize_txt		= new TextView[ productCodeSize ];
			} else {
				mProductSize_txt = null;
				
				mProductSize_txt		= new TextView[ productCodeSize ];
			}
			
			int productHarf = 0;
			
			if( productCodeSize / 2 == 0 ){
				productHarf = 1;
			} else {
				if( productCodeSize % 2  == 0 ){
					productHarf = ( productCodeSize / 2 ) ;
				} else {
					productHarf = ( productCodeSize / 2 ) + 1;
				}
			}
			
			
			
			for( int i = 0; i < productCodeSize; i++ ){
				
				mProductSize_txt[i] 		= new TextView( mContext );

				RelativeLayout.LayoutParams imgContentslayout_Back = new RelativeLayout.LayoutParams(
						ProdcutDetail_Size.width, ProdcutDetail_Size.height);

				int height_MargineCount =  i / ProdcutDetail_Size.Count;
				
				int Width_MargineCount =  i % ProdcutDetail_Size.Count;
				
				int Margine_Left  = 0;
				
				Margine_Left = ( ProdcutDetail_Size.Margine_Left * Width_MargineCount ) + 5;
				
				
				imgContentslayout_Back.setMargins( Margine_Left , ( ProdcutDetail_Size.Margine_top * height_MargineCount ) + 5 , 0, 0);
				
				try {
					if( ProductPosition == 0 ){
						mProductSize_txt[i].setText( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRODUCT_DETAIL_SEARCH_SIZE.get( i ).sizeCd );
						if( i == 0 ){
							strmSizeCD = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRODUCT_DETAIL_SEARCH_SIZE.get( i ).sizeCd;
						}
					} else {
						if( i == 0 ){
							strmSizeCD = "";
							mProductSize_txt[i].setText( "전체" );
						} else {
							mProductSize_txt[i].setText( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRODUCT_DETAIL_SEARCH_SIZE.get( i  - 1 ).sizeCd );
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				mProductSize_txt[i].setOnClickListener( this );
				
				mProductSize_txt[i].setLayoutParams(imgContentslayout_Back);
				mProductSize_txt[i].setGravity( Gravity.CENTER );
				mProductSize_txt[i].setTextSize( 13 );
				mProductSize_txt[i].setTextColor(Color.WHITE );
				mProductSize_txt[i].setSingleLine();

				layoutSearchSize.addView( mProductSize_txt[i] );
			}
			
			mProductSize_txt[ 0 ].setTextColor(Color.parseColor("#89bd50") );
		}
		
		

		@Override
		public void show() {
			// TODO Auto-generated method stub
			super.show();
			strmSizeCD = SizeCD;
			strmClassCD = TypeCD;
			strmClassName = TypeName;
			if( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRODUCT_DETAIL_SEARCH.size() == 0 ) {
				new setDetailSearch().execute();
			}
		}

		private int ProductPosition = 0;
		
		private void setProductChoice( int position ){
			ProductPosition = position;
			for( int i = 0; i < mProductCode_txt.length; i++ ){
				if( i == position ){
					mProductCode_txt[ i ].setTextColor(Color.parseColor("#44b6ce") );
				} else {
					mProductCode_txt[i].setTextColor(Color.parseColor("#ffffff") );
				}
			}
		}
		
		private void setProduct_Size_Choice( int position ){
			for( int i = 0; i < mProductSize_txt.length; i++ ){
				if( i == position ){
					mProductSize_txt[ i ].setTextColor(Color.parseColor("#89bd50") );
				} else {
					mProductSize_txt[i].setTextColor(Color.parseColor("#ffffff") );
				}
			}
		}

		@Override
		public void onClick(View v) {
			

			try {
				if( mProductCode_txt != null ){
					for( int i = 0; i < mProductCode_txt.length; i++){ 
						if( v == mProductCode_txt[i] ){
							setProductChoice( i );
							if( i == 0 ){
								strmClassCD = "";
								strmClassName = "전체";
								TypeCD = "";
								TypeName = "전체";
							} else {
								strmClassCD = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRODUCT_DETAIL_SEARCH.get( i  - 1 ).classCd;
								strmClassName = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRODUCT_DETAIL_SEARCH.get( i  - 1 ).classNm;
								TypeCD = strmClassCD;
								TypeName = strmClassName;
							}
							new setDetailSearch_SIZE().execute();
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				if( mProductSize_txt != null ){
					for( int i = 0; i < mProductSize_txt.length; i++){ 
						if( v == mProductSize_txt[i] ){
							setProduct_Size_Choice( i );
							if( ProductPosition == 0 ){
								strmSizeCD = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRODUCT_DETAIL_SEARCH_SIZE.get( i ).sizeCd;
								SizeCD = strmSizeCD;
							} else {
								if( i == 0 ){
									strmSizeCD = "";
									SizeCD = "";
								} else {
									strmSizeCD = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRODUCT_DETAIL_SEARCH_SIZE.get( i  - 1 ).sizeCd;
									SizeCD = strmSizeCD;
								}
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if( v == btnDetailConfirm ){
				CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST_PAGE.clear();
				CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.clear();
				
				CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PAGE_ALL_LIST_THUMBPATH.clear();
				
				rowKeyWord.setVisibility( View.VISIBLE );
				
				mSearchTy = "detail";
				
				num = 1;
				
				if( strmSizeCD.length() > 0 ){
					txtSearchKeyWord.setText( strmClassName + "   /   " + strmSizeCD );
				} else {
					txtSearchKeyWord.setText( strmClassName + "   /   전체" );
				}
				
				new setProductSearch().execute();		
				
				mDialogPassword.cancel();
				mDialogPassword.dismiss();
			} else if( v == btnDetailCancle ){
				mDialogPassword.cancel();
				mDialogPassword.dismiss();
			}
		}
    }
	
	private DialogInterface.OnClickListener DetailCofirm = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			mDialogPassword.show();
		}
	};
	
	private CipherUtils mCipher = new CipherUtils();
	
	private int verValue = 0, verMax = 1;

	private String strMType = "new", strmSizeCD = "", strmClassCD = "", strmprductCd = "", mSearchTy = "normal", strmprductName = "";
	private String strmClassName = "전체";
	
	private libJSON LibJSON = new libJSON();
	private libJSON_GET GET_LibJSON = new libJSON_GET();
	private DownloadFile DLF = new DownloadFile();
	public String FOLDER_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test/";
	
	class setProductSearch extends AsyncTask<Integer, Integer, Integer> {
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
						String[] aaa = new String[13];

						aaa[0] = Constance.BEANDCD;
						aaa[1] = String.valueOf( num );
						aaa[2] = "12";
						aaa[3] = "1";
						long timestamp = System.currentTimeMillis();
						aaa[4] = mCipher.encrypt( Constance.SEED , Constance.MAC + "@" + timestamp ); 
						aaa[5] = String.valueOf( timestamp ).toString().trim(); 
						aaa[6] = strmClassCD;
						aaa[7] = strmSizeCD;
						aaa[8] = strMType;
						aaa[9] = strmprductName;
						aaa[10] = strmprductCd;
						aaa[11] = Constance.SHOPCD; //6J1087
//						aaa[11] = "6J1087"; //6J1087
						aaa[12] = mSearchTy;
						
						
						GET_LibJSON.getPRODUCT_SEARCH( aaa );
						
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
						String Down_URL = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( verValue ).thumbUrl;
						
						CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PAGE_ALL_LIST_THUMBPATH.add( Down_URL );
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
	
	class setDetailSearch extends AsyncTask<Integer, Integer, Integer> {
		@Override
		protected void onPreExecute() {
			
			if( !pd.isShowing() ) {
				pd.setMessage("정보를 요청중 입니다. 잠시만 기다려 주십시요.");
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
						String[] aaa = new String[3];

						long timestamp = System.currentTimeMillis();
						aaa[0] = mCipher.encrypt( Constance.SEED , Constance.MAC + "@" + timestamp ); 
						aaa[1] = String.valueOf( timestamp ).toString().trim(); 
						aaa[2] = Constance.BEANDCD;
						
						GET_LibJSON.getPRODUCT_SEARCH_DETAIL( aaa );
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
						if( mDialogPassword.isShowing() )
							mDialogPassword.setProductCode();
						strmClassCD = "";
						new setDetailSearch_SIZE().execute();
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
	
	class setDetailSearch_SIZE extends AsyncTask<Integer, Integer, Integer> {
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
						aaa[2] = Constance.BEANDCD;
						aaa[3] = strmClassCD;
						
						GET_LibJSON.getPRODUCT_SEARCH_DETAIL_SIZE( aaa );
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
						pd.dismiss();
						pd.cancel();
					}
					try {
						if( mDialogPassword.isShowing() )
							mDialogPassword.setProductSize();
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

	private RelativeLayout[] ContentsImg_Select;
	private ImageView[] ContentsBack, ContentsImg_Img, ContentsImg_subThumb;
	private TextView[] ContentsName, ProductCode, ProductPrice;
	private int MaxContentsCount = 0;
	private RelativeLayout[] ContentsAreaLayout;
	private int AllCategoryContents = 12;
	
	int AllCount = 0;
	
	private void setContentxArea(){
		
		MaxContentsCount 	= CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.size();
				
		
		AllCount = Integer.parseInt( Constance.Page_ALL_Count );
		
		MaxPage = Integer.parseInt( Constance.PageCount );
		
		
		
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
	private ProductDialog mProductDetailDialog;

	@Override
	public void onClick(View v) {
		try {
			if( ContentsImg_Img != null ){
				for( int i = 0; i < MaxContentsCount; i++){ 
					if( v == ContentsImg_Img[i] ){
						mProductDetailDialog.SetProduct_CD( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( i ).prductCd );
						mProductDetailDialog.show();
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		if( v == btnproductDetail ){
//			handler.postDelayed(new Runnable() {
//				@Override
//				public void run() {
//					new setDetailSearch().execute();
//				}
//			}, 200);
			mDialogPassword.show();
		} else if( v == btnproductSearch ){
			HidekeyBoard.KeyboardHide(mContext, btnproductSearch.getWindowToken() );
			
			if( editSearchKeyWord.getText().toString().trim().length() > 0 ) {
				
				CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST_PAGE.clear();
				CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.clear();
				
				CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PAGE_ALL_LIST_THUMBPATH.clear();
				
				mSearchTy = "normal";
				
				num = 1;
				
				Constance.Page_ALL_Count = "0";
				
				Constance.PageCount = "1";
				
				PageState = false;
				
				strMType = "new";
				
				txtNewProduct.setTextColor( Color.parseColor( "#89bd50" ) );
				txtSales.setTextColor( Color.WHITE );
				
				if( SearchType ){
					strmprductCd 	= editSearchKeyWord.getText().toString().trim();
				} else {
					strmprductName	= editSearchKeyWord.getText().toString().trim();
				}
				new setProductSearch().execute();
			} else {
				dialogBuilder.setTitle("SalesForce");
				dialogBuilder.setMessage( "검색어를 입력해 주십시오.");
		        dialogBuilder.setPositiveButton( "확인" , null );
		        dialogBuilder.show();
			}
			
		} else if( v == btnproductName ){
			SearchType = false;
			strmprductCd = "";
			editSearchKeyWord.setText("");
			btnproductName.setBackgroundResource( R.drawable.at_sf_inv_btn_o);
			btnproductTypeCode.setBackgroundResource( R.drawable.at_sf_inv_btn_n);
		} else if( v == btnproductTypeCode ){
			SearchType = true;
			strmprductName = "";
			editSearchKeyWord.setText("");
			btnproductName.setBackgroundResource( R.drawable.at_sf_inv_btn_n);
			btnproductTypeCode.setBackgroundResource( R.drawable.at_sf_inv_btn_o);
		} else if( v == btnSearchKeyWordCancle ){
			strmClassCD = "";
			strmSizeCD = "";
			rowKeyWord.setVisibility( View.GONE );
			
			CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST_PAGE.clear();
			CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.clear();
			
			CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PAGE_ALL_LIST_THUMBPATH.clear();
			
			num = 1;
			
			Constance.Page_ALL_Count = "0";
			
			Constance.PageCount = "1";
			
			PageState = false;
			
			setContentxArea();
			
			setTest(num);
			
			navigationRow.removeAllViews();
			
			if( num == 1 ) {
				mPager.setCurrentItem( num );
			}
		} else if( v == txtNewProduct ){
			CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST_PAGE.clear();
			CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.clear();
			
			CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PAGE_ALL_LIST_THUMBPATH.clear();
			
			PageState = false;
			
			num = 1;
			strMType = "new";
			txtNewProduct.setTextColor( Color.parseColor( "#89bd50" ) );
			txtSales.setTextColor( Color.WHITE );
			new setProductSearch().execute();
		} else if( v == txtSales ){
			CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST_PAGE.clear();
			CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.clear();
			
			CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PAGE_ALL_LIST_THUMBPATH.clear();
			
			PageState = false;
			
			num = 1;
			strMType = "GoodSales";
			txtSales.setTextColor( Color.parseColor( "#89bd50" ) );
			txtNewProduct.setTextColor( Color.WHITE );
			new setProductSearch().execute();
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
