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

package org.gradle.play.internal.twirl.spec;

import com.google.common.base.Function;
import org.gradle.play.internal.scala.reflection.util.ScalaCodecMapper;
import org.gradle.play.internal.scala.reflection.util.ScalaUtil;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

public class TwirlCompileSpecV10X extends DefaultTwirlCompileSpec {

    private String codec = "UTF-8";

    private boolean inclusiveDots;
    private boolean useOldParser;

    public TwirlCompileSpecV10X(File sourceDirectory, Iterable<File> sources, File destinationDir, Iterable<File> compileClasspath, boolean fork, boolean javaProject) {
        super(sourceDirectory, sources, destinationDir, compileClasspath, fork, javaProject);
    }

    protected String defaultFormatterType() {
        return "play.twirl.api.HtmlFormat";
    }

    protected String defaultScalaAdditionalImports(String format) {
        return String.format("import models._;import controllers._;import play.api.i18n._;import play.api.mvc._;import play.api.data._;import views.%s._;", "html");
    }
    protected String defaultJavaAdditionalImports(String format) {
        return String.format("import models._;import controllers._;import java.lang._;import java.util._;import scala.collection.JavaConversions._;import scala.collection.JavaConverters._;import play.api.i18n._;import play.core.j.PlayMagicForJava._;import play.mvc._;import play.data._;import play.api.data.Field;import play.mvc.Http.Context.Implicit._;import views.%s._;", "html");
    }


    public boolean isUseOldParser() {
        return useOldParser;
    }

    public boolean isInclusiveDots() {
        return inclusiveDots;
    }

    public String getCodec() {
        return codec;
    }

    public void setCodec(String codec) {
        this.codec = codec;
    }

    public Function<Object[], Object> getCompileMethod(ClassLoader cl) throws ClassNotFoundException {
        return ScalaUtil.scalaObjectFunction(
                cl,
                "play.twirl.compiler.TwirlCompiler",
                "compile",
                new Class<?>[]{
                        File.class,
                        File.class,
                        File.class,
                        String.class,
                        String.class,
                        cl.loadClass(ScalaCodecMapper.getClassName()),
                        boolean.class,
                        boolean.class
                }
        );
    }

    public Object[] createCompileParameters(ClassLoader cl, File file) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return new Object[] {
                file,
                getSourceDirectory(),
                getDestinationDir(),
                getFormatterType(),
                getAdditionalImports(),
                ScalaCodecMapper.create(cl, getCodec()),
                isInclusiveDots(),
                isUseOldParser()
        };
    }

    public List<String> getClassLoaderPackages() {
        return Arrays.asList("play.twirl.compiler", "scala.io"); //scala.io is for Codec which is a parameter to twirl
    };
}
