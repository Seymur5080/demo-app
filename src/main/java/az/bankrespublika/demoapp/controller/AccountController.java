package az.bankrespublika.demoapp.controller;

import az.bankrespublika.demoapp.dto.request.AccountRequestDto;
import az.bankrespublika.demoapp.dto.request.AmountRequestDto;
import az.bankrespublika.demoapp.dto.request.TransferRequestDto;
import az.bankrespublika.demoapp.dto.response.AccountResponseDto;
import az.bankrespublika.demoapp.model.BaseResponse;
import az.bankrespublika.demoapp.service.AccountService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountController {
    AccountService accountService;

    // Create a new account (создание нового счёта)
    @PostMapping("/create")
    public ResponseEntity<BaseResponse<AccountResponseDto>> createAccount(
            @RequestHeader("Authorization") String token,
            @Valid
            @RequestBody AccountRequestDto request
    ) {
        return accountService.createAccount(token, request);
    }

    // Get all active accounts (получить все активные счета)
    @GetMapping
    public ResponseEntity<BaseResponse<List<AccountResponseDto>>> getAccounts(@RequestHeader("Authorization") String token) {
        return accountService.getAccounts(token);
    }

    // Get user accounts by username (получить счета пользователя по username)
    @GetMapping("/by-username")
    public ResponseEntity<BaseResponse<List<AccountResponseDto>>> getAccountsByUsername(
            @RequestHeader("Authorization") String token,
            @RequestParam String username
    ) {
        return accountService.getAccountsByUsername(token, username);
    }

    // Close account (закрыть счёт)
    @PutMapping("/close/{accountNo}")
    public ResponseEntity<BaseResponse<Void>> closeAccount(@RequestHeader("Authorization") String token,
                                                           @PathVariable String accountNo) {
        return accountService.closeAccount(token, accountNo);
    }

    // Top up your account balance (пополнить баланс счета)
    @PutMapping("/add-amount")
    public ResponseEntity<BaseResponse<Void>> addAmount(@RequestHeader("Authorization") String token,
                                                        @RequestBody AmountRequestDto request) {
        return accountService.addAmount(token, request);
    }

    // Transfer money between accounts (перевести деньги между счетами)
    @PostMapping("/transfer")
    public ResponseEntity<BaseResponse<String>> transferMoney(@RequestHeader("Authorization") String token,
                                                              @RequestBody TransferRequestDto request) {
        return accountService.transferMoney(token, request);
    }
}
