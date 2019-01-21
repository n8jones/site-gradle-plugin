package me.natejones.site

import static groovy.io.FileType.*

import com.vladsch.flexmark.Extension
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser

import org.gradle.api.*
import org.gradle.api.tasks.*
import org.gradle.api.tasks.incremental.*

//import org.yaml.snakeyaml.Yaml
import org.snakeyaml.engine.v1.api.*

class ProcessContent extends DefaultTask {
	static final List<Extension> EXTS = [ ]
  static final Parser PARSER = Parser.builder().extensions(EXTS).build()
  static final HtmlRenderer RENDERER = HtmlRenderer.builder().extensions(EXTS).build()
	//static final Yaml YAML = new Yaml()

	@InputDirectory
	File yamlDir = project.file("$project.buildDir/content/front-matter")
	
	@InputDirectory
	File rawDir = project.file("$project.buildDir/content/raw")

	@OutputDirectory
	File outputDir = project.file("$project.buildDir/site")

	@TaskAction
  void execute(IncrementalTaskInputs inputs) {
		if (!inputs.incremental)
      project.delete(outputDir.listFiles())

		def yamlPath = yamlDir.toPath()
		def ctx = [pages: [:]]

		logger.info 'Loading front matter'
		def settings = new LoadSettingsBuilder().build()
		def load = new Load(settings)
		yamlPath.eachFileRecurse FILES, {f ->
			if(!f.fileName.toString().endsWith('.yaml')) return
			
			def basename = yamlPath.relativize(f).toString()[0..-6]
			logger.debug "Parsing front matter for $basename"
			ctx.pages[basename] = f.withReader { load.loadFromReader(it) }
		}

		println ctx

		logger.info 'Rendering content'

		def rawPath = rawDir.toPath()
		def getBasename = { it.startsWith(yamlPath) ? yamlPath.relativize(it).toString()[0..-6] : rawPath.relativize(it).toString() }
		
		inputs.outOfDate { change ->
      if (change.file.directory) return
			logger.debug("out of date: ${change.file}")
			def f = change.file.toPath()
			def basename = getBasename(f)
      def page = ctx.pages[basename]
			if(page.isRendered){
				logger.debug "Already rendered: $f"
				return
			}
			page.isRendered = true
			page.basename = basename
			page.text = rawPath.resolve(f).text
			try{
				if(basename.endsWith('.md'))
					page.text = RENDERER.render(PARSER.parse(page.text))
				//TODO Call pebble template here
			}
			finally {
				page.remove('text')
			}
		}

		inputs.removed { change ->
      if (change.file.directory) return
      logger.info("removed: ${change.file}")
			// TODO Delete removed files
		}
	}
}
