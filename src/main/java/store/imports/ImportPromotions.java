package store.imports;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import store.value.Promotions;

public class ImportPromotions {

    // promotions.md 파일에 있는 프로모션 정보를 가져오는 메서드
    public static void loadPromotions(List<Promotions> promotions) throws IOException {
        FileReader fileReader = new FileReader("src/main/resources/promotions.md");
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String string = "";

        while ((string = bufferedReader.readLine()) != null) {
            String[] promotion = string.split(",");
            savePromotions(promotions, promotion);
        }
    }

    // promotions.md 파일에서 가져온 프로모션을 저장하는 메서드
    public static void savePromotions(List<Promotions> promotions, String[] promotion) {

        // 첫 줄은 제외하고 저장하기 위해 인덱스 0번째 값이 "name"이 아닌 배열만 사용
        if (!promotion[0].equals("name")) {
            promotions.add(new Promotions(promotion[0], Integer.parseInt(promotion[1])
                    , Integer.parseInt(promotion[2]), LocalDate.parse(promotion[3])
                    , LocalDate.parse(promotion[4])));
        }
    }
}
