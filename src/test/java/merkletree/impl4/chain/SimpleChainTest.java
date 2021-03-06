package merkletree.impl4.chain;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SimpleChainTest {

	@Test
	public void testBlockchain() {

		SimpleBlockchain<Transaction> chain1 = new SimpleBlockchain<Transaction>();

		chain1.add(new Transaction("A"))
			.add(new Transaction("B"))
			.add(new Transaction("C"))
			.add(new Transaction("C1"))
			.add(new Transaction("C2"))
			.add(new Transaction("C3"))
			.add(new Transaction("C4"))
			.add(new Transaction("C5"))
			.add(new Transaction("C6"))
			.add(new Transaction("C7"))
			.add(new Transaction("C8"))
			.add(new Transaction("C11"))
			.add(new Transaction("C12"))
			.add(new Transaction("C13"))
			.add(new Transaction("C14"))
			.add(new Transaction("C15"))
			.add(new Transaction("C16"))
			.add(new Transaction("C17"))
			.add(new Transaction("C18"))
			.add(new Transaction("C21"))
			.add(new Transaction("C22"))
			.add(new Transaction("C23"))
			.add(new Transaction("C24"))
			.add(new Transaction("C25"))
			.add(new Transaction("C26"))
			.add(new Transaction("C27"))
			.add(new Transaction("C28"));

		SimpleBlockchain<Transaction> chain2 = chain1.cloneChain();

		System.out.println(String.format("After Cloning, Chain 1 Hash: %s", chain1.getHead().getHash()));
		System.out.println(String.format("After Cloning, Chain 2 Hash: %s", chain2.getHead().getHash()));
		System.out.println(
				String.format("After Cloning, Chains Are In Sync: %s", chain1.getHead().getHash().equals(chain2.getHead().getHash())));

		chain1.add(new Transaction("D"));

		System.out.println(String.format("After adding a new txn to Chain 1, Chain 1 Hash: %s", chain1.getHead().getHash()));
		System.out.println(String.format("After adding a new txn to Chain 1, Chain 2 Hash: %s", chain2.getHead().getHash()));
		System.out.println(
				String.format("After adding a new txn to Chain 1, Chains Are In Sync: %s", chain1.getHead().getHash().equals(chain2.getHead().getHash())));

		chain2.add(new Transaction("D"));

		System.out.println(String.format("After adding a new txn to Chain 2, Chain 1 Hash: %s", chain1.getHead().getHash()));
		System.out.println(String.format("After adding a new txn to Chain 2, Chain 2 Hash: %s", chain2.getHead().getHash()));
		System.out.println(
				String.format("After adding a new txn to Chain 2, Chains Are In Sync: %s", chain1.getHead().getHash().equals(chain2.getHead().getHash())));

		assertTrue(chain1.blockChainHash().equals(chain2.blockChainHash()));

		System.out.println("Current Chain 1 Head Transactions: ");
		for (Block block : chain1.blocks) {
			for (Object tx : block.getTransactions()) {
				System.out.println("\t" + tx);
			}
		}

		// Block Merkle root should equal root hash in Merkle Tree computed from
		// block transactions
		Block headBlock = chain1.getHead();
		assertTrue(headBlock.getMerkleRoot().equals(headBlock.getMerkleRootTxn().hash()));

		// Validate block blocks
		assertTrue(chain1.validate());
		System.out.println(String.format("Chain is Valid: %s", chain1.validate()));

	}

	@Test
	public void merkleTreeTest() {

		// create blocks, add transaction

		SimpleBlockchain<Transaction> chain1 = new SimpleBlockchain<Transaction>();

		chain1.add(new Transaction("A")).add(new Transaction("B")).add(new Transaction("C")).add(new Transaction("D"));

		// get a block in blocks
		Block<Transaction> block = chain1.getHead();

		// get a transaction from block
		Transaction tx = block.getTransactions().get(0);

		// see if block transactions are valid, they should be
		block.transasctionsValid();
		assertTrue(block.transasctionsValid());

		// mutate the data of a transaction
		tx.setValue("Z");

		// block should no longer be valid, blocks MerkleRoot does not match computed merkle tree of transactions
		assertFalse(block.transasctionsValid());

	}

	@Test
	public void blockMinerTest() {

		// create 30 transactions, that should result in 3 blocks in the blocks.
		SimpleBlockchain<Transaction> chain = new SimpleBlockchain<Transaction>();

		// Respresents a proof of work miner
		// Creates
		Miner miner = new Miner(chain);

		// This represents transactions being created by a network
		for (int i = 0; i < 30; i++) {
			miner.mine(new Transaction("" + i));
		}

		System.out.println("Number of Blocks   = " + chain.getBlocks().size());
		assertTrue(chain.getBlocks().size() == 3);

	}

	@Test
	public void testValidateBlockchain() {

		SimpleBlockchain<Transaction> chain = new SimpleBlockchain<Transaction>();	
		// add 30 transaction should result in 3 blocks in blocks.
		for (int i = 0; i < 30 ; i++) {
	   	       chain.add(new Transaction("tx:"+i));
		}
		
		// is blocks valid
		System.out.println(String.format("Chain is Valid: %s", chain.validate()));

        // get second block from blocks and add a tx..
		Block<Transaction> block = chain.getBlocks().get(1);
		Transaction tx = new Transaction("X");
		block.add(tx);
		
		// is blocks valid, should not be changed a block...
		System.out.println(String.format("Chain is Valid: %s", chain.validate()));
	
	
	}

}
