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
package org.gradle.api.internal.artifacts;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.gradle.api.artifacts.resolution.JvmLibraryJavadocArtifact;
import org.gradle.api.artifacts.resolution.JvmLibrarySourcesArtifact;
import org.gradle.api.artifacts.resolution.SoftwareArtifact;
import org.gradle.api.internal.artifacts.metadata.DefaultModuleVersionArtifactIdentifier;
import org.gradle.api.internal.artifacts.metadata.DefaultModuleVersionArtifactMetaData;
import org.gradle.api.internal.artifacts.metadata.ModuleVersionArtifactMetaData;
import org.gradle.api.internal.artifacts.metadata.ModuleVersionMetaData;

import java.util.Set;

public class MavenClassifierArtifactScheme {
    public Set<ModuleVersionArtifactMetaData> get(ModuleVersionMetaData module, Class<? extends SoftwareArtifact> artifactType) {
        if (artifactType == JvmLibraryJavadocArtifact.class) {
            return createArtifactMetaData(module, "javadoc", "javadoc");
        }

        if (artifactType == JvmLibrarySourcesArtifact.class) {
            return createArtifactMetaData(module, "source", "sources");
        }

        throw new IllegalArgumentException(String.format("Don't know how to get candidate artifacts of type %s", artifactType.getName()));
    }

    private Set<ModuleVersionArtifactMetaData> createArtifactMetaData(ModuleVersionMetaData module, String type, String classifier) {
        return ImmutableSet.<ModuleVersionArtifactMetaData>of(new DefaultModuleVersionArtifactMetaData(new DefaultModuleVersionArtifactIdentifier(module.getComponentId(), module.getId(), module.getId().getName(), type, "jar", ImmutableMap.of("classifier", classifier))));
    }
}
