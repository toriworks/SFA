package fnc.salesforce.android.LIB;

import java.util.ArrayList;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

/**
 * DB?�成?�デ?�タ管理<br>
 */
public class KumaDBAdapter extends CursorAdapter {
	private final String DEBUG_TAG = "DBAdpater";

	/** SQLite??��?�ス変数 */
	public SQLiteDatabase mDatabase;
	/** ?�の??*/
	private int rows = 1;

	/**
	 * ?�結?�れ?�い?�SQLite??��?�を?�る
	 * 
	 * @return mDatabase
	 */
	public SQLiteDatabase getmDatabase() {
		return mDatabase;
	}

	/**
	 * ?�の?�を?�る
	 * 
	 * @return??ows
	 */
	public int getRows() {
		return rows;
	}

	/**
	 * DBAdpater?�成??
	 */
	public KumaDBAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, c);

		try {
			if (mDatabase == null || mDatabase.isOpen() == false) {
				KumaLog.LogD("DBAdapter Create!!");
					
				mDatabase = context.openOrCreateDatabase(
						KumaDBConstants.DB_SCHEME_NAME,
						SQLiteDatabase.CREATE_IF_NECESSARY, null);
				mDatabase.setVersion(1);
				mDatabase.setLocale(Locale.getDefault());
				mDatabase.setLockingEnabled(true);
			}
		} catch (Exception error) {
			error.printStackTrace();
		}
	} 

	/**
	 * ?�이�??�성
	 */
	public boolean createTable() {
		
		try {
			mDatabase.query(KumaDBConstants.TABLE_MENU_CLICK, null, null,
					null, null, null, null);
		} catch (Exception e) {
			try {
				mDatabase
						.execSQL(KumaDBConstants.SQL_CREATE_TABLE_MENU_CLICK);
			} catch (Exception e1) {
				e1.printStackTrace();
				return false;
			}
		}	
		
		
		
		return true;
	}

	/**
	 * Select?�数
	 */
	public String[][] selectColumn(String table, String[] columns,
			String selection, String selectionArgs[], String groupBy,
			String having, String orderBy) {
		String[][] result;
		Cursor mCursor;
		try {
			mCursor = mDatabase.query(table, columns, selection, selectionArgs,
					groupBy, having, orderBy);
		} catch (Exception error) {
			Log.e(DEBUG_TAG, "Select Error : " + error);
			return null;
		}

		result = new String[mCursor.getCount() + 1][mCursor.getColumnCount()];
		int dataRows = 0, columnIndex = 0;

		// ??��?�タ保存
		mCursor.moveToFirst();

		while (mCursor.isAfterLast() == false) {
			for (columnIndex = 0; columnIndex < mCursor.getColumnCount(); columnIndex++) {
				result[dataRows][columnIndex] = mCursor.getString(columnIndex);
			}
			mCursor.moveToNext();
			dataRows++;
		}

		// ??��?��??�名保存
		mCursor.moveToFirst();
		for (columnIndex = 0; columnIndex < mCursor.getColumnCount(); columnIndex++) {
			result[dataRows][columnIndex] = mCursor.getColumnName(columnIndex);
			
		}
		// 行列?�を保存
		rows = dataRows;

		mCursor.close();
		return result;
	}

	/**
	 * columns????�取?�込??
	 */
	public ArrayList<String> selectColumn(String table, String columns,
			String group) {
		ArrayList<String> array_list = new ArrayList<String>();
		array_list.clear();
		String sql = "SELECT " + columns + " FROM " + table;
		if (group != null) {
			sql += " GROUP BY " + group;
		}
		try {
			Cursor cur = mDatabase.rawQuery(sql, null);
			cur.moveToFirst();

			if (cur.getCount() > 0) {
				for (int i = 0; i < cur.getCount(); i++) {
					array_list.add(cur.getString(cur.getColumnIndex(columns)));
					cur.moveToNext();
				}
			}
			cur.close();
		} catch (Exception error) {
			Log.e(DEBUG_TAG, "Select Error : " + error);
		}
		return array_list;
	}

	/**
	 * Select?�数（�??�の?�ー?�）
	 */
	public String oneSelectColumn(String table, String[] columns,
			String selection, String selectionArgs[], String groupBy,
			String having, String orderBy) {
		String result = "";

		Cursor mCursor;
		try {
			mCursor = mDatabase.query(table, columns, selection, selectionArgs,
					groupBy, having, orderBy);
			if (mCursor.getCount() > 0) {
				mCursor.moveToFirst();
				result = mCursor.getString(0);
			} else {
				result = null;
			}
			mCursor.close();
		} catch (Exception error) {
			Log.e(DEBUG_TAG, "Select Error : " + error);
			return null;
		}

		return result;
	}

	/**
	 * ?�ー?�の??
	 */
	public int returnColumnCount(String table, String[] columns,
			String selection, String selectionArgs[], String groupBy,
			String having, String orderBy) {
		int count = -1;
		Cursor mCursor;
		try {
			mCursor = mDatabase.query(table, columns, selection, selectionArgs,
					groupBy, having, orderBy);
			count = mCursor.getCount();
			mCursor.close();
		} catch (Exception error) {
			Log.e(DEBUG_TAG, "Select Error : " + error);
		}

		return count;
	}

	/**
	 * DBinsert?�数
	 */
	public long insertColumn(String tableName, ContentValues values) {
		long insertID = -1;
		try {
			if (values != null) {
				insertID = mDatabase.insert(tableName, null, values);
			}
		} catch (Exception error) {
			Log.e(DEBUG_TAG, "Insert Column Error : " + error);
		}

		return insertID;
	}

	/**
	 * DBdelete?�数
	 */
	public long deleteColumn(String tableName, String pkId, String[] values) {
		long deleteID = -1;
		try {
			if (values != null) {
				deleteID = mDatabase.delete(tableName, pkId + "=?", values);
			} else {
				deleteID = mDatabase.delete(tableName, null, null);
			}
		} catch (Exception error) {
			Log.e(DEBUG_TAG, "Delete Column Error : " + error);
		}

		return deleteID;
	}

	/**
	 * DBupdate?�数
	 */
	public void updateColumn(String tableName, ContentValues values,
			String pkId, String[] whereArgs) {
		String updatepkId = pkId + "=?";

		try {
			if (values != null) {
				mDatabase.update(tableName, values, updatepkId, whereArgs);
			}
		} catch (Exception error) {
			Log.e(DEBUG_TAG, "Update Column Error : " + error);
		}
	}

	/**
	 * Cursur?�バ?�ン?�（使っ?�い?�い�?
	 * 
	 * @see android.widget.CursorAdapter#bindView(android.view.View,
	 *      android.content.Context, android.database.Cursor)
	 */
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		KumaLog.LogI("view id : " + view.getId());
	}

	/**
	 * ?�し?�View?�作?�（使っ?�い?�い�?
	 * 
	 * @see android.widget.CursorAdapter#newView(android.content.Context,
	 *      android.database.Cursor, android.view.ViewGroup)
	 */
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return null;
	}
}
