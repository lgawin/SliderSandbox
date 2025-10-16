package dev.gawluk.car.sandbox

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CarSpecificUi(modifier: Modifier = Modifier) {
    Box(modifier) {
        Text("This is only for car")
        // TODO
    }
}
