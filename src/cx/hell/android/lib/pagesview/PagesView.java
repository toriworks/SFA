package cx.hell.android.lib.pagesview;

import java.util.LinkedList;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.FloatMath;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;


import cx.hell.android.pdfview.Actions;
import cx.hell.android.pdfview.AndroidReflections;
import cx.hell.android.pdfview.Bookmark;
import cx.hell.android.pdfview.BookmarkEntry;
import cx.hell.android.pdfview.Options;
import fnc.salesforce.android.Constance.Constance;
import fnc.salesforce.android.LIB.DebugLog;
import fnc.salesforce.android.LIB.KumaLog;

/**
 * View that simplifies displaying of paged documents. TODO: redesign zooms,
 * pages, margins, layout TODO: use more floats for better align, or use more
 * ints for performance ;) (that is, really analyse what should be used when)
 */
public class PagesView extends View implements View.OnTouchListener, OnImageRenderedListener {
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

	/**
	 * Space between screen edge and page and between pages.
	 */
	private int marginX = 20;
	private int marginY = 10;

	/* zoom steps */
	float step = 1.414f;

	private Activity activity = null;

	/**
	 * Source of page bitmaps.
	 */
	private PagesProvider pagesProvider = null;

	@SuppressWarnings("unused")
	private long lastControlsUseMillis = 0;

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

	private int MAX_ZoomLevel = 9000;

	private int MIN_ZoomLevel = 800;

	/**
	 * Current rotation of pages.
	 */
	private int rotation = 0;

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
	private int doubleTapAction = Options.DOUBLE_TAP_ZOOM_IN_OUT;
	private int zoomCount = 0;
	private Actions actions = null;

	// false : Port , true : Land
	private boolean bDocOrientation = true;

	public PagesView(Activity activity) {
		super(activity);
		this.activity = activity;
		this.actions = null;
		this.lastControlsUseMillis = System.currentTimeMillis();

		this.setOnTouchListener(this);
		activity.setDefaultKeyMode(Activity.DEFAULT_KEYS_SEARCH_LOCAL);

		this.scroller = null; // new Scroller(activity);

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

		gestureDetector.setOnDoubleTapListener(new OnDoubleTapListener() {
			public boolean onDoubleTap(MotionEvent e) {
				switch (doubleTapAction) {
				case Options.DOUBLE_TAP_ZOOM_IN_OUT:
					DebugLog.out(TAG, "DOUBLE_TAP_ZOOM_IN_OUT :" + zoomCount);
//					if ((zoomCount > 2) && (zoomLevel > MAX_ZoomLevel)) {
//						zoomFit();
//						invalidate();
//						zoomCount = 0;
//					} else {
//						if(zoomLevel > MAX_ZoomLevel){
//							zoomFit();
//							invalidate();
//							zoomCount = 0;
//						}else{
//							left += e.getX() - width / 2;
//							top += e.getY() - height / 2;
//							zoom(2f);
//							zoomCount++;							
//						}
//					}
					if(zoomLevel >= MAX_ZoomLevel){
						zoomFit();
						invalidate();
					}else{
						left += e.getX() - width / 2;
						top += e.getY() - height / 2;
						zoom(2f);
					}
					return true;
				case Options.DOUBLE_TAP_ZOOM:
					left += e.getX() - width / 2;
					top += e.getY() - height / 2;
					zoom(2f);
					return true;
				default:
					return false;
				}
			}

			public boolean onDoubleTapEvent(MotionEvent e) {
				return false;
			}

			public boolean onSingleTapConfirmed(MotionEvent e) {
				doSingleTab();
				return  true;//doAction(actions.getAction(e.getY() < height / 2 ? Actions.TOP_TAP : Actions.BOTTOM_TAP));
			}
		});
	}

	public void setStartBookmark(Bookmark b, String bookmarkName) {
		if (b != null) {
			this.bookmarkToRestore = b.getLast(bookmarkName);
			if (this.bookmarkToRestore == null)
				return;
			if (this.bookmarkToRestore.numberOfPages != this.pageSizes.length) {
				this.bookmarkToRestore = null;
				return;
			}
			DebugLog.out(TAG, "PAGE BOOKMARK TotalPage :" + bookmarkToRestore.numberOfPages + " currentPage :"
					+ bookmarkToRestore.page);
			if (0 < this.bookmarkToRestore.page) {
				this.currentPage = this.bookmarkToRestore.page;
			}
		}
	}

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
					((float) this.width - 2 * marginX) / (float) this.pageSizes[0][0]);
		}
		DebugLog.out(TAG, "onSizeChanged w:" + width + " h:" + height + " scale:" + scaling0);
		if (oldw == 0 && oldh == 0) {
			goToBookmark();
		}
	}

	public void goToBookmark() {
		if (this.bookmarkToRestore == null || this.bookmarkToRestore.page < 0
				|| this.bookmarkToRestore.page >= this.pageSizes.length) {
			this.top = this.height / 2;
			this.left = this.width / 2;
		} else {
			Point pos = getPagePositionInDocumentWithZoom(this.bookmarkToRestore.page);
			this.currentPage = this.bookmarkToRestore.page;
			
			Constance.pdf_CurrentPage = this.currentPage + 1;

			DebugLog.out(TAG, "PAGE goToBookmark currentPage :" + bookmarkToRestore.page + " Position:" + pos.x);
			if (bDocOrientation) {
				this.top = this.height / 2;
				this.left = pos.x + this.getCurrentPageWidth(this.currentPage) / 2;
			} else {
				this.top = pos.y + this.height / 2;
				this.left = this.getCurrentPageWidth(this.currentPage) / 2 + marginX;
			}
			this.bookmarkToRestore = null;
		}
	}

	public void setPagesProvider(PagesProvider pagesProvider) {
		this.pagesProvider = pagesProvider;
		if (this.pagesProvider != null) {
			this.pageSizes = this.pagesProvider.getPageSizes();

			maxRealPageSize[0] = 0f;
			maxRealPageSize[1] = 0f;
			realDocumentSize[0] = 0f;
			realDocumentSize[1] = 0f;

			for (int i = 0; i < this.pageSizes.length; i++)
				for (int j = 0; j < 2; j++) {
					if (pageSizes[i][j] > maxRealPageSize[j])
						maxRealPageSize[j] = pageSizes[i][j];
					realDocumentSize[j] += pageSizes[i][j];
				}

			if (this.width > 0 && this.height > 0) {
				this.scaling0 = Math.min(((float) this.height - 2 * marginY) / (float) this.pageSizes[0][1],
						((float) this.width - 2 * marginX) / (float) this.pageSizes[0][0]);
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
		this.drawPages(canvas, bDocOrientation);
	}

	/**
	 * Get current maximum page width by page number taking into account zoom
	 * and rotation
	 */
	private int getCurrentMaxPageWidth() {
		float realpagewidth = this.maxRealPageSize[this.rotation % 2 == 0 ? 0 : 1];
		return (int) scale(realpagewidth);
	}

	/**
	 * Get current maximum page height by page number taking into account zoom
	 * and rotation
	 */

	private int getCurrentMaxPageHeight() {
		float realpageheight = this.maxRealPageSize[this.rotation % 2 == 0 ? 1 : 0];
		return (int) scale(realpageheight);
	}

	/**
	 * Get current maximum page width by page number taking into account zoom
	 * and rotation
	 */
	private int getCurrentDocumentHeight() {
		float realheight = this.realDocumentSize[this.rotation % 2 == 0 ? 1 : 0];
		/* we add pageSizes.length to account for round-off issues */
		return (int) (scale(realheight) + (pageSizes.length - 1) * this.getCurrentMarginY());
	}

	private int getCurrentDocumentWidth() {
		float realwidth = this.realDocumentSize[this.rotation % 2 == 0 ? 0 : 1];
		/* we add pageSizes.length to account for round-off issues */
		return (int) (scale(realwidth) + (pageSizes.length - 1) * this.getCurrentMarginX());
	}

	/**
	 * Get current page width by page number taking into account zoom and
	 * rotation
	 * 
	 * @param pageno
	 *            0-based page number
	 */
	private int getCurrentPageWidth(int pageno) {
		float realpagewidth = (float) this.pageSizes[pageno][this.rotation % 2 == 0 ? 0 : 1];
		float currentpagewidth = scale(realpagewidth);
		return (int) currentpagewidth;
	}

	private float scale(float unscaled) {
		return unscaled * scaling0 * this.zoomLevel * 0.001f;
	}

	/**
	 * Get current page height by page number taking into account zoom and
	 * rotation.
	 * 
	 * @param pageno
	 *            0-based page number
	 */
	private int getCurrentPageHeight(int pageno) {
		float realpageheight = (float) this.pageSizes[pageno][this.rotation % 2 == 0 ? 1 : 0];
		float currentpageheight = scale(realpageheight);
		return (int) currentpageheight;
	}

	private float getCurrentMarginX() {
		return scale((float) marginX);
	}

	private float getCurrentMarginY() {
		return scale((float) marginY);
	}

	/**
	 * This takes into account zoom level.
	 */
	private Point getPagePositionInDocumentWithZoom(int page) {
		float marginX = this.getCurrentMarginX();
		float marginY = this.getCurrentMarginY();
		float left = marginX;
		float top = marginY;
		if (bDocOrientation) {
			for (int i = 0; i < page; ++i) {
				left += this.getCurrentPageWidth(i);
			}
			left += (page + 1) * marginX;
		} else {
			for (int i = 0; i < page; ++i) {
				top += this.getCurrentPageHeight(i);
			}
			top += (page + 1) * marginY;
		}
		return new Point((int) left, (int) top);
	}

	@Override
	public void computeScroll() {
		if (this.scroller == null)
			return;

		if (this.scroller.computeScrollOffset()) {
			left = this.scroller.getCurrX();
			top = this.scroller.getCurrY();
			this.currentPage = computePageNumber(left);
			((cx.hell.android.pdfview.OpenFileActivity) activity).showPageNumber(false);
			postInvalidate();
		}
	}
	public int computePageNumber(int leftpos){
		int pageNum = 0;
		int docWidth = 0;
		int pageCount = this.pageSizes.length;
		float currentMarginX = this.getCurrentMarginX();
		for(int i = 0; i < (pageCount); i++){
			docWidth += currentMarginX + this.getCurrentPageWidth(i);
			if(leftpos < docWidth){
				DebugLog.out(TAG, "computeScroll  CurrentPage:" + (i));
				pageNum = i;
				break;
			}else{
				
			}
		}		
		return pageNum;
	}
	/**
	 * Draw pages. Also collect info what's visible and push this info to page
	 * renderer. orientation : false -> vertical , true -> horizontal
	 */
	private void drawPages(Canvas canvas, boolean orientation) {
		// if (this.eink)
		{
			canvas.drawColor(Color.WHITE);
		}

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

			float currpageoff;
			if (orientation) {
				viewx0 = adjustPosition(viewx0, width, (int) currentMarginX, (int) getCurrentDocumentWidth());
				viewy0 = adjustPosition(viewy0, height, (int) currentMarginY, getCurrentMaxPageHeight());
				left += viewx0 - oldviewx0;
				top += viewy0 - oldviewy0;
				currpageoff = currentMarginX;
			} else {
				viewx0 = adjustPosition(viewx0, width, (int) currentMarginX, getCurrentMaxPageWidth());
				viewy0 = adjustPosition(viewy0, height, (int) currentMarginY, (int) getCurrentDocumentHeight());

				left += viewx0 - oldviewx0;
				top += viewy0 - oldviewy0;
				currpageoff = currentMarginY;
			}
			this.currentPage = -1;

			pagey0 = 0;
			int[] tileSizes = new int[2];

			for (int i = 0; i < pageCount; ++i) {
				// is page i visible?

				pageWidth = this.getCurrentPageWidth(i);
				pageHeight = (int) this.getCurrentPageHeight(i);
				if (orientation) {
					pagex0 = currpageoff;
					pagex1 = (int) (currpageoff + pageWidth);
					pagey0 = currentMarginY;
					pagey1 = (int) (currentMarginY + pageHeight);
				} else {
					pagex0 = currentMarginX;
					pagex1 = (int) (currentMarginX + pageWidth);
					pagey0 = currpageoff;
					pagey1 = (int) (currpageoff + pageHeight);
				}

				if (rectsintersect((int) pagex0, (int) pagey0, (int) pagex1, (int) pagey1, viewx0, viewy0, viewx0
						+ this.width, viewy0 + (int) (renderAhead * this.height))) {
					if (this.currentPage == -1) {
						// remember the currently displayed page
						this.currentPage = i;
					}

					if (bDocOrientation) {
						x = (int) pagex0 - viewx0;
						y = (int) pagey0 - viewy0;
						if (this.height > pageHeight) {
							y = (this.height - pageHeight) / 2;
						}
					} else {
						x = (int) pagex0 - viewx0;
						if (this.width > pageWidth) {
							x = (this.width - pageWidth) / 2;
						}
						y = (int) pagey0 - viewy0;
					}

					getGoodTileSizes(tileSizes, pageWidth, pageHeight);

					for (int tileix = 0; tileix < (pageWidth + tileSizes[0] - 1) / tileSizes[0]; ++tileix)
						for (int tileiy = 0; tileiy < (pageHeight + tileSizes[1] - 1) / tileSizes[1]; ++tileiy) {

							dst.left = (int) (x + tileix * tileSizes[0]);
							dst.top = (int) (y + tileiy * tileSizes[1]);
							dst.right = dst.left + tileSizes[0];
							dst.bottom = dst.top + tileSizes[1];

							if (dst.intersects(0, 0, this.width, (int) (renderAhead * this.height))) {

								Tile tile = new Tile(i, (int) (this.zoomLevel * scaling0), tileix * tileSizes[0],
										tileiy * tileSizes[1], this.rotation, tileSizes[0], tileSizes[1]);
								if (dst.intersects(0, 0, this.width, this.height)) {
									Bitmap b = this.pagesProvider.getPageBitmap(tile);
									if (b != null) {
										// DebugLog.out(TAG, "  have bitmap: " + b +
										// ", size: " + b.getWidth() + " x " +
										// b.getHeight());
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

										canvas.drawBitmap(b, src, dst, null);

									}
								}
								visibleTiles.add(tile);
							}
						}
				}

				/* move to next page */
				if (orientation) {
					currpageoff += currentMarginX + this.getCurrentPageWidth(i);

				} else {
					currpageoff += currentMarginY + this.getCurrentPageHeight(i);
				}
			}
			this.pagesProvider.setVisibleTiles(visibleTiles);
		}
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

	String mode = "DRAG";
	private float oldDist = 1f;

	float oldZoomDistance;
	float zoomDistance;

	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	private float getZoomDistance(MotionEvent ev) {
		// Andy.Ko 2013.01.31 ��Ƽ��ġ ��� Bug fix
		// Begin ->
		float dx = AndroidReflections.getMotionEventX(ev, 0) - AndroidReflections.getMotionEventX(ev, 1);
        float dy = AndroidReflections.getMotionEventY(ev, 0) - AndroidReflections.getMotionEventY(ev, 1);
        return (float)Math.sqrt(dx*dx+dy*dy);
        // return (float) Math.sqrt(Math.pow(ev.getX(0) - ev.getX(1), 2) + Math.pow(ev.getY(0) - ev.getY(1), 2));
        // <- End
	}

	float zoomScale = 1.0f;

	/**
	 * Handle touch event coming from Android system.
	 */
	public boolean onTouch(View v, MotionEvent event) {
		DebugLog.out("PagesView", "onTouch");
		this.lastControlsUseMillis = System.currentTimeMillis();
		if (!gestureDetector.onTouchEvent(event)) {
			if ((event.getAction() & MotionEvent.ACTION_POINTER_DOWN) == MotionEvent.ACTION_POINTER_DOWN) {
				oldDist = spacing(event);
				downX = event.getX();
				downY = event.getY();
				lastX = downX;
				lastY = downY;
				lockedVertically = verticalScrollLock;
				maxExcursionY = 0;
				scroller = null;
				if (oldDist > 10f) {
					oldZoomDistance = getZoomDistance(event);
					mode = "ZOOM";
				} else {
					mode = "DRAG";
				}
			} else if ((event.getAction() & MotionEvent.ACTION_POINTER_UP) == MotionEvent.ACTION_POINTER_UP) {
				if (oldZoomDistance != 0) {
					DebugLog.out(TAG, "ACTION_POINTER_UP ZoomLevel:" + zoomScale);
					zoom(zoomScale);
				}
				oldZoomDistance = 0;
			} else if (event.getAction() == MotionEvent.ACTION_DOWN) {
				downX = event.getX();
				downY = event.getY();
				lastX = downX;
				lastY = downY;
				lockedVertically = verticalScrollLock;
				maxExcursionY = 0;
				scroller = null;
				mode = "DRAG";
			} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
				if (lockedVertically && unlocksVerticalLock(event))
					lockedVertically = false;

				float dx = event.getX() - lastX;
				float dy = event.getY() - lastY;

				if (mode.equals("ZOOM")) {
					zoomDistance = getZoomDistance(event);
					if (oldZoomDistance != 0) {
						zoomScale *= zoomDistance / oldZoomDistance;
						DebugLog.out(TAG, "ZOOM  zoom: " + this.zoomLevel + " Distance:" + zoomScale);
						if ((zoomScale > 0.5) && (zoomScale < 2)) {
							this.invalidate();
						} else if (zoomScale > 2) {
							zoomScale = 2.0f;
						} else if (zoomScale < 0.5) {
							zoomScale = 0.5f;
						}
					}
					oldZoomDistance = zoomDistance;
				} else {
					// DebugLog.out(TAG, "DRAG  x:" + dx + " y:" + dy + "zoom: " +
					// this.zoomLevel);
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
				oldZoomDistance = 0;
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
		float currpageoff;
		if (bDocOrientation) {
			viewx0 = adjustPosition(viewx0, width, (int) currentMarginX, (int) getCurrentDocumentWidth());
			viewy0 = adjustPosition(viewy0, height, (int) currentMarginY, getCurrentMaxPageHeight());
			currpageoff = currentMarginX;
		} else {
			viewx0 = adjustPosition(viewx0, width, (int) currentMarginX, getCurrentMaxPageWidth());
			viewy0 = adjustPosition(viewy0, height, (int) currentMarginY, (int) getCurrentDocumentHeight());
			currpageoff = currentMarginY;
		}

		float renderAhead = this.pagesProvider.getRenderAhead();

		float pagex0;
		float pagex1;
		float pagey0 = 0;
		float pagey1;
		float x;
		float y;
		int pageWidth;
		int pageHeight;

		for (int i = 0; i < pageCount; ++i) {
			// is page i visible?

			pageWidth = this.getCurrentPageWidth(i);
			pageHeight = (int) this.getCurrentPageHeight(i);

			if (bDocOrientation) {
				pagex0 = currpageoff;
				pagex1 = (int) (currpageoff + pageWidth);
				pagey0 = currentMarginY;
				pagey1 = (int) (currentMarginY + pageHeight);
			} else {
				pagex0 = currentMarginX;
				pagex1 = (int) (currentMarginX + pageWidth);
				pagey0 = currpageoff;
				pagey1 = (int) (currpageoff + pageHeight);
			}

			if (rectsintersect((int) pagex0, (int) pagey0, (int) pagex1, (int) pagey1, // page
																						// rect
																						// in
																						// doc
					viewx0, viewy0, viewx0 + this.width, viewy0 + this.height)) {
				x = pagex0 - viewx0;
				y = pagey0 - viewy0;

				for (Tile tile : renderedTiles.keySet()) {
					if (tile.getPage() == i) {
						Bitmap b = renderedTiles.get(tile);

						rect.left = (int) (x + tile.getX());
						rect.top = (int) (y + tile.getY());
						rect.right = rect.left + b.getWidth();
						rect.bottom = rect.top + b.getHeight();

						if (rect.intersects(0, 0, this.width, (int) (renderAhead * this.height))) {
							DebugLog.out(TAG, "New bitmap forces redraw");
							postInvalidate();
							return;
						}
					}
				}

			}
			if (bDocOrientation) {
				currpageoff += currentMarginX + this.getCurrentPageWidth(i);
			} else {
				currpageoff += currentMarginY + this.getCurrentPageHeight(i);
			}
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

		if (bDocOrientation) {
			for (int i = 0; i < page; ++i) {
				top += this.getCurrentPageWidth(i);
			}

			if (page > 0)
				top += scale((float) marginX) * (float) (page);
		} else {
			for (int i = 0; i < page; ++i) {
				top += this.getCurrentPageHeight(i);
			}

			if (page > 0)
				top += scale((float) marginY) * (float) (page);
		}
		// DebugLog.out(TAG, "pagePosition Page: " + page + " Position: " + top);
		return top;
	}

	/**
	 * Move current viewport over n-th page. Page is 0-based.
	 * 
	 * @param page
	 *            0-based page number
	 */
	synchronized public void scrollToPage(int page, boolean positionAtTop) {
		float top, left;
		DebugLog.out(TAG, "scrollToPage " + page);
		if (bDocOrientation) {
			if (positionAtTop) {
				left = this.width / 2 + pagePosition(page);
			} else {
				left = this.left - pagePosition(currentPage) + pagePosition(page);
			}
			this.left = (int) left;
		} else {
			if (positionAtTop) {
				top = this.height / 2 + pagePosition(page);
			} else {
				top = this.top - pagePosition(currentPage) + pagePosition(page);
			}

			this.top = (int) top;
		}
		this.currentPage = page;
		this.invalidate();
	}

	/**
	 * Rotate pages. Updates rotation variable, then invalidates view.
	 * 
	 * @param rotation
	 *            rotation
	 */
	synchronized public void rotate(int rotation) {
		this.rotation = (this.rotation + rotation) % 4;
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
	 * Get the current rotation
	 * 
	 * @return the current rotation
	 */
	public float getRotation() {
		return rotation;
	}

	/**
	 * Get page count.
	 */
	public int getPageCount() {
		return this.pageSizes.length;
	}
	private void doSingleTab(){
		DebugLog.out("PagesView", "SingleTap");
//		((cx.hell.android.pdfview.OpenFileActivity) activity).hideHeader();
	}
	private void doFling(float vx, float vy) {
		float avx = vx > 0 ? vx : -vx;
		float avy = vy > 0 ? vy : -vy;

		if (avx < .25 * avy) {
			vx = 0;
		} else if (avy < .25 * avx) {
			vy = 0;
		}

		int marginX = (int) getCurrentMarginX();
		int marginY = (int) getCurrentMarginY();

		int minx, maxx;
		int miny, maxy;
		int page = currentPage < 0 ? 0 : currentPage;

		if (bDocOrientation) {
			if (page == 0) {
				minx = this.width / 2;
				maxx = this.width / 2 + (int) pagePosition(page + 1);
			} else if (page < (this.pageSizes.length - 1)) {
				if (vx < 0) {
					minx = this.width / 2 + (int) pagePosition(page);
					maxx = this.width / 2 + (int) pagePosition(page + 1);
				} else {
					minx = this.width / 2 + (int) pagePosition(page);
					maxx = this.width / 2 + (int) pagePosition(page + 1);
				}
			} else {
				minx = this.width / 2 + (int) pagePosition(page - 1);
				maxx = this.width / 2 + (int) pagePosition(page);
			}
			miny = this.height / 2 + getLowerBound(this.height, marginY, getCurrentMaxPageHeight());
			maxy = this.height / 2 + getUpperBound(this.height, marginY, getCurrentMaxPageHeight());
		} else {
			minx = this.width / 2 + getLowerBound(this.width, marginX, getCurrentMaxPageWidth());
			maxx = this.width / 2 + getUpperBound(this.width, marginX, getCurrentMaxPageWidth());
			miny = this.height / 2 + getLowerBound(this.height, marginY, getCurrentDocumentHeight());
			maxy = this.height / 2 + getUpperBound(this.height, marginY, getCurrentDocumentHeight());
		}
		DebugLog.out(TAG, "Fling minX:" + minx + " maxX:" + maxx + " minY:" + miny + " maxY:" + maxy + " valX:" + vx
				+ " valY:" + vy);
		this.scroller = new Scroller(activity);

		this.scroller.fling(this.left, this.top, (int) -vx, (int) -vy, minx, maxx, miny, maxy);
		invalidate();
	}

	private void doScroll(int dx, int dy) {
		this.left += dx;
		this.top += dy;
		invalidate();
	}

	public void zoomWithLevel(int level) {
		this.left *= (level / this.zoomLevel);
		this.top *= (level / this.zoomLevel);
		this.zoomLevel = level;
		DebugLog.out(TAG, "zoomWithLevel level changed to " + this.zoomLevel + " level:" + level + " left:" + left + " top:"
				+ top);
		this.invalidate();
	}

	/**
	 * Zoom down one level
	 */
	public void zoom(float value) {
		int oldZoomLevel = this.zoomLevel;
		this.zoomLevel *= value;
		if(this.zoomLevel >= MAX_ZoomLevel){
			value = (float)MAX_ZoomLevel / oldZoomLevel;
			this.zoomLevel = MAX_ZoomLevel;
		}else if(this.zoomLevel < MIN_ZoomLevel){
			value = (float)MIN_ZoomLevel / oldZoomLevel;
			this.zoomLevel = MIN_ZoomLevel;
		}
		this.left *= value;
		this.top *= value;
		DebugLog.out(TAG, "zoom level changed to " + this.zoomLevel+" value:" + value + " left:" + left + " top:" + top + " value:" + value);
		this.invalidate();
	}

//	float fZoomFitScale = 1.0f;
//
//	/* zoom to width */
//	public void zoomWidth() {
//		int page = currentPage < 0 ? 0 : currentPage;
//		int pageWidth = getCurrentPageWidth(page);
//
//		this.zoomLevel = this.zoomLevel * (this.width - 2 * marginX) / pageWidth;
//
//		DebugLog.out(TAG, "zoomWidth()");
//		this.top = this.height / 2;
//		for (int i = 0; i < page; i++) {
//			this.left += getCurrentPageWidth(i) + marginX;
//		}
//		fZoomFitScale = (this.width - 2 * marginX) / pageWidth;
//		this.invalidate();
//	}
//
//	/* zoom to width */
//	public void zoomHeight() {
//		int page = currentPage < 0 ? 0 : currentPage;
//		if (bDocOrientation) {
//			int pageHeight = getCurrentPageHeight(page);
//			this.top = this.height / 2;
//			this.zoomLevel = this.zoomLevel * (this.height - 2 * marginY) / pageHeight;
//			for (int i = 0; i < page; i++) {
//				this.left += getCurrentPageWidth(i) + marginX;
//			}
//			fZoomFitScale = (this.height - 2 * marginY) / getCurrentPageHeight(page);
//		} else {
//			int pageWidth = getCurrentPageWidth(page);
//			this.top = (this.top - this.height / 2) * this.width / pageWidth + this.height / 2;
//			this.zoomLevel = this.zoomLevel * (this.width - 2 * marginX) / pageWidth;
//			this.left = (int) (this.width / 2);
//			fZoomFitScale = (this.width - 2 * marginX) / pageWidth;
//		}
//		this.invalidate();
//	}

	/* zoom to fit */
	public void zoomFit() {
		int page = currentPage < 0 ? 0 : currentPage;
		int z1 = this.zoomLevel * this.width / getCurrentPageWidth(page);
		int z2 = (int) (this.zoomLevel * this.height / getCurrentPageHeight(page));
		this.zoomLevel = z2 < z1 ? z2 : z1;
		Point pos = getPagePositionInDocumentWithZoom(page);
		this.left = this.width / 2 + pos.x;
		this.top = this.height / 2 + pos.y;
		this.invalidate();
	}

	/**
	 * Set zoom
	 */
	public void setZoomLevel(int zoomLevel) {
		if (this.zoomLevel == zoomLevel)
			return;
		this.zoomLevel = zoomLevel;
		DebugLog.out(TAG, "zoom level changed to " + this.zoomLevel);
		this.invalidate();
	}

	/**
	 * Set rotation
	 */
	public void setRotation(int rotation) {
		if (this.rotation == rotation)
			return;
		this.rotation = rotation;
		DebugLog.out(TAG, "rotation changed to " + this.rotation);
		this.invalidate();
	}

	public void setVerticalScrollLock(boolean verticalScrollLock) {
		this.verticalScrollLock = verticalScrollLock;
	}

	public void setZoomIncrement(float step) {
		this.step = step;
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

		if (pos < min)
			return min;
		else if (max < pos)
			return max;
		else
			return pos;
	}

	public BookmarkEntry toBookmarkEntry() {
		// DebugLog.out(TAG, "MAKE BookMarkEntry:" + this.currentPage );
		return new BookmarkEntry(this.pageSizes.length, this.currentPage, scaling0 * zoomLevel, rotation, this.left
				- this.getCurrentPageWidth(this.currentPage) / 2 - marginX);
	}

	public void setSideMargins(int margin) {
		this.marginX = margin;
	}

	public void setTopMargin(int margin) {
		int delta = margin - this.marginY;
		top += this.currentPage * delta;
		this.marginY = margin;
	}

	public void setDoubleTap(int doubleTapAction) {
		this.doubleTapAction = doubleTapAction;
	}

	public boolean doAction(int action) {
		float zoomValue = Actions.getZoomValue(action);
		if (0f < zoomValue) {
			zoom(zoomValue);
			return true;
		}
		return false;
	}

	public void setActions(Actions actions) {
		this.actions = actions;
	}

	public void clearRenderer() {
		this.pagesProvider.clearTiles();
	}
}
