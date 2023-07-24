# mp-plot-gql
A Kotlin Multiplatform application for web-rendering plots, with an underlying GraphQL client-server architecture.

## Architecture
The GraphQL server makes use of [GraphQL Kotlin](https://github.com/ExpediaGroup/graphql-kotlin),
which makes possible to define the GraphQL schema dynamically when building the project.

While the server is up and running, the [Apollo GraphQL](https://github.com/apollographql/apollo-kotlin) client
can introspect the server's schema for generating all the classes which provides type safety at compile time.

While the server is developed and built upon JVM, the client is built inside a Kotlin Multiplatform environment,
in particlare, it will be ported to a JS web-based application.

This application will make use of [Lets-Plot Kotlin](https://github.com/JetBrains/lets-plot-kotlin), for
displaying in the browser the received data from the server.

In the simple example provided by this applicaiton, the server will generate random 2D Points every 500ms
through a graphql [subscription](https://graphql.org/blog/subscriptions-in-graphql-and-relay/).
The client that subscribes to this, can specify a number of points to collect, real-time plotting
those points in a cartesian plane, and when the collections is finished, drawing a polinomial regression
line upon those points collected.
