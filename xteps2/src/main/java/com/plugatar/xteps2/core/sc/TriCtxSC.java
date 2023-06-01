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
import com.plugatar.xteps2.core.sc.base.BaseTriCtxSC;
import com.plugatar.xteps2.core.function.ThConsumer;
import com.plugatar.xteps2.core.function.ThFunction;
import com.plugatar.xteps2.core.function.ThTriConsumer;
import com.plugatar.xteps2.core.function.ThTriFunction;

/**
 * Tri step context.
 *
 * @param <C1> the type of the first context
 * @param <C2> the type of the second context
 * @param <C3> the type of the third context
 */
public interface TriCtxSC<C1, C2, C3> extends BaseTriCtxSC<C1, C2, C3, TriCtxSC<C1, C2, C3>> {

  @Override
  <R> MemTriCtxSC<R, C1, C2, TriCtxSC<C1, C2, C3>> with(ThTriFunction<? super C1, ? super C2, ? super C3, ? extends R, ?> action);

  @Override
  <R1, R2, R3> MemTriCtxSC<R1, R2, R3, TriCtxSC<C1, C2, C3>> map(final ThTriFunction<? super C1, ? super C2, ? super C3, ? extends R1, ?> action1,
                                                                 final ThTriFunction<? super C1, ? super C2, ? super C3, ? extends R2, ?> action2,
                                                                 final ThTriFunction<? super C1, ? super C2, ? super C3, ? extends R3, ?> action3);

  /**
   * Default {@code TriCtxSC} implementation.
   *
   * @param <C1> the type of the first context
   * @param <C2> the type of the second context
   * @param <C3> the type of the third context
   */
  class Of<C1, C2, C3> implements TriCtxSC<C1, C2, C3> {
    private final C1 context1;
    private final C2 context2;
    private final C3 context3;

    /**
     * Ctor.
     *
     * @param context1 the first context
     * @param context2 the second context
     * @param context3 the third context
     */
    public Of(final C1 context1,
              final C2 context2,
              final C3 context3) {
      this.context1 = context1;
      this.context2 = context2;
      this.context3 = context3;
    }

    @Override
    public final TriCtxSC<C1, C2, C3> exec(final ThTriConsumer<? super C1, ? super C2, ? super C3, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      ThTriConsumer.unchecked(action).accept(this.context1, this.context2, this.context3);
      return this;
    }

    @Override
    public final <R> MemTriCtxSC<R, C1, C2, TriCtxSC<C1, C2, C3>> with(final ThTriFunction<? super C1, ? super C2, ? super C3, ? extends R, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      return new MemTriCtxSC.Of<>(ThTriFunction.unchecked(action).apply(this.context1, this.context2, this.context3),
        this.context1, this.context2, this);
    }

    @Override
    public final <R> R res(final ThTriFunction<? super C1, ? super C2, ? super C3, ? extends R, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      return ThTriFunction.unchecked(action).apply(this.context1, this.context2, this.context3);
    }

    @Override
    public final TriCtxSC<C1, C2, C3> it(final ThConsumer<? super TriCtxSC<C1, C2, C3>, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      ThConsumer.unchecked(action).accept(this);
      return this;
    }

    @Override
    public final <R> R itRes(final ThFunction<? super TriCtxSC<C1, C2, C3>, ? extends R, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      return ThFunction.unchecked(action).apply(this);
    }

    @Override
    public final <R1, R2, R3> MemTriCtxSC<R1, R2, R3, TriCtxSC<C1, C2, C3>> map(final ThTriFunction<? super C1, ? super C2, ? super C3, ? extends R1, ?> action1,
                                                                                final ThTriFunction<? super C1, ? super C2, ? super C3, ? extends R2, ?> action2,
                                                                                final ThTriFunction<? super C1, ? super C2, ? super C3, ? extends R3, ?> action3) {
      if (action1 == null) { throw new XtepsException("action1 arg is null"); }
      if (action2 == null) { throw new XtepsException("action2 arg is null"); }
      if (action3 == null) { throw new XtepsException("action3 arg is null"); }
      return new MemTriCtxSC.Of<>(
        ThTriFunction.unchecked(action1).apply(this.context1, this.context2, this.context3),
        ThTriFunction.unchecked(action2).apply(this.context1, this.context2, this.context3),
        ThTriFunction.unchecked(action3).apply(this.context1, this.context2, this.context3),
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
    public final C3 context3() {
      return this.context3;
    }

    @Override
    public final MemNoCtxSC<TriCtxSC<C1, C2, C3>> noContext() {
      return new MemNoCtxSC.Of<>(this);
    }
  }
}
