package tk.atna.tedtalks.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class TalksProvider extends ContentProvider {

    private static final String AUTHORITY = TalksContract.AUTHORITY;

    private static final int MATCH_FEED = 0x00000011;
    private static final int MATCH_FEED_ITEM = 0x00000012;

    private TalksDB db;

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, TalksContract.Talks.TABLE, MATCH_FEED);
        uriMatcher.addURI(AUTHORITY, TalksContract.Talks.TABLE + "/*", MATCH_FEED_ITEM);
    }


    @Override
    public boolean onCreate() {
        db = new TalksDB(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {

            case MATCH_FEED:
                return TalksContract.Talks.CONTENT_TYPE;

            case MATCH_FEED_ITEM:
                return TalksContract.Talks.CONTENT_ITEM_TYPE;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        String table = TalksContract.Talks.TABLE;
        String where = null;

        switch (uriMatcher.match(uri)) {

            case MATCH_FEED:
                sortOrder = TalksContract.Talks.TALK_PUBLISHED + " DESC";
                break;

            case MATCH_FEED_ITEM:
                where = TalksContract.Talks._ID + " = '" + uri.getLastPathSegment() + "'";
                break;

            default:
                return null;
        }

        Cursor cursor = db.getWritableDatabase()
                          .query(table, null, where, null, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        switch (uriMatcher.match(uri)) {
            case MATCH_FEED:
            default:
                throw new UnsupportedOperationException("Unknown insert uri: " + uri);

            case MATCH_FEED_ITEM:
                break;
        }

        String table = TalksContract.Talks.TABLE;
        // insert row
        long row = db.getWritableDatabase().insert(table, null, values);
//        notifyChange(uri);

        return ContentUris.withAppendedId(uri, row);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase dBase = db.getWritableDatabase();
        String table;
        String where;

        switch (uriMatcher.match(uri)) {
            case MATCH_FEED:
                db.dropTableTalks(dBase);
                db.createTableTalks(dBase);
                return 0;

            case MATCH_FEED_ITEM:
                table = TalksContract.Talks.TABLE;
                where = TalksContract.Talks._ID + " = '" + uri.getLastPathSegment() + "'";
                break;

            default:
                throw new UnsupportedOperationException("Unknown delete uri: " + uri);
        }

        selection = (selection == null || selection.length() == 0)
                ? where : selection + " AND " + where;

        int count = dBase.delete(table, selection, null);
//        notifyChange(uri);

        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String where = null;

        switch (uriMatcher.match(uri)) {
            case MATCH_FEED:
            default:
                throw new UnsupportedOperationException("Unknown update uri: " + uri);

            case MATCH_FEED_ITEM:
                break;
        }
            int rows = delete(uri, selection, null);
            insert(uri, values);
            notifyChange(uri);
            return rows;
    }

    private void notifyChange(Uri uri) {
        getContext().getContentResolver().notifyChange(uri, null);
    }

}
