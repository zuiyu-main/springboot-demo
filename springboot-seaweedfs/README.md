# springboot + seaweedfs
## 安装seaweedfs 
* docker-compose.yml
```yaml
version: '2'

services:
  master:
    image: chrislusf/seaweedfs # use a remote image
    # build: . # build our container from the local Dockerfile
    ports:
      - 9333:9333
    command: "master"
    networks:
      default:
        aliases:
          - seaweed_master
  volume:
    image: chrislusf/seaweedfs # use a remote image
    # build: . # build our container from the local Dockerfile
    ports:
      - 8083:8080
      - 18080:18080
    command: 'volume -max=5 -mserver="master:9333" -port=8080 -publicUrl="192.168.1.91:8888"'
    depends_on:
      - master
    networks:
      default:
        aliases:
          - seaweed_volume
  filer:
    image: chrislusf/seaweedfs # use a remote image
    # build: . # build our container from the local Dockerfile
    ports:
      - 8888:8888
      - 18888:18888
    command: 'filer -master="master:9333"'
    depends_on:
      - master
      - volume
    networks:
      default:
        aliases:
          - seaweed_filer
```

## 大文件拆分拼接json文件保存（修改?cm=true）
## 获取文件FileHandleStatus content-type和fileName报错，改成传参数