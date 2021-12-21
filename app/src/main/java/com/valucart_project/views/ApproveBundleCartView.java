package com.valucart_project.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.valucart_project.R;
import com.valucart_project.interfaces.OnAddToCartSelection;
import com.valucart_project.interfaces.OnItemSelection;
import com.valucart_project.utils.Library;

public class ApproveBundleCartView extends RelativeLayout implements View.OnClickListener {

    private LayoutInflater inflater;
    RelativeLayout rlAddItems;
    ImageView ivRemoveItem, ivAddItem;
    TextView tvTotalItem;
    OnItemSelection onItemSelection;
    int position;
    OnAddToCartSelection onAddToCartSelection;
    Library library;

    public ApproveBundleCartView(Context context) {
        super(context);
        init(null);
    }

    public void cartViewCallBack(OnItemSelection onItemSelection1, int position1) {
        onItemSelection = onItemSelection1;
        position = position1;
    }

    public ApproveBundleCartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ApproveBundleCartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attributeSet) {
        library=new Library(getContext());
        inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.add_bundle_approval_cart_view, null);

        rlAddItems = view.findViewById(R.id.rlAddItems);

        ivRemoveItem = view.findViewById(R.id.ivRemoveItem);
        ivRemoveItem.setOnClickListener(this);

        tvTotalItem = view.findViewById(R.id.tvTotalItem);


        ivAddItem = view.findViewById(R.id.ivAddItem);
        ivAddItem.setOnClickListener(this);

        addView(view);
    }

    @Override
    public void onClick(View v) {

        if (ivRemoveItem == v) {
            if (Integer.parseInt(tvTotalItem.getText().toString()) == 1) {
                //rlAddItems.setVisibility(GONE);
                if (onAddToCartSelection != null) {
                    onAddToCartSelection.onAddToCart("Delete", position);
                }
            } else {
                tvTotalItem.setText("" + (Integer.parseInt(tvTotalItem.getText().toString()) - 1));
                if (onAddToCartSelection != null)
                    onAddToCartSelection.onAddToCart("RemoveOne", position);
            }
            if (onItemSelection != null)
                onItemSelection.onItemSelected("RemoveOne", position);
        }

        if (ivAddItem == v) {
            if (Integer.parseInt(tvTotalItem.getText().toString()) < 5) {
                //tvTotalItem.setText("" + (Integer.parseInt(tvTotalItem.getText().toString()) + 1));
                if (onAddToCartSelection != null)
                    onAddToCartSelection.onAddToCart("AddOne", position);
                if (onItemSelection != null)
                    onItemSelection.onItemSelected("AddOne", position);

            } else {
                library.alertErrorMessage("You can only add  same 5 item");
                //Toast.makeText(getContext(), "You can only add  same 5 item", Toast.LENGTH_LONG).show();
            }

        }

    }

    public void addItemListener(int totalItem) {
        tvTotalItem.setText("" + totalItem);
    }

    public void showAddMoreItems() {
        rlAddItems.setVisibility(VISIBLE);
    }

    public void initInterface(OnAddToCartSelection onAddToCartSelection1, int position1) {
        onAddToCartSelection = onAddToCartSelection1;
        position = position1;
    }

}

