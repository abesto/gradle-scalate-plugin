package org.murtools.gradle.plugins.scalate.test

import org.fusesource.scalate._

class Controller {
	def execute(p : Person): String = {
		val te = new TemplateEngine
		te.packagePrefix = "scalate"
		te.layout("user.ssp", Map("user" -> p))
	}
}