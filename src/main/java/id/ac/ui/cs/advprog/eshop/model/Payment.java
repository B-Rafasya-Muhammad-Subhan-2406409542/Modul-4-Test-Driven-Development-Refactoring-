package id.ac.ui.cs.advprog.eshop.model;

import lombok.Getter;
import java.util.Map;

@Getter
public class Payment {
    private String id;
    private String method;
    private Order order;
    private Map<String, String> paymentData;
    private String status;

    public Payment(String id, String method, Order order, Map<String, String> paymentData) {
        this.id = id;
        this.method = method;
        this.order = order;
        this.paymentData = paymentData;

        boolean valid = false;

        if ("VOUCHER_CODE".equals(method)) {
            String voucher = paymentData.get("voucherCode");
            if (voucher != null && voucher.length() == 16 && voucher.startsWith("ESHOP")) {
                long numCount = voucher.chars().filter(Character::isDigit).count();
                if (numCount == 8) valid = true;
            }
        }
        else if ("BANK_TRANSFER".equals(method)) {
            String bankName = paymentData.get("bankName");
            String refCode = paymentData.get("referenceCode");

            if (bankName != null && !bankName.trim().isEmpty() &&
                    refCode != null && !refCode.trim().isEmpty()) {
                valid = true;
            }
        }

        this.status = valid ? "SUCCESS" : "REJECTED";
    }

    public Payment(String id, String method, Order order, Map<String, String> paymentData, String status) {
        this(id, method, order, paymentData);
        this.setStatus(status);
    }

    public void setStatus(String status) {
        if ("SUCCESS".equals(status) || "REJECTED".equals(status)) {
            this.status = status;
        } else {
            throw new IllegalArgumentException();
        }
    }
}