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
package com.plugatar.xteps2.reportportal;

import com.epam.reportportal.service.Launch;
import com.epam.reportportal.service.step.StepRequestUtils;
import com.epam.ta.reportportal.ws.model.ParameterResource;
import com.epam.ta.reportportal.ws.model.StartTestItemRQ;
import com.plugatar.xteps2.core.Keyword;
import com.plugatar.xteps2.core.StepListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * {@link StepListener} implementation for Report Portal.
 */
public class XtepsReportPortal implements StepListener {
  private final String emptyNameReplacement;

  /**
   * Zero-argument public ctor.
   */
  public XtepsReportPortal() {
    this("Step");
  }

  /**
   * Ctor.
   *
   * @param emptyNameReplacement the empty step name replacement
   */
  public XtepsReportPortal(final String emptyNameReplacement) {
    if (emptyNameReplacement == null) { throw new NullPointerException("emptyNameReplacement arg is null"); }
    if (emptyNameReplacement.isEmpty()) { throw new IllegalArgumentException("emptyNameReplacement arg is empty"); }
    this.emptyNameReplacement = emptyNameReplacement;
  }

  @Override
  public final void stepStarted(final String uuid,
                                final Map<String, ?> artifacts) {
    final Launch launch = Launch.currentLaunch();
    if (launch != null) {
      final Keyword keyword = StepListener.Utils.keyword(artifacts);
      final String name = StepListener.Utils.name(artifacts);
      final String desc = StepListener.Utils.desc(artifacts);
      final Map<String, Object> params = StepListener.Utils.params(artifacts);
      final StartTestItemRQ startTestItemRQ = StepRequestUtils.buildStartStepRequest(
        StepListener.Utils.nameWithKeyword(name, keyword, this.emptyNameReplacement),
        desc
      );
      if (!params.isEmpty()) {
        final List<ParameterResource> rpParams = new ArrayList<>();
        params.forEach((paramName, paramValue) -> {
          final ParameterResource param = new ParameterResource();
          param.setKey(paramName);
          param.setValue(StepListener.Utils.asString(paramValue));
          rpParams.add(param);
        });
        startTestItemRQ.setParameters(rpParams);
      }
      launch.getStepReporter().startNestedStep(startTestItemRQ);
    }
  }

  @Override
  public final void stepPassed(final String uuid) {
    final Launch launch = Launch.currentLaunch();
    if (launch != null) {
      launch.getStepReporter().finishNestedStep();
    }
  }

  @Override
  public final void stepFailed(final String uuid,
                               final Throwable exception) {
    final Launch launch = Launch.currentLaunch();
    if (launch != null) {
      launch.getStepReporter().finishNestedStep(exception);
    }
  }
}
