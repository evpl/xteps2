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

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Text formatter.
 */
public interface TextFormatter {

  /**
   * Returns an object as a string.
   *
   * @param obj the object
   * @return string representation
   * @throws TextFormatException if it's impossible to convert given object to string correctly
   */
  String format(Object obj);

  /**
   * Formats given text with given replacements.
   *
   * @param text         the origin text
   * @param replacements the replacements
   * @return formatted text
   * @throws TextFormatException if it's impossible to format given text correctly
   */
  String format(String text,
                Map<String, Object> replacements);

  /**
   * Default {@code TextFormatter} implementation.
   */
  class Default implements TextFormatter {
    private final ExceptionHandler exceptionHandler;
    private final Pattern replacementPattern;
    private final boolean fieldForceAccess;
    private final boolean methodForceAccess;

    /**
     * Ctor.
     *
     * @param exceptionHandler   the exception handler
     * @param replacementPattern the replacement pattern
     * @param fieldForceAccess   the field force access flag
     * @param methodForceAccess  the method force access flag
     * @throws XtepsException if {@code exceptionHandler} is null
     *                        or if {@code replacementPattern} is null
     *                        or if {@code replacementPattern} contains less than one group
     */
    public Default(final ExceptionHandler exceptionHandler,
                   final Pattern replacementPattern,
                   final boolean fieldForceAccess,
                   final boolean methodForceAccess) {
      if (exceptionHandler == null) { throw new XtepsException("exceptionHandler arg is null"); }
      if (replacementPattern == null) { throw new XtepsException("replacementPattern arg is null"); }
      checkThatPatternContainsCapturingGroups(replacementPattern);
      this.exceptionHandler = exceptionHandler;
      this.replacementPattern = replacementPattern;
      this.fieldForceAccess = fieldForceAccess;
      this.methodForceAccess = methodForceAccess;
    }

    private static void checkThatPatternContainsCapturingGroups(final Pattern pattern) {
      if (pattern.matcher("").groupCount() < 1) {
        throw new XtepsException("replacementPattern arg " + pattern + " doesn't contain groups, " +
          "pattern must contain at least one group");
      }
    }

    @Override
    public final String format(final Object obj) {
      if (obj == null) {
        return "null";
      }
      final Class<?> cls = obj.getClass();
      try {
        if (cls.isArray()) {
          if (cls == byte[].class) {
            return Arrays.toString((byte[]) obj);
          } else if (cls == short[].class) {
            return Arrays.toString((short[]) obj);
          } else if (cls == int[].class) {
            return Arrays.toString((int[]) obj);
          } else if (cls == long[].class) {
            return Arrays.toString((long[]) obj);
          } else if (cls == char[].class) {
            return Arrays.toString((char[]) obj);
          } else if (cls == float[].class) {
            return Arrays.toString((float[]) obj);
          } else if (cls == double[].class) {
            return Arrays.toString((double[]) obj);
          } else if (cls == boolean[].class) {
            return Arrays.toString((boolean[]) obj);
          } else {
            return Arrays.deepToString((Object[]) obj);
          }
        }
        return obj.toString();
      } catch (final Exception ex) {
        throw handledEx(new TextFormatException(methodDesc(cls, "toString()") + " threw " + ex, ex));
      }
    }

    @Override
    public final String format(final String text,
                               final Map<String, Object> replacements) {
      if (text == null) { throw new XtepsException("text arg is null"); }
      if (replacements == null) { throw new XtepsException("replacements arg is null"); }
      if (text.isEmpty() || replacements.isEmpty()) {
        return text;
      }
      final StringBuffer stringBuffer = new StringBuffer();
      final Matcher matcher = this.replacementPattern.matcher(text);
      while (matcher.find()) {
        final String[] path = matcher.group(1).split("\\.");
        final String replacementPointer = path[0];
        if (replacements.containsKey(replacementPointer)) {
          final Object replacementValue = replacements.get(replacementPointer);
          matcher.appendReplacement(
            stringBuffer,
            Matcher.quoteReplacement(this.format(
              path.length == 1
                ? replacementValue
                : extractValue(path, replacementValue, this.fieldForceAccess, this.methodForceAccess)
            ))
          );
        }
      }
      matcher.appendTail(stringBuffer);
      return stringBuffer.toString();
    }

    private Object extractValue(final String[] path,
                                final Object firstPartValue,
                                final boolean fieldForceAccess,
                                final boolean methodForceAccess) {
      Object lastValue = firstPartValue;
      for (int idx = 1; idx < path.length; ++idx) {
        final String pathPart = path[idx];
        if (pathPart.indexOf('(') != -1 && pathPart.indexOf(')') != -1) {
          lastValue = methodValue(lastValue, pathPart, methodForceAccess);
        } else if (pathPart.indexOf('[') != -1 && pathPart.indexOf(']') != -1) {
          lastValue = containerValue(lastValue, pathPart);
        } else {
          lastValue = fieldValue(lastValue, pathPart, fieldForceAccess);
        }
      }
      return lastValue;
    }

    private Object containerValue(final Object obj,
                                  final String pathPart) {
      final int pathPartLength = pathPart.length();
      if (pathPartLength < 3 || pathPart.indexOf('[') != 0 || pathPart.indexOf(']') != pathPartLength - 1) {
        throw handledEx(new TextFormatException("Incorrect element index " + pathPart));
      }
      final int index;
      try {
        index = Integer.parseInt(pathPart.substring(1, pathPartLength - 1));
      } catch (final Exception ex) {
        throw handledEx(new TextFormatException("Incorrect element index " + pathPart));
      }
      if (obj == null) {
        throw handledEx(new TextFormatException("Cannot get element of null"));
      }
      if (obj.getClass().isArray()) {
        try {
          return Array.get(obj, index);
        } catch (final Exception ex) {
          throw handledEx(new TextFormatException("Cannot get array element by index " + index, ex));
        }
      } else if (obj instanceof List) {
        try {
          return ((List<?>) obj).get(index);
        } catch (final Exception ex) {
          throw handledEx(new TextFormatException("Cannot get List element by index " + index, ex));
        }
      } else if (obj instanceof Iterable) {
        final Iterator<?> iterator = ((Iterable<?>) obj).iterator();
        for (int idx = 0; iterator.hasNext(); ++idx) {
          if (idx == index) {
            return iterator.next();
          } else {
            iterator.next();
          }
        }
        throw handledEx(new TextFormatException("Cannot get Iterable element by index " + index));
      } else {
        throw handledEx(new TextFormatException("Cannot get element of " + obj.getClass()));
      }
    }

    private Object methodValue(final Object obj,
                               final String pathPart,
                               final boolean forceAccess) {
      if (pathPart.length() < 3 || !pathPart.endsWith("()")) {
        throw handledEx(new TextFormatException("Incorrect method " + pathPart));
      }
      if (obj == null) {
        throw handledEx(new TextFormatException(String.format("Cannot invoke %s method on null", pathPart)));
      }
      final Class<?> cls = obj.getClass();
      final Method method;
      try {
        method = findMethod(cls, pathPart.substring(0, pathPart.length() - 2), forceAccess);
      } catch (final Exception ex) {
        throw handledEx(new TextFormatException(String.format("Cannot get %s %s cause %s",
          methodDesc(cls, pathPart), forceAccessDesc(forceAccess), ex), ex));
      }
      if (method == null) {
        throw handledEx(new TextFormatException(String.format("Cannot find %s %s",
          methodDesc(cls, pathPart), forceAccessDesc(forceAccess))));
      }
      if (method.getReturnType() == void.class) {
        throw handledEx(new TextFormatException(String.format("Return type of %s is void", methodDesc(cls, pathPart))));
      }
      try {
        return invokeMethod(obj, method, forceAccess);
      } catch (final InvocationTargetException ex) {
        Throwable targetEx = (targetEx = ex.getCause()) != null ? targetEx : ex;
        throw handledEx(new TextFormatException(String.format("%s %s threw %s",
          methodDesc(cls, pathPart), forceAccessDesc(forceAccess), targetEx), targetEx));
      } catch (final Exception ex) {
        throw handledEx(new TextFormatException(String.format("Cannot invoke %s %s cause %s",
          methodDesc(cls, pathPart), forceAccessDesc(forceAccess), ex), ex));
      }
    }

    private static Method findMethod(final Class<?> cls,
                                     final String methodName,
                                     final boolean forceAccess) {
      try {
        return cls.getMethod(methodName);
      } catch (final NoSuchMethodException ignored) { }
      if (forceAccess) {
        for (Class<?> currentCls = cls; currentCls != null; currentCls = currentCls.getSuperclass()) {
          try {
            return currentCls.getDeclaredMethod(methodName);
          } catch (NoSuchMethodException ignored) { }
        }
      }
      return null;
    }

    private static Object invokeMethod(final Object obj,
                                       final Method method,
                                       final boolean forceAccess) throws InvocationTargetException, IllegalAccessException {
      if (forceAccess) {
        try {
          return method.invoke(obj);
        } catch (final IllegalAccessException ex) {
          method.setAccessible(true);
        }
      }
      return method.invoke(obj);
    }

    private Object fieldValue(final Object obj,
                              final String pathPart,
                              final boolean forceAccess) {
      if (obj == null) {
        throw handledEx(new TextFormatException(String.format("Cannot get %s field value of null", pathPart)));
      }
      final Class<?> cls = obj.getClass();
      final Field field;
      try {
        field = findField(cls, pathPart, forceAccess);
      } catch (final Exception ex) {
        throw handledEx(new TextFormatException(String.format("Cannot get %s cause %s",
          fieldDesc(cls, pathPart), ex), ex));
      }
      if (field == null) {
        throw handledEx(new TextFormatException(String.format("Cannot find %s %s",
          fieldDesc(cls, pathPart), forceAccessDesc(forceAccess))));
      }
      try {
        return getFieldValue(obj, field, forceAccess);
      } catch (final Exception ex) {
        throw handledEx(new TextFormatException(String.format("Cannot get %s value %s cause %s",
          fieldDesc(cls, pathPart), forceAccessDesc(forceAccess), ex), ex));
      }
    }

    private static Field findField(final Class<?> cls,
                                   final String fieldName,
                                   final boolean forceAccess) {
      try {
        return cls.getField(fieldName);
      } catch (final NoSuchFieldException ignored) { }
      if (forceAccess) {
        for (Class<?> currentCls = cls; currentCls != null; currentCls = currentCls.getSuperclass()) {
          try {
            return currentCls.getDeclaredField(fieldName);
          } catch (NoSuchFieldException ignored) { }
        }
      }
      return null;
    }

    private static Object getFieldValue(final Object obj,
                                        final Field field,
                                        final boolean forceAccess) throws IllegalAccessException {
      if (forceAccess) {
        try {
          return field.get(obj);
        } catch (final IllegalAccessException ex) {
          field.setAccessible(true);
        }
      }
      return field.get(obj);
    }

    private TextFormatException handledEx(final TextFormatException exception) {
      this.exceptionHandler.handle(exception);
      return exception;
    }

    private static String methodDesc(final Class<?> cls, final String method) {
      return cls.getTypeName() + " object " + method + " method";
    }

    private static String fieldDesc(final Class<?> cls, final String field) {
      return cls.getTypeName() + " object " + field + " field";
    }

    private static String forceAccessDesc(final boolean forceAccess) {
      return forceAccess ? "with force access" : "without force access";
    }
  }

  class Fake implements TextFormatter {

    public Fake() {
    }

    @Override
    public final String format(final Object obj) {
      return Objects.toString(obj);
    }

    @Override
    public final String format(final String text,
                               final Map<String, Object> replacements) {
      if (text == null) { throw new XtepsException("text arg is null"); }
      if (replacements == null) { throw new XtepsException("replacements arg is null"); }
      return text;
    }
  }
}
