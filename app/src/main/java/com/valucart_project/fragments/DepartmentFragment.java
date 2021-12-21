package com.valucart_project.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.valucart_project.R;
import com.valucart_project.adapter.AdapterDepartmentExpandableList;
import com.valucart_project.interfaces.APIResponseCallback;
import com.valucart_project.interfaces.OnItemSelection;
import com.valucart_project.models.Categories;
import com.valucart_project.screen.ProductListingScreen;
import com.valucart_project.utils.Constants;
import com.valucart_project.utils.Library;
import com.valucart_project.webservices.APIManager;
import org.json.JSONObject;

public class DepartmentFragment extends Fragment implements OnItemSelection {

    RecyclerView expandableListView;
    AdapterDepartmentExpandableList expandableListAdapter;
    View view;
    private Library library;
    Categories categoriesList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.department_fragment, null);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        library = new Library(getContext());
        loadDepartment();
    }

    public void initInterface(Categories response) {
        expandableListView = (RecyclerView) view.findViewById(R.id.expandableListView);
        expandableListAdapter = new AdapterDepartmentExpandableList(getContext(), categoriesList,this);
        expandableListView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        expandableListView.setAdapter(expandableListAdapter);

    }


    private void loadDepartment() {

        library.showLoading();
        APIManager.getInstance().getCategories(new APIResponseCallback<Categories>() {

            @Override
            public void onResponseLoaded(@NonNull Categories response) {
                library.hideLoading();
                categoriesList= response;
                Constants.categories = response;
                initInterface(response);
            }

            @Override
            public void onResponseError(JSONObject jsonObject, Throwable error) {
                library.hideLoading();
                try {
                    library.alertErrorMessage(""+jsonObject.get("message".toString()));
                }catch (Exception e){}

            }

        });

    }

    @Override
    public void onItemSelected(String value, int position) {

        /*if(value.equals("true")){
            for (int counter = 0; counter < categoriesList.getData().size(); counter++) {
                categoriesList.getData().get(counter).setSelectedItem(false);
            }
            expandableListAdapter.notifyDataSetChanged();
        }else {
            for (int counter = 0; counter < categoriesList.getData().size(); counter++) {
                if (counter == position) {
                    categoriesList.getData().get(counter).setSelectedItem(true);
                } else {
                    categoriesList.getData().get(counter).setSelectedItem(false);
                }
            }
            expandableListAdapter.notifyDataSetChanged();
        }
        expandableListView.smoothScrollToPosition(position);*/
        Intent intent = new Intent(getContext(), ProductListingScreen.class);
        intent.putExtra("CallingFrom","Departments");
        intent.putExtra("DepartmentCategoryName",categoriesList.getData().get(position).getName());
        intent.putExtra("SubCategoryId","");
        intent.putExtra("Categories", Constants.categories.getData().get(position).getId());
        startActivity(intent);
        //Constants.categories.getData().get(position).getId()
    }

}

