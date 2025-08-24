package com.creator.todolist

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.creator.todolist.Utils.DatabaseHelper
import com.creator.todolist.model.Task
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddNewTaskFragment : BottomSheetDialogFragment() {

    private lateinit var editText: EditText
    private var btn: Button? = null // Make nullable
    private lateinit var db: DatabaseHelper

    companion object {
        val TAG = "AddNewTask"
        fun newInstance(): AddNewTaskFragment {
            return AddNewTaskFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_new_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views with null checks
        editText = view.findViewById(R.id.edit_text)
        btn = view.findViewById(R.id.new_task_btn)

        // Check if button was found
        if (btn == null) {
            Toast.makeText(requireContext(), "UI Error: Button not found", Toast.LENGTH_SHORT).show()
            dismiss()
            return
        }

        db = DatabaseHelper(requireContext())
        var isUpdated = false
        val bundle = arguments

        if (bundle != null) {
            isUpdated = true
            val text = bundle.getString("task")
            editText.setText(text)
            text?.length?.let {
                btn?.isEnabled = false
            }
        }

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isEmpty()) {
                    btn?.isEnabled = false
                    btn?.setBackgroundColor(Color.GRAY)
                } else {
                    btn?.isEnabled = true
                    // Set the normal background color
                    btn?.setBackgroundColor(resources.getColor(android.R.color.holo_blue_dark, null))
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        btn?.setOnClickListener {
            val t = editText.text.toString()
            if (isUpdated) {
                db.updateTask(bundle!!.getInt("ID"), t)
            } else {
                val item = Task()
                item.text = t
                item.status = 0
                db.addTask(item)
            }
            dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (activity is OnDialogCloseListener) {
            (activity as OnDialogCloseListener).onDialogClose(dialog)
        }
    }
}