package merkletree.impl5.common;

@FunctionalInterface
public interface Condition<T> {
    T execute();
}
