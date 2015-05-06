package tk.atna.tedtalks.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static tk.atna.tedtalks.provider.TalksContract.*;

public class TalksDB extends SQLiteOpenHelper {

    static final String DB_NAME = "tedtalks.db";
    static final int DB_VERSION = 1;

    // table talks
    private static final String CREATE_TABLE_TALKS =
            "CREATE TABLE " + Talks.TABLE + " ("
                    + BaseColumn._ID + " INTEGER PRIMARY KEY, "
                    + Talks.TALK_NAME + " TEXT NOT NULL, "
                    + Talks.TALK_DESC + " TEXT, "
//                    + Talks.TALK_SPEAKERS + " TEXT, "
                    + Talks.TALK_PUBLISHED + " TEXT, "
                    + Talks.TALK_RECORDED + " TEXT, "
                    + Talks.TALK_UPDATED + " TEXT, "
                    + Talks.TALK_VIEWED + " TEXT, "
                    + Talks.TALK_EMAILED + " TEXT, "
                    + Talks.TALK_COMMENTED + " TEXT, "
                    + Talks.TALK_IMAGE_URL + " TEXT, "
                    + Talks.TALK_VIDEO_LOW + " TEXT, "
                    + Talks.TALK_VIDEO_HIGH + " TEXT);";

    private static final String DROP_TABLE_TALKS =
            "DROP TABLE IF EXISTS " + Talks.TABLE + ";";


	public TalksDB(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
        createTableTalks(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// nothing here
    }

    void createTableTalks(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_TALKS);
    }

    void dropTableTalks(SQLiteDatabase db) {
		db.execSQL(DROP_TABLE_TALKS);
    }

}
