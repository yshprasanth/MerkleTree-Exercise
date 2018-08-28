package merkletree.impl3;

public interface Hash<T> {
    /* The implementation class will implement this method using Java 8 Lambda expression.
     * If we don't want to use Lambda expression we can convert this method to default method.
     * */
    T getHash(T left, T right);
}