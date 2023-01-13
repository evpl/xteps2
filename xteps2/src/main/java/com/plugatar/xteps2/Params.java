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

/**
 * Utility class. Syntactic sugar for step parameters.
 * <p>
 * Methods:
 * <ul>
 * <li>{@link #param(String, Object)}</li>
 * <li>{@link #params(Map.Entry[])}</li>
 * <li>{@link #params()}</li>
 * <li>{@link #params(String, Object)}</li>
 * <li>{@link #params(String, Object, String, Object)}</li>
 * <li>{@link #params(String, Object, String, Object, String, Object)}</li>
 * <li>{@link #params(String, Object, String, Object, String, Object, String, Object)}</li>
 * <li>{@link #params(String, Object, String, Object, String, Object, String, Object, String, Object)}</li>
 * <li>{@link #params(String, Object, String, Object, String, Object, String, Object, String, Object, String, Object)}</li>
 * </ul>
 */
public final class Params {

  /**
   * Utility class ctor.
   */
  private Params() {
  }

  /**
   * Returns {@code Map.Entry} representing single parameter.
   *
   * @param paramName  the parameter name
   * @param paramValue the parameter value
   * @param <V>        the type of the parameter value
   * @return single parameter
   * @throws XtepsException if {@code paramName} arg is null
   */
  public static <V> Map.Entry<String, V> param(final String paramName,
                                               final V paramValue) {
    if (paramName == null) { throw new XtepsException("paramName arg is null"); }
    return new AbstractMap.SimpleEntry<>(paramName, paramValue);
  }

  /**
   * Returns {@code Map} representing parameters.
   *
   * @param entries the parameters
   * @param <V>     the type of the parameters value
   * @return parameters
   * @throws XtepsException if one of {@code entries} key is null
   */
  @SafeVarargs
  public static <V> Map<String, V> params(final Map.Entry<String, ? extends V>... entries) {
    if (entries == null) { throw new XtepsException("entries arg is null"); }
    final Map<String, V> map = new LinkedHashMap<>();
    for (final Map.Entry<String, ? extends V> entry : entries) {
      final String paramName = entry.getKey();
      if (paramName == null) { throw new XtepsException("paramName is null"); }
      map.put(paramName, entry.getValue());
    }
    return map;
  }

  /**
   * Returns {@code Map} representing parameters.
   *
   * @param <V> the type of the parameters value
   * @return parameters
   */
  public static <V> Map<String, V> params() {
    return new LinkedHashMap<>();
  }

  /**
   * Returns {@code Map} representing parameters.
   *
   * @param paramName  the parameter name
   * @param paramValue the parameter value
   * @param <V>        the type of the parameters value
   * @return parameters
   * @throws XtepsException if {@code paramName} arg is null
   */
  public static <V> Map<String, V> params(final String paramName, final V paramValue) {
    if (paramName == null) { throw new XtepsException("paramName arg is null"); }
    final Map<String, V> map = new LinkedHashMap<>();
    map.put(paramName, paramValue);
    return map;
  }

  /**
   * Returns {@code Map} representing parameters.
   *
   * @param paramName1  the first parameter name
   * @param paramValue1 the first parameter value
   * @param paramName2  the second parameter name
   * @param paramValue2 the second parameter value
   * @param <V>         the type of the parameters value
   * @return parameters
   * @throws XtepsException if {@code paramName1} arg is null
   *                        or if {@code paramName2} arg is null
   */
  public static <V> Map<String, V> params(final String paramName1, final V paramValue1,
                                          final String paramName2, final V paramValue2) {
    if (paramName1 == null) { throw new XtepsException("paramName1 arg is null"); }
    if (paramName2 == null) { throw new XtepsException("paramName2 arg is null"); }
    final Map<String, V> map = new LinkedHashMap<>();
    map.put(paramName1, paramValue1);
    map.put(paramName2, paramValue2);
    return map;
  }

  /**
   * Returns {@code Map} representing parameters.
   *
   * @param paramName1  the first parameter name
   * @param paramValue1 the first parameter value
   * @param paramName2  the second parameter name
   * @param paramValue2 the second parameter value
   * @param paramName3  the third parameter name
   * @param paramValue3 the third parameter value
   * @param <V>         the type of the parameters value
   * @return parameters
   * @throws XtepsException if {@code paramName1} arg is null
   *                        or if {@code paramName2} arg is null
   *                        or if {@code paramName3} arg is null
   */
  public static <V> Map<String, V> params(final String paramName1, final V paramValue1,
                                          final String paramName2, final V paramValue2,
                                          final String paramName3, final V paramValue3) {
    if (paramName1 == null) { throw new XtepsException("paramName1 arg is null"); }
    if (paramName2 == null) { throw new XtepsException("paramName2 arg is null"); }
    if (paramName3 == null) { throw new XtepsException("paramName3 arg is null"); }
    final Map<String, V> map = new LinkedHashMap<>();
    map.put(paramName1, paramValue1);
    map.put(paramName2, paramValue2);
    map.put(paramName3, paramValue3);
    return map;
  }

  /**
   * Returns {@code Map} representing parameters.
   *
   * @param paramName1  the first parameter name
   * @param paramValue1 the first parameter value
   * @param paramName2  the second parameter name
   * @param paramValue2 the second parameter value
   * @param paramName3  the third parameter name
   * @param paramValue3 the third parameter value
   * @param paramName4  the fourth parameter name
   * @param paramValue4 the fourth parameter value
   * @param <V>         the type of the parameters value
   * @return parameters
   * @throws XtepsException if {@code paramName1} arg is null
   *                        or if {@code paramName2} arg is null
   *                        or if {@code paramName3} arg is null
   *                        or if {@code paramName4} arg is null
   */
  public static <V> Map<String, V> params(final String paramName1, final V paramValue1,
                                          final String paramName2, final V paramValue2,
                                          final String paramName3, final V paramValue3,
                                          final String paramName4, final V paramValue4) {
    if (paramName1 == null) { throw new XtepsException("paramName1 arg is null"); }
    if (paramName2 == null) { throw new XtepsException("paramName2 arg is null"); }
    if (paramName3 == null) { throw new XtepsException("paramName3 arg is null"); }
    if (paramName4 == null) { throw new XtepsException("paramName4 arg is null"); }
    final Map<String, V> map = new LinkedHashMap<>();
    map.put(paramName1, paramValue1);
    map.put(paramName2, paramValue2);
    map.put(paramName3, paramValue3);
    map.put(paramName4, paramValue4);
    return map;
  }

  /**
   * Returns {@code Map} representing parameters.
   *
   * @param paramName1  the first parameter name
   * @param paramValue1 the first parameter value
   * @param paramName2  the second parameter name
   * @param paramValue2 the second parameter value
   * @param paramName3  the third parameter name
   * @param paramValue3 the third parameter value
   * @param paramName4  the fourth parameter name
   * @param paramValue4 the fourth parameter value
   * @param paramName5  the fifth parameter name
   * @param paramValue5 the fifth parameter value
   * @param <V>         the type of the parameters value
   * @return parameters
   * @throws XtepsException if {@code paramName1} arg is null
   *                        or if {@code paramName2} arg is null
   *                        or if {@code paramName3} arg is null
   *                        or if {@code paramName4} arg is null
   *                        or if {@code paramName5} arg is null
   */
  public static <V> Map<String, V> params(final String paramName1, final V paramValue1,
                                          final String paramName2, final V paramValue2,
                                          final String paramName3, final V paramValue3,
                                          final String paramName4, final V paramValue4,
                                          final String paramName5, final V paramValue5) {
    if (paramName1 == null) { throw new XtepsException("paramName1 arg is null"); }
    if (paramName2 == null) { throw new XtepsException("paramName2 arg is null"); }
    if (paramName3 == null) { throw new XtepsException("paramName3 arg is null"); }
    if (paramName4 == null) { throw new XtepsException("paramName4 arg is null"); }
    if (paramName5 == null) { throw new XtepsException("paramName5 arg is null"); }
    final Map<String, V> map = new LinkedHashMap<>();
    map.put(paramName1, paramValue1);
    map.put(paramName2, paramValue2);
    map.put(paramName3, paramValue3);
    map.put(paramName4, paramValue4);
    map.put(paramName5, paramValue5);
    return map;
  }

  /**
   * Returns {@code Map} representing parameters.
   *
   * @param paramName1  the first parameter name
   * @param paramValue1 the first parameter value
   * @param paramName2  the second parameter name
   * @param paramValue2 the second parameter value
   * @param paramName3  the third parameter name
   * @param paramValue3 the third parameter value
   * @param paramName4  the fourth parameter name
   * @param paramValue4 the fourth parameter value
   * @param paramName5  the fifth parameter name
   * @param paramValue5 the fifth parameter value
   * @param paramName6  the sixth parameter name
   * @param paramValue6 the sixth parameter value
   * @param <V>         the type of the parameters value
   * @return parameters
   * @throws XtepsException if {@code paramName1} arg is null
   *                        or if {@code paramName2} arg is null
   *                        or if {@code paramName3} arg is null
   *                        or if {@code paramName4} arg is null
   *                        or if {@code paramName5} arg is null
   *                        or if {@code paramName6} arg is null
   */
  public static <V> Map<String, V> params(final String paramName1, final V paramValue1,
                                          final String paramName2, final V paramValue2,
                                          final String paramName3, final V paramValue3,
                                          final String paramName4, final V paramValue4,
                                          final String paramName5, final V paramValue5,
                                          final String paramName6, final V paramValue6) {
    if (paramName1 == null) { throw new XtepsException("paramName1 arg is null"); }
    if (paramName2 == null) { throw new XtepsException("paramName2 arg is null"); }
    if (paramName3 == null) { throw new XtepsException("paramName3 arg is null"); }
    if (paramName4 == null) { throw new XtepsException("paramName4 arg is null"); }
    if (paramName5 == null) { throw new XtepsException("paramName5 arg is null"); }
    if (paramName6 == null) { throw new XtepsException("paramName6 arg is null"); }
    final Map<String, V> map = new LinkedHashMap<>();
    map.put(paramName1, paramValue1);
    map.put(paramName2, paramValue2);
    map.put(paramName3, paramValue3);
    map.put(paramName4, paramValue4);
    map.put(paramName5, paramValue5);
    map.put(paramName6, paramValue6);
    return map;
  }
}
