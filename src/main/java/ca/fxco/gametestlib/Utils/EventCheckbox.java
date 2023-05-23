package ca.fxco.gametestlib.Utils;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.network.chat.Component;

/**
 * Checkbox with a consumer when it changes state
 */
public class EventCheckbox extends Checkbox {

    private final BooleanConsumer consumer;

    public EventCheckbox(int x, int y, int width, int height, Component component,
                         boolean selected, BooleanConsumer consumer) {
        super(x, y, width, height, component, selected);
        this.consumer = consumer;
    }

    public EventCheckbox(int x, int y, int width, int height, Component component,
                         boolean selected, boolean showLabel, BooleanConsumer consumer) {
        super(x, y, width, height, component, selected, showLabel);
        this.consumer = consumer;
    }

    @Override
    public void onPress() {
        super.onPress();
        this.consumer.accept(this.selected());
    }
}
