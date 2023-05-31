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
package com.plugatar.xteps2;

import com.plugatar.xteps2.core.StepListener;

import java.util.Map;

public final class StaticStepListener implements StepListener {
  private static Map<String, ?> stepStartedArtifacts = null;
  private static String stepPassedUUID = null;
  private static String stepFailedUUID = null;
  private static Throwable stepFailedException = null;

  public StaticStepListener() {
  }

  public static void clear() {
    stepStartedArtifacts = null;
    stepPassedUUID = null;
    stepFailedUUID = null;
    stepFailedException = null;
  }

  public static Map<String, ?> stepStartedArtifact() {
    final Map<String, ?> last = stepStartedArtifacts;
    stepStartedArtifacts = null;
    return last;
  }

  public static String stepPassedUUID() {
    final String last = stepPassedUUID;
    stepPassedUUID = null;
    return last;
  }

  public static String stepFailedUUID() {
    final String last = stepFailedUUID;
    stepFailedUUID = null;
    return last;
  }

  public static Throwable stepFailedException() {
    final Throwable last = stepFailedException;
    stepFailedException = null;
    return last;
  }

  @Override
  public void stepStarted(final Map<String, ?> artifacts) {
    stepStartedArtifacts = artifacts;
  }

  @Override
  public void stepPassed() {
  }

  @Override
  public void stepFailed(final Throwable exception) {
    stepFailedException = exception;
  }
}
