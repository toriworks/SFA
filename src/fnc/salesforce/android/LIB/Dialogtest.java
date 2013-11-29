package fnc.salesforce.android.LIB;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface.OnClickListener;

public class Dialogtest {
	
	private AlertDialog.Builder dialogBuilder;
	
	private Activity mContext;
	
	public Dialogtest( Activity context ){
		mContext = context;
		
		dialogBuilder = new AlertDialog.Builder(mContext);
	}
	
	
	public void Show( String[] Return_Code, OnClickListener test ){
		dialogBuilder.setTitle("알림");
		
		if( Return_Code.equals("500") ){
			dialogBuilder.setMessage( "ID 및 Password 를 확인해주세요." );
		} else if( Return_Code.equals("401.1") ){
			
		} else if( Return_Code.equals("401.100") ){
			
		} else if( Return_Code.equals("401.101") ){
			
		} else if( Return_Code.equals("401.102") ){
			
		} else if( Return_Code.equals("401.103") ){
			
		} else if( Return_Code.equals("401.104") ){
			
		} else if( Return_Code.equals("401.105") ){
			
		}
		
        dialogBuilder.setPositiveButton( "확인" , test);
        dialogBuilder.show();
	}
	
//	OK	200	OK	성공
//	SERVER_ERROR	500	Internal Server Error	서버Exception
//	UNAUTHORIZED1	401.1	Unauthorized	인증오류
//	FA_UNAUTHORIZED	401.100	FA 인증 오류입니다.	인증오류_FA
//	DEVICE_UNAUTHORIZED	401.101	미등록 상태입니다.	단말 미등록
//	CONFM_UNAUTHORIZED(	401.102	미승인 상태입니다.	단말 미승인
//	LOS_UNAUTHORIZED	401.103	분실 상태입니다.	단말 분실
//	ERRORCO_UNAUTHORIZED	401.104	오류횟수 초과 상태입니다.	비밀번호 5회이상 오류
//	TIME_UNAUTHORIZED	401.105	요청시간 초과 상태입니다.	요청시간 초과


}
