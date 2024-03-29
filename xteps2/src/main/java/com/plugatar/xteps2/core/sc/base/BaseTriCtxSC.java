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
package com.plugatar.xteps2.core.sc.base;

import com.plugatar.xteps2.core.XtepsException;
import com.plugatar.xteps2.core.function.ThTriConsumer;
import com.plugatar.xteps2.core.function.ThTriFunction;

/**
 * Base tri context step context.
 *
 * @param <C1> the type of the first context
 * @param <C2> the type of the second context
 * @param <C3> the type of the third context
 * @param <S>  the type of the step context implementing {@code BaseTriCtxSC}
 */
public interface BaseTriCtxSC<C1, C2, C3, S extends BaseTriCtxSC<C1, C2, C3, S>> extends StepContext<S>, BaseAnyCtxSC<S> {

  /**
   * Executes given action.
   *
   * @param action the action
   * @return this step context
   * @throws XtepsException if {@code action} is null
   */
  S exec(ThTriConsumer<? super C1, ? super C2, ? super C3, ?> action);

  /**
   * Executes given action and returns new context step context.
   *
   * @param action the action
   * @param <R>    the type of the new context
   * @return new context step context
   * @throws XtepsException if {@code action} is null
   */
  <R> BaseTriCtxSC<R, C1, C2, ?> with(ThTriFunction<? super C1, ? super C2, ? super C3, ? extends R, ?> action);

  /**
   * Executes given action and returns action result.
   *
   * @param action the action
   * @param <R>    the type of the result
   * @return action result
   * @throws XtepsException if {@code action} is null
   */
  <R> R res(ThTriFunction<? super C1, ? super C2, ? super C3, ? extends R, ?> action);

  /**
   * Executes given action and returns new context step context.
   *
   * @param action1 the first action
   * @param action2 the second action
   * @param action3 the second action
   * @param <R1>    the type of the new context
   * @param <R2>    the type of the new context
   * @param <R3>    the type of the new context
   * @return new context step context
   * @throws XtepsException if {@code action1} is null
   *                        or if {@code action2} is null
   *                        or if {@code action3} is null
   */
  <R1, R2, R3> BaseTriCtxSC<R1, R2, R3, ?> map(final ThTriFunction<? super C1, ? super C2, ? super C3, ? extends R1, ?> action1,
                                               final ThTriFunction<? super C1, ? super C2, ? super C3, ? extends R2, ?> action2,
                                               final ThTriFunction<? super C1, ? super C2, ? super C3, ? extends R3, ?> action3);

  /**
   * Returns the first context.
   *
   * @return the first context
   */
  C1 context1();

  /**
   * Returns the second context.
   *
   * @return the second context
   */
  C2 context2();

  /**
   * Returns the third context.
   *
   * @return the third context
   */
  C3 context3();
}
