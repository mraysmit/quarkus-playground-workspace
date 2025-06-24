package org.example;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.common.annotation.NonBlocking;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @NonBlocking
    public Uni<java.util.List<User>> getAll() {
        return Panache.withTransaction(() -> User.listAll());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @NonBlocking
    public Uni<Response> create(User user) {
        return Panache.withTransaction(() -> user.persistAndFlush())
            .map(u -> Response.ok(u).status(Response.Status.OK).build());
    }

    @GET
    @Path("/stream")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @NonBlocking
    public Multi<String> stream() {
        return Multi.createFrom().ticks().every(java.time.Duration.ofSeconds(1))
            .onItem().transform(n -> "Tick: " + n);
    }
}
