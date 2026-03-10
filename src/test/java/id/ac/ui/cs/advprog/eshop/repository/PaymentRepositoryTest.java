package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PaymentRepositoryTest {
    private PaymentRepository paymentRepository;
    private Order order;
    private Map<String, String> paymentData;

    @BeforeEach
    void setUp() {
        paymentRepository = new PaymentRepository();

        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(2);
        products.add(product);

        order = new Order("13652556-012a-4c07-b546-54eb1396d79b", products, 1708560000L, "Safira Sudrajat");
        paymentData = new HashMap<>();
    }

    @Test
    void testSavePayment() {
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment("payment-1", "VOUCHER_CODE", order, paymentData);

        Payment savedPayment = paymentRepository.save(payment);
        assertEquals(payment.getId(), savedPayment.getId());
    }

    @Test
    void testFindById() {
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment("payment-1", "VOUCHER_CODE", order, paymentData);
        paymentRepository.save(payment);

        Payment foundPayment = paymentRepository.findById("payment-1");
        assertNotNull(foundPayment);
        assertEquals(payment.getId(), foundPayment.getId());
    }

    @Test
    void testFindByIdIfNotFound() {
        Payment foundPayment = paymentRepository.findById("invalid-id");
        assertNull(foundPayment);
    }

    @Test
    void testFindAll() {
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment1 = new Payment("payment-1", "VOUCHER_CODE", order, paymentData);
        paymentRepository.save(payment1);

        Map<String, String> paymentData2 = new HashMap<>();
        paymentData2.put("bankName", "BCA");
        paymentData2.put("referenceCode", "REF12345");
        Payment payment2 = new Payment("payment-2", "BANK_TRANSFER", order, paymentData2);
        paymentRepository.save(payment2);

        List<Payment> paymentList = paymentRepository.findAll();
        assertEquals(2, paymentList.size());
        assertTrue(paymentList.contains(payment1));
        assertTrue(paymentList.contains(payment2));
    }

    @Test
    void testFindAllIfEmpty() {
        List<Payment> paymentList = paymentRepository.findAll();

        assertNotNull(paymentList);
        assertTrue(paymentList.isEmpty());
        assertEquals(0, paymentList.size());
    }
}