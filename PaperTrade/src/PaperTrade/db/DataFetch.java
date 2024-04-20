package PaperTrade.db;

// import java.security.Timestamp;
// import java.sql.Date;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.sql.ResultSet;
import java.sql.SQLException;


// incomplete code
public class DataFetch {
    static String api_key = "1c34c42af1";
    static DatabaseConnection db = DatabaseConnection.getInstance();
    static final String[] symbols = {"ABB", "ADANIENSOL", "ADANIENT", "ADANIGREEN", "ADANIPORTS", "ADANIPOWER", "ATGL", "AMBUJACEM", "APOLLOHOSP", "ASIANPAINT", "DMART", "AXISBANK", "BAJAJ-AUTO", "BAJFINANCE", "BAJAJFINSV", "BAJAJHLDNG", "BANKBARODA", "BERGEPAINT", "BEL", "BPCL", "BHARTIARTL", "BOSCHLTD", "BRITANNIA", "CANBK", "CHOLAFIN", "CIPLA", "COALINDIA", "COLPAL", "DLF", "DABUR", "DIVISLAB", "DRREDDY", "EICHERMOT", "GAIL", "GODREJCP", "GRASIM", "HCLTECH", "HDFCBANK", "HDFCLIFE", "HAVELLS", "HEROMOTOCO", "HINDALCO", "HAL", "HINDUNILVR", "ICICIBANK", "ICICIGI", "ICICIPRULI", "ITC", "IOC", "IRCTC", "IRFC", "INDUSINDBK", "NAUKRI", "INFY", "INDIGO", "JSWSTEEL", "JINDALSTEL", "JIOFIN", "KOTAKBANK", "LTIM", "LT", "LICI", "M&M", "MARICO", "MARUTI", "NTPC", "NESTLEIND", "ONGC", "PIDILITIND", "PFC", "POWERGRID", "PNB", "RECLTD", "RELIANCE", "SBICARD", "SBILIFE", "SRF", "MOTHERSON", "SHREECEM", "SHRIRAMFIN", "SIEMENS", "SBIN", "SUNPHARMA", "TVSMOTOR", "TCS", "TATACONSUM", "TATAMTRDVR", "TATAMOTORS", "TATAPOWER", "TATASTEEL", "TECHM", "TITAN", "TORNTPHARM", "TRENT", "ULTRACEMCO", "MCDOWELL-N", "VBL", "VEDL", "WIPRO", "ZOMATO", "ZYDUSLIFE"};
    static final String[] upstox_id = {"NSE_EQ|INE117A01022","NSE_EQ|INE931S01010","NSE_EQ|INE423A01024","NSE_EQ|INE364U01010","NSE_EQ|INE742F01042","NSE_EQ|INE814H01011","NSE_EQ|INE399L01023","NSE_EQ|INE079A01024","NSE_EQ|INE437A01024","NSE_EQ|INE021A01026","NSE_EQ|INE192R01011","NSE_EQ|INE238A01034","NSE_EQ|INE917I01010","NSE_EQ|INE296A01024","NSE_EQ|INE918I01026","NSE_EQ|INE118A01012","NSE_EQ|INE028A01039","NSE_EQ|INE463A01038","NSE_EQ|INE263A01024","NSE_EQ|INE029A01011","NSE_EQ|INE397D01024","NSE_EQ|INE323A01026","NSE_EQ|INE216A01030","NSE_EQ|INE476A01014","NSE_EQ|INE121A08PJ0","NSE_EQ|INE059A01026","NSE_EQ|INE522F01014","NSE_EQ|INE259A01022","NSE_EQ|INE271C01023","NSE_EQ|INE016A01026","NSE_EQ|INE361B01024","NSE_EQ|INE089A01023","NSE_EQ|INE066A01021","NSE_EQ|INE129A01019","NSE_EQ|INE102D01028","NSE_EQ|INE047A01021","NSE_EQ|INE860A01027","NSE_EQ|INE040A01034","NSE_EQ|INE795G01014","NSE_EQ|INE176B01034","NSE_EQ|INE158A01026","NSE_EQ|INE038A01020","NSE_EQ|INE066F01020","NSE_EQ|INE030A01027","NSE_EQ|INE090A01021","NSE_EQ|INE765G01017","NSE_EQ|INE726G01019","NSE_EQ|INE154A01025","NSE_EQ|INE242A01010","NSE_EQ|INE335Y01020","NSE_EQ|INE053F01010","NSE_EQ|INE095A01012","NSE_EQ|INE663F01024","NSE_EQ|INE009A01021","NSE_EQ|INE646L01027","NSE_EQ|INE019A01038","NSE_EQ|INE749A01030","NSE_EQ|INE758E01017","NSE_EQ|INE237A01028","NSE_EQ|INE214T01019","NSE_EQ|INE018A01030","NSE_EQ|INE0J1Y01017","NSE_EQ|INE101A01026","NSE_EQ|INE196A01026","NSE_EQ|INE585B01010","NSE_EQ|INE733E01010","NSE_EQ|INE239A01024","NSE_EQ|INE213A01029","NSE_EQ|INE318A01026","NSE_EQ|INE134E01011","NSE_EQ|INE752E01010","NSE_EQ|INE160A01022","NSE_EQ|INE020B01018","NSE_EQ|INE002A01018","NSE_EQ|INE018E01016","NSE_EQ|INE123W01016","NSE_EQ|INE647A01010","NSE_EQ|INE775A01035","NSE_EQ|INE070A01015","NSE_EQ|INE721A01013","NSE_EQ|INE003A01024","NSE_EQ|INE062A01020","NSE_EQ|INE044A01036","NSE_EQ|INE494B01023","NSE_EQ|INE467B01029","NSE_EQ|INE192A01025","NSE_EQ|IN9155A01020","NSE_EQ|INE155A01022","NSE_EQ|INE245A01021","NSE_EQ|INE081A01020","NSE_EQ|INE669C01036","NSE_EQ|INE280A01028","NSE_EQ|INE685A01028","NSE_EQ|INE849A01020","NSE_EQ|INE481G01011","NSE_EQ|INE854D01024","NSE_EQ|INE200M01021","NSE_EQ|INE205A01025","NSE_EQ|INE075A01022","NSE_EQ|INE758T01015","NSE_EQ|INE010B01027"};
    private static final String HttpGetRequest = null;
    private static final String HttpClients = null;
    
    public static void update_database_till_date(){
        ResultSet rs = db.executeQuery("Select MAX(timestamp) from stock_price;");
        String date = null;
        try {
            rs.next();
            date = rs.getString(1);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (date == null){
            System.out.println("No data in database");
            return;
        }
        // print today's date in the format YYYY-MM-DD hh:mm:ss format
        // System.out.println(date);
        // get time stamp from now 
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(now);
        System.out.println(strDate);
        System.out.println(date);
    

        
        for (int i = 0; i < symbols.length; i++){
            String symbol = symbols[i];
            String id = upstox_id[i];
            String url = "https://api.upstox.com/v2/historical-candle/"+id+"/1minute/"+date+"%20to%20"+strDate+"?api_key="+api_key;
            System.out.println(url);
            String response = url;
            System.out.println(response);
            String[] lines = response.split("\n");
            for (int j = 1; j < lines.length; j++){
                String[] data = lines[j].split(",");
                String query = "INSERT INTO stock_price (timestamp, symbol, open, high, low, close, volume) VALUES ('"+data[0]+"', '"+symbol+"', "+data[1]+", "+data[2]+", "+data[3]+", "+data[4]+", "+data[5]+");";
                db.executeUpdate(query);
            }
        }


    }
}
