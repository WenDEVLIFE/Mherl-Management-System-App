package functions;

public class ProductBase {
    private String Productname;

    private int Quantity;

    private String Price;

    public ProductBase(String productname, int quantity, String price) {
        Productname = productname;
        Quantity = quantity;
        Price = price;
    }

    public String getProductname() {
        return Productname;
    }

    public int getQuantity() {
        return Quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setProductname(String productname) {
        Productname = productname;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public void setProductBase(String productname, int quantity, String price) {
        Productname = productname;
        Quantity = quantity;
        Price = price;
    }





}
