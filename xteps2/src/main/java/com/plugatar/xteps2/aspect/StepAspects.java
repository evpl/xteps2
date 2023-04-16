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
package com.plugatar.xteps2.aspect;

import com.plugatar.xteps2.Artifacts;
import com.plugatar.xteps2.Keywords;
import com.plugatar.xteps2.XtepsBase;
import com.plugatar.xteps2.annotation.Artifact;
import com.plugatar.xteps2.annotation.DefaultStep;
import com.plugatar.xteps2.annotation.NotImplemented;
import com.plugatar.xteps2.annotation.Param;
import com.plugatar.xteps2.annotation.Step;
import com.plugatar.xteps2.core.Keyword;
import com.plugatar.xteps2.core.StepNotImplementedException;
import com.plugatar.xteps2.core.function.ThSupplier;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.ConstructorSignature;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Step aspects.
 *
 * @see Step
 * @see DefaultStep
 * @see NotImplemented
 * @see Param
 * @see Artifact
 */
@Aspect
public class StepAspects {
  private static final String PARAM_MASK = "********";
  private static final String CTOR_STEP_NAME_PREFIX = "new ";

  /**
   * Pointcut for static method.
   */
  @Pointcut("execution(static * *(..))")
  public final void staticMethod() {
  }

  /**
   * Pointcut for non-static method.
   */
  @Pointcut("execution(!static * *(..))")
  public final void nonStaticMethod() {
  }

  /**
   * Pointcut for constructor.
   */
  @Pointcut("execution(*.new(..))")
  public final void constructor() {
  }

  /**
   * Pointcut for {@link Step} annotation.
   *
   * @param step the step annotation
   */
  @Pointcut("@annotation(step)")
  public final void withStepAnnotation(final Step step) {
  }

  /**
   * Around advice for static method annotated with {@link Step} annotation.
   *
   * @param joinPoint the join point
   * @param step      the step annotation
   * @return method result
   * @throws StepNotImplementedException if method annotated with {@link NotImplemented} annotation
   * @throws Throwable                   if method threw exception
   */
  @Around(value = "staticMethod() && withStepAnnotation(step)", argNames = "joinPoint,step")
  public final Object staticMethodStep(final ProceedingJoinPoint joinPoint,
                                       final Step step) throws Throwable {
    final Class<?> cls = joinPoint.getSignature().getDeclaringType();
    final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    final Method method = signature.getMethod();
    final Object[] args = joinPoint.getArgs();
    final DefaultStep defaultStep = cls.getAnnotation(DefaultStep.class);
    final Map<String, Object> params = new LinkedHashMap<>();
    final Map<String, Object> replacements = replacements(
      step,
      defaultStep,
      signature.getParameterNames(),
      args,
      method.getParameterAnnotations(),
      params
    );
    replacements.putIfAbsent("class", cls);
    replacements.putIfAbsent("method", method);
    replacements.putIfAbsent("args", args);
    return XtepsBase.stepExecutor().report(
      artifactMap(
        step,
        defaultStep,
        keyword(step, defaultStep),
        name(step, defaultStep, signature.getName()),
        params,
        desc(step, defaultStep),
        replacements
      ),
      action(joinPoint, method.getAnnotation(NotImplemented.class))
    );
  }

  /**
   * Around advice for non-static method annotated with {@link Step} annotation.
   *
   * @param joinPoint the join point
   * @param step      the step annotation
   * @return method result
   * @throws StepNotImplementedException if method annotated with {@link NotImplemented} annotation
   * @throws Throwable                   if method threw exception
   */
  @Around(value = "nonStaticMethod() && !constructor() && withStepAnnotation(step)", argNames = "joinPoint,step")
  public final Object nonStaticMethodStep(final ProceedingJoinPoint joinPoint,
                                          final Step step) throws Throwable {
    final Class<?> cls = joinPoint.getSignature().getDeclaringType();
    final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    final Method method = signature.getMethod();
    final Object[] args = joinPoint.getArgs();
    final DefaultStep defaultStep = cls.getAnnotation(DefaultStep.class);
    final Map<String, Object> params = new LinkedHashMap<>();
    final Map<String, Object> replacements = replacements(
      step,
      defaultStep,
      signature.getParameterNames(),
      args,
      method.getParameterAnnotations(),
      params
    );
    replacements.putIfAbsent("class", cls);
    replacements.putIfAbsent("method", method);
    replacements.putIfAbsent("args", args);
    replacements.putIfAbsent("this", joinPoint.getThis());
    return XtepsBase.stepExecutor().report(
      artifactMap(
        step,
        defaultStep,
        keyword(step, defaultStep),
        name(step, defaultStep, signature.getName()),
        params,
        desc(step, defaultStep),
        replacements
      ),
      action(joinPoint, method.getAnnotation(NotImplemented.class))
    );
  }

  /**
   * Around advice for constructor annotated with {@link Step} annotation.
   *
   * @param joinPoint the join point
   * @param step      the step annotation
   * @return constructor result
   * @throws StepNotImplementedException if constructor annotated with {@link NotImplemented} annotation
   * @throws Throwable                   if constructor threw exception
   */
  @Around(value = "constructor() && withStepAnnotation(step)", argNames = "joinPoint,step")
  public final Object ctorStep(final ProceedingJoinPoint joinPoint,
                               final Step step) throws Throwable {
    final Class<?> cls = joinPoint.getSignature().getDeclaringType();
    final ConstructorSignature signature = (ConstructorSignature) joinPoint.getSignature();
    final Constructor<?> ctor = signature.getConstructor();
    final Object[] args = joinPoint.getArgs();
    final DefaultStep defaultStep = cls.getAnnotation(DefaultStep.class);
    final Map<String, Object> params = new LinkedHashMap<>();
    final Map<String, Object> replacements = replacements(
      step,
      defaultStep,
      signature.getParameterNames(),
      args,
      ctor.getParameterAnnotations(),
      params
    );
    replacements.putIfAbsent("class", cls);
    replacements.putIfAbsent("ctor", ctor);
    replacements.putIfAbsent("args", args);
    return XtepsBase.stepExecutor().report(
      artifactMap(
        step,
        defaultStep,
        keyword(step, defaultStep),
        name(step, defaultStep, CTOR_STEP_NAME_PREFIX + cls.getSimpleName()),
        params,
        desc(step, defaultStep),
        replacements
      ),
      action(joinPoint, ctor.getAnnotation(NotImplemented.class))
    );
  }

  private static Keyword keyword(final Step step,
                                 final DefaultStep defaultStep) {
    final Keywords keyword = step.keyword();
    return keyword == Keywords.NONE && !step.ignoreDefault() && defaultStep != null
      ? defaultStep.keyword()
      : keyword;
  }

  private static String name(final Step step,
                             final DefaultStep defaultStep,
                             final String methodName) {
    String name = step.name();
    if (name.isEmpty()) {
      name = step.value();
    }
    if (name.isEmpty() && !step.ignoreDefault() && defaultStep != null) {
      name = defaultStep.name();
      if (name.isEmpty()) {
        name = defaultStep.value();
      }
    }
    return name.isEmpty() ? methodName : name;
  }

  private static String desc(final Step step,
                             final DefaultStep defaultStep) {
    final String desc = step.desc();
    return desc.isEmpty() && !step.ignoreDefault() && defaultStep != null
      ? defaultStep.desc()
      : desc;
  }

  private static Map<String, Object> replacements(final Step step,
                                                  final DefaultStep defaultStep,
                                                  final String[] paramNames,
                                                  final Object[] paramValues,
                                                  final Annotation[][] paramAnnotations,
                                                  final Map<String, Object> paramsToAdd) {
    final Map<String, Object> replacements = new HashMap<>();
    // Not hidden custom params
    if (defaultStep != null && !step.ignoreDefault()) {
      for (final Param param : defaultStep.params()) {
        if (!param.hidden()) {
          paramsToAdd.put(param.name(), param.masked() ? PARAM_MASK : param.value());
        }
      }
    }
    for (final Param param : step.params()) {
      if (!param.hidden()) {
        paramsToAdd.put(param.name(), param.masked() ? PARAM_MASK : param.value());
      }
    }
    // Arg params
    for (int idx = 0; idx < paramValues.length; ++idx) {
      if (!paramsToAdd.containsKey(paramNames[idx])) {
        Param paramAnnotation = null;
        for (final Annotation currentAnnotation : paramAnnotations[idx]) {
          if (currentAnnotation instanceof Param) {
            paramAnnotation = (Param) currentAnnotation;
          }
        }
        if (paramAnnotation == null) {
          paramsToAdd.put(paramNames[idx], paramValues[idx]);
          replacements.put(String.valueOf(idx), paramValues[idx]);
        } else {
          String name = paramAnnotation.name();
          if (name.isEmpty()) {
            name = paramNames[idx];
          }
          Object value = paramAnnotation.value();
          if (((String) value).isEmpty()) {
            value = paramValues[idx];
          }
          if (!paramAnnotation.hidden()) {
            paramsToAdd.put(name, paramAnnotation.masked() ? PARAM_MASK : value);
            replacements.put(String.valueOf(idx), paramAnnotation.masked() ? PARAM_MASK : value);
          }
        }
      }
    }
    // Hidden params
    if (defaultStep != null && !step.ignoreDefault()) {
      for (final Param param : defaultStep.params()) {
        if (param.hidden()) {
          paramsToAdd.remove(param.name());
        }
      }
    }
    for (final Param param : step.params()) {
      if (param.hidden()) {
        paramsToAdd.remove(param.name());
      }
    }
    replacements.putAll(paramsToAdd);
    return replacements;
  }

  private static ThSupplier<Object, Throwable> action(final ProceedingJoinPoint joinPoint,
                                                      final NotImplemented notImplemented) {
    return notImplemented == null
      ? joinPoint::proceed
      : () -> { throw new StepNotImplementedException(); };
  }

  private static Map<String, Object> artifactMap(final Step step,
                                                 final DefaultStep defaultStep,
                                                 final Keyword keyword,
                                                 final String name,
                                                 final Map<String, ?> params,
                                                 final String desc,
                                                 final Map<String, Object> replacements) {
    final Map<String, Object> map = new LinkedHashMap<>();
    map.put(Artifacts.keywordArtifact(), keyword);
    map.put(Artifacts.nameArtifact(), name);
    map.put(Artifacts.paramsArtifact(), params);
    map.put(Artifacts.descArtifact(), desc);
    map.put(Artifacts.replacementsArtifact(), replacements);
    if (defaultStep != null && !step.ignoreDefault()) {
      for (final Artifact artifact : defaultStep.artifacts()) {
        map.put(artifact.name(), artifact.value());
      }
    }
    for (final Artifact artifact : step.artifacts()) {
      map.put(artifact.name(), artifact.value());
    }
    return map;
  }
}
