Caused by: javax.validation.ConstraintDeclarationException:
	Only the root method of an overridden method in an inheritance hierarchy may be annotated with parameter constraints,
	but there are parameter constraints defined at all of the following overridden methods:
		ConstrainedMethod [location=UserResource$$$view1#register(), parameterMetaData=[ParameterMetaData [location=UserResource$$$view1#register(0)], name=arg0], constraints=[NotNull], isCascading=false], ParameterMetaData [location=UserResource$$$view1#register(1)], name=arg1], constraints=[], isCascading=false]], hasParameterConstraints=true]
		ConstrainedMethod [location=UserResource#register(), parameterMetaData=[ParameterMetaData [location=UserResource#register(0)],                 name=arg0], constraints=[NotNull], isCascading=false], ParameterMetaData [location=UserResource#register(1)],         name=arg1], constraints=[], isCascading=false]], hasParameterConstraints=true]


		curl -d email=foo@example.com -d password=bar http://localhost:8080/jboss-as-helloworld-rs/rest/user/