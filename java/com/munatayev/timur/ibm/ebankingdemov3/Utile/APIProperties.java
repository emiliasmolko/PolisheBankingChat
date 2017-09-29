package com.munatayev.timur.ibm.ebankingdemov3.Utile;

import android.content.Context;

import java.io.IOException;
import java.util.Properties;

public class APIProperties {

    private static Properties properties;

    public APIProperties(Context context) throws IOException {
        properties = new Properties();
        properties.load(context.getAssets().open("app.properties"));
    }

    public static String getProperty(String prop){
        return properties.getProperty(prop);
    }

}
