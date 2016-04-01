package com.github.festin666

import org.gradle.api.Project
import org.gradle.api.Plugin
import com.github.festin666.executeYii
import com.github.festin666.executeCodecept
import com.github.festin666.cInstall
import com.github.festin666.Yii2BuildPluginExtension
import org.gradle.api.file.FileCollection

class Yii2BuildPlugin implements Plugin<Project> {
	void apply (Project project) {
		project.extensions.create("yii2build", Yii2BuildPluginExtension)
		
		project.task('build') {
			dependsOn {['install', 'migrateUp', 'migrateRbac', 'createPerm', 'createAdmin']}
		}
		project.task ('test') {
			dependsOn {['install', 'migrateUp', 'migrateRbac', 'createPerm', 'createAdmin', 'testUnit']}
			def testing = true;
		}
		project.task ('testUnit', type: executeCodecept) {
			dependsOn {['buildCodeception']}
		}

		project.task ('install', type: cInstall) {
		}

		project.task ('migrateUp', type: executeYii, dependsOn: 'install') {
		}

		project.task ('migrateRbac', type: executeYii) {
			dependsOn {['install', 'migrateUp']}
			onlyIf {project.yii2build.rbacEnabled}
		}

		project.task ('createPerm', type: executeYii) {
			dependsOn {['install', 'migrateUp', 'migrateRbac']}
			onlyIf {project.yii2build.rbacEnabled}
			arg1 = ''
			doFirst {
				if (project.yii2build.commandCreatePermissions) {
					arg1 = project.yii2build.commandCreatePermissions
				} else {
					println "commandCreatePermissions not defined."
				}
			}
		}

		project.task ('createAdmin', type: executeYii) {
			dependsOn {['install', 'migrateUp', 'migrateRbac', 'createPerm']}
			onlyIf {project.yii2build.rbacEnabled}
			arg1 = ''
			doFirst {
				if (project.yii2build.commandCreateAdmin) {
						arg1 = project.yii2build.commandCreateAdmin
				} else {
					println "commandCreateAdmin not defined."
					arg1 = ''
				}
			}
		}

		project.task ('buildCodeception', type: executeCodecept) { 
			dependsOn {['install']}
			def confDir = 'tests/codeception'
			def genDir = 'tests/codeception/_support/_generated'
			FileCollection ymlConfigs = project.files(
				new File(confDir + '/unit.suite.yml'),
				new File(confDir + '/acceptance.suite.yml'),
				new File(confDir + '/functional.suite.yml'))
			FileCollection generatedActions = project.files(
				new File(genDir + '/UnitTesterActions.php'),
				new File(genDir + '/AcceptanceTesterActions.php'),
				new File(genDir + '/FunctionalTesterActions.php'))
			inputs.files ymlConfigs
			outputs.files generatedActions
		}
	}
}