package az.bankrespublika.demoapp.mapper;

import az.bankrespublika.demoapp.dao.entity.Account;
import az.bankrespublika.demoapp.dao.entity.User;
import az.bankrespublika.demoapp.dto.request.AccountRequestDto;
import az.bankrespublika.demoapp.dto.response.AccountResponseDto;

import java.util.List;
import java.util.stream.Collectors;

public class AccountMapper {
    // Account (entity) -> convert -> AccountResponseDto(DTO)
    public static AccountResponseDto map(Account account) {
        if (account == null) {
            return null;
        }

        return AccountResponseDto.builder()
                .accountNo(account.getAccountNo())
                .currency(account.getCurrency())
                .balance(account.getBalance())
                .creationDate(account.getCreationDate())
                .expirationDate(account.getExpirationDate())
                .active(account.getActive())
                .username(account.getUser().getUsername())
                .build();
    }

    public static List<AccountResponseDto> map(List<Account> accounts) {
        if (accounts == null) {
            return null;
        }

        return accounts.stream()
                .map(AccountMapper::map)
                .collect(Collectors.toList());
    }

    public static Account map(AccountRequestDto request, User user) {
        if (request == null) {
            return null;
        }

        return Account.builder()
                .currency(request.getCurrency())
                .user(user)
                .build();
    }
}
