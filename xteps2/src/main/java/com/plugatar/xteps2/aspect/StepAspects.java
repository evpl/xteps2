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
import com.plugatar.xteps2.core.TextFormatException;
import com.plugatar.xteps2.core.TextFormatter;
import com.plugatar.xteps2.core.XtepsException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.ConstructorSignature;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.plugatar.xteps2.XtepsBase.exceptionHandler;
import static com.plugatar.xteps2.XtepsBase.stepReporter;

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
   */
  @Pointcut("@annotation(com.plugatar.xteps2.annotation.Step)")
  public final void withStepAnnotation() {
  }

  /**
   * <em>Before</em> advice for static method annotated with {@link Step} annotation.
   *
   * @param joinPoint the join point
   * @throws XtepsException              if Xteps configuration is incorrect
   * @throws TextFormatException         if if it's impossible to format <em>name</em> or <em>desc</em> artifacts
   *                                     correctly
   * @throws StepNotImplementedException if method annotated with {@link NotImplemented} annotation
   */
  @Before(value = "withStepAnnotation() && staticMethod()")
  public final void staticMethodStepStart(final JoinPoint joinPoint) {
    final TextFormatter formatter = XtepsBase.textFormatter();
    final Class<?> cls = joinPoint.getSignature().getDeclaringType();
    final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    final Method method = signature.getMethod();
    final Object[] args = joinPoint.getArgs();
    final Step step = method.getAnnotation(Step.class);
    final DefaultStep defaultStep = cls.getAnnotation(DefaultStep.class);
    final Map<String, Object> params = new LinkedHashMap<>();
    final Map<String, Object> replacements = replacements(
      step, defaultStep, signature.getParameterNames(),
      args, method.getParameterAnnotations(), params
    );
    replacements.putIfAbsent("class", cls);
    replacements.putIfAbsent("method", method);
    replacements.putIfAbsent("args", args);
    stepReporter().startStep(artifactMap(
      step,
      defaultStep,
      keyword(step, defaultStep),
      formatter.format(name(step, defaultStep, signature.getName()), replacements),
      params,
      formatter.format(desc(step, defaultStep), replacements),
      replacements
    ));
    if (method.isAnnotationPresent(NotImplemented.class)) {
      final StepNotImplementedException exception = new StepNotImplementedException();
      exceptionHandler().handle(exception);
      throw exception;
    }
  }

  /**
   * <em>Before</em> advice for non-static method annotated with {@link Step} annotation.
   *
   * @param joinPoint the join point
   * @throws XtepsException              if Xteps configuration is incorrect
   * @throws TextFormatException         if if it's impossible to format <em>name</em> or <em>desc</em> artifacts
   *                                     correctly
   * @throws StepNotImplementedException if method annotated with {@link NotImplemented} annotation
   */
  @Before(value = "withStepAnnotation() && nonStaticMethod()")
  public final void nonStaticMethodStepStart(final JoinPoint joinPoint) {
    final TextFormatter formatter = XtepsBase.textFormatter();
    final Class<?> cls = joinPoint.getSignature().getDeclaringType();
    final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    final Method method = signature.getMethod();
    final Object[] args = joinPoint.getArgs();
    final Step step = method.getAnnotation(Step.class);
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
    stepReporter().startStep(artifactMap(
      step,
      defaultStep,
      keyword(step, defaultStep),
      formatter.format(name(step, defaultStep, signature.getName()), replacements),
      params,
      formatter.format(desc(step, defaultStep), replacements),
      replacements
    ));
    if (method.isAnnotationPresent(NotImplemented.class)) {
      final StepNotImplementedException exception = new StepNotImplementedException();
      exceptionHandler().handle(exception);
      throw exception;
    }
  }

  /**
   * <em>Before</em> advice for constructor annotated with {@link Step} annotation.
   *
   * @param joinPoint the join point
   * @throws XtepsException              if Xteps configuration is incorrect
   * @throws TextFormatException         if if it's impossible to format <em>name</em> or <em>desc</em> artifacts
   *                                     correctly
   * @throws StepNotImplementedException if method annotated with {@link NotImplemented} annotation
   */
  @Before(value = "withStepAnnotation() && constructor()")
  public final void constructorStepStart(final JoinPoint joinPoint) {
    final TextFormatter formatter = XtepsBase.textFormatter();
    final Class<?> cls = joinPoint.getSignature().getDeclaringType();
    final ConstructorSignature signature = (ConstructorSignature) joinPoint.getSignature();
    final Constructor<?> ctor = signature.getConstructor();
    final Object[] args = joinPoint.getArgs();
    final Step step = ctor.getAnnotation(Step.class);
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
    stepReporter().startStep(artifactMap(
      step,
      defaultStep,
      keyword(step, defaultStep),
      formatter.format(name(step, defaultStep, CTOR_STEP_NAME_PREFIX + cls.getSimpleName()), replacements),
      params,
      formatter.format(desc(step, defaultStep), replacements),
      replacements
    ));
    if (ctor.isAnnotationPresent(NotImplemented.class)) {
      final StepNotImplementedException exception = new StepNotImplementedException();
      exceptionHandler().handle(exception);
      throw exception;
    }
  }

  /**
   * <em>AfterReturning</em> advice for any method or constructor annotated with {@link Step} annotation.
   *
   * @throws XtepsException if Xteps configuration is incorrect
   */
  @AfterReturning(value = "withStepAnnotation() && (staticMethod() || nonStaticMethod() || constructor())")
  public void stepPassed() {
    stepReporter().passStep();
  }

  /**
   * <em>AfterThrowing</em> advice for any method or constructor annotated with {@link Step} annotation.
   *
   * @param exception the step exception
   * @throws XtepsException if Xteps configuration is incorrect
   */
  @AfterThrowing(value = "withStepAnnotation() && (staticMethod() || nonStaticMethod() || constructor())", throwing = "exception")
  public void stepFailed(final Throwable exception) {
    stepReporter().failStep(exception);
  }

  private static Keyword keyword(final Step step,
                                 final DefaultStep defaultStep) {
    String keywordStr = step.keywordStr();
    if (keywordStr.isEmpty() && !step.ignoreDefault() && defaultStep != null) {
      keywordStr = defaultStep.keywordStr();
    }
    if (!keywordStr.isEmpty()) {
      return new Keyword.Of(keywordStr);
    }
    Keywords keyword = step.keyword();
    if (keyword == Keywords.NONE && !step.ignoreDefault() && defaultStep != null) {
      keyword = defaultStep.keyword();
    }
    return keyword;
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

  private static Map<String, Object> artifactMap(final Step step,
                                                 final DefaultStep defaultStep,
                                                 final Keyword keyword,
                                                 final String name,
                                                 final Map<String, ?> params,
                                                 final String desc,
                                                 final Map<String, Object> replacements) {
    final Map<String, Object> map = new HashMap<>();
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
