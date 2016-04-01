package com.github.festin666

import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.GradleException

class executeCodecept extends DefaultTask {
	def prefix = project.projectDir.path + "/vendor/bin/"
	def binary = 'codecept'
	def envConfig = new File ('tests/.env')
	@TaskAction
	def action() {
		if (Os.isFamily(Os.FAMILY_WINDOWS)) {
			binary = 'codecept.bat'
		}
		if (project.yii2build.dotEnvEnabled && !envConfig.exists()) {
			throw new GradleException("File " + envConfig.path + " is not exists.")
		}
		if (name == 'testUnit') {
			project.exec {
				workingDir 'tests'
				executable prefix + binary
				args 'run', 'unit'
			}
		} else if (name == 'buildCodeception') {
			project.exec {
				workingDir 'tests'
				executable prefix + binary
				args 'build'
			}	
		} else {
			throw new GradleException("Unknown type '" + name + "' for task typed executeCodecept.")
		}
	}
}
