package com.richards.mealsapp.service;

import com.richards.mealsapp.dto.TransactionResponse;
import com.richards.mealsapp.dto.WalletInfoResponse;
import com.richards.mealsapp.dto.WalletRequest;
import com.richards.mealsapp.dto.WalletResponse;
import com.richards.mealsapp.response.BaseResponse;
import org.springframework.data.domain.Page;

public interface WalletService {
    BaseResponse<WalletResponse> fundWallet(WalletRequest walletRequest);

    BaseResponse<String> getWalletBalance();

    BaseResponse<String> processPayment(WalletRequest walletRequest);
    Page<TransactionResponse> fetchAllTransactions(Integer pageNo, Integer pageSize, String sortBy);

    BaseResponse<WalletInfoResponse> getCustomerWalletInfo();
}
