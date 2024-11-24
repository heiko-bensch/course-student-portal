package de.bensch.course.model.util;

@FunctionalInterface
public interface EntityLoader<T, I> {
    T loadEntityById(I id);
}
