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

import com.plugatar.xteps2.Artifacts;
import com.plugatar.xteps2.core.Keyword;
import io.qase.api.StepStorage;
import io.qase.client.model.ResultCreateStepsInner;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link XtepsQase}.
 */
final class XtepsQaseTest {

  @Test
  void passedStep() {
    final XtepsQase listener = new XtepsQase();

    /* stepStarted method */
    listener.stepStarted(stepArtifacts("step name", "step description"));
    final ResultCreateStepsInner stepResult = StepStorage.getCurrentStep();
    assertThat(stepResult.getAction()).isEqualTo("step name");
    assertThat(stepResult.getComment()).isEqualTo("step description");
    assertThat(stepResult.getStatus()).isNull();

    /* stepPassed method */
    listener.stepPassed();
    assertThat(stepResult.getAttachments()).isNull();
    assertThat(stepResult.getStatus()).isSameAs(ResultCreateStepsInner.StatusEnum.PASSED);
  }

  @Test
  void failedStep() {
    final XtepsQase listener = new XtepsQase();
    final Throwable stepException = new Throwable("test ex");

    /* stepStarted method */
    listener.stepStarted(stepArtifacts("step name", "step description"));
    final ResultCreateStepsInner stepResult = StepStorage.getCurrentStep();
    assertThat(stepResult.getAction()).isEqualTo("step name");
    assertThat(stepResult.getComment()).isEqualTo("step description");
    assertThat(stepResult.getStatus()).isNull();

    /* stepPassed method */
    listener.stepFailed(stepException);
    final List<String> attachments = stepResult.getAttachments();
    assertThat(attachments).isNotNull().hasSize(1);
    assertThat(attachments.get(0)).startsWith("java.lang.Throwable: test ex");
    assertThat(stepResult.getStatus()).isSameAs(ResultCreateStepsInner.StatusEnum.FAILED);
  }

  @Test
  void stepWithKeyword() {
    final XtepsQase listener = new XtepsQase();

    listener.stepStarted(stepArtifacts("step keyword", "step name", "step description"));
    final ResultCreateStepsInner stepResult = StepStorage.getCurrentStep();
    listener.stepPassed();
    assertThat(stepResult.getAction()).isEqualTo("step keyword step name");
  }

  @Test
  void emptyNameAndDescription() {
    final XtepsQase listener = new XtepsQase();

    listener.stepStarted(stepArtifacts("", ""));
    final ResultCreateStepsInner stepResult = StepStorage.getCurrentStep();
    listener.stepPassed();
    assertThat(stepResult.getAction()).isEqualTo("Step");
    assertThat(stepResult.getComment()).isNull();
  }

  private static Map<String, Object> stepArtifacts(final String keyword,
                                                   final String name,
                                                   final String desc) {
    final Map<String, Object> artifacts = new HashMap<>();
    artifacts.put(Artifacts.keywordArtifact(), new Keyword.Of(keyword));
    artifacts.put(Artifacts.nameArtifact(), name);
    artifacts.put(Artifacts.descArtifact(), desc);
    return artifacts;
  }

  private static Map<String, Object> stepArtifacts(final String name,
                                                   final String desc) {
    final Map<String, Object> artifacts = new HashMap<>();
    artifacts.put(Artifacts.nameArtifact(), name);
    artifacts.put(Artifacts.descArtifact(), desc);
    return artifacts;
  }
}
