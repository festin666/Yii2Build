package com.github.festin666

import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.GradleException

class cInstall extends DefaultTask {
	String prefix = project.projectDir.path + "/vendor/bin/"	
	String executableComposer = 'composer'
	@InputFiles
	def input = project.files(new File("composer.json"))
	@OutputDirectory
	def output = project.file("vendor/")
	@TaskAction
	def install() {
		input.each{File f ->
			if (!f.exists()) {
				throw new GradleException("File " + f.path + " is not exists.")
			}
		}
		if (Os.isFamily(Os.FAMILY_WINDOWS)) {
			executableComposer = 'composer.bat'
		}
		project.exec {
			executable executableComposer
			args 'install'
		}
	}
}
