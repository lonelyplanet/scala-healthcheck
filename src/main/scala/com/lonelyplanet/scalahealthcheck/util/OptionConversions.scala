package com.lonelyplanet.scalahealthcheck.util

import scala.collection.immutable.Seq

object OptionConversions {
  def stringToOption(str: String): Option[String] = if (trimmedStringIsEmpty(str)) None else Some(str)
  def seqToOption[T](list: Seq[T]): Option[Seq[T]] = if (list.isEmpty) None else Some(list)
  private def trimmedStringIsEmpty(str: String) = str.trim.isEmpty
}
