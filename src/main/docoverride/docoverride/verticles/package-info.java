/*
 * Copyright 2014 Red Hat, Inc.
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  The Apache License v2.0 is available at
 *  http://www.opensource.org/licenses/apache2.0.php
 *
 *  You may elect to redistribute this code under either of these licenses.
 */

/**
 * === Writing Verticles
 *
 * JavaScript verticles are implemented either as http://wiki.commonjs.org/wiki/Modules/1.1[CommonJS modules] or
 * https://www.npmjs.com/[NPM] modules
 *
 * Here's an example JavaScript verticle written as a CommonJS module.
 *
 * JavaScript verticles will have the following globals pre-set as a convenience:
 *
 * * +vertx+ - A reference to the Vertx object
 * * +console+ A reference to the <<console, console>> object
 *
 * ----
 *
 * // Start a timer
 * vertx.setPeriodic(1000, function() {
 *   console.log('Timer has fired');
 * });
 *
 * // Optional - called when verticle is undeployed
 * exports.vertxStop = function() {
 *   console.log('stop called');
 * }
 *
 *
 *
 * ----
 *
 * When the verticle is deployed the body of the script will be executed.
 *
 * Optionally a
 *
 * [[console]]
 * === The console
 *
 * Blah, blah
 *
 * JAVASCRIPT MOFO!
 *
 * Verticle classes must implement the {@link io.vertx.core.Verticle} interface.
 *
 * They can implement it directly if you like but usually it's easier to extend the abstract class {@link io.vertx.core.AbstractVerticle}.
 *
 * Here's an example verticle:
 *
 * ----
 * public class MyVerticle extends AbstractVerticle {
 *
 *   // Called when verticle is deployed
 *   public void start() {
 *   }
 *
 *   // Optional - called when verticle is undeployed
 *   public void stop() {
 *   }
 *
 * }
 * ----
 *
 * Normally you would override the start method like in the example above.
 *
 * When Vert.x deploys the verticle it will call the start method, and when the method has completed the verticle will
 * be considered started.
 *
 * You can also optionally override the stop method. This will be called by Vert.x when the verticle is undeployed and when
 * the method has completed the verticle will be considered stopped.
 *
 * === Asynchronous Verticle start and stop
 *
 * Sometimes you want to do something in your verticle start-up which takes some time and you don't want the verticle to
 * be considered deployed until that happens. For example you might want to deploy other verticles in the start method.
 *
 * You can't block waiting for the other verticles to deploy in your start method as that would break the Golden Rule.
 *
 * So how can you do this?
 *
 * The way to do it is to implement the *asynchronous* start method. This version of the method takes a Future as a parameter.
 * When the method returns the verticle will *not* be considered deployed yet. Some time later, after you've done everything
 * you need to do (e.g. start other verticles), you can call complete on the Future (or fail) to signal that you're done.
 *
 * Here's an example:
 *
 * ----
 * public class MyVerticle extends AbstractVerticle {
 *
 *   public void start(Future<Void> startFuture) {
 *     // Now deploy some other verticle:
 *
 *     vertx.deployVerticle("com.foo.OtherVerticle", res -> {
 *       if (res.succeeded()) {
 *         startFuture.complete();
 *       } else {
 *         startFuture.fail();
 *       }
 *     });
 *   }
 * }
 * ----
 *
 * Similarly, there is an asynchronous version of the stop method too. You use this if you want to do some verticle
 * cleanup that takes some time.
 *
 * ----
 * public class MyVerticle extends AbstractVerticle {
 *
 *   public void start() {
 *     // Do something
 *   }
 *
 *   public void stop(Future<Void> startFuture) {
 *     obj.doSomethingThatTakesTime(res -> {
 *       if (res.succeeded()) {
 *         startFuture.complete();
 *       } else {
 *         startFuture.fail();
 *       }
 *     });
 *   }
 * }
 * ----
 *
 * INFO: You don't need to manually undeploy child verticles started by a verticle, in the verticle's stop method. Vert.x
 * will automatically undeploy any child verticles when the parent is undeployed.
 */
@Document(fileName = "override/verticles.adoc")
package docoverride.verticles;

import io.vertx.docgen.Document;
