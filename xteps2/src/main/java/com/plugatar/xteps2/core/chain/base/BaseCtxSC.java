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

import com.plugatar.xteps2.core.function.ThConsumer;
import com.plugatar.xteps2.core.function.ThFunction;

public interface BaseCtxSC<C, S extends BaseCtxSC<C, S>> extends StepChain<S>, BaseAnyCtxSC<S> {

  S next(ThConsumer<? super C, ?> action);

  <R> BaseBiCtxSC<R, C, ?> with(ThFunction<? super C, ? extends R, ?> action);

  <R> R res(ThFunction<? super C, ? extends R, ?> action);

  <R> BaseCtxSC<R, ?> map(final ThFunction<? super C, ? extends R, ?> mapper);

  C context();

  S chainHook(ThConsumer<? super C, ?> action);

  S chainHook(int priority,
              ThConsumer<? super C, ?> action);

  S testHook(ThConsumer<? super C, ?> action);

  S testHook(int priority,
             ThConsumer<? super C, ?> action);
}
