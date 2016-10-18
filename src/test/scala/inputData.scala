/*
  # Input data

  Here we find methods for downloading input data from their original locations.
*/
package bio4j.data.titan.test

import java.io.File
import sys.process._

/*
  These classes could be part of bio4j/bio4j-data-import
*/
case class UniProtSwissProtData(
  swissProtXML      : File,
  keywordsTSV       : File,
  isoformSequences  : File
)

case class UniProtTrEMLData(
  trEMBLXML: File
)

case class UniRefData(
  uniref100ClustersXML  : File,
  uniref90ClustersXML   : File,
  uniref50ClustersXML   : File
)

case class NCBITaxonomyData(
  nodes: File,
  names: File
)

case class GeneOntologyData(
  oboXMLFilteredOntology: File
)

case class EnzymeData(
  entries: File,
  classes: File
)

/*
  ### Download data

  URLs, scripts for extracting files etc.
*/
// name ="2016_08", for examle.
case class UniProtRelease(val name: String) {

  lazy val baseFolder: String =
    s"ftp://ftp.uniprot.org/pub/databases/uniprot/previous_releases/release-${name}"

  lazy val swissProtWholeThingGzipped: String =
    s"${baseFolder}/knowledgebase/uniprot_sprot-only${name}.tar.gz"

  // TODO this requires a manual download, and putting it somewhere in S3
  lazy val keywords: String =
    ???

  lazy val isoformsSequences: String =
    "uniprot_sprot_varsplic.fasta.gz"

  lazy val isoformsSequencesGzipped: String =
    s"${isoformsSequences}.gz"

  // this is more a reminder than a working implementation
  def extractUnder(folder: File): UniProtSwissProtData = {

    // TODO proper file management
    val swissProtFileTGz      = s"${folder}/uniprot_sprot-only${name}.tar.gz"

    val swissProtXMLFile      = new File(s"${folder}/uniprot_sprot.xml")
    val keywordsTSVFile       = new File(s"${folder}/keywords-all.tsv")
    val isoformsSequencesFile = new File(s"${folder}/uniprot_sprot_varsplic.fasta")

    // download keywords
    Seq("wget", keywords, "-O", s"${keywordsTSVFile}").!
    // download SwissProt from FTP
    Seq("wget", swissProtWholeThingGzipped, "-O", swissProtFileTGz).!
    // extract
    Seq("tar", "-xvzf", swissProtFileTGz).!
    // extract individual files
    // SwissProt xml
    Seq("gzip", "-d", s"${folder}/uniprot_sprot.xml.gz").!
    // isoform sequences
    Seq("gzip", "-d", s"${folder}/uniprot_sprot_varsplic.fasta").!

    UniProtSwissProtData(
      swissProtXML      = swissProtXMLFile,
      keywordsTSV       = keywordsTSVFile,
      isoformSequences  = isoformsSequencesFile
    )
  }
}

case class UniProtTrEMBLRelease(val name: String) {

  lazy val baseFolder: String =
    s"ftp://ftp.uniprot.org/pub/databases/uniprot/previous_releases/release-${name}"

  lazy val trEMBLGzipped: String =
    s"${baseFolder}/knowledgebase/knowledgebase${name}.tar.gz"

  def extractUnder(folder: File): UniProtTrEMLData = {

    val trEMBLGzippedFile = s"${folder}/knowledgebase${name}.tar.gz"

    // download TrEMBL from FTP
    Seq("wget", trEMBLGzipped, "-O", s"${trEMBLGzippedFile}").!
    // extract
    Seq("tar", "-xvzf", s"${trEMBLGzippedFile}").!

    // TODO file name
    UniProtTrEMLData(
      trEMBLXML = ???
    )
  }
}

case class UniRefRelease(val name: String) {

  lazy val baseFolder: String =
    s"ftp://ftp.uniprot.org/pub/databases/uniprot/previous_releases/release-${name}"

  lazy val uniRefGzipped: String =
    s"${baseFolder}/uniref/uniref${name}.tar.gz"

  def extractUnder(folder: File): UniRefData =
    ???
}
/*
  **NOTE** the NCBI taxonomy has no notion of versioning or whatever.
*/
case class NCBITaxonomyRelease(val name: String) {

  // see ftp://ftp.ncbi.nih.gov/pub/taxonomy/taxdump_readme.txt
  lazy val everythingGzipped: String =
    "ftp://ftp.ncbi.nih.gov/pub/taxonomy/taxdump.tar.gz"

  def extractUnder(folder: File): NCBITaxonomyData =
    ???
}

case class GeneOntologyRelease(val name: String) {

  // there are daily automated builds here: http://archive.geneontology.org/termdb/
  lazy val termdbGzipped: String =
    "http://archive.geneontology.org/latest-full/go_monthly-termdb.obo-xml.gz"
}

case class ENZYMERelease() {

  lazy val entries: String =
    "ftp://ftp.expasy.org/databases/enzyme/release/release_with_updates/release/enzyme.dat"

  lazy val classes: String =
    "ftp://ftp.expasy.org/databases/enzyme/release/release_with_updates/release/enzclass.txt"
}
