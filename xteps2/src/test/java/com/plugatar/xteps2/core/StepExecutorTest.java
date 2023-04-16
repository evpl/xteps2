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

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

/**
 * Tests for {@link StepExecutor}.
 */
final class StepExecutorTest {

  @Test
  void ctorThrowsExceptionForNullExceptionHandlerArray() {
    assertThatCode(() -> new StepExecutor.Default((ExceptionHandler) null, new StepListener[0]))
      .isInstanceOf(XtepsException.class);
  }

  @Test
  void ctorThrowsExceptionForNullStepListenerArray() {
    assertThatCode(() -> new StepExecutor.Default(mock(ExceptionHandler.class), (StepListener[]) null))
      .isInstanceOf(XtepsException.class);
  }

  @Test
  void ctorDoesNotThrowExceptionForEmptyStepListenerArray() {
    assertThatCode(() -> new StepExecutor.Default(mock(ExceptionHandler.class), new StepListener[0]))
      .doesNotThrowAnyException();
  }

  @Test
  void execMethod() {
    final StepListener stepListener1 = mock(StepListener.class);
    final StepListener stepListener2 = mock(StepListener.class);
    final ExceptionHandler handler = mock(ExceptionHandler.class);
    final StepExecutor executor = new StepExecutor.Default(handler, new StepListener[]{stepListener1, stepListener2});
    final HookContainer container = mock(HookContainer.class);
    final Object expectedResult = new Object();

    final Object methodResult = executor.exec(container, () -> expectedResult);
    assertThat(methodResult).isSameAs(expectedResult);
    verifyNoInteractions(stepListener1);
    verifyNoInteractions(stepListener2);
    verifyNoInteractions(container);
  }

  @Test
  void execMethodWithException() {
    final StepListener stepListener1 = mock(StepListener.class);
    final StepListener stepListener2 = mock(StepListener.class);
    final ExceptionHandler handler = mock(ExceptionHandler.class);
    final StepExecutor executor = new StepExecutor.Default(handler, new StepListener[]{stepListener1, stepListener2});
    final HookContainer container = mock(HookContainer.class);
    final RuntimeException expectedException = new RuntimeException("expectedException");

    assertThatCode(() -> executor.exec(container, () -> { throw expectedException; }))
      .isSameAs(expectedException);
    verify(container).callHooks(same(expectedException));
    verifyNoInteractions(stepListener1);
    verifyNoInteractions(stepListener2);
  }

  @Test
  void reportStep() {
    final StepListener stepListener1 = mock(StepListener.class);
    final StepListener stepListener2 = mock(StepListener.class);
    final ExceptionHandler handler = mock(ExceptionHandler.class);
    final StepExecutor executor = new StepExecutor.Default(handler, new StepListener[]{stepListener1, stepListener2});
    final Map<String, ?> artifacts = new HashMap<>();
    final Object expectedResult = new Object();

    final Object methodResult = executor.report(artifacts, () -> expectedResult);
    assertThat(methodResult).isSameAs(expectedResult);
    verify(stepListener1).stepStarted(any(), same(artifacts));
    verify(stepListener1).stepPassed(any());
    verify(stepListener2).stepStarted(any(), same(artifacts));
    verify(stepListener2).stepPassed(any());
  }

  @Test
  void reportStepWithException() {
    final StepListener stepListener1 = mock(StepListener.class);
    final StepListener stepListener2 = mock(StepListener.class);
    final ExceptionHandler handler = mock(ExceptionHandler.class);
    final StepExecutor executor = new StepExecutor.Default(handler, new StepListener[]{stepListener1, stepListener2});
    final Map<String, ?> artifacts = new HashMap<>();
    final RuntimeException expectedException = new RuntimeException();

    assertThatCode(() -> executor.report(artifacts, () -> { throw expectedException; }))
      .isSameAs(expectedException);
    verify(stepListener1).stepStarted(any(), same(artifacts));
    verify(stepListener1).stepFailed(any(), same(expectedException));
    verify(stepListener2).stepStarted(any(), same(artifacts));
    verify(stepListener2).stepFailed(any(), same(expectedException));
  }
}
