package com.valucart_project.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.valucart_project.R;
import com.valucart_project.interfaces.APIResponseCallback;
import com.valucart_project.models.ProfileDetail;
import com.valucart_project.models.ProfileWallet;
import com.valucart_project.popups.EmailVerificationDialogFragment;
import com.valucart_project.screen.DashboardActivity;
import com.valucart_project.screen.HelpCentreScreen;
import com.valucart_project.screen.LoginScreen;
import com.valucart_project.screen.MyAddressScreen;
import com.valucart_project.screen.MyBankCardScreen;
import com.valucart_project.screen.MyScheduleScreen;
import com.valucart_project.screen.MyWishlistScreen;
import com.valucart_project.screen.ProfileScreen;
import com.valucart_project.screen.RegisterScreen;
import com.valucart_project.screen.UserWallet;
import com.valucart_project.utils.Constants;
import com.valucart_project.utils.Library;
import com.valucart_project.webservices.APIManager;

import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

public class MoreMenuFragment extends Fragment implements View.OnClickListener {

    TextView tvMySchedule,tvMyWishlist,tvMyCard,tvMyAddress,tvHelpCentre,tvShareApp,tvMyProfile,tvMyWalletValue;
    View view;
    RelativeLayout rlLogin,rlRegister,rlAfterLogin,rlValuWallet;
    LinearLayout llEditProfile,llVerification,lllogo,llLogoRegister;
    ImageView ivFb,ivInsta,ivYoutube,ivTwitter;
    ProfileDetail profileDetail;
    ProfileWallet profileWallet;
    EditText etEmailAddress,etName;
    Library library;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.more_menu_fragment,null);
        library = new Library(getContext());
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        initInterface();

        if(Constants.userLogin) {
            getProfileApi();
        }else {
            rlValuWallet.setVisibility(View.GONE);
        }
        return  view;
    }

    public void initInterface(){

        etEmailAddress = view.findViewById(R.id.etEmailAddress);
        etName = view.findViewById(R.id.etName);

        tvMySchedule = view.findViewById(R.id.tvMySchedule);
        tvMySchedule.setOnClickListener(this);
        tvMyWishlist = view.findViewById(R.id.tvMyWishlist);
        tvMyWishlist.setOnClickListener(this);
        tvMyCard = view.findViewById(R.id.tvMyCard);
        tvMyCard.setOnClickListener(this);
        tvMyAddress = view.findViewById(R.id.tvMyAddress);
        tvMyAddress.setOnClickListener(this);

        tvMyWalletValue = view.findViewById(R.id.tvMyWalletValue);

        rlValuWallet = view.findViewById(R.id.rlValuWallet);
        rlValuWallet.setOnClickListener(this);

        if(Constants.userLogin) {
            tvMyAddress.setVisibility(View.VISIBLE);
            tvMyWishlist.setVisibility(View.VISIBLE);
            tvMySchedule.setVisibility(View.VISIBLE);
        }
        else {
            tvMyAddress.setVisibility(View.GONE);
            tvMyWishlist.setVisibility(View.GONE);
            tvMySchedule.setVisibility(View.GONE);
        }

        tvHelpCentre = view.findViewById(R.id.tvHelpCentre);
        tvHelpCentre.setOnClickListener(this);
        tvShareApp = view.findViewById(R.id.tvShareApp);
        tvShareApp.setOnClickListener(this);

        tvMyProfile = view.findViewById(R.id.tvMyProfile);
        //tvMyProfile.setOnClickListener(this);

        rlLogin = view.findViewById(R.id.rlLogin);
        rlLogin.setOnClickListener(this);

        rlRegister = view.findViewById(R.id.rlRegister);
        rlRegister.setOnClickListener(this);

        llEditProfile = view.findViewById(R.id.llEditProfile);
        llEditProfile.setOnClickListener(this);

        llVerification= view.findViewById(R.id.llVerification);
        llVerification.setOnClickListener(this);

        //if(Constants.emailVerified)
        //    llVerification.setVisibility(View.GONE);

        rlAfterLogin = view.findViewById(R.id.rlAfterLogin);
        rlAfterLogin.setOnClickListener(this);

        lllogo = view.findViewById(R.id.lllogo);
        lllogo.setOnClickListener(this);

        llLogoRegister = view.findViewById(R.id.llLogoRegister);
        llLogoRegister.setOnClickListener(this);

        ivFb = view.findViewById(R.id.ivFb);
        ivFb.setOnClickListener(this);

        ivInsta = view.findViewById(R.id.ivInsta);
        ivInsta.setOnClickListener(this);

        ivYoutube = view.findViewById(R.id.ivYoutube);
        ivYoutube.setOnClickListener(this);

        ivTwitter = view.findViewById(R.id.ivTwitter);
        ivTwitter.setOnClickListener(this);

        if( Constants.userLogin){
            rlAfterLogin.setVisibility(View.VISIBLE);
            lllogo.setVisibility(View.GONE);
            llLogoRegister.setVisibility(View.GONE);
        }else {
            rlAfterLogin.setVisibility(View.GONE);
            lllogo.setVisibility(View.VISIBLE);
            llLogoRegister.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View view) {

        if(view == tvMySchedule){
            startActivity(new Intent(getContext(), MyScheduleScreen.class));
        }if(view == tvMyWishlist){
            startActivity(new Intent(getContext(), MyWishlistScreen.class));
        }if(view == tvMyCard){
            startActivity(new Intent(getContext(), MyBankCardScreen.class));
        }if(view == tvMyAddress){
            startActivity(new Intent(getContext(), MyAddressScreen.class));
        }if(view == tvHelpCentre){
            startActivity(new Intent(getContext(), HelpCentreScreen.class));
        }if(view == tvShareApp){
            //startActivity(new Intent(this, CartListingScreen.class));
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    "ValuCart - Online Grocery | Save more : https://play.google.com/store/apps/details?id=com.valucart_project");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }if(view == rlLogin){
            startActivity(new Intent(getContext(), LoginScreen.class));
        }if(view == rlRegister){
            startActivity(new Intent(getContext(), RegisterScreen.class));
        }
        if(view == llEditProfile){
            try {
                Intent intent = new Intent(getContext(), ProfileScreen.class);
                intent.putExtra("oauth_provider", "" + profileDetail.getData().getOauth_provider());
                startActivity(intent);
            }catch (Exception e){}
        }
        if(view == ivFb){
            browseUrl("https://www.facebook.com/valucart.ae/");
        }
        if(view == ivInsta){
            browseUrl("https://www.instagram.com/valucart.ae/");
        }

        if(view == ivYoutube){
            browseUrl("https://www.youtube.com/channel/UCpNYI9aDlD5WAAltDovY1CQ/videos");
        }

        if(view == ivTwitter){
            browseUrl("https://twitter.com/valucart_ae");
        }

        if(view == llVerification){
            EmailVerificationDialogFragment phoneVerificationDialogFragment = new EmailVerificationDialogFragment(getContext(),"MoreMenu");
            phoneVerificationDialogFragment.show(getFragmentManager(),"");
        }

        if(view == rlValuWallet){
            try {
                Intent intent = new Intent(getContext(), UserWallet.class);
                intent.putExtra("WalletValue", "" + profileWallet.getData().getWallet());
                startActivity(intent);
            }catch (Exception e){}
        }

    }

    public void browseUrl(String url){
        try {
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void getProfileApi() {
        //Constants.access_token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjBlMjc3MzFmOTYxOWNlZDMyOTIwZmQ3ZDU3ZDY3YWZiZDA1MDgxMzVlOWY5MjNkODdlZWY0MTRlZTA1ZmQ5ODhmN2JkMDEwYjY2YTdlNzQ3In0.eyJhdWQiOiI0ZDcxY2YwZjdlZWMwYzhiMGE1YjMxYWViYjJhMGEwNiIsImp0aSI6IjBlMjc3MzFmOTYxOWNlZDMyOTIwZmQ3ZDU3ZDY3YWZiZDA1MDgxMzVlOWY5MjNkODdlZWY0MTRlZTA1ZmQ5ODhmN2JkMDEwYjY2YTdlNzQ3IiwiaWF0IjoxNTU2MzU3NjU2LCJuYmYiOjE1NTYzNTc2NTYsImV4cCI6MTU1NzY1MzY1Niwic3ViIjoiMiIsInNjb3BlcyI6WyIqIl19.kADOrAOMtIn8aDak7uH8CmTf-q9fQm55_ohB8Cq3NlCKOlp9CNW0VCfuv4KskOB51jZf2DRZY8RknkzjGbn8lLoZfSNJxAFEnBOqaMg2MSfqktnx4X7LweZgNXouvLglugd79FahyLC_z6qY6Nh7v9qOAEqrNVMckQ4zM7megDro5BTMVREWJk64RYAxeUGFYK5zyWTyxTGHNSVOzw3imj-eAtvhnGX9pzIMgmPn4scnEAMy-uirttiT-f_lE3j-S5lPKgzjq4YqoEWghC0wASeRvTWF81Up35kQ501tVecZkEyRIDF69MBpIYc9IFs95EoiyllBVcWpNBwBOTWTrJ2EpJ3oDrZHJD8UwyHpwwH2wchzf0PLmtXSGmyPXtNIF3cWou6QT49XHb2bJ2g6VylMI-sOZlK05b1F86JVM1y02EDcBAHPr3v19JipKwjzvhyyyiMJsXwRnDlv0ZKPJ-pgpmnSlieRJMP5fS1NJsPJpOkARygDq-1LUXpD93-g_78Kt6B6F_n7ucsQlgvrSLSU5d99Sxqy9L6HORieB9QIBEzYms3ObvjmagAPCr59YGs8HF0HK8wlv5VW3jFkvKQxGR47mhZHiv5kUTDjb23AET2bfK4-JKr6G1JBDqwSs4pu80yvyZLnHygJ1ymuiyWh-ssba42EU_XKa4xFAw8";
        APIManager.getInstance().getProfile(new APIResponseCallback<ProfileDetail>() {

            @Override
            public void onResponseLoaded(@NonNull ProfileDetail response) {
                //customerBundlesList=response;
                //Constants.bundleId = ""+customerBundlesList.getData().get(0).getId();
                //itemList();
                profileDetail = response;
                Constants.profileDetail = profileDetail;
                Constants.emailVerified = profileDetail.getData().getEmail_verified();

                etEmailAddress.setText(""+profileDetail.getData().getEmail());
                etName.setText(""+profileDetail.getData().getName());
                getProfileWalletApi();
                if(Constants.emailVerified) {
                    llVerification.setVisibility(View.GONE);
                } else {
                    EmailVerificationDialogFragment phoneVerificationDialogFragment = new EmailVerificationDialogFragment(getContext(), "MoreMenu");
                    phoneVerificationDialogFragment.show(getFragmentManager(), "");
                }
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                try {

                    library.alertErrorMessage(""+jsonObject.get("message".toString()));
                    //Toast.makeText(getContext(), ""+jsonObject.get("message".toString()), Toast.LENGTH_LONG).show();

                }catch (Exception e){
                    Constants.userLogin=false;
                    rlValuWallet.setVisibility(View.GONE);

                    rlAfterLogin.setVisibility(View.GONE);
                    lllogo.setVisibility(View.VISIBLE);
                    llLogoRegister.setVisibility(View.VISIBLE);

                    tvMyAddress.setVisibility(View.GONE);
                    tvMyWishlist.setVisibility(View.GONE);
                    tvMySchedule.setVisibility(View.GONE);

                    emptyTotalCart(getContext());
                }

            }

        });
    }

    private void getProfileWalletApi() {
        //Constants.access_token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjBlMjc3MzFmOTYxOWNlZDMyOTIwZmQ3ZDU3ZDY3YWZiZDA1MDgxMzVlOWY5MjNkODdlZWY0MTRlZTA1ZmQ5ODhmN2JkMDEwYjY2YTdlNzQ3In0.eyJhdWQiOiI0ZDcxY2YwZjdlZWMwYzhiMGE1YjMxYWViYjJhMGEwNiIsImp0aSI6IjBlMjc3MzFmOTYxOWNlZDMyOTIwZmQ3ZDU3ZDY3YWZiZDA1MDgxMzVlOWY5MjNkODdlZWY0MTRlZTA1ZmQ5ODhmN2JkMDEwYjY2YTdlNzQ3IiwiaWF0IjoxNTU2MzU3NjU2LCJuYmYiOjE1NTYzNTc2NTYsImV4cCI6MTU1NzY1MzY1Niwic3ViIjoiMiIsInNjb3BlcyI6WyIqIl19.kADOrAOMtIn8aDak7uH8CmTf-q9fQm55_ohB8Cq3NlCKOlp9CNW0VCfuv4KskOB51jZf2DRZY8RknkzjGbn8lLoZfSNJxAFEnBOqaMg2MSfqktnx4X7LweZgNXouvLglugd79FahyLC_z6qY6Nh7v9qOAEqrNVMckQ4zM7megDro5BTMVREWJk64RYAxeUGFYK5zyWTyxTGHNSVOzw3imj-eAtvhnGX9pzIMgmPn4scnEAMy-uirttiT-f_lE3j-S5lPKgzjq4YqoEWghC0wASeRvTWF81Up35kQ501tVecZkEyRIDF69MBpIYc9IFs95EoiyllBVcWpNBwBOTWTrJ2EpJ3oDrZHJD8UwyHpwwH2wchzf0PLmtXSGmyPXtNIF3cWou6QT49XHb2bJ2g6VylMI-sOZlK05b1F86JVM1y02EDcBAHPr3v19JipKwjzvhyyyiMJsXwRnDlv0ZKPJ-pgpmnSlieRJMP5fS1NJsPJpOkARygDq-1LUXpD93-g_78Kt6B6F_n7ucsQlgvrSLSU5d99Sxqy9L6HORieB9QIBEzYms3ObvjmagAPCr59YGs8HF0HK8wlv5VW3jFkvKQxGR47mhZHiv5kUTDjb23AET2bfK4-JKr6G1JBDqwSs4pu80yvyZLnHygJ1ymuiyWh-ssba42EU_XKa4xFAw8";
        APIManager.getInstance().getProfileWallet(new APIResponseCallback<ProfileWallet>() {

            @Override
            public void onResponseLoaded(@NonNull ProfileWallet response) {
                profileWallet = response;
                if(profileWallet.getStatus()==0){
                    rlValuWallet.setVisibility(View.GONE);
                }else {
                    rlValuWallet.setVisibility(View.VISIBLE);
                    tvMyWalletValue.setText("" + profileWallet.getData().getWallet());
                }
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                try {
                    rlValuWallet.setVisibility(View.GONE);
                }catch (Exception e){}
            }

        });
    }
    public void emptyTotalCart(Context context){

        Constants.cart_id ="";
        Constants.totalCart = 0;

        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.valucartPreferences, MODE_PRIVATE).edit();
        editor.putString("TotalCart", "");
        editor.putString("cart_id", "");
        editor.putString("token", "");
        editor.putString("bundle_id", "");
        editor.apply();
        DashboardActivity.totalCartValue(getContext(),"0");
    }

}

