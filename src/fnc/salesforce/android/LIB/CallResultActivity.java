package fnc.salesforce.android.LIB;

import fnc.salesforce.android.LoginPage;
import fnc.salesforce.android.Constance.Constance;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class CallResultActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("test_1" , "CallResultActivity ");
		
		Constance.SHOPCD = "";
		Constance.USER_ID = "";
		
		mContext = this;
		
		Constance.TimeOverState = true;
		
		if( Constance.LoginStateType ){
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					
				}
			}, 1000);
			
//			Intent intent = new Intent( mContext, LoginPage.class );
//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent);
			finish();
			
//			android.os.Process.killProcess(android.os.Process.myPid());
		} else {
			android.os.Process.killProcess(android.os.Process.myPid());
		}
	}
	
	private Handler handler = new Handler();
	private Context mContext;
}
