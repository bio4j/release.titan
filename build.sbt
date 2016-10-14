Nice.scalaProject

name          := "data"
organization  := "bio4j"
description   := "data project"

bucketSuffix  := "era7.com"

libraryDependencies ++= Seq(
  "bio4j"                   %%  "import"            % "0.0.0-35-g775fdee",
  "bio4j"                   %   "angulillos-titan"  % "0.4.8",
  "com.thinkaurelius.titan" %   "titan-berkeleyje"  % "1.0.0"
) ++ testDependencies


val testDependencies = Seq("org.scalatest" %% "scalatest" % "3.0.0" % Test)
