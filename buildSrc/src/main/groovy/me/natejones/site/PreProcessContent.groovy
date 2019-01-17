package me.natejones.site

import java.nio.file.*

import org.gradle.api.*
import org.gradle.api.tasks.*
import org.gradle.api.tasks.incremental.*

@groovy.transform.CompileStatic
class PreProcessContent extends DefaultTask {
	@InputDirectory
	File inputDir = project.file('src/content')

	@OutputDirectory
	File outputDir = project.file("$project.buildDir/content")

	private transient Path inputPath
	private transient Path outputPath
	private transient Path yamlPath
	private transient Path contentPath

	@TaskAction
	void execute(IncrementalTaskInputs inputs) {
		if (!inputs.incremental)
      project.delete(outputDir.listFiles())

		inputPath = inputDir.toPath()
		outputPath = outputDir.toPath()
		yamlPath = outputPath.resolve('front-matter')
		contentPath = outputPath.resolve('raw')
		
		inputs.outOfDate { change ->
      if (change.file.directory) return
			def p = getPaths(change.file)
			logger.info "Out of Date: $p.relPath"
			ensurePath(p.yaml)
			ensurePath(p.content)
			p.path.withReader{i -> p.yaml.withWriter { y -> p.content.withWriter { c -> separateFrontMatter(i, y, c) } } }
		}

		inputs.removed { change ->
      if (change.file.directory) return
			def p = getPaths(change.file)
      logger.info "Removed: $p.relPath"
      Files.delete(p.yaml)
			Files.delete(p.content)
		}
	}

	private Map<String, Path> getPaths(File f) {
		Map<String, Path> r = new HashMap<>()
		r.path = f.toPath()
		r.relPath = inputPath.relativize(r.path)
		r.yaml = yamlPath.resolve("${r.relPath}.yaml")
		r.content = contentPath.resolve(r.relPath)
		return r
	}

	private static void ensurePath(Path f){
		if(!Files.exists(f.parent)){
			Files.createDirectories(f.parent)
		}
	}
	
	static void separateFrontMatter(Reader input, Writer frontMatter, Writer rest) {
		def line = input.readLine()
		if(line == null)
			return
		if(line == '---'){
			while(true){
				line = input.readLine()
				if(line == null || line == '---' || line == '...')
					break
				frontMatter << line << System.lineSeparator()
			}
		}
		else {
			rest << line << System.lineSeparator()
		}
		def c = input.read()
		while(c > -1){
			rest << (char)c
			c = input.read()
		}
	}
}
