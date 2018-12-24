package com.revolut.impl;

import com.revolut.adapter.DequeTransfer;
import com.revolut.api.AccountsService;
import com.revolut.api.ArchTransferService;
import com.revolut.api.TransferService;
import com.revolut.exception.RevolutException;
import com.revolut.model.Accounts;
import com.revolut.model.Transfer;
import com.revolut.utils.BodyResponse;
import com.revolut.utils.CreateDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of TransferService
 * @author Kanat K.B.
 */

public class TransferImpl implements TransferService {
    private final Logger LOG = LoggerFactory.getLogger(TransferService.class);

    private AccountsService accountsService = new AccountsImpl();

    private ArchTransferService archTransferService = new ArchTransferImpl();

    private BodyResponse bodyResponse = new BodyResponse();

    @Override
    public BodyResponse add(Transfer transfer) throws RuntimeException {
        transfer.setId(DequeTransfer.getIndex());
        transfer.setStatus("CREATE");
        transfer.setCreateDate(CreateDate.getDate());
        DequeTransfer.add(transfer);
        return bodyResponse.set("SUCCESS","Add transfer");
    }

    @Override
    public List<Transfer> get(String fromAccCode, String toAccCode) throws RevolutException{
        List<Transfer> list = new ArrayList<Transfer>();
        DequeTransfer.get().forEach(deque -> {
            if (deque.getFromAccCode().equals(fromAccCode) || deque.getToAccCode().equals(toAccCode)) {
                list.add(deque);
            }
        });
        return list;
    }

    @Override
    public List<Transfer> getAll() throws RevolutException {
        List<Transfer> list = new ArrayList<Transfer>();
        DequeTransfer.get().forEach(deque -> {
           list.add(deque);
        });
        return list;
    }

    @Override
    public BodyResponse sendTransfer() throws RevolutException {
        Integer iCount = DequeTransfer.get().size();
        Integer iResult = 0;
        Integer iSaldo = 0;
        String message = "";
        new Thread(()->{
            while(DequeTransfer.get().peek() != null) {
                Transfer deque = DequeTransfer.get().peek();
                Float saldo = 0.0f;
                Float balance = 0.0f;
                Boolean isTransfer = true;
                String title = "transfer account to account";
                String status = "SUCCESS";

                try {
                    Accounts fromAcc = accountsService.get(null, deque.getFromAccCode()).get(0);
                    Accounts toAcc = accountsService.get(null, deque.getToAccCode()).get(0);

                    if (!"ACTIVE".equals(fromAcc.getStatus()) || !"ACTIVE".equals(toAcc.getStatus())) {
                        isTransfer=false;
                        title = "This accounts is not active";
                        status = "FAIL";
                    }

                    if (isTransfer && !fromAcc.getCurrCode().equals(deque.getCurrCode())) {
                        isTransfer=false;
                        title = "Currency code of the sender and the recipient do not match";
                        status = "FAIL";
                    }

                    if (isTransfer && fromAcc.getSumm() < deque.getSumm()) {
                        isTransfer=false;
                        title = "Not enough count to transfer";
                        status = "FAIL";
                    }

                    if (isTransfer) {
                        saldo = fromAcc.getSumm() - deque.getSumm();
                        fromAcc.setSumm(saldo);
                        balance = toAcc.getSumm() + deque.getSumm();
                        toAcc.setSumm(balance);
                        title = "From "+fromAcc.getClientName()+" "+title;
                    }

                    accountsService.update(fromAcc);
                    accountsService.update(toAcc);

                    deque.setStatus(status);
                    deque.setTitle(title);
                    deque.setSendDate(CreateDate.getDate());
                    archTransferService.add(deque);

                    DequeTransfer.get().pop();
                } catch (RevolutException ex) {
                    LOG.error("ERROR: sendTransfer(): " + ex.getMessage());
                }
            }
        }).run();

        iSaldo=DequeTransfer.get().size();
        iResult=iCount-iSaldo;

        if (iCount==0) message="Not found transfer count: "+iCount;
        else message="Send transfer completed was count: "+iCount+"; Result: "+iResult+"; Saldo: "+iSaldo;

        return bodyResponse.set("FINISH",message);
    }

}
