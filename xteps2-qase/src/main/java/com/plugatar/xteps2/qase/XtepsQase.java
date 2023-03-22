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
    this("Step");
  }

  /**
   * @param emptyNameReplacement the empty step name replacement
   */
  public XtepsQase(final String emptyNameReplacement) {
    if (emptyNameReplacement == null) { throw new NullPointerException("emptyNameReplacement arg is null"); }
    if (emptyNameReplacement.isEmpty()) { throw new IllegalArgumentException("emptyNameReplacement arg is empty"); }
    this.emptyNameReplacement = emptyNameReplacement;
  }

  @Override
  public final void stepStarted(final String uuid,
                                final Map<String, ?> artifacts) {
    final Keyword keyword = StepListener.Utils.keyword(artifacts);
    final String name = StepListener.Utils.name(artifacts);
    final String desc = StepListener.Utils.desc(artifacts);
    StepStorage.startStep();
    StepStorage.getCurrentStep()
      .action(StepListener.Utils.nameWithKeyword(name, keyword, this.emptyNameReplacement))
      .comment(desc);
  }

  @Override
  public final void stepPassed(final String uuid) {
    StepStorage.getCurrentStep().status(ResultCreateStepsInner.StatusEnum.PASSED);
    StepStorage.stopStep();
  }

  @Override
  public final void stepFailed(final String uuid,
                               final Throwable exception) {
    StepStorage.getCurrentStep()
      .status(ResultCreateStepsInner.StatusEnum.FAILED)
      .addAttachmentsItem(IntegrationUtils.getStacktrace(exception));
    StepStorage.stopStep();
  }
}
