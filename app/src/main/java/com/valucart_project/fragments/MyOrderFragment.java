package com.valucart_project.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.valucart_project.R;
import com.valucart_project.adapter.AdapterRvInterval;
import com.valucart_project.adapter.AdapterRvMyOrder;
import com.valucart_project.interfaces.APIResponseCallback;
import com.valucart_project.interfaces.DateTimeChangeListener;
import com.valucart_project.interfaces.OnItemSelection;
import com.valucart_project.models.Intervals;
import com.valucart_project.models.MyOrder;
import com.valucart_project.popups.DateDialogFragment;
import com.valucart_project.screen.AddToCartProductScreen;
import com.valucart_project.screen.LoginScreen;
import com.valucart_project.screen.OrderSummaryDetailScreen;
import com.valucart_project.utils.Constants;
import com.valucart_project.utils.Library;
import com.valucart_project.webservices.APIManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyOrderFragment extends Fragment implements OnItemSelection , View.OnClickListener {

    private RecyclerView rvMyOrder;
    private AdapterRvMyOrder adapterRvMyOrder;
    Library library;
    View view;
    BottomSheetDialog bottomSheetDialog;
    LinearLayout llDate,llLoginFirst;
    TextView tvDate,tvNoRecordAvailable;
    SimpleDateFormat dateFormatter = new SimpleDateFormat(Constants.DateFormat);
    MyOrder myOrderList;
    EditText etMessage;
    private RecyclerView rvItemsCategories;
    private AdapterRvInterval intervalAdapter;
    Intervals intervalsList;
    String intervalId="",intervalDate="";
    ProgressBar pbBYOB;
    int currentPageNumber = 0;
    int firstTimeAvoid = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.my_order_fragment, null);
        //initInterface();
        pbBYOB = view.findViewById(R.id.pbBYOB);
        pbBYOB.setVisibility(View.GONE);

        rvMyOrder = view.findViewById(R.id.rvMyOrder);

        library = new Library(getContext());
        tvNoRecordAvailable = view.findViewById(R.id.tvNoRecordAvailable);
        tvNoRecordAvailable.setVisibility(View.GONE);

        llLoginFirst = view.findViewById(R.id.llLoginFirst);

        if(Constants.userLogin) {
            llLoginFirst.setVisibility(View.GONE);
            GetOrderApi(1);
            getInterval();
        } else {
            rvMyOrder.setVisibility(View.GONE);
            llLoginFirst.setVisibility(View.VISIBLE);
        }

        RelativeLayout rlLogin = view.findViewById(R.id.rlLogin);
        rlLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), LoginScreen.class));
            }
        });

        return view;
    }


    private void itemList(final int pageNumber) {
        if(myOrderList.getData().size()==0) {
            tvNoRecordAvailable.setVisibility(View.VISIBLE);
        }

        if (pageNumber == 1) {
            //rvAddItem.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
            adapterRvMyOrder = new AdapterRvMyOrder(myOrderList, getContext(), this);

            if (library.IsTablet())
                rvMyOrder.setLayoutManager(new GridLayoutManager(getContext(), library.getOrientationLandScape(getContext()) ? 2 : 1));
            else
                rvMyOrder.setLayoutManager(new GridLayoutManager(getContext(), 1));
            rvMyOrder.setAdapter(adapterRvMyOrder);
        }else {
            adapterRvMyOrder.notifyDataSetChanged();
        }
        firstTimeAvoid = 0;
        rvMyOrder.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!rvMyOrder.canScrollVertically(1)) {

                    if (pageNumber < myOrderList.getMeta().getLast_page()) {
                        if (firstTimeAvoid == 1) {
                            if (pageNumber > currentPageNumber) {
                                currentPageNumber = pageNumber;
                                GetOrderApi(pageNumber + 1);
                                firstTimeAvoid = 0;
                            }
                        } else {
                            firstTimeAvoid = 1;
                        }
                    }

                }
            }
        });
    }



    public void feedbackPopup(final int position) {
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_feedback, null);
        TextView tvOrderId = view.findViewById(R.id.tvOrderId);
        tvOrderId.setText(""+myOrderList.getData().get(position).getOrder_reference());
        RelativeLayout rlDone = view.findViewById(R.id.rlDone);
        etMessage = view.findViewById(R.id.etMessage);
        rlDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                sendFeedbackApi(""+myOrderList.getData().get(position).getId(),etMessage.getText().toString());
                //getContext().startActivity(new Intent(getContext(), BundleSummaryScreen.class));
                //getContext().startActivity(new Intent(getContext(), AddToCartProductScreen.class));
            }
        });

        ImageView ivCancel = view.findViewById(R.id.ivCancel);
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();

                //getContext().startActivity(new Intent(getContext(), BundleSummaryScreen.class));
                //getContext().startActivity(new Intent(getContext(), AddToCartProductScreen.class));
            }
        });


        bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.AppBottomSheetDialogTheme);
        bottomSheetDialog.setContentView(view);

        bottomSheetDialog.show();
    }

    @Override
    public void onItemSelected(String value, int position) {
        if(value.equals("OrderDetail")){
            Constants.orderReference = myOrderList.getData().get(position).getOrder_reference();
            startActivity(new Intent(getContext(), OrderSummaryDetailScreen.class));
        } else if(value.equals("Schedule")){
            reSchedulePopup(position);
        }else if(value.equals("interval")){
            intervalId = ""+intervalsList.getData().get(position).getId();
            if (intervalsList.getData().get(position).getSelectedItem()) {
                intervalsList.getData().get(position).setSelectedItem(false);
            } else {

                for (int counter = 0; counter < intervalsList.getData().size(); counter++) {
                    if (counter == position)
                        intervalsList.getData().get(counter).setSelectedItem(true);
                    else
                        intervalsList.getData().get(counter).setSelectedItem(false);
                }
            }

            if (intervalAdapter == null) {
                intervalAdapter = new AdapterRvInterval(intervalsList, getContext(), this);
                LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                rvItemsCategories.setLayoutManager(horizontalLayoutManager);
                rvItemsCategories.setAdapter(intervalAdapter);
            } else
                intervalAdapter.notifyDataSetChanged();
        }else if(value.equals("ReOrder")){
            makeReOrder(""+myOrderList.getData().get(position).getId());
        }else {
            feedbackPopup(position);
        }
    }

    public void reSchedulePopup(final int position){
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_schedule_product, null);

        RelativeLayout rlDone = view.findViewById(R.id.rlDone);
        tvDate =  view.findViewById(R.id.tvDate);


        rlDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                //startActivity(new Intent(getContext(), DashboardActivity.class));
                if(intervalId.equals(""))
                    Toast.makeText(getContext(), "Select Interval Duration".toString(), Toast.LENGTH_LONG).show();
                else if(intervalDate.equals(""))
                    Toast.makeText(getContext(), "Select Start Date".toString(), Toast.LENGTH_LONG).show();
                else
                    sendScheduleApi(""+myOrderList.getData().get(position).getId(),intervalId,intervalDate);
            }
        });

        ImageView ivCancel = view.findViewById(R.id.ivCancel);
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                //getContext().startActivity(new Intent(getContext(), BundleSummaryScreen.class));
            }
        });

        llDate = view.findViewById(R.id.llDate);
        llDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialogFragment dateTimeDialogFragment = new DateDialogFragment();
                dateTimeDialogFragment.show(getFragmentManager(), "");
                dateTimeDialogFragment.setMinDate(Calendar.getInstance().getTimeInMillis());
                dateTimeDialogFragment.setDateTimeChangeListener(new DateTimeChangeListener() {
                    @Override
                    public void onDateTimeChange(String dateTime) {
                        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                        try {
                            Date newDate = format.parse(dateTime);
                            tvDate.setText(dateFormatter.format(newDate));
                            intervalDate = ""+dateFormatter.format(newDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        rvItemsCategories = view.findViewById(R.id.rvItemsCategories);
        intervalAdapter = new AdapterRvInterval(intervalsList, getContext(), this);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvItemsCategories.setLayoutManager(horizontalLayoutManager);
        rvItemsCategories.setAdapter(intervalAdapter);

        bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.AppBottomSheetDialogTheme);
        bottomSheetDialog.setContentView(view);

        bottomSheetDialog.show();
    }

    @Override
    public void onClick(View v) {

    }
    int currentPage;
    private void GetOrderApi(int pageNumber) {
        currentPage = pageNumber;
        if (currentPage > 1)
           pbBYOB.setVisibility(View.VISIBLE);
        else
            library.showLoading();
        APIManager.getInstance().getMyOrder(pageNumber,14,new APIResponseCallback<MyOrder>() {

            @Override
            public void onResponseLoaded(@NonNull MyOrder response) {
                library.hideLoading();
                pbBYOB.setVisibility(View.GONE);
                //myOrderList=response;
                if (currentPage != 1) {
                    for (int counter = 0; counter < response.getData().size(); counter++) {
                        myOrderList.getData().add(response.getData().get(counter));
                    }
                    itemList(currentPage);
                } else {
                    myOrderList = response;
                    if(myOrderList.getData().size()>0) {
                        rvMyOrder.setVisibility(View.VISIBLE);
                        itemList(currentPage);
                    }
                    else {
                        rvMyOrder.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                library.hideLoading();
                pbBYOB.setVisibility(View.GONE);
                tvNoRecordAvailable.setVisibility(View.VISIBLE);
                try {
                    library.alertErrorMessage(""+jsonObject.get("message".toString()));
//                    Toast.makeText(getContext(), ""+jsonObject.get("message".toString()), Toast.LENGTH_LONG).show();
                }catch (Exception e){}

            }

        });
    }

    private void sendFeedbackApi(String orderId , String message) {
        library.showLoading();
        APIManager.getInstance().sendFeedbackApi(orderId , message , new APIResponseCallback<JsonElement>() {

            @Override
            public void onResponseLoaded(@NonNull JsonElement response) {
                try {
                    library.hideLoading();
                    Toast.makeText(getContext(), response.getAsJsonObject().get("message").getAsString(), Toast.LENGTH_LONG).show();
                }catch (Exception e){}
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                library.hideLoading();
                try {
                    library.alertErrorMessage(""+jsonObject.get("message".toString()));
//                    Toast.makeText(getContext(), ""+jsonObject.get("message".toString()), Toast.LENGTH_LONG).show();
                }catch (Exception e){}

            }

        });
    }


    private void getInterval() {
        APIManager.getInstance().getInterval(new APIResponseCallback<Intervals>() {

            @Override
            public void onResponseLoaded(@NonNull Intervals response) {
                try {
                    intervalsList = response;
                }catch (Exception e){}
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                try {
                    library.alertErrorMessage(""+jsonObject.get("message".toString()));
//                    Toast.makeText(getContext(), ""+jsonObject.get("message".toString()), Toast.LENGTH_LONG).show();
                }catch (Exception e){}

            }

        });
    }

    private void sendScheduleApi(String orderId , String intervalId,String intervalDate) {
        //Constants.access_token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjBlMjc3MzFmOTYxOWNlZDMyOTIwZmQ3ZDU3ZDY3YWZiZDA1MDgxMzVlOWY5MjNkODdlZWY0MTRlZTA1ZmQ5ODhmN2JkMDEwYjY2YTdlNzQ3In0.eyJhdWQiOiI0ZDcxY2YwZjdlZWMwYzhiMGE1YjMxYWViYjJhMGEwNiIsImp0aSI6IjBlMjc3MzFmOTYxOWNlZDMyOTIwZmQ3ZDU3ZDY3YWZiZDA1MDgxMzVlOWY5MjNkODdlZWY0MTRlZTA1ZmQ5ODhmN2JkMDEwYjY2YTdlNzQ3IiwiaWF0IjoxNTU2MzU3NjU2LCJuYmYiOjE1NTYzNTc2NTYsImV4cCI6MTU1NzY1MzY1Niwic3ViIjoiMiIsInNjb3BlcyI6WyIqIl19.kADOrAOMtIn8aDak7uH8CmTf-q9fQm55_ohB8Cq3NlCKOlp9CNW0VCfuv4KskOB51jZf2DRZY8RknkzjGbn8lLoZfSNJxAFEnBOqaMg2MSfqktnx4X7LweZgNXouvLglugd79FahyLC_z6qY6Nh7v9qOAEqrNVMckQ4zM7megDro5BTMVREWJk64RYAxeUGFYK5zyWTyxTGHNSVOzw3imj-eAtvhnGX9pzIMgmPn4scnEAMy-uirttiT-f_lE3j-S5lPKgzjq4YqoEWghC0wASeRvTWF81Up35kQ501tVecZkEyRIDF69MBpIYc9IFs95EoiyllBVcWpNBwBOTWTrJ2EpJ3oDrZHJD8UwyHpwwH2wchzf0PLmtXSGmyPXtNIF3cWou6QT49XHb2bJ2g6VylMI-sOZlK05b1F86JVM1y02EDcBAHPr3v19JipKwjzvhyyyiMJsXwRnDlv0ZKPJ-pgpmnSlieRJMP5fS1NJsPJpOkARygDq-1LUXpD93-g_78Kt6B6F_n7ucsQlgvrSLSU5d99Sxqy9L6HORieB9QIBEzYms3ObvjmagAPCr59YGs8HF0HK8wlv5VW3jFkvKQxGR47mhZHiv5kUTDjb23AET2bfK4-JKr6G1JBDqwSs4pu80yvyZLnHygJ1ymuiyWh-ssba42EU_XKa4xFAw8";
        library.showLoading();
        APIManager.getInstance().sendScheduleApi(orderId , intervalId ,intervalDate, new APIResponseCallback<JsonElement>() {

            @Override
            public void onResponseLoaded(@NonNull JsonElement response) {
                try {
                    library.hideLoading();
                    Toast.makeText(getContext(), response.getAsJsonObject().get("message").getAsString(), Toast.LENGTH_LONG).show();
                }catch (Exception e){}
            }
//{"status":1,"message":"The given data was invalid.","errors":{"start_date":["Start date can not be today or past and can not be Friday."]}}
            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                    try {
                        library.hideLoading();
                        JSONObject jsonObject1 = new JSONObject(jsonObject.getString("errors"));
                        if (jsonObject1.toString().contains("start_date")) {
                            JSONArray jsonArray = new JSONArray(jsonObject1.getString("start_date").toString());
                            library.alertErrorMessage(""+jsonArray.get(0).toString().toString());
//                            Toast.makeText(getContext(), jsonArray.get(0).toString(), Toast.LENGTH_LONG).show();
                        } else if (jsonObject1.toString().contains("interval_id")) {
                            JSONArray jsonArray = new JSONArray(jsonObject1.getString("interval_id").toString());
                            library.alertErrorMessage(""+jsonArray.get(0).toString().toString());
                            //Toast.makeText(getContext(), jsonArray.get(0).toString(), Toast.LENGTH_LONG).show();
                        }else {
                            library.alertErrorMessage(""+jsonObject.getString("message").toString());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        library.alertErrorMessage("The given data was invalid.");
//                        Toast.makeText(getContext(),"The given data was invalid.",Toast.LENGTH_LONG).show();
                    }catch (Exception e) {
                        e.printStackTrace();
                        library.alertErrorMessage("The given data was invalid.");
                        //Toast.makeText(getContext(),"The given data was invalid.",Toast.LENGTH_LONG).show();
                    }

            }

        });
    }


    private void makeReOrder(String orderId ) {

        library.showLoading();
        APIManager.getInstance().reOrder(orderId , new APIResponseCallback<JsonElement>() {

            @Override
            public void onResponseLoaded(@NonNull JsonElement response) {
                try {
                    library.hideLoading();
                    if(response.getAsJsonObject().get("status").getAsString().equals("1")){
                        startActivity(new Intent(getContext(), AddToCartProductScreen.class));
                    }
                    Toast.makeText(getContext(), response.getAsJsonObject().get("message").getAsString(), Toast.LENGTH_LONG).show();
                }catch (Exception e){}
            }
            //{"status":1,"message":"The given data was invalid.","errors":{"start_date":["Start date can not be today or past and can not be Friday."]}}
            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                try {
                    library.hideLoading();
                    JSONObject jsonObject1 = new JSONObject(jsonObject.getString("errors"));
                    if (jsonObject1.toString().contains("order")) {
                        JSONArray jsonArray = new JSONArray(jsonObject1.getString("order").toString());
                        library.alertErrorMessage(""+jsonArray.get(0).toString().toString());
//                            Toast.makeText(getContext(), jsonArray.get(0).toString(), Toast.LENGTH_LONG).show();
                    }else {
                        library.alertErrorMessage(""+jsonObject.getString("message").toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    library.alertErrorMessage("The given data was invalid.");
//                        Toast.makeText(getContext(),"The given data was invalid.",Toast.LENGTH_LONG).show();
                }catch (Exception e) {
                    e.printStackTrace();
                    library.alertErrorMessage("The given data was invalid.");
                    //Toast.makeText(getContext(),"The given data was invalid.",Toast.LENGTH_LONG).show();
                }

            }

        });
    }

}

