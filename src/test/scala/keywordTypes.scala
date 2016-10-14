package bio4j.data.titan.test

import com.bio4j.data.uniprot.KeywordRow

case object KeywordTypes {

  def fromFile: Iterator[KeywordRow] =
    io.Source.fromFile("keywords-all.tsv").getLines.map(
      line => {
        val columns = line.split('\t')

        KeywordRow(
          id          = columns(0),
          description = columns(1),
          category    = columns(2)
        )
      }
    )
}
