package merkletree.impl1;

import java.util.List;


public class Leaf
{
	// The data to be stored in this node
	private final List<byte[]> dataBlock;
	

	public Leaf(final List<byte[]> dataBlock)
	{
		this.dataBlock = dataBlock;
	}
	

	public List<byte[]> getDataBlock()
	{
		return (dataBlock);
	}


	private String toHexString(final byte[] array)
	{
		final StringBuilder str = new StringBuilder();
		
		str.append("[");
		
		boolean isFirst = true;
		for(int idx=0; idx<array.length; idx++)
		{
			final byte b = array[idx];
			
			if (isFirst)
			{			
				//str.append(Integer.toHexString(i));
				isFirst = false;
			}
			else
			{
				//str.append("," + Integer.toHexString(i));
				str.append(",");
			}
			
			final int hiVal = (b & 0xF0) >> 4;
	        final int loVal = b & 0x0F;
	        str.append((char) ('0' + (hiVal + (hiVal / 10 * 7))));
	        str.append((char) ('0' + (loVal + (loVal / 10 * 7))));
		}
		
		str.append("]");
		
		return(str.toString());
	}
	

	public String toString()
	{
		final StringBuilder str = new StringBuilder();
		
		for(byte[] block: dataBlock)
		{
			str.append(toHexString(block));
		}
		
		return(str.toString());
	}
	
}
