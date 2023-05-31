/*
 * Copyright 2023 Evgenii Plugatar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.plugatar.xteps2.core;

import com.plugatar.xteps2.core.function.ThSupplier;

import java.util.Map;

/**
 * Step reporter.
 */
public interface StepReporter {

  /**
   * Start new step with given artifacts.
   *
   * @param artifacts the artifacts
   */
  void startStep(Map<String, ?> artifacts);

  /**
   * Pass current step.
   */
  void passStep();

  /**
   * Fail step with given exception.
   *
   * @param exception the exception
   */
  void failStep(Throwable exception);

  /**
   * Executes and report {@code action}.
   *
   * @param artifacts the artifacts
   * @param action    the action
   * @param <R>       the type of the result
   * @return action result
   * @throws XtepsException if {@code artifacts} arg is null
   *                        or if {@code action} arg is null
   */
  <R> R executeStep(Map<String, ?> artifacts,
                    ThSupplier<? extends R, ?> action);

  /**
   * Default {@code StepReporter} implementation.
   */
  class Default implements StepReporter {
    private final ExceptionHandler exceptionHandler;
    private final StepListener[] listeners;

    /**
     * Ctor.
     *
     * @param exceptionHandler the exception handler
     * @param listeners        the listeners list
     * @throws XtepsException if {@code exceptionHandler} arg is null
     *                        or if {@code listeners} arg is null
     */
    public Default(final ExceptionHandler exceptionHandler,
                   final StepListener[] listeners) {
      if (exceptionHandler == null) { throw new XtepsException("exceptionHandler arg is null"); }
      if (listeners == null) { throw new XtepsException("listeners arg is null"); }
      this.exceptionHandler = exceptionHandler;
      this.listeners = listeners;
    }

    @Override
    public final void startStep(final Map<String, ?> artifacts) {
      if (artifacts == null) { throw new XtepsException("artifacts arg is null"); }
      for (final StepListener listener : this.listeners) {
        listener.stepStarted(artifacts);
      }
    }

    @Override
    public final void passStep() {
      for (final StepListener listener : this.listeners) {
        listener.stepPassed();
      }
    }

    @Override
    public final void failStep(final Throwable exception) {
      if (exception == null) { throw new XtepsException("exception arg is null"); }
      for (final StepListener listener : this.listeners) {
        listener.stepFailed(exception);
      }
    }

    @Override
    public final <R> R executeStep(final Map<String, ?> artifacts,
                                   final ThSupplier<? extends R, ?> action) {
      if (artifacts == null) { throw new XtepsException("artifacts arg is null"); }
      if (action == null) { throw new XtepsException("action arg is null"); }
      this.startStep(artifacts);
      Throwable stepException = null;
      R stepResult = null;
      try {
        stepResult = action.get();
      } catch (final Throwable ex) {
        stepException = ex;
      }
      if (stepException == null) {
        this.passStep();
        return stepResult;
      } else {
        this.exceptionHandler.handle(stepException);
        this.failStep(stepException);
        throw sneakyThrow(stepException);
      }
    }

    @SuppressWarnings("unchecked")
    private static <E extends Throwable> RuntimeException sneakyThrow(final Throwable exception) throws E {
      throw (E) exception;
    }
  }

  /**
   * Fake {@code StepReporter} implementation.
   */
  class Fake implements StepReporter {
    private final ExceptionHandler exceptionHandler;

    /**
     * Ctor.
     *
     * @param exceptionHandler the exception handler
     * @throws XtepsException if {@code exceptionHandler} arg is null
     */
    public Fake(final ExceptionHandler exceptionHandler) {
      if (exceptionHandler == null) { throw new XtepsException("exceptionHandler arg is null"); }
      this.exceptionHandler = exceptionHandler;
    }

    @Override
    public final void startStep(final Map<String, ?> artifacts) {
      if (artifacts == null) { throw new XtepsException("artifacts arg is null"); }
    }

    @Override
    public final void passStep() {

    }

    @Override
    public final void failStep(final Throwable exception) {
      if (exception == null) { throw new XtepsException("exception arg is null"); }
      this.exceptionHandler.handle(exception);
    }

    @Override
    public final <R> R executeStep(final Map<String, ?> artifacts,
                                   final ThSupplier<? extends R, ?> action) {
      if (artifacts == null) { throw new XtepsException("artifacts arg is null"); }
      if (action == null) { throw new XtepsException("action arg is null"); }
      try {
        return ThSupplier.unchecked(action).get();
      } catch (final Throwable stepEx) {
        this.exceptionHandler.handle(stepEx);
        throw stepEx;
      }
    }
  }
}
