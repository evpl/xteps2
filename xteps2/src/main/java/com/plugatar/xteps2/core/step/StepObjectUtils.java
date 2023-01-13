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
package com.plugatar.xteps2.core.step;

import com.plugatar.xteps2.Artifacts;
import com.plugatar.xteps2.Keywords;
import com.plugatar.xteps2.XtepsBase;
import com.plugatar.xteps2.core.Keyword;
import com.plugatar.xteps2.core.StepExecutor;
import com.plugatar.xteps2.core.XtepsException;

import java.util.LinkedHashMap;
import java.util.Map;

final class StepObjectUtils {
  static final String EMPTY_STRING = "";

  private StepObjectUtils() {
  }

  static Keyword emptyKeyword() {
    return Keywords.NONE;
  }

  static StepExecutor currentStepExecutor() {
    return XtepsBase.stepExecutor();
  }

  static Map<String, Object> artifactMapArgs(final Keyword keyword,
                                             final String name,
                                             final Map<String, ?> params,
                                             final String desc) {
    if (keyword == null) { throw new XtepsException("keyword arg is null"); }
    if (name == null) { throw new XtepsException("name arg is null"); }
    if (params == null) { throw new XtepsException("params arg is null"); }
    if (desc == null) { throw new XtepsException("desc arg is null"); }
    final Map<String, Object> map = new LinkedHashMap<>();
    map.put(Artifacts.keywordArtifact(), keyword);
    map.put(Artifacts.nameArtifact(), name);
    map.put(Artifacts.paramsArtifact(), params);
    map.put(Artifacts.descArtifact(), desc);
    return map;
  }

  static <K, V> Map<K, V> copyMapAndPutArgs(final Map<? extends K, ? extends V> origin,
                                            final K name,
                                            final V value) {
    if (origin == null) { throw new XtepsException("origin arg is null"); }
    if (name == null) { throw new XtepsException("name arg is null"); }
    final Map<K, V> copy = new LinkedHashMap<>(origin);
    copy.put(name, value);
    return copy;
  }
}
