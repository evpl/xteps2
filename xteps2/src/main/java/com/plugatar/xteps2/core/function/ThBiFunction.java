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
package com.plugatar.xteps2.core.function;

/**
 * The {@link java.util.function.BiFunction} specialization that might throw an exception.
 *
 * @param <T> the type of the first input argument
 * @param <U> the type of the second input argument
 * @param <R> the type of the result
 * @param <E> the type of the throwing exception
 * @see java.util.function.BiFunction
 */
@FunctionalInterface
public interface ThBiFunction<T, U, R, E extends Throwable> {

  /**
   * Applies this function to the given arguments.
   *
   * @param t the first input argument
   * @param u the second input argument
   * @return result
   * @throws E if function threw exception
   */
  R apply(T t, U u) throws E;

  /**
   * Returns given {@code ThBiFunction} as unchecked or null if {@code function} arg is null.
   *
   * @param function the function
   * @param <T>      the type of the {@code function} first input argument
   * @param <U>      the type of the {@code function} second input argument
   * @param <R>      the type of the {@code function} result
   * @return unchecked {@code ThBiFunction} or null
   */
  @SuppressWarnings("unchecked")
  static <T, U, R> ThBiFunction<T, U, R, RuntimeException> unchecked(final ThBiFunction<? super T, ? super U, ? extends R, ?> function) {
    return (ThBiFunction<T, U, R, RuntimeException>) function;
  }
}
