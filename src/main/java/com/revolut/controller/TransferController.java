package com.revolut.controller;

import com.revolut.api.TransferService;
import com.revolut.exception.RevolutException;
import com.revolut.impl.TransferImpl;
import com.revolut.model.Transfer;
import com.revolut.utils.BodyResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * API of transfer
 * @author Kanat K.B.
 */
@Api(value = "Transfer", description = "Settings Transfer")
@Path("api/transfer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TransferController {
    private final Logger LOG = LoggerFactory.getLogger(TransferController.class);

    private TransferService transferService = new TransferImpl();

    private BodyResponse bodyResponse = new BodyResponse();

    @ApiOperation(value = "Create transfer")
    @POST()
    @Path(value="add")
    public Response add(Transfer transfer) {
        try {
            return Response.ok().entity(transferService.add(transfer)).build();
        } catch (RuntimeException | RevolutException ex) {
            LOG.error("ERROR: transfer.add "+ex.getMessage());
            return Response.ok().entity(bodyResponse.set("FAIL","Add transfer")).build();
        }
    }

    @ApiOperation(value = "Send transfer")
    @GET
    @Path(value = "send")
    public Response send() {
        try {
            return Response.ok(transferService.sendTransfer()).build();
        } catch (RuntimeException | RevolutException ex) {
            LOG.error("ERROR: transfer.send "+ex.getMessage());
            return Response.ok().entity(bodyResponse.set("FAIL","Send transfer")).build();
        }
    }

    @ApiOperation(value = "Get transfer by accCode")
    @GET()
    @Path(value = "get")
    public Response getTransfer(@QueryParam(value = "fromAccCode") String fromAccCode,
                                @QueryParam(value = "toAccCode") String toAccCode) {
        try {
            return  Response.ok().entity(transferService.get(fromAccCode, toAccCode)).build();
        } catch (RuntimeException | RevolutException ex) {
            LOG.error("ERROR: transfer.get "+ex.getMessage());
            return null;
        }
    }

    @ApiOperation(value = "Get all transfer")
    @GET
    @Path(value = "getAll")
    public  Response getAll() {
        try {
            return  Response.ok().entity(transferService.getAll()).build();
        } catch (RuntimeException | RevolutException ex) {
            LOG.error("ERROR: transfer.get "+ex.getMessage());
            return null;
        }
    }

}
