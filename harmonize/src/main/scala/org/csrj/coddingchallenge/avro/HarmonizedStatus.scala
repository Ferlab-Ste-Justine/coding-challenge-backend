package org.csrj.coddingchallenge.avro

import org.apache.avro.Schema
import org.apache.avro.generic.{GenericData, GenericRecord}
import org.apache.avro.util.Utf8
import org.csrj.coddingchallenge.StringUtil
import org.csrj.coddingchallenge.StringUtil.reverseContent

object HarmonizedStatus {

  val schemaValue: Schema = new Schema.Parser().parse(
    """
      |{"namespace": "org.crsj.coddingChallenge",
      | "type": "record",
      | "name": "HarmonizedStatus",
      | "fields": [
      |     {"name": "screenName", "type": "string"},
      |     {"name": "language", "type":  ["string", "null"]},
      |     {"name": "content", "type": "string"}
      | ]
      |}
    """.stripMargin)

  val schemaKey: Schema = new Schema.Parser().parse(
    """
      |{"namespace": "org.crsj.coddingChallenge",
      | "type": "record",
      | "name": "HarmonizedStatusKey",
      | "fields": [
      |     {"name": "id", "type": "long"}
      | ]
      |}
    """.stripMargin)

  def harmonizedStatus(status: GenericRecord): GenericRecord = {
    val user = status.get("User").asInstanceOf[GenericRecord]
    val r = new GenericData.Record(schemaValue)
    r.put("screenName", user.get("ScreenName"))
    r.put("language", user.get("Lang"))
    r.put("content", reverseContent(status.get("Text").asInstanceOf[Utf8].toString))
    r
  }

  def harmonizedKey(status: GenericRecord): GenericRecord = {
    val r = new GenericData.Record(schemaKey)
    r.put("id", status.get("Id"))
    r

  }

}
