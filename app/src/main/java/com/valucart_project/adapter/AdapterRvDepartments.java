package com.valucart_project.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.valucart_project.R;
import com.valucart_project.interfaces.OnItemSelection;
import com.valucart_project.models.Grocery;
import com.valucart_project.utils.Library;
import java.util.List;

public class AdapterRvDepartments extends RecyclerView.Adapter<AdapterRvDepartments.GroceryViewHolder>{

    private List<Grocery> horizontalGrocderyList;
    Context context;
    OnItemSelection onItemSelection;
    String name;
    Library library;

    public AdapterRvDepartments(List<Grocery> horizontalGrocderyList, Context context, OnItemSelection onItemSelection1 , String name1){
        this.horizontalGrocderyList= horizontalGrocderyList;
        this.context = context;
        onItemSelection = onItemSelection1;
        name = name1;
        library = new Library(context);
    }

    @Override
    public GroceryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.department_grid_item, parent, false);
        GroceryViewHolder gvh = new GroceryViewHolder(groceryProductView);
        return gvh;
    }

    @Override
    public void onBindViewHolder(final GroceryViewHolder holder, final int position) {
        holder.tvProduct.setText(horizontalGrocderyList.get(position).getProductName());
        //holder.cvItem.setRadius(20);
        //holder.ivDepartment.setBackgroundResource(horizontalGrocderyList.get(position).getProductImage());
        //Picasso.with(context).load(horizontalGrocderyList.get(position).getIcon()+"?w=50".replaceAll(" ", "%20")).into(holder.ivDepartment);

        if(horizontalGrocderyList.get(position).getImage()!=null)
            library.displayImage(horizontalGrocderyList.get(position).getImage() + "?w=250",holder.ivDepartment);

        holder.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onItemSelection.onItemSelected("Department",position);
                
               // String productName = horizontalGrocderyList.get(position).getProductName().toString();
               // Toast.makeText(context, productName + " is selected", Toast.LENGTH_SHORT).show();
               /* if(horizontalGrocderyList.get(position).getSelectedItem()){
                    holder.llItem.setBackgroundResource(R.drawable.bg_white_rounded_gray_border);
                    holder.tvProduct.setTextColor(context.getResources().getColor(R.color.colorBlack));
                    horizontalGrocderyList.get(position).setSelectedItem(false);
                }else {
                    holder.llItem.setBackgroundResource(R.drawable.bg_rounded_purple);
                    holder.tvProduct.setTextColor(context.getResources().getColor(R.color.colorClearWhite));
                    horizontalGrocderyList.get(position).setSelectedItem(true);
                }*/

            }
        });

    }

    @Override
    public int getItemCount() {
        return horizontalGrocderyList.size();
    }

    public class GroceryViewHolder extends RecyclerView.ViewHolder {
        TextView tvProduct;
        LinearLayout llItem;
        ImageView ivDepartment;
        public GroceryViewHolder(View view) {
            super(view);
            tvProduct=view.findViewById(R.id.tvProduct);
            llItem=view.findViewById(R.id.llItem);
            ivDepartment=view.findViewById(R.id.ivDepartment);
        }
    }

}

