package com.motion.laundryq;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TextView;

import com.motion.laundryq.adapter.CategoryAdapter;
import com.motion.laundryq.model.CategoryModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.motion.laundryq.utils.AppConstant.KEY_DATA_INTENT_CATEGORIES;
import static com.motion.laundryq.utils.AppConstant.KEY_DATA_INTENT_LAUNDRY_NAME;

public class OrderActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_note_order)
    TextView tvNoteOrder;
    @BindView(R.id.rv_category)
    RecyclerView rvCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.activity_order_title);

        Intent dataIntent = getIntent();
        List<CategoryModel> categories = dataIntent.getParcelableArrayListExtra(KEY_DATA_INTENT_CATEGORIES);

        String laundryName = dataIntent.getStringExtra(KEY_DATA_INTENT_LAUNDRY_NAME);
        String text = getString(R.string.txt_note_order, laundryName);
        CharSequence styledText = Html.fromHtml(text);

        tvNoteOrder.setText(styledText);

        CategoryAdapter adapter = new CategoryAdapter(this);
        rvCategory.setHasFixedSize(true);
        rvCategory.setLayoutManager(new LinearLayoutManager(this));
        rvCategory.setAdapter(adapter);
        adapter.setCategoryList(categories);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
