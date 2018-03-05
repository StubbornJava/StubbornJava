variable "region" {}

provider "aws" {
    region = "${var.region}"
}

resource "aws_s3_bucket" "stubbornjava-terraform" {
    bucket = "stubbornjava-terraform"
    acl    = "private"

    versioning {
        enabled = true
    }
}

resource "aws_s3_bucket" "stubbornjava-jenkins" {
    bucket = "stubbornjava-jenkins"
    acl    = "private"

    versioning {
        enabled = true
    }
}
