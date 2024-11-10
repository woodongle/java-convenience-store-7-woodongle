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
            loadProducts(stock);
            loadPromotions(promotions);
            printStoreOpenMessage();
            printProducts(stock);
            openForBusiness();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    // 편의점 영업 프로세스 메서드
    private void openForBusiness() {
        selectProductAndQuantity();
        applyPromotionsProcess();
        deductedQuantityProcess();
    }

    // 프로모션 적용 프로세스 메서드
    private void applyPromotionsProcess() {
        List<List<String>> updatedPurchases = new ArrayList<>();
        for (List<String> purchase : purchaseProductAndQuantity) {
            List<String> updatedPurchase = new ArrayList<>(purchase);
            String productName = purchase.get(0);

            for (Products product : stock) {
                if (product.getName().equals(productName)) {
                    updatedPurchase = applyPromotions(updatedPurchase, product, promotions);
                    break;
                }
            }
            updatedPurchases.add(updatedPurchase);
        }
        purchaseProductAndQuantity.clear();
        purchaseProductAndQuantity.addAll(updatedPurchases);
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

        purchaseProductAndQuantity.add(
                confirmStockQuantityExceeded(stock, confirmProductExists(stock, split)));
    }

    // 구매할 상품과 개수를 입력 받아 형식이 맞는지 검증한 후 저장하는 메서드
    private void selectProductAndQuantity() {
        try {
            List<String> productAndQuantity = readProductAndQuantity();

            for (String string : productAndQuantity) {
                saveProductAndQuantity(confirmInputFormat(string)
                        .replace("[", "").replace("]", ""));
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            selectProductAndQuantity();
        }
    }
}
