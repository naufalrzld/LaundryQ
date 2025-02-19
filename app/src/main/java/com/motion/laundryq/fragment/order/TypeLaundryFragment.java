package com.motion.laundryq.fragment.order;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.motion.laundryq.R;
import com.motion.laundryq.adapter.CategoryAdapter;
import com.motion.laundryq.model.CategoryModel;
import com.motion.laundryq.model.OrderLaundryModel;
import com.motion.laundryq.utils.CurrencyConverter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.motion.laundryq.utils.AppConstant.KEY_DATA_INTENT_CATEGORIES;
import static com.motion.laundryq.utils.AppConstant.KEY_DATA_INTENT_LAUNDRY_NAME;

/**
 * A simple {@link Fragment} subclass.
 */
public class TypeLaundryFragment extends Fragment implements CategoryAdapter.ListItemClickListener {
    @BindView(R.id.tv_note_order)
    TextView tvNoteOrder;
    @BindView(R.id.rv_category)
    RecyclerView rvCategory;
    @BindView(R.id.tv_total)
    TextView tvTotal;

    private List<CategoryModel> categories = new ArrayList<>();

    private int total;

    public TypeLaundryFragment() {
        // Required empty public constructor
    }

    public List<CategoryModel> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryModel> categories) {
        this.categories = categories;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_type_laundry, container, false);
        ButterKnife.bind(this, v);

        assert getArguments() != null;
        List<CategoryModel> categories = getArguments().getParcelableArrayList(KEY_DATA_INTENT_CATEGORIES);
        String laundryName = getArguments().getString(KEY_DATA_INTENT_LAUNDRY_NAME);
        String text = getString(R.string.txt_note_order, laundryName);
        CharSequence styledText = Html.fromHtml(text);

        tvNoteOrder.setText(styledText);

        CategoryAdapter adapter = new CategoryAdapter(getContext());
        rvCategory.setHasFixedSize(true);
        rvCategory.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCategory.setAdapter(adapter);
        adapter.setCategoryList(categories, this);

        tvTotal.setText(CurrencyConverter.toIDR(0));
        return v;
    }

    @Override
    public void onOnItemSelected(CategoryModel categoryModel, int total) {
        categoryModel.setStatus(0);
        categories.add(categoryModel);
        tvTotal.setText(CurrencyConverter.toIDR(total));

        setTotal(total);
        setCategories(categories);
    }

    @Override
    public void onItemUpdate(CategoryModel categoryModel, int total) {
        categories.remove(categoryModel);
        tvTotal.setText(CurrencyConverter.toIDR(total));

        if (categoryModel.getQuantity() != 0) {
            categories.add(categoryModel);
        }

        setTotal(total);
        setCategories(categories);
    }
}
