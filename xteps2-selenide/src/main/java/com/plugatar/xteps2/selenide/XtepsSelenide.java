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
package com.plugatar.xteps2.selenide;

import com.codeborne.selenide.logevents.LogEvent;
import com.codeborne.selenide.logevents.SelenideLog;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.plugatar.xteps2.core.Keyword;
import com.plugatar.xteps2.core.StepListener;
import com.plugatar.xteps2.core.XtepsException;

import java.util.LinkedList;
import java.util.Map;

/**
 * {@link StepListener} implementation for Selenide.
 */
public class XtepsSelenide implements StepListener {
  private static final ThreadLocal<LinkedList<SelenideLog>> STEPS = new InheritableThreadLocal<LinkedList<SelenideLog>>() {
    @Override
    protected LinkedList<SelenideLog> initialValue() {
      return new LinkedList<>();
    }

    @Override
    protected LinkedList<SelenideLog> childValue(final LinkedList<SelenideLog> parentValue) {
      return new LinkedList<>(parentValue);
    }
  };
  private final String emptyNameReplacement;

  /**
   * Zero-argument public ctor.
   */
  public XtepsSelenide() {
    this.emptyNameReplacement = "Step";
  }

  @Override
  public final void stepStarted(final Map<String, ?> artifacts) {
    final Keyword keyword = Utils.getKeyword(artifacts);
    final String name = Utils.getName(artifacts);
    final String desc = Utils.getDesc(artifacts);
    final SelenideLog selenideLog = SelenideLogger.beginStep(
      Utils.getNameWithKeyword(name, keyword, this.emptyNameReplacement),
      desc
    );
    STEPS.get().addLast(selenideLog);
  }

  @Override
  public final void stepPassed() {
    final LinkedList<SelenideLog> steps = STEPS.get();
    if (steps.isEmpty()) {
      throw new XtepsException("Not found current step");
    }
    SelenideLogger.commitStep(steps.pollLast(), LogEvent.EventStatus.PASS);
  }

  @Override
  public final void stepFailed(final Throwable exception) {
    final LinkedList<SelenideLog> steps = STEPS.get();
    if (steps.isEmpty()) {
      throw new XtepsException("Not found current step");
    }
    SelenideLogger.commitStep(steps.pollLast(), exception);
  }
}
