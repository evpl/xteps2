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

import com.plugatar.xteps2.core.function.ThTriConsumer;
import com.plugatar.xteps2.core.function.ThTriFunction;

public interface BaseTriCtxSC<C1, C2, C3, S extends BaseTriCtxSC<C1, C2, C3, S>> extends StepChain<S>, BaseAnyCtxSC<S> {

  S next(ThTriConsumer<? super C1, ? super C2, ? super C3, ?> action);

  <R> BaseTriCtxSC<R, C1, C2, ?> with(ThTriFunction<? super C1, ? super C2, ? super C3, ? extends R, ?> action);

  <R> R res(ThTriFunction<? super C1, ? super C2, ? super C3, ? extends R, ?> action);

  <R1, R2, R3> BaseTriCtxSC<R1, R2, R3, ?> map(final ThTriFunction<? super C1, ? super C2, ? super C3, ? extends R1, ?> mapper1,
                                               final ThTriFunction<? super C1, ? super C2, ? super C3, ? extends R2, ?> mapper2,
                                               final ThTriFunction<? super C1, ? super C2, ? super C3, ? extends R3, ?> mapper3);

  C1 context1();

  C2 context2();

  C3 context3();

  S chainHook(ThTriConsumer<? super C1, ? super C2, ? super C3, ?> action);

  S chainHook(int priority,
              ThTriConsumer<? super C1, ? super C2, ? super C3, ?> action);

  S testHook(ThTriConsumer<? super C1, ? super C2, ? super C3, ?> action);

  S testHook(int priority,
             ThTriConsumer<? super C1, ? super C2, ? super C3, ?> action);
}
