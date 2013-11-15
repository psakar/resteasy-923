# RESTEASY-923#

To download AS into target directory and use it for test run

    mvn verify

To use existing AS installation run

    mvn verify -Djboss.home=...

To use existing running AS installation run

    mvn verify -Djboss.home=... -DallowConnectingToRunningServer=true

To connect to AS running on different address:port then defualt localhost:8080

    -Djboss.bind.address=... -Djboss.bind.port=...


Current error
=============
```
Caused by: javax.validation.ConstraintDeclarationException:
	Only the root method of an overridden method in an inheritance hierarchy may be annotated with parameter constraints,
	but there are parameter constraints defined at all of the following overridden methods:
		ConstrainedMethod [location=UserResource$$$view1#register(), parameterMetaData=[ParameterMetaData [location=UserResource$$$view1#register(0)], name=arg0], constraints=[NotNull], isCascading=false], ParameterMetaData [location=UserResource$$$view1#register(1)], name=arg1], constraints=[], isCascading=false]], hasParameterConstraints=true]
		ConstrainedMethod [location=UserResource#register(), parameterMetaData=[ParameterMetaData [location=UserResource#register(0)],                 name=arg0], constraints=[NotNull], isCascading=false], ParameterMetaData [location=UserResource#register(1)],         name=arg1], constraints=[], isCascading=false]], hasParameterConstraints=true]
```


Manual test

1. start AS

2. mvn package jboss-as:deploy

3. curl -d email=foo@example.com -d password=bar http://localhost:8080/RESTEASY-923/rest/user/
