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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Step executor.
 */
public interface StepExecutor {

  /**
   * Executes {@code action}. Calls hooks in case of any exception.
   *
   * @param hooks  hooks
   * @param action the action
   * @param <R>    the type of the result
   * @return action result
   * @throws XtepsException if {@code hooks} arg is null
   *                        or if {@code action} arg is null
   */
  <R> R exec(HookContainer hooks,
             ThSupplier<? extends R, ?> action);

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
  <R> R report(Map<String, ?> artifacts,
               ThSupplier<? extends R, ?> action);

  /**
   * Default {@code StepExecutor} implementation.
   */
  class Default implements StepExecutor {
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
    public final <R> R exec(final HookContainer hooks,
                            final ThSupplier<? extends R, ?> action) {
      if (hooks == null) { throw new XtepsException("hooks arg is null"); }
      if (action == null) { throw new XtepsException("action arg is null"); }
      try {
        return action.get();
      } catch (final Throwable actionException) {
        hooks.callHooks(actionException);
        this.exceptionHandler.handle(actionException);
        throw sneakyThrow(actionException);
      }
    }

    @Override
    public final <R> R report(final Map<String, ?> artifacts,
                              final ThSupplier<? extends R, ?> action) {
      if (artifacts == null) { throw new XtepsException("artifacts arg is null"); }
      if (action == null) { throw new XtepsException("action arg is null"); }
      /* Step start */
      final List<Throwable> listenerExceptions = new ArrayList<>();
      final String uuid = UUID.randomUUID().toString();
      for (final StepListener listener : this.listeners) {
        try {
          listener.stepStarted(uuid, artifacts);
        } catch (final Throwable ex) {
          listenerExceptions.add(ex);
        }
      }
      /* Step action */
      Throwable stepException = null;
      R stepResult = null;
      try {
        stepResult = action.get();
      } catch (final Throwable ex) {
        stepException = ex;
      }
      if (stepException == null) { /* Step finish */
        for (final StepListener listener : this.listeners) {
          try {
            listener.stepPassed(uuid);
          } catch (final Throwable ex) {
            listenerExceptions.add(ex);
          }
        }
      } else { /* Step fail */
        for (final StepListener listener : this.listeners) {
          try {
            listener.stepFailed(uuid, stepException);
          } catch (final Throwable ex) {
            listenerExceptions.add(ex);
          }
        }
      }
      /* Exceptions processing */
      final Throwable finishException;
      if (stepException != null) {
        finishException = stepException;
        if (!listenerExceptions.isEmpty()) {
          listenerExceptions.forEach(finishException::addSuppressed);
        }
      } else if (!listenerExceptions.isEmpty()) {
        finishException = listenerExceptions.get(0);
        if (listenerExceptions.size() > 1) {
          listenerExceptions.remove(0);
          listenerExceptions.forEach(finishException::addSuppressed);
        }
      } else {
        finishException = null;
      }
      /* Result */
      if (finishException == null) {
        return stepResult;
      } else {
        this.exceptionHandler.handle(finishException);
        throw sneakyThrow(finishException);
      }
    }

    @SuppressWarnings("unchecked")
    private static <E extends Throwable> RuntimeException sneakyThrow(final Throwable exception) throws E {
      throw (E) exception;
    }
  }
}
