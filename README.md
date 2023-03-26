## Setting up MySQL Container
```bash
docker run -d -p 3306:3306 --name mysql-docker-container -e MYSQL_ROOT_PASSWORD=spring -e MYSQL_DATABASE=spring -e MYSQL_USER=spring -e MYSQL_PASSWORD=spring mysql/mysql-server:latest
```

## Spring Data

**ORM & data manipulation options**:

- Repositories
	- Repository
		- makes possible to create custom repositories
	- JpaRepository
	- PagingAndSortingRepository
	- CrudRepository
- Additional options
	- QuerydslPredicateExecutor
	- QueryByExampleExecutor
- Querydsl
	- used in repositories
- Named queries
	- referenced from entities in repositories
- Native queries
	- referenced from entities in repositories
- Criteria API
- Helpers
	- JdbcTemplate
	- NamedParameterJdbcTemplate
	- Sort.by
	- PageRequest.of
- Auditing
	- @CreatedDate
	- @CreatedBy
	- @LastModifiedDate
	- @LastModifiedBy

## Spring querydsl

|Keyword            |Sample method name                                         |Sample query																		|
|-------------------|-----------------------------------------------------------|-----------------------------------------------------------------------------------|
|Distinc            |findDistinctByLastnameAndFirstname                         |`select distinc ... where x.lastname = ?1 and x.firstname = ?2`		|
|And                |findByLastnameAndFirstname                                 |`... where x.lastname = ?1 and x.firstname = ?2`							|
|Or                 |findByLastnameOrFirstname                                  |`... wgere x.lastname - ?1 or x.firstname = ?2`							|
|Is, Equals         |findByFirstname, findByFirstnameIs, findBy, FirstnameEquals|`... where x.firstname = ?1`													|
|Between            |findByStartDateBetween                                     |`... where x.startDate between ?1 and ?2`									|
|LessThan           |findByAgeLessThan                                          |`... where x.age < ?1`															|
|LessThanEqual      |findByAgeLessThanEqual                                     |`... where x.age <= ?1`														|
|GreaterThan        |findByAgeGreaterThan                                       |`... where x.age > ?1`															|
|GreaterThanEqual   |findByAgeGreaterThanEqual                                  |`... where x.age >= ?1`														|
|After              |findByStartDateAfter                                       |`... whee x.startDate > ?1`													|
|Before             |findByStartDateBefore                                      |`... whee x.startDate < ?1`													|
|Is Null            |findByAge(Is)Null                                          |`... where x.age is null`														|
|Is Not null        |findByAge(Is)NotNull                                       |`... where x.age is not null`												|
|Like               |findByFirstnameLike                                        |`... where x.firstname like ?1`												|
|Not Like           |findByFirstnameNotLike                                     |`... where x.firstname not like ?1`											|
|Starting With      |findByFirstnameStartingWith                                |`... where x.firstname like ?1 (parameter bound with appended %)`	|
|Ending With        |findByFirstnameEndingWith                                  |`... where x.firstname like ?1 (parameter bound with prepended %)`	|
|Containing         |findByFirstnameContaing                                    |`... where x.firstname like ?1 (parameter bound wrapped in %)`		|
|Order by           |findByAgeOrderByLastnameDesc                               |`... where x.age = ?1 order by x.lastname desc`							|
|Not                |findByLastnameNot                                          |`... where x.lastname <> ?1`													|
|In                 |findByAgeIn(Collection<Age> age)                           |`... where x.age in ?1`														|
|Not In             |findByAgeNotIn(Collection<Age> age)                        |`... where x.age not in ?1`													|
|True               |findByActiveTrue                                           |`... where x.active = true`													|
|False              |findByActiveFalse                                          |`... where x.active = false`													|
|Ignore case        |findByFirstnameIgnoreCase                                  |`... where UPPER(x.firstname) = UPPER(?1)`								|

## Additional resources

- [Spring Data JPA](https://github.com/matebence/jpa)

## Spring REST

Two options:
- **@Controller**
	- @ResponseBody
	- @RequestMapping(value = "/get/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
- **@RestController**
	- @GetMapping(value = "/{id}")

Additional performance improvement with HAZELCAST (caching)
- @Cacheable(value = "account-cache", key = "#id")
- @CacheEvict(value = "account-cache", key = "#id")

API docs via
- springdoc-openapi
