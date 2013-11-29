package fnc.salesforce.android.Look_10001;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView.ScaleType;
import fnc.salesforce.android.R;
import fnc.salesforce.android.Activity.Look_10001;
import fnc.salesforce.android.Activity.Promotion_10004;
import fnc.salesforce.android.Catal_10002.ProductItemListAdabter;
import fnc.salesforce.android.Constance.CONTENTS_PAGE_ALL_LIST_CONSTANCE;
import fnc.salesforce.android.Constance.Constance;
import fnc.salesforce.android.LIB.BackPress;
import fnc.salesforce.android.LIB.CipherUtils;
import fnc.salesforce.android.LIB.DownloadFile;
import fnc.salesforce.android.LIB.KumaLog;
import fnc.salesforce.android.LIB.MenuActivity;
import fnc.salesforce.android.LIB.ProductDialog;
import fnc.salesforce.android.LIB.libJSON;
import fnc.salesforce.android.LIB.libJSON_GET;
import fnc.salesforce.android.Look_10001.Look_10001_Sub.setDeviceInfor;
import fnc.salesforce.android.Look_10001.Look_10001_Sub.setThumbNailDown;
import fnc.salesforce.android.Look_10001.Look_10001_Sub.setThumbNailDown_SUB;

public class Look_10001_Sub_Detail extends Activity implements OnClickListener{

	@Override
	public void onBackPressed() {
//		Intent intent = new Intent( mContext, Look_10001.class);
//		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		intent.putExtra("ctgryId", DetailParam[4] );
//		intent.putExtra("brandCd", Constance.BEANDCD );
//		intent.putExtra("ctgryNm", DetailParam[6]);
//		intent.putExtra("Detail_State", "detatil");
//		startActivity(intent);
		finish();
	}

	private RelativeLayout layoutProductList;
	
	private ListView ProductList;
	
	private ImageView imgProduct_btn;
	
	private ProgressDialog pd;
	
	private ProgressBar mProductDialog;
	
	private Context mContext;
	
	private Button btnCfg, btnMenu_Activity;
	
	private MenuActivity mMenu;

	public void setProduct( boolean state ){
		
		if( state ){
			RelativeLayout.LayoutParams imglinelayout = new RelativeLayout.LayoutParams(
					316, RelativeLayout.LayoutParams.MATCH_PARENT);
			imglinelayout.setMargins( 485, 58, 0, 0);

			layoutProductList.setLayoutParams(imglinelayout);
			
			layoutProductList.setAnimation( AnimationUtils.loadAnimation(mContext, R.anim.pdf_left_in) );

		} else {
			Animation aaa = AnimationUtils.loadAnimation(mContext, R.anim.pdf_right_out);
			
			aaa.setAnimationListener( new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
					
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					RelativeLayout.LayoutParams imglinelayout = new RelativeLayout.LayoutParams(
							318, RelativeLayout.LayoutParams.MATCH_PARENT);
					imglinelayout.setMargins( 765, 58, 0, 0);

					layoutProductList.setLayoutParams(imglinelayout);
					
				}
			});
			layoutProductList.startAnimation( aaa );
		}
	}
	
	private String[] DetailParam;
	
	private boolean LastPageState = false;
	
	private TextView txt_lookBookDetail;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView( R.layout.look_10001_detail );
		
		DetailParam = new String[7];
				
		Intent intent = getIntent();

	    if( intent.getStringArrayExtra("param") != null ){
	    	DetailParam = intent.getStringArrayExtra("param");
        }
	    
	    
	    for( int i = 0; i < DetailParam.length; i++ ){
//	    	KumaLog.LogD("test_1"," DetailParam : " + DetailParam[i] );
	    }
		pd = new ProgressDialog( this );
		
		mMenu = new MenuActivity( this, R.style.Transparent);
		
		mContext = this;
		
		layoutProductList 	= (RelativeLayout) findViewById( R.id.layoutProductList );
		
		ProductList 		= (ListView) findViewById( R.id.ProductList );
		
		imgProduct_btn 		= (ImageView) findViewById( R.id.imgProduct_btn );
		
		mProductDialog		= ( ProgressBar ) findViewById( R.id.ProductListDialog );

		btnMenu_Activity    = (Button) findViewById( R.id.btnMenu_Activity );
		
		btnCfg				= ( Button ) findViewById( R.id.btnCfg );
		
		txt_lookBookDetail	= ( TextView ) findViewById( R.id.txt_lookBookDetail );
		
		txt_lookBookDetail.setText("LOOKBOOK");
		
		mProductDialog.setVisibility( View.GONE );
		
		ContentsAreaLayout = null;
		
		PIA = new ProductItemListAdabter( mContext );

		ProductList.setAdapter( PIA );
		
		mPager = (ViewPager) findViewById(R.id.pager);
		
		int maxContentsCount = Integer.parseInt( DetailParam[0] );

		if( maxContentsCount / AllCategoryContents == 0 ){
			MaxPage = 1;
		} else {
			MaxPage = ( maxContentsCount / AllCategoryContents ) + 1;
		}
		
		mPager.setOnPageChangeListener( new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				statrPosition = position;
				if( MaxPage != num ){
					if( position >= maxCount ){
						num++;
						new setDeviceInfor().execute();
					} else {
						if( Prucut_STATE ){
							new setProduct_Information().execute();
						}
					}
				} else {
					if( position >= maxCount ){
						LastPageState = true;
						statrPosition = position - 1;
						mPager.setCurrentItem( statrPosition );
					} else {
						if( Prucut_STATE ){
							if( LastPageState ){
								LastPageState = false;
							} else {
								new setProduct_Information().execute();
							}
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
		
		setLayout();
		
		ID = new ProductDialog( mContext, R.style.Transparent);
		
		BKPA = new BkPagerAdapter( this );
		
		mPager.setAdapter(BKPA);
		
		imgProduct_btn.setOnClickListener( this );
		
		btnMenu_Activity.setOnClickListener( this );
		
		btnCfg.setOnClickListener( this );
		
//		setProduct( false );
		
		try {
			RelativeLayout.LayoutParams imglinelayout = new RelativeLayout.LayoutParams(
					318, RelativeLayout.LayoutParams.MATCH_PARENT);
			imglinelayout.setMargins( 765, 58, 0, 0);

			layoutProductList.setLayoutParams(imglinelayout);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Prucut_STATE = false;
		
		try {
			mPager.setCurrentItem( Integer.parseInt( DetailParam[2] ) );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		ProductList.setOnItemClickListener( new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				ID.SetProduct_CD( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_LIST_VIEW.get(arg2).prductCd );
				ID.show();
			}
		});
	}

	private ProductDialog ID;
	
	private int num = 1, MaxPage = 0;
	
	private boolean PageState = false;

	private int statrPosition = 0;
	
	private BkPagerAdapter BKPA;
	
	private ViewPager mPager;
	
	private RelativeLayout[] ContentsAreaLayout;
	
	int maxCount = 0;
	private void setLayout(){
		
		maxCount = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_LOOKBOOK_CONTENTS_PATH.size();
		
		if( ContentsAreaLayout != null )
			ContentsAreaLayout = null;
		
		ContentsAreaLayout = new RelativeLayout[maxCount];
		
	}

	private class BkPagerAdapter extends PagerAdapter{
		public BkPagerAdapter( Context con) { 
			super(); 
		}

		@Override 
		public int getCount() {
			return maxCount + 1;
		}

		//�������� ����� �䰴ü ��/���
		@Override 
		public Object instantiateItem(View pager, int position)
		{	
			ImageView ContentsImg_Img = null;
			
			
			try {
				ContentsImg_Img 		= new ImageView( mContext );
				
				RelativeLayout.LayoutParams imgContentslayout_Type = new RelativeLayout.LayoutParams(
						760, 991);
				ContentsImg_Img.setLayoutParams( imgContentslayout_Type );
				
				try {
					ContentsImg_Img.setImageBitmap( BitmapFactory.decodeFile( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_LOOKBOOK_CONTENTS_PATH.get( position ).toString())  );
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				ContentsImg_Img.setScaleType( ScaleType.FIT_XY );
				
				((ViewPager)pager).addView(ContentsImg_Img, 0);	
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			return ContentsImg_Img;
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
	
	private Handler handler = new Handler();


	class setProduct_Information extends AsyncTask<Integer, Integer, Integer> {
		@Override
		protected void onPreExecute() {
			
			verMax = 1;
			verValue = 0;
			
			try {
				GET_LibJSON.NetworkDisconnect();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			mProductDialog.setVisibility( View.VISIBLE );
		}
		
		@Override
		protected Integer doInBackground(Integer... params) {
			while (isCancelled() == false) {
				if (verValue < verMax) {
					try {
						String[] parameter = new String[5];
						
						parameter[0] = Constance.BEANDCD;
						
						int ProductPosition = statrPosition % AllCategoryContents;
						
						if( DetailParam[5].equals("ALL") ){
							parameter[1] = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( ProductPosition ).cntntsId;
						} else {
							parameter[1] = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.get( ProductPosition ).cntntsId;
						}
						
						
						parameter[2] = Constance.SHOPCD;
						
						long timestamp = System.currentTimeMillis();
						
						parameter[3] = mCipher.encrypt( Constance.SEED , Constance.MAC + "@" + timestamp ); 
						parameter[4] = String.valueOf( timestamp ).toString().trim(); 
						
						GET_LibJSON.getPRODUCT_LIST( parameter );

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
						mProductDialog.setVisibility( View.GONE );
						
						try {
							CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_LIST_VIEW.clear();
							
							for( int i = 0; i < CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_LIST.size(); i++ ){
								CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_LIST_VIEW.add( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_LIST.get(i) );
							}
							
							PIA.notifyDataSetChanged();
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
	
	private CipherUtils mCipher = new CipherUtils();
	
	private int verValue = 0, verMax = 1;

	private int AllCategoryContents = 30;
	
	private libJSON_GET GET_LibJSON = new libJSON_GET();
	private DownloadFile DLF = new DownloadFile();
	public String FOLDER_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test/";
	
	class setDeviceInfor extends AsyncTask<Integer, Integer, Integer> {
		@Override
		protected void onPreExecute() {
			try {
				CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.clear();
				
				CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.clear();
				
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
						
						aaa[0] = Constance.BEANDCD;
						aaa[1] = DetailParam[4];
						aaa[2] = String.valueOf( num );
						aaa[3] = String.valueOf( AllCategoryContents );
						aaa[4] = "1";
						
						long timestamp = System.currentTimeMillis();
						
						aaa[5] = mCipher.encrypt( Constance.SEED , Constance.MAC + "@" + timestamp ); 
						aaa[6] = String.valueOf( timestamp ).toString().trim(); 
						
						if( DetailParam[5].equals("ALL") ){
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
					if( DetailParam[5].equals("ALL") ){
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
												
						aaa[0] = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( verValue ).brandCd;
						aaa[1] = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( verValue ).cntntsId;	
						
						Down_URL = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( verValue ).dwldCntntsPath;
						strimgNAme = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_PAGE_ALL_LIST.get( verValue ).cntntsFileLc;

						CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_LOOKBOOK_CONTENTS_PATH.add( DLF.Download_HttpFile(Down_URL, Constance.FILEPATH + "/" + aaa[0] + "/" + aaa[1] +"/", strimgNAme) );
						
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
						setLayout();

						BKPA = null;
						
						BKPA = new BkPagerAdapter( mContext );
						
						mPager.setAdapter(BKPA);
						
						mPager.setCurrentItem( statrPosition );
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
						
						aaa[0] = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.get( verValue ).brandCd;
						aaa[1] = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.get( verValue ).cntntsId;	
						
						Down_URL = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.get( verValue ).dwldCntntsPath;
						strimgNAme = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_LIST.get( verValue ).cntntsFileLc;

						CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_LOOKBOOK_CONTENTS_PATH.add( DLF.Download_HttpFile(Down_URL, Constance.FILEPATH + "/" + aaa[0] + "/" + aaa[1] +"/", strimgNAme) );
						
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
					e.printStackTrace();
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
						setLayout();
						
						BKPA = null;
						
						BKPA = new BkPagerAdapter( mContext );
						
						mPager.setAdapter(BKPA);
						
						mPager.setCurrentItem( statrPosition );
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
	private boolean Prucut_STATE = false;
	
	private ProductItemListAdabter PIA;
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
		if( v == imgProduct_btn ) {
			if( !Prucut_STATE ){
				setProduct( true );
				Prucut_STATE = true;
				new setProduct_Information().execute();
			} else {
				setProduct( false );
				Prucut_STATE = false;
			}
		} else if( v == btnCfg ){
//			Intent intent = new Intent( mContext, Look_10001.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			intent.putExtra("ctgryId", DetailParam[4] );
//			intent.putExtra("brandCd", Constance.BEANDCD );
//			intent.putExtra("ctgryNm", DetailParam[6]);
//			intent.putExtra("Detail_State", "detatil");
//			startActivity(intent);
			finish();
		} else if( v == btnMenu_Activity ){
			mMenu.show();
		}
	}
}