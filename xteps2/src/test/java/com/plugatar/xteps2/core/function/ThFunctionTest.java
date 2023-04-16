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
package com.plugatar.xteps2.core.function;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link ThFunction}.
 */
final class ThFunctionTest {

  @Test
  void uncheckedMethodReturnsNullForNullArg() {
    assertThat(ThFunction.unchecked(null)).isNull();
  }

  @Test
  void uncheckedMethodExceptionLambdaResult() throws Throwable {
    final Throwable throwable = new Throwable();
    @SuppressWarnings("unchecked")
    final ThFunction<Object, Object, Throwable> originFunction = mock(ThFunction.class);
    final Object functionInput = new Object();
    doThrow(throwable).when(originFunction).apply(same(functionInput));

    final ThFunction<Object, Object, RuntimeException> methodResult = ThFunction.unchecked(originFunction);
    assertThatCode(() -> methodResult.apply(functionInput))
      .isSameAs(throwable);
    verify(originFunction, times(1)).apply(same(functionInput));
  }

  @Test
  void uncheckedMethodLambdaResult() throws Throwable {
    @SuppressWarnings("unchecked")
    final ThFunction<Object, Object, Throwable> originFunction = mock(ThFunction.class);
    final Object functionInput = new Object();
    final Object functionResult = new Object();
    when(originFunction.apply(functionInput)).thenReturn(functionResult);

    final ThFunction<Object, Object, RuntimeException> methodResult = ThFunction.unchecked(originFunction);
    assertThat(methodResult.apply(functionInput)).isSameAs(functionResult);
    verify(originFunction, times(1)).apply(same(functionInput));
  }
}
