package tk.atna.tedtalks.http;

import retrofit.http.GET;
import retrofit.http.Query;
import tk.atna.tedtalks.model.Inbound;

interface ServerApi {

    String SERVER_URL = "https://api.ted.com/v1";
    String TALKS = "/talks";

    String API_KEY = "api-key";
    String ADDITIONAL_FIELDS = "fields";
    String ORDER = "order";
    String LIMIT = "limit";
    String OFFSET = "offset";

    String PHOTO_URLS = "photo_urls";
    String MEDIA_URIS = "media_profile_uris";
    String SPEAKERS = "speakers";
    String VIEWED = "viewed_count";
    String EMAILED = "emailed_count";
    String COMMENTED = "commented_count";

    String DEFAULT_ADDITIONAL_FIELDS = PHOTO_URLS + "," + MEDIA_URIS + ","
                                     + SPEAKERS + "," + VIEWED + ","
                                     + EMAILED + "," + COMMENTED;

    String DEFAULT_ORDER = "published_at:desc";
    int DEFAULT_LIMIT = 10;
    String DEFAULT_FORMAT = ".json";

    String FEED_ENDPOINT = TALKS + DEFAULT_FORMAT;


    @GET(FEED_ENDPOINT)
    Inbound.Feed getFeed(
            @Query(API_KEY) String key,
            @Query(ADDITIONAL_FIELDS) String fields,
            @Query(ORDER) String order,
            @Query(LIMIT) int limit,
            @Query(OFFSET) int offset
    );

}
