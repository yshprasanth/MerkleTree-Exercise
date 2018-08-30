package merkletree.impl4.chain;

import merkletree.impl4.helpers.SHA256;

public class DerivedTransaction extends Transaction {

    private Tx leftTx;
    private Tx rightTx;

    private DerivedTransaction(Tx tx){
        super(tx.value());
        this.leftTx = null;
        this.rightTx = null;
    }

    public DerivedTransaction(Tx leftTx, Tx rightTx) {
        setValue(leftTx.value() + (rightTx!=null?rightTx.value():""), true);
        setHash(SHA256.generateHash(leftTx.hash() + (rightTx!=null?rightTx.hash():"")));
        this.leftTx = leftTx;
        this.rightTx = rightTx;
    }

    public static DerivedTransaction from(Tx tx) {
        return new DerivedTransaction(tx);
    }

    @Override
    public String value() {
        if(leftTx==null && rightTx==null)
            return super.value();
        else
            return leftTx.value() + (rightTx!=null?rightTx.value():"");
    }

    public Tx getLeftTx() {
        return leftTx;
    }

    public Tx getRightTx() {
        return rightTx;
    }
}
