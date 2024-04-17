package PaperTrade.controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import PaperTrade.Main;
import PaperTrade.db.DatabaseConnection;
import PaperTrade.models.Session;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class InvestmentController {
    @FXML
    private HBox navbar;
    @FXML
    private Label curr_value_label;
    @FXML
    private Label invested_value_label;
    @FXML
    private Label total_return_label;
    @FXML
    private Label one_d_return_label;
    @FXML 
    private VBox investments_list;
    List<Investment> investments = new ArrayList<Investment>();

    public void initialize() {
        // Initialize the controller
        // Add any initialization logic here
        Main.getInstance().setupNavbar(navbar);
        addInvestments();
        updateLabels();
        populateInvestments();
    }

    private void populateInvestments(){
        HBox header = new HBox();
        header.setId("h_handler_investments");
        header.setAlignment(javafx.geometry.Pos.CENTER);
        HBox.setHgrow(header, Priority.ALWAYS);
        VBox[] vhead = new VBox[4];
        Label[] head = new Label[4];
        for (int i = 0; i < 4; i++) {
            vhead[i] = new VBox();
            head[i] = new Label();
        }
        head[0].setText("Company");
        head[1].setText("Price");
        head[2].setText("Returns (%)");
        head[3].setText("Current");
        for (int i = 0; i < 4; i++) {
            vhead[i].getChildren().add(head[i]);
            vhead[i].setId("v_handler_investments");
            HBox.setHgrow(vhead[i],Priority.ALWAYS);
            header.getChildren().
            add(vhead[i]);
        }
        header.setSpacing(20);
        header.styleProperty().set("-fx-background-color: #ffffff; -fx-cursor: default;");
        investments_list.getChildren().add(header);

        // populate the investments
        for (Investment i : investments) {
            HBox h = new HBox();
            h.setId("h_handler_investments");
            HBox.setHgrow(h, Priority.ALWAYS);
            Label symbol = new Label(i.symbol);
            symbol.maxWidth(60);
            symbol.setWrapText(true);
            try {
                ResultSet r = DatabaseConnection.getInstance().executeQuery("Select name from stock where symbol = '" + i.symbol + "';");
                if (r.next())
                    symbol.setText(r.getString("name"));
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Label quantity_av = new Label(i.quantity + " shares • " + "Avg. ₹" + i.avg_price);
            Label curr_price = new Label("₹" + i.market_price);
            String one_ret = (i.market_price - i.one_ago_price)*i.quantity + " (" + String.format("%.2f",(i.market_price - i.one_ago_price)/i.one_ago_price*100) + "%)";
            Label one_day_return = new Label(one_ret);
            Label total_return = new Label((i.market_price - i.avg_price)*i.quantity+"");
            Label total_return_percentage = new Label(String.format("%.2f",100*(i.market_price - i.avg_price)/i.avg_price)+"%");
            Label curr_value = new Label("₹" + i.market_price*i.quantity);
            Label original_value = new Label("₹" + i.avg_price*i.quantity);
            if (i.market_price - i.avg_price >0){
                total_return_percentage.setStyle("-fx-text-fill: #00ee00;");
            }
            else{
                total_return_percentage.setStyle("-fx-text-fill: #ee0000;");
            }
            if (i.market_price - i.one_ago_price>0){
                one_day_return.setStyle("-fx-text-fill: #00ee00;");
            }
            else{
                one_day_return.setStyle("-fx-text-fill: #ee0000;");
            }
            VBox[] varray = new VBox[4];
            for (int j = 0; j < 4; j++) {
                varray[j] = new VBox();
            }
            varray[0].getChildren().addAll(symbol,quantity_av);
            varray[1].getChildren().addAll(curr_price,one_day_return);
            varray[2].getChildren().addAll(total_return,total_return_percentage);
            varray[3].getChildren().addAll(curr_value,original_value);
            for (VBox v : varray) {
                v.setId("v_handler_investments");
                v.setPrefWidth(400);
                HBox.setHgrow(v,Priority.ALWAYS);
                h.getChildren().add(v);
            }
            h.setOnMouseClicked(e->{
                try {
                    Main.getInstance().openStock(i.symbol);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            });
            investments_list.getChildren().add(h);
            
        }
    }
    private void updateLabels() {
        // update the labels
        float curr_value = 0;
        float invested_value = 0;
        float one_ago_value = 0;
        for (Investment i : investments) {
            curr_value += i.market_price * i.quantity;
            invested_value += i.avg_price * i.quantity;
            one_ago_value += i.one_ago_price * i.quantity;
        }
        curr_value_label.setText("₹" + curr_value);
        invested_value_label.setText("₹" + invested_value);
        total_return_label.setText(String.format("%.2f",100*(curr_value - invested_value)/invested_value)+"%");
        one_d_return_label.setText(String.format("%.2f",(curr_value - one_ago_value)/one_ago_value*100)+"%");
        if (curr_value - invested_value > 0) {
            total_return_label.setStyle("-fx-text-fill: #00ee00;");
        } else {
            total_return_label.setStyle("-fx-text-fill: #ee0000;");
        }
        if(curr_value - one_ago_value > 0){
            one_d_return_label.setStyle("-fx-text-fill: #00ee00;");
        }
        else{
            one_d_return_label.setStyle("-fx-text-fill: #ee0000;");
        }
    }

    private void addInvestments(){
        // add investments
        try {
            String d = "SELECT Distinct symbol FROM Holds WHERE user_id = " + Session.getId() + ";";
            ResultSet r = DatabaseConnection.getInstance().executeQuery(d);
            while (r.next()) {
                Investment i = new Investment(r.getString("symbol"));
                // add the investment to the view
                investments.add(i);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    class Investment {
        // Investment class
        String symbol;
        int user_id;
        int quantity;
        float market_price;
        float avg_price;
        float one_ago_price;

        public Investment(String symbol) {
            this.symbol = symbol;
            this.user_id = Session.getId();
            try {
                String d = "SELECT * FROM Holds WHERE user_id = " + user_id + " AND symbol = '" + symbol + "';";
                ResultSet r = DatabaseConnection.getInstance().executeQuery(d);
                if (r.next()) {
                    this.quantity = r.getInt("quantity");
                    this.avg_price = r.getFloat("avg_price");
                    this.market_price = Session.getMarketPrice(symbol);
                    this.one_ago_price = -1*(Session.get1DayChange(symbol) - market_price);
                }
            } catch (Exception e) {
                this.quantity = 0;
                this.avg_price = 0;
                this.market_price = 0;
                this.one_ago_price = 0;
                // TODO: handle exception
            }
            
        }

    }

}

