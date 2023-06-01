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
import com.plugatar.xteps2.core.sc.base.BaseNoCtxSC;
import com.plugatar.xteps2.core.sc.base.MemSC;
import com.plugatar.xteps2.core.sc.base.StepContext;
import com.plugatar.xteps2.core.function.ThConsumer;
import com.plugatar.xteps2.core.function.ThFunction;
import com.plugatar.xteps2.core.function.ThRunnable;
import com.plugatar.xteps2.core.function.ThSupplier;

/**
 * Memorizing no context step context.
 *
 * @param <P> the type of the previous step context
 */
public interface MemNoCtxSC<P extends StepContext<?>> extends
  BaseNoCtxSC<MemNoCtxSC<P>>,
  MemSC<P, NoCtxSC> {

  @Override
  <R> MemCtxSC<R, P> with(ThSupplier<? extends R, ?> action);

  /**
   * Default {@code MemNoCtxSC} implementation.
   *
   * @param <P> the type of the previous step context
   */
  class Of<P extends StepContext<?>> implements MemNoCtxSC<P> {
    private final P previous;

    /**
     * Ctor.
     *
     * @param previous the previous step context
     */
    public Of(final P previous) {
      this.previous = previous;
    }

    @Override
    public final MemNoCtxSC<P> exec(final ThRunnable<?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      ThRunnable.unchecked(action).run();
      return this;
    }

    @Override
    public final <R> MemCtxSC<R, P> with(final ThSupplier<? extends R, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      return new MemCtxSC.Of<>(ThSupplier.unchecked(action).get(), this.previous);
    }

    @Override
    public final <R> R res(final ThSupplier<? extends R, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      return ThSupplier.unchecked(action).get();
    }

    @Override
    public final MemNoCtxSC<P> it(final ThConsumer<? super MemNoCtxSC<P>, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      ThConsumer.unchecked(action).accept(this);
      return this;
    }

    @Override
    public final <R> R itRes(final ThFunction<? super MemNoCtxSC<P>, ? extends R, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      return ThFunction.unchecked(action).apply(this);
    }

    @Override
    public final P previous() {
      return this.previous;
    }

    @Override
    public final NoCtxSC forget() {
      return NoCtxSC.instance();
    }
  }
}
