package edu.umd.cs.funanical;

/**
 * Created by apple on 4/22/18.
 */

import android.content.Context;
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

import edu.umd.cs.funanical.Objects.Store;


/**
 * Created by apple on 4/18/18.
 */

public class StoreActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StoreAdapter adapter;
    private List<Store> storesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        // make the toolbar take up the whole space
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);
        toolbar.getContentInsetEnd();
        toolbar.setPadding(0, 0, 0, 0);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);


        // BACK BUTTON
        toolbar.findViewById(R.id.toolbar_back_store).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        storesList = new ArrayList<>();
        adapter = new StoreAdapter(this, storesList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareCategories();
    }

    /**
     * Adding few categories for testing
     */
    private void prepareCategories() {
        int[] stores = new int[]{
                R.drawable.store1,
                R.drawable.store2,
                R.drawable.store3,
                R.drawable.store4,
                R.drawable.store5,
                R.drawable.store6,
                R.drawable.store7,
                R.drawable.store8,
                R.drawable.store9,
                R.drawable.store10,
                R.drawable.store11,
                R.drawable.store12};

        Store a = new Store("Tree", 100, stores[0]);
        storesList.add(a);

        a = new Store("Street Light", 150, stores[1]);
        storesList.add(a);

        a = new Store("Fountain", 500, stores[2]);
        storesList.add(a);

        a = new Store("Bus Stop", 500, stores[5]);
        storesList.add(a);

        a = new Store("Patio Table", 750, stores[3]);
        storesList.add(a);

        a = new Store("Traffic Light", 750, stores[6]);
        storesList.add(a);

        a = new Store("Air Ballon", 1000, stores[7]);
        storesList.add(a);

        a = new Store("Squirrel", 1250, stores[8]);
        storesList.add(a);

        a = new Store("Rabbit", 1250, stores[9]);
        storesList.add(a);

        a = new Store("City Hall", 1000, stores[10]);
        storesList.add(a);

        a = new Store("Big Ben", 1500, stores[4]);
        storesList.add(a);


        a = new Store("Upgrade building", 1500, stores[11]);
        storesList.add(a);

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

    public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.MyViewHolder> {

        private Context mContext;
        private List<Store> storesList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView title, price;
            public ImageView itemPicture;

            public MyViewHolder(View view) {
                super(view);
                title = (TextView) view.findViewById(R.id.title);
                price = (TextView) view.findViewById(R.id.price);
                itemPicture = (ImageView) view.findViewById(R.id.itemPicture);
            }
        }


        public StoreAdapter(Context mContext, List<Store> storesList) {
            this.mContext = mContext;
            this.storesList = storesList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_store, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            Store category = storesList.get(position);
            holder.title.setText(category.getName());
            holder.price.setText("¢ "+ category.getPrice());

            // loading album cover using Glide library
            Glide.with(mContext).load(category.getPictureID()).into(holder.itemPicture);
        }
        @Override
        public int getItemCount () {
            return storesList.size();
        }
    }

}
