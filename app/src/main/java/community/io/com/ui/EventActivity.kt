package community.io.com.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import community.io.com.R
import community.io.com.api.BaseResponse
import community.io.com.data.model.Event
import community.io.com.data.model.EventResponseMode
import community.io.com.data.model.Locations
import community.io.com.data.model.LocationsResponceModel
import community.io.com.databinding.*
import community.io.com.ui.adapter.EventAdapter
import community.io.com.ui.adapter.LocationAdapter
import community.io.com.utils.CurrentDayDecorator
import community.io.com.utils.SquareDecorator
import community.io.com.utils.Utils
import community.io.com.viewmodel.EventViewModel
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.text.SimpleDateFormat
import java.util.*


class EventActivity : AppCompatActivity() {

    lateinit var loginViewModel: EventViewModel

    lateinit var binding: ActivityEventsBinding

    val event_data = mutableListOf<Event>()

    val spl_data = mutableListOf<Locations>()


    lateinit var adapter: EventAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_events)
        loginViewModel = EventViewModel(application)
        binding.loginViewModel = loginViewModel
        binding.lifecycleOwner = this
        bindViewModel()


    }


    fun bindViewModel() {


        loginViewModel.locationResult.observe(this) {
            when (it) {
                is BaseResponse.Loading -> {
//                    showLoading()
                }

                is BaseResponse.Success -> {
                    stopLoading()

                    processLocationList(it.data)

                }

                is BaseResponse.Error -> {
                    processError(it.msg)
                }
                else -> {
                    stopLoading()
                }
            }
        }


        loginViewModel.eventResult.observe(this) {
            when (it) {
                is BaseResponse.Loading -> {
                    showLoading()
                }

                is BaseResponse.Success -> {
                    stopLoading()

                    processEventList(it.data)

                }

                is BaseResponse.Error -> {
                    processError(it.msg)
                }
                else -> {
                    stopLoading()
                }
            }
        }


        loginViewModel.getEvents()
        loginViewModel.getEventLocations()


        binding.ivToolbarBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        loginViewModel.onAddEventClick = {
            openAddEventDialog()
        }

        loginViewModel.onLoadEventClick = {
            binding.calendarView.setCurrentDate(CalendarDay.today())
        }

        binding.calendarView.topbarVisible = false


        var eventsData = mutableListOf<Event>()

        adapter = EventAdapter(this, eventsData)
        binding.rcvEvents.adapter = adapter
        binding.rcvEvents.layoutManager = LinearLayoutManager(this)
        binding.rcvEvents.setHasFixedSize(true)


        adapter.onEventViewClick = { i: Int, event: Event ->

            openEventDetailDialog(event)
        }
        val squareDecorator = SquareDecorator(this)
        binding.calendarView.addDecorators(squareDecorator)

        changeMonth(CalendarDay.today())

//        binding.calendarView.addDecorators(
//            CurrentDayDecorator(
//                this,
//                CalendarDay.today(),
//                ContextCompat.getDrawable(
//                    this,
//                    R.drawable.square_drawable
//                )
//            )
//        )
        binding.calendarView.addDecorators(
            CurrentDayDecorator(
                this,
                CalendarDay.today(),
                ContextCompat.getDrawable(
                    this,
                    R.drawable.current_day_selector_green
                )
            )
        )

//        binding.calendarView.setSelectedDate(CalendarDay.today());
        binding.calendarView.setOnMonthChangedListener { widget, date -> changeMonth(date) }

        binding.calendarView.setOnDateChangedListener { widget, date, selected -> changeDate(date) }


    }

    fun changeDate(date1: CalendarDay) {

        val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        var eventsData = mutableListOf<Event>()
        for (events in event_data) {
            val date: Date =
                simpleDateFormat.parse(events.startTime)
            val day: CalendarDay = CalendarDay.from(date.time)
            if (day == date1) {
                eventsData.add(events)
            }


        }

        adapter.updateList(eventsData)

    }

    fun changeMonth(date: CalendarDay) {
        val simpleDateFormat = SimpleDateFormat("MMMM yyyy", Locale.US) //"February 2016" format


        val monthAndYear: String = simpleDateFormat.format(date.date)

        loginViewModel.changeMonthName(monthAndYear)


    }


    fun openEventDetailDialog( event: Event) {

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val view: View = LayoutInflater.from(this)
            .inflate(R.layout.dialog_view_event, null, false)

        val binding1 =  DialogViewEventBinding.bind(view)

        binding1.loginViewModel = loginViewModel
//        binding1.lifecycleOwner = this
        builder.setView(view)
//        builder.setView(binding1.getRoot())

        val alert: AlertDialog = builder.create()

        binding1.eventName.text = ": ${event.title}"
        binding1.eventDescription.text = ": ${event.short_desc}"
        binding1.eventLocation.text = ": ${event.location}"

        val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        val date: Date = simpleDateFormat.parse(event!!.startTime)

        val dateFormat = SimpleDateFormat("MMMM d, yyyy hh:mm a")
        var startdate : String     =    dateFormat.format(date)
        val outFormat = SimpleDateFormat("EEEE")
        val dayName = outFormat.format(date)

        binding1.eventFrom.text = ": $dayName, $startdate"

        val date1: Date = simpleDateFormat.parse(event!!.endTime)
        var enddate : String     =    dateFormat.format(date1)
        val endDayName = outFormat.format(date1)

        binding1.eventTo.text = ": $endDayName, $enddate"

        binding1.btnDone.setOnClickListener {
            Log.e("myselectedDataaname11", "myselectedDataaname11")
            alert.dismiss()
        }

        alert.show()

    }

    fun openAddEventDialog() {

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val view: View = LayoutInflater.from(this)
            .inflate(R.layout.layout_add_event, null, false)

        val binding1 = LayoutAddEventBinding.bind(view);

//        val binding1: LayoutAddEventBinding = DataBindingUtil.inflate(
//            LayoutInflater.from(this),
//            R.layout.layout_add_event,
//            null,
//            false
//        )
        binding1.loginViewModel = loginViewModel
//        binding1.lifecycleOwner = this
        builder.setView(view)
//        builder.setView(binding1.getRoot())

        val alert: AlertDialog = builder.create()

        binding1.eventDate.setOnClickListener {
            openDatePickerDialog()
        }

        binding1.inputLocations.setOnClickListener {
            openLocationSelectDialog()
        }

        binding1.btnOkay.setOnClickListener {
            Log.e("myselectedDataaname11", "myselectedDataaname11")
            loginViewModel.createEvent()
            alert.dismiss()
        }

        binding1.btnCancel.setOnClickListener {
            Log.e("myselectedDataaname11", "myselectedDataaname11")
            alert.dismiss()
        }

        var cal = Calendar.getInstance()
        cal.timeInMillis = System.currentTimeMillis()
        val myFormat = "MMM.dd.yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        loginViewModel.onEventDateChanged(cal.time)

//        loginViewModel.onEventDateChanged(sdf.format(System.currentTimeMillis()))

        alert.show()

        openScheduleEventMesageDialog()
    }


    fun openScheduleEventMesageDialog() {

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)

        val binding: LayoutScheduleWarningBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this),
            R.layout.layout_schedule_warning,
            null,
            false
        )

        builder.setView(binding.getRoot())
//        setContentView(binding.getRoot())
        val alert: AlertDialog = builder.create()
        binding.btnOkay.setOnClickListener {
            Log.e("myselectedDataaname11", "myselectedDataaname11")
            loginViewModel.createEvent()
            alert.dismiss()
        }
        alert.show()

    }


    fun openDatePickerDialog() {
        var cal = Calendar.getInstance()

//        loginViewModel.onEventDateChanged(sdf.format(System.currentTimeMillis()))

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                loginViewModel.onEventDateChanged(cal.time)

//            loginViewModel.onEventDateChanged(sdf.format(cal.time))

            }
        DatePickerDialog(
            this@EventActivity, dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()

    }

    fun openLocationSelectDialog() {
        Log.e("LocationselectionDialog:", "LocationselectionDialog:")

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)

        val binding1: LocationLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this),
            R.layout.location_layout,
            null,
            false
        )




        builder.setView(binding1.getRoot())
//        setContentView(binding.getRoot())
        val alert: AlertDialog = builder.create()

        var adapter = LocationAdapter(this, spl_data)
        binding1.rcvLocations.adapter = adapter
        binding1.rcvLocations.layoutManager = LinearLayoutManager(this)
        binding1.rcvLocations.setHasFixedSize(true)


        binding1.btnCancel.setOnClickListener {
            alert.dismiss()
        }

        binding1.btnOkay.setOnClickListener {

            Log.e("myselectedDataaname", "myselectedDataaname")
            Log.e("myselectedDataaname", "${adapter.getSelectedPositons()}")
            var item = spl_data.get(adapter.getSelectedPositons())
            Log.e("myselectedDataaname", item.name)
            loginViewModel.evetLocationsId.value = item.id
//            loginViewModel.onEventLocationsChanged(item.name)
            loginViewModel.evetLocations.value = item.name
            alert.dismiss()
        }


        alert.show()

//        val listview: Listview = dialog.findViewById(R.id.listview) as ListView
//
//        val adapter = CustomAdapter(context, your_list)
//        listview.setadapter(adapter)
//        listView.setOnItemClickListener(OnItemClickListener { parent, view, position, id ->
//            //Do something
//        })

    }

    fun showLoading() {
        Utils.showCustomLottieAnimationDialog(this, "Loading event...")
//        binding.prgbar.visibility = View.VISIBLE
    }

    fun stopLoading() {
        Utils.hideCustomLottieAnimationDialog()
//        binding.prgbar.visibility = View.GONE
    }

    fun processError(msg: String?) {
        Log.e("Error:", msg.toString())
        Utils.showSnackbarMessage(binding.root, msg.toString())
    }


    private fun processLocationList(data: LocationsResponceModel?) {
        Log.e("Success:", data?.message.toString())
        if (data!!.spl_data.size >= 0) {
            spl_data.addAll(data?.spl_data!!)
            Log.e("Success:", "${data?.spl_data!!.size}")

        } else {

        }

        Utils.showSnackbarMessage(binding.root, data?.message.toString())
    }


    private fun processEventList(data: EventResponseMode?) {
        Log.e("Success:", data?.message.toString())
        if (data!!.data.size >= 0) {
            event_data.addAll(data?.data!!)
            Log.e("Success:", "${data?.data!!.size}")

            val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US)
            var callenderdate = mutableListOf<CalendarDay>()
            for (events in data?.data) {
                val date: Date =
                    simpleDateFormat.parse(events.startTime)
                val day: CalendarDay = CalendarDay.from(date.time)
                callenderdate.add(day)

            }

            val squareDecorator = SquareDecorator(this)
//            binding.calendarView.addDecorators(squareDecorator)

            binding.calendarView.removeDecorator(squareDecorator)

            binding.calendarView.addDecorators(
                CurrentDayDecorator(
                    this,
                    callenderdate
                )
            )


        } else {

        }

        Utils.showSnackbarMessage(binding.root, data?.message.toString())
    }

}