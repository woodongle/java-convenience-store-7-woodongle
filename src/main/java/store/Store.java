package store;

import static store.view.InputView.*;
import static store.view.OutputView.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import store.value.Products;

public class Store {

    private final List<Products> products = new ArrayList<>();

    public void open() {
        try {
            loadProducts();
            printStoreOpenMessage();
            printProducts(products);
            readProductAndQuantity();
        } catch (IllegalArgumentException | IOException e) {
            e.getMessage();
        }
    }

    // products.md 파일에 있는 상품 정보를 가져오는 메서드
    private void loadProducts() throws IOException {
        FileReader fileReader = new FileReader("src/main/resources/products.md");
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String string = "";

        while ((string = bufferedReader.readLine()) != null) {
            String[] product = string.split(",");
            saveProducts(product);
        }
    }

    // products.md 파일에서 가져온 상품을 저장하는 메서드
    public void saveProducts(String[] product) {

        // 첫 줄은 제외하고 저장하기 위해 인덱스 0번째 값이 "name"이 아닌 배열만 사용
        if (!product[0].equals("name")) {
            products.add(new Products(product[0], Integer.parseInt(product[1])
                    , Integer.parseInt(product[2]), product[3]));
        }
    }
}
