package ca.fxco.api.gametestlib.config;

import lombok.AllArgsConstructor;

import java.util.function.Consumer;
import java.util.function.Supplier;

@AllArgsConstructor
public class IndirectParsedValue<T> extends ParsedValue<T> {

    private final Consumer<T> setValueConsumer;
    private final Runnable setDefaultConsumer;
    private final Supplier<T[]> getTestingValuesSupplier;

    @Override
    public T[] getTestingValues() {
        return getTestingValuesSupplier.get();
    }

    @Override
    public void setValue(T value) {
        setValueConsumer.accept(value);
    }

    @Override
    public void setDefault() {
        setDefaultConsumer.run();
    }
}
