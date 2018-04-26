package edu.umd.cs.funanical;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.List;
import java.util.Vector;

import edu.umd.cs.funanical.R;

/**
 * Created by apple on 4/23/18.
 */

public class AnalyticsActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);


        // make the toolbar takes up the whole space
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setContentInsetsAbsolute(0, 0);
        toolbar.getContentInsetEnd();
        toolbar.setPadding(0, 0, 0, 0);

        setSupportActionBar(toolbar);

        // BACK BUTTON
        toolbar.findViewById(R.id.toolbar_back_category).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        // Initializing tab and pager views
        tabLayout = (TabLayout) findViewById(R.id.tab_selection);
        final ViewPager pager = (ViewPager) findViewById(R.id.tab_layout);

        // Making new tabs and adding to tabLayout
        tabLayout.addTab(tabLayout.newTab().setText("Statistics"));
        tabLayout.addTab(tabLayout.newTab().setText("Categories"));
        tabLayout.getTabAt(1).setText("Statistics");

        // Adding fragments to a list
        List<Fragment> fragments = new Vector<Fragment>();
        fragments.add(Fragment.instantiate(this, AnalyticsBarChartFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, AnalyticsPieChartFragment.class.getName()));

        // Attaching fragments into tabLayout with ViewPager
        pager.setAdapter(new SectionPagerAdapter(getSupportFragmentManager(), fragments));
        tabLayout.setupWithViewPager(pager);

        // Need communication between two fragment to know current selected time frame
    }

}