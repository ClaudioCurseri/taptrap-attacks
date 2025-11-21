package edu.hm.itsec.taptrapattackshowcase.notificationservice

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification

class NotificationListenerService : NotificationListenerService() {

    override fun onListenerConnected() {
        super.onListenerConnected()
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
    }
}