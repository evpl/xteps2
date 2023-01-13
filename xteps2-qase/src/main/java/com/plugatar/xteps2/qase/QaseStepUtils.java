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
package com.plugatar.xteps2.qase;

import com.plugatar.xteps2.core.XtepsException;
import com.plugatar.xteps2.core.function.ThConsumer;
import com.plugatar.xteps2.core.function.ThFunction;
import io.qase.api.StepStorage;
import io.qase.client.model.ResultCreateStepsInner;

/**
 * Utility class. Qase step utils.
 */
public final class QaseStepUtils {

  /**
   * Utility class ctor.
   */
  private QaseStepUtils() {
  }

  /**
   * Updates the current step name.
   *
   * @param name the step name
   * @return step name
   */
  public static String stepName(final String name) {
    final ResultCreateStepsInner step = StepStorage.getCurrentStep();
    if (step != null) {
      StepStorage.getCurrentStep().action(name);
    }
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
    final ResultCreateStepsInner step = StepStorage.getCurrentStep();
    final String newName;
    if (step != null) {
      newName = ThFunction.unchecked(updateFunction).apply(step.getAction());
      step.action(newName);
    } else {
      newName = ThFunction.unchecked(updateFunction).apply(null);
    }
    return newName;
  }

  /**
   * Updates the current step description.
   *
   * @param desc the step description
   * @return step description
   */
  public static String stepDesc(final String desc) {
    final ResultCreateStepsInner step = StepStorage.getCurrentStep();
    if (step != null) {
      StepStorage.getCurrentStep().comment(desc);
    }
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
    final ResultCreateStepsInner step = StepStorage.getCurrentStep();
    final String newDesc;
    if (step != null) {
      newDesc = ThFunction.unchecked(updateFunction).apply(step.getComment());
      step.comment(newDesc);
    } else {
      newDesc = ThFunction.unchecked(updateFunction).apply(null);
    }
    return newDesc;
  }

  /**
   * Updates the current step.
   *
   * @param updateConsumer the update consumer
   * @throws XtepsException if {@code updateConsumer} is null
   */
  public static void updateStep(final ThConsumer<ResultCreateStepsInner, ?> updateConsumer) {
    if (updateConsumer == null) { throw new XtepsException("updateConsumer arg is null"); }
    ThConsumer.unchecked(updateConsumer).accept(StepStorage.getCurrentStep());
  }
}
