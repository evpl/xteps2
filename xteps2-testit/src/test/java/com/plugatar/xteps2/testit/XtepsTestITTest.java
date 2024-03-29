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
package com.plugatar.xteps2.testit;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * Tests for {@link XtepsTestIT}.
 */
final class XtepsTestITTest {

  @Test
  void stepStartedMethod() {
    assertThatCode(() -> new XtepsTestIT().stepStarted(new HashMap<>())).doesNotThrowAnyException();
  }

  @Test
  void stepPassedMethod() {
    assertThatCode(() -> new XtepsTestIT().stepPassed()).doesNotThrowAnyException();
  }

  @Test
  void stepFailedMethod() {
    assertThatCode(() -> new XtepsTestIT().stepFailed(new Throwable())).doesNotThrowAnyException();
  }
}
