package store.view;

import camp.nextstep.edu.missionutils.Console;
import java.util.List;

public class InputView {

    // 구매할 상품과 개수를 입력하는 메서드
    public static List<String> readProductAndQuantity() {
        System.out.println("\n구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");

        List<String> inputProductAndQuantity = List.of(Console.readLine().trim().split(","));
        return inputProductAndQuantity;
    }
}
