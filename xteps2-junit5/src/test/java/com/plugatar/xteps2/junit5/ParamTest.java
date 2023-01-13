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

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(XtepsJUnit5.class)
final class ParamTest {
  final Map<Integer, List<Integer>> hooksInvocations = new HashMap<>();

  {
    hooksInvocations.put(1, new ArrayList<>());
    hooksInvocations.put(2, new ArrayList<>());
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 2, 3})
  void test(final int arg) {
    if (arg != 3) {
      new XtepsJUnit5().addHook(5, () -> this.hooksInvocations.get(arg).add(1));
      new XtepsJUnit5().addHook(5, () -> this.hooksInvocations.get(arg).add(2));
      new XtepsJUnit5().addHook(9, () -> this.hooksInvocations.get(arg).add(3));
      new XtepsJUnit5().addHook(9, () -> this.hooksInvocations.get(arg).add(4));
      new XtepsJUnit5().addHook(9, () -> this.hooksInvocations.get(arg).add(5));
      new XtepsJUnit5().addHook(3, () -> this.hooksInvocations.get(arg).add(6));
    }
    if (arg != 1) {
      assertThat(hooksInvocations.get(arg - 1)).containsExactly(3, 4, 5, 1, 2, 6);
    }
  }
}
