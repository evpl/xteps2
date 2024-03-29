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
import com.plugatar.xteps2.core.function.ThConsumer;
import com.plugatar.xteps2.core.function.ThFunction;
import com.plugatar.xteps2.core.sc.base.BaseCtxSC;

/**
 * Single context step context.
 *
 * @param <C> the type of the context
 */
public interface CtxSC<C> extends BaseCtxSC<C, CtxSC<C>> {

  @Override
  <R> MemBiCtxSC<R, C, CtxSC<C>> with(ThFunction<? super C, ? extends R, ?> action);

  @Override
  <R> MemCtxSC<R, CtxSC<C>> map(final ThFunction<? super C, ? extends R, ?> action);

  /**
   * Default {@code CtxSC} implementation.
   *
   * @param <C> the type of the context
   */
  class Of<C> implements CtxSC<C> {
    private final C context;

    /**
     * Ctor.
     *
     * @param context the context
     */
    public Of(final C context) {
      this.context = context;
    }

    @Override
    public final CtxSC<C> exec(final ThConsumer<? super C, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      ThConsumer.unchecked(action).accept(this.context);
      return this;
    }

    @Override
    public final <R> MemBiCtxSC<R, C, CtxSC<C>> with(final ThFunction<? super C, ? extends R, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      return new MemBiCtxSC.Of<>(ThFunction.unchecked(action).apply(this.context), this.context, this);
    }

    @Override
    public final <R> R res(final ThFunction<? super C, ? extends R, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      return ThFunction.unchecked(action).apply(this.context);
    }

    @Override
    public final CtxSC<C> it(final ThConsumer<? super CtxSC<C>, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      ThConsumer.unchecked(action).accept(this);
      return this;
    }

    @Override
    public final <R> R itRes(final ThFunction<? super CtxSC<C>, ? extends R, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      return ThFunction.unchecked(action).apply(this);
    }

    @Override
    public final <R> MemCtxSC<R, CtxSC<C>> map(final ThFunction<? super C, ? extends R, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      return new MemCtxSC.Of<>(ThFunction.unchecked(action).apply(this.context), this);
    }

    @Override
    public final C context() {
      return this.context;
    }

    @Override
    public final MemNoCtxSC<CtxSC<C>> noContext() {
      return new MemNoCtxSC.Of<>(this);
    }
  }
}
