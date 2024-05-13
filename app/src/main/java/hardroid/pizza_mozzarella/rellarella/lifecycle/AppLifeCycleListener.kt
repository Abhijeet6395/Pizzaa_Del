package hardroid.pizza_mozzarella.rellarella.lifecycle

import android.app.Activity
import android.app.Application
import android.os.Bundle
import hardroid.pizza_mozzarella.rellarella.App

class AppLifecycleListener : Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

    override fun onActivityStarted(activity: Activity) {}

    override fun onActivityResumed(activity: Activity) {}

    override fun onActivityPaused(activity: Activity) {
        saveChanges()
    }

    override fun onActivityStopped(activity: Activity) {
        saveChanges()
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {
        //saveChanges()
    }


    private fun saveChanges(){
        App.orders?.saveOrdersToDB()
    }

}