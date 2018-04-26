package edu.umd.cs.funanical;

/**
 * Created by kayxqli on 4/18/18.
 * Two pop up:
 * 1. Add new purchase
 * 2. Menu
 */

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class MainActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return MainFragment.newInstance();
    }

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }
}
