package br.com.acmestore;

public class Constants {

    //extra intents
    public static final String INTENT_KEY_USER = "user";
    public static final String INTENT_KEY_PRODUCTID = "productId";
    public static final String INTENT_KEY_PRODUCTLIST = "productList";
    public static final String INTENT_KEY_ISANYPRODUCTSELECTED = "isAnyProductSelected";
    public static final String INTENT_KEY_FROMVIEW = "fromView";
    public static final String INTENT_KEY_CURRENTPAGE = "currentPage";
    public static final String INTENT_KEY_MESSAGE = "message";
    public static final String INTENT_KEY_FROMNOTIFICATION = "fromNotification";

    //request codes
    public static final int REQUESTCODE_PRODUCTADD = 100;
    public static final int REQUESTCODE_PRODUCTDETAIL = 200;

    //texts
    public static final String COMMA = ",";
    public static final String BLANK_SPACE = " ";
    public static final String EMPTY_SPACE = "";
    public static final String DOLLAR_SIGN = "U$";

    //endpoint constants
    public static final String BASE_SERVICE_URL = "http://acmestorewebapp.azurewebsites.net/api/";
    public static final String BASE_RANDOM_IMAGE = "http://lorempixel.com/100/100/technics/";
}
