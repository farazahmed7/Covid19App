package coronago.coronalive.utility

import android.util.Log
import com.github.mikephil.charting.formatter.ValueFormatter

class MyValueFormatter : ValueFormatter() {

    override fun getFormattedValue(value: Float): String {
        Log.d("value",value.toString())
        return "123"
    }
}