package dev.lgawin.sandbox.slider

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.lgawin.sandbox.slider.ui.theme.AppTheme
import kotlin.reflect.KProperty

class MainActivity : ComponentActivity() {

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
                        var sliderValue: SliderValue by remember { mutableStateOf(SliderValue.Settled(0f)) }
                        val value by sliderValue
                        Text("Value: $sliderValue")
                        Slider(
                            value = value,
                            onValueChange = { sliderValue = SliderValue.Dragging(it) },
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            valueRange = 0f..42f,
                            onValueChangeFinished = { sliderValue = SliderValue.Settled(value) },
                        )
                    }
                }
            }
        }
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
