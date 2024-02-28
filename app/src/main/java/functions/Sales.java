package functions;

import androidx.annotation.NonNull;

public class Sales {

    public String productName;

    public int quantity;

    public int totalprice;

    public String date;

    public Sales( String productName, int quantity, int totalprice, String date) {
        this.productName = productName;
        this.quantity = quantity;
        this.totalprice = totalprice;
        this.date = date;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = Integer.parseInt(quantity);
    }

    public int getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(String totalprice) {
        this.totalprice = Integer.parseInt(totalprice);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    @NonNull
    public String toString() {
        return "Sales{" +
                "productName='" + productName + '\'' +
                ", quantity='" + quantity + '\'' +
                ", totalprice='" + totalprice + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

}
