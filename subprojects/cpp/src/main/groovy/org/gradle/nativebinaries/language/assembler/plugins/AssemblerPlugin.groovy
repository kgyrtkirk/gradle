/*
 * Copyright 2011 the original author or authors.
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
package org.gradle.nativebinaries.language.assembler.plugins

import org.gradle.api.Action
import org.gradle.api.Incubating
import org.gradle.api.Plugin
import org.gradle.api.internal.project.ProjectInternal
import org.gradle.model.ModelRule
import org.gradle.model.ModelRules
import org.gradle.nativebinaries.toolchain.Clang
import org.gradle.nativebinaries.toolchain.Gcc
import org.gradle.nativebinaries.toolchain.internal.ToolChainRegistryInternal
import org.gradle.nativebinaries.toolchain.internal.ToolType
import org.gradle.nativebinaries.toolchain.internal.plugins.StandardToolChainsPlugin
import org.gradle.nativebinaries.toolchain.internal.tools.DefaultTool

import javax.inject.Inject

/**
 * A plugin for projects wishing to build native binary components from Assembly language sources.
 *
 * <p>Adds core tool chain support to the {@link AssemblerNativeBinariesPlugin}.</p>
 */
@Incubating
class AssemblerPlugin implements Plugin<ProjectInternal> {


    private ModelRules modelRules

    @Inject
    public AssemblerPlugin(ModelRules modelRules){
        this.modelRules = modelRules
    }

    void apply(ProjectInternal project) {
        project.plugins.apply(StandardToolChainsPlugin)

        modelRules.rule(new ModelRule() {
            void addAssembler(ToolChainRegistryInternal toolChainRegistry) {
                toolChainRegistry.withType(Clang).all(new Action<Clang>(){
                    void execute(Clang toolchain) {
                        toolchain.add(new DefaultTool("assembler", ToolType.ASSEMBLER, "as"));
                    }
                })

                toolChainRegistry.withType(Gcc).all(new Action<Gcc>(){
                    void execute(Gcc toolchain) {
                        toolchain.add(new DefaultTool("assembler", ToolType.ASSEMBLER, "as"));
                    }
                })
            }
        });

        project.plugins.apply(AssemblerNativeBinariesPlugin)
    }
}