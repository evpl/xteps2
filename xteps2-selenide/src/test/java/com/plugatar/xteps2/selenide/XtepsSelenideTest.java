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
import com.codeborne.selenide.logevents.LogEventListener;
import com.codeborne.selenide.logevents.SelenideLogger;
import com.plugatar.xteps2.Artifacts;
import com.plugatar.xteps2.core.Keyword;
import com.plugatar.xteps2.core.XtepsException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link XtepsSelenide}.
 */
final class XtepsSelenideTest {
  private static final String DEFAULT_LISTENER_NAME = "simpleReport";

  @Test
  void passedStep() {
    final LogEventListener listener = mock();
    SelenideLogger.addListener(DEFAULT_LISTENER_NAME, listener);
    final XtepsSelenide xtepsSelenide = new XtepsSelenide();
    final String elementStr = "element";
    final String subjectStr = "subject";

    /* step started */
    xtepsSelenide.stepStarted(stepArtifacts(elementStr, subjectStr));
    final ArgumentCaptor<LogEvent> eventCaptor = ArgumentCaptor.forClass(LogEvent.class);
    verify(listener, times(1)).beforeEvent(eventCaptor.capture());
    final LogEvent event = eventCaptor.getValue();
    verify(listener, never()).afterEvent(any());
    verifyEventStart(event, elementStr, subjectStr);

    /* step passed */
    xtepsSelenide.stepPassed();
    verify(listener, times(1)).afterEvent(same(event));
    verify(listener, times(1)).afterEvent(any());
    verify(listener, times(1)).beforeEvent(any());
    verifyEventPassed(event, elementStr, subjectStr);
  }

  @Test
  void failedStep() {
    final LogEventListener listener = mock();
    SelenideLogger.addListener(DEFAULT_LISTENER_NAME, listener);
    final XtepsSelenide xtepsSelenide = new XtepsSelenide();
    final String elementStr = "element";
    final String subjectStr = "subject";
    final Throwable exception = new Throwable();

    /* step started */
    xtepsSelenide.stepStarted(stepArtifacts(elementStr, subjectStr));
    final ArgumentCaptor<LogEvent> eventCaptor = ArgumentCaptor.forClass(LogEvent.class);
    verify(listener, times(1)).beforeEvent(eventCaptor.capture());
    final LogEvent event = eventCaptor.getValue();
    verify(listener, never()).afterEvent(any());
    verifyEventStart(event, elementStr, subjectStr);

    /* step failed */
    xtepsSelenide.stepFailed(exception);
    verify(listener, times(1)).afterEvent(same(event));
    verify(listener, times(1)).afterEvent(any());
    verify(listener, times(1)).beforeEvent(any());
    verifyEventFail(event, elementStr, subjectStr, exception);
  }

  @Test
  void nestedSteps() {
    final LogEventListener listener = mock();
    SelenideLogger.addListener(DEFAULT_LISTENER_NAME, listener);
    final XtepsSelenide xtepsSelenide = new XtepsSelenide();
    final String elementStrStep1 = "element step 1";
    final String subjectStrStep1 = "subject step 1";
    final String elementStrStep2 = "element step 2";
    final String subjectStrStep2 = "subject step 2";
    final Throwable exceptionStep2 = new Throwable();

    /* step 1 started */
    xtepsSelenide.stepStarted(stepArtifacts(elementStrStep1, subjectStrStep1));
    final ArgumentCaptor<LogEvent> event1Captor = ArgumentCaptor.forClass(LogEvent.class);
    verify(listener, times(1)).beforeEvent(event1Captor.capture());
    final LogEvent event1 = event1Captor.getValue();
    verify(listener, never()).afterEvent(any());
    verifyEventStart(event1, elementStrStep1, subjectStrStep1);

    /* step 2 started */
    xtepsSelenide.stepStarted(stepArtifacts(elementStrStep2, subjectStrStep2));
    final ArgumentCaptor<LogEvent> event2Captor = ArgumentCaptor.forClass(LogEvent.class);
    verify(listener, times(2)).beforeEvent(event2Captor.capture());
    final LogEvent event2 = event2Captor.getValue();
    verify(listener, never()).afterEvent(any());
    verifyEventStart(event2, elementStrStep2, subjectStrStep2);

    /* step 2 failed */
    xtepsSelenide.stepFailed(exceptionStep2);
    verify(listener, times(1)).afterEvent(same(event2));
    verify(listener, times(1)).afterEvent(any());
    verify(listener, times(2)).beforeEvent(any());
    verifyEventFail(event2, elementStrStep2, subjectStrStep2, exceptionStep2);

    /* step 1 passed */
    xtepsSelenide.stepPassed();
    verify(listener, times(1)).afterEvent(same(event1));
    verify(listener, times(2)).afterEvent(any());
    verify(listener, times(2)).beforeEvent(any());
    verifyEventPassed(event1, elementStrStep1, subjectStrStep1);
  }

  @Test
  void stepWithKeyword() {
    final LogEventListener listener = mock();
    SelenideLogger.addListener(DEFAULT_LISTENER_NAME, listener);
    final XtepsSelenide xtepsSelenide = new XtepsSelenide();
    final String keywordStr = "keyword";
    final String elementStr = "element";
    final String subjectStr = "subject";

    xtepsSelenide.stepStarted(stepArtifacts(keywordStr, elementStr, subjectStr));
    final ArgumentCaptor<LogEvent> eventCaptor = ArgumentCaptor.forClass(LogEvent.class);
    verify(listener).beforeEvent(eventCaptor.capture());
    final LogEvent event = eventCaptor.getValue();
    assertThat(event.getElement()).isEqualTo("keyword element");
    xtepsSelenide.stepPassed();
  }

  @Test
  void emptyNameAndDescription() {
    final LogEventListener listener = mock();
    SelenideLogger.addListener(DEFAULT_LISTENER_NAME, listener);
    final XtepsSelenide xtepsSelenide = new XtepsSelenide();

    xtepsSelenide.stepStarted(stepArtifacts("", ""));
    final ArgumentCaptor<LogEvent> eventCaptor = ArgumentCaptor.forClass(LogEvent.class);
    verify(listener).beforeEvent(eventCaptor.capture());
    final LogEvent event = eventCaptor.getValue();
    assertThat(event.getElement()).isEqualTo("Step");
    assertThat(event.getSubject()).isEmpty();
    xtepsSelenide.stepPassed();
  }

  @Test
  void stepPassedMethodWithoutStartedStep() {
    final XtepsSelenide xtepsSelenide = new XtepsSelenide();

    assertThatThrownBy(() -> xtepsSelenide.stepPassed())
      .isInstanceOf(XtepsException.class);
    xtepsSelenide.stepStarted(stepArtifacts("keyword", "name", "desc"));
    xtepsSelenide.stepPassed();
    assertThatThrownBy(() -> xtepsSelenide.stepPassed())
      .isInstanceOf(XtepsException.class);
  }

  @Test
  void stepFailedMethodWithoutStartedStep() {
    final XtepsSelenide xtepsSelenide = new XtepsSelenide();
    final Throwable exception = new Throwable();

    assertThatThrownBy(() -> xtepsSelenide.stepFailed(exception))
      .isInstanceOf(XtepsException.class);
    xtepsSelenide.stepStarted(stepArtifacts("keyword", "name", "desc"));
    xtepsSelenide.stepPassed();
    assertThatThrownBy(() -> xtepsSelenide.stepFailed(exception))
      .isInstanceOf(XtepsException.class);
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

  private void verifyEventStart(final LogEvent event,
                                final String element,
                                final String subject) {
    assertThat(event.getElement()).isEqualTo(element);
    assertThat(event.getSubject()).isEqualTo(subject);
    assertThat(event.getStatus()).isSameAs(LogEvent.EventStatus.IN_PROGRESS);
    assertThat(event.getError()).isNull();
  }

  private void verifyEventPassed(final LogEvent event,
                                 final String element,
                                 final String subject) {
    assertThat(event.getElement()).isEqualTo(element);
    assertThat(event.getSubject()).isEqualTo(subject);
    assertThat(event.getStatus()).isSameAs(LogEvent.EventStatus.PASS);
    assertThat(event.getError()).isNull();
  }

  private void verifyEventFail(final LogEvent event,
                               final String element,
                               final String subject,
                               final Throwable exception) {
    assertThat(event.getElement()).isEqualTo(element);
    assertThat(event.getSubject()).isEqualTo(subject);
    assertThat(event.getStatus()).isSameAs(LogEvent.EventStatus.FAIL);
    assertThat(event.getError()).isSameAs(exception);
  }
}
