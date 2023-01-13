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
package com.plugatar.xteps2.testng;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Listeners({CatchExceptionListener.class, XtepsTestNG.class})
public final class FailedTestWithSuccessHooksTest {
  final List<Integer> hooksInvocations = new ArrayList<>();
  final RuntimeException expectedException = new CatchExceptionListener.Exception("test exception");

  @Test(priority = 1)
  void hooks() {
    new XtepsTestNG().addHook(5, () -> this.hooksInvocations.add(1));
    new XtepsTestNG().addHook(5, () -> this.hooksInvocations.add(2));
    new XtepsTestNG().addHook(9, () -> this.hooksInvocations.add(3));
    new XtepsTestNG().addHook(9, () -> this.hooksInvocations.add(4));
    new XtepsTestNG().addHook(9, () -> this.hooksInvocations.add(5));
    new XtepsTestNG().addHook(3, () -> this.hooksInvocations.add(6));
    throw this.expectedException;
  }

  @Test(priority = 2)
  void check() {
    assertThat(this.hooksInvocations).containsExactly(3, 4, 5, 1, 2, 6);
    assertThat(CatchExceptionListener.lastException())
      .isSameAs(this.expectedException)
      .hasMessage("test exception")
      .hasNoCause()
      .hasNoSuppressedExceptions();
  }
}
