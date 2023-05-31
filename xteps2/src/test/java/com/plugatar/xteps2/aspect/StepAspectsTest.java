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
package com.plugatar.xteps2.aspect;

import com.plugatar.xteps2.Keywords;
import com.plugatar.xteps2.StaticStepListener;
import com.plugatar.xteps2.annotation.DefaultStep;
import com.plugatar.xteps2.annotation.Param;
import com.plugatar.xteps2.annotation.Step;
import com.plugatar.xteps2.core.StepListener;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

/**
 * Tests for {@link StepAspects}.
 */
final class StepAspectsTest {

  @BeforeAll
  static void beforeAll() {
    System.setProperty("xteps.listener.list", "com.plugatar.xteps2.StaticStepListener");
  }

  @Test
  void nonStaticMethodStep() throws Throwable {
    final ClassWithDefaultStep classWithDefaultStep = new ClassWithDefaultStep(new Object(), new Object());
    classWithDefaultStep.nonStaticMethod(1, "value");

    /* stepStarted method */
    final Map<String, ?> artifacts = StaticStepListener.stepStartedArtifact();
    assertThat(StepListener.Utils.name(artifacts)).isEqualTo("Custom name");
    assertThat(StepListener.Utils.desc(artifacts)).isEqualTo("Default desc");
    assertThat(StepListener.Utils.params(artifacts)).containsExactly(
      entry("Default param 1", "Default value 1"),
      entry("Default param 2", "Default value 2"),
      entry("Custom param", "Custom value"),
      entry("methodArg1", 1),
      entry("methodArg2", "value")
    );
    assertThat(StepListener.Utils.replacements(artifacts)).contains(
      entry("args", new Object[]{1, "value"}),
      entry("Default param 1", "Default value 1"),
      entry("Default param 2", "Default value 2"),
      entry("Custom param", "Custom value"),
      entry("methodArg1", 1),
      entry("0", 1),
      entry("methodArg2", "value"),
      entry("1", "value"),
      entry("this", classWithDefaultStep),
      entry("class", classWithDefaultStep.getClass()),
      entry("method", classWithDefaultStep.getClass().getDeclaredMethod("nonStaticMethod", Object.class, Object.class))
    );

    /* stepPassed method */
    StaticStepListener.clear();
  }

  @Test
  void staticMethodStep() throws Throwable {
    ClassWithDefaultStep.staticMethod(1, "value");

    /* stepStarted method */
    final Map<String, ?> artifacts = StaticStepListener.stepStartedArtifact();
    assertThat(StepListener.Utils.name(artifacts)).isEqualTo("Custom name");
    assertThat(StepListener.Utils.desc(artifacts)).isEqualTo("Default desc");
    assertThat(StepListener.Utils.params(artifacts)).containsExactly(
      entry("Default param 1", "Default value 1"),
      entry("Default param 2", "Default value 2"),
      entry("Custom param", "Custom value"),
      entry("methodArg1", 1),
      entry("methodArg2", "value")
    );
    assertThat(StepListener.Utils.replacements(artifacts)).contains(
      entry("args", new Object[]{1, "value"}),
      entry("Default param 1", "Default value 1"),
      entry("Default param 2", "Default value 2"),
      entry("Custom param", "Custom value"),
      entry("methodArg1", 1),
      entry("0", 1),
      entry("methodArg2", "value"),
      entry("1", "value"),
      entry("class", ClassWithDefaultStep.class),
      entry("method", ClassWithDefaultStep.class.getDeclaredMethod("staticMethod", Object.class, Object.class))
    );

    /* stepPassed method */
    StaticStepListener.clear();
  }

  @Test
  void ctorStep() throws Throwable {
    new ClassWithDefaultStep(1, "value");

    /* stepStarted method */
    final Map<String, ?> artifacts = StaticStepListener.stepStartedArtifact();
    assertThat(StepListener.Utils.name(artifacts)).isEqualTo("Custom name");
    assertThat(StepListener.Utils.desc(artifacts)).isEqualTo("Default desc");
    assertThat(StepListener.Utils.params(artifacts)).containsExactly(
      entry("Default param 1", "Default value 1"),
      entry("Default param 2", "Default value 2"),
      entry("Custom param", "Custom value"),
      entry("methodArg1", 1),
      entry("methodArg2", "value")
    );
    assertThat(StepListener.Utils.replacements(artifacts)).contains(
      entry("args", new Object[]{1, "value"}),
      entry("Default param 1", "Default value 1"),
      entry("Default param 2", "Default value 2"),
      entry("Custom param", "Custom value"),
      entry("methodArg1", 1),
      entry("0", 1),
      entry("methodArg2", "value"),
      entry("1", "value"),
      entry("class", ClassWithDefaultStep.class),
      entry("ctor", ClassWithDefaultStep.class.getDeclaredConstructor(Object.class, Object.class))
    );

    /* stepPassed method */
    StaticStepListener.clear();
  }

  @DefaultStep(
    keyword = Keywords.AND,
    name = "Default name",
    desc = "Default desc",
    params = {
      @Param(name = "Default param 1", value = "Default value 1"),
      @Param(name = "Default param 2", value = "Default value 2")
    }
  )
  static final class ClassWithDefaultStep {

    @Step(name = "Custom name", params = {@Param(name = "Custom param", value = "Custom value")})
    ClassWithDefaultStep(final Object methodArg1,
                         final Object methodArg2) {
    }

    @Step(name = "Custom name", params = {@Param(name = "Custom param", value = "Custom value")})
    void nonStaticMethod(final Object methodArg1,
                         final Object methodArg2) {
    }

    @Step(name = "Custom name", params = {@Param(name = "Custom param", value = "Custom value")})
    static void staticMethod(final Object methodArg1,
                             final Object methodArg2) {
    }
  }
}
