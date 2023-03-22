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

import com.plugatar.xteps2.core.XtepsException;

import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.Map;

public final class Params {

  private Params() {
  }

  public static <V> Map.Entry<String, V> param(final String name, final V value) {
    return new AbstractMap.SimpleEntry<>(name, value);
  }

  @SafeVarargs
  public static <V> Map<String, V> params(final Map.Entry<String, ? extends V>... entries) {
    if (entries == null) { throw new XtepsException("entries arg is null"); }
    final Map<String, V> map = new LinkedHashMap<>();
    for (final Map.Entry<String, ? extends V> entry : entries) {
      map.put(entry.getKey(), entry.getValue());
    }
    return map;
  }

  public static <V> Map<String, V> params() {
    return new LinkedHashMap<>();
  }

  public static <V> Map<String, V> params(final String name, final V value) {
    final Map<String, V> map = new LinkedHashMap<>();
    map.put(name, value);
    return map;
  }

  public static <V> Map<String, V> params(final String name1, final V value1,
                                          final String name2, final V value2) {
    final Map<String, V> map = new LinkedHashMap<>();
    map.put(name1, value1);
    map.put(name2, value2);
    return map;
  }

  public static <V> Map<String, V> params(final String name1, final V value1,
                                          final String name2, final V value2,
                                          final String name3, final V value3) {
    final Map<String, V> map = new LinkedHashMap<>();
    map.put(name1, value1);
    map.put(name2, value2);
    map.put(name3, value3);
    return map;
  }

  public static <V> Map<String, V> params(final String name1, final V value1,
                                          final String name2, final V value2,
                                          final String name3, final V value3,
                                          final String name4, final V value4) {
    final Map<String, V> map = new LinkedHashMap<>();
    map.put(name1, value1);
    map.put(name2, value2);
    map.put(name3, value3);
    map.put(name4, value4);
    return map;
  }

  public static <V> Map<String, V> params(final String name1, final V value1,
                                          final String name2, final V value2,
                                          final String name3, final V value3,
                                          final String name4, final V value4,
                                          final String name5, final V value5) {
    final Map<String, V> map = new LinkedHashMap<>();
    map.put(name1, value1);
    map.put(name2, value2);
    map.put(name3, value3);
    map.put(name4, value4);
    map.put(name5, value5);
    return map;
  }

  public static <V> Map<String, V> params(final String name1, final V value1,
                                          final String name2, final V value2,
                                          final String name3, final V value3,
                                          final String name4, final V value4,
                                          final String name5, final V value5,
                                          final String name6, final V value6) {
    final Map<String, V> map = new LinkedHashMap<>();
    map.put(name1, value1);
    map.put(name2, value2);
    map.put(name3, value3);
    map.put(name4, value4);
    map.put(name5, value5);
    map.put(name6, value6);
    return map;
  }
}
