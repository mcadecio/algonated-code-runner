package com.daio.fyp;

import com.daio.fyp.response.Response;
import com.daio.fyp.runner.CodeOptions;
import com.daio.fyp.runner.CodeRunner;
import com.daio.fyp.runner.CodeRunnerSummary;
import com.daio.fyp.runner.calculator.ScalesEfficiencyCalculator;
import com.daio.fyp.runner.calculator.ScalesFitnessCalculator;
import com.daio.fyp.runner.calculator.TSPEfficiencyCalculator;
import com.daio.fyp.runner.calculator.TSPFitnessCalculator;
import com.google.gson.Gson;
import io.vertx.core.AbstractVerticle;
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

    private static final Gson gson = new Gson();

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
            var response = new Response(
                    false,
                    rc.failure().getMessage() == null ?
                            "Null Pointer Exception, Please review your code and try again" :
                            rc.failure().getMessage(),
                    Collections.emptyList(),
                    rc.getBodyAsJson().getJsonArray("data").getList(),
                    new CodeRunnerSummary()
            );

            rc.response().setChunked(true).end(gson.toJson(response));
        });

        router.get().handler(rc -> {
            logger.info("Receiving request on path -> {}", rc.normalisedPath());
            rc.next();
        });

        router.get("/hello").handler(rc -> rc.response().end("Hello, World!"));

        router.post("/exercise/submit/scales").handler(this::handleScalesRequest);

        router.post("/exercise/submit/tsp").handler(this::handleTSPRequest);

        httpServer = vertx.createHttpServer();
        httpServer
                .requestHandler(router)
                .listen(3030, "0.0.0.0", result -> {
                    logger.info("HTTP Server Started ...");
                    startPromise.complete();
                });
    }

    private void handleTSPRequest(RoutingContext rc) {
        final String prettyRequest = rc.getBodyAsJson().encodePrettily();
        logger.info("This was the code submitted -> \n{}", prettyRequest);


        final CodeOptions codeOptions = gson.fromJson(prettyRequest, CodeOptions.class);
        codeOptions.<List<List<Double>>, double[][]>setModifier(list ->
                list.stream().map(listOfList -> listOfList.stream()
                        .mapToDouble(Double::doubleValue)
                        .toArray()).toArray(double[][]::new));
        final CodeRunner codeRunner = new CodeRunner(codeOptions);

        codeRunner.compile();
        final List<Integer> result = codeRunner.execute();
        CodeRunnerSummary summary = codeRunner.getSummary(
                new TSPFitnessCalculator(),
                new TSPEfficiencyCalculator(),
                (double[][]) codeOptions.getModifier().apply(codeOptions.getData()),
                result
        );

        var response = new Response(
                codeRunner.isSuccess(),
                codeRunner.getErrorMessage(),
                result,
                codeOptions.getData(),
                summary
        );
        rc.response().setChunked(true).end(gson.toJson(response));
    }

    private void handleScalesRequest(RoutingContext rc) {
        final String prettyRequest = rc.getBodyAsJson().encodePrettily();
        logger.info("This was the code submitted -> \n{}", prettyRequest);


        final CodeOptions codeOptions = gson.fromJson(prettyRequest, CodeOptions.class);

        final CodeRunner codeRunner = new CodeRunner(codeOptions);

        codeRunner.compile();
        final List<Integer> result = codeRunner.execute();
        CodeRunnerSummary summary = codeRunner.getSummary(
                new ScalesFitnessCalculator(),
                new ScalesEfficiencyCalculator(),
                (List<Double>) codeOptions.getData(),
                result
        );

        var response = new Response(
                codeRunner.isSuccess(),
                codeRunner.getErrorMessage(),
                result,
                codeOptions.getData(),
                summary
        );
        rc.response().setChunked(true).end(gson.toJson(response));
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