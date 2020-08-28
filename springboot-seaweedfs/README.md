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
      - 8080:8080
      - 18080:18080
    command: 'volume -max=5 -mserver="master:9333" -port=8080 -publicUrl="127.0.0.1:8888"'
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




# seaweedfs 文件分块上传

## CURL demo

* 分割测试文件为小文件，此处分割pdf为4m一个的小文件，生成xaa，xab，xac

```text
split -b 4194304 test.pdf
```

* 上传xaa

```text
curl -X POST -F "file=@xaa" http://localhost:9333/submit?pretty=yes

{
  "eTag": "0d40388f",
  "fid": "5,ff1faf68bc",
  "fileName": "xaa",
  "fileUrl": "192.168.31.38:8888/5,ff1faf68bc",
  "size": 4194304
}% 
```

* 上传xab

```text
curl -X POST -F "file=@xab" http://localhost:9333/submit\?pretty\=yes
{
  "eTag": "2e3c8ab1",
  "fid": "5,01027f72e307",
  "fileName": "xab",
  "fileUrl": "192.168.31.38:8888/5,01027f72e307",
  "size": 4194304
}
```

* 上传xac

```text
curl -X POST -F "file=@xac" http://localhost:9333/submit?pretty=yes

{
  "eTag": "dfbd8ba3",
  "fid": "7,0103e1a47667",
  "fileName": "xac",
  "fileUrl": "192.168.31.38:8888/7,0103e1a47667",
  "size": 785120
}
```

* 申请一个合并文件的id

```text
curl "http://localhost:9333/dir/assign?pretty=yes"

{
  "fid": "1,0104d6e92a03",
  "url": "172.22.0.3:8080",
  "publicUrl": "192.168.31.38:8888",
  "count": 1
}
```

* 本地创建一个名称为manifest.json的文件，组装合并文件的信息，size为文件的总大小，offset为偏移量，依次从0开始

```text
manifest.json

{
    "name":"test.pdf",
    "mime":"application/pdf",
    "size":9173728,
    "chunks":[
        {
            "fid":"5,ff1faf68bc",
            "offset":0,
            "size":4194304
        },
        {
            "fid":"5,01027f72e307",
            "offset":4194304,
            "size":4194304
        },
        {
            "fid":"7,0103e1a47667",
            "offset":8388608,
            "size":785120
        }
    ]
}

```

* 上传合并的json 文件，url为上面申请合并文件的url，端口一般都映射到我们的主机，或者使用publicUrl

```text
curl -v -F "file=@manifest.json" "http://127.0.0.1:8080/1,0104d6e92a03?cm=true&pretty=yes"

*   Trying 127.0.0.1...
* TCP_NODELAY set
* Connected to 127.0.0.1 (127.0.0.1) port 8080 (#0)
> POST /1,0104d6e92a03?cm=true&pretty=yes HTTP/1.1
> Host: 127.0.0.1:8080
> User-Agent: curl/7.54.0
> Accept: */*
> Content-Length: 635
> Expect: 100-continue
> Content-Type: multipart/form-data; boundary=------------------------1a763c42ac2be18e
>
> < HTTP/1.1 100 Continue
> < HTTP/1.1 201 Created
> < Content-Type: application/json
> < Etag: "c9700b75"
> < Date: Thu, 27 Aug 2020 10:08:44 GMT
> < Content-Length: 66
> < 
> {
> "name": "manifest.json",
> "size": 430,
> "eTag": "c9700b75"
* Connection #0 to host 127.0.0.1 left intact
}

```

* 通过合并的文件下载来测试文件是否正常

```text
curl -v "http://127.0.0.1:8080/1,0104d6e92a03" -o out.pdf


  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
                                 Dload  Upload   Total   Spent    Left  Speed
  0     0    0     0    0     0      0      0 --:--:-- --:--:-- --:--:--     0*   Trying 127.0.0.1...
* TCP_NODELAY set
* Connected to 127.0.0.1 (127.0.0.1) port 8080 (#0)
> GET /1,0104d6e92a03 HTTP/1.1
> Host: 127.0.0.1:8080
> User-Agent: curl/7.54.0
> Accept: */*
>
> < HTTP/1.1 200 OK
> < Accept-Ranges: bytes
> < Content-Disposition: inline; filename="test.pdf"
> < Content-Length: 9173728
> < Content-Type: application/pdf
> < Etag: "c9700b75"
> < Last-Modified: Thu, 27 Aug 2020 10:08:44 GMT
> < X-File-Store: chunked
> < Date: Thu, 27 Aug 2020 10:10:09 GMT
> < 
> { [3825 bytes data]
> 100 8958k  100 8958k    0     0  29.8M      0 --:--:-- --:--:-- --:--:-- 29.9M

* Connection #0 to host 127.0.0.1 left intact
```

## SpringBoot 分块文件上传

### 参考 

https://github.com/chrislusf/seaweedfs/wiki/Large-File-Handling

### 分块上传

test.pdf 为要分割的文件， xaa,xab,xac为拆分的文件，使用时注意修改自己本机电脑文件的路径

* 下面先开始上传文件，本示例使用junit测试演示

  ```java
  @Test
      public void blockUpload() throws IOException {
          File file = new File("/Users/cxt/Downloads/test2/test.pdf");
          MultipartFile allFile = FileUtils.file2MultipartFile(file);
          String md5 = Md5Utils.getMD5(file);
          long allFileSize = allFile.getSize();
  
          File f1 = new File("/Users/cxt/Downloads/test2/xaa");
          MultipartFile multipartFile = FileUtils.file2MultipartFile(f1);
          MultipartFileParam param = MultipartFileParam.builder()
                  .chunk(0)
                  .chunkMd5(Md5Utils.getMD5(f1))
                  .chunks(3)
                  .contentType(multipartFile.getContentType())
                  .chunkSize(4194304L)
                  .file(multipartFile)
                  .md5(md5)
                  .name(f1.getName())
                  .size(allFileSize)
                  .build();
  
  
          File f2 = new File("/Users/cxt/Downloads/test2/xab");
          MultipartFile multipartFile2 = FileUtils.file2MultipartFile(f2);
          MultipartFileParam param1 = MultipartFileParam.builder()
                  .chunk(1)
                  .chunkMd5(Md5Utils.getMD5(f2))
                  .chunks(3)
                  .contentType(multipartFile2.getContentType())
                  .chunkSize(4194304L)
                  .file(multipartFile2)
                  .md5(md5)
                  .name(f2.getName())
                  .size(allFileSize)
                  .build();
  
  
          File f3 = new File("/Users/cxt/Downloads/test2/xac");
          MultipartFile multipartFile3 = FileUtils.file2MultipartFile(f3);
          MultipartFileParam param2 = MultipartFileParam.builder()
                  .chunk(2)
                  .chunkMd5(Md5Utils.getMD5(f3))
                  .chunks(3)
                  .contentType(multipartFile3.getContentType())
                  .chunkSize(4194304L)
                  .file(multipartFile3)
                  .md5(Md5Utils.getMD5(file))
                  .name(f3.getName())
                  .size(allFile.getSize())
                  .build();
  
          Object o = fileController.blockUpload(param);
          System.out.println("o "+o);
          Object o1 = fileController.blockUpload(param1);
          System.out.println("o1 "+o1);
          Object o2 = fileController.blockUpload(param2);
          System.out.println("o2 "+o2);
      }
  ```

  代码中主要演示了拼接调用blockUpload上传文件的参数，依次上传三个文件

  blockUpload的代码如下，使用时，前段封装对应的参数列表即可，使用了一个自定义的封装对象来接受这些参数，前端在调用时使用form表单请求即可提交,submit 为我们创建的上传seaweedfs的url，直接使用seaweedfs client 上传的文件不能合并成功，这个问题暂时没有在自习研究，怀疑版本最新的就是如此操作的，而client集成的还没有更新。

  submit = http://127.0.0.1:9333/submit?pretty=yes

  ```java
      @PostMapping("/blockUpload")
      public Object blockUpload(MultipartFileParam param) throws IOException {
  
          /**
           * 对分块文件上传
           * seaweedfs返回的文件信息封装对象保存到redis
           * key= 总文件的md5，value为分块文件的信息
           * 返回
           */
  
          String key = FILE_UPLOAD_CHUNK_KEY + param.getMd5();
          // 上传文件
          //设置请求头
          HttpHeaders headers = new HttpHeaders();
          MediaType type = MediaType.parseMediaType("multipart/form-data");
          headers.setContentType(type);
          Resource resource = new InputStreamResource(param.getFile().getInputStream());
          //设置请求体，注意是LinkedMultiValueMap
          MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
          form.add("file", resource);
          form.add("filename",param.getName());
          //用HttpEntity封装整个请求报文
          HttpEntity<MultiValueMap<String, Object>> files = new HttpEntity<>(form, headers);
          FileStatus fileStatus = restTemplate.postForObject(submit, files, FileStatus.class);
          // 封装seaweedfs 需要的分块文件信息
          ChunkInfo chunkInfo = new ChunkInfo();
          chunkInfo.setFid(fileStatus.getFid());
          // offset 偏移量设置为当前是第几块乘每块的文件大小
          chunkInfo.setOffset(param.getChunkSize() * param.getChunk());
          // size 为返回的当前分块文件的大小
          chunkInfo.setSize(fileStatus.getSize());
          // 缓存在redis
          JedisUtils.hset(key, param.getChunkMd5(), objectMapper.writeValueAsString(chunkInfo), 0);
          return objectMapper.writeValueAsString(chunkInfo);
      }
  ```

### 合并文件

* 调用发起请求

```java
    @Test
    public void mergeFiles() throws Exception {
        File file = new File("/Users/cxt/Downloads/test2/test.pdf");
        MultipartFile allFile = FileUtils.file2MultipartFile(file);

        MultipartFileParam param = MultipartFileParam.builder()
                //"application/pdf"
                .contentType(allFile.getContentType())
                .md5(Md5Utils.getMD5(file))
                .name(allFile.getOriginalFilename())
                .size(allFile.getSize())
                .build();
        Object o = fileController.mergeFiles(param);
        System.out.println("合并" + o);
    }
```

* 合并文件

  ```java
  @PostMapping("/mergeFiles")
      public Object mergeFiles(MultipartFileParam param) throws Exception {
          /**
           * 根据文件md5获取redis中所有缓存的块文件信息
           * 封装seaweedfs需要的合并文件数据样式
           * 调用携带？cm=true的接口进行文件上传
           * 返回上传之后的文件信息
           */
          String key = FILE_UPLOAD_CHUNK_KEY + param.getMd5();
          // 根据key 获取到当前文件所有的子文件列表 
          Map<String, String> stringStringMap = JedisUtils.hgetAll(key, 0);
          Collection<String> values = stringStringMap.values();
          List<ChunkInfo> list = new LinkedList<>();
          for (String value : values) {
              ChunkInfo chunkInfo = objectMapper.readValue(value, ChunkInfo.class);
              list.add(chunkInfo);
          }
          // 根据所有字文件的大小计算合并的总文件大小，其实这个要是前端有传完全不需要计算
          long sum = list.stream().mapToLong(ChunkInfo::getSize).sum();
          // 封装seaweedfs 需要的合并文件json文本
          ChunkManifest chunkManiFest = new ChunkManifest();
          chunkManiFest.setChunks(list);
          chunkManiFest.setMime(param.getContentType());
          chunkManiFest.setName(param.getName());
          chunkManiFest.setSize(sum);
          // 转换json进行保存
          String text = objectMapper.writeValueAsString(chunkManiFest);
          log.info("mergeFiles 合并文件json [{}]",text);
          FileStatus fileStatus = restTemplate.getForEntity(fidUrl, FileStatus.class).getBody();
          log.info("mergeFiles getFileDetail fileStatus [{}] [{}]", publicUrl, fileStatus);
          InputStream inputStream = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
          seaweedFileService.uploadFileWithCm(publicUrl, fileStatus.getFid(), param.getName()+".json", inputStream, param.getContentType());
          log.info("mergeFiles 文件合并成功 fid [{}]",fileStatus.getFid());
          return objectMapper.writeValueAsString(fileStatus);
      }
  ```

### 坑1

子文件的偏移量计算问题

* 尽量使用平均分的 文件，这样偏移容易计算
* 不平均分的文件，注意偏移量一定要修改对
* 偏移量是从0开始的

### 坑2 

分块文件上传的url

* 使用的第三方即成的client 直接上传文件失败，改为http://127.0.0.1:9333/submit?pretty=yes 即可了

### 坑3 

合并文件上传的url

* 使用8080 端口 的url，配置没有配置好，此处自定义配置了http://127.0.0.1:8080

* 如果seaweedfs 配置好直接可以使用返回的url

  

### 坑4

* 合并文件的文件名后缀需要带`.json`

### 坑5

主要还是合并之后的json文本，合并的文件对了，基本就成功了一大半了，正确的文件可以参考上面的合并文本


