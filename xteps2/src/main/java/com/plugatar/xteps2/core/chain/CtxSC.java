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
import com.plugatar.xteps2.core.function.ThConsumer;
import com.plugatar.xteps2.core.function.ThFunction;

import static com.plugatar.xteps2.core.HookPriority.NORM_HOOK_PRIORITY;
import static com.plugatar.xteps2.core.chain.StepChainUtils.checkPriorityArg;
import static com.plugatar.xteps2.core.chain.StepChainUtils.currentStepExecutor;
import static com.plugatar.xteps2.core.chain.StepChainUtils.currentTestHookContainer;
import static com.plugatar.xteps2.core.chain.StepChainUtils.newChainHookContainer;

/**
 * Context step chain.
 *
 * @param <C> the type of the context
 */
public interface CtxSC<C> extends BaseCtxSC<C, CtxSC<C>> {

  @Override
  <R> MemBiCtxSC<R, C, CtxSC<C>> with(ThFunction<? super C, ? extends R, ?> action);

  @Override
  <R> CtxSC<R> map(final ThFunction<? super C, ? extends R, ?> action);

  /**
   * Default {@code CtxSC} implementation.
   *
   * @param <C> the type of the context
   */
  class Of<C> implements CtxSC<C> {
    private final StepExecutor executor;
    private final HookContainer hooks;
    private final C context;

    /**
     * Ctor.
     *
     * @param context the context
     * @throws XtepsException if Xteps configuration is incorrect
     */
    public Of(final C context) {
      this(currentStepExecutor(), newChainHookContainer(), context);
    }

    /**
     * Ctor.
     *
     * @param executor the step executor
     * @param hooks    the hooks container
     * @param context  the context
     */
    public Of(final StepExecutor executor,
              final HookContainer hooks,
              final C context) {
      if (executor == null) { throw new XtepsException("executor arg is null"); }
      if (hooks == null) { throw new XtepsException("hooks arg is null"); }
      this.executor = executor;
      this.hooks = hooks;
      this.context = context;
    }

    @Override
    public final CtxSC<C> next(final ThConsumer<? super C, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      this.executor.exec(this.hooks, () -> {
        action.accept(this.context);
        return null;
      });
      return this;
    }

    @Override
    public final <R> MemBiCtxSC<R, C, CtxSC<C>> with(final ThFunction<? super C, ? extends R, ?> action) {
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
    public final CtxSC<C> chain(final ThConsumer<? super CtxSC<C>, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      this.executor.exec(this.hooks, () -> {
        action.accept(this);
        return null;
      });
      return this;
    }

    @Override
    public final <R> R chainRes(final ThFunction<? super CtxSC<C>, ? extends R, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      return this.executor.exec(this.hooks, () -> action.apply(this));
    }

    @Override
    public final <R> CtxSC<R> map(final ThFunction<? super C, ? extends R, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      return new CtxSC.Of<>(this.executor, this.hooks, this.executor.exec(this.hooks, () -> action.apply(this.context)));
    }

    @Override
    public final C context() {
      return this.context;
    }

    @Override
    public final MemNoCtxSC<CtxSC<C>> noContext() {
      return new MemNoCtxSC.Of<>(this.executor, this.hooks, this);
    }

    @Override
    public final CtxSC<C> callChainHooks() {
      this.hooks.callHooks();
      return this;
    }

    @Override
    public final CtxSC<C> chainHook(final ThConsumer<? super C, ?> action) {
      return this.chainHook(NORM_HOOK_PRIORITY, action);
    }

    @Override
    public final CtxSC<C> chainHook(final int priority,
                                    final ThConsumer<? super C, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      checkPriorityArg(priority);
      this.hooks.addHook(priority, () -> action.accept(this.context));
      return this;
    }

    @Override
    public final CtxSC<C> testHook(final ThConsumer<? super C, ?> action) {
      return this.testHook(NORM_HOOK_PRIORITY, action);
    }

    @Override
    public final CtxSC<C> testHook(final int priority,
                                   final ThConsumer<? super C, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      checkPriorityArg(priority);
      currentTestHookContainer().addHook(priority, () -> action.accept(this.context));
      return this;
    }
  }
}
