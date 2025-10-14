package dev.lgawin.sandbox.slider

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ModernApi {
    private val mutableValue = MutableStateFlow(0)
    val value = mutableValue.asStateFlow()

    suspend fun update(value: Int) {
        delay(2_000)
        mutableValue.update { value }
    }
}

class ModernApiControllerImpl(val api: ModernApi = ModernApi()) : SomeController {

    override val value = api.value

    override suspend fun setValue(value: Int) {
        logi("setValue: $value...")
        api.update(value)
        logi("setValue: done")
    }
}
