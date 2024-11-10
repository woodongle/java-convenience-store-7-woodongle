package store.value;

import static store.validation.Validation.confirmYOrN;
import static store.view.InputView.readPromotionYOrN;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Products {
    public static int NUMBER_OF_ADDITIONAL_GIFTS = 1;

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
                    isPromotionExists(product) &&
                    product.name.equals(purchase.getFirst())) {
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
                Integer.parseInt(purchase.getLast()) <= product.quantity &&
                isCurrentDateInRange(promotion.getStartDate(), promotion.getEndDate());
    }

    // 사용자가 입력한 값에 따라 프로모션 적용 유무를 결정하는 메서드
    private static void processPromotion(Products product, List<String> purchase, Promotions promotion) {
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
