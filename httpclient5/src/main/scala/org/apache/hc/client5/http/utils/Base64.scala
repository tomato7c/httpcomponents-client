package org.apache.hc.client5.http.utils

object Base64 {
  val EMPTY_BYTES = Array.empty[Byte]

  def decodeBase64(base64: Array[Byte]): Array[Byte] = {
    if (base64 == null) return null
    try {
      return java.util.Base64.getMimeDecoder.decode(base64)
    } catch {
      case _: IllegalArgumentException => EMPTY_BYTES
    }
  }

  def decodeBase64(base64: String): Array[Byte] = {
    if (base64 == null) return null

    try {
      return java.util.Base64.getMimeDecoder.decode(base64)
    } catch {
      case _: IllegalArgumentException => EMPTY_BYTES
    }
  }

  def decode(base64: String): Array[Byte] = {
    if (base64 == null) return null
    try {
      return java.util.Base64.getMimeDecoder.decode(base64)
    } catch {
      case _: IllegalArgumentException => EMPTY_BYTES
    }
  }

  def decode(base64: Array[Byte]): Array[Byte] = {
    if (base64 == null) return null

    try {
      return java.util.Base64.getMimeDecoder.decode(base64)
    } catch {
      case _: IllegalArgumentException => EMPTY_BYTES
    }
  }

  def encodeBase64(base64: Array[Byte]): Array[Byte] = encode(base64)

  def encodeBase64String(bytes: Array[Byte]): String = {
    bytes match {
      case null => null
      case _ => java.util.Base64.getEncoder.encodeToString(bytes)
    }
  }

  def encode(value: Array[Byte]): Array[Byte] = {
    value match {
      case null => null
      case _ => java.util.Base64.getEncoder.encode(value)
    }
  }
}