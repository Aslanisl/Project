package com.livetyping.moydom.utils;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by Ivan on 04.12.2017.
 */

public class ItemTouchMoveHelperCallback extends ItemTouchHelper.Callback{

    private final ItemTouchMoveHelper mTouchHelper;

    public ItemTouchMoveHelperCallback(ItemTouchMoveHelper helper) {
        mTouchHelper = helper;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        return makeMovementFlags(dragFlags, 0);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        mTouchHelper.onItemMove(viewHolder, target);
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }
}
