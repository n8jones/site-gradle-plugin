package me.natejones.site

import static org.junit.Assert.*

import org.junit.*

class PreProcessContentTest {
	@Test
	void test_separateFrontMatter() {
		def input = '''---
date: 2019-01-16
---
Hello World'''
		def yaml = new StringWriter()
		def content = new StringWriter()
		PreProcessContent.separateFrontMatter(new StringReader(input), yaml, content)
		assertEquals('yaml', 'date: 2019-01-16', yaml.toString().trim())
		assertEquals('content', 'Hello World', content.toString().trim())
	}
}
