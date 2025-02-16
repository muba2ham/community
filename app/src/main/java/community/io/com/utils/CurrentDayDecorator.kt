package community.io.com.utils



import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.Log
import community.io.com.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

//import com.prolificinteractive.materialcalendarview.DayViewFacade

//class CurrentDayDecorator(context: Activity?, currentDay: CalendarDay, drawable: Drawable?) : DayViewDecorator {
class CurrentDayDecorator : DayViewDecorator {
    private var drawable: Drawable? = null
    private var drawable1: Drawable? = null
    var myDay: CalendarDay? = null

    //    private var highlightDrawable: Drawable? = null
    private var context: Context? = null
    private var dates: HashSet<CalendarDay>? = null


    constructor(context: Activity?, currentDay: CalendarDay, drawable: Drawable?) {
        this.drawable = drawable!!
        myDay = currentDay

        this.context = context

//        highlightDrawable = this.context?.getResources()?.getDrawable(R.drawable.first_day_month)
    }

    constructor(mContext: Context?, mEventDays: MutableList<CalendarDay>) {
        this.context = mContext
        drawable = this.context?.getResources()?.getDrawable(R.drawable.first_day_month)
        drawable1 = this.context?.getResources()?.getDrawable(R.drawable.defaullt_date_selector)
//        highlightDrawable = this.context?.getResources()?.getDrawable(R.drawable.first_day_month)
        dates = HashSet(mEventDays)



    }


    override fun shouldDecorate(day: CalendarDay): Boolean {
        if (dates != null && dates!!.isNotEmpty()) {
//            Log.e("myShouldOveRideDate===>", "${day}")
//            Log.e("myShouldOveRideDate===>", "${dates!!.contains(day)}")
//            for (i in 0 until dates!!.size) {
//            var isDayAvailable = false;
//            for (s in dates!!) {
//                Log.e("myShouldOveRideDate11===>", "${s}")
//                 if (s.equals(day)) {
//                     isDayAvailable =    true
//                     break
//                }
////                 else {
////                    false
////                }
//            }
            if (dates!!.contains(day)) { // check if 'day' is in mapOfCalendarDays (a map of CalendarDay to Event)
                myDay = day!!

            }

//            return isDayAvailable
            return dates!!.contains(day) == true
//        return dates?.contains(day) == true
        }
        return day == myDay
    }

    override fun decorate(view: DayViewFacade) {
//        Log.e("MydayDecoratecaled", myDay.toString())
        view.setSelectionDrawable(drawable!!)
//        view.addSpan(ForegroundColorSpan(Color.RED))
//        view.addSpan(StyleSpan(Typeface.BOLD))
//        view.addSpan(RelativeSizeSpan(1.1f))
    }

    //    init {
//        // You can set background for Decorator via drawable here
//        drawable = ContextCompat.getDrawable(context!!, R.drawable.first_day_month)
//    }
}

//class CurrentDayDecorator : DayViewDecorator {
//    private var drawable: Drawable? = null
//    var myDay: CalendarDay? = null
//
//    //    private var highlightDrawable: Drawable? = null
//    private var context: Context? = null
//    private var dates: HashSet<CalendarDay>? = null
//
//
//    constructor(context: Activity?, currentDay: CalendarDay, drawable: Drawable?) {
//        this.drawable = drawable!!
//        myDay = currentDay
//
//        this.context = context
//
////        highlightDrawable = this.context?.getResources()?.getDrawable(R.drawable.first_day_month)
//    }
//
//
//    override fun shouldDecorate(day: CalendarDay): Boolean {
//        return day == myDay
//    }
//
//    override fun decorate(view: DayViewFacade) {
////        Log.e("MydayDecoratecaled", myDay.toString())
//        view.setSelectionDrawable(drawable!!)
////        view.addSpan(ForegroundColorSpan(Color.RED))
////        view.addSpan(StyleSpan(Typeface.BOLD))
////        view.addSpan(RelativeSizeSpan(1.1f))
//    }
//
//    //    init {
////        // You can set background for Decorator via drawable here
////        drawable = ContextCompat.getDrawable(context!!, R.drawable.first_day_month)
////    }
//}