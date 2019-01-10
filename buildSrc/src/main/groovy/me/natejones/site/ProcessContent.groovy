package me.natejones.site

import org.gradle.api.*
import org.gradle.api.tasks.*
import org.gradle.api.tasks.incremental.*

import com.vladsch.flexmark.Extension
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.ext.yaml.front.matter.*

class ProcessContent extends DefaultTask {
	static final List<Extension> EXTS = [ YamlFrontMatterExtension.create() ]
  static final Parser PARSER = Parser.builder().extensions(EXTS).build();
  static final HtmlRenderer RENDERER = HtmlRenderer.builder().extensions(EXTS).build();
	
	@InputDirectory
	File inputDir = project.file('src/content')

	@OutputDirectory
	File outputDir = project.file("$project.buildDir/site")

	@TaskAction
  void execute(IncrementalTaskInputs inputs) {
		println "Processing content in $inputDir"
		if (!inputs.incremental)
      project.delete(outputDir.listFiles())

		inputs.outOfDate { change ->
      if (change.file.directory) return
			
      println "out of date: ${change.file.name}"
      def targetFile = new File(outputDir, change.file.name)
      def doc = change.file.withReader { PARSER.parseReader(it) }
			def visitor = new AbstractYamlFrontMatterVisitor()
			visitor.visit(doc)
			println visitor.data
			targetFile.text = RENDERER.render(doc)
		}

		inputs.removed { change ->
      if (change.file.directory) return
      println "removed: ${change.file.name}"
      def targetFile = new File(outputDir, change.file.name)
      targetFile.delete()
		}
	}
}
