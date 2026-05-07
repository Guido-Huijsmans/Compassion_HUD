package uberpookie.compassion

import dev.isxander.yacl3.api.*
import dev.isxander.yacl3.api.controller.*
import dev.isxander.yacl3.dsl.binding
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import java.awt.Color

object CompassionHUDConfigScreen {

    private fun Int.toColor() = Color(this, true)
    private fun Color.toInt() = this.rgb

    fun create(parent: Screen?): Screen {
        val defaults = CompassionHUDConfig()
        var pending = CompassionHUDConfig.instance.copy()
        val tickStepPresets = listOf(5f, 10f, 11.25f, 22.5f)

        return YetAnotherConfigLib.createBuilder()
            .title(Component.literal("Compassion HUD"))
            .category(ConfigCategory.createBuilder()
                .name(Component.literal("General"))

                .option(Option.createBuilder<Int>()
                    .name(Component.translatable("compassion.config.bar_width"))
                    .description(OptionDescription.of(Component.translatable("compassion.config.bar_width.desc")))
                    .binding(defaults.barWidth, { pending.barWidth }, { pending = pending.copy(barWidth = it) })
                    .controller { opt -> IntegerSliderControllerBuilder.create(opt).range(64, 320).step(8) }
                    .build()
                )

                .option(Option.createBuilder<Color>()
                    .name(Component.translatable("compassion.config.text_color"))
                    .description(OptionDescription.of(Component.translatable("compassion.config.text_color.desc")))
                    .binding(
                        defaults.textColor.toColor(),
                        { pending.textColor.toColor() },
                        { pending = pending.copy(textColor = it.toInt()) }
                    )
                    .controller { opt -> ColorControllerBuilder.create(opt).allowAlpha(true) }
                    .build()
                )

                .option(Option.createBuilder<Float>()
                    .name(Component.translatable("compassion.config.degrees_shown"))
                    .description(OptionDescription.of(Component.translatable("compassion.config.degrees_shown.desc")))
                    .binding(defaults.degreesShown, { pending.degreesShown }, { pending = pending.copy(degreesShown = it) })
                    .controller { opt -> FloatSliderControllerBuilder.create(opt).range(45f, 180f).step(5f).valueFormatter { value -> Component.literal("%.0f°".format(value)) } }
                    .build()
                )

                .option(Option.createBuilder<Float>()
                    .name(Component.translatable("compassion.config.tick_step"))
                    .description(OptionDescription.of(Component.translatable("compassion.config.tick_step.desc")))
                    .binding(
                        tickStepPresets.indexOf(defaults.tickStep).toFloat(),               // ← index e.g. 2
                        { tickStepPresets.indexOf(pending.tickStep).toFloat().coerceAtLeast(0f) }, // ← index e.g. 2
                        { newValue ->
                            val index = newValue.toInt().coerceIn(0, tickStepPresets.size - 1)
                            pending = pending.copy(tickStep = tickStepPresets[index])
                        }
                    )
                    .controller { opt ->
                        FloatSliderControllerBuilder.create(opt)
                            .range(0f, (tickStepPresets.size - 1).toFloat())
                            .step(1f)
                            .formatValue { valIndex ->
                                Component.literal(tickStepPresets[valIndex.toInt()].toString() + "°")
                            }
                    }
                    .build()
                )

                .option(Option.createBuilder<Boolean>()
                    .name(Component.translatable("compassion.config.hide_on_debug"))
                    .description(OptionDescription.of(Component.translatable("compassion.config.hide_on_debug.desc")))
                    .binding(defaults.hideOnDebug, { pending.hideOnDebug }, { pending = pending.copy(hideOnDebug = it) })
                    .controller(TickBoxControllerBuilder::create)
                    .build()
                )

                .build()
            )
            .save { CompassionHUDConfig.save(pending) }  // called when user clicks Done
            .build()
            .generateScreen(parent)
    }
}