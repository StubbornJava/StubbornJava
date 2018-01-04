terraform {
  backend "s3" {
    bucket = "stubbornjava-terraform"
    key    = "prod/terraform.tfstate"
    region = "us-east-1"
  }
}
