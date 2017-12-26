package com.livetyping.moydom.utils;

import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * Created by Ivan on 02.12.2017.
 */

public class BottomNavigationViewHelper {

    @SuppressWarnings("RestrictedApi")
    public static void removeShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);

                Field largeLabel = item.getClass().getDeclaredField("mLargeLabel");
                largeLabel.setAccessible(true);
                TextView largeLabelTextView = (TextView) largeLabel.get(item);
                largeLabelTextView.setEllipsize(null);
                largeLabelTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                largeLabel.setAccessible(false);

                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e("ERROR NO SUCH FIELD", "Unable to get shift mode field");
        } catch (IllegalAccessException e) {
            Log.e("ERROR ILLEGAL ALG", "Unable to change value of shift mode");
        }
    }
}
