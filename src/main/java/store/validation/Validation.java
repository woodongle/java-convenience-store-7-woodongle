package store.validation;

import java.util.List;
import store.value.Products;

public class Validation {

    public static List<String> confirmProductExists(List<Products> stock, List<String> split) {
        if (!Products.isProductExists(stock, split)) {
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
