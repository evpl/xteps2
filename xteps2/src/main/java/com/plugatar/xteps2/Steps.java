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

public final class Steps {

  private Steps() {
  }

  public static void step(final ThRunnable<?> action) {
    new RunnableStep.Of(action).run();
  }

  public static void step(final Keyword keyword,
                          final ThRunnable<?> action) {
    new RunnableStep.Of(keyword, action).run();
  }

  public static void step(final String name,
                          final ThRunnable<?> action) {
    new RunnableStep.Of(name, action).run();
  }

  public static void step(final Keyword keyword,
                          final String name,
                          final ThRunnable<?> action) {
    new RunnableStep.Of(keyword, name, action).run();
  }

  public static void step(final String name,
                          final String desc,
                          final ThRunnable<?> action) {
    new RunnableStep.Of(name, desc, action).run();
  }

  public static void step(final Keyword keyword,
                          final String name,
                          final String desc,
                          final ThRunnable<?> action) {
    new RunnableStep.Of(keyword, name, desc, action).run();
  }

  public static void step(final String name,
                          final Map<String, ?> params,
                          final ThRunnable<?> action) {
    new RunnableStep.Of(name, params, action).run();
  }

  public static void step(final Keyword keyword,
                          final String name,
                          final Map<String, ?> params,
                          final ThRunnable<?> action) {
    new RunnableStep.Of(keyword, name, params, action).run();
  }

  public static void step(final String name,
                          final Map<String, ?> params,
                          final String desc,
                          final ThRunnable<?> action) {
    new RunnableStep.Of(name, params, desc, action).run();
  }

  public static void step(final Keyword keyword,
                          final String name,
                          final Map<String, ?> params,
                          final String desc,
                          final ThRunnable<?> action) {
    new RunnableStep.Of(keyword, name, params, desc, action).run();
  }

  public static void step(final Map<String, ?> artifacts,
                          final ThRunnable<?> action) {
    new RunnableStep.Of(artifacts, action).run();
  }

  public static <R> R step(final ThSupplier<? extends R, ?> action) {
    return new SupplierStep.Of<>(action).get();
  }

  public static <R> R step(final Keyword keyword,
                           final ThSupplier<? extends R, ?> action) {
    return new SupplierStep.Of<>(keyword, action).get();
  }

  public static <R> R step(final String name,
                           final ThSupplier<? extends R, ?> action) {
    return new SupplierStep.Of<>(name, action).get();
  }

  public static <R> R step(final Keyword keyword,
                           final String name,
                           final ThSupplier<? extends R, ?> action) {
    return new SupplierStep.Of<>(keyword, name, action).get();
  }

  public static <R> R step(final String name,
                           final String desc,
                           final ThSupplier<? extends R, ?> action) {
    return new SupplierStep.Of<>(name, desc, action).get();
  }

  public static <R> R step(final Keyword keyword,
                           final String name,
                           final String desc,
                           final ThSupplier<? extends R, ?> action) {
    return new SupplierStep.Of<>(keyword, name, desc, action).get();
  }

  public static <R> R step(final String name,
                           final Map<String, ?> params,
                           final ThSupplier<? extends R, ?> action) {
    return new SupplierStep.Of<>(name, params, action).get();
  }

  public static <R> R step(final Keyword keyword,
                           final String name,
                           final Map<String, ?> params,
                           final ThSupplier<? extends R, ?> action) {
    return new SupplierStep.Of<>(keyword, name, params, action).get();
  }

  public static <R> R step(final String name,
                           final Map<String, ?> params,
                           final String desc,
                           final ThSupplier<? extends R, ?> action) {
    return new SupplierStep.Of<>(name, params, desc, action).get();
  }

  public static <R> R step(final Keyword keyword,
                           final String name,
                           final Map<String, ?> params,
                           final String desc,
                           final ThSupplier<? extends R, ?> action) {
    return new SupplierStep.Of<>(keyword, name, params, desc, action).get();
  }

  public static <R> R step(final Map<String, ?> artifacts,
                           final ThSupplier<? extends R, ?> action) {
    return new SupplierStep.Of<>(artifacts, action).get();
  }

  public static <R> R step(final Map<String, ?> artifacts) {
    return new SupplierStep.Of<R>(artifacts).get();
  }

  public static void step(final RunnableStep step) {
    if (step == null) { throw new XtepsException("step arg is null"); }
    step.run();
  }

  public static <R> R step(final SupplierStep<? extends R> step) {
    if (step == null) { throw new XtepsException("step arg is null"); }
    return step.get();
  }

  public static <T> void step(final ConsumerStep<? super T> step,
                              final T t) {
    if (step == null) { throw new XtepsException("step arg is null"); }
    step.accept(t);
  }

  public static <T, U> void step(final BiConsumerStep<? super T, ? super U> step,
                                 final T t,
                                 final U u) {
    if (step == null) { throw new XtepsException("step arg is null"); }
    step.accept(t, u);
  }

  public static <T, U, V> void step(final TriConsumerStep<? super T, ? super U, ? super V> step,
                                    final T t,
                                    final U u,
                                    final V v) {
    if (step == null) { throw new XtepsException("step arg is null"); }
    step.accept(t, u, v);
  }

  public static <T, R> R step(final FunctionStep<? super T, ? extends R> step,
                              final T t) {
    if (step == null) { throw new XtepsException("step arg is null"); }
    return step.apply(t);
  }

  public static <T, U, R> R step(final BiFunctionStep<? super T, ? super U, ? extends R> step,
                                 final T t,
                                 final U u) {
    if (step == null) { throw new XtepsException("step arg is null"); }
    return step.apply(t, u);
  }

  public static <T, U, V, R> R step(final TriFunctionStep<? super T, ? super U, ? super V, ? extends R> step,
                                    final T t,
                                    final U u,
                                    final V v) {
    if (step == null) { throw new XtepsException("step arg is null"); }
    return step.apply(t, u, v);
  }

  public static <R> R step() {
    return new SupplierStep.Of<R>().get();
  }

  public static <R> R step(final Keyword keyword) {
    return new SupplierStep.Of<R>(keyword).get();
  }

  public static <R> R step(final String name) {
    return new SupplierStep.Of<R>(name).get();
  }

  public static <R> R step(final Keyword keyword,
                           final String name) {
    return new SupplierStep.Of<R>(keyword, name).get();
  }

  public static <R> R step(final String name,
                           final String desc) {
    return new SupplierStep.Of<R>(name, desc).get();
  }

  public static <R> R step(final Keyword keyword,
                           final String name,
                           final String desc) {
    return new SupplierStep.Of<R>(keyword, name, desc).get();
  }

  public static <R> R step(final String name,
                           final Map<String, ?> params) {
    return new SupplierStep.Of<R>(name, params).get();
  }

  public static <R> R step(final Keyword keyword,
                           final String name,
                           final Map<String, ?> params) {
    return new SupplierStep.Of<R>(keyword, name, params).get();
  }

  public static <R> R step(final String name,
                           final Map<String, ?> params,
                           final String desc) {
    return new SupplierStep.Of<R>(name, params, desc).get();
  }

  public static <R> R step(final Keyword keyword,
                           final String name,
                           final Map<String, ?> params,
                           final String desc) {
    return new SupplierStep.Of<R>(keyword, name, params, desc).get();
  }
}
