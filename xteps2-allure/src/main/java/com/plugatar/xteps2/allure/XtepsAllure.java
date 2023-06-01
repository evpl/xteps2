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

import com.plugatar.xteps2.XtepsBase;
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
import java.util.UUID;

/**
 * {@link StepListener} implementation for Allure.
 */
public class XtepsAllure implements StepListener {
  private final String emptyNameReplacement;
  private final String descAttachmentName;

  /**
   * Zero-argument public ctor.
   */
  public XtepsAllure() {
    final Map<String, String> properties = XtepsBase.properties();
    this.emptyNameReplacement = properties.getOrDefault("xteps.allure.emptyNameReplacement", "Step");
    this.descAttachmentName = properties.getOrDefault("xteps.allure.emptyNameReplacement", "Description");
  }

  @Override
  public final void stepStarted(final Map<String, ?> artifacts) {
    final Keyword keyword = Utils.getKeyword(artifacts);
    final String name = Utils.getName(artifacts);
    final String desc = Utils.getDesc(artifacts);
    final Map<String, Object> params = Utils.getParams(artifacts);
    final StepResult stepResult = new StepResult();
    stepResult.setName(Utils.getNameWithKeyword(name, keyword, this.emptyNameReplacement));
    if (!desc.isEmpty()) {
      stepResult.setDescription(desc);
    }
    if (!params.isEmpty()) {
      final List<Parameter> allureParams = stepResult.getParameters();
      params.forEach((paramName, paramValue) ->
        allureParams.add(new Parameter().setName(paramName).setValue(XtepsBase.textFormatter().format(paramValue))));
    }
    Allure.getLifecycle().startStep(UUID.randomUUID().toString(), stepResult);
  }

  @Override
  public final void stepPassed() {
    final AllureLifecycle allureLifecycle = Allure.getLifecycle();
    allureLifecycle.updateStep(stepResult -> {
      this.attachStepDescIfPresent(stepResult);
      stepResult.setStatus(Status.PASSED);
    });
    allureLifecycle.stopStep();
  }

  @Override
  public final void stepFailed(final Throwable exception) {
    final AllureLifecycle allureLifecycle = Allure.getLifecycle();
    allureLifecycle.updateStep(stepResult -> {
      this.attachStepDescIfPresent(stepResult);
      stepResult.setStatus(ResultsUtils.getStatus(exception).orElse(Status.BROKEN))
        .setStatusDetails(ResultsUtils.getStatusDetails(exception).orElse(null));
    });
    allureLifecycle.stopStep();
  }

  private void attachStepDescIfPresent(final StepResult stepResult) {
    final String stepDescription = stepResult.getDescription();
    if (stepDescription != null && !stepDescription.isEmpty()) {
      Allure.attachment(this.descAttachmentName, stepDescription);
    }
  }
}
