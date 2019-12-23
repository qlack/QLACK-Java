# QLACK Fuse - Search
 
 This module provides connection and communication with an elastic search database. 

 You can use this module in order to index documents in the elastic search database and execute search queries. 
 In order to do so, you have to create indices, put records in the database and syntax the queries. 
 When creating an index, you have to define the mapping between the object and the database fieds by either defining the mapping in a json file or using the ES annotation `@Field`.
 Also, in order to persist an object in the database, you have to create a repository that extends ElasticsearchRepository<ObjectClass, IdType> and define this repository in the `@EnableElasticsearchRepositories` Spring annotation. 
 Qlack provides a variety of search queries than can be used to query documents. 
 Moreover, elasticsearch repositories can be enhanced with findBy{Attribute} methods to implement wanted search functionality.

## Integration

### Run elastic search:
Run the following cmd command to start ElasticSearch v6.4.3:
`docker run --name=A_NAME_OF_YOUR_FLAVOR -p 9400:9200  -p 9401:9300 -e "http.host=0.0.0.0" -e "transport.host=0.0.0.0"  -e "xpack.security.enabled=false" -d docker.elastic.co/elasticsearch/elasticsearch:6.4.3`

### Add qlack-fuse-search to your pom.xml:
```xml
  <dependency>
	   <groupId>com.eurodyn.qlack.fuse</groupId>
	   <artifactId>qlack-fuse-search</artifactId>
	   <version>${qlack.version}</version>
  </dependency>
```

### Add the required properties in the application.properties file:
```properties
################################################################################
# Elasticsearch configuration
################################################################################
## Qlack uses 2 different Elasticsearch clients:

# RestHighLevelClient ES client (A comma-separated list of ES hosts in the form of protocol1:host1:port1,protocol2:host2:port2,etc.)
qlack.fuse.search.es_hosts=http:localhost:9400

# Repo ES client (org.elasticsearch.client.Client)
qlack.fuse.search.host.name=localhost
qlack.fuse.search.host.port=9401

#`docker-cluster` is the docker default cluster.name, it will be different in any other Elasticsearch environment.
qlack.fuse.search.cluster.name=docker-cluster

qlack.fuse.search.es_username=
qlack.fuse.search.es_password=


spring.main.allow-bean-definition-overriding=true
```

### Add the packages in the Spring boot application main class declaration:

```java
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
// ..

@SpringBootApplication
@EnableAsync
@EnableJpaRepositories({
  "com.eurodyn.qlack.fuse.search",
  // ..

})
@EnableElasticsearchRepositories({
  "domain.appName.repository.es" 
})
@EnableCaching
@ComponentScan({
  "com.eurodyn.qlack.fuse.search",
  //..
})
```
### Example

#### Define the mapping in a JSON file: 
```json
{
   "animals":{ //This is the type of the index
      "dynamic": false,
      "properties":{
         "id":{
            "type":"text",
            "fields":{
               "keyword":{
                  "type":"keyword"
               }
            }
         },
         "name":{
            "type":"text",
            "fields":{
               "keyword":{
                  "type":"keyword"
               }
            }
         },
         "type":{
            "type":"text",
            "fields":{
               "keyword":{
                  "type":"keyword"
               }
            }
         },
         "age":{
            "type":"long"
         }
      }
   }
}
```

#### Define the mapping in the object ('@Field' annotations are needed only if the above step is missed):
```java

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import static org.springframework.data.elasticsearch.annotations.FieldType.Text;
//..

@Document(indexName = "animal", type = "animals")
public class Animal {

    @Field(type = Text, index = true)
    private String id;

    // Searchable with the french analyzer and Retrievable
    @Field(type = Text, index = true, searchAnalyzer="french", analyzer = "french")
    private String name;

    // Retrievable but not searchable
    @Field(type = Text, index = false)
    private String type;

    // Searchable and Retrievable 
    @Field(type = Text, index = true)
    private int age;

    //...
}
```

#### Create the ES repository for the object:

```java
package domain.appName.repository.es;

//...

public interface ApplicationAnimalsRepository extends ElasticsearchRepository<Animal, String> {

    // custom query declaration for the field 
    // int Age  of Animal class
    //no further implementation needed
    List<Animal> findByAge(int age);

    //...
}
```

#### Create the index for the object (using the JSON file):
```java
import com.eurodyn.qlack.fuse.search.request.CreateIndexRequest;
import com.eurodyn.qlack.fuse.search.service.AdminService;
//..

    private AdminService adminService;

    @Autowired
    public AdminServiceTest(AdminService adminService) {
        this.adminService = adminService;
    }
    
    public void createIndexFromJsonMapping() {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest();
        createIndexRequest.setName("animal");
        createIndexRequest.setType("animals");

        Path resourceDirectory = Paths.get("src/main/resources/animal.json");

        try {
          String mapping = new String(Files.readAllBytes(resourceDirectory), "UTF-8");
          createIndexRequest.setIndexMapping(mapping);
        } catch (IOException e) {
          e.printStackTrace();
        }
        
        adminService.createIndex(createIndexRequest):
    }
    //..
}
```

#### Create the index for the object (using the object annotations):
```java
import com.eurodyn.qlack.fuse.search.request.CreateIndexRequest;
import com.eurodyn.qlack.fuse.search.service.AdminService;
//..

    private AdminService adminService;

    @Autowired
    public AdminServiceTest(AdminService adminService) {
        this.adminService = adminService;
    }
    
    public void createIndexFromAnnotatedClass() {
        adminService.createIndex(Animal.class):
    }
    //..
}
```

#### Delete index (using the indexing name): 
 ```java
import com.eurodyn.qlack.fuse.search.request.CreateIndexRequest;
import com.eurodyn.qlack.fuse.search.service.AdminService;
//..
 
    private AdminService adminService;
 
    @Autowired
    public AdminServiceTest(AdminService adminService) {
        this.adminService = adminService;
    }
     
    public void deleteIndexByName(){
        adminService.deleteIndex("animals"):
    }
    //..
 }
```

#### Delete index (using the object): 
 ```java
import com.eurodyn.qlack.fuse.search.request.CreateIndexRequest;
import com.eurodyn.qlack.fuse.search.service.AdminService;
 //..
 
    private AdminService adminService;
 
    @Autowired
    public AdminServiceTest(AdminService adminService) {
        this.adminService = adminService;
    }

    public void deleteIndexByClass(){
        adminService.deleteIndex(Animal.class);
    }

    //..
 }
```

#### Index document (using the ES repository method):
 ```java
import com.eurodyn.qlack.fuse.search.request.CreateIndexRequest;
import com.eurodyn.qlack.fuse.search.service.AdminService;
 //..
 
    private ApplicationAnimalsRepository applicationAnimalsRepository;
 
    @Autowired
    public AdminServiceTest(ApplicationAnimalsRepository applicationAnimalsRepository) {
        this.applicationAnimalsRepository = applicationAnimalsRepository;
    }

    public void indexDocuemntUsingRepository(){
        List<Animal> animals = getAllAnimals();
        applicationAnimalsRepository.saveAll(animals);
    }

    //..
 }
 ```

#### Index document (using the Qlack service):
  ```java
import com.eurodyn.qlack.fuse.search.request.CreateIndexRequest;
import com.eurodyn.qlack.fuse.search.service.IndexingService;
//..
  
    private IndexingService indexingService;
  
    @Autowired
    public IndexingServiceTest(IndexingService indexingService;) {
        this.indexingService = indexingService;
    }
 
    public void indexDocuemntUsingService(){
        Animal animal = new Animal("some_id", "Rex", "Dog", "5");
        IndexingDTO indexingDTO = new IndexingDTO();
        indexingDTO.setSourceObject(animal);
        indexingDTO.setIndex("animal");
        indexingDTO.setType("animals");
        indexingDTO.setId(animal.getId());

        indexingService.indexDocument(indexingDTO);
    }

    //..
}
  ```

#### Search (using the ES repository method)
```java
import domain.appName.repository.es.ApplicationAnimalRepository;
//**

    private ApplicationAnimalsRepository applicationAnimalRepository;

    public void searchByAge() {
      List<Animal> animals = applicationAnimalRepository.findByAge(5);
    }

    //..
}
```
  
#### Search (using the Qlack service):

```java
import com.eurodyn.qlack.fuse.search.dto.SearchResultDTO;
import com.eurodyn.qlack.fuse.search.dto.queries.QueryRange;
import com.eurodyn.qlack.fuse.search.dto.queries.QuerySort;
import com.eurodyn.qlack.fuse.search.service.SearchService;
//**

    private SearchService searchService;

    public void searchQueryRange() {
      System.out.println("******************");
      System.out.println("Testing query range");

      QueryRange queryRange = new QueryRange() {};
      queryRange.setTerm("age", 28,30);
      queryRange.setIndex("animal");
      
      QuerySort querySort = new QuerySort();
      querySort.setSort("age", "asc");
      queryRange.setQuerySort(querySort);

      SearchResultDTO searchResultDTO = searchService.search(queryRange);
      System.out.println(searchResultDTO.getHits());
    }

    //..
}
```
