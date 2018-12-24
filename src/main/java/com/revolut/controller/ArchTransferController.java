package com.revolut.controller;

import com.revolut.api.ArchTransferService;
import com.revolut.exception.RevolutException;
import com.revolut.impl.ArchTransferImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * API of archive transfer
 * @author Kanat K.B.
 */

@Api(value = "ArchTransfer", description = "Settings  ArchTransfer")
@Path("api/archTransfer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ArchTransferController {
    private final Logger LOG = LoggerFactory.getLogger(ArchTransferController.class);

    private ArchTransferService archTransferService = new ArchTransferImpl();

    @ApiOperation(value = "Get ArchTransfer by accCode")
    @GET()
    @Path(value = "getArch")
    public Response getArchTransfer(@QueryParam(value = "fromAccCode") String fromAccCode,
                                    @QueryParam(value = "toAccCode") String toAccCode) {
        try {
            return  Response.ok().entity(archTransferService.get(fromAccCode, toAccCode)).build();
        } catch (RuntimeException | RevolutException ex) {
            LOG.error("ERROR: ArchTransfer.getArch "+ex.getMessage());
            return null;
        }
    }

    @ApiOperation(value = "Get all ArchTransfer")
    @GET
    @Path(value = "getAllArch")
    public  Response getAllArch() {
        try {
            return  Response.ok().entity(archTransferService.getAll()).build();
        } catch (RuntimeException | RevolutException ex) {
            LOG.error("ERROR: ArchTransfer.getAllArch "+ex.getMessage());
            return null;
        }
    }


}
