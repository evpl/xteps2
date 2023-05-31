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
package com.plugatar.xteps2.extentreports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.junit.jupiter.api.Test;

import static com.plugatar.xteps2.Steps.step;
import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * Tests for {@link XtepsExtentReports}.
 */
final class XtepsExtentReportsTest {

  @Test
  void test() {
    final ExtentTest test = new ExtentReports().createTest("Test");
    XtepsExtentReports.registerTest(test);
    assertThatCode(() -> step("Step", () -> { })).doesNotThrowAnyException();
    XtepsExtentReports.unregisterTest();
  }
}
