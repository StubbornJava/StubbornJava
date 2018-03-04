terraform {
  backend "s3" {
    bucket = "stubbornjava-terraform"
    key    = "iam/terraform.tfstate"
    region = "us-east-1"
  }
}
