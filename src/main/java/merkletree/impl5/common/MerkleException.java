package merkletree.impl5.common;

import org.springframework.stereotype.Component;

@Component
public class MerkleException extends RuntimeException {

    public MerkleException(String message) {
        super(message);
    }
}
