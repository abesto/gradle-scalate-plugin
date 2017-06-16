package org.murtools.gradle.plugins.scalate

import groovy.util.logging.Slf4j
import org.gradle.api.Plugin
import org.gradle.api.Project

@Slf4j
class ScalatePlugin implements Plugin<Project> {

    static final String SCALATE_PRECOMPILE = 'scalatePreCompile'

    @Override
    void apply(Project project) {
        project.extensions.create('scalate', ScalatePluginExtension)

        PrecompileTask precompileTask = project.tasks.create(SCALATE_PRECOMPILE, PrecompileTask)
        precompileTask.description = 'Precompile the scalate templates'
        precompileTask.group = 'Scalate'
        precompileTask.inputs.dir project.scalate.templateSrcDir
        precompileTask.outputs.dir project.scalate.generatedDir

        project.compileScala.dependsOn precompileTask
        project.sourceSets.main.scala.srcDir precompileTask.outputs

        project.clean.delete project.scalate.generatedDir
    }
}

class ScalatePluginExtension {
    String templateSrcDir = 'src/main/webapp'
    String generatedDir = "build/scalate-gen"
    String loggingConfig = 'src/main/resources/logback.xml'
    String packagePrefix = 'scalate'
}
