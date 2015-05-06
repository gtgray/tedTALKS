package tk.atna.tedtalks.provider;

import android.net.Uri;

public final class TalksContract {

    public static final String AUTHORITY = "tk.atna.tedtalks.provider";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String TYPE_PREFIX = "vnd.android.cursor.dir/vnd.tedtalks.";
    public static final String ITEM_TYPE_PREFIX = "vnd.android.cursor.item/vnd.tedtalks.";


    private TalksContract() {
        // nothing here
    }


    interface BaseColumn {

        String _ID = "_id";
    }


    interface TalksColumns {

        String TALK_NAME = "talk_name";
        String TALK_DESC = "talk_desc";
//        String TALK_SPEAKERS = "talk_speakers";
        String TALK_PUBLISHED = "talk_published";
        String TALK_RECORDED = "talk_recorded";
        String TALK_UPDATED = "talk_updated";
        String TALK_VIEWED = "talk_viewed";
        String TALK_EMAILED = "talk_emailed";
        String TALK_COMMENTED = "talk_commented";
        String TALK_IMAGE_URL = "talk_image_url";
        String TALK_VIDEO_LOW = "talk_video_low";
        String TALK_VIDEO_HIGH = "talk_video_high";
    }


    public static class Talks implements BaseColumn, TalksColumns {

        public static final String TABLE = "talks";

        public static final String CONTENT_TYPE = getContentType(TABLE);
        public static final String CONTENT_ITEM_TYPE = getContentItemType(TABLE);

        public static final Uri CONTENT_URI = getContentUri(TABLE);
    }

    private static String getContentType(String table) {
        return TYPE_PREFIX + table;
    }

    private static String getContentItemType(String table) {
        return ITEM_TYPE_PREFIX + table;
    }

    private static Uri getContentUri(String table) {
        return BASE_CONTENT_URI.buildUpon().appendPath(table).build();
    }
}
