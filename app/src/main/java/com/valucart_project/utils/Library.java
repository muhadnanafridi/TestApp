package com.valucart_project.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.annotation.NonNull;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.JsonElement;
import com.squareup.picasso.Picasso;
import com.valucart_project.R;
import com.valucart_project.application.ValuCartApp;
import com.valucart_project.interfaces.APIResponseCallback;
import com.valucart_project.interfaces.SpinnerViewSelection;
import com.valucart_project.webservices.APIManager;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class Library {

    private Context context;
    private Activity activity;
    private ProgressDialog progressDialog = null;
    private Toast toast = null;
    //smart grocery for smart people

    public Library(Context context) {
        //super(context, new Constants());
        this.context = context;
        activity = (Activity) context;
    }


    public void showLoading() {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                /*
                progressDialog = ProgressDialog.show(context, "", "Loading...", true, true);
                progressDialog.setCancelable(false);
                */
                progressDialog = new ProgressDialog(context,R.style.MyTheme);
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener(){
                    @Override
                    public void onCancel(DialogInterface dialog){
                        /****cleanup code****/
                    }});
                progressDialog.setOnKeyListener(new ProgressDialog.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface arg0, int keyCode,
                                         KeyEvent event) {
                        // TODO Auto-generated method stub
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                        }
                        return true;
                    }
                });
                if (!progressDialog.isShowing()) {
                    progressDialog.show();
                }
            }
        });
    }


    public void hideLoading() {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                try {
                    if (progressDialog != null) {
                        if (progressDialog.isShowing()) {
                            progressDialog.cancel();
                        }
                    }
                } catch (Exception e) {}
            }
        });
    }

    public void showToast(final String message) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                if (toast == null) {
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View view = inflater.inflate(R.layout.toast_custom_layout, null, false);
                    TextView txtMessage = view.findViewById(R.id.txtToastMessage);
                    txtMessage.setText(message);
                    toast = new Toast(context.getApplicationContext());
                    toast.setView(view);
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                } else {
                    ((TextView) toast.getView().findViewById(R.id.txtToastMessage)).setText(message);
                }
                toast.show();
            }
        });
    }


    public void messageBox(final String massage) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder alertbox = new AlertDialog.Builder(context);
                alertbox.setTitle(activity.getResources().getString(R.string.app_name));
                alertbox.setMessage(massage);
                alertbox.setIcon(R.mipmap.ic_launcher);
                alertbox.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        });

                alertbox.show();
            }
        });
    }

    public void messageBoxFinish(final String massage) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder alertbox = new AlertDialog.Builder(context);
                alertbox.setTitle(activity.getResources().getString(R.string.app_name));
                alertbox.setMessage(massage);
                alertbox.setIcon(R.mipmap.ic_launcher);
                alertbox.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                activity.finish();
                            }
                        });

                alertbox.show();
            }
        });
    }


    public boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected()) {
                    haveConnectedWifi = true;
                    return haveConnectedWifi;
                }
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected()) {
                    haveConnectedMobile = true;
                    return haveConnectedMobile;
                }
        }
        return false;
    }


    public String convertBitmapToByteArray(Bitmap bitmap) {
        byte[] byteArray = null;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            int mbSize = (bitmap.getByteCount() / 1024) / 1024;

            if (mbSize <= 10) {

                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);


            } else if (mbSize <= 15) {

                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
            } else if (mbSize <= 25) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
            } else {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOutputStream);
            }

            byteArray = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        //return Base64.encodeToString(byteArray, Base64.NO_WRAP);
        return Base64.encodeToString(byteArray, Base64.NO_WRAP);
    }


    public int convertPxToDp(int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public void hideKeyBoard() {
        View view = ((Activity) context).getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void showKeyBoard() {
        View view = ((Activity) context).getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public Boolean IsTablet(){
       return context.getResources().getBoolean(R.bool.isTablet);
    }

    public int getScreenOrientation(Context context){
        final int screenOrientation = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getOrientation();
        switch (screenOrientation) {
            case Surface.ROTATION_0:
                return 2;
            case Surface.ROTATION_90:
                return 4;
            case Surface.ROTATION_180:
                return 2;
            default:
                return 4;
        }
    }

    public Boolean getOrientationLandScape(Context context){
        final int screenOrientation = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getOrientation();
        switch (screenOrientation) {
            case Surface.ROTATION_0:
                return false;
            case Surface.ROTATION_90:
                return true;
            case Surface.ROTATION_180:
                return false;
            default:
                return true;
        }
    }

    public   void apiLoadItemToWishlist(String action , String item_id , String item_type) {
        //{"status":1,"data":{"cart_id":5,"item_type":"product","item_id":17,"quantity":1,"total_items":1}}
        APIManager.getInstance().addWishlist(action, item_id,item_type, new APIResponseCallback<JsonElement>() {

            @Override
            public void onResponseLoaded(@NonNull JsonElement response) {
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                //Toast.makeText(context,"",Toast.LENGTH_LONG).show();
                try {
                    alertErrorMessage(""+jsonObject.get("message").toString());

                    //Toast.makeText(context, ""+jsonObject.get("message".toString()), Toast.LENGTH_LONG).show();
                }catch (Exception e){}

            }

        });
    }

    BottomSheetDialog mBottomSheetDialog;
    public void bottomsheetSort(Activity activity, final SpinnerViewSelection spinnerViewSelection){
        mBottomSheetDialog = new BottomSheetDialog(context, R.style.AppBottomSheetDialogTheme);
        View sheetView = activity.getLayoutInflater().inflate(R.layout.sort_by_bottom_sheet, null);
        ImageView ivCancel = sheetView.findViewById(R.id.ivCancel);
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mBottomSheetDialog.dismiss();
            }
        });

        LinearLayout llMinimumPrice = sheetView.findViewById(R.id.llMinimumPrice);
        llMinimumPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerViewSelection.onValueSelected("price:low-high",1);
                mBottomSheetDialog.dismiss();
            }
        });

        LinearLayout llMaximumPrice = sheetView.findViewById(R.id.llMaximumPrice);
        llMaximumPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerViewSelection.onValueSelected("price:high-low",2);
                mBottomSheetDialog.dismiss();
            }
        });

        LinearLayout llLatestProducts = sheetView.findViewById(R.id.llLatestProducts);
        llLatestProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerViewSelection.onValueSelected("price:latest",3);
                mBottomSheetDialog.dismiss();
            }
        });

        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.show();
    }


    public void alertErrorMessage(String message){
        new AlertDialog.Builder(context)
                .setMessage(message)

                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                //.setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void displayImage(String imageurl,ImageView view){
        URL sourceUrl = null;
        //String temp = imageurl.replace("https", "http");
        String temp = imageurl.replace("https", "https");
        temp = temp.replaceAll(" ", "%20");
        try {
            sourceUrl = new URL(temp);
            //Picasso.with(context).load(String.valueOf(sourceUrl.toURI())).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).into(view);
            Picasso.with(context).load(String.valueOf(sourceUrl.toURI())).into(view);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }catch (Exception e){}
    }


}
