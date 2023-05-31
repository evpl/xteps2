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
package com.plugatar.xteps2.core.chain.base;

import com.plugatar.xteps2.core.XtepsException;
import com.plugatar.xteps2.core.function.ThRunnable;
import com.plugatar.xteps2.core.function.ThSupplier;

/**
 * Base no context step context.
 *
 * @param <S> the type of the step context implementing {@code BaseNoCtxSC}
 */
public interface BaseNoCtxSC<S extends BaseNoCtxSC<S>> extends StepContext<S> {

  /**
   * Executes given action.
   *
   * @param action the action
   * @return this step context
   * @throws XtepsException if {@code action} is null
   */
  S exec(ThRunnable<?> action);

  /**
   * Executes given action and returns new context step context.
   *
   * @param action the action
   * @param <R>    the type of the nwe context
   * @return new context step context
   * @throws XtepsException if {@code action} is null
   */
  <R> BaseCtxSC<R, ?> with(ThSupplier<? extends R, ?> action);

  /**
   * Executes given action and returns action result.
   *
   * @param action the action
   * @param <R>    the type of the result
   * @return action result
   * @throws XtepsException if {@code action} is null
   */
  <R> R res(ThSupplier<? extends R, ?> action);
}
