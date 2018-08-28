package merkletree.impl3;

public interface Hash<T> {

    T getHash(T left, T right);
}