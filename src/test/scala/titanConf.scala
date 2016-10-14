package bio4j.data.titan.test

import com.bio4j.angulillos.UntypedGraph
import com.bio4j.angulillos.titan._
import com.thinkaurelius.titan.core._
import com.bio4j.model._
import com.bio4j.data._
import scala.compat.java8.OptionConverters._
import scala.collection.JavaConverters._
import UniProtGraph._
import com.thinkaurelius.titan.graphdb.database.serialize.attribute.EnumSerializer

class ExistenceEvidenceSerializer extends EnumSerializer[ExistenceEvidence](classOf[ExistenceEvidence])
class DatasetsSerializer          extends EnumSerializer[Datasets](classOf[Datasets])
class CommentTopicsSerializer     extends EnumSerializer[CommentTopics](classOf[CommentTopics])
class FeatureTypesSerializer      extends EnumSerializer[FeatureTypes](classOf[FeatureTypes])
class KeywordCategoriesSerializer extends EnumSerializer[KeywordCategories](classOf[KeywordCategories])
class GeneLocationsSerializer     extends EnumSerializer[GeneLocations](classOf[GeneLocations])

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
      .set( "storage.transactions",                 true            )
      // .set( "storage.transactions",                 false           )
      .set( "storage.berkeleyje.cache-percentage",  1              )
      .set( "cache.db-cache",                       true          )

      // .set( "storage.berkeleyje.isolation-level",   "SERIALIZABLE"  )
      // custom serializers
      .set( "attributes.custom.attribute1.attribute-class",   s"${bio4jPkg}.UniProtGraph$$ExistenceEvidence"  )
      .set( "attributes.custom.attribute1.serializer-class",  "bio4j.data.titan.test.ExistenceEvidenceSerializer"   )
      .set( "attributes.custom.attribute2.attribute-class",   s"${bio4jPkg}.UniProtGraph$$Datasets"  )
      .set( "attributes.custom.attribute2.serializer-class",  "bio4j.data.titan.test.DatasetsSerializer"   )
      .set( "attributes.custom.attribute3.attribute-class",   s"${bio4jPkg}.UniProtGraph$$CommentTopics"  )
      .set( "attributes.custom.attribute3.serializer-class",  "bio4j.data.titan.test.CommentTopicsSerializer"   )
      .set( "attributes.custom.attribute4.attribute-class",   s"${bio4jPkg}.UniProtGraph$$FeatureTypes"  )
      .set( "attributes.custom.attribute4.serializer-class",  "bio4j.data.titan.test.FeatureTypesSerializer"   )
      .set( "attributes.custom.attribute5.attribute-class",   s"${bio4jPkg}.UniProtGraph$$KeywordCategories"  )
      .set( "attributes.custom.attribute5.serializer-class",  "bio4j.data.titan.test.KeywordCategoriesSerializer"   )
      .set( "attributes.custom.attribute6.attribute-class",   s"${bio4jPkg}.UniProtGraph$$GeneLocations"  )
      .set( "attributes.custom.attribute6.serializer-class",  "bio4j.data.titan.test.GeneLocationsSerializer"   )

  lazy val graph =
    factory.open()

  lazy val untypedGraph =
    new TitanUntypedGraph(graph)
}
