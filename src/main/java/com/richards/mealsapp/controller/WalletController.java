package com.richards.mealsapp.controller;

import com.richards.mealsapp.dto.WalletInfoResponse;
import com.richards.mealsapp.dto.WalletRequest;
import com.richards.mealsapp.dto.WalletResponse;
import com.richards.mealsapp.response.BaseResponse;
import com.richards.mealsapp.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class WalletController {
    private final WalletService walletService;

    @GetMapping("/customer/wallet/balance")
    public BaseResponse<String> getWalletBalance() {
        return walletService.getWalletBalance();
    }

    @PostMapping("/customer/wallet/fund")
    public BaseResponse<WalletResponse> fundWallet(@RequestBody WalletRequest walletRequest) {
        return walletService.fundWallet(walletRequest);
    }

    @PostMapping("/customer/wallet/process-payment")
    public BaseResponse<String> processPayment(@RequestBody WalletRequest walletRequest) {
        return walletService.processPayment(walletRequest);
    }

    @GetMapping("customer/wallet/transactions")
    public ResponseEntity<Object> fetchAllTrans(@RequestParam(defaultValue = "0") Integer pageNo,
                                                @RequestParam(defaultValue = "10") Integer pageSize,
                                                @RequestParam(defaultValue = "id") String sortBy) {

        return ResponseEntity.ok(walletService.fetchAllTransactions(pageNo, pageSize, sortBy));
    }

    @GetMapping("customer/wallet/info")
    public BaseResponse<WalletInfoResponse> walletInfo(){
        return walletService.getCustomerWalletInfo();
    }

}
