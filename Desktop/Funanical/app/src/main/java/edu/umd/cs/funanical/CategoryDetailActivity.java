package edu.umd.cs.funanical;

/**
 * Created by apple on 4/18/18.
 */

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import com.rd.PageIndicatorView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.umd.cs.funanical.Objects.Purchase;

/**
 * Created by apple on 4/20/18.
 */

public class CategoryDetailActivity extends AppCompatActivity {


    private static final int REQUEST_CODE_DETAIL = 1;
    private FloatingActionButton quick_add_btn;
    private LinearLayout main_screen;
    private PopupWindow menu_popup;
    private TextView title;
    private RecyclerView recyclerView;
    private PageIndicatorView pageIndicatorView;
    private List<Purchase> purchaseList;
    private PurchaseListAdapter listAdapter;
    private int timePosition = 0;
    private int curPosition = 0; // -1 means last week, 0 current week, 1 next week
    private ViewPager pager;
    private AnalyticsGraphicAdapter graphicAdapter;
    private static final String TAG = "LALALALA";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);
        initViews();
    }

    @SuppressWarnings("ConstantConditions")
    private void initViews() {
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


        createCurrentGraphList();
//        createLastGraphList();
//        createNextGraphList();

        title = (TextView) findViewById(R.id.graph_title);
        quick_add_btn = (FloatingActionButton)findViewById(R.id.quick_add);
        main_screen = (LinearLayout)findViewById(R.id.main_menu);

        findViewById(R.id.last_graph).setOnClickListener(new View.OnClickListener() {
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

        findViewById(R.id.next_graph).setOnClickListener(new View.OnClickListener() {
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

        // set button's onClick, inflate menu
        quick_add_btn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        // Initalize the pop up window
                        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                        View menuView = inflater.inflate(R.layout.quick_add_detail, null);;
                        menu_popup = new PopupWindow(menuView, LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);

                        List<String> lst = new ArrayList<String>();
                        lst.add("Clothing");
                        lst.add("Movies");
                        lst.add("Pets");
                        lst.add("Geoceries");
                        Spinner categorySpinner = (Spinner)menuView.findViewById(R.id.category_spinner);
                        ArrayAdapter<String> adapter =  new ArrayAdapter<String>(CategoryDetailActivity.this,
                                android.R.layout.simple_spinner_item, lst);
                        // Specify the layout to use when the list of choices appears
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        // Apply the adapter to the spinner
                        categorySpinner.setAdapter(adapter);

                        final EditText date_input = (EditText)menuView.findViewById(R.id.date);
                        String today = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                        date_input.setText(today);
                        final Calendar myCalendar = Calendar.getInstance();
                        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
                                myCalendar.set(Calendar.YEAR, year);
                                myCalendar.set(Calendar.MONTH, monthOfYear);
                                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                String myFormat = "MM/dd/yy"; //In which you need put here
                                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                                date_input.setText(sdf.format(myCalendar.getTime()));
                            }

                        };

                        date_input.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                new DatePickerDialog(CategoryDetailActivity.this, date, myCalendar
                                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                            }
                        });


                        // Create another layout fix size to put it in the middle
                        menu_popup = new PopupWindow(menuView, LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);

                        menu_popup.setFocusable(true);
                        menu_popup.update();

                        if(Build.VERSION.SDK_INT >= 21){
                            menu_popup.setElevation(5.0f);
                        }

                        // Get reference for the close button and listener for all buttons
                        menuView.findViewById(R.id.cancel_btn).setOnClickListener(
                                new View.OnClickListener(){
                                    public void onClick(View v){
                                        menu_popup.dismiss();
                                    }

                                });
                        menu_popup.showAtLocation(main_screen, Gravity.CENTER, 0, 0);
                    }
                }
        );

        graphicAdapter = new AnalyticsGraphicAdapter();
        graphicAdapter.setData(createCurrentGraphList());

        purchaseList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        listAdapter = new PurchaseListAdapter(purchaseList);

        pager = (ViewPager) findViewById(R.id.viewPager);
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


        pageIndicatorView = (PageIndicatorView) findViewById(R.id.viewIndicator);
        pageIndicatorView.setViewPager(pager);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(listAdapter);

        preparePurchases();
    }


    // ONLY CALL THESE METHOD ONCE
    private List<View> createCurrentGraphList() {
        List<View> currentPageList = new ArrayList<>();
        currentPageList.add(createPageView(R.drawable.clumative_current));
        currentPageList.add(createPageView(R.drawable.clumative_month));
        currentPageList.add(createPageView(R.drawable.clumative_year));
        return currentPageList;
    }

    private List<View> createLastGraphList() {
        List<View> lastPageList = new ArrayList<>();
        lastPageList.add(createPageView(R.drawable.clumative_last));
        lastPageList.add(createPageView(R.drawable.clumative_month));
        lastPageList.add(createPageView(R.drawable.clumative_year));
        return lastPageList;
    }

    private List<View> createNextGraphList() {
        List<View> nextPageList = new ArrayList<>();
        nextPageList.add(createPageView(R.drawable.clumative_next));
        nextPageList.add(createPageView(R.drawable.clumative_month));
        nextPageList.add(createPageView(R.drawable.clumative_year));
        return nextPageList;
    }

    private void preparePurchases() {

        Purchase a = new Purchase("Apples", "04/24/2018", 10.00);
        purchaseList.add(a);

        a = new Purchase("Yogurt", "04/20/2018", 4.00);
        purchaseList.add(a);

        a = new Purchase("Cheese", "04/19/2018", 2.89);
        purchaseList.add(a);

        a = new Purchase("Kiwi", "04/14/2018", 9.00);
        purchaseList.add(a);

        a = new Purchase("Water", "04/13/2018", 3.99);
        purchaseList.add(a);

        listAdapter.notifyDataSetChanged();
    }

    @NonNull
    private View createPageView(int picture) {
        View view = new View(this);
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

    private class PurchaseListAdapter extends RecyclerView.Adapter<PurchaseListAdapter.MyViewHolder> {

        private List<Purchase> purchaseList;

        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public TextView name, date, amount;

            public MyViewHolder(View view) {
                super(view);
                view.setOnClickListener(this);
                name = (TextView) view.findViewById(R.id.purchase_name);
                date = (TextView) view.findViewById(R.id.purchase_date);
                amount = (TextView) view.findViewById(R.id.purchase_amount);
            }

            @Override
            public void onClick(View view) {
                Intent detailIntent = new Intent(getApplicationContext(), PurchaseDetailActivity.class);
                startActivityForResult(detailIntent, REQUEST_CODE_DETAIL);
            }
        }


        public PurchaseListAdapter(List<Purchase> purchaseList) {
            this.purchaseList = purchaseList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_purchase, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            Purchase purchase = purchaseList.get(position);
            holder.name.setText(purchase.getName());
            holder.amount.setText("$" + purchase.getAmount());
            holder.date.setText(purchase.getDate());
        }

        @Override
        public int getItemCount () {
            return purchaseList.size();
        }
    }
}