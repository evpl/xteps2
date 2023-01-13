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

import com.plugatar.xteps2.core.XtepsException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThatCode;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(XtepsJUnit5.class)
final class BeforeAfterTest {

  @BeforeAll
  void beforeAll() {
    final XtepsJUnit5 extension = new XtepsJUnit5();
    assertThatCode(() -> extension.addHook(5, () -> { })).isInstanceOf(XtepsException.class);
  }

  @AfterAll
  void afterAll() {
    final XtepsJUnit5 extension = new XtepsJUnit5();
    assertThatCode(() -> extension.addHook(5, () -> { })).isInstanceOf(XtepsException.class);
  }

  @BeforeEach
  void beforeEach() {
    final XtepsJUnit5 extension = new XtepsJUnit5();
    assertThatCode(() -> extension.addHook(5, () -> { })).isInstanceOf(XtepsException.class);
  }

  @AfterEach
  void afterEach() {
    final XtepsJUnit5 extension = new XtepsJUnit5();
    assertThatCode(() -> extension.addHook(5, () -> { })).isInstanceOf(XtepsException.class);
  }

  @Test
  void test() {
    final XtepsJUnit5 extension = new XtepsJUnit5();
    assertThatCode(() -> extension.addHook(5, () -> { })).doesNotThrowAnyException();
  }
}
