package fnc.salesforce.android.Repair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import fnc.salesforce.android.R;
import fnc.salesforce.android.Constance.CONTENTS_PAGE_ALL_LIST_CONSTANCE;
import fnc.salesforce.android.Constance.Constance;
import fnc.salesforce.android.LIB.CipherUtils;
import fnc.salesforce.android.LIB.HidekeyBoard;
import fnc.salesforce.android.LIB.libJSON_GET;
import fnc.salesforce.android.PullToList.BottomPullToRefreshView;
import fnc.salesforce.android.PullToList.PullToRefreshView;
import fnc.salesforce.android.PullToList.PullToRefreshView.Listener;
import fnc.salesforce.android.PullToList.PullToRefreshView.MODE;

public class Repair_Cost implements OnTouchListener, OnClickListener{
	
	private BottomPullToRefreshView pullView2 = null;
	private ListView listView = null;
	private RepairAdabter adapter =null;

	private RelativeLayout AreaBtnLayout;
	
	private Context mContext;
	
	private ProgressDialog pd;
	
	private boolean RefreshState = true;
	
	private Button btnShopNameSearch;
	
	private EditText editShopNameSearch;
			
	LayoutInflater inflater;
	
	private RelativeLayout layoutRepairType, layoutRepairProduct, layoutRepairProduct_Third;
	
	public Repair_Cost(Context context){
		mContext = context;
		
		pd = new ProgressDialog( context );
		
		inflater = ((Activity) mContext).getLayoutInflater();
	}
	
	View mRepairview = null;
    /** Called when the activity is first created. */
	public View setRepair_AllView(){
		 mRepairview = null;
		 CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_REPAIR_RESULT_LIST.clear();
        CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_AREA_LIST_DETAIL.clear();
        
        pd = new ProgressDialog( mContext );
        
        mRepairview = inflater.inflate(R.layout.repair_cost, null);
        
        pullView2			= (BottomPullToRefreshView) mRepairview.findViewById(R.id.pull_to_refresh2);
        
        AreaBtnLayout 		= (RelativeLayout) mRepairview.findViewById( R.id.AreaBtnLayout ); 
        
        layoutRepairType			= (RelativeLayout) mRepairview.findViewById( R.id.layoutRepairType );
        layoutRepairProduct			= (RelativeLayout) mRepairview.findViewById( R.id.layoutRepairProduct );
        layoutRepairProduct_Third	= (RelativeLayout) mRepairview.findViewById( R.id.layoutRepairProduct_Third );
        
        btnShopNameSearch	= ( Button ) mRepairview.findViewById( R.id.btnShopNameSearch );
        listView 			= (ListView) mRepairview.findViewById(R.id.listView);
        editShopNameSearch	= ( EditText ) mRepairview.findViewById( R.id.editShopNameSearch );

        adapter = new RepairAdabter( mContext );
        		
        btnShopNameSearch.setOnClickListener( this );
        
        
        pullView2.setListener(new BottomPullToRefreshView.Listener() {
			@Override
			public void onChangeMode(fnc.salesforce.android.PullToList.BottomPullToRefreshView.MODE mode) {
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
										new setRepair_Search_Infor().execute();
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

        setRepair_Type();
        
        return mRepairview;
    }
	
	public View getView(){
		return mRepairview;
	}
	
	// 메인 좌측 메뉴 Param
	private static class mRepair_Product_Type_Param {
		static int width = 140;
		static int height = 30;
		static int Count = 1;
		static int Margine_top = 30;
		static int Margine_Left = 5;
	};
	
	
	private TextView[] mRepair_Product_Type_txt;

	private void setRepair_Goods(){
		layoutRepairProduct.removeAllViews();
		
		int productCodeSize = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_REPAIR_GOODS_LIST.size() + 1;
		
		if( mRepair_Product_Type_txt == null ){
			mRepair_Product_Type_txt		= new TextView[ productCodeSize ];
		} else {
			mRepair_Product_Type_txt = null;
			
			mRepair_Product_Type_txt		= new TextView[ productCodeSize ];
		}
		
		for( int i = 0; i < productCodeSize; i++ ){
			
			mRepair_Product_Type_txt[i] 		= new TextView( mContext );

			RelativeLayout.LayoutParams imgContentslayout_Back = new RelativeLayout.LayoutParams(
					mRepair_Product_Type_Param.width, mRepair_Product_Type_Param.height);

			int height_MargineCount =  i / mRepair_Product_Type_Param.Count;

			int Margine_Left  = 0;
			
			Margine_Left = mRepair_Product_Type_Param.Margine_Left;
			
			
			imgContentslayout_Back.setMargins( Margine_Left , ( mRepair_Product_Type_Param.Margine_top * height_MargineCount ) + 5 , 0, 0);
			
			try {
				if( i == 0 ){
					mRepair_Product_Type_txt[i].setText( "전체" );
				} else {
					mRepair_Product_Type_txt[i].setText( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_REPAIR_GOODS_LIST.get( i - 1 ).rpairsGoodsNm );
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			mRepair_Product_Type_txt[i].setOnClickListener( this );
			
			mRepair_Product_Type_txt[i].setLayoutParams(imgContentslayout_Back);
			mRepair_Product_Type_txt[i].setGravity( Gravity.CENTER_VERTICAL );
			mRepair_Product_Type_txt[i].setTextSize( 15 );
			mRepair_Product_Type_txt[i].setTextColor(Color.WHITE );
			mRepair_Product_Type_txt[i].setSingleLine();

			layoutRepairProduct.addView( mRepair_Product_Type_txt[i] );
		}
	}
	
	private void setRepair_Goods_Choice( int position ){
		for( int i = 0; i < mRepair_Product_Type_txt.length; i++ ){
			if( i == position ){
				mRepair_Product_Type_txt[ i ].setTextColor(Color.parseColor("#44b6ce") );
			} else {
				mRepair_Product_Type_txt[i].setTextColor(Color.parseColor("#ffffff") );
			}
		}
	}
	
	
	
	// 메인 좌측 메뉴 Param
	private static class Repair_Type {
		static int width = 140;
		static int height = 30;
		static int Count = 1;
		static int Margine_top = 30;
		static int Margine_Left = 5;
	};
	
	
	private TextView[] mRepair_Type_txt;

	private void setRepair_Type(){
		layoutRepairType.removeAllViews();
		
		int productCodeSize = 2;
		
		if( mRepair_Type_txt == null ){
			mRepair_Type_txt		= new TextView[ productCodeSize ];
		} else {
			mRepair_Type_txt = null;
			
			mRepair_Type_txt		= new TextView[ productCodeSize ];
		}
		
		for( int i = 0; i < productCodeSize; i++ ){
			
			mRepair_Type_txt[i] 		= new TextView( mContext );

			RelativeLayout.LayoutParams imgContentslayout_Back = new RelativeLayout.LayoutParams(
					Repair_Type.width, Repair_Type.height);

			int height_MargineCount =  i / Repair_Type.Count;

			int Margine_Left  = 0;
			
			Margine_Left = Repair_Type.Margine_Left;
			
			
			imgContentslayout_Back.setMargins( Margine_Left , ( Repair_Type.Margine_top * height_MargineCount ) + 5 , 0, 0);
			
			try {
				if( i == 0 ){
					mRepair_Type_txt[i].setText( "일반수선" );
				} else {
					mRepair_Type_txt[i].setText( "판매시점수선" );
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			mRepair_Type_txt[i].setOnClickListener( this );
			
			mRepair_Type_txt[i].setLayoutParams(imgContentslayout_Back);
			mRepair_Type_txt[i].setGravity( Gravity.CENTER_VERTICAL );
			mRepair_Type_txt[i].setTextSize( 15 );
			mRepair_Type_txt[i].setTextColor(Color.WHITE );
			mRepair_Type_txt[i].setSingleLine();

			layoutRepairType.addView( mRepair_Type_txt[i] );
		}
	}
	
	private void setRepair_Type_Choice( int position ){
		
		layoutRepairProduct.removeAllViews();
		layoutRepairProduct_Third.removeAllViews();
		
		for( int i = 0; i < mRepair_Type_txt.length; i++ ){
			if( i == position ){
				mRepair_Type_txt[ i ].setTextColor(Color.parseColor("#e3cf2a") );
			} else {
				mRepair_Type_txt[i].setTextColor(Color.parseColor("#ffffff") );
			}
		}
	}
	
	// 메인 좌측 메뉴 Param
	private static class layoutRepairProduct_Third_Param {
		static int width = 140;
		static int height = 30;
		static int Count = 3;
		static int Margine_top = 30;
		static int Margine_Left = 156;
	};

	
	private TextView[] mRepair_species_txt;

	private void setRepair_Species(){
		layoutRepairProduct_Third.removeAllViews();
		
		int productCodeSize = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_REPAIR_SPECIES_LIST.size() + 1;
		
		if( mRepair_species_txt == null ){
			mRepair_species_txt		= new TextView[ productCodeSize ];
		} else {
			mRepair_species_txt = null;
			
			mRepair_species_txt		= new TextView[ productCodeSize ];
		}
		
		for( int i = 0; i < productCodeSize; i++ ){
			
			mRepair_species_txt[i] 		= new TextView( mContext );

			RelativeLayout.LayoutParams imgContentslayout_Back = new RelativeLayout.LayoutParams(
					layoutRepairProduct_Third_Param.width, layoutRepairProduct_Third_Param.height);

			int height_MargineCount =  i / layoutRepairProduct_Third_Param.Count;
			
			int Width_MargineCount =  i % layoutRepairProduct_Third_Param.Count;
			
			int Margine_Left  = 0;
			
			Margine_Left = ( layoutRepairProduct_Third_Param.Margine_Left * Width_MargineCount ) + 7;
			
			
			imgContentslayout_Back.setMargins( Margine_Left , ( layoutRepairProduct_Third_Param.Margine_top * height_MargineCount ) + 5 , 0, 0);
			
			try {
				if( i == 0 ){
					mRepair_species_txt[i].setText( "전체" );
				} else {
					mRepair_species_txt[i].setText( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_REPAIR_SPECIES_LIST.get( i - 1 ).rpairsSpeciesNm );
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			mRepair_species_txt[i].setOnClickListener( this );
			
			mRepair_species_txt[i].setLayoutParams(imgContentslayout_Back);
			mRepair_species_txt[i].setGravity( Gravity.CENTER_VERTICAL );
			mRepair_species_txt[i].setTextSize( 15 );
			mRepair_species_txt[i].setTextColor(Color.WHITE );
			mRepair_species_txt[i].setSingleLine();

			layoutRepairProduct_Third.addView( mRepair_species_txt[i] );
		}
	}
	
	private void setRepair_Species_Choice( int position ){
		for( int i = 0; i < mRepair_species_txt.length; i++ ){
			if( i == position ){
				mRepair_species_txt[ i ].setTextColor(Color.parseColor("#89bd50") );
			} else {
				mRepair_species_txt[i].setTextColor(Color.parseColor("#ffffff") );
			}
		}
	}

	private CipherUtils mCipher = new CipherUtils();
	
	private int verValue = 0, verMax = 1;

	private libJSON_GET GET_LibJSON = new libJSON_GET();

	public String FOLDER_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test/";
	
	private int num = 1, mMaxResult = 23;
	private String mSearchName = "", mSearchCode = "";
	
	class setRepair_GoodsInfor extends AsyncTask<Integer, Integer, Integer> {
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
						String[] aaa = new String[4];
						long timestamp = System.currentTimeMillis();
						aaa[0] = mCipher.encrypt( Constance.SEED , Constance.MAC + "@" + timestamp ); 
						aaa[1] = String.valueOf( timestamp ).toString().trim();
						
						aaa[2] = Constance.BEANDCD;
						aaa[3] = Type_Code;
						
						GET_LibJSON.getREPAIR_GOODS_LIST( aaa );

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
						
						setRepair_Goods();
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
	
	class setRepair_SpeciesInfor extends AsyncTask<Integer, Integer, Integer> {
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
						String[] aaa = new String[5];
						long timestamp = System.currentTimeMillis();
						aaa[0] = mCipher.encrypt( Constance.SEED , Constance.MAC + "@" + timestamp ); 
						aaa[1] = String.valueOf( timestamp ).toString().trim();
						
						aaa[2] = Constance.BEANDCD;
						aaa[3] = Type_Code;
						aaa[4] = Goods_Code;
						
						GET_LibJSON.getREPAIR_SPECIES_LIST( aaa );

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
						
						setRepair_Species();
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
	
	
	class setRepair_Search_Infor extends AsyncTask<Integer, Integer, Integer> {
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
						
						String[] aaa = new String[10];
						
						aaa[0] = String.valueOf( num ); 
						aaa[1] = String.valueOf( mMaxResult );
						aaa[2] = "1"; 
						
						long timestamp = System.currentTimeMillis();
						aaa[3] = mCipher.encrypt( Constance.SEED , Constance.MAC + "@" + timestamp ); 
						aaa[4] = String.valueOf( timestamp ).toString().trim();
						
						aaa[5] = Type_Code;
						aaa[6] = Goods_Code;
						aaa[7] = Species_Code;
						aaa[8] = Constance.BEANDCD;
						aaa[9] = mSearch_KeyWord;
								
						GET_LibJSON.getREPAIR_RESULT_LIST( aaa );

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

	private String Type_Code = "", Goods_Code = "", Species_Code = "", mSearch_KeyWord = "";
	
	@Override
	public void onClick(View v) {
		
		try {
			if( mRepair_Type_txt != null ){
				for( int i = 0; i < mRepair_Type_txt.length; i++){ 
					if( v == mRepair_Type_txt[i] ){
						setRepair_Type_Choice( i );
						if( i == 0 ){
							Type_Code = "0";
						} else if( i == 1 ){
							Type_Code = "1";
						}
						new setRepair_GoodsInfor().execute();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			if( mRepair_Product_Type_txt != null ){
				for( int i = 0; i < mRepair_Product_Type_txt.length; i++){ 
					if( v == mRepair_Product_Type_txt[i] ){
						setRepair_Goods_Choice( i );
						
						if( i == 0 ){
							mSearch_KeyWord = "";
							editShopNameSearch.setText("");
							num = 1;
							CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_REPAIR_RESULT_LIST.clear();
							
							layoutRepairProduct_Third.removeAllViews();
							
							Species_Code = "";
							Goods_Code = "";
							new setRepair_Search_Infor().execute();
						} else {
							Goods_Code = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_REPAIR_GOODS_LIST.get( i - 1 ).rpairsGoodsCd;
							new setRepair_SpeciesInfor().execute();
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			if( mRepair_species_txt != null ){
				for( int i = 0; i < mRepair_species_txt.length; i++){ 
					if( v == mRepair_species_txt[i] ){
						num = 1;
						
						CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_REPAIR_RESULT_LIST.clear();
						
						mSearch_KeyWord = "";
						
						editShopNameSearch.setText("");
						
						setRepair_Species_Choice( i );
						
						if( i == 0 ){
							Species_Code = "";
						} else {
							Species_Code = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_REPAIR_SPECIES_LIST.get( i - 1 ).rpairsSpeciesCd;
						}
						
						new setRepair_Search_Infor().execute();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if( v == btnShopNameSearch ){
			num = 1;
			HidekeyBoard.KeyboardHide(mContext, btnShopNameSearch.getWindowToken() );
			
			CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_REPAIR_RESULT_LIST.clear();
			
			mSearch_KeyWord = editShopNameSearch.getText().toString().trim();
			
			new setRepair_Search_Infor().execute();
		}
	}
}
