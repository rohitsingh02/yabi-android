package com.yabi;

import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.json.JsonFactory;

/**
 * Created by yogeshmadaan on 03/01/16.
 */
public class AppConstants {
    /**
     * Your WEB CLIENT ID from the API Access screen of the Developer Console for your project. This
     * is NOT the Android client id from that screen.
     *
     * @see <a href="https://developers.google.com/console">https://developers.google.com/console</a>
     */
    public static final String WEB_CLIENT_ID = "1073204284509-brq0386avrh5bnf4ueo0r1obef0j7b56.apps.googleusercontent.com";

    /**
     * The audience is defined by the web client id, not the Android client id.
     */
    public static final String AUDIENCE = "server:client_id:" + WEB_CLIENT_ID;

    /**
     * Class instance of the JSON factory.
     */
    public static final JsonFactory JSON_FACTORY = new AndroidJsonFactory();


}
