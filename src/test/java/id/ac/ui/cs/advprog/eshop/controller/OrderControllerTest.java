package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @InjectMocks
    OrderController orderController;

    @Mock
    OrderService orderService;

    @Mock
    Model model;

    Order order;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(2);
        products.add(product);

        order = new Order("order-1", products, 123456789L, "Rafasya");
    }

    @Test
    void testCreateOrderPage() {
        String viewName = orderController.createOrderPage();
        assertEquals("CreateOrder", viewName);
    }

    @Test
    void testHistoryOrderPage() {
        String viewName = orderController.historyOrderPage();
        assertEquals("OrderHistory", viewName);
    }

    @Test
    void testShowHistory() {
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        when(orderService.findAllByAuthor("Rafasya")).thenReturn(orders);

        String viewName = orderController.showHistory("Rafasya", model);

        verify(model).addAttribute("orders", orders);
        verify(model).addAttribute("author", "Rafasya");
        assertEquals("OrderList", viewName);
    }

    @Test
    void testPayOrderPage() {
        when(orderService.findById("order-1")).thenReturn(order);
        String viewName = orderController.payOrderPage("order-1", model);
        verify(model).addAttribute("order", order);
        assertEquals("PaymentOrder", viewName);
    }

    @Test
    void testPayOrderPost() {
        String viewName = orderController.payOrder("order-1", model);
        verify(model).addAttribute(eq("paymentId"), anyString());
        assertEquals("PaymentSuccess", viewName);
    }
}