terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.10.0"
    }
  }
  backend "s3" {
    bucket = "summit-terraform-state"
    key    = "summit-cas-mock-server/base.json"
  }
}

locals {
  product_name = "summit-cas-mock-server"
  component_name = "cas-mock-server"
}

provider "aws" {
  default_tags {
    tags = {
      Owner = local.product_name
    }
  }
}

module public_ecr_repository {
  source = "git::ssh://git@code.vt.edu/crest/summit-terraform.git//public_ecr_repository"
  component_name = local.component_name
}

module ci_pipeline_user {
  source = "git::ssh://git@code.vt.edu/crest/summit-terraform.git//ci_pipeline_user_public_ecr_only"
  product_name = local.product_name
  ecr_repository_arn = module.public_ecr_repository.arn
}
