package store.value;

public class Products {
    private String name;
    private int price;
    private int quantity;
    private String promotion;

    public Products(String name, int price, int quantity, String promotion) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = confirmNullPromotion(promotion);
    }

    private String confirmNullPromotion(String promotion) {
        if (promotion.equals("null")) {
            promotion = "";
        }
        return promotion;
    }

    @Override
    public String toString() {
        return "- " + name + " " + price + "원 " + quantity + "개 " + promotion;
    }
}
