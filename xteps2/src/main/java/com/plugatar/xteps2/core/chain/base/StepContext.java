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
import com.plugatar.xteps2.core.function.ThConsumer;
import com.plugatar.xteps2.core.function.ThFunction;

/**
 * Base step context.
 *
 * @param <S> the type of the step context implementing {@code StepContext}
 */
public interface StepContext<S extends StepContext<S>> {

  /**
   * Executes given action on this step context.
   *
   * @param action the action
   * @return this step context
   * @throws XtepsException if {@code action} is null
   */
  S it(ThConsumer<? super S, ?> action);

  /**
   * Executes given action on this step context and returns result.
   *
   * @param action the action
   * @param <R>    the type of the result
   * @return action result
   * @throws XtepsException if {@code action} is null
   */
  <R> R itRes(ThFunction<? super S, ? extends R, ?> action);
}
