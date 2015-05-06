package tk.atna.tedtalks.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Inbound {


    public static class Feed {

        List<Talk> talks;


        static class Talk {

            Details talk;


            static class Details {

                int id;
                String name;
                String description;
//                List<Speaker> speakers;

                @SerializedName("media_profile_uris")
                Media mediaUris;

                @SerializedName("photo_urls")
                List<Image> photoUrls;

                @SerializedName("published_at")
                String publishedAt;

                @SerializedName("recorded_at")
                String recordedAt;

                @SerializedName("updated_at")
                String updatedAt;

                @SerializedName("viewed_count")
                int viewedCount;

                @SerializedName("emailed_count")
                int emailedCount;

                @SerializedName("commented_count")
                int commentedCount;

/*
                static class Speaker {

                    int id;
                    String firstname;
                    String lastname;
                    String description;
                }
*/

                static class Media {

                    Videos internal;


                    static class Videos {

                        static final String QUALITY_180K = "180k";
                        static final String QUALITY_320K = "320k";
                        static final String QUALITY_450K = "450k";
                        static final String QUALITY_600K = "600k";

                        @SerializedName(QUALITY_180K)
                        Video quality180k;

                        @SerializedName(QUALITY_320K)
                        Video quality320k;

                        @SerializedName(QUALITY_450K)
                        Video quality450k;

                        @SerializedName(QUALITY_600K)
                        Video quality600k;


                        static class Video {

                            String uri;
                        }
                    }
                }


                static class Image {

                    static final String SIZE_113X85 = "113x85";
                    static final String SIZE_240X180 = "240x180";
//                static final String SIZE_615X461 = "615x461";
//                static final String SIZE_800X600 = "800x600";

                    String size;
                    String url;
                }
            }
        }

    }

}
