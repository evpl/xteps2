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

import com.plugatar.xteps2.XtepsBase;
import com.plugatar.xteps2.core.XtepsException;
import com.plugatar.xteps2.core.function.ThConsumer;
import com.plugatar.xteps2.core.function.ThFunction;
import ru.testit.models.StepResult;
import ru.testit.services.Adapter;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Utility class. Test IT step utils.
 */
public final class TestITStepUtils {

  /**
   * Utility class ctor.
   */
  private TestITStepUtils() {
  }

  /**
   * Updates the current step name.
   *
   * @param name the step name
   * @return step name
   */
  public static String stepName(final String name) {
    Adapter.getAdapterManager().updateStep(stepResult -> stepResult.setName(name));
    return name;
  }

  /**
   * Updates the current step name.
   *
   * @param updateFunction the update function
   * @return step name
   * @throws XtepsException if {@code updateFunction} is null
   */
  public static String stepName(final ThFunction<String, String, ?> updateFunction) {
    if (updateFunction == null) { throw new XtepsException("updateFunction arg is null"); }
    final AtomicReference<String> newNameRef = new AtomicReference<>();
    Adapter.getAdapterManager().updateStep(stepResult -> {
      final String newName = ThFunction.unchecked(updateFunction).apply(stepResult.getName());
      newNameRef.set(newName);
      stepResult.setName(newName);
    });
    return newNameRef.get();
  }

  /**
   * Updates the current step description.
   *
   * @param desc the step description
   * @return step description
   */
  public static String stepDesc(final String desc) {
    Adapter.getAdapterManager().updateStep(stepResult -> stepResult.setDescription(desc));
    return desc;
  }

  /**
   * Updates the current step description.
   *
   * @param updateFunction the update function
   * @return step description
   * @throws XtepsException if {@code updateFunction} is null
   */
  public static String stepDesc(final ThFunction<String, String, ?> updateFunction) {
    if (updateFunction == null) { throw new XtepsException("updateFunction arg is null"); }
    final AtomicReference<String> newDescRef = new AtomicReference<>();
    Adapter.getAdapterManager().updateStep(stepResult -> {
      final String newDesc = ThFunction.unchecked(updateFunction).apply(stepResult.getDescription());
      newDescRef.set(newDesc);
      stepResult.setDescription(newDesc);
    });
    return newDescRef.get();
  }

  /**
   * Adds parameter to the current step.
   *
   * @param name  the name
   * @param value the value
   * @param <T>   the type of value
   * @return the value
   */
  public static <T> T stepParam(final String name,
                                final T value) {
    Adapter.getAdapterManager().updateStep(stepResult ->
      stepResult.getParameters().put(name, XtepsBase.textFormatter().asString(value)));
    return value;
  }

  /**
   * Updates the current step.
   *
   * @param updateConsumer the update consumer
   * @throws XtepsException if {@code updateConsumer} is null
   */
  public static void updateStep(final ThConsumer<StepResult, ?> updateConsumer) {
    if (updateConsumer == null) { throw new XtepsException("updateConsumer arg is null"); }
    Adapter.getAdapterManager().updateStep(stepResult -> ThConsumer.unchecked(updateConsumer).accept(stepResult));
  }
}
