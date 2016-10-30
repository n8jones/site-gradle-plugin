/*global module:false*/

module.exports = function(grunt) {
	grunt.initConfig({
		pkg: grunt.file.readJSON('package.json'),
		// Tasks

		jekyll: {
			dist: {
				config: '_config.yml'
			}
		},

		watch: {
			src: {
				files: 'src/**/*',
				tasks: 'jekyll',
			}
		},

		connect: {
			server: {
				options: {
					port: 4000,
					base: 'target/site',
				}
			}
		}

	});

	// Load the plugin that provides the "uglify" task.
	 
	grunt.loadNpmTasks('grunt-jekyll');
        grunt.loadNpmTasks('grunt-contrib-watch');
        grunt.loadNpmTasks('grunt-contrib-connect');
	 
	// Default task(s).
	 
	grunt.registerTask('default', ['jekyll', 'connect', 'watch']);
};
