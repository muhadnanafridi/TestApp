package com.valucart_project.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.valucart_project.R;
import com.valucart_project.interfaces.OnItemSelection;
import com.valucart_project.models.Search;

public class AdapterRvPopularSearch extends RecyclerView.Adapter<AdapterRvPopularSearch.GroceryViewHolder>{

    Context context;
    OnItemSelection onItemSelection;
    String nameItem;
    Search search;

    public AdapterRvPopularSearch(Search search1, Context context, String nameItem1 , OnItemSelection onItemSelection1){
        this.search= search1;
        this.context = context;
        onItemSelection = onItemSelection1;
        nameItem = nameItem1;
    }

    @Override
    public GroceryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_popular_list_item, parent, false);
        GroceryViewHolder gvh = new GroceryViewHolder(groceryProductView);
        return gvh;
    }

    @Override
    public void onBindViewHolder(final GroceryViewHolder holder, final int position) {

        if(search.getData().get(position).getIn().contains(" in ") && search.getData().get(position).getResource_type().equals("products")){
            if(search.getData().get(position).getIn().split(" in ").length==2)
                holder.tvProduct.setText(Html.fromHtml(search.getData().get(position).getIn().split("\\sin\\s+")[0]+" in <b>"+search.getData().get(position).getIn().split("\\sin\\s")[1]+"</b>"));
            else
               holder.tvProduct.setText(Html.fromHtml("<b>" + search.getData().get(position).getIn() + "</b>"));
        }else {
            holder.tvProduct.setText(search.getData().get(position).getIn());
        }

        holder.rlPopularList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String productName = horizontalGrocderyList.get(position).getProductName().toString();
                //context.startActivity(new Intent(context, BundleSummaryScreen.class));
                //if( search.getData().size()==1){
                //    Toast.makeText(context,"No Record Available",Toast.LENGTH_LONG).show();
                //}else
                    onItemSelection.onItemSelected(search.getData().get(position).getResource_type(),position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return search.getData().size();
    }

    public class GroceryViewHolder extends RecyclerView.ViewHolder {
        TextView tvProduct;
        RelativeLayout rlPopularList;
        public GroceryViewHolder(View view) {
            super(view);
            tvProduct=view.findViewById(R.id.tvProduct);
            rlPopularList=view.findViewById(R.id.rlPopularList);
        }
    }

}

