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

import com.plugatar.xteps2.core.HookPriority;
import com.plugatar.xteps2.core.XtepsException;
import com.plugatar.xteps2.core.function.ThRunnable;

/**
 * Utility class. Test hooks.
 * <p>
 * Methods:
 * <ul>
 * <li>{@link #testHook(ThRunnable)}</li>
 * <li>{@link #testHook(int, ThRunnable)}</li>
 * </ul>
 */
public final class TestHooks {

  /**
   * Utility class ctor.
   */
  private TestHooks() {
  }

  /**
   * Adds given hook for the current test.
   *
   * @param action the action
   * @throws XtepsException if {@code TestHookContainer} implementation not found
   *                        or if current test not found
   *                        or if {@code action} is null
   */
  public static void testHook(final ThRunnable<?> action) {
    XtepsBase.CONFIG.get().testHookContainer.addHook(HookPriority.NORM_HOOK_PRIORITY, action);
  }

  /**
   * Adds given hook with given priority for the current test.
   *
   * @param priority the priority
   * @param action   the action
   * @throws XtepsException if {@code TestHookContainer} implementation not found
   *                        or if current test not found
   *                        or if {@code action} is null
   *                        or if {@code priority} is not in the range {@link HookPriority#MIN_HOOK_PRIORITY} to
   *                        {@link HookPriority#MAX_HOOK_PRIORITY}
   */
  public static void testHook(final int priority,
                              final ThRunnable<?> action) {
    XtepsBase.CONFIG.get().testHookContainer.addHook(priority, action);
  }
}
