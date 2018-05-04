package edu.umd.cs.funanical;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
//import android.support.design.widget.AppBarLayout;
//import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import edu.umd.cs.funanical.Objects.Category;


/**
 * Ask user to add a new category "Eating out with budget 40, 150, 2000"
 * Created by apple on 4/18/18.
 */

public class CategoriesActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_DETAIL = 2;
    private RecyclerView recyclerView;
    private CategoryAdapter adapter;
    private List<Category> categoryList;
    private static final int REQUEST_CODE_ADD_CATEGORY = 1;
    private boolean addNewCategory = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        // make the toolbar take up the whole space
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);
        toolbar.getContentInsetEnd();
        toolbar.setPadding(0, 0, 0, 0);

        // BACK BUTTON
        toolbar.findViewById(R.id.toolbar_back_categories).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addNewCategory == true){
                    // need to let main page know the new category
                    setResult(RESULT_OK);
                    finish();
                } else{
                    setResult(RESULT_CANCELED);
                    finish();
                }
            }
        });


        findViewById(R.id.add_category).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent categoryIntent = new Intent(getApplicationContext(), AddNewCategoryActivity.class);
                startActivityForResult(categoryIntent,REQUEST_CODE_ADD_CATEGORY);
            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        categoryList = new ArrayList<>();
        adapter = new CategoryAdapter(this, categoryList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareCategories();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == REQUEST_CODE_ADD_CATEGORY) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Category a = new Category("Eating Out", 30.00, 40.00, 40.00, 150.00, 2000.00, 0.00, 0.00, 0.00, 0, 0, R.drawable.restaurant, 0);
                categoryList.add(a);
                adapter.notifyDataSetChanged();
                addNewCategory = true;
            }
        }
    }

    private void prepareCategories() {
        int[] categories = new int[]{
                R.drawable.pet,
                R.drawable.geoceries,
                R.drawable.clothing,
                R.drawable.movies};

        Category a = new Category("Pets", 25.00, 25.00, 25.00, 50.00, 500.00, 12.25, 19.25, 22.25, 22.25, 252.25, categories[0], 0);
        categoryList.add(a);

        a = new Category("Groceries", 35.00, 35.00, 35.00, 150.00, 1500.00, 26.12, 31.12, 36.22, 60.12, 500.12, categories[1], 0);
        categoryList.add(a);

        a = new Category("Clothing", 35.00, 20.00, 30.00, 75.00, 700.00, 35.8, 15.80, 22.80, 30.8, 300.80, categories[2], 0);
        categoryList.add(a);

        a = new Category("Movies", 15.00, 15.00, 15.00, 20.00, 200.00, 13, 20, 11, 15, 80, categories[3], 0);
        categoryList.add(a);

        adapter.notifyDataSetChanged();
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

        private Context mContext;
        private List<Category> categoriesList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView title, budget;
            public ImageView categoryPicture, overflow;

            public MyViewHolder(View view) {
                super(view);
                title = (TextView) view.findViewById(R.id.title);
                //budget = (TextView) view.findViewById(R.id.budget);
                categoryPicture = (ImageView) view.findViewById(R.id.categoryPicture);
                //overflow = (ImageView) view.findViewById(R.id.overflow);
            }
        }


        public CategoryAdapter(Context mContext, List<Category> categoriesList) {
            this.mContext = mContext;
            this.categoriesList = categoriesList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_category, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            Category category = categoriesList.get(position);
            holder.title.setText(category.getName());

            // loading album cover using Glide library
            Glide.with(mContext).load(category.getBuildingId()).into(holder.categoryPicture);
            holder.categoryPicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent detailIntent = new Intent(getApplicationContext(), CategoryDetailActivity.class);
                    startActivityForResult(detailIntent, REQUEST_CODE_DETAIL);
                    finish();
                }
            });
        }

        @Override
        public int getItemCount () {
            return categoriesList.size();
        }
    }

}
