package org.jboss.as.tests.resteasy923.model;

import org.jboss.as.tests.resteasy923.model.EmailAleadyRegisteredException;
import org.jboss.as.tests.resteasy923.model.User;
import org.jboss.as.tests.resteasy923.model.UserResource;

//import javax.ejb.Stateless;

//@Stateless
public class UserResourceImpl implements UserResource {
	@Override
	public User register(String email,  String password)
			throws EmailAleadyRegisteredException {
		return new User(email, password);
	}

  @Override
  public String info() {
    String className = getClass().getName();
    System.err.println("Resource implementation class " + className);
    return className;
  }
}
