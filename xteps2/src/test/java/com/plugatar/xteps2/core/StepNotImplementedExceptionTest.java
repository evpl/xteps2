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

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link StepNotImplementedException}.
 */
final class StepNotImplementedExceptionTest {

  @Test
  void emptyCtor() {
    final StepNotImplementedException exception = new StepNotImplementedException();

    assertThat(exception)
      .hasMessage(null)
      .hasCause(null);
  }

  @Test
  void messageCtor() {
    final String message = "message";
    final StepNotImplementedException exception = new StepNotImplementedException(message);

    assertThat(exception)
      .hasMessage(message)
      .hasCause(null);
  }

  @Test
  void causeCtor() {
    final Throwable cause = new RuntimeException("cause message");
    final StepNotImplementedException exception = new StepNotImplementedException(cause);

    assertThat(exception)
      .hasMessage("java.lang.RuntimeException: cause message")
      .hasCause(cause);
  }

  @Test
  void messageAndCauseCtor() {
    final String message = "message";
    final Throwable cause = new RuntimeException("cause message");
    final StepNotImplementedException exception = new StepNotImplementedException(message, cause);

    assertThat(exception)
      .hasMessage(message)
      .hasCause(cause);
  }
}
