package game.knowledge.tsoglanakos.knowledgegame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DB extends SQLiteOpenHelper {

	public static final String DB_NAME = "MyDB";
	public static final String SCORE = "Score";
	public static final String STAGE = "Stage";
	private static final int DATABASE_VERSION = 1;
	private Context context;
	// creation SQLite statement
	private static final String DATABASE_CREATE = "CREATE TABLE " + DB_NAME
			+ "(" + STAGE + " INTEGER, " + SCORE + " INTEGER)";

	public DB(Context context) {
		super(context, DB_NAME, null, DATABASE_VERSION);
		this.context = context;
		try {
			SQLiteDatabase db = getWritableDatabase();
			db.execSQL(DATABASE_CREATE);
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			db.execSQL(DATABASE_CREATE);
			Log.e("On Create  DB", "DB");
		} catch (Exception e) {
			// e.printStackTrace();
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		db.execSQL("DROP TABLE IF EXISTS " + DB_NAME);
		onCreate(db);
	}

	public void addScore(int score, int level) {
		if (score > getMaxScore(level)) {
			SQLiteDatabase db = getWritableDatabase();
			ContentValues cv = new ContentValues();
			cv.put(DB.SCORE, score);
			cv.put(DB.STAGE, level);
			db.insert(DB.DB_NAME, null, cv);
			db.close();
			Log.e("Score = ", Integer.toString(score));
			Toast.makeText(context,
					"Congratulation your new record is " + score,
					Toast.LENGTH_SHORT).show();
		}

	}

	public int getMaxScore(int level) {
		int maxScore = 0;
		try {
			String query = "SELECT  * FROM " + DB.DB_NAME;

			// 2. get reference to writable DB
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(query, null);
			if (cursor.moveToFirst()) {
				do {
					int cms = cursor.getInt(1);
					int lvl = cursor.getInt(0);
					Log.e("lvl = ",Integer.toString(cms));
					if (lvl == level) {
						maxScore = cms;
					}

				} while (cursor.moveToNext());
			}
			db.close();
		} catch (Exception e) {
		} finally {

		}

		return maxScore;
	}

}