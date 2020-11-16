package me.apps.countdowntimer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import me.apps.countdowntimer.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), MainView {

    private var timer: CountDownTimer? = null
    private lateinit var binding: ActivityMainBinding
    private lateinit var time: Calendar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        time = Calendar.getInstance()

        with(binding) {
            tvNote.text = getString(R.string.pick_time_after_now_to_start_counting)
            tvDatePicked.text = getDateFormat(time.timeInMillis)
            tvTimePicked.text = getTimeFormat(time.timeInMillis)
            tvChangeDate.setOnClickListener {
                DatePicker(
                    this@MainActivity,
                    time[Calendar.DAY_OF_MONTH],
                    time[Calendar.MONTH],
                    time[Calendar.YEAR]
                ).show(supportFragmentManager, "datePicker")
            }
            tvChangeTime.setOnClickListener {
                TimePicker(
                    this@MainActivity,
                    time[Calendar.HOUR_OF_DAY],
                    time[Calendar.MINUTE],
                    time[Calendar.SECOND]
                ).show(supportFragmentManager, "timePicker")
            }
        }
    }

    private fun getDateFormat(time: Long): String =
        SimpleDateFormat("d MMMM", Locale.ENGLISH).format(time)

    private fun getTimeFormat(time: Long): String =
        SimpleDateFormat("H:mm:ss", Locale.ENGLISH).format(time)

    private fun getTimeLeftFormat(time: Long): String {
        val diffInDays = String.format("%02d", TimeUnit.DAYS.convert(time, TimeUnit.MILLISECONDS))
        val diffInHours = String.format(
            "%02d", TimeUnit.HOURS.convert(time, TimeUnit.MILLISECONDS)
                    - (TimeUnit.DAYS.convert(time, TimeUnit.MILLISECONDS) * 24)
        )
        val diffInMinutes =
            String.format(
                "%02d", TimeUnit.MINUTES.convert(time, TimeUnit.MILLISECONDS)
                        - (TimeUnit.HOURS.convert(time, TimeUnit.MILLISECONDS) * 60)
            )
        val diffInSeconds =
            String.format(
                "%02d", TimeUnit.SECONDS.convert(time, TimeUnit.MILLISECONDS)
                        - (TimeUnit.MINUTES.convert(time, TimeUnit.MILLISECONDS) * 60)
            )
        return "$diffInDays:$diffInHours:$diffInMinutes:$diffInSeconds"
    }

    override fun updatedDate(day: Int, month: Int, year: Int) {
        time[Calendar.DAY_OF_MONTH] = day
        time[Calendar.MONTH] = month
        time[Calendar.YEAR] = year
        binding.tvDatePicked.text = getDateFormat(time.timeInMillis)
        updateTimerState()
    }

    override fun updatedTime(hours: Int, minutes: Int, seconds: Int) {
        time[Calendar.HOUR_OF_DAY] = hours
        time[Calendar.MINUTE] = minutes
        time[Calendar.SECOND] = seconds
        binding.tvTimePicked.text = getTimeFormat(time.timeInMillis)
        updateTimerState()
    }

    private fun updateTimerState() {
        if (time > Calendar.getInstance()) {
            binding.tvNote.text = ""
            timer?.cancel()
            timer = object :
                CountDownTimer(
                    time.timeInMillis - Calendar.getInstance().timeInMillis,
                    1000
                ) {
                override fun onTick(p0: Long) {
                    binding.tvCountdown.text = getTimeLeftFormat(p0)
                }

                override fun onFinish() {
                    binding.tvCountdown.text = getTimeLeftFormat(0)
                }
            }
            timer?.start()
        } else {
            timer?.cancel()
            binding.tvCountdown.text = getString(R.string.reset_counter_value)
            binding.tvNote.text = getString(R.string.pick_time_after_now_to_start_counting)
        }
    }
}

interface MainView {
    fun updatedDate(day: Int, month: Int, year: Int)
    fun updatedTime(hours: Int, minutes: Int, seconds: Int)
}