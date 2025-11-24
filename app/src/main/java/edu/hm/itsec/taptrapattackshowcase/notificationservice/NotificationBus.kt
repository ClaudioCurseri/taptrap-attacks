package edu.hm.itsec.taptrapattackshowcase.notificationservice

import android.service.notification.StatusBarNotification
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

object NotificationBus {
    // tracks if the listener is active
    private val _isConnected = MutableStateFlow(false)
    val isConnected = _isConnected.asStateFlow()

    // emits new notifications as they arrive
    private val _notifications = MutableSharedFlow<StatusBarNotification>()
    val notifications = _notifications.asSharedFlow()

    fun setConnected(connected: Boolean) {
        _isConnected.value = connected
    }

    suspend fun postNotification(sbn: StatusBarNotification) {
        _notifications.emit(sbn)
    }
}