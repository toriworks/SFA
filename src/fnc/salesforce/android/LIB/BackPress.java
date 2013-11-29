package fnc.salesforce.android.LIB;

import java.lang.reflect.Method;
import android.content.Context;

public class BackPress {
	static Method[] meth = new Method[12];
	static Context mContext;
	
	/* * @param contenxt
	 * @param method Return Method
	 * @param position
	 * 
	 * 0 : getDetailPage 백키 눌렀을 때
	 * 1 : ScreenSaverRestart 스크린세이버 재시작
	 * 2 : ScreenSaverStop    스크린세이버 중단
	 * 3 : ScreenSaverResetCount 스크린세이버 카운트 초기화
	 * 4: ScreenSaverDestroy 스크린세이버 카운트 해제
	 * 
	 * */
	public static void setDetailPage(Context context, String method, int position){
		mContext = context;
		meth[position] = null;
		try {
			// 동적 메소드 호출을 위한 셋팅
			meth[position] = mContext.getClass().getMethod(method,
					new Class[] { int.class });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void getDetailPage(int position){
		try {
			meth[position].invoke(mContext,	new Object[] { new Integer(1) });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
