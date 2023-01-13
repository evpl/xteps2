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

/**
 * Tests for {@link ThTriConsumer}.
 */
final class ThTriConsumerTest {

  @Test
  void uncheckedMethodReturnsNullForNullArg() {
    assertThat(ThTriConsumer.unchecked(null)).isNull();
  }

  @Test
  void uncheckedMethodExceptionLambdaResult() throws Throwable {
    final Throwable throwable = new Throwable();
    @SuppressWarnings("unchecked")
    final ThTriConsumer<Object, Object, Object, Throwable> originConsumer = mock(ThTriConsumer.class);
    final Object consumerInput1 = new Object();
    final Object consumerInput2 = new Object();
    final Object consumerInput3 = new Object();
    doThrow(throwable).when(originConsumer).accept(same(consumerInput1), same(consumerInput2), same(consumerInput3));

    final ThTriConsumer<Object, Object, Object, RuntimeException> methodResult = ThTriConsumer.unchecked(originConsumer);
    assertThatCode(() -> methodResult.accept(consumerInput1, consumerInput2, consumerInput3))
      .isSameAs(throwable);
    verify(originConsumer, times(1)).accept(same(consumerInput1), same(consumerInput2), same(consumerInput3));
  }

  @Test
  void uncheckedMethodLambdaResult() throws Throwable {
    @SuppressWarnings("unchecked")
    final ThTriConsumer<Object, Object, Object, Throwable> originConsumer = mock(ThTriConsumer.class);
    final Object consumerInput1 = new Object();
    final Object consumerInput2 = new Object();
    final Object consumerInput3 = new Object();

    final ThTriConsumer<Object, Object, Object, RuntimeException> methodResult = ThTriConsumer.unchecked(originConsumer);
    methodResult.accept(consumerInput1, consumerInput2, consumerInput3);
    verify(originConsumer, times(1)).accept(same(consumerInput1), same(consumerInput2), same(consumerInput3));
  }
}
