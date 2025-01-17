package ru.flamexander.transfer.service.core.backend.services;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.flamexander.transfer.service.core.api.dtos.CreateAccountDto;
import ru.flamexander.transfer.service.core.backend.entities.Account;
import ru.flamexander.transfer.service.core.backend.errors.AppLogicException;
import ru.flamexander.transfer.service.core.backend.repositories.AccountsRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountsService {
    private final AccountsRepository accountsRepository;

    private static final Logger logger = LoggerFactory.getLogger(AccountsService.class.getName());

    public List<Account> getAllAccounts(Long clientId) {
        return accountsRepository.findAllByClientId(clientId);
    }

    public Optional<Account> findByClientIdAndAccountNumber(Long clientId, String accountNumber) {
        logger.info("findByClientIdAndAccountNumber1 clientId id = {}     accountNumber {}", clientId, accountNumber);
        return accountsRepository.findByClientIdAndAccountNumber(clientId, accountNumber);
    }

    public Account createNewAccount(Long clientId, CreateAccountDto createAccountDto) {
        if (createAccountDto.getInitialBalance() == null) {
            throw new AppLogicException("VALIDATION_ERROR", "Создаваемый счет не может иметь null баланс");
        }
        Account account = new Account(clientId, createAccountDto.getInitialBalance());
        account = accountsRepository.save(account);
        logger.info("Account id = {} created from {}", account.getId(), createAccountDto);
        return account;
    }
}
