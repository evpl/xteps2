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

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public final class Artifacts {
  private static final String KEYWORD_ARTIFACT_NAME = "keyword";
  private static final String NAME_ARTIFACT_NAME = "name";
  private static final String DESC_ARTIFACT_NAME = "desc";
  private static final String PARAMS_ARTIFACT_NAME = "params";

  private Artifacts() {
  }

  public static String keywordArtifact() {
    return KEYWORD_ARTIFACT_NAME;
  }

  public static String nameArtifact() {
    return NAME_ARTIFACT_NAME;
  }

  public static String descArtifact() {
    return DESC_ARTIFACT_NAME;
  }

  public static String paramsArtifact() {
    return PARAMS_ARTIFACT_NAME;
  }

  @SuppressWarnings("unchecked")
  public static <T extends StepObject> T withKeyword(final Keyword keyword,
                                                     final T step) {
    if (keyword == null) { throw new XtepsException("keyword arg is null"); }
    if (step == null) { throw new XtepsException("step arg is null"); }
    return (T) step.withArtifact(KEYWORD_ARTIFACT_NAME, keyword);
  }

  @SuppressWarnings("unchecked")
  public static <T extends StepObject> T withName(final String name,
                                                  final T step) {
    if (name == null) { throw new XtepsException("name arg is null"); }
    if (step == null) { throw new XtepsException("step arg is null"); }
    return (T) step.withArtifact(NAME_ARTIFACT_NAME, name);
  }

  @SuppressWarnings("unchecked")
  public static <T extends StepObject> T withDesc(final String desc,
                                                  final T step) {
    if (desc == null) { throw new XtepsException("desc arg is null"); }
    if (step == null) { throw new XtepsException("step arg is null"); }
    return (T) step.withArtifact(DESC_ARTIFACT_NAME, desc);
  }

  @SuppressWarnings("unchecked")
  public static <T extends StepObject> T withParam(final String key,
                                                   final Object value,
                                                   final T step) {
    if (key == null) { throw new XtepsException("key arg is null"); }
    if (value == null) { throw new XtepsException("value arg is null"); }
    if (step == null) { throw new XtepsException("step arg is null"); }
    final Optional<Map<String, ?>> optParams = (Optional<Map<String, ?>>) (Optional<?>) step.artifact(PARAMS_ARTIFACT_NAME);
    final Map<String, Object> newParams = new LinkedHashMap<>(optParams.orElse(Collections.emptyMap()));
    newParams.put(key, value);
    return (T) step.withArtifact(PARAMS_ARTIFACT_NAME, newParams);
  }

  @SuppressWarnings("unchecked")
  public static <T extends StepObject> T withParams(final Map<String, ?> params,
                                                    final T step) {
    if (params == null) { throw new XtepsException("params arg is null"); }
    if (step == null) { throw new XtepsException("step arg is null"); }
    final Optional<Map<String, ?>> optParams = (Optional<Map<String, ?>>) (Optional<?>) step.artifact(PARAMS_ARTIFACT_NAME);
    final Map<String, Object> newParams = new LinkedHashMap<>(optParams.orElse(Collections.emptyMap()));
    newParams.putAll(params);
    return (T) step.withArtifact(PARAMS_ARTIFACT_NAME, newParams);
  }
}
