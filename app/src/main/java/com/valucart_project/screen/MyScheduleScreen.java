package com.valucart_project.screen;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.JsonElement;
import com.valucart_project.R;
import com.valucart_project.adapter.AdapterRvInterval;
import com.valucart_project.adapter.AdapterRvMySchedule;
import com.valucart_project.interfaces.APIResponseCallback;
import com.valucart_project.interfaces.DateTimeChangeListener;
import com.valucart_project.interfaces.OnItemSelection;
import com.valucart_project.models.Grocery;
import com.valucart_project.models.Intervals;
import com.valucart_project.models.Scheduled;
import com.valucart_project.popups.DateDialogFragment;
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

public class MyScheduleScreen extends FragmentActivity implements View.OnClickListener, OnItemSelection {

    private RecyclerView rvMySchedule;
    private AdapterRvMySchedule adapterRvMySchedule;
    Library library;
    LinearLayout llDate;
    TextView tvDate;
    SimpleDateFormat dateFormatter = new SimpleDateFormat(Constants.DateFormat);
    TextView tvHome, tvOffice, tvOther;
    LinearLayout llHome, llOffice, llOther;
    BottomSheetDialog bottomSheetDialog;
    Scheduled scheduledList;
    TextView tvNoRecordAvailable;
    String intervalId="",intervalDate="";
    private RecyclerView rvItemsCategories;
    private AdapterRvInterval intervalAdapter;
    Intervals intervalsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.my_schedule_screen);
        library = new Library(this);
        
        rvMySchedule = findViewById(R.id.rvMySchedule);

        tvNoRecordAvailable = findViewById(R.id.tvNoRecordAvailable);
        ImageView ivCancel = findViewById(R.id.ivCancel);
        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        loadMySchedule();
        getInterval();
    }

    private void itemList() {
        // add a divider after each item for more clarity
        //rvAddItem.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        adapterRvMySchedule = new AdapterRvMySchedule(scheduledList, this, this);

        if (library.IsTablet())
            rvMySchedule.setLayoutManager(new GridLayoutManager(this, library.getOrientationLandScape(getApplicationContext()) ? 2 : 1));
        else
            rvMySchedule.setLayoutManager(new GridLayoutManager(this, 1));
        rvMySchedule.setAdapter(adapterRvMySchedule);

        adapterRvMySchedule.notifyDataSetChanged();
    }

    private void loadMySchedule() {
        library.showLoading();
        APIManager.getInstance().getMySchedule(new APIResponseCallback<Scheduled>() {

            @Override
            public void onResponseLoaded(@NonNull Scheduled response) {
                library.hideLoading();

                if(response.getData().size()!=0) {
                    scheduledList=new Scheduled();
                    scheduledList = response;
                    itemList();
                }else {
                    scheduledList=new Scheduled();
                    tvNoRecordAvailable.setVisibility(View.VISIBLE);
                    rvMySchedule.setVisibility(View.GONE);
                }
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                library.hideLoading();
                try {
                    library.alertErrorMessage(""+jsonObject.get("message").toString());
//                    Toast.makeText(getApplicationContext(), ""+jsonObject.get("message".toString()), Toast.LENGTH_LONG).show();
                }catch (Exception e){}

            }

        });
    }

    private void deleteMySchedule(String orderId) {
        library.showLoading();
        APIManager.getInstance().deleteMySchedule(orderId,new APIResponseCallback<JsonElement>() {

            @Override
            public void onResponseLoaded(@NonNull JsonElement response) {
                library.hideLoading();
                Toast.makeText(getApplicationContext(),"Schedule deleted successfully",Toast.LENGTH_LONG).show();
                loadMySchedule();
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                library.hideLoading();
                try {
                    library.alertErrorMessage(""+jsonObject.get("message").toString());
//                    Toast.makeText(getApplicationContext(), ""+jsonObject.get("message".toString()), Toast.LENGTH_LONG).show();
                }catch (Exception e){}

            }

        });
    }


    public void reSchedulePopup() {
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_schedule_product, null);

        RelativeLayout rlDone = view.findViewById(R.id.rlDone);
        tvDate = view.findViewById(R.id.tvDate);

        tvHome = view.findViewById(R.id.tvHome);
        tvHome.setOnClickListener(this);

        tvOffice = view.findViewById(R.id.tvOffice);
        tvOffice.setOnClickListener(this);

        tvOther = view.findViewById(R.id.tvOther);
        tvOther.setOnClickListener(this);

        llHome = view.findViewById(R.id.llHome);

        llOffice = view.findViewById(R.id.llOffice);

        llOther = view.findViewById(R.id.llOther);

        rlDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                //getContext().startActivity(new Intent(getContext(), BundleSummaryScreen.class));
                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
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
                dateTimeDialogFragment.show(getSupportFragmentManager(), "");
                dateTimeDialogFragment.setMinDate(Calendar.getInstance().getTimeInMillis());
                dateTimeDialogFragment.setDateTimeChangeListener(new DateTimeChangeListener() {
                    @Override
                    public void onDateTimeChange(String dateTime) {
                        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                        try {
                            Date newDate = format.parse(dateTime);
                            tvDate.setText(dateFormatter.format(newDate));

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        bottomSheetDialog = new BottomSheetDialog(this, R.style.AppBottomSheetDialogTheme);
        bottomSheetDialog.setContentView(view);

        bottomSheetDialog.show();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemSelected(String value, int position) {

        if (value.equals("EditSchedule")) {
            reSchedulePopup(position);
        } else if(value.equals("interval")){
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
                intervalAdapter = new AdapterRvInterval(intervalsList, this, this);
                LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                rvItemsCategories.setLayoutManager(horizontalLayoutManager);
                rvItemsCategories.setAdapter(intervalAdapter);
            } else
                intervalAdapter.notifyDataSetChanged();
        }else {
            deleteMySchedule(value);
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
                    Toast.makeText(MyScheduleScreen.this, "Select Interval Duration".toString(), Toast.LENGTH_LONG).show();
                else if(intervalDate.equals(""))
                    Toast.makeText(MyScheduleScreen.this, "Select Start Date".toString(), Toast.LENGTH_LONG).show();
                else
                    sendScheduleApi(""+scheduledList.getData().get(position).getOrder_id(),intervalId,intervalDate);
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
                dateTimeDialogFragment.show(getSupportFragmentManager(), "");
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
        intervalAdapter = new AdapterRvInterval(intervalsList, this, this);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvItemsCategories.setLayoutManager(horizontalLayoutManager);
        rvItemsCategories.setAdapter(intervalAdapter);

        bottomSheetDialog = new BottomSheetDialog(this, R.style.AppBottomSheetDialogTheme);
        bottomSheetDialog.setContentView(view);

        bottomSheetDialog.show();
    }

    private void sendScheduleApi(String orderId , String intervalId,String intervalDate) {
        //Constants.access_token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjBlMjc3MzFmOTYxOWNlZDMyOTIwZmQ3ZDU3ZDY3YWZiZDA1MDgxMzVlOWY5MjNkODdlZWY0MTRlZTA1ZmQ5ODhmN2JkMDEwYjY2YTdlNzQ3In0.eyJhdWQiOiI0ZDcxY2YwZjdlZWMwYzhiMGE1YjMxYWViYjJhMGEwNiIsImp0aSI6IjBlMjc3MzFmOTYxOWNlZDMyOTIwZmQ3ZDU3ZDY3YWZiZDA1MDgxMzVlOWY5MjNkODdlZWY0MTRlZTA1ZmQ5ODhmN2JkMDEwYjY2YTdlNzQ3IiwiaWF0IjoxNTU2MzU3NjU2LCJuYmYiOjE1NTYzNTc2NTYsImV4cCI6MTU1NzY1MzY1Niwic3ViIjoiMiIsInNjb3BlcyI6WyIqIl19.kADOrAOMtIn8aDak7uH8CmTf-q9fQm55_ohB8Cq3NlCKOlp9CNW0VCfuv4KskOB51jZf2DRZY8RknkzjGbn8lLoZfSNJxAFEnBOqaMg2MSfqktnx4X7LweZgNXouvLglugd79FahyLC_z6qY6Nh7v9qOAEqrNVMckQ4zM7megDro5BTMVREWJk64RYAxeUGFYK5zyWTyxTGHNSVOzw3imj-eAtvhnGX9pzIMgmPn4scnEAMy-uirttiT-f_lE3j-S5lPKgzjq4YqoEWghC0wASeRvTWF81Up35kQ501tVecZkEyRIDF69MBpIYc9IFs95EoiyllBVcWpNBwBOTWTrJ2EpJ3oDrZHJD8UwyHpwwH2wchzf0PLmtXSGmyPXtNIF3cWou6QT49XHb2bJ2g6VylMI-sOZlK05b1F86JVM1y02EDcBAHPr3v19JipKwjzvhyyyiMJsXwRnDlv0ZKPJ-pgpmnSlieRJMP5fS1NJsPJpOkARygDq-1LUXpD93-g_78Kt6B6F_n7ucsQlgvrSLSU5d99Sxqy9L6HORieB9QIBEzYms3ObvjmagAPCr59YGs8HF0HK8wlv5VW3jFkvKQxGR47mhZHiv5kUTDjb23AET2bfK4-JKr6G1JBDqwSs4pu80yvyZLnHygJ1ymuiyWh-ssba42EU_XKa4xFAw8";
        library.showLoading();
        APIManager.getInstance().sendScheduleApi(orderId , intervalId ,intervalDate, new APIResponseCallback<JsonElement>() {

            @Override
            public void onResponseLoaded(@NonNull JsonElement response) {
                try {
                    library.hideLoading();
                    loadMySchedule();
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
                        library.alertErrorMessage(""+jsonArray.get(0).toString());
//                        Toast.makeText(getApplicationContext(), jsonArray.get(0).toString(), Toast.LENGTH_LONG).show();
                    } else if (jsonObject1.toString().contains("interval_id")) {
                        JSONArray jsonArray = new JSONArray(jsonObject1.getString("interval_id").toString());
                        library.alertErrorMessage(""+jsonArray.get(0).toString());
//                        Toast.makeText(getApplicationContext(), jsonArray.get(0).toString(), Toast.LENGTH_LONG).show();
                    }else {
                        library.alertErrorMessage(""+jsonObject.getString("message").toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    library.alertErrorMessage("The given data was invalid.");
                    //Toast.makeText(getApplicationContext(),"The given data was invalid.",Toast.LENGTH_LONG).show();
                }catch (Exception e) {
                    e.printStackTrace();
                    library.alertErrorMessage("The given data was invalid.");
//                    Toast.makeText(getApplicationContext(),"The given data was invalid.",Toast.LENGTH_LONG).show();
                }

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
                    Toast.makeText(getApplicationContext(), ""+jsonObject.get("message".toString()), Toast.LENGTH_LONG).show();
                }catch (Exception e){}

            }

        });
    }

}

