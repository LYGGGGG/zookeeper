# 1、安装linux系统

配置网络，主机ip：192.168.42.100

子网掩码：255.255.255.0

网关：192.168.42.2

DNS：8.8.8.8

# 2、安装jdk

①创建文件夹/opt/zookeeper

②传输jdk安装包：jdk-8u202-linux-x64.tar.gz

③解压jdk安装包

```
cd /opt/zookeeper
tar -xvf jdk-8u202-linux-x64.tar.gz
```

④配置环境变量

Linux环境变量配置都在：/etc/profile文件中

```
vi /etc/profile
```

在文件添加

```
export JAVA_HOME=/opt/zookeeper/jdk1.8.0_202
export PATH=$JAVA_HOME/bin:$PATH
export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
```

重新载入配置文件

```
source /etc/profile
```

测试

```
java -version

测试结果
/**
java version "1.8.0_202"
Java(TM) SE Runtime Environment (build 1.8.0_202-b08)
Java HotSpot(TM) 64-Bit Server VM (build 25.202-b08, mixed mode)
**/
```

# 3、安装zookeeper

①传输zookeeper到/opt/zookeeper文件

②解压

```
tar -zxvf apache-ZooKeeper-3.5.6-bin.tar.gz 
```

③配置zoo.cfg

复制zoo_sample.cfg一份，并改名为zoo.cfg

④创建zooKeeper存储目录
```
mkdir  zkdata
```

⑤修改zoo.cfg

```
vi /opt/zooKeeper/apache-zooKeeper-3.5.6-bin/conf/zoo.cfg
```

⑥修改存储目录

```
dataDir = /opt/zookeeper/zkdata
```

⑦启动

```
./zkServer.sh start
./zkCli.sh
```

# 4.关闭防火墙

```
systemctl stop firewalld
systemctl disable firewalld
```

