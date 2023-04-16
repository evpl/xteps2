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
import com.plugatar.xteps2.core.chain.base.BaseTriCtxSC;
import com.plugatar.xteps2.core.chain.base.MemSC;
import com.plugatar.xteps2.core.chain.base.StepChain;
import com.plugatar.xteps2.core.function.ThConsumer;
import com.plugatar.xteps2.core.function.ThFunction;
import com.plugatar.xteps2.core.function.ThTriConsumer;
import com.plugatar.xteps2.core.function.ThTriFunction;

import static com.plugatar.xteps2.core.HookPriority.NORM_HOOK_PRIORITY;
import static com.plugatar.xteps2.core.chain.StepChainUtils.checkPriorityArg;
import static com.plugatar.xteps2.core.chain.StepChainUtils.currentStepExecutor;
import static com.plugatar.xteps2.core.chain.StepChainUtils.currentTestHookContainer;
import static com.plugatar.xteps2.core.chain.StepChainUtils.newChainHookContainer;

/**
 * Memorizing tri context step chain.
 *
 * @param <C1> the type of the first context
 * @param <C2> the type of the second context
 * @param <C3> the type of the third context
 * @param <P>  the type of the previous step chain
 */
public interface MemTriCtxSC<C1, C2, C3, P extends StepChain<?>> extends
  BaseTriCtxSC<C1, C2, C3, MemTriCtxSC<C1, C2, C3, P>>,
  MemSC<P, TriCtxSC<C1, C2, C3>> {

  @Override
  <R> MemTriCtxSC<R, C1, C2, MemTriCtxSC<C1, C2, C3, P>> with(ThTriFunction<? super C1, ? super C2, ? super C3, ? extends R, ?> action);

  @Override
  <R1, R2, R3> MemTriCtxSC<R1, R2, R3, P> map(final ThTriFunction<? super C1, ? super C2, ? super C3, ? extends R1, ?> action1,
                                              final ThTriFunction<? super C1, ? super C2, ? super C3, ? extends R2, ?> action2,
                                              final ThTriFunction<? super C1, ? super C2, ? super C3, ? extends R3, ?> action3);

  /**
   * Default {@code MemTriCtxSC} implementation.
   *
   * @param <C1> the type of the first context
   * @param <C2> the type of the second context
   * @param <C3> the type of the third context
   * @param <P>  the type of the previous step chain
   */
  class Of<C1, C2, C3, P extends StepChain<?>> implements MemTriCtxSC<C1, C2, C3, P> {
    private final StepExecutor executor;
    private final HookContainer hooks;
    private final C1 context1;
    private final C2 context2;
    private final C3 context3;
    private final P previous;

    /**
     * Ctor.
     *
     * @param context1 the first context
     * @param context2 the second context
     * @param context3 the third context
     * @param previous the previous step chain
     * @throws XtepsException if Xteps configuration is incorrect
     */
    public Of(final C1 context1,
              final C2 context2,
              final C3 context3,
              final P previous) {
      this(currentStepExecutor(), newChainHookContainer(), context1, context2, context3, previous);
    }

    /**
     * Ctor.
     *
     * @param executor the step executor
     * @param hooks    the hooks container
     * @param context1 the first context
     * @param context2 the second context
     * @param context3 the third context
     * @param previous the previous step chain
     */
    public Of(final StepExecutor executor,
              final HookContainer hooks,
              final C1 context1,
              final C2 context2,
              final C3 context3,
              final P previous) {
      if (executor == null) { throw new XtepsException("executor arg is null"); }
      if (hooks == null) { throw new XtepsException("hooks arg is null"); }
      this.executor = executor;
      this.hooks = hooks;
      this.context1 = context1;
      this.context2 = context2;
      this.context3 = context3;
      this.previous = previous;
    }

    @Override
    public final MemTriCtxSC<C1, C2, C3, P> next(final ThTriConsumer<? super C1, ? super C2, ? super C3, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      this.executor.exec(this.hooks, () -> {
        action.accept(this.context1, this.context2, this.context3);
        return null;
      });
      return this;
    }

    @Override
    public final <R> MemTriCtxSC<R, C1, C2, MemTriCtxSC<C1, C2, C3, P>> with(final ThTriFunction<? super C1, ? super C2, ? super C3, ? extends R, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      return new MemTriCtxSC.Of<>(this.executor, this.hooks, this.executor.exec(this.hooks, () -> action.apply(this.context1, this.context2, this.context3)),
        this.context1, this.context2, this);
    }

    @Override
    public final <R> R res(final ThTriFunction<? super C1, ? super C2, ? super C3, ? extends R, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      return this.executor.exec(this.hooks, () -> action.apply(this.context1, this.context2, this.context3));
    }

    @Override
    public final MemTriCtxSC<C1, C2, C3, P> chain(final ThConsumer<? super MemTriCtxSC<C1, C2, C3, P>, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      this.executor.exec(this.hooks, () -> {
        action.accept(this);
        return null;
      });
      return this;
    }

    @Override
    public final <R> R chainRes(final ThFunction<? super MemTriCtxSC<C1, C2, C3, P>, ? extends R, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      return this.executor.exec(this.hooks, () -> action.apply(this));
    }

    @Override
    public final <R1, R2, R3> MemTriCtxSC<R1, R2, R3, P> map(final ThTriFunction<? super C1, ? super C2, ? super C3, ? extends R1, ?> action1,
                                                             final ThTriFunction<? super C1, ? super C2, ? super C3, ? extends R2, ?> action2,
                                                             final ThTriFunction<? super C1, ? super C2, ? super C3, ? extends R3, ?> action3) {
      if (action1 == null) { throw new XtepsException("action1 arg is null"); }
      if (action2 == null) { throw new XtepsException("action2 arg is null"); }
      if (action3 == null) { throw new XtepsException("action3 arg is null"); }
      return new MemTriCtxSC.Of<>(
        this.executor,
        this.hooks,
        this.executor.exec(this.hooks, () -> action1.apply(this.context1, this.context2, this.context3)),
        this.executor.exec(this.hooks, () -> action2.apply(this.context1, this.context2, this.context3)),
        this.executor.exec(this.hooks, () -> action3.apply(this.context1, this.context2, this.context3)),
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
    public final C3 context3() {
      return this.context3;
    }

    @Override
    public final MemNoCtxSC<MemTriCtxSC<C1, C2, C3, P>> noContext() {
      return new MemNoCtxSC.Of<>(this.executor, this.hooks, this);
    }

    @Override
    public final P previous() {
      return this.previous;
    }

    @Override
    public final TriCtxSC<C1, C2, C3> forget() {
      return new TriCtxSC.Of<>(this.executor, this.hooks, this.context1, this.context2, this.context3);
    }

    @Override
    public final MemTriCtxSC<C1, C2, C3, P> callChainHooks() {
      this.hooks.callHooks();
      return this;
    }

    @Override
    public final MemTriCtxSC<C1, C2, C3, P> chainHook(final ThTriConsumer<? super C1, ? super C2, ? super C3, ?> action) {
      return this.chainHook(NORM_HOOK_PRIORITY, action);
    }

    @Override
    public final MemTriCtxSC<C1, C2, C3, P> chainHook(final int priority,
                                                      final ThTriConsumer<? super C1, ? super C2, ? super C3, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      checkPriorityArg(priority);
      this.hooks.addHook(priority, () -> action.accept(this.context1, this.context2, this.context3));
      return this;
    }

    @Override
    public final MemTriCtxSC<C1, C2, C3, P> testHook(final ThTriConsumer<? super C1, ? super C2, ? super C3, ?> action) {
      return this.testHook(NORM_HOOK_PRIORITY, action);
    }

    @Override
    public final MemTriCtxSC<C1, C2, C3, P> testHook(final int priority,
                                                     final ThTriConsumer<? super C1, ? super C2, ? super C3, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      checkPriorityArg(priority);
      currentTestHookContainer().addHook(priority, () -> action.accept(this.context1, this.context2, this.context3));
      return this;
    }
  }
}
