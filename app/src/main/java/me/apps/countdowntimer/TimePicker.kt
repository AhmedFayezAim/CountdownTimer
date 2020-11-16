package me.apps.countdowntimer

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.ikovac.timepickerwithseconds.MyTimePickerDialog

class TimePicker(
    private val view: MainView,
    private val hour: Int,
    private val minute: Int,
    private val seconds: Int
) : DialogFragment(), MyTimePickerDialog.OnTimeSetListener {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MyTimePickerDialog(requireActivity(), this, hour, minute, seconds, true)
    }

    override fun onTimeSet(
        picker: com.ikovac.timepickerwithseconds.TimePicker?,
        hourOfDay: Int,
        minute: Int,
        seconds: Int
    ) {
        view.updatedTime(hourOfDay, minute, seconds)
    }
}