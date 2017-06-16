package org.murtools.gradle.plugins.scalate

import groovy.util.logging.Slf4j;
import groovy.util.Proxy
import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.TaskAction

@Slf4j
class PrecompileTask extends DefaultTask {
	final def NoArgs = null

	@TaskAction
	protected void start() {
		def classpathURLs = convertURL(project.files(project.configurations.compile, project.scalate.generatedDir))
        logger.info("Precompiling Scalate templates. sources=${project.scalate.templateSrcDir} targetDirectory=${project.scalate.generatedDir} packagePrefix=${project.scalate.packagePrefix} logConfig=${project.scalate.loggingConfig}")

		def precompiler = loadPrecompiler(classpathURLs)
		precompiler.invokeMethod('sources_$eq', project.file(project.scalate.templateSrcDir))
		precompiler.invokeMethod('targetDirectory_$eq', project.file(project.scalate.generatedDir))
		precompiler.invokeMethod('packagePrefix_$eq', project.scalate.packagePrefix)
		precompiler.invokeMethod('logConfig_$eq', project.file(project.scalate.loggingConfig))
		precompiler.invokeMethod("execute", NoArgs);
	}

	private static GroovyObject loadPrecompiler(URL[] classpathURLs) {
		def precompilerClassName = 'org.fusesource.scalate.Precompiler'

        def oldClassLoader = Thread.currentThread().contextClassLoader
		def newClassLoader = new URLClassLoader(classpathURLs, oldClassLoader)
		Thread.currentThread().contextClassLoader = newClassLoader
		def precompiler = newClassLoader.loadClass(precompilerClassName).newInstance()
		Thread.currentThread().contextClassLoader = oldClassLoader

		return new Proxy().wrap(precompiler)
	}

	private static URL[] convertURL(FileCollection files) {
		log.debug "${files*.path}"
		return files.collect {it.toURL()}
	}
}
