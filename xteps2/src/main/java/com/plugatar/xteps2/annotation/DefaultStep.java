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
package com.plugatar.xteps2.annotation;

import com.plugatar.xteps2.Keywords;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Default step.
 *
 * @see Step
 * @see Param
 * @see Artifact
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DefaultStep {

  /**
   * The step name. Overridden by {@link #name()} when specified.
   *
   * @return step name
   */
  String value() default "";

  /**
   * The step name. Overrides {@link #value()} when specified.
   *
   * @return step name
   */
  String name() default "";

  /**
   * The step keyword. Overridden by {@link #keywordStr()} when specified.
   *
   * @return step keyword
   * @see Keywords
   */
  Keywords keyword() default Keywords.NONE;

  /**
   * The custom step keyword. Overrides {@link #keyword()} when specified.
   *
   * @return custom step keyword
   */
  String keywordStr() default "";

  /**
   * The step description.
   *
   * @return step description
   */
  String desc() default "";

  /**
   * The list of step parameters.
   *
   * @return list of step parameters
   * @see Param
   */
  Param[] params() default {};

  /**
   * The list of additional step artifacts.
   *
   * @return list of step artifacts
   * @see Artifact
   */
  Artifact[] artifacts() default {};
}
