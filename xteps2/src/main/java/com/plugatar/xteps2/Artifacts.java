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
import com.plugatar.xteps2.core.function.ThFunction;
import com.plugatar.xteps2.core.step.StepObject;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Utility class. Step artifacts.
 * <p>
 * Methods:
 * <ul>
 * <li>{@link #keywordArtifact()}</li>
 * <li>{@link #nameArtifact()}</li>
 * <li>{@link #descArtifact()}</li>
 * <li>{@link #paramsArtifact()}</li>
 * <li>{@link #withArtifact(String, Object, StepObject)}</li>
 * <li>{@link #withArtifacts(Map, StepObject)}</li>
 * <li>{@link #withKeyword(Keyword, StepObject)}</li>
 * <li>{@link #withName(String, StepObject)}</li>
 * <li>{@link #withDesc(String, StepObject)}</li>
 * <li>{@link #withParam(String, Object, StepObject)}</li>
 * <li>{@link #withParams(Map, StepObject)}</li>
 * </ul>
 */
public final class Artifacts {
  private static final String KEYWORD_ARTIFACT = "keyword";
  private static final String NAME_ARTIFACT = "name";
  private static final String DESC_ARTIFACT = "desc";
  private static final String PARAMS_ARTIFACT = "params";
  private static final String CONTEXTS_ARTIFACT = "contexts";
  private static final String REPLACEMENTS_ARTIFACT = "replacements";

  /**
   * Utility class ctor.
   */
  private Artifacts() {
  }

  /**
   * Return <em>keyword</em> artifact name.
   *
   * @return <em>keyword</em> artifact name
   */
  public static String keywordArtifact() {
    return KEYWORD_ARTIFACT;
  }

  /**
   * Return <em>name</em> artifact name.
   *
   * @return <em>name</em> artifact name
   */
  public static String nameArtifact() {
    return NAME_ARTIFACT;
  }

  /**
   * Return <em>desc</em> artifact name.
   *
   * @return <em>desc</em> artifact name
   */
  public static String descArtifact() {
    return DESC_ARTIFACT;
  }

  /**
   * Return <em>params</em> artifact name.
   *
   * @return <em>params</em> artifact name
   */
  public static String paramsArtifact() {
    return PARAMS_ARTIFACT;
  }

  /**
   * Return <em>contexts</em> artifact name.
   *
   * @return <em>contexts</em> artifact name
   */
  public static String contextsArtifact() {
    return REPLACEMENTS_ARTIFACT;
  }

  /**
   * Return <em>replacements</em> artifact name.
   *
   * @return <em>replacements</em> artifact name
   */
  public static String replacementsArtifact() {
    return CONTEXTS_ARTIFACT;
  }

  /**
   * Returns step object with given artifact.
   *
   * @param artifactName  the artifact name
   * @param artifactValue the artifact value
   * @param step          the origin step object
   * @param <S>           the type of the step object
   * @return step object with given artifact
   * @throws XtepsException if {@code artifactName} arg is null
   *                        or if {@code step} arg is null
   */
  @SuppressWarnings("unchecked")
  public static <S extends StepObject> S withArtifact(final String artifactName,
                                                      final Object artifactValue,
                                                      final S step) {
    if (artifactName == null) { throw new XtepsException("artifactName arg is null"); }
    if (step == null) { throw new XtepsException("step arg is null"); }
    return (S) step.withArtifact(artifactName, artifactValue);
  }

  /**
   * Returns step object with given artifacts.
   *
   * @param artifacts the artifacts map
   * @param step      the origin step object
   * @param <S>       the type of the step object
   * @return step object with given artifacts
   * @throws XtepsException if {@code artifacts} arg is null
   *                        or if {@code step} arg is null
   *                        or if one of parameter names is null
   */
  @SuppressWarnings("unchecked")
  public static <S extends StepObject> S withArtifacts(final Map<String, ?> artifacts,
                                                       final S step) {
    if (artifacts == null) { throw new XtepsException("artifacts arg is null"); }
    if (step == null) { throw new XtepsException("step arg is null"); }
    S currentStep = step;
    for (final Map.Entry<String, ?> entry : artifacts.entrySet()) {
      final String name = entry.getKey();
      if (name == null) {
        throw new XtepsException("One of artifacts names is null");
      }
      currentStep = (S) currentStep.withArtifact(name, entry.getValue());
    }
    return currentStep;
  }

  /**
   * Returns step object with given keyword.
   *
   * @param keyword the keyword
   * @param step    the origin step object
   * @param <S>     the type of the step object
   * @return step object with given keyword
   * @throws XtepsException if {@code keyword} arg is null
   *                        or if {@code step} arg is null
   */
  @SuppressWarnings("unchecked")
  public static <S extends StepObject> S withKeyword(final Keyword keyword,
                                                     final S step) {
    if (keyword == null) { throw new XtepsException("keyword arg is null"); }
    if (step == null) { throw new XtepsException("step arg is null"); }
    return (S) step.withArtifact(KEYWORD_ARTIFACT, keyword);
  }

  /**
   * Returns step object with given name.
   *
   * @param name the name
   * @param step the origin step object
   * @param <S>  the type of the step object
   * @return step object with given name
   * @throws XtepsException if {@code name} arg is null
   *                        or if {@code step} arg is null
   */
  @SuppressWarnings("unchecked")
  public static <S extends StepObject> S withName(final String name,
                                                  final S step) {
    if (name == null) { throw new XtepsException("name arg is null"); }
    if (step == null) { throw new XtepsException("step arg is null"); }
    return (S) step.withArtifact(NAME_ARTIFACT, name);
  }

  /**
   * Returns step object with given name.
   *
   * @param nameFunction the name function
   * @param step         the origin step object
   * @param <S>          the type of the step object
   * @return step object with given name
   * @throws XtepsException if {@code nameFunction} arg is null
   *                        or if {@code step} arg is null
   */
  @SuppressWarnings("unchecked")
  public static <S extends StepObject> S withName(final ThFunction<String, String, ?> nameFunction,
                                                  final S step) {
    if (nameFunction == null) { throw new XtepsException("nameFunction arg is null"); }
    if (step == null) { throw new XtepsException("step arg is null"); }
    final Object rawName = step.artifact(NAME_ARTIFACT).orElse("");
    return (S) step.withArtifact(NAME_ARTIFACT, ThFunction.unchecked(nameFunction).apply(
      rawName instanceof String ? (String) rawName : ""
    ));
  }

  /**
   * Returns step object with given description.
   *
   * @param desc the description
   * @param step the origin step object
   * @param <S>  the type of the step object
   * @return step object with given description
   * @throws XtepsException if {@code desc} arg is null
   *                        or if {@code step} arg is null
   */
  @SuppressWarnings("unchecked")
  public static <S extends StepObject> S withDesc(final String desc,
                                                  final S step) {
    if (desc == null) { throw new XtepsException("desc arg is null"); }
    if (step == null) { throw new XtepsException("step arg is null"); }
    return (S) step.withArtifact(DESC_ARTIFACT, desc);
  }

  /**
   * Returns step object with given description.
   *
   * @param descFunction the description function
   * @param step         the origin step object
   * @param <S>          the type of the step object
   * @return step object with given description
   * @throws XtepsException if {@code descFunction} arg is null
   *                        or if {@code step} arg is null
   */
  @SuppressWarnings("unchecked")
  public static <S extends StepObject> S withDesc(final ThFunction<String, String, ?> descFunction,
                                                  final S step) {
    if (descFunction == null) { throw new XtepsException("descFunction arg is null"); }
    if (step == null) { throw new XtepsException("step arg is null"); }
    final Object rawName = step.artifact(DESC_ARTIFACT).orElse("");
    return (S) step.withArtifact(NAME_ARTIFACT, ThFunction.unchecked(descFunction).apply(
      rawName instanceof String ? (String) rawName : ""
    ));
  }

  /**
   * Returns step object with given parameter.
   *
   * @param paramName  the parameter name
   * @param paramValue the parameter value
   * @param step       the origin step object
   * @param <S>        the type of the step object
   * @return step object with given param
   * @throws XtepsException if {@code paramName} arg is null
   *                        or if {@code step} arg is null
   */
  @SuppressWarnings("unchecked")
  public static <S extends StepObject> S withParam(final String paramName,
                                                   final Object paramValue,
                                                   final S step) {
    if (paramName == null) { throw new XtepsException("paramName arg is null"); }
    if (step == null) { throw new XtepsException("step arg is null"); }
    final Optional<Map<String, ?>> optParams = (Optional<Map<String, ?>>) (Optional<?>) step.artifact(PARAMS_ARTIFACT);
    final Map<String, Object> newParams = new LinkedHashMap<>(optParams.orElse(Collections.emptyMap()));
    newParams.put(paramName, paramValue);
    return (S) step.withArtifact(PARAMS_ARTIFACT, newParams);
  }

  /**
   * Returns step object with given parameters.
   *
   * @param params the parameters map
   * @param step   the origin step object
   * @param <S>    the type of the step object
   * @return step object with given parameters
   * @throws XtepsException if {@code params} arg is null
   *                        or if {@code step} arg is null
   *                        or if one of parameter names is null
   */
  @SuppressWarnings("unchecked")
  public static <S extends StepObject> S withParams(final Map<String, ?> params,
                                                    final S step) {
    if (params == null) { throw new XtepsException("params arg is null"); }
    if (step == null) { throw new XtepsException("step arg is null"); }
    params.forEach((k, v) -> {
      if (k == null) {
        throw new XtepsException("One of parameter names is null");
      }
    });
    final Optional<Map<String, ?>> optParams = (Optional<Map<String, ?>>) (Optional<?>) step.artifact(PARAMS_ARTIFACT);
    final Map<String, Object> newParams = new LinkedHashMap<>(optParams.orElse(Collections.emptyMap()));
    newParams.putAll(params);
    return (S) step.withArtifact(PARAMS_ARTIFACT, newParams);
  }
}
