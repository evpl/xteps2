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
import com.plugatar.xteps2.Keywords;

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

  /**
   * Step listener utils.
   */
  class Utils {

    /**
     * Utility class ctor.
     */
    private Utils() {
    }

    /**
     * Returns <em>keyword</em> artifact from given map.
     *
     * @param artifacts the artifacts map
     * @return <em>keyword</em> artifact
     * @throws XtepsException if {@code artifacts} arg is null
     */
    public static Keyword keyword(final Map<String, ?> artifacts) {
      if (artifacts == null) { throw new XtepsException("artifacts arg is null"); }
      final Object value = artifacts.get(Artifacts.keywordArtifact());
      return value instanceof Keyword ? (Keyword) value : Keywords.NONE;
    }

    /**
     * Returns <em>name</em> artifact from given map.
     *
     * @param artifacts the artifacts map
     * @return <em>keyword</em> artifact
     * @throws XtepsException if {@code artifacts} arg is null
     */
    public static String name(final Map<String, ?> artifacts) {
      if (artifacts == null) { throw new XtepsException("artifacts arg is null"); }
      final Object value = artifacts.get(Artifacts.nameArtifact());
      return value instanceof String ? (String) value : "";
    }

    /**
     * Returns <em>desc</em> artifact from given map.
     *
     * @param artifacts the artifacts map
     * @return <em>desc</em> artifact
     * @throws XtepsException if {@code artifacts} arg is null
     */
    public static String desc(final Map<String, ?> artifacts) {
      if (artifacts == null) { throw new XtepsException("artifacts arg is null"); }
      final Object value = artifacts.get(Artifacts.descArtifact());
      return value instanceof String ? (String) value : "";
    }

    /**
     * Returns <em>params</em> artifact from given map.
     *
     * @param artifacts the artifacts map
     * @return <em>params</em> artifact
     * @throws XtepsException if {@code artifacts} arg is null
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> params(final Map<String, ?> artifacts) {
      if (artifacts == null) { throw new XtepsException("artifacts arg is null"); }
      final Object value = artifacts.get(Artifacts.paramsArtifact());
      return value instanceof Map ? (Map<String, Object>) value : Collections.emptyMap();
    }

    /**
     * Returns <em>replacements</em> artifact from given map.
     *
     * @param artifacts the artifacts map
     * @return <em>replacements</em> artifact
     * @throws XtepsException if {@code artifacts} arg is null
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> replacements(final Map<String, ?> artifacts) {
      if (artifacts == null) { throw new XtepsException("artifacts arg is null"); }
      final Object value = artifacts.get(Artifacts.replacementsArtifact());
      return value instanceof Map ? (Map<String, Object>) value : Collections.emptyMap();
    }

    /**
     * Returns a string consisting of given keyword and given name.
     *
     * @param name                 the name
     * @param keyword              the keyword
     * @param emptyNameReplacement the empty name replacement
     * @return string consisting of given keyword and given name
     * @throws XtepsException if {@code name} arg is null
     *                        or if {@code keyword} arg is null
     *                        or if {@code emptyNameReplacement} arg is null
     */
    public static String nameWithKeyword(final String name,
                                         final Keyword keyword,
                                         final String emptyNameReplacement) {
      if (name == null) { throw new XtepsException("artifacts arg is null"); }
      if (keyword == null) { throw new XtepsException("artifacts arg is null"); }
      if (emptyNameReplacement == null) { throw new XtepsException("artifacts arg is null"); }
      final String keywordValue = keyword.toString();
      final String nameValue = name.isEmpty() ? emptyNameReplacement : name;
      return keywordValue.isEmpty() ? nameValue : keywordValue + " " + nameValue;
    }
  }
}
