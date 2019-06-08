### Create directory

```
mkdir ./conf
```

### Create appropriate hazelcast-client.xml File and copy to configs directory

### Execute the jar file

#### Get All Keys
```
java -jar cacheutil.jar --xml-file=hazelcast-cl-custome.xml --cache-name=my-test --key=any --action=getAll
```

#### Get by Key
```
java -jar cacheutil.jar --xml-file=hazelcast-cl-custom.xml --cache-name=my-test --key=KEY-HERE --action=get
```

#### Remove the Key
```
java -jar cacheutil.jar --xml-file=hazelcast-cl-custom.xml --cache-name=my-test --key=KEY-HERE --action=remove
```