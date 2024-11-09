package store.validation;

public class Validation {

    public static String confirmInputFormat(String input) {
        if (!input.matches("^\\[([가-힣]*)-([1-9]\\d*)]$")) {
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }

        return input;
    }
}
