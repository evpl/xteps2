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

import com.plugatar.xteps2.core.Keyword;
import com.plugatar.xteps2.core.StepListener;
import ru.testit.models.ItemStatus;
import ru.testit.models.StepResult;
import ru.testit.services.Adapter;
import ru.testit.services.AdapterManager;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link StepListener} implementation for Test IT.
 */
public class XtepsTestIT implements StepListener {
  private final String emptyNameReplacement;

  /**
   * Zero-argument public ctor.
   */
  public XtepsTestIT() {
    this("Step");
  }

  /**
   * Ctor.
   *
   * @param emptyNameReplacement the empty step name replacement
   */
  public XtepsTestIT(final String emptyNameReplacement) {
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
    final Map<String, Object> params = StepListener.Utils.params(artifacts);
    final StepResult stepResult = new StepResult();
    stepResult.setName(StepListener.Utils.nameWithKeyword(name, keyword, this.emptyNameReplacement));
    if (!desc.isEmpty()) {
      stepResult.setDescription(desc);
    }
    if (!params.isEmpty()) {
      final Map<String, String> processedParams = new HashMap<>();
      params.forEach((paramName, paramValue) ->
        processedParams.put(paramName, StepListener.Utils.asString(paramValue)));
      stepResult.setParameters(processedParams);
    }
    Adapter.getAdapterManager().startStep(uuid, stepResult);
  }

  @Override
  public final void stepPassed(final String uuid) {
    final AdapterManager adapterManager = Adapter.getAdapterManager();
    adapterManager.updateStep(uuid, stepResult -> stepResult.setItemStatus(ItemStatus.PASSED));
    adapterManager.stopStep(uuid);
  }

  @Override
  public final void stepFailed(final String uuid,
                               final Throwable exception) {
    final AdapterManager adapterManager = Adapter.getAdapterManager();
    adapterManager.updateStep(
      uuid,
      stepResult -> stepResult.setItemStatus(ItemStatus.FAILED).setThrowable(exception)
    );
    adapterManager.stopStep(uuid);
  }
}
