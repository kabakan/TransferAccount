package com.revolut.impl;

import com.revolut.adapter.MapAccounts;
import com.revolut.api.AccountsService;
import com.revolut.exception.RevolutException;
import com.revolut.model.Accounts;
import com.revolut.utils.BodyResponse;
import com.revolut.utils.CreateDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

/**
 * Implementation of AccountsService
 * @author Kanat K.B.
 */
public class AccountsImpl implements AccountsService {
    private final Logger LOG = LoggerFactory.getLogger(AccountsImpl.class);

    private BodyResponse bodyResponse = new BodyResponse();

    @Override
    public BodyResponse add(Accounts accounts) throws RevolutException {
        accounts.setId(MapAccounts.getIndex());
        accounts.setStatus("ACTIVE");
        accounts.setChangeDate(CreateDate.getDate());
        accounts.setCreateDate(CreateDate.getDate());
        MapAccounts.add(accounts);
        return bodyResponse.set("SUCCESS","Add account");
    }

    @Override
    public BodyResponse update(Accounts accounts) throws RevolutException {
        accounts.setChangeDate(CreateDate.getDate());
        MapAccounts.update(accounts);
        return bodyResponse.set("SUCCESS","Update account");
    }

    @Override
    public BodyResponse delete(Integer id) throws RevolutException {
        Accounts accounts = getId(id);
        accounts.setStatus("DELETE");
        accounts.setChangeDate(CreateDate.getDate());
        MapAccounts.delete(accounts);
        return bodyResponse.set("SUCCESS","Delete account");
    }

    @Override
    public List<Accounts> get(String clientName, String accCode) throws RevolutException{
        List<Accounts> list = new ArrayList<Accounts>();
        MapAccounts.get().entrySet().forEach(acc -> {
            if (acc.getValue().getClientName().equals(clientName) || acc.getValue().getAccCode().equals(accCode)) {
                list.add(acc.getValue());
            }
        });
        return list;
    }

    @Override
    public Accounts getId(Integer id) throws RevolutException {
        return MapAccounts.get().get(id);
    }

    @Override
    public List<Accounts> getAll() throws RevolutException {
        List<Accounts> list = new ArrayList<Accounts>();
        MapAccounts.get().entrySet().forEach(acc -> {
            list.add(acc.getValue());
        });
        return list;
    }

    @Override
    public Map<String,String> generate() throws RevolutException {
        Map<String,String> acc = new Hashtable<>();
        Random rnd = new Random();
        int n = 100000 + rnd.nextInt(900000);
        acc.put("account","ACC5577"+n);
        return acc;
    }
}
