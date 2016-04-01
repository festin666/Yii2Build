# Yii2Build
Gradle plugin for build basic Yii2 projects

You can use build.gradle like that (imports is my bad, I'll correct in pugin):

    import com.github.festin666.executeYii
    import com.github.festin666.executeCodecept
    import com.github.festin666.cInstall
    
    plugins {
      id "com.github.festin666.yii2build" version "0.1"
    }
    
    task build () {
    	dependsOn {['install', 'migrateUp', 'migrateRbac', 'createPerm', 'createAdmin']}
    }
    task test() {
    	dependsOn {['install', 'migrateUp', 'migrateRbac', 'createPerm', 'createAdmin', 'testUnit']}
    	def test = true;
    }
    
    task createPerm (type: executeYii, dependsOn: ['install', 'migrateUp', 'migrateRbac']) {
    	arg1 = 'perm/create-hierarchy'
    }
    
    task createAdmin (type: executeYii, dependsOn: ['install', 'migrateUp', 'migrateRbac', 'createPerm']) {
    	arg1 = 'perm/create-admin'
    }
    
    task testUnit (type: executeCodecept) {
    	dependsOn {['buildCodeception']}
    }
    
    task install(type: cInstall) {
    }
    
    task migrateUp (type: executeYii, dependsOn: install) {
    }
    
    task migrateRbac (type: executeYii) {
    	dependsOn {['install', 'migrateUp']}
    }
    
    task buildCodeception(type: executeCodecept) { 
    	dependsOn {['install']}
    	def confDir = 'tests/codeception'
    	def genDir = 'tests/codeception/_support/_generated'
    	FileCollection ymlConfigs = files(
    		new File(confDir + '/unit.suite.yml'),
    		new File(confDir + '/acceptance.suite.yml'),
    		new File(confDir + '/functional.suite.yml'))
    	FileCollection generatedActions = files(
    		new File(genDir + '/UnitTesterActions.php'),
    		new File(genDir + '/AcceptanceTesterActions.php'),
    		new File(genDir + '/FunctionalTesterActions.php'))
    	inputs.files ymlConfigs
    	outputs.files generatedActions
    }
