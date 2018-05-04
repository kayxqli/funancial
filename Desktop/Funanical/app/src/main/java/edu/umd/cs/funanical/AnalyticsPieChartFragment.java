package edu.umd.cs.funanical;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.rd.PageIndicatorView;

import java.util.ArrayList;
import java.util.List;
import edu.umd.cs.funanical.Objects.Category;

/**
 * Created by apple on 4/20/18.
 */

public class AnalyticsPieChartFragment extends Fragment {


    // have one list and only change the first one
    private static final int REQUEST_CODE_DETAIL = 1;
    private TextView title;
    private RecyclerView recyclerView;
    private PageIndicatorView pageIndicatorView;
    private List<Category> categoryList;
    private AnalyticsListAdapter listAdapter;
    private int timePosition = 0;
    private int curPosition = 0; // -1 means last week, 0 current week, 1 next week
    private ViewPager pager;
    private AnalyticsGraphicAdapter graphicAdapter;
    private int[] indicators = new int[]{
            R.drawable.dot1_final,
            R.drawable.dot2_final,
            R.drawable.dot3_final,
            R.drawable.dot4_final};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pie_chart, container, false);
        initViews(view);

        return view;
    }

    @SuppressWarnings("ConstantConditions")
        private void initViews(View view) {
        title = (TextView) view.findViewById(R.id.graph_title);

        view.findViewById(R.id.last_graph).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timePosition == 0){
                    // weekly view, go back to last week
                    // also need to update adapter for graphic
                    if(curPosition == 0){ // current week
                        title.setText("Apirl 8 - 14, 2018");
                        graphicAdapter.setData(createLastGraphList());
                        graphicAdapter.notifyDataSetChanged();
                    }
                    if(curPosition == 1){
                        title.setText("Apirl 15 - 21, 2018");
                        graphicAdapter.setData(createCurrentGraphList());
                        graphicAdapter.notifyDataSetChanged();
                    }
                    // avoid false negative
                    if(curPosition == 1 || curPosition == 0){
                        curPosition = curPosition - 1;
                    }
                }
            }
        });

        view.findViewById(R.id.next_graph).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timePosition == 0){
                    // weekly view, go back to next week
                    if(curPosition == 0){ // current week
                        title.setText("Apirl 22 - 28, 2018");
                        graphicAdapter.setData(createNextGraphList());
                        graphicAdapter.notifyDataSetChanged();
                    }
                    if(curPosition == -1) {
                        title.setText("Apirl 15 - 21, 2018");
                        graphicAdapter.setData(createCurrentGraphList());
                        graphicAdapter.notifyDataSetChanged();
                    }
                    // avoid false negative
                    if(curPosition == -1 || curPosition == 0){
                        curPosition = curPosition + 1;
                    }
                }
            }
        });

        graphicAdapter = new AnalyticsGraphicAdapter();
        graphicAdapter.setData(createCurrentGraphList());

        categoryList = new ArrayList<>();

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        listAdapter = new AnalyticsListAdapter(categoryList);

        pager = (ViewPager) view.findViewById(R.id.viewPager);
        pager.setAdapter(graphicAdapter);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private static final float thresholdOffset = 0.5f;
            private static final int thresholdOffsetPixels = 1;
            private boolean scrollStarted, checkDirection;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (checkDirection) {
                    if (thresholdOffset > positionOffset && positionOffsetPixels > thresholdOffsetPixels) {
                        timePosition = timePosition + 1;
                    } else {
                        timePosition = timePosition - 1;

                    }
                    checkDirection = false;
                    listAdapter.notifyDataSetChanged();
                    if(timePosition == 0){
                        if(curPosition == 0){ // current week
                            title.setText("Apirl 15 - 21, 2018");
                        } else if(curPosition == -1){
                            title.setText("Apirl 8 - 14, 2018");
                        } else{
                            title.setText("Apirl 22 - 28, 2018");
                        }
                    } else if(timePosition == 1){
                        title.setText("April, 2018");
                    } else{
                        title.setText("2018");
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {}

            @Override
            public void onPageScrollStateChanged(int state) {
                if (!scrollStarted && state == ViewPager.SCROLL_STATE_DRAGGING) {
                    scrollStarted = true;
                    checkDirection = true;
                } else {
                    scrollStarted = false;
                }
            }
        });


        pageIndicatorView = (PageIndicatorView) view.findViewById(R.id.viewIndicator);
        pageIndicatorView.setViewPager(pager);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(listAdapter);

        prepareCategories();
    }

    @NonNull
    private List<View> createCurrentGraphList() {
        List<View> currentPageList = new ArrayList<>();
        currentPageList.add(createPageView(R.drawable.pie_current_week));
        currentPageList.add(createPageView(R.drawable.pie_month));
        currentPageList.add(createPageView(R.drawable.pie_year));
        return currentPageList;
    }

    @NonNull
    private List<View> createLastGraphList() {
        List<View> lastPageList = new ArrayList<>();
        lastPageList.add(createPageView(R.drawable.pie_last_week));
        lastPageList.add(createPageView(R.drawable.pie_month));
        lastPageList.add(createPageView(R.drawable.pie_year));
        return lastPageList;
    }

    @NonNull
    private List<View> createNextGraphList() {
        List<View> nextPageList = new ArrayList<>();
        nextPageList.add(createPageView(R.drawable.pie_next_week));
        nextPageList.add(createPageView(R.drawable.pie_month));
        nextPageList.add(createPageView(R.drawable.pie_year));
        return nextPageList;
    }

    private void prepareCategories() {

        Category a = new Category("Pets", 25.00, 25.00, 25.00, 50.00, 500.00, 12.25, 19.25, 22.25, 22.25, 252.25, 0, indicators[0]);
        categoryList.add(a);

        a = new Category("Groceries", 35.00, 35.00, 35.00, 150.00, 1500.00, 26.12, 31.12, 36.22, 60.12, 500.12, 1, indicators[1]);
        categoryList.add(a);

        a = new Category("Clothing", 35.00, 20.00, 30.00, 75.00, 700.00, 35.8, 15.80, 22.80, 30.8, 300.80, 2, indicators[2]);
        categoryList.add(a);

        a = new Category("Movies", 15.00, 15.00, 15.00, 20.00, 200.00, 13, 20, 11, 15, 80, 3, indicators[3]);
        categoryList.add(a);

        listAdapter.notifyDataSetChanged();
    }

    @NonNull
    private View createPageView(int picture) {
        View view = new View(getActivity());
        view.setBackground(getResources().getDrawable(picture));

        return view;
    }


    private class AnalyticsGraphicAdapter extends PagerAdapter {

        private List<View> viewList;

        AnalyticsGraphicAdapter() {
            this.viewList = new ArrayList<>();
        }

        @Override
        public Object instantiateItem(ViewGroup collection, int position) {
            View view = viewList.get(position);
            collection.addView(view);
            listAdapter.notifyDataSetChanged();
            return view;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        void setData(@Nullable List<View> list) {
            this.viewList.clear();
            if (list != null && !list.isEmpty()) {
                this.viewList.addAll(list);
            }

            notifyDataSetChanged();
        }

        @NonNull
        List<View> getData() {
            if (viewList == null) {
                viewList = new ArrayList<>();
            }

            return viewList;
        }
    }

    private class AnalyticsListAdapter extends RecyclerView.Adapter<AnalyticsListAdapter.MyViewHolder> {

        private List<Category> categoriesList;

        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public TextView title, spent;
            ImageView indicator;

            public MyViewHolder(View view) {
                super(view);
                view.setOnClickListener(this);
                title = (TextView) view.findViewById(R.id.category_name);
                spent = (TextView) view.findViewById(R.id.category_spent);
                indicator = (ImageView) view.findViewById(R.id.graph_indicator);
            }

            @Override
            public void onClick(View view) {
                Intent detailIntent = new Intent(getActivity(), CategoryDetailActivity.class);
                startActivityForResult(detailIntent, REQUEST_CODE_DETAIL);
            }
        }


        public AnalyticsListAdapter(List<Category> categoriesList) {
            this.categoriesList = categoriesList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_analysis_pie_chart, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            Category category = categoriesList.get(position);
            holder.title.setText(category.getName());
            int id = indicators[position];
            Drawable drawable = getResources().getDrawable(id);
            holder.indicator.setImageDrawable(drawable);
            if(timePosition == 1){
                holder.spent.setText("$" + category.getMontlySpending());
            }
            if (timePosition == 2){
                holder.spent.setText("$" + category.getYearlySpending());
            }if (timePosition == 0){
                // current, last or next
                if(curPosition == 0){
                    holder.spent.setText("$" + category.getWeeklySpending());
                }
                if(curPosition == -1){
                    holder.spent.setText("$" + category.getLastWeeklySpending());
                }
                if(curPosition == 1){
                    holder.spent.setText("$" + category.getNextWeeklySpending());
                }
            }
        }

        @Override
        public int getItemCount () {
            return categoriesList.size();
        }
    }




}