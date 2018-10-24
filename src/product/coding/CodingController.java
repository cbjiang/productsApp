package product.coding;

import com.alibaba.fastjson.JSONArray;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import product.base.BaseController;
import product.utils.ExcelUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Created by cbjiang on 2018/10/17.
 */
public class CodingController extends BaseController {

    @FXML
    private Button importStocks;

    @FXML
    private Button startTaking;

    @FXML
    private Button importPiece;

    @FXML
    private Button importBox;

    @FXML
    private Button undo;

    @FXML
    private Button saveTaking;

    @FXML
    private Button giveUpTaking;

    @FXML
    private Button exportStocks;

    @FXML
    private TextField perBoxCount;

    @FXML
    private CheckBox useNameSpec;

    @FXML
    private Label totalLabel;

    @FXML
    private TableView<CodingModel> takingTable;
    @FXML
    private TableColumn<CodingModel, String> barColumn;
    @FXML
    private TableColumn<CodingModel, String> codeColumn;
    @FXML
    private TableColumn<CodingModel, String> nameColumn;
    @FXML
    private TableColumn<CodingModel, String> specColumn;
    @FXML
    private TableColumn<CodingModel, String> brandColumn;
    @FXML
    private TableColumn<CodingModel, String> typeColumn;
    @FXML
    private TableColumn<CodingModel, Integer> realityColumn;
    @FXML
    private TableColumn<CodingModel, Integer> lockedColumn;
    @FXML
    private TableColumn<CodingModel, Integer> availableColumn;
    @FXML
    private TableColumn<CodingModel, Integer> wayColumn;
    @FXML
    private TableColumn<CodingModel, Integer> takingColumn;
    @FXML
    private TableColumn<CodingModel, Integer> diffColumn;

    private List<CodingModel> stockData;

    private String TOTAL_LABEL_TEXT_PRE="总条数：";

    @FXML
    private void initialize() {
        startTaking.setDisable(true);
        importPiece.setDisable(true);
        importBox.setDisable(true);
        undo.setDisable(true);
        saveTaking.setDisable(true);
        giveUpTaking.setDisable(true);
        exportStocks.setDisable(true);
    }

    @FXML
    private void handleImportStocks(){
        System.out.println("import stocks");
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("选择Excel文件");
            Stage selectFile = new Stage();
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XLS", "*.xls"));
            File file = fileChooser.showOpenDialog(selectFile);
            if (file != null) {
                try {
                    HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(file));
                    JSONArray data = ExcelUtil.importExcel(wb);

                    System.out.println(data);

                    if (CodingUtils.checkHeader(data.getJSONArray(0).getJSONObject(0), CodingUtils.ACTUALITY_TABLE_HEADER)) {
                        String[] keys = new String[]{
                                "0", "1", "2", "3", "5", "6", "7", "8", "9", "10", "$", "$"
                        };
                        data.getJSONArray(0).remove(0);
                        JSONArray dataArr = data.getJSONArray(0);
                        stockData = CodingUtils.jsonArray2List(dataArr, keys);
                        loadData(stockData);
                    } else {
                        throw new Exception("表头不匹配！");
                    }

                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            importPiece.setDisable(true);
            importBox.setDisable(true);
            undo.setDisable(true);
            saveTaking.setDisable(true);
            giveUpTaking.setDisable(true);

            importStocks.setDisable(false);
            startTaking.setDisable(false);
            exportStocks.setDisable(false);
        }catch (Exception e){
            e.printStackTrace();
            CodingUtils.alert("错误", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleStartTaking(){
        System.out.println("start coding");

        loadData(new LinkedList<>());

        importPiece.setDisable(false);
        importBox.setDisable(false);
        undo.setDisable(true);
        saveTaking.setDisable(false);
        giveUpTaking.setDisable(false);

        importStocks.setDisable(true);
        startTaking.setDisable(true);
        exportStocks.setDisable(true);
    }

    @FXML
    private void handleImportPiece(){
        System.out.println("import coding piece");
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("选择Excel文件");
            Stage selectFile = new Stage();
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XLS", "*.xls"));
            File file = fileChooser.showOpenDialog(selectFile);
            if (file != null) {
                try {
                    HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(file));
                    JSONArray data = ExcelUtil.importExcel(wb);

                    System.out.println(data);

                    String[] keys = new String[]{
                            "$", "0", "1", "2", "$", "$", "$", "$", "$", "$", "3", "$"
                    };
                    data.getJSONArray(0).remove(0);
                    JSONArray dataArr = data.getJSONArray(0);
                    List<CodingModel> takingData = CodingUtils.jsonArray2List(dataArr, keys);

                    List<CodingModel> takingResult = new LinkedList<>();

                    if(!useNameSpec.isSelected()){
                        Set<String> codeConditions = CodingUtils.generateCodeConditions(takingData);
                        if (codeConditions.size() > 0) {
                            stockData.forEach(item -> {
                                for (String condition : codeConditions) {
                                    if (CodingUtils.compareCodeCondition(item.getCode(),condition)) {
                                        CodingModel itemCopy = item.copy();
                                        for (int i = 0; i < takingData.size(); i++) {
                                            if (takingData.get(i).getCode().equals(item.getCode())) {
                                                itemCopy.setTaking(takingData.get(i).getTaking());
                                                takingData.remove(i);
                                                break;
                                            }
                                        }
                                        takingResult.add(itemCopy);
                                    }
                                }
                            });
                        }
                    }else{
                        stockData.forEach(item -> {
                            CodingModel itemCopy = item.copy();
                            for (int i = 0; i < takingData.size(); i++) {
                                if(takingData.get(i).getName()!=null && takingData.get(i).getSpec()!=null){
                                    if (CodingUtils.handleNameOrSpec(item.getName()).contains(CodingUtils.handleNameOrSpec(takingData.get(i).getName())) &&
                                            CodingUtils.handleNameOrSpec(item.getSpec()).contains(CodingUtils.handleNameOrSpec(takingData.get(i).getSpec()))) {
                                        itemCopy.setTaking(takingData.get(i).getTaking());
                                        takingData.remove(i);
                                        takingResult.add(itemCopy);
                                        break;
                                    }
                                }
                            }
                        });
                    }
                    checkRemainTakingData(takingData);
                    loadTakingData(takingResult);
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            importPiece.setDisable(false);
            importBox.setDisable(false);
            undo.setDisable(false);
            saveTaking.setDisable(false);
            giveUpTaking.setDisable(false);

            importStocks.setDisable(true);
            startTaking.setDisable(true);
            exportStocks.setDisable(true);
        }catch (Exception e){
            e.printStackTrace();
            CodingUtils.alert("错误", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void checkRemainTakingData(List<CodingModel> takingData){
        if (takingData.size() > 0) {
            Set<String> codes=new HashSet<>();
            for(CodingModel model:takingData){
                codes.add(model.getCode());
            }
            CodingUtils.alert("警告", "盘点表中有" + takingData.size() + "个商品无法与库存表匹配！商品编号如下："+codes.toString(), Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void handleImportBox(){
        System.out.println("import coding box");
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("选择Excel文件");
            Stage selectFile = new Stage();
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XLS", "*.xls"));
            File file = fileChooser.showOpenDialog(selectFile);
            if (file != null) {
                try {
                    HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(file));
                    JSONArray data = ExcelUtil.importExcel(wb);

                    System.out.println(data);

                    String[] keys = new String[]{
                            "$", "0", "$", "$", "$", "$", "$", "$", "$", "$", "1", "$"
                    };
                    JSONArray dataArr = data.getJSONArray(0);
                    List<CodingModel> takingData = CodingUtils.jsonArray2List(dataArr, keys);

                    Set<String> codeConditions = CodingUtils.generateCodeConditions(takingData);

                    if (codeConditions.size() > 0) {
                        Integer perCount;
                        try {
                            perCount = Integer.parseInt(perBoxCount.getText());
                        } catch (Exception e) {
                            throw new Exception("每箱个数请输入数字!");
                        }

                        List<CodingModel> takingResult = new LinkedList<>();
                        stockData.forEach(item -> {
                            for (String condition : codeConditions) {
                                if (CodingUtils.compareCodeCondition(item.getCode(),condition)) {
                                    CodingModel itemCopy = item.copy();
                                    for (int i = 0; i < takingData.size(); i++) {
                                        if (takingData.get(i).getCode().equals(item.getCode())) {
                                            itemCopy.setTaking(takingData.get(i).getTaking() * perCount);
                                            takingData.remove(i);
                                            break;
                                        }
                                    }
                                    takingResult.add(itemCopy);
                                }
                            }
                        });
                        checkRemainTakingData(takingData);
                        loadTakingData(takingResult);
                    }

                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            importPiece.setDisable(false);
            importBox.setDisable(false);
            undo.setDisable(false);
            saveTaking.setDisable(false);
            giveUpTaking.setDisable(false);

            importStocks.setDisable(true);
            startTaking.setDisable(true);
            exportStocks.setDisable(true);
        }catch (Exception e){
            e.printStackTrace();
            CodingUtils.alert("错误", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private Stack<List> undoStack = new Stack<>();

    @FXML
    private void handleUndo(){
        if(undoStack.size()>0){
            List<CodingModel> his=undoStack.pop();
            loadData(his);
        }

        if(undoStack.size()==0){
            undo.setDisable(true);
        }
    }

    @FXML
    private void handleSaveTaking(){
        System.out.println("save coding");

        List<CodingModel> takingData = takingTable.getItems();

        stockData.forEach(item -> {
            for(CodingModel model:takingData){
                if(item.getCode().equals(model.getCode())){
                    item.setTaking(model.getTaking());
                    item.setDiff(model.getDiff());
                }
            }
        });

        loadData(stockData);

        importPiece.setDisable(true);
        importBox.setDisable(true);
        undo.setDisable(true);
        saveTaking.setDisable(true);
        giveUpTaking.setDisable(true);

        importStocks.setDisable(false);
        startTaking.setDisable(false);
        exportStocks.setDisable(false);
    }

    @FXML
    private void handleGiveUpTaking(){
        System.out.println("give up coding");

        loadData(stockData);

        importPiece.setDisable(true);
        importBox.setDisable(true);
        undo.setDisable(true);
        saveTaking.setDisable(true);
        giveUpTaking.setDisable(true);

        importStocks.setDisable(false);
        startTaking.setDisable(false);
        exportStocks.setDisable(false);
    }

    @FXML
    private void handleExportStocks(){
        System.out.println("export stocks");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("导出库存");
        Stage exportFile = new Stage();
        fileChooser.setInitialFileName("库存盘点.xls");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XLS Files", "*.xls"));
        File file = fileChooser.showSaveDialog(exportFile);
        if(file != null){
            JSONArray data=new JSONArray();
            for(CodingModel model:takingTable.getItems()){
                if(model.getTaking()!=null){
                    JSONArray rowData=new JSONArray();
                    rowData.add(model.getBar());
                    rowData.add(model.getCode());
                    rowData.add(model.getName());
                    rowData.add(model.getSpec());
                    rowData.add(model.getTaking());
                    rowData.add("");
                    data.add(rowData);
                }
            }
            Boolean result=ExcelUtil.exportExcel(file.getAbsolutePath(),"库存盘点", CodingUtils.TEMP_TABLE_HEADER,data);
            if(result){
                CodingUtils.alert("导出库存","导出成功！", Alert.AlertType.INFORMATION);
            }else{
                CodingUtils.alert("导出库存","导出失败！",Alert.AlertType.ERROR);
            }
        }
    }

    private void pushUndoStack(List<CodingModel> tableData){
        List<CodingModel> result=new LinkedList<>();

        for(CodingModel model:tableData){
            result.add(model.copy());
        }

        undoStack.push(result);
    }

    private void loadTakingData(List<CodingModel> data){
        List<CodingModel> tableData = takingTable.getItems();
        pushUndoStack(takingTable.getItems());
        List<CodingModel> result=new LinkedList<>();

        for(CodingModel model:tableData){
            result.add(model.copy());
        }



        for(CodingModel dataModel:data){
            Boolean doAdd = true;
            for(CodingModel model:result){
                if(model.getCode().equals(dataModel.getCode())){
                    model.setTaking(
                            (model.getTaking()==null?0:model.getTaking())+
                                    (dataModel.getTaking()==null?0:dataModel.getTaking()));
                    doAdd = false;
                    break;
                }
            }
            if(doAdd){
                result.add(dataModel.copy());
            }
        }

        loadData(result);
    }

    private void loadData(List<CodingModel> data){

        ObservableList<CodingModel> takingData = FXCollections.observableArrayList();
        takingData.addAll(data);

        takingTable.setItems(takingData);
        barColumn.setCellValueFactory(cellData -> cellData.getValue().barProperty());
        codeColumn.setCellValueFactory(cellData -> cellData.getValue().codeProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        specColumn.setCellValueFactory(cellData -> cellData.getValue().specProperty());
        brandColumn.setCellValueFactory(cellData -> cellData.getValue().brandProperty());
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        realityColumn.setCellValueFactory(cellData -> cellData.getValue().realityProperty());
        lockedColumn.setCellValueFactory(cellData -> cellData.getValue().lockedProperty());
        availableColumn.setCellValueFactory(cellData -> cellData.getValue().availableProperty());
        wayColumn.setCellValueFactory(cellData -> cellData.getValue().wayProperty());
        takingColumn.setCellValueFactory(cellData -> cellData.getValue().takingProperty());
        takingColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        takingColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<CodingModel, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<CodingModel, Integer> event) {
                if(!undo.isDisable()){
                    pushUndoStack(takingTable.getItems());
                }
                (event.getTableView().getItems().get(event.getTablePosition().getRow())).setTaking(event.getNewValue());
                event.getTableView().refresh();
            }
        });
        diffColumn.setCellValueFactory(cellData -> {
            if(cellData.getValue().getTaking()!=null){
                cellData.getValue().setDiff(cellData.getValue().getTaking()-cellData.getValue().getAvailable());
            }else{
                cellData.getValue().setDiff(null);
            }
            return cellData.getValue().diffProperty();
        });
        Callback diffFactory = new Callback<TableColumn<CodingModel, Integer>, TableCell<CodingModel, Integer>>() {

            @Override
            public TableCell<CodingModel, Integer> call(TableColumn<CodingModel, Integer> param) {
                return new TableCell<CodingModel, Integer>() {

                    private int columnIndex = param.getTableView().getColumns().indexOf(param);

                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item.toString());
                        }

                        if(item !=null && item != 0){
                            if(item > 0){
                                this.setStyle("-fx-background-color: green;");
                            }else {
                                this.setStyle("-fx-background-color: red;");
                            }
                        }else{
                            this.setStyle("");
                        }
                    }

                };
            }

        };
        diffColumn.setCellFactory(diffFactory);

        totalLabel.setText(TOTAL_LABEL_TEXT_PRE+takingTable.getItems().size());
    }



}
