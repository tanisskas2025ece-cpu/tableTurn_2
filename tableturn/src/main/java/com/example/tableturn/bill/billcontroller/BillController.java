package com.example.tableturn.bill.billcontroller;

import com.example.tableturn.bill.billdto.BillDTO;
import com.example.tableturn.bill.billservice.BillService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    @PostMapping
    public BillDTO createBill(@RequestBody BillDTO dto) {
        return billService.createBill(dto);
    }

    @GetMapping
    public List<BillDTO> getAllBills() {
        return billService.getAllBills();
    }

    @GetMapping("/{id}")
    public BillDTO getBillById(@PathVariable Long id) {
        return billService.getBillById(id);
    }

    @GetMapping("/order/{orderId}")
    public BillDTO getBillByOrderId(@PathVariable Long orderId) {
        return billService.getBillByOrderId(orderId);
    }

    @PutMapping("/{id}")
    public BillDTO updateBill(
            @PathVariable Long id,
            @RequestBody BillDTO dto) {
        return billService.updateBill(id, dto);
    }

    @DeleteMapping("/{id}")
    public String deleteBill(@PathVariable Long id) {
        billService.deleteBill(id);
        return "Bill deleted successfully";
    }
}