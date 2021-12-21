package com.valucart_project.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.valucart_project.R;
import com.valucart_project.models.Addresses;
import com.valucart_project.screen.MyAddressDetailScreen;

public class AdapterRvMyAddress extends RecyclerView.Adapter<AdapterRvMyAddress.GroceryViewHolder>{
    private Addresses horizontalGrocderyList;
    Context context;

    public AdapterRvMyAddress(Addresses horizontalGrocderyList, Context context){
        this.horizontalGrocderyList= horizontalGrocderyList;
        this.context = context;
    }

    @Override
    public GroceryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_address_item, parent, false);
        GroceryViewHolder gvh = new GroceryViewHolder(groceryProductView);
        return gvh;
    }

    @Override
    public void onBindViewHolder(GroceryViewHolder holder, final int position) {
        holder.tvName.setText(horizontalGrocderyList.getData().get(position).getName());
        holder.tvDetail.setText("" + horizontalGrocderyList.getData().get(position).getBuilding()+ horizontalGrocderyList.getData().get(position).getVilla_number()  + " , " + horizontalGrocderyList.getData().get(position).getApartment_number()
                + "" + horizontalGrocderyList.getData().get(position).getStreet()+ "  " + horizontalGrocderyList.getData().get(position).getLandmark()
                );
        holder.llItemHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String addressId = ""+horizontalGrocderyList.getData().get(position).getId();
                Intent intent = new Intent(context, MyAddressDetailScreen.class);
                intent.putExtra("id",""+horizontalGrocderyList.getData().get(position).getId());
                intent.putExtra("Apartment",horizontalGrocderyList.getData().get(position).getApartment_number());
                intent.putExtra("State",horizontalGrocderyList.getData().get(position).getState().getName());
                intent.putExtra("Area",horizontalGrocderyList.getData().get(position).getArea().getName());
                intent.putExtra("AreaId",""+horizontalGrocderyList.getData().get(position).getArea().getId());
                intent.putExtra("Building",horizontalGrocderyList.getData().get(position).getBuilding());
                intent.putExtra("Floor",horizontalGrocderyList.getData().get(position).getFloor());
                intent.putExtra("Landmark",horizontalGrocderyList.getData().get(position).getLandmark());
                intent.putExtra("Street",horizontalGrocderyList.getData().get(position).getStreet());
                intent.putExtra("Name",horizontalGrocderyList.getData().get(position).getName());
                intent.putExtra("villa",horizontalGrocderyList.getData().get(position).getVilla_number());
                intent.putExtra("phone",horizontalGrocderyList.getData().get(position).getPhone_number());
                intent.putExtra("apartmentvilla",horizontalGrocderyList.getData().get(position).getLocation_type());
                context.startActivity(intent);

            }
        });

        holder.llDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                horizontalGrocderyList.getData().remove(position);
                notifyDataSetChanged();
            }
        });
/*
        holder.llEditAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, DeliveryAddressScreen.class));
            }
        });
*/

        holder.tvArea.setText(""+horizontalGrocderyList.getData().get(position).getArea().getName() +" , " + horizontalGrocderyList.getData().get(position).getState().getName()+ " \n" +horizontalGrocderyList.getData().get(position).getPhone_number());
    }

    @Override
    public int getItemCount() {
        return horizontalGrocderyList.getData().size();
    }

    public class GroceryViewHolder extends RecyclerView.ViewHolder {
        TextView tvName,tvDetail,tvArea;
        LinearLayout llItemHeader,llDelete,llEditAddress;
        ;
        public GroceryViewHolder(View view) {
            super(view);
            tvName=view.findViewById(R.id.tvName);
            tvDetail=view.findViewById(R.id.tvDetail);

            llItemHeader=view.findViewById(R.id.llItemHeader);
            llDelete=view.findViewById(R.id.llDelete);
            llEditAddress=view.findViewById(R.id.llEditAddress);

            tvArea=view.findViewById(R.id.tvArea);
        }
    }
}

