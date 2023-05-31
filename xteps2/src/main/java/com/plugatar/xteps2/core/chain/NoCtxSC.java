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
package com.plugatar.xteps2.core.chain;

import com.plugatar.xteps2.core.XtepsException;
import com.plugatar.xteps2.core.chain.base.BaseNoCtxSC;
import com.plugatar.xteps2.core.function.ThConsumer;
import com.plugatar.xteps2.core.function.ThFunction;
import com.plugatar.xteps2.core.function.ThRunnable;
import com.plugatar.xteps2.core.function.ThSupplier;

/**
 * No context step context.
 */
public interface NoCtxSC extends BaseNoCtxSC<NoCtxSC> {

  @Override
  <R> CtxSC<R> with(ThSupplier<? extends R, ?> action);

  static NoCtxSC instance() {
    return Of.INSTANCE;
  }

  /**
   * Default {@code NoCtxSC} implementation.
   */
  class Of implements NoCtxSC {
    private static final NoCtxSC INSTANCE = new Of();

    /**
     * Ctor.
     */
    public Of() {
    }

    @Override
    public final NoCtxSC exec(final ThRunnable<?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      ThRunnable.unchecked(action).run();
      return this;
    }

    @Override
    public final <R> CtxSC<R> with(final ThSupplier<? extends R, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      return new CtxSC.Of<>(ThSupplier.unchecked(action).get());
    }

    @Override
    public final <R> R res(final ThSupplier<? extends R, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      return ThSupplier.unchecked(action).get();
    }

    @Override
    public final NoCtxSC it(final ThConsumer<? super NoCtxSC, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      ThConsumer.unchecked(action).accept(this);
      return this;
    }

    @Override
    public final <R> R itRes(final ThFunction<? super NoCtxSC, ? extends R, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      return ThFunction.unchecked(action).apply(this);
    }
  }
}
