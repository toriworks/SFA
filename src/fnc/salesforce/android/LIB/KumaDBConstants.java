package fnc.salesforce.android.LIB;

import android.provider.BaseColumns;

/**
 * DB?�使?�れ?�マ?�タ?�ー??br>
 * DB?�と?�ー?�ル?�な?�を決め??br>
 */
public final class KumaDBConstants {

	private KumaDBConstants() {
	};

	/** DB??��??��?�名 */
	public static final String DB_SCHEME_NAME = "SALES_FORCE";

	// -----------------------------------------------------------------------

	/** 매장 정보 히스토리Table */
	public static final String TABLE_MENU_CLICK = "MENU_HITORY";
	public static final String COLUMN_ID = BaseColumns._ID;
	public static final String MENU_CLICK_TIME = "CLICK_TIME";
	public static final String CLICK_MENU_NAME = "MENU_NAME";
	
	// -----------------------------------------------------------------------
	/** 매장 정보 히스토리Table */
	public static final String SQL_CREATE_TABLE_MENU_CLICK = KumaDBConstants
			.replaceString("CREATE TABLE ##### ( "
					+ "##### INTEGER PRIMARY KEY AUTOINCREMENT "
					+ ", ##### TEXT "+ ", ##### TEXT )", TABLE_MENU_CLICK, COLUMN_ID,
					MENU_CLICK_TIME, CLICK_MENU_NAME);
	// -----------------------------------------------------------------------

	
	// -----------------------------------------------------------------------
	/** �?��?�え?�字??*/
	public static final String PREDEFINED_REPLACE_STRING = "#####";

	/**
	 * 対象?�字?�の?�定?�分?�パ?�メ?�タ????�置?�す??br>
	 * 
	 * @param source
	 *            対象?�字??	 * @param args
	 *            �?��?�ラ?�ー??	 * @return �?��結果?�字??	 * @version 0.1
	 * @since 0.1
	 */

	public static String replaceString(String source, String... args) {
		int argLength = args.length;

		if (argLength > 0) {
			for (int i = 0; i < argLength; i++) {
				source = source.replaceFirst(
						KumaDBConstants.PREDEFINED_REPLACE_STRING, args[i]);
			}
		}
		;
//		KumaLog.LogW("tes_1"," source : " + source);
		return source;
	}
}
