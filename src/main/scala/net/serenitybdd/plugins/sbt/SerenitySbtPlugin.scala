package com.tysafe.sbt.serenity

import sbt.Keys._
import sbt.Tests.Cleanup
import sbt._
import plugins._
import dev.cheleb.sbtserenity.tasks.SerenityTasks

object SerenitySbtPlugin extends AutoPlugin {

  def projectKey = Def.setting(name.value).toString

  object autoImport {
    val serenityTasks = taskKey[SerenityTasks]("Serenity sbt tasks")
    val serenityReportTask = taskKey[Unit]("Serenity sbt report task")
    val clearReports =
      taskKey[Unit]("Serenity sbt task to delete report directory")
    val historyReports = taskKey[Unit]("Serenity sbt task to create history")
    val clearHistory = taskKey[Unit]("Serenity sbt task to delete history")
  }

  import autoImport._

  override def trigger: PluginTrigger = allRequirements

  override def requires: AutoPlugin = JvmPlugin

  override lazy val projectSettings: Seq[Setting[_]] = Seq(
    serenityTasks := new SerenityTasks(
      projectKey,
      streams.value.log,
      baseDirectory.value
    ),
    test := {
      serenityReportTask.dependsOn((Test / test).result).value
    },
    testOnly := (Def.inputTaskDyn {
      import sbt.complete.Parsers.spaceDelimited
      val args = spaceDelimited("<args>").parsed
      Def.taskDyn {
        serenityReportTask.dependsOn(
          (Test / testOnly).toTask(" " + args.mkString(" ")).result
        )
      }
    }).evaluated,
    testQuick := (Def.inputTaskDyn {
      import sbt.complete.Parsers.spaceDelimited
      val args = spaceDelimited("<args>").parsed
      Def.taskDyn {
        serenityReportTask.dependsOn(
          (Test / testQuick).toTask(" " + args.mkString(" ")).result
        )
      }
    }).evaluated,
    clean := {
      clearReports.dependsOn((clean).result).value
    },
    clearReports := serenityTasks.value.clearReports,
    clearHistory := serenityTasks.value.clearHistory,
    historyReports := serenityTasks.value.historyReports,
    serenityReportTask := serenityTasks.value.serenityReport
  )

}
