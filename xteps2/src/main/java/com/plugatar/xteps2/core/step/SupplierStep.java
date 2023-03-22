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
package com.plugatar.xteps2.core.step;

import com.plugatar.xteps2.core.Keyword;
import com.plugatar.xteps2.core.StepExecutor;
import com.plugatar.xteps2.core.StepNotImplementedException;
import com.plugatar.xteps2.core.XtepsException;
import com.plugatar.xteps2.core.function.ThSupplier;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static com.plugatar.xteps2.core.step.StepObjectUtils.EMPTY_STRING;
import static com.plugatar.xteps2.core.step.StepObjectUtils.artifactMapArgs;
import static com.plugatar.xteps2.core.step.StepObjectUtils.copyMapAndPutArgs;
import static com.plugatar.xteps2.core.step.StepObjectUtils.currentStepExecutor;

/**
 * Supplier step object.
 *
 * @param <R> the type of the result
 */
public interface SupplierStep<R> extends
  ThSupplier<R, RuntimeException>,
  StepObject,
  RunnableStep,
  ConsumerStep<Object>,
  BiConsumerStep<Object, Object>,
  TriConsumerStep<Object, Object, Object>,
  FunctionStep<Object, R>,
  BiFunctionStep<Object, Object, R>,
  TriFunctionStep<Object, Object, Object, R> {

  /**
   * Performs this step.
   *
   * @return result
   */
  @Override
  R get();

  @Override
  SupplierStep<R> withArtifact(String key,
                               Object value);

  /**
   * Default {@code SupplierStep} implementation.
   *
   * @param <R> the type of the result
   */
  class Of<R> implements SupplierStep<R> {
    private final StepExecutor stepExecutor;
    private final Map<String, ?> artifacts;
    private final ThSupplier<? extends R, ?> action;

    /**
     * Ctor.
     *
     * @throws XtepsException if Xteps configuration is incorrect
     */
    public Of() {
      this(notImplementedAction());
    }

    /**
     * Ctor.
     *
     * @param action the step action
     * @throws XtepsException if Xteps configuration is incorrect
     *                        or if {@code action} arg is null
     */
    public Of(final ThSupplier<? extends R, ?> action) {
      this(Keyword.EMPTY, EMPTY_STRING, Collections.emptyMap(), EMPTY_STRING, action);
    }

    /**
     * Ctor.
     *
     * @param keyword the step keyword
     * @throws XtepsException if Xteps configuration is incorrect
     *                        or if {@code keyword} arg is null
     */
    public Of(final Keyword keyword) {
      this(keyword, notImplementedAction());
    }

    /**
     * Ctor.
     *
     * @param keyword the step keyword
     * @param action  the step action
     * @throws XtepsException if Xteps configuration is incorrect
     *                        or if {@code keyword} arg is null
     *                        or if {@code action} arg is null
     */
    public Of(final Keyword keyword,
              final ThSupplier<? extends R, ?> action) {
      this(keyword, EMPTY_STRING, Collections.emptyMap(), EMPTY_STRING, action);
    }

    /**
     * Ctor.
     *
     * @param name the step name
     * @throws XtepsException if Xteps configuration is incorrect
     *                        or if {@code name} arg is null
     */
    public Of(final String name) {
      this(name, notImplementedAction());
    }

    /**
     * Ctor.
     *
     * @param name   the step name
     * @param action the step action
     * @throws XtepsException if Xteps configuration is incorrect
     *                        or if {@code name} arg is null
     *                        or if {@code action} arg is null
     */
    public Of(final String name,
              final ThSupplier<? extends R, ?> action) {
      this(Keyword.EMPTY, name, Collections.emptyMap(), EMPTY_STRING, action);
    }

    /**
     * Ctor.
     *
     * @param keyword the step keyword
     * @param name    the step name
     * @throws XtepsException if Xteps configuration is incorrect
     *                        or if {@code keyword} arg is null
     *                        or if {@code name} arg is null
     */
    public Of(final Keyword keyword,
              final String name) {
      this(keyword, name, notImplementedAction());
    }

    /**
     * Ctor.
     *
     * @param keyword the step keyword
     * @param name    the step name
     * @param action  the step action
     * @throws XtepsException if Xteps configuration is incorrect
     *                        or if {@code keyword} arg is null
     *                        or if {@code name} arg is null
     *                        or if {@code action} arg is null
     */
    public Of(final Keyword keyword,
              final String name,
              final ThSupplier<? extends R, ?> action) {
      this(keyword, name, Collections.emptyMap(), EMPTY_STRING, action);
    }

    /**
     * Ctor.
     *
     * @param name the step name
     * @param desc the step description
     * @throws XtepsException if Xteps configuration is incorrect
     *                        or if {@code name} arg is null
     *                        or if {@code desc} arg is null
     */
    public Of(final String name,
              final String desc) {
      this(name, desc, notImplementedAction());
    }

    /**
     * Ctor.
     *
     * @param name   the step name
     * @param desc   the step description
     * @param action the step action
     * @throws XtepsException if Xteps configuration is incorrect
     *                        or if {@code name} arg is null
     *                        or if {@code desc} arg is null
     *                        or if {@code action} arg is null
     */
    public Of(final String name,
              final String desc,
              final ThSupplier<? extends R, ?> action) {
      this(Keyword.EMPTY, name, Collections.emptyMap(), desc, action);
    }

    /**
     * Ctor.
     *
     * @param keyword the step keyword
     * @param name    the step name
     * @param desc    the step description
     * @throws XtepsException if Xteps configuration is incorrect
     *                        or if {@code keyword} arg is null
     *                        or if {@code name} arg is null
     *                        or if {@code desc} arg is null
     */
    public Of(final Keyword keyword,
              final String name,
              final String desc) {
      this(keyword, name, desc, notImplementedAction());
    }

    /**
     * Ctor.
     *
     * @param keyword the step keyword
     * @param name    the step name
     * @param desc    the step description
     * @param action  the step action
     * @throws XtepsException if Xteps configuration is incorrect
     *                        or if {@code keyword} arg is null
     *                        or if {@code name} arg is null
     *                        or if {@code desc} arg is null
     *                        or if {@code action} arg is null
     */
    public Of(final Keyword keyword,
              final String name,
              final String desc,
              final ThSupplier<? extends R, ?> action) {
      this(keyword, name, Collections.emptyMap(), desc, action);
    }

    /**
     * Ctor.
     *
     * @param name   the step name
     * @param params the step params
     * @throws XtepsException if Xteps configuration is incorrect
     *                        or if {@code name} arg is null
     *                        or if {@code params} arg is null
     */
    public Of(final String name,
              final Map<String, ?> params) {
      this(name, params, notImplementedAction());
    }

    /**
     * Ctor.
     *
     * @param name   the step name
     * @param params the step params
     * @param action the step action
     * @throws XtepsException if Xteps configuration is incorrect
     *                        or if {@code name} arg is null
     *                        or if {@code params} arg is null
     *                        or if {@code action} arg is null
     */
    public Of(final String name,
              final Map<String, ?> params,
              final ThSupplier<? extends R, ?> action) {
      this(Keyword.EMPTY, name, params, EMPTY_STRING, action);
    }

    /**
     * Ctor.
     *
     * @param keyword the step keyword
     * @param name    the step name
     * @param params  the step params
     * @throws XtepsException if Xteps configuration is incorrect
     *                        or if {@code keyword} arg is null
     *                        or if {@code name} arg is null
     *                        or if {@code params} arg is null
     */
    public Of(final Keyword keyword,
              final String name,
              final Map<String, ?> params) {
      this(keyword, name, params, notImplementedAction());
    }

    /**
     * Ctor.
     *
     * @param keyword the step keyword
     * @param name    the step name
     * @param params  the step params
     * @param action  the step action
     * @throws XtepsException if Xteps configuration is incorrect
     *                        or if {@code keyword} arg is null
     *                        or if {@code name} arg is null
     *                        or if {@code params} arg is null
     *                        or if {@code action} arg is null
     */
    public Of(final Keyword keyword,
              final String name,
              final Map<String, ?> params,
              final ThSupplier<? extends R, ?> action) {
      this(keyword, name, params, EMPTY_STRING, action);
    }

    /**
     * Ctor.
     *
     * @param name   the step name
     * @param params the step params
     * @param desc   the step description
     * @throws XtepsException if Xteps configuration is incorrect
     *                        or if {@code name} arg is null
     *                        or if {@code params} arg is null
     *                        or if {@code desc} arg is null
     */
    public Of(final String name,
              final Map<String, ?> params,
              final String desc) {
      this(name, params, desc, notImplementedAction());
    }

    /**
     * Ctor.
     *
     * @param name   the step name
     * @param params the step params
     * @param desc   the step description
     * @param action the step action
     * @throws XtepsException if Xteps configuration is incorrect
     *                        or if {@code name} arg is null
     *                        or if {@code params} arg is null
     *                        or if {@code desc} arg is null
     *                        or if {@code action} arg is null
     */
    public Of(final String name,
              final Map<String, ?> params,
              final String desc,
              final ThSupplier<? extends R, ?> action) {
      this(Keyword.EMPTY, name, params, desc, action);
    }

    /**
     * Ctor.
     *
     * @param keyword the step keyword
     * @param name    the step name
     * @param params  the step params
     * @param desc    the step description
     * @throws XtepsException if Xteps configuration is incorrect
     *                        or if {@code keyword} arg is null
     *                        or if {@code name} arg is null
     *                        or if {@code params} arg is null
     *                        or if {@code desc} arg is null
     */
    public Of(final Keyword keyword,
              final String name,
              final Map<String, ?> params,
              final String desc) {
      this(keyword, name, params, desc, notImplementedAction());
    }

    /**
     * Ctor.
     *
     * @param keyword the step keyword
     * @param name    the step name
     * @param params  the step params
     * @param desc    the step description
     * @param action  the step action
     * @throws XtepsException if Xteps configuration is incorrect
     *                        or if {@code keyword} arg is null
     *                        or if {@code name} arg is null
     *                        or if {@code params} arg is null
     *                        or if {@code desc} arg is null
     *                        or if {@code action} arg is null
     */
    public Of(final Keyword keyword,
              final String name,
              final Map<String, ?> params,
              final String desc,
              final ThSupplier<? extends R, ?> action) {
      this(artifactMapArgs(keyword, name, params, desc), action);
    }

    /**
     * Ctor.
     *
     * @param artifacts the step artifacts
     * @throws XtepsException if Xteps configuration is incorrect
     *                        or if {@code artifacts} arg is null
     */
    public Of(final Map<String, ?> artifacts) {
      this(artifacts, notImplementedAction());
    }

    /**
     * Ctor.
     *
     * @param artifacts the step artifacts
     * @param action    the step action
     * @throws XtepsException if Xteps configuration is incorrect
     *                        or if {@code artifacts} arg is null
     *                        or if {@code action} arg is null
     */
    public Of(final Map<String, ?> artifacts,
              final ThSupplier<? extends R, ?> action) {
      this(currentStepExecutor(), artifacts, action);
    }

    /**
     * Ctor.
     *
     * @param stepExecutor the step executor
     * @param artifacts    the step artifacts
     * @throws XtepsException if {@code stepExecutor} arg is null
     *                        or if {@code artifacts} arg is null
     */
    public Of(final StepExecutor stepExecutor,
              final Map<String, ?> artifacts) {
      this(stepExecutor, artifacts, notImplementedAction());
    }

    /**
     * Ctor.
     *
     * @param stepExecutor the step executor
     * @param artifacts    the step artifact
     * @param action       the step action
     * @throws XtepsException if {@code stepExecutor} arg is null
     *                        or if {@code artifacts} arg is null
     *                        or if {@code action} arg is null
     */
    public Of(final StepExecutor stepExecutor,
              final Map<String, ?> artifacts,
              final ThSupplier<? extends R, ?> action) {
      if (stepExecutor == null) { throw new XtepsException("stepExecutor arg is null"); }
      if (artifacts == null) { throw new XtepsException("artifacts arg is null"); }
      if (action == null) { throw new XtepsException("action arg is null"); }
      this.stepExecutor = stepExecutor;
      this.artifacts = artifacts;
      this.action = action;
    }

    private static <R> ThSupplier<R, ?> notImplementedAction() {
      return () -> { throw new StepNotImplementedException(); };
    }

    @Override
    public final R get() {
      return this.stepExecutor.report(this.artifacts, this.action);
    }

    @Override
    public final void run() {
      this.get();
    }

    @Override
    public final void accept(final Object ignored) {
      this.get();
    }

    @Override
    public final void accept(final Object ignored1,
                             final Object ignored2) {
      this.get();
    }

    @Override
    public final void accept(final Object ignored1,
                             final Object ignored2,
                             final Object ignored3) {
      this.get();
    }

    @Override
    public final R apply(final Object ignored) {
      return this.get();
    }

    @Override
    public final R apply(final Object ignored1,
                         final Object ignored2) {
      return this.get();
    }

    @Override
    public final R apply(final Object ignored1,
                         final Object ignored2,
                         final Object ignored3) {
      return this.get();
    }

    @Override
    public final SupplierStep<R> withArtifact(final String key,
                                              final Object value) {
      return new SupplierStep.Of<>(this.stepExecutor, copyMapAndPutArgs(this.artifacts, key, value), this.action);
    }

    @Override
    public final Optional<Object> artifact(final String key) {
      if (key == null) { throw new XtepsException("key arg is null"); }
      return Optional.ofNullable(this.artifacts.getOrDefault(key, null));
    }
  }
}
