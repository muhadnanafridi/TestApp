package com.valucart_project.adapter;

import android.content.Context;
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
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.valucart_project.R;
import com.valucart_project.fragments.BuildYourOwnBundleFragment;
import com.valucart_project.interfaces.APIResponseCallback;
import com.valucart_project.interfaces.OnAddToCartSelection;
import com.valucart_project.interfaces.OnItemSelection;
import com.valucart_project.models.ByobSummary;
import com.valucart_project.screen.DashboardActivity;
import com.valucart_project.utils.Constants;
import com.valucart_project.utils.Library;
import com.valucart_project.views.ApproveBundleCartView;
import com.valucart_project.webservices.APIManager;

import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

public class AdapterRvBundleSummary extends RecyclerView.Adapter<AdapterRvBundleSummary.GroceryViewHolder> implements OnItemSelection , OnAddToCartSelection {

    private ByobSummary horizontalGrocderyList;
    Context context;
    OnItemSelection onItemSelection;
    String bundleId;
    Library library;

    public AdapterRvBundleSummary(ByobSummary horizontalGrocderyList, Context context , OnItemSelection onItemSelection1,String bundleId1){
        this.horizontalGrocderyList= horizontalGrocderyList;
        this.context = context;
        onItemSelection = onItemSelection1;
        bundleId= bundleId1;
        library = new Library(context);
    }

    @Override
    public GroceryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bundle_summary_item, parent, false);
        GroceryViewHolder gvh = new GroceryViewHolder(groceryProductView);
        return gvh;
    }

    @Override
    public void onBindViewHolder(GroceryViewHolder holder, final int position) {
        //holder.approveBundleCartView.cartViewCallBack(this,position);
        //holder.ivProductImage.setImageResource(horizontalGrocderyList.getData().getProducts().get(position).getProductImage());
        try {
            //Picasso.with(context).load(horizontalGrocderyList.getData().getProducts().get(position).getProduct().getThumbnail() + "?w=100".replace(" ", "%20")).placeholder(R.mipmap.ic_add_byob).into(holder.ivProductImage);
            library.displayImage(horizontalGrocderyList.getData().getProducts().get(position).getProduct().getThumbnail() + "?w=100",holder.ivProductImage);
            holder.tvProductName.setText(horizontalGrocderyList.getData().getProducts().get(position).getProduct().getName());
            holder.llItemHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //String productName = horizontalGrocderyList.get(position).getProductName().toString();
                    //context.startActivity(new Intent(context, BundleSummaryScreen.class));
                }
            });
            holder.ivCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //horizontalGrocderyList.remove(position);
                    //notifyDataSetChanged();
                    onItemSelection.onItemSelected("", position);
                    Toast.makeText(context, "Removed successfully", Toast.LENGTH_SHORT).show();
                }
            });


            holder.tvProductOldPrice.setPaintFlags(holder.tvProductOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
/*        if(horizontalGrocderyList.getData().getProducts().get(position).getProduct().getMaximum_selling_price()==horizontalGrocderyList.getData().getProducts().get(position).getProduct().getValucart_price())
            holder.tvProductOldPrice.setText("");
        else
            holder.tvProductOldPrice.setText("AED  "+horizontalGrocderyList.getData().getProducts().get(position).getProduct().getMaximum_selling_price());*/
            if (horizontalGrocderyList.getData().getProducts().get(position).getQuantity() > 1)
                holder.tvProductTotalPrice.setText("AED  " + horizontalGrocderyList.getData().getProducts().get(position).getProduct().getValucart_price() + " x " + horizontalGrocderyList.getData().getProducts().get(position).getQuantity());
            else
                holder.tvProductTotalPrice.setText("AED  " + horizontalGrocderyList.getData().getProducts().get(position).getProduct().getValucart_price());

            holder.tvRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //horizontalGrocderyList.remove(position);
                    //notifyDataSetChanged();
                    //horizontalGrocderyList.remove(position);
                    //notifyDataSetChanged();
                    //onItemSelection.onItemSelected("",position);
                    //Toast.makeText(context, "Removed successfully", Toast.LENGTH_SHORT).show();
                    loadCart("remove", "" + horizontalGrocderyList.getData().getProducts().get(position).getProduct().getId(), bundleId);
                }
            });

            holder.approveBundleCartView.initInterface(this, position);
            holder.approveBundleCartView.addItemListener(horizontalGrocderyList.getData().getProducts().get(position).getQuantity());

            holder.tvProductWeight.setText(""+horizontalGrocderyList.getData().getProducts().get(position).getProduct().getPackaging_quantity() + horizontalGrocderyList.getData().getProducts().get(position).getProduct().getPackaging_quantity_unit().getSymbol());
        }catch (Exception e){}
    }

    @Override
    public int getItemCount() {
        return horizontalGrocderyList.getData().getProducts().size();
    }

    @Override
    public void onItemSelected(String value, int position) {
        //horizontalGrocderyList.remove(position);
        //notifyDataSetChanged();
    }

    @Override
    public void onAddToCart(String value, int position) {
        if(value.equals("AddOne")){
            loadCart("add",""+horizontalGrocderyList.getData().getProducts().get(position).getProduct().getId(),bundleId);
        }else {
            loadCart("subtract",""+horizontalGrocderyList.getData().getProducts().get(position).getProduct().getId(),bundleId);
        }
    }

    public class GroceryViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage,ivCancel;
        TextView tvProductName,tvProductOldPrice,tvRemove,tvProductTotalPrice,tvProductWeight;
        LinearLayout llItemHeader;
        ApproveBundleCartView approveBundleCartView;

        public GroceryViewHolder(View view) {
            super(view);
            ivProductImage=view.findViewById(R.id.ivProductImage);
            tvProductName=view.findViewById(R.id.tvProductName);
            llItemHeader=view.findViewById(R.id.llItemHeader);
            ivCancel=view.findViewById(R.id.ivCancel);
            tvProductOldPrice=view.findViewById(R.id.tvProductOldPrice);
            approveBundleCartView=view.findViewById(R.id.approveBundleCartView);
            tvProductTotalPrice=view.findViewById(R.id.tvProductTotalPrice);
            tvRemove=view.findViewById(R.id.tvRemove);
            tvProductWeight=view.findViewById(R.id.tvWeight);
        }
    }

    private void loadCart(final String action , String product_id , String bundle_Id) {
        //{"status":1,"data":{"cart_id":5,"item_type":"product","item_id":17,"quantity":1,"total_items":1}}
        //{"status":0,"message":"Product on offer can not exceed quantity 1 in the cart."}
        APIManager.getInstance().addByob(action, product_id,bundle_Id, new APIResponseCallback<JsonElement>() {

            @Override
            public void onResponseLoaded(@NonNull JsonElement response) {
                try {
                    if(response.getAsJsonObject().get("status").getAsString().equals("1")) {
                        onItemSelection.onItemSelected("UpDateCart", 0);
                        BuildYourOwnBundleFragment.progressPrice(action,Float.parseFloat(((JsonObject) ((JsonObject) response).get("data")).get("valucart_price").getAsString()));
                        Constants.totalCart = Integer.parseInt(((JsonObject) ((JsonObject) response).get("data")).get("item_count").toString());
                        displayTotalCart(context, "" + ((JsonObject) ((JsonObject) response).get("data")).get("item_count"));
                        DashboardActivity.totalCartValue(context, "" + ((JsonObject) ((JsonObject) response).get("data")).get("item_count"));
                    }else {
                        Toast.makeText(context,response.getAsJsonObject().get("message").getAsString(), Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){}
                //Toast.makeText(context,"",Toast.LENGTH_LONG).show();  //((JsonObject) ((JsonObject) response).get("data")).get("cart_id")
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                //Toast.makeText(context,"",Toast.LENGTH_LONG).show();
                try {
                    library.alertErrorMessage(""+jsonObject.get("message".toString()));

                    //Toast.makeText(context, ""+jsonObject.get("message".toString()), Toast.LENGTH_LONG).show();
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

}

