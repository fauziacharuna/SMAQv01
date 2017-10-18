package com.smaq.apps.smaqv01;

import android.support.constraint.ConstraintLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by felix on 27/01/17.
 */

public class Constants {
    // public static final String ROOT_URL = "http://192.168.42.30/smaq/"; // local
    public static final String ROOT_URL = "http://smaq.myogir.com/";

    public static final boolean QUIZ_STATE_ANSWERS = false;
    public static final boolean QUIZ_STATE_QUESTION = true;

    public static boolean pictureChanged;

    public static ConstraintLayout TRADE_DETAIL_OVERLAY;
    public static TextView TEXT_TRADE_DETAIL_TITLE;
    public static TextView TEXT_TRADE_DETAIL_PRICE;
    public static TextView TEXT_TRADE_DETAIL_DESCRIPTION;
    public static ImageView TRADE_DETAIL_IMAGE;

    public static ConstraintLayout PROMO_DETAIL_OVERLAY;
    public static TextView TEXT_PROMO_DETAIL_TITLE;
    public static TextView TEXT_PROMO_DETAIL_DATE;
    public static TextView TEXT_PROMO_DETAIL_DESCRIPTION;
    public static ImageView PROMO_DETAIL_IMAGE;
}
