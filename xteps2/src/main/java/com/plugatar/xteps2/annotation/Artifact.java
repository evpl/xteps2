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

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Custom step artifact.
 *
 * @see Step#artifacts()
 * @see DefaultStep#artifacts()
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Artifact {

  /**
   * The artifact name.
   *
   * @return artifact name
   */
  String name();

  /**
   * The artifact value.
   *
   * @return artifact value
   */
  String value();
}
