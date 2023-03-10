package com.richards.mealsapp.service.impl;

import com.richards.mealsapp.dto.TransactionResponse;
import com.richards.mealsapp.dto.WalletInfoResponse;
import com.richards.mealsapp.dto.WalletRequest;
import com.richards.mealsapp.dto.WalletResponse;
import com.richards.mealsapp.entity.Person;
import com.richards.mealsapp.entity.Transaction;
import com.richards.mealsapp.entity.Wallet;
import com.richards.mealsapp.enums.ResponseCodeEnum;
import com.richards.mealsapp.event.TransactionMail;
import com.richards.mealsapp.exceptions.AuthorizationException;
import com.richards.mealsapp.exceptions.ResourceNotFoundException;
import com.richards.mealsapp.exceptions.UserNotFoundException;
import com.richards.mealsapp.repository.PersonRepository;
import com.richards.mealsapp.repository.TransactionRepository;
import com.richards.mealsapp.repository.WalletRepository;
import com.richards.mealsapp.response.BaseResponse;
import com.richards.mealsapp.service.WalletService;
import com.richards.mealsapp.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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

    @Value("${spring.admin.super.email}")
    private String superAdminEmail;

    @Override
    public BaseResponse<WalletResponse> fundWallet(WalletRequest walletRequest) {
        BigDecimal bigDecimalAmount  = BigDecimal.valueOf(walletRequest.getAmount());

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
                WalletResponse.builder()
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

    @Override
    public BaseResponse<String> processPayment(WalletRequest walletRequest) {
        BigDecimal grandTotal = BigDecimal.valueOf(walletRequest.getAmount());

        String email = UserUtil.extractEmailFromPrincipal()
                .orElseThrow(() -> new UsernameNotFoundException("You are not logged in"));

        Person person = personRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Wallet customerWallet = person.getCustomer().getWallet();

        if(customerWallet.getAccountBalance().compareTo(grandTotal)>=0){
            customerWallet.setAccountBalance(customerWallet.getAccountBalance().subtract(grandTotal));

            Wallet adminWallet = personRepository.findByEmail(superAdminEmail)
                    .orElseThrow(()-> new UserNotFoundException("System Error: Please contact the admin"))
                    .getSuperAdmin().getWallet();

            adminWallet.setAccountBalance(adminWallet.getAccountBalance().add(grandTotal));
            transactionMail.notifySuperAdminOfBalance(email, grandTotal);
            new BaseResponse<>(ResponseCodeEnum.SUCCESS, "Transaction successful");
        }
        throw new AuthorizationException("Insufficient balance, Please fund your wallet.");
    }

    @Override
    public Page<TransactionResponse> fetchAllTransactions(Integer pageNo, Integer pageSize, String sortBy){
        String email = UserUtil.extractEmailFromPrincipal()
                .orElseThrow(() -> new UsernameNotFoundException("You are not logged in"));

        Person person = personRepository.findByEmail(email).orElseThrow();
        Wallet wallet = person.getCustomer().getWallet();

        Set<Transaction> transactions = wallet.getTransactions();
        List<Transaction> transactionsList = new ArrayList<>(transactions);

        List<TransactionResponse> response = transactionsList.stream().map(this::responseMapper).collect(Collectors.toList());

        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, Sort.Direction.DESC, sortBy);

        int minimum = pageNo*pageSize;
        int max = Math.min(pageSize * (pageNo + 1), transactions.size());

        return new PageImpl<>(response.subList(minimum, max), pageRequest, response.size());
    }

    @Override
    public BaseResponse<WalletInfoResponse> getCustomerWalletInfo() {

        String email = UserUtil.extractEmailFromPrincipal()
                .orElseThrow(() -> new UsernameNotFoundException("You are not logged in"));

        Person person = personRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        Wallet wallet = person.getCustomer().getWallet();

        String currencyString = UserUtil.formatToLocale(wallet.getAccountBalance());

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("en", "NG"));
        symbols.setCurrencySymbol("₦");


        return new BaseResponse<>(ResponseCodeEnum.SUCCESS ,WalletInfoResponse.builder()
                .firstName(person.getFirstName())
                .lastName(person.getLastName())
                .email(person.getEmail())
                .walletBalance(currencyString)
                .baseCurrency(String.valueOf(wallet.getBaseCurrency()))
                .build());

    }

    protected TransactionResponse responseMapper(Transaction transaction){
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");

        return TransactionResponse.builder()
                .id(transaction.getId())
                .date(date.format(transaction.getCreatedAt()))
                .time(time.format(transaction.getCreatedAt()))
                .amount(transaction.getAmount())
                .purpose(String.valueOf(transaction.getPurpose()).toLowerCase())
                .status(String.valueOf(transaction.getStatus()).toLowerCase())
                .reference(transaction.getReference())
                .build();
    }

}
