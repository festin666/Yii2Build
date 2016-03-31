package com.github.festin666

import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.GradleException

class executeYii extends DefaultTask {
	def prefix = ''
	def execYii = './yii'
	def envConfig
	def arg1 = ''
	def arg2 = ''
	def arg3 = ''
	@TaskAction
	def action() {
		if (project.hasProperty('test')) {
			envConfig = new File ('tests/.env')
			prefix = 'tests/codeception/bin/'
		} else {
			envConfig = new File ('.env')
		}
		if (Os.isFamily(Os.FAMILY_WINDOWS)) {
			execYii = 'yii.bat'
		}
		if (envConfig.exists()) {
			if (name == 'migrateUp') {
				project.exec {
					executable prefix + execYii
					args 'migrate/up', '--interactive=0'
				}
			} else if (name == 'migrateRbac') {
				project.exec {
					executable prefix + execYii
					args 'migrate/up', '--migrationPath=@app/vendor/yiisoft/yii2/rbac/migrations', '--interactive=0'
				}
			} else {
				project.exec {
					executable prefix + execYii
					args arg1, arg2, arg3
				}
			}
		} else {
			throw new GradleException("File " + envConfig.path + " is not exists.")
		}
	}
}
