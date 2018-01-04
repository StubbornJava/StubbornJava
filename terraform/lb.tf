data "aws_acm_certificate" "stubbornjava" {
  domain   = "stubbornjava.com"
  statuses = ["ISSUED"]
}

data "aws_acm_certificate" "wildcard_stubbornjava" {
  domain   = "*.stubbornjava.com"
  statuses = ["ISSUED"]
}

resource "aws_alb" "StubbornJavaLB" {
  name               = "StubbornJavaLB"
  internal           = false
  load_balancer_type = "application"
  security_groups    = ["sg-d10c37ac"]
  subnets            = ["${data.aws_subnet_ids.public.ids}"]
  ip_address_type    = "ipv4"

  enable_deletion_protection = true
}

resource "aws_lb_target_group" "StubbornJavaWeb" {
  name     = "StubbornJavaWeb"
  port     = 8080
  protocol = "HTTP"
  vpc_id   = "${data.aws_vpc.selected.id}"

  health_check {
    interval            = 30
    path                = "/"
    port                = "traffic-port"
    protocol            = "HTTP"
    timeout             = 5
    healthy_threshold   = 5
    unhealthy_threshold = 2
    matcher             = 200
  }
}

resource "aws_lb_listener" "sj_https" {
  load_balancer_arn = "${aws_alb.StubbornJavaLB.arn}"
  port              = "443"
  protocol          = "HTTPS"
  ssl_policy        = "ELBSecurityPolicy-2015-05"
  certificate_arn   = "${data.aws_acm_certificate.stubbornjava.arn}"

  default_action {
    target_group_arn = "${aws_lb_target_group.StubbornJavaWeb.arn}"
    type             = "forward"
  }
}

resource "aws_lb_listener" "sj_http" {
  load_balancer_arn = "${aws_alb.StubbornJavaLB.arn}"
  port              = "80"
  protocol          = "HTTP"

  default_action {
    target_group_arn = "${aws_lb_target_group.StubbornJavaWeb.arn}"
    type             = "forward"
  }
}

resource "aws_lb_target_group_attachment" "StubbornJavaWeb" {
  target_group_arn = "${aws_lb_target_group.StubbornJavaWeb.arn}"
  target_id        = "i-0839a0bbe4cd3cf40"
  port             = 8080
}

resource "aws_alb" "InternalAppsLB" {
  name               = "InternalAppsLB"
  internal           = false
  load_balancer_type = "application"
  security_groups    = ["sg-3d320448"]
  subnets            = ["${data.aws_subnet_ids.public.ids}"]
  ip_address_type    = "ipv4"

  enable_deletion_protection = true
}

resource "aws_lb_target_group" "InternalApps80" {
  name     = "InternalApps80"
  port     = 80
  protocol = "HTTP"
  vpc_id   = "vpc-e130ee84"

  health_check {
    interval            = 30
    path                = "/"
    port                = "traffic-port"
    protocol            = "HTTP"
    timeout             = 5
    healthy_threshold   = 5
    unhealthy_threshold = 2
    matcher             = 301
  }
}

resource "aws_lb_target_group" "Jenkins8080" {
  name     = "Jenkins8080"
  port     = 8080
  protocol = "HTTP"
  vpc_id   = "vpc-e130ee84"

  health_check {
    interval            = 30
    path                = "/"
    port                = "traffic-port"
    protocol            = "HTTP"
    timeout             = 5
    healthy_threshold   = 5
    unhealthy_threshold = 2
    matcher             = 403
  }
}

resource "aws_lb_listener" "internal_https" {
  load_balancer_arn = "${aws_alb.InternalAppsLB.arn}"
  port              = "443"
  protocol          = "HTTPS"
  ssl_policy        = "ELBSecurityPolicy-2015-05"
  certificate_arn   = "${data.aws_acm_certificate.wildcard_stubbornjava.arn}"

  default_action {
    target_group_arn = "${aws_lb_target_group.Jenkins8080.arn}"
    type             = "forward"
  }
}

resource "aws_lb_listener" "internal_http" {
  load_balancer_arn = "${aws_alb.InternalAppsLB.arn}"
  port              = "80"
  protocol          = "HTTP"

  default_action {
    target_group_arn = "${aws_lb_target_group.InternalApps80.arn}"
    type             = "forward"
  }
}

resource "aws_lb_target_group_attachment" "InternalApps80" {
  target_group_arn = "${aws_lb_target_group.InternalApps80.arn}"
  target_id        = "${aws_instance.ci.id}"
  port             = 80
}

resource "aws_lb_target_group_attachment" "Jenkins8080" {
  target_group_arn = "${aws_lb_target_group.Jenkins8080.arn}"
  target_id        = "${aws_instance.ci.id}"
  port             = 8080
}

resource "aws_lb_listener_rule" "jenkins_http" {
 listener_arn = "${aws_lb_listener.internal_http.arn}"
 priority     = 99

 action {
   type             = "forward"
   target_group_arn = "${aws_lb_target_group.InternalApps80.arn}"
 }

 condition {
   field  = "host-header"
   values = ["jenkins.stubbornjava.com"]
 }
}

resource "aws_lb_listener_rule" "jenkins_https" {
 listener_arn = "${aws_lb_listener.internal_https.arn}"
 priority     = 99

 action {
   type             = "forward"
   target_group_arn = "${aws_lb_target_group.Jenkins8080.arn}"
 }

 condition {
   field  = "host-header"
   values = ["jenkins.stubbornjava.com"]
 }
}
