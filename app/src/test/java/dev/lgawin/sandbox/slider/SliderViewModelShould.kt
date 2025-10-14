package dev.lgawin.sandbox.slider

import com.google.common.truth.Truth.assertThat
import dev.lgawin.testing.MainDispatcherExtension
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MainDispatcherExtension::class)
class SliderViewModelShould {

    @Test
    fun `send value to controller`() {
        val slot = slot<Int>()
        val controller = mockk<SomeController> {
            every { setValue(capture(slot)) } just Runs
        }
        val viewModel = SliderViewModel(controller = controller)

        viewModel.update(SliderValue.Dragging(3f))

        verify { controller.setValue(any()) }
        assertThat(slot.captured).isEqualTo(3)
    }
}
