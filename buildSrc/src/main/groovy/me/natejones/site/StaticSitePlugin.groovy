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
		def pp = project.tasks.create('preProcessContent', PreProcessContent)
		project.tasks.create('processContent', ProcessContent).dependsOn pp
  }
}
