package org.jboss.as.tests.resteasy923.model;

import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.jboss.resteasy.spi.validation.ValidateRequest;

@Path("/user")
public interface UserResource {
	@ValidateRequest
	@POST
	@Path("/")
	public User register(@NotNull @FormParam("email") String email,
			@FormParam("password") String password)
			throws EmailAleadyRegisteredException;

  @GET
  @Path("/")
  public String info();


}