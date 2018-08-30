package merkletree.impl4.chain;

import merkletree.impl4.helpers.SHA256;

public class Transaction implements Tx {
	
	private String hash;
	private String value;
	
	public String hash() { return hash; }

	protected Transaction() {
	}

	public Transaction(String value) {
		this.hash = SHA256.generateHash(value);
		this.setValue(value);		
	}

	@Override
	public String value() {
		return getValue();
	}

	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		
		// new value need to recalc hash
		this.hash = SHA256.generateHash(value);	
		this.value = value;
	}

	protected void setValue(String value, Boolean ignoreHash) {
		if(!ignoreHash) {
			// new value need to recalc hash
			this.hash = SHA256.generateHash(value);
		}
		this.value = value;
	}

	protected void setHash(String hash) {
		this.hash = hash;
	}

	@Override
	public String toString() {
		return value() + "[" + hash() + "]";
	}

}
