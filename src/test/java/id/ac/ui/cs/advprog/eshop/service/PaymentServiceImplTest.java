package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private PaymentRepository paymentRepository;

    private Order order;
    private Map<String, String> paymentData;

    @BeforeEach
    void setUp() {
        // Buat dummy product agar list tidak null / kosong
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(2);
        products.add(product);

        // Masukkan list products ke dalam order
        order = new Order("order-1", products, 1708560000L, "Safira");
        paymentData = new HashMap<>();
    }

    @Test
    void testAddPaymentBankTransferSuccess() {
        paymentData.put("bankName", "BCA");
        paymentData.put("referenceCode", "REF12345");
        Payment payment = new Payment("payment-1", "BANK_TRANSFER", order, paymentData);

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment result = paymentService.addPayment(order, "BANK_TRANSFER", paymentData);

        assertEquals("SUCCESS", result.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentVoucherSuccess() {
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment("payment-2", "VOUCHER_CODE", order, paymentData);

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment result = paymentService.addPayment(order, "VOUCHER_CODE", paymentData);

        assertNotNull(result);
        assertEquals("SUCCESS", result.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testSetStatusSuccess() {
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment("payment-3", "VOUCHER_CODE", order, paymentData);

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment result = paymentService.setStatus(payment, "REJECTED");

        assertNotNull(result);
        assertEquals("REJECTED", result.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testGetPaymentSuccess() {
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment("payment-4", "VOUCHER_CODE", order, paymentData);

        when(paymentRepository.findById("payment-4")).thenReturn(payment);

        Payment result = paymentService.getPayment("payment-4");

        assertNotNull(result);
        assertEquals(payment.getId(), result.getId());
        verify(paymentRepository, times(1)).findById("payment-4");
    }

    @Test
    void testGetPaymentNotFound() {
        when(paymentRepository.findById("invalid-id")).thenReturn(null);

        Payment result = paymentService.getPayment("invalid-id");

        assertNull(result);
        verify(paymentRepository, times(1)).findById("invalid-id");
    }
}