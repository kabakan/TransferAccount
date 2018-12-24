package controller;

import com.revolut.Application;
import com.revolut.api.AccountsService;
import com.revolut.exception.RevolutException;
import com.revolut.impl.AccountsImpl;
import com.revolut.model.Accounts;
import org.glassfish.grizzly.http.server.HttpServer;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import static org.testng.AssertJUnit.assertEquals;

/**
 * Unit test of AccountsController
 * @author Kanat K.B.
 */


public class TestAccountsController {
    private static HttpServer server;
    private static WebTarget target;

    @BeforeClass
    public static void beforeAll() {
        server = Application.startHttpServer();
        Client c = ClientBuilder.newClient();
        target = c.target(Application.BASE_URI);
    }

    @AfterClass
    public static void afterAll() {
        server.shutdownNow();
    }

    private AccountsService accountsService = new AccountsImpl();

    @Test
    public void add() throws RevolutException {
        Accounts acc = new Accounts();
        acc.setClientName("Alex Li");
        acc.setAccCode("ACC7788454545");
        acc.setCurrCode("USD");
        acc.setSumm(10.1f);

        Response response = target.path("/api/accounts/add").request().post(from(acc));

        assertEquals(Response.Status.OK, response.getStatusInfo().toEnum());
    }

    @Test
    public void update() throws RevolutException {

        Accounts acc = new Accounts();
        acc.setClientName("Alex Li");
        acc.setAccCode("ACC7788454545");
        acc.setCurrCode("USD");
        acc.setSumm(10.1f);

        accountsService.add(acc);

        Accounts accAfter = new Accounts();
        accAfter.setId(1);
        accAfter.setClientName("Alex Li");
        accAfter.setAccCode("ACC7788454545");
        accAfter.setCurrCode("USD");
        accAfter.setSumm(10.2f);

        Response response = target.path("/api/accounts/update").request().post(from(accAfter));

        assertEquals(Response.Status.OK, response.getStatusInfo().toEnum());
    }

    @Test
    public void delete() throws RevolutException {
        Accounts acc = new Accounts();
        acc.setClientName("Alex Li");
        acc.setAccCode("ACC7788454545");
        acc.setCurrCode("USD");
        acc.setSumm(10.1f);

        accountsService.add(acc);

        Response response = target.path("/api/accounts/delete")
                .queryParam(acc.getId().toString()).request().get();

        assertEquals(Response.Status.OK, response.getStatusInfo().toEnum());
    }

    @Test
    public void get() throws RevolutException {
        Accounts acc = new Accounts();
        acc.setClientName("Alex Li");
        acc.setAccCode("ACC7788454545");
        acc.setCurrCode("USD");
        acc.setSumm(10.1f);

        accountsService.add(acc);

        Response response = target.path("/api/accounts/get")
                .queryParam(acc.getClientName(),acc.getAccCode()).request().get();

        assertEquals(Response.Status.OK, response.getStatusInfo().toEnum());
    }

    @Test
    public void getAll() throws RevolutException {
        Accounts acc = new Accounts();
        acc.setClientName("Alex Li");
        acc.setAccCode("ACC7788454545");
        acc.setCurrCode("USD");
        acc.setSumm(10.1f);

        accountsService.add(acc);

        Response response = target.path("/api/accounts/getAll").request().get();

        assertEquals(Response.Status.OK, response.getStatusInfo().toEnum());
    }

    @Test
    public void generate() throws RevolutException {
        Response response = target.path("/api/accounts/generate").request().get();
        assertEquals(Response.Status.OK, response.getStatusInfo().toEnum());
    }

    private Entity from(Accounts accounts) throws RevolutException {
        return Entity.entity(accounts, MediaType.valueOf(MediaType.APPLICATION_JSON));
    }
}
