package merkletree.impl4.chain;

import com.google.gson.Gson;
import merkletree.impl4.helpers.SHA256;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Block<T extends Tx> {
	public long timeStamp;
	private int index;
	private List<T> transactions = new ArrayList<T>();
	private String hash;
	private String previousHash;
	private String merkleRoot;
	private String nonce = "0000";
	
	// caches Transaction SHA256 hashes
    public Map<String,T> map = new HashMap<String,T>();
    
	public Block<T> add(T tx) {
		transactions.add(tx);
		map.put(tx.hash(), tx);
		computeMerkleRoot();
		computeHash();
		return this;
	}
	

	public void computeMerkleRoot() {		
		List<String> treeList = merkleTree();
		// Last element is the merkle root hash if transactions
		setMerkleRoot(treeList.get(treeList.size()-1) );		
	}
	
	
	public Block<T> Clone() {
		// Object serialized then rehydrated into a new instance of an object so
		// memory conflicts don't happen
		// There are more efficent ways but this is the most reaadable
		Block<T> clone = new Block();
		clone.setIndex(this.getIndex());
		clone.setPreviousHash(this.getPreviousHash());
		clone.setMerkleRoot(this.getMerkleRoot());
		clone.setTimeStamp(this.getTimeStamp());
		//clone.setTransactions(this.getTransactions());
		
		List<T> clonedtx = new ArrayList<T>();
		Consumer<T> consumer = (t) -> clonedtx.add(t);
		this.getTransactions().forEach(consumer);
	    clone.setTransactions(clonedtx);
		
		return clone;
	}

	public boolean transasctionsValid()  {
		
		List<String> tree = merkleTree();
		String root = tree.get(tree.size() -1 );
		return root.equals(this.getMerkleRoot());
		
	}

	public List<String> merkleTree() {
		ArrayList<String> tree = new ArrayList<>();
		// add all transactions as leaves of the tree.
		for (T t : transactions) {
			tree.add(t.hash());
		}
		int levelOffset = 0; // first level
								
		// Iterate through each level, stopping when we reach the root (levelSize
		// == 1).
		for (int levelSize = transactions.size(); levelSize > 1; levelSize = (levelSize + 1) / 2) {
			// For each pair of nodes on that level:
			for (int left = 0; left < levelSize; left += 2) {
				// The right hand node can be the same as the left hand, in the
				// case where we don't have enough
				// transactions.
				int right = Math.min(left + 1, levelSize - 1);
				String tleft = tree.get(levelOffset + left);
				String tright = tree.get(levelOffset + right);
				tree.add(SHA256.generateHash(tleft + tright));
			}
			// Move to the next level.
			levelOffset += levelSize;
		}
		return tree;
	}

	public void computeHash() {
		  Gson parser = new Gson();
		  String serializedData = parser.toJson(transactions);
		  setHash(SHA256.generateHash(timeStamp + index + merkleRoot + serializedData + nonce + previousHash));
	}
	
	public String getHash() {
		
		// calc hash if not defined, just for testing...
		if (hash == null) {
		   computeHash();
		}
		
		return hash;
	}
	
	public void setHash(String h) {
		this.hash = h;	
	}
	
	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getPreviousHash() {
		return previousHash;
	}

	public void setPreviousHash(String previousHash) {
		this.previousHash = previousHash;
	}

	public List<T> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<T> transactions) {
		this.transactions = transactions;
	}
	
	public String getMerkleRoot() {
		return merkleRoot;
	}

	public void setMerkleRoot(String merkleRoot) {
		this.merkleRoot = merkleRoot;
	}
	
	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	
	
}
