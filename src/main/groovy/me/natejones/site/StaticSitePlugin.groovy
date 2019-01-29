package me.natejones.site

import org.gradle.api.*
import org.gradle.api.file.*
import org.gradle.api.provider.*

/**
 * - Pre-Process Content
 *   - Extract Yaml Front Matter
 *   - Write Content Body
 * - Process Content
 *   - Read Site Config
 *   - Read Front Matters
 *   - Render Markdown
 *   - Apply template
 */
class StaticSitePlugin implements Plugin<Project> {
  void apply(Project project) {
		def extension = project.extensions.create('site', StaticSiteExtension, project)
		def processAssets = project.tasks.create('processAssets', org.gradle.api.tasks.Copy) {
			from project.file('src/assets')
			into "$project.buildDir/site/assets"
		}
		def preProcessContent = project.tasks.create('preProcessContent', PreProcessContent)
		def processContent = project.tasks.create('processContent', ProcessContent){
			yamlDir.set(extension.yamlDir)
			rawDir.set(extension.rawDir)
			outputDir.set(extension.outputDir)
			templateDir.set(extension.templateDir)
			title.set(extension.title)
			siteSettings.set(extension.settings)
		}
		processContent.dependsOn preProcessContent, processAssets
  }
}


