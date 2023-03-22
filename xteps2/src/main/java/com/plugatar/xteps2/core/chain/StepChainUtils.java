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

import com.plugatar.xteps2.XtepsBase;
import com.plugatar.xteps2.core.HookContainer;
import com.plugatar.xteps2.core.StepExecutor;
import com.plugatar.xteps2.core.TestHookContainer;
import com.plugatar.xteps2.core.XtepsException;

import static com.plugatar.xteps2.core.HookPriority.MAX_HOOK_PRIORITY;
import static com.plugatar.xteps2.core.HookPriority.MIN_HOOK_PRIORITY;

final class StepChainUtils {

  private StepChainUtils() {
  }

  static StepExecutor currentStepExecutor() {
    return XtepsBase.stepExecutor();
  }

  static TestHookContainer currentTestHookContainer() {
    return XtepsBase.testHookContainer();
  }

  static HookContainer newChainHookContainer() {
    return new HookContainer.Of();
  }

  static void correctPriorityArg(final int priority) {
    if (priority < MIN_HOOK_PRIORITY || priority > MAX_HOOK_PRIORITY) {
      throw new XtepsException("priority arg not in the range " + MIN_HOOK_PRIORITY + " to " + MAX_HOOK_PRIORITY);
    }
  }

  static <R> R notNullArg(final String argName,
                          final R obj) {
    if (obj == null) {
      throw new XtepsException(argName + " is null");
    }
    return obj;
  }
}
