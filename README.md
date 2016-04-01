# Yii2Build
Gradle plugin for build basic Yii2 projects

Yii2 PHP projects have many things when deploy - composer update, migrations for DB, tests rebuild, add default user, ... I forget something sometimes. Deploying from scratch like a hell for me.

Now, you can place into build.gradle this code:

	plugins {
	  id "com.github.festin666.yii2build" version "0.2-2"
	}

	yii2build {
		dotEnvEnabled = true
		rbacEnabled = true
		rbacMigrationsPath = '@app/vendor/yiisoft/yii2/rbac/migrations'
		commandCreatePermissions = 'perm/create-hierarchy'
		commandCreateAdmin = 'perm/create-admin'
	}
	
And run `gradle build` for:

- Install PHP dependencies by composer.
- Roll up migrations by Yii2 internal system.
- Roll up migrations for Yii2 RBAC.
- Create RBAC permissions by your own command.
- Create default user by your own command.

If you want to test your project, run `gradle test` for:

- Roll up migrations by Yii2 internal system.
- Roll up migrations for Yii2 RBAC.
- Create RBAC permissions by your own command.
- Create default user by your own command.
- Run codeception unit tests.

All database operations in `test` task performs in test database (configured in tests/codeception/config/config.php).

You can only prepare test environment for usage (without running tests) by specifying `-Ptesting` for gradle: `gradle build -Ptesting`.

Config description:

- `dotEnvEnabled` - is dotEnv (https://github.com/vlucas/phpdotenv) used or not.
- `rbacEnabled` - is RBAC (http://www.yiiframework.com/doc-2.0/guide-security-authorization.html#rbac) used or not.
- `rbacMigrationsPath` - path to Yii2 RBAC migrations. 
- `commandCreatePermissions` - your own console command for Yii2 to create all known permissions.
- `commandCreateAdmin` - your own console command for Yii2 to create default user.
