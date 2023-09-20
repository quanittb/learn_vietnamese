package com.mobiai.app.ui.fragment

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mobiai.R
import com.mobiai.app.broadcast.ReminderReceiver
import com.mobiai.app.ui.dialog.RepeatDialog
import com.mobiai.app.ui.dialog.TimeDialog
import com.mobiai.base.basecode.storage.SharedPreferenceUtils
import com.mobiai.base.basecode.ui.fragment.BaseFragment
import com.mobiai.databinding.FragmentReminderBinding
import java.lang.StringBuilder
import java.util.Calendar
import java.util.Locale

class ReminderFragment : BaseFragment<FragmentReminderBinding>() {
    companion object{
        fun instance(): ReminderFragment{
            return newInstance(ReminderFragment::class.java)
        }
    }
    private var bottomSheetTimeDialog: TimeDialog? = null
    private var bottomSheetRepeatDialog: RepeatDialog? = null
    private var alarmManager: AlarmManager? = null
    private var dayOfWeek: ArrayList<String> = arrayListOf()
    var intent: Intent? = null
    private var pendingIntent: PendingIntent? = null
    override fun initView() {
        initData()
        onClick()
    }
    fun initData(){
        alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        intent = Intent(requireContext(), ReminderReceiver::class.java)
        pendingIntent =
            PendingIntent.getBroadcast(requireContext(), 12, intent!!, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)

        createNotification()
        getRepeat()
        dayOfWeek = ArrayList()
        insertData()
        checkAutoToggle()
    }
    fun onClick(){
        if (SharedPreferenceUtils.minuteAlarm >= 10)
            binding.txtTime.text =
                "${SharedPreferenceUtils.hourAlarm}:${SharedPreferenceUtils.minuteAlarm}"
        else
            binding.txtTime.text =
                "${SharedPreferenceUtils.hourAlarm}:0${SharedPreferenceUtils.minuteAlarm}"
        binding.txtTime.setOnClickListener {
            openTimeBottomSheet()
            setAlarm()
        }
        binding.turnOnReminder.setOnClickListener {
            SharedPreferenceUtils.alarm = !SharedPreferenceUtils.alarm
            checkAutoToggle()
            setAlarm()

        }
        binding.btnClose.setOnClickListener {
            handlerBackPressed()
        }
        binding.btnRepeat.setOnClickListener {
            openRepeatBottomSheet()
            setAlarm()
        }
        binding.btnClose.setOnClickListener {
            handlerBackPressed()
        }
    }

    private fun openTimeBottomSheet() {
        if (bottomSheetTimeDialog == null) {
            bottomSheetTimeDialog = TimeDialog(
                requireContext(),
                object : TimeDialog.OnClickBottomSheetListener {
                    @SuppressLint("SetTextI18n")
                    override fun onClickSaveFrom() {
                        if (SharedPreferenceUtils.minuteAlarm >= 10)
                            binding.txtTime.text =
                                "${SharedPreferenceUtils.hourAlarm}:${SharedPreferenceUtils.minuteAlarm}"
                        else
                            binding.txtTime.text =
                                "${SharedPreferenceUtils.hourAlarm}:0${SharedPreferenceUtils.minuteAlarm}"
                    }

                })
        }
        bottomSheetTimeDialog?.checkShowBottomSheet()
    }

    private fun openRepeatBottomSheet() {
        if (bottomSheetRepeatDialog == null) {
            bottomSheetRepeatDialog = RepeatDialog(
                requireContext(),
                object : RepeatDialog.OnClickBottomSheetListener {
                    @SuppressLint("SetTextI18n")
                    override fun onClickSaveFrom() {
                        if (SharedPreferenceUtils.minuteAlarm >= 10)
                            binding.txtTime.text =
                                "${SharedPreferenceUtils.hourAlarm}:${SharedPreferenceUtils.minuteAlarm}"
                        else
                            binding.txtTime.text =
                                "${SharedPreferenceUtils.hourAlarm}:0${SharedPreferenceUtils.minuteAlarm}"
                        getRepeat()
                    }

                })
        }
        bottomSheetRepeatDialog?.checkShowBottomSheet()
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentReminderBinding {
        return FragmentReminderBinding.inflate(inflater,container,false)
    }
    override fun handlerBackPressed() {
        super.handlerBackPressed()
        closeFragment(this)
    }
    private fun insertData() {
        dayOfWeek = ArrayList()
        dayOfWeek.clear()
        if (SharedPreferenceUtils.monday)
            dayOfWeek.add("Monday")
        if (SharedPreferenceUtils.tuesday)
            dayOfWeek.add("Tuesday")
        if (SharedPreferenceUtils.wednesday)
            dayOfWeek.add("Wednesday")
        if (SharedPreferenceUtils.thursday)
            dayOfWeek.add("Thursday")
        if (SharedPreferenceUtils.friday)
            dayOfWeek.add("Friday")
        if (SharedPreferenceUtils.saturday)
            dayOfWeek.add("Saturday")
        if (SharedPreferenceUtils.sunday)
            dayOfWeek.add("Sunday")
    }
    fun checkAutoToggle() {
        if (SharedPreferenceUtils.alarm) {
            binding.turnOnReminder.setImageDrawable(resources.getDrawable(R.drawable.toggle_turn_on))
        } else {
            binding.turnOnReminder.setImageDrawable(resources.getDrawable(R.drawable.toggle_turn_off))
        }
    }
    private fun createNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = CHANNEL_ID
            val description = "message reminder"
            val important = NotificationManager.IMPORTANCE_HIGH
            val chanel = NotificationChannel(CHANNEL_ID, name, important)
            chanel.description = description

            val notificationManager =
                requireContext().getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(chanel)
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    fun setWeeklyNotifications() {
        pendingIntents.clear()

        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Xác định thời điểm bắt đầu (lần đầu) thông báo

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, SharedPreferenceUtils.hourAlarm)
        calendar.set(Calendar.MINUTE, SharedPreferenceUtils.minuteAlarm)
        calendar.set(Calendar.SECOND, 0)

        // Tạo một danh sách các PendingIntent cho từng ngày trong tuần

        for (day in dayOfWeek) {
            val calendarForDay = Calendar.getInstance() // Tạo bản sao của calendar
            calendarForDay.timeInMillis = calendar.timeInMillis
            calendarForDay.set(Calendar.DAY_OF_WEEK, getDayOfWeek(day))
            if (calendarForDay.timeInMillis < System.currentTimeMillis()) {
                calendarForDay.add(Calendar.DAY_OF_WEEK, 7)
            }
            val intent = Intent(requireContext(), ReminderReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                requireContext(),
                getDayOfWeek(day),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )

            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP, calendarForDay.timeInMillis,
                AlarmManager.INTERVAL_DAY * 7,
                pendingIntent)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendarForDay.timeInMillis,
                    pendingIntent
                )
            }
            else{
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP, calendarForDay.timeInMillis,
                    AlarmManager.INTERVAL_DAY * 7,
                    pendingIntent)
            }
            pendingIntents.add(pendingIntent)
        }

    }

    private fun clearNotification(){
        for(pendingIntent in pendingIntents){
            alarmManager?.cancel(pendingIntent)
            pendingIntent.cancel()
        }
    }
    // Lấy ngày trong tuần dựa trên tên
    private fun getDayOfWeek(day: String): Int {
        return when (day.toLowerCase(Locale.getDefault())) {
            "sunday" -> Calendar.SUNDAY
            "monday" -> Calendar.MONDAY
            "tuesday" -> Calendar.TUESDAY
            "wednesday" -> Calendar.WEDNESDAY
            "thursday" -> Calendar.THURSDAY
            "friday" -> Calendar.FRIDAY
            "saturday" -> Calendar.SATURDAY
            else -> Calendar.MONDAY // Mặc định là Thứ 2
        }
    }

    fun setAlarm(){
        if (SharedPreferenceUtils.alarm) {
            insertData()
            setWeeklyNotifications()
        } else {
            setWeeklyNotifications()
            clearNotification()
        }
    }
    fun getRepeat() {
        val content = StringBuilder()
        if (SharedPreferenceUtils.monday)
            content.append(" "+ getString(R.string._mon))
        if (SharedPreferenceUtils.tuesday)
            content.append(" "+ getString(R.string._tue))
        if (SharedPreferenceUtils.wednesday)
            content.append(" "+ getString(R.string._wed))
        if (SharedPreferenceUtils.thursday)
            content.append(" "+ getString(R.string._thu))
        if (SharedPreferenceUtils.friday)
            content.append(" "+ getString(R.string._fri))
        if (SharedPreferenceUtils.saturday)
            content.append(" "+ getString(R.string._sat))
        if (SharedPreferenceUtils.sunday)
            content.append(" "+ getString(R.string._sun))
        binding.txtRepeat.text = content
    }
}

const val CHANNEL_ID = "notifyAlarm"
val pendingIntents = mutableListOf<PendingIntent>()