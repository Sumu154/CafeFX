/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cafe;

import java.sql.Date;



/**
 *
 * @author sumaiya
 */
public class productData {
    private Integer id;
    private String productId;
    private String productName;
    private String type;
    private Double price;
    private Integer stock;
    private String status;
    private String image;
    private Date date;
    
    
    public productData(Integer id, String productId, String productName, String type, 
            Double price, Integer stock, String status, String image, Date date){
        
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.type = type;
        this.price = price;
        this.stock = stock;
        this.status = status;
        this.image = image;
        this.date = date;
    }
    
    public Integer getId(){
        return id;
    }
    
    public String getProductId(){
        return productId;
    }
    
    public String getProductName(){
        return productName;
    }
    
    public String getType(){
        return type;
    }
    
    public Double getPrice(){
        return price;
    }
    
    public Integer getStock(){
        return stock;
    }
    
    public String getStatus(){
        return status;
    }
    
    public String getImage(){
        return image;
    }
    
    public Date getDate(){
        return date;
    }
    
    
}
