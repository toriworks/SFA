package cx.hell.android.pdfview;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;    
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.webkit.DownloadListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import cx.hell.android.lib.pagesview.PagesGridView;
import cx.hell.android.lib.pagesview.PagesView;
import cx.hell.android.lib.pdf.PDF;
import cx.hell.android.lib.pdf.PDF.Outline;
import fnc.salesforce.android.R;
import fnc.salesforce.android.Activity.Catalog__10002;
import fnc.salesforce.android.Activity.Look_10001;
import fnc.salesforce.android.Catal_10002.ProductItemListAdabter;
import fnc.salesforce.android.Constance.CONTENTS_PAGE_ALL_LIST_CONSTANCE;
import fnc.salesforce.android.Constance.Constance;
import fnc.salesforce.android.LIB.DebugLog;
import fnc.salesforce.android.LIB.KumaLog;

/**
 * Document display activity.
 */
@SuppressWarnings("unused")
public class OpenFileActivity extends Activity implements  OnClickListener {

	private final static String TAG = "cx.hell.android.pdfview";

	private PDF pdf = null;
	private PagesView pagesView = null;
	private PagesGridView pagesGridView = null;

	private PDFPagesProvider pdfPagesProvider = null;
	private Actions actions = null;

	private RelativeLayout activityLayout = null;
	private RelativeLayout gridLayout = null;
	private String filePath = "/";

	// private String findText = null;
	// private Integer currentFindResultPage = null;
	// private Integer currentFindResultNumber = null;

	private int box = 2;
	private int colorMode = Options.COLOR_MODE_NORMAL;

	private SensorManager sensorManager;
	private float[] gravity = { 0f, -9.81f, 0f };

	private int prevOrientation;

	private boolean history = true;

	Toast toastMsg;
	boolean bViewMode = false;
	private Button btnBack;
	private Button btnSel0;
	private Button btnCfg;
	
	private LinearLayout llHeader;
	private LinearLayout llTitle;
	private TextView tvYearTitle;

	public static Animation animUp = null;
	public static Animation animDown = null;

	
	private void initHeaderComponent() {
		btnBack = (Button) findViewById(R.id.btnBack);
		btnSel0 = (Button) findViewById(R.id.btnSel0);
		btnCfg = (Button) findViewById(R.id.btnCfg);
		llTitle = (LinearLayout) findViewById(R.id.llYearSelect);

		animUp = AnimationUtils.loadAnimation(this, R.anim.bottom_slide_up);
		animDown = AnimationUtils.loadAnimation(this, R.anim.bottom_slide_down);

		btnBack.setOnClickListener(this);
		btnSel0.setOnClickListener(this);
		btnCfg.setOnClickListener(this);

		btnBack.setBackgroundResource(R.drawable.pdf_btn_index_n);
		btnSel0.setBackgroundResource(R.drawable.pdf_btn_thumb_n);
		btnCfg.setBackgroundResource(R.drawable.pdf_btn_close_n);


		llTitle.setVisibility(View.INVISIBLE);

	}

	// // #ifdef pro
	// /**
	// * If true, then current activity is in text reflow mode.
	// */
	// private boolean textReflowMode = false;
	// // #endif
	LinearLayout llMainView;
	LinearLayout llGridView;
	String sYear;
	String sMonth;
	private Context mContext;
	
	private ProductItemListAdabter PIA;
	
	private ListView IndexList;
	
	private RelativeLayout layoutIndexList;
	
	private RelativeLayout layoutProductList;
	private ListView ProductList;
	private ImageView imgProduct_btn;
	
	private String strCategoryID = "", strCategoryName = ""; 
	
	/**
	 * Called when the activity is first created. TODO: initialize dialog fast,
	 * then move file loading to other thread TODO: add progress bar for file
	 * load TODO: add progress icon for file rendering
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// display this
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mainview);
		
//		sYear = intent.getStringExtra("YEAR");
//		sMonth = intent.getStringExtra("MONTH");
		
		mContext = this;
		
		llMainView = (LinearLayout) findViewById(R.id.llDocView);
		llGridView = (LinearLayout) findViewById(R.id.llGridView);
		llHeader = (LinearLayout) findViewById(R.id.llHeader);

		
		layoutProductList 	= (RelativeLayout) findViewById( R.id.layoutProductList );
		layoutIndexList		= (RelativeLayout) findViewById( R.id.layoutIndexList );
		
		ProductList 		= (ListView) findViewById( R.id.ProductList );
		IndexList 			= (ListView) findViewById( R.id.IndexList );
		
		imgProduct_btn 		= (ImageView) findViewById( R.id.imgProduct_btn );

		if (bViewMode) {
			llMainView.setVisibility(View.GONE);
			llGridView.setVisibility(View.VISIBLE);
		} else {
			llMainView.setVisibility(View.VISIBLE);
			llGridView.setVisibility(View.GONE);
		}

		layoutIndexList.setVisibility( View.GONE );
		
		initHeaderComponent();
		toastMsg = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		Options.setOrientation(this);
		SharedPreferences options = PreferenceManager.getDefaultSharedPreferences(this);

		this.box = Integer.parseInt(options.getString(Options.PREF_BOX, "2"));

		// Get display metrics
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		// Main View Init
		// use a relative layout to stack the views
		activityLayout = new RelativeLayout(this);
		// the PDF view
		this.pagesView = new PagesView(this);
		activityLayout.addView(pagesView);
		startPDF(options);
		Intent intent = getIntent();
		
		if( intent.getStringExtra("ctgryId") != null ){
			strCategoryID = intent.getStringExtra("ctgryId");
			strCategoryName = intent.getStringExtra("ctgryNm");
		}
		
		if (!this.pdf.isValid()) {
			Intent i = new Intent( mContext, Catalog__10002.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i.putExtra("ctgryId", strCategoryID );
			i.putExtra("brandCd", Constance.BEANDCD );
			i.putExtra("ctgryNm", strCategoryName);
			startActivity(i);
			finish();
		}
		llMainView.addView(activityLayout);

		// Grid View Init
		// use a relative layout to stack the views
		gridLayout = new RelativeLayout(this);
		// the PDF view
		this.pagesGridView = new PagesGridView(this);
		gridLayout.addView(pagesGridView);
		llGridView.addView(gridLayout);
		// go to last viewed page

		// send keyboard events to this view
		pagesView.setFocusable(true);
		pagesView.setFocusableInTouchMode(true);

		// this.zoomHandler = new Handler();
		// this.pageHandler = new Handler();
		// this.zoomRunnable = new Runnable() {
		// public void run() {
		// fadeZoom();
		// }
		// };
		// this.pageRunnable = new Runnable() {
		// public void run() {
		// fadePage();
		// }
		// }; 
		
		imgProduct_btn.setOnClickListener( this );
		
		PIA = new ProductItemListAdabter( this );
		
		ProductList.setAdapter( PIA );
		
		try {
			setProduct( Prucut_STATE );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
	@Override
	public void onBackPressed() {
		
	}



	/**
	 * Save the current page before exiting
	 */
	@Override
	protected void onPause() {
		super.onPause();

		saveLastPage();

//		if (sensorManager != null) {
//			sensorManager.unregisterListener(this);
//			sensorManager = null;
//		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		sensorManager = null;

		if (Options.setOrientation(this)) {
//			sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//			if (sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() > 0) {
//				gravity[0] = 0f;
//				gravity[1] = -9.81f;
//				gravity[2] = 0f;
//				sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
//						SensorManager.SENSOR_DELAY_NORMAL);
//				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_BEHIND);
//				this.prevOrientation = ActivityInfo.SCREEN_ORIENTATION_BEHIND;
//			} else {
//				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//			}
//			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		SharedPreferences options = PreferenceManager.getDefaultSharedPreferences(this);

		history = options.getBoolean(Options.PREF_HISTORY, true);

		if (options.getBoolean(Options.PREF_KEEP_ON, false))
			this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		else
			this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		actions = new Actions(options);
		this.pagesView.setActions(actions);
		// setZoomLayout(options);
		// this.pagesView.setZoomLayout(zoomLayout);

		// this.showZoomOnScroll =
		// options.getBoolean(Options.PREF_SHOW_ZOOM_ON_SCROLL, false);
		this.pagesView.setSideMargins(Integer.parseInt(options.getString(Options.PREF_SIDE_MARGINS, "0")));
		this.pagesView.setTopMargin(Integer.parseInt(options.getString(Options.PREF_TOP_MARGIN, "0")));
		this.pagesView.setDoubleTap(Integer.parseInt(options.getString(Options.PREF_DOUBLE_TAP, ""
				+ Options.DOUBLE_TAP_ZOOM_IN_OUT)));

		int newBox = Integer.parseInt(options.getString(Options.PREF_BOX, "2"));
		if (this.box != newBox) {
			saveLastPage();
			this.box = newBox;
			startPDF(options);
			this.pagesView.goToBookmark();
		}

		// yeo100
		//this.pdfPagesProvider.setGray(Options.isGray(this.colorMode));
		this.pdfPagesProvider.setExtraCache(1024 * 1024 * Options
				.getIntFromString(options, Options.PREF_EXTRA_CACHE, 0));
		this.pdfPagesProvider.setOmitImages(options.getBoolean(Options.PREF_OMIT_IMAGES, false));
		// this.pagesView.setColorMode(this.colorMode);

		this.pdfPagesProvider.setRenderAhead(options.getBoolean(Options.PREF_RENDER_AHEAD, true));
		this.pagesView.setVerticalScrollLock(options.getBoolean(Options.PREF_VERTICAL_SCROLL_LOCK, false));
		this.pagesView.invalidate();

//		hideHeader();
	}

	// /**
	// * Set handlers on zoom level buttons
	// */
	// private void setZoomButtonHandlers() {
	// this.zoomDownButton.setOnLongClickListener(new View.OnLongClickListener()
	// {
	// public boolean onLongClick(View v) {
	// pagesView.doAction(actions.getAction(Actions.LONG_ZOOM_IN));
	// return true;
	// }
	// });
	// this.zoomDownButton.setOnClickListener(new View.OnClickListener() {
	// public void onClick(View v) {
	// pagesView.doAction(actions.getAction(Actions.ZOOM_IN));
	// }
	// });
	// this.zoomWidthButton.setOnClickListener(new View.OnClickListener() {
	// public void onClick(View v) {
	// pagesView.zoomWidth();
	// }
	// });
	// this.zoomWidthButton.setOnLongClickListener(new
	// View.OnLongClickListener() {
	// public boolean onLongClick(View v) {
	// pagesView.zoomFit();
	// return true;
	// }
	// });
	// this.zoomUpButton.setOnClickListener(new View.OnClickListener() {
	// public void onClick(View v) {
	// pagesView.doAction(actions.getAction(Actions.ZOOM_OUT));
	// }
	// });
	// this.zoomUpButton.setOnLongClickListener(new View.OnLongClickListener() {
	// public boolean onLongClick(View v) {
	// pagesView.doAction(actions.getAction(Actions.LONG_ZOOM_OUT));
	// return true;
	// }
	// });
	// }

	private void startPDFGrid(SharedPreferences options) {
		this.pdf = this.getPDF();
		if (!this.pdf.isValid()) {
			DebugLog.out(TAG, "Invalid PDF");
			if (this.pdf.isInvalidPassword()) {
				Toast.makeText(this, "This file needs a password", 4000).show();
			} else {
				Toast.makeText(this, "Invalid PDF file", 4000).show();
			}
			return;
		}
		this.colorMode = Options.getColorMode(options);
		this.pdfPagesProvider = new PDFPagesProvider(this, pdf, false, false);	//yeo100
		pagesGridView.setPagesProvider(pdfPagesProvider);
		Bookmark b = new Bookmark(this.getApplicationContext()).open();
		
		b.close();
	}

	private void startPDF(SharedPreferences options) {
		this.pdf = this.getPDF();
		if (!this.pdf.isValid()) {
			DebugLog.out(TAG, "Invalid PDF");
			if (this.pdf.isInvalidPassword()) {
				Toast.makeText(this, "This file needs a password", 4000).show();
			} else {
				Toast.makeText(this, "Invalid PDF file", 4000).show();
			}
			return;
		}
		this.colorMode = Options.getColorMode(options);
		this.pdfPagesProvider = new PDFPagesProvider(this, pdf, false, false);	//yeo100
		pagesView.setPagesProvider(pdfPagesProvider);
		Bookmark b = new Bookmark(this.getApplicationContext()).open();
		pagesView.setStartBookmark(b, filePath);
		getBookmark(b, filePath);
		b.close();

	}

	/**
	 * Return PDF instance wrapping file referenced by Intent. Currently reads
	 * all bytes to memory, in future local files should be passed to native
	 * code and remote ones should be downloaded to local tmp dir.
	 * 
	 * @return PDF instance
	 */
	private PDF getPDF() {
		final Intent intent = getIntent();
		Uri uri = intent.getData();
		filePath = uri.getPath();
		if (uri.getScheme().equals("file")) {
			return new PDF(new File(filePath), this.box);
		} else if (uri.getScheme().equals("content")) {
			ContentResolver cr = this.getContentResolver();
    		ParcelFileDescriptor fileDescriptor = null;
			try {
			    fileDescriptor = cr.openFileDescriptor(uri, "r");
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e); // TODO: handle errors
			}
			return new PDF(fileDescriptor, this.box);
		} else {
			throw new RuntimeException("don't know how to get filename from " + uri);
		}
	}

	private void setOrientation(int orientation) {
//		if (orientation != this.prevOrientation) {
//			setRequestedOrientation(orientation);
//			this.prevOrientation = orientation;
//		}
	}

	/**
	 * Intercept touch events to handle the zoom buttons animation
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		int action = event.getAction();
		// if (action == MotionEvent.ACTION_UP || action ==
		// MotionEvent.ACTION_DOWN) {
		// showPageNumber(true);
		// }
		return super.dispatchTouchEvent(event);
	};

	public void slideUp() {
		int duration = 500;
		llHeader.setVisibility(View.VISIBLE);
		animUp.setDuration(duration);
		animUp.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {
			}

			public void onAnimationEnd(Animation animation) {
				if (!bViewMode)
					llHeader.setVisibility(View.GONE);
				bHeaderStatus = false;
			}

			public void onAnimationRepeat(Animation animation) {
			}
		});
		llHeader.startAnimation(animUp);
	}

	public void slideDown() {
		int duration = 500;
		llHeader.setVisibility(View.GONE);
		animDown.setDuration(duration);
		animDown.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {
				// ((ImageButton)mActivity.findViewById(R.id.btn_gnb)).setBackgroundResource(R.drawable.sel_gnb_up);
			}

			public void onAnimationEnd(Animation animation) {
				llHeader.setVisibility(View.VISIBLE);
				bHeaderStatus = false;
			}

			public void onAnimationRepeat(Animation animation) {
			}
		});
		llHeader.startAnimation(animDown);
	}

	Handler mTimerHandler;
	public Runnable bottomMenu = new Runnable() {
		public void run() {
			try {
				// TODO Auto-generated method stub
				slideDown();
			} catch (NullPointerException ne) {

			}
		}
	};
	public Runnable upMenu = new Runnable() {
		public void run() {
			try {
				// TODO Auto-generated method stub
				if(!bViewMode){
					slideUp();					
				}
				
			} catch (NullPointerException ne) {

			}
		}
	};
	
	

	private void setProduct( boolean state ){
		
		CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_LIST_VIEW.clear();
		
		for( int i = 0; i < CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_LIST.size(); i++ ){
			if( Constance.pdf_CurrentPage == Integer.parseInt( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_LIST.get(i).expsrOrdr )){
				CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_LIST_VIEW.add( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_LIST.get(i) );
			}
		}
		
		if( state ){
			PIA.notifyDataSetChanged();

			FrameLayout.LayoutParams imglinelayout = new FrameLayout.LayoutParams(
					316, FrameLayout.LayoutParams.MATCH_PARENT);
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
					FrameLayout.LayoutParams imglinelayout = new FrameLayout.LayoutParams(
							318, FrameLayout.LayoutParams.MATCH_PARENT);
					imglinelayout.setMargins( 765, 58, 0, 0);

					layoutProductList.setLayoutParams(imglinelayout);
					
				}
			});
			layoutProductList.startAnimation( aaa );
		}
	}

	
	
	boolean bHeaderStatus = false;
//	public void hideHeader() {
//		
//		if( Prucut_STATE ){
//			Prucut_STATE = false;
//			setProduct(false);
//		}
//		
//		if (!bViewMode) {
//			if (!bHeaderStatus) {
//				// 제품 정보
//				bHeaderStatus = true;
//				mTimerHandler = new Handler();
//				slideDown();
//				mTimerHandler.postDelayed(upMenu, 2000);
//			}
//		}
//	}

	public void showPageNumber(boolean force) {
		
		if( Prucut_STATE ){
			Prucut_STATE = false;
			setProduct(false);
		}
		
		if (!bViewMode) {
			toastMsg.setText("" + (pagesView.getCurrentPage() + 1) + "/" + this.pdfPagesProvider.getPageCount());
			toastMsg.setGravity(Gravity.BOTTOM, 0, 0);
			toastMsg.show();
			Constance.pdf_CurrentPage = ( pagesView.getCurrentPage() + 1 );
		}
	}

	private void fadePage() {

	}

	/**
	 * Show zoom buttons and page number
	 */
	public void showAnimated(boolean alsoZoom) {
		showPageNumber(true);
	}

	/**
	 * Show error message to user.
	 * 
	 * @param message
	 *            message to show
	 */
	private void errorMessage(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		AlertDialog dialog = builder.setMessage(message).setTitle("Error").create();
		dialog.show();
	}

	private void gotoPage(int page) {
		DebugLog.out("test", "rewind to page " + page);
		if (this.pagesView != null) {
			this.pagesView.scrollToPage(page);
			showAnimated(true);
		}
	}

	/**
	 * Save the last page in the bookmarks
	 */
	public void saveLastPage() {
		BookmarkEntry entry;
		if (bViewMode) {
			entry = this.pagesGridView.toBookmarkEntry();
		} else {
			entry = this.pagesView.toBookmarkEntry();
		}
		Bookmark b = new Bookmark(this.getApplicationContext()).open();
		b.setLast(filePath, entry);
		b.close();
		DebugLog.out(TAG, "last page saved for " + filePath + " entry:" + entry.toString());
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		DebugLog.out(TAG, "onConfigurationChanged(" + newConfig + ")");
	}

	// #ifdef pro
	/**
	 * Build and display dialog containing table of contents.
	 * 
	 * @param outline
	 *            root of TOC tree
	 */
	private void showTableOfContentsDialog(Outline outline) {
		
		final ArrayList<String> tocList = new ArrayList<String>();
		final ArrayList<Integer> tocPages = new ArrayList<Integer>();
		this.outlineToArrayList(tocList, tocPages, outline, 0);

		IndexList.setAdapter(new ArrayAdapter<String>(this, R.layout.toc_list_item, tocList));
		IndexList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				int pageNumber = tocPages.get(position);
				OpenFileActivity.this.gotoPage(pageNumber);
				layoutIndexList.setVisibility( View.GONE );
			}
		});
	}

	/**
	 * Quick and dirty way to reduce tree to two lists for TOC dialog.
	 */
	private void outlineToArrayList(List<String> list, List<Integer> pages, Outline outline, int level) {
		final class Pair {
			public Pair(int level, Outline outline) {
				this.level = level;
				this.outline = outline;
			}

			public int level;
			public Outline outline;
		}
		;

		Stack<Pair> stack = new Stack<Pair>();
		stack.push(new Pair(0, outline));

		DebugLog.out(TAG, "converting table of contents...");

		while (!stack.empty()) {
			Pair p = stack.pop();
			String s = "";
			for (int i = 0; i < p.level; ++i)
				s += " ";
			s += p.outline.title;
			list.add(s);
			pages.add(p.outline.page);
			if (p.outline.next != null) {
				stack.push(new Pair(p.level, p.outline.next));
			}
			if (p.outline.down != null) {
				stack.push(new Pair(p.level + 1, p.outline.down));
			}
		}

		/*
		 * if (outline == null) return; String s = ""; for(int i = 0; i < level;
		 * ++i) s += " "; s += outline.title; list.add(s);
		 * pages.add(outline.page); if (outline.down != null)
		 * this.outlineToArrayList(list, pages, outline.down, level+1); if
		 * (outline.next != null) this.outlineToArrayList(list, pages,
		 * outline.next, level);
		 */
	}

	// #endif

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	public void onSensorChanged(SensorEvent event) {
//		gravity[0] = 0.8f * gravity[0] + 0.2f * event.values[0];
//		gravity[1] = 0.8f * gravity[1] + 0.2f * event.values[1];
//		gravity[2] = 0.8f * gravity[2] + 0.2f * event.values[2];
//
//		float sq0 = gravity[0] * gravity[0];
//		float sq1 = gravity[1] * gravity[1];
//		float sq2 = gravity[2] * gravity[2];
//
//		if (sq1 > .85 * (sq0 + sq2)) {
//			if (gravity[1] > 4)
//				setOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//			// else if (gravity[1] < -4 && Integer.parseInt(Build.VERSION.SDK)
//			// >= 9)
//			// setOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
//		} else if (sq0 > .85 * (sq1 + sq2)) {
////			if (gravity[0] > 4)
////				setOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//			// else if (gravity[0] < -4 && Integer.parseInt(Build.VERSION.SDK)
//			// >= 9)
//			// setOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
//		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			// Back 키가 눌렸을 경우 해당 모드에 따른 동작 정의
			if (bViewMode) {
				changeView();
			} else {
				Intent intent = new Intent( mContext, Catalog__10002.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("ctgryId", strCategoryID );
				intent.putExtra("brandCd", Constance.BEANDCD );
				intent.putExtra("ctgryNm", strCategoryName);
				startActivity(intent);
				finish();
			}
			return true;

		}
		return super.onKeyDown(keyCode, event);
	}

	public void changeView() {
		Bookmark b = new Bookmark(this.getApplicationContext()).open();
				
		if (bViewMode) {
			pagesGridView.clearRenderer();
			bViewMode = false;
			llMainView.setVisibility(View.VISIBLE);
			llGridView.setVisibility(View.GONE);

			btnBack.setVisibility(View.VISIBLE);
			btnSel0.setVisibility(View.VISIBLE);
			btnCfg.setVisibility(View.VISIBLE);

			llTitle.setVisibility(View.INVISIBLE);

			pagesView.setStartBookmark(b, filePath);
			pagesView.goToBookmark();
			btnCfg.setBackgroundResource(R.drawable.pdf_btn_close_n);
//			hideHeader() ;
		} else {
			pagesView.clearRenderer();
			bViewMode = true;
			llHeader.setVisibility(View.VISIBLE);
			llMainView.setVisibility(View.GONE);
			llGridView.setVisibility(View.VISIBLE);

			btnBack.setVisibility(View.VISIBLE);
			btnSel0.setVisibility(View.GONE);
			btnCfg.setVisibility(View.VISIBLE);

			llTitle.setVisibility(View.VISIBLE);
			SharedPreferences options = PreferenceManager.getDefaultSharedPreferences(this);
			startPDFGrid(options);
			pagesGridView.getBookmark(b, filePath);
			pagesGridView.goToBookmark();
			pagesGridView.setFocusable(true);
			pagesGridView.setFocusableInTouchMode(true);
			if (pagesGridView.getBookMarkView()) {
				btnCfg.setBackgroundResource(R.drawable.pdf_btn_close_n);
			} else {
				btnCfg.setBackgroundResource(R.drawable.pdf_btn_close_n);
			}
			
		}
		b.close();
	}

	ArrayList<BookmarkEntry> alBookMark;
	HashMap<Integer, String> hmBookMark;

	public void getBookmark(Bookmark b, String bookmarkName) {
		hmBookMark = new HashMap<Integer, String>();
		if (b != null) {
			this.alBookMark = b.getBookmarks(bookmarkName);
			if (this.alBookMark != null) {
				for (BookmarkEntry bm : alBookMark) {
					DebugLog.out(TAG, "PAGEGRID BOOKMARK Entry : " + bm.page);
					hmBookMark.put(bm.page, bm.comment);
				}
			}
		}
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnBack:
			if( layoutIndexList.getVisibility() == View.GONE ){
				layoutIndexList.setVisibility( View.VISIBLE );
				Outline outline = this.pdf.getOutline();
				if (outline != null) {
					this.showTableOfContentsDialog(outline);
				} else {
					/* TODO: show toast info about toc not found */
					toastMsg.setText("목차를 읽어올 수 없습니다."); 
					toastMsg.show();
				}
			} else {
				layoutIndexList.setVisibility( View.GONE );
			}
			
			break;
		case R.id.btnSel0:
			saveLastPage();
			changeView();
			break;
		
		case R.id.btnCfg:
			
			Intent intent = new Intent( mContext, Catalog__10002.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("ctgryId", strCategoryID );
			intent.putExtra("brandCd", Constance.BEANDCD );
			intent.putExtra("ctgryNm", strCategoryName);
			startActivity(intent);
			finish();
			
			break;
		case R.id.imgProduct_btn:
			if( !Prucut_STATE ){
				imgProduct_btn.setImageResource( R.drawable.pdf_productlisttitle02 );
				setProduct( true );
				Prucut_STATE = true;
			} else {
				imgProduct_btn.setImageResource( R.drawable.pdf_productlisttitle01 );
				setProduct( false );
				Prucut_STATE = false;
			}
			
			break;

		}
	}

	private boolean Prucut_STATE = false;
}
