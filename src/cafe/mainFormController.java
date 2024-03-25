/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cafe;


import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;

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
    private Button inventory_addBtn;

    @FXML
    private Button inventory_btn;

    @FXML
    private Button inventory_clearBtn;

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

    public Alert alert;
    
    public Connection connect;
    public PreparedStatement prepare;
    public Statement statement;
    public ResultSet result;
    public Image image;
    
    
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
                            + "(prod_id, prod_name, type, price, stock, status, image, date) "
                            + "VALUES(?,?,?,?,?,?,?,?)";
                    
                    prepare = connect.prepareStatement(insertData);
                    prepare.setString(1, inventory_productID.getText());
                    prepare.setString(2, inventory_productName.getText());
                    prepare.setString(3, (String)inventory_type.getSelectionModel().getSelectedItem());
                    prepare.setString(4, inventory_price.getText());
                    prepare.setString(5, inventory_stock.getText());
                    prepare.setString(6, (String)inventory_status.getSelectionModel().getSelectedItem());
                    
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


    public void inventoryClearBtn(){
        
        inventory_productID.setText("");
        inventory_productName.setText("");
        inventory_stock.setText("");
        inventory_price.setText("");
        inventory_type.getSelectionModel().clearSelection();
        inventory_status.getSelectionModel().clearSelection();
        data.path = "";
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
    
    
    
    


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inventoryTypeList();
        inventoryStatusList();
        inventoryShowData();
    }
    
}
