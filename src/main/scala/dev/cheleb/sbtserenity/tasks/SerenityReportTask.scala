package dev.cheleb.sbtserenity.tasks

import sbt._
import sbt.Keys._
import sbt.Def.Initialize
import net.serenitybdd.plugins.sbt.SerenityCapabilities

class SerenityTasks(projectKey: String, log: Logger, baseDirectory: File)
    extends SerenityCapabilities(projectKey) {

  def serenityReport = {

    System.setProperty(
      "project.build.directory",
      baseDirectory.getAbsolutePath
    )
    log.info("generating Serenity report.")
    execute()

  }

  def historyReports {
    System.setProperty(
      "project.build.directory",
      baseDirectory.getAbsolutePath
    )
    log.info("generating Serenity report history.")
    generateHistory()
  }

  def clearHistory {
    System.setProperty(
      "project.build.directory",
      baseDirectory.getAbsolutePath
    )
    log.info("cleaning serenity report history.")
    clearHistoryFiles()
  }

  def clearReports {
    System.setProperty(
      "project.build.directory",
      baseDirectory.getAbsolutePath
    )
    log.info("cleaning serenity report directory.")
    clearReportFiles()
  }

}
