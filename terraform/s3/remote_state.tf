terraform {
  backend "s3" {
    bucket = "stubbornjava-terraform"
    key    = "s3/terraform.tfstate"
    region = "us-east-1"
  }
}
