package sample.cafekiosk.spring.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static sample.cafekiosk.spring.domain.order.OrderStatus.PAYMENT_COMPLETED;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

@DataJpaTest
@ActiveProfiles("test")
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("주문의 등록 시간이 시작과 끝 시간 사이이고 상품상태가 결제완료 상태인 주문들을 조회한다.")
    @Test
    void findOrdersBy() {
        // given
        LocalDateTime targetDateTime = LocalDateTime.of(2023, 7, 17, 23, 0);

        Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
        Product product2 = createProduct("002", HANDMADE, HOLD, "카페라떼", 4500);
        Product product3 = createProduct("003", HANDMADE, STOP_SELLING, "팥빙수", 7000);
        List<Product> products = productRepository.saveAll(List.of(product1, product2, product3));

        Order order1 = Order.create(List.of(product1, product2, product3), targetDateTime);
        Order order2 = Order.create(List.of(product2, product3), LocalDateTime.of(2023, 7, 18, 0, 0));
        orderRepository.saveAll(List.of(order1, order2));

        // when
        List<Order> ordersBy = orderRepository.findOrdersBy(
            LocalDateTime.of(2023, 7, 17, 0, 1),
            LocalDateTime.of(2023, 7, 17, 23, 59),
            PAYMENT_COMPLETED
        );

        // then
        assertThat(ordersBy).hasSize(1)
            .extracting("orderStatus", "registeredDateTime")
            .containsExactlyInAnyOrder(
                tuple(PAYMENT_COMPLETED, targetDateTime)
            );
    }

    private Product createProduct(String productNumber, ProductType type, ProductSellingStatus sellingStatus, String name, int price) {
        return Product.builder()
            .productNumber(productNumber)
            .type(type)
            .sellingStatus(sellingStatus)
            .name(name)
            .price(price)
            .build();
    }
}