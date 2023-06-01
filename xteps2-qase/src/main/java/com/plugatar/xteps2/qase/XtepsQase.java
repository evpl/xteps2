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

import com.plugatar.xteps2.XtepsBase;
import com.plugatar.xteps2.core.Keyword;
import com.plugatar.xteps2.core.StepListener;
import io.qase.api.StepStorage;
import io.qase.api.utils.IntegrationUtils;
import io.qase.client.model.ResultCreateStepsInner;

import java.util.Map;

/**
 * {@link StepListener} implementation for Qase.
 */
public class XtepsQase implements StepListener {
  private final String emptyNameReplacement;

  /**
   * Zero-argument public ctor.
   */
  public XtepsQase() {
    final Map<String, String> properties = XtepsBase.properties();
    this.emptyNameReplacement = properties.getOrDefault("xteps.qase.emptyNameReplacement", "Step");
  }

  @Override
  public final void stepStarted(final Map<String, ?> artifacts) {
    final Keyword keyword = Utils.getKeyword(artifacts);
    final String name = Utils.getName(artifacts);
    final String desc = Utils.getDesc(artifacts);
    StepStorage.startStep();
    StepStorage.getCurrentStep()
      .action(Utils.getNameWithKeyword(name, keyword, this.emptyNameReplacement))
      .comment(desc);
  }

  @Override
  public final void stepPassed() {
    StepStorage.getCurrentStep().status(ResultCreateStepsInner.StatusEnum.PASSED);
    StepStorage.stopStep();
  }

  @Override
  public final void stepFailed(final Throwable exception) {
    StepStorage.getCurrentStep()
      .status(ResultCreateStepsInner.StatusEnum.FAILED)
      .addAttachmentsItem(IntegrationUtils.getStacktrace(exception));
    StepStorage.stopStep();
  }
}
