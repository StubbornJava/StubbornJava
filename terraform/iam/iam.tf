variable "region" {}

provider "aws" {
    region = "${var.region}"
}

resource "aws_iam_policy" "jenkins-s3" {
    name        = "jenkins-s3"
    policy      = <<EOF
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "s3:PutObject"
            ],
            "Resource": [
                "arn:aws:s3:::stubbornjava-jenkins/*"
            ]
        }
    ]
}
EOF
}

resource "aws_iam_role" "jenkins" {
  name = "jenkins"

  assume_role_policy = <<EOF
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": "sts:AssumeRole",
      "Principal": {
        "Service": "ec2.amazonaws.com"
      },
      "Effect": "Allow",
      "Sid": ""
    }
  ]
}
EOF
}

resource "aws_iam_instance_profile" "jenkins" {
  name  = "jenkins"
  role = "${aws_iam_role.jenkins.name}"
}

resource "aws_iam_role_policy_attachment" "jenkins-s3" {
    role       = "${aws_iam_role.jenkins.name}"
    policy_arn = "${aws_iam_policy.jenkins-s3.arn}"
}
