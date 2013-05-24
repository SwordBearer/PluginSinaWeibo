package xmu.swordbearer.sinaplugin.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库
 * 
 * @author SwordBearer
 * 
 */
public class DBHelper {
	private SQLiteDatabase db;
	private SQLiteOpenHelper openHelper;

	public static final String TBL_PINLIST = "tbl_watch_list";
	public static final String KEY_PINLIST_ID = "_id";
	public static final String KEY_PINLIST_UID = "uid";
	public static final String KEY_PINLIST_NAME = "name";
	// public static final String KEY_PINLIST_REMARK = "remark";
	public static final String KEY_PINLIST_LATESTID = "latest_id";// 最新一条微博的ID

	public DBHelper(Context context) {
		this.openHelper = new DBOpenHelper(context);
	}

	public void open() {
		if (db == null || !db.isOpen()) {
			db = openHelper.getWritableDatabase();
			if (db == null) {
				db = openHelper.getReadableDatabase();
			}
		}
	}

	public void close() {
		if (db != null && db.isOpen()) {
			db.close();
		}
	}

	public boolean add(String tblName, ContentValues values) {
		return (db.insert(tblName, null, values) > 0);
	}

	public boolean delete(String tblName, long id) {
		String ids[] = { id + "" };
		return (db.delete(tblName, "_id=?", ids) > 0);
	}

	public Cursor query(String tblName) {
		return db.query(tblName, null, null, null, null, null, null);
	}

	public void update(String tblName, ContentValues[] values) {
		int len = values.length;
		for (int i = 0; i < len; i++) {
			db.update(tblName, values[i], null, null);
		}
	}

	private class DBOpenHelper extends SQLiteOpenHelper {
		private static final String DB_NAME = "db_us_sina";
		private static final int VERION = 1;

		private static final String sql_create_pinlist = "CREATE TABLE " + TBL_PINLIST + "(" + KEY_PINLIST_ID
				+ " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " + KEY_PINLIST_UID + " INTEGER NOT NULL, " + KEY_PINLIST_NAME + " TEXT NOT NULL,"
				+ KEY_PINLIST_LATESTID + " LONG NOT NULL)";

		public DBOpenHelper(Context context) {
			super(context, DB_NAME, null, VERION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(sql_create_pinlist);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
	}
}
