package ru.wasiliysoft.simplemagazin

import android.app.Application
import com.google.android.material.color.DynamicColors
import ru.wasiliysoft.simplemagazin.data.prefs.PrefHelper

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
        PrefHelper.initPreferences(this)
    }
}