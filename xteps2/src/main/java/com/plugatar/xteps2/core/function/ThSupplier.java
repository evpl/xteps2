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
 * The {@link java.util.function.Supplier} specialization that might throw an exception.
 *
 * @param <R> the type of the result
 * @param <E> the type of the throwing exception
 * @see java.util.function.Supplier
 */
@FunctionalInterface
public interface ThSupplier<R, E extends Throwable> {

  /**
   * Gets the result.
   *
   * @return result
   * @throws E if supplier threw exception
   */
  R get() throws E;

  /**
   * Returns given {@code ThSupplier} as unchecked or null if {@code supplier} arg is null.
   *
   * @param supplier the supplier
   * @param <R>      the type of the {@code supplier} result
   * @return unchecked {@code ThSupplier} or null
   */
  @SuppressWarnings("unchecked")
  static <R> ThSupplier<R, RuntimeException> unchecked(final ThSupplier<? extends R, ?> supplier) {
    return (ThSupplier<R, RuntimeException>) supplier;
  }
}
