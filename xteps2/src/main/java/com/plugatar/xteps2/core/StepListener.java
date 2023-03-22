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
package com.plugatar.xteps2.core;

import com.plugatar.xteps2.Artifacts;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

/**
 * Step listener.
 */
public interface StepListener {

  /**
   * Invoked each time a step starts.
   *
   * @param uuid      the step UUID (not null, UUID format)
   * @param artifacts the step artifacts (not null, may be empty)
   */
  void stepStarted(String uuid,
                   Map<String, ?> artifacts);

  /**
   * Invoked each time a step passes.
   *
   * @param uuid the step UUID (not null, UUID format)
   */
  void stepPassed(String uuid);

  /**
   * Invoked each time a step fails.
   *
   * @param uuid      the step UUID (not null, UUID format)
   * @param exception the step exception (not null)
   */
  void stepFailed(String uuid,
                  Throwable exception);

  class Utils {

    private Utils() {
    }

    public static Keyword keyword(final Map<String, ?> artifacts) {
      final Object value = artifacts.get(Artifacts.keywordArtifact());
      return value instanceof Keyword ? (Keyword) value : Keyword.EMPTY;
    }

    public static String name(final Map<String, ?> artifacts) {
      final Object value = artifacts.get(Artifacts.nameArtifact());
      return value instanceof String ? (String) value : "";
    }

    public static String desc(final Map<String, ?> artifacts) {
      final Object value = artifacts.get(Artifacts.descArtifact());
      return value instanceof String ? (String) value : "";
    }

    public static Map<String, Object> params(final Map<String, ?> artifacts) {
      final Object value = artifacts.get(Artifacts.paramsArtifact());
      return value instanceof Map ? (Map<String, Object>) value : Collections.emptyMap();
    }

    public static String nameWithKeyword(final String name,
                                         final Keyword keyword,
                                         final String emptyNameReplacement) {
      final String keywordValue = keyword.toString();
      final String nameValue = name.isEmpty() ? emptyNameReplacement : name;
      return keywordValue.isEmpty() ? nameValue : keywordValue + " " + nameValue;
    }

    public static String asString(final Object obj) {
      if (obj == null) {
        return "null";
      }
      if (obj.getClass().isArray()) {
        if (obj instanceof Object[]) {
          return Arrays.toString((Object[]) obj);
        } else if (obj instanceof long[]) {
          return Arrays.toString((long[]) obj);
        } else if (obj instanceof short[]) {
          return Arrays.toString((short[]) obj);
        } else if (obj instanceof int[]) {
          return Arrays.toString((int[]) obj);
        } else if (obj instanceof char[]) {
          return Arrays.toString((char[]) obj);
        } else if (obj instanceof double[]) {
          return Arrays.toString((double[]) obj);
        } else if (obj instanceof float[]) {
          return Arrays.toString((float[]) obj);
        } else if (obj instanceof boolean[]) {
          return Arrays.toString((boolean[]) obj);
        } else if (obj instanceof byte[]) {
          return Arrays.toString((byte[]) obj);
        }
      }
      return obj.toString();
    }
  }
}
