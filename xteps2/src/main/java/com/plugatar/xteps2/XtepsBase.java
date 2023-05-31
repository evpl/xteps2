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

import com.plugatar.xteps2.core.ExceptionHandler;
import com.plugatar.xteps2.core.StepListener;
import com.plugatar.xteps2.core.StepReporter;
import com.plugatar.xteps2.core.TextFormatter;
import com.plugatar.xteps2.core.XtepsException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

/**
 * Utility class. Xteps base.
 * <p>
 * Methods:
 * <ul>
 * <li>{@link #properties()}</li>
 * <li>{@link #stepReporter()}</li>
 * <li>{@link #exceptionHandler()}</li>
 * <li>{@link #textFormatter()}</li>
 * </ul>
 */
public final class XtepsBase {

  /**
   * Utility class ctor.
   */
  private XtepsBase() {
  }

  /**
   * Returns Xteps properties.
   *
   * @return Xteps properties
   */
  public static Map<String, String> properties() {
    return PROPERTIES.get();
  }

  /**
   * Returns {@code StepReporter}.
   *
   * @return {@code StepReporter}
   * @throws XtepsException if Xteps configuration is incorrect
   */
  public static StepReporter stepReporter() {
    return CONFIG.get().stepReporter;
  }

  /**
   * Returns {@code ExceptionHandler}.
   *
   * @return {@code ExceptionHandler}
   * @throws XtepsException if Xteps configuration is incorrect
   */
  public static ExceptionHandler exceptionHandler() {
    return CONFIG.get().exceptionHandler;
  }

  /**
   * Returns {@code TextFormatter}.
   *
   * @return {@code TextFormatter}
   * @throws XtepsException if Xteps configuration is incorrect
   */
  public static TextFormatter textFormatter() {
    return CONFIG.get().textFormatter;
  }

  private static final Supplier<Map<String, String>> PROPERTIES = new Supplier<Map<String, String>>() {
    private volatile Map<String, String> instance = null;

    @Override
    public Map<String, String> get() {
      Map<String, String> result;
      if ((result = this.instance) == null) {
        synchronized (this) {
          if ((result = this.instance) == null) {
            result = Collections.unmodifiableMap(xtepsProperties());
            this.instance = result;
          }
          return result;
        }
      }
      return result;
    }
  };

  static final Supplier<Config> CONFIG = new Supplier<Config>() {
    private volatile Config instance = null;

    @Override
    public Config get() {
      Config result;
      if ((result = this.instance) == null) {
        synchronized (this) {
          if ((result = this.instance) == null) {
            result = configByProperties();
            this.instance = result;
          }
          return result;
        }
      }
      return result;
    }
  };

  private static Map<String, String> xtepsProperties() {
    final String xtepsPropertiesPrefix = "xteps.";
    final String xtepsPropertiesFilePath = xtepsPropertiesPrefix + "properties";
    final Properties systemProperties = System.getProperties();
    final Properties fileProperties = new Properties();
    try (final InputStream stream =
           ClassLoader.getSystemClassLoader().getResourceAsStream(xtepsPropertiesFilePath)) {
      if (stream != null) {
        fileProperties.load(stream);
      }
    } catch (final Exception ignored) { }
    try (final InputStream stream =
           Thread.currentThread().getContextClassLoader().getResourceAsStream(xtepsPropertiesFilePath)) {
      if (stream != null) {
        fileProperties.load(stream);
      }
    } catch (final Exception ignored) { }
    final Map<String, String> propertiesMap = new HashMap<>();
    final BiConsumer<Object, Object> filter = (k, v) -> {
      if (((String) k).startsWith(xtepsPropertiesPrefix)) {
        propertiesMap.put((String) k, (String) v);
      }
    };
    fileProperties.forEach(filter);
    systemProperties.forEach(filter);
    return propertiesMap;
  }

  private static Config configByProperties() {
    final Map<String, String> properties = PROPERTIES.get();
    final ExceptionHandler exceptionHandler = booleanProperty(properties, "xteps.exceptionHandler.cleanStackTrace.enabled", true)
      ? new ExceptionHandler.CleanStackTrace()
      : new ExceptionHandler.Fake();
    final StepReporter stepReporter;
    final TextFormatter textFormatter;
    if (booleanProperty(properties, "xteps.reporter.enabled", true)) {
      final List<StepListener> listeners = new ArrayList<>(
        listenersByClassNames(stringListProperty(properties, "xteps.listener.list", ",", Collections.emptyList()))
      );
      if (booleanProperty(properties, "xteps.listener.autodetection", true)) {
        listeners.addAll(listenersBySPI());
      }
      if (listeners.isEmpty()) {
        throw new XtepsException("No StepListener implementation found");
      }
      final StepListener[] listenersArray = uniqueByClass(listeners).toArray(new StepListener[0]);
      stepReporter = new StepReporter.Default(exceptionHandler, listenersArray);
      if (booleanProperty(properties, "xteps.textFormatter.enabled", true)) {
        textFormatter = new TextFormatter.Default(
          exceptionHandler,
          patternContainsCapturingGroupsProperty(properties, "xteps.textFormatter.replacementPattern", Pattern.compile("\\{([^}]*)}")),
          booleanProperty(properties, "xteps.textFormatter.field.forceAccess.enabled", true),
          booleanProperty(properties, "xteps.textFormatter.method.forceAccess.enabled", true)
        );
      } else {
        textFormatter = new TextFormatter.Fake();
      }
    } else {
      stepReporter = new StepReporter.Fake(exceptionHandler);
      textFormatter = new TextFormatter.Fake();
    }
    return new Config(stepReporter, exceptionHandler, textFormatter);
  }

  private static boolean booleanProperty(final Map<String, String> properties,
                                         final String propertyName,
                                         final boolean defaultValue) {
    final String propertyValue = properties.get(propertyName);
    if (propertyValue == null) {
      return defaultValue;
    }
    final String trimmedPropertyValue = propertyValue.trim();
    if (trimmedPropertyValue.isEmpty()) {
      return defaultValue;
    }
    if (trimmedPropertyValue.equalsIgnoreCase("true")) {
      return true;
    }
    if (trimmedPropertyValue.equalsIgnoreCase("false")) {
      return false;
    }
    throw new XtepsException("Illegal boolean property value, property: " + propertyName + ", value: " + propertyValue);
  }

  private static List<String> stringListProperty(final Map<String, String> properties,
                                                 final String propertyName,
                                                 final String delimiter,
                                                 final List<String> defaultValue) {
    final String propertyValue = properties.get(propertyName);
    if (propertyValue == null) {
      return defaultValue;
    }
    final List<String> stringList = Arrays.stream(propertyValue.split(delimiter))
      .map(String::trim)
      .filter(str -> !str.isEmpty())
      .collect(Collectors.toList());
    if (stringList.isEmpty()) {
      return defaultValue;
    }
    return stringList;
  }

  private static Pattern patternContainsCapturingGroupsProperty(final Map<String, String> properties,
                                                                final String propertyName,
                                                                final Pattern defaultValue) {
    final String propertyValue = properties.get(propertyName);
    if (propertyValue == null) {
      return defaultValue;
    }
    final String trimmedPropertyValue = propertyValue.trim();
    if (trimmedPropertyValue.isEmpty()) {
      return defaultValue;
    }
    final Pattern pattern;
    try {
      pattern = Pattern.compile(propertyValue);
    } catch (final PatternSyntaxException ex) {
      throw new XtepsException("Illegal pattern property value, property: " + propertyName + ", value: " + propertyValue, ex);
    }
    if (pattern.matcher("").groupCount() < 1) {
      throw new XtepsException("Illegal pattern property value, pattern must contain at least one group, property: " +
        propertyName + ", value: " + propertyValue);
    }
    return defaultValue;
  }

  private static List<StepListener> listenersBySPI() {
    final List<StepListener> listeners = new ArrayList<>();
    try {
      for (final StepListener listener : ServiceLoader.load(StepListener.class)) {
        listeners.add(listener);
      }
    } catch (final Exception ex) {
      throw new XtepsException("Cannot instantiate StepListener by SPI cause " + ex, ex);
    }
    return listeners;
  }

  private static List<StepListener> listenersByClassNames(final List<String> classNames) {
    final List<StepListener> listeners = new ArrayList<>();
    for (final String className : classNames) {
      final StepListener listener;
      try {
        listener = (StepListener) Class.forName(className).getConstructor().newInstance();
      } catch (final Exception ex) {
        throw new XtepsException("Cannot instantiate StepListener " + className + " cause " + ex, ex);
      }
      listeners.add(listener);
    }
    return listeners;
  }

  private static <T> List<T> uniqueByClass(final List<T> listeners) {
    final Set<Class<?>> classes = Collections.newSetFromMap(new IdentityHashMap<>(8));
    return listeners.stream()
      .filter(listener -> classes.add(listener.getClass()))
      .collect(Collectors.toList());
  }

  private static final class Config {
    final ExceptionHandler exceptionHandler;
    final StepReporter stepReporter;
    final TextFormatter textFormatter;

    private Config(final StepReporter stepReporter,
                   final ExceptionHandler exceptionHandler,
                   final TextFormatter textFormatter) {
      this.stepReporter = stepReporter;
      this.exceptionHandler = exceptionHandler;
      this.textFormatter = textFormatter;
    }
  }
}
