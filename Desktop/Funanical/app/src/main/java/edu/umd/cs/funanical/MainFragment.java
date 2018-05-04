package edu.umd.cs.funanical;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.OnMatrixChangedListener;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.OnSingleFlingListener;
import com.github.chrisbanes.photoview.PhotoView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import edu.umd.cs.funanical.Objects.Category;

import static android.app.Activity.RESULT_OK;

/**
 * Created by apple on 4/18/18.
 */

public class MainFragment extends Fragment {

    private ImageView menu_btn;
    private PhotoView mPhotoView;
    private View menuView;
    private TextView buildingTitle;
    private FloatingActionButton quick_add_btn;
    private PopupWindow menu_popup;
    private LinearLayout main_screen;
    private static final int REQUEST_CODE_CATEGORY = 1;
    private static final int REQUEST_CODE_STORE = 2;
    private static final int REQUEST_CODE_ANALYTICS = 3;
    static final String PHOTO_TAP_TOAST_STRING = "Photo Tap! X: %.2f %% Y:%.2f %% ID: %d";
    static final String FLING_LOG_STRING = "Fling velocityX: %.2f, velocityY: %.2f";
    private Toast mCurrentToast;
    private boolean addNewCategory = false;



    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // initalize all the UI componment in onCreateView and set up onclick
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mPhotoView = (PhotoView) view.findViewById(R.id.photo_view);

        mPhotoView.setOnPhotoTapListener(new PhotoTapListener());
        mPhotoView.setOnSingleFlingListener(new SingleFlingListener());
        updateBackground();


        // initalize all button
        menu_btn = (ImageView)view.findViewById(R.id.menu_btn);
        main_screen = (LinearLayout)view.findViewById(R.id.main_menu);
        quick_add_btn = (FloatingActionButton)view.findViewById(R.id.quick_add);
        buildingTitle = (TextView)view.findViewById(R.id.building_title);

        menu_btn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        // Initalize the pop up window
                        menuView = inflater.inflate(R.layout.menu, null);
                        LinearLayout category_btn = (LinearLayout)menuView.findViewById(R.id.category);
                        LinearLayout stat_btn = (LinearLayout)menuView.findViewById(R.id.stat);
                        LinearLayout store_btn = (LinearLayout)menuView.findViewById(R.id.store);
                        RelativeLayout menu_popupMain = (RelativeLayout)menuView.findViewById(R.id.outside_menu);


                        // Create another layout fix size to put it in the middle
                        menu_popup = new PopupWindow(menuView, LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);

                        // set button's onClick, inflate menu
                        category_btn.setOnClickListener(
                                new View.OnClickListener() {
                                    public void onClick(View v) {
                                        Intent categoryIntent = new Intent(getActivity(), CategoriesActivity.class);
                                        startActivityForResult(categoryIntent,REQUEST_CODE_CATEGORY);
                                    }
                                }
                        );

                        store_btn.setOnClickListener(
                                new View.OnClickListener() {
                                    public void onClick(View v) {
                                        Intent storeIntent = new Intent(getActivity(), StoreActivity.class);
                                        startActivityForResult(storeIntent,REQUEST_CODE_STORE);
                                    }
                                }
                        );

                        stat_btn.setOnClickListener(
                                new View.OnClickListener() {
                                    public void onClick(View v) {
                                        Intent statIntnet = new Intent(getActivity(), AnalyticsActivity.class);
                                        startActivityForResult(statIntnet,REQUEST_CODE_ANALYTICS);
                                    }
                                }
                        );

                        if(Build.VERSION.SDK_INT >= 21){
                            menu_popup.setElevation(5.0f);
                        }

                        // Get reference for the close button and listener for all buttons
                        ImageView close_menu = (ImageView) menuView.findViewById(R.id.cancel_menu);
                        close_menu.setOnClickListener(
                                new View.OnClickListener(){
                                    public void onClick(View v){
                                        menu_popup.dismiss();
                                    }

                                });
                        menu_popup.showAtLocation(main_screen, Gravity.CENTER, 0, 0);
                        // Closes the popup window when touch outside.
                    }
                }
        );

        // set button's onClick, inflate menu
        quick_add_btn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        // Initalize the pop up window
                        final View menuView = inflater.inflate(R.layout.quick_add_main, null);
                        Spinner categorySpinner = (Spinner)menuView.findViewById(R.id.category_spinner);
                        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                                R.array.category_array, android.R.layout.simple_spinner_item);
                        // Specify the layout to use when the list of choices appears
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        // Apply the adapter to the spinner
                        categorySpinner.setAdapter(adapter);

                        final EditText date_input = (EditText)menuView.findViewById(R.id.date);
                        final EditText purchase_name = (EditText)menuView.findViewById(R.id.purchase_name);
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
                                new DatePickerDialog(getActivity(), date, myCalendar
                                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                            }
                        });

                        final TableLayout receipt = (TableLayout)menuView.findViewById(R.id.receipt_layout);



                        menuView.findViewById(R.id.camera_btn).setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                receipt.setVisibility(View.VISIBLE);
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
                        ImageView close_menu = (ImageView) menuView.findViewById(R.id.cancel_menu);

                        menuView.findViewById(R.id.save_btn).setOnClickListener(
                                new View.OnClickListener(){
                                    public void onClick(View v){
                                        menu_popup.dismiss();
                                    }

                                });

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


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == REQUEST_CODE_CATEGORY) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                addNewCategory = true;
                updateBackground();
            }
        }
    }

    private void updateBackground(){
        if (addNewCategory == false){
            mPhotoView.setImageResource(R.drawable.town_final);
        } else{
            mPhotoView.setImageResource(R.drawable.before_town);
            buildingTitle.setVisibility(View.INVISIBLE);
        }
    }

    private class PhotoTapListener implements OnPhotoTapListener {

        @Override
        public void onPhotoTap(ImageView view, float x, float y) {
            float xPercentage = x * 100f;
            float yPercentage = y * 100f;

            if(yPercentage > 37 && yPercentage < 55  && xPercentage < 40){
                buildingTitle.setVisibility(View.VISIBLE);
                buildingTitle.setText("Groceries      Lv3");
                buildingTitle.setOnClickListener(
                        new View.OnClickListener() {
                            public void onClick(View v) {
                                Intent storeIntent = new Intent(getActivity(), CategoryDetailActivity.class);
                                startActivityForResult(storeIntent,REQUEST_CODE_CATEGORY);
                            }
                        }
                );
            } else if(yPercentage > 10 && yPercentage < 24  && xPercentage < 24) {
                buildingTitle.setVisibility(View.VISIBLE);
                buildingTitle.setText("Clothing      Lv2");
            } else if(yPercentage > 30 && yPercentage < 45  && xPercentage < 86 && xPercentage > 54) {
                buildingTitle.setVisibility(View.VISIBLE);
                buildingTitle.setText("Pets      Lv2");
            } else if(yPercentage > 59 && yPercentage < 83  && xPercentage > 50 && xPercentage < 90) {
                buildingTitle.setVisibility(View.VISIBLE);
                buildingTitle.setText("Movies      Lv2");
            } else if(yPercentage > 83 && yPercentage < 98  && xPercentage < 61 && xPercentage > 30) {
                buildingTitle.setVisibility(View.VISIBLE);
                buildingTitle.setText("Eating Out      Lv1");
            } else{
                    buildingTitle.setVisibility(View.INVISIBLE);
                }
            //showToast(String.format(PHOTO_TAP_TOAST_STRING, xPercentage, yPercentage, view == null ? 0 : view.getId()));
        }
    }

    private void showToast(CharSequence text) {
        if (mCurrentToast != null) {
            mCurrentToast.cancel();
        }

        mCurrentToast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
        mCurrentToast.show();
    }

    private class SingleFlingListener implements OnSingleFlingListener {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.d("PhotoView", String.format(FLING_LOG_STRING, velocityX, velocityY));
            return true;
        }
    }

}
