package com.valucart_project.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.valucart_project.R;
import com.valucart_project.interfaces.OnItemSelection;
import com.valucart_project.models.ByobSummary;
import com.valucart_project.utils.Library;
import com.valucart_project.views.ApproveBundleCartView;

public class AdapterRvBYOBApproval extends RecyclerView.Adapter<AdapterRvBYOBApproval.GroceryViewHolder> implements OnItemSelection {

    private ByobSummary.Data  horizontalGrocderyList;
    Context context;
    OnItemSelection onItemSelection;
    Library library;

    public AdapterRvBYOBApproval(ByobSummary.Data horizontalGrocderyList, Context context, OnItemSelection onItemSelection1){
        this.horizontalGrocderyList= horizontalGrocderyList;
        this.context = context;
        onItemSelection = onItemSelection1;
        library = new Library(context);
    }


    @Override
    public GroceryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.build_your_own_bundle_approval_item, parent, false);
        GroceryViewHolder gvh = new GroceryViewHolder(groceryProductView);
        return gvh;
    }

    @Override
    public void onBindViewHolder(GroceryViewHolder holder, final int position) {

            holder.rlItemHeader.setVisibility(View.VISIBLE);
            holder.approveBundleCartView.addItemListener(horizontalGrocderyList.getProducts().get(position).getQuantity());
            holder.approveBundleCartView.cartViewCallBack(this,position);
            //holder.ivProductImage.setImageResource(R.drawable.ic_product);
            //Picasso.with(context).load(horizontalGrocderyList.getProducts().get(position).getProduct().getThumbnail() + "?w=151".replaceAll(" ", "%20")).into(holder.ivProductImage);
          library.displayImage(horizontalGrocderyList.getProducts().get(position).getProduct().getThumbnail() + "?w=151",holder.ivProductImage);

            holder.tvProductName.setText(horizontalGrocderyList.getProducts().get(position).getProduct().getName());
            holder.tvProductPrice.setText(""+horizontalGrocderyList.getProducts().get(position).getProduct().getValucart_price()+" AED");
            holder.rlItemHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //String productName = horizontalGrocderyList.get(position).getName().toString();
                    //context.startActivity(new Intent(context, ProductDetailScreen.class));
                }
            });

    }

    @Override
    public int getItemCount() {
        return horizontalGrocderyList.getProducts().size();
    }

    @Override
    public void onItemSelected(String value, int position) {

        onItemSelection.onItemSelected(value,position);
/*
        if(value.equals("Delete")) {
            horizontalGrocderyList.getProducts().remove(position);
            //horizontalGrocderyList.get(position).setAddToByob(position);
            notifyDataSetChanged();
            //BuildYourOwnBundleFragment.removeItem();
        }
*/
    }

    public class GroceryViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProductImage;
        TextView tvProductName,tvProductPrice;
        RelativeLayout rlItemHeader;
        ApproveBundleCartView approveBundleCartView;

        public GroceryViewHolder(View view) {
            super(view);
            ivProductImage=view.findViewById(R.id.ivProductImage);
            tvProductName=view.findViewById(R.id.tvProductName);
            rlItemHeader=view.findViewById(R.id.rlItemHeader);
            tvProductPrice=view.findViewById(R.id.tvProductPrice);
            approveBundleCartView=view.findViewById(R.id.bundleCVApprovel);
        }

    }

}

