/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cafe;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;


/**
 *
 * @author sumaiya, saif, araf
 */

public class mainFormController implements Initializable {
    
    @FXML
    private Button customers_btn;

    @FXML
    private Button dashboard_btn;
    
    @FXML
    private Label username;

    @FXML
    private Button inventory_addBtn;

    @FXML
    private Button inventory_btn;

    @FXML
    private Button inventory_clearBtn;
    
    @FXML
    private AnchorPane dashboard_form;

    @FXML
    private TableColumn<productData, String> inventory_col_date;
    
    @FXML
    private TableColumn<productData, String> inventory_col_price;

    @FXML
    private TableColumn<productData, String> inventory_col_productID;

    @FXML
    private TableColumn<productData, String> inventory_col_productName;

    @FXML
    private TableColumn<productData, String> inventory_col_status;

    @FXML
    private TableColumn<productData, String> inventory_col_stock;

    @FXML
    private TableColumn<productData, String> inventory_col_type;

    @FXML
    private Button inventory_deleteBtn;

    @FXML
    private AnchorPane inventory_form;

    @FXML
    private ImageView inventory_imageView;

    @FXML
    private Button inventory_importBtn;

    @FXML
    private TextField inventory_price;

    @FXML
    private TextField inventory_productID;

    @FXML
    private TextField inventory_productName;

    @FXML
    private ComboBox<?> inventory_status;

    @FXML
    private TextField inventory_stock;

    @FXML
    private TableView<productData> inventory_tableView;

    @FXML
    private ComboBox<?> inventory_type;

    @FXML
    private Button inventory_updateBtn;

    @FXML
    private Button logout_btn;

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button menu_btn;
    
  

    @FXML
    private TextField menu_amount;


    @FXML
    private Label menu_change;

    @FXML
    private TableColumn<productData, String> menu_col_price;

    @FXML
    private TableColumn<productData, String> menu_col_productName;

    @FXML
    private TableColumn<productData, String> menu_col_quantity;

    @FXML
    private AnchorPane menu_form;

    @FXML
    private GridPane menu_gridPane;

    @FXML
    private Button menu_payBtn;

    @FXML
    private Button menu_receiptBtn;

    @FXML
    private Button menu_removeBtn;

    @FXML
    private ScrollPane menu_scrollPane;

    @FXML
    private TableView<productData> menu_tableView;

    @FXML
    private Label menu_total;
    
    
    public Alert alert;
    
    public Connection connect;
    public PreparedStatement prepare;
    public Statement statement;
    public ResultSet result;
    public Image image;
    
    private ObservableList<productData> cardListData = FXCollections.observableArrayList();
    
    
    public void inventoryAddBtn(){
        
        if(inventory_productID.getText().isEmpty() || inventory_productName.getText().isEmpty() || inventory_price.getText().isEmpty()
                || inventory_type.getSelectionModel().getSelectedItem() == null
                || inventory_status.getSelectionModel().getSelectedItem() == null
                || data.path == null){
            
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
            
        }
        else{
            
            //check product id;
            String checkProdID = "SELECT prod_id FROM product WHERE prod_id = '"
                    + inventory_productID.getText() + "'";
            
            connect = database.connectDB();
            
            try{
                statement = connect.createStatement();
                result = statement.executeQuery(checkProdID);
                
                if(result.next()){
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText(inventory_productID.getText() + " was already taken");
                    alert.showAndWait();
                }
                else{
                    
                    String insertData = "INSERT INTO product "
                            + "(prod_id, prod_name, type, stock, price, status, image, date) "
                            + "VALUES(?,?,?,?,?,?,?,?)";
                    
                    prepare = connect.prepareStatement(insertData);
                    prepare.setString(1, inventory_productID.getText());
                    prepare.setString(2, inventory_productName.getText());
                    prepare.setString(3, (String) inventory_type.getSelectionModel().getSelectedItem());
                    prepare.setString(4, inventory_stock.getText());
                    prepare.setString(5, inventory_price.getText());
                    prepare.setString(6, (String) inventory_status.getSelectionModel().getSelectedItem());
                    
                    String path = data.path;
                    path = path.replace("\\", "\\\\");
                    
                    prepare.setString(7, path);
                    
                    Date date = new Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                    
                    prepare.setString(8, String.valueOf(sqlDate));
                    
                    prepare.executeUpdate();
                    
                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Added...");
                    alert.showAndWait();
                    
                    inventoryShowData();
                    inventoryClearBtn();
                   
                }
                
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    
    public void inventoryUpdateBtn(){
        
        if (inventory_productID.getText().isEmpty()
                || inventory_productName.getText().isEmpty()
                || inventory_type.getSelectionModel().getSelectedItem() == null
                || inventory_stock.getText().isEmpty()
                || inventory_price.getText().isEmpty()
                || inventory_status.getSelectionModel().getSelectedItem() == null
                || data.path == null || data.id == 0) {
            
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
            
        } else {
            
            String path = data.path;
            path = path.replace("\\", "\\\\");
            
            Date date = new Date();
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            
            String updateData = "UPDATE product SET "
                    + "prod_id = '" + inventory_productID.getText() + "', prod_name = '"
                    + inventory_productName.getText() + "', type = '"
                    + inventory_type.getSelectionModel().getSelectedItem() + "', stock = '"
                    + inventory_stock.getText() + "', price = '"
                    + inventory_price.getText() + "', status = '"
                    + inventory_status.getSelectionModel().getSelectedItem() + "', image = '"
                    + path + "', date = '"
                    + String.valueOf(sqlDate) + "' WHERE id = " + data.id;
            
            connect = database.connectDB();
            
            try {
                
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to UPDATE PRoduct ID: " + inventory_productID.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();
                
                if (option.get().equals(ButtonType.OK)) {
                    prepare = connect.prepareStatement(updateData);
                    prepare.executeUpdate();
                    
                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Updated!");
                    alert.showAndWait();

                    inventoryShowData();
                    inventoryClearBtn();
                } else {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Cancelled.");
                    alert.showAndWait();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    // delete button
    public void inventoryDeleteBtn(){
        if(data.id == 0){
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
            
        } else {
            alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to DELETE Product ID: " + inventory_productID.getText() + "?");
            Optional<ButtonType> option = alert.showAndWait();
            
            if (option.get().equals(ButtonType.OK)) {
                String deleteData = "DELETE FROM product WHERE id = " + data.id;
                try {
                    prepare = connect.prepareStatement(deleteData);
                    prepare.executeUpdate();
                    
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("successfully Deleted!");
                    alert.showAndWait();

            
                    inventoryShowData();
                    inventoryClearBtn();
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Cancelled");
                alert.showAndWait();
            }
        }
    }
    
    public void inventoryClearBtn(){
        
        inventory_productID.setText("");
        inventory_productName.setText("");
        inventory_stock.setText("");
        inventory_price.setText("");
        inventory_type.getSelectionModel().clearSelection();
        inventory_status.getSelectionModel().clearSelection();
        data.path = "";
        data.id = 0;
        inventory_imageView.setImage(null);
    }
    
    public void inventoryImportBtn(){
        
        FileChooser openFile = new FileChooser();
        openFile.getExtensionFilters().add(new ExtensionFilter("Open Image File", "*png", "*jpg"));
        
        File file = openFile.showOpenDialog(main_form.getScene().getWindow());
        
        if(file != null){
            data.path = file.getAbsolutePath();
            image = new Image(file.toURI().toString(), 124, 135, false, true);
            
            inventory_imageView.setImage(image);
        }
        
    }
    
    
    public void inventorySelectData(){
        productData pdata = inventory_tableView.getSelectionModel().getSelectedItem();
        int number = inventory_tableView.getSelectionModel().getSelectedIndex();
        
        if((number-1) < -1){
            return;
        }
        
        inventory_productID.setText(pdata.getProductId());
        inventory_productName.setText(pdata.getProductName());
        inventory_stock.setText(String.valueOf(pdata.getStock()));
        inventory_price.setText(String.valueOf(pdata.getPrice()));
        
        data.path = pdata.getImage();
        
        String path = "File:" + pdata.getImage();
        data.date = String.valueOf(pdata.getDate());
        data.id = pdata.getId();
        
        image = new Image(path, 124, 135, false, true);
        inventory_imageView.setImage(image);
        
        
    }
    
    
    
    // to merge all datas
    public ObservableList<productData> inventoryDataList(){
        ObservableList<productData> listdata = FXCollections.observableArrayList();
        
        String sql = "SELECT * FROM product";
        
        connect = database.connectDB();
        
        productData prodData;
        
        try{
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            
            while(result.next()){
                
                prodData = new productData(result.getInt("id"), 
                        result.getString("prod_id"),
                        result.getString("prod_name"),
                        result.getString("type"),
                        result.getInt("stock"),
                        result.getDouble("price"),
                        result.getString("status"),
                        result.getString("image"),
                        result.getDate("date"));
                
                listdata.add(prodData);
                
            }
            
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
        return listdata; 
    }
    
    
    // to show all the data in table
    private ObservableList<productData> inventoryListData;
    
    public void inventoryShowData(){
        inventoryListData = inventoryDataList();
        
        
        
        inventory_col_productID.setCellValueFactory(new PropertyValueFactory<>("productId"));
        inventory_col_productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        inventory_col_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        inventory_col_stock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        inventory_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        inventory_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));
        inventory_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
            
        inventory_tableView.setItems(inventoryListData);
    }
    
  
    private String[] typeList = {"Drinks", "Meals"};
    public void inventoryTypeList(){
        List<String> list = new ArrayList<>();
        
        for(String data: typeList){
            list.add(data);
        }
        
        ObservableList listData = FXCollections.observableArrayList(list);
        inventory_type.setItems(listData);
    }
    
    
    
    private String[] statusList = {"Available", "Unavailable"};
    public void inventoryStatusList(){
        List<String> list = new ArrayList<>();
        
        for(String data: statusList){
            list.add(data);
        }
        
        ObservableList listdata = FXCollections.observableArrayList(list);
        inventory_status.setItems(listdata);
        
    }
    
    
    public ObservableList<productData> menuGetData() {
        
        String sql = "SELECT * FROM product";
        
        ObservableList<productData> listData = FXCollections.observableArrayList();
        connect = database.connectDB();
        
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            
            productData prod;
            
            while (result.next()) {
                prod = new productData(result.getInt("id"),
                        result.getString("prod_id"),
                        result.getString("prod_name"),
                        result.getString("type"),
                        result.getInt("stock"),
                        result.getDouble("price"),
                        result.getString("image"),
                        result.getDate("date"));
                
                listData.add(prod);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return listData;
    }
    
    public void menuDisplayCard() {
        
        cardListData.clear();
        cardListData.addAll(menuGetData());
        
        int row = 0;
        int column = 0;
        
        menu_gridPane.getChildren().clear();
        menu_gridPane.getRowConstraints().clear();
        menu_gridPane.getColumnConstraints().clear();
        
        for (int q = 0; q < cardListData.size(); q++) {
            
            try {
                FXMLLoader load = new FXMLLoader();
                load.setLocation(getClass().getResource("cardProduct.fxml"));
                AnchorPane pane = load.load();
                cardProductController cardC = load.getController();
                cardC.setData(cardListData.get(q));
                
                if (column == 3) {
                    column = 0;
                    row += 1;
                }
                
                menu_gridPane.add(pane, column++, row);
                
                GridPane.setMargin(pane, new Insets(10));
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public ObservableList<productData> menuGetOrder() {
        customerID();
        ObservableList<productData> listData = FXCollections.observableArrayList();
        
        String sql = "SELECT * FROM customer WHERE customer_id = " + cID;
        
        connect = database.connectDB();
        
        try {
            
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            
            productData prod;
            
            while (result.next()) {
                prod = new productData(result.getInt("id"),
                        result.getString("prod_id"),
                        result.getString("prod_name"),
                        result.getString("type"),
                        result.getInt("quantity"),
                        result.getDouble("price"),
                        result.getString("image"),
                        result.getDate("date"));
                listData.add(prod);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return listData;
    }
    
    private ObservableList<productData> menuOrderListData;
    
    public void menuShowOrderData() {
        menuOrderListData = menuGetOrder();
        
        menu_col_productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        menu_col_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        menu_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        menu_tableView.setItems(menuOrderListData);
    }
    private int getid;
    
    public void menuSelectOrder() {
        productData prod = menu_tableView.getSelectionModel().getSelectedItem();
        int num = menu_tableView.getSelectionModel().getSelectedIndex();
        
        if ((num - 1) < -1) {
            return;
        }
        // TO GET THE ID PER ORDER
        getid = prod.getId();
        
    }
    
    private double totalP;
    
    public void menuGetTotal() {
        customerID();
        String total = "SELECT SUM(price) FROM customer WHERE customer_id = " + cID;
        
        connect = database.connectDB();
        
        try {
            
            prepare = connect.prepareStatement(total);
            result = prepare.executeQuery();
            
            if (result.next()) {
                totalP = result.getDouble("SUM(price)");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    public void menuDisplayTotal() {
        menuGetTotal();
        menu_total.setText("$" + totalP);
    }
    
    private double amount;
    private double change;
    
    public void menuAmount() {
        menuGetTotal();
        if (menu_amount.getText().isEmpty() || totalP == 0) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Invalid :3");
            alert.showAndWait();
        } else {
            amount = Double.parseDouble(menu_amount.getText());
            if (amount < totalP) {
                menu_amount.setText("");
            } else {
                change = (amount - totalP);
                menu_change.setText("$" + change);
            }
        }
    }
    
    public void menuPayBtn() {
        
        if (totalP == 0) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please choose your order first!");
            alert.showAndWait();
        } else {
            menuGetTotal();
            String insertPay = "INSERT INTO receipt (customer_id, total, date, em_username) "
                    + "VALUES(?,?,?,?)";
            
            connect = database.connectDB();
            
            try {
                
                if (amount == 0) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Messaged");
                    alert.setHeaderText(null);
                    alert.setContentText("Something wrong :3");
                    alert.showAndWait();
                } else {
                    alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Are you sure?");
                    Optional<ButtonType> option = alert.showAndWait();
                    
                    if (option.get().equals(ButtonType.OK)) {
                        customerID();
                        menuGetTotal();
                        prepare = connect.prepareStatement(insertPay);
                        prepare.setString(1, String.valueOf(cID));
                        prepare.setString(2, String.valueOf(totalP));
                        
                        Date date = new Date();
                        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                        
                        prepare.setString(3, String.valueOf(sqlDate));
                        prepare.setString(4, data.username);
                        
                        prepare.executeUpdate();
                        
                        alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Infomation Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Successful.");
                        alert.showAndWait();
                        
                        menuShowOrderData();
                        
                    } else {
                        alert = new Alert(AlertType.WARNING);
                        alert.setTitle("Infomation Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Cancelled.");
                        alert.showAndWait();
                    }
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
    }
    
    public void menuRemoveBtn() {
        
        if (getid == 0) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please select the order you want to remove");
            alert.showAndWait();
        } else {
            String deleteData = "DELETE FROM customer WHERE id = " + getid;
            connect = database.connectDB();
            try {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to delete this order?");
                Optional<ButtonType> option = alert.showAndWait();
                
                if (option.get().equals(ButtonType.OK)) {
                    prepare = connect.prepareStatement(deleteData);
                    prepare.executeUpdate();
                }
                
                menuShowOrderData();
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }
    }
/*    
    public void menuReceiptBtn() {
        
        if (totalP == 0 || menu_amount.getText().isEmpty()) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setContentText("Please order first");
            alert.showAndWait();
        } else {
            HashMap map = new HashMap();
            map.put("getReceipt", (cID - 1));
            
            try {
                
                JasperDesign jDesign = JRXmlLoader.load("C:\\Users\\WINDOWS 10\\Documents\\NetBeansProjects\\cafeShopManagementSystem\\src\\cafeshopmanagementsystem\\report.jrxml");
                JasperReport jReport = JasperCompileManager.compileReport(jDesign);
                JasperPrint jPrint = JasperFillManager.fillReport(jReport, map, connect);
                
                JasperViewer.viewReport(jPrint, false);
                
                menuRestart();
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }
        
    }
 */   
    public void menuRestart() {
        totalP = 0;
        change = 0;
        amount = 0;
        menu_total.setText("$0.0");
        menu_amount.setText("");
        menu_change.setText("$0.0");
    }


    
     private int cID;
    
    public void customerID() {
        
        String sql = "SELECT MAX(customer_id) FROM customer";
        connect = database.connectDB();
        
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            
            if (result.next()) {
                cID = result.getInt("MAX(customer_id)");
            }
            
            String checkCID = "SELECT MAX(customer_id) FROM receipt";
            prepare = connect.prepareStatement(checkCID);
            result = prepare.executeQuery();
            int checkID = 0;
            if (result.next()) {
                checkID = result.getInt("MAX(customer_id)");
            }
            
            if (cID == 0) {
                cID += 1;
            } else if (cID == checkID) {
                cID += 1;
            }
            
            data.cID = cID;
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void switchForm(ActionEvent event) {
        
        if (event.getSource() == dashboard_btn) {
            dashboard_form.setVisible(true);
            inventory_form.setVisible(false);
            menu_form.setVisible(false);
            /*
            customers_form.setVisible(false);
            
            dashboardDisplayNC();
            dashboardDisplayTI();
            dashboardTotalI();
            dashboardNSP();
            dashboardIncomeChart();
            dashboardCustomerChart();
*/
            
        } else if (event.getSource() == inventory_btn) {
            dashboard_form.setVisible(false);
            inventory_form.setVisible(true);
            menu_form.setVisible(false);
      //      customers_form.setVisible(false);
            
            inventoryTypeList();
            inventoryStatusList();
            inventoryShowData();
        } else if (event.getSource() == menu_btn) {
            dashboard_form.setVisible(false);
            inventory_form.setVisible(false);
            menu_form.setVisible(true);
         //   customers_form.setVisible(false);
            
            menuDisplayCard();
            menuDisplayTotal();
            menuShowOrderData();
        } else if (event.getSource() == customers_btn) {
            dashboard_form.setVisible(false);
            inventory_form.setVisible(false);
            menu_form.setVisible(false);
            //customers_form.setVisible(true);
            
//            customersShowData();
        }
        
    }
    
    public void logout(){
        try{
            alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure to logout");
            Optional<ButtonType> option = alert.showAndWait();
            
            
            if(option.get().equals(ButtonType.OK)){
                
                // link the login form
                logout_btn.getScene().getWindow().hide();
                
                Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
                
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                
                stage.setTitle("CafeFx");
                
                stage.setScene(scene);
                stage.show();
            }
            
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    public void displayUsername() {
        
        String user = data.username;
        user = user.substring(0, 1).toUpperCase() + user.substring(1);
        
        username.setText(user);
        
    }
    
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inventoryTypeList();
        inventoryStatusList();
        inventoryShowData();
        
        menuDisplayCard();
        menuGetOrder();
        menuDisplayTotal();
        menuShowOrderData();
    }
    
}
