import java.io.File
import java.util.Properties
import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra

val Project.commitSha: String
  get() = exec("git rev-parse HEAD")
val Project.commitCount: Int
  get() = exec("git rev-list --count HEAD").toInt()

@Suppress("UnstableApiUsage")
fun Project.exec(command: String) =
  providers.exec { commandLine(command.split(" ")) }.standardOutput.asText.get().trim()

val Project.releaseKeyStore: File
  get() = File(extra["keyStore"] as String)
val Project.releaseKeyStorePassword: String
  get() = extra["keyStorePassword"] as String
val Project.releaseKeyAlias: String
  get() = extra["keyAlias"] as String
val Project.releaseKeyPassword: String
  get() = extra["keyPassword"] as String
val Project.hasReleaseKeyStore: Boolean
  get() {
    signingProperties(rootDir).forEach { key, value -> extra[key as String] = value }

    return extra.has("keyStore")
  }

private fun signingProperties(rootDir: File): Properties {
  val properties = Properties()
  val signingProperties = rootDir.resolve("signing.properties")
  if (signingProperties.isFile) {
    signingProperties.inputStream().use(properties::load)
  }

  return properties
}
