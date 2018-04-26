package edu.umd.cs.funanical;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import edu.umd.cs.funanical.Objects.Building;

/**
 * Created by apple on 4/18/18.
 *
 */

public class AddNewCategoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ChooseBuildingAdapter adapter;
    private List<Building> buildingLst;
    private EditText name, weeklyBudget, monthlyBudget, yearlyBudget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_category);

        // make the toolbar take up the whole space
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);
        toolbar.getContentInsetEnd();
        toolbar.setPadding(0, 0, 0, 0);

        // BACK BUTTON
        toolbar.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        // BACK BUTTON
        toolbar.findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        name = (EditText) findViewById(R.id.category_name);
        weeklyBudget = (EditText) findViewById(R.id.weekly_budget);
        monthlyBudget = (EditText) findViewById(R.id.monthly_budget);
        yearlyBudget = (EditText) findViewById(R.id.yearly_budget);

        buildingLst = new ArrayList<>();
        adapter = new ChooseBuildingAdapter(this, buildingLst);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareCategories();
    }

    /**
     * Adding few albums for testing
     */
    private void prepareCategories() {
        int[] stores = new int[]{
                R.drawable.restaurant,
                R.drawable.pub,
                R.drawable.coffee,
                R.drawable.pizzeria,
                R.drawable.hotel,
                R.drawable.pharmacy,
                R.drawable.movies,
                R.drawable.pet};

        Building a = new Building(stores[0]);
        buildingLst.add(a);

        a = new Building(stores[1]);
        buildingLst.add(a);

        a = new Building(stores[2]);
        buildingLst.add(a);

        a = new Building(stores[3]);
        buildingLst.add(a);

        a = new Building(stores[4]);
        buildingLst.add(a);

        a = new Building(stores[5]);
        buildingLst.add(a);

        a = new Building(stores[6]);
        buildingLst.add(a);

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

    public class ChooseBuildingAdapter extends RecyclerView.Adapter<ChooseBuildingAdapter.MyViewHolder> {

        private Context mContext;
        private List<Building> buildingList;
        private int row_index = -1;

        public class MyViewHolder extends RecyclerView.ViewHolder   {
            public ImageView itemPicture;
            LinearLayout chooseBuilding;

            public MyViewHolder(View view) {
                super(view);
                itemPicture = (ImageView) view.findViewById(R.id.itemPicture);
                chooseBuilding = (LinearLayout) view.findViewById(R.id.choose_building);
            }
        }


        public ChooseBuildingAdapter(Context mContext, List<Building> buildingList) {
            this.mContext = mContext;
            this.buildingList = buildingList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_choose_building, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            Building building = buildingList.get(position);

            // loading album cover using Glide library
            Glide.with(mContext).load(building.getPictureId()).into(holder.itemPicture);

            holder.chooseBuilding.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    row_index = position;
                    notifyDataSetChanged();
                }
            });
            if(row_index==position){
                holder.chooseBuilding.setBackground(ContextCompat.getDrawable(mContext, R.drawable.round_border_choose_a_building));
            }
            else
            {
                holder.chooseBuilding.setBackgroundColor(Color.TRANSPARENT);
            }


        }
        @Override
        public int getItemCount () {
            return buildingList.size();
        }
    }

}
