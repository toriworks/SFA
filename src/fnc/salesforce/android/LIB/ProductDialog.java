package fnc.salesforce.android.LIB;

import fnc.salesforce.android.R;
import fnc.salesforce.android.Constance.CONTENTS_PAGE_ALL_LIST_CONSTANCE;
import fnc.salesforce.android.Constance.Constance;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.view.View.OnClickListener;

/**
 * 해빛 소개 다이얼로그
 * */
public class ProductDialog extends Dialog implements OnClickListener{

	int value = 0;
	Handler mHandler;

	static String pathURL = "", displayCode = "";

	private ImageView  imgPruDuctThumb;
	
	
	private ImageButton btnPruductDetail, btnPruductInventory, btnClose, btnPruductTOP_3;
	
	
	private ListView beautyList;
	
	private Production_Detail_ListAdabter PDL;
	
	private ProgressDialog pd;
	
	private TextView txtProduct_Contents, txtProduct_Made, txtProduct_Made_By, txtProduct_Made_What, txtMove;
	private TextView txtProduct_Price_USALLY, txtProduct_Price_SEll, txtProduct_Product_Code, txtProductName;
	
	private boolean ProductType = false;
	
	private Context mContext;
	
	private RelativeLayout rowColor;
	
	private RelativeLayout layout_DetailList;
	
	private ScrollView layout_DetailContents;
	
	private Decimal_Coma DC = new Decimal_Coma();
	
	/**
	 * 해빛 소개 다이얼로그 초기화
	 * 
	 * @param context
	 * @param theme 다이얼로그 테마 
	 * */
	public ProductDialog(Context context, int theme) {
		super(context, theme);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				 WindowManager.LayoutParams.FLAG_FULLSCREEN);
		WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
		lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		lpWindow.dimAmount = 0.45f;
		lpWindow.windowAnimations = android.R.anim.accelerate_interpolator
				| android.R.anim.fade_in | android.R.anim.fade_out;
		getWindow().setAttributes(lpWindow);
		
		mContext = context;
		
		pd = new ProgressDialog( context );
		
		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		View web_View = inflater.inflate(R.layout.product_detail, null);
		
		txtProduct_Made 		= (TextView ) web_View.findViewById( R.id.txtProduct_Made );
		txtProduct_Made_By 		= (TextView ) web_View.findViewById( R.id.txtProduct_Made_By );
		txtProduct_Made_What 	= (TextView ) web_View.findViewById( R.id.txtProduct_Made_What );
		txtProduct_Price_USALLY = (TextView ) web_View.findViewById( R.id.txtProduct_Price_USALLY );
		txtProduct_Price_SEll 	= (TextView ) web_View.findViewById( R.id.txtProduct_Price_SEll );
		txtProduct_Product_Code = (TextView ) web_View.findViewById( R.id.txtProduct_Product_Code );
		txtProductName			= (TextView ) web_View.findViewById( R.id.txtProductName );
		txtMove					= (TextView ) web_View.findViewById( R.id.txtMove );
		
		imgPruDuctThumb			= (ImageView ) web_View.findViewById( R.id.imgPruDuctThumb );
		btnPruductDetail		= (ImageButton ) web_View.findViewById( R.id.btnContentsCn );
		btnPruductInventory 	= (ImageButton ) web_View.findViewById( R.id.btnPruductInventory );
		btnClose 				= (ImageButton ) web_View.findViewById( R.id.btnClose );
		btnPruductTOP_3			= (ImageButton ) web_View.findViewById( R.id.btnPruductTOP_3 );
		
		layout_DetailList 		= (RelativeLayout ) web_View.findViewById( R.id.layout_DetailList );
		layout_DetailContents 	= (ScrollView ) web_View.findViewById( R.id.layout_DetailContents );
				
		rowColor				= (RelativeLayout) web_View.findViewById( R.id.rowColor );
		
		btnPruductDetail.setOnClickListener( this );
		btnPruductInventory.setOnClickListener( this );
		btnClose.setOnClickListener( this );
		btnPruductTOP_3.setOnClickListener( this );
		
		mPager = (ViewPager) web_View.findViewById(R.id.pager);
		
		mPager.setOnPageChangeListener( new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				if( position == 0 ){
					btnPruductDetail.setBackgroundResource( R.drawable.im_lp_btn_detail_o );
					btnPruductInventory.setBackgroundResource( R.drawable.im_lp_btn_inventory_n );
					btnPruductTOP_3.setBackgroundResource( R.drawable.im_lp_btn_star_n );
				} else if( position == 1 ){
					PDL.notifyDataSetChanged();
					btnPruductDetail.setBackgroundResource( R.drawable.im_lp_btn_detail_n );
					btnPruductInventory.setBackgroundResource( R.drawable.im_lp_btn_inventory_o );
					btnPruductTOP_3.setBackgroundResource( R.drawable.im_lp_btn_star_n );
				} else if( position == 2 ){
					btnPruductDetail.setBackgroundResource( R.drawable.im_lp_btn_detail_n );
					btnPruductInventory.setBackgroundResource( R.drawable.im_lp_btn_inventory_n );
					btnPruductTOP_3.setBackgroundResource( R.drawable.im_lp_btn_star_o );
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		
		txtMove.setVisibility( View.GONE );
		
		setContentView( web_View );
		// this.setCancelable(false);
	}
	
	private int num = 1;
	private BkPagerAdapter BKPA;
	
	private ViewPager mPager;
	
	private RelativeLayout[] ContentsAreaLayout = new RelativeLayout[3];
	
	//Pager �ƴ��� ����
	private class BkPagerAdapter extends PagerAdapter{
		private LayoutInflater mInflater;
		public BkPagerAdapter( Context con) { 
			super(); 
		}

		@Override 
		public int getCount() {
			return 3;
		}

		//�������� ����� �䰴ü ��/���
		@Override 
		public Object instantiateItem(View pager, int position)
		{			
			View v = null;
			if(position == 0){
				try {
					v = setContents();
				} catch (Exception e) {
					v = null;
					e.printStackTrace();
				}
			} else if(position == 1){
				try {
					v = setListView();
				} catch (Exception e) {
					v = null;
					e.printStackTrace();
				}
			} else {
				try {
					v = setTOP_3();
				} catch (Exception e) {
					v = null;
					e.printStackTrace();
				}
			}

			((ViewPager)pager).addView(v, 0);
			return v; 
		}
		//�� ��ü ����.
		@Override 
		public void destroyItem(View pager, int position, Object view) {
				((ViewPager)pager).removeView((View)view);
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
	
	private View setListView(){
//		ContentsAreaLayout[1].removeAllViews();
		
		LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
		View web_View = inflater.inflate(R.layout.product_detail_list, null);
		
		beautyList = (ListView ) web_View.findViewById( R.id.beautyList );
		
//		ContentsAreaLayout[1].addView(web_View);
		
		PDL = null;
		PDL = new Production_Detail_ListAdabter( mContext );
		
		beautyList.setAdapter(PDL);
		
		return web_View;
	}
	
	private RelativeLayout layoutProduct_Top3;
	
	private View setTOP_3(){
//		ContentsAreaLayout[2].removeAllViews();
		
		LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
		View web_View = inflater.inflate(R.layout.product_detail_top_3, null);
		
		layoutProduct_Top3	= (RelativeLayout ) web_View.findViewById( R.id.layoutProduct_Top3 );
		
		setTest();
		
		return web_View;
	}
	
	
	// 메인 좌측 메뉴 Param
	private static class ContentsImgBack_Param {
		static int width = 188;
		static int height = 246;
		static int Count = 3;
		static int Margine_top = 0;
		static int Margine_Left = 203;
	};
	// 메인 좌측 메뉴 Param
	private static class ContentsImg_Param {
		static int width = 173;
		static int height = 173;
		static int Count = 3;
		static int Margine_top = 0;
		static int Margine_Left = 203;
	};
	
	// 메인 좌측 메뉴 Param
	private static class ContentstxtName_Param {
		static int width = 173;
		static int height = 30;
		static int Count = 4;
		static int Margine_top = 184;
		static int Margine_Left = 203;
	};
	
	// 메인 좌측 메뉴 Param
	private static class ContentstxtCode_Param {
		static int width = 173;
		static int height = 30;
		static int Count = 4;
		static int Margine_top = 214;
		static int Margine_Left = 203;
	};
	
	private void setTest(){
		
		layoutProduct_Top3.removeAllViews();
				
		ImageView[] ContentsBack		= new ImageView[ 3 ];
		ImageView[] ContentsImg_Img		= new ImageView[ 3 ];
		TextView[] ContentsName			= new TextView[ 3 ];
		TextView[] ProductCode			= new TextView[ 3 ];
		
		int ContentsCount = 3;

		for( int i = 0; i < ContentsCount; i++ ){
			ContentsBack[i] 		= new ImageView( mContext );
			ContentsImg_Img[i] 		= new ImageView( mContext );
			ContentsName[i]	 		= new TextView( mContext );
			ProductCode[i]	 		= new TextView( mContext );

			RelativeLayout.LayoutParams imgContentslayout_Back = new RelativeLayout.LayoutParams(
					ContentsImgBack_Param.width, ContentsImgBack_Param.height);

			int height_MargineCount =  1;
			
			int Width_MargineCount =  i % ContentsImgBack_Param.Count;
			
			int Margine_Left  = 0;
			
			Margine_Left = ( ContentsImgBack_Param.Margine_Left * Width_MargineCount ) + 7;
			
			
			imgContentslayout_Back.setMargins( Margine_Left , ContentsImgBack_Param.Margine_top * height_MargineCount , 0, 0);
			
			ContentsBack[i].setLayoutParams( imgContentslayout_Back );
			ContentsBack[i].setBackgroundResource( R.drawable.at_as_thumbbg );
			
			RelativeLayout.LayoutParams imgContentslayout_Img = new RelativeLayout.LayoutParams(
					ContentsImg_Param.width, ContentsImg_Param.height);

			height_MargineCount =  1;
			
			Width_MargineCount =  i % ContentsImg_Param.Count;
			
			Margine_Left  = 0;
			
			Margine_Left = ( ContentsImg_Param.Margine_Left * Width_MargineCount ) + 14;
			
			
			imgContentslayout_Img.setMargins( Margine_Left , ( ContentsImg_Param.Margine_top * height_MargineCount ) + 6, 0, 0);

			ContentsImg_Img[i].setLayoutParams( imgContentslayout_Img );

			ContentsImg_Img[i].setScaleType( ScaleType.CENTER );
			
			ContentsImg_Img[i].setImageBitmap( null );
			try {
				String First_URL = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_DETAIL_LIST_TOP3.get( i ).thumbUrl.toString();
				mImageDown.download(mContext, First_URL, ContentsImg_Img[i], null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			RelativeLayout.LayoutParams imgContentslayout_Name = new RelativeLayout.LayoutParams(
					ContentstxtName_Param.width, ContentstxtName_Param.height);

			height_MargineCount =  1;
			
			Width_MargineCount =  i % ContentstxtName_Param.Count;
			
			Margine_Left  = 0;
			
			Margine_Left = ( ContentstxtName_Param.Margine_Left * Width_MargineCount ) + 14;
			
			
			imgContentslayout_Name.setMargins( Margine_Left , ( ContentsImgBack_Param.Margine_top * height_MargineCount ) + ContentstxtName_Param.Margine_top, 0, 0);
			
			ContentsName[i].setText("");
			
			try {
				ContentsName[i].setText( "판매율 ( " + CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_DETAIL_LIST_TOP3.get( i ).top3Cp  + " )");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			ContentsName[i].setLayoutParams(imgContentslayout_Name);
			ContentsName[i].setGravity( Gravity.CENTER );
			ContentsName[i].setTextSize( 16 );
			ContentsName[i].setTextColor(Color.WHITE );
			ContentsName[i].setSingleLine();

			
			RelativeLayout.LayoutParams imgContentslayout_Code = new RelativeLayout.LayoutParams(
					ContentstxtCode_Param.width, ContentstxtCode_Param.height);

			height_MargineCount =  1;
			
			Width_MargineCount =  i % ContentstxtCode_Param.Count;
			
			Margine_Left  = 0;
			
			Margine_Left = ( ContentstxtCode_Param.Margine_Left * Width_MargineCount ) + 14;
			
			
			imgContentslayout_Code.setMargins( Margine_Left , ( ContentsImgBack_Param.Margine_top * height_MargineCount ) + ContentstxtCode_Param.Margine_top, 0, 0);
			
			ProductCode[i].setText("");
			
			try {
				ProductCode[i].setText( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_DETAIL_LIST_TOP3.get( i ).prductCd );
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			ProductCode[i].setLayoutParams(imgContentslayout_Code);
			ProductCode[i].setGravity( Gravity.CENTER );
			ProductCode[i].setTextSize( 16 );
			ProductCode[i].setTextColor(Color.WHITE );
			ProductCode[i].setSingleLine();
			
			layoutProduct_Top3.addView( ContentsBack[i] );
			layoutProduct_Top3.addView( ContentsImg_Img[i] );
			layoutProduct_Top3.addView( ContentsName[i] );
			layoutProduct_Top3.addView( ProductCode[i] );
		}
	}
	private ImageDownloader mImageDown = new ImageDownloader();
	private void setContentxArea(){
		if( ContentsAreaLayout == null ){
			ContentsAreaLayout	= new RelativeLayout[ 3 ];
		}

		if( BKPA == null ){
			BKPA = new BkPagerAdapter( mContext );
			mPager.setAdapter( BKPA );
		} else {
			if( num == 1 )
				BKPA.notifyDataSetChanged();
		}

		mPager.setCurrentItem( 0 );
		
//		setContents();
	}
	
	private View setContents(){
//		ContentsAreaLayout[0].removeAllViews();
		LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
		View web_View = inflater.inflate(R.layout.prodoct_detail_contents, null);
		
		txtProduct_Contents = (TextView ) web_View.findViewById( R.id.txtProduct_Contents );
		
		if( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_DETAIL_LIST.get( 0 ).prductDesc.equals("null") ){
			txtProduct_Contents.setText( "" );
		} else {
			txtProduct_Contents.setText( Html.fromHtml( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_DETAIL_LIST.get( 0 ).prductDesc )  );
		}
		
		return web_View;
	}
	
	private void setDetailContents_1(){
		if( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_DETAIL_LIST.size() > 0 ){
			
			IDL.download(mContext, CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_DETAIL_LIST.get( 0 ).prductUrl, imgPruDuctThumb, null);
			
			try {
				if( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_DETAIL_LIST.get( 0 ).origNm.equals("null") ){
					txtProduct_Made.setText( " " );
				} else {
					txtProduct_Made.setText( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_DETAIL_LIST.get( 0 ).origNm ); 
				}
			} catch (Exception e) {
				txtProduct_Made.setText( " " );
			}
			
			
			try {
				if( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_DETAIL_LIST.get( 0 ).maker.equals("null") ){
					txtProduct_Made_By.setText( " " );
				} else {
					txtProduct_Made_By.setText( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_DETAIL_LIST.get( 0 ).maker );
				}
			} catch (Exception e) {
				txtProduct_Made_By.setText( " " );
			}
			
			try {
				if( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_DETAIL_LIST.get( 0 ).matt.equals("null") ){
					txtProduct_Made_What.setText( " " );
				} else {
					txtProduct_Made_What.setText( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_DETAIL_LIST.get( 0 ).matt ); 
				}
			} catch (Exception e) {
				txtProduct_Made_What.setText( " " );
			}
			 
			try {
				if( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_DETAIL_LIST.get( 0 ).copr.equals("null") ){
					txtProduct_Price_USALLY.setText( " " );
				} else {
					String mCopr = DC.Numeric3comma( Integer.parseInt( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_DETAIL_LIST.get( 0 ).copr ) );
					
					txtProduct_Price_USALLY.setText( mCopr );
				}
			} catch (Exception e) {
				txtProduct_Price_USALLY.setText( " " );
			}
			
			try {
				if( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_DETAIL_LIST.get( 0 ).rspr.equals("null") ){
					txtProduct_Price_SEll.setText( " " );
				} else {
					String mRspr = DC.Numeric3comma( Integer.parseInt( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_DETAIL_LIST.get( 0 ).rspr ) );
					txtProduct_Price_SEll.setText( mRspr );
				}
			} catch (Exception e) {
				txtProduct_Price_SEll.setText( " " );
			}
			
			try {
				if( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_DETAIL_LIST.get( 0 ).prductCd.equals("null") ){
					txtProduct_Product_Code.setText( " " );
				} else {
					txtProduct_Product_Code.setText( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_DETAIL_LIST.get( 0 ).prductCd );
				}
			} catch (Exception e) {
				txtProduct_Product_Code.setText( " " );
			}
			
			try {
				if( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_DETAIL_LIST.get( 0 ).prductCd.equals("null") ){
					txtProductName.setText( " " );
				} else {
					txtProductName.setText( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_DETAIL_LIST.get( 0 ).prductNm );
				}
			} catch (Exception e) {
				txtProductName.setText( " " );
			}
			
			
			
		} else {
			try {
				imgPruDuctThumb.getDrawingCache().recycle();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			txtProduct_Contents.setText( "제품 준비 중 입니다." );
			txtProduct_Made.setText( "" );
			txtProduct_Made_By.setText( "" );
			txtProduct_Made_What.setText( "" );
			txtProduct_Price_USALLY.setText( "" );
			txtProduct_Price_SEll.setText( "" );
			txtProduct_Product_Code.setText( "" );
		}
	}
	
	private int MaxColorbCount = 0, MaxLineCount = 8;
	
	private ImageView[] tabImgBtn;
	
	private RelativeLayout[] tabImgBackGround;
	
	private void setColor(){
		rowColor.removeAllViews();
		
		MaxColorbCount = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_DETAIL_LIST_COLOR.size();
		
		if( tabImgBtn != null )
			tabImgBtn = null;
		
		if( tabImgBackGround != null )
			tabImgBackGround = null;
		
		tabImgBtn			= new ImageView[ MaxColorbCount ];
		
		tabImgBackGround	= new RelativeLayout[ MaxColorbCount ];
		
		if( MaxColorbCount > 16 ) {
			MaxColorbCount = 16;
		}
		
		for( int i = 0; i < MaxColorbCount; i++ ){
			
			tabImgBtn[i] 		= new ImageView( mContext );
			tabImgBackGround[i] = new RelativeLayout( mContext );
			
			RelativeLayout.LayoutParams imglinelayout = new RelativeLayout.LayoutParams(
					24, 24);
			
			tabImgBtn[i].setLayoutParams(imglinelayout);
			tabImgBtn[i].setScaleType( ScaleType.FIT_XY );
			
			try {
				IDL.download(mContext, CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_DETAIL_LIST_COLOR.get( i ).colorUrl, tabImgBtn[i], null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			RelativeLayout.LayoutParams ColorImgBacklayout = new RelativeLayout.LayoutParams(
					26, 26);
			
			if( i / MaxLineCount == 0 ){
				ColorImgBacklayout.setMargins( ( 34 * i ), 0, 0, 0);
			} else {
				ColorImgBacklayout.setMargins( ( 34 * ( i % MaxLineCount ) ), 30, 0, 0);
			}
			
			tabImgBackGround[i].setLayoutParams(ColorImgBacklayout);
			tabImgBackGround[i].addView( tabImgBtn[i] );
			tabImgBackGround[i].setGravity( Gravity.CENTER );
			tabImgBackGround[i].setBackgroundColor( Color.WHITE );
			
			tabImgBtn[i].setOnClickListener( this );
			
			rowColor.addView( tabImgBackGround[i] );
		}
	}
	
	private ImageDownloader IDL = new ImageDownloader();
	
	private libJSON_GET GET_LibJSON = new libJSON_GET();
	
	private int verValue = 0, verMax = 1;
	
	private Handler handler = new Handler();
	
	private CipherUtils mCipher = new CipherUtils();
	
	
	class setProDuctInfor extends AsyncTask<Integer, Integer, Integer> {
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
						String[] aaa = new String[5];
						
						aaa[0] = Constance.BEANDCD;
						
						aaa[1] = strProductID;
						
						aaa[2] = Constance.SHOPCD;
						
						long timestamp = System.currentTimeMillis();
						
						aaa[3] = mCipher.encrypt( Constance.SEED , Constance.MAC + "@" + timestamp ); 
						
						aaa[4] = String.valueOf( timestamp ).toString().trim(); 
						
						GET_LibJSON.getPRODUCT_LIST_INFORMATION(  aaa );
						
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
						setColor();
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					try {
						setDetailContents_1();
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					try {
						setContentxArea();
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

	private String strProductID = ""; 
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
		
		ProductType = false;
		
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				new setProDuctInfor().execute();
			}
		}, 100);
		
	}

	public void SetProduct_CD( String strproduct ){
		strProductID = strproduct;
	}
	
	@Override
	public void cancel() {
		// TODO Auto-generated method stub
		super.cancel();
	}
	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		super.dismiss();
	}
	@Override
	public void onBackPressed() {
		dismiss();
		cancel();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
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
			if( v == btnClose ){
				dismiss();
				cancel();
			} else if( v == btnPruductDetail ){
				mPager.setCurrentItem( 0 );
			} else if( v == btnPruductInventory ){
				mPager.setCurrentItem( 1 );
			} else if( v == btnPruductTOP_3 ){
				mPager.setCurrentItem( 2 );
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		try {
			if( tabImgBtn != null ){
				for( int i = 0; i < tabImgBtn.length; i++ ){
					if( v == tabImgBtn[i] ){
						CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_DETAIL_LIST_INVENTS.clear();
//						layout_DetailList.setVisibility( View.VISIBLE );
//						layout_DetailContents.setVisibility( View.GONE );
						ProductType = true;
						if( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_DETAIL_LIST_COLOR.size() > 0 )
							strProductID = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_DETAIL_LIST_COLOR.get( i ).prductCd;
						new setProDuctInfor().execute();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		

	}
}
