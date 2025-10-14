package dev.lgawin.sandbox.slider

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import dev.lgawin.sandbox.slider.ui.theme.AppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.reflect.KProperty

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<SliderViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(all = 24.dp),
                    ) {
                        Greeting(
                            name = "Android",
                            modifier = Modifier.padding(innerPadding),
                        )
                        val sliderValue by viewModel.value.collectAsStateWithLifecycle()
                        Text("Value: $sliderValue")
                        Slider(
                            value = sliderValue,
                            onValueChange = {
                                logd("slider.onValueChange($it)")
                                viewModel.update(SliderValue.Dragging(it))
                            },
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            valueRange = 0f..42f,
                            onValueChangeFinished = {
                                logd("slider.onValueChangeFinished()")
                                viewModel.update(SliderValue.Settled(sliderValue))
                            },
                        )
                    }
                }
            }
        }
    }
}

class SliderViewModel(
    private val controller: SomeController = SomeControllerImpl(),
) : ViewModel() {
    private val mutableValue = MutableStateFlow(0f)
    val value = mutableValue.asStateFlow()

    init {
        viewModelScope.launch {
            value.map { it.roundToInt() }
                .distinctUntilChanged()
                .collect(controller::setValue)
        }
        viewModelScope.launch {
            controller.value.collect {
                mutableValue.value = it.toFloat()
            }
        }
    }

    fun update(sliderValue: SliderValue) {
        mutableValue.update { sliderValue.value }
    }
}

interface SomeController {
    val value: Flow<Int>
    suspend fun setValue(value: Int)
}

class SomeControllerImpl : SomeController {

    val mutableValue = MutableStateFlow(0)
    override val value: StateFlow<Int> = mutableValue.asStateFlow()

    override suspend fun setValue(value: Int) {
        logi("setValue($value)")
        mutableValue.update { value }
    }
}

sealed interface SliderValue {
    val value: Float

    @JvmInline
    value class Dragging(override val value: Float) : SliderValue

    @JvmInline
    value class Settled(override val value: Float) : SliderValue
}

@Suppress("NOTHING_TO_INLINE")
inline operator fun SliderValue.getValue(thisObj: Any?, property: KProperty<*>): Float = value

val SliderValueSaver = object : Saver<SliderValue, Float> {
    override fun restore(value: Float): SliderValue = SliderValue.Settled(value)
    override fun SaverScope.save(value: SliderValue): Float = value.value
}

private fun logv(message: String) {
    Log.v("gawluk", message)
}

private fun logd(message: String) {
    Log.d("gawluk", message)
}

fun logi(message: String) {
    Log.i("gawluk", message)
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AppTheme {
        Greeting("Android")
    }
}
