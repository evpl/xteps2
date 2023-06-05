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
package com.plugatar.xteps2;

import com.plugatar.xteps2.core.Keyword;
import com.plugatar.xteps2.core.StepNotImplementedError;
import com.plugatar.xteps2.core.XtepsException;
import com.plugatar.xteps2.core.function.ThRunnable;
import com.plugatar.xteps2.core.function.ThSupplier;
import com.plugatar.xteps2.core.step.BiConsumerStep;
import com.plugatar.xteps2.core.step.BiFunctionStep;
import com.plugatar.xteps2.core.step.ConsumerStep;
import com.plugatar.xteps2.core.step.FunctionStep;
import com.plugatar.xteps2.core.step.RunnableStep;
import com.plugatar.xteps2.core.step.SupplierStep;
import com.plugatar.xteps2.core.step.TriConsumerStep;
import com.plugatar.xteps2.core.step.TriFunctionStep;

import java.util.Map;

/**
 * Utility class. Contains methods for immediately performing steps.
 * <p>
 * Empty step methods:
 * <ul>
 * <li>{@link #emptyStep(Keyword)}</li>
 * <li>{@link #emptyStep(String)}</li>
 * <li>{@link #emptyStep(Keyword, String)}</li>
 * <li>{@link #emptyStep(String, String)}</li>
 * <li>{@link #emptyStep(Keyword, String, String)}</li>
 * <li>{@link #emptyStep(String, Map)}</li>
 * <li>{@link #emptyStep(Keyword, String, Map)}</li>
 * <li>{@link #emptyStep(String, Map, String)}</li>
 * <li>{@link #emptyStep(Keyword, String, Map, String)}</li>
 * <li>{@link #emptyStep(Map)}</li>
 * </ul>
 * Runnable step methods:
 * <ul>
 * <li>{@link #step(ThRunnable)}</li>
 * <li>{@link #step(Keyword, ThRunnable)}</li>
 * <li>{@link #step(String, ThRunnable)}</li>
 * <li>{@link #step(Keyword, String, ThRunnable)}</li>
 * <li>{@link #step(String, String, ThRunnable)}</li>
 * <li>{@link #step(Keyword, String, String, ThRunnable)}</li>
 * <li>{@link #step(String, Map, ThRunnable)}</li>
 * <li>{@link #step(Keyword, String, Map, ThRunnable)}</li>
 * <li>{@link #step(String, Map, String, ThRunnable)}</li>
 * <li>{@link #step(Keyword, String, Map, String, ThRunnable)}</li>
 * <li>{@link #step(Map, ThRunnable)}</li>
 * </ul>
 * Supplier steps methods:
 * <ul>
 * <li>{@link #step(ThSupplier)}</li>
 * <li>{@link #step(Keyword, ThSupplier)}</li>
 * <li>{@link #step(String, ThSupplier)}</li>
 * <li>{@link #step(Keyword, String, ThSupplier)}</li>
 * <li>{@link #step(String, String, ThSupplier)}</li>
 * <li>{@link #step(Keyword, String, String, ThSupplier)}</li>
 * <li>{@link #step(String, Map, ThSupplier)}</li>
 * <li>{@link #step(Keyword, String, Map, ThSupplier)}</li>
 * <li>{@link #step(String, Map, String, ThSupplier)}</li>
 * <li>{@link #step(Keyword, String, Map, String, ThSupplier)}</li>
 * <li>{@link #step(Map, ThSupplier)}</li>
 * </ul>
 * Not implemented step methods:
 * <ul>
 * <li>{@link #step()}</li>
 * <li>{@link #step(Keyword)}</li>
 * <li>{@link #step(String)}</li>
 * <li>{@link #step(Keyword, String)}</li>
 * <li>{@link #step(String, String)}</li>
 * <li>{@link #step(Keyword, String, String)}</li>
 * <li>{@link #step(String, Map)}</li>
 * <li>{@link #step(Keyword, String, Map)}</li>
 * <li>{@link #step(String, Map, String)}</li>
 * <li>{@link #step(Keyword, String, Map, String)}</li>
 * <li>{@link #step(Map)}</li>
 * </ul>
 * StepObject step methods:
 * <ul>
 * <li>{@link #step(RunnableStep)}</li>
 * <li>{@link #step(SupplierStep)}</li>
 * <li>{@link #step(ConsumerStep, Object)}</li>
 * <li>{@link #step(BiConsumerStep, Object, Object)}</li>
 * <li>{@link #step(TriConsumerStep, Object, Object, Object)}</li>
 * <li>{@link #step(FunctionStep, Object)}</li>
 * <li>{@link #step(BiFunctionStep, Object, Object)}</li>
 * <li>{@link #step(TriFunctionStep, Object, Object, Object)}</li>
 * </ul>
 */
public final class Steps {

  /**
   * Utility class ctor.
   */
  private Steps() {
  }

  //region Empty step methods

  /**
   * Performs step without action.
   *
   * @param keyword the step keyword
   * @throws XtepsException if Xteps configuration is incorrect
   *                        or if {@code keyword} arg is null
   */
  public static void emptyStep(final Keyword keyword) {
    new RunnableStep.Of(keyword, () -> { }).run();
  }

  /**
   * Performs step without action.
   *
   * @param name the step name
   * @throws XtepsException if Xteps configuration is incorrect
   *                        or if {@code name} arg is null
   */
  public static void emptyStep(final String name) {
    new RunnableStep.Of(name, () -> { }).run();
  }

  /**
   * Performs step without action.
   *
   * @param keyword the step keyword
   * @param name    the step name
   * @throws XtepsException if Xteps configuration is incorrect
   *                        or if {@code keyword} arg is null
   *                        or if {@code name} arg is null
   */
  public static void emptyStep(final Keyword keyword,
                               final String name) {
    new RunnableStep.Of(keyword, name, () -> { }).run();
  }

  /**
   * Performs step without action.
   *
   * @param name the step name
   * @param desc the step description
   * @throws XtepsException if Xteps configuration is incorrect
   *                        or if {@code name} arg is null
   *                        or if {@code desc} arg is null
   */
  public static void emptyStep(final String name,
                               final String desc) {
    new RunnableStep.Of(name, desc, () -> { }).run();
  }

  /**
   * Performs step without action.
   *
   * @param keyword the step keyword
   * @param name    the step name
   * @param desc    the step description
   * @throws XtepsException if Xteps configuration is incorrect
   *                        or if {@code keyword} arg is null
   *                        or if {@code name} arg is null
   *                        or if {@code desc} arg is null
   */
  public static void emptyStep(final Keyword keyword,
                               final String name,
                               final String desc) {
    new RunnableStep.Of(keyword, name, desc, () -> { }).run();
  }

  /**
   * Performs step without action.
   *
   * @param name   the step name
   * @param params the step params
   * @throws XtepsException if Xteps configuration is incorrect
   *                        or if {@code name} arg is null
   *                        or if {@code params} arg is null
   */
  public static void emptyStep(final String name,
                               final Map<String, ?> params) {
    new RunnableStep.Of(name, params, () -> { }).run();
  }

  /**
   * Performs step without action.
   *
   * @param keyword the step keyword
   * @param name    the step name
   * @param params  the step params
   * @throws XtepsException if Xteps configuration is incorrect
   *                        or if {@code keyword} arg is null
   *                        or if {@code name} arg is null
   *                        or if {@code params} arg is null
   */
  public static void emptyStep(final Keyword keyword,
                               final String name,
                               final Map<String, ?> params) {
    new RunnableStep.Of(keyword, name, params, () -> { }).run();
  }

  /**
   * Performs step without action.
   *
   * @param name   the step name
   * @param params the step params
   * @param desc   the step description
   * @throws XtepsException if Xteps configuration is incorrect
   *                        or if {@code name} arg is null
   *                        or if {@code params} arg is null
   *                        or if {@code desc} arg is null
   */
  public static void emptyStep(final String name,
                               final Map<String, ?> params,
                               final String desc) {
    new RunnableStep.Of(name, params, desc, () -> { }).run();
  }

  /**
   * Performs step without action.
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
  public static void emptyStep(final Keyword keyword,
                               final String name,
                               final Map<String, ?> params,
                               final String desc) {
    new RunnableStep.Of(keyword, name, params, desc, () -> { }).run();
  }

  /**
   * Performs step without action.
   *
   * @param artifacts the step artifacts
   * @throws XtepsException if Xteps configuration is incorrect
   *                        or if {@code artifacts} arg is null
   */
  public static void emptyStep(final Map<String, ?> artifacts) {
    new RunnableStep.Of(artifacts, () -> { }).run();
  }

  //endregion

  //region Runnable step methods

  /**
   * Performs given action as a step.
   *
   * @param action the step action
   * @throws XtepsException if Xteps configuration is incorrect
   *                        or if {@code action} arg is null
   */
  public static void step(final ThRunnable<?> action) {
    new RunnableStep.Of(action).run();
  }

  /**
   * Performs given action as a step.
   *
   * @param keyword the step keyword
   * @param action  the step action
   * @throws XtepsException if Xteps configuration is incorrect
   *                        or if {@code keyword} arg is null
   *                        or if {@code action} arg is null
   */
  public static void step(final Keyword keyword,
                          final ThRunnable<?> action) {
    new RunnableStep.Of(keyword, action).run();
  }

  /**
   * Performs given action as a step.
   *
   * @param name   the step name
   * @param action the step action
   * @throws XtepsException if Xteps configuration is incorrect
   *                        or if {@code name} arg is null
   *                        or if {@code action} arg is null
   */
  public static void step(final String name,
                          final ThRunnable<?> action) {
    new RunnableStep.Of(name, action).run();
  }

  /**
   * Performs given action as a step.
   *
   * @param keyword the step keyword
   * @param name    the step name
   * @param action  the step action
   * @throws XtepsException if Xteps configuration is incorrect
   *                        or if {@code keyword} arg is null
   *                        or if {@code name} arg is null
   *                        or if {@code action} arg is null
   */
  public static void step(final Keyword keyword,
                          final String name,
                          final ThRunnable<?> action) {
    new RunnableStep.Of(keyword, name, action).run();
  }

  /**
   * Performs given action as a step.
   *
   * @param name   the step name
   * @param desc   the step description
   * @param action the step action
   * @throws XtepsException if Xteps configuration is incorrect
   *                        or if {@code name} arg is null
   *                        or if {@code desc} arg is null
   *                        or if {@code action} arg is null
   */
  public static void step(final String name,
                          final String desc,
                          final ThRunnable<?> action) {
    new RunnableStep.Of(name, desc, action).run();
  }

  /**
   * Performs given action as a step.
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
  public static void step(final Keyword keyword,
                          final String name,
                          final String desc,
                          final ThRunnable<?> action) {
    new RunnableStep.Of(keyword, name, desc, action).run();
  }

  /**
   * Performs given action as a step.
   *
   * @param name   the step name
   * @param params the step params
   * @param action the step action
   * @throws XtepsException if Xteps configuration is incorrect
   *                        or if {@code name} arg is null
   *                        or if {@code params} arg is null
   *                        or if {@code action} arg is null
   */
  public static void step(final String name,
                          final Map<String, ?> params,
                          final ThRunnable<?> action) {
    new RunnableStep.Of(name, params, action).run();
  }

  /**
   * Performs given action as a step.
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
  public static void step(final Keyword keyword,
                          final String name,
                          final Map<String, ?> params,
                          final ThRunnable<?> action) {
    new RunnableStep.Of(keyword, name, params, action).run();
  }

  /**
   * Performs given action as a step.
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
  public static void step(final String name,
                          final Map<String, ?> params,
                          final String desc,
                          final ThRunnable<?> action) {
    new RunnableStep.Of(name, params, desc, action).run();
  }

  /**
   * Performs given action as a step.
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
  public static void step(final Keyword keyword,
                          final String name,
                          final Map<String, ?> params,
                          final String desc,
                          final ThRunnable<?> action) {
    new RunnableStep.Of(keyword, name, params, desc, action).run();
  }

  /**
   * Performs given action as a step.
   *
   * @param artifacts the step artifacts
   * @param action    the step action
   * @throws XtepsException if Xteps configuration is incorrect
   *                        or if {@code artifacts} arg is null
   *                        or if {@code action} arg is null
   */
  public static void step(final Map<String, ?> artifacts,
                          final ThRunnable<?> action) {
    new RunnableStep.Of(artifacts, action).run();
  }

  //endregion

  //region Supplier step methods

  /**
   * Performs given action as a step.
   *
   * @param action the step action
   * @param <R>    the type of the step result
   * @return step result
   * @throws XtepsException if Xteps configuration is incorrect
   *                        or if {@code action} arg is null
   */
  public static <R> R step(final ThSupplier<? extends R, ?> action) {
    return new SupplierStep.Of<>(action).get();
  }

  /**
   * Performs given action as a step.
   *
   * @param keyword the step keyword
   * @param action  the step action
   * @param <R>     the type of the step result
   * @return step result
   * @throws XtepsException if Xteps configuration is incorrect
   *                        or if {@code keyword} arg is null
   *                        or if {@code action} arg is null
   */
  public static <R> R step(final Keyword keyword,
                           final ThSupplier<? extends R, ?> action) {
    return new SupplierStep.Of<>(keyword, action).get();
  }

  /**
   * Performs given action as a step.
   *
   * @param name   the step name
   * @param action the step action
   * @param <R>    the type of the step result
   * @return step result
   * @throws XtepsException if Xteps configuration is incorrect
   *                        or if {@code name} arg is null
   *                        or if {@code action} arg is null
   */
  public static <R> R step(final String name,
                           final ThSupplier<? extends R, ?> action) {
    return new SupplierStep.Of<>(name, action).get();
  }

  /**
   * Performs given action as a step.
   *
   * @param keyword the step keyword
   * @param name    the step name
   * @param action  the step action
   * @param <R>     the type of the step result
   * @return step result
   * @throws XtepsException if Xteps configuration is incorrect
   *                        or if {@code keyword} arg is null
   *                        or if {@code name} arg is null
   *                        or if {@code action} arg is null
   */
  public static <R> R step(final Keyword keyword,
                           final String name,
                           final ThSupplier<? extends R, ?> action) {
    return new SupplierStep.Of<>(keyword, name, action).get();
  }

  /**
   * Performs given action as a step.
   *
   * @param name   the step name
   * @param desc   the step description
   * @param action the step action
   * @param <R>    the type of the step result
   * @return step result
   * @throws XtepsException if Xteps configuration is incorrect
   *                        or if {@code name} arg is null
   *                        or if {@code desc} arg is null
   *                        or if {@code action} arg is null
   */
  public static <R> R step(final String name,
                           final String desc,
                           final ThSupplier<? extends R, ?> action) {
    return new SupplierStep.Of<>(name, desc, action).get();
  }

  /**
   * Performs given action as a step.
   *
   * @param keyword the step keyword
   * @param name    the step name
   * @param desc    the step description
   * @param action  the step action
   * @param <R>     the type of the step result
   * @return step result
   * @throws XtepsException if Xteps configuration is incorrect
   *                        or if {@code keyword} arg is null
   *                        or if {@code name} arg is null
   *                        or if {@code desc} arg is null
   *                        or if {@code action} arg is null
   */
  public static <R> R step(final Keyword keyword,
                           final String name,
                           final String desc,
                           final ThSupplier<? extends R, ?> action) {
    return new SupplierStep.Of<>(keyword, name, desc, action).get();
  }

  /**
   * Performs given action as a step.
   *
   * @param name   the step name
   * @param params the step params
   * @param action the step action
   * @param <R>    the type of the step result
   * @return step result
   * @throws XtepsException if Xteps configuration is incorrect
   *                        or if {@code name} arg is null
   *                        or if {@code params} arg is null
   *                        or if {@code action} arg is null
   */
  public static <R> R step(final String name,
                           final Map<String, ?> params,
                           final ThSupplier<? extends R, ?> action) {
    return new SupplierStep.Of<>(name, params, action).get();
  }

  /**
   * Performs given action as a step.
   *
   * @param keyword the step keyword
   * @param name    the step name
   * @param params  the step params
   * @param action  the step action
   * @param <R>     the type of the step result
   * @return step result
   * @throws XtepsException if Xteps configuration is incorrect
   *                        or if {@code keyword} arg is null
   *                        or if {@code name} arg is null
   *                        or if {@code params} arg is null
   *                        or if {@code action} arg is null
   */
  public static <R> R step(final Keyword keyword,
                           final String name,
                           final Map<String, ?> params,
                           final ThSupplier<? extends R, ?> action) {
    return new SupplierStep.Of<>(keyword, name, params, action).get();
  }

  /**
   * Performs given action as a step.
   *
   * @param name   the step name
   * @param params the step params
   * @param desc   the step description
   * @param action the step action
   * @param <R>    the type of the step result
   * @return step result
   * @throws XtepsException if Xteps configuration is incorrect
   *                        or if {@code name} arg is null
   *                        or if {@code params} arg is null
   *                        or if {@code desc} arg is null
   *                        or if {@code action} arg is null
   */
  public static <R> R step(final String name,
                           final Map<String, ?> params,
                           final String desc,
                           final ThSupplier<? extends R, ?> action) {
    return new SupplierStep.Of<>(name, params, desc, action).get();
  }

  /**
   * Performs given action as a step.
   *
   * @param keyword the step keyword
   * @param name    the step name
   * @param params  the step params
   * @param desc    the step description
   * @param action  the step action
   * @param <R>     the type of the step result
   * @return step result
   * @throws XtepsException if Xteps configuration is incorrect
   *                        or if {@code keyword} arg is null
   *                        or if {@code name} arg is null
   *                        or if {@code params} arg is null
   *                        or if {@code desc} arg is null
   *                        or if {@code action} arg is null
   */
  public static <R> R step(final Keyword keyword,
                           final String name,
                           final Map<String, ?> params,
                           final String desc,
                           final ThSupplier<? extends R, ?> action) {
    return new SupplierStep.Of<>(keyword, name, params, desc, action).get();
  }

  /**
   * Performs given action as a step.
   *
   * @param artifacts the step artifacts
   * @param action    the step action
   * @param <R>       the type of the step result
   * @return step result
   * @throws XtepsException if Xteps configuration is incorrect
   *                        or if {@code artifacts} arg is null
   *                        or if {@code action} arg is null
   */
  public static <R> R step(final Map<String, ?> artifacts,
                           final ThSupplier<? extends R, ?> action) {
    return new SupplierStep.Of<>(artifacts, action).get();
  }

  //endregion

  //region Not implemented step methods

  /**
   * Performs a step that throws a {@link StepNotImplementedError}.
   *
   * @param <R> the type of the step result
   * @return step result
   * @throws XtepsException          if Xteps configuration is incorrect
   * @throws StepNotImplementedError in any case
   */
  public static <R> R step() {
    return new SupplierStep.Of<R>().get();
  }

  /**
   * Performs a step that throws a {@link StepNotImplementedError}.
   *
   * @param keyword the step keyword
   * @param <R>     the type of the step result
   * @return step result
   * @throws XtepsException          if Xteps configuration is incorrect
   *                                 or if {@code keyword} arg is null
   * @throws StepNotImplementedError in any other case
   */
  public static <R> R step(final Keyword keyword) {
    return new SupplierStep.Of<R>(keyword).get();
  }

  /**
   * Performs a step that throws a {@link StepNotImplementedError}.
   *
   * @param name the step name
   * @param <R>  the type of the step result
   * @return step result
   * @throws XtepsException          if Xteps configuration is incorrect
   *                                 or if {@code name} arg is null
   * @throws StepNotImplementedError in any other case
   */
  public static <R> R step(final String name) {
    return new SupplierStep.Of<R>(name).get();
  }

  /**
   * Performs a step that throws a {@link StepNotImplementedError}.
   *
   * @param keyword the step keyword
   * @param name    the step name
   * @param <R>     the type of the step result
   * @return step result
   * @throws XtepsException          if Xteps configuration is incorrect
   *                                 or if {@code keyword} arg is null
   *                                 or if {@code name} arg is null
   * @throws StepNotImplementedError in any other case
   */
  public static <R> R step(final Keyword keyword,
                           final String name) {
    return new SupplierStep.Of<R>(keyword, name).get();
  }

  /**
   * Performs a step that throws a {@link StepNotImplementedError}.
   *
   * @param name the step name
   * @param desc the step description
   * @param <R>  the type of the step result
   * @return step result
   * @throws XtepsException          if Xteps configuration is incorrect
   *                                 or if {@code name} arg is null
   *                                 or if {@code desc} arg is null
   * @throws StepNotImplementedError in any other case
   */
  public static <R> R step(final String name,
                           final String desc) {
    return new SupplierStep.Of<R>(name, desc).get();
  }

  /**
   * Performs a step that throws a {@link StepNotImplementedError}.
   *
   * @param keyword the step keyword
   * @param name    the step name
   * @param desc    the step description
   * @param <R>     the type of the step result
   * @return step result
   * @throws XtepsException          if Xteps configuration is incorrect
   *                                 or if {@code keyword} arg is null
   *                                 or if {@code name} arg is null
   *                                 or if {@code desc} arg is null
   * @throws StepNotImplementedError in any other case
   */
  public static <R> R step(final Keyword keyword,
                           final String name,
                           final String desc) {
    return new SupplierStep.Of<R>(keyword, name, desc).get();
  }

  /**
   * Performs a step that throws a {@link StepNotImplementedError}.
   *
   * @param name   the step name
   * @param params the step params
   * @param <R>    the type of the step result
   * @return step result
   * @throws XtepsException          if Xteps configuration is incorrect
   *                                 or if {@code name} arg is null
   *                                 or if {@code params} arg is null
   * @throws StepNotImplementedError in any other case
   */
  public static <R> R step(final String name,
                           final Map<String, ?> params) {
    return new SupplierStep.Of<R>(name, params).get();
  }

  /**
   * Performs a step that throws a {@link StepNotImplementedError}.
   *
   * @param keyword the step keyword
   * @param name    the step name
   * @param params  the step params
   * @param <R>     the type of the step result
   * @return step result
   * @throws XtepsException          if Xteps configuration is incorrect
   *                                 or if {@code keyword} arg is null
   *                                 or if {@code name} arg is null
   *                                 or if {@code params} arg is null
   * @throws StepNotImplementedError in any other case
   */
  public static <R> R step(final Keyword keyword,
                           final String name,
                           final Map<String, ?> params) {
    return new SupplierStep.Of<R>(keyword, name, params).get();
  }

  /**
   * Performs a step that throws a {@link StepNotImplementedError}.
   *
   * @param name   the step name
   * @param params the step params
   * @param desc   the step description
   * @param <R>    the type of the step result
   * @return step result
   * @throws XtepsException          if Xteps configuration is incorrect
   *                                 or if {@code name} arg is null
   *                                 or if {@code params} arg is null
   *                                 or if {@code desc} arg is null
   * @throws StepNotImplementedError in any other case
   */
  public static <R> R step(final String name,
                           final Map<String, ?> params,
                           final String desc) {
    return new SupplierStep.Of<R>(name, params, desc).get();
  }

  /**
   * Performs a step that throws a {@link StepNotImplementedError}.
   *
   * @param keyword the step keyword
   * @param name    the step name
   * @param params  the step params
   * @param desc    the step description
   * @param <R>     the type of the step result
   * @return step result
   * @throws XtepsException          if Xteps configuration is incorrect
   *                                 or if {@code keyword} arg is null
   *                                 or if {@code name} arg is null
   *                                 or if {@code params} arg is null
   *                                 or if {@code desc} arg is null
   * @throws StepNotImplementedError in any other case
   */
  public static <R> R step(final Keyword keyword,
                           final String name,
                           final Map<String, ?> params,
                           final String desc) {
    return new SupplierStep.Of<R>(keyword, name, params, desc).get();
  }

  /**
   * Performs a step that throws a {@link StepNotImplementedError}.
   *
   * @param artifacts the step artifacts
   * @param <R>       the type of the step result
   * @return step result
   * @throws XtepsException          if Xteps configuration is incorrect
   *                                 or if {@code artifacts} arg is null
   * @throws StepNotImplementedError in any other case
   */
  public static <R> R step(final Map<String, ?> artifacts) {
    return new SupplierStep.Of<R>(artifacts).get();
  }

  //endregion

  //region StepObject step methods

  /**
   * Performs given {@code RunnableStep}.
   *
   * @param step the step
   * @throws XtepsException if {@code step} arg is null
   */
  public static void step(final RunnableStep step) {
    if (step == null) { throw new XtepsException("step arg is null"); }
    step.run();
  }

  /**
   * Performs given {@code SupplierStep}.
   *
   * @param step the step
   * @param <R>  the type of the step result
   * @return step result
   * @throws XtepsException if {@code step} arg is null
   */
  public static <R> R step(final SupplierStep<? extends R> step) {
    if (step == null) { throw new XtepsException("step arg is null"); }
    return step.get();
  }

  /**
   * Performs given {@code ConsumerStep}.
   *
   * @param step the step
   * @param c    the step context
   * @param <C>  the type of the step context
   * @throws XtepsException if {@code step} arg is null
   */
  public static <C> void step(final ConsumerStep<? super C> step,
                              final C c) {
    if (step == null) { throw new XtepsException("step arg is null"); }
    step.accept(c);
  }

  /**
   * Performs given {@code BiConsumerStep}.
   *
   * @param step the step
   * @param c1   the first step context
   * @param c2   the second step context
   * @param <C1> the type of the first step context
   * @param <C2> the type of the second step context
   * @throws XtepsException if {@code step} arg is null
   */
  public static <C1, C2> void step(final BiConsumerStep<? super C1, ? super C2> step,
                                   final C1 c1,
                                   final C2 c2) {
    if (step == null) { throw new XtepsException("step arg is null"); }
    step.accept(c1, c2);
  }

  /**
   * Performs given {@code TriConsumerStep}.
   *
   * @param step the step
   * @param c1   the first step context
   * @param c2   the second step context
   * @param c3   the third step context
   * @param <C1> the type of the first step context
   * @param <C2> the type of the second step context
   * @param <C3> the type of the third step context
   * @throws XtepsException if {@code step} arg is null
   */
  public static <C1, C2, C3> void step(final TriConsumerStep<? super C1, ? super C2, ? super C3> step,
                                       final C1 c1,
                                       final C2 c2,
                                       final C3 c3) {
    if (step == null) { throw new XtepsException("step arg is null"); }
    step.accept(c1, c2, c3);
  }

  /**
   * Performs given {@code FunctionStep}.
   *
   * @param step the step
   * @param c    the step context
   * @param <C>  the type of the step context
   * @param <R>  the type of the step result
   * @return step result
   * @throws XtepsException if {@code step} arg is null
   */
  public static <C, R> R step(final FunctionStep<? super C, ? extends R> step,
                              final C c) {
    if (step == null) { throw new XtepsException("step arg is null"); }
    return step.apply(c);
  }

  /**
   * Performs given {@code BiFunctionStep}.
   *
   * @param step the step
   * @param c1   the first step context
   * @param c2   the second step context
   * @param <C1> the type of the first step context
   * @param <C2> the type of the second step context
   * @param <R>  the type of the step result
   * @return step result
   * @throws XtepsException if {@code step} arg is null
   */
  public static <C1, C2, R> R step(final BiFunctionStep<? super C1, ? super C2, ? extends R> step,
                                   final C1 c1,
                                   final C2 c2) {
    if (step == null) { throw new XtepsException("step arg is null"); }
    return step.apply(c1, c2);
  }

  /**
   * Performs given {@code TriFunctionStep}.
   *
   * @param step the step
   * @param c1   the first step context
   * @param c2   the second step context
   * @param c3   the third step context
   * @param <C1> the type of the first step context
   * @param <C2> the type of the second step context
   * @param <C3> the type of the third step context
   * @param <R>  the type of the step result
   * @return step result
   * @throws XtepsException if {@code step} arg is null
   */
  public static <C1, C2, C3, R> R step(final TriFunctionStep<? super C1, ? super C2, ? super C3, ? extends R> step,
                                       final C1 c1,
                                       final C2 c2,
                                       final C3 c3) {
    if (step == null) { throw new XtepsException("step arg is null"); }
    return step.apply(c1, c2, c3);
  }

  //endregion
}
