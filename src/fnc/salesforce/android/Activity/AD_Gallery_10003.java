/*
 * Copyright 2012 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fnc.salesforce.android.Activity;

import java.util.regex.Pattern;

import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.ErrorReason;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.PlayerStateChangeListener;
import com.google.android.youtube.player.YouTubePlayerView;

import fnc.salesforce.android.LoginPage;
import fnc.salesforce.android.Main_Page;
import fnc.salesforce.android.R;
import fnc.salesforce.android.AD_10003.AD_GalleryMain;
import fnc.salesforce.android.AD_10003.DeveloperKey;
import fnc.salesforce.android.AD_10003.MediaCutView;
import fnc.salesforce.android.AD_10003.TV_CutView;
import fnc.salesforce.android.Constance.CONTENTS_PAGE_ALL_LIST_CONSTANCE;
import fnc.salesforce.android.Constance.Constance;
import fnc.salesforce.android.LIB.BackPress;
import fnc.salesforce.android.LIB.BrandActivity;
import fnc.salesforce.android.LIB.CipherUtils;
import fnc.salesforce.android.LIB.ProductDialog;
import fnc.salesforce.android.LIB.KumaLog;
import fnc.salesforce.android.LIB.MenuActivity;
import fnc.salesforce.android.LIB.libJSON_GET;
import fnc.salesforce.android.LIB.libUDID;
import fnc.salesforce.android.R.layout;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * A simple YouTube Android API demo application demonstrating the use of {@link YouTubePlayer}
 * programmatic controls.
 */
public class AD_Gallery_10003 extends YouTubeFailureRecoveryActivity implements OnClickListener{

	ProgressDialog pd;
	
	private Context mContext;
	
	private MediaCutView MCV;
	
	private AD_GalleryMain AGM;
	
	private TV_CutView TV_View;
	
	private GestureDetector GD;
	
	private RelativeLayout layoutContetsView;
	
	private ImageButton btn_Brand_Activity;
	
	private libUDID lUDID = new libUDID();
	
	private static final String KEY_CURRENTLY_SELECTED_ID = "currentlySelectedId";

	private YouTubePlayerView youTubePlayerView;
	private YouTubePlayer mPlayer;

	private String currentlySelectedId;
	
	private ImageView btnMenu_Activity, btn_Brand_LogOut;
	
	private String[] arrCategory_ID = new String[2];
	
	private String strCategoryID = "", strBrandCD = "";;
	
	private LinearLayout layoutCategoryView, layout_Tabbar;
  	
	private MenuActivity mMenu;
	
	private BrandActivity mBrand;
	
	private AlertDialog.Builder dialogBuilder;
	
	private TextView txtCategoryName, txt_AdGallery_Logo;
	
	private ImageView btnGo_Main, btnBack_Page;
	
	private HorizontalScrollView subCategory;
	@Override
  	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	    setContentView(R.layout.ad_galler_all_main);
//	    getWindow().setFlags( WindowManager.LayoutParams.FLAG_BLUR_BEHIND, WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
	    
	    Constance.arrTransInformation[0] = "null";
		Constance.arrTransInformation[1] = "null";
		
	    
		try {
			lUDID.Check_UDID( this );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		dialogBuilder = new AlertDialog.Builder(this);
		
		mMenu = new MenuActivity( this, R.style.Transparent);
		
		mBrand	= new BrandActivity( this, R.style.Transparent);
		
		Intent intent = getIntent();
		String strCategoryName = "";
	    if( intent.getStringExtra("brandCd") != null ){
	    	arrCategory_ID[0] = intent.getStringExtra("ctgryId");
	    	arrCategory_ID[1] = intent.getStringExtra("brandCd");
	    	strCategoryName = intent.getStringExtra("ctgryNm");
        }

	    strCategoryID  	= arrCategory_ID[0];
	    strBrandCD  	= arrCategory_ID[1];
	    
		MCV 	= new MediaCutView( this );
		AGM		= new AD_GalleryMain( this );
		TV_View	= new TV_CutView( this );
		
		layoutContetsView	= ( RelativeLayout ) findViewById( R.id.layoutContetsView );
		
		layoutCategoryView 	= (LinearLayout) findViewById( R.id.layoutCategoryView );
		
//		btn_AdGallery_All	= ( ImageButton ) findViewById( R.id.btn_AdGallery_All );
//		btn_AdGallery_TV	= ( ImageButton ) findViewById( R.id.btn_AdGallery_TV );
//		btn_AdGallery_Media	= ( ImageButton ) findViewById( R.id.btn_AdGallery_Media );
		btn_Brand_Activity	= ( ImageButton ) findViewById( R.id.btn_Brand_Activity );
		
		btn_Brand_LogOut    = (ImageView) findViewById( R.id.btn_Brand_LogOut );
		txt_AdGallery_Logo  = (TextView) findViewById( R.id.txt_AdGallery_Logo );
		btnMenu_Activity    = (ImageView) findViewById( R.id.btnMenu_Activity );
		
		btnGo_Main    		= (ImageView) findViewById( R.id.btnGo_Main );
		btnBack_Page    	= (ImageView) findViewById( R.id.btnBack_Page );
		
		txtCategoryName    = (TextView) findViewById( R.id.txtCategoryName );
		
		subCategory			= ( HorizontalScrollView ) findViewById( R.id.subCategory );
		
		layout_Tabbar 		= (LinearLayout) findViewById( R.id.layout_Tabbar );
		
		txtCategoryName.setText( strCategoryName );
		
		
		try {
        	txt_AdGallery_Logo.setText( Constance.SHOPNAME );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		mContext = this;
		
		pd = new ProgressDialog( this );
	
//		btn_AdGallery_All.setOnClickListener( this );
//		btn_AdGallery_TV.setOnClickListener( this );
//		btn_AdGallery_Media.setOnClickListener( this );
		btnMenu_Activity.setOnClickListener( this );
		
		btn_Brand_Activity.setOnClickListener( this );
		btn_Brand_LogOut.setOnClickListener( this );

		btnGo_Main.setOnClickListener( this );
		btnBack_Page.setOnClickListener( this );
		
		
		
		BackPress.setDetailPage(this, "getPageCategory_Trans", 0);
		BackPress.setDetailPage(this, "YouTubePlay", 1);
		BackPress.setDetailPage(this, "YouTubePlay_Stop", 2);
		BackPress.setDetailPage(this, "getPageContents_Trans", 3);
		
		
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {

				new setCategoryInfor().execute();
			}
		}, 200);
		
	
	  }
	
	private void setCategory( boolean state){
		RelativeLayout.LayoutParams imglinelayout = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		if( state ){
			subCategory.setVisibility( View.VISIBLE );
			imglinelayout.setMargins( 0, 230, 0, 0);
		} else {
			subCategory.setVisibility( View.INVISIBLE );
			imglinelayout.setMargins( 0, 176, 0, 0);
		}
		
		layoutContetsView.setLayoutParams(imglinelayout);
	}
	
	  @Override
	  public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
	      boolean wasRestored) {
	    this.mPlayer = player;
	    
	    player.setPlayerStateChangeListener( new PlayerStateChangeListener() {
			
			@Override
			public void onVideoStarted() {
			}
			
			@Override
			public void onVideoEnded() {
				mPlayer.play();
			}
			
			@Override
			public void onLoading() {			
			}
			
			@Override
			public void onLoaded(String arg0) {
				mPlayer.play();
			}
			
			@Override
			public void onError(ErrorReason arg0) {
				
			}
			
			@Override
			public void onAdStarted() {
				
			}
		});
	    if (!wasRestored) {
	      playVideoAtSelection();
	    }
	  }
	
	  @Override
	  protected YouTubePlayer.Provider getYouTubePlayerProvider() {
	    return youTubePlayerView;
	  }
	
	  public void playVideoAtSelection() {

		  try {
			  if( Constance.YouTuBe_URL.length() > 2 ){
				  mPlayer.cueVideo( Constance.YouTuBe_URL );
			  }
		  } catch (Exception e) {
			e.printStackTrace();
		  }
		  
	  }
	
	  @Override
	  protected void onSaveInstanceState(Bundle state) {
	    super.onSaveInstanceState(state);
	    state.putString(KEY_CURRENTLY_SELECTED_ID, currentlySelectedId);
	  }
	
	  @Override
	  protected void onRestoreInstanceState(Bundle state) {
	    super.onRestoreInstanceState(state);
	    currentlySelectedId = state.getString(KEY_CURRENTLY_SELECTED_ID);
	  }
	  
	  public void YouTubePlay_Stop(int is){
		  mPlayer.pause();
	  }
	  
	  public void YouTubePlay(int is){
		  try {
			  playVideoAtSelection();
		  } catch (Exception e) {
			e.printStackTrace();
		  }
		  
	  }
	  
	  public void getPageCategory_Trans(int is){

		  String upCategory = "null";
		  
		  for( int j = 0; j < CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_CATEGORY.size(); j++){
			  if( Constance.arrTrans_UP_Information[3].equals( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_CATEGORY.get( j ).ctgryNm ) ) {
				  upCategory = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_CATEGORY.get( j ).ctgryId;
				  setChoiceCategory( j + 1 );
			  }
		  }
		  		  
		if( Constance.arrTrans_UP_Information[1].equals("010003") ){
			
			setCategory( false );
			
			layoutContetsView.removeAllViews();
			
			if( youTubePlayerView != null )
				youTubePlayerView = null;
			
			Constance.YouTuBe_URL = "";
			
			youTubePlayerView = new YouTubePlayerView(mContext);
			
			try {
				if( AGM != null)  
					AGM.DestroyView();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			layoutContetsView.addView( TV_View.setTV_CutView( strBrandCD, upCategory, strCategoryID, Constance.arrTrans_UP_Information[0], youTubePlayerView ) );
			
			youTubePlayerView.initialize( DeveloperKey.DEVELOPER_KEY, (OnInitializedListener) mContext);
		} else if( Constance.arrTrans_UP_Information[1].equals("010001") ){
			
		}
	}
	  
  public void getPageContents_Trans(int is){
	  
	  String upCategory = "null";

	  for( int j = 0; j < CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_CATEGORY.size(); j++){
		  if( Constance.arrTrans_UP_Information[3].equals( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_CATEGORY.get( j ).ctgryNm ) ) {
			  upCategory = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_CATEGORY.get( j ).ctgryId;
			  setChoiceCategory( j + 1 );
		  }
	  }
	  
	  setCategory( false );
	  
	  layoutContetsView.removeAllViews();
	  
		if( youTubePlayerView != null )
			youTubePlayerView = null;
		
		Constance.YouTuBe_URL = "";
		
		youTubePlayerView = new YouTubePlayerView(mContext);
		
		try {
			if( AGM != null)  
				AGM.DestroyView();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		layoutContetsView.addView( TV_View.setTV_CutView( strBrandCD, upCategory, strCategoryID, Constance.arrTrans_UP_Information[2], youTubePlayerView ) );
		
		youTubePlayerView.initialize( DeveloperKey.DEVELOPER_KEY, (OnInitializedListener) mContext);
	}
  
  private int PagePosition = 0;
  
  private void setChoiceCategory( int position ){
	  
	  PagePosition = position;
	  
	  for( int i = 0; i < txtMainTabView.length; i++ ){
		  if( position == i ){
			  txtMainTabView[i].setTextColor( Color.parseColor( "#f45947" ) );
			  mainTabImg[i].setBackgroundResource( R.drawable.at_sub_tabbullet_o );
		  } else {
			  txtMainTabView[i].setTextColor( Color.parseColor( "#ffffff" ) );
			  mainTabImg[i].setBackgroundResource( R.drawable.at_sub_tabbullet_n );
		  }
	  }
  }
	  
  private int MaxMainTabCount = 0;
  private ImageView[] mainTabImg;
  private RelativeLayout[] MainCategoryAreaLayout;
  TextView[] txtMainTabView;
  
  private void setCategory(){
	  MaxMainTabCount = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_CATEGORY.size() + 1;
	  mainTabImg					= new ImageView[ MaxMainTabCount ];
	  MainCategoryAreaLayout		= new RelativeLayout[ MaxMainTabCount ];
	  txtMainTabView				= new TextView[ MaxMainTabCount ];
	
		for( int i = 0; i < MaxMainTabCount; i++ ){
			mainTabImg[i] = new ImageButton( mContext );
			txtMainTabView[i] = new TextView( mContext );
			MainCategoryAreaLayout[i] = new RelativeLayout ( mContext );
				
			
						
			RelativeLayout.LayoutParams imglinelayout = new RelativeLayout.LayoutParams(
								9, 9);
			imglinelayout.setMargins( 1, 12, 0, 0);
			
			mainTabImg[i].setLayoutParams(imglinelayout);
			mainTabImg[i].setBackgroundResource( R.drawable.at_sub_tabbullet_n );
			
			String ctgryNm = "";
			
			int mCategoryWidth = 0;
			if( i == 0 ){
				txtMainTabView[i].setText( "전체" );
				mCategoryWidth = 35;
			} else {
				
				ctgryNm = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_CATEGORY.get( i - 1).ctgryNm;
				
				txtMainTabView[i].setText( ctgryNm );
				
				mCategoryWidth = ( 9 * validateInputString( ctgryNm ) );
			}
			
			
			
			RelativeLayout.LayoutParams txtlayout = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, 32);
			txtlayout.setMargins( 15, 0, 0, 0);
			
			txtMainTabView[i].setLayoutParams(txtlayout);
			txtMainTabView[i].setGravity( Gravity.CENTER );
			
			MainCategoryAreaLayout[i].addView( mainTabImg[i] );
			MainCategoryAreaLayout[i].addView( txtMainTabView[i] );
			
			RelativeLayout.LayoutParams Contentslayout = new RelativeLayout.LayoutParams(
					mCategoryWidth + 20, 32);
			MainCategoryAreaLayout[i].setLayoutParams(Contentslayout);
			
			MainCategoryAreaLayout[i].setGravity( Gravity.CENTER );
			
			txtMainTabView[i].setOnClickListener( this );
			
			layoutCategoryView.addView( MainCategoryAreaLayout[i] );
		}
		
		txtMainTabView[0].setTextColor( Color.parseColor( "#f45947" ) );
		mainTabImg[0].setBackgroundResource( R.drawable.at_sub_tabbullet_o );
		setCategory( false );
		layoutContetsView.removeAllViews();
  }
  
  
  private int validateInputString( String workType) {
	int TextLeng = 0;
	try {
		for( int i = 0; i < workType.length(); i++ ){
			if(Pattern.matches("^[ㄱ-ㅎㅏ-ㅣ가-�R]+$", String.valueOf( workType.charAt( i ) ) ) ){  //한글일경우의 패턴 체크
				TextLeng = TextLeng + 2;
			} else {
				TextLeng = TextLeng + 1;
			}
		}
	} catch (Exception e) {
		TextLeng = 50;
		e.printStackTrace();
	}
	return TextLeng;
 }
  private CipherUtils mCipher = new CipherUtils();
  private int verValue = 0, verMax = 0; 
  private libJSON_GET GET_LibJSON = new libJSON_GET();
  private Handler handler = new Handler();
  
  
  
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
						
						aaa[1] = strCategoryID;
						
						long timestamp = System.currentTimeMillis();
						
						aaa[2] = mCipher.encrypt( Constance.SEED , Constance.MAC + "@" + timestamp ); 
						
						aaa[3] = String.valueOf( timestamp ).toString().trim(); 
						
						GET_LibJSON.getCATEGORY_LIST(  aaa );
						
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
					setCategory();
					
					if( AGM == null)
						AGM = new AD_GalleryMain( mContext );
					
					layoutContetsView.addView( AGM.setAD_GalleryView(strBrandCD,  strCategoryID, "ALL" ) );
					
					pd.dismiss();
					pd.cancel();
				}
			});
		}
		
		@Override
		protected void onCancelled() {
		}
	}

  class setCategoryInfor_SUB extends AsyncTask<Integer, Integer, Integer> {
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
						
						aaa[1] = strCategoryID;
						
						long timestamp = System.currentTimeMillis();
						
						aaa[2] = mCipher.encrypt( Constance.SEED , Constance.MAC + "@" + timestamp ); 
						
						aaa[3] = String.valueOf( timestamp ).toString().trim(); 
						
						GET_LibJSON.getCATEGORY_LIST_SUB(  aaa );
						
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
					pd.dismiss();
					pd.cancel();
				}
			});
		} 
		
		@Override
		protected void onCancelled() {
		}
	}
	
  private int MaxTabbCount = 0;
	
	RelativeLayout[] tabImgBtn;
	
	View[] viewTerm;
	
	private void setScrollViewTAbBar(){
		layout_Tabbar.removeAllViews();
		
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
				
				mCategoryWidth = ( 11 * validateInputString ( ctgryNm.toString().trim() ) ) + 10;
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
			layout_Tabbar.addView( tabImgBtn[i] );
			layout_Tabbar.addView( viewTerm[i] );
			
		}
		tabImgBtn[0].setBackgroundResource( R.drawable.at_sub_tabbg_o );
	}
  
	@Override
	public void onClick(View v) {
		if( txtMainTabView != null ){
			for( int i = 0; i < MaxMainTabCount; i++){
				if( v == txtMainTabView[i] ){
					setChoiceCategory( i );
					if( i == 0 ){
						txtMainTabView[0].setTextColor( Color.parseColor( "#f45947" ) );
						mainTabImg[0].setBackgroundResource( R.drawable.at_sub_tabbullet_o );
						
						strBrandCD = arrCategory_ID[1];
						strCategoryID = arrCategory_ID[0];
						
						layoutContetsView.removeAllViews();
						setCategory( false );
						if( AGM == null)
							AGM = new AD_GalleryMain( mContext );
						
						layoutContetsView.addView( AGM.setAD_GalleryView(strBrandCD,  strCategoryID, "ALL" ) );
					} else {
						strBrandCD = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_CATEGORY.get( i - 1 ).brandCd;
						strCategoryID = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_CATEGORY.get( i - 1 ).ctgryId;

						setCategory( true );
						layoutContetsView.removeAllViews();
						
						if( AGM == null)
							AGM = new AD_GalleryMain( mContext );
						
						layoutContetsView.addView( AGM.setAD_GalleryView(strBrandCD,  strCategoryID, "SUB" ) );
						
						new setCategoryInfor_SUB().execute();
						
//						String ScreenType =  CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_CATEGORY.get( i - 1 ).scrinTyCd;
//						
//						String upCategory = "null";
//						upCategory = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_CATEGORY.get( i - 1 ).ctgryId;
//						
//						if( ScreenType.equals("010003") ){
//							try {
//								if( AGM != null)  
//									AGM.DestroyView();
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//							
//							
//							layoutContetsView.removeAllViews();
//							if( youTubePlayerView != null )
//								youTubePlayerView = null;
//							
//							Constance.YouTuBe_URL = "";
//							
//							youTubePlayerView = new YouTubePlayerView(mContext);
//							
//							layoutContetsView.addView( TV_View.setTV_CutView( strBrandCD, upCategory, strCategoryID, "null", youTubePlayerView ) );
//							
//							youTubePlayerView.initialize( DeveloperKey.DEVELOPER_KEY, (OnInitializedListener) mContext);
//							
//						} else if( ScreenType.equals("010001") ){
//							
//						}
					}
				}
			}
		}
		
		try {
			if( tabImgBtn != null ){
				for( int i = 0; i < MaxTabbCount; i++){
					if( v == tabImgBtn[i] ){
						setSubTabBarChicoe( i );
						layoutContetsView.removeAllViews();
						if( i == 0 ){
							layoutContetsView.addView( AGM.setAD_GalleryView( strBrandCD, strCategoryID, "SUB") );
						} else {
							layoutContetsView.addView( AGM.setAD_GalleryView( strBrandCD, CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_CATEGORY_SUB.get( i - 1).ctgryId, "SUB") );
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
/**
		if( v == btn_AdGallery_All ){
			Constance.arrTransInformation[0] = "null";
			Constance.arrTransInformation[1] = "null";
			setButton( 0 ); 
			layoutContetsView.removeAllViews();
			layoutContetsView.addView( AGM.setAD_GalleryView() );
		} else if( v == btn_AdGallery_TV ){
			Constance.arrTransInformation[0] = "null";
			Constance.arrTransInformation[1] = "null";
			setButton( 1 );
			layoutContetsView.removeAllViews();
			if( youTubePlayerView != null )
				youTubePlayerView = null;
			Constance.YouTuBe_URL = "";
			
			youTubePlayerView = new YouTubePlayerView(mContext);
			
			layoutContetsView.addView( TV_View.setTV_CutView(CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_CATEGORY.get( 1 ).ctgryId, youTubePlayerView ) );
			
			youTubePlayerView.initialize( DeveloperKey.DEVELOPER_KEY, (OnInitializedListener) mContext);
			
		} else if( v == btn_AdGallery_Media ){
			Constance.arrTransInformation[0] = "null";
			Constance.arrTransInformation[1] = "null";
			setButton( 2 );
			layoutContetsView.removeAllViews();
			layoutContetsView.addView( MCV.setMediaCutView( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_CATEGORY.get( 0 ).ctgryId ) );
		} else 
		*/
		if( v == btnMenu_Activity ){
			mMenu.show();
		} else if( v == btn_Brand_Activity ) {
			mBrand.show();
		} else if( v == btn_Brand_LogOut ) {
			dialogBuilder.setTitle("SalesForce");
			dialogBuilder.setMessage( "로그아웃 하시겠습니까?");
	        dialogBuilder.setPositiveButton( "확인" , LogOut_Yes );
	        dialogBuilder.setNegativeButton("취소", null);
	        dialogBuilder.show();
		} else if( v == btnGo_Main ) {
			Intent intent = new Intent( mContext, Main_Page.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		} else if( v == btnBack_Page ) {
			try {
				if( layoutContetsView.getChildAt( 0 ) == AGM.getView()){
					if(  PagePosition != 0  ){
						
						try {
							if( mPlayer != null ){
								mPlayer.pause();
								mPlayer.setFullscreen( false );
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						setChoiceCategory( 0 );
						
						txtMainTabView[0].setTextColor( Color.parseColor( "#f45947" ) );
						  
						mainTabImg[0].setBackgroundResource( R.drawable.at_sub_tabbullet_o );
						  
						strBrandCD = arrCategory_ID[1];
						strCategoryID = arrCategory_ID[0];				
						
						layoutContetsView.removeAllViews();
						
						setCategory( false );
						
						if( AGM == null)
							AGM = new AD_GalleryMain( mContext );
						
						layoutContetsView.addView( AGM.setAD_GalleryView(strBrandCD,  strCategoryID, "ALL" ) );
					} else {
						Intent intent = new Intent( mContext, Main_Page.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						finish();
					}
				} else {
					strBrandCD = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_CATEGORY.get( PagePosition - 1 ).brandCd;
					strCategoryID = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_CATEGORY.get( PagePosition - 1 ).ctgryId;

					setCategory( true );
					
					layoutContetsView.removeAllViews();
					
					if( AGM == null)
						AGM = new AD_GalleryMain( mContext );
					
					layoutContetsView.addView( AGM.setAD_GalleryView(strBrandCD,  strCategoryID, "SUB" ) );
					
					new setCategoryInfor_SUB().execute();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private DialogInterface.OnClickListener LogOut_Yes = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			Constance.SHOPCD = "";
			Constance.USER_ID = "";
			Intent intent = new Intent( mContext, LoginPage.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		}
	};
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		
		try {
			if( layoutContetsView.getChildAt( 0 ) == AGM.getView()){
				if(  PagePosition != 0  ){
					
					try {
						if( mPlayer != null ){
							mPlayer.pause();
							mPlayer.setFullscreen( false );
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					setChoiceCategory( 0 );
					
					txtMainTabView[0].setTextColor( Color.parseColor( "#f45947" ) );
					  
					mainTabImg[0].setBackgroundResource( R.drawable.at_sub_tabbullet_o );
					  
					strBrandCD = arrCategory_ID[1];
					strCategoryID = arrCategory_ID[0];		
					
					layoutContetsView.removeAllViews();
					
					setCategory( false );
					
					if( AGM == null)
						AGM = new AD_GalleryMain( mContext );
					
					layoutContetsView.addView( AGM.setAD_GalleryView(strBrandCD,  strCategoryID, "ALL" ) );
				} else {
					Intent intent = new Intent( mContext, Main_Page.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					finish();
				}
			} else {
				strBrandCD = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_CATEGORY.get( PagePosition - 1 ).brandCd;
				strCategoryID = CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_CONTENTS_CATEGORY.get( PagePosition - 1 ).ctgryId;

				layoutContetsView.removeAllViews();
				
				setCategory( true );
				
				if( AGM == null)
					AGM = new AD_GalleryMain( mContext );
				
				layoutContetsView.addView( AGM.setAD_GalleryView(strBrandCD,  strCategoryID, "SUB" ) );
				
				new setCategoryInfor_SUB().execute();
			}
		} catch (Exception e) {
			e.printStackTrace();
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
}
