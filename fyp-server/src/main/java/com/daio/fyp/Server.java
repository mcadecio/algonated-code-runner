package com.daio.fyp;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import org.joor.Reflect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public class Server extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    private HttpServer httpServer;

    @Override
    public void start(Promise<Void> startPromise) {

        final var router = Router.router(vertx);

        router.route().handler(BodyHandler.create());

        // Cors
        router.route().handler(CorsHandler.create(".*://localhost:.*")
                .allowedMethod(io.vertx.core.http.HttpMethod.GET)
                .allowedMethod(io.vertx.core.http.HttpMethod.POST)
                .allowedMethod(io.vertx.core.http.HttpMethod.OPTIONS)
                .allowCredentials(true)
                .allowedHeader("Authorization")
                .allowedHeader("Access-Control-Request-Method")
                .allowedHeader("Access-Control-Allow-Credentials")
                .allowedHeader("Access-Control-Allow-Origin")
                .allowedHeader("Access-Control-Allow-Headers")
                .allowedHeader("Content-Type"));

        router.route().failureHandler(rc -> {
            logger.error("Error", rc.failure());
            rc.response().setChunked(true).end("There was an error, lol");
        });

        router.get().handler(rc -> {
            logger.info("Receiving request on path -> {}", rc.normalisedPath());
            rc.next();
        });

        router.get("/hello").handler(rc -> rc.response().end("Hello, World!"));

        router.post("/exercise/submit").handler(rc -> {
            final String prettyRequest = rc.getBodyAsJson().encodePrettily();
            logger.info("This was the code submitted -> \n{}", prettyRequest);

            final String result = new ExerciseRunner().run(rc.getBodyAsJson().getString("code"));
            rc.response().setChunked(true).end(result);
        });

        httpServer = vertx.createHttpServer();
        httpServer
                .requestHandler(router)
                .listen(3030, "0.0.0.0", result -> {
                    logger.info("HTTP Server Started ...");
                    startPromise.complete();
                });
    }

    @Override
    public void stop(Promise<Void> stopPromise) {
        httpServer.close(ar -> logger.info("Shutting Down HTTP Server"));
        logger.info("Shutting Down...");
        vertx.close();
        stopPromise.complete();
    }

    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(new Server());
    }

}

class ExerciseRunner {

    private static final String NAME = "com.example.Exercise";
    private static final String PACKAGE = "package com.example;\n";

    public String run(String codeContent) {

        String result;

        if (codeContent.matches("import (.|\\n)*")) {
            return "Cheeky, no imports please!";
        }

        try {
            result = Reflect.compile(
                    NAME,
                            PACKAGE+
                            codeContent

            ).create().call("print").get();
        } catch (Exception e) {
            result = e.getMessage();
        }
        return result;
    }

    public static void main(String[] args) {
        try {
            Supplier<String> supplier = Reflect.compile(
                    "com.example.HelloWorld",
                    "package com.example;\n" +
                            "class HelloWorld implements java.util.function.Supplier<String> {\n" +
                            "    public String get() {\n" +
                            "        return \"1\";\n" +
                            "    }\n" +
                            "}\n").create().call("geta").get();
        } catch (Exception e) {
            System.out.println(e);
        }
    }


}