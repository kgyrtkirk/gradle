/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.play.internal.scala.reflection.util;


import org.gradle.internal.Cast;

import java.lang.reflect.Method;

public class ScalaOptionInvocationWrapper<T> {
    private final Object obj;


    public ScalaOptionInvocationWrapper(Object obj) {
        this.obj = obj;
    }

    public boolean isDefined() {
        try {
            Method resultIsDefined = obj.getClass().getMethod("isDefined");
            return (Boolean) resultIsDefined.invoke(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public T get() {
        try {
            return Cast.uncheckedCast(obj.getClass().getMethod("get").invoke(obj));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
