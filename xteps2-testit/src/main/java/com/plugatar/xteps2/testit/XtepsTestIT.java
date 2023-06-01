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
import com.plugatar.xteps2.core.Keyword;
import com.plugatar.xteps2.core.StepListener;
import ru.testit.models.ItemStatus;
import ru.testit.models.StepResult;
import ru.testit.services.Adapter;
import ru.testit.services.AdapterManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * {@link StepListener} implementation for Test IT.
 */
public class XtepsTestIT implements StepListener {
  private final String emptyNameReplacement;

  /**
   * Zero-argument public ctor.
   */
  public XtepsTestIT() {
    final Map<String, String> properties = XtepsBase.properties();
    this.emptyNameReplacement = properties.getOrDefault("xteps.testit.emptyNameReplacement", "Step");
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
      final Map<String, String> processedParams = new HashMap<>();
      params.forEach((paramName, paramValue) -> processedParams.put(paramName, XtepsBase.textFormatter().format(paramValue)));
      stepResult.setParameters(processedParams);
    }
    Adapter.getAdapterManager().startStep(UUID.randomUUID().toString(), stepResult);
  }

  @Override
  public final void stepPassed() {
    final AdapterManager adapterManager = Adapter.getAdapterManager();
    adapterManager.updateStep(stepResult -> stepResult.setItemStatus(ItemStatus.PASSED));
    adapterManager.stopStep();
  }

  @Override
  public final void stepFailed(final Throwable exception) {
    final AdapterManager adapterManager = Adapter.getAdapterManager();
    adapterManager.updateStep(stepResult -> stepResult.setItemStatus(ItemStatus.FAILED).setThrowable(exception));
    adapterManager.stopStep();
  }
}
