#!/bin/bash
# Much less than ideal deploy script but it works enough for now.
# Ideally it would keep versioned jars and symlink the current version for easy rollbacks
# Another option is to use AWS code build / deploy eventually
cd .. && gradle clean shadowJar &&
ansible --vault-password-file ansible/.vault_pw.txt -b -i ansible/inventories/production stubbornjava -m copy -a "src=stubbornjava-webapp/build/libs/stubbornjava-all.jar dest=/apps/stubbornjava owner=stubbornjava group=stubbornjava" &&
ansible --vault-password-file ansible/.vault_pw.txt -i ansible/inventories/production stubbornjava -m command -a "supervisorctl restart stubbornjava"
