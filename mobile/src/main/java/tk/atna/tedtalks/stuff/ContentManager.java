package tk.atna.tedtalks.stuff;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Looper;
import android.widget.ImageView;

import tk.atna.tedtalks.http.HttpHelper;
import tk.atna.tedtalks.model.ContentMapper;
import tk.atna.tedtalks.model.Inbound;
import tk.atna.tedtalks.model.Talk;


public class ContentManager {

    private static ContentManager INSTANCE;

    private Context context;

    private HttpHelper httpHelper;

    private ContentResolver contentResolver;


    private ContentManager(Context context) {
        this.context = context;
        this.httpHelper = new HttpHelper(context);
        this.contentResolver = context.getContentResolver();
    }

    /**
     * Initializes content manager.
     * It is better to give it an application context
     *
     * @param context application context
     */
    public static synchronized void init(Context context) {
        if(context == null)
            throw new NullPointerException("Can't create instance with null context");
        if(INSTANCE != null)
            throw new IllegalStateException("Can't initialize ContentManager twice");

        INSTANCE = new ContentManager(context);
    }

    /**
     * Gets only instance of content manager.
     * Can't be called from non UI thread
     *
     * @return content manager instance
     */
    public static ContentManager get() {
        if(Looper.myLooper() != Looper.getMainLooper())
            throw new IllegalStateException("Must be called from UI thread");

        if(INSTANCE == null)
            throw new IllegalStateException("ContentManager is null. It must have been"
                                          + " created at application init");

        return INSTANCE;
    }

    /**
     * Refreshes talks feed
     */
    public void getFeed() {
        loadFeedAsync(0);
     }

    /**
     * Retrieves talks earlier than this offset
     *
     * @param offset pagination indicator
     */
    public void getFeedEarlier(int offset) {
        loadFeedAsync(offset);
     }

    /**
     * Drops image with url into view
     *
     * @param url image url to get from
     * @param view image view to drop into
     */
    public void getImage(String url, ImageView view) {
        loadImageAsync(url, view);
    }

    /**
     * Makes async sqlite query to get data about single talk with id
     * and translates from cursor into talk object
     *
     * @param talkId id of talk to get
     * @param callback callback to return object with talk data
     */
    public void pullTalkFromCache(final int talkId,
                                  final ContentCallback<Talk> callback) {

        (new Worker.Task<Talk>() {
            @Override
            public void run() {
                this.result = ContentMapper.pullTalkFromProvider(contentResolver, talkId);
            }
        }).execute(new Worker.Task.Callback<Talk>() {
            @Override
            public void onComplete(Talk result, Exception ex) {
                callback.onResult(result, ex);
            }
        });
    }

    /**
     * Makes async TED server api call to get talks feed
     * and pushes it into sqlite
     *
     * @param offset pagination indicator
     */
    private void loadFeedAsync(final int offset) {

        (new Worker.SimpleTask() {
            @Override
            public void run() {
                Inbound.Feed feed = httpHelper.getFeed(offset);
                ContentMapper.pushFeedToProvider(contentResolver, feed);
            }
        }).execute(new Worker.SimpleTask.Callback() {
            @Override
            public void onComplete() {
                notifyChanges(LocalBroadcaster.ACTION_REFRESH_FEED);
            }
        });
    }

    /**
     * Loads image with url from cache/sd/server and shows it in view
     *
     * @param url image url to load from
     * @param view image view to load into
     */
    private void loadImageAsync(String url, ImageView view) {
        httpHelper.loadImage(url, view);
    }

    /**
     * Sends local broadcast notification
     *
     * @param action action to process
     */
    private void notifyChanges(int action) {
        LocalBroadcaster.sendLocalBroadcast(action, null, context);
    }


    /**
     * Content manager callback to return data after async extraction
     *
     * @param <T> Object to receive as a result
     */
    public interface ContentCallback<T> {
        /**
         * Fires when async data extraction is completed and data/exception
         * is ready to be returned
         *
         * @param result received result
         * @param exception possible exception
         */
        void onResult(T result, Exception exception);
    }

}
