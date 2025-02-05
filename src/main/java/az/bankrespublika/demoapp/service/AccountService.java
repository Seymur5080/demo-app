package az.bankrespublika.demoapp.service;

import az.bankrespublika.demoapp.dao.entity.Account;
import az.bankrespublika.demoapp.dao.entity.User;
import az.bankrespublika.demoapp.dao.repository.AccountRepository;
import az.bankrespublika.demoapp.dao.repository.UserRepository;
import az.bankrespublika.demoapp.dto.request.AccountRequestDto;
import az.bankrespublika.demoapp.dto.request.AmountRequestDto;
import az.bankrespublika.demoapp.dto.request.TransferRequestDto;
import az.bankrespublika.demoapp.dto.response.AccountResponseDto;
import az.bankrespublika.demoapp.exception.NotFoundException;
import az.bankrespublika.demoapp.model.BaseResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static az.bankrespublika.demoapp.mapper.AccountMapper.map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class AccountService {
    AccountRepository accountRepository;
    UserRepository userRepository;
    CurrencyService currencyService;
    AuthenticationService authenticationService;

    public ResponseEntity<BaseResponse<AccountResponseDto>> createAccount(String token, AccountRequestDto request) {
        // check login user token (проверяем токен пользователя в БД)
        authenticationService.validateToken(token);

        // get user by username (получаем пользователя по username из DB)
        User accountUser = getUserByUsername(request.getUsername());

        // AZN currency not checked (AZN валюта не проверяется)
        if (!request.getCurrency().equalsIgnoreCase("AZN")) {
            // check currency in currency table (проверяем валюту)
            currencyService.getCurrencyByCode(request.getCurrency());
        }

        // map dto to entity (маппим dto -> entity)
        Account account = map(request, accountUser);

        // save to database (сохраняем аккаунт в базе)
        accountRepository.save(account);

        // return create account response (возвращаем успешный ответ)
        return BaseResponse.ok(map(account));
    }

    public ResponseEntity<BaseResponse<List<AccountResponseDto>>> getAccounts(String token) {
        // check login user token (проверяем токен пользователя в БД)
        authenticationService.validateToken(token);

        // Get all account in DB (получает все счета)
        return BaseResponse.ok(map(accountRepository.findAll()));
    }

    public ResponseEntity<BaseResponse<List<AccountResponseDto>>> getAccountsByUsername(String token, String username) {
        // check login user token (проверяем токен пользователя в БД)
        authenticationService.validateToken(token);

        // find user by username (ищем пользователя по username)
        User findUser = getUserByUsername(username);

        // Get account only one user (получает одного пользователя)
        return BaseResponse.ok(map(accountRepository.findByUser(findUser)));
    }

    public ResponseEntity<BaseResponse<Void>> closeAccount(String token, String accountNo) {
        // check login user token (проверяем токен пользователя в БД)
//        authenticationService.validateToken(token);

        validateAccountOwnership(token, accountNo);

        // check active account in DB
        Account account = getAccountByAccountNo(accountNo, "Account not found!");
        account.setActive(false);
        account.setExpirationDate(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS));
        accountRepository.save(account);

        return BaseResponse.ok();
    }

    public ResponseEntity<BaseResponse<Void>> addAmount(String token, AmountRequestDto request) {
        // check login user token (проверяем токен пользователя)
//        authenticationService.validateToken(token);

        validateAccountOwnership(token, request.getAccountNo());

        // check account in DB (ищем счет в БД по номеру)
        Account account = getAccountByAccountNo(request.getAccountNo(), "Account not found!");
        account.setBalance(request.getAmount());
        accountRepository.save(account);

        return BaseResponse.ok();
    }

    public ResponseEntity<BaseResponse<String>> transferMoney(String token, TransferRequestDto request) {
        // check login user token (проверяем токен пользователя)
        User user = authenticationService.validateToken(token);
//        validateAccountOwnership(token, request.getFromAccountNo());

        // check from account in database (проверка счета отправителя)
        Account fromAccount = getAccountByAccountNo(request.getFromAccountNo(), "From account not found!");

        // check to account in database (проверка счета получателя)
        Account toAccount = getAccountByAccountNo(request.getToAccountNo(), "To account not found!");

        // eger bu if serti kommentden cixarilsa emeliyyati eden sexs sadece oz hesablari arasinda pul transferi ede bilecek
//        if (!fromAccount.getUser().equals(user)) {
//            throw new IllegalArgumentException("Unauthorized");
//        }

        BigDecimal amount = BigDecimal.valueOf(request.getAmount());

        // Check if the sender's account has enough balance (проверяем, хватает ли денег на счете отправителя)
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance!");
        }

        // Check if sender and receiver account currencies match (проверяем, совпадают ли валюты счетов отправителя и получателя)
        if (fromAccount.getCurrency().equals(toAccount.getCurrency())) {
            fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
            toAccount.setBalance(toAccount.getBalance().add(amount));
        } else {
            BigDecimal convertedAmount = currencyService.convert(
                    fromAccount.getCurrency(), toAccount.getCurrency(), request.getAmount());

            fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
            toAccount.setBalance(toAccount.getBalance().add(convertedAmount));
        }

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        return BaseResponse.ok("Transferred successfully!");
    }

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found!"));
    }

    private Account getAccountByAccountNo(String accountNo, String message) {
        return accountRepository.findByAccountNoAndActiveTrue(accountNo)
                .orElseThrow(() -> new NotFoundException(message));
    }

    // Checking the account ownership of a user (проверка принадлежности счёта пользователю)
    private void validateAccountOwnership(String token, String accountNo) {
        User user = authenticationService.validateToken(token);
        Account account = getAccountByAccountNo(accountNo, "Account not found!");

        if (!account.getUser().equals(user)) {
            throw new IllegalArgumentException("Unauthorized: This account does not belong to you!");
        }
    }
}
