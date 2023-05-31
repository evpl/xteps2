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
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link StepReporter}.
 */
final class StepReporterTest {

  @Test
  void ctorThrowsExceptionForNullExceptionHandlerArray() {
    assertThatCode(() -> new StepReporter.Default((ExceptionHandler) null, new StepListener[0]))
      .isInstanceOf(XtepsException.class);
  }

  @Test
  void ctorThrowsExceptionForNullStepListenerArray() {
    assertThatCode(() -> new StepReporter.Default(mock(ExceptionHandler.class), (StepListener[]) null))
      .isInstanceOf(XtepsException.class);
  }

  @Test
  void ctorDoesNotThrowExceptionForEmptyStepListenerArray() {
    assertThatCode(() -> new StepReporter.Default(mock(ExceptionHandler.class), new StepListener[0]))
      .doesNotThrowAnyException();
  }

  @Test
  void reportStep() {
    final StepListener stepListener1 = mock(StepListener.class);
    final StepListener stepListener2 = mock(StepListener.class);
    final ExceptionHandler handler = mock(ExceptionHandler.class);
    final StepReporter executor = new StepReporter.Default(handler, new StepListener[]{stepListener1, stepListener2});
    final Map<String, ?> artifacts = new HashMap<>();
    final Object expectedResult = new Object();

    final Object methodResult = executor.executeStep(artifacts, () -> expectedResult);
    assertThat(methodResult).isSameAs(expectedResult);
    verify(stepListener1).stepStarted(same(artifacts));
    verify(stepListener1).stepPassed();
    verify(stepListener2).stepStarted(same(artifacts));
    verify(stepListener2).stepPassed();
  }

  @Test
  void reportStepWithException() {
    final StepListener stepListener1 = mock(StepListener.class);
    final StepListener stepListener2 = mock(StepListener.class);
    final ExceptionHandler handler = mock(ExceptionHandler.class);
    final StepReporter executor = new StepReporter.Default(handler, new StepListener[]{stepListener1, stepListener2});
    final Map<String, ?> artifacts = new HashMap<>();
    final RuntimeException expectedException = new RuntimeException();

    assertThatCode(() -> executor.executeStep(artifacts, () -> { throw expectedException; }))
      .isSameAs(expectedException);
    verify(stepListener1).stepStarted(same(artifacts));
    verify(stepListener1).stepFailed(same(expectedException));
    verify(stepListener2).stepStarted(same(artifacts));
    verify(stepListener2).stepFailed(same(expectedException));
  }
}
