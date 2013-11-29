package fnc.salesforce.android.LIB;

import android.content.Context;
import android.os.IBinder;
import android.view.inputmethod.InputMethodManager;

public class HidekeyBoard {
	
	public static void KeyboardHide( Context context,  IBinder mWindowToken){
		InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		
		//키보드를 없앤다.
		imm.hideSoftInputFromWindow(mWindowToken, 0);
	}
}
