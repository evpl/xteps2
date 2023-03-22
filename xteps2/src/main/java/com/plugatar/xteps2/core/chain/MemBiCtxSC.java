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
import com.plugatar.xteps2.core.chain.base.BaseBiCtxSC;
import com.plugatar.xteps2.core.chain.base.MemSC;
import com.plugatar.xteps2.core.chain.base.StepChain;
import com.plugatar.xteps2.core.function.ThBiConsumer;
import com.plugatar.xteps2.core.function.ThBiFunction;
import com.plugatar.xteps2.core.function.ThConsumer;
import com.plugatar.xteps2.core.function.ThFunction;

import static com.plugatar.xteps2.core.HookPriority.NORM_HOOK_PRIORITY;
import static com.plugatar.xteps2.core.chain.StepChainUtils.correctPriorityArg;
import static com.plugatar.xteps2.core.chain.StepChainUtils.currentStepExecutor;
import static com.plugatar.xteps2.core.chain.StepChainUtils.currentTestHookContainer;
import static com.plugatar.xteps2.core.chain.StepChainUtils.newChainHookContainer;
import static com.plugatar.xteps2.core.chain.StepChainUtils.notNullArg;

public interface MemBiCtxSC<C1, C2, P extends StepChain<?>> extends
  BaseBiCtxSC<C1, C2, MemBiCtxSC<C1, C2, P>>,
  MemSC<P, BiCtxSC<C1, C2>> {

  @Override
  <R> MemTriCtxSC<R, C1, C2, MemBiCtxSC<C1, C2, P>> with(ThBiFunction<? super C1, ? super C2, ? extends R, ?> action);

  @Override
  <R1, R2> MemBiCtxSC<R1, R2, P> map(final ThBiFunction<? super C1, ? super C2, ? extends R1, ?> mapper1,
                                     final ThBiFunction<? super C1, ? super C2, ? extends R2, ?> mapper2);

  class Of<C1, C2, P extends StepChain<?>> implements MemBiCtxSC<C1, C2, P> {
    private final StepExecutor executor;
    private final HookContainer hooks;
    private final C1 context1;
    private final C2 context2;
    private final P previous;

    public Of(final C1 context1,
              final C2 context2,
              final P previous) {
      this(currentStepExecutor(), newChainHookContainer(), context1, context2, previous);
    }

    public Of(final StepExecutor executor,
              final HookContainer hooks,
              final C1 context1,
              final C2 context2,
              final P previous) {
      this.executor = notNullArg("executor", executor);
      this.hooks = notNullArg("hooks", hooks);
      this.context1 = context1;
      this.context2 = context2;
      this.previous = previous;
    }

    @Override
    public final MemBiCtxSC<C1, C2, P> next(final ThBiConsumer<? super C1, ? super C2, ?> action) {
      notNullArg("action", action);
      this.executor.exec(this.hooks, () -> {
        action.accept(this.context1, this.context2);
        return null;
      });
      return this;
    }

    @Override
    public final <R> MemTriCtxSC<R, C1, C2, MemBiCtxSC<C1, C2, P>> with(final ThBiFunction<? super C1, ? super C2, ? extends R, ?> action) {
      notNullArg("action", action);
      return new MemTriCtxSC.Of<>(this.executor, this.hooks, this.executor.exec(this.hooks, () -> action.apply(this.context1, this.context2)),
        this.context1, this.context2, this);
    }

    @Override
    public final <R> R res(final ThBiFunction<? super C1, ? super C2, ? extends R, ?> action) {
      notNullArg("action", action);
      return this.executor.exec(this.hooks, () -> action.apply(this.context1, this.context2));
    }

    @Override
    public final MemBiCtxSC<C1, C2, P> chain(final ThConsumer<? super MemBiCtxSC<C1, C2, P>, ?> action) {
      notNullArg("action", action);
      this.executor.exec(this.hooks, () -> {
        action.accept(this);
        return null;
      });
      return this;
    }

    @Override
    public final <R> R chainRes(final ThFunction<? super MemBiCtxSC<C1, C2, P>, ? extends R, ?> action) {
      notNullArg("action", action);
      return this.executor.exec(this.hooks, () -> action.apply(this));
    }

    @Override
    public final <R1, R2> MemBiCtxSC<R1, R2, P> map(final ThBiFunction<? super C1, ? super C2, ? extends R1, ?> mapper1,
                                                    final ThBiFunction<? super C1, ? super C2, ? extends R2, ?> mapper2) {
      notNullArg("mapper1", mapper1);
      notNullArg("mapper2", mapper2);
      return new MemBiCtxSC.Of<>(
        this.executor,
        this.hooks,
        this.executor.exec(this.hooks, () -> mapper1.apply(this.context1, this.context2)),
        this.executor.exec(this.hooks, () -> mapper2.apply(this.context1, this.context2)),
        this.previous
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
    public final MemNoCtxSC<MemBiCtxSC<C1, C2, P>> noContext() {
      return new MemNoCtxSC.Of<>(this.executor, this.hooks, this);
    }

    @Override
    public final P previous() {
      return this.previous;
    }

    @Override
    public final BiCtxSC<C1, C2> forget() {
      return new BiCtxSC.Of<>(this.executor, this.hooks, this.context1, this.context2);
    }

    @Override
    public final MemBiCtxSC<C1, C2, P> callChainHooks() {
      this.hooks.callHooks();
      return this;
    }

    @Override
    public final MemBiCtxSC<C1, C2, P> chainHook(final ThBiConsumer<? super C1, ? super C2, ?> action) {
      return this.chainHook(NORM_HOOK_PRIORITY, action);
    }

    @Override
    public final MemBiCtxSC<C1, C2, P> chainHook(final int priority,
                                                 final ThBiConsumer<? super C1, ? super C2, ?> action) {
      notNullArg("action", action);
      correctPriorityArg(priority);
      this.hooks.addHook(priority, () -> action.accept(this.context1, this.context2));
      return this;
    }

    @Override
    public final MemBiCtxSC<C1, C2, P> testHook(final ThBiConsumer<? super C1, ? super C2, ?> action) {
      return this.testHook(NORM_HOOK_PRIORITY, action);
    }

    @Override
    public final MemBiCtxSC<C1, C2, P> testHook(final int priority, final ThBiConsumer<? super C1, ? super C2, ?> action) {
      notNullArg("action", action);
      correctPriorityArg(priority);
      currentTestHookContainer().addHook(priority, () -> action.accept(this.context1, this.context2));
      return this;
    }
  }
}
