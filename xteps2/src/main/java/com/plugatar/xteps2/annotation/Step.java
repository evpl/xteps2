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
 * Annotation to mark step methods or constructors. Method or constructor execution will be
 * logged as a step to Xteps.
 * <p>
 * You can use method or constructor arguments as step name replacements.
 * By index (like <i>{0}</i>, <i>{1}</i>) or by argument names (like <i>{methodFirstArgument}</i>, <i>{arg}</i>).
 * <table>
 * <caption>Other step name replacements</caption>
 * <tr><th> Replacement name </th><th> Description </th></tr>
 * <tr><td> <i>class</i>     </td><td> Returns the {@link Class} instance. </td></tr>
 * <tr><td> <i>method</i>    </td><td> Returns the {@link java.lang.reflect.Method} instance, only for method steps. </td></tr>
 * <tr><td> <i>ctor</i>      </td><td> Returns the {@link java.lang.reflect.Constructor} instance, only for ctor steps. </td></tr>
 * <tr><td> <i>this</i>      </td><td> Returns the currently executing object, only for non static methods. </td></tr>
 * </table>
 * Step {@code name} example:
 * <br><i>Step {class.getSimpleName()} {method.getName()} with args: {arg1}, {arg2.getArray()[10]}</i>
 *
 * @see Param
 * @see Artifact
 * @see NotImplemented
 * @see DefaultStep
 */
@Documented
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
public @interface Step {

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
   * The step keyword.
   *
   * @return step keyword
   * @see Keywords
   */
  Keywords keyword() default Keywords.NONE;

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

  /**
   * Ignore default step flag.
   *
   * @return ignore default step flag
   * @see DefaultStep
   */
  boolean ignoreDefault() default false;
}
