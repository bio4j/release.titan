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

  test("find protein by gene name") {

    // val z: Option[String] =
    //   uniProtGraph.geneName.name.index.find("APP").asScala map { _.get[String, UniProtGraph[TitanVertex,TitanEdge]#GeneNameType#Name](uniProtGraph.geneName.name) }
    //
    // println { z }
    //
    // val allNames = uniProtGraph.geneName.vertices().iterator().asScala.zipWithIndex foreach {
    //   case (gn,index) =>
    //     if(index % 1000 == 0) {
    //       println {
    //         gn.get[String, UniProtGraph[TitanVertex,TitanEdge]#GeneNameType#Name](uniProtGraph.geneName.name)
    //       }
    //     }
    // }
  }

  ignore("Import annotations") {}
}
