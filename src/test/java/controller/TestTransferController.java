package controller;

import com.revolut.Application;
import com.revolut.api.TransferService;
import com.revolut.exception.RevolutException;
import com.revolut.impl.TransferImpl;
import com.revolut.model.Transfer;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.Test;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import static org.testng.AssertJUnit.assertEquals;


/**
 * Unit test of TransferController
 * @author Kanat K.B.
 */

public class TestTransferController {
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

    private TransferService transferService = new TransferImpl();

    @Test
    public void add() throws Exception {
        Transfer transfer = new Transfer();
        transfer.setFromAccCode("ACC7788454545");
        transfer.setToAccCode("ACC7788757575");
        transfer.setCurrCode("USD");
        transfer.setSumm(100.0f);

        transferService.add(transfer);

        Response response = target.path("/api/transfer/add").request().post(from(transfer));

        assertEquals(Response.Status.OK, response.getStatusInfo().toEnum());
    }

    @Test
    public void send() throws Exception {
        Transfer transfer = new Transfer();
        transfer.setFromAccCode("ACC7788454545");
        transfer.setToAccCode("ACC7788757575");
        transfer.setCurrCode("USD");
        transfer.setSumm(100.0f);

        transferService.add(transfer);

        Response response = target.path("/api/transfer/send").request().get();

        assertEquals(Response.Status.OK, response.getStatusInfo().toEnum());
    }


    @Test
    public void get() throws Exception {
        Transfer transfer = new Transfer();
        transfer.setFromAccCode("ACC7788454545");
        transfer.setToAccCode("ACC7788757575");
        transfer.setCurrCode("USD");
        transfer.setSumm(10.1f);

        transferService.add(transfer);

        Response response = target.path("/api/transfer/get")
                .queryParam(transfer.getFromAccCode(),transfer.getToAccCode()).request().get();

        assertEquals(Response.Status.OK, response.getStatusInfo().toEnum());
    }

    @Test
    public void getAll() throws Exception {
        Transfer transfer = new Transfer();
        transfer.setFromAccCode("ACC7788454545");
        transfer.setToAccCode("ACC7788757575");
        transfer.setCurrCode("USD");
        transfer.setSumm(10.1f);
        transferService.add(transfer);

        Response response = target.path("/api/transfer/getAll").request().get();

        assertEquals(Response.Status.OK, response.getStatusInfo().toEnum());
    }

    private Entity from(Transfer transfer) throws RevolutException {
        return Entity.entity(transfer, MediaType.valueOf(MediaType.APPLICATION_JSON));
    }

}
