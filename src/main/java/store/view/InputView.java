package store.view;

import camp.nextstep.edu.missionutils.Console;
import java.util.List;

public class InputView {

    // 구매할 상품과 개수를 입력하는 메서드
    public static List<String> readProductAndQuantity() {
        System.out.println("\n구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
        return List.of(Console.readLine().trim().split(","));
    }

    // 프로모션을 적용 유무를 입력하는 메서드
    public static String readPromotionYOrN(String productName) {
        System.out.println("현재 " + productName + "은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)");
        return Console.readLine();
    }

    // 멤버십 할인 적용 유무를 입력하는 메서드
    public static String readMembershipYOrN() {
        System.out.println("멤버십 할인을 받으시겠습니까? (Y/N)");
        return Console.readLine();
    }

    public static String printRepurchase() {
        System.out.println("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");
        return Console.readLine().trim();
    }
}
