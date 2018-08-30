package merkletree.impl4.ui;


import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import merkletree.impl4.chain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class MerkleTreeView extends Application {

//    private final Node rootIcon =
//            new ImageView(new Image(getClass().getResourceAsStream("root.png")));
//    private final Image depIcon =
//            new Image(getClass().getResourceAsStream("department.png"));
    SimpleBlockchain<Tx> simpleBlockchain = new SimpleBlockchain<Tx>();
    VBox vBox = new VBox(5);
    GridPane buttonsGridPane = new GridPane();
    GridPane addNewGridPane = new GridPane();
    GridPane headerGridPane = new GridPane();
    HBox hBox = new HBox(10);

    StackPane treeViewStackPane = new StackPane();
    TreeView<String> treeView = new TreeView<>();
    TreeItem<String> rootTreeItem = new TreeItem<>("Merkle Tree");;

    Label blocksTxtLbl = new Label("Number of Blocks:");
    Label blocksValLbl = new Label("");
    Label txnsTxtLbl = new Label("Total Number of Txns in all Blocks:");
    Label txnsValLbl = new Label("");
    Label noteTxtLbl = new Label("Number of Txns allowed in a Block:");
    Label noteValLbl = new Label(String.valueOf(SimpleBlockchain.BLOCK_SIZE));

    public static void main(String[] args) {
        launch(args);
    }

    public MerkleTreeView() {
        simpleBlockchain
            .add(new Transaction("A"))
            .add(new Transaction("B"))
            .add(new Transaction("C"))
            .add(new Transaction("D"))
            .add(new Transaction("E"))
            .add(new Transaction("F"))
            .add(new Transaction("G"))
            .add(new Transaction("H"))
            .add(new Transaction("I"))
            .add(new Transaction("J"))
            .add(new Transaction("K"))
            .add(new Transaction("L"))
            .add(new Transaction("M"))
            .add(new Transaction("N"))
            .add(new Transaction("O"))
            .add(new Transaction("P"));
    }

    private void traverseTxn(DerivedTransaction dtxn, TreeItem<String> parentNode){
        traverseChildTxn(dtxn.getLeftTx(), parentNode);
        traverseChildTxn(dtxn.getRightTx(), parentNode);
    }

    private void traverseChildTxn(Tx tx, TreeItem<String> parentNode){
        if(tx!=null) {
            String prefix = "";
            if(tx instanceof DerivedTransaction) {
                prefix = "Derived: ";
                TreeItem<String> childNode = new TreeItem(prefix + tx);
                parentNode.getChildren().add(childNode);
                traverseTxn((DerivedTransaction)tx, childNode);
            } else if(tx instanceof Transaction) {
                prefix = "Txn: ";
                parentNode.getChildren().add(new TreeItem(prefix + tx));
            }
        }
    }

    private void getMerkleTreeView() {
        rootTreeItem.getChildren().clear();

        List<DerivedTransaction> blockRootTxns = new ArrayList<>();
        simpleBlockchain.blocks
                        .forEach(block ->
                                blockRootTxns.add((DerivedTransaction)block.getMerkleRootTxn()));

        blockRootTxns.forEach(txn -> {
                    TreeItem<String> blockRootTree = new TreeItem("Block: " + txn);
                    traverseTxn(txn, blockRootTree);
                    rootTreeItem.getChildren().add(blockRootTree);

                });

    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Simple Block Chain - Merkle Tree");

        setDefaultProperties();
        addNewBlockPane();
        addContent();

        final Scene scene = new Scene(vBox, 700, 520);
        scene.setFill(Color.LIGHTGRAY);
        stage.setScene(scene);
        stage.show();
    }

    private void setDefaultProperties() {
        headerGridPane.setPadding(new Insets(5));
        headerGridPane.setHgap(5);
        headerGridPane.setVgap(5);
        ColumnConstraints column1 = new ColumnConstraints(300);
        ColumnConstraints column2 = new ColumnConstraints(50, 150, 300);
        column2.setHgrow(Priority.ALWAYS);
        headerGridPane.getColumnConstraints().addAll(column1, column2);
        headerGridPane.add(blocksTxtLbl, 0, 0);
        headerGridPane.add(txnsTxtLbl, 0, 1);
        headerGridPane.add(noteTxtLbl, 0, 2);
        headerGridPane.add(noteValLbl, 1, 2);
        GridPane.setHalignment(blocksTxtLbl, HPos.RIGHT);
        GridPane.setHalignment(txnsTxtLbl, HPos.RIGHT);
        GridPane.setHalignment(noteTxtLbl, HPos.RIGHT);
        GridPane.setHalignment(noteValLbl, HPos.LEFT);
        headerGridPane.setStyle("-fx-padding: 10;" + "-fx-border-style: solid inside;"
                + "-fx-border-width: 2;" + "-fx-border-insets: 5;"
                + "-fx-border-radius: 5;" + "-fx-border-color: darkblue;");

        buttonsGridPane.setPadding(new Insets(5));
        buttonsGridPane.setHgap(5);
        buttonsGridPane.setVgap(5);

        hBox.setAlignment(Pos.CENTER);
        hBox.setStyle("-fx-padding: 5;" + "-fx-border-style: solid inside;"
                + "-fx-border-width: 2;" + "-fx-border-insets: 5;"
                + "-fx-border-radius: 5;" + "-fx-border-color: darkblue;");

        rootTreeItem.setExpanded(true);
        treeView.setRoot(rootTreeItem);
        treeViewStackPane.getChildren().add(treeView);
        treeViewStackPane.setMaxHeight(300);
    }

    private void addContent() {
        vBox.getChildren().clear();
        hBox.getChildren().clear();

        getHeaderPane();
        getBlocksPane();
        getMerkleTreeView();

        hBox.getChildren().addAll(buttonsGridPane, addNewGridPane);
        vBox.getChildren().addAll(headerGridPane, hBox, treeViewStackPane);
    }

    private void getHeaderPane() {
        headerGridPane.getChildren().removeAll(blocksValLbl, txnsValLbl);

        blocksValLbl = new Label(String.valueOf(simpleBlockchain.getBlocks().size()));
        txnsValLbl = new Label(String.valueOf(simpleBlockchain.getBlocks().stream().flatMapToInt(b -> IntStream.of(b.getTransactions().size())).sum()));

        GridPane.setHalignment(blocksValLbl, HPos.LEFT);
        GridPane.setHalignment(txnsValLbl, HPos.LEFT);

        headerGridPane.add(blocksValLbl, 1, 0);
        headerGridPane.add(txnsValLbl, 1, 1);
    }

    private void getBlocksPane() {
        buttonsGridPane.getChildren().clear();

        int rowIndex = 0;
        for(Block<Tx> block : simpleBlockchain.blocks) {
            int colIndex = 0;

            Button blockBtn = new Button("Block #" + (rowIndex+1));
            blockBtn.setTooltip(new Tooltip(block.getPreviousHash() + " + " + block.getHash() + " + " + block.getMerkleRoot()));
            blockBtn.setStyle("-fx-background-color: darkblue; -fx-text-fill: #ffffff;");

            buttonsGridPane.add(blockBtn, colIndex, rowIndex);
            for(Tx tx : block.getTransactions()) {
                Button txnBtn = new Button(tx.value());
                txnBtn.setTooltip(new Tooltip(tx.hash()));
                buttonsGridPane.add(txnBtn, ++colIndex, rowIndex);
            }
            rowIndex++;
        }
    }

    private void addNewBlockPane() {
        addNewGridPane.getChildren().clear();
        addNewGridPane.setPadding(new Insets(5));
        addNewGridPane.setHgap(5);
        addNewGridPane.setVgap(5);

        ColumnConstraints column1 = new ColumnConstraints(100);
        ColumnConstraints column2 = new ColumnConstraints(50, 50, 100);
        column2.setHgrow(Priority.ALWAYS);

        addNewGridPane.getColumnConstraints().addAll(column1, column2);

        Label newTxnLbl = new Label("Txn value:");
        TextField newTxnTxt = new TextField();
        newTxnTxt.setPrefColumnCount(10);
        GridPane.setHalignment(newTxnLbl, HPos.RIGHT);
        GridPane.setHalignment(newTxnTxt, HPos.LEFT);

        addNewGridPane.add(newTxnLbl, 0, 0);
        addNewGridPane.add(newTxnTxt, 1, 0);

        Button addBtn = new Button("Add New Txn");
        addBtn.setTooltip(new Tooltip("Add New Txn to Block"));
        addBtn.setId("add-button");
        addBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        addNewGridPane.add(addBtn, 0, 1);
        addBtn.setOnAction(v -> {
            simpleBlockchain.add(new Transaction(newTxnTxt.getText()));
            addContent();
        });

        GridPane.setColumnSpan(addBtn, 2);
        GridPane.setHgrow(addBtn, Priority.ALWAYS);
    }
}