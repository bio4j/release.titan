package bio4j.data.test

import com.bio4j.angulillos.UntypedGraph
import com.bio4j.angulillos.titan._
import com.thinkaurelius.titan.core._
import com.bio4j.model._
import com.bio4j.data.uniprot._
import scala.compat.java8.OptionConverters._
import scala.collection.JavaConverters._

import org.scalatest.FunSuite

class DataTest extends FunSuite {

  lazy val conf = TitanConf(new java.io.File("db"))

  def entries =
    Entry.fromUniProtLines( io.Source.fromFile("../uniprot_sprot.xml").getLines )

  lazy val uniProtGraph =
    new UniProtGraph(conf.untypedGraph)

  lazy val titan =
    uniProtGraph.raw().asInstanceOf[TitanUntypedGraph]

  lazy val importProteins =
    ImportEntryProteins(uniProtGraph)

  lazy val importAnnotations =
    ImportAnnotations(uniProtGraph)

  lazy val importGeneNames =
    ImportEntryGeneNames(uniProtGraph)

  ignore("initialize types") {

    val mgmt = titan.titanGraph().openManagement()
    val schema = new TitanUntypedGraphSchema()

    // init types
    schema.createSchema(mgmt,uniProtGraph)

    mgmt.commit()
  }

  ignore("Import SwissProt proteins") {

    entries.zipWithIndex foreach {

      case (entry, index) =>
        importProteins.fromEntry.addVertex(entry, uniProtGraph)
        if(index % 1000 == 0) {
          println { s"Committing after ${index} proteins" }
          titan.commit()
        }
    }
  }

  ignore("Import gene names") {

    entries.zipWithIndex foreach {

      case (entry, index) =>
        importGeneNames.fromEntry.addVertex(entry, uniProtGraph)
        if(index % 5000 == 0) {
          println { s"Committing after ${index} gene names" }
          titan.commit()
        }
    }
  }

  test("find protein by gene name") {

    // val z: Option[String] =
    //   uniProtGraph.geneName.name.index.find("APP").asScala map { _.get[String, UniProtGraph[TitanVertex,TitanEdge]#GeneNameType#Name](uniProtGraph.geneName.name) }
    //
    // println { z }

    val allNames = uniProtGraph.geneName.vertices().iterator().asScala.zipWithIndex foreach {
      case (gn,index) =>
        if(index % 1000 == 0) {
          println {
            gn.get[String, UniProtGraph[TitanVertex,TitanEdge]#GeneNameType#Name](uniProtGraph.geneName.name)
          }
        }
    }
  }

  ignore("Import annotations") {

    entries.zipWithIndex foreach {

      case (entry, index) =>
        importAnnotations.fromEntry.addVertex(entry, uniProtGraph)
        if(index % 1000 == 0) {
          println { s"Committing after ${index} entries" }
          titan.commit()
        }
    }
  }

  test("close db") { titan.shutdown() }
}

import UniProtGraph._
import com.thinkaurelius.titan.graphdb.database.serialize.attribute.EnumSerializer

class ExistenceEvidenceSerializer extends EnumSerializer[ExistenceEvidence](classOf[ExistenceEvidence])
class DatasetsSerializer          extends EnumSerializer[Datasets](classOf[Datasets])
class CommentTopicsSerializer     extends EnumSerializer[CommentTopics](classOf[CommentTopics])
class FeatureTypesSerializer      extends EnumSerializer[FeatureTypes](classOf[FeatureTypes])
class KeywordCategoriesSerializer extends EnumSerializer[KeywordCategories](classOf[KeywordCategories])

case class TitanConf(val file: java.io.File) {

  lazy val bio4jPkg =
    "com.bio4j.model"
  lazy val titanSerializers =
    "com.thinkaurelius.titan.graphdb.database.serialize.attribute"


  lazy val factory =
    TitanFactory.build()
      .set("schema.default", "none")
      // .set("storage.batch-loading", "true")
      .set( "storage.backend",                      "berkeleyje"    )
      .set( "storage.directory",                    file.getPath()  )
      // .set( "storage.transactions",                 true            )
      .set( "storage.transactions",                 false           )
      .set( "storage.berkeleyje.cache-percentage",  1              )
      .set( "cache.db-cache",                       true          )

      // .set( "storage.berkeleyje.isolation-level",   "SERIALIZABLE"  )
      // custom serializers
      .set( "attributes.custom.attribute1.attribute-class",   s"${bio4jPkg}.UniProtGraph$$ExistenceEvidence"  )
      .set( "attributes.custom.attribute1.serializer-class",  "bio4j.data.test.ExistenceEvidenceSerializer"   )
      .set( "attributes.custom.attribute2.attribute-class",   s"${bio4jPkg}.UniProtGraph$$Datasets"  )
      .set( "attributes.custom.attribute2.serializer-class",  "bio4j.data.test.DatasetsSerializer"   )
      .set( "attributes.custom.attribute3.attribute-class",   s"${bio4jPkg}.UniProtGraph$$CommentTopics"  )
      .set( "attributes.custom.attribute3.serializer-class",  "bio4j.data.test.CommentTopicsSerializer"   )
      .set( "attributes.custom.attribute4.attribute-class",   s"${bio4jPkg}.UniProtGraph$$FeatureTypes"  )
      .set( "attributes.custom.attribute4.serializer-class",  "bio4j.data.test.FeatureTypesSerializer"   )
      .set( "attributes.custom.attribute5.attribute-class",   s"${bio4jPkg}.UniProtGraph$$KeywordCategories"  )
      .set( "attributes.custom.attribute5.serializer-class",  "bio4j.data.test.KeywordCategoriesSerializer"   )

  lazy val graph =
    factory.open()

  lazy val untypedGraph =
    new TitanUntypedGraph(graph)
}
