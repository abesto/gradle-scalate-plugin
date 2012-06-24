package org.murtools

import groovy.util.logging.Slf4j;
import groovy.util.Proxy
import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.TaskAction

@Slf4j
class PrecompileTask extends DefaultTask {
	final def DEFAULT_SRC_DIR = 'src/main/webapp'
	final def DEFAULT_WORKDIR = 'build/scalate-out/work'
	final def DEFAULT_CLASSES = 'build/classes/main'
	final def NoArgs = null
	
	File templateSrcDir
	File workingDirectory
	File targetDirectory 
	List<String> templates
	String contextClass
	String bootClassName
	
	@TaskAction
	protected void start() {
		def targetDirectorySpec = targetDirectory ?: project.file(DEFAULT_CLASSES)
		def templateSrcDirSpec = templateSrcDir ?: project.file(DEFAULT_SRC_DIR)
		def workingDirectorySpec = workingDirectory ?: project.file(DEFAULT_WORKDIR)
		
		log.info("sources: given[$templateSrcDir], actual[$templateSrcDirSpec]")
		log.info("targetDirectory: given[$targetDirectory], actual[$targetDirectorySpec]")
		log.info("workingDirectory: given[$workingDirectory], actual[$workingDirectorySpec]")
		
		def classpathURLs = convertURL(project.files(project.configurations.compile, targetDirectorySpec))
		def precompiler = loadPrecompiler(classpathURLs)
		precompiler.invokeMethod('sources_$eq', templateSrcDirSpec)
		precompiler.invokeMethod('workingDirectory_$eq', workingDirectorySpec)
		precompiler.invokeMethod('targetDirectory_$eq', targetDirectorySpec)
		precompiler.invokeMethod('templates_$eq', templates?.toArray())
		precompiler.invokeMethod('contextClass_$eq', contextClass)
		precompiler.invokeMethod('bootClassName_$eq', bootClassName)
		precompiler.invokeMethod("execute", NoArgs);
	}

	private GroovyObject loadPrecompiler(URL[] classpathURLs) {
		def precompilerClassName = 'org.fusesource.scalate.support.Precompiler'

		ClassLoader parent = Thread.currentThread().getContextClassLoader()
		def precompiler = new URLClassLoader(classpathURLs, parent).loadClass(precompilerClassName).newInstance()
		
		return new Proxy().wrap(precompiler)
	}

	private URL[] convertURL(FileCollection files) {
		log.debug "${files*.path}"
		
		return files.collect {it.toURL()}
	}
}
