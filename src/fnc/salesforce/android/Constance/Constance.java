package fnc.salesforce.android.Constance;

import java.util.Calendar;

import fnc.salesforce.android.R;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;


public class Constance {

    public static boolean TimeOverState = false;

    public static String UDID = "";
    public static String PHONE_MODEL = "";
    public static String MAC = "";

    public static String SEED = "NTE3YjA0YjUtZGJkZS00NDQ5LTg4NzgtMzFiNWFjMjg1YmZi";

    public static String FILEPATH = Environment.getExternalStorageDirectory() + "/.SalesForce";

    public static String TEST_FILEPATH = Environment.getExternalStorageDirectory() + "/KolonSabo/CPMAG_PDF133.pdf";

    public static int Ad_GalleryPage_Trans = 0;

//	public static String[] MenuGojung = { };

    //	public static String[] MenuGojung = {"수선정보","매장정보","상품조회","멤버쉽" ,"매장Stop제","매장Checklist"};
    public static String[] MenuGojung = {"멤버쉽", "매장매출조회", "제품조회", "수선정보", "매장정보", "매장Checklist"};
    public static String[] MenuGojung_IMG = {"IM_Main_Icon22.png", "IM_Main_Icon12.png", "IM_Main_Icon16.png", "IM_Main_Icon36.png", "IM_Main_Icon47.png", "IM_Main_Icon03.png"};

    public static String UPLOAD_FILE_NAME_ORIGINAL = "";
    public static String UPLOAD_FILE_NAME_MASK = "";
    public static String UPLOAD_FILE_NAME = "";


    public static String[] arrTransInformation = {"", "", "", ""};
    public static String[] arrTrans_UP_Information = {"", "", "", ""};
    public static String YouTuBe_URL = "";

    public static String BEANDCD = "";
    public static String SHOPCD = "";
    public static String SHOPNAME = "";
    public static String LOOK_CONTENTS_ID = "";

    public static String LOGIN_PASSWORD = "";

    public static String USER_ID = "";

    public static int pdf_CurrentPage = 0;


    public static int[] MainMenuIcon = {R.drawable.at_main_icon01, R.drawable.at_main_icon02, R.drawable.at_main_icon03, R.drawable.at_main_icon04};
    public static int[] MainMenuBG = {R.drawable.at_main_menu01, R.drawable.at_main_menu02, R.drawable.at_main_menu03, R.drawable.at_main_menu04};

    public static String[] BrandName = {"KOLON SPORT", "ELORD", "HEAD", "Jack nicklaus", "Customellow", "CAMBRIDGE MEMBERS", "GGIO II", "Brentwood", "SPASSO",
            "QUA", "lucky chouette", "HenryCottons", "Series", "COURONNE", "GEOX", "suecomma bonnie", "ELORD GOLF", "CLUB CAMBRIDGE"};
    public static String[] BrandCode = {"6J", "7N", "6H", "7L", "3A", "3M", "1M", "3B", "1K", "1Q", "7J", "", "7S", "7C", "1G", "9S", "7Z", "3C"};

    public static int[] BrandCodeImage_Small = {R.drawable.at_brand001_kolonsport_small, R.drawable.at_brand002_elord_small, R.drawable.at_brand003_head_small, R.drawable.at_brand004_jackniclaus_small,
            R.drawable.at_brand005_customellow_small, R.drawable.at_brand006_cambridge_small, R.drawable.at_brand007_ggio_small, R.drawable.at_brand008_brentwood_small,
            R.drawable.at_brand009_spasso_small, R.drawable.at_brand010_qua_small, R.drawable.at_brand011_luckychouette_small, R.drawable.at_brand012_henrycottons_small,
            R.drawable.at_brand013_series_small};

    public static int[] BrandCodeImage_Large = {R.drawable.at_brand001_kolonsport_large, R.drawable.at_brand002_elord_large, R.drawable.at_brand003_head_large, R.drawable.at_brand004_jackniclaus_large,
            R.drawable.at_brand005_customellow_large, R.drawable.at_brand006_cambridge_large, R.drawable.at_brand007_ggio_large, R.drawable.at_brand008_brentwood_large,
            R.drawable.at_brand009_spasso_large, R.drawable.at_brand010_qua_large, R.drawable.at_brand011_luckychouette_large, R.drawable.at_brand012_henrycottons_large,
            R.drawable.at_brand013_series_large};

    public static String empNo = "";
    public static String userNm = "";

    public static String PageCount = "";
    public static String Page_ALL_Count = "";

    public static boolean LoginStateType = true;

    public static boolean HEAD_LOGIN = true;

    public static int getBrandImg_Large(String strBrandCD) {
        int logoPosition = 0;
        for(int i = 0; i < BrandCode.length; i++) {
            if(BrandCode[i].equals(strBrandCD)) {
                logoPosition = i;
            }
        }

        if(logoPosition < BrandCodeImage_Large.length) {
            return BrandCodeImage_Large[logoPosition];
        } else {
            return R.drawable.a11;
        }

    }

    public static int getBrandImg_Small(String strBrandCD) {

        int logoPosition = 0;

        for(int i = 0; i < BrandCode.length; i++) {
            if(BrandCode[i].equals(strBrandCD)) {
                logoPosition = i;
            }
        }

        if(logoPosition < BrandCodeImage_Small.length) {
            return BrandCodeImage_Small[logoPosition];
        } else {
            return R.drawable.a11;
        }
    }
}
