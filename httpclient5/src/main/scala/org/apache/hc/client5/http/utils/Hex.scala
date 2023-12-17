package org.apache.hc.client5.http.utils

object Hex {

  def encodeHexString(bytes: Array[Byte]): String = {
    import Extensions.{ByteExtensions, IntExtensions}
    val res = new StringBuilder()
    for (b <- bytes) {
      res += b.higher4Bit.toHex
      res += b.lower4Bit.toHex
    }
    res.toString()
  }
}


object Extensions {
  implicit class ByteExtensions(b: Byte) {
    def higher4Bit: Int = (b & 0xF0) >>> 4
    def lower4Bit: Int = b & 0x0F
  }

  private val hexMap = Array(
    '0', '1', '2', '3', '4', '5', '6', '7',
    '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')

  implicit class IntExtensions(i: Int) {
    def toHex: Char = hexMap(i)
  }
}