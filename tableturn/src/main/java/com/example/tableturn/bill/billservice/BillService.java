package com.example.tableturn.bill.billservice;

import com.example.tableturn.bill.billdto.BillDTO;
import com.example.tableturn.bill.billentity.Bill;
import com.example.tableturn.bill.billrepository.BillRepository;
import com.example.tableturn.order.orderentity.FoodOrder;
import com.example.tableturn.order.orderrepository.FoodOrderRepository;
import com.example.tableturn.orderitem.orderitementity.OrderItem;
import com.example.tableturn.orderitem.orderitemrepository.OrderItemRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class BillService {

    private final BillRepository billRepository;
    private final FoodOrderRepository foodOrderRepository;
    private final OrderItemRepository orderItemRepository;

    public BillService(
            BillRepository billRepository,
            FoodOrderRepository foodOrderRepository,
            OrderItemRepository orderItemRepository) {
        this.billRepository = billRepository;
        this.foodOrderRepository = foodOrderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public BillDTO createBill(BillDTO dto) {

        validateBill(dto);

        FoodOrder order = foodOrderRepository.findById(dto.getOrderId())
                .orElseThrow(() ->
                        new RuntimeException("Order not found"));

        if (billRepository.findByOrderId(dto.getOrderId()).isPresent()) {
            throw new RuntimeException("A bill already exists for this order");
        }

        List<OrderItem> items =
                orderItemRepository.findByOrderId(order.getId());

        if (items.isEmpty()) {
            throw new RuntimeException("No order items found for this order");
        }

        BigDecimal subtotal = items.stream()
                .map(OrderItem::getSubtotal)
                .filter(amount -> amount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal taxPercent = dto.getTaxPercent();
        BigDecimal taxAmount = subtotal
                .multiply(taxPercent)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

        BigDecimal discount = dto.getDiscount();

        if (discount.compareTo(subtotal.add(taxAmount)) > 0) {
            throw new RuntimeException("Discount cannot exceed bill amount");
        }

        BigDecimal totalAmount = subtotal
                .add(taxAmount)
                .subtract(discount)
                .setScale(2, RoundingMode.HALF_UP);

        Bill bill = new Bill();
        bill.setOrderId(order.getId());
        bill.setSubtotal(subtotal);
        bill.setTaxPercent(taxPercent);
        bill.setTaxAmount(taxAmount);
        bill.setDiscount(discount);
        bill.setTotalAmount(totalAmount);
        bill.setPaymentStatus("UNPAID");
        bill.setPaymentMethod(dto.getPaymentMethod());

        return convertToDTO(billRepository.save(bill));
    }

    public List<BillDTO> getAllBills() {
        return billRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public BillDTO getBillById(Long id) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Bill not found"));

        return convertToDTO(bill);
    }

    public BillDTO getBillByOrderId(Long orderId) {
        Bill bill = billRepository.findByOrderId(orderId)
                .orElseThrow(() ->
                        new RuntimeException("Bill not found for this order"));

        return convertToDTO(bill);
    }

    public BillDTO updateBill(Long id, BillDTO dto) {

        validateBill(dto);

        Bill bill = billRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Bill not found"));

        FoodOrder order = foodOrderRepository.findById(dto.getOrderId())
                .orElseThrow(() ->
                        new RuntimeException("Order not found"));

        List<OrderItem> items =
                orderItemRepository.findByOrderId(order.getId());

        if (items.isEmpty()) {
            throw new RuntimeException("No order items found for this order");
        }

        BigDecimal subtotal = items.stream()
                .map(OrderItem::getSubtotal)
                .filter(amount -> amount != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal taxAmount = subtotal
                .multiply(dto.getTaxPercent())
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

        BigDecimal discount = dto.getDiscount();

        if (discount.compareTo(subtotal.add(taxAmount)) > 0) {
            throw new RuntimeException("Discount cannot exceed bill amount");
        }

        BigDecimal totalAmount = subtotal
                .add(taxAmount)
                .subtract(discount)
                .setScale(2, RoundingMode.HALF_UP);

        bill.setOrderId(order.getId());
        bill.setSubtotal(subtotal);
        bill.setTaxPercent(dto.getTaxPercent());
        bill.setTaxAmount(taxAmount);
        bill.setDiscount(discount);
        bill.setTotalAmount(totalAmount);

        if (dto.getPaymentStatus() != null) {
            bill.setPaymentStatus(dto.getPaymentStatus());
        }

        bill.setPaymentMethod(dto.getPaymentMethod());

        return convertToDTO(billRepository.save(bill));
    }

    public void deleteBill(Long id) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Bill not found"));

        billRepository.delete(bill);
    }

    private void validateBill(BillDTO dto) {

        if (dto.getOrderId() == null) {
            throw new RuntimeException("Order ID is required");
        }

        if (dto.getTaxPercent() == null
                || dto.getTaxPercent().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Tax percentage cannot be negative");
        }

        if (dto.getDiscount() == null
                || dto.getDiscount().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Discount cannot be negative");
        }
    }

    private BillDTO convertToDTO(Bill bill) {

        BillDTO dto = new BillDTO();

        dto.setId(bill.getId());
        dto.setOrderId(bill.getOrderId());
        dto.setBillDate(bill.getBillDate());
        dto.setSubtotal(bill.getSubtotal());
        dto.setTaxPercent(bill.getTaxPercent());
        dto.setTaxAmount(bill.getTaxAmount());
        dto.setDiscount(bill.getDiscount());
        dto.setTotalAmount(bill.getTotalAmount());
        dto.setPaymentStatus(bill.getPaymentStatus());
        dto.setPaymentMethod(bill.getPaymentMethod());

        return dto;
    }
}
