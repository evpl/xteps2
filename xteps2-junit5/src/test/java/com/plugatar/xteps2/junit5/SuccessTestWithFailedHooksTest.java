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
package com.plugatar.xteps2.junit5;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(XtepsJUnit5.class)
final class SuccessTestWithFailedHooksTest {
  @RegisterExtension final CatchExceptionExtension extension = new CatchExceptionExtension();
  final List<Integer> hooksInvocations = new ArrayList<>();
  final RuntimeException hook2Exception = new CatchExceptionExtension.Exception("hook2 exception");
  final RuntimeException hook4Exception = new CatchExceptionExtension.Exception("hook4 exception");

  @Test
  @Order(1)
  void hooks() {
    new XtepsJUnit5().addHook(5, () -> this.hooksInvocations.add(1));
    new XtepsJUnit5().addHook(5, () -> {
      this.hooksInvocations.add(2);
      throw hook2Exception;
    });
    new XtepsJUnit5().addHook(9, () -> this.hooksInvocations.add(3));
    new XtepsJUnit5().addHook(9, () -> {
      this.hooksInvocations.add(4);
      throw hook4Exception;
    });
    new XtepsJUnit5().addHook(9, () -> this.hooksInvocations.add(5));
    new XtepsJUnit5().addHook(3, () -> this.hooksInvocations.add(6));
  }

  @Test
  @Order(2)
  void check() {
    assertThat(this.hooksInvocations).containsExactly(3, 4, 5, 1, 2, 6);
    final Throwable testEx = this.extension.exception();
    assertThat(testEx)
      .isSameAs(this.hook4Exception)
      .hasMessage("hook4 exception")
      .hasNoCause();
    assertThat(testEx.getSuppressed()).containsExactly(this.hook2Exception);
  }
}
