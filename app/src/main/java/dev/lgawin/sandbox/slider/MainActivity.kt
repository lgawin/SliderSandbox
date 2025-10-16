package dev.lgawin.sandbox.slider

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import dev.gawluk.car.sandbox.CarSpecificUi
import dev.gawluk.car.sandbox.installCarFeature
import dev.lgawin.sandbox.slider.ui.theme.AppTheme
import dev.lgawin.utils.isAutomotive
import kotlinx.coroutines.flow.MutableStateFlow
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

        if (isAutomotive) {
            installCarFeature()
        }

        setContent {
            AppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(all = 24.dp),
                    ) {
                        val viewModelValue by viewModel.value.collectAsStateWithLifecycle()
                        Greeting(
                            name = "Android",
                            modifier = Modifier.padding(innerPadding),
                        )
                        if (isAutomotive) {
                            CarSpecificUi()
                        }
                        var sliderValue by remember(viewModelValue) {
                            mutableFloatStateOf(
                                viewModelValue
                            )
                        }
                        Text("Value: $sliderValue")
                        Slider(
                            value = sliderValue,
                            onValueChange = {
                                sliderValue = it
                                logd("slider.onValueChange($it)")
                                viewModel.update(SliderValue.Dragging(it))
                            },
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            valueRange = 0f..42f,
                            onValueChangeFinished = {
                                logd("slider.onValueChangeFinished($sliderValue)")
                                viewModel.update(SliderValue.Settled(sliderValue))
                            },
                        )
                        var showDialog by rememberSaveable { mutableStateOf(false) }
                        AnimatedContent(
                            showDialog,
                            transitionSpec = {
                                fadeIn(
                                    animationSpec = tween(durationMillis = 300, delayMillis = 300)
                                ) togetherWith fadeOut(animationSpec = tween(300))
                            },
                        ) { isDialogShown ->
                            if (isDialogShown) {
                                DirectInputDialog(
                                    onSubmit = { viewModel.update(SliderValue.Settled(it.toFloat())) },
                                    onDismissRequest = { -> showDialog = false })
                            } else {
                                Button(
                                    onClick = { showDialog = !showDialog },
                                    modifier = Modifier.align(Alignment.End),
                                ) {
                                    Text("Direct Input")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DirectInputDialog(
    onSubmit: (Int) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 32.dp),
            ) {
                var input by remember { mutableStateOf("") }
                val focusRequester = remember { FocusRequester() }
                TextField(
                    value = input,
                    onValueChange = { input = it },
                    modifier = Modifier.focusRequester(focusRequester),
                    label = { Text("Input integer value") },
                )
                LaunchedEffect(Unit) {
                    focusRequester.requestFocus()
                }
                val parsedToInt by remember { derivedStateOf { input.toIntOrNull() } }
                Spacer(
                    modifier = Modifier
                        .heightIn(min = 16.dp)
                        .weight(1f),
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Cancel")
                    }
                    TextButton(
                        onClick = {
                            parsedToInt?.let { onSubmit(it) }
                            input = ""
                            onDismissRequest()
                        },
                        modifier = Modifier.padding(8.dp),
                        enabled = parsedToInt != null,
                    ) {
                        Text("Submit")
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
            value.map { it.roundToInt() }.distinctUntilChanged().collect(controller::setValue)
        }
    }

    fun update(sliderValue: SliderValue) {
        mutableValue.update { sliderValue.value }
    }
}

interface SomeController {
    suspend fun setValue(value: Int)
}

class SomeControllerImpl : SomeController {
    override suspend fun setValue(value: Int) {
        logi("setValue($value)")
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

private fun logi(message: String) {
    Log.i("gawluk", message)
}

private fun logw(t: Throwable) {
    Log.w("gawluk", t)
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
