package com.valucart_project.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.valucart_project.R;
import com.valucart_project.interfaces.OnItemSelection;
import com.valucart_project.models.BundleDetail;
import com.valucart_project.utils.Library;

public class AdapterRvBundleDetail extends RecyclerView.Adapter<AdapterRvBundleDetail.GroceryViewHolder>  {

    private BundleDetail horizontalGrocderyList;
    Context context;
    GroceryViewHolder holderGrocery;
    OnItemSelection  onItemSelection;
    Library library;

    public AdapterRvBundleDetail(BundleDetail horizontalGrocderyList, Context context, OnItemSelection  onItemSelection1){
        this.horizontalGrocderyList= horizontalGrocderyList;
        this.context = context;
        onItemSelection = onItemSelection1;
        library = new Library(context);
    }

    @Override
    public GroceryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bundle_detail_item, parent, false);
        GroceryViewHolder gvh = new GroceryViewHolder(groceryProductView);
        return gvh;
    }

    @Override
    public void onBindViewHolder(final GroceryViewHolder holder, final int position) {
        try {
            holderGrocery = holder;
            //holder.ivProductImage.setImageResource(horizontalGrocderyList.getProducts().get(position).getProductImage());
            //Picasso.with(context).load(horizontalGrocderyList.getData().getProducts().get(position).getData().getThumbnail() + "?w=151".replaceAll(" ", "%20")).into(holder.ivProductImage);
            library.displayImage(horizontalGrocderyList.getData().getProducts().get(position).getData().getThumbnail() + "?w=151",holder.ivProductImage);


            holder.tvProductName.setText(horizontalGrocderyList.getData().getProducts().get(position).getData().getName());
            holder.tvProductPrice.setText("" + horizontalGrocderyList.getData().getProducts().get(position).getData().getValucart_price());
            holder.tvQuantity.setText("" + horizontalGrocderyList.getData().getProducts().get(position).getQuantity());

            holder.llItemHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //String productName = horizontalGrocderyList.get(position).getProductName().toString();
                    //context.startActivity(new Intent(context, BundleSummaryScreen.class));
                }
            });

            holder.rlOption.setVisibility(View.GONE);
            holder.llProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                /*if(holder.rlOption.getVisibility() == View.VISIBLE){
                    holder.rlOption.setVisibility(View.GONE);
                    holder.viewLine.setVisibility(View.GONE);
                }else {
                    holder.rlOption.setVisibility(View.VISIBLE);
                    holder.viewLine.setVisibility(View.VISIBLE);
                }*/
                    onItemSelection.onItemSelected("", position);
                }
            });

            //holder.viewAddToCard.showAddMoreItems();
            //radioButtonHandler();
            holder.tvProductOldPrice.setPaintFlags(holder.tvProductOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            if (horizontalGrocderyList.getData().getProducts().get(position).getAlternatives().size() > 0) {
                holder.tvAlternative.setVisibility(View.VISIBLE);
                holder.ivAlternative.setVisibility(View.VISIBLE);
                holder.llAlternative.setVisibility(View.VISIBLE);
            }
            else {
                holder.ivAlternative.setVisibility(View.GONE);
                holder.tvAlternative.setVisibility(View.GONE);
                holder.llAlternative.setVisibility(View.GONE);
            }

            holder.tvWeight.setText(""+horizontalGrocderyList.getData().getProducts().get(position).getData().getPackaging_quantity() + horizontalGrocderyList.getData().getProducts().get(position).getData().getPackaging_quantity_unit().getSymbol());
            //holder.tvWeight.setText(new java.text.DecimalFormat("#").format(horizontalGrocderyList.getData().getProducts().get(position).getData().getPackaging_quantity()) + horizontalGrocderyList.getData().getProducts().get(position).getData().getPackaging_quantity_unit().getSymbol());
        }catch (Exception e){}
    }

    public void radioButtonHandler(){

        RadioGroup rg = new RadioGroup(context);
        rg.setOrientation(RadioGroup.VERTICAL);

        // Initialize the layout parameters for RadioGroup
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        rg.setLayoutParams(lp);

        // Create a Radio Button for RadioGroup
        RadioButton rb_coldfusion = new RadioButton(context);
        rb_coldfusion.setText("ColdFusion");
        rb_coldfusion.setTextColor(Color.BLACK);
        rg.addView(rb_coldfusion);
        rb_coldfusion.setChecked(true);
        // Create another Radio Button for RadioGroup
        RadioButton rb_flex = new RadioButton(context);
        rb_flex.setText("Flex");
        rb_flex.setTextColor(Color.BLACK);
        rg.addView(rb_flex);

        // Create another Radio Button for RadioGroup
        RadioButton rb_flash = new RadioButton(context);
        rb_flash.setText("Flash");
        rb_flash.setTextColor(Color.BLACK);
        rg.addView(rb_flash);
        // Finally, add the RadioGroup to main layout
        holderGrocery.rlOption.addView(rg);


    }

    @Override
    public int getItemCount() {
        return horizontalGrocderyList.getData().getProducts().size();
    }


    public class GroceryViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProductImage,ivAlternative;
        TextView tvProductName,tvProductOldPrice,tvWeight,tvProductPrice,tvQuantity,tvAlternative;
        LinearLayout llItemHeader,llProduct,llAlternative;
        RelativeLayout rlOption;
        View viewLine;

        public GroceryViewHolder(View view) {
            super(view);
            ivProductImage=view.findViewById(R.id.ivProductImage);
            tvProductName=view.findViewById(R.id.tvProductName);
            llItemHeader=view.findViewById(R.id.llItemHeader);
            rlOption = view.findViewById(R.id.rlOption);
            llProduct = view.findViewById(R.id.llProduct);
            viewLine = view.findViewById(R.id.viewLine);
            tvProductOldPrice = view.findViewById(R.id.tvProductOldPrice);
            tvWeight = view.findViewById(R.id.tvWeight);
            tvProductPrice = view.findViewById(R.id.tvProductPrice);
            tvQuantity= view.findViewById(R.id.tvQuantity);
            ivAlternative= view.findViewById(R.id.ivAlternative);
            tvAlternative= view.findViewById(R.id.tvAlternative);
            llAlternative= view.findViewById(R.id.llAlternative);
        }

    }

}

