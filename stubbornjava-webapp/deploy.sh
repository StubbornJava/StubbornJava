#!/bin/bash
# Much less than ideal deploy script but it works enough for now.
# Ideally it would keep versioned jars and symlink the current version for easy rollbacks
# Another option is to use AWS code build / deploy eventually
cd .. && gradle clean shadowJar &&
ansible -i ../stubbornjava/ansible/hosts stubbornjava -m copy -a "src=stubbornjava-webapp/build/libs/stubbornjava-all.jar dest=~/" &&
ansible -i ../stubbornjava/ansible/hosts stubbornjava -m command -a "supervisorctl restart all"
