provider "aws" {
  region = "us-east-1"
}

variable "amis" {
    type = "map"
    default = {}
}

# TODO: import stubbornjava-webapp

resource "aws_volume_attachment" "ebs_jenkins" {
  device_name = "/dev/sdh"
  volume_id   = "${aws_ebs_volume.jenkins.id}"
  instance_id = "${aws_instance.ci.id}"

  skip_destroy = true
}

resource "aws_ebs_volume" "jenkins" {
  availability_zone = "us-east-1a"
  size              = 100
  type              = "gp2"

  tags {
      Name = "jenkins"
      Path = "/var/lib/jenkins"
  }
}

resource "aws_instance" "ci" {
  count                       = 1
  ami                         = "${var.amis["amazon-linux-2017-09.1"]}"
  disable_api_termination     = true
  iam_instance_profile        = "jenkins"
  instance_type               = "t2.medium"
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
    volume_size               = 10
    delete_on_termination     = true
  }

  user_data = <<USER_DATA
#cloud-config
bootcmd:
 - [ cloud-init-per, once, mymkfs, mkfs.ext4, /dev/xvdh ]
 - [ cloud-init-per, once, createjenkinsdir, mkdir, /var/lib/jenkins ]

mounts:
 - [ /dev/xvdh, /var/lib/jenkins, "ext4", "defaults,noatime", "0", "0" ]
USER_DATA
}
