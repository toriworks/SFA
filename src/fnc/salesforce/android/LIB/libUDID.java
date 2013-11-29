package fnc.salesforce.android.LIB;


import fnc.salesforce.android.Constance.Constance;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.view.Display;
import android.view.WindowManager;

public class libUDID {

	public void Check_UDID(Context context) {

		// ?�드??모델�?UDID 구하�?
		TelephonyManager telManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		Constance.UDID = telManager.getDeviceId();

		Constance.PHONE_MODEL = Build.MODEL;

		Context tmpCtx = context.getApplicationContext();
//		WifiManager wfManager = ((WifiManager)tmpCtx.getSystemService(Context.WIFI_SERVICE));
		String str = ((WifiManager)tmpCtx.getSystemService(Context.WIFI_SERVICE)).getConnectionInfo().getMacAddress();
		
		Constance.MAC = str;
		
		KumaLog.LogD("test", "Constance.MAC : " + Constance.MAC );
	}
}
