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

import com.plugatar.xteps2.core.XtepsException;
import com.plugatar.xteps2.core.function.ThBiConsumer;
import com.plugatar.xteps2.core.function.ThBiFunction;
import com.plugatar.xteps2.core.function.ThConsumer;
import com.plugatar.xteps2.core.function.ThFunction;
import com.plugatar.xteps2.core.function.ThRunnable;
import com.plugatar.xteps2.core.function.ThSupplier;
import com.plugatar.xteps2.core.function.ThTriConsumer;
import com.plugatar.xteps2.core.function.ThTriFunction;

/**
 * Utility class. Contains methods for lambda functions converting.
 * <p>
 * Methods:
 * <ul>
 * <li>{@link #runnable(Object, ThConsumer)}</li>
 * <li>{@link #runnable(Object, Object, ThBiConsumer)}</li>
 * <li>{@link #runnable(Object, Object, Object, ThTriConsumer)}</li>
 * <li>{@link #supplier(Object, ThFunction)}</li>
 * <li>{@link #supplier(Object, Object, ThBiFunction)}</li>
 * <li>{@link #supplier(Object, Object, Object, ThTriFunction)}</li>
 * </ul>
 */
public final class Functions {

  /**
   * Utility class ctor.
   */
  private Functions() {
  }

  /**
   * Returns {@code ThRunnable} that wraps given consumer.
   *
   * @param t        the input argument
   * @param consumer the consumer
   * @param <T>      the type of the input argument
   * @return {@code ThRunnable} that wraps given consumer
   * @throws XtepsException if {@code consumer} arg is null
   */
  public static <T> ThRunnable<RuntimeException> runnable(final T t,
                                                          final ThConsumer<? super T, ?> consumer) {
    if (consumer == null) { throw new XtepsException("consumer arg is null"); }
    return ThRunnable.unchecked(() -> consumer.accept(t));
  }

  /**
   * Returns {@code ThRunnable} that wraps given consumer.
   *
   * @param t        the first input argument
   * @param u        the second input argument
   * @param consumer the consumer
   * @param <T>      the type of the first input argument
   * @param <U>      the type of the second input argument
   * @return {@code ThRunnable} that wraps given consumer
   * @throws XtepsException if {@code consumer} arg is null
   */
  public static <T, U> ThRunnable<RuntimeException> runnable(final T t,
                                                             final U u,
                                                             final ThBiConsumer<? super T, ? super U, ?> consumer) {
    if (consumer == null) { throw new XtepsException("consumer arg is null"); }
    return ThRunnable.unchecked(() -> consumer.accept(t, u));
  }

  /**
   * Returns {@code ThRunnable} that wraps given consumer.
   *
   * @param t        the first input argument
   * @param u        the second input argument
   * @param v        the third input argument
   * @param consumer the consumer
   * @param <T>      the type of the first input argument
   * @param <U>      the type of the second input argument
   * @param <V>      the type of the third input argument
   * @return {@code ThRunnable} that wraps given consumer
   * @throws XtepsException if {@code consumer} arg is null
   */
  public static <T, U, V> ThRunnable<RuntimeException> runnable(final T t,
                                                                final U u,
                                                                final V v,
                                                                final ThTriConsumer<? super T, ? super U, ? super V, ?> consumer) {
    if (consumer == null) { throw new XtepsException("consumer arg is null"); }
    return ThRunnable.unchecked(() -> consumer.accept(t, u, v));
  }

  /**
   * Returns {@code ThSupplier} that wraps given function.
   *
   * @param t        the first input argument
   * @param function the function
   * @param <T>      the type of the input argument
   * @param <R>      the type of the result
   * @return {@code ThSupplier} that wraps given function
   * @throws XtepsException if {@code function} arg is null
   */
  public static <T, R> ThSupplier<R, RuntimeException> supplier(final T t,
                                                                final ThFunction<? super T, ? extends R, ?> function) {
    if (function == null) { throw new XtepsException("function arg is null"); }
    return ThSupplier.unchecked(() -> function.apply(t));
  }

  /**
   * Returns {@code ThSupplier} that wraps given function.
   *
   * @param t        the first input argument
   * @param u        the second input argument
   * @param function the function
   * @param <T>      the type of the first input argument
   * @param <U>      the type of the second input argument
   * @param <R>      the type of the result
   * @return {@code ThSupplier} that wraps given function
   * @throws XtepsException if {@code function} arg is null
   */
  public static <T, U, R> ThSupplier<R, RuntimeException> supplier(final T t,
                                                                   final U u,
                                                                   final ThBiFunction<? super T, ? super U, ? extends R, ?> function) {
    if (function == null) { throw new XtepsException("function arg is null"); }
    return ThSupplier.unchecked(() -> function.apply(t, u));
  }

  /**
   * Returns {@code ThSupplier} that wraps given function.
   *
   * @param t        the first input argument
   * @param u        the second input argument
   * @param v        the third input argument
   * @param function the function
   * @param <T>      the type of the first input argument
   * @param <U>      the type of the second input argument
   * @param <V>      the type of the third input argument
   * @param <R>      the type of the result
   * @return {@code ThSupplier} that wraps given function
   * @throws XtepsException if {@code function} arg is null
   */
  public static <T, U, V, R> ThSupplier<R, RuntimeException> supplier(final T t,
                                                                      final U u,
                                                                      final V v,
                                                                      final ThTriFunction<? super T, ? super U, ? super V, ? extends R, ?> function) {
    if (function == null) { throw new XtepsException("function arg is null"); }
    return ThSupplier.unchecked(() -> function.apply(t, u, v));
  }
}
