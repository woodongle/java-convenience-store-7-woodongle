package store.validation;

import static store.value.Products.*;

import java.util.List;
import store.value.Products;

public class Validation {

    public static List<String> confirmStockQuantityExceeded(List<Products> stock, List<String> split) {
        if (!isStockQuantityExceeded(stock, split)) {
            throw new IllegalArgumentException("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
        }

        return split;
    }

    public static List<String> confirmProductExists(List<Products> stock, List<String> split) {
        if (!isProductExists(stock, split)) {
            throw new IllegalArgumentException("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.");
        }

        return split;
    }

    public static String confirmInputFormat(String input) {
        if (!input.matches("^\\[([가-힣]*)-([1-9]\\d*)]$")) {
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }

        return input;
    }
}
