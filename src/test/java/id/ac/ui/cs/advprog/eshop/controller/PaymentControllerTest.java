package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    @InjectMocks
    PaymentController paymentController;

    @Mock
    PaymentService paymentService;

    @Mock
    Model model;

    Payment payment;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(2);
        products.add(product);

        Order order = new Order("order-1", products, 123456789L, "Rafasya");
        payment = new Payment("payment-1", "BANK_TRANSFER", order, new HashMap<>());
    }

    @Test
    void testPaymentDetailFormPage() {
        String viewName = paymentController.paymentDetailFormPage();
        assertEquals("paymentDetailForm", viewName);
    }

    @Test
    void testPaymentDetailPage() {
        when(paymentService.getPayment("payment-1")).thenReturn(payment);
        String viewName = paymentController.paymentDetailPage("payment-1", model);
        verify(model).addAttribute("payment", payment);
        assertEquals("paymentDetail", viewName);
    }

    @Test
    void testPaymentAdminListPage() {
        List<Payment> payments = new ArrayList<>();
        payments.add(payment);
        when(paymentService.getAllPayments()).thenReturn(payments);

        String viewName = paymentController.paymentAdminListPage(model);

        verify(model).addAttribute("payments", payments);
        assertEquals("paymentAdminList", viewName);
    }

    @Test
    void testPaymentAdminDetailPage() {
        when(paymentService.getPayment("payment-1")).thenReturn(payment);
        String viewName = paymentController.paymentAdminDetailPage("payment-1", model);
        verify(model).addAttribute("payment", payment);
        assertEquals("paymentAdminDetail", viewName);
    }

    @Test
    void testPaymentAdminSetStatusPost() {
        when(paymentService.getPayment("payment-1")).thenReturn(payment);
        String viewName = paymentController.paymentAdminSetStatusPost("payment-1", "SUCCESS");
        verify(paymentService).setStatus(payment, "SUCCESS");
        assertEquals("redirect:/payment/admin/list", viewName);
    }
}