package com.lonelyplanet.scalahealthcheck

sealed abstract class DependencyStatus(val value: String)
case object DependencyStatusGreen extends DependencyStatus("green")
case object DependencyStatusRed extends DependencyStatus("red")
