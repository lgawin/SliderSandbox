package dev.lgawin.sandbox.slider

import com.google.common.truth.Truth.assertThat
import dev.lgawin.testing.MainDispatcherExtension
import io.mockk.CapturingSlot
import io.mockk.Runs
import io.mockk.clearMocks
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

@ExtendWith(MainDispatcherExtension::class)
class SliderViewModelShould {

    private lateinit var slot: CapturingSlot<Int>
    private lateinit var controller: SomeController
    private lateinit var viewModel: SliderViewModel

    @BeforeEach
    fun setUp() {
        slot = slot<Int>()
        controller = mockk<SomeController> {
            every { setValue(capture(slot)) } just Runs
        }
        viewModel = SliderViewModel(controller = controller)
        // clear initial setValue(0) (?)
        clearMocks(controller, answers = false)
    }

    @AfterEach
    fun tearDown() {
        confirmVerified(controller)
    }

    @Test
    fun `send value to controller`() {
        viewModel.update(SliderValue.Dragging(3f))

        verify { controller.setValue(any()) }
        assertThat(slot.captured).isEqualTo(3)
    }

    @ParameterizedTest
    @CsvSource(
        "1.2,1",
        "1.5,2",
        "2.29,2",
    )
    fun `send integer value to controller with proper rounding`(value: Float, expected: Int) {
        viewModel.update(SliderValue.Dragging(value))

        verify { controller.setValue(expected) }
    }

    @Test
    fun `not call controller if value is not changed`() {
        viewModel.update(SliderValue.Dragging(1.3f))
        viewModel.update(SliderValue.Dragging(1.4f))
        viewModel.update(SliderValue.Dragging(1.6f))
        viewModel.update(SliderValue.Dragging(1.8f))
        viewModel.update(SliderValue.Dragging(2.1f))

        verify(exactly = 1) {
            controller.setValue(1)
        }
        verify(exactly = 1) {
            controller.setValue(2)
        }
    }
}
