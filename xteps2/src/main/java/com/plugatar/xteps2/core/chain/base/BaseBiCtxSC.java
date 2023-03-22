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

import com.plugatar.xteps2.core.function.ThBiConsumer;
import com.plugatar.xteps2.core.function.ThBiFunction;

public interface BaseBiCtxSC<C1, C2, S extends BaseBiCtxSC<C1, C2, S>> extends StepChain<S>, BaseAnyCtxSC<S> {

  S next(ThBiConsumer<? super C1, ? super C2, ?> action);

  <R> BaseTriCtxSC<R, C1, C2, ?> with(ThBiFunction<? super C1, ? super C2, ? extends R, ?> action);

  <R> R res(ThBiFunction<? super C1, ? super C2, ? extends R, ?> action);

  <R1, R2> BaseBiCtxSC<R1, R2, ?> map(final ThBiFunction<? super C1, ? super C2, ? extends R1, ?> mapper1,
                                      final ThBiFunction<? super C1, ? super C2, ? extends R2, ?> mapper2);

  C1 context1();

  C2 context2();

  S chainHook(ThBiConsumer<? super C1, ? super C2, ?> action);

  S chainHook(int priority,
              ThBiConsumer<? super C1, ? super C2, ?> action);

  S testHook(ThBiConsumer<? super C1, ? super C2, ?> action);

  S testHook(int priority,
             ThBiConsumer<? super C1, ? super C2, ?> action);
}
