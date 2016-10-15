package bio4j.data.titan.test

import com.bio4j.angulillos.UntypedGraph
import com.bio4j.angulillos.titan._
import com.thinkaurelius.titan.core._
import com.bio4j.model._
import com.bio4j.data._
import scala.compat.java8.OptionConverters._
import scala.collection.JavaConverters._

import org.scalatest.FunSuite

class SwissProtTests extends FunSuite {

  type G = UniProtGraph[TitanVertex,TitanEdge]
  type G0 = com.bio4j.angulillos.TypedGraph[G,TitanVertex,TitanEdge]

  lazy val conf =
    ReadOnlyTitan(new java.io.File("db"))

  lazy val uniProtGraph =
    new UniProtGraph(conf.untypedGraph)

  test("find protein by gene name") {

    val z: Option[String] =
      uniProtGraph.geneName.name.index.find("APP").asScala map { _.get[String, G#GeneNameType#Name](uniProtGraph.geneName.name) }

    assert { z == Some("APP") }

    // val allNames = uniProtGraph.geneName.vertices().iterator().asScala.zipWithIndex foreach {
    //   case (gn,index) =>
    //     if(index % 1000 == 0) {
    //       println {
    //         gn.get[String, UniProtGraph[TitanVertex,TitanEdge]#GeneNameType#Name](uniProtGraph.geneName.name)
    //       }
    //     }
    // }
  }

  test("acute phase proteins") {

    val proteinAccessions = uniProtGraph.keyword.id.index.find("KW-0024").asScala.map { kw =>
      kw.inV[G#Protein, G0#VertexType[G#Protein], G#Keywords, G0#EdgeType[G#Protein,G#Keywords,G#Keyword]](uniProtGraph.keywords).iterator().asScala.toList.map { protein =>

        protein.getOpt[String](uniProtGraph.protein.accession).asScala
      }
      .flatten
    }

    proteinAccessions.foreach { l => println(l.size) }

    println{ proteinAccessions }
  }
}
