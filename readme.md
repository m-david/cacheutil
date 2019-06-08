### Create directory

```
mkdir ./conf
```

### Create appropriate hazelcast-client.xml File and copy to configs directory

### Execute the jar file

#### Get All Keys
##### Note: You will get serialization error if cached objects are POJOs
```
java -jar cacheutil.jar --xml-file=hazelcast-cl-custome.xml --cache-name=my-test --action=getAll
```

#### Get by Key
```
java -jar cacheutil.jar --xml-file=hazelcast-cl-custom.xml --cache-name=my-test --key=KEY-HERE --action=get
```

#### Remove the Key
```
java -jar cacheutil.jar --xml-file=hazelcast-cl-custom.xml --cache-name=my-test --key=KEY-HERE --action=remove
```

#### Put the Key
```
java -jar cacheutil.jar --xml-file=hazelcast-cl-custom.xml --cache-name=my-test-cache --key=KEY-HERE --action=put --value="My Value Here But Only String allowed"
```

#### Create the cache
```
java -jar cacheutil.jar --xml-file=hazelcast-cl-custom.xml --cache-name=my-test-cache-001 --action=create
```

#### Destroy the cache - Drop cache
```
java -jar cacheutil.jar --xml-file=hazelcast-cl-custom.xml --cache-name=my-test-cache-001 --action=destroy
```