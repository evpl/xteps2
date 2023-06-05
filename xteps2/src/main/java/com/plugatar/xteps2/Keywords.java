/*
 * Copyright 2023 Evgenii Plugatar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.plugatar.xteps2;

import com.plugatar.xteps2.core.Keyword;
import com.plugatar.xteps2.core.XtepsException;
import com.plugatar.xteps2.core.step.StepObject;

import java.util.Map;

import static com.plugatar.xteps2.XtepsBase.properties;

/**
 * Default keywords.
 * <p>
 * Enum:
 * <ul>
 * <li>{@link #NONE}</li>
 * <li>{@link #FEATURE}</li>
 * <li>{@link #BACKGROUND}</li>
 * <li>{@link #SCENARIO}</li>
 * <li>{@link #SCENARIO_OUTLINE}</li>
 * <li>{@link #GIVEN}</li>
 * <li>{@link #WHEN}</li>
 * <li>{@link #THEN}</li>
 * <li>{@link #AND}</li>
 * <li>{@link #BUT}</li>
 * <li>{@link #ASTERISK}</li>
 * </ul>
 * <p>
 * Methods:
 * <ul>
 * <li>{@link #feature()}</li>
 * <li>{@link #background()}</li>
 * <li>{@link #scenario()}</li>
 * <li>{@link #scenarioOutline()}</li>
 * <li>{@link #given()}</li>
 * <li>{@link #when()}</li>
 * <li>{@link #then()}</li>
 * <li>{@link #and()}</li>
 * <li>{@link #but()}</li>
 * <li>{@link #asterisk()}</li>
 * <li>{@link #feature(StepObject)}</li>
 * <li>{@link #background(StepObject)}</li>
 * <li>{@link #scenario(StepObject)}</li>
 * <li>{@link #scenarioOutline(StepObject)}</li>
 * <li>{@link #given(StepObject)}</li>
 * <li>{@link #when(StepObject)}</li>
 * <li>{@link #then(StepObject)}</li>
 * <li>{@link #and(StepObject)}</li>
 * <li>{@link #but(StepObject)}</li>
 * <li>{@link #asterisk(StepObject)}</li>
 * </ul>
 */
public enum Keywords implements Keyword {
  /**
   * The empty keyword.
   */
  NONE(""),
  /**
   * The <em>Feature</em> keyword.
   */
  FEATURE(stringProperty(properties(), "xteps.keyword.feature", "Feature")),
  /**
   * The <em>Background</em> keyword.
   */
  BACKGROUND(stringProperty(properties(), "xteps.keyword.background", "Background")),
  /**
   * The <em>Scenario</em> keyword.
   */
  SCENARIO(stringProperty(properties(), "xteps.keyword.scenario", "Scenario")),
  /**
   * The <em>Scenario Outline</em> keyword.
   */
  SCENARIO_OUTLINE(stringProperty(properties(), "xteps.keyword.scenarioOutline", "Scenario Outline")),
  /**
   * The <em>Given</em> keyword.
   */
  GIVEN(stringProperty(properties(), "xteps.keyword.given", "Given")),
  /**
   * The <em>When</em> keyword.
   */
  WHEN(stringProperty(properties(), "xteps.keyword.when", "When")),
  /**
   * The <em>Then</em> keyword.
   */
  THEN(stringProperty(properties(), "xteps.keyword.then", "Then")),
  /**
   * The <em>And</em> keyword.
   */
  AND(stringProperty(properties(), "xteps.keyword.and", "And")),
  /**
   * The <em>But</em> keyword.
   */
  BUT(stringProperty(properties(), "xteps.keyword.but", "But")),
  /**
   * The <em>*</em> keyword.
   */
  ASTERISK(stringProperty(properties(), "xteps.keyword.asterisk", "*"));

  private final String keywordStr;

  Keywords(final String keywordStr) {
    this.keywordStr = keywordStr;
  }

  @Override
  public final String toString() {
    return this.keywordStr;
  }

  /**
   * Returns <em>Feature</em> keyword.
   *
   * @return <em>Feature</em> keyword
   */
  public static Keyword feature() {
    return FEATURE;
  }

  /**
   * Returns <em>Background</em> keyword.
   *
   * @return <em>Background</em> keyword
   */
  public static Keyword background() {
    return BACKGROUND;
  }

  /**
   * Returns <em>Scenario</em> keyword.
   *
   * @return <em>Scenario</em> keyword
   */
  public static Keyword scenario() {
    return SCENARIO;
  }

  /**
   * Returns <em>Scenario Outline</em> keyword.
   *
   * @return <em>Scenario Outline</em> keyword
   */
  public static Keyword scenarioOutline() {
    return SCENARIO_OUTLINE;
  }

  /**
   * Returns <em>Given</em> keyword.
   *
   * @return <em>Given</em> keyword
   */
  public static Keyword given() {
    return GIVEN;
  }

  /**
   * Returns <em>When</em> keyword.
   *
   * @return <em>When</em> keyword
   */
  public static Keyword when() {
    return WHEN;
  }

  /**
   * Returns <em>Then</em> keyword.
   *
   * @return <em>Then</em> keyword
   */
  public static Keyword then() {
    return THEN;
  }

  /**
   * Returns <em>And</em> keyword.
   *
   * @return <em>And</em> keyword
   */
  public static Keyword and() {
    return AND;
  }

  /**
   * Returns <em>But</em> keyword.
   *
   * @return <em>But</em> keyword
   */
  public static Keyword but() {
    return BUT;
  }

  /**
   * Returns <em>*</em> keyword.
   *
   * @return <em>*</em> keyword
   */
  public static Keyword asterisk() {
    return ASTERISK;
  }

  /**
   * Returns step object with <em>Feature</em> keyword.
   *
   * @param step the origin step object
   * @param <S>  the type of the step object
   * @return step object with <em>Feature</em> keyword
   * @throws XtepsException if {@code step} arg is null
   */
  public static <S extends StepObject> S feature(final S step) {
    return Artifacts.withKeyword(FEATURE, step);
  }

  /**
   * Returns step object with <em>Background</em> keyword.
   *
   * @param step the origin step object
   * @param <S>  the type of the step object
   * @return step object with <em>Background</em> keyword
   * @throws XtepsException if {@code step} arg is null
   */
  public static <S extends StepObject> S background(final S step) {
    return Artifacts.withKeyword(BACKGROUND, step);
  }

  /**
   * Returns step object with <em>Scenario</em> keyword.
   *
   * @param step the origin step object
   * @param <S>  the type of the step object
   * @return step object with <em>Scenario</em> keyword
   * @throws XtepsException if {@code step} arg is null
   */
  public static <S extends StepObject> S scenario(final S step) {
    return Artifacts.withKeyword(SCENARIO, step);
  }

  /**
   * Returns step object with <em>Scenario Outline</em> keyword.
   *
   * @param step the origin step object
   * @param <S>  the type of the step object
   * @return step object with <em>Scenario Outline</em> keyword
   * @throws XtepsException if {@code step} arg is null
   */
  public static <S extends StepObject> S scenarioOutline(final S step) {
    return Artifacts.withKeyword(SCENARIO_OUTLINE, step);
  }

  /**
   * Returns step object with <em>Given</em> keyword.
   *
   * @param step the origin step object
   * @param <S>  the type of the step object
   * @return step object with <em>Given</em> keyword
   * @throws XtepsException if {@code step} arg is null
   */
  public static <S extends StepObject> S given(final S step) {
    return Artifacts.withKeyword(GIVEN, step);
  }

  /**
   * Returns step object with <em>When</em> keyword.
   *
   * @param step the origin step object
   * @param <S>  the type of the step object
   * @return step object with <em>When</em> keyword
   * @throws XtepsException if {@code step} arg is null
   */
  public static <S extends StepObject> S when(final S step) {
    return Artifacts.withKeyword(WHEN, step);
  }

  /**
   * Returns step object with <em>Then</em> keyword.
   *
   * @param step the origin step object
   * @param <S>  the type of the step object
   * @return step object with <em>Then</em> keyword
   * @throws XtepsException if {@code step} arg is null
   */
  public static <S extends StepObject> S then(final S step) {
    return Artifacts.withKeyword(THEN, step);
  }

  /**
   * Returns step object with <em>And</em> keyword.
   *
   * @param step the origin step object
   * @param <S>  the type of the step object
   * @return step object with <em>And</em> keyword
   * @throws XtepsException if {@code step} arg is null
   */
  public static <S extends StepObject> S and(final S step) {
    return Artifacts.withKeyword(AND, step);
  }

  /**
   * Returns step object with <em>But</em> keyword.
   *
   * @param step the origin step object
   * @param <S>  the type of the step object
   * @return step object with <em>But</em> keyword
   * @throws XtepsException if {@code step} arg is null
   */
  public static <S extends StepObject> S but(final S step) {
    return Artifacts.withKeyword(BUT, step);
  }

  /**
   * Returns step object with <em>*</em> keyword.
   *
   * @param step the origin step object
   * @param <S>  the type of the step object
   * @return step object with <em>*</em> keyword
   * @throws XtepsException if {@code step} arg is null
   */
  public static <S extends StepObject> S asterisk(final S step) {
    return Artifacts.withKeyword(ASTERISK, step);
  }

  private static String stringProperty(final Map<String, String> properties,
                                       final String propertyName,
                                       final String defaultValue) {
    final String propertyValue = properties.get(propertyName);
    if (propertyValue == null) {
      return defaultValue;
    }
    final String trimmedPropertyValue = propertyValue.trim();
    if (trimmedPropertyValue.isEmpty()) {
      return defaultValue;
    }
    return trimmedPropertyValue;
  }
}
