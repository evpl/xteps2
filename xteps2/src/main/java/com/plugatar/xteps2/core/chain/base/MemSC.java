/*
 * Copyright 2023 Evgenii Plugatar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.plugatar.xteps2.core.chain.base;

/**
 * Memorizing step context.
 *
 * @param <P> the type of the previous step context
 * @param <F> the type of the non-memorizing step context
 */
public interface MemSC<P extends StepContext<?>, F extends StepContext<?>> {

  /**
   * Returns previous step context.
   *
   * @return previous step context
   */
  P previous();

  /**
   * Returns non-memorizing step context.
   *
   * @return non-memorizing step context
   */
  F forget();
}
