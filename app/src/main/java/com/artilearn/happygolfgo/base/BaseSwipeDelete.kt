package com.artilearn.happygolfgo.base

import android.graphics.Canvas
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.artilearn.happygolfgo.ui.home.alram.AlramAdapter
import kotlinx.android.synthetic.main.item_home_alram.view.*

abstract class BaseSwipeDelete : ItemTouchHelper.Callback() {

    abstract fun onSwipe(viewHolder: RecyclerView.ViewHolder, direction: Int)

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(0, ItemTouchHelper.LEFT)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        this.onSwipe(viewHolder, direction)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        getDefaultUIUtil().clearView(getView(viewHolder))
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        viewHolder?.let {
            getDefaultUIUtil().onSelected(getView(it))
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val view = getView(viewHolder)

            getDefaultUIUtil().onDraw(
                c,
                recyclerView,
                view,
                dX,
                dY,
                actionState,
                isCurrentlyActive
            )
        }
    }

    private fun getView(viewHolder: RecyclerView.ViewHolder): View {
        //return (viewHolder as AlramAdapter.AlramViewHolder).itemView.card_view
        return when (viewHolder) {
            is AlramAdapter.AlramViewHolder -> viewHolder.itemView.card_view
            else -> throw IllegalStateException("ViewHolder Not Found !")
        }
    }
}