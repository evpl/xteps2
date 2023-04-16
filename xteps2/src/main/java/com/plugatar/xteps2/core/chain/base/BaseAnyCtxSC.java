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
package com.plugatar.xteps2.core.chain.base;

import com.plugatar.xteps2.core.chain.MemNoCtxSC;

/**
 * Base any context step chain.
 *
 * @param <S> the type of the step chain implementing {@code BaseAnyCtxSC}
 */
public interface BaseAnyCtxSC<S extends BaseAnyCtxSC<S>> extends StepChain<S> {

  /**
   * Returns no context step chain.
   *
   * @return no context step chain
   */
  MemNoCtxSC<S> noContext();
}
