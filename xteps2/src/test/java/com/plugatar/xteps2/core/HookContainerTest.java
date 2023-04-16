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
package com.plugatar.xteps2.core;

import com.plugatar.xteps2.core.function.ThRunnable;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.plugatar.xteps2.core.HookPriority.MAX_HOOK_PRIORITY;
import static com.plugatar.xteps2.core.HookPriority.MIN_HOOK_PRIORITY;
import static com.plugatar.xteps2.core.HookPriority.NORM_HOOK_PRIORITY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * Tests for {@link HookContainer}.
 */
final class HookContainerTest {

  @Test
  void nullArgExceptionForAddMethod() {
    final HookContainer container = new HookContainer.Default();

    assertThatCode(() -> container.addHook(NORM_HOOK_PRIORITY, (ThRunnable<?>) null))
      .isInstanceOf(XtepsException.class);
  }

  @Test
  void illegalArgExceptionForAddMethod() {
    final HookContainer container = new HookContainer.Default();

    assertThatCode(() -> container.addHook(MIN_HOOK_PRIORITY - 1, () -> { }))
      .isInstanceOf(XtepsException.class);
    assertThatCode(() -> container.addHook(MAX_HOOK_PRIORITY + 1, () -> { }))
      .isInstanceOf(XtepsException.class);
  }

  @Test
  void nullArgExceptionForCallHooksMethod() {
    final HookContainer container = new HookContainer.Default();

    assertThatCode(() -> container.callHooks((Throwable) null))
      .isInstanceOf(XtepsException.class);
  }

  @Test
  void callHooksMethod() {
    final HookContainer container = new HookContainer.Default();
    final List<String> executionLog = new ArrayList<>();
    final ThRunnable<RuntimeException> hook1 = () -> executionLog.add("hook1");
    final ThRunnable<RuntimeException> hook2 = () -> executionLog.add("hook2");
    final ThRunnable<RuntimeException> hook3 = () -> executionLog.add("hook3");
    final ThRunnable<RuntimeException> hook4 = () -> executionLog.add("hook4");
    container.addHook(MIN_HOOK_PRIORITY, hook1);
    container.addHook(NORM_HOOK_PRIORITY, hook2);
    container.addHook(MAX_HOOK_PRIORITY, hook3);
    container.addHook(NORM_HOOK_PRIORITY, hook4);

    container.callHooks();
    assertThat(executionLog).isEqualTo(Arrays.asList("hook3", "hook2", "hook4", "hook1"));
  }

  @Test
  void callHooksMethodForException() {
    final HookContainer container = new HookContainer.Default();
    final Exception exception1 = new Exception("exception 1");
    final ThRunnable<?> hook1 = () -> { throw exception1; };
    final Exception exception2 = new Exception("exception 2");
    final ThRunnable<?> hook2 = () -> { throw exception2; };
    container.addHook(NORM_HOOK_PRIORITY, hook1);
    container.addHook(NORM_HOOK_PRIORITY, hook2);

    assertThatCode(() -> container.callHooks())
      .isSameAs(exception1)
      .hasSuppressedException(exception2);
  }

  @Test
  void callHooksMethodWithBaseExceptionForException() {
    final HookContainer container = new HookContainer.Default();
    final Exception exception1 = new Exception("exception 1");
    final ThRunnable<?> hook1 = () -> { throw exception1; };
    final Exception exception2 = new Exception("exception 2");
    final ThRunnable<?> hook2 = () -> { throw exception2; };
    final RuntimeException baseException = new RuntimeException();
    container.addHook(NORM_HOOK_PRIORITY, hook1);
    container.addHook(NORM_HOOK_PRIORITY, hook2);

    container.callHooks(baseException);
    assertThat(baseException)
      .hasSuppressedException(exception1)
      .hasSuppressedException(exception2);
  }
}
