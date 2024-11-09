package store;

import static store.validation.Validation.*;
import static store.view.InputView.*;
import static store.view.OutputView.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import store.value.Products;

public class Store {

    private final List<Products> stock = new ArrayList<>();
    private final List<List<String>> purchaseProductAndQuantity = new ArrayList<>();

    public void open() {
        try {
            loadProducts();
            printStoreOpenMessage();
            printProducts(stock);
            openForBusiness();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    // 편의점 영업 프로세스 메서드
    private void openForBusiness() {
        selectProductAndQuantity();
    }

    // 구매할 상품이 존재하는지 검증한 후 상품과 개수를 저장하는 메서드
    private void saveProductAndQuantity(String replaceInput) {
        List<String> split = List.of(replaceInput.split("-"));

        purchaseProductAndQuantity.add(
                confirmStockQuantityExceeded(stock, confirmProductExists(stock, split)));
    }

    // 구매할 상품과 개수를 입력 받아 형식이 맞는지 검증한 후 저장하는 메서드
    private void selectProductAndQuantity() {
        try {
            List<String> productAndQuantity = readProductAndQuantity();

            for (String string : productAndQuantity) {
                saveProductAndQuantity(confirmInputFormat(string)
                        .replace("[", "").replace("]", ""));
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            selectProductAndQuantity();
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
    private void saveProducts(String[] product) {

        // 첫 줄은 제외하고 저장하기 위해 인덱스 0번째 값이 "name"이 아닌 배열만 사용
        if (!product[0].equals("name")) {
            stock.add(new Products(product[0], Integer.parseInt(product[1])
                    , Integer.parseInt(product[2]), product[3]));
        }
    }
}
