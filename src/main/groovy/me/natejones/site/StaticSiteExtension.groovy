package me.natejones.site

import org.gradle.api.*
import org.gradle.api.file.*
import org.gradle.api.provider.*

@groovy.transform.CompileStatic
class StaticSiteExtension {
	final Property<String> baseUrl
	final Property<String> title
	final DirectoryProperty outputDir
	final DirectoryProperty yamlDir
	final DirectoryProperty rawDir
	final DirectoryProperty templateDir
	final MapProperty<String, Object> settings

	StaticSiteExtension(Project project) {
		baseUrl = project.objects.property(String)
		baseUrl.set('http://localhost')
		title = project.objects.property(String)
		title.set('')
		outputDir = project.objects.directoryProperty()
		outputDir.set(project.layout.buildDirectory.dir('site'))
		yamlDir = project.objects.directoryProperty()
		yamlDir.set(project.layout.buildDirectory.dir('content/front-matter'))
		rawDir = project.objects.directoryProperty()
		rawDir.set(project.layout.buildDirectory.dir('content/raw'))
		templateDir = project.objects.directoryProperty()
		templateDir.set(project.layout.projectDirectory.dir('src/layouts'))
		settings = project.objects.mapProperty(String, Object)
		settings.set([:])
	}
}
