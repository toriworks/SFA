package fnc.salesforce.android.LIB;

import java.util.HashMap;

import android.graphics.drawable.Drawable;


public class Define {
	// ���� ����� Preference
	public final static String PREF_NAME = "KMAGAZINE";

	public final static String PREF_LAST_YEAR = "LAST_YEAR";

	public final static String SAVE_ROOT = "KolonSabo";
	// �纸 ���� URL
	//���� ���� ��û : appNm=kNewsletter
	public final static String SABO_VERSION = "http://m.kolon.com:8080/business/app/servAppVer.do?";
	//App download URL ��û : appNm=kNewsletter
	public final static String SABO_APPURL = "http://m.kolon.com:8080/business/app/servAppUrl.do?";
	//���� ���� ��û 
	public final static String SABO_NOTICE = "http://www.kolon.com/DATA/CYBERPR/INMAGAZINE/Notice.txt";
	
	//mode=yearlist (��������Ʈ) : &year=2003
	//mode=lastest (�ֽ� ��� ��)
	public final static String SABO_SERVER = "http://www.kolon.com/mobilesabo/mobile_sabo.aspx?";
	
	//mode=selectlist (��,�� �Ѱ� ����Ʈ) : &year=2003&month=4
	public final static String SABO_SELECT_LIST = "http://www.kolon.com/mobilesabo/mobile_sabo?mode=selectlist";

	
	// �̹��� �� PDF �ٿ�ε� ��� : 
	// r : Ŀ�� �÷ο� �� , s : �� ���̽� �̹����� 
	// Image : CPMAGM + �⵵(2011) + ��(01) + r/s + .png
	public final static String SABO_DOWNLOAD = "http://www.kolon.com/DATA/CYBERPR/INMAGAZINE/";
	
	public final static String SABO_FILE_PREFIX = SABO_DOWNLOAD + "CPMAGM";
	public final static String SABO_FILE_EXT = ".png";
	
	public static HashMap<String, Drawable> hmDrawable = new HashMap<String, Drawable>();
	
	public static boolean bIsAlpha = true;
	
	/**
	 * < ���� URL ���� >
1. yearlist (��������Ʈ)
 year
 http://www.kolon.com/mobilesabo/mobile_sabo.aspx?mode=yearlist&year=2003
 
2. selectlist (��,�� �Ѱ� ����Ʈ)
 year, month
 http://www.kolon.com/mobilesabo/mobile_sabo?mode=selectlist&year=2003&month=4
 
3. lastest (�ֽ� ��� ��)
 http://www.kolon.com/mobilesabo/mobile_sabo.aspx?mode=lastest

�纸 �̹��� URL : http://www.kolon.com/DATA/CYBERPR/INMAGAZINE/CPMAGM201112s.png

  1. �����̽� �̹�����
1�� : CPMAGM201101s.png2�� : CPMAGM201102s.png 3�� : CPMAGM201103s.png
4�� : CPMAGM201104s.png5�� : CPMAGM201105s.png 6�� : CPMAGM201106s.png
7�� : CPMAGM201107s.png8�� : CPMAGM201108s.png 9�� : CPMAGM201109s.png
10�� : CPMAGM201110s.png11�� : CPMAGM201111s.png 12�� : CPMAGM201112s.png


  1. Ŀ���÷ο� �̹�����
1�� : CPMAGM201101r.png2�� : CPMAGM201102r.png 3�� : CPMAGM201103r.png
4�� : CPMAGM201104r.png5�� : CPMAGM201105r.png 6�� : CPMAGM201106r.png
7�� : CPMAGM201107r.png8�� : CPMAGM201108r.png 9�� : CPMAGM201109r.png
     10�� : CPMAGM201110r.png11�� : CPMAGM201111r.png 12�� : CPMAGM201112r.png
	 */

	public static final int DIALOG_QUIT = 1;
	public static final int DIALOG_STOP_YES_NO 	= 100;
	public static final int DIALOG_PROGRESS 	= 101;

}
