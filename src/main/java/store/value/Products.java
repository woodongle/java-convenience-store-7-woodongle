package store.value;

import static store.validation.Validation.confirmYOrN;
import static store.view.InputView.*;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Products {
    private static final double MEMBERSHIP_DISCOUNT_RATE = 0.3;
    private static List<Promotions> promotions = new ArrayList<>();

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

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }

    public String getPromotion() {
        return promotion;
    }

    public static void setPromotions(List<Promotions> promos) {
        promotions = new ArrayList<>(promos);
    }

    public String getName() {
        return name;
    }

    private String confirmNullPromotion(String promotion) {
        if (promotion.equals("null")) {
            promotion = "";
        }
        return promotion;
    }

    @Override
    public String toString() {
        if (quantity == 0) {
            return "- " + name + " " + String.format("%,d", price) + "원 " + "재고 없음";
        }
        return "- " + name + " " + String.format("%,d", price) + "원 " + quantity + "개 " + promotion;
    }

    public static int calculateFinalAmount(List<Products> purchasedProducts, int freeItemCount) {
        int totalPurchaseAmount = calculateTotalPurchaseAmount(purchasedProducts);
        int promotionDiscountAmount = calculatePromotionDiscountAmount(purchasedProducts, freeItemCount);
        return totalPurchaseAmount - promotionDiscountAmount;
    }

    public static int calculateTotalPurchaseAmount(List<Products> purchasedProducts) {
        int totalAmount = 0;

        for (Products product : purchasedProducts) {
            totalAmount += product.price * product.quantity;
        }
        return totalAmount;
    }

    public static int calculatePromotionDiscountAmount(List<Products> purchasedProducts, int freeItemCount) {
        int promotionDiscountAmount = 0;

        for (Products product : purchasedProducts) {
            if (!product.promotion.isEmpty()) {
                // 무료 증정 받은 상품 가격 * 무료 증정 받은 상품 개수
                int freeItems = calculateFreeItemsForProduct(product);
                promotionDiscountAmount += product.price * freeItems;
            }
        }
        return promotionDiscountAmount;
    }

    public static int calculateFreeItemsForProduct(Products product) {
        Promotions promotion = findPromotionByName(product.promotion);
        if (promotion == null || !isCurrentDateInRange(promotion.getStartDate(), promotion.getEndDate())) {
            return 0; // 프로모션이 없거나 현재 날짜가 프로모션 기간이 아니면 무료 아이템 없음
        }

        if (isTwoPlusOnePromotion(promotion)) {
            return product.quantity / 3; // 2+1 프로모션
        }
        if (isOnePlusOnePromotion(promotion)) {
            return product.quantity / 2; // 1+1 프로모션
        }

        return 0; // 다른 프로모션은 없음
    }

    private static boolean isTwoPlusOnePromotion(Promotions promotion) {
        return promotion.getBuy() == 2 && promotion.getGet() == 1;
    }

    private static boolean isOnePlusOnePromotion(Promotions promotion) {
        return promotion.getBuy() == 1 && promotion.getGet() == 1;
    }

    private static Promotions findPromotionByName(String name) {
        for (Promotions promotion : promotions) {
            if (promotion.getName().equals(name)) {
                return promotion;
            }
        }
        return null; // 프로모션을 찾지 못한 경우
    }

    // 프로모션을 적용하는 메서드
    public static List<String> applyPromotions(List<String> purchase, Products product, List<Promotions> promotions) {
        List<String> updatedPurchase = new ArrayList<>(purchase);
        Promotions applicablePromotion = findPromotion(product, promotions, updatedPurchase);

        if (applicablePromotion != null && isEligibleForPromotion(product, updatedPurchase, applicablePromotion)) {
            processPromotion(product, updatedPurchase, applicablePromotion);
        }
        return updatedPurchase;
    }

    // 사용자가 구매할 상품의 프로모션 정보를 찾는 메서드
    private static Promotions findPromotion(Products product, List<Promotions> promotions, List<String> purchase) {
        for (Promotions promotion : promotions) {
            if (promotion.getName().equals(product.promotion) &&
                    isPromotionExists(product) && product.name.equals(purchase.getFirst())) {
                return promotion;
            }
        }

        return null;
    }

    // 사용자가 구매할 상품이 프로모션 할인 기간인지 확인하는 메서드
    public static boolean isCurrentDateInRange(LocalDate startDate, LocalDate endDate) {
        LocalDate currentDate = LocalDate.from(DateTimes.now());

        return !currentDate.isBefore(startDate) && !currentDate.isAfter(endDate);
    }

    // 사용자가 구매할 상품이 프로모션 적용이 되는 상품인지 확인하는 메서드
    private static boolean isEligibleForPromotion(Products product, List<String> purchase, Promotions promotion) {
        return isPromotionExists(product) &&
                product.name.equals(purchase.getFirst()) &&
                Integer.parseInt(purchase.getLast()) >= promotion.getBuy() &&
                Integer.parseInt(purchase.getLast()) <= product.quantity;
    }

    // 사용자가 입력한 값에 따라 프로모션 적용 유무를 결정하는 메서드
    private static void processPromotion(Products product, List<String> purchase, Promotions promotion) {
        if (!isCurrentDateInRange(promotion.getStartDate(), promotion.getEndDate())) {
            return; // 프로모션 기간이 아니면 처리하지 않음
        }

        int purchaseQuantity = Integer.parseInt(purchase.getLast());
        int freeItems = 0;
        int buyCount = promotion.getBuy();

        int promotionApplicableCount = purchaseQuantity / (buyCount + 1);
        int remainder = purchaseQuantity % (buyCount + 1);

        freeItems = promotionApplicableCount;

        boolean isPromotionApplicable = (remainder == buyCount);
        if (isPromotionApplicable) {
            String userInput = confirmYOrN(readPromotionYOrN(product.name));
            boolean userAcceptsPromotion = userInput.equalsIgnoreCase("Y");
            if (userAcceptsPromotion) {
                freeItems += 1;
            }
        }

        purchaseQuantity += freeItems;
        purchase.set(1, String.valueOf(purchaseQuantity));
    }

    // 구매하려는 상품의 개수를 재고에 맞게 차감하는 메서드
    public static void deductedQuantity(List<Products> stock, List<String> purchase) {
        int purchaseQuantity = Integer.parseInt(purchase.getLast());

        for (Products products : stock) {
            if (products.name.equals(purchase.getFirst())) {
                products.quantity -= purchaseQuantity;
                break;
            }
        }
    }

    private static boolean isPromotionExists(Products products) {
        return !products.promotion.isEmpty();
    }

    public static boolean isStockQuantityExceeded(List<Products> stock, List<String> split) {
        for (Products products : stock) {
            if (products.name.equals(split.getFirst()) && products.quantity >= Integer.parseInt(split.getLast())) {
                return true;
            }
        }

        return false;
    }

    public static boolean isProductExists(List<Products> stock, List<String> split) {
        for (Products products : stock) {
            if (products.name.equals(split.getFirst())) {
                return true;
            }
        }

        return false;
    }
}