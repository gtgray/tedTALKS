package tk.atna.tedtalks.stuff;

import android.app.Application;

public class TedtalksApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        // init content manager
        ContentManager.init(this);
    }

}
