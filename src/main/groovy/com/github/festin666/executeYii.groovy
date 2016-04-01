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
		if (project.hasProperty('testing')) {
			envConfig = new File ('tests/.env')
			prefix = 'tests/codeception/bin/'
		} else {
			envConfig = new File ('.env')
		}
		if (Os.isFamily(Os.FAMILY_WINDOWS)) {
			execYii = 'yii.bat'
		}
		if (project.yii2build.dotEnvEnabled && !envConfig.exists()) {
			throw new GradleException("File " + envConfig.path + " is not exists.")
		}
		if (name == 'migrateUp') {
			project.exec {
				executable prefix + execYii
				args 'migrate/up', '--interactive=0'
			}
			println ""
		} else if (name == 'migrateRbac') {
				def rbacMigrationsPath = '@app/vendor/yiisoft/yii2/rbac/migrations'
				if (project.yii2build.rbacMigrationsPath) {
					rbacMigrationsPath = project.yii2build.rbacMigrationsPath
				}
				project.exec {
					executable prefix + execYii
					args 'migrate/up', '--migrationPath=' + rbacMigrationsPath, '--interactive=0'
				}
			
			println ""
		} else {
			if (arg1 != '') {
				println "running command: " + arg1
				project.exec {
					executable prefix + execYii
					args arg1, arg2, arg3 // todo make arguments list dynamic
				}
			} else {
				println 'command for Yii console not specified.'
			}
		}
	}
}
