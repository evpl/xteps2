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

import com.plugatar.xteps2.core.chain.BiCtxSC;
import com.plugatar.xteps2.core.chain.CtxSC;
import com.plugatar.xteps2.core.chain.NoCtxSC;
import com.plugatar.xteps2.core.chain.TriCtxSC;

public final class StepChains {

  private StepChains() {
  }

  public static NoCtxSC stepChain() {
    return new NoCtxSC.Of();
  }

  public static <C> CtxSC<C> stepChain(final C context) {
    return new CtxSC.Of<>(context);
  }

  public static <C, C2> BiCtxSC<C, C2> stepChain(final C context,
                                                 final C2 context2) {
    return new BiCtxSC.Of<>(context, context2);
  }

  public static <C, C2, C3> TriCtxSC<C, C2, C3> stepChain(final C context,
                                                          final C2 context2,
                                                          final C3 context3) {
    return new TriCtxSC.Of<>(context, context2, context3);
  }
}
