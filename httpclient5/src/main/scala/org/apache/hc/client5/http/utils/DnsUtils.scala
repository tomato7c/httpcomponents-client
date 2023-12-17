package org.apache.hc.client5.http.utils

object DnsUtils {

  /**
   * 不明白 '\u212A' 为什么 tm 的会被 toUpper 处理
   */
  def normalize(s: String): String = Option(s) match {
    case None => null
    case Some(s) => s.map(it => if (it >= 'A' && it <= 'Z') it.toLower else it)
  }
}
