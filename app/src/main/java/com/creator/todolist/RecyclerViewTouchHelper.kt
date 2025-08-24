package com.creator.todolist

import android.R
import android.graphics.Canvas
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.creator.todolist.adapter.ToDoAdapter
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator


class RecyclerViewTouchHelper(val adapter: ToDoAdapter) : ItemTouchHelper.SimpleCallback(
    0, // drag directions - 0 means no drag
    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT // enable both left and right swipe
) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        // Since we're not supporting drag & drop, return false
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition

        when (direction) {
            ItemTouchHelper.RIGHT -> {
                // Show confirmation dialog for delete
                val builder = AlertDialog.Builder(adapter.getContext())
                builder.setTitle("Delete Task")
                builder.setMessage("Are You Sure?")
                builder.setPositiveButton("Yes") { dialog, which ->
                    adapter.deleteTask(position)
                }
                builder.setNegativeButton("Cancel") { dialog, which ->
                    adapter.notifyItemChanged(position)
                }
                val dialog = builder.create()
                dialog.show()
            }
            ItemTouchHelper.LEFT -> {
                // Edit task on left swipe
                adapter.editTask(position)
            }
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
        RecyclerViewSwipeDecorator.Builder(
            c,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
            .addSwipeRightBackgroundColor(ContextCompat.getColor(adapter.getContext(), R.color.holo_red_light))
            .addSwipeRightActionIcon(R.drawable.ic_delete)
            .addSwipeLeftBackgroundColor(ContextCompat.getColor(adapter.getContext(), R.color.holo_green_dark))
            .addSwipeLeftActionIcon(R.drawable.ic_menu_edit)
            .create()
            .decorate()

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}