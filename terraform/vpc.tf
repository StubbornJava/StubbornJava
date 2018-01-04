data "aws_vpc" "selected" {
  state = "available"
}

data "aws_subnet_ids" "public" {
  vpc_id = "${data.aws_vpc.selected.id}"
  tags {
    Public = "Yes"
  }
}
