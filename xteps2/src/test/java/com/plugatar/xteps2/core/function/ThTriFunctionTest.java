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
 * Tests for {@link ThTriFunction}.
 */
final class ThTriFunctionTest {

  @Test
  void uncheckedMethodReturnsNullForNullArg() {
    assertThat(ThTriFunction.unchecked(null)).isNull();
  }

  @Test
  void uncheckedMethodExceptionLambdaResult() throws Throwable {
    final Throwable throwable = new Throwable();
    @SuppressWarnings("unchecked")
    final ThTriFunction<Object, Object, Object, Object, Throwable> originFunction = mock(ThTriFunction.class);
    final Object functionInput1 = new Object();
    final Object functionInput2 = new Object();
    final Object functionInput3 = new Object();
    doThrow(throwable).when(originFunction).apply(same(functionInput1), same(functionInput2), same(functionInput3));

    final ThTriFunction<Object, Object, Object, Object, RuntimeException> methodResult = ThTriFunction.unchecked(originFunction);
    assertThatCode(() -> methodResult.apply(functionInput1, functionInput2, functionInput3))
      .isSameAs(throwable);
    verify(originFunction, times(1)).apply(same(functionInput1), same(functionInput2), same(functionInput3));
  }

  @Test
  void uncheckedMethodLambdaResult() throws Throwable {
    @SuppressWarnings("unchecked")
    final ThTriFunction<Object, Object, Object, Object, Throwable> originFunction = mock(ThTriFunction.class);
    final Object functionInput1 = new Object();
    final Object functionInput2 = new Object();
    final Object functionInput3 = new Object();
    final Object functionResult = new Object();
    when(originFunction.apply(functionInput1, functionInput2, functionInput3)).thenReturn(functionResult);

    final ThTriFunction<Object, Object, Object, Object, RuntimeException> methodResult = ThTriFunction.unchecked(originFunction);
    assertThat(methodResult.apply(functionInput1, functionInput2, functionInput3)).isSameAs(functionResult);
    verify(originFunction, times(1)).apply(same(functionInput1), same(functionInput2), same(functionInput3));
  }
}
