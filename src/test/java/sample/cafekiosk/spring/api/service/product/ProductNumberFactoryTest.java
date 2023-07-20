package sample.cafekiosk.spring.api.service.product;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import sample.cafekiosk.spring.IntegrationTestSupport;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

class ProductNumberFactoryTest extends IntegrationTestSupport {

    @Autowired
    ProductNumberFactory productNumberFactory;

    @Autowired
    private ProductRepository productRepository;

    @AfterEach
    void tearDown() {
        productRepository.deleteAllInBatch();
    }

    @DisplayName("가장 마지막으로 저장한 상품이 없을 때, 문자열 001을 반환한다.")
    @Test
    void createProductNumber001WhenNoProduct() {
        // when
        String nextProductNumber = productNumberFactory.createNextProductNumber();

        // then
        assertThat(nextProductNumber).isEqualTo("001");
    }

    @DisplayName("가장 마지막으로 저장한 상품의 상품 번호의 1 증가된 값을 반환한다.")
    @Test
    void createNextProductNumber() {
        // given
        Product product1 = createProduct("001");
        Product product2 = createProduct("002");
        Product product3 = createProduct("003");
        productRepository.saveAll(List.of(product1, product2, product3));

        // when
        String nextProductNumber = productNumberFactory.createNextProductNumber();

        // then
        assertThat(nextProductNumber).isEqualTo("004");
    }

    private Product createProduct(String productNumber) {
        return Product.builder()
            .productNumber(productNumber)
            .type(HANDMADE)
            .sellingStatus(SELLING)
            .name("아메리카노")
            .price(1000)
            .build();
    }
}
