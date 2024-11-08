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
}
