package me.natejones.site

import org.gradle.api.*
import org.gradle.api.file.*
import org.gradle.api.provider.*

class StaticSiteExtension {
	final Property<String> baseUrl
	final Property<String> title
	final DirectoryProperty outputDir

	StaticSiteExtension(Project project) {
		baseUrl = project.objects.property(String)
		title = project.objects.property(String)
		outputDir = project.objects.directoryProperty()
	}
}
