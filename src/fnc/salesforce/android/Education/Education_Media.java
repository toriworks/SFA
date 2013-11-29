package fnc.salesforce.android.Education;


import fnc.salesforce.android.R;
import fnc.salesforce.android.Constance.CONTENTS_PAGE_ALL_LIST_CONSTANCE;
import fnc.salesforce.android.Constance.Constance;
import fnc.salesforce.android.LIB.BackPress;
import fnc.salesforce.android.LIB.CipherUtils;
import fnc.salesforce.android.LIB.DownloadFile;
import fnc.salesforce.android.LIB.KumaLog;
import fnc.salesforce.android.LIB.libJSON_GET;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnInfoListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.ImageView.ScaleType;
import android.widget.VideoView;


public class Education_Media implements OnClickListener{

	LayoutInflater inflater;
	
	private Context mContext;
		
	ProgressDialog pd;
	
	private LinearLayout navigationRow;
	
	private boolean PageState = false;
	
	private TextView txtEducation_MediaText;
	private VideoView videoViewExample;
	
	public Education_Media(Context context){
		mContext = context;
		
		pd = new ProgressDialog( context );
		
		inflater = ((Activity) mContext).getLayoutInflater();
	}
		
	String strBrnadCD = "", strContentsID = "";
	
	View view1 = null;
	
	Uri videoUri;
	
	public View setEducation_MediaView(String mMediaPath, String mMediaTitle, String mMediaContents){
		
		view1 = inflater.inflate(R.layout.education_media, null);
		
		videoViewExample 		= (VideoView) view1.findViewById(R.id.videoViewExample);
		txtEducation_MediaText	= (TextView) view1.findViewById(R.id.txtEducation_MediaText);
		
        
        
        MediaController mediaController = new MediaController( mContext );
        mediaController.setAnchorView(videoViewExample);
        
        videoViewExample.setMediaController(mediaController);
        
//        videoUri = Uri.parse("http://203.225.255.146:8081/BenitFnc/upload/cntnts/FILE_B3bu6VJrSeeNu0PPbHUERA.mp4");
        videoUri = Uri.parse( mMediaPath );
        
        mMediaContents = mMediaTitle;
        
        txtEducation_MediaText.setText( mMediaContents );
        
        videoViewExample.setOnCompletionListener( new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				Toast.makeText(mContext, "재생 종료", 1000);	
			}
		});
        
        videoViewExample.setVideoURI(videoUri);

        videoViewExample.start();
        
		return view1;
	}

	public View getView(){
		return view1;
	}
	
	private Handler handler = new Handler();
	
    
	private CipherUtils mCipher = new CipherUtils();
	
	private int verValue = 0, verMax = 1;

	private libJSON_GET GET_LibJSON = new libJSON_GET();

	class setDeviceInfor extends AsyncTask<Integer, Integer, Integer> {
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
			verMax = 1; 
			verValue = 0;
		}
		
		@Override
		protected Integer doInBackground(Integer... params) {
			while (isCancelled() == false) {
				if (verValue < verMax) {
					try {
						
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
					videoViewExample.start();
				}
			});
		}
		@Override
		protected void onCancelled() {
		}
	}
	
	@Override
	public void onClick(View v) {

	}

	public boolean onDown(MotionEvent e) {
		return false;
	}

	public boolean onFling(MotionEvent startEvent, MotionEvent endEvent,
			float velocityX, float velocityY) {
		return false;
	}

	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}
}