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
package com.plugatar.xteps2;

import com.plugatar.xteps2.core.XtepsException;
import com.plugatar.xteps2.core.chain.BiCtxSC;
import com.plugatar.xteps2.core.chain.CtxSC;
import com.plugatar.xteps2.core.chain.NoCtxSC;
import com.plugatar.xteps2.core.chain.TriCtxSC;

/**
 * Utility class. Contains methods for creating step contexts.
 * <p>
 * Methods:
 * <ul>
 * <li>{@link #stepContext()}</li>
 * <li>{@link #stepContext(Object)}</li>
 * <li>{@link #stepContext(Object, Object)}</li>
 * <li>{@link #stepContext(Object, Object, Object)}</li>
 * </ul>
 */
public final class StepContexts {

  /**
   * Utility class ctor.
   */
  private StepContexts() {
  }

  /**
   * Returns no context step context.
   *
   * @return no context step context
   * @throws XtepsException if Xteps configuration is incorrect
   */
  public static NoCtxSC stepContext() {
    return NoCtxSC.instance();
  }

  /**
   * Returns single context step context.
   *
   * @param context the context
   * @param <C>     the type of the context
   * @return context step context
   * @throws XtepsException if Xteps configuration is incorrect
   */
  public static <C> CtxSC<C> stepContext(final C context) {
    return new CtxSC.Of<>(context);
  }

  /**
   * Returns bi context step context.
   *
   * @param context1 the first context
   * @param context2 the second context
   * @param <C1>     the type of the first context
   * @param <C2>     the type of the second context
   * @return bi context step context
   * @throws XtepsException if Xteps configuration is incorrect
   */
  public static <C1, C2> BiCtxSC<C1, C2> stepContext(final C1 context1,
                                                     final C2 context2) {
    return new BiCtxSC.Of<>(context1, context2);
  }

  /**
   * Returns tri context step context.
   *
   * @param context1 the first context
   * @param context2 the second context
   * @param context3 the third context
   * @param <C1>     the type of the first context
   * @param <C2>     the type of the second context
   * @param <C3>     the type of the third context
   * @return tri context step context
   * @throws XtepsException if Xteps configuration is incorrect
   */
  public static <C1, C2, C3> TriCtxSC<C1, C2, C3> stepContext(final C1 context1,
                                                              final C2 context2,
                                                              final C3 context3) {
    return new TriCtxSC.Of<>(context1, context2, context3);
  }
}
