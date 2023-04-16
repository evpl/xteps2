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
import com.plugatar.xteps2.core.chain.base.BaseNoCtxSC;
import com.plugatar.xteps2.core.chain.base.MemSC;
import com.plugatar.xteps2.core.chain.base.StepChain;
import com.plugatar.xteps2.core.function.ThConsumer;
import com.plugatar.xteps2.core.function.ThFunction;
import com.plugatar.xteps2.core.function.ThRunnable;
import com.plugatar.xteps2.core.function.ThSupplier;

import static com.plugatar.xteps2.core.HookPriority.NORM_HOOK_PRIORITY;
import static com.plugatar.xteps2.core.chain.StepChainUtils.checkPriorityArg;
import static com.plugatar.xteps2.core.chain.StepChainUtils.currentStepExecutor;
import static com.plugatar.xteps2.core.chain.StepChainUtils.currentTestHookContainer;
import static com.plugatar.xteps2.core.chain.StepChainUtils.newChainHookContainer;

/**
 * Memorizing no context step chain.
 *
 * @param <P> the type of the previous step chain
 */
public interface MemNoCtxSC<P extends StepChain<?>> extends
  BaseNoCtxSC<MemNoCtxSC<P>>,
  MemSC<P, NoCtxSC> {

  @Override
  <R> MemCtxSC<R, P> with(ThSupplier<? extends R, ?> action);

  /**
   * Default {@code MemNoCtxSC} implementation.
   *
   * @param <P> the type of the previous step chain
   */
  class Of<P extends StepChain<?>> implements MemNoCtxSC<P> {
    private final StepExecutor executor;
    private final HookContainer hooks;
    private final P previous;

    /**
     * Ctor.
     *
     * @param previous the previous step chain
     * @throws XtepsException if Xteps configuration is incorrect
     */
    public Of(final P previous) {
      this(currentStepExecutor(), newChainHookContainer(), previous);
    }

    /**
     * Ctor.
     *
     * @param executor the step executor
     * @param hooks    the hooks container
     * @param previous the previous step chain
     */
    public Of(final StepExecutor executor,
              final HookContainer hooks,
              final P previous) {
      if (executor == null) { throw new XtepsException("executor arg is null"); }
      if (hooks == null) { throw new XtepsException("hooks arg is null"); }
      this.executor = executor;
      this.hooks = hooks;
      this.previous = previous;
    }

    @Override
    public final MemNoCtxSC<P> next(final ThRunnable<?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      this.executor.exec(this.hooks, () -> {
        action.run();
        return null;
      });
      return this;
    }

    @Override
    public final <R> MemCtxSC<R, P> with(final ThSupplier<? extends R, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      return new MemCtxSC.Of<>(this.executor, this.hooks, this.executor.exec(this.hooks, action), this.previous);
    }

    @Override
    public final <R> R res(final ThSupplier<? extends R, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      return this.executor.exec(this.hooks, action);
    }

    @Override
    public final MemNoCtxSC<P> chain(final ThConsumer<? super MemNoCtxSC<P>, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      this.executor.exec(this.hooks, () -> {
        action.accept(this);
        return null;
      });
      return this;
    }

    @Override
    public final <R> R chainRes(final ThFunction<? super MemNoCtxSC<P>, ? extends R, ?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      return this.executor.exec(this.hooks, () -> action.apply(this));
    }

    @Override
    public final P previous() {
      return this.previous;
    }

    @Override
    public final NoCtxSC forget() {
      return new NoCtxSC.Of(this.executor, this.hooks);
    }

    @Override
    public final MemNoCtxSC<P> callChainHooks() {
      this.hooks.callHooks();
      return this;
    }

    @Override
    public final MemNoCtxSC<P> chainHook(final ThRunnable<?> action) {
      return this.chainHook(NORM_HOOK_PRIORITY, action);
    }

    @Override
    public final MemNoCtxSC<P> chainHook(final int priority,
                                         final ThRunnable<?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      checkPriorityArg(priority);
      this.hooks.addHook(priority, action);
      return this;
    }

    @Override
    public final MemNoCtxSC<P> testHook(final ThRunnable<?> action) {
      return this.testHook(NORM_HOOK_PRIORITY, action);
    }

    @Override
    public final MemNoCtxSC<P> testHook(final int priority,
                                        final ThRunnable<?> action) {
      if (action == null) { throw new XtepsException("action arg is null"); }
      checkPriorityArg(priority);
      currentTestHookContainer().addHook(priority, action);
      return this;
    }
  }
}
