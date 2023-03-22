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
import com.plugatar.xteps2.core.chain.base.BaseCtxSC;
import com.plugatar.xteps2.core.chain.base.MemSC;
import com.plugatar.xteps2.core.chain.base.StepChain;
import com.plugatar.xteps2.core.function.ThConsumer;
import com.plugatar.xteps2.core.function.ThFunction;

import static com.plugatar.xteps2.core.HookPriority.NORM_HOOK_PRIORITY;
import static com.plugatar.xteps2.core.chain.StepChainUtils.correctPriorityArg;
import static com.plugatar.xteps2.core.chain.StepChainUtils.currentStepExecutor;
import static com.plugatar.xteps2.core.chain.StepChainUtils.currentTestHookContainer;
import static com.plugatar.xteps2.core.chain.StepChainUtils.newChainHookContainer;
import static com.plugatar.xteps2.core.chain.StepChainUtils.notNullArg;

public interface MemCtxSC<C, P extends StepChain<?>> extends
  BaseCtxSC<C, MemCtxSC<C, P>>,
  MemSC<P, CtxSC<C>> {

  @Override
  <R> MemBiCtxSC<R, C, MemCtxSC<C, P>> with(ThFunction<? super C, ? extends R, ?> action);

  @Override
  <R> MemCtxSC<R, P> map(final ThFunction<? super C, ? extends R, ?> mapper);

  class Of<C, P extends StepChain<?>> implements MemCtxSC<C, P> {
    private final StepExecutor executor;
    private final HookContainer hooks;
    private final C context;
    private final P previous;

    public Of(final C context,
              final P previous) {
      this(currentStepExecutor(), newChainHookContainer(), context, previous);
    }

    public Of(final StepExecutor executor,
              final HookContainer hooks,
              final C context,
              final P previous) {
      this.executor = notNullArg("executor", executor);
      this.hooks = notNullArg("hooks", hooks);
      this.context = context;
      this.previous = previous;
    }

    @Override
    public final MemCtxSC<C, P> next(final ThConsumer<? super C, ?> action) {
      notNullArg("action", action);
      this.executor.exec(this.hooks, () -> {
        action.accept(this.context);
        return null;
      });
      return this;
    }

    @Override
    public final <R> MemBiCtxSC<R, C, MemCtxSC<C, P>> with(final ThFunction<? super C, ? extends R, ?> action) {
      notNullArg("action", action);
      return new MemBiCtxSC.Of<>(this.executor, this.hooks, this.executor.exec(this.hooks, () -> action.apply(this.context)),
        this.context, this);
    }

    @Override
    public final <R> R res(final ThFunction<? super C, ? extends R, ?> action) {
      notNullArg("action", action);
      return this.executor.exec(this.hooks, () -> action.apply(this.context));
    }

    @Override
    public final MemCtxSC<C, P> chain(final ThConsumer<? super MemCtxSC<C, P>, ?> action) {
      notNullArg("action", action);
      this.executor.exec(this.hooks, () -> {
        action.accept(this);
        return null;
      });
      return this;
    }

    @Override
    public final <R> R chainRes(final ThFunction<? super MemCtxSC<C, P>, ? extends R, ?> action) {
      notNullArg("action", action);
      return this.executor.exec(this.hooks, () -> action.apply(this));
    }

    @Override
    public <R> MemCtxSC<R, P> map(final ThFunction<? super C, ? extends R, ?> mapper) {
      notNullArg("mapper", mapper);
      return new MemCtxSC.Of<>(this.executor, this.hooks, this.executor.exec(this.hooks, () -> mapper.apply(this.context)), previous);
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
      notNullArg("action", action);
      correctPriorityArg(priority);
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
      notNullArg("action", action);
      correctPriorityArg(priority);
      currentTestHookContainer().addHook(priority, () -> action.accept(this.context));
      return this;
    }
  }
}
