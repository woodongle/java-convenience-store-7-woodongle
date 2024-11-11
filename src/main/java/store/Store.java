package store;

import static store.imports.ImportProducts.loadProducts;
import static store.imports.ImportPromotions.loadPromotions;
import static store.validation.Validation.*;
import static store.value.Products.*;
import static store.view.InputView.*;
import static store.view.OutputView.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import store.value.Products;
import store.value.Promotions;

public class Store {

    private final List<Products> stock = new ArrayList<>();
    private final List<Promotions> promotions = new ArrayList<>();
    private final List<List<String>> purchaseProductAndQuantity = new ArrayList<>();

    public void open() {
        try {
            initializeStore();
            openForBusiness();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    // 편의점 재고를 추가하고 영업을 개시하는 메서드
    private void initializeStore() throws IOException {
        loadProducts(stock);
        loadPromotions(promotions);
        setPromotions(promotions);
        printStoreOpenMessage();
        printProducts(stock);
    }

    // 편의점 영업 프로세스 메서드
    private void openForBusiness() {
        selectProductAndQuantity();
        applyPromotionsProcess();
        deductedQuantityProcess();
        calculateAndDisplayFinalAmount();
    }

    // 프로모션 적용 프로세스 메서드
    private void calculateAndDisplayFinalAmount() {
        List<Products> purchasedProducts = getPurchasedProducts();
        int freeItemCount = calculateTotalFreeItems();

        // 최종 결제 금액 계산
        int totalPurchaseAmount = Products.calculateTotalPurchaseAmount(purchasedProducts);
        int promotionDiscountAmount = Products.calculatePromotionDiscountAmount(purchasedProducts, freeItemCount);

        // 멤버십 할인 여부 확인 (한 번만 물어봄)
        boolean applyMembershipDiscount = askForMembershipDiscount();
        int membershipDiscountAmount = 0;
        if (applyMembershipDiscount) {
            membershipDiscountAmount = calculateMembershipDiscountAmount(totalPurchaseAmount, promotionDiscountAmount);
        }

        int finalAmount = totalPurchaseAmount - promotionDiscountAmount - membershipDiscountAmount;

        // 총 구매한 상품 수 계산
        int totalQuantity = calculateTotalQuantity(purchasedProducts);

        // 구매 요약 출력
        printPurchaseSummary(totalQuantity, purchasedProducts, freeItemCount, totalPurchaseAmount, promotionDiscountAmount, membershipDiscountAmount, finalAmount);
    }

    private boolean askForMembershipDiscount() {
        String userInput = confirmYOrN(readMembershipYOrN());
        return userInput.equalsIgnoreCase("Y");
    }

    private int calculateMembershipDiscountAmount(int totalPurchaseAmount, int promotionDiscountAmount) {
        int nonPromotionTotal = totalPurchaseAmount - promotionDiscountAmount;
        return (int)(nonPromotionTotal * 0.3); // 30% 멤버십 할인
    }

    private int calculateTotalQuantity(List<Products> purchasedProducts) {
        int totalQuantity = 0;
        for (Products product : purchasedProducts) {
            totalQuantity += product.getQuantity();
        }
        return totalQuantity;
    }


    // 구매한 상품 목록을 생성하는 메서드
    private List<Products> getPurchasedProducts() {
        List<Products> purchasedProducts = new ArrayList<>();
        for (List<String> purchase : purchaseProductAndQuantity) {
            addPurchasedProduct(purchasedProducts, purchase);
        }
        return purchasedProducts;
    }

    // 구매한 상품을 목록에 추가하는 메서드
    private void addPurchasedProduct(List<Products> purchasedProducts, List<String> purchase) {
        String productName = purchase.getFirst();
        int quantity = Integer.parseInt(purchase.getLast());
        Products product = findProductByName(productName);
        if (product != null) {
            purchasedProducts.add(new Products(product.getName(), product.getPrice(), quantity, product.getPromotion()));
        }
    }

    // 구매할 상품을 찾는 메서드
    private Products findProductByName(String name) {
        for (Products product : stock) {
            if (product.getName().equals(name)) {
                return product;
            }
        }
        return null;
    }

    // 무료 아이템 개수를 계산하는 메서드
    private int calculateTotalFreeItems() {
        int totalFreeItems = 0;
        for (List<String> purchase : purchaseProductAndQuantity) {
            totalFreeItems += calculateFreeItemsForPurchase(purchase);
        }
        return totalFreeItems;
    }

    // 각 구매에 대한 무료 아이템 수를 계산하는 메서드
    private int calculateFreeItemsForPurchase(List<String> purchase) {
        Products product = findProductByName(purchase.getFirst());
        if (product == null || product.getPromotion().isEmpty()) {
            return 0;
        }
        Promotions promotion = findPromotionByName(product.getPromotion());
        if (promotion == null) {
            return 0;
        }
        int purchasedQuantity = Integer.parseInt(purchase.getLast());
        return calculateFreeItems(purchasedQuantity, promotion.getBuy());
    }

    // 구매 수량과 프로모션 조건에 따라 무료 아이템 수를 계산하는 메서드
    private int calculateFreeItems(int purchaseQuantity, int buyCount) {
        return purchaseQuantity / (buyCount + 1);
    }

    // 구매할 상품의 프로모션을 찾는 메서드
    private Promotions findPromotionByName(String name) {
        for (Promotions promotion : promotions) {
            if (promotion.getName().equals(name)) {
                return promotion;
            }
        }
        return null;
    }

    // 구매한 상품에 프로모션을 적용하는 프로세스
    private void applyPromotionsProcess() {
        try {
            List<List<String>> updatedPurchases = new ArrayList<>();
            for (List<String> purchase : purchaseProductAndQuantity) {
                updatedPurchases.add(applyPromotionToPurchase(purchase));
            }
            purchaseProductAndQuantity.clear();
            purchaseProductAndQuantity.addAll(updatedPurchases);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            applyPromotionsProcess();
        }
    }

    // 구매할 상품과 수량을 처리하는 메서드
    private List<String> applyPromotionToPurchase(List<String> purchase) {
        String productName = purchase.getFirst();
        Products product = findProductByName(productName);
        if (product == null) {
            return purchase;
        }
        return applyPromotions(new ArrayList<>(purchase), product, promotions);
    }

    // 상품 개수 차감 프로세스 메서드
    private void deductedQuantityProcess() {
        for (List<String> purchase : purchaseProductAndQuantity) {
            deductedQuantity(stock, purchase);
        }
    }

    // 구매할 상품이 존재하는지 검증한 후 상품과 개수를 저장하는 메서드
    private void saveProductAndQuantity(String replaceInput) {
        List<String> split = List.of(replaceInput.split("-"));
        List<String> confirmedPurchase = confirmStockQuantityExceeded(stock, confirmProductExists(stock, split));
        purchaseProductAndQuantity.add(confirmedPurchase);
    }

    // 구매할 상품과 개수를 입력 받아 형식이 맞는지 검증한 후 저장하는 메서드
    private void selectProductAndQuantity() {
        try {
            List<String> productAndQuantity = readProductAndQuantity();
            processProductAndQuantity(productAndQuantity);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            selectProductAndQuantity();
        }
    }

    private void processProductAndQuantity(List<String> productAndQuantity) {
        for (String string : productAndQuantity) {
            String formattedInput = confirmInputFormat(string).replace("[", "").replace("]", "");
            saveProductAndQuantity(formattedInput);
        }
    }
}