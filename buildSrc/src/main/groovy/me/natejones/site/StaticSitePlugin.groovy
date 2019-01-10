package me.natejones.site

import org.gradle.api.*

class StaticSitePlugin implements Plugin<Project> {
  void apply(Project project) {
		project.tasks.create('processContent', ProcessContent)
  }
}
