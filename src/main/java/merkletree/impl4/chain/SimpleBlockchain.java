package merkletree.impl4.chain;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class SimpleBlockchain<T extends Tx> {
	public static final int BLOCK_SIZE = 10;
	public List<Block<T>> blocks = new ArrayList<Block<T>>();

	public SimpleBlockchain() {
		// create genesis block
		blocks.add(newBlock());
	}

	public SimpleBlockchain(List<Block<T>> blocks) {
		this();
		this.blocks = blocks;
	}

	public Block<T> getHead() {

		Block<T> result = null;
		if (this.blocks.size() > 0) {
			result = this.blocks.get(this.blocks.size() - 1);
		} else {

			throw new RuntimeException("No Block's have been added to blocks...");
		}

		return result;
	}

	public void addAndValidateBlock(Block<T> block) {

		// compare previous block hash back to genesis hash
		Block<T> current = block;
		for (int i = blocks.size() - 1; i >= 0; i--) {
			Block<T> b = blocks.get(i);
			if (b.getHash().equals(current.getPreviousHash())) {
				current = b;
			} else {

				throw new RuntimeException("Block Invalid");
			}

		}

		this.blocks.add(block);

	}

	public boolean validate() {

		String previousBlockHash = blocks.get(0).getHash();
		for (Block<T> block : blocks) {
			String currentBlockHash = block.getHash();
			String currentBlockPreviousHash = "root".equals(block.getPreviousHash())? block.getHash():block.getPreviousHash();
			if (!currentBlockPreviousHash.equals(previousBlockHash)) {
				return false;
			}

			previousBlockHash = currentBlockHash;

		}

		return true;

	}

	public Block<T> newBlock() {
		int count = blocks.size();
		String previousHash = "root";

		if (count > 0)
			previousHash = blockChainHash();

		Block<T> block = new Block<T>();

		block.setTimeStamp(System.currentTimeMillis());
		block.setIndex(count);
		block.setPreviousHash(previousHash);
		return block;
	}

	public SimpleBlockchain<T> add(T item) {

		if (blocks.size() == 0) {
			// genesis block
			this.blocks.add(newBlock());
		}

		// See if head block is full
		if (getHead().getTransactions().size() >= BLOCK_SIZE) {
			this.blocks.add(newBlock());
		}

		getHead().add(item);

		return this;
	}

	/* Deletes the index of the after. */
	public void DeleteAfterIndex(int index) {
		if (index >= 0) {
			Predicate<Block<T>> predicate = b -> blocks.indexOf(b) >= index;
			blocks.removeIf(predicate);
		}
	}

	public SimpleBlockchain<T> cloneChain() {
		List<Block<T>> clonedChain = new ArrayList<Block<T>>();
		Consumer<Block> consumer = (b) -> clonedChain.add(b.cloneBlock());
		blocks.forEach(consumer);
		return new SimpleBlockchain<T>(clonedChain);
	}

	public List<Block<T>> getBlocks() {
		return blocks;
	}

	public void setBlocks(List<Block<T>> blocks) {
		this.blocks = blocks;
	}

	/* Gets the root hash. */
	public String blockChainHash() {
		return getHead().getHash();
	}

}