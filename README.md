# scala-play-benchmark

### local
```
$ sbt clean
$ sbt api/dist
$ scp -i ~/ssh/hoge.pem api/target/universal/api-0.1.0-SNAPSHOT.zip ec2-user@XX.XX.YY.ZZ:~
```
### ec2
```
$ sudo yum install -y java-1.8.0-openjdk-devel.x86_64
$ unzip api-0.1.0-SNAPSHOT.zip
$ sudo api-0.1.0-SNAPSHOT/bin/api -Dhttp.port=80 -Dplay.http.secret.key=XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
```
