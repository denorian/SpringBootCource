#!/usr/bin/env bash
mvn clean package

echo 'Copy files ... '

scp -i ~/.ssh/brovkotechFrankfurt.pem \
    target/sweater-1.0-SNAPSHOT.jar \
    ec2-user@54.93.250.231:/home/ec2-user/sweater/

echo 'Restart server...'

ssh -T -i ~/.ssh/brovkotechFrankfurt.pem  ec2-user@54.93.250.231 << EOF
cd sweater/
pgrep java | xargs kill -9
nohup java -jar sweater-1.0-SNAPSHOT.jar > log.txt &

EOF

echo 'Bye'