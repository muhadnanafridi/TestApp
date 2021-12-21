package com.valucart_project.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Paint;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.valucart_project.R;
import com.valucart_project.interfaces.APIResponseCallback;
import com.valucart_project.interfaces.OnAddToCartSelection;
import com.valucart_project.interfaces.OnItemSelection;
import com.valucart_project.models.Products;
import com.valucart_project.screen.DashboardActivity;
import com.valucart_project.utils.Constants;
import com.valucart_project.utils.Library;
import com.valucart_project.views.ApproveBundleCartView;
import com.valucart_project.webservices.APIManager;
import org.json.JSONObject;
import static android.content.Context.MODE_PRIVATE;

public class AdapterRvCheckout extends RecyclerView.Adapter<AdapterRvCheckout.GroceryViewHolder> implements OnAddToCartSelection {

    private Products horizontalGrocderyList;
    Context context;
    OnItemSelection onItemSelection;
    Library library;

    public AdapterRvCheckout(Products  horizontalGrocderyList1, Context context, OnItemSelection onItemSelection1) {
        this.horizontalGrocderyList = horizontalGrocderyList1;
        this.context = context;
        library = new Library(context);
        onItemSelection = onItemSelection1;
    }

    @Override
    public GroceryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_checkout_item, parent, false);
        GroceryViewHolder gvh = new GroceryViewHolder(groceryProductView);
        return gvh;
    }

    @Override
    public void onBindViewHolder(final GroceryViewHolder holder, final int position) {

        /*holder.ivProductImage.setImageResource(horizontalGrocderyList.get(position).getProductImage());*/
        if(horizontalGrocderyList.getData().get(position).getProductType().equals("customer_bundle"))
            holder.ivProductImage.setImageResource(R.mipmap.ic_add_byob);
        else
            library.displayImage(horizontalGrocderyList.getData().get(position).getThumbnail() + "?w=151",holder.ivProductImage);
            //Picasso.with(context).load(horizontalGrocderyList.getData().get(position).getThumbnail()+"?w=151".replaceAll(" ", "%20")).into(holder.ivProductImage);

        if(horizontalGrocderyList.getData().get(position).getName()==null)
            holder.tvProductName.setText("Bundle 1");
        else
            holder.tvProductName.setText(horizontalGrocderyList.getData().get(position).getName());

        holder.llItemHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String productName = horizontalGrocderyList.get(position).getProductName().toString();
                //context.startActivity(new Intent(context, BundleSummaryScreen.class));
            }
        });

        holder.tvRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //horizontalGrocderyList.remove(position);
                //notifyDataSetChanged();
                //onItemSelection.onItemSelected("", position);
                alertRemoveItem(position , "delete",horizontalGrocderyList.getData().get(position).getId(),horizontalGrocderyList.getData().get(position).getProductType());

            }
        });
        holder.viewAddToCard.initInterface(this, position);
        holder.viewAddToCard.addItemListener(horizontalGrocderyList.getData().get(position).getQuantity());

        holder.tvProductOldPrice.setPaintFlags(holder.tvProductOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        if(horizontalGrocderyList.getData().get(position).getQuantity()>1)
            holder.tvProductTotalPrice.setText("AED  "+horizontalGrocderyList.getData().get(position).getValucart_price()+" x "+horizontalGrocderyList.getData().get(position).getQuantity());
        else
            holder.tvProductTotalPrice.setText("AED  "+horizontalGrocderyList.getData().get(position).getValucart_price());


        try {
            if (horizontalGrocderyList.getData().get(position).getMaximum_selling_price().equals(horizontalGrocderyList.getData().get(position).getValucart_price()))
                holder.tvProductOldPrice.setText("");
            else {
                if(horizontalGrocderyList.getData().get(position).getQuantity()>1)
                    holder.tvProductOldPrice.setText("AED  "+horizontalGrocderyList.getData().get(position).getMaximum_selling_price()+" x "+horizontalGrocderyList.getData().get(position).getQuantity());
                else
                    holder.tvProductOldPrice.setText("AED  "+horizontalGrocderyList.getData().get(position).getMaximum_selling_price());
            }

            if(horizontalGrocderyList.getData().get(position).getIs_bulk() ==null){
                holder.tvProductWeight.setText(horizontalGrocderyList.getData().get(position).getPackaging_quantity() + horizontalGrocderyList.getData().get(position).getPackaging_quantity_weight());
            }else {
                if (horizontalGrocderyList.getData().get(position).getIs_bulk() && horizontalGrocderyList.getData().get(position).getBulk_quantity() > 0)
                    holder.tvProductWeight.setText(""+horizontalGrocderyList.getData().get(position).getPackaging_quantity() + horizontalGrocderyList.getData().get(position).getPackaging_quantity_weight() + " x " + horizontalGrocderyList.getData().get(position).getBulk_quantity());
                else
                    holder.tvProductWeight.setText(""+horizontalGrocderyList.getData().get(position).getPackaging_quantity() + horizontalGrocderyList.getData().get(position).getPackaging_quantity_weight());
            }
        }catch (Exception e){
            holder.tvProductOldPrice.setText("");
        }

    }


    @Override
    public int getItemCount() {
        return horizontalGrocderyList.getData().size();
    }

    @Override
    public void onAddToCart(String value, int position) {
        if(value.equals("AddOne")){
            loadCart("add",horizontalGrocderyList.getData().get(position).getId(),horizontalGrocderyList.getData().get(position).getProductType());
        }else {
            loadCart("subtract",horizontalGrocderyList.getData().get(position).getId(),horizontalGrocderyList.getData().get(position).getProductType());
        }
    }

    public class GroceryViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProductImage;
        TextView tvProductName, tvRemove, tvProductOldPrice,tvProductTotalPrice,tvProductWeight;
        LinearLayout llItemHeader;
        ApproveBundleCartView viewAddToCard;

        public GroceryViewHolder(View view) {
            super(view);
            ivProductImage = view.findViewById(R.id.ivProductImage);
            tvProductName = view.findViewById(R.id.tvProductName);
            llItemHeader = view.findViewById(R.id.llItemHeader);
            tvRemove = view.findViewById(R.id.tvRemove);
            tvProductOldPrice = view.findViewById(R.id.tvProductOldPrice);
            tvProductTotalPrice = view.findViewById(R.id.tvProductTotalPrice);
            viewAddToCard = view.findViewById(R.id.viewAddToCard);
            tvProductWeight = view.findViewById(R.id.tvProductWeight);
        }

    }

    private void loadCart(final String action , String item_id , String item_type) {
        //{"status":1,"data":{"cart_id":5,"item_type":"product","item_id":17,"quantity":1,"total_items":1}}
        library.showLoading();
        APIManager.getInstance().addCart(action, item_id,item_type, new APIResponseCallback<JsonElement>() {

            @Override
            public void onResponseLoaded(@NonNull JsonElement response) {
             try {
                 library.hideLoading();
                 if(response.getAsJsonObject().get("status").toString().equals("1")) {
                     //if (Constants.cart_id.equals(""))
                     if (!((JsonObject) ((JsonObject) response).get("data")).get("id").toString().equals("null"))
                        Constants.cart_id = ((JsonObject) ((JsonObject) response).get("data")).get("id").getAsString();

                     Constants.totalCart = Integer.parseInt(((JsonObject) ((JsonObject) response).get("data")).get("item_count").toString());

                     if(action.equals("remove"))
                         onItemSelection.onItemSelected("refreshCart", 0);
                     else
                         onItemSelection.onItemSelected("UpDateCart", 0);
                     displayTotalCart(context, "" + ((JsonObject) ((JsonObject) response).get("data")).get("item_count"));
                     DashboardActivity.totalCartValue(context, "" + ((JsonObject) ((JsonObject) response).get("data")).get("item_count"));
                 }else {
                     library.alertErrorMessage(""+response.getAsJsonObject().get("message"));
                     //Toast.makeText(context,response.getAsJsonObject().get("message").toString(),Toast.LENGTH_LONG).show();
                 }
             }catch (Exception e){}
                //Toast.makeText(context,"",Toast.LENGTH_LONG).show();  //((JsonObject) ((JsonObject) response).get("data")).get("cart_id")
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                //{"status":0,"message":"Product on offer can not exceed quantity 1 in the cart."}
                library.hideLoading();
                try {
                    library.alertErrorMessage(""+jsonObject.get("message".toString()));

//                    Toast.makeText(context, ""+jsonObject.get("message".toString()), Toast.LENGTH_LONG).show();
                }catch (Exception e){}

            }

        });
    }

    public static void displayTotalCart(Context context , String value){
        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.valucartPreferences, MODE_PRIVATE).edit();
        editor.putString("TotalCart", ""+Constants.totalCart);
        editor.putString("cart_id", ""+Constants.cart_id);
        editor.apply();
    }



    public void alertRemoveItem(final int position , String action , String item_id , String item_type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Product Alert")
                .setMessage("Are you sure, you want to Delete this Item?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        try {
                            loadCart("remove", horizontalGrocderyList.getData().get(position).getId(), horizontalGrocderyList.getData().get(position).getProductType());
                        }catch (Exception e){}
                        //loadCartToRemoveItem("delete",horizontalGrocderyList.getData().get(position).getId(),horizontalGrocderyList.getData().get(position).getProductType());
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        //Creating dialog box
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}

