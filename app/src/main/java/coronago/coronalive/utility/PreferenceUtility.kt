package coronago.coronalive.utility

import android.content.Context

class PreferenceUtility(val context: Context) {

    companion object {
        const val KEY = "CONFIRMED"
        const val PREFERENCENAME="PREFNAME"
        const val INTARRAY="intarray"
        const val DATE="DATE"
    }

    internal fun saveConfirmedPref(value: String) {
        val prefs = context.getSharedPreferences(PREFERENCENAME, 0).edit()
        prefs.putString(KEY, value)
        prefs.apply()
    }

    internal fun saveDatePref(value: String) {
        val prefs = context.getSharedPreferences(PREFERENCENAME, 0).edit()
        prefs.putString(DATE, value)
        prefs.apply()
    }

    internal fun loadConfirmedPref(): String? {
        val prefs = context.getSharedPreferences(PREFERENCENAME, 0)
        return prefs.getString(KEY, "")
    }
}