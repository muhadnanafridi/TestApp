package com.valucart_project.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.valucart_project.R;
import com.valucart_project.interfaces.OnItemSelection;
import com.valucart_project.models.Vendors;
import com.valucart_project.utils.Library;

public class AdapterRvHorizantalVendors extends RecyclerView.Adapter<AdapterRvHorizantalVendors.GroceryViewHolder> {

    private Vendors horizontalVendorsList;
    Context context;
    String itemValue;
    OnItemSelection onItemSelection;
    Library library;

    public AdapterRvHorizantalVendors(Vendors horizontalVendorsList, Context context, String itemValue1 ){
        this.horizontalVendorsList= horizontalVendorsList;
        this.context = context;
        itemValue = itemValue1;
        library = new Library(context);
    }

    @Override
    public GroceryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_list_vendors_item, parent, false);
        GroceryViewHolder gvh = new GroceryViewHolder(groceryProductView);

        return gvh;
    }

    @Override
    public void onBindViewHolder(final  GroceryViewHolder holder, final int position) {
        if(horizontalVendorsList.getData().get(position).getImage() ==null){
            holder.rlItemHeader.setVisibility(View.GONE);
        }else  if(horizontalVendorsList.getData().get(position).getImage().equals("")){
            holder.rlItemHeader.setVisibility(View.GONE);
        }
        else  library.displayImage(horizontalVendorsList.getData().get(position).getImage()+ "?w=275",holder.ivVendorImage);
            //Picasso.with(context).load(horizontalVendorsList.getData().get(position).getImage() + "?w=275".replaceAll(" ", "%20")).into(holder.ivVendorImage);
    }

    @Override
    public int getItemCount() {
        return horizontalVendorsList.getData().size();
    }

    public class GroceryViewHolder extends RecyclerView.ViewHolder {
        ImageView ivVendorImage;
        RelativeLayout rlItemHeader;

        public GroceryViewHolder(View view) {
            super(view);
            ivVendorImage=view.findViewById(R.id.ivVendorImage);
            rlItemHeader=view.findViewById(R.id.rlItemHeader);
        }
    }

}

