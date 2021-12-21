package com.valucart_project.adapter;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.valucart_project.R;
import com.valucart_project.interfaces.OnItemSelection;
import com.valucart_project.models.Categories;
import com.valucart_project.models.SubCategory;
import com.valucart_project.screen.ProductListingScreen;
import com.valucart_project.utils.Library;

public class AdapterDepartmentExpandableList  extends RecyclerView.Adapter<AdapterDepartmentExpandableList.GroceryViewHolder> implements OnItemSelection{

    private Categories horizontalGrocderyList;
    Context context;
    private Library library;
    AdapterRvHorizantalSubCategoriesDepartment horizontalListAdapter;
    SubCategory categoriesList = new SubCategory();
    OnItemSelection onItemSelection;

    public AdapterDepartmentExpandableList(Context context , Categories horizontalGrocderyList1 , OnItemSelection onItemSelection1){
        this.horizontalGrocderyList= horizontalGrocderyList1;
        this.context = context;
        library = new Library(context);
        onItemSelection = onItemSelection1;
    }

    @Override
    public AdapterDepartmentExpandableList.GroceryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.department_list_item, parent, false);
        AdapterDepartmentExpandableList.GroceryViewHolder gvh = new AdapterDepartmentExpandableList.GroceryViewHolder(groceryProductView);
        return gvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final GroceryViewHolder holder, final int position) {

        //holderItem = holder;
        if(horizontalGrocderyList.getData().get(position).getSelectedItem()){
            holder.ivDropDown.setImageResource(0);
            holder.ivDropDown.setImageResource(R.mipmap.ic_drop_up);
            categoriesList = new SubCategory();
            holder.pbProgress.setVisibility(View.VISIBLE);
            //loadDepartmentSubCategories(holder, Constants.categories.getData().get(position).getId());
        }else {
            holder.rvDepartmentCategories.setVisibility(View.GONE);
            holder.pbProgress.setVisibility(View.GONE);
            holder.ivDropDown.setImageResource(R.mipmap.ic_drop_down);
        }

        holder.tvTitle.setText(horizontalGrocderyList.getData().get(position).getName().substring(0, 1).toUpperCase() + horizontalGrocderyList.getData().get(position).getName().substring(1));
        holder.rvDepartmentCategories.setVisibility(View.GONE);
        //holder.pbProgress.setVisibility(View.GONE);

        library.displayImage(horizontalGrocderyList.getData().get(position).getIcon()+"?w=100",holder.ivDepartment);
        //Picasso.with(context).load(horizontalGrocderyList.getData().get(position).getIcon()+"?w=100".replaceAll(" ", "%20")).into(holder.ivDepartment);

        holder.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //holder.pbProgress.setVisibility(View.VISIBLE);
/*                if(holder.rvDepartmentCategories.getVisibility() == View.VISIBLE){
                    onItemSelection.onItemSelected("true",position);
                }else {*/
                    onItemSelection.onItemSelected("false",position);
                //}

            }
        });

    }



    @Override
    public int getItemCount() {
        return horizontalGrocderyList.getData().size();
    }

    @Override
    public void onItemSelected(String id, int position) {
        Intent intent = new Intent(context, ProductListingScreen.class);
        intent.putExtra("CallingFrom","Departments");
        intent.putExtra("DepartmentCategoryName",categoriesList.getData().get(position).getName());
        intent.putExtra("SubCategoryId",id);
        context.startActivity(intent);
    }

    public class GroceryViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        LinearLayout llItem;
        RecyclerView rvDepartmentCategories;
        ProgressBar pbProgress;
        ImageView ivDropDown,ivDepartment;
        public GroceryViewHolder(View view) {
            super(view);
            tvTitle = view.findViewById(R.id.tvTitle);
            llItem = view.findViewById(R.id.llItem);
            rvDepartmentCategories = view.findViewById(R.id.rvDepartmentCategories);
            pbProgress = view.findViewById(R.id.pbProgress);
            ivDropDown = view.findViewById(R.id.ivDropDown);
            ivDepartment = view.findViewById(R.id.ivDepartment);
        }

    }

/*
    private void loadDepartmentSubCategories(final GroceryViewHolder holder , String id) {

        //library.showLoading();
        APIManager.getInstance().getSubCategories(id ,new APIResponseCallback<SubCategory>() {

            @Override
            public void onResponseLoaded(@NonNull SubCategory response) {
                //library.hideLoading();
                categoriesList = response;
                //horizontalListAdapter.notifyDataSetChanged();
                callAdopter(holder);
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                //library.hideLoading();
                holder.pbProgress.setVisibility(View.GONE);
                library.alertErrorMessage("No category Available");
                //Toast.makeText(context, "No category Available", Toast.LENGTH_LONG).show();
            }

        });

    }
*/

    public void callAdopter(GroceryViewHolder holder ){
        holder.pbProgress.setVisibility(View.GONE);
        holder.rvDepartmentCategories.setVisibility(View.VISIBLE);
        if(categoriesList.getData().size()==0) {
            Toast.makeText(context, "No category Available", Toast.LENGTH_LONG).show();
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        holder.rvDepartmentCategories.setLayoutManager(layoutManager);

        horizontalListAdapter = new AdapterRvHorizantalSubCategoriesDepartment(categoriesList.getData() ,context, this);
        holder.rvDepartmentCategories.setAdapter(horizontalListAdapter);
    }

}

