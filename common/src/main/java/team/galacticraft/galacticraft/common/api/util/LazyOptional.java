package team.galacticraft.galacticraft.common.api.util;

import org.jetbrains.annotations.Nullable;

public class LazyOptional<T> {
    private final Provider<T> provider;
    private static final LazyOptional<?> EMPTY = new LazyOptional<>(null);

    private LazyOptional(Provider<T> provider) {
        this.provider = provider;
    }

    public static <T> LazyOptional<T> create(@Nullable Provider<T> provider) {
        if (provider == null) return (LazyOptional<T>) EMPTY;
        return new LazyOptional<>(provider);
    }

    public boolean isPresent() {
        return this.provider != null;
    }

    public T orElse(T other) {
        if (!isPresent()) return other;
        return provider.apply();
    }

    public <t extends Throwable> T orElseThrow(t throwable) throws t {
        if (!isPresent()) throw throwable;
        return provider.apply();
    }

    public T get() {
        return this.provider.apply();
    }

    @FunctionalInterface
    public interface Provider<T> {
        T apply();
    }
}
