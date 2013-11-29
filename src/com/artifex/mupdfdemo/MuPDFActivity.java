package com.artifex.mupdfdemo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.Executor;

import cx.hell.android.lib.pagesview.PagesGridView;
import cx.hell.android.lib.pdf.PDF;
import cx.hell.android.lib.pdf.PDF.Outline;
import cx.hell.android.pdfview.Bookmark;
import cx.hell.android.pdfview.BookmarkEntry;
import cx.hell.android.pdfview.OpenFileActivity;
import cx.hell.android.pdfview.PDFPagesProvider;

import fnc.salesforce.android.R;
import fnc.salesforce.android.Activity.Catalog__10002;
import fnc.salesforce.android.Activity.EDUCATION_Main;
import fnc.salesforce.android.Catal_10002.ProductItemListAdabter;
import fnc.salesforce.android.Constance.CONTENTS_PAGE_ALL_LIST_CONSTANCE;
import fnc.salesforce.android.Constance.Constance;
import fnc.salesforce.android.LIB.DebugLog;
import fnc.salesforce.android.LIB.KumaLog;
import fnc.salesforce.android.LIB.ProductDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;
import android.widget.AdapterView.OnItemClickListener;

class ThreadPerTaskExecutor implements Executor {
	public void execute(Runnable r) {
		new Thread(r).start();
	}
}

public class MuPDFActivity extends Activity implements OnClickListener
{
	/* The core rendering instance */
	enum TopBarMode {Main, Search, Annot, Delete, More, Accept};
	enum AcceptMode {Highlight, Underline, StrikeOut, Ink, CopyText};


	private final int    OUTLINE_REQUEST=0;
	private final int    PRINT_REQUEST=1;
	private MuPDFCore    core;
	private String       mFileName;
	private MuPDFReaderView mDocView;
	
	private View         mButtonsView;
	
	private boolean      mButtonsVisible;

	
	private TextView     mFilenameView;

	private TextView     mPageNumberView;
	private ImageButton  mReflowButton;
	private ImageButton  mOutlineButton;

	private TextView     mAnnotTypeText;

	
	private AlertDialog.Builder mAlertBuilder;
	private boolean    mLinkHighlight = false;
	private final Handler mHandler = new Handler();
	private boolean mAlertsActive= false;
	private boolean mReflow = false;
	private AsyncTask<Void,Void,MuPDFAlert> mAlertTask;
	private AlertDialog mAlertDialog;
	
	Toast toastMsg;
	
	public void createAlertWaiter() {
		mAlertsActive = true;
		// All mupdf library calls are performed on asynchronous tasks to avoid stalling
		// the UI. Some calls can lead to javascript-invoked requests to display an
		// alert dialog and collect a reply from the user. The task has to be blocked
		// until the user's reply is received. This method creates an asynchronous task,
		// the purpose of which is to wait of these requests and produce the dialog
		// in response, while leaving the core blocked. When the dialog receives the
		// user's response, it is sent to the core via replyToAlert, unblocking it.
		// Another alert-waiting task is then created to pick up the next alert.
		if (mAlertTask != null) {
			mAlertTask.cancel(true);
			mAlertTask = null;
		}
		if (mAlertDialog != null) {
			mAlertDialog.cancel();
			mAlertDialog = null;
		}
		mAlertTask = new AsyncTask<Void,Void,MuPDFAlert>() {

			@Override
			protected MuPDFAlert doInBackground(Void... arg0) {
				if (!mAlertsActive)
					return null;

				return core.waitForAlert();
			}

			@Override
			protected void onPostExecute(final MuPDFAlert result) {
				// core.waitForAlert may return null when shutting down
				if (result == null)
					return;
				final MuPDFAlert.ButtonPressed pressed[] = new MuPDFAlert.ButtonPressed[3];
				for(int i = 0; i < 3; i++)
					pressed[i] = MuPDFAlert.ButtonPressed.None;
				DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						mAlertDialog = null;
						if (mAlertsActive) {
							int index = 0;
							switch (which) {
							case AlertDialog.BUTTON1: index=0; break;
							case AlertDialog.BUTTON2: index=1; break;
							case AlertDialog.BUTTON3: index=2; break;
							}
							result.buttonPressed = pressed[index];
							// Send the user's response to the core, so that it can
							// continue processing.
							core.replyToAlert(result);
							// Create another alert-waiter to pick up the next alert.
							createAlertWaiter();
						}
					}
				};
				mAlertDialog = mAlertBuilder.create();
				mAlertDialog.setTitle(result.title);
				mAlertDialog.setMessage(result.message);
				switch (result.iconType)
				{
				case Error:
					break;
				case Warning:
					break;
				case Question:
					break;
				case Status:
					break;
				}
				switch (result.buttonGroupType)
				{
				case OkCancel:
					mAlertDialog.setButton(AlertDialog.BUTTON2, getString(R.string.cancel), listener);
					pressed[1] = MuPDFAlert.ButtonPressed.Cancel;
				case Ok:
					mAlertDialog.setButton(AlertDialog.BUTTON1, getString(R.string.okay), listener);
					pressed[0] = MuPDFAlert.ButtonPressed.Ok;
					break;
				case YesNoCancel:
					mAlertDialog.setButton(AlertDialog.BUTTON3, getString(R.string.cancel), listener);
					pressed[2] = MuPDFAlert.ButtonPressed.Cancel;
				case YesNo:
					mAlertDialog.setButton(AlertDialog.BUTTON1, getString(R.string.yes), listener);
					pressed[0] = MuPDFAlert.ButtonPressed.Yes;
					mAlertDialog.setButton(AlertDialog.BUTTON2, getString(R.string.no), listener);
					pressed[1] = MuPDFAlert.ButtonPressed.No;
					break;
				}
				mAlertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
					public void onCancel(DialogInterface dialog) {
						mAlertDialog = null;
						if (mAlertsActive) {
							result.buttonPressed = MuPDFAlert.ButtonPressed.None;
							core.replyToAlert(result);
							createAlertWaiter();
						}
					}
				});

				mAlertDialog.show();
			}
		};

		mAlertTask.executeOnExecutor(new ThreadPerTaskExecutor());
	}

	public void destroyAlertWaiter() {
		mAlertsActive = false;
		if (mAlertDialog != null) {
			mAlertDialog.cancel();
			mAlertDialog = null;
		}
		if (mAlertTask != null) {
			mAlertTask.cancel(true);
			mAlertTask = null;
		}
	}

	private MuPDFCore openFile(String path)
	{
		int lastSlashPos = path.lastIndexOf('/');
		mFileName = new String(lastSlashPos == -1
					? path
					: path.substring(lastSlashPos+1));
		
		System.out.println("Trying to open "+path);
		
		try
		{
			core = new MuPDFCore(this, path);
			// New file: drop the old outline data
			OutlineActivityData.set(null);
		}
		catch (Exception e)
		{
			System.out.println(e);
			return null;
		}
		return core;
	}

	private MuPDFCore openBuffer(byte buffer[])
	{
		System.out.println("Trying to open byte buffer");
		try
		{
			core = new MuPDFCore(this, buffer);
			// New file: drop the old outline data
			OutlineActivityData.set(null);
		}
		catch (Exception e)
		{
			System.out.println(e);
			return null;
		}
		return core;
	}

	private String strCategoryID = "", strCategoryName = "", strActivity = "", mdwldYn = ""; 
	
	
	private ProductItemListAdabter PIA;
	
	private ListView IndexList;
	
	private RelativeLayout layoutIndexList;
	
	private RelativeLayout layoutProductList;
	private ListView ProductList;
	private ImageView imgProduct_btn;
	
	private boolean Prucut_STATE = false;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		mContext = this;
		
		mAlertBuilder = new AlertDialog.Builder(this);

		if (core == null) {
			core = (MuPDFCore)getLastNonConfigurationInstance();

			if (savedInstanceState != null && savedInstanceState.containsKey("FileName")) {
				mFileName = savedInstanceState.getString("FileName");
			}
		}
		
		toastMsg = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		
		ID = new ProductDialog( this, R.style.Transparent);
		
		if (core == null) {
			Intent intent = getIntent();
			byte buffer[] = null;
			if (Intent.ACTION_VIEW.equals(intent.getAction())) {
				Uri uri = intent.getData();
				if (uri.toString().startsWith("content://")) {
					// Handle view requests from the Transformer Prime's file manager
					// Hopefully other file managers will use this same scheme, if not
					// using explicit paths.
					Cursor cursor = getContentResolver().query(uri, new String[]{"_data"}, null, null, null);
					if (cursor.moveToFirst()) {
						String str = cursor.getString(0);
						String reason = null;
						if (str == null) {
							try {
								mPath = uri.getPath();
								
								InputStream is = getContentResolver().openInputStream(uri);
								int len = is.available();
								buffer = new byte[len];
								is.read(buffer, 0, len);
								is.close();
							}
							catch (java.lang.OutOfMemoryError e)
							{
								System.out.println("Out of memory during buffer reading");
								reason = e.toString();
							}
							catch (Exception e) {
								reason = e.toString();
							}
							if (reason != null)
							{
								buffer = null;
								Resources res = getResources();
								AlertDialog alert = mAlertBuilder.create();
								setTitle(String.format(res.getString(R.string.cannot_open_document_Reason), reason));
								alert.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.dismiss),
										new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog, int which) {
												
												try {
													File imageFile = new File(mPath);
													if( imageFile.exists() )
														imageFile.delete();
												} catch (Exception e) {
													e.printStackTrace();
												}
												
												Intent intent = null;
												if( strActivity.equals( "Education" )){
													intent = new Intent( mContext, EDUCATION_Main.class);
												} else {
													intent = new Intent( mContext, Catalog__10002.class);
												}
												intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
												intent.putExtra("backState", "pdf" );
												intent.putExtra("ctgryId", strCategoryID );
												intent.putExtra("brandCd", Constance.BEANDCD );
												intent.putExtra("ctgryNm", strCategoryName);
												startActivity(intent);
												finish();
											}
										});
								alert.show();
								return;
							}
						} else {
							uri = Uri.parse(str);
						}
					}
				}
				if (buffer != null) {
					core = openBuffer(buffer);
				} else {
					core = openFile(Uri.decode(uri.getEncodedPath()));
				}
				SearchTaskResult.set(null);
			}
			
			if( intent.getStringExtra("Activity") != null ){
				strCategoryID = intent.getStringExtra("ctgryId");
				strCategoryName = intent.getStringExtra("ctgryNm");
				strActivity = intent.getStringExtra("Activity");
				mdwldYn		= intent.getStringExtra("dwldYn");
			}
			
			if (core != null && core.needsPassword()) {

				return;
			}
			if (core != null && core.countPages() == 0)
			{
				core = null;
			}
		}
		if (core == null)
		{
			AlertDialog alert = mAlertBuilder.create();
			alert.setTitle(R.string.cannot_open_document);
			alert.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.dismiss),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							
							try {
								File imageFile = new File(mPath);
								if( imageFile.exists() )
									imageFile.delete();
							} catch (Exception e) {
								e.printStackTrace();
							}
							
							Intent intent = null;
							if( strActivity.equals( "Education" )){
								intent = new Intent( mContext, EDUCATION_Main.class);
							} else {
								intent = new Intent( mContext, Catalog__10002.class);
							}
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							intent.putExtra("ctgryId", strCategoryID );
							intent.putExtra("backState", "pdf" );
							intent.putExtra("brandCd", Constance.BEANDCD );
							intent.putExtra("ctgryNm", strCategoryName);
							startActivity(intent);
							finish();
						}
					});
			alert.show();
			return;
		}

		createUI(savedInstanceState);
		
		
	}
	
	private String mPath = "";

	private void setProduct( boolean state ){
		
		CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_LIST_VIEW.clear();
		
		for( int i = 0; i < CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_LIST.size(); i++ ){
			if( Constance.pdf_CurrentPage == Integer.parseInt( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_LIST.get(i).expsrOrdr )){
				CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_LIST_VIEW.add( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_LIST.get(i) );
			}
		}
		
		if( state ){
			PIA.notifyDataSetChanged();

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


	private Context mContext;
	public void createUI(Bundle savedInstanceState) {
		if (core == null)
			return;

		// Now create the UI.
		// First create the document view
		mDocView = new MuPDFReaderView(this) {
			@Override
			protected void onMoveToChild(int i) {
				if (core == null)
					return;
				
				Constance.pdf_CurrentPage = i + 1;
				
				mPageNumberView.setText(String.format("%d / %d", i + 1,
						core.countPages()));
				
				try {
					reSetProduct();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				super.onMoveToChild(i);
			}

		};
		mDocView.setAdapter(new MuPDFPageAdapter(this, core));

		// Make the buttons overlay, and store all its
		// controls in variables
		makeButtonsView();

		// Set up the page slider
		int smax = Math.max(core.countPages()-1,1);

		// Set the file-name text
		mFilenameView.setText( strCategoryName );
		
		// Activate the reflow button
		mReflowButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
//				toggleReflow();
				Bookmark b = new Bookmark( mContext ).open();
				llGridView.setVisibility(View.VISIBLE);
				vvv();
				pagesGridView.getBookmark(b, filePath);
				pagesGridView.goToBookmark();
				pagesGridView.setFocusable(true);
				pagesGridView.setFocusableInTouchMode(true);
				
				b.close();
			}
		});


		

		// Reenstate last state if it was recorded
		SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
		mDocView.setDisplayedViewIndex(prefs.getInt("page"+mFileName, 0));

		if (savedInstanceState == null || !savedInstanceState.getBoolean("ButtonsHidden", false))
			showButtons();


		if(savedInstanceState != null && savedInstanceState.getBoolean("ReflowMode", false))
			reflowModeSet(true);

		// Stick the document view and the buttons overlay into a parent view
		RelativeLayout layout = new RelativeLayout(this);
		
		llGridView =  new LinearLayout(this);
		
		llGridView.setBackgroundColor( Color.parseColor("#000000"));
		aaa();
		layout.addView(mDocView);
		layout.addView(mButtonsView);
		layout.addView(llGridView);
		
		llGridView.setVisibility(View.GONE);
		
		setContentView(layout);
	}

	private LinearLayout llGridView;
	private RelativeLayout gridLayout;
	private PagesGridView pagesGridView = null;
	private PDFPagesProvider pdfPagesProvider = null;
	private PDF pdf = null;
	
	private void aaa(){
		gridLayout = new RelativeLayout(this);
		// the PDF view
		this.pagesGridView = new PagesGridView(this);
		gridLayout.addView(pagesGridView);
		llGridView.addView(gridLayout);
	}
	
	private void vvv(){
		this.pdf = this.getPDF();
		
		this.pdfPagesProvider = new PDFPagesProvider(this, pdf, false, false);	//yeo100
		pagesGridView.setPagesProvider(pdfPagesProvider);
		Bookmark b = new Bookmark(this.getApplicationContext()).open();
		
		b.close();
	}
	
	public void ccc( ){
		if( Prucut_STATE ){
			Prucut_STATE = false;
			setProduct(false);
		}
		
		llGridView.setVisibility(View.GONE);

		mDocView.setDisplayedViewIndex( Constance.pdf_CurrentPage - 1 );
	}
	/**
	 * Save the last page in the bookmarks
	 */
	public void saveLastPage() {
		BookmarkEntry entry;
		entry = this.pagesGridView.toBookmarkEntry();
		Bookmark b = new Bookmark(this.getApplicationContext()).open();
		b.setLast(filePath, entry);
		b.close();
	}
	
	String filePath = "";
	private int box = 2;

	private PDF getPDF() {
		final Intent intent = getIntent();
		Uri uri = intent.getData();
		filePath = uri.getPath();
		
		return new PDF(new File(filePath), this.box);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case OUTLINE_REQUEST:
			if (resultCode >= 0)
				mDocView.setDisplayedViewIndex(resultCode);
			break;
		case PRINT_REQUEST:

			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public Object onRetainNonConfigurationInstance()
	{
		MuPDFCore mycore = core;
		core = null;
		return mycore;
	}

	private void reflowModeSet(boolean reflow)
	{
		mReflow = reflow;
		mDocView.setAdapter(mReflow ? new MuPDFReflowAdapter(this, core) : new MuPDFPageAdapter(this, core));
		mReflowButton.setColorFilter(mReflow ? Color.argb(0xFF, 172, 114, 37) : Color.argb(0xFF, 255, 255, 255));

		if (reflow) setLinkHighlight(false);

		mDocView.refresh(mReflow);
	}

	private void toggleReflow() {
		reflowModeSet(!mReflow);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		if (mFileName != null && mDocView != null) {
			outState.putString("FileName", mFileName);

			// Store current page in the prefs against the file name,
			// so that we can pick it up each time the file is loaded
			// Other info is needed only for screen-orientation change,
			// so it can go in the bundle
			SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
			SharedPreferences.Editor edit = prefs.edit();
			edit.putInt("page"+mFileName, mDocView.getDisplayedViewIndex());
			edit.commit();
		}

		if (!mButtonsVisible)
			outState.putBoolean("ButtonsHidden", true);

		if (mReflow)
			outState.putBoolean("ReflowMode", true);
	}

	@Override
	protected void onPause() {
		super.onPause();
		
		if (mFileName != null && mDocView != null) {
			SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
			SharedPreferences.Editor edit = prefs.edit();
			edit.putInt("page"+mFileName, mDocView.getDisplayedViewIndex());
			edit.commit();
		}
	}

	public void onDestroy()
	{
		if (core != null)
			core.onDestroy();
		if (mAlertTask != null) {
			mAlertTask.cancel(true);
			mAlertTask = null;
		}
		core = null;
		super.onDestroy();

		if( strActivity.equals( "Education" )){
			if( mdwldYn.equals("011002") ){
				try {
					final Intent intent = getIntent();
					Uri uri = intent.getData();
					String afilePath = uri.getPath();

					File imageFile = new File(afilePath);
					imageFile.delete();
				} catch (Exception e) {
					e.printStackTrace();
				}	
			}
		}
	}

	private void setButtonEnabled(ImageButton button, boolean enabled) {
		button.setEnabled(enabled);
		button.setColorFilter(enabled ? Color.argb(255, 255, 255, 255):Color.argb(255, 128, 128, 128));
	}

	private void setLinkHighlight(boolean highlight) {
		mLinkHighlight = highlight;

		// Inform pages of the change.
		mDocView.setLinksEnabled(highlight);
	}

	private void showButtons() {
		if (core == null)
			return;
		if (!mButtonsVisible) {
			mButtonsVisible = true;
			// Update page number text and slider
			int index = mDocView.getDisplayedViewIndex();
			updatePageNumView(index);

		}
	}
	
	private void reSetProduct(){
		CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_LIST_VIEW.clear();
		
		for( int i = 0; i < CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_LIST.size(); i++ ){
			if( Constance.pdf_CurrentPage == Integer.parseInt( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_LIST.get(i).expsrOrdr )){
				CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_LIST_VIEW.add( CONTENTS_PAGE_ALL_LIST_CONSTANCE.ARR_PRUDUCT_LIST.get(i) );
			}
		}
		
		PIA.notifyDataSetChanged();
		KumaLog.LogD("test_1"," Constance.pdf_CurrentPage : " + Constance.pdf_CurrentPage);
	}
	
	private void updatePageNumView(int index) {
		if (core == null)
			return;
		
		Constance.pdf_CurrentPage = index+1;
		
		mPageNumberView.setText(String.format("%d / %d", index+1, core.countPages()));
		
	}


	private ViewAnimator mTopBarSwitcher;
	private ImageButton mbackButton;
	private void makeButtonsView() {
		mButtonsView = getLayoutInflater().inflate(R.layout.buttons,null);
		
		mFilenameView = (TextView)mButtonsView.findViewById(R.id.docNameText);
		
		mPageNumberView = (TextView)mButtonsView.findViewById(R.id.pageNumber);

		mReflowButton = (ImageButton)mButtonsView.findViewById(R.id.reflowButton);
		mOutlineButton = (ImageButton)mButtonsView.findViewById(R.id.outlineButton);

		mTopBarSwitcher = (ViewAnimator)mButtonsView.findViewById(R.id.switcher);

		mbackButton = (ImageButton)mButtonsView.findViewById(R.id.moreButton);
		
		layoutProductList 	= (RelativeLayout) mButtonsView.findViewById( R.id.layoutProductList );
		layoutIndexList		= (RelativeLayout) mButtonsView.findViewById( R.id.layoutIndexList );
		
		ProductList 		= (ListView) mButtonsView.findViewById( R.id.ProductList );
		IndexList 			= (ListView) mButtonsView.findViewById( R.id.IndexList );
		
		imgProduct_btn 		= (ImageView) mButtonsView.findViewById( R.id.imgProduct_btn );
		
		mOutlineButton.setOnClickListener( this );
		mbackButton.setOnClickListener( this );
		imgProduct_btn.setOnClickListener( this );
		
		PIA = new ProductItemListAdabter( this );
		
		ProductList.setAdapter( PIA );
		layoutIndexList.setVisibility( View.GONE );
		try {
			setProduct( Prucut_STATE );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if( strActivity.equals( "Education" )){
			layoutProductList.setVisibility( View.GONE );
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
	public void OnCancelAcceptButtonClick(View v) {
		MuPDFView pageView = (MuPDFView) mDocView.getDisplayedView();
		if (pageView != null) {
			pageView.deselectText();
			pageView.cancelDraw();
		}
		mDocView.setMode(MuPDFReaderView.Mode.Viewing);
	}


	@Override
	protected void onStart() {
		if (core != null) {
			core.startAlerts();
			createAlertWaiter();
		}
		super.onStart();
	}

	@Override
	protected void onStop(){
		if (core != null) {
			destroyAlertWaiter();
			core.stopAlerts();
		}
		
		super.onStop();
	}

	@Override
	public void onBackPressed() {
		if (core.hasChanges()) {
			DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					if (which == AlertDialog.BUTTON_POSITIVE)
						core.save();

					Intent intent = null;
					if( strActivity.equals( "Education" )){
						intent = new Intent( mContext, EDUCATION_Main.class);
					} else {
						intent = new Intent( mContext, Catalog__10002.class);
					}
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra("backState", "pdf" );
					intent.putExtra("ctgryId", strCategoryID );
					intent.putExtra("brandCd", Constance.BEANDCD );
					intent.putExtra("ctgryNm", strCategoryName);
					startActivity(intent);
					finish();
				}
			};
			AlertDialog alert = mAlertBuilder.create();
			alert.setTitle("MuPDF");
			alert.setMessage(getString(R.string.document_has_changes_save_them_));
			alert.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.yes), listener);
			alert.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.no), listener);
			alert.show();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public void onClick(View v) {
		mOutlineButton.setOnClickListener( this );
		mbackButton.setOnClickListener( this );
		imgProduct_btn.setOnClickListener( this );
		
		if( v == imgProduct_btn ){
			if( !Prucut_STATE ){
				imgProduct_btn.setImageResource( R.drawable.pdf_productlisttitle02 );
				setProduct( true );
				Prucut_STATE = true;
			} else {
				imgProduct_btn.setImageResource( R.drawable.pdf_productlisttitle01 );
				setProduct( false );
				Prucut_STATE = false;
			}
		} else if( v == mbackButton ){
			Intent intent = null;
			if( strActivity.equals( "Education" )){
				intent = new Intent( mContext, EDUCATION_Main.class);
			} else {
				intent = new Intent( mContext, Catalog__10002.class);
			}
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("backState", "pdf" );
			intent.putExtra("ctgryId", strCategoryID );
			intent.putExtra("brandCd", Constance.BEANDCD );
			intent.putExtra("ctgryNm", strCategoryName);
			startActivity(intent);
			finish();
		} else if( v == mOutlineButton ){
			if( layoutIndexList.getVisibility() == View.GONE ){
				layoutIndexList.setVisibility( View.VISIBLE );
				
				OutlineItem outline[] = core.getOutline();
				
				if (outline != null) {
					OutlineActivityData.get().items = outline;
					
					mItems = OutlineActivityData.get().items;
					IndexList.setAdapter(new OutlineAdapter(getLayoutInflater(),mItems));
					
					IndexList.setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
							int pageNumber = mItems[position].page;

							layoutIndexList.setVisibility( View.GONE );
							
							if( Prucut_STATE ){
								Prucut_STATE = false;
								setProduct(false);
							}
							mDocView.setDisplayedViewIndex( pageNumber );
						}
					});
				} else {
					/* TODO: show toast info about toc not found */
					toastMsg.setText("목차를 읽어올 수 없습니다.");
					toastMsg.show();
				}
			} else {
				layoutIndexList.setVisibility( View.GONE );
			}	
		}		
	}
	
	OutlineItem mItems[];
}
