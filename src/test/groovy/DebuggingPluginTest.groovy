import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class DebuggingPluginTest extends Specification {

    @Rule
    TemporaryFolder projectDir = new TemporaryFolder()

    File buildFile

    def setup() {
        buildFile = projectDir.newFile("build.gradle")
        buildFile << """
            plugins {
                id 'de.lukaskoerfer.gradle.debugging'
            }
        """
    }

    def "can manually create Debug task"() {
        buildFile << """
            task myDebugTask(type: de.lukaskoerfer.gradle.debugging.Debug) {
                port = 8000
            }
        """

        when:
        def result = GradleRunner.create()
            .withProjectDir(projectDir.root)
            .withPluginClasspath()
            .withArguments('tasks', '--all')
            .build()

        then:
        result.output.contains('myDebugTask')
    }

    def ""() {
        buildFile << """
            task test(type: Test)
        """


    }

}
