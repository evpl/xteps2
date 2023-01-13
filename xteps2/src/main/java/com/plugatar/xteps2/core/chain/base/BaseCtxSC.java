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

import com.plugatar.xteps2.core.HookPriority;
import com.plugatar.xteps2.core.XtepsException;
import com.plugatar.xteps2.core.function.ThConsumer;
import com.plugatar.xteps2.core.function.ThFunction;

/**
 * Base context step chain.
 *
 * @param <C> the type of the context
 * @param <S> the type of the step chain implementing {@code BaseCtxSC}
 */
public interface BaseCtxSC<C, S extends BaseCtxSC<C, S>> extends StepChain<S>, BaseAnyCtxSC<S> {

  /**
   * Performs given action.
   *
   * @param action the action
   * @return this step chain
   * @throws XtepsException if {@code action} is null
   */
  S next(ThConsumer<? super C, ?> action);

  /**
   * Performs given action and returns new context step chain.
   *
   * @param action the action
   * @param <R>    the type of the new context
   * @return new context step chain
   * @throws XtepsException if {@code action} is null
   */
  <R> BaseBiCtxSC<R, C, ?> with(ThFunction<? super C, ? extends R, ?> action);

  /**
   * Performs given action and returns action result.
   *
   * @param action the action
   * @param <R>    the type of the result
   * @return action result
   * @throws XtepsException if {@code action} is null
   */
  <R> R res(ThFunction<? super C, ? extends R, ?> action);

  /**
   * Performs given action and returns new context step chain.
   *
   * @param action the action
   * @param <R>    the type of the new context
   * @return new context step chain
   * @throws XtepsException if {@code action} is null
   */
  <R> BaseCtxSC<R, ?> map(final ThFunction<? super C, ? extends R, ?> action);

  /**
   * Returns the context.
   *
   * @return the context
   */
  C context();

  /**
   * Adds given hook to this steps chain.
   *
   * @param action the action
   * @return this step chain
   * @throws XtepsException if {@code action} is null
   */
  S chainHook(ThConsumer<? super C, ?> action);

  /**
   * Adds given hook with given priority to this steps chain.
   *
   * @param priority the priority
   * @param action   the action
   * @return this step chain
   * @throws XtepsException if {@code action} is null
   *                        or if {@code priority} is not in the range {@link HookPriority#MIN_HOOK_PRIORITY} to
   *                        {@link HookPriority#MAX_HOOK_PRIORITY}
   */
  S chainHook(int priority,
              ThConsumer<? super C, ?> action);

  /**
   * Adds given hook for the current test.
   *
   * @param action the action
   * @return this step chain
   * @throws XtepsException if {@code TestHookContainer} implementation not found
   *                        or if current test not found
   *                        or if {@code action} is null
   */
  S testHook(ThConsumer<? super C, ?> action);

  /**
   * Adds given hook with given priority for the current test.
   *
   * @param priority the priority
   * @param action   the action
   * @return this step chain
   * @throws XtepsException if {@code TestHookContainer} implementation not found
   *                        or if current test not found
   *                        or if {@code action} is null
   *                        or if {@code priority} is not in the range {@link HookPriority#MIN_HOOK_PRIORITY} to
   *                        {@link HookPriority#MAX_HOOK_PRIORITY}
   */
  S testHook(int priority,
             ThConsumer<? super C, ?> action);
}
