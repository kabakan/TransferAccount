package controller;

import com.revolut.Application;
import com.revolut.api.ArchTransferService;
import com.revolut.impl.ArchTransferImpl;
import com.revolut.model.Transfer;
import com.revolut.utils.CreateDate;
import org.glassfish.grizzly.http.server.HttpServer;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static org.testng.AssertJUnit.assertEquals;

/**
 * Unit test of ArchTransferController
 * @author Kanat K.B.
 */

public class TestArchTransferController {
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

    private ArchTransferService archTransferService = new ArchTransferImpl();


    @Test
    public void get() throws Exception {
        List<Transfer> transfers = new ArrayList<>();
        Transfer transfer = new Transfer();
        transfer.setFromAccCode("ACC7788454545");
        transfer.setToAccCode("ACC7788757575");
        transfer.setTitle("Test transfer");
        transfer.setStatus("SUCCESS");
        transfer.setCurrCode("USD");
        transfer.setSumm(10.1f);
        transfer.setCreateDate(CreateDate.getDate());

        archTransferService.add(transfer);

        Response response = target.path("/api/archTransfer/getArch")
                .queryParam(transfer.getFromAccCode(),transfer.getToAccCode()).request().get();

        assertEquals(Response.Status.OK, response.getStatusInfo().toEnum());
    }

    @Test
    public void getAll() throws Exception {
        List<Transfer> transfers = new ArrayList<>();
        Transfer transfer = new Transfer();
        transfer.setFromAccCode("ACC7788454545");
        transfer.setToAccCode("ACC7788757575");
        transfer.setTitle("Test transfer");
        transfer.setStatus("SUCCESS");
        transfer.setCurrCode("USD");
        transfer.setSumm(10.1f);
        transfer.setCreateDate(CreateDate.getDate());

        Response response = target.path("/api/archTransfer/getAllArch").request().get();

        assertEquals(Response.Status.OK, response.getStatusInfo().toEnum());
    }

}
