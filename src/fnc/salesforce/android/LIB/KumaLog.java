package fnc.salesforce.android.LIB;

import android.util.Log;

public class KumaLog {

	private static String LOG_TAG = "test";
	private static boolean state = true;

	public static void LogE(String Message) {
		if (state)
			Log.e(LOG_TAG, Message);
	}

	public static void LogD(String Message) {
		if (state)
			Log.d(LOG_TAG, Message);
	}

	public static void LogI(String Message) {
		if (state)
			Log.i(LOG_TAG, Message);
	}

	public static void LogW(String Message) {
		if (state)
			Log.w(LOG_TAG, Message);
	}
	
	public static void LogE(String tag, String Message) {
		if (state)
			Log.e(tag, Message);
	}

	public static void LogD(String tag, String Message) {
		if (state)
			Log.d(tag, Message);
	}

	public static void LogI(String tag, String Message) {
		if (state)
			Log.i(tag, Message);
	}

	public static void LogW(String tag, String Message) {
		if (state)
			Log.w(tag, Message);
	}
}
