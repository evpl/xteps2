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
import io.qameta.allure.Allure;
import io.qameta.allure.model.Parameter;
import io.qameta.allure.model.Stage;
import io.qameta.allure.model.StepResult;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static com.plugatar.xteps2.Artifacts.descArtifact;
import static com.plugatar.xteps2.Artifacts.keywordArtifact;
import static com.plugatar.xteps2.Artifacts.nameArtifact;
import static com.plugatar.xteps2.Artifacts.paramsArtifact;
import static com.plugatar.xteps2.Artifacts.replacementsArtifact;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link XtepsAllure}.
 */
final class XtepsAllureTest {

  @Test
  void passedStep() {
    final XtepsAllure listener = new XtepsAllure();
    final String uuid = UUID.randomUUID().toString();
    final Map<String, Object> params = new HashMap<>();
    params.put("param1", 1);
    params.put("param2", 'a');
    final Map<String, Object> replacements = new HashMap<>();
    replacements.put("rep1", 1);
    replacements.put("rep2", 'a');
    final Map<String, Object> artifacts = new HashMap<>();
    artifacts.put(keywordArtifact(), new Keyword.Of("test keyword"));
    artifacts.put(nameArtifact(), "step name {rep2}");
    artifacts.put(descArtifact(), "step description");
    artifacts.put(paramsArtifact(), params);
    artifacts.put(replacementsArtifact(), replacements);
    final AtomicReference<StepResult> stepResult = new AtomicReference<>();

    /* stepStarted method */
    listener.stepStarted(uuid, artifacts);
    Allure.getLifecycle().updateStep(uuid, stepResult::set);
    assertThat(stepResult.get().getName()).isEqualTo("test keyword step name a");
    assertThat(stepResult.get().getDescription()).isEqualTo("step description");
    assertThat(stepResult.get().getParameters()).containsExactly(
      new Parameter().setName("param1").setValue("1"),
      new Parameter().setName("param2").setValue("a")
    );
    assertThat(stepResult.get().getStage()).isEqualTo(Stage.RUNNING);
    stepResult.set(null);

    /* stepPassed method */
    listener.stepPassed(uuid);
    Allure.getLifecycle().updateStep(uuid, stepResult::set);
    assertThat(stepResult.get()).isNull();
  }

  @Test
  void failedStep() {
    final XtepsAllure listener = new XtepsAllure();
    final String uuid = UUID.randomUUID().toString();
    final Map<String, Object> params = new HashMap<>();
    params.put("param1", 1);
    params.put("param2", 'a');
    final Map<String, Object> replacements = new HashMap<>();
    replacements.put("rep1", 1);
    replacements.put("rep2", 'a');
    final Map<String, Object> artifacts = new HashMap<>();
    artifacts.put(keywordArtifact(), new Keyword.Of("test keyword"));
    artifacts.put(nameArtifact(), "step name {rep2}");
    artifacts.put(descArtifact(), "step description");
    artifacts.put(paramsArtifact(), params);
    artifacts.put(replacementsArtifact(), replacements);
    final AssertionError stepException = new AssertionError();
    final AtomicReference<StepResult> stepResult = new AtomicReference<>();

    /* stepStarted method */
    listener.stepStarted(uuid, artifacts);
    Allure.getLifecycle().updateStep(uuid, stepResult::set);
    assertThat(stepResult.get().getName()).isEqualTo("test keyword step name a");
    assertThat(stepResult.get().getDescription()).isEqualTo("step description");
    assertThat(stepResult.get().getParameters()).containsExactly(
      new Parameter().setName("param1").setValue("1"),
      new Parameter().setName("param2").setValue("a")
    );
    assertThat(stepResult.get().getStage()).isEqualTo(Stage.RUNNING);
    stepResult.set(null);

    /* stepFailed method */
    listener.stepFailed(uuid, stepException);
    Allure.getLifecycle().updateStep(uuid, stepResult::set);
    assertThat(stepResult.get()).isNull();
  }
}
