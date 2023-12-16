package org.apache.hc.client5.testing.fluent

import org.apache.hc.client5.http.fluent.Request
import org.junit.jupiter.api.Test

class TestApi {

  val BASE_URL = "http://localhost:8080"

  @Test
  def testWhatIsYourName(): Unit = {
    val res = Request.get(BASE_URL + "/what_is_your_name").execute().returnContent().asString()
    println(res)
  }
}
