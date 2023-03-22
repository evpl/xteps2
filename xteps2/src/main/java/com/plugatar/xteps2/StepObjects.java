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
import com.plugatar.xteps2.core.function.ThBiConsumer;
import com.plugatar.xteps2.core.function.ThBiFunction;
import com.plugatar.xteps2.core.function.ThConsumer;
import com.plugatar.xteps2.core.function.ThFunction;
import com.plugatar.xteps2.core.function.ThRunnable;
import com.plugatar.xteps2.core.function.ThSupplier;
import com.plugatar.xteps2.core.function.ThTriConsumer;
import com.plugatar.xteps2.core.function.ThTriFunction;
import com.plugatar.xteps2.core.step.BiConsumerStep;
import com.plugatar.xteps2.core.step.BiFunctionStep;
import com.plugatar.xteps2.core.step.ConsumerStep;
import com.plugatar.xteps2.core.step.FunctionStep;
import com.plugatar.xteps2.core.step.RunnableStep;
import com.plugatar.xteps2.core.step.SupplierStep;
import com.plugatar.xteps2.core.step.TriConsumerStep;
import com.plugatar.xteps2.core.step.TriFunctionStep;

import java.util.Map;

public final class StepObjects {

  private StepObjects() {
  }

  public static RunnableStep runnableStep(final ThRunnable<?> action) {
    return new RunnableStep.Of(action);
  }

  public static RunnableStep runnableStep(final Keyword keyword,
                                          final ThRunnable<?> action) {
    return new RunnableStep.Of(keyword, action);
  }

  public static RunnableStep runnableStep(final String name,
                                          final ThRunnable<?> action) {
    return new RunnableStep.Of(name, action);
  }

  public static RunnableStep runnableStep(final Keyword keyword,
                                          final String name,
                                          final ThRunnable<?> action) {
    return new RunnableStep.Of(keyword, name, action);
  }

  public static RunnableStep runnableStep(final String name,
                                          final String desc,
                                          final ThRunnable<?> action) {
    return new RunnableStep.Of(name, desc, action);
  }

  public static RunnableStep runnableStep(final Keyword keyword,
                                          final String name,
                                          final String desc,
                                          final ThRunnable<?> action) {
    return new RunnableStep.Of(keyword, name, desc, action);
  }

  public static RunnableStep runnableStep(final String name,
                                          final Map<String, ?> params,
                                          final ThRunnable<?> action) {
    return new RunnableStep.Of(name, params, action);
  }

  public static RunnableStep runnableStep(final Keyword keyword,
                                          final String name,
                                          final Map<String, ?> params,
                                          final ThRunnable<?> action) {
    return new RunnableStep.Of(keyword, name, params, action);
  }

  public static RunnableStep runnableStep(final String name,
                                          final Map<String, ?> params,
                                          final String desc,
                                          final ThRunnable<?> action) {
    return new RunnableStep.Of(name, params, desc, action);
  }

  public static RunnableStep runnableStep(final Keyword keyword,
                                          final String name,
                                          final Map<String, ?> params,
                                          final String desc,
                                          final ThRunnable<?> action) {
    return new RunnableStep.Of(keyword, name, params, desc, action);
  }

  public static RunnableStep runnableStep(final Map<String, ?> artifacts,
                                          final ThRunnable<?> action) {
    return new RunnableStep.Of(artifacts, action);
  }

  public static <R> SupplierStep<R> supplierStep(final ThSupplier<? extends R, ?> action) {
    return new SupplierStep.Of<>(action);
  }

  public static <R> SupplierStep<R> supplierStep(final Keyword keyword,
                                                 final ThSupplier<? extends R, ?> action) {
    return new SupplierStep.Of<>(keyword, action);
  }

  public static <R> SupplierStep<R> supplierStep(final String name,
                                                 final ThSupplier<? extends R, ?> action) {
    return new SupplierStep.Of<>(name, action);
  }

  public static <R> SupplierStep<R> supplierStep(final Keyword keyword,
                                                 final String name,
                                                 final ThSupplier<? extends R, ?> action) {
    return new SupplierStep.Of<>(keyword, name, action);
  }

  public static <R> SupplierStep<R> supplierStep(final String name,
                                                 final String desc,
                                                 final ThSupplier<? extends R, ?> action) {
    return new SupplierStep.Of<>(name, desc, action);
  }

  public static <R> SupplierStep<R> supplierStep(final Keyword keyword,
                                                 final String name,
                                                 final String desc,
                                                 final ThSupplier<? extends R, ?> action) {
    return new SupplierStep.Of<>(keyword, name, desc, action);
  }

  public static <R> SupplierStep<R> supplierStep(final String name,
                                                 final Map<String, ?> params,
                                                 final ThSupplier<? extends R, ?> action) {
    return new SupplierStep.Of<>(name, params, action);
  }

  public static <R> SupplierStep<R> supplierStep(final Keyword keyword,
                                                 final String name,
                                                 final Map<String, ?> params,
                                                 final ThSupplier<? extends R, ?> action) {
    return new SupplierStep.Of<>(keyword, name, params, action);
  }

  public static <R> SupplierStep<R> supplierStep(final String name,
                                                 final Map<String, ?> params,
                                                 final String desc,
                                                 final ThSupplier<? extends R, ?> action) {
    return new SupplierStep.Of<>(name, params, desc, action);
  }

  public static <R> SupplierStep<R> supplierStep(final Keyword keyword,
                                                 final String name,
                                                 final Map<String, ?> params,
                                                 final String desc,
                                                 final ThSupplier<? extends R, ?> action) {
    return new SupplierStep.Of<>(keyword, name, params, desc, action);
  }

  public static <R> SupplierStep<R> supplierStep(final Map<String, ?> artifacts,
                                                 final ThSupplier<? extends R, ?> action) {
    return new SupplierStep.Of<>(artifacts, action);
  }

  public static <T> ConsumerStep<T> consumerStep(final ThConsumer<? super T, ?> action) {
    return new ConsumerStep.Of<>(action);
  }

  public static <T> ConsumerStep<T> consumerStep(final Keyword keyword,
                                                 final ThConsumer<? super T, ?> action) {
    return new ConsumerStep.Of<>(keyword, action);
  }

  public static <T> ConsumerStep<T> consumerStep(final String name,
                                                 final ThConsumer<? super T, ?> action) {
    return new ConsumerStep.Of<>(name, action);
  }

  public static <T> ConsumerStep<T> consumerStep(final Keyword keyword,
                                                 final String name,
                                                 final ThConsumer<? super T, ?> action) {
    return new ConsumerStep.Of<>(keyword, name, action);
  }

  public static <T> ConsumerStep<T> consumerStep(final String name,
                                                 final String desc,
                                                 final ThConsumer<? super T, ?> action) {
    return new ConsumerStep.Of<>(name, desc, action);
  }

  public static <T> ConsumerStep<T> consumerStep(final Keyword keyword,
                                                 final String name,
                                                 final String desc,
                                                 final ThConsumer<? super T, ?> action) {
    return new ConsumerStep.Of<>(keyword, name, desc, action);
  }

  public static <T> ConsumerStep<T> consumerStep(final String name,
                                                 final Map<String, ?> params,
                                                 final ThConsumer<? super T, ?> action) {
    return new ConsumerStep.Of<>(name, params, action);
  }

  public static <T> ConsumerStep<T> consumerStep(final Keyword keyword,
                                                 final String name,
                                                 final Map<String, ?> params,
                                                 final ThConsumer<? super T, ?> action) {
    return new ConsumerStep.Of<>(keyword, name, params, action);
  }

  public static <T> ConsumerStep<T> consumerStep(final String name,
                                                 final Map<String, ?> params,
                                                 final String desc,
                                                 final ThConsumer<? super T, ?> action) {
    return new ConsumerStep.Of<>(name, params, desc, action);
  }

  public static <T> ConsumerStep<T> consumerStep(final Keyword keyword,
                                                 final String name,
                                                 final Map<String, ?> params,
                                                 final String desc,
                                                 final ThConsumer<? super T, ?> action) {
    return new ConsumerStep.Of<>(keyword, name, params, desc, action);
  }

  public static <T> ConsumerStep<T> consumerStep(final Map<String, ?> artifacts,
                                                 final ThConsumer<? super T, ?> action) {
    return new ConsumerStep.Of<>(artifacts, action);
  }

  public static <T, U> BiConsumerStep<T, U> biConsumerStep(final ThBiConsumer<? super T, ? super U, ?> action) {
    return new BiConsumerStep.Of<>(action);
  }

  public static <T, U> BiConsumerStep<T, U> biConsumerStep(final Keyword keyword,
                                                           final ThBiConsumer<? super T, ? super U, ?> action) {
    return new BiConsumerStep.Of<>(keyword, action);
  }

  public static <T, U> BiConsumerStep<T, U> biConsumerStep(final String name,
                                                           final ThBiConsumer<? super T, ? super U, ?> action) {
    return new BiConsumerStep.Of<>(name, action);
  }

  public static <T, U> BiConsumerStep<T, U> biConsumerStep(final Keyword keyword,
                                                           final String name,
                                                           final ThBiConsumer<? super T, ? super U, ?> action) {
    return new BiConsumerStep.Of<>(keyword, name, action);
  }

  public static <T, U> BiConsumerStep<T, U> biConsumerStep(final String name,
                                                           final String desc,
                                                           final ThBiConsumer<? super T, ? super U, ?> action) {
    return new BiConsumerStep.Of<>(name, desc, action);
  }

  public static <T, U> BiConsumerStep<T, U> biConsumerStep(final Keyword keyword,
                                                           final String name,
                                                           final String desc,
                                                           final ThBiConsumer<? super T, ? super U, ?> action) {
    return new BiConsumerStep.Of<>(keyword, name, desc, action);
  }

  public static <T, U> BiConsumerStep<T, U> biConsumerStep(final String name,
                                                           final Map<String, ?> params,
                                                           final ThBiConsumer<? super T, ? super U, ?> action) {
    return new BiConsumerStep.Of<>(name, params, action);
  }

  public static <T, U> BiConsumerStep<T, U> biConsumerStep(final Keyword keyword,
                                                           final String name,
                                                           final Map<String, ?> params,
                                                           final ThBiConsumer<? super T, ? super U, ?> action) {
    return new BiConsumerStep.Of<>(keyword, name, params, action);
  }

  public static <T, U> BiConsumerStep<T, U> biConsumerStep(final String name,
                                                           final Map<String, ?> params,
                                                           final String desc,
                                                           final ThBiConsumer<? super T, ? super U, ?> action) {
    return new BiConsumerStep.Of<>(name, params, desc, action);
  }

  public static <T, U> BiConsumerStep<T, U> biConsumerStep(final Keyword keyword,
                                                           final String name,
                                                           final Map<String, ?> params,
                                                           final String desc,
                                                           final ThBiConsumer<? super T, ? super U, ?> action) {
    return new BiConsumerStep.Of<>(keyword, name, params, desc, action);
  }

  public static <T, U> BiConsumerStep<T, U> biConsumerStep(final Map<String, ?> artifacts,
                                                           final ThBiConsumer<? super T, ? super U, ?> action) {
    return new BiConsumerStep.Of<>(artifacts, action);
  }

  public static <T, U, V> TriConsumerStep<T, U, V> triConsumerStep(final ThTriConsumer<? super T, ? super U, ? super V, ?> action) {
    return new TriConsumerStep.Of<>(action);
  }

  public static <T, U, V> TriConsumerStep<T, U, V> triConsumerStep(final Keyword keyword,
                                                                   final ThTriConsumer<? super T, ? super U, ? super V, ?> action) {
    return new TriConsumerStep.Of<>(keyword, action);
  }

  public static <T, U, V> TriConsumerStep<T, U, V> triConsumerStep(final String name,
                                                                   final ThTriConsumer<? super T, ? super U, ? super V, ?> action) {
    return new TriConsumerStep.Of<>(name, action);
  }

  public static <T, U, V> TriConsumerStep<T, U, V> triConsumerStep(final Keyword keyword,
                                                                   final String name,
                                                                   final ThTriConsumer<? super T, ? super U, ? super V, ?> action) {
    return new TriConsumerStep.Of<>(keyword, name, action);
  }

  public static <T, U, V> TriConsumerStep<T, U, V> triConsumerStep(final String name,
                                                                   final String desc,
                                                                   final ThTriConsumer<? super T, ? super U, ? super V, ?> action) {
    return new TriConsumerStep.Of<>(name, desc, action);
  }

  public static <T, U, V> TriConsumerStep<T, U, V> triConsumerStep(final Keyword keyword,
                                                                   final String name,
                                                                   final String desc,
                                                                   final ThTriConsumer<? super T, ? super U, ? super V, ?> action) {
    return new TriConsumerStep.Of<>(keyword, name, desc, action);
  }

  public static <T, U, V> TriConsumerStep<T, U, V> triConsumerStep(final String name,
                                                                   final Map<String, ?> params,
                                                                   final ThTriConsumer<? super T, ? super U, ? super V, ?> action) {
    return new TriConsumerStep.Of<>(name, params, action);
  }

  public static <T, U, V> TriConsumerStep<T, U, V> triConsumerStep(final Keyword keyword,
                                                                   final String name,
                                                                   final Map<String, ?> params,
                                                                   final ThTriConsumer<? super T, ? super U, ? super V, ?> action) {
    return new TriConsumerStep.Of<>(keyword, name, params, action);
  }

  public static <T, U, V> TriConsumerStep<T, U, V> triConsumerStep(final String name,
                                                                   final Map<String, ?> params,
                                                                   final String desc,
                                                                   final ThTriConsumer<? super T, ? super U, ? super V, ?> action) {
    return new TriConsumerStep.Of<>(name, params, desc, action);
  }

  public static <T, U, V> TriConsumerStep<T, U, V> triConsumerStep(final Keyword keyword,
                                                                   final String name,
                                                                   final Map<String, ?> params,
                                                                   final String desc,
                                                                   final ThTriConsumer<? super T, ? super U, ? super V, ?> action) {
    return new TriConsumerStep.Of<>(keyword, name, params, desc, action);
  }

  public static <T, U, V> TriConsumerStep<T, U, V> triConsumerStep(final Map<String, ?> artifacts,
                                                                   final ThTriConsumer<? super T, ? super U, ? super V, ?> action) {
    return new TriConsumerStep.Of<>(artifacts, action);
  }

  public static <T, R> FunctionStep<T, R> functionStep(final ThFunction<? super T, ? extends R, ?> action) {
    return new FunctionStep.Of<>(action);
  }

  public static <T, R> FunctionStep<T, R> functionStep(final Keyword keyword,
                                                       final ThFunction<? super T, ? extends R, ?> action) {
    return new FunctionStep.Of<>(keyword, action);
  }

  public static <T, R> FunctionStep<T, R> functionStep(final String name,
                                                       final ThFunction<? super T, ? extends R, ?> action) {
    return new FunctionStep.Of<>(name, action);
  }

  public static <T, R> FunctionStep<T, R> functionStep(final Keyword keyword,
                                                       final String name,
                                                       final ThFunction<? super T, ? extends R, ?> action) {
    return new FunctionStep.Of<>(keyword, name, action);
  }

  public static <T, R> FunctionStep<T, R> functionStep(final String name,
                                                       final String desc,
                                                       final ThFunction<? super T, ? extends R, ?> action) {
    return new FunctionStep.Of<>(name, desc, action);
  }

  public static <T, R> FunctionStep<T, R> functionStep(final Keyword keyword,
                                                       final String name,
                                                       final String desc,
                                                       final ThFunction<? super T, ? extends R, ?> action) {
    return new FunctionStep.Of<>(keyword, name, desc, action);
  }

  public static <T, R> FunctionStep<T, R> functionStep(final String name,
                                                       final Map<String, ?> params,
                                                       final ThFunction<? super T, ? extends R, ?> action) {
    return new FunctionStep.Of<>(name, params, action);
  }

  public static <T, R> FunctionStep<T, R> functionStep(final Keyword keyword,
                                                       final String name,
                                                       final Map<String, ?> params,
                                                       final ThFunction<? super T, ? extends R, ?> action) {
    return new FunctionStep.Of<>(keyword, name, params, action);
  }

  public static <T, R> FunctionStep<T, R> functionStep(final String name,
                                                       final Map<String, ?> params,
                                                       final String desc,
                                                       final ThFunction<? super T, ? extends R, ?> action) {
    return new FunctionStep.Of<>(name, params, desc, action);
  }

  public static <T, R> FunctionStep<T, R> functionStep(final Keyword keyword,
                                                       final String name,
                                                       final Map<String, ?> params,
                                                       final String desc,
                                                       final ThFunction<? super T, ? extends R, ?> action) {
    return new FunctionStep.Of<>(keyword, name, params, desc, action);
  }

  public static <T, R> FunctionStep<T, R> functionStep(final Map<String, ?> artifacts,
                                                       final ThFunction<? super T, ? extends R, ?> action) {
    return new FunctionStep.Of<>(artifacts, action);
  }

  public static <T, U, R> BiFunctionStep<T, U, R> biFunctionStep(final ThBiFunction<? super T, ? super U, ? extends R, ?> action) {
    return new BiFunctionStep.Of<>(action);
  }

  public static <T, U, R> BiFunctionStep<T, U, R> biFunctionStep(final Keyword keyword,
                                                                 final ThBiFunction<? super T, ? super U, ? extends R, ?> action) {
    return new BiFunctionStep.Of<>(keyword, action);
  }

  public static <T, U, R> BiFunctionStep<T, U, R> biFunctionStep(final String name,
                                                                 final ThBiFunction<? super T, ? super U, ? extends R, ?> action) {
    return new BiFunctionStep.Of<>(name, action);
  }

  public static <T, U, R> BiFunctionStep<T, U, R> biFunctionStep(final Keyword keyword,
                                                                 final String name,
                                                                 final ThBiFunction<? super T, ? super U, ? extends R, ?> action) {
    return new BiFunctionStep.Of<>(keyword, name, action);
  }

  public static <T, U, R> BiFunctionStep<T, U, R> biFunctionStep(final String name,
                                                                 final String desc,
                                                                 final ThBiFunction<? super T, ? super U, ? extends R, ?> action) {
    return new BiFunctionStep.Of<>(name, desc, action);
  }

  public static <T, U, R> BiFunctionStep<T, U, R> biFunctionStep(final Keyword keyword,
                                                                 final String name,
                                                                 final String desc,
                                                                 final ThBiFunction<? super T, ? super U, ? extends R, ?> action) {
    return new BiFunctionStep.Of<>(keyword, name, desc, action);
  }

  public static <T, U, R> BiFunctionStep<T, U, R> biFunctionStep(final String name,
                                                                 final Map<String, ?> params,
                                                                 final ThBiFunction<? super T, ? super U, ? extends R, ?> action) {
    return new BiFunctionStep.Of<>(name, params, action);
  }

  public static <T, U, R> BiFunctionStep<T, U, R> biFunctionStep(final Keyword keyword,
                                                                 final String name,
                                                                 final Map<String, ?> params,
                                                                 final ThBiFunction<? super T, ? super U, ? extends R, ?> action) {
    return new BiFunctionStep.Of<>(keyword, name, params, action);
  }

  public static <T, U, R> BiFunctionStep<T, U, R> biFunctionStep(final String name,
                                                                 final Map<String, ?> params,
                                                                 final String desc,
                                                                 final ThBiFunction<? super T, ? super U, ? extends R, ?> action) {
    return new BiFunctionStep.Of<>(name, params, desc, action);
  }

  public static <T, U, R> BiFunctionStep<T, U, R> biFunctionStep(final Keyword keyword,
                                                                 final String name,
                                                                 final Map<String, ?> params,
                                                                 final String desc,
                                                                 final ThBiFunction<? super T, ? super U, ? extends R, ?> action) {
    return new BiFunctionStep.Of<>(keyword, name, params, desc, action);
  }

  public static <T, U, R> BiFunctionStep<T, U, R> biFunctionStep(final Map<String, ?> artifacts,
                                                                 final ThBiFunction<? super T, ? super U, ? extends R, ?> action) {
    return new BiFunctionStep.Of<>(artifacts, action);
  }

  public static <T, U, V, R> TriFunctionStep<T, U, V, R> triFunctionStep(final ThTriFunction<? super T, ? super U, ? super V, ? extends R, ?> action) {
    return new TriFunctionStep.Of<>(action);
  }

  public static <T, U, V, R> TriFunctionStep<T, U, V, R> triFunctionStep(final Keyword keyword,
                                                                         final ThTriFunction<? super T, ? super U, ? super V, ? extends R, ?> action) {
    return new TriFunctionStep.Of<>(keyword, action);
  }

  public static <T, U, V, R> TriFunctionStep<T, U, V, R> triFunctionStep(final String name,
                                                                         final ThTriFunction<? super T, ? super U, ? super V, ? extends R, ?> action) {
    return new TriFunctionStep.Of<>(name, action);
  }

  public static <T, U, V, R> TriFunctionStep<T, U, V, R> triFunctionStep(final Keyword keyword,
                                                                         final String name,
                                                                         final ThTriFunction<? super T, ? super U, ? super V, ? extends R, ?> action) {
    return new TriFunctionStep.Of<>(keyword, name, action);
  }

  public static <T, U, V, R> TriFunctionStep<T, U, V, R> triFunctionStep(final String name,
                                                                         final String desc,
                                                                         final ThTriFunction<? super T, ? super U, ? super V, ? extends R, ?> action) {
    return new TriFunctionStep.Of<>(name, desc, action);
  }

  public static <T, U, V, R> TriFunctionStep<T, U, V, R> triFunctionStep(final Keyword keyword,
                                                                         final String name,
                                                                         final String desc,
                                                                         final ThTriFunction<? super T, ? super U, ? super V, ? extends R, ?> action) {
    return new TriFunctionStep.Of<>(keyword, name, desc, action);
  }

  public static <T, U, V, R> TriFunctionStep<T, U, V, R> triFunctionStep(final String name,
                                                                         final Map<String, ?> params,
                                                                         final ThTriFunction<? super T, ? super U, ? super V, ? extends R, ?> action) {
    return new TriFunctionStep.Of<>(name, params, action);
  }

  public static <T, U, V, R> TriFunctionStep<T, U, V, R> triFunctionStep(final Keyword keyword,
                                                                         final String name,
                                                                         final Map<String, ?> params,
                                                                         final ThTriFunction<? super T, ? super U, ? super V, ? extends R, ?> action) {
    return new TriFunctionStep.Of<>(keyword, name, params, action);
  }

  public static <T, U, V, R> TriFunctionStep<T, U, V, R> triFunctionStep(final String name,
                                                                         final Map<String, ?> params,
                                                                         final String desc,
                                                                         final ThTriFunction<? super T, ? super U, ? super V, ? extends R, ?> action) {
    return new TriFunctionStep.Of<>(name, params, desc, action);
  }

  public static <T, U, V, R> TriFunctionStep<T, U, V, R> triFunctionStep(final Keyword keyword,
                                                                         final String name,
                                                                         final Map<String, ?> params,
                                                                         final String desc,
                                                                         final ThTriFunction<? super T, ? super U, ? super V, ? extends R, ?> action) {
    return new TriFunctionStep.Of<>(keyword, name, params, desc, action);
  }

  public static <T, U, V, R> TriFunctionStep<T, U, V, R> triFunctionStep(final Map<String, ?> artifacts,
                                                                         final ThTriFunction<? super T, ? super U, ? super V, ? extends R, ?> action) {
    return new TriFunctionStep.Of<>(artifacts, action);
  }

//  public static <S extends StepChain<?>> ConsumerStep<S> chain() {
//
//  }
}
