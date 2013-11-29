package cx.hell.android.lib.pagesview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;


import cx.hell.android.pdfview.Bookmark;
import cx.hell.android.pdfview.BookmarkEntry;
import fnc.salesforce.android.R;
import fnc.salesforce.android.Constance.Constance;
import fnc.salesforce.android.LIB.DebugLog;

/**
 * View that simplifies displaying of paged documents. TODO: redesign zooms,
 * pages, margins, layout TODO: use more floats for better align, or use more
 * ints for performance ;) (that is, really analyse what should be used when)
 */
public class PagesGridView extends View implements View.OnTouchListener, OnImageRenderedListener {
	/**
	 * Logging tag.
	 */
	private static final String TAG = "cx.hell.android.pdfview";

	/*
	 * Experiments show that larger tiles are faster, but the gains do drop off,
	 * and must be balanced against the size of memory chunks being requested.
	 */
	private static final int MIN_TILE_WIDTH = 256;
	private static final int MAX_TILE_WIDTH = 640;
	private static final int MIN_TILE_HEIGHT = 128;
	private static final int MAX_TILE_PIXELS = 640 * 360;

	// private final static int MAX_ZOOM = 4000;
	// private final static int MIN_ZOOM = 100;

	/**
	 * Space between screen edge and page and between pages.
	 */
	private int marginX = 0;
	private int marginY = 40;

	/* zoom steps */
	float step = 1.414f;

	/* volume keys page */
	boolean pageWithVolume = true;

	private Activity activity = null;

	/**
	 * Source of page bitmaps.
	 */
	private PagesProvider pagesProvider = null;

	@SuppressWarnings("unused")
	private long lastControlsUseMillis = 0;
	private long oldControlsUseMillis = 0;

	private float maxRealPageSize[] = { 0f, 0f };
	private float realDocumentSize[] = { 0f, 0f };

	/**
	 * Current width of this view.
	 */
	private int width = 0;

	/**
	 * Current height of this view.
	 */
	private int height = 0;

	/**
	 * Position over book, not counting drag. This is position of viewports
	 * center, not top-left corner.
	 */
	private int left = 0;

	/**
	 * Position over book, not counting drag. This is position of viewports
	 * center, not top-left corner.
	 */
	private int top = 0;

	/**
	 * Current zoom level. 1000 is 100%.
	 */
	private int zoomLevel = 1000;

	/**
	 * Current rotation of pages.
	 */
	//private int rotation = 0;

	/**
	 * Base scaling factor - how much shrink (or grow) page to fit it nicely to
	 * screen at zoomLevel = 1000. For example, if we determine that 200x400
	 * image fits screen best, but PDF's pages are 400x800, then base scaling
	 * would be 0.5, since at base scaling, without any zoom, page should fit
	 * into screen nicely.
	 */
	private float scaling0 = 0f;

	/**
	 * Page sized obtained from pages provider. These do not change.
	 */
	private int pageSizes[][];

	/**
	 * hold the currently displayed page
	 */
	private int currentPage = 0;

	/**
	 * avoid too much allocations in rectsintersect()
	 */
	private Rect r1 = new Rect();

	/**
	 * Bookmarked page to go to.
	 */
	private BookmarkEntry bookmarkToRestore = null;

	/**
	 * Construct this view.
	 * 
	 * @param activity
	 *            parent activity
	 */

	private GestureDetector gestureDetector = null;
	private Scroller scroller = null;

	private boolean verticalScrollLock = true;
	private boolean lockedVertically = false;
	private float downX = 0;
	private float downY = 0;
	private float lastX = 0;
	private float lastY = 0;
	private float maxExcursionY = 0;

	public PagesGridView(Activity activity) {
		
		super(activity);
		this.activity = activity;
		this.lastControlsUseMillis = System.currentTimeMillis();
		this.setOnTouchListener(this);
		activity.setDefaultKeyMode(Activity.DEFAULT_KEYS_SEARCH_LOCAL);

		this.scroller = null; 

		this.gestureDetector = new GestureDetector(activity, new GestureDetector.OnGestureListener() {
			public boolean onDown(MotionEvent e) {
				return false;
			}

			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

				doFling(velocityX, velocityY);
				return true;
			}

			public void onLongPress(MotionEvent e) {
			}

			public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
				return false;
			}

			public void onShowPress(MotionEvent e) {
			}

			public boolean onSingleTapUp(MotionEvent e) {
				return false;
			}
		});

	}
	boolean bBookmarkView = false;
	public boolean getBookMarkView(){
		return bBookmarkView;
	}
	public boolean setBookMarkView(boolean bFlag){
		bBookmarkView = bFlag;
		//ReDraw
		this.invalidate();
		return bBookmarkView;
	}
	ArrayList<BookmarkEntry> alBookMark;
	HashMap<Integer, String> hmBookMark;
	public void getBookmark(Bookmark b, String bookmarkName) {
		hmBookMark = new HashMap<Integer, String>();
		if (b != null) {
			this.alBookMark = b.getBookmarks(bookmarkName);			
			if(this.alBookMark != null){
				for(BookmarkEntry bm:alBookMark){
					DebugLog.out(TAG,"PAGEGRID BOOKMARK Entry : " + bm.page);					
					hmBookMark.put(bm.page, bm.comment);
				}
			}
		}
	}
	
	public void setStartBookmark(Bookmark b, String bookmarkName) {
		if (b != null) {
			this.bookmarkToRestore = b.getLast(bookmarkName);
			DebugLog.out(TAG,"PAGEGRID BOOKMARK TotalPage :" + bookmarkToRestore.numberOfPages + " currentPage :" + bookmarkToRestore.page);
			if (this.bookmarkToRestore == null)
				return;
			if (this.bookmarkToRestore.numberOfPages != this.pageSizes.length) {
				this.bookmarkToRestore = null;
				return;
			}
			if (0 < this.bookmarkToRestore.page) {
				this.currentPage = this.bookmarkToRestore.page;
			}
		}
	}
	// Thumbnail Image Width 
	private int iThumbWidth = getResources().getDimensionPixelSize(R.dimen.thumb_width);
	/**
	 * Handle size change event. Update base scaling, move zoom controls to
	 * correct place etc.
	 * 
	 * @param w
	 *            new width
	 * @param h
	 *            new height
	 * @param oldw
	 *            old width
	 * @param oldh
	 *            old height
	 */
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		this.width = w;
		this.height = h;
		if (this.scaling0 == 0f) {
			this.scaling0 = Math.min(((float) this.height - 2 * marginY) / (float) this.pageSizes[0][1],
									 ((float) iThumbWidth - 2 * marginX) / (float) this.pageSizes[0][0]);
		}
		DebugLog.out(TAG, "onSizeChanged :" + this.width + " " + this.height + " Scale:" + this.scaling0);
		if (oldw == 0 && oldh == 0) {
			goToBookmark();
		}
	}

	public void goToBookmark() {
		if (this.bookmarkToRestore == null 
				|| this.bookmarkToRestore.page < 0 || this.bookmarkToRestore.page >= this.pageSizes.length) {
		
		} else {
			DebugLog.out(TAG,"PAGEGRID goToBookmark TotalPage :" + bookmarkToRestore.numberOfPages + " currentPage :" + bookmarkToRestore.page);
			//this.zoomLevel = (int) (this.bookmarkToRestore.absoluteZoomLevel / this.scaling0);
			//this.rotation = this.bookmarkToRestore.rotation;
			//Point pos = getPagePositionInDocumentWithZoom(this.bookmarkToRestore.page);
			this.currentPage = this.bookmarkToRestore.page;
//			this.top = pos.y + this.height / 2;
			//this.left = this.getCurrentPageWidth(this.currentPage) / 2 + marginX ;//+ this.bookmarkToRestore.offsetX;
			this.bookmarkToRestore = null;
		}
		this.top = this.height / 2;
		this.left = this.width / 2;
		scrollToPage(this.currentPage);
	}

	public void setPagesProvider(PagesProvider pagesProvider) {
		this.pagesProvider = pagesProvider;
		if (this.pagesProvider != null) {
			this.pageSizes = this.pagesProvider.getPageSizes();

			maxRealPageSize[0] = 0f;
			maxRealPageSize[1] = 0f;
			realDocumentSize[0] = 0f;
			realDocumentSize[1] = 0f;

			for (int i = 0; i < this.pageSizes.length; i++) {
				for (int j = 0; j < 2; j++) {
					if (pageSizes[i][j] > maxRealPageSize[j])
						maxRealPageSize[j] = pageSizes[i][j];
					realDocumentSize[j] += pageSizes[i][j];
				}
			}
			
			if (this.width > 0 && this.height > 0) {
//				this.scaling0 = Math.min(((float) this.height - 2 * marginY) / ((float) this.pageSizes[0][1]),
//						((float) this.iThumbWidth - 2 * marginX) / ((float) this.pageSizes[0][0]));
				this.left = this.width / 2;
				this.top = this.height / 2;
			}
		} else {
			this.pageSizes = null;
		}
		this.pagesProvider.setOnImageRenderedListener(this);
	}

	/**
	 * Draw view.
	 * 
	 * @param canvas
	 *            what to draw on
	 */

	int prevTop = -1;
	int prevLeft = -1;

	public void onDraw(Canvas canvas) {
		this.drawPages(canvas);
	}

	/**
	 * Get current maximum page width by page number taking into account zoom
	 * and rotation
	 */
	private int getCurrentMaxPageWidth() {
		float realpagewidth = this.maxRealPageSize[0];
		return (int) scale(realpagewidth);
	}

	/**
	 * Get current maximum page height by page number taking into account zoom
	 * and rotation
	 */
	/*
	 * private int getCurrentMaxPageHeight() { float realpageheight =
	 * this.maxRealPageSize[this.rotation % 2 == 0 ? 1 : 0]; return
	 * (int)(realpageheight * scaling0 * (this.zoomLevel*0.001f)); }
	 */

	/**
	 * Get current maximum page width by page number taking into account zoom
	 * and rotation
	 */
	private int getCurrentDocumentHeight() {
		float realheight;
		if(bBookmarkView){
			realheight = hmBookMark.size() * getCurrentPageHeight(0) / 2;
//			realheight = this.realDocumentSize[1] / 2;		
		}else{
			realheight = this.realDocumentSize[1] / 2;			
		}
		/* we add pageSizes.length to account for round-off issues */
		return (int) (scale(realheight) + (pageSizes.length / 2  - 1) * this.getCurrentMarginY());
	}

	/**
	 * Get current page width by page number taking into account zoom and
	 * rotation
	 * 
	 * @param pageno
	 *            0-based page number
	 */
	private int getCurrentPageWidth(int pageno) {
		float realpagewidth = (float) this.pageSizes[pageno][0];
		float currentpagewidth = scale(realpagewidth);
		return (int) currentpagewidth;
	}

	private float scale(float unscaled) {
		return unscaled * scaling0; // * this.zoomLevel * 0.001f;
	}

	/**
	 * Get current page height by page number taking into account zoom and
	 * rotation.
	 * 
	 * @param pageno
	 *            0-based page number
	 */
	private float getCurrentPageHeight(int pageno) {
		float realpageheight = (float) this.pageSizes[pageno][1];
		float currentpageheight = scale(realpageheight);
		return currentpageheight;
	}

	private float getCurrentMarginX() {
		marginX = (getWidth() - (iThumbWidth) * 2 ) / 4;
		return marginX;//scale((float) marginX);
	}

	private float getCurrentMarginY() {
		return marginY;// scale((float) marginY);
	}

	/**
	 * This takes into account zoom level.
	 */
	private Point getPagePositionInDocumentWithZoom(int page) {
		float marginX = this.getCurrentMarginX();
		float marginY = this.getCurrentMarginY();
		float left = marginX;
		float top = 0;
		for (int i = 0; i < (page / 2); i++) {
			top += this.getCurrentPageHeight(i);
		}
		top += ((page/2) + 1) * marginY;

		return new Point((int) left, (int) top);
	}

	/**
	 * Calculate screens (viewports) top-left corner position over document.
	 */
	private Point getScreenPositionOverDocument() {
		float top = this.top - this.height / 2;
		float left = this.left - this.width / 2;
		return new Point((int) left, (int) top);
	}

	/**
	 * Calculate current page position on screen in pixels.
	 * 
	 * @param page
	 *            base-0 page number
	 */
	private Point getPagePositionOnScreen(int page) {
		if (page < 0)
			throw new IllegalArgumentException("page must be >= 0: " + page);
		if (page >= this.pageSizes.length)
			throw new IllegalArgumentException("page number too big: " + page);

		Point pagePositionInDocument = this.getPagePositionInDocumentWithZoom(page);
		Point screenPositionInDocument = this.getScreenPositionOverDocument();

		return new Point(pagePositionInDocument.x - screenPositionInDocument.x, pagePositionInDocument.y
				- screenPositionInDocument.y);
	}

	@Override
	public void computeScroll() {
		if (this.scroller == null)
			return;

		if (this.scroller.computeScrollOffset()) {
			left = this.scroller.getCurrX();
			top = this.scroller.getCurrY();
			//((cx.hell.android.pdfview.OpenFileActivity) activity).showPageNumber(false);
			postInvalidate();
		}
	}
	int iOffsetY = 0;
	/**
	 * Draw pages. Also collect info what's visible and push this info to page
	 * renderer.
	 */
	private void drawPages(Canvas canvas) {

		Rect src = new Rect(); /* TODO: move out of drawPages */
		Rect dst = new Rect(); /* TODO: move out of drawPages */
		int pageWidth = 0;
		int pageHeight = 0;
		float pagex0, pagey0, pagex1, pagey1; // in doc, counts zoom
		int x, y; // on screen
		int viewx0, viewy0; // view over doc
		LinkedList<Tile> visibleTiles = new LinkedList<Tile>();
		float currentMarginX = this.getCurrentMarginX();
		float currentMarginY = this.getCurrentMarginY();
		float renderAhead = this.pagesProvider.getRenderAhead();
//		Bitmap bmBookMark = BitmapFactory.decodeResource(getResources(), R.drawable.bookmark01);
		if (this.pagesProvider != null) {
			if (this.zoomLevel < 5)
				this.zoomLevel = 5;

			viewx0 = left - width / 2;
			viewy0 = top - height / 2;

			int pageCount = this.pageSizes.length;

			/*
			 * We now adjust the position to make sure we don't scroll too far
			 * away from the document text.
			 */
			int oldviewx0 = viewx0;
			int oldviewy0 = viewy0;

			viewx0 = adjustPosition(viewx0, width, (int) currentMarginX, getCurrentMaxPageWidth());
			viewy0 = adjustPosition(viewy0, height, (int) currentMarginY, (int) getCurrentDocumentHeight());

			left += viewx0 - oldviewx0;
			top += viewy0 - oldviewy0;
			iOffsetY = viewy0;
			float currpageoffy = currentMarginY;
			float currpageoffx = currentMarginX;
			this.currentPage = -1;
			
			pagey0 = 0;
			int[] tileSizes = new int[2];
			int iPageCount = 0; 
			for (int i = 0; i < pageCount; ++i) {
				// is page i visible?
				
				pageWidth = iThumbWidth;//this.getCurrentPageWidth(i);
				pageHeight = (int) this.getCurrentPageHeight(i);
				if(bBookmarkView == true){
					if(!hmBookMark.containsKey(i)){
						continue;						
					}
				}
				
				pagex0 = currpageoffx;
				pagex1 = (int) (currpageoffx + pageWidth);
				pagey0 = currpageoffy;
				pagey1 = (int) (currpageoffy + pageHeight);
				
				
	            //DebugLog.out("PageGridView", "Draw page:" + i + " x:" + pagex0 + " y:" + pagey0 + " W:" + this.getCurrentPageWidth(i) + " H:" + this.getCurrentPageHeight(i));
				
				if (rectsintersect((int) pagex0, (int) pagey0, (int) pagex1, (int) pagey1, 
						viewx0, viewy0, viewx0 + this.width, viewy0 + (int) (renderAhead * this.height) )) {
					if (this.currentPage == -1) {
						// remember the currently displayed page
						this.currentPage = i;
					}
										
					x = (int) pagex0;// - viewx0;
					y = (int) pagey0 - viewy0;
					
					getGoodTileSizes(tileSizes, pageWidth, pageHeight);

					for (int tileix = 0; tileix < (pageWidth + tileSizes[0] - 1) / tileSizes[0]; ++tileix)
						for (int tileiy = 0; tileiy < (pageHeight + tileSizes[1] - 1) / tileSizes[1]; ++tileiy) {

							dst.left = (int) (x + tileix * tileSizes[0]);
							dst.top = (int) (y + tileiy * tileSizes[1]);
							dst.right = dst.left + tileSizes[0];
							dst.bottom = dst.top + tileSizes[1];

							if (dst.intersects(0, 0, this.width, (int) (renderAhead * this.height))) {

								Tile tile = new Tile(i, (int) (this.zoomLevel * scaling0), tileix * tileSizes[0],
										tileiy * tileSizes[1], 0, tileSizes[0], tileSizes[1]);

								if (dst.intersects(0, 0, this.width, this.height)) {
									Bitmap b = this.pagesProvider.getPageBitmap(tile);
									if (b != null) {
										src.left = 0;
										src.top = 0;
										src.right = b.getWidth();
										src.bottom = b.getHeight();

										if (dst.right > x + pageWidth) {
											src.right = (int) (b.getWidth() * (float) ((x + pageWidth) - dst.left) / (float) (dst.right - dst.left));
											dst.right = (int) (x + pageWidth);
										}

										if (dst.bottom > y + pageHeight) {
											src.bottom = (int) (b.getHeight() * (float) ((y + pageHeight) - dst.top) / (float) (dst.bottom - dst.top));
											dst.bottom = (int) (y + pageHeight);
										}

										drawBitmap(canvas, b, src, dst);
//										if(hmBookMark.containsKey(i))
//											canvas.drawBitmap(bmBookMark, x + pageWidth - 32, y, null);
									}
								}
								visibleTiles.add(tile);
							}
						}
				}

				/* move to next page */
				if ((iPageCount % 2) == 0) {
					// Ȧ��
					currpageoffx = currentMarginX * 3 + pageWidth;
					//currpageoffy += currentMarginY + pageHeight;// this.getCurrentPageHeight(i);
				} else {
					// ¦��
					currpageoffx = currentMarginX;
					currpageoffy += currentMarginY + pageHeight;//this.getCurrentPageHeight(i);
				}
				iPageCount++;
			}
			this.pagesProvider.setVisibleTiles(visibleTiles);
		}
	}

	private void drawBitmap(Canvas canvas, Bitmap b, Rect src, Rect dst) {
//		if (colorMode != Options.COLOR_MODE_NORMAL) {
//			Paint paint = new Paint();
//			Bitmap out;
//
//			if (b.getConfig() == Bitmap.Config.ALPHA_8) {
//				out = b.copy(Bitmap.Config.ARGB_8888, false);
//			} else {
//				out = b;
//			}
//
//			paint.setColorFilter(new ColorMatrixColorFilter(new ColorMatrix(Options.getColorModeMatrix(this.colorMode))));
//
//			canvas.drawBitmap(out, src, dst, paint);
//
//			if (b.getConfig() == Bitmap.Config.ALPHA_8) {
//				out.recycle();
//			}
//		} else {
			canvas.drawBitmap(b, src, dst, null);
//		}
	}

	private boolean unlocksVerticalLock(MotionEvent e) {
		float dx;
		float dy;

		dx = Math.abs(e.getX() - downX);
		dy = Math.abs(e.getY() - downY);

		if (dy > 0.25 * dx || maxExcursionY > 0.8 * dx)
			return false;

		return dx > width / 5 || dx > height / 5;
	}

	/**
	 * Handle touch event coming from Android system.
	 */
	public boolean onTouch(View v, MotionEvent event) {
		this.lastControlsUseMillis = System.currentTimeMillis();
		if (!gestureDetector.onTouchEvent(event)) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				downX = event.getX();
				downY = event.getY();
				lastX = downX;
				lastY = downY;
				lockedVertically = verticalScrollLock;
				maxExcursionY = 0;
				scroller = null;
			} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
				if (lockedVertically && unlocksVerticalLock(event))
					lockedVertically = false;

				float dx = event.getX() - lastX;
				float dy = event.getY() - lastY;

				{
					float excursionY = Math.abs(event.getY() - downY);
					if (excursionY > maxExcursionY)
						maxExcursionY = excursionY;
					if (lockedVertically)
						dx = 0;
					doScroll((int) -dx, (int) -dy);
				}

				lastX = event.getX();
				lastY = event.getY();
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				int iOffY = ((int) this.getCurrentPageHeight(0) + (int)this.getCurrentMarginY());				
				if(oldControlsUseMillis != 0){
					// 500ms ���� ���� Ŭ���� ��� 
					if((lastControlsUseMillis - oldControlsUseMillis) < (5 * 100)){
						DebugLog.out(TAG, "ACTION_UP  X:" + (int)event.getX() + " Y: " + (int)(event.getY() + iOffsetY) + "/" + (int)getCurrentDocumentHeight()
								+ " page:" + (((int)(event.getY() + iOffsetY) / iOffY * 2) + ((int)event.getX() > this.width/2?1:0)));
						
						int iPage = (((int)(event.getY() + iOffsetY) / iOffY * 2) + ((int)event.getX() > this.width/2?1:0));
						if(bBookmarkView){
							int iTemp = 0; 
							for(int i=0; i < pageSizes.length; i++){
								
								if(hmBookMark.containsKey(i)){
									if(iTemp == iPage){
										iTemp = i;
										break;
									}else{
										iTemp++;
									}
								}
							}
							if(this.pageSizes.length > iTemp){
								this.currentPage = iTemp;
								DebugLog.out(TAG, "BV Double Click :" + this.currentPage);
								Constance.pdf_CurrentPage = this.currentPage + 1;
								((cx.hell.android.pdfview.OpenFileActivity) activity).saveLastPage();
								((cx.hell.android.pdfview.OpenFileActivity) activity).changeView();							
							}							
						}else{
							if(this.pageSizes.length > iPage){
								this.currentPage = iPage;
								DebugLog.out(TAG, "Double Click :" + this.currentPage);
								Constance.pdf_CurrentPage = this.currentPage + 1;
								((com.artifex.mupdfdemo.MuPDFActivity) activity).saveLastPage();
								((com.artifex.mupdfdemo.MuPDFActivity) activity).ccc();							
							}							
						}
					}
				}
				oldControlsUseMillis = lastControlsUseMillis;
			
			}

		}
		return true;
	}

	/**
	 * Test if specified rectangles intersect with each other. Uses Androids
	 * standard Rect class.
	 */
	private boolean rectsintersect(int r1x0, int r1y0, int r1x1, int r1y1, int r2x0, int r2y0, int r2x1, int r2y1) {
		r1.set(r1x0, r1y0, r1x1, r1y1);
		return r1.intersects(r2x0, r2y0, r2x1, r2y1);
	}

	/**
	 * Used as a callback from pdf rendering code. TODO: only invalidate what
	 * needs to be painted, not the whole view
	 */
	public void onImagesRendered(Map<Tile, Bitmap> renderedTiles) {
		Rect rect = new Rect(); /* TODO: move out of onImagesRendered */

		int viewx0 = left - width / 2;
		int viewy0 = top - height / 2;

		int pageCount = this.pageSizes.length;
		float currentMarginX = this.getCurrentMarginX();
		float currentMarginY = this.getCurrentMarginY();

		viewx0 = adjustPosition(viewx0, width, (int) currentMarginX, getCurrentMaxPageWidth());
		viewy0 = adjustPosition(viewy0, height, (int) currentMarginY, (int) getCurrentDocumentHeight());

		float currpageoffy = currentMarginY;
		float currpageoffx = currentMarginX;
		float renderAhead = this.pagesProvider.getRenderAhead();

		float pagex0;
		float pagex1;
		float pagey0 = 0;
		float pagey1;
		float x;
		float y;
		int pageWidth;
		int pageHeight;
		int iPageCount = 0;
		for (int i = 0; i < pageCount; ++i) {
			// is page i visible?

			pageWidth = iThumbWidth;//this.getCurrentPageWidth(i);
			pageHeight = (int) this.getCurrentPageHeight(i);

			if(bBookmarkView == true){
				if(!hmBookMark.containsKey(i)){
					continue;						
				}
			}
			// ¦��
			pagex0 = currpageoffx;
			pagex1 = (int) (currpageoffx + pageWidth);
			pagey0 = currpageoffy;
			pagey1 = (int) (currpageoffy + pageHeight);
			//DebugLog.out("PageGridView", "onImagesRendered page:" + i + " x:" + pagex0 + " y:" + pagey0);

			if (rectsintersect((int) pagex0, (int) pagey0, (int) pagex1, (int) pagey1, 
					viewx0, viewy0, viewx0 + this.width, viewy0 + this.height)) {
				x = pagex0;// - viewx0;
				y = pagey0 - viewy0;

				for (Tile tile : renderedTiles.keySet()) {
					if (tile.getPage() == i) {
						Bitmap b = renderedTiles.get(tile);

						rect.left = (int) (x + tile.getX());
						rect.top = (int) (y + tile.getY());
						rect.right = rect.left + b.getWidth();
						rect.bottom = rect.top + b.getHeight();

						if (rect.intersects(0, 0, this.width, (int) (renderAhead * this.height))) {
							//DebugLog.out(TAG, "New bitmap forces redraw");
							postInvalidate();
							return;
						}
					}
				}

			}
			/* move to next page */
			if ((iPageCount % 2) == 0) {
				// Ȧ��
				currpageoffx = currentMarginX * 3 + pageWidth;
				//currpageoffy += currentMarginY + pageHeight;// this.getCurrentPageHeight(i);
			} else {
				// ¦��
				currpageoffx = currentMarginX;
				currpageoffy += currentMarginY + pageHeight;//this.getCurrentPageHeight(i);
			}
			iPageCount++;
		}
		DebugLog.out(TAG, "New bitmap does not require redraw");
	}

	/**
	 * Handle rendering exception. Show error message and then quit parent
	 * activity. TODO: find a proper way to finish an activity when something
	 * bad happens in view.
	 */
	public void onRenderingException(RenderingException reason) {
		final Activity activity = this.activity;
		final String message = reason.getMessage();
		this.post(new Runnable() {
			public void run() {
				AlertDialog errorMessageDialog = new AlertDialog.Builder(activity).setTitle("Error")
						.setMessage(message).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								activity.finish();
							}
						}).setOnCancelListener(new DialogInterface.OnCancelListener() {
							public void onCancel(DialogInterface dialog) {
								activity.finish();
							}
						}).create();
				errorMessageDialog.show();
			}
		});
	}

	synchronized public void scrollToPage(int page) {
		scrollToPage(page, true);
	}

	public float pagePosition(int page) {
		float top = 0;

		for (int i = 0; i < page; i+=2) {
			top += this.getCurrentPageHeight(i);
		}

		if (page > 0)
			top +=  marginY * (int) (page / 2);

		return top;
	}

	/**
	 * Move current viewport over n-th page. Page is 0-based.
	 * 
	 * @param page
	 *            0-based page number
	 */
	synchronized public void scrollToPage(int page, boolean positionAtTop) {
		float top;

		if (positionAtTop) {
			top =  pagePosition(page);
		} else {
			top = this.top - pagePosition(currentPage) + pagePosition(page);
		}

		this.top = (int) top;
		this.currentPage = page;
		this.invalidate();
	}

	/**
	 * Get the current page number
	 * 
	 * @return the current page. 0-based
	 */
	public int getCurrentPage() {
		return currentPage;
	}

	/**
	 * Get the current zoom level
	 * 
	 * @return the current zoom level
	 */
	public int getCurrentAbsoluteZoom() {
		return zoomLevel;
	}

	/**
	 * Get page count.
	 */
	public int getPageCount() {
		return this.pageSizes.length;
	}

	private void doFling(float vx, float vy) {
		float avx = vx > 0 ? vx : -vx;
		float avy = vy > 0 ? vy : -vy;

		if (avx < .25 * avy) {
			vx = 0;
		} else if (avy < .25 * avx) {
			vy = 0;
		}
		vx = 0;
		int marginX = (int) getCurrentMarginX();
		int marginY = (int) getCurrentMarginY();
		int minx = this.width / 2 + getLowerBound(this.width, marginX, getCurrentMaxPageWidth());
		int maxx = this.width / 2 + getUpperBound(this.width, marginX, getCurrentMaxPageWidth());
		int miny = this.height / 2 + getLowerBound(this.height, marginY, getCurrentDocumentHeight());
		int maxy = this.height / 2 + getUpperBound(this.height, marginY, getCurrentDocumentHeight());
		this.scroller = new Scroller(activity);
		
		this.scroller.fling(this.left, this.top, (int) -vx, (int) -vy, minx, maxx, miny, maxy);
		invalidate();
	}

	private void doScroll(int dx, int dy) {
		//this.left += dx;
		this.top += dy;
		invalidate();
	}

	private void getGoodTileSizes(int[] sizes, int pageWidth, int pageHeight) {
		sizes[0] = getGoodTileSize(pageWidth, MIN_TILE_WIDTH, MAX_TILE_WIDTH);
		sizes[1] = getGoodTileSize(pageHeight, MIN_TILE_HEIGHT, MAX_TILE_PIXELS / sizes[0]);
	}

	private int getGoodTileSize(int pageSize, int minSize, int maxSize) {
		if (pageSize <= 2)
			return 2;
		if (pageSize <= maxSize)
			return pageSize;
		int numInPageSize = (pageSize + maxSize - 1) / maxSize;
		int proposedSize = (pageSize + numInPageSize - 1) / numInPageSize;
		if (proposedSize < minSize)
			return minSize;
		else
			return proposedSize;
	}

	/*
	 * Get the upper and lower bounds for the viewpoint. The document itself is
	 * drawn from margin to margin+docDim.
	 */
	private int getLowerBound(int screenDim, int margin, int docDim) {
		if (docDim <= screenDim) {
			/* all pages can and do fit */
			return margin + docDim - screenDim;
		} else {
			/* document is too wide/tall to fit */
			return 0;
		}
	}

	private int getUpperBound(int screenDim, int margin, int docDim) {
		if (docDim <= screenDim) {
			/* all pages can and do fit */
			return margin;
		} else {
			/* document is too wide/tall to fit */
			return 2 * margin + docDim - screenDim;
		}
	}

	private int adjustPosition(int pos, int screenDim, int margin, int docDim) {
		int min = getLowerBound(screenDim, margin, docDim);
		int max = getUpperBound(screenDim, margin, docDim);
		//DebugLog.out("PagesGridView", "adjustPosition pos:" + pos + " screen:" + screenDim + " margin:" + margin + " Doc:"+ docDim);
		if (pos < min)
			return min;
		else if (max < pos)
			return max;
		else
			return pos;
	}

	public BookmarkEntry toBookmarkEntry() {
		return new BookmarkEntry(this.pageSizes.length, this.currentPage, scaling0 * zoomLevel, 0, this.left
				- this.getCurrentPageWidth(this.currentPage) / 2 - marginX);
	}
	public void clearRenderer(){
		this.pagesProvider.clearTiles();
	}
//	public void setSideMargins(int margin) {
//		this.marginX = margin;
//	}
//
//	public void setTopMargin(int margin) {
//		int delta = margin - this.marginY;
//		top += this.currentPage * delta;
//		this.marginY = margin;
//	}
}
