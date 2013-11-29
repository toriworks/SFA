package fnc.salesforce.android.LIB;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class setLoginAlram {
	private String TAG = "test_1";
	
	private static final String INTENT_ACTION = "arabiannight.tistory.com.alarmmanager";
	 
	public setLoginAlram(){
		
	}
	
	int SECS = 1000;
    
    int MINS = 60 * SECS;
    
    int HOUR = 60 * MINS;
    
	// 알람 등록
	public void setAlarm(Context context, int second){  
        Log.i(TAG, "setAlarm()"); 
        
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
 
        SECS = 1000;
        
        MINS = 60 * SECS;
        
        HOUR = 60 * MINS;
        
        int InterVal =  second * HOUR;
        
//        int InterVal = 20000;
        
        Calendar cal = Calendar.getInstance();

        Intent Intent = new Intent(INTENT_ACTION);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, Intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), InterVal, pIntent);
//        alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + second, pIntent);
    }
     
    // 알람 해제
	public void releaseAlarm(Context context){  
        Log.i(TAG, "releaseAlarm()"); 
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
 
        Intent Intent = new Intent(INTENT_ACTION);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, Intent, 0);
        alarmManager.cancel(pIntent);
        // 주석을 풀면 먼저 실행되는 알람이 있을 경우, 제거하고
        // 새로 알람을 실행하게 된다. 상황에 따라 유용하게 사용 할 수 있다.
//      alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 3000, pIntent);
    }
}
