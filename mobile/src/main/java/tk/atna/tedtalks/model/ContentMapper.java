package tk.atna.tedtalks.model;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import java.util.List;

import tk.atna.tedtalks.provider.TalksContract;

import static tk.atna.tedtalks.model.Inbound.Feed.Talk.Details.*;

public class ContentMapper {

    private static final String DEFAULT_IMAGE_QUALITY = Image.SIZE_240X180;
    private static final String DEFAULT_VIDEO_QUALITY_LOW = Media.Videos.QUALITY_320K;
    private static final String DEFAULT_VIDEO_QUALITY_HIGH = Media.Videos.QUALITY_600K;


    /**
     * Pushes talks feed into sqlite
     *
     * @param cr ContentResolver object
     * @param feed talks feed model object
     */
    public static void pushFeedToProvider(ContentResolver cr, Inbound.Feed feed) {
        if(cr == null || feed == null)
            throw new IllegalArgumentException("One or more arguments are null");

        for(Inbound.Feed.Talk talk : feed.talks) {
            ContentMapper.pushTalkToProvider(cr, talk);
        }
    }

    /**
     * Pushes single talk into sqlite
     *
     * @param cr ContentResolver object
     * @param talk single talk model object
     */
     private static void pushTalkToProvider(ContentResolver cr, Inbound.Feed.Talk talk) {
        if(cr == null || talk == null)
            throw new IllegalArgumentException("One or more arguments are null");

        ContentValues cv = new ContentValues();
        cv.put(TalksContract.Talks._ID, talk.talk.id);
        cv.put(TalksContract.Talks.TALK_NAME, talk.talk.name);
        cv.put(TalksContract.Talks.TALK_DESC, talk.talk.description);
        cv.put(TalksContract.Talks.TALK_PUBLISHED, talk.talk.publishedAt);
        cv.put(TalksContract.Talks.TALK_RECORDED, talk.talk.recordedAt);
        cv.put(TalksContract.Talks.TALK_UPDATED, talk.talk.updatedAt);
        cv.put(TalksContract.Talks.TALK_VIEWED, String.valueOf(talk.talk.viewedCount));
        cv.put(TalksContract.Talks.TALK_EMAILED, String.valueOf(talk.talk.emailedCount));
        cv.put(TalksContract.Talks.TALK_COMMENTED, String.valueOf(talk.talk.commentedCount));
        cv.put(TalksContract.Talks.TALK_IMAGE_URL,
               getImageWithQuality(talk.talk.photoUrls, DEFAULT_IMAGE_QUALITY));
        cv.put(TalksContract.Talks.TALK_VIDEO_LOW,
               getVideoWithQuality(talk.talk.mediaUris.internal, DEFAULT_VIDEO_QUALITY_LOW));
        cv.put(TalksContract.Talks.TALK_VIDEO_HIGH,
               getVideoWithQuality(talk.talk.mediaUris.internal, DEFAULT_VIDEO_QUALITY_HIGH));
        // update values
        cr.update(Uri.withAppendedPath(TalksContract.Talks.CONTENT_URI,
                                       String.valueOf(talk.talk.id)),
                  cv, null, null);
    }

    /**
     * Pulls single talk with id from sqlite
     *
     * @param cr ContentResolver object
     * @param talkId id of the talk to pull
     * @return talk in Talk object
     */
    public static Talk pullTalkFromProvider(ContentResolver cr, int talkId) {
        Cursor cursor = cr.query(Uri.withAppendedPath(TalksContract.Talks.CONTENT_URI,
                String.valueOf(talkId)), null, null, null, null);
        Talk talk =  cursorToTalk(cursor);
        cursor.close();
        return talk;
    }

    /**
     * Converts data from cursor into Talk object representation
     *
     * @param cursor cursor to take talk data from
     * @return talk in Talk object
     */
    private static Talk cursorToTalk(Cursor cursor) {
        if(cursor != null && cursor.moveToFirst()) {
            if(cursor.getCount() == 1) {
                return new Talk(cursor.getInt(cursor.getColumnIndex(TalksContract.Talks._ID)))
                        .setName(cursor.getString(
                                cursor.getColumnIndex(TalksContract.Talks.TALK_NAME)))
                        .setDesc(cursor.getString(
                                cursor.getColumnIndex(TalksContract.Talks.TALK_DESC)))
                        .setPublishedAt(cursor.getString(
                                cursor.getColumnIndex(TalksContract.Talks.TALK_PUBLISHED)))
                        .setRecordedAt(cursor.getString(
                                cursor.getColumnIndex(TalksContract.Talks.TALK_RECORDED)))
                        .setUpdatedAt(cursor.getString(
                                cursor.getColumnIndex(TalksContract.Talks.TALK_UPDATED)))
                        .setViewedCount(Integer.valueOf(cursor.getString(
                                cursor.getColumnIndex(TalksContract.Talks.TALK_VIEWED))))
                        .setEmailedCount(Integer.valueOf(cursor.getString(
                                cursor.getColumnIndex(TalksContract.Talks.TALK_EMAILED))))
                        .setCommentedCount(Integer.valueOf(cursor.getString(
                                cursor.getColumnIndex(TalksContract.Talks.TALK_COMMENTED))))
                        .setImageUrl(cursor.getString(
                                cursor.getColumnIndex(TalksContract.Talks.TALK_IMAGE_URL)))
                        .setVideoLowUrl(cursor.getString(
                                cursor.getColumnIndex(TalksContract.Talks.TALK_VIDEO_LOW)))
                        .setVideoHighUrl(cursor.getString(
                                cursor.getColumnIndex(TalksContract.Talks.TALK_VIDEO_HIGH)));
            }
        }
        return null;
    }

    /**
     * Gets image url according to quality
     *
     * @param images list of images
     * @param quality resolution to choose
     * @return image url
     */
    private static String getImageWithQuality(List<Image> images, String quality) {
        for(Image image : images) {
            if(quality.equals(image.size))
                return image.url;
        }
        return null;
    }

    /**
     * Gets video url according to quality
     *
     * @param videos list of videos
     * @param quality resolution to choose
     * @return video url
     */
    private static String getVideoWithQuality(Media.Videos videos, String quality) {
        switch (quality) {
            case Media.Videos.QUALITY_180K:
                return videos.quality180k != null ? videos.quality180k.uri : null;

            case Media.Videos.QUALITY_320K:
                return videos.quality320k != null ? videos.quality320k.uri : null;

            case Media.Videos.QUALITY_450K:
                return videos.quality450k != null ? videos.quality450k.uri : null;

            case Media.Videos.QUALITY_600K:
                return videos.quality600k != null ? videos.quality600k.uri : null;

             default:
                return null;
        }
    }

}
