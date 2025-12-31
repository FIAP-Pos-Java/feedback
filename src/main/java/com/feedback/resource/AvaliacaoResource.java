package com.feedback.resource;

import com.feedback.dto.AvaliacaoRequest;
import com.feedback.dto.AvaliacaoResponse;
import com.feedback.dto.ErrorResponse;
import com.feedback.service.AvaliacaoService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

@Path("/avaliacao")
@ApplicationScoped
public class AvaliacaoResource {

    private static final Logger LOG = Logger.getLogger(AvaliacaoResource.class);

    @Inject
    AvaliacaoService avaliacaoService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response criarAvaliacao(@Valid AvaliacaoRequest request) {
        try {
            LOG.infof("Recebida avaliação - Nota: %d, Descrição: %s", 
                request.getNota(), 
                request.getDescricao().substring(0, Math.min(50, request.getDescricao().length())));

            AvaliacaoResponse response = avaliacaoService.processarAvaliacao(request);

            return Response.status(Response.Status.CREATED)
                .entity(response)
                .build();
        } catch (IllegalArgumentException e) {
            LOG.errorf(e, "Erro de validação ao processar avaliação");
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse("Erro de validação: " + e.getMessage()))
                .build();
        } catch (Exception e) {
            LOG.errorf(e, "Erro inesperado ao processar avaliação");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Erro interno ao processar avaliação"))
                .build();
        }
    }
}

