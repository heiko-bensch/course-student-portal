package de.bensch.course.model.util;

@FunctionalInterface
public interface EntityLoader<T, ID> {
    T loadEntityById(ID id);
}
