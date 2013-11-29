package fnc.salesforce.android.Activity;

import fnc.salesforce.android.LoginPage;
import fnc.salesforce.android.Main_Page;
import fnc.salesforce.android.R;
import fnc.salesforce.android.Constance.CONTENTS_PAGE_ALL_LIST_CONSTANCE;
import fnc.salesforce.android.Constance.Constance;
import fnc.salesforce.android.LIB.BrandActivity;
import fnc.salesforce.android.LIB.CipherUtils;
import fnc.salesforce.android.LIB.DownloadFile;
import fnc.salesforce.android.LIB.HidekeyBoard;
import fnc.salesforce.android.LIB.MenuActivity;
import fnc.salesforce.android.LIB.libJSON;
import fnc.salesforce.android.LIB.libJSON_GET;
import fnc.salesforce.android.PullToList.BottomPullToRefreshView;
import fnc.salesforce.android.PullToList.PullToRefreshView;
import fnc.salesforce.android.PullToList.ShopListAdabter;

import fnc.salesforce.android.PullToList.PullToRefreshView.Listener;
import fnc.salesforce.android.PullToList.PullToRefreshView.MODE;
import android.R.integer;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

public class ShopInformation extends Activity implements OnTouchListener, OnClickListener{
	
	private BottomPullToRefreshView pullView2 = null;
	private ListView listView = null;
	private ShopListAdabter adapter =null;

	private RelativeLayout AreaBtnLayout;
	
	private Context mContext;
	
	private ProgressDialog pd;
	
	private boolean RefreshState = true;
	
	private Button btnShopNameSearch;
	
	private EditText editShopNameSearch;
	
	private ImageView btnMenu_Activity, btnGo_Main, btnBack_Page;
	
	private ImageButton btn_Brand_Activity;
	
	private TextView txt_AdGallery_Logo;
	
	private MenuActivity mMenu;
	
	private BrandActivity mBrand;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_main);
        
        mContext = this;
        
        CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_AREA_LIST_DETAIL.clear();
        
        pd = new ProgressDialog( this );

        pullView2			= (BottomPullToRefreshView)findViewById(R.id.pull_to_refresh2);
        AreaBtnLayout 		= (RelativeLayout) findViewById( R.id.AreaBtnLayout ); 
        
        btnShopNameSearch	= ( Button ) findViewById( R.id.btnShopNameSearch );
        listView 			= (ListView)findViewById(R.id.listView);
        editShopNameSearch	= ( EditText )findViewById( R.id.editShopNameSearch );
        
        btnMenu_Activity    = (ImageView) findViewById( R.id.btnMenu_Activity );
		
		btn_Brand_Activity	= ( ImageButton ) findViewById( R.id.btn_Brand_Activity );
		
		btnGo_Main    		= (ImageView) findViewById( R.id.btnGo_Main );
		btnBack_Page    	= (ImageView) findViewById( R.id.btnBack_Page );
		
		txt_AdGallery_Logo  = (TextView) findViewById( R.id.txt_AdGallery_Logo );
		
        adapter = new ShopListAdabter( this );
        
        mMenu = new MenuActivity( this, R.style.Transparent);
		
		mBrand	= new BrandActivity( this, R.style.Transparent);
		
        btnShopNameSearch.setOnClickListener( this );
        btnMenu_Activity.setOnClickListener( this );
    	btn_Brand_Activity.setOnClickListener( this );
    	
    	
    	btnGo_Main.setOnClickListener( this );
		btnBack_Page.setOnClickListener( this );
		
        try {
        	txt_AdGallery_Logo.setText( Constance.SHOPNAME );
		} catch (Exception e) {
			e.printStackTrace();
		}        
        
        pullView2.setListener(new BottomPullToRefreshView.Listener() {
			@Override
			public void onChangeMode(fnc.salesforce.android.PullToList.BottomPullToRefreshView.MODE mode) {
				Log.w("test_1","pullView2:"+mode);
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
										new setAreaDetailInfor().execute();
									}
								}, 200);
								
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

       listView.setAdapter(adapter);
        
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
				new setAreaInfor().execute();
			}
		}, 200);
    }
    
    
    @Override
	public void onBackPressed() {
    	Intent intent = new Intent( mContext, Main_Page.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		try {
			System.gc();
		} catch (Exception e) {
			e.printStackTrace();
		}
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


	private static class imgAreaBtn_Param {
		static int width = 122;
		static int height = 36;
		static int Margine_Left = 126;
		static int Margine_top = 40;
	};
	
    ImageButton[] tabImgBtn;
	TextView[] txtAreaText;
	private int AllCategoryContents = 0, One_lineCount = 6;
	
	private void setAreaLayout(){
		
		AllCategoryContents = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_AREA_LIST.size() + 1;

		
		tabImgBtn	= new ImageButton[ AllCategoryContents ];
		txtAreaText	= new TextView[ AllCategoryContents ];
				
//		TabLayout = new RelativeLayout( mContext ); 
		
		
		for( int i = 0; i < AllCategoryContents; i++ ){
			tabImgBtn[i] = new ImageButton( mContext );
			txtAreaText[i]  = new TextView( mContext );
			
			RelativeLayout.LayoutParams imgContentslayout_Down = new RelativeLayout.LayoutParams(
					imgAreaBtn_Param.width, imgAreaBtn_Param.height);

			int height_MargineCount =  i / One_lineCount;
			
			int Width_MargineCount =  i % One_lineCount;
			
			int Margine_Left  = 0;
			
			if( Width_MargineCount == 0 ){
				Margine_Left = 0;
			} else {
				Margine_Left = ( imgAreaBtn_Param.Margine_Left * Width_MargineCount );
			}

			imgContentslayout_Down.setMargins( Margine_Left , imgAreaBtn_Param.Margine_top * height_MargineCount , 0, 0);	
			
			tabImgBtn[i].setLayoutParams(imgContentslayout_Down);
			tabImgBtn[i].setOnClickListener( this );
			
			txtAreaText[i].setLayoutParams(imgContentslayout_Down);
			txtAreaText[i].setGravity( Gravity.CENTER );
			txtAreaText[i].setTextColor( Color.parseColor("#ffffff"));
			txtAreaText[i].setTextSize( 15 );
			
			String ctgryNm = "";
			if( i == 0 ){
				txtAreaText[i].setText( "전체" );
			} else {
				ctgryNm = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_AREA_LIST.get( i - 1).areaNm;
				
				txtAreaText[i].setText( ctgryNm );
			}
			
			AreaBtnLayout.addView( tabImgBtn[i] );
			AreaBtnLayout.addView( txtAreaText[i] );
		}
		setChoiceCategory( 0 );
		mSearchName = "";
		mSearchCode = "";
		try {
			new setAreaDetailInfor().execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setChoiceCategory( int position ){
		
		for( int i = 0; i < tabImgBtn.length; i++ ){
			if( position == i ){
				tabImgBtn[i].setBackgroundResource( R.drawable.at_sf_store_btnbg_o );
			} else {
				tabImgBtn[i].setBackgroundResource( R.drawable.at_sf_store_btnbg_n );
			}
		}
	}

	private CipherUtils mCipher = new CipherUtils();
	
	private int verValue = 0, verMax = 1;

	private libJSON_GET GET_LibJSON = new libJSON_GET();

	public String FOLDER_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test/";
	
	class setAreaInfor extends AsyncTask<Integer, Integer, Integer> {
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
						String[] aaa = new String[2];
						long timestamp = System.currentTimeMillis();
						aaa[0] = mCipher.encrypt( Constance.SEED , Constance.MAC + "@" + timestamp ); 
						aaa[1] = String.valueOf( timestamp ).toString().trim();
						
						GET_LibJSON.getSHOP_AREA_LIST( aaa );

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
						pd.dismiss();
						pd.cancel();
						
						setAreaLayout();
						
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
	
	private int num = 1, mMaxResult = 24;
	private String mSearchName = "", mSearchCode = "";
	
	class setAreaDetailInfor extends AsyncTask<Integer, Integer, Integer> {
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
						long timestamp = System.currentTimeMillis();
						aaa[0] = mCipher.encrypt( Constance.SEED , Constance.MAC + "@" + timestamp ); 
						aaa[1] = String.valueOf( timestamp ).toString().trim();
						
						aaa[2] = String.valueOf( num );
						aaa[3] = String.valueOf( mMaxResult );
						aaa[4] = String.valueOf( 1 );
						aaa[5] = mSearchName;
						aaa[6] = mSearchCode;
								
						GET_LibJSON.getSHOP_AREA_LIST_DETAIL( aaa );

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
						pd.dismiss();
						pd.cancel();
						adapter.notifyDataSetChanged();
						
						pullView2.completeRefresh();
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
	/**
	 * @param v
	 * @param event
	 * @return
	 * @see android.view.View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		pullView2.touchDelegate(v, event);
		return false;
	}

	@Override
	public void onClick(View v) {
		try {
			if( tabImgBtn != null ){
				for( int i = 0; i < AllCategoryContents; i++){
					if( v == tabImgBtn[i] ){
						mSearchName = "";
						editShopNameSearch.setText("");
						setChoiceCategory( i );
						CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_AREA_LIST_DETAIL.clear();
						if( i == 0 ){
							mSearchName = "";
							mSearchCode = "";
						} else {
							mSearchCode = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_AREA_LIST.get( i - 1).areaCd;
						}
						
						num = 1;
						
						try {
							new setAreaDetailInfor().execute();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if( v == btnShopNameSearch ){
			
			HidekeyBoard.KeyboardHide(mContext, btnShopNameSearch.getWindowToken() );
			
			CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_AREA_LIST_DETAIL.clear();
			try {
				mSearchName = editShopNameSearch.getText().toString().trim();
			} catch (Exception e) {
				mSearchName = "";
				e.printStackTrace();
			}

			num = 1;
			
			try {
				new setAreaDetailInfor().execute();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if( v == btnMenu_Activity ){
			mMenu.show();
		} else if( v == btn_Brand_Activity ) {
			mBrand.show();
		} else if( v == btnGo_Main ) {
			Intent intent = new Intent( mContext, Main_Page.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		} else if( v == btnBack_Page ) {
			try {
				Intent intent = new Intent( mContext, Main_Page.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
