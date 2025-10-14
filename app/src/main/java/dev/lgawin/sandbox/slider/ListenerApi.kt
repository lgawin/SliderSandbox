package dev.lgawin.sandbox.slider

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class ListenerApi {
    private var listener: ((Int) -> Unit)? = null

    fun setListener(listener: ((Int) -> Unit)?) {
        this.listener = listener
    }

    fun change(value: Int) {
        listener?.invoke(value)
    }
}

class ListenerApiControllerImpl(val api: ListenerApi = ListenerApi()) : SomeController {

    override val value = callbackFlow {
        api.setListener { value: Int ->
            trySend(value)
        }
        awaitClose {
            api.setListener(null)
        }
    }

    override suspend fun setValue(value: Int) {
        api.change(value)
    }
}
