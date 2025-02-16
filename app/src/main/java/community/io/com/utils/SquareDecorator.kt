package community.io.com.utils

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import community.io.com.R
import community.io.com.ui.EventActivity
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade


class SquareDecorator : DayViewDecorator{
    private var squareDrawable: Drawable? = null

    constructor(context: Context?) {
        // Create a square Drawable for the dates
//        squareDrawable = ContextCompat.getDrawable(context!!, R.drawable.square_drawable)
        squareDrawable = ContextCompat.getDrawable(context!!, R.drawable.defaullt_date_selector)
    }

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return true
    }

    override fun decorate(view: DayViewFacade) {
        view.setSelectionDrawable(squareDrawable!!)
//        squareDrawable?.let { view.setBackgroundDrawable(it) }
    }

}


//public class SquareDecorator implements DayViewDecorator {
//
//    public SquareDecorator(Context context) {
//        // Create a square Drawable for the dates
//        squareDrawable = ContextCompat.getDrawable(context, R.drawable.square_drawable);
//    }
//
//    @Override
//    public boolean shouldDecorate(CalendarDay day) {
//        // Decorate all dates
//        return true;
//    }
//
//    @Override
//    public void decorate(DayViewFacade view) {
//        // Set the square Drawable as the background for the dates
//        view.setBackgroundDrawable(squareDrawable);
//    }
//}

