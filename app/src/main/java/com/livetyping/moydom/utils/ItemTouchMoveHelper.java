package com.livetyping.moydom.utils;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Ivan on 04.12.2017.
 */

public interface ItemTouchMoveHelper {
    void onItemMove(RecyclerView.ViewHolder fromHolder, RecyclerView.ViewHolder toHolder);
}

