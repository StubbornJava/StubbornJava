variable "region" {}

provider "aws" {
    region = "${var.region}"
}

resource "aws_iam_user" "jenkins" {
  name = "jenkins"
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

resource "aws_iam_user_policy_attachment" "jenkins-s3" {
    user       = "${aws_iam_user.jenkins.name}"
    policy_arn = "${aws_iam_policy.jenkins-s3.arn}"
}
