resource "aws_route53_zone" "stubbornjava" {
  name = "stubbornjava.com."
  comment = "HostedZone created by Route53 Registrar"
  force_destroy = false
}

resource "aws_route53_record" "acm_wildcard_validation" {
  zone_id = "${aws_route53_zone.stubbornjava.zone_id}"
  name    = "_0aae0c14fdb61a1eace4820316e1b289.${aws_route53_zone.stubbornjava.name}"
  type    = "CNAME"
  ttl     = "300"
  records = ["_0285fe9cd2fa8d2e5b3307a3d627e407.acm-validations.aws"]
}

resource "aws_route53_record" "elb" {
  zone_id = "${aws_route53_zone.stubbornjava.zone_id}"
  name    = "${aws_route53_zone.stubbornjava.name}"
  type    = "A"

  alias {
    name                   = "${lower(aws_alb.StubbornJavaLB.dns_name)}"
    zone_id                = "${aws_alb.StubbornJavaLB.zone_id}"
    evaluate_target_health = false
  }
}

resource "aws_route53_record" "www" {
  zone_id = "${aws_route53_zone.stubbornjava.zone_id}"
  name    = "www.${aws_route53_zone.stubbornjava.name}"
  type    = "A"

  alias {
    name                   = "${aws_route53_zone.stubbornjava.name}"
    zone_id                = "${aws_route53_zone.stubbornjava.zone_id}"
    evaluate_target_health = false
  }
}

resource "aws_route53_record" "www_local" {
  zone_id = "${aws_route53_zone.stubbornjava.zone_id}"
  name    = "www.local.${aws_route53_zone.stubbornjava.name}"
  type    = "A"
  ttl     = 300
  records = ["127.0.0.1"]
}

resource "aws_route53_record" "local" {
  zone_id = "${aws_route53_zone.stubbornjava.zone_id}"
  name    = "local.${aws_route53_zone.stubbornjava.name}"
  type    = "A"
  ttl     = 300
  records = ["127.0.0.1"]
}

resource "aws_route53_record" "jenkins" {
  zone_id = "${aws_route53_zone.stubbornjava.zone_id}"
  name    = "jenkins.${aws_route53_zone.stubbornjava.name}"
  type    = "A"

  alias {
    name                   = "${lower(aws_alb.InternalAppsLB.dns_name)}"
    zone_id                = "${aws_alb.InternalAppsLB.zone_id}"
    evaluate_target_health = false
  }
}

resource "aws_route53_record" "git" {
  zone_id = "${aws_route53_zone.stubbornjava.zone_id}"
  name    = "git.${aws_route53_zone.stubbornjava.name}"
  type    = "TXT"
  ttl     = 300
  records = ["https://github.com/StubbornJava"]
}
