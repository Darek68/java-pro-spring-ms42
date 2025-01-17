package ru.flamexander.transfer.service.core.backend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.flamexander.transfer.service.core.api.dtos.ExecuteTransferDtoRequest;
import ru.flamexander.transfer.service.core.backend.configurations.AppProperties;
import ru.flamexander.transfer.service.core.backend.dtos.TransferDto;
import ru.flamexander.transfer.service.core.backend.entities.Account;
import ru.flamexander.transfer.service.core.backend.entities.Transfer;
import ru.flamexander.transfer.service.core.backend.errors.AppLogicException;
import ru.flamexander.transfer.service.core.backend.repositories.TransfersRepository;
import ru.flamexander.transfer.service.core.backend.validators.ExecuteTransferValidator;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransfersServiceImpl implements TransfersService {
    private final AccountsService accountsService;
    private final ExecuteTransferValidator executeTransferValidator;
    private final ClientsInfoService clientsInfoService;
    private final AppProperties appProperties;
    private final TransfersRepository transfersRepository;

    @Transactional
    @Override
    public void execute(Long clientId, ExecuteTransferDtoRequest executeTransferDtoRequest) {
        if (clientsInfoService.isClientBlocker(clientId)) {
            throw new AppLogicException("SENDER_IS_BLOCKED", "Клиент-отправитель id = " + clientId + " не может выполнить отправку перевода, так как заблокирован");
        }
        if (clientsInfoService.isClientBlocker(executeTransferDtoRequest.getReceiverId())) {
            throw new AppLogicException("RECEIVER_IS_BLOCKED", "Невозможно выполнить перевод заблокированному клиенту с id = " + executeTransferDtoRequest.getReceiverId());
        }
//        if (appProperties.getBlockedAccountsMasks().stream().anyMatch(
//                new Predicate<String>() {
//                    @Override
//                    public boolean test(String s) {
//                        return false;
//                    }
//                }
//        ))

        Account senderAccount = accountsService.findByClientIdAndAccountNumber(clientId, executeTransferDtoRequest.getSenderAccountNumber()).orElseThrow(() -> new AppLogicException("TRANSFER_SOURCE_ACCOUNT_NOT_FOUND", "Перевод невозможен поскольку не существует счет отправителя"));
        Account receiverAccount = accountsService.findByClientIdAndAccountNumber(executeTransferDtoRequest.getReceiverId(), executeTransferDtoRequest.getReceiverAccountNumber()).orElseThrow(() -> new AppLogicException("TRANSFER_TARGET_ACCOUNT_NOT_FOUND", "Перевод невозможен поскольку не существует счет получателяч"));

        senderAccount.setBalance(senderAccount.getBalance().subtract(executeTransferDtoRequest.getTransferSum()));
        receiverAccount.setBalance(receiverAccount.getBalance().add(executeTransferDtoRequest.getTransferSum()));

        Transfer transfer = new Transfer(
                clientId,
                executeTransferDtoRequest.getReceiverId(),
                executeTransferDtoRequest.getSenderAccountNumber(),
                executeTransferDtoRequest.getReceiverAccountNumber(),
                executeTransferDtoRequest.getTransferSum(),
                "SUCCESS"
        );
        transfersRepository.save(transfer);
    }

    @Override
    public List<TransferDto> getTransfersHistory(Long clientId) {
        return transfersRepository.findAllClientTransfers(clientId);
    }
}
