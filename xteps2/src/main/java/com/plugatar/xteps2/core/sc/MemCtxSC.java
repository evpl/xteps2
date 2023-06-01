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
import com.plugatar.xteps2.core.sc.base.BaseCtxSC;
import com.plugatar.xteps2.core.sc.base.MemSC;
import com.plugatar.xteps2.core.sc.base.StepContext;
import com.plugatar.xteps2.core.function.ThConsumer;
import com.plugatar.xteps2.core.function.ThFunction;

/**
 * Memorizing single context step context.
 *
 * @param <C> the type of the context
 * @param <P> the type of the previous step context
 */
public interface MemCtxSC<C, P extends StepContext<?>> extends
  BaseCtxSC<C, MemCtxSC<C, P>>,
  MemSC<P, CtxSC<C>> {

  @Override
  <R> MemBiCtxSC<R, C, MemCtxSC<C, P>> with(ThFunction<? super C, ? extends R, ?> action);

  @Override
  <R> MemCtxSC<R, MemCtxSC<C, P>> map(final ThFunction<? super C, ? extends R, ?> action);

  /**
   * Default {@code MemCtxSC} implementation.
   *
   * @param <C> the type of the context
   * @param <P> the type of the previous step context
   */
  class Of<C, P extends StepContext<?>> implements MemCtxSC<C, P> {
    private final C context;
    private final P previous;

    /**
     * Ctor.
     *
     * @param context  the context
     * @param previous the previous step context
     */
    public Of(final C context,
              final P previous) {
      this.context = context;
      this.previous = previous;
    }

    @Override
    public final MemCtxSC<C, P> exec(final ThConsumer<? super C, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      ThConsumer.unchecked(action).accept(this.context);
      return this;
    }

    @Override
    public final <R> MemBiCtxSC<R, C, MemCtxSC<C, P>> with(final ThFunction<? super C, ? extends R, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      return new MemBiCtxSC.Of<>(ThFunction.unchecked(action).apply(this.context), this.context, this);
    }

    @Override
    public final <R> R res(final ThFunction<? super C, ? extends R, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      return ThFunction.unchecked(action).apply(this.context);
    }

    @Override
    public final MemCtxSC<C, P> it(final ThConsumer<? super MemCtxSC<C, P>, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      ThConsumer.unchecked(action).accept(this);
      return this;
    }

    @Override
    public final <R> R itRes(final ThFunction<? super MemCtxSC<C, P>, ? extends R, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      return ThFunction.unchecked(action).apply(this);
    }

    @Override
    public final <R> MemCtxSC<R, MemCtxSC<C, P>> map(final ThFunction<? super C, ? extends R, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      return new MemCtxSC.Of<>(ThFunction.unchecked(action).apply(this.context), this);
    }

    @Override
    public final C context() {
      return this.context;
    }

    @Override
    public final MemNoCtxSC<MemCtxSC<C, P>> noContext() {
      return new MemNoCtxSC.Of<>(this);
    }

    @Override
    public final P previous() {
      return this.previous;
    }

    @Override
    public final CtxSC<C> forget() {
      return new CtxSC.Of<>(this.context);
    }
  }
}
