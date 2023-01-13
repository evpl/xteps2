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
import com.plugatar.xteps2.core.StepExecutor;
import com.plugatar.xteps2.core.StepListener;
import com.plugatar.xteps2.core.TestHookContainer;
import com.plugatar.xteps2.core.TextFormatter;
import com.plugatar.xteps2.core.XtepsException;
import com.plugatar.xteps2.core.function.ThRunnable;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

/**
 * Utility class. Xteps base.
 * <p>
 * Methods:
 * <ul>
 * <li>{@link #exceptionHandler()}</li>
 * <li>{@link #stepExecutor()}</li>
 * <li>{@link #testHookContainer()}</li>
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
   * Returns current {@code ExceptionHandler}.
   *
   * @return current {@code ExceptionHandler}
   * @throws XtepsException if Xteps configuration is incorrect
   */
  public static ExceptionHandler exceptionHandler() {
    return CONFIG.get().exceptionHandler;
  }

  /**
   * Returns current {@code StepExecutor}.
   *
   * @return current {@code StepExecutor}
   * @throws XtepsException if Xteps configuration is incorrect
   */
  public static StepExecutor stepExecutor() {
    return CONFIG.get().stepExecutor;
  }

  /**
   * Returns current {@code TestHookContainer}.
   *
   * @return current {@code TestHookContainer}
   * @throws XtepsException if Xteps configuration is incorrect
   */
  public static TestHookContainer testHookContainer() {
    return CONFIG.get().testHookContainer;
  }

  /**
   * Returns current {@code TextFormatter}.
   *
   * @return current {@code TextFormatter}
   * @throws XtepsException if Xteps configuration is incorrect
   */
  public static TextFormatter textFormatter() {
    return CONFIG.get().textFormatter;
  }

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

  private static Config configByProperties() {
    final Properties properties = systemPropertiesWithFile("xteps.properties");
    final List<StepListener> listeners = new ArrayList<>();
    if (booleanProperty(properties, "xteps.listeners.autodetection", true)) {
      listeners.addAll(listenersBySPI());
    }
    listeners.addAll(listenersByClassNames(stringListProperty(properties, "xteps.listeners.list", ",", Collections.emptyList())));
    Optional<TestHookContainer> optionalTestHookContainer = testHooksContainerByProperty(properties, "xteps.testHooks.impl");
    if (!optionalTestHookContainer.isPresent() && booleanProperty(properties, "xteps.testHooks.autodetection", true)) {
      optionalTestHookContainer = testHooksContainerBySPI();
    }
    final TestHookContainer testHooksContainer = optionalTestHookContainer.orElseGet(() -> new TestHookContainer() {
      @Override
      public void addHook(final int priority,
                          final ThRunnable<?> hook) {
        throw new XtepsException("TestHookContainer implementation not found");
      }
    });
    final ExceptionHandler exceptionHandler = new ExceptionHandler.CleanStackTraceExceptionHandler();
    return new Config(
      properties,
      exceptionHandler,
      new StepExecutor.Default(exceptionHandler, uniqueByClass(listeners).toArray(new StepListener[0])),
      new TextFormatter.Default(
        patternContainsCapturingGroupsProperty(properties, "xteps.formatter.pattern", Pattern.compile("\\{([^}]*)}")),
        booleanProperty(properties, "xteps.formatter.fieldForceAccess", false),
        booleanProperty(properties, "xteps.formatter.methodForceAccess", false)
      ),
      testHooksContainer
    );
  }

  private static Properties systemPropertiesWithFile(final String propertiesFilePath) {
    final Properties properties = new Properties();
    try (final InputStream stream =
           ClassLoader.getSystemClassLoader().getResourceAsStream(propertiesFilePath)) {
      if (stream != null) {
        properties.load(stream);
      }
    } catch (final Exception ignored) { }
    try (final InputStream stream =
           Thread.currentThread().getContextClassLoader().getResourceAsStream(propertiesFilePath)) {
      if (stream != null) {
        properties.load(stream);
      }
    } catch (final Exception ignored) { }
    properties.putAll(System.getProperties());
    return properties;
  }

  static String stringProperty(final Properties properties,
                               final String propertyName,
                               final String defaultValue) {
    final String propertyValue = properties.getProperty(propertyName);
    if (propertyValue == null) {
      return defaultValue;
    }
    final String trimmedPropertyValue = propertyValue.trim();
    if (trimmedPropertyValue.isEmpty()) {
      return defaultValue;
    }
    return trimmedPropertyValue;
  }

  private static boolean booleanProperty(final Properties properties,
                                         final String propertyName,
                                         final boolean defaultValue) {
    final String propertyValue = properties.getProperty(propertyName);
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

  private static Pattern patternContainsCapturingGroupsProperty(final Properties properties,
                                                                final String propertyName,
                                                                final Pattern defaultValue) {
    final String propertyValue = properties.getProperty(propertyName);
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

  private static List<String> stringListProperty(final Properties properties,
                                                 final String propertyName,
                                                 final String delimiter,
                                                 final List<String> defaultValue) {
    final String propertyValue = properties.getProperty(propertyName);
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

  private static Optional<TestHookContainer> testHooksContainerBySPI() {
    try {
      final Iterator<TestHookContainer> iterator = ServiceLoader.load(TestHookContainer.class).iterator();
      if (iterator.hasNext()) {
        return Optional.of(iterator.next());
      }
    } catch (final Exception ex) {
      throw new XtepsException("Cannot instantiate TestHooksContainer by SPI cause " + ex, ex);
    }
    return Optional.empty();
  }

  private static Optional<TestHookContainer> testHooksContainerByProperty(final Properties properties,
                                                                          final String propertyName) {
    final String propertyValue = properties.getProperty(propertyName);
    if (propertyValue == null) {
      return Optional.empty();
    }
    final String trimmedPropertyValue = propertyValue.trim();
    if (trimmedPropertyValue.isEmpty()) {
      return Optional.empty();
    }
    try {
      return Optional.of((TestHookContainer) Class.forName(trimmedPropertyValue).getConstructor().newInstance());
    } catch (final Exception ex) {
      throw new XtepsException("Cannot instantiate TestHooksContainer " + propertyValue + " cause " + ex, ex);
    }
  }

  private static <T> List<T> uniqueByClass(final List<T> listeners) {
    final Set<Class<?>> classes = Collections.newSetFromMap(new IdentityHashMap<>(8));
    return listeners.stream()
      .filter(listener -> classes.add(listener.getClass()))
      .collect(Collectors.toList());
  }

  static final class Config {
    final Properties properties;
    final ExceptionHandler exceptionHandler;
    final StepExecutor stepExecutor;
    final TextFormatter textFormatter;
    final TestHookContainer testHookContainer;

    private Config(final Properties properties,
                   final ExceptionHandler exceptionHandler,
                   final StepExecutor stepExecutor,
                   final TextFormatter textFormatter,
                   final TestHookContainer testHookContainer) {
      this.properties = properties;
      this.exceptionHandler = exceptionHandler;
      this.stepExecutor = stepExecutor;
      this.textFormatter = textFormatter;
      this.testHookContainer = testHookContainer;
    }
  }
}
