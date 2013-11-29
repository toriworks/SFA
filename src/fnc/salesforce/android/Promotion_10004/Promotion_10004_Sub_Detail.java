package fnc.salesforce.android.Promotion_10004;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ViewFlipper;
import android.widget.ImageView.ScaleType;
import fnc.salesforce.android.LoginPage;
import fnc.salesforce.android.R;
import fnc.salesforce.android.Constance.CONTENTS_PAGE_ALL_LIST_CONSTANCE;
import fnc.salesforce.android.Constance.Constance;
import fnc.salesforce.android.LIB.BackPress;
import fnc.salesforce.android.LIB.CipherUtils;
import fnc.salesforce.android.LIB.DownloadFile;
import fnc.salesforce.android.LIB.KumaLog;
import fnc.salesforce.android.LIB.libJSON;
import fnc.salesforce.android.LIB.libJSON_GET;
import fnc.salesforce.android.Promotion_10004.Promotion_10004_Sub.setDeviceInfor;

public class Promotion_10004_Sub_Detail implements OnClickListener{

	LayoutInflater inflater;
	
	private Activity mContext;
		
	ProgressDialog pd;
	
	private AlertDialog.Builder dialogBuilder;
	
	public Promotion_10004_Sub_Detail(Activity context){
		mContext = context;
		
		pd = new ProgressDialog( context );
		
		inflater = ((Activity) mContext).getLayoutInflater();
		
		dialogBuilder = new AlertDialog.Builder( context );
	}
	
	private ImageView imgpromotionDetail;
	
	private String[] strParam = new String[4];
	
	public View setPromotion_10004_Sub_DetailView(String[] Param){
		
		try {
			strParam[0] = Param[0];
			strParam[1] = Param[1];
			strParam[2] = Param[2];
			strParam[3] = Param[3];
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		View view = null;
		
		view = inflater.inflate(R.layout.promotion_10004_detail, null);
		
		imgpromotionDetail = (ImageView) view.findViewById(R.id.imgpromotionDetail);
		
		imgpromotionDetail.setOnClickListener( this );
		
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				new setThumbNailDown_SUB().execute();
			}
		}, 200);
		
		return view;
	}
	
	String imgPATH = "";
	
	private int verValue = 0, verMax = 1;
	
	class setThumbNailDown_SUB extends AsyncTask<Integer, Integer, Integer> {
		@Override
		protected void onPreExecute() {
			if( !pd.isShowing() ) {
				pd.setMessage("프로모션 정보를 요청중 입니다. 잠시만 기다려 주십시요.");
				pd.setCancelable( false );
				pd.show();
			}
			verValue = 0;
		}
		
		@Override
		protected Integer doInBackground(Integer... params) {
			while (isCancelled() == false) {
				if (verValue < verMax) {
					
					try {
						imgPATH = DLF.Download_HttpFile(strParam[0], Constance.FILEPATH + "/" + Constance.BEANDCD + "/" + strParam[2] +"/", strParam[3]);
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
						imgpromotionDetail.setImageBitmap( BitmapFactory.decodeFile( imgPATH )  );
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
	
	private DownloadFile DLF = new DownloadFile();
	
	private Handler handler = new Handler();
	@Override
	public void onClick(View v) {
		try {
			if( v == imgpromotionDetail ){
				KumaLog.LogD("test_1"," strParam[1]: " + strParam[1] );
				if( !strParam[1].equals("null") &&  strParam[1] != null && strParam[1].length() > 0 ){
					dialogBuilder.setTitle("SalesForce");
					dialogBuilder.setMessage( "링크페이지가 존재합니다. 이동하시겠습니까?");
			        dialogBuilder.setPositiveButton( "예" , LinkOK );
			        dialogBuilder.setNegativeButton("아니오", null);
			        dialogBuilder.show();
				} else {
//					dialogBuilder.setTitle("SalesForce");
//					dialogBuilder.setMessage( "링크페이지가 존재 하지 않습니다.");
//			        dialogBuilder.setPositiveButton( "확인" , null );
//			        dialogBuilder.show();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private DialogInterface.OnClickListener LinkOK = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface arg0, int arg1) {
			Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(strParam[1]));
			mContext.startActivity(i);
		}
	};
}