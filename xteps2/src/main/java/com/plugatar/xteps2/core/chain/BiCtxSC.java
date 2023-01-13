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
import com.plugatar.xteps2.core.chain.base.BaseBiCtxSC;
import com.plugatar.xteps2.core.function.ThBiConsumer;
import com.plugatar.xteps2.core.function.ThBiFunction;
import com.plugatar.xteps2.core.function.ThConsumer;
import com.plugatar.xteps2.core.function.ThFunction;

import static com.plugatar.xteps2.core.HookPriority.NORM_HOOK_PRIORITY;
import static com.plugatar.xteps2.core.chain.StepChainUtils.checkPriorityArg;
import static com.plugatar.xteps2.core.chain.StepChainUtils.currentStepExecutor;
import static com.plugatar.xteps2.core.chain.StepChainUtils.currentTestHookContainer;
import static com.plugatar.xteps2.core.chain.StepChainUtils.newChainHookContainer;

/**
 * Bi context step chain.
 *
 * @param <C1> the type of the first context
 * @param <C2> the type of the second context
 */
public interface BiCtxSC<C1, C2> extends BaseBiCtxSC<C1, C2, BiCtxSC<C1, C2>> {

  @Override
  <R> MemTriCtxSC<R, C1, C2, BiCtxSC<C1, C2>> with(ThBiFunction<? super C1, ? super C2, ? extends R, ?> action);

  @Override
  <R1, R2> BiCtxSC<R1, R2> map(final ThBiFunction<? super C1, ? super C2, ? extends R1, ?> action1,
                               final ThBiFunction<? super C1, ? super C2, ? extends R2, ?> action2);

  /**
   * Default {@code BiCtxSC} implementation.
   *
   * @param <C1> the type of the first context
   * @param <C2> the type of the second context
   */
  class Of<C1, C2> implements BiCtxSC<C1, C2> {
    private final StepExecutor executor;
    private final HookContainer hooks;
    private final C1 context1;
    private final C2 context2;

    /**
     * Ctor.
     *
     * @param context1 the first context
     * @param context2 the second context
     * @throws XtepsException if Xteps configuration is incorrect
     */
    public Of(final C1 context1,
              final C2 context2) {
      this(currentStepExecutor(), newChainHookContainer(), context1, context2);
    }

    /**
     * Ctor.
     *
     * @param executor the step executor
     * @param hooks    the hooks container
     * @param context1 the first context
     * @param context2 the second context
     */
    public Of(final StepExecutor executor,
              final HookContainer hooks,
              final C1 context1,
              final C2 context2) {
      if (executor == null) { throw new XtepsException("executor arg is null"); }
      if (hooks == null) { throw new XtepsException("hooks arg is null"); }
      this.executor = executor;
      this.hooks = hooks;
      this.context1 = context1;
      this.context2 = context2;
    }

    @Override
    public final BiCtxSC<C1, C2> next(final ThBiConsumer<? super C1, ? super C2, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      this.executor.exec(this.hooks, () -> {
        action.accept(this.context1, this.context2);
        return null;
      });
      return this;
    }

    @Override
    public final <R> MemTriCtxSC<R, C1, C2, BiCtxSC<C1, C2>> with(final ThBiFunction<? super C1, ? super C2, ? extends R, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      return new MemTriCtxSC.Of<>(this.executor, this.hooks, this.executor.exec(this.hooks, () -> action.apply(this.context1, this.context2)),
        this.context1, this.context2, this);
    }

    @Override
    public final <R> R res(final ThBiFunction<? super C1, ? super C2, ? extends R, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      return this.executor.exec(this.hooks, () -> action.apply(this.context1, this.context2));
    }

    @Override
    public final BiCtxSC<C1, C2> chain(final ThConsumer<? super BiCtxSC<C1, C2>, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      this.executor.exec(this.hooks, () -> {
        action.accept(this);
        return null;
      });
      return this;
    }

    @Override
    public final <R> R chainRes(final ThFunction<? super BiCtxSC<C1, C2>, ? extends R, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      return this.executor.exec(this.hooks, () -> action.apply(this));
    }

    @Override
    public final <R1, R2> BiCtxSC<R1, R2> map(final ThBiFunction<? super C1, ? super C2, ? extends R1, ?> action1,
                                              final ThBiFunction<? super C1, ? super C2, ? extends R2, ?> action2) {
      if (action1 == null) { throw new XtepsException("action1 arg is null"); }
      if (action2 == null) { throw new XtepsException("action2 arg is null"); }
      return new BiCtxSC.Of<>(
        this.executor,
        this.hooks,
        this.executor.exec(this.hooks, () -> action1.apply(this.context1, this.context2)),
        this.executor.exec(this.hooks, () -> action2.apply(this.context1, this.context2))
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
      return new MemNoCtxSC.Of<>(this.executor, this.hooks, this);
    }

    @Override
    public final BiCtxSC<C1, C2> callChainHooks() {
      this.hooks.callHooks();
      return this;
    }

    @Override
    public final BiCtxSC<C1, C2> chainHook(final ThBiConsumer<? super C1, ? super C2, ?> action) {
      return this.chainHook(NORM_HOOK_PRIORITY, action);
    }

    @Override
    public final BiCtxSC<C1, C2> chainHook(final int priority,
                                           final ThBiConsumer<? super C1, ? super C2, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      checkPriorityArg(priority);
      this.hooks.addHook(priority, () -> action.accept(this.context1, this.context2));
      return this;
    }

    @Override
    public final BiCtxSC<C1, C2> testHook(final ThBiConsumer<? super C1, ? super C2, ?> action) {
      return this.testHook(NORM_HOOK_PRIORITY, action);
    }

    @Override
    public final BiCtxSC<C1, C2> testHook(final int priority,
                                          final ThBiConsumer<? super C1, ? super C2, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      checkPriorityArg(priority);
      currentTestHookContainer().addHook(priority, () -> action.accept(this.context1, this.context2));
      return this;
    }
  }
}
