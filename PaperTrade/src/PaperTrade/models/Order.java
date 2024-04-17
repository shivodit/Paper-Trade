package PaperTrade.models;

import java.sql.Timestamp;

public class Order {
    private int order_id;
    private String stock;
    private int quantity;
    private float price;
    private Timestamp time;
    private String order_type;
    private String order_class;
    private String order_status;

    public Order(int order_id, String stock, int quantity, float price, Timestamp time, String order_type, String order_class, String order_status){
        this.order_id = order_id;
        this.stock = stock;
        this.quantity = quantity;
        this.price = price;
        this.time = time;
        this.order_type = order_type;
        this.order_class = order_class;
        this.order_status = order_status;
    }
    public int getOrder_id(){
        return order_id;
    }
    public String getStock(){
        return stock;
    }
    public int getQuantity(){
        return quantity;
    }
    public float getPrice(){
        return price;
    }
    public Timestamp getTime(){
        return time;
    }
    public String getOrder_type(){
        return order_type;
     }
    public String getOrder_class(){
        return order_class;
    }
    public String getOrder_status(){
        return order_status;
    }
    public void setOrder_id(int order_id){
        this.order_id = order_id;
    }
    public void setStock(String stock){
        this.stock = stock;
    }
    public void setQuantity(int quantity){
        this.quantity = quantity;
    }
    public void setPrice(float price){
        this.price = price;
    }
    public void setTime(Timestamp time){
        this.time = time;
    }
    public void setOrder_type(String order_type){
        this.order_type = order_type;
    }
    public void setOrder_class(String order_class){
        this.order_class = order_class;
    }
    public void setOrder_status(String order_status){
        this.order_status = order_status;
    }
}
