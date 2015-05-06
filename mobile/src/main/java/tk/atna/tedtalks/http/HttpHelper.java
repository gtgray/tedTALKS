package tk.atna.tedtalks.http;


import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import retrofit.RestAdapter;
import tk.atna.tedtalks.R;
import tk.atna.tedtalks.model.Inbound;

public class HttpHelper {

    private final static int DEFAULT_PICTURE = R.drawable.ic_picture_default;

    private ServerApi api;

    private final Context context;

    // TED api key
    private String key;


    public HttpHelper(Context context) {
        this.context = context;
        this.key = context.getString(R.string.api_key);
        // setup retrofit
        RestAdapter ra = new RestAdapter.Builder()
                .setEndpoint(ServerApi.SERVER_URL)
                .setLogLevel(RestAdapter.LogLevel.BASIC)
                .build();
        // retrofit server api inflater
        api = ra.create(ServerApi.class);
    }

    /**
     * Makes TED server api call to get talks feed
     *
     * @param offset pagination indicator
     * @return talks feed model object (parsed from json)
     */
    public Inbound.Feed getFeed(int offset) {
        if(key == null)
            throw new IllegalArgumentException("API key can't be null");

        return api.getFeed(key, ServerApi.DEFAULT_ADDITIONAL_FIELDS,
                           ServerApi.DEFAULT_ORDER, ServerApi.DEFAULT_LIMIT, offset);
    }

    /**
     * Loads image with url from cache/sd/server and shows it in view
     *
     * @param url image url to load from
     * @param view image view to load into
     */
    public void loadImage(String url, ImageView view) {

        Picasso.with(context)
               .load(url)
               .placeholder(DEFAULT_PICTURE)
               .error(DEFAULT_PICTURE)
               .into(view);
    }

}

