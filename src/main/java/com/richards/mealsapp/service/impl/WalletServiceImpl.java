package com.richards.mealsapp.service.impl;

import com.richards.mealsapp.dto.FundWalletRequest;
import com.richards.mealsapp.dto.FundWalletResponse;
import com.richards.mealsapp.entity.Person;
import com.richards.mealsapp.entity.Transaction;
import com.richards.mealsapp.entity.Wallet;
import com.richards.mealsapp.enums.ResponseCodeEnum;
import com.richards.mealsapp.event.TransactionMail;
import com.richards.mealsapp.exceptions.ResourceNotFoundException;
import com.richards.mealsapp.repository.PersonRepository;
import com.richards.mealsapp.repository.TransactionRepository;
import com.richards.mealsapp.repository.WalletRepository;
import com.richards.mealsapp.response.BaseResponse;
import com.richards.mealsapp.service.WalletService;
import com.richards.mealsapp.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

import static com.richards.mealsapp.enums.PaymentPurpose.DEPOSIT;
import static com.richards.mealsapp.enums.TransactionStatus.COMPLETED;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletServiceImpl implements WalletService {
    private final TransactionMail transactionMail;
    private final PersonRepository personRepository;
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    @Override
    public BaseResponse<FundWalletResponse> fundWallet(FundWalletRequest fundWalletRequest) {
        BigDecimal bigDecimalAmount  = BigDecimal.valueOf(fundWalletRequest.getAmount());

        String email = UserUtil.extractEmailFromPrincipal()
                .orElseThrow(() -> new UsernameNotFoundException("You are not logged in"));

        Person person = personRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Wallet wallet = person.getCustomer().getWallet();
        wallet.setAccountBalance(wallet.getAccountBalance().add(bigDecimalAmount));
        walletRepository.save(wallet);

        Transaction transaction = Transaction.builder()
                .wallet(wallet)
                .amount(UserUtil.formatToLocale(bigDecimalAmount))
                .purpose(DEPOSIT)
                .reference(String.valueOf(UUID.randomUUID()))
                .status(COMPLETED).build();
        transactionRepository.save(transaction);

        transactionMail.sendTransactionMail(person, bigDecimalAmount, wallet.getAccountBalance());

        return new BaseResponse<>( ResponseCodeEnum.SUCCESS,
                FundWalletResponse.builder()
                .fullName(person.getFirstName() + " " + person.getLastName())
                .depositAmount(bigDecimalAmount)
                .newBalance(wallet.getAccountBalance()).build());

    }

    @Override
    public BaseResponse<String> getWalletBalance() {
        String email = UserUtil.extractEmailFromPrincipal()
                .orElseThrow(() -> new UsernameNotFoundException("You are not logged in"));

        Person person = personRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Wallet wallet = person.getCustomer().getWallet();
        BigDecimal walletBalance = wallet.getAccountBalance();

        transactionMail.sendWalletBalance(person, wallet);

        return new BaseResponse<>(ResponseCodeEnum.SUCCESS, UserUtil.formatToLocale(walletBalance));
    }
}
