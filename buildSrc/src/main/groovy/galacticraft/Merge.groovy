package galacticraft

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.gradle.api.Project
import org.gradle.api.file.CopySpec
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.util.PatternFilterable

class Merge {
	static List<String> getGeneralPathsToExclude(Project project) {
		List<String> toExclude = new ArrayList<>()
		toExclude.add('mcmod.info')
		toExclude.add('META-INF/accesstransformer.cfg')
		return toExclude
	}

	static Closure createExcludeClosure(List<String> baseExcludeData, String... extraExclusions) {
		List<String> toExcludeFromAll = new ArrayList<>(baseExcludeData)
		for (String extraExclusion : extraExclusions) {
			toExcludeFromAll.add(extraExclusion)
		}
		return { CopySpec c ->
			c.exclude(toExcludeFromAll)
		}
	}
	
	static void merge(Project project, SourceSet... sourceSets) {
		//Generate folders, merge the access transformers and mods.toml files
		project.mkdir("$project.buildDir/generated/META-INF")
		(new File("$project.buildDir/generated/META-INF/accesstransformer.cfg")).text = mergeATs(sourceSets)
		(new File("$project.buildDir/generated/mcmod.info")).text = mergeMcModInfo(sourceSets)
	}
	
	static List<Closure> getGeneratedClosures(def versionProperties) {
		List<Closure> generated = new ArrayList<>()
		generated.add({ CopySpec c ->
			c.include('mcmod.info')
			c.expand(versionProperties)
		})
		generated.add({ CopySpec c ->
			c.include('META-INF/accesstransformer.cfg')
		})
		return generated
	}
	
	private static String mergeATs(SourceSet... sourceSets) {
		String text = ""
		for (SourceSet sourceSet : sourceSets) {
			sourceSet.resources.matching { PatternFilterable pf ->
				pf.include('META-INF/accesstransformer.cfg')
			}.each { file ->
				text = text.isEmpty() ? file.getText() : text + "\n" + file.getText()
			}
		}
		return text
	}

	private static String mergeMcModInfo(SourceSet... sourceSets) {
		String text = ""
		for (SourceSet sourceSet : sourceSets) {
			sourceSet.resources.matching { PatternFilterable pf ->
				pf.include('mcmod.info')
			}.each { file ->
				if (text.isEmpty()) {
					//Nothing added yet, take it all
					text = file.getText()
				} else {
					//Otherwise add all but the first four lines (which are duplicated between the files)
					String[] lines = file.getText().split("\n")
					for (int i = 4; i < lines.length; i++) {
						text = text + "\n" + lines[i]
					}
				}
			}
		}
		return text
	}
}
