Nice.scalaProject

name          := "data"
organization  := "bio4j"
description   := "data project"

bucketSuffix  := "era7.com"

libraryDependencies ++= Seq(
  "bio4j"                   %%  "import"            % "0.0.0-84-g43be2e6",
  "bio4j"                   %   "angulillos-titan"  % "0.4.8",
  "com.thinkaurelius.titan" %   "titan-berkeleyje"  % "1.0.0",
  "ohnosequences"           %%  "fastarious"        % "0.6.0"
) ++ testDependencies


val testDependencies = Seq("org.scalatest" %% "scalatest" % "3.0.0" % Test)

dependencyOverrides ++= Set(
  "bio4j" % "angulillos" % "0.9.0-20-g2b2e685"
)
