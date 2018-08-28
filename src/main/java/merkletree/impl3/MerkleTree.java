package merkletree.impl3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by abhilashk.cse@gmail.com on 20-06-2016.
 * Simple implementation of Merkle Tree in Java using Generics
 */

public class MerkleTree<T> {

    private List<T> seed;
    private List<T> hash;

    public MerkleTree(List<T> seed) {
        this.seed = seed;
    }

    public T buildTree() {
        this.hash = getHashed(seed);

        while (hash.size() != 1) {
            hash = getHashed(hash);
        }

        return hash.get(0);
    }

    public List<T> getHashed(List<T> data) {

        List<T> temp = new ArrayList<T>();

        int i = 0;
        while (i < data.size()) {
            T left = data.get(i);
            i++;

            T right = null;
            if(i != data.size()) {
                right = data.get(i);
            }

            /* Code to demonstrate the use of Java 8 Lambda expression */
            Hash<T> hash = (l, r) -> {
                if(l instanceof Integer && r instanceof Integer)
                    return (T) (Integer) ((Integer)l + (Integer) r);
                else return null;
            };

            T hashed = hashing(left,right,hash);
            temp.add(hashed);
            i++;
        }
        return temp;
    }

    public T hashing(T left, T right, Hash<T> hash) {
        return hash.getHash(left,right);
    }

    public static void main(String args[]) {
        int low = 1;
        int high = 100;
        List<Integer> seedList = new ArrayList<Integer>();

        Random random = new Random();
        for(int i=0; i<4; i++) {
            seedList.add(random.nextInt(high - low) + low);
        }

        System.out.print("Seed Data: ");
        seedList.forEach( (val) -> System.out.print( val + ", "));
        System.out.println("");

        MerkleTree<Integer> merkleTree = new MerkleTree<Integer>(seedList);

        Integer merkleRoot = merkleTree.buildTree();
        System.out.println("Merkle Root is " + merkleRoot);
    }
}