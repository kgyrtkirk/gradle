/*
 * Copyright 2013 the original author or authors.
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

package org.gradle.api.internal.tasks.compile.incremental.jar;

import org.gradle.api.internal.tasks.compile.incremental.deps.ClassDependents;
import org.gradle.api.internal.tasks.compile.incremental.deps.DependentsSet;
import org.gradle.api.tasks.incremental.InputFileDetails;

public class JarChangeDependentsFinder {

    private JarSnapshotFeeder jarSnapshotFeeder;

    public JarChangeDependentsFinder(JarSnapshotFeeder jarSnapshotFeeder) {
        this.jarSnapshotFeeder = jarSnapshotFeeder;
    }

    //TODO SF coverage
    public DependentsSet getActualDependents(InputFileDetails jarChangeDetails, JarArchive jarArchive) {
        JarSnapshot existing = jarSnapshotFeeder.changedJar(jarChangeDetails.getFile());
        if (jarChangeDetails.isAdded()) {
            return ClassDependents.emptyDependents();
        }

        if (existing == null) {
            //we don't know what classes were dependents of the jar in the previous build
            //for example, a class (in jar) with a constant might have changed into a class without a constant - we need to rebuild everything
            return ClassDependents.dependencyToAll();
        }

        if (jarChangeDetails.isRemoved()) {
            return ClassDependents.dependencyToAll();
        }

        if (jarChangeDetails.isModified()) {
            JarSnapshot snapshotNoDeps = jarSnapshotFeeder.newSnapshotWithoutDependents(jarArchive);
            return existing.getDependentsDelta(snapshotNoDeps);
        }

        throw new IllegalArgumentException("Unknown input file details provided: " + jarChangeDetails);
    }
}