package store.view;

import java.util.List;
import store.value.Products;

public class OutputView {

    // 편의점 개점했다는 메시지를 출력하는 메서드
    public static void printStoreOpenMessage() {
        System.out.println("안녕하세요. W편의점입니다.");
    }

    // 편의점이 보유하고 있는 재고를 출력하는 메서드
    public static void printProducts(List<Products> products) {
        System.out.println("현재 보유하고 있는 상품입니다.\n");

        for (Products product : products) {
            System.out.println(product);
        }
    }

    // 구매 내역을 출력하는 메서드
    public static void printReceipt(int totalQuantity, List<Products> purchasedProducts, int freeItemCount, int totalPurchaseAmount, int promotionDiscountAmount, int membershipDiscountAmount, int finalAmount) {
        System.out.println("==============W 편의점================");
        System.out.println("상품명\t\t수량\t금액");

        for (Products product : purchasedProducts) {
            System.out.printf("%s\t\t%d\t%,d\n", product.getName(), product.getQuantity(), product.getPrice() * product.getQuantity());
        }

        System.out.println("=============증정===============");
        for (Products product : purchasedProducts) {
            int freeItems = freeItemCount; // Store에서 계산된 무료 아이템 수를 사용
            if (freeItems > 0) {
                System.out.printf("%s\t\t%d\n", product.getName(), freeItems);
            }
        }

        System.out.println("====================================");
        System.out.printf("총구매액\t\t%d\t%,d\n", totalQuantity, totalPurchaseAmount);
        System.out.printf("행사할인\t\t-%d\n", promotionDiscountAmount);
        System.out.printf("멤버십할인\t\t-%d\n", membershipDiscountAmount);
        System.out.printf("내실돈\t\t\t%,d\n", finalAmount);
    }
}
