package edu.hm.itsec.taptrapattackshowcase.notificationservice

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationListenerService : NotificationListenerService() {

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onListenerConnected() {
        super.onListenerConnected()
        // broadcast: the listener is connected
        NotificationBus.setConnected(true)
        // post all current notifications after connecting
        activeNotifications.forEach { sbn ->
            scope.launch { NotificationBus.postNotification(sbn) }
        }
    }

    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
        NotificationBus.setConnected(false)
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        scope.launch {
            NotificationBus.postNotification(sbn)
        }
    }
}