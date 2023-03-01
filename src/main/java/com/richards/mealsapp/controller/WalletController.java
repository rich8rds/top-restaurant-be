package com.richards.mealsapp.controller;

import com.richards.mealsapp.dto.FundWalletRequest;
import com.richards.mealsapp.dto.FundWalletResponse;
import com.richards.mealsapp.response.BaseResponse;
import com.richards.mealsapp.service.WalletService;
import lombok.RequiredArgsConstructor;
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
    public BaseResponse<FundWalletResponse> fundWallet(@RequestBody FundWalletRequest fundWalletRequest) {
        return walletService.fundWallet(fundWalletRequest);
    }
}
