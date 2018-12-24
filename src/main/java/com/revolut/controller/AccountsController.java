package com.revolut.controller;

import com.revolut.api.AccountsService;
import com.revolut.exception.RevolutException;
import com.revolut.impl.AccountsImpl;
import com.revolut.model.Accounts;
import com.revolut.model.Currency;
import com.revolut.utils.BodyResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.time.LocalDate;

/**
 * API of accounts
 * @author Kanat K.B.
 */

@Api(value = "Accounts", description = "Settings accounts")
@Path("/api/accounts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccountsController {
    private final Logger LOG = LoggerFactory.getLogger(AccountsController.class);

    private AccountsService accountsService = new AccountsImpl();

    private BodyResponse bodyResponse = new BodyResponse();

    @ApiOperation(value = "Create accounts")
    @POST()
    @Path(value="add")
    public Response add(Accounts accounts ) {
        try {
            return Response.ok().entity(accountsService.add(accounts)).build();
        } catch (RuntimeException | RevolutException ex) {
            LOG.error("ERROR: ServiceAccounts.add "+ex.getMessage());
            return Response.ok().entity(bodyResponse.set("FAIL","Add account")).build();
        }
    }

    @ApiOperation(value = "Update accounts")
    @POST()
    @Path(value="update")
    public Response update(Accounts accounts ) {
        try {
            return Response.ok().entity(accountsService.update(accounts)).build();
        } catch (RuntimeException | RevolutException ex) {
            LOG.error("ERROR: ServiceAccounts.update "+ex.getMessage());
            return Response.ok(bodyResponse.set("FAIL","update account")).build();
        }
    }

    @ApiOperation(value = "Delete accounts")
    @GET()
    @Path(value = "delete")
    public Response delete(@QueryParam(value = "id") Integer id) {
        try {
            return Response.ok().entity(accountsService.delete(id)).build();
        } catch (RuntimeException | RevolutException ex) {
            LOG.error("ERROR: ServiceAccounts.delete "+ex.getMessage());
            return Response.ok().entity(bodyResponse.set("FAIL","Delete account")).build();
        }
    }

    @ApiOperation(value = "Get accounts by accCode")
    @GET()
    @Path("get")
    public  Response getAccounts(@QueryParam(value = "clientName") String clientName,
                                 @QueryParam(value = "accCode") String accCode) {
        try {
            return  Response.ok().entity(accountsService.get(clientName, accCode)).build();
        } catch (RuntimeException | RevolutException ex) {
            LOG.error("ERROR: ServiceAccounts.get "+ex.getMessage());
            return null;
        }
    }

    @ApiOperation(value = "Get all accounts")
    @GET
    @Path(value = "getAll")
    public  Response getAll() {
        try {
            return  Response.ok().entity(accountsService.getAll()).build();
        } catch (RuntimeException | RevolutException ex) {
            LOG.error("ERROR: ServiceAccounts.get "+ex.getMessage());
            return null;
        }
    }

    @ApiOperation(value = "Get all currency")
    @GET
    @Path(value = "getCurrency")
    public  Response getCurrency() {
        try {
            Currency currency = new Currency();
            return  Response.ok().entity(currency.getCurrCode()).build();
        } catch (RuntimeException | RevolutException ex) {
            LOG.error("ERROR: GetCurrecny "+ex.getMessage());
            return null;
        }
    }

    @ApiOperation(value = "Getenerate account code")
    @GET
    @Path(value = "generate")
    public  Response getnereateAcc() {
        try {
            return  Response.ok().entity(accountsService.generate()).build();
        } catch (RuntimeException | RevolutException ex) {
            LOG.error("ERROR: GetCurrecny "+ex.getMessage());
            return null;
        }
    }

   @GET
    @Path(value = "/health")
    public String home() {
        return "RevolutTransfer: "+ LocalDate.now();
    }
}
