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
package com.plugatar.xteps2.core.sc;

import com.plugatar.xteps2.core.XtepsException;
import com.plugatar.xteps2.core.sc.base.BaseBiCtxSC;
import com.plugatar.xteps2.core.function.ThBiConsumer;
import com.plugatar.xteps2.core.function.ThBiFunction;
import com.plugatar.xteps2.core.function.ThConsumer;
import com.plugatar.xteps2.core.function.ThFunction;

/**
 * Bi context step context.
 *
 * @param <C1> the type of the first context
 * @param <C2> the type of the second context
 */
public interface BiCtxSC<C1, C2> extends BaseBiCtxSC<C1, C2, BiCtxSC<C1, C2>> {

  @Override
  <R> MemTriCtxSC<R, C1, C2, BiCtxSC<C1, C2>> with(ThBiFunction<? super C1, ? super C2, ? extends R, ?> action);

  @Override
  <R1, R2> MemBiCtxSC<R1, R2, BiCtxSC<C1, C2>> map(final ThBiFunction<? super C1, ? super C2, ? extends R1, ?> action1,
                                                   final ThBiFunction<? super C1, ? super C2, ? extends R2, ?> action2);

  /**
   * Default {@code BiCtxSC} implementation.
   *
   * @param <C1> the type of the first context
   * @param <C2> the type of the second context
   */
  class Of<C1, C2> implements BiCtxSC<C1, C2> {
    private final C1 context1;
    private final C2 context2;

    /**
     * Ctor.
     *
     * @param context1 the first context
     * @param context2 the second context
     */
    public Of(final C1 context1,
              final C2 context2) {
      this.context1 = context1;
      this.context2 = context2;
    }

    @Override
    public final BiCtxSC<C1, C2> exec(final ThBiConsumer<? super C1, ? super C2, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      ThBiConsumer.unchecked(action).accept(this.context1, this.context2);
      return this;
    }

    @Override
    public final <R> MemTriCtxSC<R, C1, C2, BiCtxSC<C1, C2>> with(final ThBiFunction<? super C1, ? super C2, ? extends R, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      return new MemTriCtxSC.Of<>(ThBiFunction.unchecked(action).apply(this.context1, this.context2),
        this.context1, this.context2, this);
    }

    @Override
    public final <R> R res(final ThBiFunction<? super C1, ? super C2, ? extends R, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      return ThBiFunction.unchecked(action).apply(this.context1, this.context2);
    }

    @Override
    public final BiCtxSC<C1, C2> it(final ThConsumer<? super BiCtxSC<C1, C2>, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      ThConsumer.unchecked(action).accept(this);
      return this;
    }

    @Override
    public final <R> R itRes(final ThFunction<? super BiCtxSC<C1, C2>, ? extends R, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      return ThFunction.unchecked(action).apply(this);
    }

    @Override
    public final <R1, R2> MemBiCtxSC<R1, R2, BiCtxSC<C1, C2>> map(final ThBiFunction<? super C1, ? super C2, ? extends R1, ?> action1,
                                                                  final ThBiFunction<? super C1, ? super C2, ? extends R2, ?> action2) {
      if (action1 == null) { throw new XtepsException("action1 arg is null"); }
      if (action2 == null) { throw new XtepsException("action2 arg is null"); }
      return new MemBiCtxSC.Of<>(
        ThBiFunction.unchecked(action1).apply(this.context1, this.context2),
        ThBiFunction.unchecked(action2).apply(this.context1, this.context2),
        this
      );
    }

    @Override
    public final C1 context1() {
      return this.context1;
    }

    @Override
    public final C2 context2() {
      return this.context2;
    }

    @Override
    public final MemNoCtxSC<BiCtxSC<C1, C2>> noContext() {
      return new MemNoCtxSC.Of<>(this);
    }
  }
}
