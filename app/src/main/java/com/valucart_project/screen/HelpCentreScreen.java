package com.valucart_project.screen;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.valucart_project.R;
import com.valucart_project.adapter.AdapterRvHelpCentre;
import com.valucart_project.interfaces.OnItemSelection;
import com.valucart_project.models.Grocery;
import com.valucart_project.utils.Library;

import java.util.ArrayList;
import java.util.List;

public class HelpCentreScreen extends Activity implements View.OnClickListener , OnItemSelection {

    private List<Grocery> groceryList = new ArrayList<>();
    Library library;
    RecyclerView rvAddItemHelpCentre;
    AdapterRvHelpCentre adapterHelpCentre;
    String url ;
    ImageView ivCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        library = new Library(this);
        setContentView(R.layout.help_centre_screen);
        itemList();
    }

    private void itemList(){
        ivCancel = findViewById(R.id.ivCancel);
        ivCancel.setOnClickListener(this);

        populategroceryList();
        rvAddItemHelpCentre = findViewById(R.id.rvAddItemHelpCentre);
        rvAddItemHelpCentre.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        adapterHelpCentre = new AdapterRvHelpCentre(groceryList, this, "PopularSearch", this);
        if (library.IsTablet())
            rvAddItemHelpCentre.setLayoutManager(new GridLayoutManager(this, library.getOrientationLandScape(this) ? 2 : 1));
        else
            rvAddItemHelpCentre.setLayoutManager(new GridLayoutManager(this, 1));

        rvAddItemHelpCentre.setAdapter(adapterHelpCentre);

        adapterHelpCentre.notifyDataSetChanged();


    }

    private void populategroceryList(){
        groceryList.add( new Grocery("About us"));
        groceryList.add( new Grocery("Terms & Conditions"));
        groceryList.add( new Grocery("Privacy policy"));
        groceryList.add(new Grocery("News Feeds/Blogs"));
        groceryList.add(new Grocery("FAQ"));
        groceryList.add( new Grocery("Contact us"));
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    public void onItemSelected(String value, int position) {
        if(position==0){
            url = "https://www.valucart.com/about-us";
        }else if(position==1){
            url = "https://www.valucart.com/terms-and-conditions";
        }else if(position==2){
            url = "https://www.valucart.com/privacy-policy";
        }else if(position==3){
            url = "https://valucart.wordpress.com/blog";
        }else if(position==4){
            url = "https://www.valucart.com/faq";
        }else if(position==5){
            url = "https://www.valucart.com/contact-us";
        }

        try {
            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

}

