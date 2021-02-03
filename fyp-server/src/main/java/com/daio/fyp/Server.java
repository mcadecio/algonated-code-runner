package com.daio.fyp;

import com.daio.fyp.response.Response;
import com.daio.fyp.runner.CodeOptions;
import com.daio.fyp.runner.CodeRunnerSummary;
import com.daio.fyp.runner.ScalesCodeRunner;
import com.daio.fyp.runner.TSPCodeRunner;
import com.daio.fyp.runner.demo.DemoRequest;
import com.daio.fyp.runner.demo.ScalesRunner;
import com.daio.fyp.runner.demo.TSPRunner;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

public class Server extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    private final Gson gson = new Gson();
    private final ObjectMapper mapper = new ObjectMapper();

    private HttpServer httpServer;

    @Override
    public void start(Promise<Void> startPromise) {

        final var router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.route().handler(createCorsHandler());
        router.route().failureHandler(this::handleFailure);
        router.get().handler(rc -> {
            logger.info("Receiving request on path -> {}", rc.normalisedPath());
            rc.next();
        });

        router.get("/hello").handler(rc -> rc.response().end("Hello, World!"));

        router.post("/exercise/submit/").handler(rc -> {
            final String prettyRequest = rc.getBodyAsJson().encodePrettily();
            logger.info("This was the code submitted -> \n{}", prettyRequest);
            rc.next();
        });

        router.post("/exercise/submit/scales")
                .handler(rc -> executeBlocking(() -> handleScalesRequest(rc), rc));

        router.post("/exercise/demo")
                .handler(rc -> executeBlocking(() -> handleDemoRequest(rc), rc));

        router.post("/exercise/submit/tsp")
                .handler(rc -> executeBlocking(() -> handleTSPRequest(rc), rc));

        httpServer = vertx.createHttpServer();
        httpServer
                .requestHandler(router)
                .listen(getPort(), "0.0.0.0", result -> {
                    logger.info("HTTP Server Started ...");
                    startPromise.complete();
                });
    }

    private void handleFailure(RoutingContext rc) {
        logger.error("Error", rc.failure());
        var response = new Response()
                .setSuccess(false)
                .setConsoleOutput(
                        rc.failure().getMessage() == null ?
                                "Null Pointer Exception, Please review your code and try again" :
                                rc.failure().getMessage()
                ).setResult(Collections.emptyList())
                .setData(rc.getBodyAsJson().getJsonArray("data").getList())
                .setSummary(new CodeRunnerSummary());

        rc.response().setChunked(true).end(gson.toJson(response));
    }

    private int getPort() {
        String port = System.getProperty("heroku.port", "80");
        logger.info(port);
        return Integer.parseInt(port);
    }

    private String getAllowedDomain() {
        String allowedDomain = System.getProperty("cors.allowed.domain", ".*://localhost:.*");
        logger.info(allowedDomain);
        return allowedDomain;
    }

    private Handler<RoutingContext> createCorsHandler() {
        return CorsHandler.create(getAllowedDomain())
                .allowedMethod(io.vertx.core.http.HttpMethod.GET)
                .allowedMethod(io.vertx.core.http.HttpMethod.POST)
                .allowedMethod(io.vertx.core.http.HttpMethod.OPTIONS)
                .allowCredentials(true)
                .allowedHeader("Authorization")
                .allowedHeader("Access-Control-Request-Method")
                .allowedHeader("Access-Control-Allow-Credentials")
                .allowedHeader("Access-Control-Allow-Origin")
                .allowedHeader("Access-Control-Allow-Headers")
                .allowedHeader("Content-Type");
    }

    private void handleTSPRequest(RoutingContext rc) {
        Timer.runTimedTask(() -> {
            final CodeOptions codeOptions = gson.fromJson(rc.getBodyAsString(), CodeOptions.class);

            List<List<Double>> data = gson.<List<List<Double>>>fromJson(
                    rc.getBodyAsJson()
                            .getJsonArray("data")
                            .encode(), List.class);

            sendResponse(rc, Timer.runTimedTask(
                    () -> new TSPCodeRunner(codeOptions, data).compile().execute(),
                    "TSP Code Runner"
            ).toResponse());


        }, "TSP Request");
    }

    private void handleScalesRequest(RoutingContext rc) {
        Timer.runTimedTask(() -> {
            final String prettyRequest = rc.getBodyAsJson().encodePrettily();
            logger.debug("This was the code submitted -> \n{}", prettyRequest);

            final CodeOptions codeOptions = gson.fromJson(prettyRequest, CodeOptions.class);

            List<Double> data = gson.<List<Double>>fromJson(
                    rc.getBodyAsJson()
                            .getJsonArray("data")
                            .encode(),
                    List.class
            );

            sendResponse(rc, Timer.runTimedTask(
                    () -> new ScalesCodeRunner(codeOptions, data).compile().execute(),
                    "Code Runner"
            ).toResponse());

        }, "Scales Request");

    }

    private void handleDemoRequest(RoutingContext rc) {
        Timer.runTimedTask(() -> {

            Response response;
            if (rc.getBodyAsJson().getString("problem").equals("scales")) {
                DemoRequest<List<Double>> request = gson.<DemoRequest<List<Double>>>fromJson(rc.getBodyAsString(), DemoRequest.class);
                response = new ScalesRunner(request).run().toResponse();
            } else if (rc.getBodyAsJson().getString("problem").equals("tsp")) {
                DemoRequest<double[][]> request = gson.<DemoRequest<double[][]>>fromJson(rc.getBodyAsString(), DemoRequest.class);
                request.setData(gson.<List<List<Double>>>fromJson(rc.getBodyAsJson().getJsonArray("data").encode(), List.class)
                        .stream().map(listOfList -> listOfList.stream()
                                .mapToDouble(Double::doubleValue)
                                .toArray()).toArray(double[][]::new));
                response = new TSPRunner(request).run().toResponse();
            } else {
                response = new Response()
                        .setSuccess(false)
                        .setSolutions(Collections.emptyList())
                        .setConsoleOutput("I didn't understand your request")
                        .setResult(Collections.emptyList())
                        .setData(Collections.emptyList())
                        .setSummary(new CodeRunnerSummary());
            }

            sendResponse(rc, response);

        }, "Demo Request");

    }

    private void sendResponse(RoutingContext rc, Response response) {
        String chunk = Timer.runTimedTaskWithException(
                () -> mapper.writeValueAsString(response),
                "Mapper timer",
                "{}"
        );

        rc.response().setChunked(true).end(chunk);
    }

    private void executeBlocking(Runnable runnable, RoutingContext rc) {
        vertx.executeBlocking(promise -> {
            runnable.run();
            promise.complete();
        }, result -> {
            if (result.failed()) {
                rc.fail(result.cause());
            }
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