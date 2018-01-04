provider "aws" {
  region = "us-east-1"
}

variable "amis" {
    type = "map"
    default = {}
}

# TODO: import stubbornjava-webapp

resource "aws_instance" "ci" {
  count                       = 1
  ami                         = "${var.amis["amazon-linux-2017-09"]}"
  disable_api_termination     = true
  instance_type               = "t2.micro"
  monitoring                  = false
  subnet_id                   = "${element(data.aws_subnet_ids.public.ids, count.index)}"
  key_name                    = "stubbornjava"
  vpc_security_group_ids      = ["sg-e10c3a94", "sg-1a39ad66"]
  associate_public_ip_address = true

  tags {
    Name = "Jenkins"
    Role = "ci"
  }

  root_block_device {
    volume_type               = "gp2"
    volume_size               = 20
    delete_on_termination     = true
  }
}
