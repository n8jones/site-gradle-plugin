package me.natejones.site

import org.gradle.api.*
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
		def pa = project.tasks.create('processAssets', org.gradle.api.tasks.Copy) {
			from project.file('src/assets')
			into "$project.buildDir/site/assets"
		}
		def pp = project.tasks.create('preProcessContent', PreProcessContent)
		project.tasks.create('processContent', ProcessContent).dependsOn pp, pa
  }
}


