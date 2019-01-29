package controllers

import org.scalatest.matchers._
import org.scalatestplus.play._
import play.api.libs.json._
import play.api.libs.ws._
import play.api.test._

trait ControllerSpecHelpers extends OneServerPerTest with DefaultAwaitTimeout with FutureAwaits {
  this: PlaySpec =>

  class BeLikeMatchers[A](func: PartialFunction[A, Unit]) extends Matcher[A] {
    def apply(left: A) = {
      val defined = func isDefinedAt left
      val result = MatchResult(defined, s"No match for $left", s"Match found for $left")

      if(defined) {
        func(left)
      }

      result
    }
  }

  def belike[A](func: PartialFunction[A, Unit]): BeLikeMatchers[A] = {
    new BeLikeMatchers(func)
  }
}