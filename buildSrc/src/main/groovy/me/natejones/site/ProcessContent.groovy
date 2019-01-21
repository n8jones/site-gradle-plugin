package me.natejones.site

import static groovy.io.FileType.*

import java.nio.file.*

import com.mitchellbosecke.pebble.*
import com.mitchellbosecke.pebble.loader.*
import com.vladsch.flexmark.Extension
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser

import org.gradle.api.*
import org.gradle.api.tasks.*
import org.gradle.api.tasks.incremental.*

import org.snakeyaml.engine.v1.api.*

class ProcessContent extends DefaultTask {
	static final List<Extension> EXTS = [ ]
  static final Parser PARSER = Parser.builder().extensions(EXTS).build()
  static final HtmlRenderer RENDERER = HtmlRenderer.builder().extensions(EXTS).build()

	@InputDirectory
	File yamlDir = project.file("$project.buildDir/content/front-matter")
	
	@InputDirectory
	File rawDir = project.file("$project.buildDir/content/raw")

	File templateDir = project.file('src/layouts/')

	@OutputDirectory
	File outputDir = project.file("$project.buildDir/site")

	@TaskAction
  void execute(IncrementalTaskInputs inputs) {
		//TODO fix this so it doesn't blow away assets
		//if (!inputs.incremental)
    //  project.delete(outputDir.listFiles())

		def yamlPath = yamlDir.toPath()
		def ctx = [pages: [:]]

		logger.info 'Loading front matter'
		def settings = new LoadSettingsBuilder().build()
		def load = new Load(settings)
		yamlPath.eachFileRecurse FILES, {f ->
			if(!f.fileName.toString().endsWith('.yaml')) return
			def basename = yamlPath.relativize(f).toString()[0..-6]
			logger.debug "Parsing front matter for $basename"
			def page = f.withReader { load.loadFromReader(it) }
			page.basename = basename
			page.url = basename.endsWith('.md') ? "${basename[0..-4]}.html" : basename
			page.url = page.url.replaceAll('\\\\', '/')
			ctx.pages[basename] = page
		}

		ctx.baseUrl = outputDir.toURL()

		logger.debug "Context: $ctx"

		logger.info 'Rendering content'

		def rawPath = rawDir.toPath()
		def getBasename = { it.startsWith(yamlPath) ? yamlPath.relativize(it).toString()[0..-6] : rawPath.relativize(it).toString() }
		def outputPath = outputDir.toPath()

		def floader = new FileLoader()
		floader.prefix = templateDir.toString()
		floader.suffix = '.html'
		def pebble = new PebbleEngine.Builder().loader(floader).build()		
		
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
			page.text = rawPath.resolve(f).text
			try{
				Path output
				String url
				if(basename.endsWith('.md')) {
					page.text = RENDERER.render(PARSER.parse(page.text))
					url = "${basename[0..-4]}.html"
					output = outputPath.resolve(url)
				}
				else {
					output = outputPath.resolve(basename)
					url = basename
				}
				page.output = output
				logger.debug "Page: $page"
				if(page.layout) {
					ctx.page = page
					def template = pebble.getTemplate(page.layout)
					logger.info "Writing $basename"
					Files.createDirectories(output.parent)
					output.withWriter { template.evaluate(it, ctx) }
				}
				else {
					output.text = page.text
				}
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
