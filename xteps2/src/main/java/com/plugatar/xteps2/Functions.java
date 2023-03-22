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

import com.plugatar.xteps2.core.function.ThBiConsumer;
import com.plugatar.xteps2.core.function.ThBiFunction;
import com.plugatar.xteps2.core.function.ThConsumer;
import com.plugatar.xteps2.core.function.ThFunction;
import com.plugatar.xteps2.core.function.ThRunnable;
import com.plugatar.xteps2.core.function.ThSupplier;
import com.plugatar.xteps2.core.function.ThTriConsumer;
import com.plugatar.xteps2.core.function.ThTriFunction;

public final class Functions {

  private Functions() {
  }

  public static <T> ThRunnable<RuntimeException> runnable(final T context,
                                                          final ThConsumer<? super T, ?> consumer) {
    return ThRunnable.unchecked(() -> consumer.accept(context));
  }

  public static <T, U> ThRunnable<RuntimeException> runnable(final T context,
                                                             final U context2,
                                                             final ThBiConsumer<? super T, ? super U, ?> consumer) {
    return ThRunnable.unchecked(() -> consumer.accept(context, context2));
  }

  public static <T, U, V> ThRunnable<RuntimeException> runnable(final T context,
                                                                final U context2,
                                                                final V context3,
                                                                final ThTriConsumer<? super T, ? super U, ? super V, ?> consumer) {
    return ThRunnable.unchecked(() -> consumer.accept(context, context2, context3));
  }

  public static <T, R> ThSupplier<R, RuntimeException> supplier(final T context,
                                                                final ThFunction<? super T, ? extends R, ?> function) {
    return ThSupplier.unchecked(() -> function.apply(context));
  }

  public static <T, U, R> ThSupplier<R, RuntimeException> supplier(final T context,
                                                                   final U context2,
                                                                   final ThBiFunction<? super T, ? super U, ? extends R, ?> function) {
    return ThSupplier.unchecked(() -> function.apply(context, context2));
  }

  public static <T, U, V, R> ThSupplier<R, RuntimeException> supplier(final T context,
                                                                      final U context2,
                                                                      final V context3,
                                                                      final ThTriFunction<? super T, ? super U, ? super V, ? extends R, ?> function) {
    return ThSupplier.unchecked(() -> function.apply(context, context2, context3));
  }
}
