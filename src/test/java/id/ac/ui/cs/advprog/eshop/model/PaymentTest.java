package id.ac.ui.cs.advprog.eshop.model;

import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {
    private Order order;
    private Map<String, String> paymentData;

    @BeforeEach
    void setUp() {
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
    void testCreatePaymentVoucherCodeSuccess() {
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment("payment-1", PaymentMethod.VOUCHER_CODE.getValue(), order, paymentData);
        assertEquals(PaymentStatus.SUCCESS.getValue(), payment.getStatus());
    }

    @Test
    void testCreatePaymentVoucherCodeRejected() {
        paymentData.put("voucherCode", "ESHOP1234");
        Payment payment = new Payment("payment-2", PaymentMethod.VOUCHER_CODE.getValue(), order, paymentData);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testCreatePaymentBankTransferSuccess() {
        paymentData.put("bankName", "BCA");
        paymentData.put("referenceCode", "REF123456789");
        Payment payment = new Payment("payment-3", PaymentMethod.BANK_TRANSFER.getValue(), order, paymentData);
        assertEquals(PaymentStatus.SUCCESS.getValue(), payment.getStatus());
    }

    @Test
    void testCreatePaymentBankTransferRejectedEmptyBankName() {
        paymentData.put("bankName", "");
        paymentData.put("referenceCode", "REF123456789");
        Payment payment = new Payment("payment-4", PaymentMethod.BANK_TRANSFER.getValue(), order, paymentData);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testCreatePaymentBankTransferRejectedEmptyReference() {
        paymentData.put("bankName", "BCA");
        paymentData.put("referenceCode", "");
        Payment payment = new Payment("payment-5", PaymentMethod.BANK_TRANSFER.getValue(), order, paymentData);
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testSetStatusToSuccess() {
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment("payment-1", PaymentMethod.VOUCHER_CODE.getValue(), order, paymentData);
        payment.setStatus(PaymentStatus.SUCCESS.getValue());
        assertEquals(PaymentStatus.SUCCESS.getValue(), payment.getStatus());
    }

    @Test
    void testSetStatusToRejected() {
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment("payment-1", PaymentMethod.VOUCHER_CODE.getValue(), order, paymentData);
        payment.setStatus(PaymentStatus.REJECTED.getValue());
        assertEquals(PaymentStatus.REJECTED.getValue(), payment.getStatus());
    }

    @Test
    void testSetStatusToInvalidStatusThrowsException() {
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment("payment-1", PaymentMethod.VOUCHER_CODE.getValue(), order, paymentData);

        assertThrows(IllegalArgumentException.class, () -> {
            payment.setStatus("PENDING");
        });
    }

    @Test
    void testCreatePaymentWithSpecificStatusSuccess() {
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment("payment-1", PaymentMethod.VOUCHER_CODE.getValue(), order, paymentData, PaymentStatus.SUCCESS.getValue());
        assertEquals(PaymentStatus.SUCCESS.getValue(), payment.getStatus());
    }

    @Test
    void testCreatePaymentWithInvalidSpecificStatusThrowsException() {
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        assertThrows(IllegalArgumentException.class, () -> {
            new Payment("payment-1", PaymentMethod.VOUCHER_CODE.getValue(), order, paymentData, "UNKNOWN_STATUS");
        });
    }
}