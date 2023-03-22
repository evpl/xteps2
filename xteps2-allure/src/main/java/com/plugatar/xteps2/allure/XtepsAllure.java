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
package com.plugatar.xteps2.allure;

import com.plugatar.xteps2.core.Keyword;
import com.plugatar.xteps2.core.StepListener;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.Parameter;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;
import io.qameta.allure.util.ResultsUtils;

import java.util.List;
import java.util.Map;

/**
 * {@link StepListener} implementation for Allure.
 */
public class XtepsAllure implements StepListener {
  private final String emptyNameReplacement;
  private final String descriptionAttachmentName;

  /**
   * Zero-argument public ctor.
   */
  public XtepsAllure() {
    this("Step", "Step description");
  }

  /**
   * Ctor.
   *
   * @param emptyNameReplacement the empty step name replacement
   * @param descAttachmentName   the step description attachment name
   */
  public XtepsAllure(final String emptyNameReplacement,
                     final String descAttachmentName) {
    if (emptyNameReplacement == null) { throw new NullPointerException("emptyNameReplacement arg is null"); }
    if (emptyNameReplacement.isEmpty()) { throw new IllegalArgumentException("emptyNameReplacement arg is empty"); }
    if (descAttachmentName == null) { throw new NullPointerException("descAttachmentName arg is null"); }
    if (descAttachmentName.isEmpty()) { throw new IllegalArgumentException("descAttachmentName arg is empty"); }
    this.emptyNameReplacement = emptyNameReplacement;
    this.descriptionAttachmentName = descAttachmentName;
  }

  @Override
  public void stepStarted(final String uuid,
                          final Map<String, ?> artifacts) {
    final Keyword keyword = StepListener.Utils.keyword(artifacts);
    final String name = StepListener.Utils.name(artifacts);
    final String desc = StepListener.Utils.desc(artifacts);
    final Map<String, Object> params = StepListener.Utils.params(artifacts);
    final StepResult stepResult = new StepResult();
    stepResult.setName(StepListener.Utils.nameWithKeyword(name, keyword, this.emptyNameReplacement));
    if (!desc.isEmpty()) {
      stepResult.setDescription(desc);
    }
    if (!params.isEmpty()) {
      final List<Parameter> allureParams = stepResult.getParameters();
      params.forEach((paramName, paramValue) ->
        allureParams.add(new Parameter().setName(paramName).setValue(StepListener.Utils.asString(paramValue))));
    }
    Allure.getLifecycle().startStep(uuid, stepResult);
  }

  @Override
  public final void stepPassed(final String uuid) {
    final AllureLifecycle allureLifecycle = Allure.getLifecycle();
    allureLifecycle.updateStep(uuid, stepResult -> {
      this.attachStepDescIfPresent(stepResult);
      stepResult.setStatus(Status.PASSED);
    });
    allureLifecycle.stopStep(uuid);
  }

  @Override
  public final void stepFailed(final String uuid,
                               final Throwable exception) {
    final AllureLifecycle allureLifecycle = Allure.getLifecycle();
    allureLifecycle.updateStep(uuid, stepResult -> {
      this.attachStepDescIfPresent(stepResult);
      stepResult.setStatus(ResultsUtils.getStatus(exception).orElse(Status.BROKEN))
        .setStatusDetails(ResultsUtils.getStatusDetails(exception).orElse(null));
    });
    allureLifecycle.stopStep(uuid);
  }

  private void attachStepDescIfPresent(final StepResult stepResult) {
    final String stepDescription = stepResult.getDescription();
    if (stepDescription != null && !stepDescription.isEmpty()) {
      Allure.attachment(this.descriptionAttachmentName, stepDescription);
    }
  }
}
