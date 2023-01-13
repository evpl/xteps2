/*
 * Copyright 2023 Evgenii Plugatar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
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
import com.plugatar.xteps2.core.function.ThTriConsumer;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static com.plugatar.xteps2.core.step.StepObjectUtils.EMPTY_STRING;
import static com.plugatar.xteps2.core.step.StepObjectUtils.artifactMapArgs;
import static com.plugatar.xteps2.core.step.StepObjectUtils.copyMapAndPutArgs;
import static com.plugatar.xteps2.core.step.StepObjectUtils.currentStepExecutor;
import static com.plugatar.xteps2.core.step.StepObjectUtils.emptyKeyword;

/**
 * TriConsumer step object.
 *
 * @param <C1> the type of the first context argument
 * @param <C2> the type of the second context argument
 * @param <C3> the type of the third context argument
 */
public interface TriConsumerStep<C1, C2, C3> extends
  ThTriConsumer<C1, C2, C3, RuntimeException>,
  StepObject {

  /**
   * Performs this step.
   *
   * @param c1 the first context argument
   * @param c2 the second context argument
   * @param c3 the third context argument
   */
  @Override
  void accept(C1 c1, C2 c2, C3 c3);

  @Override
  TriConsumerStep<C1, C2, C3> withArtifact(String name,
                                           Object value);

  /**
   * Default {@code TriConsumerStep} implementation.
   *
   * @param <C1> the type of the first context argument
   * @param <C2> the type of the second context argument
   * @param <C3> the type of the third context argument
   */
  class Of<C1, C2, C3> implements TriConsumerStep<C1, C2, C3> {
    private final StepExecutor stepExecutor;
    private final Map<String, ?> artifacts;
    private final ThTriConsumer<? super C1, ? super C2, ? super C3, ?> action;

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
    public Of(final ThTriConsumer<? super C1, ? super C2, ? super C3, ?> action) {
      this(emptyKeyword(), EMPTY_STRING, Collections.emptyMap(), EMPTY_STRING, action);
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
              final ThTriConsumer<? super C1, ? super C2, ? super C3, ?> action) {
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
              final ThTriConsumer<? super C1, ? super C2, ? super C3, ?> action) {
      this(emptyKeyword(), name, Collections.emptyMap(), EMPTY_STRING, action);
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
              final ThTriConsumer<? super C1, ? super C2, ? super C3, ?> action) {
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
              final ThTriConsumer<? super C1, ? super C2, ? super C3, ?> action) {
      this(emptyKeyword(), name, Collections.emptyMap(), desc, action);
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
              final ThTriConsumer<? super C1, ? super C2, ? super C3, ?> action) {
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
              final ThTriConsumer<? super C1, ? super C2, ? super C3, ?> action) {
      this(emptyKeyword(), name, params, EMPTY_STRING, action);
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
              final ThTriConsumer<? super C1, ? super C2, ? super C3, ?> action) {
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
              final ThTriConsumer<? super C1, ? super C2, ? super C3, ?> action) {
      this(emptyKeyword(), name, params, desc, action);
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
              final ThTriConsumer<? super C1, ? super C2, ? super C3, ?> action) {
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
              final ThTriConsumer<? super C1, ? super C2, ? super C3, ?> action) {
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
              final ThTriConsumer<? super C1, ? super C2, ? super C3, ?> action) {
      if (stepExecutor == null) { throw new XtepsException("stepExecutor arg is null"); }
      if (artifacts == null) { throw new XtepsException("artifacts arg is null"); }
      if (action == null) { throw new XtepsException("action arg is null"); }
      this.stepExecutor = stepExecutor;
      this.artifacts = artifacts;
      this.action = action;
    }

    private static <C1, C2, C3> ThTriConsumer<? super C1, ? super C2, ? super C3, ?> notImplementedAction() {
      return (c1, c2, c3) -> { throw new StepNotImplementedException(); };
    }

    @Override
    public final void accept(final C1 c1,
                             final C2 c2,
                             final C3 c3) {
      this.stepExecutor.report(this.artifacts, () -> {
        this.action.accept(c1, c2, c3);
        return null;
      });
    }

    @Override
    public final TriConsumerStep<C1, C2, C3> withArtifact(final String name,
                                                          final Object value) {
      return new TriConsumerStep.Of<>(this.stepExecutor, copyMapAndPutArgs(this.artifacts, name, value), this.action);
    }

    @Override
    public final Optional<Object> artifact(final String name) {
      if (name == null) { throw new XtepsException("name arg is null"); }
      return Optional.ofNullable(this.artifacts.getOrDefault(name, null));
    }
  }
}
