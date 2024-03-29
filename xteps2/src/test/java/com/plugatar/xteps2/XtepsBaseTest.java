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
package com.plugatar.xteps2;

import com.plugatar.xteps2.core.function.ThSupplier;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * Tests for {@link XtepsBase}.
 */
final class XtepsBaseTest {

  @BeforeAll
  static void beforeAll() {
    System.setProperty("xteps.listener.list", "com.plugatar.xteps2.StaticStepListener");
  }

  @Test
  void passedStep() {
    final Map<String, Object> stepArtifacts = new HashMap<>();
    final Object stepResult = new Object();
    final ThSupplier<Object, RuntimeException> stepAction = () -> stepResult;

    XtepsBase.stepReporter().executeStep(stepArtifacts, stepAction);
    /* stepStarted method */
    assertThat(StaticStepListener.stepStartedArtifacts()).isSameAs(stepArtifacts);
    /* stepPassed method */
    StaticStepListener.clear();
  }

  @Test
  void failedStep() {
    final Map<String, Object> stepArtifacts = new HashMap<>();
    final RuntimeException stepException = new RuntimeException();
    final ThSupplier<Object, RuntimeException> stepAction = () -> { throw stepException; };

    assertThatCode(() -> XtepsBase.stepReporter().executeStep(stepArtifacts, stepAction))
      .isSameAs(stepException);
    /* stepStarted method */
    assertThat(StaticStepListener.stepStartedArtifacts()).isSameAs(stepArtifacts);
    /* stepFailed method */
    assertThat(StaticStepListener.stepFailedException()).isSameAs(stepException);
    StaticStepListener.clear();
  }
}
