package com.pk_eagle.arpoint;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Eagle on 1/13/2016.
 */
public class UtilSharedPref {


    public static final String MyPREFERENCES = "StoryPreferences";
    public static final String USERFIRSTNAME = "ARUserFirstName";
    public static final String USERID = "ARUserID";
    public static final String USERLASTNAME = "ARUserLastName";
    public static final String USEREMAIL = "ARUserEmail";


    ////////////////////////////////

    public static String U_NAME = "";
    public static String U_ID = "";
    public static String U_FULLNAME = "";
    public static String U_EMAIL = "";
    public static String U_COUNTRY = "";
    public static String U_STATE = "";
    public static String U_ZIP = "";
    public static String U_CITY= "";
    public static String U_THUMB= "";
    public static String U_IMAGE = "";



    public static final String MSISDN = "ZD_MSISDN";

    public static final String CITY_SELECTION = "ZD_CITY_SELECTION";

    public static final String LOGIN_STATUS = "ZD_LG_STATUS";


    public static final String PASSWORD = "ZD_LG_PSWRD";

    public static final String TERMS_CONDITIONS = "TERMS_CONDITIONS";

    public static final String SOUND = "ZD_IS_SOUND_ENABLED";
    public static final String VIBRATION = "ZD_IS_VIBRATION_ENABLED";
    public static final String NOTIFICATIONS = "ZD_GCM_NOTIFICATIONS";

    public static final String DEAL_IDS = "ZD_READ_DEAL_IDS";

    public static final String ZD_CHARGES_DLG = "ZD_SUB_CHARGES";

    public static final String SEARCH_CITY = "SEARCH_CITY";
    public static final String SELECTED_AREA = "ZD_SLCTD_AREA";
    public static final String SEARCH_HISTORY = "SEARCH_HISTORY";
    public static final String SEARCH_KEYWORD = "SEARCH_KEYWORD";
    public static final String SELECTED_COMPANY = "ZD_SLCTD_CMPNY";
    public static final String INTERNAL_SEARCH_HISTORY = "INTERNAL_SEARCH_HISTORY";

    public static final String TOP_DEALS = "TOP_DEALS";
    public static final String RST_DEALS = "RST_DEALS";
    public static final String SHP_DEALS = "SHP_DEALS";
    public static final String HLT_DEALS = "HLT_DEALS";
    public static final String EDU_DEALS = "EDU_DEALS";
    public static final String ENT_DEALS = "ENT_DEALS";

    public static final String TIME_TOP = "TIME_TOP_DEALS";
    public static final String TIME_RST = "TIME_RST_DEALS";
    public static final String TIME_SHP = "TIME_SHP_DEALS";
    public static final String TIME_HLT = "TIME_HLT_DEALS";
    public static final String TIME_EDU = "TIME_EDU_DEALS";
    public static final String TIME_ENT = "TIME_ENT_DEALS";

    public static SharedPreferences sharedPreferences;

    public static boolean getBooleanVal(Context activity, final String KEY) {
        try {
            sharedPreferences = activity.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
            if (sharedPreferences != null) {
                if (sharedPreferences.contains(KEY)) {
                    return sharedPreferences.getBoolean(KEY, false);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void updateVal(Context activity, final String KEY, final boolean VAL) {
        try {
            sharedPreferences = activity.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
            if (sharedPreferences != null) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (editor != null) {
                    editor.putBoolean(KEY, VAL);
                    editor.commit();
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void updateVal(Context activity, final String KEY, final String VAL) {
        try {
            sharedPreferences = activity.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
            if (sharedPreferences != null) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (editor != null) {
                    editor.putString(KEY, VAL);
                    editor.commit();
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void updateVal(Context activity, final String KEY, final long VAL) {
        try {
            if (activity != null) {
                sharedPreferences = activity.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                if (sharedPreferences != null) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    if (editor != null) {
                        editor.putLong(KEY, VAL);
                        editor.commit();
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void updateVal(Context activity, final String KEY, final int VAL) {
        try {
            if (activity != null) {
                sharedPreferences = activity.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                if (sharedPreferences != null) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    if (editor != null) {
                        editor.putInt(KEY, VAL);
                        editor.commit();
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static String getStringVal(Context activity, final String KEY) {
        String tmp = "";
        try {
            if (activity != null) {
                sharedPreferences = activity.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                if (sharedPreferences != null) {
                    if (sharedPreferences.contains(KEY)) {
                        tmp = sharedPreferences.getString(KEY, "");
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return tmp;
    }

    public static long getLongVal(Context activity, final String KEY) {
        try {
            if (activity != null) {
                sharedPreferences = activity.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                if (sharedPreferences != null) {
                    if (sharedPreferences.contains(KEY)) {
                        return sharedPreferences.getLong(KEY, 0);
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getIntVal(Context activity, final String KEY) {
        try {
            if (activity != null) {
                sharedPreferences = activity.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                if (sharedPreferences != null) {
                    if (sharedPreferences.contains(KEY)) {
                        return sharedPreferences.getInt(KEY, 0);
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return 0;
    }

//    public static void updateVal(Activity activity, final String KEY, final String VAL) {
//        try {
//            if (activity != null) {
//                sharedPreferences = activity.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
//                if (isNotNullOrEmpty(VAL)) {
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    if (editor != null) {
//                        editor.putString(KEY, VAL);
//                        editor.commit();
//                    }
//                }
//            }
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
//    }

    public static void saveKeyToSharedPreferences(Context activity, final String KEY, final String VAL) {
        try {
            if (activity != null) {
                sharedPreferences = activity.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                if (sharedPreferences != null) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    boolean doSave = true;
                    if (editor != null) {
                        String tmp = sharedPreferences.getString(KEY, "");
                        if (tmp.equals("")) {
                            tmp = VAL;
//                        } else if (!tmp.contains(VAL)) {
//                            tmp = tmp + ":::" + VAL;
//                        } else {
//                            doSave = false;
//                        }
//                        if (doSave) {
                            editor.putString(KEY, tmp);
                            editor.commit();
                        }
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

//    public static void saveInternalSearchHistory(Activity activity, String keyword) {
//        try {
//            if (activity != null) {
//                sharedPreferences = activity.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
//                if (sharedPreferences != null) {
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    if (editor != null) {
//                        String tmp = sharedPreferences.getString(INTERNAL_SEARCH_HISTORY, "");
//                        if (isNullOrEmpty(tmp)) {
//                            tmp = keyword;
//                        } else if (!tmp.contains(keyword)) {
//                            tmp = tmp + ":::" + keyword;
//                        }
//                        /*iSearchHistory = tmp;
//                        if (isNullOrEmpty(iSearchHistory)) {
//                            iSearchHistoryArr = null;
//                        } else if (iSearchHistory.contains(":::")) {
//                            iSearchHistoryArr = iSearchHistory.split(":::");
//                        } else {
//                            iSearchHistoryArr = new String[1];
//                            iSearchHistoryArr[0] = iSearchHistory;
//                        }*/
//                        editor.putString(INTERNAL_SEARCH_HISTORY, tmp);
//                        editor.commit();
//                    }
//                }
//            }
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
//    }


}
