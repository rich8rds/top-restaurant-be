package com.richards.mealsapp.service;

import com.richards.mealsapp.dto.FundWalletRequest;
import com.richards.mealsapp.dto.FundWalletResponse;
import com.richards.mealsapp.response.BaseResponse;

public interface WalletService {
    BaseResponse<FundWalletResponse> fundWallet(FundWalletRequest fundWalletRequest);

    BaseResponse<String> getWalletBalance();
}
