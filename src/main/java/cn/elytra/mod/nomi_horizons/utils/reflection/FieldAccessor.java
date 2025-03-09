package cn.elytra.mod.nomi_horizons.utils.reflection;

public interface FieldAccessor<T> {
    T get(Object self);

    void set(Object self, T value);
}
