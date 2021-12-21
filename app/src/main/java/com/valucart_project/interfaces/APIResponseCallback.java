package com.valucart_project.interfaces;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class APIResponseCallback<T> implements Callback<T> {

    JSONObject jObjError;

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        /*try {
            Log.w("xf", "error code=" + response.errorBody().string());
        }catch (Exception e){}*/
        if (response.isSuccessful() && response.body() != null)
            this.onResponseLoaded(response.body());
        else {

            try {
                jObjError=new JSONObject();
                jObjError = new JSONObject(response.errorBody().string());
                this.onResponseError(jObjError,new Throwable("Error while calling api"));
            } catch (JSONException e) {
                onResponseError(jObjError,new Throwable("Error while calling api"));
            } catch (IOException e) {
                onResponseError(jObjError,new Throwable("Error while calling api"));
            }catch (Exception e) {
                onResponseError(jObjError,new Throwable("Error while calling api"));
            }

        }
    }
    //CLEARTEXT communication to testing.v2.api.valucart.com not permitted by network security policy
    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Log.d("api", "API Error");
        t.printStackTrace();
        onResponseError(jObjError,new Throwable("Error while calling api"));
    }

    public abstract void onResponseLoaded(T response);

    public abstract void onResponseError( JSONObject jObjError ,Throwable error);

}

