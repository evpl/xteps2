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

import com.plugatar.xteps2.core.HookContainer;
import com.plugatar.xteps2.core.StepExecutor;
import com.plugatar.xteps2.core.XtepsException;
import com.plugatar.xteps2.core.chain.base.BaseCtxSC;
import com.plugatar.xteps2.core.chain.base.MemSC;
import com.plugatar.xteps2.core.chain.base.StepChain;
import com.plugatar.xteps2.core.function.ThConsumer;
import com.plugatar.xteps2.core.function.ThFunction;

import static com.plugatar.xteps2.core.HookPriority.NORM_HOOK_PRIORITY;
import static com.plugatar.xteps2.core.chain.StepChainUtils.checkPriorityArg;
import static com.plugatar.xteps2.core.chain.StepChainUtils.currentStepExecutor;
import static com.plugatar.xteps2.core.chain.StepChainUtils.currentTestHookContainer;
import static com.plugatar.xteps2.core.chain.StepChainUtils.newChainHookContainer;

/**
 * Memorizing context step chain.
 *
 * @param <C> the type of the context
 * @param <P> the type of the previous step chain
 */
public interface MemCtxSC<C, P extends StepChain<?>> extends
  BaseCtxSC<C, MemCtxSC<C, P>>,
  MemSC<P, CtxSC<C>> {

  @Override
  <R> MemBiCtxSC<R, C, MemCtxSC<C, P>> with(ThFunction<? super C, ? extends R, ?> action);

  @Override
  <R> MemCtxSC<R, P> map(final ThFunction<? super C, ? extends R, ?> action);

  /**
   * Default {@code MemCtxSC} implementation.
   *
   * @param <C> the type of the context
   * @param <P> the type of the previous step chain
   */
  class Of<C, P extends StepChain<?>> implements MemCtxSC<C, P> {
    private final StepExecutor executor;
    private final HookContainer hooks;
    private final C context;
    private final P previous;

    /**
     * Ctor.
     *
     * @param context  the context
     * @param previous the previous step chain
     * @throws XtepsException if Xteps configuration is incorrect
     */
    public Of(final C context,
              final P previous) {
      this(currentStepExecutor(), newChainHookContainer(), context, previous);
    }

    /**
     * Ctor.
     *
     * @param executor the step executor
     * @param hooks    the hooks container
     * @param context  the context
     * @param previous the previous step chain
     */
    public Of(final StepExecutor executor,
              final HookContainer hooks,
              final C context,
              final P previous) {
      if (executor == null) { throw new XtepsException("executor arg is null"); }
      if (hooks == null) { throw new XtepsException("hooks arg is null"); }
      this.executor = executor;
      this.hooks = hooks;
      this.context = context;
      this.previous = previous;
    }

    @Override
    public final MemCtxSC<C, P> next(final ThConsumer<? super C, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      this.executor.exec(this.hooks, () -> {
        action.accept(this.context);
        return null;
      });
      return this;
    }

    @Override
    public final <R> MemBiCtxSC<R, C, MemCtxSC<C, P>> with(final ThFunction<? super C, ? extends R, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      return new MemBiCtxSC.Of<>(this.executor, this.hooks, this.executor.exec(this.hooks, () -> action.apply(this.context)),
        this.context, this);
    }

    @Override
    public final <R> R res(final ThFunction<? super C, ? extends R, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      return this.executor.exec(this.hooks, () -> action.apply(this.context));
    }

    @Override
    public final MemCtxSC<C, P> chain(final ThConsumer<? super MemCtxSC<C, P>, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      this.executor.exec(this.hooks, () -> {
        action.accept(this);
        return null;
      });
      return this;
    }

    @Override
    public final <R> R chainRes(final ThFunction<? super MemCtxSC<C, P>, ? extends R, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      return this.executor.exec(this.hooks, () -> action.apply(this));
    }

    @Override
    public final <R> MemCtxSC<R, P> map(final ThFunction<? super C, ? extends R, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      return new MemCtxSC.Of<>(this.executor, this.hooks, this.executor.exec(this.hooks, () -> action.apply(this.context)), previous);
    }

    @Override
    public final C context() {
      return this.context;
    }

    @Override
    public final MemNoCtxSC<MemCtxSC<C, P>> noContext() {
      return new MemNoCtxSC.Of<>(this.executor, this.hooks, this);
    }

    @Override
    public final P previous() {
      return this.previous;
    }

    @Override
    public final CtxSC<C> forget() {
      return new CtxSC.Of<>(this.executor, this.hooks, this.context);
    }

    @Override
    public final MemCtxSC<C, P> callChainHooks() {
      this.hooks.callHooks();
      return this;
    }

    @Override
    public final MemCtxSC<C, P> chainHook(final ThConsumer<? super C, ?> action) {
      return this.chainHook(NORM_HOOK_PRIORITY, action);
    }

    @Override
    public final MemCtxSC<C, P> chainHook(final int priority,
                                          final ThConsumer<? super C, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      checkPriorityArg(priority);
      this.hooks.addHook(priority, () -> action.accept(this.context));
      return this;
    }

    @Override
    public final MemCtxSC<C, P> testHook(final ThConsumer<? super C, ?> action) {
      return this.testHook(NORM_HOOK_PRIORITY, action);
    }

    @Override
    public final MemCtxSC<C, P> testHook(final int priority,
                                         final ThConsumer<? super C, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      checkPriorityArg(priority);
      currentTestHookContainer().addHook(priority, () -> action.accept(this.context));
      return this;
    }
  }
}
