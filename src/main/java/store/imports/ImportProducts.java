package store.imports;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import store.value.Products;

public class ImportProducts {

    // products.md 파일에 있는 상품 정보를 가져오는 메서드
    public static void loadProducts(List<Products> stock) throws IOException {
        FileReader fileReader = new FileReader("src/main/resources/products.md");
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String string = "";

        while ((string = bufferedReader.readLine()) != null) {
            String[] product = string.split(",");
            saveProducts(stock, product);
        }
    }

    // products.md 파일에서 가져온 상품을 저장하는 메서드
    public static void saveProducts(List<Products> stock, String[] product) {

        // 첫 줄은 제외하고 저장하기 위해 인덱스 0번째 값이 "name"이 아닌 배열만 사용
        if (!product[0].equals("name")) {
            stock.add(new Products(product[0], Integer.parseInt(product[1])
                    , Integer.parseInt(product[2]), product[3]));
        }
    }
}
