package com.example.food_safari;

import android.app.Application;
import android.app.Application;
import io.smooch.core.Settings;
import io.smooch.core.Smooch;
import io.smooch.core.SmoochCallback;

public class ChatApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Settings settings = new Settings("YOUR_APP_ID");
        settings.setMapsApiKey("YOUR_MAPS_API_KEY");

        Smooch.init(this, settings, new SmoochCallback() {
            @Override
            public void run(Response response) {
                if (response.getError() == null) {
                    // Your code after init is complete
                } else {
                    // Something went wrong during initialization
                }
            }
        });
    }
}
